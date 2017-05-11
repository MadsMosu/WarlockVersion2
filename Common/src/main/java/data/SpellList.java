/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;
import data.SpellType;

/**
 *
 * @author mads1
 */
public class SpellList {
    
    public static final SpellType spellType = SpellType.FIREBALL;
    public static final int FIREBALL_DMG = 10;
    public static final int FIREBALL_BOUNCE = 4;
    public static final int FIREBALL_SPEED = 200;
    public static final int FIREBALL_EXPIRATION = 5;
    public static final int FIREBALL_ACCELERATION = 20;
    public static final int FIREBALL_COOLDOWN = 4;
    public static final boolean FIREBALL_STATIC = false;
    
    public static int getSpellSpeed(SpellType spellType){
        switch(spellType){
            case FIREBALL:
                return FIREBALL_SPEED;
        }
        return 0; 
    }
}
