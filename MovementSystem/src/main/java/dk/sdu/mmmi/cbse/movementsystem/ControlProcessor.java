/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.movementsystem;

import States.AiStateMachine;
import States.CharacterState;
import static States.CharacterState.MOVING;
import States.MovementState;
import com.badlogic.gdx.math.Vector2;
import data.Entity;
import data.EntityType;
import data.GameData;
import static data.GameKeys.*;
import data.ImageManager;
import data.SpellType;
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

    private float startX, startY;
    private float endX, endY;
    private float distance;

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
        Velocity v = ai.get(Velocity.class);

        if (aiComp.getCurrentTarget() != null) {
            Vector2 aiPosition = new Vector2(ai.get(Position.class).getX(), ai.get(Position.class).getY());
            Vector2 entityPosition = new Vector2(aiComp.getCurrentTarget().get(Position.class).getX(), aiComp.getCurrentTarget().get(Position.class).getY());
            float gap = aiPosition.dst(entityPosition);
            float aiDistance = (float) Math.sqrt(Math.pow(entityPosition.x - aiPosition.x, 2) + Math.pow(entityPosition.y - aiPosition.y, 2));
            v.setDirectionX((entityPosition.x - aiPosition.x) / aiDistance);
            v.setDirectionY((entityPosition.y - aiPosition.y) / aiDistance);

            ai.setCharState(MOVING);

            if (aiComp.getState().equals(AiStateMachine.INLAVA)) {
                float distanceToMiddle = (float) Math.sqrt(Math.pow(gameData.getMapWidth() / 2 - aiPosition.x, 2) + Math.pow(gameData.getMapHeight() / 2 - aiPosition.y, 2));
                ai.setAngle((float) Math.toDegrees(Math.atan2(gameData.getMapHeight() / 2 - aiPosition.y, gameData.getMapWidth() / 2 - aiPosition.x)));
                setRunningState(ai.getAngle(), ai);

                v.setDirectionX((gameData.getMapWidth() / 2 - aiPosition.x) - distanceToMiddle);
                v.setDirectionY((gameData.getMapHeight() / 2 - aiPosition.y) - distanceToMiddle);

                p.setX(p.getX() + v.getDirectionX() * v.getSpeed() * gameData.getDelta());
                p.setY(p.getY() + v.getDirectionY() * v.getSpeed() * gameData.getDelta());
            } else {
                if (gap > 200) {
                    ai.setAngle((float) Math.toDegrees(Math.atan2(entityPosition.y - aiPosition.y, entityPosition.x - aiPosition.x)));
                    setRunningState(ai.getAngle(), ai);

                    p.setX(p.getX() + v.getDirectionX() * v.getSpeed() * gameData.getDelta());
                    p.setY(p.getY() + v.getDirectionY() * v.getSpeed() * gameData.getDelta());
                } else {
                    ai.setAngle((float) Math.toDegrees(Math.atan2(aiPosition.y - entityPosition.y, aiPosition.x - entityPosition.x)));
                    setRunningState(ai.getAngle(), ai);

                    p.setX(p.getX() - v.getDirectionX() * v.getSpeed() * gameData.getDelta());
                    p.setY(p.getY() - v.getDirectionY() * v.getSpeed() * gameData.getDelta());

                }
            }
            if (aiComp.getState().equals(AiStateMachine.AVOIDINGSPELL)) {
                float distanceToMove = (float) Math.sqrt(Math.pow(aiPosition.x + 20 - aiPosition.x, 2) + Math.pow(aiPosition.y + 20 - aiPosition.y, 2));
                v.setDirectionX((aiPosition.x + 20 - aiPosition.x) / distanceToMove);
                v.setDirectionY((aiPosition.y + 20 - aiPosition.y) / distanceToMove);

                ai.setAngle((float) Math.toDegrees(Math.atan2(aiPosition.y + 20 - aiPosition.y, aiPosition.x + 20 - aiPosition.x)));
                setRunningState(ai.getAngle(), ai);

                p.setX(p.getX() + v.getDirectionX() * v.getSpeed() * gameData.getDelta());
                p.setY(p.getY() + v.getDirectionY() * v.getSpeed() * gameData.getDelta());
            }
        }
    }

    private void handleSpellMovement(Entity spell, GameData gameData) {
        Position sp = spell.get(Position.class);
        SpellInfos si = spell.get(SpellInfos.class);
        Velocity v = spell.get(Velocity.class);
        if (!si.isMoving()) {
            float sStartX = sp.getX();
            float sStartY = sp.getY();
            float sEndX = gameData.getMousePositionX();
            float sEndY = gameData.getMousePositionY();
            spell.setAngle((float) Math.toDegrees(Math.atan2(sEndY - sStartY, sEndX - sStartX)));
            float sDistance = (float) Math.sqrt(Math.pow(sEndX - sStartX, 2) + Math.pow(sEndY - sStartY, 2));

            v.setDirectionX((sEndX - sStartX) / sDistance);
            v.setDirectionY((sEndY - sStartY) / sDistance);
            sp.setX(sStartX);
            sp.setY(sStartY);
            si.setIsMoving(true);

        }

        if (si.isMoving()) {

            sp.setX(sp.getX() + v.getDirectionX() * v.getSpeed() * gameData.getDelta());
            sp.setY(sp.getY() + v.getDirectionY() * v.getSpeed() * gameData.getDelta());
        }
    }

    private void handleMoveClick(Entity e, GameData gameData) {
        Position p = e.get(Position.class);
        Body b = e.get(Body.class);
        Velocity v = e.get(Velocity.class);

        if (gameData.getKeys().isPressed(RIGHT_MOUSE)) {
            gameData.getKeys().setKey(RIGHT_MOUSE, false);

            startX = p.getX();
            startY = p.getY();
            endX = gameData.getMousePositionX() - (b.getWidth() / 2);
            endY = gameData.getMousePositionY() - (b.getHeight() / 2);

            if (startX == endX && startY == endY) {
                e.setCharState(CharacterState.IDLE);
            } else {
                e.setAngle((float) Math.toDegrees(Math.atan2(endY - startY, endX - startX)));

                distance = (float) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

                v.setDirectionX((endX - startX) / distance);
                v.setDirectionY((endY - startY) / distance);
                p.setX(startX);
                p.setY(startY);
                e.setCharState(CharacterState.MOVING);
                setRunningState(e.getAngle(), e);
            }
        }
        if (e.getCharState().equals(CharacterState.MOVING)) {
            p.setX(p.getX() + v.getDirectionX() * v.getSpeed() * gameData.getDelta());
            p.setY(p.getY() + v.getDirectionY() * v.getSpeed() * gameData.getDelta());
            if ((float) Math.sqrt(Math.pow(p.getX() - startX, 2) + Math.pow(p.getY() - startY, 2)) >= distance) {
                p.setX(endX);
                p.setY(endY);
                e.setCharState(CharacterState.IDLE);
                e.setMoveState(MovementState.STANDING);
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
            e.setMoveState(MovementState.STANDING);
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
