package dk.sdu.mmmi.cbse.roundsystem;

import States.CharacterState;
import data.Entity;
import data.EntityType;
import data.GameData;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;
import States.GameState;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import data.Netherworld;
import data.componentdata.Health;
import data.componentdata.Position;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class)
    ,
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class RoundPlugin implements IGamePluginService, IEntityProcessingService {

    private float roundTime;
    private float numbOfCharacters;

    @Override
    public void start(GameData gameData, World world) {
        gameData.setRoundNumber(1);
        gameData.setRoundTime(60);
        gameData.setNextRoundCountdown(5);
        gameData.setMaxRounds(5);
    }

    private void resetRoundTime(GameData gameData) {
        gameData.setRoundTime(60);
    }

    private void resetNextRoundTime(GameData gameData) {
        gameData.setNextRoundCountdown(5);
    }

    @Override
    public void process(GameData gameData, World world, Netherworld netherworld) {

        float dt = gameData.getDelta();
        if (gameData.getRoundTime() > 0 && gameData.getRoundNumber() <= gameData.getMaxRounds() && !gameData.getGameState().equals(GameState.ROUNDEND)) {
            roundTime = gameData.getRoundTime() - dt;
        }

        numbOfCharacters = world.getEntities(EntityType.ENEMY, EntityType.PLAYER).size();
        System.out.println(numbOfCharacters);
        gameData.setRoundTime(roundTime);
        if (gameData.getRoundTime() <= 0 || numbOfCharacters == 1  || numbOfCharacters == 0 && gameData.getRoundNumber() <= gameData.getMaxRounds()) {
            gameData.setGameState(GameState.ROUNDEND);

            if (numbOfCharacters == 1) {
                for (Entity e : world.getEntities(EntityType.ENEMY, EntityType.PLAYER)) {
                    System.out.println("many people xD");
                    e.setCharState(CharacterState.IDLE);
                    if (e.isType(EntityType.ENEMY)) {
                        gameData.setWhoWinsRound("ENEMY WINS THE ROUND!");
                    } else if (e.isType(EntityType.PLAYER)) {
                        System.out.println("hi");
                        gameData.setWhoWinsRound("PLAYER WINS THE ROUND!");
                    }
                }
            } else {
                gameData.setWhoWinsRound("THE ROUND IS A DRAW!");
            }

            for (Entity e : world.getEntities()) {
                if (!e.isType(EntityType.SPELL)) {
                    netherworld.addEntity(e);
                }
                world.removeEntity(e);
            
            }
            
            if (gameData.getNextRoundCountdown() <= 0) {
                System.out.println("should be in nether: " + netherworld.getEntities().size());
                for (Entity e : netherworld.getEntities()) {

                        world.addEntity(e);
                        netherworld.removeEntity(e);
                }
                System.out.println("actual entities in world: " + world.getEntities().size());
                resetNextRoundTime(gameData);
                resetRoundTime(gameData);
                gameData.setRoundNumber(gameData.getRoundNumber() + 1);
                gameData.setGameState(GameState.RUN);
            }
            if (gameData.getRoundNumber() < gameData.getMaxRounds() && gameData.getGameState().equals(GameState.ROUNDEND)) {
                gameData.setNextRoundCountdown(gameData.getNextRoundCountdown() - dt);
            }
        } else if (gameData.getRoundNumber() == gameData.getMaxRounds()+1) {
            for (Entity e : world.getEntities()) {
                world.removeEntity(e);
            }
        }
        
    }

    @Override
    public void stop() {
    }

}