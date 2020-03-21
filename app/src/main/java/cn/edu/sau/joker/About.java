package cn.edu.sau.joker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * 关于界面
 */
public class About extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //绑定布局，设置标题栏
        TextView about = findViewById(R.id.common_actionbar);
        about.setOnClickListener(this);
        about.setText("关于");
    }
    @Override
    public void onClick(View v) {
    }
}
