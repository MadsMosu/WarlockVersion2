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

        for (Entity entity : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
            handleMovement(entity, gameData);

        }

        for (Entity spell : world.getEntities(EntityType.SPELL)) {

            if (spell.get(Owner.class).getOwnerType().equals(PLAYER)) {
                handleSpellMovement(spell, gameData);
            }
            if (spell.get(Owner.class).getOwnerType().equals(ENEMY)) {
                handleSpellMovement(spell, gameData);
            }
        }
    }



    private void handleSpellMovement(Entity spell, GameData gameData) {
        Position sp = spell.get(Position.class);
        Velocity v = spell.get(Velocity.class);

        sp.setX(sp.getX() + v.getVector().getX() * v.getSpeed() * gameData.getDelta());
        sp.setY(sp.getY() + v.getVector().getY() * v.getSpeed() * gameData.getDelta());

    }

    private void handleMovement(Entity e, GameData gameData) {
        Position p = e.get(Position.class);
        Velocity v = e.get(Velocity.class);

        if (e.getCharState().equals(CharacterState.BOUNCING)) {

        }
        else if (e.getCharState().equals(CharacterState.MOVING)) {
            p.setX(p.getX() + v.getVector().getX() * v.getSpeed() * gameData.getDelta());
            p.setY(p.getY() + v.getVector().getY() * v.getSpeed() * gameData.getDelta());
        }

    }
}
