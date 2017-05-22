/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;


public class Body
{
    private int height;
    private int width;
    private int spriteHeight;
    private int spriteWidth;
    private int frames;
    private float frameSpeed;
    private Geometry geometry;

    public enum Geometry
    {
        CIRCLE, RECTANGLE
    }

    public Body(int height, int width, Geometry geometry)
    {
        this.height = height;
        this.width = width;
        this.geometry = geometry;
    }
    
    public Position getCenter(float posx, float posy){
        return new Position(posx + (width/2), posy + (height/2));
    }

    public Geometry getGeometry()
    {
        return geometry;
    }

    public float getFrameSpeed()
    {
        return frameSpeed;
    }

    public void setFrameSpeed(float frameSpeed)
    {
        this.frameSpeed = frameSpeed;
    }

    
    public int getFrames()
    {
        return frames;
    }

    public void setFrames(int frames)
    {
        this.frames = frames;
    }
    

    public void setGeometry(Geometry geometry)
    {
        this.geometry = geometry;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getSpriteHeight()
    {
        return spriteHeight;
    }

    public void setSpriteHeight(int spriteHeight)
    {
        this.spriteHeight = spriteHeight;
    }
    
    public void setSpriteSize(int width, int height){
        this.spriteWidth = width;
        this.spriteHeight = height;
    }

    public int getSpriteWidth()
    {
        return spriteWidth;
    }

    public void setSpriteWidth(int spriteWidth)
    {
        this.spriteWidth = spriteWidth;
    }
    
    
}
