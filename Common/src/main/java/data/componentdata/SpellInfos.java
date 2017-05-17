
package data.componentdata;

import data.Entity;
import data.SpellType;

/**
 *
 * @author Aleksander
 */
public class SpellInfos {

    private Entity hitBy;
    private Entity spellEntity;
    private SpellType usedSpell;

    private SpellType spellType;
    private float damage;
    private boolean isMoving;
    private float speed;
    private float acceleration;
    private int cooldown;
    private int bouncePoints;
    private boolean isHit = false;

    public SpellType getUsedSpell() {
        return usedSpell;
    }

    public void setUsedSpell(SpellType usedSpell) {
        this.usedSpell = usedSpell;
    }

    public Entity getSpellEntity() {
        return spellEntity;
    }

    public void setSpellEntity(Entity spellEntity) {
        this.spellEntity = spellEntity;
    }

    public boolean isIsHit() {
        return isHit;
    }

    public void setIsHit(boolean isHit) {
        this.isHit = isHit;
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

    public boolean isMoving() {
        return isMoving;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
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
