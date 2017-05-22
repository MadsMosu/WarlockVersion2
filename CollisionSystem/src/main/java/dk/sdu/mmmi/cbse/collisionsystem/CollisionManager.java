
package dk.sdu.mmmi.cbse.collisionsystem;

import data.util.Vector2;
import data.Entity;
import static data.componentdata.Body.Geometry.CIRCLE;
import static data.componentdata.Body.Geometry.RECTANGLE;
import data.componentdata.Owner;
import data.componentdata.Position;

/**
 *
 * @author jonaspedersen
 */
public class CollisionManager {
    
    public static boolean isColliding(Entity e1, Entity e2)
    {



        Owner ow1 = e1.get(Owner.class);
        Owner ow2 = e2.get(Owner.class);

        if ((ow1 != null && ow1.getID().equals(e2.getID())) || (ow2 != null && ow2.getID().equals(e1.getID())))
        {
            return false;
        }

        CollisionDAO o1 = new CollisionDAO(e1);
        CollisionDAO o2 = new CollisionDAO(e2);

        if (o1.geometry == CIRCLE)
        {
            if (o2.geometry == CIRCLE)
            {
                return collisionCircleCircle(o1, o2);
            }
            else
            {
                return collisionCircleRectangle(o1, o2);
            }
        }
        if (o1.geometry == RECTANGLE)
        {
            if (o2.geometry == CIRCLE)
            {
                return collisionCircleRectangle(o1, o2);
            }
            else
            {
                return collisionRectangleRectangle(o1, o2);
            }
        }
        return false;
    }

    private static boolean collisionCircleCircle(CollisionDAO responding, CollisionDAO other)
    {
        double dx = responding.centerX - other.centerX;
        double dy = responding.centerY - other.centerY;
        double respondingRadius = responding.height / 2;
        double otherRadius = other.height / 2;
        return Math.sqrt((dx * dx) + (dy * dy)) <= (respondingRadius + otherRadius);
    }

    public static boolean collisionCircleRectangle(CollisionDAO circ, CollisionDAO rect)
    {
        double circleDistanceX = Math.abs(rect.centerX - circ.centerX);
        double circleDistanceY = Math.abs(rect.centerY - circ.centerY);
        if (circleDistanceY >= (rect.height / 2 + circ.height / 2))
        {
            return false;
        }
        if (circleDistanceX >= (rect.width / 2 + circ.width / 2))
        {
            return false;
        }
        if (circleDistanceY < rect.height / 2)
        {
            return true;
        }
        if (circleDistanceX < rect.width / 2)
        {
            return true;
        }

        float cornerX = (float) rect.x;
        float cornerY = (float) rect.y;
        if (circ.centerX > rect.x) 
        {
            cornerX += rect.width; 
            if (circ.centerY > rect.y) 
            {
                cornerY += rect.height; 
            }

        }
        else if (circ.centerX < rect.x) 
        {
            if (circ.centerY > rect.y) 
            {
                cornerY += rect.height; 
            }
        }
        
        Vector2 cornerToCircCenter = new Vector2(new Position(cornerX, cornerY), new Position(circ.centerX, circ.centerY));

        return (cornerToCircCenter.getMagnitude() < circ.height / 2);
    }

    public static boolean collisionRectangleRectangle(CollisionDAO o1, CollisionDAO o2)
    {
        boolean xOverlap = valueInRange(o1.x, o2.x, o2.x + o2.width) || valueInRange(o2.x, o1.x, o1.x + o1.width);
        boolean yOverlap = valueInRange(o1.y, o2.y, o2.y + o2.height) || valueInRange(o2.y, o1.y, o1.y + o1.height);
        return xOverlap && yOverlap;
    }
    
    private static boolean valueInRange(double value, double start, double end)
    {
        // FIXME if more methods like these pop up around the application it should be put in a new Math class.
        return (value >= start) && (value <= end);
    }
}
