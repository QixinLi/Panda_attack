package com.lqx.l7246.pandaattack;

/**
 * Created by l7246 on 2017/11/8.
 */

public class Temp{
    public String name;
    public int hp;
    public int energy;
    public int atk;
    public int def;
    public int charatertype;
    public int turnofdef;
    public int hpplus;
    public int energyplus;
    public Character character;
    public Temp(Player player)
    {
        turnofdef=0;
        this.name=player.name;
        this.hp=player.hp;
        this.energy=player.energy;
        this.atk=player.atk;
        this.def=player.defence;
        this.charatertype=player.charactertype;
        this.hpplus=player.hpplus;
        this.energyplus=player.energyplus;
    }
}