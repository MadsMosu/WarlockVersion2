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

    private SpellArchive archive;
    private World world;

    @Override
    public void start(GameData gameData, World world) {
        archive = new SpellArchive(world);

        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            SpellBook spellBook = new SpellBook();
        }

    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            SpellInfos s = entity.get(SpellInfos.class);
            Position p = entity.get(Position.class);
            if (entity.getCharState() == CASTING) {
                useSpell(world, s.getChosenSpell(), p.getX(), p.getY(), entity);
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

    public void unlockSpell(World world, Entity owner, SpellType spellType) {
        SpellBook sb = owner.get(SpellBook.class);
        spellBook.addToSpellBook(world, owner, spellType);
    }

    public void useSpell(World world, SpellType spellType, float x, float y, Entity caster) {
        SpellBook sb = caster.get(SpellBook.class);
        for (SpellType spell : sb.getSpells()) {
            if (spell.getSpellType().equals(spellType)) {
                Entity se = spell.getSpellEntity();
                //archive.getAnimator().getBatch().draw((TextureRegion) spellBook.getSpell(spellType).getAnimation().getKeyFrame(archive.getAnimator().getStateTime()), x, y);
                se.setType(SPELL);
                Position p = caster.get(Position.class);
                se.add(new Position(p.getX(), p.getY()));
                se.setRadians(caster.getRadians());
                se.setMaxSpeed(spellBook.getSpell(spellType).getSpeed());
                se.setAcceleration(spellBook.getSpell(spellType).getAcceleration());
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
