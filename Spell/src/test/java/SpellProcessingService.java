
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
import dk.sdu.mmmi.cbse.spell.SpellPlugin;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class SpellProcessingService {

    private IEntityProcessingService processor;
    private GameData gameData;
    private World world;
    private Netherworld netherworld;
    private Entity player;
    private Velocity v;
    private SpellBook sb;

    public SpellProcessingService() {
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
        player.add(new Position(0, 0));
        player.add(new Body(0, 0, Body.Geometry.CIRCLE));   
        player.add(sb);
        player.add(v);
        world = new World();
        netherworld = new Netherworld();
        processor = new SpellPlugin();
        world.addEntity(player);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test the velocity of the player upon moving up
     */
    @Test
    public void testMovementVector() {

//        gameData.setMousePosition(100, 100);
//        gameData.getKeys().setKey(GameKeys.RIGHT_MOUSE, true);
//        processor.process(gameData, world, netherworld);
//
//        Vector2 expectedVec = new Vector2(100, 100);
//        Vector2 actualVec = player.get(Velocity.class).getVector();
//
//        assertTrue(expectedVec.equals(actualVec));
    }



    /**
     * Test that the right spell is chosen
     */
    @Test
    public void testChooseSpell() {
//        gameData.getKeys().setKey(GameKeys.NUM_1, true);
//        processor.process(gameData, world, netherworld);
//        for (Entity e : world.getEntities(EntityType.PLAYER)) {
//            assertTrue(e.get(SpellBook.class).getChosenSpell().equals(SpellType.FIREBALL));
//        }
//
//        gameData.getKeys().setKey(GameKeys.NUM_2, true);
//        processor.process(gameData, world, netherworld);
//        for (Entity e : world.getEntities(EntityType.PLAYER)) {
//            assertTrue(e.get(SpellBook.class).getChosenSpell().equals(SpellType.FROSTBOLT));
//        }
//
//        gameData.getKeys().setKey(GameKeys.NUM_3, true);
//        processor.process(gameData, world, netherworld);
//        for (Entity e : world.getEntities(EntityType.PLAYER)) {
//            assertTrue(e.get(SpellBook.class).getChosenSpell().equals(SpellType.TELEPORT1));
//        }
    }

    /**
     * Test that the characters state is set correctly.
     */
    @Test
    public void testShootSpell() {
////        gameData.getKeys().setKey(GameKeys.LEFT_MOUSE, true);
////        for (Entity e : world.getEntities(EntityType.PLAYER)) {
////            assertTrue(e.getCharState().equals(CharacterState.CASTING));
////        }

    }
}
