package ai;

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
import data.componentdata.Position;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})

public class AIPlugin implements IEntityProcessingService, IGamePluginService {

    private Entity player;
    public static final String CHARACTER_IMAGE_PATH = "assets/Characters.png";
    public static String CHARACTER_FINAL_IMAGE_PATH = "";
    private World world;

    @Override
    public void start(GameData gameData, World world) {
        

    }

    @Override
    public void process(GameData gameData, World world) {

    }

    @Override
    public void stop() {
 
    }

}
