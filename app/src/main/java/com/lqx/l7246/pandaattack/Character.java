package com.lqx.l7246.pandaattack;

/**
 * Created by l7246 on 2017/11/1.
 */

public class Character {
    public int type;
    public String describe;
    public int defaulthp,defaultenergy,defaultatk,defaultdefence;
    public String skilldescribe;

    public Character(int type)
    {
        this.type=type;
    }
    public int getIMG()
    {
        int temp=0;
        switch (type)
        {
            case 1:
                temp=R.drawable.superpanda;
                break;
            case 2:
                temp=R.drawable.zhugepanda;
                break;
            case 3:
                temp=R.drawable.foodiepanda;
                break;
            default:
                break;
        }
        return temp;
    }
    public void setDefaultMSG()
    {
        switch (type)
        {
            //超人熊猫
            case 1:
                describe="熊猫界的超人，哦不，超猫。其实他只是在Cosplay罢了。不过，你同样要小心他的拳头。";
                skilldescribe="电眼逼人";
                defaulthp=100;
                defaultenergy=10;
                defaultatk=25;
                defaultdefence=10;
                break;
            //诸葛熊猫
            case 2:
                describe="此熊猫来自三国时期，赤壁之战利用草船借到了不少箭。善用计谋，总能化解敌人的攻击。";
                skilldescribe="诸葛连弩";
                defaulthp=100;
                defaultenergy=10;
                defaultatk=20;
                defaultdefence=15;
                break;
            //吃货熊猫
            case 3:
                describe="吃货熊猫满脑子就一个字‘吃’。你可不想打扰他吃东西，当心他朝你扔饭团！";
                skilldescribe="丢雷饭团";
                defaulthp=150;
                defaultenergy=10;
                defaultatk=20;
                defaultdefence=10;
                break;
            default:
                break;
        }
    }
}
