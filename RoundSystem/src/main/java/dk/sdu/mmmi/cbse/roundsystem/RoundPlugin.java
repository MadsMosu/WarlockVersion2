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
import data.componentdata.Score;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class)
    ,
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class RoundPlugin implements IGamePluginService, IEntityProcessingService {

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

        if (gameData.getRoundNumber() > gameData.getMaxRounds()) {
            for (Entity e : world.getEntities()) {
                if (e.isType(EntityType.PLAYER) || e.isType(EntityType.ENEMY)) {
                    netherworld.addEntity(e);
                }
                world.removeEntity(e);
                gameData.setGameState(GameState.PAUSE);
            }
            int enemyRoundsWon = 0;
            for (Entity e : netherworld.getEntities()) {
                Score score = e.get(Score.class);
                if (e.isType(EntityType.ENEMY)) {
                    enemyRoundsWon += score.getRoundsWon();
                }
                if (e.isType(EntityType.PLAYER) && score.getRoundsWon() > gameData.getMaxRounds() / 2) {
                    gameData.setWinner("Player");
                } else if (e.isType(EntityType.PLAYER) && enemyRoundsWon > gameData.getMaxRounds() / 2) {
                    gameData.setWinner("Enemy");
                }
            }
        }
        float dt = gameData.getDelta();
        if (gameData.getRoundTime() > 0 && gameData.getGameState().equals(GameState.RUN)) {
            gameData.setRoundTime(gameData.getRoundTime() - dt);

        }
        numbOfCharacters = world.getEntities(EntityType.ENEMY, EntityType.PLAYER).size();
        if (gameData.getRoundTime() <= 0 || numbOfCharacters == 1 && gameData.getGameState().equals(GameState.RUN) && gameData.getRoundNumber() <= gameData.getMaxRounds()) {

            if (numbOfCharacters == 1) {
                for (Entity e : world.getEntities()) {
                    Score score = e.get(Score.class);
                    e.setCharState(CharacterState.IDLE);
                    if (e.isType(EntityType.ENEMY)) {
                        gameData.setWhoWinsRound("ENEMY WINS THE ROUND!");
                        score.setRoundsWon(score.getRoundsWon() + 1);
                    } else if (e.isType(EntityType.PLAYER)) {
                        gameData.setWhoWinsRound("PLAYER WINS THE ROUND!");
                        score.setRoundsWon(score.getRoundsWon() + 1);
                    }
                }
            } else {
                gameData.setWhoWinsRound("THE ROUND IS A DRAW!");
            }
            gameData.setGameState(GameState.ROUNDEND);
            resetRoundTime(gameData);
        }

        if (gameData.getGameState().equals(GameState.ROUNDEND) && world.getEntities().isEmpty()) {
            gameData.setGameState(GameState.PAUSE);
        }

        if (gameData.getGameState().equals(GameState.PAUSE)) {
            if (gameData.getNextRoundCountdown() <= 0) {
                resetNextRoundTime(gameData);
                gameData.setRoundNumber(gameData.getRoundNumber() + 1);
                gameData.setGameState(GameState.RUN);
            } else {
                gameData.setNextRoundCountdown(gameData.getNextRoundCountdown() - dt);
            }
        }

    }

    @Override
    public void stop() {
    }

}
