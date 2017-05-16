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
public class Velocity {
    private float directionX;
    private float directionY;
    private float speed;

    
    public Velocity(float directionX, float directionY, float speed){
        this.directionX = directionX;
        this.directionY = directionY;
        this.speed = speed;
    }
    
    public float getDirectionX() {
        return directionX;
    }

    public void setDirectionX(float DirectionX) {
        this.directionX = DirectionX;
    }

    public float getDirectionY() {
        return directionY;
    }

    public void setDirectionY(float DirectionY) {
        this.directionY = DirectionY;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    
    
}
