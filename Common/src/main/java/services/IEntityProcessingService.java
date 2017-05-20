package services;

import data.Entity;
import data.GameData;
import data.Netherworld;
import data.World;
import java.util.Map;

public interface IEntityProcessingService {

    void process(GameData gameData, World world, Netherworld netherworld);
}
