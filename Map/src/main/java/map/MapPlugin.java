/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package map;

import com.badlogic.gdx.maps.MapLayers;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import services.MapSPI;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import data.GameData;

/**
 *
 * @author jonaspedersen
 */
@ServiceProvider(service = MapSPI.class)
public class MapPlugin implements MapSPI {

    private World world;
    private TiledMap map;
    private MapLayers mapLayers, groundLayers;
    private int layerCount;
    private float shrinkTimer;

    @Override
    public void unloadMap()
    {
        
    }

    @Override
    public TiledMap generateMap(World world,GameData gameData, int shrinkTime)
    {
        this.world = world;
        gameData.setShrinkTime(shrinkTime);
        
        map = new TmxMapLoader().load("assets/shrinkingmap.tmx");
        gameData.setLayerCount(0);
        mapLayers = map.getLayers();
        groundLayers = new MapLayers();
        

        for (int i = 2; i < mapLayers.getCount(); i++) {
            mapLayers.get(i).setVisible(false);
            groundLayers.add(mapLayers.get(i));
        }
        return this.map;
    }

    @Override
    public void mapShrink(int layerCount)
    {

        mapLayers.get(1).setVisible(false);
        for (int i = 0; i < groundLayers.getCount(); i++) {
            if (layerCount == i + 1 && i != 4) {
                groundLayers.get(i).setVisible(true);
            } else {
                groundLayers.get(i).setVisible(false);
            }

        }
    }
    
    public TiledMap getMap(){
        return this.map;
    }

    @Override
    public void processMap(World world, GameData gameData)
    {
        this.shrinkTimer += gameData.getDelta();
        if (this.shrinkTimer >= gameData.getShrinkTime()) {
            gameData.setLayerCount(gameData.getLayerCount()+1);
            if(gameData.getLayerCount() >= groundLayers.getCount()){
                gameData.setLayerCount(groundLayers.getCount() - 1);
            }
            mapShrink(layerCount);
            this.shrinkTimer = 0;
        }
    }

}
