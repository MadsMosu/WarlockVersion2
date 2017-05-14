
package data.componentdata;

import data.Entity;


public class AI {
    
    private Entity beingAttacked;
    private boolean avoidSpell = false;
    
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
}
