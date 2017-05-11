/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;

import java.io.Serializable;

/**
 *
 * @author Aleksander
 */
public class Position implements Serializable{

    
    private float x;
    private float y;

    public Position(Position pos) {
        x = pos.getX();
        y = pos.getY();
    }

    public Position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

        @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Position other = (Position)obj;
        if (x != other.getX() || y != other.getY())
        {
            return false;
        }
        return true;
    }
    
}
