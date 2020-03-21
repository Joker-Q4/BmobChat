package cn.edu.sau.joker;

/**
 * 全局的静态变量存储
 */
public class Tips {
    static int MainActivity_success= 0;    //记录Toast次数，目的是让同一个Toast只显示一次
    static int MainActivity_fail= 0;
    static int current = 0;                //记录当前界面
    static String Receiver = "";           //记录当前聊天对象的name
}