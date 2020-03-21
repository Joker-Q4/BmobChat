package cn.edu.sau.joker;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.bmob.newim.BmobIM;

/**
 * 个人资料界面逻辑处理
 */
public class My extends Fragment implements View.OnClickListener{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my,container,false);
        //绑定
        TextView username = view.findViewById(R.id.nah_name);
        TextView list_username = view.findViewById(R.id.list_name);
        Button about = view.findViewById(R.id.list_about);
        Button quit = view.findViewById(R.id.list_quit);
        Button exit = view.findViewById(R.id.list_exit);
        username.setOnClickListener(this);
        list_username.setOnClickListener(this);
        about.setOnClickListener(this);
        quit.setOnClickListener(this);
        exit.setOnClickListener(this);
        //设置用户名
        username.setText(MyUser.getUni());
        list_username.setText("用户名："+MyUser.getUni());
        return view;
    }
    //点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list_about:
                //跳转关于界面
                Intent intent0 = new Intent(getActivity(),About.class);
                startActivity(intent0);
                break;
            case R.id.list_quit:
                //退出登录
                showDialogquit();
                break;
            case R.id.list_exit:
                //退出程序
                showDialogexit();
                break;
            default:break;
        }
    }
    //退出登录对话框
    private void showDialogquit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("是否退出登录？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击确认做的操作
                BmobIM.getInstance().disConnect();
                Intent intent = new Intent(getActivity(),login.class);
                startActivity(intent);
                getActivity().finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消做的操作
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    //退出程序对话框
    private void showDialogexit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("确认退出Joker？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击确认做的操作
                BmobIM.getInstance().disConnect();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                intent.putExtra(MainActivity.TAG_EXIT, true);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消做的操作
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
