
import data.Entity;
import data.EntityType;
import data.GameData;
import data.World;
import services.IGamePluginService;
import enemy.EnemyPlugin;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EnemyPluginTest {

   private World world;
    private GameData gameData;
    private IGamePluginService plugin;
    private boolean hasEnemy;

    public EnemyPluginTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        gameData = new GameData();
        plugin = new EnemyPlugin();
        world = new World();
        hasEnemy = false;
    }

    @After
    public void tearDown() {
    }

    /**
     * Test that an enemy is added to the world at game start.
     */
    @Test
    public void testStart() {
        plugin.start(gameData, world);

        for (Entity e : world.getEntities(EntityType.ENEMY)) {
            hasEnemy = true;
        }
        assertTrue(hasEnemy);
    }

    /**
     * Test that the player is removed from the world when the plugin is
     * stopped.
     */
    @Test
    public void testStop() {
        plugin.start(gameData, world);
        plugin.stop();

        for (Entity e : world.getEntities(EntityType.ENEMY)) {
            hasEnemy = true;
        }
        assertFalse(hasEnemy);
    }
}
