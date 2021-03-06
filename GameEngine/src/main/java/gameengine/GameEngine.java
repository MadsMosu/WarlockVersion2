/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameengine;

import managers.DotaCamera;
import managers.HUDManager;
import States.GameState;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import data.Entity;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import static data.EntityType.SPELL;
import data.GameData;
import data.World;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import managers.AssetsJarFileResolver;
import managers.GameInputProcessor;
import org.openide.util.Lookup;
import services.IEntityProcessingService;
import services.IGamePluginService;
import data.componentdata.Image;
import data.ImageManager;
import data.Netherworld;
import static data.SpellType.TELEPORT1;
import static data.SpellType.TELEPORT2;
import data.componentdata.Body;
import data.componentdata.Position;
import data.componentdata.SpellInfos;
import java.util.Collection;
import managers.AnimationHandler;
import managers.HealthBarManager;
import managers.MapManager;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author jonaspedersen
 */
public class GameEngine implements ApplicationListener {

    private GameData gameData;
    public World world;
    private Netherworld netherworld;
    private final Lookup lookup = Lookup.getDefault();
    private Lookup.Result<IGamePluginService> pluginResult;
    public List<IGamePluginService> entityPlugins;
    private IsometricTiledMapRenderer renderer;
    public DotaCamera camera;
    private OrthographicCamera hudCamera;
    private AssetManager assetManager;
    private ShapeRenderer sr;
    private SpriteBatch spriteBatch;
    private AnimationHandler animator;
    private MapManager mapManager;
    private HealthBarManager healthBarManager;
    private HUDManager hud;
    private Music backgroundMusic;

    public static final String BACKGROUNDMUSIC_PATH = "assets/sounds/Soundtrack.ogg";
    public static String BACKGROUNDMUSIC_FINAL_PATH = "";

    @Override
    public void create() {
        world = new World();
        netherworld = new Netherworld();
        spriteBatch = new SpriteBatch();
        gameData = new GameData();
        animator = new AnimationHandler();
        mapManager = new MapManager();
        healthBarManager = new HealthBarManager();

        AssetsJarFileResolver jfhr = new AssetsJarFileResolver();
        assetManager = new AssetManager(jfhr);

        sr = new ShapeRenderer();
        mapManager.loadMap(assetManager, gameData);
        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
        renderer = new IsometricTiledMapRenderer(mapManager.getMap());
        camera = new DotaCamera();
        hudCamera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        hudCamera.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        hudCamera.update();
        entityPlugins = new CopyOnWriteArrayList<>();

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        camera.position.set(mapManager.getMapWidth() / 2, 0, 0);

        pluginResult = lookup.lookupResult(IGamePluginService.class);
        pluginResult.addLookupListener(lookupListener);
        pluginResult.allItems();
        for (IGamePluginService plugin : pluginResult.allInstances()) {
            plugin.start(gameData, world);
            entityPlugins.add(plugin);
        }
        loadImages();
        hud = new HUDManager(spriteBatch, gameData, world);

        gameData.setGameState(GameState.RUN);
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("assets/sounds/backgroundmusic.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.5f);
        backgroundMusic.play();
//        BACKGROUNDMUSIC_FINAL_PATH = GameEngine.class.getResource(BACKGROUNDMUSIC_PATH).getPath().replace("file:", "");
//        ImageManager.createImage(BACKGROUNDMUSIC_FINAL_PATH, false);
//        backgroundMusic.loop();

    }

    private void loadImages() {
        for (Image image : ImageManager.images()) {
            String imagePath = image.getImageFilePath();
            if (!assetManager.isLoaded(imagePath, Texture.class)) {
                assetManager.load(imagePath, Texture.class);
                assetManager.finishLoading();
            }
        }
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (entity.get(Image.class) != null) {
                String imagePath = entity.get(Image.class).getImageFilePath();
                animator.initializeCharacters(assetManager.get(imagePath, Texture.class), entity, gameData);
            }

        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void render() {
        gameData.setDelta(Gdx.graphics.getDeltaTime());
        gameData.setFPS(Gdx.graphics.getFramesPerSecond());

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera);
        renderer.render();
        camera.updateAndMove();
        animator.updateStateTime(gameData.getDelta());
        update();
        draw();

        spriteBatch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().act(gameData.getDelta());
        hud.getStage().draw();

    }

    private void draw() {

        for (Entity e : world.getEntities(PLAYER, SPELL, ENEMY)) {
            sr.setColor(Color.MAGENTA);
            sr.begin(ShapeType.Line);

            if (e.getType() == SPELL) {
                sr.circle(e.get(Position.class).getX(), e.get(Position.class).getY(), e.get(Body.class).getWidth() / 2);
            } else {
                sr.rect(e.get(Position.class).getX(), e.get(Position.class).getY(), e.get(Body.class).getWidth(), e.get(Body.class).getHeight());

            }
            sr.setProjectionMatrix(camera.combined);
            sr.end();
        }

        healthBarManager.drawHealthBar(sr, world, camera);

        for (Entity e : world.getEntities(PLAYER, ENEMY)) {
            Position p = e.get(Position.class);
            Image image = e.get(Image.class);
            if (assetManager.isLoaded(image.getImageFilePath(), Texture.class)) {

                if (!image.isRepeat()) {
                    spriteBatch.setProjectionMatrix(camera.combined);
                    spriteBatch.begin();
                    spriteBatch.draw(animator.getFrame(e), p.getX(), p.getY());
                    spriteBatch.end();
                }
            }
        }
        for (Entity e : world.getEntities(SPELL)) {
            Position p = e.get(Position.class);
            Image image = e.get(Image.class);
            if (assetManager.isLoaded(image.getImageFilePath(), Texture.class)) {

                animator.initializeSpell(assetManager.get(image.getImageFilePath(), Texture.class), e);
                if (image.isRepeat()) {
                    spriteBatch.setProjectionMatrix(camera.combined);
                    spriteBatch.begin();
                    if (e.get(SpellInfos.class).getSpellType() != TELEPORT1 && e.get(SpellInfos.class).getSpellType() != TELEPORT2) {
                        spriteBatch.draw(animator.getSpellAnimation(e), p.getX(), p.getY(), 0, animator.getSpellAnimation(e).getRegionHeight()/2, animator.getSpellAnimation(e).getRegionWidth(), animator.getSpellAnimation(e).getRegionHeight(), 1, 1, e.getAngle());
                    } else {
                        spriteBatch.draw(animator.getSpellAnimation(e), p.getX(), p.getY());
                    }

                    spriteBatch.end();
                }
            }

        }
    }

    private void update() {
        assetManager.update();

        gameData.setMousePosition(Gdx.input.getX() + (int) (camera.position.x - camera.viewportWidth / 2),
                -Gdx.input.getY() + Gdx.graphics.getHeight() + (int) (camera.position.y - camera.viewportHeight / 2));

        if (gameData.getRoundNumber() <= gameData.getMaxRounds()) {
            for (IEntityProcessingService processor : getEntityProcessingServices()) {
                processor.process(gameData, world, netherworld);
            }
        }

        camera.update();
        if (!gameData.getGameState().equals(GameState.ROUNDEND)) {
            mapManager.ShrinkMap(gameData);
            mapManager.OnLava(world, gameData);
        }
        hud.update(gameData, world);
    }

    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    private Collection<? extends IGamePluginService> getGamePluginServices() {
        return lookup.lookupAll(IGamePluginService.class);
    }

    private final LookupListener lookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent lookupEvent) {
            Collection<? extends IGamePluginService> updated = pluginResult.allInstances();
            for (IGamePluginService updatedService : updated) {
                if (!entityPlugins.contains(updatedService)) {
                    updatedService.start(gameData, world);
                    entityPlugins.add(updatedService);
                }
            }
            for (IGamePluginService pluginService : entityPlugins) {
                if (!updated.contains(pluginService)) {
                    pluginService.stop();
                    entityPlugins.remove(pluginService);
                }
            }
        }
    };

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        renderer.dispose();
        assetManager.dispose();
    }

    
}
