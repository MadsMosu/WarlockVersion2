/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.collisionsystem;

import data.Entity;
import data.EntityType;
import data.componentdata.Body;
import data.componentdata.Body.Geometry;
import data.componentdata.Position;


/**
 *
 * @author jonaspedersen
 */
public class CollisionDAO
{
    public float x;
    public float y;
    public float centerX;
    public float centerY;
    public int height;
    public int width;
    public Geometry geometry;

    public CollisionDAO(Entity e){
    
        if(e.getType() != EntityType.MAP){    
        Position pos = e.get(Position.class);
        Body body = e.get(Body.class);
        x = pos.getX();
        y = pos.getY();
        height = body.getHeight();
        width = body.getWidth();
        geometry = body.getGeometry();
        centerX = x + width / 2;
        centerY = y + height / 2;
        }
    }
}
