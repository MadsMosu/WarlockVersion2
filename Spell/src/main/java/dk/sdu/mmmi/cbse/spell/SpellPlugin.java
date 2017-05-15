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
import data.EntityType;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import static data.EntityType.SPELL;
import static data.GameKeys.LEFT_MOUSE;
import data.ImageManager;
import data.SpellList;
import data.componentdata.Expiration;
import data.componentdata.Image;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class),
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class SpellPlugin implements IGamePluginService, IEntityProcessingService {

    String SPELL_IMAGE_PATH = "";
    private World world;

    @Override
    public void start(GameData gameData, World world)
    {

        this.world = world;
        for (Entity entity : world.getEntities(PLAYER)) {
            SpellBook book = entity.get(SpellBook.class);
            book.addToSpellBook(SpellType.FIREBALL);
        }
    }

    @Override
    public void process(GameData gameData, World world)
    {

        for (Entity entity : world.getEntities(PLAYER)) {
            SpellBook book = entity.get(SpellBook.class);
            if (book.getSpells().isEmpty()) {
                book.addToSpellBook(SpellType.FIREBALL);
            }
            
            book.reduceCooldownTimeLeft(gameData.getDelta()) ;
            if (entity.getCharState() == CASTING && book.getChosenSpell() != null && book.getCooldownTimeLeft() <=0) {
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

    public void unlockSpell(Entity owner, SpellType spellType)
    {
        SpellBook sb = owner.get(SpellBook.class);
        sb.addToSpellBook(spellType);
    }

    public void useSpell(SpellType spellType, Entity caster)
    {
        SpellBook book = caster.get(SpellBook.class);
        for (SpellType spell : book.getSpells()) {
            if (spell == spellType) {
                Entity se = new Entity();
                se.setType(SPELL);
                Position p = caster.get(Position.class);
                se.add(new Expiration(SpellList.FIREBALL_EXPIRATION));
                se.add(new Position(p.getX(), p.getY()));
                se.setRadians(caster.getRadians());
                se.setMaxSpeed(SpellList.getSpellSpeed(spellType));
                
                SPELL_IMAGE_PATH = SpellPlugin.class.getResource(SpellList.FIREBALL_IMAGE).getPath().replace("file:", "");
                System.out.println(SPELL_IMAGE_PATH);
                ImageManager.createImage(SPELL_IMAGE_PATH, true);
                se.add(ImageManager.getImage(SPELL_IMAGE_PATH));
                world.addEntity(se);

            }
        }
    }

    public void moveSpell(Entity entity, GameData gameData)
    {


    }

        @Override
        public void stop
        
            ()
    {
        for (Entity spell : world.getEntities(SPELL)) {
                world.removeEntity(spell);
            }
        }

    }
