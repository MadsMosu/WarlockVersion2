/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.cbse.movementsystem;

import States.CharacterState;
import data.Entity;
import data.EntityType;
import data.GameData;
import data.Netherworld;
import data.World;
import data.componentdata.AI;
import data.componentdata.Position;
import data.componentdata.Velocity;
import org.openide.util.lookup.ServiceProvider;
import services.IEntityProcessingService;

@ServiceProvider(service = IEntityProcessingService.class)
/**
 *
 * @author jonaspedersen
 */
public class ControlProcessor implements IEntityProcessingService {

    @Override
    public void process(GameData gameData, World world, Netherworld netherworld) {

        for (Entity entity : world.getEntities(EntityType.PLAYER, EntityType.ENEMY)) {
            handleMovement(entity, gameData);
        }
        for (Entity spell : world.getEntities(EntityType.SPELL)) {
                handleSpellMovement(spell, gameData);
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

        if (e.getCharState().equals(CharacterState.MOVING)) {
            p.setX(p.getX() + v.getVector().getX() * v.getSpeed() * gameData.getDelta());
            p.setY(p.getY() + v.getVector().getY() * v.getSpeed() * gameData.getDelta());
        } else if(e.getCharState().equals(CharacterState.BOUNCING)){
            p.setX(p.getX() + v.getVector().getX() * v.getSpeed()* 2.5f * gameData.getDelta());
            p.setY(p.getY() + v.getVector().getY() * v.getSpeed()* 2.5f * gameData.getDelta());
        }
    }
}
