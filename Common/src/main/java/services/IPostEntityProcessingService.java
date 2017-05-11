package services;

import data.GameData;
import data.World;

/**
 *
 * @author jcs
 */
public interface IPostEntityProcessingService  {
        void process(GameData gameData, World world);
}
