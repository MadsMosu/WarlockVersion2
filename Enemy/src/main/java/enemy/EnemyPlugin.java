package enemy;

import States.CharacterState;
import data.Entity;
import data.GameData;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;
import States.MovementState;
import static data.EntityType.ENEMY;
import static data.GameKeys.LEFT_MOUSE;
import data.ImageManager;
import data.componentdata.AI;
import data.componentdata.Body;
import data.componentdata.Health;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.Velocity;
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
    public static final String ENEMY_IMAGE_PATH = "assets/enemysprites.png";
    public static String ENEMY_FINAL_IMAGE_PATH = "";
    private World world;
    private List<Entity> enemies;
    private Entity enemy;
    private float[] shapex = new float[4];
    private float[] shapey = new float[4];

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
    public void process(GameData gameData, World world) {

        for (Entity e : world.getEntities(ENEMY)) {
            if (e.getCharState().equals(CharacterState.DEAD)) {
                world.removeEntity(e);
            }
            setShape(e);

            handleShoot(e, gameData);
        }
    }

    private void handleShoot(Entity e, GameData gameData) {
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
        setShape(enemy);
        world.addEntity(enemy);
        return enemy;
    }

    private void setShape(Entity ent) {
        float height = ent.get(Body.class).getHeight();
        float width = ent.get(Body.class).getWidth();
        float enemyX = ent.get(Position.class).getX();
        float enemyY = ent.get(Position.class).getY();

        shapex[0] = (float) (enemyX);
        shapey[0] = (float) (enemyY);

        shapex[1] = (float) (enemyX + width);
        shapey[1] = (float) (enemyY);

        shapex[2] = (float) (enemyX + width);
        shapey[2] = (float) (enemyY + height);

        shapex[3] = (float) (enemyX);
        shapey[3] = (float) (enemyY + height);

        ent.setShapeX(shapex);
        ent.setShapeY(shapey);
    }

    @Override
    public void stop() {
        // Remove entities
        for (Entity enemy : enemies) {

            world.removeEntity(enemy);
        }
    }

}
