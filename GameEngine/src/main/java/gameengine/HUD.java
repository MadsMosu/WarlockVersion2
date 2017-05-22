package gameengine;

import States.GameState;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import data.Entity;
import data.EntityType;
import data.GameData;
import data.World;
import data.componentdata.Currency;
import data.componentdata.Health;
import data.componentdata.Score;

public class HUD {

    private Stage stage;
    private FitViewport viewPort;

    private float health;
    private float roundTimer;
    private int roundNumb;
    private int kills;
    private int damageDone;
    private int roundsWon;
    private Label roundTimerLabel;
    private Label roundNumbLabel;
    private Label healthLabel;
    private Label FPSLabel;
    
    private Label killsLabel;
    private Label roundsWonLabel;

    private Table table;
    private Table winnerTable;
    private Entity player;

    private Label winnerLabel;
    private Label nextRoundCDLabel;

    public HUD(SpriteBatch spriteBatch, GameData gameData, World world) {
        roundTimer = gameData.getRoundTime();
        roundNumb = gameData.getRoundNumber();

        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            this.player = player;
            health = player.get(Health.class).getHp();
            Score score = player.get(Score.class);
            kills = score.getKills();
            roundsWon = score.getRoundsWon();
        }

        viewPort = new FitViewport(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        stage = new Stage(viewPort, spriteBatch);

        table = new Table();
        table.top();
        table.setFillParent(true);

        winnerTable = new Table();
        winnerTable.center();
        winnerTable.setFillParent(true);
        
        healthLabel = new Label("HP: " + health, new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        roundNumbLabel = new Label("Round: " + roundNumb, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        roundTimerLabel = new Label("Time: " + String.format("%.2f", roundTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        FPSLabel = new Label("FPS: " + gameData.getFPS(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        
        table.add(healthLabel).expandX().padTop(10);
        table.add(roundNumbLabel).expandX().padTop(10);
        table.add(roundTimerLabel).expandX().padTop(10);
        table.add(FPSLabel).expandX().padTop(10);
        table.add(killsLabel).expandX().padTop(10);
        table.add(roundsWonLabel).expandX().padTop(10);

        winnerLabel = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        nextRoundCDLabel = new Label("Next round starts in " + String.format("%.2f", gameData.getNextRoundCountdown()), new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        winnerTable.add(winnerLabel).center();
        winnerTable.row().padTop(5).padBottom(5);
        winnerTable.add(nextRoundCDLabel).center();
        
        winnerTable.setVisible(false);
        
        killsLabel = new Label("Kills: " + kills, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        roundsWonLabel = new Label("Rounds won: " + roundsWon, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        
        
        

        
        stage.addActor(table);
        stage.addActor(winnerTable);
    }

    public Stage getStage() {
        return stage;
    }

    public void update(GameData gameData, World world) {
        healthLabel.setText("Health: " + player.get(Health.class).getHp());
        roundNumbLabel.setText("Round: " + gameData.getRoundNumber());
        roundTimerLabel.setText("Time: " + String.format("%.2f", gameData.getRoundTime()));
        FPSLabel.setText("FPS: " + gameData.getFPS());      
        killsLabel.setText("Kills: " + player.get(Score.class).getKills());
        roundsWonLabel.setText("Rounds won: " + player.get(Score.class).getRoundsWon());

        if (gameData.getGameState().equals(GameState.PAUSE)) {
            winnerTable.setVisible(true);          
            winnerLabel.setText(gameData.getWhoWinsRound());
            if(gameData.getRoundNumber() < gameData.getMaxRounds()){
                nextRoundCDLabel.setText("Next round starts in " + String.format("%.2f", gameData.getNextRoundCountdown()));     
            } else {
                nextRoundCDLabel.setText("Game has ended..." + gameData.getWinner() + " wins the game");
            }
        } else{
            winnerTable.setVisible(false);
        }
    }
}
