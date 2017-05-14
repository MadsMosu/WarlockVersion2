/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.healthsystem;

import data.Entity;
import data.EntityType;
import static data.EntityType.*;
import data.GameData;
import data.SpellType;
import data.World;
import data.SpellList;
import data.componentdata.Health;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class)
    ,
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class HealthPlugin implements IGamePluginService, IEntityProcessingService {

    @Override
    public void start(GameData gameData, World world) {
        for (Entity e : world.getEntities(PLAYER, ENEMY)) {
            e.setHealth(100);
        }
    }

    @Override
    public void process(GameData data, World world) {
        for (Entity e : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
            Health health = e.get(Health.class);

//            if (health.getHp() > 0 && !health.getDamageTaken().isEmpty()) {
//                for (DamageTaken dtaken : health.getDamageTaken()) {
//                    int dmg = dtaken.getDamage();
//                    e.setHealth(e.getHealth() - dmg);
//
//                    if (e.getHealth() <= 0) {
//                        e.setCharState(CharacterState.DEAD);
//                    }
//                }
//            }
        }
    }

    public int getDamage(SpellType spellType) {
        switch (spellType) {
            case FIREBALL:
                return SpellList.FIREBALL_DMG;
        }
        return 0;
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
