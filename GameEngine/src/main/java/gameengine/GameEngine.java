/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameengine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
import services.MapSPI;
import data.componentdata.Image;
import data.ImageManager;
import data.componentdata.Position;

/**
 *
 * @author jonaspedersen
 */
public class GameEngine implements ApplicationListener {

    private GameData gameData;
    private World world;
    private final Lookup lookup = Lookup.getDefault();
    private Lookup.Result<IGamePluginService> pluginResult;
    private List<IGamePluginService> entityPlugins;
    private Lookup.Result<IEntityProcessingService> processorResult;
    private List<IEntityProcessingService> processors;
    private Lookup.Result<MapSPI> mapResult;
    private List<MapSPI> maps;
    private TiledMap map;
    private IsometricTiledMapRenderer renderer;
    public DotaCamera camera;
    private AssetManager assetManager;
    private MapLayers mapLayers, groundLayers;
    private float shrinkTimer, shrinkTime;
    private int layerCount;
    private ShapeRenderer sr;
    private SpriteBatch spriteBatch;
    private Animator animator;
    private HUD hud;
    
    private MapProperties prop;
    private int mapHeight;
    private int mapWidth;

    @Override
    public void create() {
        world = new World();
        gameData = new GameData();
        maps = new CopyOnWriteArrayList<>();
        animator = new Animator();
        shrinkTime = 15;
        AssetsJarFileResolver jfhr = new AssetsJarFileResolver();
        assetManager = new AssetManager(jfhr);

        pluginResult = lookup.lookupResult(IGamePluginService.class);
        processorResult = lookup.lookupResult(IEntityProcessingService.class);
        mapResult = lookup.lookupResult(MapSPI.class);
        sr = new ShapeRenderer();
        loadMap();
        map = assetManager.get("assets/shrinkingmap.tmx", TiledMap.class);

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
        renderer = new IsometricTiledMapRenderer(map);
        camera = new DotaCamera();
        processors = new CopyOnWriteArrayList<>();
        entityPlugins = new CopyOnWriteArrayList<>();

        mapLayers = map.getLayers();
        groundLayers = new MapLayers();

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);
        
        prop = map.getProperties();
        mapHeight = prop.get("height", Integer.class)* prop.get("tileheight", Integer.class);
        mapWidth = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);;
        System.out.println(mapHeight + "  " + mapWidth);
 
        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        //camera.setToOrtho(false, gameData.getMapWidth(), gameData.getMapHeight());
        camera.position.set(mapWidth/2, 0, 0);

        for (int i = 2; i < mapLayers.getCount(); i++) {
            mapLayers.get(i).setVisible(false);
            groundLayers.add(mapLayers.get(i));
        }

        for (IGamePluginService plugin : pluginResult.allInstances()) {
            plugin.start(gameData, world);
            entityPlugins.add(plugin);
        }

        for (IEntityProcessingService processor : processorResult.allInstances()) {
            processors.add(processor);
        }

        loadImages();

        spriteBatch = new SpriteBatch();

        hud = new HUD(spriteBatch, gameData, world);
    }

    private void loadImages() {
        for (Image image : ImageManager.images()) {
            String imagePath = image.getImageFilePath();

            if (!assetManager.isLoaded(imagePath, Texture.class)) {
                assetManager.load(imagePath, Texture.class);
                assetManager.finishLoading();
                animator.initializeSprite(assetManager.get(imagePath, Texture.class), gameData);
            }
        }
    }

    private void loadMap() {
        if (!assetManager.isLoaded("assets/shrinkingmap.tmx", TiledMap.class)) {
            assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
            assetManager.load("assets/shrinkingmap.tmx", TiledMap.class);
            assetManager.finishLoading();
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(camera);
        renderer.render();
        camera.updateAndMove();
        animator.render(gameData);
        update();
        draw();
    }

    private void draw() {

        for (Entity e : world.getEntities(PLAYER)) {
            Position p = e.get(Position.class);
            Image image = e.get(Image.class);
            if (assetManager.isLoaded(image.getImageFilePath(), Texture.class)) {

                

                if (!image.isRepeat()) {
                    animator.updateStateTime(gameData.getDelta());
                    spriteBatch.setProjectionMatrix(camera.combined);
                    spriteBatch.begin();
                    spriteBatch.draw(animator.getFrame(e), p.getX(), p.getY());
                    spriteBatch.end();

                }
            }
        }

        for (Entity e : world.getEntities(ENEMY)) {
            Position p = e.get(Position.class);
            Image image = e.get(Image.class);
            if (assetManager.isLoaded(image.getImageFilePath(), Texture.class)) {

                

                if (!image.isRepeat()) {
                    animator.updateStateTime(gameData.getDelta());
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
            System.out.println(world.getEntities(SPELL).size());
            if (assetManager.isLoaded(image.getImageFilePath(), Texture.class)) {

                
                animator.initializeSpell(assetManager.get(image.getImageFilePath(), Texture.class));

                if (!image.isRepeat()) {
                    animator.updateStateTime(gameData.getDelta());
                    spriteBatch.setProjectionMatrix(camera.combined);
                    spriteBatch.begin();
                    spriteBatch.draw(animator.getSpellTexture(), p.getX(), p.getY());
                    spriteBatch.end();
                }
            }
        }

        spriteBatch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().act(gameData.getDelta());
        hud.getStage().draw();
    }

    private void mapShrink(int layerCount) {

        mapLayers.get(1).setVisible(false);
        for (int i = 0; i < groundLayers.getCount(); i++) {
            if (layerCount == i + 1 && i != 4) {
                groundLayers.get(i).setVisible(true);
            }
            else {
                groundLayers.get(i).setVisible(false);
            }

        }
    }

    private void update() {
        assetManager.update();

        gameData.setMousePosition(Gdx.input.getX() + (int) (camera.position.x - camera.viewportWidth / 2),
                -Gdx.input.getY() + Gdx.graphics.getHeight() + (int) (camera.position.y - camera.viewportHeight / 2));

        shrinkTimer += gameData.getDelta();
        if (shrinkTimer >= shrinkTime) {
            layerCount++;
            if (layerCount >= groundLayers.getCount()) {
                layerCount--;
            }
            mapShrink(layerCount);
            shrinkTimer = 0;
        }

//        for (MapSPI map : lookup.lookupAll(MapSPI.class)) {
//            map.processMap(world, gameData);
//        }
        for (IEntityProcessingService processor : processors) {
            processor.process(gameData, world);
        }

        camera.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        assetManager.dispose();
    }

}
