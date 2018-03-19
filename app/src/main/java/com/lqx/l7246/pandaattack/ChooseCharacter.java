package com.lqx.l7246.pandaattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by l7246 on 2017/11/1.
 */

public class ChooseCharacter extends Activity{
    private long exitTime=0;
    public int width,height;
    private String[] mSpinnerData = {"超人熊猫","诸葛熊猫","吃货熊猫"};
    private Spinner character_choose_spinner;
    private ArrayAdapter<String> mAdapter;
    private TextView character_choose_text1;
    private RelativeLayout choose_character_table;
    private ImageView choose_character_img;
    private TextView choose_describe,choose_hp,choose_energy,choose_atk,choose_defence;
    private GridLayout choose_msg_grid;
    private ImageView choose_confirm_bt;
    public int choosetype=1;
    public Character chosenCharacter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        WindowManager wm = this.getWindowManager();
        this.width = wm.getDefaultDisplay().getWidth();
        this.height = wm.getDefaultDisplay().getHeight();

        setContentView(R.layout.choose_character);

        character_choose_text1=(TextView)findViewById(R.id.character_choose_text1);
        setMargins(character_choose_text1,width/5,height/10,width/5*2,height/10*8);

        character_choose_spinner=(Spinner)findViewById(R.id.character_choose_spinner);
        setMargins(character_choose_spinner,width/5*3,height/10,width/10,height/10*8);

        mAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,mSpinnerData);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        character_choose_spinner.setAdapter(mAdapter);

        choose_character_table=(RelativeLayout)findViewById(R.id.choose_character_table);
        choose_character_img=(ImageView)findViewById(R.id.choose_character_img);
        choose_describe=(TextView)findViewById(R.id.choose_describe);
        choose_hp=(TextView)findViewById(R.id.choose_hp);
        choose_energy=(TextView)findViewById(R.id.choose_energy);
        choose_atk=(TextView)findViewById(R.id.choose_atk);
        choose_defence=(TextView)findViewById(R.id.choose_defence);
        choose_msg_grid=(GridLayout)findViewById(R.id.choose_msg_grid);

        setCharacterMSG(1);

        setMargins(choose_character_img,0,0,width/10*3,0);
        setMargins(choose_msg_grid,width/20*7,0,0,0);

        setMargins(choose_character_table,width/5,height/10*3,width/5,height/10);

        choose_confirm_bt=(ImageView)findViewById(R.id.choose_confirm_bt);
        int tempck=height/3;
        setMargins(choose_confirm_bt,width-tempck,height-tempck,0,0);

        character_choose_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        setCharacterMSG(1);
                        choosetype=1;
                        break;
                    case 1:
                        setCharacterMSG(2);
                        choosetype=2;
                        break;
                    case 2:
                        setCharacterMSG(3);
                        choosetype=3;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        choose_confirm_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ChooseCharacter.this).setTitle("你确认选择这个熊猫吗？")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定，就是它了", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“确认”后的操作
                                STATIC.myplayer.charactertype=choosetype;
                                STATIC.myplayer.mycharacter=new Character(STATIC.myplayer.charactertype);
                                STATIC.myplayer.mycharacter.setDefaultMSG();
                                STATIC.myplayer.hp=STATIC.myplayer.mycharacter.defaulthp;
                                STATIC.myplayer.energy=STATIC.myplayer.mycharacter.defaultenergy;
                                STATIC.myplayer.atk=STATIC.myplayer.mycharacter.defaultatk;
                                STATIC.myplayer.defence=STATIC.myplayer.mycharacter.defaultdefence;
                                STATIC.myplayer.hp=STATIC.myplayer.mycharacter.defaulthp;
                                if(STATIC.UpdateUserData())
                                {
                                    Toast.makeText(ChooseCharacter.this, "用户资料存储成功", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(ChooseCharacter.this,MapChoosePOI.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(ChooseCharacter.this, "用户资料存储失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("我要再想一想", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 点击“返回”后的操作,这里不设置没有任何操作
                            }
                        }).show();
            }

        });

    }
    public void setCharacterMSG(int n)
    {
        chosenCharacter=new Character(n);
        chosenCharacter.setDefaultMSG();
        choose_character_img.setImageResource(chosenCharacter.getIMG());
        choose_describe.setText(""+chosenCharacter.describe);
        choose_hp.setText("初始血量："+chosenCharacter.defaulthp);
        choose_energy.setText("初始能量值："+chosenCharacter.defaultenergy);
        choose_atk.setText("初始攻击力："+chosenCharacter.defaultatk);
        choose_defence.setText("初始防御力："+chosenCharacter.defaultdefence);
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
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(ChooseCharacter.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    //退出登录Logout
                    finish();
                    System.exit(0);
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
    @Override
    public void onBackPressed()
    {
        finish();
    }
}
