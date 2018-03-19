package com.lqx.l7246.pandaattack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.lqx.l7246.pandaattack.Music.Arrow;
import com.lqx.l7246.pandaattack.Music.Bamboo;
import com.lqx.l7246.pandaattack.Music.Fireball;
import com.lqx.l7246.pandaattack.Music.Lighting;
import com.lqx.l7246.pandaattack.Music.Riceroll;

import java.util.Random;

public class Game extends Activity {
    public static boolean flag=true;  //玩家是否可以进行点击摁扭的标志，false为不可点击

    public boolean isGameOver=false;
    //生成Player和Enemy对象
    public Temp playertemp;
    public Temp enemytemp;
    TextView textView;
    public Intent intent1,intent2,intent3,intent4,intent5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyApplication.getInstance().addActivity(this);
        //全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //取消标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  //取消状态栏

        //加载屏幕控件
        setContentView(R.layout.game);

        //加载控件数据
        playertemp=new Temp(STATIC.myplayer);
        enemytemp=new Temp(STATIC.enemyplayer);

        setLayout();

        textView = (TextView) findViewById(R.id.messagetv);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());

        loadPort(playertemp.charatertype,enemytemp.charatertype);
        loadData(playertemp,playertemp.hp,playertemp.energy,enemytemp,enemytemp.hp,enemytemp.energy);

        //事件处理
        Button specatkbttn=(Button)findViewById(R.id.action0);
        specatkbttn.setText(STATIC.myplayer.mycharacter.skilldescribe);
        Button usualatkbttn=(Button)findViewById(R.id.action1);
        Button defbttn=(Button)findViewById(R.id.action2);
        Button toolbttn=(Button)findViewById(R.id.action3);
        Button hpbttn=(Button)findViewById(R.id.hpbttn);
        final Button enegybttn=(Button)findViewById(R.id.energybttn);

        intent1 = new Intent(Game.this,Lighting.class);
        intent2 = new Intent(Game.this,Arrow.class);
        intent3 = new Intent(Game.this,Riceroll.class);
        intent4 = new Intent(Game.this,Fireball.class);
        intent5 = new Intent(Game.this,Bamboo.class);

        specatkbttn.setOnClickListener (new View.OnClickListener()   //技能攻击
        {
            @Override
            public void onClick(View view) {
                if(flag)
                {
                    if(playertemp.energy>=3){
                        flag=false;
                        int atk=spec_decrease(playertemp,enemytemp);
                        SendMSG(playertemp.name+"发动"+STATIC.myplayer.mycharacter.skilldescribe+"，对"+enemytemp.name+"造成"+atk+"点伤害\n");
                        enemytemp.hp-=atk;
                        impact(playertemp.charatertype);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                                if(!isAlive(enemytemp.hp)){
                                    endGame(isAlive(enemytemp.hp));
                                }
                                enemyaction();
                            }
                        },2000);
                    }
                    else
                    {
                        SendWarning("没蓝了骚年！磕瓶蓝罐补补肾吧！");
                    }
                }
            }
        });
        defbttn.setOnClickListener(new View.OnClickListener()   //防御
        {
            @Override
            public void onClick(View view) {
                if(flag)
                {
                    if(playertemp.energy>=3){
                        flag=false;
                        playertemp.energy-=3;
                        playertemp.atk++;
                        playertemp.def+=3;
                        loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                        SendMSG(playertemp.name+"发动金钟罩，提升了自身的属性\n");
                        enemyaction();
                    }
                    else
                    {
                        SendWarning("没蓝了骚年！磕瓶蓝罐补补肾吧！");
                    }
                }
            }
        });
        usualatkbttn.setOnClickListener(new View.OnClickListener()   //普通攻击
        {
            @Override
            public void onClick(View view) {
                if(flag) {
                    flag=false;
                    int atk=usual_decrease(playertemp, enemytemp);
                    enemytemp.hp-=atk;

                    impact(5);
                    SendMSG(playertemp.name+"朝"+enemytemp.name+"扔竹子，造成了"+atk+"点伤害\n");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                            if(!isAlive(enemytemp.hp)){
                                endGame(isAlive(enemytemp.hp));
                            }
                            enemyaction();
                        }
                    },2000);
                }
            }
        });
        toolbttn.setOnClickListener(new View.OnClickListener()   //工具
        {
            @Override
            public void onClick(View view) {
                if(flag){
                    LinearLayout layout=(LinearLayout) findViewById(R.id.toolLayout);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });
        hpbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag)
                {
                    if(playertemp.hp==STATIC.myplayer.hp)
                    {
                        SendWarning("是药三分毒，血满的就不要嗑药了！");
                    }
                    else if(playertemp.hpplus>=1){
                        flag=false;
                        playertemp.hpplus--;
                        STATIC.myplayer.hpplus--;
                        playertemp.hp+=50;
                        if(playertemp.hp>=STATIC.myplayer.hp)
                            playertemp.hp=STATIC.myplayer.hp;
                        loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                        SendMSG(playertemp.name+"磕了一瓶红罐，恢复了自身的生命值\n");
                        enemyaction();
                    }
                    else
                    {
                        SendWarning("别投机取巧，我知道你压根就没有红罐！");
                    }
                    LinearLayout layout=(LinearLayout)findViewById(R.id.toolLayout);
                    layout.setVisibility(View.INVISIBLE);
                }
            }
        });
        enegybttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flag)
                {
                    if(playertemp.energy==STATIC.myplayer.energy)
                    {
                        SendWarning("太高的法力会冲昏你的头脑！");
                    }
                    else if(playertemp.energyplus>=1){
                        flag=false;
                        playertemp.energyplus--;
                        STATIC.myplayer.energyplus--;
                        playertemp.energy+=10;
                        if(playertemp.energy>=STATIC.myplayer.energy)
                            playertemp.energy=STATIC.myplayer.energy;
                        loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                        SendMSG(playertemp.name+"磕了一瓶蓝罐，恢复了自身的法力值\n");
                        enemyaction();
                    }
                    else
                    {
                        SendWarning("别投机取巧，我知道你压根就没有蓝罐！");
                    }
                    LinearLayout layout=(LinearLayout) findViewById(R.id.toolLayout);
                    layout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
    private void setLayout()  //加载屏幕时的控件布置
    {
        //获取屏幕宽度和高度
        WindowManager wm = this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();

        //设置控件的的宽和高
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int)(height*0.125f+0.5f),(int)(height*0.125f+0.5f));
        LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams((int)(width*0.2f+0.5f),(int)((height*0.0625f+0.5f)*2/3));


        Button bttn1=(Button) findViewById(R.id.action0);
        Button bttn2=(Button)findViewById(R.id.action1);
        Button bttn3=(Button)findViewById(R.id.action2);
        Button bttn4=(Button)findViewById(R.id.action3);
        Button bttn5=(Button)findViewById(R.id.hpbttn);
        Button bttn6=(Button)findViewById(R.id.energybttn);
        float textsize=(float)(width*0.009);
        bttn1.setTextSize(textsize);
        bttn2.setTextSize(textsize);
        bttn3.setTextSize(textsize);
        bttn4.setTextSize(textsize);
        bttn5.setTextSize(textsize);
        bttn6.setTextSize(textsize);


        ProgressBar pb1=(ProgressBar) findViewById(R.id.HPPlayer);
        ProgressBar pb2=(ProgressBar)findViewById(R.id.EnergyPlayer);
        ProgressBar pb3=(ProgressBar)findViewById(R.id.HPEnemy);
        ProgressBar pb4=(ProgressBar)findViewById(R.id.EnergyEnemy);
        TextView tv1=(TextView) findViewById(R.id.tvPlayerHP);
        TextView tv2=(TextView)findViewById(R.id.tvPlayerEnergy);
        TextView tv3=(TextView)findViewById(R.id.tvEnemyHP);
        TextView tv4=(TextView)findViewById(R.id.tvEnemyEnergy);
        TextView pname=(TextView)findViewById(R.id.game_playername);
        TextView ename=(TextView)findViewById(R.id.game_enemyname);
        TextView space1=(TextView)findViewById(R.id.game_space1);
        TextView space2=(TextView)findViewById(R.id.game_space2);

        pname.setText(playertemp.name);
        ename.setText(enemytemp.name);
        float tempsize=(float) (width*0.009);
        tv1.setTextSize(tempsize);
        tv2.setTextSize(tempsize);
        tv3.setTextSize(tempsize);
        tv4.setTextSize(tempsize);
        pname.setTextSize(tempsize);
        ename.setTextSize(tempsize);
        space1.setTextSize(tempsize);
        space2.setTextSize(tempsize);

        bttn1.setLayoutParams(params);
        bttn2.setLayoutParams(params);
        bttn3.setLayoutParams(params);
        bttn4.setLayoutParams(params);
        bttn5.setLayoutParams(params);
        bttn6.setLayoutParams(params);
        pname.setLayoutParams(params1);
        ename.setLayoutParams(params1);
        space1.setLayoutParams(params1);
        space2.setLayoutParams(params1);
        pb1.setLayoutParams(params1);
        pb2.setLayoutParams(params1);
        pb3.setLayoutParams(params1);
        pb4.setLayoutParams(params1);
        tv1.setLayoutParams(params1);
        tv2.setLayoutParams(params1);
        tv3.setLayoutParams(params1);
        tv4.setLayoutParams(params1);
        bttn1.getBackground().setAlpha(100);
        bttn2.getBackground().setAlpha(100);
        bttn3.getBackground().setAlpha(100);
        bttn4.getBackground().setAlpha(100);
        bttn5.getBackground().setAlpha(100);
        bttn6.getBackground().setAlpha(100);

        ImageView iv1=(ImageView)findViewById(R.id.portPlayer);
        ImageView iv2=(ImageView)findViewById(R.id.portEnemy);
        iv1.setLayoutParams(params);
        iv2.setLayoutParams(params);
    }
    @Override
    protected void onResume()   //设置为横屏
    {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        super.onResume();
    }
    public void impact(int ch)  //释放技能时的效果显示，显示时间为2秒
    {
        flag=false;
        final ImageView imageView=(ImageView) findViewById(R.id.impactImage);
        switch (ch){
            case 1:
                Glide.with(this).load(R.drawable.lighting).into(new GlideDrawableImageViewTarget(imageView, 1));
                startService(intent1);
                break;
            case 2:
                Glide.with(this).load(R.drawable.arrow).into(new GlideDrawableImageViewTarget(imageView, 1));
                startService(intent2);
                break;
            case 3:
                Glide.with(this).load(R.drawable.riceroll).into(new GlideDrawableImageViewTarget(imageView, 1));
                startService(intent3);
                break;
            case 4:
                Glide.with(this).load(R.drawable.fireball).into(new GlideDrawableImageViewTarget(imageView, 1));
                startService(intent4);
                break;
            case 5:
                Glide.with(this).load(R.drawable.bamboo).into(new GlideDrawableImageViewTarget(imageView, 1));
                startService(intent5);
                break;
            default:
                break;
        }
        imageView.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.INVISIBLE);
                flag=true;
            }
        },2000);
    }
    public int spec_decrease(Temp player1,Temp player2)  //技能攻击，player1攻击player2，返回值为player2减少的血量
    {
        Random random=new Random();
        player1.energy-=3;
        if((int)(player1.atk*2)>player2.def)
            return (int)(player1.atk*2)-player2.def+random.nextInt(5)+1;
        else
            return random.nextInt(5)+1;
    }
    public int usual_decrease(Temp player1,Temp player2)  //普通攻击，player1攻击player2，返回值为player2减少的血量
    {
        Random random=new Random();
        if((int)(player1.atk*1.2)>player2.def)
            return (int)(player1.atk*1.2)-player2.def+random.nextInt(3)+1;
        else
            return random.nextInt(3)+1;
    }
    public void loadData(Temp player1,int hp1,int ener1, Temp player2,int hp2,int ener2)  //加载控件数据
    {
        TextView tv1=(TextView) findViewById(R.id.tvPlayerHP);
        TextView tv2=(TextView)findViewById(R.id.tvPlayerEnergy);
        TextView tv3=(TextView)findViewById(R.id.tvEnemyHP);
        TextView tv4=(TextView)findViewById(R.id.tvEnemyEnergy);
        tv1.setText(player1.hp+"/"+hp1);
        tv2.setText(player1.energy+"/"+ener1);
        tv3.setText(player2.hp+"/"+hp2);
        tv4.setText(player2.energy+"/"+ener2);
        ProgressBar pb1=(ProgressBar) findViewById(R.id.HPPlayer);
        ProgressBar pb2=(ProgressBar)findViewById(R.id.EnergyPlayer);
        ProgressBar pb3=(ProgressBar)findViewById(R.id.HPEnemy);
        ProgressBar pb4=(ProgressBar)findViewById(R.id.EnergyEnemy);

        setProgressBarVisibility(true);
        pb1.setMax(hp1);
        pb1.setProgress(player1.hp);
        pb2.setMax(ener1);
        pb2.setProgress(player1.energy);
        pb3.setMax(hp2);
        pb3.setProgress(player2.hp);
        pb4.setMax(ener2);
        pb4.setProgress(player2.energy);

    }
    public void loadPort(int pl1,int pl2)  //加载头像
    {
        ImageView iv1=(ImageView) findViewById(R.id.portPlayer);
        ImageView iv2=(ImageView) findViewById(R.id.portEnemy);
        ImageView pla1=(ImageView)findViewById(R.id.playerImage);
        ImageView pla2=(ImageView)findViewById(R.id.enemyimage);
        switch (pl1){
            case 1:
                iv1.setImageResource(R.drawable.superpanda);
                pla1.setImageResource(R.drawable.superpanda);
                break;
            case 2:
                iv1.setImageResource(R.drawable.zhugepanda);
                pla1.setImageResource(R.drawable.zhugepanda);
                break;
            case 3:
                iv1.setImageResource(R.drawable.foodiepanda);
                pla1.setImageResource(R.drawable.foodiepanda);
                break;
        }
        switch (pl2){
            case 1:
                iv2.setImageResource(R.drawable.superpanda);
                pla2.setImageResource(R.drawable.superpanda);
                break;
            case 2:
                iv2.setImageResource(R.drawable.zhugepanda);
                pla2.setImageResource(R.drawable.zhugepanda);
                break;
            case 3:
                iv2.setImageResource(R.drawable.foodiepanda);
                pla2.setImageResource(R.drawable.foodiepanda);
                break;
            case 4:
                iv2.setImageResource(R.drawable.pig);
                pla2.setImageResource(R.drawable.pig);
                break;
            case 5:
                iv2.setImageResource(R.drawable.pigking);
                pla2.setImageResource(R.drawable.pigking);
                break;
        }
    }
    public boolean isAlive(int p)  //判断游戏是否结束，false表示对手Game over
    {
        if(p>0)
            return true;
        else
            return false;
    }

    public void enemyaction()  //对手的动作
    {
        if(isGameOver)
        {
            return;
        }
        flag=false;
        //TextView textView = (TextView) findViewById(R.id.messagetv);
        int h=0;
        Random random=new Random();
        switch (random.nextInt(5)){
            case 0:
                h=usual_decrease(enemytemp,playertemp);
                playertemp.hp-=h;
                SendMSG(enemytemp.name+"朝"+playertemp.name+"扔火球，造成了"+h+"点伤害\n");
                if(!isAlive(playertemp.hp))
                    endGame(!isAlive(playertemp.hp));
                impact(4);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                        flag=true;
                    }
                },2000);
                break;
            case 1:
                if(enemytemp.energy>=3){
                    h=spec_decrease(enemytemp,playertemp);
                    playertemp.hp-=h;
                    SendMSG(enemytemp.name+"朝"+playertemp.name+"扔大火球，造成了"+h+"点伤害\n");
                    if(!isAlive(playertemp.hp))
                        endGame(!isAlive(playertemp.hp));
                    impact(4);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                            flag=true;
                        }
                    },2000);
                }
                else
                    enemyaction();
                break;
            case 2:
                if (enemytemp.energy>=3){
                    SendMSG(enemytemp.name+"使用了金钟罩，提升了自身属性\n");
                    enemytemp.energy-=3;
                    enemytemp.def+=3;
                    enemytemp.atk++;
                    loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                    flag=true;
                }
                else
                    enemyaction();
                break;
            case 3:
                if(enemytemp.hpplus>=1&&enemytemp.hp!=STATIC.enemyplayer.hp){
                    enemytemp.hpplus--;
                    SendMSG(enemytemp.name+"磕了瓶红罐，恢复了自身的生命值\n");
                    enemytemp.hp+=50;
                    if(enemytemp.hp>=STATIC.enemyplayer.hp)
                        enemytemp.hp=STATIC.enemyplayer.hp;
                    loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                    flag=true;
                }
                else
                    enemyaction();
                break;
            case 4:
                if(enemytemp.energyplus>=1&&enemytemp.energy!=STATIC.enemyplayer.energy) {
                    enemytemp.energyplus--;
                    SendMSG(enemytemp.name+"磕了瓶蓝罐，恢复了自身的法力值\n");
                    enemytemp.energy+=10;
                    if(enemytemp.energy>=STATIC.enemyplayer.energy)
                        enemytemp.energy=STATIC.enemyplayer.energy;
                    loadData(playertemp,STATIC.myplayer.hp,STATIC.myplayer.energy,enemytemp,STATIC.enemyplayer.hp,STATIC.enemyplayer.energy);
                    flag=true;
                }
                else
                    enemyaction();
                break;
        }

    }
    public void endGame(final boolean flag)
    {
        stopService(intent1);
        stopService(intent2);
        stopService(intent3);
        stopService(intent4);
        stopService(intent5);
        AlertDialog.Builder builder=new AlertDialog.Builder(Game.this);
        builder.setIcon(R.drawable.doctorpanda);//设置图标，图片id即可
        if(!flag)
        {
            isGameOver=true;
            if(STATIC.OccupyPOI())
            {
                boolean islevelup=false;
                int getmoney=(STATIC.enemyplayer.hp+STATIC.enemyplayer.atk*10+STATIC.enemyplayer.defence*10+STATIC.enemyplayer.energy*10)/5;
                int getexp=STATIC.enemyplayer.hp+STATIC.enemyplayer.atk*10+STATIC.enemyplayer.defence*10+STATIC.enemyplayer.energy*10;
                STATIC.myplayer.money+=getmoney;
                STATIC.myplayer.exp+=getexp;
                while(STATIC.myplayer.exp>=2000)
                {
                    islevelup=true;
                    STATIC.myplayer.level++;
                    STATIC.myplayer.talent++;
                    STATIC.myplayer.exp-=2000;
                }
                builder.setTitle("攻城胜利！"); //设置标题
                String tempstr;
                if(islevelup)
                {
                    tempstr="你击败了"+enemytemp.name+",成功占领了该城池\n缴获了"+getmoney+"个竹子\n等级提升至"+STATIC.myplayer.level+"级";
                }
                else
                {
                    tempstr="你击败了"+enemytemp.name+",成功占领了该城池\n缴获了"+getmoney+"个竹子";
                }
                builder.setMessage(tempstr);
            }
            else
            {
                builder.setTitle("ERROR"); //设置标题
                builder.setMessage("网络不畅，请链接网络后再试");
            }
        }
        else
        {
            if(STATIC.ismeetplayer)
            {
                int getmoney=(STATIC.myplayer.hp+STATIC.myplayer.atk*10+STATIC.myplayer.defence*10+STATIC.myplayer.energy*10)/5;
                STATIC.enemyplayer.money+=getmoney;
            }
            builder.setTitle("攻城失败！"); //设置标题
            builder.setMessage("再回去修炼修炼吧骚年");
        }

        builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(!flag)
                {
                    if(STATIC.OccupyPOI()&&STATIC.UpdateUserData())
                    {
                        Toast.makeText(Game.this, "用户信息存储成功", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    STATIC.UpdateEnemyData();
                }
                Intent intent=new Intent(Game.this,MapChoosePOI.class);
                startActivity(intent);
            }
        });
        builder.create().show();
    }
    public void SendMSG(String msg){
        textView.append(msg);
        int offset=textView.getLineCount()*textView.getLineHeight();
        if(offset>textView.getHeight()){
            textView.scrollTo(0,offset-textView.getHeight());
        }
    }
    public void SendWarning(String msg){
        textView.append(Html.fromHtml(String.format("<font color='#FF0000'>%1$s</font>", msg)));
        SendMSG("\n");
        int offset=textView.getLineCount()*textView.getLineHeight();
        if(offset>textView.getHeight()){
            textView.scrollTo(0,offset-textView.getHeight());
        }
    }
}