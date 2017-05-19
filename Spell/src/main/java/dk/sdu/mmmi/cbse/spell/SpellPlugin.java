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
import data.ImageManager;
import data.SpellList;
import data.componentdata.AI;
import data.componentdata.Body;
import data.componentdata.Body.Geometry;
import data.componentdata.Bounce;
import data.componentdata.Damage;
import data.componentdata.Expiration;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.SpellInfos;
import data.componentdata.Velocity;
import data.util.Vector2;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class)
    ,
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class SpellPlugin implements IGamePluginService, IEntityProcessingService {

    SpellArchive spellArchive;
    String SPELL_IMAGE_PATH = "";
    private World world;

    @Override
    public void start(GameData gameData, World world) {
        spellArchive = new SpellArchive(world);
        SPELL_IMAGE_PATH = SpellPlugin.class.getResource(SpellList.FIREBALL_IMAGE).getPath().replace("file:", "");
        ImageManager.createImage(SPELL_IMAGE_PATH, true);
        this.world = world;

    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            SpellBook book = entity.get(SpellBook.class);
            if (book.getSpells().isEmpty()) {
                book.addToSpellBook(SpellType.FIREBALL);
            }
            book.reduceCooldownTimeLeft(gameData.getDelta());
            if (entity.getCharState() == CASTING && book.getChosenSpell() != null && book.getCooldownTimeLeft() <= 0) {
                useSpell(book.getChosenSpell(), entity, gameData);

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

    public void useSpell(SpellType spellType, Entity caster, GameData gameData) {
        SpellBook book = caster.get(SpellBook.class);
        for (SpellType spell : book.getSpells()) {
            if (spell == spellType) {
                book.setCooldownTimeLeft(book.getGlobalCooldownTime());
                createSpellEntity(spellType, caster, gameData);
                book.setChosenSpell(null);

            }
        }
    }
    
    private void createSpellEntity(SpellType spellType, Entity caster, GameData gameData){
                Entity se = new Entity();
                se.setType(SPELL);
                Position p = caster.get(Position.class);
                float x = p.getX() + caster.get(Body.class).getWidth() / 2;
                float y = p.getY() + caster.get(Body.class).getHeight() / 2;
                SpellInfos si = new SpellInfos();
                Body b = new Body(spellArchive.getSpell(spellType).getHeight(), spellArchive.getSpell(spellType).getWidth(), Geometry.CIRCLE);
                Damage dmg = new Damage(spellArchive.getSpell(spellType).getDamage());
                Bounce bounce = new Bounce(spellArchive.getSpell(spellType).getBouncePoints());
                Velocity v = new Velocity();
                v.setVector(setSpellDirection(caster, v, gameData));
                se.setAngle(v.getVector().getAngle());
                Owner owner = new Owner(caster.getID());
                if (caster.isType(PLAYER)) {
                    owner.setOwnerType(PLAYER);
                }
                else if (caster.isType(ENEMY)) {
                    owner.setOwnerType(ENEMY);
                }
                owner.setOwnerEntity(caster);
                v.setSpeed(SpellList.getSpellSpeed(spellType));
                si.setSpellType(spellType);
                si.setIsMoving(false);
                se.add(dmg);
                se.add(bounce);
                se.add(new Expiration(SpellList.FIREBALL_EXPIRATION));
                se.add(owner);
                se.add(new Position(x, y));
                se.add(si);
                se.add(v);
                se.add(ImageManager.getImage(SPELL_IMAGE_PATH));
                se.add(b);
                world.addEntity(se);
    }

    private Vector2 setSpellDirection(Entity e, Velocity v, GameData gameData) {
        if (e.getType() == ENEMY) {
            AI ai = e.get(AI.class);
            Position aiPosition = e.get(Position.class);
            Position targetPosition = ai.getCurrentTarget().get(Position.class);
            Vector2 direction = new Vector2(aiPosition, targetPosition);
            direction.normalize();
            return direction;
        }
        else {
            Position p = e.get(Position.class);
            Position targetPosition = new Position(gameData.getMousePositionX(), gameData.getMousePositionY());
            Vector2 direction = new Vector2(p, targetPosition);
            direction.normalize();
            return direction;
        }
    }

    @Override
    public void stop() {
        for (Entity spell : world.getEntities(SPELL)) {
            world.removeEntity(spell);
        }
    }

}
