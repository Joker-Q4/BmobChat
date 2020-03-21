package cn.edu.sau.joker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.List;
import static org.litepal.LitePalBase.TAG;

/**
 * 好友列表界面逻辑
 */
public class Friends extends Fragment implements View.OnClickListener{

    private List<LList> lList = new ArrayList<>();
    private LListAdapter adapter;
    private EditText eText;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend,container,false);
        //绑定
        RecyclerView recyclerView = view.findViewById(R.id.list_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        //添加适配器
        adapter = new LListAdapter(lList);
        recyclerView.setAdapter(adapter);
        eText = view.findViewById(R.id.et_find_name);
        Button button = view.findViewById(R.id.btn_search);
        eText.setOnClickListener(this);
        button.setOnClickListener(this);
        //默认添加账号，默认添加自己为好友
        if(MyUser.getUni()!=null){
        LList li = new LList(MyUser.getUni());
            lList.add(li);
        }
        //读取数据库
        read__db();
        return view;
    }
    //点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                String string = eText.getText().toString();
                string = string.replaceAll(" ", "");    //去除所有空格
                LList li = new LList(string);
                if(string.length()>0 && string.length()<=6) {             //判断长度是否符合要求
                    lList.add(li);
                    li.setFriends(string);
                    li.save();                                            //保存到数据库
                    adapter.notifyItemInserted(lList.size() - 1);
                    eText.setText("");                                    //清空输入框
                }else
                    Toast.makeText(getActivity(),"输入长度不合理！限定1-6位",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    //读取数据库中的好友列表
    private void read__db() {
        List<LList> lLi = LitePal.findAll(LList.class);
        for (int i=0 ; i<lLi.size(); i++){
            //加个异常
            try{
                LList lLis = new LList(lLi.get(i).getFriends());
                lList.add(lLis);     //读取并添加
            }catch (Exception e) {
                Log.e(TAG, "read__db: 空指针异常");
            }
        }
        // 当有新消息时，刷新ListView中的显示
        adapter.notifyItemInserted(lList.size() - 1);
    }
}
