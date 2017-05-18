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
import data.componentdata.Damage;
import data.componentdata.DamageTaken;
import data.componentdata.Health;
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

            if (handled.getType() == MAP && collideWith.getType() == PLAYER && CollisionHandler.isColliding(handled, collideWith)) {

                collideWith.get(Position.class).setX(1800);
            } else if (handled.getType() == SPELL && collideWith.getType() == ENEMY && CollisionHandler.isColliding(handled, collideWith)) {

                if (!handled.get(Owner.class).getID().equals(collideWith.getID())) {
                    Damage dmg = new Damage(handled.get(Damage.class).getDamage());
                    System.out.println("Damage: " + dmg.getDamage());
                    Owner owner = new Owner(collideWith.getID());
                    DamageTaken dmgTaken = new DamageTaken(dmg, owner);
                    System.out.println("dmgTaken: " + dmgTaken.getDamage());
                    collideWith.get(Health.class).addDamageTaken(dmgTaken);
                    System.out.println(collideWith.get(Health.class).getHp());
                    world.removeEntity(handled);

                }
            } else if (handled.getType() == SPELL && collideWith.getType() == PLAYER && CollisionHandler.isColliding(handled, collideWith)) {
                if (!handled.get(Owner.class).getID().equals(collideWith.getID())) {
                    collideWith.get(Health.class).addDamageTaken(new DamageTaken(new Damage(handled.get(Damage.class).getDamage()), new Owner(collideWith.getID())));
                    
                    world.removeEntity(handled);

                }
            } else if (handled.getType() == PLAYER && collideWith.getType() == ENEMY && CollisionHandler.isColliding(handled, collideWith)) {
                if (!handled.get(Owner.class).getID().equals(collideWith.getID())) {

                    
                }

            } else if (handled.getType() == ENEMY && collideWith.getType() == ENEMY && CollisionHandler.isColliding(handled, collideWith)) {
                if (!handled.get(Owner.class).getID().equals(collideWith.getID())) {

                    
                }

            }

        }

    }
}
