package cn.edu.sau.joker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;
import static org.litepal.LitePalBase.TAG;
import cn.bmob.newim.BmobIM;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 登陆界面
 */
public class login extends Activity implements View.OnClickListener{
    private EditText accountEdit;
    private EditText passwordEdit;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private CheckBox rememberPass;
    MyUser user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //数据库初始化
        LitePal.initialize(this);
        LitePal.getDatabase();
        // 注册订阅者
        EventBus.getDefault().register(this);
        //初始化bmob
        Bmob.initialize(this, "d4dafd6854343ba7b42a4c347757c7e6");
        BmobIM.init(this);
        BmobIM.registerDefaultMessageHandler(new ImMessageHandler());
        //TODO 集成：1.4、初始化数据服务SDK、初始化设备信息并启动推送服务
        // 使用推送服务时的初始化操作
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {}});
        // 启动推送服务
        BmobPush.startWork(this);
        //使用SharedPreferences明文存储，不安全，需改进，可以参考MD5
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        //绑定按钮
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember);
        Button login = findViewById(R.id.login);
        TextView register = findViewById(R.id.zhuce);
        //监听事件
        register.setOnClickListener(this);
        login.setOnClickListener(this);
        //是否记住密码
        boolean isRemember = pref.getBoolean("remember",false);
        if(isRemember){
            String account = pref.getString("account","");
            String password = pref.getString("password","");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        try {
            Intent intent0 = getIntent();
            String acc = intent0.getStringExtra("account");
            String pass = intent0.getStringExtra("password");
            //判断是否记住
            if(isRemember) {
                String acc1 = accountEdit.getText().toString().trim();
                String pass1 = passwordEdit.getText().toString().trim();
                editor = pref.edit();
                if(rememberPass.isChecked()){
                    editor.putBoolean("remember",true);
                    editor.putString("account",acc1);
                    editor.putString("password",pass1);
                } else {
                    editor.clear();
                }
                editor.apply();
            }
            //判断传递值是否为空
            if(!(acc.equals("")&&pass.equals(""))){
                accountEdit.setText(acc);
                passwordEdit.setText(pass);
            }
        }catch (Exception e) {
            Log.i(TAG, "onCreate: "+e);
        }
    }
    //发送广播
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(cn.edu.sau.joker.MessageEvent event) {
        Intent intent = new Intent("cn.edu.sau.action.MESSAGE");
        intent.putExtra("msg",event.message);
        sendBroadcast(intent);
    }
    //点击事件处理
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            //登陆事件
            case R.id.login:       //点击登陆
                String account1 = accountEdit.getText().toString().trim();
                String password1 = passwordEdit.getText().toString().trim();
                BmobUser us = new BmobUser();
                us.setUsername(account1);
                us.setPassword(password1);
                user = new MyUser();
                user.setUsername(account1);
                user.setPassword(password1);
                MyUser.setUni(account1);
                //记住密码
                editor = pref.edit();
                if(rememberPass.isChecked()){
                    editor.putBoolean("remember",true);
                    editor.putString("account",account1);
                    editor.putString("password",password1);
                } else {
                    editor.clear();
                }
                editor.apply();
                //检查网络连接
                if(!NetWork.isNetConnection(this)){
                    Toast.makeText(this,"无网络连接！",Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    // 登录验证
                    us.login(new SaveListener<BmobUser>() {

                        @Override
                        public void done(BmobUser bmobUser, BmobException e) {
                            if(e==null){
                                Intent intent1 = new Intent(login.this,MainActivity.class);
                                startActivity(intent1);
                                finish();
                            }else{
                                Toast.makeText(login.this,"用户名或密码错误，请重新登陆！",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
                //点击注册按钮，跳转到注册界面
            case R.id.zhuce:
                Intent intent2 = new Intent(this,Register.class);
                startActivity(intent2);
                finish();
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
    }
}
