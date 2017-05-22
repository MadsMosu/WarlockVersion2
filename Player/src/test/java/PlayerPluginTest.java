/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import data.Entity;
import data.EntityType;
import data.GameData;
import data.World;
import services.IGamePluginService;
import player.PlayerPlugin;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class PlayerPluginTest {

    private World world;
    private GameData gameData;
    private IGamePluginService plugin;

    public PlayerPluginTest() {
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
        plugin = new PlayerPlugin();
        world = new World();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test that a player is added to the world at game start.
     */
    @Test
    public void testStart() {
        plugin.start(gameData, world);

        boolean hasPlayer = false;
        for (Entity e : world.getEntities(EntityType.PLAYER)) {
            hasPlayer = true;

        }
        assertTrue(hasPlayer);
    }

    /**
     * Test that the player is removed from the world when the plugin is
     * stopped.
     */
    @Test
    public void testStop() {
        plugin.start(gameData, world);
        plugin.stop();

        boolean hasPlayer = false;
        for (Entity e : world.getEntities(EntityType.PLAYER)) {

            hasPlayer = true;

        }
        assertFalse(hasPlayer);
    }
}
