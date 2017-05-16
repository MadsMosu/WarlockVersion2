
package data.componentdata;

import data.Entity;
import java.util.HashMap;
import java.util.Map;


public class AI {
    
    private Entity beingAttacked;
    private Entity closestEntity;
    private Entity spellToAvoid;
    private boolean avoidSpell = false;
    private Map<Entity, Float> AllEntities = new HashMap();

    public Map<Entity, Float> getAllEntities() {
        return AllEntities;
    }
    
    public void isAttackingWho(Entity entity){
        this.beingAttacked = entity;
    }
    
    public Entity getBeingAttacked(){
        return beingAttacked;
    }
    
    public boolean getAvoidSpell(){
        return avoidSpell;
    }
    
    public void setAvoidSpell(boolean avoidSpell){
        this.avoidSpell = avoidSpell;
    }

    public Entity getClosestEntity() {
        return closestEntity;
    }

    public void setClosestEntity(Entity closestEntity) {
        this.closestEntity = closestEntity;
    }

    public Entity getSpellToAvoid() {
        return spellToAvoid;
    }

    public void setSpellToAvoid(Entity spellToAvoid) {
        this.spellToAvoid = spellToAvoid;
    }
}
