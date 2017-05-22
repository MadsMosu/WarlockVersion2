/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import States.CharacterState;
import data.Entity;
import data.EntityType;
import data.GameData;
import data.GameKeys;
import data.Netherworld;
import data.SpellType;
import data.World;
import data.componentdata.Body;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.Velocity;
import data.componentdata.SpellBook;
import data.util.Vector2;
import services.IEntityProcessingService;
import player.PlayerPlugin;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Morten
 */
public class PlayerProcessingTest {

    private IEntityProcessingService processor;
    private GameData gameData;
    private World world;
    private Netherworld netherworld;
    private Entity player;
    private Velocity v;
    private SpellBook sb;

    public PlayerProcessingTest() {
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
        player = new Entity();
        sb = new SpellBook(new Owner(player.getID()));
        v = new Velocity();

        player.add(new Position(gameData.getMapWidth() / 2, 0));
        player.add(new Body(0, 0, Body.Geometry.CIRCLE));
        sb.addToSpellBook(SpellType.FIREBALL);
        sb.addToSpellBook(SpellType.FROSTBOLT);
        sb.addToSpellBook(SpellType.TELEPORT1);
        player.add(sb);
        player.add(v);
        world = new World();
        netherworld = new Netherworld();
        processor = new PlayerPlugin();
        world.addEntity(player);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test the velocity of the player upon moving up
     */
    @Test
    public void testMoveUp() {

//        Vector2 expectedVec = new Vector2(0, 1);
//        Vector2 actualVec = player.get(Velocity.class).getVector().normalize();
//
//        assertTrue(expectedVec.equals(actualVec));
    }

    /**
     * Test the velocity of the player upon moving down
     */
    @Test
    public void testMoveDown() {

    }

    /**
     * Test the velocity of the player upon moving left
     */
    @Test
    public void testMoveLeft() {
    }

    /**
     * Test the velocity of the player upon moving right
     */
    @Test
    public void testMoveRight() {

    }

    /**
     * Test that player's weapon is attacking when the action SHOOT is true
     */
    @Test
    public void testChooseSpell() {
        gameData.getKeys().setKey(GameKeys.NUM_1, true);
        processor.process(gameData, world, netherworld);
        for (Entity e : world.getEntities(EntityType.PLAYER)) {
            assertTrue(e.get(SpellBook.class).getChosenSpell().equals(SpellType.FIREBALL));
        }

        gameData.getKeys().setKey(GameKeys.NUM_2, true);
        processor.process(gameData, world, netherworld);
        for (Entity e : world.getEntities(EntityType.PLAYER)) {
            assertTrue(e.get(SpellBook.class).getChosenSpell().equals(SpellType.FROSTBOLT));
        }

        gameData.getKeys().setKey(GameKeys.NUM_3, true);
        processor.process(gameData, world, netherworld);
        for (Entity e : world.getEntities(EntityType.PLAYER)) {
            assertTrue(e.get(SpellBook.class).getChosenSpell().equals(SpellType.TELEPORT1));
        }
    }

    /**
     * Test that weapon is reloading when the action RELOAD is true
     */
    @Test
    public void testShootSpell() {
        gameData.getKeys().setKey(GameKeys.LEFT_MOUSE, true);
        for (Entity e : world.getEntities(EntityType.PLAYER)) {
            assertTrue(e.getCharState().equals(CharacterState.CASTING));
        }

    }
}
