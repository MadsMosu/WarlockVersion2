/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

import data.util.Vector2;

/**
 *
 * @author Aleksander
 */
public class Velocity { 
    private Vector2 vector;
    private float speed;

    
     public Velocity(float x, float y)
    {
        this.vector = new Vector2(x, y);
    }

    public Velocity(Vector2 vec)
    {
        vector = vec;
    }

    public Velocity()
    {
        vector = new Vector2(0, 0);
    }

    public Vector2 getVector()
    {
        return vector;
    }



    public void setVector(Vector2 vector)
    {
        this.vector = vector;
    }
    
        public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    

}
    
    
    

