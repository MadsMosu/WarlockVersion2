/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.movementsystem;

import States.CharacterState;
import static States.CharacterState.MOVING;
import States.MovementState;
import com.badlogic.gdx.math.Vector2;
import data.Entity;
import data.EntityType;
import data.GameData;
import static data.GameKeys.*;
import static data.SpellType.FIREBALL;
import data.World;
import data.componentdata.AI;
import data.componentdata.Body;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.SpellInfos;
import data.componentdata.Velocity;
import java.util.Random;
import org.openide.util.lookup.ServiceProvider;
import services.IEntityProcessingService;

@ServiceProvider(service = IEntityProcessingService.class)
/**
 *
 * @author jonaspedersen
 */
public class ControlProcessor implements IEntityProcessingService {

    float startX, startY, endX, endY;
    float sStartX, sStartY, sEndX, sEndY;
    float directionX;
    float directionY;
    float sDirectionX;
    float sDirectionY;
    float distance;
    float sDistance;
    float speed;
    float dt;
    float angle;
    boolean spellIsMoving = false;

    private float changeDirection = 2f;
    private float directionTimer = 0f;

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(EntityType.PLAYER)) {
            handleMoveClick(entity, gameData);
            handleTargetClick(entity, gameData);
            handleShoot(entity, gameData, world);

        }
        for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
            handleAiMovement(gameData, enemy);
        }
        for (Entity spell : world.getEntities(EntityType.SPELL)) {
            handleSpellMovement(spell, gameData);
        }
    }

    private void handleAiMovement(GameData gameData, Entity ai) {
        AI aiComp = ai.get(AI.class);
        Position p = ai.get(Position.class);

        if (aiComp.getClosestEntity() != null) {
            Vector2 aiPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
            Vector2 entityPosition = new Vector2(aiComp.getClosestEntity().get(Position.class).getX(), aiComp.getClosestEntity().get(Position.class).getY());
            float gap = aiPosition.dst(entityPosition);
            float Distance = (float) Math.sqrt(Math.pow(entityPosition.x - aiPosition.x, 2) + Math.pow(entityPosition.y - aiPosition.y, 2));
            float directionX = (entityPosition.x - aiPosition.x) / Distance;
            float directionY = (entityPosition.y - aiPosition.y) / Distance;
            float speed = 50;

            Velocity v = new Velocity(directionX, directionY, speed);

            ai.setAngle((float) Math.toDegrees(Math.atan2(entityPosition.y - aiPosition.y, entityPosition.x - aiPosition.x)));

            ai.setCharState(MOVING);
            setRunningState(ai.getAngle(), ai);
            if (gap < 150) {
                p.setX(p.getX() + v.getDirectionX() * v.getSpeed() * gameData.getDelta());
                p.setY(p.getY() + v.getDirectionY() * v.getSpeed() * gameData.getDelta());
            } else {
                directionTimer += gameData.getDelta();
                if (directionTimer >= changeDirection) {
                    Random rand = new Random();
                    float n = rand.nextFloat();
                    v.setDirectionX(v.getDirectionX() * n);
                    v.setDirectionY(v.getDirectionY() * n);
                    directionTimer = 0;
                }
                p.setX(p.getX() + v.getDirectionX() * v.getSpeed() * gameData.getDelta());
                p.setY(p.getY() + v.getDirectionY() * v.getSpeed() * gameData.getDelta());
            }
        }
    }

    private void bounceBack(Entity e, GameData gameData) {
        SpellInfos si = e.get(SpellInfos.class);
        Position p = e.get(Position.class);

        //needs time duration or how long distance
        if (si.isIsHit()) {
            p.setX(p.getX() + si.getHitBy().get(Velocity.class).getDirectionX() * 100 * gameData.getDelta());
            p.setY(p.getY() + si.getHitBy().get(Velocity.class).getDirectionY() * 100 * gameData.getDelta());
        }
    }

    private void handleSpellMovement(Entity spell, GameData gameData) {
        Position sp = spell.get(Position.class);
        SpellInfos si = spell.get(SpellInfos.class);
        if (!si.isMoving()) {
            sStartX = sp.getX();
            sStartY = sp.getY();
            sEndX = gameData.getMousePositionX();
            sEndY = gameData.getMousePositionY();
            float angle = (float) Math.toDegrees(Math.atan2(sEndY - sStartY, sEndX - sStartX));
            spell.setAngle(angle);
            sDistance = (float) Math.sqrt(Math.pow(sEndX - sStartX, 2) + Math.pow(sEndY - sStartY, 2));

            sDirectionX = (sEndX - sStartX) / sDistance;
            sDirectionY = (sEndY - sStartY) / sDistance;
            sp.setX(sStartX);
            sp.setY(sStartY);
            si.setIsMoving(true);

        }
        if (si.isMoving()) {
            sp.setX(sp.getX() + sDirectionX * spell.getMaxSpeed() * gameData.getDelta());
            sp.setY(sp.getY() + sDirectionY * spell.getMaxSpeed() * gameData.getDelta());
        }
    }

    private void handleMoveClick(Entity e, GameData gameData) {
        Position p = e.get(Position.class);
        Body b = e.get(Body.class);
        if (gameData.getKeys().isPressed(RIGHT_MOUSE)) {
            gameData.getKeys().setKey(RIGHT_MOUSE, false);

            startX = p.getX();
            startY = p.getY();
            endX = gameData.getMousePositionX() - (b.getWidth() / 2);
            endY = gameData.getMousePositionY() - (b.getHeight() / 2);

            if (startX == endX && startY == endY) {
                e.setCharState(CharacterState.IDLE);
            } else {
                angle = (float) Math.toDegrees(Math.atan2(endY - startY, endX - startX));
                e.setAngle(angle);
                speed = 100;
                distance = (float) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

                directionX = (endX - startX) / distance;
                directionY = (endY - startY) / distance;
                p.setX(startX);
                p.setY(startY);
                e.setCharState(CharacterState.MOVING);
                setRunningState(e.getAngle(), e);
            }

        }
        if (e.getCharState().equals(CharacterState.MOVING)) {
            p.setX(p.getX() + directionX * speed * gameData.getDelta());
            p.setY(p.getY() + directionY * speed * gameData.getDelta());
            if ((float) Math.sqrt(Math.pow(p.getX() - startX, 2) + Math.pow(p.getY() - startY, 2)) >= distance) {
                speed = 0;
                p.setX(endX);
                p.setY(endY);
                e.setCharState(CharacterState.IDLE);
                setStandingState(e.getAngle(), e);
            }
        }
        if (gameData.getKeys().isPressed(ESCAPE)) {
            //leGameMenu.plsShowUp();

        }
        if (gameData.getKeys().isPressed(Q)) {
            //lePotion.plsDrink();
        }
    }

    private void handleShoot(Entity e, GameData gameData, World world) {
        if (gameData.getKeys().isDown(LEFT_MOUSE) && e.get(SpellBook.class).getChosenSpell() != null) {
            setStandingState(e.getAngle(), e);
            e.setCharState(CharacterState.CASTING);

        }
    }

    private void setRunningState(float angle, Entity e) {
        if (angle > -45 && angle < 45) {
            e.setMoveState(MovementState.RUNNINGRIGHT);
        } else if (angle < 135 && angle > 45) {
            e.setMoveState(MovementState.RUNNINGUP);
        } else if (angle > -135 && angle < -45) {
            e.setMoveState(MovementState.RUNNINGDOWN);
        } else {
            e.setMoveState(MovementState.RUNNINGLEFT);
        }
    }

    private void setStandingState(float angle, Entity e) {
        if (angle > -45 && angle < 45) {
            e.setMoveState(MovementState.STANDINGRIGHT);
        } else if (angle < 135 && angle > 45) {
            e.setMoveState(MovementState.STANDINGUP);
        } else if (angle > -135 && angle < -45) {
            e.setMoveState(MovementState.STANDINGDOWN);
        } else {
            e.setMoveState(MovementState.STANDINGLEFT);
        }
    }

    private void handleTargetClick(Entity e, GameData gameData) {
        if (gameData.getKeys().isPressed(NUM_1)) {
            SpellBook sb = e.get(SpellBook.class);
            sb.setChosenSpell(FIREBALL);

        } else {
            return;
        }
        if (gameData.getKeys().isPressed(NUM_2)) {
            //e.setChosenSpell(SpellType.SPELL2);
            System.out.println("Frostbolt chosen");

        }
        if (gameData.getKeys().isPressed(NUM_3)) {
            //Spell 3

        }
        if (gameData.getKeys().isPressed(NUM_4)) {
            //Spell 4

        }

    }

}
