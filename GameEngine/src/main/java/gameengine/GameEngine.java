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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import data.Entity;
import static data.EntityType.ENEMY;
import static data.EntityType.MAP;
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
import data.componentdata.AI;
import data.componentdata.Body;
import data.componentdata.Damage;
import data.componentdata.DamageTaken;
import data.componentdata.Health;
import data.componentdata.Owner;
import data.componentdata.Position;
import java.util.Collection;
import managers.AnimationHandler;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

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
    private TiledMap map;
    private IsometricTiledMapRenderer renderer;
    public DotaCamera camera;
    private OrthographicCamera hudCamera;
    private AssetManager assetManager;
    private MapLayers mapLayers, groundLayers;
    private float shrinkTimer, shrinkTime;
    private float lavaTimer = 0;
    private int layerCount;
    private ShapeRenderer sr;
    private SpriteBatch spriteBatch;
    private AnimationHandler animator;
    private HUD hud;
    private TiledMapTileLayer currentLayer;
    private MapProperties prop;
    private int mapHeight;
    private int mapWidth;

    @Override
    public void create() {
        world = new World();
        gameData = new GameData();
        animator = new AnimationHandler();
        shrinkTime = 15;
        AssetsJarFileResolver jfhr = new AssetsJarFileResolver();
        assetManager = new AssetManager(jfhr);

        sr = new ShapeRenderer();
        loadMap();
        map = assetManager.get("assets/shrinkingmap.tmx", TiledMap.class);

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
        renderer = new IsometricTiledMapRenderer(map);
        camera = new DotaCamera();
        hudCamera = new OrthographicCamera(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        hudCamera.translate(gameData.getDisplayWidth() / 2, gameData.getDisplayHeight() / 2);
        hudCamera.update();
        entityPlugins = new CopyOnWriteArrayList<>();

        mapLayers = map.getLayers();
        groundLayers = new MapLayers();

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        camera.position.set(camera.viewportWidth, camera.viewportHeight, 0);

        prop = map.getProperties();
        mapHeight = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);
        mapWidth = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);;
        System.out.println(mapHeight + "  " + mapWidth);

        int mapPixelWidth = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        int mapPixelHeight = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);
        gameData.setMapHeight(mapPixelHeight);
        gameData.setMapWidth(mapPixelWidth);

        gameData.setDisplayWidth(Gdx.graphics.getWidth());
        gameData.setDisplayHeight(Gdx.graphics.getHeight());
        //camera.setToOrtho(false, gameData.getMapWidth(), gameData.getMapHeight());
        camera.position.set(mapWidth / 2, 0, 0);
        currentLayer = (TiledMapTileLayer) mapLayers.get(0);

        for (int i = 1; i < mapLayers.getCount(); i++) {
            mapLayers.get(i).setVisible(false);
            groundLayers.add(mapLayers.get(i));
        }

        pluginResult = lookup.lookupResult(IGamePluginService.class);
        pluginResult.addLookupListener(lookupListener);
        pluginResult.allItems();
        for (IGamePluginService plugin : pluginResult.allInstances()) {
            plugin.start(gameData, world);
            entityPlugins.add(plugin);
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
            }
        }
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (entity.get(Image.class) != null) {
                String imagePath = entity.get(Image.class).getImageFilePath();
                animator.initializeCharacters(assetManager.get(imagePath, Texture.class), entity, gameData);
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

        for (Entity e : world.getEntities(MAP, PLAYER, SPELL, ENEMY)) {
            sr.setColor(Color.MAGENTA);
            sr.begin(ShapeType.Line);

            if(e.getType() == SPELL){
                sr.circle(e.get(Position.class).getX(), e.get(Position.class).getY(), e.get(Body.class).getWidth() / 2);
            } else {
                float[] shapex = e.getShapeX();
            float[] shapey = e.getShapeY();

            for (int i = 0, j = shapex.length - 1;
                    i < shapex.length;
                    j = i++) {

                sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);

            }
            
            }
            sr.setProjectionMatrix(camera.combined);
            sr.end();
        }

        for (Entity e : world.getEntities(PLAYER)) {
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

        for (Entity e : world.getEntities(ENEMY)) {
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
            if (!assetManager.isLoaded(image.getImageFilePath(), Texture.class)) {
                assetManager.load(image.getImageFilePath(), Texture.class);
                assetManager.finishLoading();

            }
            animator.initializeSpell(assetManager.get(image.getImageFilePath(), Texture.class));
            if (image.isRepeat()) {
                spriteBatch.setProjectionMatrix(camera.combined);
                spriteBatch.begin();
                spriteBatch.draw(animator.getSpellAnimation(), p.getX(), p.getY(), 0, animator.getSpellAnimation().getRegionHeight()/2, animator.getSpellAnimation().getRegionWidth(), animator.getSpellAnimation().getRegionHeight(), 1, 1, e.getAngle());
                spriteBatch.end();
            }
        }

    }

    private void mapShrink(int layerCount) {
        mapLayers.get(0).setVisible(false);
        for (int i = 0; i < groundLayers.getCount(); i++) {
            if (layerCount == i + 1 && i != 4) {
                if (i > 0) {
                    groundLayers.get(i - 1).setVisible(false);
                }
                groundLayers.get(i).setVisible(true);
                currentLayer = (TiledMapTileLayer) groundLayers.get(i);
            } else {
                groundLayers.get(i).setVisible(false);
            }

        }
    }

//    private boolean checkIfOnLava()
//    {
//        for (Entity e : world.getEntities(PLAYER)) {
//            float height = currentLayer.getTileHeight() * currentLayer.getHeight();
//            float width = currentLayer.getTileWidth() * currentLayer.getWidth();
//            
//            float playerX = e.get(Position.class).getX();
//            float playerY = e.get(Position.class).getY();
//            
//            int tileRow = (int) (playerX / currentLayer.getTileWidth());
//            int tileCol = (int) Math.abs((playerY - (height / 2)) / currentLayer.getTileHeight());
//            System.out.println("playerX: " + playerX);
//            System.out.println("playerY: " + playerY);
//            if(currentLayer.getCell(tileRow, tileCol).getTile() != null){
//                if (currentLayer.getCell(tileRow, tileCol).getTile().getId() == 3) {
//                    System.out.println("Walking on lava");
//                    currentLayer.getCell(tileRow, tileCol).setTile(null);
//                    return true;
//                }
//            }
//            currentLayer.getCell(tileRow, tileCol).setTile(null);
//        }
//        System.out.println("walking on ground");
//        
//        return false;
//    }
    private boolean OnLava() {

        for (Entity e : world.getEntities(PLAYER, ENEMY)) {

            float entityX = e.get(Position.class).getX();
            float entityY = e.get(Position.class).getY();

            int tileRow = (int) (entityX / currentLayer.getTileWidth() - (entityY / currentLayer.getTileHeight()));
            int tileCol = (int) Math.abs((tileRow * currentLayer.getTileHeight() / 2 + entityY) / (currentLayer.getTileHeight() / 2));
            if (currentLayer.getCell(tileCol, tileCol) != null) {
                if (currentLayer.getCell(tileRow, tileCol).getTile().getId() == 5) {
                    lavaTimer += gameData.getDelta();
                    if (lavaTimer >= 1) {
                        e.get(Position.class).setInLava(true);
                        e.get(Health.class).addDamageTaken(new DamageTaken(new Damage(5), new Owner(e.getID())));
                        lavaTimer = 0;
                        System.out.println(e.get(Health.class).getHp());
                    }
                    return true;
                } else {
                    e.get(Position.class).setInLava(false);
                }
            }
        }
        return false;
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

        for (IEntityProcessingService processor : getEntityProcessingServices()) {
            processor.process(gameData, world);
        }

        camera.update();
        OnLava();
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
        map.dispose();
        renderer.dispose();
        assetManager.dispose();
    }

}
