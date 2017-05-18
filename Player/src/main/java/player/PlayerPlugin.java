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
import data.ImageManager;
import data.componentdata.Body;
import data.componentdata.Body.Geometry;
import data.componentdata.Health;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.Velocity;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})

public class PlayerPlugin implements IEntityProcessingService, IGamePluginService {

    private float[] shapex = new float[4];
    private float[] shapey = new float[4];
    private Entity player;
    public static final String CHARACTER_IMAGE_PATH = "assets/Characters.png";
    public static String CHARACTER_FINAL_IMAGE_PATH = "";
    private World world;

    @Override
    public void start(GameData gameData, World world) {
        CHARACTER_FINAL_IMAGE_PATH = PlayerPlugin.class.getResource(CHARACTER_IMAGE_PATH).getPath().replace("file:", "");
        ImageManager.createImage(CHARACTER_FINAL_IMAGE_PATH, false);
        this.world = world;
        createPlayer();
    }

    @Override
    public void process(GameData gameData, World world) {

        for(Entity p : world.getEntities(PLAYER)){
        setShape();
        
        }       
        if (player.getCharState() == CharacterState.DEAD) {
            stop();
        }
    }

    private void createPlayer() {
        player = new Entity();
        player.setType(PLAYER);
        Position pos = new Position(3200, 0);
        Health health = new Health(100);
        Owner ow = new Owner(player.getID());
        SpellBook sb = new SpellBook(new Owner(player.getID()));
        Velocity v = new Velocity();
        v.setSpeed(100);
        player.add(ow);
        player.add(ImageManager.getImage(CHARACTER_FINAL_IMAGE_PATH));
        player.add(health);
        player.add(pos);
        player.add(sb);
        player.add(v);

        
        Body body = new Body(50, 50, Geometry.RECTANGLE);
        player.add(body);
        player.setMoveState(MovementState.STANDING);
        player.setCharState(CharacterState.IDLE);
        world.addEntity(player);

    }

    private void setShape() {
        float height = player.get(Body.class).getHeight();
        float width = player.get(Body.class).getWidth();
        float playerX = player.get(Position.class).getX();
        float playerY = player.get(Position.class).getY();

        shapex[0] = (float) (playerX);
        shapey[0] = (float) (playerY);

        shapex[1] = (float) (playerX + width);
        shapey[1] = (float) (playerY);

        shapex[2] = (float) (playerX + width);
        shapey[2] = (float) (playerY + height);

        shapex[3] = (float) (playerX);
        shapey[3] = (float) (playerY + height);

        player.setShapeX(shapex);
        player.setShapeY(shapey);
    }

    @Override
    public void stop() {
        // Remove entities
        world.removeEntity(player);
    }

}
