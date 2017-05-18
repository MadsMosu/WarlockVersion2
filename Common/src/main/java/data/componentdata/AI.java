package data.componentdata;

import States.AiStateMachine;
import data.Entity;
import java.util.HashMap;
import java.util.Map;

public class AI {

    private Entity currentTarget;
    private Entity spellToAvoid;
    private Entity chasedBy;
    private boolean avoidSpell = false;
    private AiStateMachine state;
    private Map<Entity, Float> allEntities = new HashMap();
    private Map<Entity, Double> allEntitiesHealth = new HashMap();
    private Map<Entity, Float> closeSpells = new HashMap();

    public Map<Entity, Float> getCloseSpells() {
        return closeSpells;
    }

    public boolean getAvoidSpell() {
        return avoidSpell;
    }

    public void setAvoidSpell(boolean avoidSpell) {
        this.avoidSpell = avoidSpell;
    }

    public Entity getSpellToAvoid() {
        return spellToAvoid;
    }

    public void setSpellToAvoid(Entity spellToAvoid) {
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


    public AiStateMachine getState() {
        return state;
    }

    public void setState(AiStateMachine state) {
        this.state = state;
    }

    public Map<Entity, Double> getAllEntitiesHealth() {
        return allEntitiesHealth;
    }


    public Entity getChasedBy() {
        return chasedBy;
    }

    public void setChasedBy(Entity chasedBy) {
        this.chasedBy = chasedBy;
    }



    

    



}
