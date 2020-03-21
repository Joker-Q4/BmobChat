package cn.edu.sau.joker;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import static org.litepal.LitePalBase.TAG;

/**
 * 好友列表的适配器
 */
public class LListAdapter extends RecyclerView.Adapter<LListAdapter.ViewHolder>{
    private List<LList> llist;
    //绑定传入的数据源
    LListAdapter(List<LList> list) {
        this.llist = list;
    }
    //实现onCreateViewHolder方法，返回给recyclerView使用
    @Override
    public LListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Log.e(TAG, "onClick: "+position);
                LList list = llist.get(position);
                //点击事件，跳转聊天界面
                Intent intent = new Intent(v.getContext(),Main.class);
                intent.putExtra("friend", list.getFriends());
                v.getContext().startActivity(intent);
            }
        });
        return holder;
    }
    //实现onBindViewHolder方法，设置子Item上各个实例
    @Override
    public void onBindViewHolder(LListAdapter.ViewHolder holder, final int position) {
        LList lis = llist.get(position);
        holder.mText.setText(lis.getFriends());
    }
    //返回子项个数
    @Override
    public int getItemCount() {
        return llist.size();
    }
    //定义一个内部类ViewHolder，继承自RecyclerView.ViewHolder，用来缓存子项的各个实例，提高效率
    class ViewHolder extends RecyclerView.ViewHolder {
        View listview;
        TextView mText;
        ViewHolder(View itemView) {
            super(itemView);
            listview = itemView;
            mText = itemView.findViewById(R.id.list);
        }
    }
}