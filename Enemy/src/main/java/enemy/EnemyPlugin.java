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
import static data.EntityType.ENEMY;
import data.ImageManager;
import data.Netherworld;
import data.componentdata.AI;
import data.componentdata.Body;
import data.componentdata.DamageTaken;
import data.componentdata.Health;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.Velocity;
import data.util.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})
public class EnemyPlugin implements IEntityProcessingService, IGamePluginService {

    public static final String ENEMY_IMAGE_PATH = "assets/enemysprites.png";
    public static String ENEMY_FINAL_IMAGE_PATH = "";
    private World world;
    private List<Entity> enemies;
    private Entity enemy;

    @Override
    public void start(GameData gameData, World world) {
        ENEMY_FINAL_IMAGE_PATH = EnemyPlugin.class.getResource(ENEMY_IMAGE_PATH).getPath().replace("file:", "");
        ImageManager.createImage(ENEMY_FINAL_IMAGE_PATH, false);
        this.world = world;
        enemies = new ArrayList();

        enemies.add(makeEnemy(3000, 0));
        enemies.add(makeEnemy(3600, 0));

    }

    @Override
    public void process(GameData gameData, World world, Netherworld netherworld) {

        for (Entity e : world.getEntities(ENEMY)) {
            if (e.getCharState().equals(CharacterState.DEAD)) {
                world.removeEntity(e);
                netherworld.addEntity(e);
            }

            handleShoot(e);
            handleMovement(e, gameData);
        }
        if (gameData.getGameState().equals(GameState.ROUNDEND)) {
            for (Entity e : netherworld.getEntities(ENEMY)) {
                resetPosition(e);
            }
        }
    }

    private void resetPosition(Entity enemy) {
        Random rand = new Random();
        int randX = rand.nextInt(4000) + 2500;
        int randY = rand.nextInt(700);
        Position p = enemy.get(Position.class);
        p.setPosition(randX, randY);
    }

    private void handleShoot(Entity e) {
        if (e.get(SpellBook.class).getChosenSpell() != null) {
            e.setMoveState(MovementState.STANDING);
            e.setCharState(CharacterState.CASTING);
        }
    }

    private Entity makeEnemy(float xPosition, float yPosition) {
        enemy = new Entity();
        enemy.setType(ENEMY);

        Position pos = new Position(xPosition, yPosition);
        Health health = new Health(100);
        SpellBook sb = new SpellBook(new Owner(enemy.getID()));
        Owner ow = new Owner(enemy.getID());
        AI ai = new AI();
        Velocity v = new Velocity();
        v.setSpeed(50);
        sb.setCooldownTimeLeft(sb.getGlobalCooldownTime());
        enemy.add(ow);
        enemy.add(ImageManager.getImage(ENEMY_FINAL_IMAGE_PATH));
        enemy.add(health);
        enemy.add(pos);
        enemy.add(sb);
        enemy.add(ai);

        enemy.add(v);

        Body body = new Body(50, 50, Body.Geometry.RECTANGLE);
        enemy.add(body);

        enemy.setMoveState(MovementState.STANDING);
        enemy.setCharState(CharacterState.IDLE);
        world.addEntity(enemy);
        return enemy;
    }

    private void handleMovement(Entity e, GameData gameData) {
        AI aiComp = e.get(AI.class);
        Position p = e.get(Position.class);
        Velocity v = e.get(Velocity.class);
        
        if(e.getCharState().equals(CharacterState.BOUNCING)){
            Vector2 stopCheck = new Vector2(p, p.getStartPosition());
            if (v.getTravelDist() <= stopCheck.getMagnitude()) {

                e.setCharState(CharacterState.IDLE);
                e.setMoveState(MovementState.STANDING);
            }
        } else if (aiComp.getCurrentTarget() != null) {
            Position aiPosition = new Position(p);
            Position entityPosition = new Position(aiComp.getCurrentTarget().get(Position.class));
            Vector2 direction = new Vector2(aiPosition, entityPosition);
            float gap = direction.getMagnitude();
            v.setVector(direction);
            v.getVector().normalize();

            if (p.isInLava()) {
                Position middle = new Position(gameData.getMapWidth() / 2, gameData.getMapHeight() / 2);
                Vector2 directionToMiddle = new Vector2(aiPosition, middle);
                float distanceToMiddle = directionToMiddle.getMagnitude();

                e.setAngle((float) directionToMiddle.getAngle());
                e.setRunningState(e.getAngle(), e);

                v.setVector(directionToMiddle);
                v.getVector().normalize();
                if (e.getCharState().equals(CharacterState.IDLE)) {
                    e.setCharState(CharacterState.MOVING);
                }
            }  else {
                if (gap >= 100) {
                    if (gap >= 100 && gap < 101) {
                        e.setMoveState(MovementState.STANDING);
                    } else {
                        e.setAngle(direction.getAngle());
                        e.setRunningState(e.getAngle(), e);
                        if (e.getCharState().equals(CharacterState.IDLE)) {
                            e.setCharState(CharacterState.MOVING);
                        }

                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        for (Entity enemy : enemies) {
            world.removeEntity(enemy);
        }
    }

}
