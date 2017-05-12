/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.movementsystem;

import States.CharacterState;
import States.MovementState;
import data.Entity;
import data.EntityType;
import data.GameData;
import static data.GameKeys.*;
import data.SpellType;
import static data.SpellType.FIREBALL;
import data.World;
import data.componentdata.Body;
import data.componentdata.Position;
import data.componentdata.SpellInfos;
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
    float sAngle;
    float angle;
    boolean spellIsMoving = false;

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(EntityType.PLAYER)) {
            handleMoveClick(entity, gameData);
            handleTargetClick(entity, gameData);
            handleShoot(entity, gameData);
            
            for (Entity spell : world.getEntities(EntityType.SPELL)) {
                if (gameData.getKeys().isPressed(RIGHT_MOUSE)) {
                    Position sp = spell.get(Position.class);
                    sStartX = sp.getX();
                    sStartY = sp.getY();
                    sEndX = gameData.getMousePositionX();
                    sEndY = gameData.getMousePositionY();
                    sAngle = (float) Math.toDegrees(Math.atan2(sEndY - sStartY, sEndX - sStartX));
                    sDistance = (float) Math.sqrt(Math.pow(sEndX - sStartX, 2) + Math.pow(sEndY - sStartY, 2));

                    sDirectionX = (sEndX - sStartX) / sDistance;
                    sDirectionY = (sEndY - sStartY) / sDistance;
                    sp.setX(sStartX);
                    sp.setY(sStartY);
                    spellIsMoving = true;

                    if (spellIsMoving) {
                        sp.setX(sp.getX() + sDirectionX * spell.getMaxSpeed() * gameData.getDelta());
                        sp.setY(sp.getY() + sDirectionY * spell.getMaxSpeed() * gameData.getDelta());
                        if ((float) Math.sqrt(Math.pow(sp.getX() - sStartX, 2) + Math.pow(sp.getY() - sStartY, 2)) >= sDistance) {
                            sp.setX(sEndX);
                            sp.setY(sEndY);
                            spellIsMoving = false;
                        }
                    }
                }
            }
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
                speed = 100;
                distance = (float) Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2));

                directionX = (endX - startX) / distance;
                directionY = (endY - startY) / distance;
                p.setX(startX);
                p.setY(startY);
                e.setCharState(CharacterState.MOVING);
            }
            setRunningState(e);
        }
        if (e.getCharState().equals(CharacterState.MOVING)) {
            p.setX(p.getX() + directionX * speed * gameData.getDelta());
            p.setY(p.getY() + directionY * speed * gameData.getDelta());
            if ((float) Math.sqrt(Math.pow(p.getX() - startX, 2) + Math.pow(p.getY() - startY, 2)) >= distance) {
                speed = 0;
                p.setX(endX);
                p.setY(endY);
                e.setCharState(CharacterState.IDLE);
                setStandingState(e);
            }
        }
        if (gameData.getKeys().isPressed(ESCAPE)) {
            //leGameMenu.plsShowUp();

        }
        if (gameData.getKeys().isPressed(Q)) {
            //lePotion.plsDrink();
        }
    }

    private void handleShoot(Entity e, GameData gameData) {
        if (gameData.getKeys().isDown(LEFT_MOUSE)) {
            SpellInfos spell = e.get(SpellInfos.class);
//            if (spell.getChosenSpell() == null) {
//                System.out.println("No spell chosen");
//            }
//            else {
            setStandingState(e);
            System.out.println("shoot at target location");
            e.setCharState(CharacterState.CASTING);
            //System.out.println("Shooting: + " + spell.getChosenSpell());
            //}
        }
    }

    private void setRunningState(Entity e) {
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

    private void setStandingState(Entity e) {
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
            SpellInfos si = e.get(SpellInfos.class);
            si.setChosenSpell(FIREBALL);

        } else {
            return;
        }
        if (gameData.getKeys().isPressed(NUM_2)) {
            //e.setChosenSpell(SpellType.SPELL2);

        }
        if (gameData.getKeys().isPressed(NUM_3)) {
            //Spell 3

        }
        if (gameData.getKeys().isPressed(NUM_4)) {
            //Spell 4

        }

    }

}
