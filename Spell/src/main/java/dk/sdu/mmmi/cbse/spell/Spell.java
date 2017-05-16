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
    private final float damage;
    private boolean isStatic;
    private Entity spellEntity;
    private float speed;
    private float acceleration;
    private float cooldown;
    private int bouncePoints;
    private String SPELL_IMAGE_PATH = "";

    public Spell(World world, SpellType spellType, float damage, String path, boolean isStatic, float expiration, float speed, float acceleration, float cooldown, int bouncePoints) {
        SPELL_IMAGE_PATH = Spell.class.getResource(path).getPath().replace("file:", "");
        ImageManager.createImage(SPELL_IMAGE_PATH, false);

        spellEntity = new Entity();
        spellEntity.add(new Expiration(expiration));
        spellEntity.setView(ImageManager.getImage(SPELL_IMAGE_PATH));
        world.addEntity(spellEntity);

        this.spellType = spellType;
        this.damage = damage;
        this.isStatic = isStatic;
        this.speed = speed;
        this.acceleration = acceleration;
        this.cooldown = cooldown;
        this.bouncePoints = bouncePoints;
    }

    public int getBouncePoints() {
        return bouncePoints;
    }

    public SpellType getSpellType() {
        return spellType;
    }

    public float getDamage() {
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
