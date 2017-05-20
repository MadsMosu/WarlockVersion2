/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.levelsystem;

import data.Entity;
import data.EntityType;
import data.GameData;
import data.Netherworld;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class),
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class LevelPlugin implements IGamePluginService, IEntityProcessingService {

    private LevelSystem levelSystem;

    @Override
    public void start(GameData gameData, World world) {
        levelSystem = new LevelSystem(world);
    }

    @Override
    public void process(GameData gameData, World world, Netherworld netherworld) {
        float dt = gameData.getDelta();
        
        //---------- UDKOMMERTERET PAAGRUND AF NULLPOINTER DA INGEN ANDRE ENTITIES END PLAYER ------------
//        for (Entity caster : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
//            for (Entity reciever : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
//            if (reciever.getHitBy().equals(caster) && !(reciever.equals(caster))) {
//                levelSystem.gainExp(caster, levelSystem.HIT);
//            }
//            if (reciever.getHealth() <= 0 && !(reciever.equals(caster))) {
//                levelSystem.gainExp(caster, levelSystem.KILL);
//            }
//        }
//        }
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
