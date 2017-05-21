
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
import dk.sdu.mmmi.cbse.roundsystem.RoundPlugin;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoundTest {

    private World world;
    private Netherworld netherworld;
    private GameData gameData;
    private IEntityProcessingService processor;

    public RoundTest() {
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

        gameData.setRoundNumber(1);
        gameData.setRoundTime(60);
        gameData.setNextRoundCountdown(5);
        gameData.setMaxRounds(5);
        processor = new RoundPlugin();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test that the entity moves to the right position when creating a vector
     * from a given start and end position.
     */
//    @Test
//    public void testRoundSystem() {
//        Entity entity = new Entity();
//
//        entity.setType(EntityType.PLAYER);
//        world.addEntity(entity);
//
//        processor.process(gameData, world, netherworld);
//
//    }

}
