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
    private float maxHp;
    private float hp;
    private ArrayList<DamageTaken> damageTaken;

    public Health(float maxHp)
    {
        this.maxHp = maxHp;
        this.hp = maxHp;
        damageTaken = new ArrayList<>();
    }

    public float getHp()
    {
        return hp;
    }

    public void setHp(float hp)
    {
        this.hp = hp;
    }


    public double getMaxHp()
    {
        return maxHp;
    }

    public void setMaxHp(float maxHp)
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
