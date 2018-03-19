package com.lqx.l7246.pandaattack;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lqx.l7246.pandaattack.Music.MusicBackground;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LoginActivity extends Activity {
    private long exitTime=0;
    private RelativeLayout signin_signup_panel;
    private ImageView signin_bt;
    private ImageView signup_bt;
    public int width,height;
    public static Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        WindowManager wm = this.getWindowManager();
        this.width = wm.getDefaultDisplay().getWidth();
        this.height = wm.getDefaultDisplay().getHeight();

        setContentView(R.layout.login);

        signin_signup_panel=(RelativeLayout)findViewById(R.id.signin_signup_panel);
        signin_bt=(ImageView)findViewById(R.id.signin_bt);
        signup_bt=(ImageView)findViewById(R.id.signup_bt);

        int panelwidth=width/5*4;
        int panelheight=height;
        setMargins(signin_signup_panel,width/10,width/10,panelwidth,panelheight,width,height);
        setMargins(signin_bt,panelwidth/4,panelheight/10*4,panelwidth/2,panelheight/5,panelwidth,panelheight);
        setMargins(signup_bt,panelwidth/4,panelheight/10*6,panelwidth/2,panelheight/5,panelwidth,panelheight);

        intent = new Intent(LoginActivity.this,MusicBackground.class);
        startService(intent);

        signin_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File("data/data/com.lqx.l7246.pandaattack/userinfo.txt");
                //如果文件存在，则读取
                if(file.exists())
                {
                    try {
                        FileInputStream fin = new FileInputStream(file);
                        BufferedReader buffer = new BufferedReader(new InputStreamReader(fin));
                        String myjson = buffer.readLine();
                        JSONObject jsonobj=new JSONObject(myjson);
                        //boolean isCheck=jsonobj.getBoolean("isCheck");
                        boolean isAuto=jsonobj.getBoolean("isAuto");
                        String tel=jsonobj.getString("tel");
                        String pwd=jsonobj.getString("pwd");
                        STATIC.mytel=tel;
                        STATIC.mypassword=pwd;
                        if(isAuto)
                        {
                            try {
                                String jsonData=HttpUtils.doGet("http://106.14.151.117:9090/GameJSON/WebContent/getUserDetails.jsp?tel="+tel+"&loginkey="+pwd);
                                JSONObject jsonObject = new JSONObject(jsonData);
                                String result = jsonObject.getString("result");
                                if(result.equals("fail"))
                                {
                                    Toast.makeText(LoginActivity.this, "账号密码有误！", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(LoginActivity.this,SignIn.class);
                                    startActivity(intent);
                                }
                                else if(result.equals("ConnectionError"))
                                {
                                    Toast.makeText(LoginActivity.this, "网络连接失败！请检查你的网络！", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    String name = jsonObject.getString("name");
                                    int money = jsonObject.getInt("money");
                                    int hp = jsonObject.getInt("hp");
                                    int energy = jsonObject.getInt("energy");
                                    int atk = jsonObject.getInt("atk");
                                    int defence = jsonObject.getInt("defence");
                                    int exp = jsonObject.getInt("exp");
                                    int level = jsonObject.getInt("level");
                                    int hpplus = jsonObject.getInt("hpplus");
                                    int energyplus = jsonObject.getInt("energyplus");
                                    int charactertype = jsonObject.getInt("charactertype");
                                    int talent = jsonObject.getInt("talent");
                                    STATIC.myplayer=new Player(tel,name,money,hp,energy,atk,defence,exp,level,hpplus,energyplus,charactertype,talent);
                                    switch (charactertype)
                                    {
                                        case 0:
                                            Intent intent1=new Intent(LoginActivity.this,ChooseCharacter.class);
                                            startActivity(intent1);
                                            break;
                                        case 1:
                                        case 2:
                                        case 3:
                                            Intent intent2=new Intent(LoginActivity.this,MapChoosePOI.class);
                                            startActivity(intent2);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else
                        {
                            Intent intent=new Intent(LoginActivity.this,SignIn.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                }
                else
                {
                    Intent intent=new Intent(LoginActivity.this,SignIn.class);
                    startActivity(intent);
                }
            }
        });
        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

    }
    public void setMargins (View view, int x, int y, int vwidth, int vheight,int pwidth,int pheight) {
        int left,top,right,bottom;
        left=x;
        top=y;
        right=pwidth-x-vwidth;
        bottom=pheight-y-vheight;
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
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                        if ((System.currentTimeMillis() - exitTime) > 2000) {
                            Toast.makeText(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                            exitTime = System.currentTimeMillis();
                        } else {
                            //退出登录Logout
                            finish();
                            MyApplication.getInstance().exit();
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}
