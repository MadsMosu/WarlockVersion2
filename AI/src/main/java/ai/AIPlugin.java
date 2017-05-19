package ai;

import static States.CharacterState.CASTING;
import com.badlogic.gdx.math.Vector2;
import data.Entity;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import static data.EntityType.SPELL;
import data.GameData;
import static data.SpellType.FIREBALL;
import data.World;
import data.componentdata.AI;
import data.componentdata.Health;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.Velocity;
import java.util.Map;
import java.util.Map.Entry;
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

    @Override
    public void start(GameData gameData, World world) {

    }

    private boolean opponentInDistance(World world, Entity ai, float distanceValue) {
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                Vector2 aiPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
                Vector2 entityPosition = new Vector2(entity.get(Position.class).getX(), entity.get(Position.class).getY());
                float distance = aiPosition.dst(entityPosition);
                if (distance <= distanceValue) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean SpellInDistance(World world, Entity ai, float distanceValue) {
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                Vector2 aiPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
                Vector2 entityPosition = new Vector2(entity.get(Position.class).getX(), entity.get(Position.class).getY());
                float distance = aiPosition.dst(entityPosition);
                if (distance <= distanceValue) {
                    return true;
                }
            }
        }
        return false;
    }

    private float getDistance(Entity ai, Entity opponent) {
        Vector2 aiPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
        Vector2 entityPosition = new Vector2(opponent.get(Position.class).getX(), opponent.get(Position.class).getY());
        return aiPosition.dst(entityPosition);
    }

    private void detectEnemies(World world, Entity ai) {
        AI aiComp = ai.get(AI.class);
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                Vector2 aiPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
                Vector2 entityPosition = new Vector2(entity.get(Position.class).getX(), entity.get(Position.class).getY());
                float distance = aiPosition.dst(entityPosition);
                aiComp.getAllEntities().put(entity, distance);
            }
        }
    }

    public void detectIncommingSpells(World world, Entity ai, float distanceValue) {
        AI aiComp = ai.get(AI.class);
        for (Entity spell : world.getEntities(SPELL)) {
            Vector2 aiPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
            Vector2 spellPosition = new Vector2(spell.get(Position.class).getX(), spell.get(Position.class).getY());
            float distance = aiPosition.dst(spellPosition);
            if (distance <= distanceValue) {
                aiComp.getCloseSpells().put(spell, distance);
            }
        }
    }

    private void detectEnemiesHealthInDist(World world, Entity ai, float distanceValue) {
        AI aiComp = ai.get(AI.class);
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai) && opponentInDistance(world, ai, distanceValue)) {
                aiComp.getEntitiesHealthInDist().put(entity, entity.get(Health.class).getHp());
            }
        }
    }

    private Entity lowestValue(Map<Entity, Float> map) {
        Entry<Entity, Float> min = null;
        for (Entry<Entity, Float> entry : map.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        return min.getKey();
    }

    private void avoidSpells(World world, Entity ai) {
        AI aiComp = ai.get(AI.class);
        if (SpellInDistance(world, ai, 150)) {

        }
        aiComp.setAvoidSpell(true);
//        for (Entry<Entity, Float> entry : aiComp.getCloseSpells().entrySet()) {
//            if(entry.getKey().get(Velocity.class).getDirectionX()){
//                aiComp.setSpellToAvoid(lowestDistance(aiComp.getCloseSpells()));               
//            }
//        }
        aiComp.setSpellToAvoid(null);
    }

    private boolean checkForSameHP(Map<Entity, Float> HPmap) {
        Object value = null;
        for (Object entry : HPmap.values()) {
            if (value == null) {
                value = entry;
            } else if (!value.equals(entry)) {
                return false;
            }
        }
        return true;
    }

    private void attack(World world, Entity ai) {
        AI aiComp = ai.get(AI.class);
        SpellBook sb = ai.get(SpellBook.class);
        if (opponentInDistance(world, ai, 200) && !ai.get(Position.class).isInLava()) {
            sb.setChosenSpell(FIREBALL);        
            if (checkForSameHP(aiComp.getEntitiesHealthInDist())) {
                aiComp.setCurrentTarget(lowestValue(aiComp.getAllEntities()));
            } else {
                aiComp.setCurrentTarget(lowestValue(aiComp.getEntitiesHealthInDist()));
            }
        } else {
            aiComp.setCurrentTarget(lowestValue(aiComp.getAllEntities()));
            if (opponentInDistance(world, ai, 200)) {
                sb.setChosenSpell(FIREBALL);
            }
        }
    }

    private void clearRadar(Entity ai) {
        AI aiComp = ai.get(AI.class
        );
        aiComp.getAllEntities().clear();
        aiComp.getCloseSpells().clear();
        aiComp.getEntitiesHealthInDist().clear();
    }

    private void raderScan(World world, Entity ai) {
        detectEnemies(world, ai);
        detectIncommingSpells(world, ai, 150);
        detectEnemiesHealthInDist(world, ai, 200);

    }

    private void behaviour(World world, Entity ai) {
        clearRadar(ai);
        raderScan(world, ai);

            avoidSpells(world, ai);
            attack(world, ai);
        
    }

    @Override
    public void process(GameData gameData, World world) {
        for (Entity ai : world.getEntities(ENEMY)) {
            behaviour(world, ai);
        }

    }

    @Override
    public void stop() {

    }

}
