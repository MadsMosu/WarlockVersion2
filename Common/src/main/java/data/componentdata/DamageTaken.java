/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

import data.Entity;

/**
 *
 * @author Aleksander
 */
public class DamageTaken
{
    private Damage damage;
    private Owner owner;

    public DamageTaken(Damage damage, Owner owner)
    {
        this.damage = damage;
        this.owner = owner;
    }

    public int getDamage()
    {
        return damage.getDamage();
    }

    public Owner getOwner()
    {
        return owner;
    }

    
    

}