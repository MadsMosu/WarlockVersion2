package dk.sdu.mmmi.cbse.currencysystem;

import data.Entity;
import data.EntityType;
import data.GameData;
import data.Netherworld;
import data.World;
import data.componentdata.Currency;
import data.componentdata.Score;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;


@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class),
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class CurrencyPlugin implements IGamePluginService, IEntityProcessingService {
    private int GOLD_FOR_HIT = 20;
    private int GOLD_FOR_KILL = 100; 

    @Override
    public void start(GameData gameData, World world) {
        for(Entity entity : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)){
            entity.add(new Currency(0));
        }
    }

    @Override
    public void process(GameData gameData, World world, Netherworld netheworld) {
        for(Entity entity : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)){
            Score score = entity.get(Score.class);
            Currency c = entity.get(Currency.class);
//            for(int i = 0; i < score.getTotalHits(); i++){
//                c.setGold(c.getGold() + GOLD_FOR_HIT);
//            }
//            for(int i = 0; i < score.getTotalKills(); i++){
//                c.setGold(c.getGold() + GOLD_FOR_KILL);
//            }
        }
    }



    @Override
    public void stop() {
    }

}
