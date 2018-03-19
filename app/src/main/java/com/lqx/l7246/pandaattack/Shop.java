package com.lqx.l7246.pandaattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by l7246 on 2017/11/6.
 */

public class Shop extends Activity{
    public int width,height;
    private Button buy_hp,buy_defence;
    private ImageView buy_money_plus;
    private TextView buy_money;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        WindowManager wm = this.getWindowManager();
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();

        setContentView(R.layout.shop);

        buy_hp=(Button)findViewById(R.id.buy_hp);
        buy_defence=(Button)findViewById(R.id.buy_energy);
        buy_money=(TextView)findViewById(R.id.buy_money);
        buy_money_plus=(ImageView)findViewById(R.id.buy_moneyimg);

        setMargins(buy_hp,(int)(width*0.534),(int)(height*0.689),(int)(width*0.397),(int)(height*0.214));
        setMargins(buy_defence,(int)(width*0.534),(int)(height*0.851),(int)(width*0.397),(int)(height*0.056));
        setMargins(buy_money,(int)(width*0.09),(int)(height*0.01),(int)(width*0.748),(int)(height*0.867));
        setMargins(buy_money_plus,(int)(width*0.01),(int)(height*0.01),(int)(width*0.748),(int)(height*0.867));
        buy_money.setTextSize((float)(width*0.020));

        buy_money.setText(""+STATIC.myplayer.money);
        buy_money_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Shop.this,Pay.class);
                startActivity(intent);
            }
        });

        buy_hp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Shop.this);  //先得到构造器
                builder.setTitle("红罐虽好,但不要贪杯哦"); //设置标题
                builder.setMessage("你确定花费99个竹子购买一个红罐？"); //设置内容
                builder.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
                builder.setPositiveButton("买买买", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        if(STATIC.myplayer.money>=99)
                        {
                            STATIC.myplayer.money-=99;
                            STATIC.myplayer.hpplus++;
                            buy_money.setText(""+STATIC.myplayer.money);
                            Toast.makeText(Shop.this, "购买成功", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Shop.this, "竹子不足，正在前往充值页面", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Shop.this,Pay.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
            }
        });
        buy_defence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Shop.this);  //先得到构造器
                builder.setTitle("蓝罐诚可贵,竹子价更高"); //设置标题
                builder.setMessage("你确定花费99个竹子购买一个蓝罐？"); //设置内容
                builder.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
                builder.setPositiveButton("买买买", new DialogInterface.OnClickListener() { //设置确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //关闭dialog
                        if(STATIC.myplayer.money>=99)
                        {
                            STATIC.myplayer.money-=99;
                            STATIC.myplayer.energyplus++;
                            buy_money.setText(""+STATIC.myplayer.money);
                            Toast.makeText(Shop.this, "购买成功", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(Shop.this, "竹子不足，正在前往充值页面", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Shop.this,Pay.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.setNegativeButton("不了", new DialogInterface.OnClickListener() { //设置取消按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //参数都设置完成了，创建并显示出来
                builder.create().show();
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
