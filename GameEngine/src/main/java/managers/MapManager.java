/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managers;

import States.StateMachine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import data.Entity;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import data.GameData;
import data.World;
import data.componentdata.AI;
import data.componentdata.Damage;
import data.componentdata.DamageTaken;
import data.componentdata.Health;
import data.componentdata.Owner;
import data.componentdata.Position;

/**
 *
 * @author Aleksander
 */
public class MapManager {

    private float shrinkTimer, shrinkTime;
    private TiledMap map;
    private TiledMapTileLayer currentLayer;
    private MapLayers mapLayers, groundLayers;
    private float lavaTimer = 0;
    private int layerCount;
    private MapProperties prop;
    private int mapHeight;
    private int mapWidth;

    public MapManager() {
        shrinkTime = 15;

    }

    public void loadMap(AssetManager assetManager, GameData gameData) {
        if (!assetManager.isLoaded("assets/shrinkingmap.tmx", TiledMap.class)) {
            assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
            assetManager.load("assets/shrinkingmap.tmx", TiledMap.class);
            assetManager.finishLoading();

        }
        map = assetManager.get("assets/shrinkingmap.tmx", TiledMap.class);
        initializeMapLayers(gameData);
    }

    private void initializeMapLayers(GameData gameData) {
        mapLayers = map.getLayers();
        groundLayers = new MapLayers();
        prop = map.getProperties();
        mapHeight = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);
        mapWidth = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);;
        System.out.println(mapHeight + "  " + mapWidth);

        int mapPixelWidth = prop.get("width", Integer.class) * prop.get("tilewidth", Integer.class);
        int mapPixelHeight = prop.get("height", Integer.class) * prop.get("tileheight", Integer.class);
        gameData.setMapHeight(mapPixelHeight);
        gameData.setMapWidth(mapPixelWidth);
        currentLayer = (TiledMapTileLayer) mapLayers.get(0);

        for (int i = 1; i < mapLayers.getCount(); i++) {
            mapLayers.get(i).setVisible(false);
            groundLayers.add(mapLayers.get(i));
        }
    }

    public void ShrinkMap(GameData gameData) {
        shrinkTimer += gameData.getDelta();
        if (shrinkTimer >= shrinkTime) {
            layerCount++;
            if (layerCount >= groundLayers.getCount()) {
                layerCount--;
            }
            mapShrink(layerCount);
            shrinkTimer = 0;
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
            }
            else {
                groundLayers.get(i).setVisible(false);
            }

        }
    }

    
    public boolean OnLava(World world, GameData gameData) {

        for (Entity e : world.getEntities(PLAYER, ENEMY)) {

            float entityX = e.get(Position.class).getX();
            float entityY = e.get(Position.class).getY();

            int tileRow = (int) (entityX / currentLayer.getTileWidth() - (entityY / currentLayer.getTileHeight()));
            int tileCol = (int) Math.abs((tileRow * currentLayer.getTileHeight() / 2 + entityY) / (currentLayer.getTileHeight() / 2));
            if(currentLayer.getCell(tileRow, tileCol-1) == null){
                e.get(Health.class).addDamageTaken(new DamageTaken(new Damage(100), new Owner(e.getID())));
                System.out.println("Outside map");
            }
            if (currentLayer.getCell(tileRow, tileCol) != null) {
                if (currentLayer.getCell(tileRow, tileCol).getTile().getId() == 5) {
                    e.get(Position.class).setInLava(true);
                    lavaTimer += gameData.getDelta();
                    if (lavaTimer >= 1) {
                        e.get(Health.class).addDamageTaken(new DamageTaken(new Damage(5), new Owner(e.getID())));
                        lavaTimer = 0;
                        
                    }
                    return true;
                }
                else {
                    e.get(Position.class).setInLava(false);
                }
            }
        }
        return false;
    }

    public TiledMap getMap() {
        return map;
    }

    public int getMapWidth() {
        return mapWidth;
    }
}
