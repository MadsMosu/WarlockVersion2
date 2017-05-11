package player;

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
import com.badlogic.gdx.math.Rectangle;
import data.ImageManager;
import data.componentdata.Body;
import data.componentdata.Body.Geometry;
import data.componentdata.Image;
import data.componentdata.Position;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)
})
/**
 *
 * @author jcs
 */
public class PlayerPlugin implements IEntityProcessingService, IGamePluginService {

    private float[] shapex = new float[4];
    private float[] shapey = new float[4];
    private float directionY;
    private float directionX;
    private Entity player;
    public static final String CHARACTER_IMAGE_PATH = "assets/Characters.png";
    public static String CHARACTER_FINAL_IMAGE_PATH = "";
    private World world;
    private Rectangle r;        
    
    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        CHARACTER_FINAL_IMAGE_PATH = PlayerPlugin.class.getResource(CHARACTER_IMAGE_PATH).getPath().replace("file:", "");
        ImageManager.createImage(CHARACTER_FINAL_IMAGE_PATH, false);
        this.world = world;
        player = new Entity();
        player.setType(PLAYER);

	player.add(ImageManager.getImage(CHARACTER_FINAL_IMAGE_PATH));
        
        Position pos = new Position(0.0f, 0.0f);
        player.add(pos);

        player.setMaxSpeed(2);
        player.setAcceleration(2);
        player.setDeacceleration(1);

        player.setRadians(3.1415f / 2);
        
        Body body = new Body(50, 32, Geometry.RECTANGLE);
        player.add(body);
        
        
        player.setMoveState(MovementState.STANDINGRIGHT);
        player.setCharState(CharacterState.IDLE);
        world.addEntity(player);
        
        
        
    }

    @Override
    public void process(GameData gameData, World world) {
        // TODO: Implement entity processor


//        for (Entity p : world.getEntities(PLAYER)) {
//            
//            float x = p.getX();
//            float y = p.getY();
//
//
//            setShape(x, y, p.getRadians());
//            p.setShapeX(shapex);
//            p.setShapeY(shapey);
//
//        }

    }

    private void setShape(float x, float y, float radians) {
        shapex[0] = (float) (x + Math.cos(radians) * 32);
        shapey[0] = (float) (y + Math.sin(radians) * 32);

        shapex[1] = (float) (x + Math.cos(radians - 16 * 3.1415f / 20) * 32);
        shapey[1] = (float) (y + Math.sin(radians - 16 * 3.1145f / 20) * 32);

        shapex[2] = (float) (x + Math.cos(radians + 3.1415f) * 20);
        shapey[2] = (float) (y + Math.sin(radians + 3.1415f) * 20);

        shapex[3] = (float) (x + Math.cos(radians + 16 * 3.1415f / 20) * 32);
        shapey[3] = (float) (y + Math.sin(radians + 16 * 3.1415f / 20) * 32);

    }

    

    @Override
    public void stop() {
        // Remove entities
        world.removeEntity(player);
    }

}
