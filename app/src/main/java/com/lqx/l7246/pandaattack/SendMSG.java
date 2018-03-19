package com.lqx.l7246.pandaattack;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by l7246 on 2017/12/3.
 */

public class SendMSG extends Activity {
    private EditText text;
    private Button bt;
    public int width,height;
    public boolean isbt=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        WindowManager wm = this.getWindowManager();
        this.width = wm.getDefaultDisplay().getWidth();
        this.height = wm.getDefaultDisplay().getHeight();

        setContentView(R.layout.sendmsg);

        text=(EditText)findViewById(R.id.sendmsg_text);
        bt=(Button)findViewById(R.id.sendmsg_bt);
        text.setHeight(height-100);
        text.getBackground().setAlpha(80);
        setMargins(text,0,0,0,100);
        bt.setHeight(100);
        setMargins(bt,0,height-100,0,0);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isbt)
                {
                    Toast.makeText(SendMSG.this, "你已经提交过反馈了", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(text.getText().toString().equals("")||text.getText().toString().equals("如果您有什么意见或者建议，请务必告知我们。谢谢！"))
                    {
                        Toast.makeText(SendMSG.this, "请认真填写反馈信息", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        if(isNetworkConnected(SendMSG.this))
                        {
                            String feedbacktext=text.getText().toString();
                            String strfeedback[]=feedbacktext.split("\n");
                            String feedback="";
                            for(int i=0;i<strfeedback.length;i++)
                            {
                                feedback+=strfeedback[i]+"。";
                            }
                            String str="{\"name\":\""+STATIC.myplayer.name+"\","
                                    +"\"tel\":\""+STATIC.myplayer.tel+"\","
                                    +"\"feedback\":\""+feedback+"\"}\r\n";
                            try {
                                str=URLEncoder.encode(str,"UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            String strurl=HttpUtils.doGet("http://106.14.151.117:9090/GameJSON/WebContent/feedback.jsp?feedback="+str);
                            if(!strurl.equals(""))
                            {
                                Toast.makeText(SendMSG.this, "感谢您的反馈", Toast.LENGTH_SHORT).show();
                                isbt=true;
                            }
                        }
                        else
                        {
                            Toast.makeText(SendMSG.this, "请检查你的网络", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
    }
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    public void setMargins (View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }
    @Override
    protected void onResume() {
        /**
         * 设置为横屏
         */
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
