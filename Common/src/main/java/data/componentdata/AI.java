package data.componentdata;

import data.Entity;
import java.util.HashMap;
import java.util.Map;

public class AI {

    private Entity currentTarget;
    private Entity spellToAvoid;
    private boolean avoidSpell = false;
    private Map<Entity, Float> allEntities = new HashMap();
    private Map<Entity, Float> EntitiesHealthInDist = new HashMap();
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

    public Map<Entity, Float> getEntitiesHealthInDist() {
        return EntitiesHealthInDist;
    }


}
