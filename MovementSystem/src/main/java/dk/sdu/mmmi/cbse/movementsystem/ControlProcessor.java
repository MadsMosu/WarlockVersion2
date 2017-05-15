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
import data.componentdata.SpellBook;
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
    private int spellNumber = 0;

    @Override
    public void process(GameData gameData, World world) {
        
        if(gameData.getKeys().isPressed(NUM_1)){
            spellNumber = 1;
        }
        

        for (Entity entity : world.getEntities(EntityType.PLAYER)) {
           
//            if(!entity.get(SpellBook.class).getSpells().contains(FIREBALL)){
//                entity.get(SpellBook.class).addToSpellBook(FIREBALL);
//                System.out.println("Fireball added to spellbook");
//            }
            
            handleMoveClick(entity, gameData);
            handleTargetClick(entity, gameData);
            handleShoot(entity, gameData, world);

            for (Entity spell : world.getEntities(EntityType.SPELL)) {
                Position sp = spell.get(Position.class);
                if (gameData.getKeys().isPressed(LEFT_MOUSE)) {
                    gameData.getKeys().setKey(LEFT_MOUSE, false);
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

                }
                if (spellIsMoving) {
                    sp.setX(sp.getX() + sDirectionX * spell.getMaxSpeed() * gameData.getDelta());
                    sp.setY(sp.getY() + sDirectionY * spell.getMaxSpeed() * gameData.getDelta());
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
                setRunningState(e);
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

    private void handleShoot(Entity e, GameData gameData, World world) {
        if (gameData.getKeys().isDown(LEFT_MOUSE) && spellNumber > 0 ) {
            SpellType spell;
            if(spellNumber == 1){
                spell = e.get(SpellBook.class).getChosenSpell();
            }

            
            setStandingState(e);
            e.setCharState(CharacterState.CASTING);
            
            spellNumber = 1;
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
