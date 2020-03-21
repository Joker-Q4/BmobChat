package cn.edu.sau.joker;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.json.JSONException;
import org.json.JSONObject;
import cn.bmob.push.PushConstants;

/**
 * 系统推送
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    private static final String TAG = "MyPushMessageReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            // 收到广播时,发送一个通知
            String jsonStr = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            String content = null;
            try {
                // 处理JSON
                JSONObject jsonObject = new JSONObject(jsonStr);
                content = jsonObject.getString("alert");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
            Notification notify = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("系统推送")
                    .setContentText(content)
                    .build();
            manager.notify(1, notify);
        }
    }
}