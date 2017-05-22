/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;
import data.SpellType;
import data.componentdata.Image;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author mads1
 */
public class SpellList {
    
    private static final Map<SpellType, String> spellSpriteMap = new ConcurrentHashMap<>();
    
    public static final String FIREBALL_IMAGE = "assets/fireballsprite.png";
    public static final int FIREBALL_DMG = 10;
    public static final int FIREBALL_BOUNCE = 4;
    public static final float FIREBALL_SPEED = 200f;
    public static final float FIREBALL_EXPIRATION = 3f;
    public static final float FIREBALL_ACCELERATION = 20f;
    public static final float FIREBALL_COOLDOWN = 2f;
    public static final boolean FIREBALL_STATIC = false;
    public static final int FIREBALL_WIDTH = 15;
    public static final int FIREBALL_HEIGHT = 15;
    public static final int FIREBALL_SPRITE_WIDTH = 51;
    public static final int FIREBALL_SPRITE_HEIGHT = 15;
    public static final int FIREBALL_FRAMES = 8;
    public static final float FIREBALL_FRAME_SPEED = 0.1f;
    
    public static final String FROSTBOLT_IMAGE = "assets/frostboltsprite.png";
    public static final int FROSTBOLT_DMG = 5;
    public static final int FROSTBOLT_BOUNCE = 4;
    public static final float FROSTBOLT_SPEED = 200f;
    public static final float FROSTBOLT_EXPIRATION = 3f;
    public static final float FROSTBOLT_ACCELERATION = 20f;
    public static final float FROSTBOLT_COOLDOWN = 3f;
    public static final boolean FROSTBOLT_STATIC = false;
    public static final int FROSTBOLT_WIDTH = 15;
    public static final int FROSTBOLT_HEIGHT = 15;
    public static final int FROSTBOLT_SPRITE_WIDTH = 51;
    public static final int FROSTBOLT_SPRITE_HEIGHT = 15;
    public static final int FROSTBOLT_FRAMES = 8;
    public static final float FROSTBOLT_FRAME_SPEED = 0.1f;
    
    public static final String TELEPORT_IMAGE = "assets/teleportsprite1.png";
    public static final float TELEPORT_COOLDOWN = 10f;
    public static final int TELEPORT_WIDTH = 50;
    public static final int TELEPORT_HEIGHT = 90;
    public static final float TELEPORT_SPEED = 150f;
    public static final float TELEPORT_EXPIRATION = 0.5f;
    public static final float TELEPORT_ACCELERATION = 20f;
    public static final int TELEPORT_DMG = 10;
    public static final int TELEPORT_SPRITE_WIDTH = 50;
    public static final int TELEPORT_SPRITE_HEIGHT = 90;
    public static final int TELEPORT_FRAMES = 19;
    public static final float TELEPORT_FRAME_SPEED = 0.01f;
    
    
    
    
    public static Map getSpellMap(){
        spellSpriteMap.put(SpellType.FIREBALL, FIREBALL_IMAGE);
        spellSpriteMap.put(SpellType.FROSTBOLT, FROSTBOLT_IMAGE);
        spellSpriteMap.put(SpellType.TELEPORT1, TELEPORT_IMAGE);
        spellSpriteMap.put(SpellType.TELEPORT2, TELEPORT_IMAGE);
        
        return spellSpriteMap;
    }
    public static float getSpellSpeed(SpellType spellType){
        switch(spellType){
            case FIREBALL:
                return FIREBALL_SPEED;
        }
        return 0; 
    }
    
    public static float getSpellDmg(SpellType spellType){
        switch(spellType){
            case FIREBALL:
                return FIREBALL_DMG;
            case FROSTBOLT:
                return FROSTBOLT_DMG;
        }
        return 0; 
    }
    
    
}
