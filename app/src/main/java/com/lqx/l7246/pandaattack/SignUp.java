package com.lqx.l7246.pandaattack;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by l7246 on 2017/11/1.
 */

public class SignUp extends Activity {
    public int width,height;
    private EditText signup_tel_edit;
    private EditText signup_pwd_edit;
    private EditText signup_repwd_edit;
    private EditText signup_name_edit;
    private ImageView signup_signupbt;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        setContentView(R.layout.signup);

        signup_tel_edit=(EditText)findViewById(R.id.signup_tel_edit);
        signup_pwd_edit=(EditText)findViewById(R.id.signup_pwd_edit);
        signup_repwd_edit=(EditText)findViewById(R.id.signup_repwd_edit);
        signup_name_edit=(EditText)findViewById(R.id.signup_name_edit);
        signup_signupbt=(ImageView) findViewById(R.id.signup_signupbt);

        setMargins(signup_tel_edit,width/10,height/10,width/10,height/10*7);
        setMargins(signup_pwd_edit,width/10,height/10*2,width/10,height/10*5);
        setMargins(signup_repwd_edit,width/10,height/10*3,width/10,height/10*4);
        setMargins(signup_name_edit,width/10,height/10*4,width/10,height/10*3);
        setMargins(signup_signupbt,width/6,height/10*6,width/6,height/10);

        signup_signupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = signup_tel_edit.getText().toString();
                String pwd = signup_pwd_edit.getText().toString();
                String repwd = signup_repwd_edit.getText().toString();
                String name = signup_name_edit.getText().toString();
                Pattern p = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,1,2,5-9])|(17[3|7]))\\d{8}$");
                Matcher m = p.matcher(tel);
                if(tel.equals("")){
                    Toast.makeText(SignUp.this, "请输入您的手机号！", Toast.LENGTH_SHORT).show();
                }
                else if(!m.matches())
                {
                    Toast.makeText(SignUp.this, "请输入正确的手机号！", Toast.LENGTH_SHORT).show();
                }
                else if(pwd.equals(""))
                {
                    Toast.makeText(SignUp.this, "请输入您的密码！", Toast.LENGTH_SHORT).show();
                }
                else if(repwd.equals(""))
                {
                    Toast.makeText(SignUp.this, "请再次输入您的密码！", Toast.LENGTH_SHORT).show();
                }
                else if(!pwd.equals(repwd))
                {
                    Toast.makeText(SignUp.this, "两次输入的密码不符，请核实！", Toast.LENGTH_SHORT).show();
                }
                else if(name.equals(""))
                {
                    Toast.makeText(SignUp.this, "不打算给自己起一个响亮的名字？", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        String tname= URLEncoder.encode(name,"UTF-8");
                        String jsonData=HttpUtils.doGet("http://106.14.151.117:9090/GameJSON/WebContent/Register.jsp?tel="+tel+"&loginkey="+pwd+"&name="+tname);
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String result = jsonObject.getString("result");
                        if(result.equals("fail"))
                        {
                            Toast.makeText(SignUp.this, "网络连接失败！请检查你的网络！", Toast.LENGTH_SHORT).show();
                        }
                        else if(result.equals("repeat"))
                        {
                            Toast.makeText(SignUp.this, "此手机号已注册", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            STATIC.mytel=tel;
                            STATIC.mypassword=pwd;
                            Toast.makeText(SignUp.this, "注册成功，正在跳往登录页面...", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(SignUp.this,SignIn.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
