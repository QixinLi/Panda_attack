package com.lqx.l7246.pandaattack;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by l7246 on 2017/10/30.
 */

public class Player {
    String tel;
    String name;
    int money;
    int hp;
    int energy;
    int atk;
    int defence;
    int exp;
    int level;
    int hpplus;
    int energyplus;
    int charactertype;
    int talent;
    Character mycharacter;
    Player(String tel,String name,int money,int hp,int energy,int atk,int defence,int exp,int level,int hpplus,int energyplus,int charactertype,int talent)
    {
        this.tel=tel;
        this.name=name;
        this.money=money;
        this.hp=hp;
        this.energy=energy;
        this.atk=atk;
        this.defence=defence;
        this.exp=exp;
        this.level=level;
        this.hpplus=hpplus;
        this.energyplus=energyplus;
        this.charactertype=charactertype;
        this.talent=talent;
        if(this.charactertype!=0)
        {
            this.mycharacter=new Character(this.charactertype);
            this.mycharacter.setDefaultMSG();
        }
    }
}
class STATIC{
    public static String mytel="";
    public static String mypassword="";
    public static Player myplayer;
    public static Player enemyplayer;
    public static String enemytel;
    public static boolean isCheckWifi=false;
    public static String chosenpoi="";
    public static boolean ismeetplayer=false;
    public static boolean UpdateUserData()//更新玩家数据
    {
        boolean temp=false;
        try {
            String jsonData=HttpUtils.doGet("http://106.14.151.117:9090/GameJSON/WebContent/UpdateUserData.jsp?tel="+myplayer.tel+"&money="+myplayer.money+"&hp="+myplayer.hp+"&energy="+myplayer.energy+"&atk="+myplayer.atk+"&defence="+myplayer.defence+"&exp="+myplayer.exp+"&level="+myplayer.level+"&hpplus="+myplayer.hpplus+"&energyplus="+myplayer.energyplus+"&talent="+myplayer.talent+"&charactertype="+myplayer.charactertype);
            JSONObject jsonObject = new JSONObject(jsonData);
            String result = jsonObject.getString("result");
            if(result.equals("fail"))
            {
                temp=false;
            }
            else
            {
                temp=true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    public static boolean UpdateEnemyData()//更新敌方玩家数据
    {
        boolean temp=false;
        try {
            String jsonData=HttpUtils.doGet("http://106.14.151.117:9090/GameJSON/WebContent/UpdateUserData.jsp?tel="+enemyplayer.tel+"&money="+enemyplayer.money+"&hp="+enemyplayer.hp+"&energy="+enemyplayer.energy+"&atk="+enemyplayer.atk+"&defence="+enemyplayer.defence+"&exp="+enemyplayer.exp+"&level="+enemyplayer.level+"&hpplus="+enemyplayer.hpplus+"&energyplus="+enemyplayer.energyplus+"&talent="+enemyplayer.talent+"&charactertype="+enemyplayer.charactertype);
            JSONObject jsonObject = new JSONObject(jsonData);
            String result = jsonObject.getString("result");
            if(result.equals("fail"))
            {
                temp=false;
            }
            else
            {
                temp=true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    public static boolean OccupyPOI()//占领城池
    {
        boolean temp=false;
        try {
            String tpoi= URLEncoder.encode(chosenpoi,"UTF-8");
            String jsonData=HttpUtils.doGet("http://106.14.151.117:9090/GameJSON/WebContent/occupyPOI.jsp?poi="+tpoi+"&tel="+myplayer.tel);
            JSONObject jsonObject = new JSONObject(jsonData);
            String result = jsonObject.getString("result");
            if(result.equals("fail"))
            {
                temp=false;
            }
            else
            {
                temp=true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
    public static int searchPOI()//查询当前选中城池的被占有情况
    {
        int resultdata=3;//0-无人占有、1-自己占有、2-他人占有、3-网络不畅
        try {
            String tpoi= URLEncoder.encode(chosenpoi,"UTF-8");
            String jsonData=HttpUtils.doGet("http://106.14.151.117:9090/GameJSON/WebContent/GetPoiData.jsp?poi="+tpoi);
            JSONObject jsonObject = new JSONObject(jsonData);
            String result = jsonObject.getString("result");
            if(result.equals("success"))
            {
                String playertel=jsonObject.getString("playertel");
                enemytel=playertel;
                if(playertel.equals(myplayer.tel))
                {
                    resultdata=1;
                }
                else
                {
                    resultdata=2;
                }
            }
            else if(result.equals("fail"))
            {
                resultdata=0;
            }
            else
            {
                resultdata=3;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultdata;
    }
    public static boolean getEnemyData() //获取敌方玩家数据
    {
        boolean temp = false;
        try {
            String jsonData = HttpUtils.doGet("http://106.14.151.117:9090/GameJSON/WebContent/getPlayerDetails.jsp?tel=" + enemytel);
            JSONObject jsonObject = new JSONObject(jsonData);
            String result = jsonObject.getString("result");
            if (result.equals("fail")) {
                temp = false;
            } else {
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
                STATIC.enemyplayer = new Player(enemytel, name, money, hp, energy, atk, defence, exp, level, hpplus, energyplus, charactertype,talent);
                temp = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }
}
