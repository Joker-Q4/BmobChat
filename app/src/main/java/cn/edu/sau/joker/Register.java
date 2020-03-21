package cn.edu.sau.joker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册界面逻辑处理
 */
public class Register extends AppCompatActivity implements View.OnClickListener{
    private ImageView yanzhengmatu;
    private EditText accountEdit;
    private EditText passwordEdit;
    private EditText yanzhengma;
    private int a;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化界面
        initView();
    }
    //初始化
    private void initView() {
        Button register = findViewById(R.id.register);   //注册按钮
        TextView login = findViewById(R.id.login);       //登陆按钮
        yanzhengmatu = findViewById(R.id.yanzhengmatu);  //验证码
        accountEdit = findViewById(R.id.account1);
        passwordEdit = findViewById(R.id.password1);
        yanzhengma = findViewById(R.id.yanzhengma);
        register.setOnClickListener(this);               //监听事件
        login.setOnClickListener(this);
        yanzhengmatu.setOnClickListener(this);
        accountEdit.setOnClickListener(this);
        passwordEdit.setOnClickListener(this);
        yanzhengma.setOnClickListener(this);
    }
    //点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                //跳转到登录界面
                Intent intent = new Intent(this,login.class);
                startActivity(intent);
                finish();
                break;
            case R.id.yanzhengmatu:
                //获取随机验证码
                yanzhengmatu.setImageDrawable(getResources().getDrawable(R.drawable.yanzhengma2));
                a = (int)((Math.random()*9+1)*100000);
                yanzhengma.setText(Integer.toString(a));
                break;
            case R.id.register:
                //注册按钮触发事件
                String account = accountEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                String yanzheng = yanzhengma.getText().toString().trim();
                if(account.equals("")||password.equals("")||yanzheng.equals("")) {
                    if (account.equals(""))
                        Toast.makeText(this, "请填写用户名！", Toast.LENGTH_SHORT).show();
                    else if(password.equals(""))
                        Toast.makeText(this, "请填写用户密码！", Toast.LENGTH_SHORT).show();
                    else if(yanzheng.equals(""))
                        Toast.makeText(this, "请点击获取验证码！", Toast.LENGTH_SHORT).show();
                } else if(a != Integer.valueOf(yanzheng)){
                    Toast.makeText(this, "验证码输入错误！", Toast.LENGTH_SHORT).show();
                } else if (account.length() > 6 || account.length() < 2) {
                    Toast.makeText(this, "请输入2-6位用户名！", Toast.LENGTH_SHORT).show();
                    accountEdit.setText("");
                } else if (password.length() > 8 || password.length() < 4) {
                    Toast.makeText(this, "请输入4-8位密码！", Toast.LENGTH_SHORT).show();
                    passwordEdit.setText("");
                } else {
                    //存储
                    BmobUser us = new BmobUser();
                    us.setUsername(account);
                    us.setPassword(password);
                    //检查网络连接
                    if(!NetWork.isNetConnection(this)){
                        Toast.makeText(this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        us.signUp(new SaveListener<BmobUser>() {
                            @Override
                            public void done(BmobUser re, BmobException e) {
                                if(e==null){
                                    Toast.makeText(Register.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Register.this, "用户名已存在，请直接登录！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        //跳转到登录界面
                        Intent intent2 = new Intent(Register.this,login.class);
                        intent2.putExtra("account", account);
                        intent2.putExtra("password", password);
                        startActivity(intent2);
                        finish();
                    }
                }
                break;
        }
    }
}
