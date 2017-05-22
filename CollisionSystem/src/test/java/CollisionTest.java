
import dk.sdu.mmmi.cbse.collisionsystem.CollisionManager;
import data.Entity;
import data.componentdata.Body;
import data.componentdata.Position;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CollisionTest {

    Entity entity1, entity2;
    Position pos1, pos2;
    Body b1, b2;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

        entity1 = new Entity();
        entity2 = new Entity();

        pos1 = new Position(0, 0);
        pos2 = new Position(0, 0);

        b1 = new Body(0, 0, Body.Geometry.RECTANGLE);
        b2 = new Body(0, 0, Body.Geometry.CIRCLE);

        entity1.add(pos1);
        entity1.add(b1);
        entity2.add(pos2);
        entity2.add(b2);

    }

    @After
    public void tearDown() {
    }

    /**
     * Collision test of a rectangle and a circle.
     */
    @Test
    public void testCircleRectangleCollision() {
        b1.setGeometry(Body.Geometry.CIRCLE); b1.setHeight(15); b1.setWidth(15);
        b2.setGeometry(Body.Geometry.RECTANGLE); b2.setHeight(50); b2.setWidth(50);

        pos1.setX(100);
        pos1.setY(100);
        pos2.setX(100);
        pos2.setY(70);

        assertTrue(CollisionManager.isColliding(entity1, entity2));
        
        pos1.setX(100);
        pos1.setY(100);
        pos2.setX(100);
        pos2.setY(50);
        assertFalse(CollisionManager.isColliding(entity1, entity2));
    }

    /**
     * Collision test of 2 circles .
     */
    @Test
    public void testCircleCircleCollision() {
        b1.setGeometry(Body.Geometry.CIRCLE); b1.setHeight(15); b1.setWidth(15);
        b2.setGeometry(Body.Geometry.RECTANGLE); b2.setHeight(15); b2.setWidth(15);

        pos1.setX(100);
        pos1.setY(100);
        pos2.setX(100);
        pos2.setY(90);

        assertTrue(CollisionManager.isColliding(entity1, entity2));
        
        pos1.setX(100);
        pos1.setY(100);
        pos2.setX(100);
        pos2.setY(80);

        assertFalse(CollisionManager.isColliding(entity1, entity2));
    }

    /**
     * Test collision between 2 rectangles
     * */
    @Test
    public void testRectangleRectangleCollision() {
        b1.setGeometry(Body.Geometry.CIRCLE); b1.setHeight(50); b1.setWidth(50);
        b2.setGeometry(Body.Geometry.RECTANGLE); b2.setHeight(50); b2.setWidth(50);

        pos1.setX(100);
        pos1.setY(100);
        pos2.setX(100);
        pos2.setY(60);

        assertTrue(CollisionManager.isColliding(entity1, entity2));
        
        pos1.setX(100);
        pos1.setY(100);
        pos2.setX(100);
        pos2.setY(40);

        assertFalse(CollisionManager.isColliding(entity1, entity2));
    }

   
}
