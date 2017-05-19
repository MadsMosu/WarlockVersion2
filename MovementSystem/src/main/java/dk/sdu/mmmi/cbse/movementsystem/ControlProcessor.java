/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.movementsystem;

import States.CharacterState;
import static States.CharacterState.MOVING;
import States.MovementState;
import data.Entity;
import data.EntityType;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import data.GameData;
import static data.GameKeys.*;
import static data.SpellType.FIREBALL;
import data.World;
import data.componentdata.AI;
import data.componentdata.Body;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.SpellInfos;
import data.componentdata.Velocity;
import data.util.Vector2;
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

        }
        for (Entity enemy : world.getEntities(EntityType.ENEMY)) {
            handleAiMovement(gameData, enemy);
        }
        for (Entity spell : world.getEntities(EntityType.SPELL)) {
            Position p = spell.get(Position.class);
            if (spell.get(Owner.class).getOwnerType().equals(PLAYER)) {
                handleSpellMovement(spell, gameData, p.getX(), p.getY(), gameData.getMousePositionX(), gameData.getMousePositionY());
            }
            if (spell.get(Owner.class).getOwnerType().equals(ENEMY)) {
                Body target = spell.get(Owner.class).getOwnerEntity().get(AI.class).getCurrentTarget().get(Body.class);
                handleSpellMovement(spell, gameData, p.getX(), p.getY(), target.getWidth() / 2, target.getHeight() / 2);
            }
        }
    }

    private void handleAiMovement(GameData gameData, Entity ai) {
        AI aiComp = ai.get(AI.class);
        Position p = ai.get(Position.class);
        Velocity v = ai.get(Velocity.class);

        if (aiComp.getCurrentTarget() != null) {
            Position aiPosition = new Position(ai.get(Position.class).getX(), ai.get(Position.class).getY());
            Position entityPosition = new Position(aiComp.getCurrentTarget().get(Position.class).getX(), aiComp.getCurrentTarget().get(Position.class).getY());
            Vector2 gap = new Vector2(aiPosition,entityPosition);
            float aiDistance = (float)gap.getMagnitude();
            gap.normalize();

            if (p.isInLava()) {
                Position middle = new Position(gameData.getMapWidth()/2, gameData.getMapHeight()/2);
                Vector2 distanceToMiddle = new Vector2(aiPosition, middle);
                Vector2 angle = new Vector2(middle, aiPosition);
                ai.setAngle((float)angle.getAngle());
       
                setRunningState(ai.getAngle(), ai);

                distanceToMiddle.normalize();


                p.setX(p.getX() + v.getDirectionX() * v.getSpeed() * gameData.getDelta());
                p.setY(p.getY() + v.getDirectionY() * v.getSpeed() * gameData.getDelta());
            } else {
                if (gap.getMagnitude() >= 100) {
                    if (gap.getMagnitude() >= 100 && gap.getMagnitude() < 101) {
                        ai.setMoveState(MovementState.STANDING);
                    } else {
                        ai.setAngle((float) Math.toDegrees(Math.atan2(entityPosition.y - aiPosition.y, entityPosition.x - aiPosition.x)));
                        setRunningState(ai.getAngle(), ai);
                        p.setX(p.getX() + v.getDirectionX() * v.getSpeed() * gameData.getDelta());
                        p.setY(p.getY() + v.getDirectionY() * v.getSpeed() * gameData.getDelta());
                    }
                }
            }
        }
    }

    private void handleSpellMovement(Entity spell, GameData gameData, float startX, float startY, float endX, float endY) {
        Position sp = spell.get(Position.class);
        SpellInfos si = spell.get(SpellInfos.class);
        Velocity v = spell.get(Velocity.class);
        if (!si.isMoving()) {
            spell.setAngle((float) Math.toDegrees(Math.atan2(endY - startY, endX - startX)));
            float sDistance = (float) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

            v.setDirectionX((endX - startX) / sDistance);
            v.setDirectionY((endY - startY) / sDistance);
            sp.setX(startX);
            sp.setY(startY);
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
        if (e.getCharState().equals(CharacterState.BOUNCING)) {
            
        } else if (e.getCharState().equals(CharacterState.MOVING)) {
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

}
