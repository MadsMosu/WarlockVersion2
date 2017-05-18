package gameengine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import data.Entity;
import data.EntityType;
import data.GameData;
import data.World;
import data.componentdata.Currency;
import data.componentdata.Health;

import managers.HealthBarManager;

public class HUD {

    private Stage stage;
    private FitViewport viewPort;

    private double health;
    private int gold;
    private float roundTimer;
    private int roundNumb;
    private int exp;
    private int level;
    private HealthBarManager healthBarManager;
    private Label goldLabel;
    private Label roundTimerLabel;
    private Label roundNumbLabel;
    private Label expLabel;
    private Label levelLabel;
    private Label healthLabel;
    private Table table;
    private Entity player;

    public HUD(SpriteBatch spriteBatch, GameData gameData, World world) {
        roundTimer = gameData.getRoundTime();
        roundNumb = gameData.getRoundNumber();

        for (Entity player : world.getEntities(EntityType.PLAYER)) {
            this.player = player;
            gold = player.get(Currency.class).getGold();
            health = player.get(Health.class).getHp();
            exp = player.getExpPoints();
            level = player.getLevel();
        }

        viewPort = new FitViewport(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        stage = new Stage(viewPort, spriteBatch);

        table = new Table();
        table.top();
        table.setFillParent(true);

        goldLabel = new Label(gold + "", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        roundTimerLabel = new Label(String.format("%.2f", roundTimer), new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        roundNumbLabel = new Label(roundNumb + "", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        levelLabel = new Label(level + "", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        expLabel = new Label(exp + "", new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        healthLabel = new Label(health + "", new Label.LabelStyle(new BitmapFont(), Color.GREEN));

        table.add(levelLabel).expandX().padTop(10);
        table.add(expLabel).expandX().padTop(10);
        table.add(healthLabel).expandX().padTop(10);
        table.add(roundNumbLabel).expandX().padTop(10);
        table.add(roundTimerLabel).expandX().padTop(10);

        for (Entity entity : world.getEntities(EntityType.PLAYER)) {
            healthBarManager = new HealthBarManager(entity, 10);
            stage.addActor(healthBarManager.getHealthBar());

        }
        stage.addActor(table);
    }

    public Stage getStage() {
        return stage;
    }

    public void update(GameData gameData, World world) {
        roundTimer = gameData.getCurrentTime();
        roundTimerLabel.setText(String.format("%.2f", roundTimer));
        healthLabel.setText(player.get(Health.class).getHp() + "");
        for (Entity entity : world.getEntities(EntityType.PLAYER)) {
            healthBarManager.getHealthBar().setValue(entity.get(Health.class).getHp());
        }
    }

//    private ProgressBarStyle setSkin()
//    {
//        Color bgColor = new Color(100 / 256f, 100 / 256f, 100 / 256f, 1f);
//        Skin skin;
//        skin = new Skin();
//        Pixmap pixmap = new Pixmap(1, 10, Pixmap.Format.RGBA8888);
//        pixmap.fill();
//        skin.add("white", new Texture(pixmap));
//        ProgressBar.ProgressBarStyle barStyle;
//        barStyle = new ProgressBar.ProgressBarStyle(skin.newDrawable("white", bgColor), skin.newDrawable("white", Color.RED));
//        barStyle.knobAfter = barStyle.knob;
//        return barStyle;
//    }
}
