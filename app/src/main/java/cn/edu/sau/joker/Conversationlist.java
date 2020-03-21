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
import org.litepal.LitePal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.litepal.LitePalBase.TAG;

/**
 * 会话列表的逻辑处理
 */
public class Conversationlist extends Fragment implements View.OnClickListener{

    public  List<ConList> lList = new ArrayList<ConList>();
    public  List<ConList> llList = new ArrayList<ConList>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation,container,false);

        RecyclerView recyclerView = view.findViewById(R.id.conlist_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(layoutManager);
        ConListAdapter adapter = new ConListAdapter(lList);
        recyclerView.setAdapter(adapter);
        read__db();      //读取数据库会话列表
        delcom();        //去重
        copyAndshow();   //赋值并显示
        delDB();         //删库
        recreateDB();    //写库
        return view;
    }
    //读取数据库中的好友列表
    public void read__db() {
        llList.clear();
        llList = LitePal.findAll(ConList.class);
    }
    //去重
    public void delcom() {
        Log.e(TAG, "delcom: 删除之前"+llList.size());
        int i,j;
        Collections.reverse(llList);
        for (i=0; i<llList.size(); i++){
            for (j=i+1; j<llList.size(); j++){
                if(llList.get(i).getName().equals(llList.get(j).getName())){
                    //有重复，需删除
                    llList.remove(llList.get(j));
                    Log.e(TAG, "delcom: 删除之后"+llList.size());
                }
            }
        }
    }
    //赋值并显示
    private void copyAndshow() {
        lList.clear();
        ConList lLis;
        for (int i=0 ; i<llList.size(); i++){
            //     Toast.makeText(getActivity(),"长度："+llList.size(),Toast.LENGTH_SHORT).show();
            //加个异常
            try{
                lLis = new ConList(llList.get(i).getName(),llList.get(i).getMessage());
                ad(lLis);
            }catch (Exception e) {
                Log.e(TAG, "read__db: 空指针异常");
            }
        }
    }
    //删库
    private void delDB() {
        LitePal.deleteAll("ConList");
        //   LitePal.deleteDatabase("ConList");
    }
    //写库
    private void recreateDB() {
        ConList lLis;
        Collections.reverse(llList);
        //建库
        LitePal.getDatabase();
        //导数据
        for (int i=0 ; i<llList.size(); i++){
            //加个异常
            try{
                lLis = new ConList(llList.get(i).getName(),llList.get(i).getMessage());
                lLis.save();
            }catch (Exception e) {
                Log.e(TAG, "read__db: 空指针异常");
            }
        }
    }
    //活动重启，重新调用方法
    @Override
    public void onStart() {
        super.onStart();
        read__db();
        delcom();
        copyAndshow();
        delDB();
        recreateDB();
    }
    //添加到列表
    public void ad(ConList lis){
        lList.add(lis);
    }
    //点击事件处理，该界面无点击
    @Override
    public void onClick(View v) {
    }
}
