package data.componentdata;

import States.StateMachine;
import data.Entity;
import data.util.Vector2;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AI {

    private Entity currentTarget;
    private Entity[] spellToAvoid;
    private boolean avoidSpell = false;
    private StateMachine state;
    private Map<Entity, Float> allEntities = new HashMap();
    private Map<Entity, Float> EntitiesHealthInDist = new HashMap();
    private Map<Entity, List<Entity>> closeSpells = new HashMap();
    
    public Map<Entity, List<Entity>> getCloseSpells() {
        return closeSpells;
    }

    public StateMachine getState() {
        return state;
    }

    public void setState(StateMachine state) {
        this.state = state;
    }
    

    public void setCloseSpells(Map<Entity,List<Entity>> closeSpellMap){
        this.closeSpells =  closeSpellMap;
    }
    
    public boolean getAvoidSpell() {
        return avoidSpell;
    }

    public void setAvoidSpell(boolean avoidSpell) {
        this.avoidSpell = avoidSpell;
    }

    public Entity[] getSpellToAvoid() {
        return spellToAvoid;
    }

    public void setSpellToAvoid(Entity[] spellToAvoid) {
        this.spellToAvoid = spellToAvoid;
    }

    public Map<Entity, Float> getAllEntities() {
        return allEntities;
    }

    public Entity getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(Entity currentTarget) {
        this.currentTarget = currentTarget;
    }

    public Map<Entity, Float> getEntitiesHealthInDist() {
        return EntitiesHealthInDist;
    }


}
