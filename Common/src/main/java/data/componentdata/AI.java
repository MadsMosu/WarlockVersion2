package data.componentdata;

import States.StateMachine;
import data.Entity;
import data.util.Vector2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AI {

    private Entity currentAttackTarget, currentChasingTarget;
    private Entity[] spellToAvoid;
    private boolean avoidSpell = false;
    private boolean isWandering = false;
    private StateMachine state;
    private Map<Entity, Float> allEntities = new HashMap();
    private Map<Entity, Float> attackTargetsHealthInDist = new HashMap();
    private Map<Entity, Float> chaseTargetsHealthInDist = new HashMap();
    private Map<Entity, List<Entity>> closeSpells = new HashMap();
    private List<Entity> attackTargetsInRange = new ArrayList();
    private List<Entity> chasingTargetsInRange = new ArrayList();
    
    public Map<Entity, List<Entity>> getCloseSpells() {
        return closeSpells;
    }

    public boolean isIsWandering()
    {
        return isWandering;
    }

    public List<Entity> getChasingTargetsInRange()
    {
        return chasingTargetsInRange;
    }

    public void setChasingTargetsInRange(List<Entity> chasingTargetsInRange)
    {
        this.chasingTargetsInRange = chasingTargetsInRange;
    }

    
    
    public void setIsWandering(boolean isWandering)
    {
        this.isWandering = isWandering;
    }

    public Entity getCurrentChasingTarget()
    {
        return currentChasingTarget;
    }

    public void setCurrentChasingTarget(Entity currentChasingTarget)
    {
        this.currentChasingTarget = currentChasingTarget;
    }
    
    

    public List<Entity> getAttackTargetsInRange()
    {
        return attackTargetsInRange;
    }

    public void setEntitiesInRange(List<Entity> entitiesInRange)
    {
        this.attackTargetsInRange = entitiesInRange;
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

    public Entity getCurrentAttackTarget() {
        return currentAttackTarget;
    }

    public void setCurrentAttackTarget(Entity currentTarget) {
        this.currentAttackTarget = currentTarget;
    }

    public Map<Entity, Float> getAttackTargetsHealthInDist() {
        return attackTargetsHealthInDist;
    }
    
    public Map<Entity, Float> getChaseTargetsHealthInDist(){
        return chaseTargetsHealthInDist;
    }


}
