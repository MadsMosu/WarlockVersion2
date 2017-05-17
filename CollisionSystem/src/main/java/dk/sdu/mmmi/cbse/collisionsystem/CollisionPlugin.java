/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.collisionsystem;

import data.Entity;
import data.EntityType;
import static data.EntityType.*;
import data.GameData;
import data.World;
import data.componentdata.Body;
import data.componentdata.Owner;
import data.componentdata.Position;
import events.Event;
import events.EventType;
import java.util.Collection;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;

/**
 *
 * @author jonas_000
 */
@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),})

public class CollisionPlugin implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world)
    {
        Collection<Entity> entities = world.getEntities();

        for (Entity handled : entities) {
            Collection<Entity> collideWithEntities = world.getEntities();

            for (Entity collideWith : collideWithEntities) {
                handleCollision(handled, collideWith, world, gameData);
//                isCollision(handled, collideWith);

            }

        }
    }

    private void handleCollision(Entity handled, Entity collideWith, World world, GameData gameData)
    {
        if (handled != null && collideWith != null) {

            if (handled.getType() == MAP && collideWith.getType() == PLAYER && !handled.contains(collideWith.get(Position.class).getX(), collideWith.get(Position.class).getY())) {
                System.out.println("Player collision with Map");
                collideWith.get(Position.class).setX(1800);
            } else if (handled.getType() == SPELL && collideWith.getType() == ENEMY && collideWith.contains(handled.get(Position.class).getX(), handled.get(Position.class).getY())) {
               
                if(!handled.get(Owner.class).getID().equals(collideWith.getID())){
                    world.removeEntity(handled);
                    System.out.println("Spell collision with enemy");
                }
                
            } else if(handled.getType() == SPELL && collideWith.getType() == PLAYER && collideWith.contains(handled.get(Position.class).getX(), handled.get(Position.class).getY())){
                if(!handled.get(Owner.class).getID().equals(collideWith.getID())){
                    world.removeEntity(handled);
                    System.out.println("Spell collision with player");
                }
            }

        }
//            if (handled.contains(collideWith.get(Position.class).getX(), collideWith.get(Position.class).getY()) && handled.getType() == SPELL && collideWith.getType() == PLAYER) {
//                System.out.println("PLAYER collision with SPELL");
//                
//            }  else if (handled.contains(collideWith.get(Position.class).getX(), collideWith.get(Position.class).getY()) && handled.getType() == MAP && collideWith.getType() == PLAYER) {
//                System.out.println("PLAYER collision with MAP");
//                collideWith.setSpeed(0);
//                
//            }  else if (handled.contains(collideWith.get(Position.class).getX(), collideWith.get(Position.class).getY()) && handled.getType() == MAP && collideWith.getType() == ENEMY) {
//                System.out.println("ENEMY collision with MAP");
//                collideWith.setSpeed(0);
//                
//            } else if (handled.contains(collideWith.get(Position.class).getX(), collideWith.get(Position.class).getY()) && handled.getType() == SPELL && collideWith.getType() == ENEMY) {
//                System.out.println("SPELL collision with ENEMY");
//                world.removeEntity(collideWith);
//                //gameData.addEvent(new Event(EventType.ASTEROID_SPLIT, handled.getID()));
//            }
    }
}
