
package map;

import com.badlogic.gdx.maps.MapLayers;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import services.MapSPI;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import data.AssetManager;
import data.Entity;
import data.GameData;
import services.IGamePluginService;
import org.openide.util.lookup.ServiceProviders;

/**
 *
 * @author jonaspedersen
 */
//@ServiceProviders(value = {
//    @ServiceProvider(service = IGamePluginService.class),
//    @ServiceProvider(service = MapSPI.class)
//        
//})
public class MapPlugin implements IGamePluginService, MapSPI {

    private World world;
    private TiledMap map;
    private MapLayers mapLayers, groundLayers;
    private int layerCount;
    private float shrinkTimer;
    private Entity mapBoundary;
    String MAP_PATH = "assets/shrinkingmap.tmx";
    String MAP_FINAL_PATH = "";
    
    
    @Override
    public void unloadMap()
    {
        
    }

    
    @Override
    public TiledMap generateMap(World world,GameData gameData, int shrinkTime)
    {
        MAP_FINAL_PATH = MapPlugin.class.getResource(MAP_PATH).getPath().replace("file:", "");
        AssetManager.createAsset(MAP_FINAL_PATH, false);
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
    
    @Override
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
	@Override
    public void start(GameData gameData, World world) {
//        this.world = world;
//        mapBoundary = new Entity();
//        mapBoundary.setType(MAP);
//        
//        
//        int shortDiagonal = (int) (gameData.getMapHeight() *(Math.sqrt(2+2* Math.cos(60))));
//        int longDiagonal = (int) (gameData.getMapWidth() *(Math.sqrt(2+2* Math.cos(120))));
//        
//        float[] shapeX = new float[] {0, 3200, 6400, 3200};
//        float[] shapeY = new float[] {16, 1600, 0, -1600};
//        mapBoundary.setShapeX(shapeX);
//        mapBoundary.setShapeY(shapeY);
//        world.addEntity(mapBoundary);
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
