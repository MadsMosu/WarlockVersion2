/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.collisionsystem;

import States.CharacterState;
import data.Entity;
import static data.EntityType.*;
import data.GameData;
import data.Netherworld;
import data.World;
import data.componentdata.Damage;
import data.componentdata.DamageTaken;
import data.componentdata.Health;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.Score;
import data.componentdata.SpellBook;
import data.componentdata.SpellInfos;
import data.componentdata.Velocity;
import java.util.Collection;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class),})

public class CollisionPlugin implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world, Netherworld netherworld) {
        Collection<Entity> entities = world.getEntities();

        for (Entity handled : entities) {
            Collection<Entity> collideWithEntities = world.getEntities();

            for (Entity collideWith : collideWithEntities) {
                handleCollision(handled, collideWith, world, gameData);
//                isCollision(handled, collideWith);

            }

        }
    }

    private void handleCollision(Entity handled, Entity collideWith, World world, GameData gameData) {
        if (handled != null && collideWith != null) {
            if (handled.getType() == SPELL && collideWith.getType() == ENEMY && CollisionHandler.isColliding(handled, collideWith)) {

                if (!handled.get(Owner.class).getID().equals(collideWith.getID())) {
                    //Add damage to entity
                    Damage dmg = new Damage(handled.get(Damage.class).getDamage());
                    Owner owner = (handled.get(Owner.class));
                    DamageTaken dmgTaken = new DamageTaken(dmg, owner);
                    collideWith.get(Health.class).addDamageTaken(dmgTaken);
                    //System.out.println(collideWith.get(Health.class).getHp());
                    Velocity v = collideWith.get(Velocity.class);

                    v.setVector(handled.get(Velocity.class).getVector());
                    v.getVector().setMagnitude(200);
                    v.setTravelDist(v.getVector().getMagnitude());
                    v.getVector().normalize();
                    Position p = collideWith.get(Position.class);
                    p.setStartPosition(new Position(p));
                    collideWith.setCharState(CharacterState.BOUNCING);
                    world.removeEntity(handled);
                }
            } else if (handled.getType() == SPELL && collideWith.getType() == PLAYER && CollisionHandler.isColliding(handled, collideWith)) {
                if (!handled.get(Owner.class).getID().equals(collideWith.getID())) {
                    collideWith.get(Health.class).addDamageTaken(new DamageTaken(handled.get(Damage.class), handled.get(Owner.class)));

                    Velocity v = collideWith.get(Velocity.class);
                    v.setVector(handled.get(Velocity.class).getVector());
                    v.getVector().setMagnitude(200);
                    v.setTravelDist(v.getVector().getMagnitude());
                    v.getVector().normalize();
                    collideWith.setCharState(CharacterState.BOUNCING);
                    world.removeEntity(handled);
                }
            } else if (handled.getType() == PLAYER && collideWith.getType() == ENEMY && CollisionHandler.isColliding(handled, collideWith)) {
                if (!handled.get(Owner.class).getID().equals(collideWith.getID())) {

                }

            } else if (handled.getType() == ENEMY && collideWith.getType() == ENEMY && CollisionHandler.isColliding(handled, collideWith)) {
                if (!handled.get(Owner.class).getID().equals(collideWith.getID())) {

                }

            } else if (handled.getType() == SPELL && collideWith.getType() == SPELL && CollisionHandler.isColliding(handled, collideWith)) {
                if (!handled.get(Owner.class).getID().equals(collideWith.get(Owner.class).getID())) {
//                    world.removeEntity(handled);
//                    world.removeEntity(collideWith);
                }

            }

        }

    }
}
