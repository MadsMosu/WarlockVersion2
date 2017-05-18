package ai;

import States.AiStateMachine;
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

    private boolean tooFarDistance(Entity ai, Entity opponent, float distanceValue) {
        Vector2 aiPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
        Vector2 entityPosition = new Vector2(opponent.get(Position.class).getX(), opponent.get(Position.class).getY());
        return aiPosition.dst(entityPosition) > distanceValue;
    }

    private void detectEnemies(World world, Entity ai) {
        AI aiComp = ai.get(AI.class);
        aiComp.getAllEntities().clear();
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

    private void detectEnemiesHealth(World world, Entity ai) {
        AI aiComp = ai.get(AI.class);
        aiComp.getAllEntities().clear();
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                aiComp.getAllEntitiesHealth().put(entity, entity.get(Health.class).getHp());
            }
        }
    }

    private Entity lowestDistance(Map<Entity, Float> map) {
        Entry<Entity, Float> min = null;
        for (Entry<Entity, Float> entry : map.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        return min.getKey();
    }

    private Entity lowestHealth(Map<Entity, Double> map) {
        Entry<Entity, Double> min = null;
        for (Entry<Entity, Double> entry : map.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }
        return min.getKey();
    }

    private void chase(Entity ai, Entity opponent) {
        AI aiComp = ai.get(AI.class);
        AI opponentAI = opponent.get(AI.class);
        aiComp.setCurrentTarget(opponent);
        aiComp.setState(AiStateMachine.CHASING);
        if (opponent.isType(ENEMY)) {
            opponentAI.setChasedBy(ai);
        }
    }

    private void avoidSpell(Entity ai, World world) {
        AI aiComp = ai.get(AI.class);

        aiComp.setState(AiStateMachine.AVOIDINGSPELL);
        aiComp.setSpellToAvoid(lowestDistance(aiComp.getCloseSpells()));
    }

    private void attack(Entity ai, World world) {
        AI aiComp = ai.get(AI.class);
        SpellBook sb = ai.get(SpellBook.class);
        //Random random = new Random();
        //int randNumb = random.nextInt(sb.getSpells().size());

        //sb.setChosenSpell(sb.getSpells().get(randNumb));
        sb.setChosenSpell(FIREBALL);
        ai.setCharState(CASTING);
        aiComp.setCurrentTarget(lowestDistance(aiComp.getAllEntities()));
    }

    private void changeTarget(Entity ai, World world, Entity newTarget) {
        AI aiComp = ai.get(AI.class);
        if (aiComp.getChasedBy() != null) {
            newTarget = aiComp.getChasedBy();
        } else {
            newTarget = lowestHealth(aiComp.getAllEntitiesHealth());
        }
    }

    private void clearRadar(Entity ai) {
        AI aiComp = ai.get(AI.class);
        aiComp.getAllEntities().clear();
        aiComp.getAllEntitiesHealth().clear();
        aiComp.getCloseSpells().clear();
    }

    private void raderScan(World world, Entity ai) {
        detectEnemies(world, ai);
        detectIncommingSpells(world, ai, 70);
        detectEnemiesHealth(world, ai);
    }

    private void behaviour(World world, Entity ai) {
        AI aiComp = ai.get(AI.class);
        clearRadar(ai);
        raderScan(world, ai);

        if (aiComp.getCurrentTarget() == null) {
            aiComp.setCurrentTarget(lowestDistance(aiComp.getAllEntities()));
        }

        if (!aiComp.getState().equals(AiStateMachine.INLAVA)) {

            if (opponentInDistance(world, ai, 200)) {
                attack(ai, world);
            } else {
                chase(ai, lowestDistance(aiComp.getAllEntities()));
            }
            if (SpellInDistance(world, ai, 70)) {
                avoidSpell(ai, world);
            }
            if (tooFarDistance(ai, aiComp.getCurrentTarget(), 300)) {
                changeTarget(ai, world, aiComp.getCurrentTarget());
            }
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
