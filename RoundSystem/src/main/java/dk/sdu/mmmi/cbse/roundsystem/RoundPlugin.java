package dk.sdu.mmmi.cbse.roundsystem;

import com.badlogic.gdx.Gdx;
import data.Entity;
import data.EntityType;
import data.GameData;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;
import States.GameState;


@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class),
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class RoundPlugin implements IGamePluginService, IEntityProcessingService {
    
    @Override
    public void start(GameData gameData, World world) {
        gameData.setRoundNumber(1);
        gameData.setCurrentTime(0);
        gameData.setRoundTime(3);
    }

    @Override
    public void process(GameData gameData, World world) {
        //DONT DELETE
//        float dt = Gdx.graphics.getDeltaTime();
//        
//        gameData.setCurrentTime(gameData.getRoundTime() - dt);
//        int minutes = ((int)gameData.getRoundTime()) / 60;
//        int seconds = ((int)gameData.getRoundTime()) % 60;
//        if(gameData.getCurrentTime() <= 0){
//            gameData.setGameState(GameState.ROUNDEND);
//            gameData.setRoundNumber(gameData.getRoundNumber() + 1);
//        }
        
    }



    @Override
    public void stop() {
    }

}
