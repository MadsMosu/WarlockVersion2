/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

import java.util.ArrayList;

/**
 *
 * @author Aleksander
 */
public class Health
{
    private double maxHp;
    private double hp;
    private ArrayList<DamageTaken> damageTaken;

    public Health(double maxHp)
    {
        this.maxHp = maxHp;
        this.hp = maxHp;
        damageTaken = new ArrayList<>();
    }

    public double getHp()
    {
        return hp;
    }

    public void setHp(double hp)
    {
        this.hp = hp;
    }


    public double getMaxHp()
    {
        return maxHp;
    }

    public void setMaxHp(double maxHp)
    {
        this.maxHp = maxHp;
    }

    public ArrayList<DamageTaken> getDamageTaken()
    {
        return damageTaken;
    }

    public void addDamageTaken(DamageTaken dt)
    {
        damageTaken.add(dt);
    }

}
