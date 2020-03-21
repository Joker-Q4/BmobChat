package cn.edu.sau.joker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;
import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 聊天界面
 */
public class Main extends AppCompatActivity {
    public static List<Msg> msgList = new ArrayList<Msg>();
    public static List<Msg> msgL = new ArrayList<Msg>();
    public static EditText inputText;
    private Button send;
    public static RecyclerView msgRecyclerView;
    public static MsgAdapter adapter;
    private BmobIMConversation mBmobIMConversation;
    private static String friend;
    private static String username = MyUser.getUni();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //初始化
        init();
        createConversation();
    }
    private void createConversation() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查网络连接
                if(!NetWork.isNetConnection(Main.this))
                    Toast.makeText(Main.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                else{
                    if(!MainActivity.isConnect)
                        Toast.makeText(Main.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();
                    //网络正常
                    String content = inputText.getText().toString();
                    if (!"".equals(content)) {
                        final Msg ms = new Msg(content, Msg.TYPE_SENT);
                        //连接成功，发送消息
                        BmobIMUserInfo info =new BmobIMUserInfo();
                        info.setAvatar("填写接收者的头像");
                        info.setUserId(friend);
                        info.setName("填写接收者的名字");
                        ms.setReceiver(friend);
                        ms.setSender(MyUser.getUni());
                        Tips.Receiver = friend;
                        //         BmobIMConversation conversationEntrance =
                        try{
                            BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                                @Override
                                public void done(BmobIMConversation c, BmobException e) {
                                    if(e==null){
                                        //在此跳转到聊天页面或者直接转化
                                        mBmobIMConversation=BmobIMConversation.obtain(BmobIMClient.getInstance(),c);
                                        BmobIMTextMessage msg =new BmobIMTextMessage();
                                        msg.setContent(ms.getContent());
                                        //发送消息
                                        mBmobIMConversation.sendMessage(msg, new MessageSendListener() {
                                            @Override
                                            public void done(BmobIMMessage msg, BmobException e) {
                                                if (e != null) {
                                                    Toast.makeText(Main.this, "发送失败", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    //添加消息
                                                    add(ms);
                                                }
                                            }
                                        });
                                    }else{
                                        Toast.makeText(Main.this, "开启会话出错", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }catch (Exception e){}
                    }
                }
            }
        });
    }
    //初始化界面
    private void init() {
        try {
            Intent intent = getIntent();
            friend = intent.getStringExtra("friend");
        }catch (Exception e) {}
        TextView textView = findViewById(R.id.common_actionbar);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //设置标题栏
        if(!friend.equals(""))
            textView.setText("会话<"+friend+">");
        else
            textView.setText("会话");

        inputText = findViewById(R.id.input_text);
        send = findViewById(R.id.send);
        msgRecyclerView = findViewById(R.id.msg_recycle_view);
        //添加适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        //读取消息从本地数据库
        read__db();
    }
    //读取数据库
    private void read__db() {
        msgList.clear();
        msgL = LitePal.where("Sender = ? and Receiver = ?;",username,friend).find(Msg.class);
        int count = msgL.size();
        //判断消息长度，最多从数据库读取50条消息
        if(count > 0 && count <=50 ){
            sel_50(0);
        }else if(count > 50){
            sel_50(count - 50);
        }
        // 当有新消息时，刷新ListView中的显示
        adapter.notifyItemInserted(msgList.size() - 1);
        // 将ListView定位到最后一行
        msgRecyclerView.scrollToPosition(msgList.size() - 1);
    }
    //查询截取50条数据
    private void sel_50(int i){
        for (; i < msgL.size(); i++){
            //加个异常
            try{
                Msg msgLi = new Msg(msgL.get(i).getContent(),msgL.get(i).getType());
                msgList.add(msgLi);
            }catch (Exception e) {}
        }
    }
    //添加消息
    public static void add(Msg msg){
        if(msg.getType() == 1){
            msgList.add(msg);
            // 当有新消息时，刷新ListView中的显示
            adapter.notifyItemInserted(msgList.size() - 1);
            // 将ListView定位到最后一行
            msgRecyclerView.scrollToPosition(msgList.size() - 1);
            // 清空输入框中的内容
            inputText.setText("");
        }else if(msg.getReceiver().equals(friend)) {
            msgList.add(msg);
            adapter.notifyItemInserted(msgList.size() - 1);
            msgRecyclerView.scrollToPosition(msgList.size() - 1);
        }
        //保存数据库
        msg.save();
        int len = msg.getContent().length();
        ConList lis;
        if(len > 15){
            String show = msg.getContent().substring(0,12);
            if(msg.getType() == 1)
                lis = new ConList(friend,"我："+show+"......");
            else
                lis = new ConList(friend,"对方："+show+"......");
        }else {
            if(msg.getType() == 1)
                lis = new ConList(friend,"我："+msg.getContent());
            else
                lis = new ConList(friend,"对方："+msg.getContent());
        }
        lis.save();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
        createConversation();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tips.Receiver = "";
    }
}