package ai;

import data.util.PQHeap;
import data.util.EntityValue;
import States.CharacterState;
import States.MovementState;
import States.StateMachine;
import data.Entity;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import static data.EntityType.SPELL;
import data.GameData;
import data.Netherworld;
import static data.SpellType.FIREBALL;
import data.World;
import data.componentdata.AI;
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
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})

public class AIPlugin implements IEntityProcessingService, IGamePluginService {

    Map<Entity, List<Entity>> incomingSpells;

    @Override
    public void start(GameData gameData, World world) {
        incomingSpells = new HashMap();
    }

    private boolean opponentInDistance(World world, Entity ai, float distanceValue) {
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

    private void detectEnemies(World world, Entity ai) {
        AI aiComp = ai.get(AI.class);
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai)) {
                Vector2 direction = new Vector2(ai.get(Position.class), entity.get(Position.class));
                float distance = direction.getMagnitude();
                aiComp.getAllEntities().put(entity, distance);
            }
        }
    }

    public void detectIncommingSpells(World world, Entity ai, float distanceValue) {

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

    private void detectEnemiesHealthInDist(World world, Entity ai, float distanceValue) {
        AI aiComp = ai.get(AI.class);
        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            if (!entity.equals(ai) && opponentInDistance(world, ai, distanceValue)) {
                aiComp.getEntitiesHealthInDist().put(entity, entity.get(Health.class).getHp());
            }
        }
    }

    private Entity lowestValue(Map<Entity, Float> map) {
        PQHeap pq = new PQHeap(map.size());

        if (!map.isEmpty()) {

            for (Entry<Entity, Float> entry : map.entrySet()) {
                pq.insert(new EntityValue(entry.getValue()));
            }
            if (!pq.getQueue().isEmpty()) {

                for (Entry<Entity, Float> entry : map.entrySet()) {
                    float value = pq.extractMin().value;
                    if (value == entry.getValue()) {
                        return entry.getKey();
                    }
                }
            }
        }
        return null;
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

    private void clearRadar(Entity ai) {
        AI aiComp = ai.get(AI.class);
        aiComp.getAllEntities().clear();
        aiComp.getEntitiesHealthInDist().clear();
        if (incomingSpells.get(ai) != null) {
            incomingSpells.get(ai).clear();
        }
    }

    private void radarScan(World world, Entity ai) {
        detectEnemies(world, ai);
        detectIncommingSpells(world, ai, 400);
        detectEnemiesHealthInDist(world, ai, 200);

    }

    private void stateProcessor(Entity ai, World world, Position p, AI aiComp) {
        if ((opponentInDistance(world, ai, 400) && !p.isInLava())) {
            aiComp.setState(StateMachine.ATTACK);
        } else if (p.isInLava() || !incomingSpells.get(ai).isEmpty()) {
            aiComp.setState(StateMachine.EVADE);
        } else if (!p.isInLava()) {
            aiComp.setState(StateMachine.CHASE);
        }
    }

    private void handleBouncing(Entity ai, Position p, Velocity v) {
        Vector2 stopCheck = new Vector2(p, p.getStartPosition());
        if (v.getTravelDist() <= stopCheck.getMagnitude()) {
            ai.setCharState(CharacterState.IDLE);
            ai.setMoveState(MovementState.STANDING);
        }
    }

    private void handleChasing(Entity ai, AI aiComp, Position p, Velocity v) {
        aiComp.setCurrentTarget(lowestValue(aiComp.getAllEntities()));

        if (aiComp.getCurrentTarget() != null) {
            Position aiPosition = new Position(p);
            Position entityPosition = new Position(aiComp.getCurrentTarget().get(Position.class));
            Vector2 direction = new Vector2(aiPosition, entityPosition);
            v.setVector(direction);
            v.getVector().normalize();

            ai.setCharState(CharacterState.MOVING);
        }
    }

    private void setSpriteAngle(Entity e, Velocity v) {
        e.setAngle(v.getVector().getAngle());
        e.setRunningState(e.getAngle(), e);
    }

    private void handleEvade(Entity ai, Velocity v, Position p) {
        List<Entity> collidingSpells = incomingSpells.get(ai);
        for (Entity spell : collidingSpells) {
            Vector2 spellDirection = new Vector2(spell.get(Velocity.class).getVector());
            Vector2 dodgeVector = spellDirection.rotateDegrees(80);
            v.setVector(dodgeVector.setMagnitude(100));
            v.setTravelDist(dodgeVector.getMagnitude());
            v.getVector().normalize();
            ai.setCharState(CharacterState.MOVING);

            p.setStartPosition(new Position(p));
            Vector2 stopCheck = new Vector2(p, p.getStartPosition());
            if (v.getTravelDist() <= stopCheck.getMagnitude()) {
                ai.setCharState(CharacterState.IDLE);
                ai.setMoveState(MovementState.STANDING);
            }
        }
    }

    private void handleInLava(Entity ai, GameData gameData, Position p, Velocity v) {
        Position middle = new Position(gameData.getMapWidth() / 2, 0);
        Vector2 directionToMiddle = new Vector2(new Position(p), middle);
        //float distanceToMiddle = directionToMiddle.getMagnitude();

        ai.setAngle((float) directionToMiddle.getAngle());
        ai.setRunningState(ai.getAngle(), ai);

        v.setVector(directionToMiddle);
        v.getVector().normalize();
        if (ai.getCharState().equals(CharacterState.IDLE)) {
            ai.setCharState(CharacterState.MOVING);
        }
    }

    private void handleAttacking(Entity ai, SpellBook sb, AI aiComp) {
        ai.setMoveState(MovementState.STANDING);

        if (checkForSameHP(aiComp.getEntitiesHealthInDist())) {
            aiComp.setCurrentTarget(lowestValue(aiComp.getAllEntities()));
        } else {
            aiComp.setCurrentTarget(lowestValue(aiComp.getEntitiesHealthInDist()));
        }
        sb.setChosenSpell(FIREBALL);
    }

    private void behaviour(World world, GameData gameData, Entity ai) {
        AI aiComp = ai.get(AI.class);
        Position p = ai.get(Position.class);
        Velocity v = ai.get(Velocity.class);
        SpellBook sb = ai.get(SpellBook.class);

        clearRadar(ai);
        radarScan(world, ai);
        if (!ai.getCharState().equals(CharacterState.BOUNCING)) {
            stateProcessor(ai, world, p, aiComp);
        }
        if (ai.getCharState().equals(CharacterState.BOUNCING)) {
            handleBouncing(ai, p, v);
        }
        if (aiComp.getState() == StateMachine.EVADE) {
            handleEvade(ai, v, p);
        }
        if (p.isInLava() && !ai.getCharState().equals(CharacterState.BOUNCING)) {
            handleInLava(ai, gameData, p, v);
        }
        if (!p.isInLava()) {
            if (aiComp.getState() == StateMachine.ATTACK) {
                handleAttacking(ai, sb, aiComp);
            }

            if (aiComp.getState() == StateMachine.CHASE) {
                handleChasing(ai, aiComp, p, v);
            }
        }
        setSpriteAngle(ai, v);
    }

    @Override
    public void process(GameData gameData, World world, Netherworld netherWorld) {
        for (Entity ai : world.getEntities(ENEMY)) {
            behaviour(world, gameData, ai);
        }
    }

    @Override
    public void stop() {

    }

}
