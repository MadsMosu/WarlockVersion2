
import States.CharacterState;
import data.Entity;
import data.EntityType;
import data.GameData;
import data.Netherworld;
import data.World;
import data.componentdata.Damage;
import data.componentdata.DamageTaken;
import data.componentdata.Health;
import data.componentdata.Owner;
import services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.healthsystem.HealthPlugin;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class HealthProcessorTest {

    IEntityProcessingService processor;
    World world;
    Netherworld netherworld;
    GameData gameData;
    Entity entity1, entity2;
    Health hp;
    DamageTaken dt;

    public HealthProcessorTest() {
    }

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

        world = new World();
        netherworld = new Netherworld();
        gameData = new GameData();
        gameData.setDelta(0.1f);
        processor = new HealthPlugin();

        entity1.setType(EntityType.PLAYER);
        world.addEntity(entity1);
        hp = new Health(100);
        entity1.add(hp);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test that hp is removed when an entity takes damage.
     */
    @Test
    public void testDamageTaken() {

        int startHealth = 100;
        hp.setHp(startHealth);
        dt = new DamageTaken(new Damage(10), new Owner(entity2.getID()));
        hp.addDamageTaken(dt);

        processor.process(gameData, world, netherworld);
        assertTrue(entity1.get(Health.class).getHp() < startHealth);
    }

    /**
     * Test that an entity dies when his hp drops to 0.
     */
    @Test
    public void testPlayerDead() {
        hp.setHp(100);
        dt = new DamageTaken(new Damage(110), new Owner(entity2.getID()));
        hp.addDamageTaken(dt);

        processor.process(gameData, world, netherworld);
        assertTrue(entity1.getCharState() == CharacterState.DEAD);
    }
}
