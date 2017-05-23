/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

import data.SpellType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Aleksander
 */
public class SpellBook {

    private SpellType chosenSpell;
    private float globalCooldownTime;
    private float cooldownTimeLeft;
    private ArrayList<SpellType> spells;
    private Map<SpellType, Float> spellCooldowns;
    private Owner owner;

    public SpellBook(Owner owner){
        this.owner = owner;
        spells = new ArrayList<>();
        spellCooldowns = new ConcurrentHashMap();
        globalCooldownTime = 1f;
        
    }

    public Map<SpellType, Float> getSpellCooldowns()
    {
        return spellCooldowns;
    }

    public void setSpellCooldowns(Map<SpellType, Float> spellCooldowns)
    {
        this.spellCooldowns = spellCooldowns;
    }
    
    public void fillCooldownMap(){

        for(SpellType spell : spells){
            spellCooldowns.put(spell, 0f);
        }
    }
    
    public void reduceCooldown(SpellType spellType, float delta){
        spellCooldowns.put(spellType, spellCooldowns.get(spellType) - delta);
    }
        
    public float getGlobalCooldownTime() {
        return globalCooldownTime;
    }

    public void setGlobalCooldownTime(float globalCooldownTime) {
        this.globalCooldownTime = globalCooldownTime;
    }

    public float getCooldownTimeLeft() {
        return cooldownTimeLeft;
    }

    public void setCooldownTimeLeft(float cooldownTimeLeft) {
        this.cooldownTimeLeft = cooldownTimeLeft;
    }

        public void reduceCooldownTimeLeft(float delta) {
        this.cooldownTimeLeft -= delta;
    }
    
    public List<SpellType> getSpells() {
        return spells;
    }

    public void addToSpellBook(SpellType spell) {
        spells.add(spell);
    }

    public void setSpells(ArrayList<SpellType> spells) {
        this.spells = spells;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public SpellType getChosenSpell() {
        return chosenSpell;
    }

    public void setChosenSpell(SpellType chosenSpell) {
        this.chosenSpell = chosenSpell;
    }

}
