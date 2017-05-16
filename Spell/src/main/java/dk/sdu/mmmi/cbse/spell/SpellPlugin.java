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
import static data.EntityType.PLAYER;
import static data.EntityType.SPELL;
import data.ImageManager;
import data.SpellList;
import data.componentdata.Expiration;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.SpellInfos;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class)
    ,
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class SpellPlugin implements IGamePluginService, IEntityProcessingService {

    String SPELL_IMAGE_PATH = "";
    private World world;

    @Override
    public void start(GameData gameData, World world) {
        SPELL_IMAGE_PATH = SpellPlugin.class.getResource(SpellList.FIREBALL_IMAGE).getPath().replace("file:", "");
        ImageManager.createImage(SPELL_IMAGE_PATH, true);
        this.world = world;

    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(PLAYER)) {
            SpellBook book = entity.get(SpellBook.class);
            if (book.getSpells().isEmpty()) {
                book.addToSpellBook(SpellType.FIREBALL);
            }
            book.reduceCooldownTimeLeft(gameData.getDelta());
            if (entity.getCharState() == CASTING && book.getChosenSpell() != null && book.getCooldownTimeLeft() <= 0) {
                useSpell(book.getChosenSpell(), entity);

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
            moveSpell(spell, gameData);
        }
    }

    public void unlockSpell(Entity owner, SpellType spellType) {
        SpellBook sb = owner.get(SpellBook.class);
        sb.addToSpellBook(spellType);
    }

    public void useSpell(SpellType spellType, Entity caster) {
        SpellBook book = caster.get(SpellBook.class);
        for (SpellType spell : book.getSpells()) {
            if (spell == spellType) {
                book.setCooldownTimeLeft(book.getGlobalCooldownTime());
                Entity se = new Entity();
                se.setType(SPELL);
                se.setMaxSpeed(SpellList.getSpellSpeed(spellType));
                Position p = caster.get(Position.class);
                SpellInfos si = new SpellInfos();
                si.setSpellType(spellType);
                si.setIsMoving(false);
                se.add(new Expiration(SpellList.FIREBALL_EXPIRATION));
                se.add(new Position(p));
                se.add(si);
                se.add(ImageManager.getImage(SPELL_IMAGE_PATH));
                world.addEntity(se);
                book.setChosenSpell(null);

            }
        }
    }

    public void moveSpell(Entity entity, GameData gameData) {
        //s
    }

    @Override
    public void stop() {
        for (Entity spell : world.getEntities(SPELL)) {
            world.removeEntity(spell);
        }
    }

}
