package com.lqx.l7246.pandaattack;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by l7246 on 2017/11/6.
 */

public class UserDetails extends Activity {
    public int width,height;
    private ProgressBar details_progressbar;
    private RelativeLayout details_progressbar_layout;
    private ImageView details_img,details_plushp,details_plusenergy,details_plusatk,details_plusdefence,details_hp_plus_img,details_energy_plus_img;
    private TextView details_level,details_name,details_tel,details_money,details_hp,details_energy,details_atk,details_defence,details_talent,details_tip,details_hp_plus_num,details_energy_plus_num;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        setContentView(R.layout.userdetails);

        details_progressbar_layout=(RelativeLayout)findViewById(R.id.details_progressbarlayout);
        details_level=(TextView)findViewById(R.id.details_level);
        details_progressbar=(ProgressBar)findViewById(R.id.details_progressbar);
        details_img=(ImageView)findViewById(R.id.details_img);
        details_plushp=(ImageView)findViewById(R.id.details_plushp);
        details_plusenergy=(ImageView)findViewById(R.id.details_plusenergy);
        details_plusatk=(ImageView)findViewById(R.id.details_plusatk);
        details_plusdefence=(ImageView)findViewById(R.id.details_plusdefence);
        details_name=(TextView)findViewById(R.id.details_name);
        details_tel=(TextView)findViewById(R.id.details_tel);
        details_money=(TextView)findViewById(R.id.details_money);
        details_hp=(TextView)findViewById(R.id.details_hp);
        details_energy=(TextView)findViewById(R.id.details_energy);
        details_atk=(TextView)findViewById(R.id.details_atk);
        details_defence=(TextView)findViewById(R.id.details_defence);
        details_talent=(TextView)findViewById(R.id.details_talent);
        details_tip=(TextView)findViewById(R.id.details_tip);
        details_hp_plus_img=(ImageView)findViewById(R.id.details_hp_plus_img);
        details_energy_plus_img=(ImageView)findViewById(R.id.details_energy_plus_img);
        details_hp_plus_num=(TextView)findViewById(R.id.details_hp_plus_num);
        details_energy_plus_num=(TextView)findViewById(R.id.details_energy_plus_num);

        details_level.setText(""+STATIC.myplayer.level);
        details_progressbar.setProgress(STATIC.myplayer.exp);
        details_img.setImageResource(STATIC.myplayer.mycharacter.getIMG());
        details_name.setText(STATIC.myplayer.name);
        details_tel.setText("tel:"+STATIC.myplayer.tel);
        details_money.setText("竹子:"+STATIC.myplayer.money);
        details_hp.setText("生命值:"+STATIC.myplayer.hp);
        details_energy.setText("能量值:"+STATIC.myplayer.energy);
        details_atk.setText("攻击力:"+STATIC.myplayer.atk);
        details_defence.setText("防御力:"+STATIC.myplayer.defence);
        details_talent.setText("天赋点:"+STATIC.myplayer.talent);
        details_hp_plus_num.setText(" "+STATIC.myplayer.hpplus);
        details_energy_plus_num.setText(" "+STATIC.myplayer.energyplus);
        details_tip.setText("tip:退出游戏前不要忘了点击主页的保存喔");

        float textsize=(float)(width*0.014);
        details_level.setTextSize(textsize);
        details_name.setTextSize(textsize);
        details_tel.setTextSize(textsize);
        details_money.setTextSize(textsize);
        details_hp.setTextSize(textsize);
        details_energy.setTextSize(textsize);
        details_atk.setTextSize(textsize);
        details_defence.setTextSize(textsize);
        details_talent.setTextSize(textsize);
        details_hp_plus_num.setTextSize(textsize);
        details_energy_plus_num.setTextSize(textsize);

        textsize=(float)(width*0.006);
        details_tip.setTextSize(textsize);

        setMargins(details_level,(int)(width*0.165),(int)(height*0.125),0,0);
        setMargins(details_progressbar_layout,(int)(width*0.25),(int)(height*0.125),(int)(width*0.185),(int)(height*0.835));
        setMargins(details_img,(int)(width*0.165),(int)(height*0.205),(int)(width*0.66),(int)(height*0.54));
        setMargins(details_name,(int)(width*0.37),(int)(height*0.205),0,0);
        setMargins(details_tel,(int)(width*0.37),(int)(height*0.31),0,0);
        setMargins(details_money,(int)(width*0.37),(int)(height*0.405),0,0);
        setMargins(details_hp,(int)(width*0.43),(int)(height*0.525),0,0);
        setMargins(details_energy,(int)(width*0.43),(int)(height*0.625),0,0);
        setMargins(details_atk,(int)(width*0.43),(int)(height*0.725),0,0);
        setMargins(details_defence,(int)(width*0.43),(int)(height*0.825),0,0);
        setMargins(details_talent,(int)(width*0.165),(int)(height*0.525),0,0);
        setMargins(details_tip,(int)(width*0.658),(int)(height*0.911),0,0);
        setMargins(details_plushp,(int)(width*0.374),(int)(height*0.525),(int)(width*0.593),(int)(height*0.421));
        setMargins(details_plusenergy,(int)(width*0.374),(int)(height*0.625),(int)(width*0.593),(int)(height*0.321));
        setMargins(details_plusatk,(int)(width*0.374),(int)(height*0.725),(int)(width*0.593),(int)(height*0.221));
        setMargins(details_plusdefence,(int)(width*0.374),(int)(height*0.825),(int)(width*0.593),(int)(height*0.121));
        setMargins(details_hp_plus_img,(int)(width*0.73),(int)(height*0.39),(int)(width*0.185),(int)(height*0.45));
        setMargins(details_energy_plus_img,(int)(width*0.73),(int)(height*0.59),(int)(width*0.185),(int)(height*0.25));
        setMargins(details_hp_plus_num,(int)(width*0.82),(int)(height*0.45),0,0);
        setMargins(details_energy_plus_num,(int)(width*0.82),(int)(height*0.65),0,0);

        details_plushp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(STATIC.myplayer.talent>0)
                {
                    STATIC.myplayer.hp+=10;
                    STATIC.myplayer.talent--;
                    details_hp.setText("生命值:"+STATIC.myplayer.hp);
                    details_talent.setText("天赋点:"+STATIC.myplayer.talent);
                }
            }
        });
        details_plusenergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(STATIC.myplayer.talent>0)
                {
                    STATIC.myplayer.energy+=1;
                    STATIC.myplayer.talent--;
                    details_energy.setText("能量值:"+STATIC.myplayer.energy);
                    details_talent.setText("天赋点:"+STATIC.myplayer.talent);
                }
            }
        });
        details_plusatk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(STATIC.myplayer.talent>0)
                {
                    STATIC.myplayer.atk+=1;
                    STATIC.myplayer.talent--;
                    details_atk.setText("攻击力:"+STATIC.myplayer.atk);
                    details_talent.setText("天赋点:"+STATIC.myplayer.talent);
                }
            }
        });
        details_plusdefence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(STATIC.myplayer.talent>0)
                {
                    STATIC.myplayer.defence+=1;
                    STATIC.myplayer.talent--;
                    details_defence.setText("防御力:"+STATIC.myplayer.defence);
                    details_talent.setText("天赋点:"+STATIC.myplayer.talent);
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
