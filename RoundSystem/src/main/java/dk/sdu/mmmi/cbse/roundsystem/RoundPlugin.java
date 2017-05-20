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
import data.Netherworld;
import data.componentdata.Health;

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
        if (gameData.getRoundTime() > 0 && gameData.getRoundNumber() <= gameData.getMaxRounds() && gameData.getGameState() != GameState.ROUNDEND) {
            roundTime = gameData.getRoundTime() - dt;
        }

        for (Entity e : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
            numbOfCharacters++;
        }

        gameData.setRoundTime(roundTime);
        if (gameData.getRoundTime() <= 0 || numbOfCharacters == 1 && gameData.getRoundNumber() <= gameData.getMaxRounds()) {
            gameData.setGameState(GameState.ROUNDEND);
            for (Entity e : world.getEntities()) {
                if (!e.isType(EntityType.SPELL)) {
                    netherworld.addEntity(e);
                }
                world.removeEntity(e);
            }

            if (numbOfCharacters == 1) {
                for (Entity e : world.getEntities()) {
                    e.setCharState(CharacterState.IDLE);
                    if (e.isType(EntityType.ENEMY)) {
                        gameData.setWhoWinsRound("ENEMY WINS THE ROUND!");
                    } else if (e.isType(EntityType.PLAYER)) {
                        gameData.setWhoWinsRound("PLAYER WINS THE ROUND!");
                    }
                }
            } else {
                gameData.setWhoWinsRound("THE ROUND IS A DRAW!");
            }
            if (gameData.getNextRoundCountdown() <= 0) {
                for (Entity e : netherworld.getEntities()) {
                        Health hp = e.get(Health.class);
                        hp.setHp(hp.getMaxHp());
                        world.addEntity(e);
                        netherworld.removeEntity(e);
                }
                resetNextRoundTime(gameData);
                resetRoundTime(gameData);
                gameData.setRoundNumber(gameData.getRoundNumber() + 1);
                gameData.setGameState(GameState.RUN);
            }
            if (gameData.getRoundNumber() < gameData.getMaxRounds()) {
                gameData.setNextRoundCountdown(gameData.getNextRoundCountdown() - dt);
            }
        } else if (gameData.getRoundNumber() == gameData.getMaxRounds()+1) {
            for (Entity e : world.getEntities()) {
                world.removeEntity(e);
            }
        }
        numbOfCharacters = 0;
    }

    @Override
    public void stop() {
    }

}
