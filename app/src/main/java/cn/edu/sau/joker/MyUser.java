package cn.edu.sau.joker;

import cn.bmob.v3.BmobUser;

/**
 * 用户类
 */
public class MyUser extends BmobUser{
    private String username;
    private String password;
    private static String uni;

    public static String getUni() {
        return uni;
    }
    public static void setUni(String uni) {
        MyUser.uni = uni;
    }
    MyUser() {
        super();
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    @Override
    public void setPassword(String password) {
        this.password = password;
    }
}