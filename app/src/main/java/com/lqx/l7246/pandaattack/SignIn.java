package com.lqx.l7246.pandaattack;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by l7246 on 2017/10/31.
 */

public class SignIn extends Activity {
    public int width,height;
    private EditText signin_tel_edit,signin_pwd_edit;
    private CheckBox signin_checklg,signin_autolg;
    private ImageView signin_bt;
    private boolean isChecklg=false,isAutolg=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        setContentView(R.layout.signin);



        signin_tel_edit=(EditText) findViewById(R.id.signin_tel_edit);
        setMargins(signin_tel_edit,(int)(width*0.2),(int)(height*0.15),(int)(width*0.2),(int)(height*0.726));

        signin_pwd_edit=(EditText)findViewById(R.id.signin_pwd_edit);
        setMargins(signin_pwd_edit,(int)(width*0.2),(int)(height*0.32),(int)(width*0.2),(int)(height*0.554));

        signin_checklg=(CheckBox)findViewById(R.id.signin_checklg);
        setMargins(signin_checklg,(int)(width*0.31),(int)(height*0.56),(int)(width*0.5),(int)(height*0.3));

        signin_autolg=(CheckBox)findViewById(R.id.signin_autolg);
        setMargins(signin_autolg,(int)(width*0.529),(int)(height*0.56),(int)(width*0.1),(int)(height*0.3));

        signin_bt=(ImageView)findViewById(R.id.signin_signinbt);
        setMargins(signin_bt,(int)(width*0.229),(int)(height*0.72),(int)(width*0.229),(int)(height*0.08));

        if(!STATIC.mytel.equals(""))
        {
            signin_tel_edit.setText(STATIC.mytel);
            signin_checklg.setChecked(true);
        }
        if(!STATIC.mypassword.equals(""))
        {
            signin_pwd_edit.setText(STATIC.mypassword);
            signin_checklg.setChecked(true);
        }
        signin_checklg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isChecklg)
                {
                    isChecklg=true;
                }
                else
                {
                    isChecklg=false;
                    if(isAutolg)
                    {
                        isAutolg=false;
                        signin_autolg.setChecked(false);
                    }
                }
            }
        });
        signin_autolg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isAutolg)
                {
                    isAutolg=true;
                    if(!isChecklg)
                    {
                        isChecklg=true;
                        signin_checklg.setChecked(true);
                    }
                }
                else
                {
                    isAutolg=false;
                }
            }
        });

        signin_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel=signin_tel_edit.getText().toString();
                String pwd=signin_pwd_edit.getText().toString();
                if(tel.equals(""))
                {
                    Toast.makeText(SignIn.this, "请输入您的账号！", Toast.LENGTH_SHORT).show();
                }
                else if(pwd.equals(""))
                {
                    Toast.makeText(SignIn.this, "请输入您的密码！", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    try {
                        String jsonData=HttpUtils.doGet("http://106.14.151.117:9090/GameJSON/WebContent/getUserDetails.jsp?tel="+tel+"&loginkey="+pwd);
                        JSONObject jsonObject = new JSONObject(jsonData);
                        String result = jsonObject.getString("result");
                        if(result.equals("fail"))
                        {
                            Toast.makeText(SignIn.this, "账号密码有误！", Toast.LENGTH_SHORT).show();
                        }
                        else if(result.equals("ConnectionError"))
                        {
                            Toast.makeText(SignIn.this, "网络连接失败！请检查你的网络！", Toast.LENGTH_SHORT).show();
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
                            if(isChecklg||isAutolg)
                            {
                                String myjson="{" +
                                        "\"isCheck\":\""+isChecklg+"\","+
                                        "\"isAuto\":\""+isAutolg+"\","+
                                        "\"tel\":\""+tel+"\","+
                                        "\"pwd\":\""+pwd+"\"}";
                                File file = new File("data/data/com.lqx.l7246.pandaattack/userinfo.txt");
                                try {
                                    FileOutputStream fos = new FileOutputStream(file);
                                    fos.write((myjson).getBytes());
                                    fos.close();
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            switch (charactertype)
                            {
                                case 0:
                                    Intent intent1=new Intent(SignIn.this,ChooseCharacter.class);
                                    startActivity(intent1);
                                    break;
                                case 1:
                                case 2:
                                case 3:
                                    Intent intent2=new Intent(SignIn.this,MapChoosePOI.class);
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
