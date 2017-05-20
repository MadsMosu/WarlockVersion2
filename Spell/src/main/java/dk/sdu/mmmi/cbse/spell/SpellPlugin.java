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
import States.GameState;
import static data.EntityType.ENEMY;
import static data.EntityType.PLAYER;
import static data.EntityType.SPELL;
import data.ImageManager;
import data.Netherworld;
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
import java.util.Map;

@ServiceProviders(value = {
    @ServiceProvider(service = IGamePluginService.class),
    @ServiceProvider(service = IEntityProcessingService.class)
})

public class SpellPlugin implements IGamePluginService, IEntityProcessingService {

    SpellArchive spellArchive;
    String SPELL_IMAGE_PATH = "";
    private Map<SpellType, String> spriteMap;
    private World world;

    @Override
    public void start(GameData gameData, World world)
    {
        spellArchive = new SpellArchive(world);
        spriteMap = SpellList.getSpellMap();
//        SPELL_IMAGE_PATH = SpellPlugin.class.getResource(SpellList.FIREBALL_IMAGE).getPath().replace("file:", "");
//        ImageManager.createImage(SPELL_IMAGE_PATH, true);
        ImageManager.createImage(SpellPlugin.class.getResource(SpellList.FIREBALL_IMAGE).getPath().replace("file:", ""), true);
        ImageManager.createImage(SpellPlugin.class.getResource(SpellList.TELEPORT_IMAGE).getPath().replace("file:", ""), true);
        ImageManager.createImage(SpellPlugin.class.getResource(SpellList.FROSTBOLT_IMAGE).getPath().replace("file:", ""), true);
        this.world = world;

    }

    @Override
    public void process(GameData gameData, World world, Netherworld netherworld) {

        for (Entity entity : world.getEntities(PLAYER, ENEMY)) {
            SpellBook book = entity.get(SpellBook.class);
            if (book.getSpells().isEmpty()) {
                book.addToSpellBook(SpellType.FIREBALL);
                book.addToSpellBook(SpellType.TELEPORT1);
                book.addToSpellBook(SpellType.FROSTBOLT);
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
            if (e.getExpiration() <= 0 || gameData.getGameState().equals(GameState.ROUNDEND)) {
                world.removeEntity(spell);
            }
        }
    }

    public void unlockSpell(Entity owner, SpellType spellType)
    {
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

    private void createSpellEntity(SpellType spellType, Entity caster, GameData gameData)
    {
        String spriteString = SpellPlugin.class.getResource(spriteMap.get(spellType)).getPath().replace("file:", "");
        ImageManager.createImage(spriteString, true);

        Entity se = new Entity();

        se.setType(SPELL);
        Position p = caster.get(Position.class);

        SpellInfos si = new SpellInfos();
        Body b = new Body(spellArchive.getSpell(spellType).getHeight(), spellArchive.getSpell(spellType).getWidth(), Geometry.CIRCLE);
        b.setSpriteSize(spellArchive.getSpell(spellType).getSpriteWidth(), spellArchive.getSpell(spellType).getSpriteHeight());
        b.setFrames(SpellArchive.getSpell(spellType).getFrames());
        b.setFrameSpeed(SpellArchive.getSpell(spellType).getFrameSpeed());
        Damage dmg = new Damage(spellArchive.getSpell(spellType).getDamage());
        Bounce bounce = new Bounce(spellArchive.getSpell(spellType).getBouncePoints());
        Velocity v = new Velocity();
        v.setVector(setSpellDirection(caster, v, gameData));
        v.setSpeed(spellArchive.getSpell(spellType).getSpeed());
        se.setAngle(v.getVector().getAngle());
        si.setSpellType(spellType);
        Owner owner = new Owner(caster.getID());
        owner.setOwnerType(caster.getType());
        si.setIsMoving(false);
        se.add(dmg);
        se.add(bounce);
        se.add(new Expiration(spellArchive.getSpell(spellType).getExpiration()));
        se.add(owner);
        float x = p.getX() + caster.get(Body.class).getWidth() / 2 - spellArchive.getSpell(spellType).getWidth() / 2;
        float y = p.getY() + caster.get(Body.class).getHeight() / 2 - spellArchive.getSpell(spellType).getHeight() / 2;
        se.add(new Position(x, y));

        se.add(si);
        se.add(v);
        se.add(ImageManager.getImage(spriteString));
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
    public void stop()
    {
        for (Entity spell : world.getEntities(SPELL)) {
            world.removeEntity(spell);
        }
    }

}
