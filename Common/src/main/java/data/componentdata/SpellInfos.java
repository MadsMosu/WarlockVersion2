/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

import data.Entity;
import data.SpellType;

/**
 *
 * @author Aleksander
 */
public class SpellInfos {

    private Entity hitBy;
    private SpellType usedSpell;
    private SpellType chosenSpell;
    private SpellType spellType;
    private float damage;
    private boolean isStatic;
    private float speed;
    private float acceleration;
    private int cooldown;
    private int bouncePoints;

    public SpellType getUsedSpell() {
        return usedSpell;
    }

    public void setUsedSpell(SpellType usedSpell) {
        this.usedSpell = usedSpell;
    }

    public SpellType getChosenSpell() {
        return chosenSpell;
    }

    public void setChosenSpell(SpellType chosenSpell) {
        this.chosenSpell = chosenSpell;
    }

    
    
    public SpellType hitByWhichSpell() {
        return getHitBy().get(SpellType.class);
    }

    public Entity getHitBy() {
        return hitBy;
    }

    public void setHitBy(Entity hitBy) {
        this.hitBy = hitBy;
    }

    public SpellType getSpellType() {
        return spellType;
    }

    public void setSpellType(SpellType spellType) {
        this.spellType = spellType;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void setCooldown(int cooldown) {
        this.cooldown = cooldown;
    }

    public int getBouncePoints() {
        return bouncePoints;
    }

    public void setBouncePoints(int bouncePoints) {
        this.bouncePoints = bouncePoints;
    }

}
