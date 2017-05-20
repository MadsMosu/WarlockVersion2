/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.spell;


import data.SpellType;
import data.Entity;
import data.ImageManager;
import data.World;
import data.componentdata.Expiration;

public class Spell {

    private SpellType spellType;
    private int damage;
    private boolean isStatic;
    private Entity spellEntity;
    private float speed;
    private float acceleration;
    private float cooldown;
    private int bouncePoints;
    private String SPELL_IMAGE_PATH = "";
    private int width;
    private int height;
    private int spriteHeight;
    private int spriteWidth;
    private int frames;
    private float frameSpeed;
    private float expiration;

    public Spell(World world, SpellType spellType, int damage, String path, boolean isStatic, float expiration, float speed, float acceleration, float cooldown, int bouncePoints, int width, int height, int spriteWidth, int spriteHeight, int frames, float frameSpeed) {

        this.spellType = spellType;
        this.damage = damage;
        this.isStatic = isStatic;
        this.speed = speed;
        this.acceleration = acceleration;
        this.cooldown = cooldown;
        this.bouncePoints = bouncePoints;
        this.width = width;
        this.height = height;
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;
        this.frames = frames;
        this.frameSpeed = frameSpeed;
        this.expiration = expiration;
    }

    public Spell(World world, SpellType spellType, String path, float expiration, float cooldown, int width, int height, int spriteWidth, int spriteHeight, int frames, float frameSpeed){
 
        this.spellType = spellType;
        this.cooldown = cooldown;
        this.width = width;
        this.height = height;
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;
        this.frames = frames;
        this.frameSpeed = frameSpeed;
        this.expiration = expiration;
    }
    
    public float getExpiration(){
        return expiration;
    }
    public float getFrameSpeed(){
        return frameSpeed;
    }
    public int getFrames(){
        return frames;
    }
    
    public int getSpriteHeight(){
        return spriteHeight;
    }
    
    public int getSpriteWidth(){
        return spriteWidth;
    }
    public int getBouncePoints() {
        return bouncePoints;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }

    public SpellType getSpellType() {
        return spellType;
    }

    public int getDamage() {
        return damage;
    }

    public float getCooldown() {
        return cooldown;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public Entity getSpellEntity() {
        return spellEntity;
    }

    public float getSpeed() {
        return speed;
    }

    public float getAcceleration() {
        return acceleration;
    }

}
