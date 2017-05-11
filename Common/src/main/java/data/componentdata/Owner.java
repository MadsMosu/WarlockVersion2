/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

/**
 *
 * @author Aleksander
 */
public class Owner {
    private String ID;

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
}



