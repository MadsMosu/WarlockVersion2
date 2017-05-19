/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

import data.Entity;
import data.EntityType;

/**
 *
 * @author Aleksander
 */
public class Owner {
    private String ID;
    private EntityType ownerType;
    private Entity ownerEntity;

    public Owner(String ownerId)
    {
        ID = ownerId;
    }

    public String getID()
    {
        return ID;
    }

    public void setID(String ID)
    {
        this.ID = ID;
    }

    public EntityType getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(EntityType ownerType) {
        this.ownerType = ownerType;
    }

    public Entity getOwnerEntity() {
        return ownerEntity;
    }

    public void setOwnerEntity(Entity ownerEntity) {
        this.ownerEntity = ownerEntity;
    }
    
}



