package cn.edu.sau.joker;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

/**
 * 会话列表的适配器
 */
public class ConListAdapter extends RecyclerView.Adapter<ConListAdapter.ViewHolder>{

    private List<ConList> llist;
    //绑定传入的数据源
    ConListAdapter(List<ConList> list) {
        this.llist = list;
    }
    //实现onCreateViewHolder方法，返回给recyclerView使用
    @Override
    public ConListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.conv_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.listview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                ConList list = llist.get(position);
                //点击事件，跳转聊天
                Intent intent = new Intent(v.getContext(),Main.class);
                intent.putExtra("friend", list.getName());
                v.getContext().startActivity(intent);
                }
        });
        return holder;
    }

    //实现onBindViewHolder方法，设置子Item上各个实例
    @Override
    public void onBindViewHolder(ConListAdapter.ViewHolder holder, final int position) {
        ConList lis = llist.get(position);
        holder.nameText.setText(lis.getName());
        holder.mesText.setText(lis.getMessage());
    }

    //返回子项个数
    @Override
    public int getItemCount() {
        return llist.size();
    }

    //定义一个内部类ViewHolder，继承自RecyclerView.ViewHolder，用来缓存子项的各个实例，提高效率
    class ViewHolder extends RecyclerView.ViewHolder {
        View listview;
        TextView nameText;
        TextView mesText;
        ViewHolder(View itemView) {
            super(itemView);
            listview = itemView;
            nameText = itemView.findViewById(R.id.name);
            mesText = itemView.findViewById(R.id.mes);
        }
    }
}