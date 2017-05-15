package services;

import data.GameData;
import data.World;

public interface IGamePluginService {
    void start(GameData gameData, World world);

    void stop();
    
}
