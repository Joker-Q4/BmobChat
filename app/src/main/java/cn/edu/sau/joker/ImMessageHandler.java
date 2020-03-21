package cn.edu.sau.joker;

import org.greenrobot.eventbus.EventBus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.BmobIMMessageHandler;

/**
 * 接收消息
 */
public class ImMessageHandler extends BmobIMMessageHandler{
    /**
     * 接收在线消息
     * @param messageEvent
     */
    @Override
    public void onMessageReceive(MessageEvent messageEvent) {
        super.onMessageReceive(messageEvent);
        final Msg msg = new Msg(messageEvent.getMessage().getContent(), Msg.TYPE_RECEIVED);
        msg.setSender(MyUser.getUni());
        msg.setReceiver(messageEvent.getFromUserInfo().getUserId());
        int len = msg.getContent().length();
        // 过滤字符串
        String str="=[cn.bmob.newim.event.Message Event@";
        String str1="=[cn.bmob.newim.event.MessageEvent@";
        String str2 = msg.getContent().trim();
        int bl = str2.indexOf(str);
        int bo = str2.indexOf(str1);
        if(bl!=-1 || bo!=-1){
        }else{
            if(!msg.getReceiver().equals(Tips.Receiver))
                if(len > 10){
                    String show = msg.getContent().substring(0,8);
                    //使用EventBus发送通知，主Activity中处理事件
                    EventBus.getDefault().post(new cn.edu.sau.joker.MessageEvent(msg.getReceiver()+"："+show+"......"));
                }else {
                    EventBus.getDefault().post(new cn.edu.sau.joker.MessageEvent(msg.getReceiver()+"："+msg.getContent()));
                }
                //聊天界面添加消息
                Main.add(msg);
        }
    }

    /**
     * 接收离线消息，本程序不做处理
     * @param offlineMessageEvent
     */
    @Override
    public void onOfflineReceive(OfflineMessageEvent offlineMessageEvent) {
        super.onOfflineReceive(offlineMessageEvent);
    }
}
