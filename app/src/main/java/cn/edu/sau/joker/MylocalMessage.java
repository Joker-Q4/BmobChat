package cn.edu.sau.joker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

/**
 * 自定义广播，接收消息并在通知栏显示
 */
public class MylocalMessage extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 收到广播时,发送一个通知
        String content = intent.getStringExtra("msg");
        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.drawable.icon)
                .setContentTitle("您收到一条消息")
                .setContentText(content)
                .build();
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notify.sound = uri;
        long[] vibrates = { 0, 200, 200, 200 };
        notify.vibrate = vibrates;
        manager.notify(1, notify);
    }
}
