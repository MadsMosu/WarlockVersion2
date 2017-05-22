package dk.sdu.mmmi.cbse.healthsystem;

import States.CharacterState;
import data.Entity;
import data.EntityType;
import static data.EntityType.*;
import data.GameData;
import data.Netherworld;
import data.SpellType;
import data.World;
import data.SpellList;
import data.componentdata.DamageTaken;
import data.componentdata.Health;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class HealthPlugin implements  IEntityProcessingService {

    float regenTime = 4;
    float regenTimer = 0;


    @Override
    public void process(GameData data, World world, Netherworld netherworld)
    {
        for (Entity e : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
            Health health = e.get(Health.class);

            if (health.getHp() > 0 && !health.getDamageTaken().isEmpty()) {
                for (DamageTaken dtaken : health.getDamageTaken()) {
                    int dmg = dtaken.getDamage();
                    health.setHp(health.getHp() - dmg);

                    if (health.getHp() <= 0) {
                        e.setCharState(CharacterState.DEAD);
                    }
                }
                health.getDamageTaken().clear();

            }
            regen(e, data);
        }
    }

    public int getDamage(SpellType spellType)
    {
        switch (spellType) {
            case FIREBALL:
                return SpellList.FIREBALL_DMG;
        }
        return 0;
    }

    private void regen(Entity e, GameData data)
    {
        regenTimer += data.getDelta();
        if (regenTimer >= regenTime) {
            if (e.get(Health.class).getHp() < e.get(Health.class).getMaxHp() && e.get(Health.class).getHp() > 0) {
                e.get(Health.class).setHp(e.get(Health.class).getHp() + 1);
            }
            regenTimer = 0;
        }
    }


}
