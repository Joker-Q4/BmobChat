package cn.edu.sau.joker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.exception.BmobException;

/**
 * 主活动，包含三个Fragment
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static long firstTime;
    public static final String TAG_EXIT = "exit";
    private Fragment first,second,third;
    private TextView textView;
    private Integer retry = 0;
    public static boolean isConnect = true;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            hideAll();
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    textView.setText("会话");
                    ft.show(first);
                    Tips.current = 1;
                    ft.commit();
                    return true;
                case R.id.navigation_home:
                    textView.setText("联系人");
                    ft.show(second);
                    Tips.current = 2;
                    ft.commit();
                    return true;
                case R.id.navigation_setting:
                    textView.setText("设置");
                    ft.show(third);
                    Tips.current = 3;
                    ft.commit();
                    return true;
                default:
                    return false;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        init();
        initFragment();
        // 注册订阅者
        EventBus.getDefault().register(this);
        connect();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    //隐藏所有Fragment
    private void hideAll(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.hide(first)
                .hide(second)
                .hide(third)
                .commit();
    }
    //初始化布局
    private void init() {
        textView = findViewById(R.id.common_actionbar);
        textView.setOnClickListener(this);
        textView.setText("会话");
    }
    //初始化Fragment
    private void initFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        first = new Conversationlist();
        second = new Friends();
        third = new My();
        ft.add(R.id.fragment,first);
        ft.add(R.id.fragment,second);
        ft.add(R.id.fragment,third);
        hideAll();
        //判断当前显示界面
        if(Tips.current == 0 || Tips.current == 1){
            ft.show(first);
            textView.setText("会话");
        }else if(Tips.current == 2){
            ft.show(second);
            textView.setText("联系人");
        }else {
            ft.show(third);
            textView.setText("设置");
        }
        ft.commit();
    }
    //连接服务器
    private void connect() {
        //检查网络连接
        if(!NetWork.isNetConnection(this)){
            Toast.makeText(this,"无网络连接！",Toast.LENGTH_SHORT).show();
            isConnect = false;
        }else{
            //每秒检查是否连接
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!isConnected())
                        isConnect = false;
                    else
                        isConnect = true;
                }
            }, 300,1000);
            try {
                if (!isConnect) {
                    if (Tips.MainActivity_fail == 0) {
                        Toast.makeText(MainActivity.this, "服务器连接失败！正在重试...", Toast.LENGTH_SHORT).show();
                        Tips.MainActivity_fail = 1;
                        Tips.MainActivity_success = 0;
                    }
                    BmobIM.connect(MyUser.getUni(), new ConnectListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                isConnect = true;
                                retry = 0;
                                if (Tips.MainActivity_success == 0) {
                                    Toast.makeText(MainActivity.this, "服务器连接成功！", Toast.LENGTH_SHORT).show();
                                    Tips.MainActivity_success = 1;
                                    Tips.MainActivity_fail = 0;
                                }
                            }else {
                                if (retry == 0)
                                    if (Tips.MainActivity_fail == 0) {
                                        Toast.makeText(MainActivity.this, "服务器连接失败！正在重试...", Toast.LENGTH_SHORT).show();
                                        Tips.MainActivity_fail = 1;
                                        Tips.MainActivity_success = 0;
                                    } else
                                        retry++;
                                connect();
                            }
                        }
                    });
                }
            }catch (Exception e){}
        }
    }
    //判断是否连接
    private boolean isConnected(){
        boolean IsCon = false;
        try {
            String mg = BmobIM.getInstance().getCurrentStatus().getMsg();
            if(mg.equals("connected"))
                IsCon = true;
        }catch (Exception e){
            IsCon = false;
        }
        return IsCon;
    }
    //点击事件处理
    @Override
    public void onClick(View v) {

    }
    //发送广播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(cn.edu.sau.joker.MessageEvent event) {
        Intent intent = new Intent("cn.edu.sau.action.MESSAGE");
        intent.putExtra("msg",event.message);
        sendBroadcast(intent);
    }
    //销毁活动
    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }
    //连续按两次返回键就退出
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (firstTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            //     Toa("再按一次退出程序");
        }
        firstTime = System.currentTimeMillis();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        init();
        initFragment();
        connect();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
