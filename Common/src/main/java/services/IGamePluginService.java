package services;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import data.GameData;
import data.World;

public interface IGamePluginService {
    void start(GameData gameData, World world);

    void stop();
    
}
