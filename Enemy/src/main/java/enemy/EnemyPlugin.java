package enemy;

import States.CharacterState;
import data.Entity;
import static data.EntityType.PLAYER;
import data.GameData;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;
import States.MovementState;
import data.ImageManager;
import data.componentdata.Body;
import data.componentdata.Position;
import java.util.ArrayList;
import java.util.List;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})
/**
 *
 * @author jcs
 */
public class EnemyPlugin implements IEntityProcessingService, IGamePluginService {

    private float directionY;
    private float directionX;
    public static final String CHARACTER_IMAGE_PATH = "assets/Characters.png";
    public static String CHARACTER_FINAL_IMAGE_PATH = "";
    private World world;
    private List<Entity> enemies;

    @Override
    public void start(GameData gameData, World world) {
        CHARACTER_FINAL_IMAGE_PATH = EnemyPlugin.class.getResource(CHARACTER_IMAGE_PATH).getPath().replace("file:", "");
        ImageManager.createImage(CHARACTER_FINAL_IMAGE_PATH, false);
        this.world = world;
        enemies = new ArrayList();

        
        enemies.add(makeEnemy());

    }

    @Override
    public void process(GameData gameData, World world) {

    }

    private Entity makeEnemy() {
        Entity enemy = new Entity();
        enemy.setType(PLAYER);

        enemy.add(ImageManager.getImage(CHARACTER_FINAL_IMAGE_PATH));
        Position pos = new Position(0.0f, 0.0f);
        enemy.add(pos);

        enemy.setMaxSpeed(2);
        enemy.setAcceleration(2);
        enemy.setDeacceleration(1);

        enemy.setRadians(3.1415f / 2);

        Body body = new Body(50, 32, Body.Geometry.RECTANGLE);
        enemy.add(body);

        enemy.setMoveState(MovementState.STANDINGRIGHT);
        enemy.setCharState(CharacterState.IDLE);
        world.addEntity(enemy);
        return enemy;
    }

    @Override
    public void stop() {
        // Remove entities
        for (Entity enemy : enemies) {
            world.removeEntity(enemy);
        }
    }

}
