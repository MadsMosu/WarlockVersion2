package dk.sdu.mmmi.cbse.spell;

import data.Entity;
import data.GameData;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;
import data.SpellType;
import static States.CharacterState.CASTING;
import static States.CharacterState.IDLE;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import static data.EntityType.SPELL;
import data.SpellList;
import data.componentdata.Expiration;
import data.componentdata.Position;
import data.componentdata.SpellInfos;
import data.componentdata.SpellBook;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class)
    ,
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class SpellPlugin implements IGamePluginService, IEntityProcessingService {

    private World world;

    @Override
    public void start(GameData gameData, World world) {

        for (Entity entity : world.getEntities(PLAYER)) {
            SpellBook sb = entity.get(SpellBook.class);
            sb.addToSpellBook(SpellType.FIREBALL);
        }
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            SpellBook s = entity.get(SpellBook.class);
            Position p = entity.get(Position.class);
            if (entity.getCharState() == CASTING && s.getChosenSpell() != null) {
                useSpell(s.getChosenSpell(), p.getX(), p.getY(), entity);
                entity.setCharState(IDLE);
            }
        }
        for (Entity spell : world.getEntities(SPELL)) {
            float dt = gameData.getDelta();
            Expiration e = spell.get(Expiration.class);
            e.reduceExpiration(dt);
            if (e.getExpiration() <= 0) {
                world.removeEntity(spell);
            }
        }
    }

    public void unlockSpell(Entity owner, SpellType spellType) {
        SpellBook sb = owner.get(SpellBook.class);
        sb.addToSpellBook(spellType);
    }

    public void useSpell(SpellType spellType, float x, float y, Entity caster) {
        SpellBook sb = caster.get(SpellBook.class);
        for (SpellType spell : sb.getSpells()) {
            if (spell.equals(spellType)) {
                Entity se = new Entity();
                se.setType(SPELL);
                Position p = caster.get(Position.class);
                se.add(new Position(p.getX(), p.getY()));
                se.setRadians(caster.getRadians());
                se.setMaxSpeed(SpellList.getSpellSpeed(spellType));
                return;
            }
        }
    }

    @Override
    public void stop() {
        for (Entity spell : world.getEntities(SPELL)) {
            world.removeEntity(spell);
        }
    }

}
