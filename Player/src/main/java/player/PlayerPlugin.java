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
import data.componentdata.Image;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.SpellBook;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})
/**
 *
 * @author jcs
 */
public class PlayerPlugin implements IEntityProcessingService, IGamePluginService {

    private Entity player;
    public static final String CHARACTER_IMAGE_PATH = "assets/Characters.png";
    public static String CHARACTER_FINAL_IMAGE_PATH = "";
    private World world;

    @Override
    public void start(GameData gameData, World world) {
        CHARACTER_FINAL_IMAGE_PATH = PlayerPlugin.class.getResource(CHARACTER_IMAGE_PATH).getPath().replace("file:", "");
        ImageManager.createImage(CHARACTER_FINAL_IMAGE_PATH, false);
        this.world = world;
        player = new Entity();
        player.setType(PLAYER);
        Position pos = new Position(1888,0);
        Health health = new Health(100);
        SpellBook sb = new SpellBook(new Owner(player.getID()));
        sb.setCooldownTimeLeft(sb.getGlobalCooldownTime());
        player.add(ImageManager.getImage(CHARACTER_FINAL_IMAGE_PATH));
        player.add(health);
        player.add(pos);
        player.add(sb);
        player.setMaxSpeed(2);
        player.setAcceleration(2);
        player.setDeacceleration(1);


        Body body = new Body(50, 50, Geometry.RECTANGLE);
        player.add(body);

        player.setMoveState(MovementState.STANDINGRIGHT);
        player.setCharState(CharacterState.IDLE);
        world.addEntity(player);

    }

    @Override
    public void process(GameData gameData, World world) {

        if(player.getCharState() == CharacterState.DEAD){
            stop();
        }
    }

    @Override
    public void stop() {
        // Remove entities
        world.removeEntity(player);
    }

}
