package ai;

import States.CharacterState;
import data.Entity;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import static data.EntityType.SPELL;
import data.GameData;
import data.Netherworld;
import static data.SpellType.FIREBALL;
import data.World;
import data.componentdata.AI;
import data.componentdata.Body;
import data.componentdata.Health;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.Velocity;
import data.util.Vector2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),
    @ServiceProvider(service = IGamePluginService.class)
})

public class AIPlugin implements IEntityProcessingService, IGamePluginService {

    Map<Entity, List<Entity>> incomingSpells;

    @Override
    public void start(GameData gameData, World world)
    {
        incomingSpells = new HashMap();
    }

    private boolean opponentInDistance(World world, Entity ai, float distanceValue)
    {
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                Vector2 direction = new Vector2(ai.get(Position.class), entity.get(Position.class));
                float distance = direction.getMagnitude();
                if (distance <= distanceValue) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean SpellInDistance(World world, Entity ai, float distanceValue)
    {
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                Vector2 direction = new Vector2(ai.get(Position.class), entity.get(Position.class));

                float distance = direction.getMagnitude();
                if (distance <= distanceValue) {
                    return true;
                }
            }
        }
        return false;
    }

    private float getDistance(Entity ai, Entity opponent)
    {
        Vector2 direction = new Vector2(ai.get(Position.class), opponent.get(Position.class));
        return direction.getMagnitude();
    }

    private void detectEnemies(World world, Entity ai)
    {
        AI aiComp = ai.get(AI.class);
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                Vector2 direction = new Vector2(ai.get(Position.class), entity.get(Position.class));
                float distance = direction.getMagnitude();
                aiComp.getAllEntities().put(entity, distance);
            }
        }
    }

    public void detectIncommingSpells(World world, Entity ai, float distanceValue)
    {

        for (Entity AI : world.getEntities(ENEMY)) {
            List<Entity> spellsToAvoid = new ArrayList();

            for (Entity spell : world.getEntities(SPELL)) {

                Vector2 spellDirection = new Vector2(spell.get(Velocity.class).getVector());

                if (!spell.get(Owner.class).getID().equals(ai.getID())) {
                    if (spellDirection.isOnLine(ai.get(Velocity.class).getVector())) {
                        spellsToAvoid.add(spell);
                    }
                }

            }
            incomingSpells.put(AI, spellsToAvoid);
        }
    }

    private void detectEnemiesHealthInDist(World world, Entity ai, float distanceValue)
    {
        AI aiComp = ai.get(AI.class);
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai) && opponentInDistance(world, ai, distanceValue)) {
                aiComp.getEntitiesHealthInDist().put(entity, entity.get(Health.class).getHp());
            }
        }
    }

    private Entity lowestValue(Map<Entity, Float> map)
    {
        Entry<Entity, Float> min = null;
        if (!map.isEmpty()) {
            for (Entry<Entity, Float> entry : map.entrySet()) {
                if (min == null || min.getValue() > entry.getValue()) {
                    min = entry;
                }
            }
            return min.getKey();
        }
        return null;
    }

    private void avoidSpells(Entity ai, World world)
    {
        List<Entity> collidingSpells = incomingSpells.get(ai);

        for (Entity spell : collidingSpells) {
            
            Velocity v = ai.get(Velocity.class);
            Vector2 spellDirection = new Vector2(spell.get(Velocity.class).getVector());
            Vector2 dodgeVector = spellDirection.rotateDegrees(80);
            v.setTravelDist(dodgeVector.getMagnitude());
            v.setVector(dodgeVector.setMagnitude(100));
            ai.setCharState(CharacterState.MOVING);
            
            
            //System.out.println("Spell is at positions y valuejhjjjj");
        }

    }

//        AI aiComp = ai.get(AI.class);
//        if (SpellInDistance(world, ai, 400)) {
//
//        }
//        aiComp.setAvoidSpell(true);
////        for (Entry<Entity, Float> entry : aiComp.getCloseSpells().entrySet()) {
////            if(entry.getKey().get(Velocity.class).getDirectionX()){
////                aiComp.setSpellToAvoid(lowestDistance(aiComp.getCloseSpells()));               
////            }
////        }
//        aiComp.setSpellToAvoid(null);
    private boolean checkForSameHP(Map<Entity, Float> HPmap)
    {
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

    private void attack(World world, Entity ai)
    {
        AI aiComp = ai.get(AI.class);
        SpellBook sb = ai.get(SpellBook.class);
        if (opponentInDistance(world, ai, 400) && !ai.get(Position.class).isInLava()) {
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

    private void clearRadar(Entity ai)
    {
        AI aiComp = ai.get(AI.class
        );
        aiComp.getAllEntities().clear();
        aiComp.getCloseSpells().clear();
        aiComp.getEntitiesHealthInDist().clear();
    }

    private void radarScan(World world, Entity ai)
    {
        detectEnemies(world, ai);
        detectIncommingSpells(world, ai, 400);
        detectEnemiesHealthInDist(world, ai, 200);

    }

    private void behaviour(World world, Entity ai)
    {
        clearRadar(ai);
        radarScan(world, ai);

        avoidSpells(ai, world);
        attack(world, ai);

    }

    @Override
    public void process(GameData gameData, World world, Netherworld netherWorld)
    {
        for (Entity ai : world.getEntities(ENEMY)) {
            behaviour(world, ai);
        }

    }

    @Override
    public void stop()
    {

    }

}
