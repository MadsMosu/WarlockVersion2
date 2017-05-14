package ai;

import static States.CharacterState.CASTING;
import com.badlogic.gdx.math.Vector2;
import data.Entity;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import static data.EntityType.SPELL;
import data.GameData;
import data.World;
import data.componentdata.AI;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.SpellInfos;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})

public class AIPlugin implements IEntityProcessingService, IGamePluginService {

    private Map<Entity, Float> closeBy;

    @Override
    public void start(GameData gameData, World world) {

    }

    private boolean inDistance(World world, Entity ai) {
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                Vector2 enemyPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
                Vector2 entityPosition = new Vector2(entity.get(Position.class).getX(), entity.get(Position.class).getY());
                float distance = enemyPosition.dst(entityPosition);
                if (distance <= 100) {
                    return true;
                }
            }
        }
        return false;
    }

    private Map<Entity, Float> whoIsClose(World world, Entity ai) {
        closeBy = new HashMap();
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                Vector2 enemyPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
                Vector2 entityPosition = new Vector2(entity.get(Position.class).getX(), entity.get(Position.class).getY());
                float distance = enemyPosition.dst(entityPosition);
                if (distance <= 100) {
                    closeBy.put(entity, distance);
                }
            }
        }
        return null;
    }

    private Entity closestBy(World world, Entity ai) {
        Entry<Entity, Float> min = null;
        for (Entry<Entity, Float> entry : whoIsClose(world, ai).entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        return min.getKey();
    }
    
    public Entity detectIncommingSpell(World world, Entity ai){
        AI aiComp = ai.get(AI.class);
        for (Entity spell : world.getEntities(SPELL)) {
                Vector2 enemyPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
                Vector2 spellPosition = new Vector2(spell.get(Position.class).getX(), spell.get(Position.class).getY());
                float distance = enemyPosition.dst(spellPosition);
                if (distance <= 30) {
                  aiComp.setAvoidSpell(true);
                } else aiComp.setAvoidSpell(false);
        }
        return null;
    }
    
    private void wander(Entity ai){
        
    }
    
    private void chooseSpell(Entity ai){
        SpellBook sb = ai.get(SpellBook.class);
        SpellInfos si = ai.get(SpellInfos.class);
        Random random = new Random();
        int randNumb = random.nextInt(sb.getSpells().size());
        
        si.setChosenSpell(sb.getSpells().get(randNumb));
    }

    private void behaviour(World world, Entity ai) {
        if(inDistance(world, ai)){
            AI aiComp = ai.get(AI.class);
            ai.setCharState(CASTING);
            aiComp.isAttackingWho(closestBy(world, ai));
            chooseSpell(ai);
        } else {
            wander(ai);
        }
    }

    @Override
    public void process(GameData gameData, World world) {
                for (Entity enemy : world.getEntities(ENEMY)) {
            behaviour(world, enemy);
        }

    }

    @Override
    public void stop() {

    }

}
