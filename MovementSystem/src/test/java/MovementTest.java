
import States.CharacterState;
import data.Entity;
import data.EntityType;
import data.GameData;
import data.Netherworld;
import data.World;
import data.componentdata.Position;
import data.componentdata.Velocity;
import data.util.Vector2;
import services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.movementsystem.ControlProcessor;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MovementTest {

    private World world;
    private Netherworld netherworld;
    private GameData gameData;
    private IEntityProcessingService processor;

    public MovementTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        world = new World();
        netherworld = new Netherworld();
        gameData = new GameData();
        gameData.setDelta(0.1f);

        processor = new ControlProcessor();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test that the entity moves to the right position when creating a vector from 
     * a given start and end position.
     */
    @Test
    public void testEntityMove() {
        Entity entity = new Entity();
        Velocity v = new Velocity();
        Position p1 = new Position(100, 0);
        Position p2 = new Position(200, 100);
        v.setVector(new Vector2(p1, p2));
        v.setSpeed(10);
        entity.add(v);
        entity.add(p1);
        entity.setType(EntityType.PLAYER);
        entity.setCharState(CharacterState.MOVING);
        world.addEntity(entity);

        float expectedX = (float) (p1.getX() + (v.getVector().getX() * v.getSpeed() * gameData.getDelta()));
        float expectedY = (float) (p1.getY() + (v.getVector().getY() * v.getSpeed() * gameData.getDelta()));
        Position expectedPosition = new Position(expectedX, expectedY);
        
        processor.process(gameData, world, netherworld);
        float actualX = (float) entity.get(Position.class).getX();
        float actualY = (float) entity.get(Position.class).getY();
        Position actualPosition = new Position(actualX, actualY);
        
        assertTrue(expectedPosition.equals(actualPosition));
        assertTrue(actualX == p2.getX() && actualY == p2.getY());
    }

}
