package data.componentdata;

import States.StateMachine;
import data.Entity;
import data.util.Vector2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AI {

    private Entity currentTarget;
    private Entity[] spellToAvoid;
    private boolean avoidSpell = false;
    private boolean isWandering = false;
    private StateMachine state;
    private Map<Entity, Float> allEntities = new HashMap();
    private Map<Entity, Float> EntitiesHealthInDist = new HashMap();
    private Map<Entity, List<Entity>> closeSpells = new HashMap();
    private List<Entity> entitiesInRange = new ArrayList();
    
    public Map<Entity, List<Entity>> getCloseSpells() {
        return closeSpells;
    }

    public boolean isIsWandering()
    {
        return isWandering;
    }

    public void setIsWandering(boolean isWandering)
    {
        this.isWandering = isWandering;
    }
    
    

    public List<Entity> getEntitiesInRange()
    {
        return entitiesInRange;
    }

    public void setEntitiesInRange(List<Entity> entitiesInRange)
    {
        this.entitiesInRange = entitiesInRange;
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
