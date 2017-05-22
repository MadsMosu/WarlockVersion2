
import States.CharacterState;
import States.GameState;
import data.Entity;
import data.EntityType;
import data.GameData;
import data.Netherworld;
import data.World;
import data.componentdata.Position;
import data.componentdata.Score;
import data.componentdata.Velocity;
import data.util.Vector2;
import services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.roundsystem.RoundPlugin;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoundTest {

    private World world;
    private Netherworld netherworld;
    private GameData gameData;
    private IEntityProcessingService processor;
    private Entity entity1, entity2;

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

        entity1 = new Entity();
        entity2 = new Entity();
        entity1.setType(EntityType.PLAYER);
        entity2.setType(EntityType.ENEMY);
        world.addEntity(entity1);
        world.addEntity(entity2);
    }

    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void testStateRun() {
        gameData.setGameState(GameState.RUN);

        processor.process(gameData, world, netherworld);
        assertTrue(gameData.getGameState().equals(GameState.RUN));

    }

    @Test
    public void testRoundTimeOver() {
        gameData.setRoundTime(0);
        gameData.setGameState(GameState.RUN);
        processor.process(gameData, world, netherworld);
        assertTrue(gameData.getGameState().equals(GameState.ROUNDEND));

        gameData.setRoundTime(60);
    }

    @Test
    public void testOneCharacterLeft() {
        gameData.setGameState(GameState.RUN);
        world.removeEntity(entity2);
        netherworld.addEntity(entity2);
        entity1.add(new Score());
        processor.process(gameData, world, netherworld);
        assertTrue(gameData.getGameState().equals(GameState.ROUNDEND));
        assertTrue(entity1.get(Score.class).getRoundsWon() > 0);

    }

    @Test
    public void testGamePause() {
        gameData.setGameState(GameState.ROUNDEND);
        for (Entity e : world.getEntities()) {
            netherworld.addEntity(e);
            world.removeEntity(e);
        }
        processor.process(gameData, world, netherworld);
        assertTrue(gameData.getGameState().equals(GameState.PAUSE));

    }

    @Test
    public void testStartNextRoundFalse() {
        gameData.setGameState(GameState.PAUSE);
        gameData.setNextRoundCountdown(4);
        processor.process(gameData, world, netherworld);
        assertFalse(gameData.getGameState().equals(GameState.RUN));

    }

    @Test
    public void testStartNextRoundTrue() {
        gameData.setGameState(GameState.PAUSE);
        gameData.setNextRoundCountdown(0);
        processor.process(gameData, world, netherworld);
        assertTrue(gameData.getGameState().equals(GameState.RUN));
        assertTrue(gameData.getRoundNumber() > 1);

    }

}
