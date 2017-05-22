package enemy;

import States.CharacterState;
import States.GameState;
import data.Entity;
import data.GameData;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;
import States.MovementState;
import States.StateMachine;
import static data.EntityType.ENEMY;
import data.ImageManager;
import data.Netherworld;
import data.componentdata.AI;
import data.componentdata.Body;
import data.componentdata.Health;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.Score;
import data.componentdata.SpellBook;
import data.componentdata.Velocity;
import data.util.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)
})
public class EnemyPlugin implements IEntityProcessingService, IGamePluginService {

    public static final String ENEMY_IMAGE_PATH = "assets/enemysprites.png";
    public static String ENEMY_FINAL_IMAGE_PATH = "";
    private World world;
    private List<Entity> enemies;

    @Override
    public void start(GameData gameData, World world)
    {
        ENEMY_FINAL_IMAGE_PATH = EnemyPlugin.class.getResource(ENEMY_IMAGE_PATH).getPath().replace("file:", "");
        ImageManager.createImage(ENEMY_FINAL_IMAGE_PATH, false);
        this.world = world;
        enemies = new ArrayList();
 
        enemies.add(makeEnemy(gameData));
        enemies.add(makeEnemy(gameData));
        enemies.add(makeEnemy(gameData));
        enemies.add(makeEnemy(gameData));
    }

    @Override
    public void process(GameData gameData, World world, Netherworld netherworld)
    {

        for (Entity e : world.getEntities(ENEMY)) {
            if (e.getCharState() == CharacterState.DEAD || gameData.getGameState().equals(GameState.ROUNDEND)) {
                netherworld.addEntity(e);
                world.removeEntity(e);

            }
            handleShoot(e);
        }
        for (Entity e : netherworld.getEntities(ENEMY)) {
            if (gameData.getGameState().equals(GameState.PAUSE)) {
                resetPosition(e, gameData);
                e.get(Health.class).setHp(e.get(Health.class).getMaxHp());
            } else if (gameData.getGameState().equals(GameState.RUN) && gameData.getRoundTime() >= 58) {
                world.addEntity(e);
                netherworld.removeEntity(e);
            }

        }
    }

    private void resetPosition(Entity enemy, GameData gameData)
    {
        Random rand = new Random();
        int randX = rand.nextInt(500) + gameData.getMapWidth() / 2;
        int randY = rand.nextInt(500);
        Position p = enemy.get(Position.class);
        p.setPosition(randX, randY);
    }

    private void handleShoot(Entity e)
    {
        if (e.get(SpellBook.class).getChosenSpell() != null && !e.getCharState().equals(CharacterState.BOUNCING)) {
            e.setMoveState(MovementState.STANDING);
            e.setCharState(CharacterState.CASTING);
        }
    }

    private Entity makeEnemy(GameData gameData)
    {
        Entity enemy = new Entity();
        enemy.setType(ENEMY);
        
        Random rand = new Random();
        int randX = rand.nextInt(500) + gameData.getMapWidth() / 2;
        int randY = rand.nextInt(500);

        Position pos = new Position(randX, randY);
        Health health = new Health(100);
        SpellBook sb = new SpellBook(new Owner(enemy.getID()));
        Owner ow = new Owner(enemy.getID());
        AI ai = new AI();
        Velocity v = new Velocity();
        Body body = new Body(50, 50, Body.Geometry.RECTANGLE);
        Score score = new Score();
        v.setSpeed(100);
        sb.setCooldownTimeLeft(sb.getGlobalCooldownTime());
        enemy.add(ow);
        enemy.add(ImageManager.getImage(ENEMY_FINAL_IMAGE_PATH));
        enemy.add(health);
        enemy.add(pos);
        enemy.add(sb);
        enemy.add(ai);
        enemy.add(v);
        enemy.add(body);
        enemy.add(score);

        enemy.setMoveState(MovementState.STANDING);
        enemy.setCharState(CharacterState.IDLE);
        world.addEntity(enemy);
        return enemy;
    }

    @Override
    public void stop()
    {
        for (Entity enemy : enemies) {
            world.removeEntity(enemy);
        }
    }

}
