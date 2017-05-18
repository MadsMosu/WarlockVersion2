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
    
    public static final String FIREBALL_IMAGE = "assets/fireballsprite.png";
    public static final SpellType spellType = SpellType.FIREBALL;
    public static final int FIREBALL_DMG = 10;
    public static final int FIREBALL_BOUNCE = 4;
    public static final float FIREBALL_SPEED = 150f;
    public static final float FIREBALL_EXPIRATION = 4f;
    public static final float FIREBALL_ACCELERATION = 20f;
    public static final float FIREBALL_COOLDOWN = 4f;
    public static final boolean FIREBALL_STATIC = false;
    public static final int FIREBALL_WIDTH = 15;
    public static final int FIREBALL_HEIGHT = 15;
    
    public static float getSpellSpeed(SpellType spellType){
        switch(spellType){
            case FIREBALL:
                return FIREBALL_SPEED;
        }
        return 0; 
    }
    
    
}
