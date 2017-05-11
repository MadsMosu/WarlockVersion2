/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import com.badlogic.gdx.maps.tiled.TiledMap;
import data.GameData;
import data.World;

/**
 *
 * @author jonaspedersen
 */
public interface MapSPI {
    
    void unloadMap();
    TiledMap generateMap(World world, GameData gamedata, int shrinkTime);
    void mapShrink(int layerCount); 
    void processMap(World world, GameData gameData);
    TiledMap getMap();
    
}
