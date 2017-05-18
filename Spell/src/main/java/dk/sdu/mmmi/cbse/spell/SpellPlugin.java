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
import data.componentdata.Body;
import data.componentdata.Body.Geometry;
import data.componentdata.Expiration;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.SpellInfos;
import data.componentdata.Velocity;

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
                useSpell(book.getChosenSpell(), entity);

                entity.setCharState(IDLE);
            }
        }
        for (Entity spell : world.getEntities(SPELL)) {
            setShape(spell);
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

    public void useSpell(SpellType spellType, Entity caster) {
        SpellBook book = caster.get(SpellBook.class);
        for (SpellType spell : book.getSpells()) {
            if (spell == spellType) {
                book.setCooldownTimeLeft(book.getGlobalCooldownTime());
                Entity se = new Entity();
                se.setType(SPELL);
                Position p = caster.get(Position.class);
                
                SpellInfos si = new SpellInfos();
                Body b = new Body(spellArchive.getSpell(spellType).getHeight(), spellArchive.getSpell(spellType).getWidth(), Geometry.CIRCLE);
                Velocity v = new Velocity();
                v.setSpeed(SpellList.getSpellSpeed(spellType));
                si.setSpellType(spellType);
                si.setIsMoving(false);
                se.add(new Expiration(SpellList.FIREBALL_EXPIRATION));
                se.add(new Position(p));
                se.add(new Owner(caster.getID()));
                se.get(Position.class).setPosition(p.getX() + caster.get(Body.class).getWidth()/2, p.getY() + caster.get(Body.class).getHeight()/2);
                
                se.add(si);
                se.add(v);
                se.add(ImageManager.getImage(SPELL_IMAGE_PATH));
                se.add(b);
                world.addEntity(se);
                book.setChosenSpell(null);

            }
        }
    }
    
    private void setShape(Entity spell){
        float[] shapex = new float[4];
        float[] shapey = new float[4];
        float angle = spell.getAngle();
        
        
            shapex[0] = spell.get(Position.class).getX() + spell.get(Body.class).getWidth() - 5;
            shapey[0] = spell.get(Position.class).getY();
            
            shapex[1] = spell.get(Position.class).getX() + spell.get(Body.class).getWidth();
            shapey[1] = spell.get(Position.class).getY();
            
            shapex[2] = spell.get(Position.class).getX() + spell.get(Body.class).getWidth();
            shapey[2] = spell.get(Position.class).getY() + spell.get(Body.class).getHeight();
            
            shapex[3] = spell.get(Position.class).getX() + spell.get(Body.class).getWidth() - 5;
            shapey[3] = spell.get(Position.class).getY() + spell.get(Body.class).getHeight();
            
            spell.setShapeX(shapex);
            spell.setShapeY(shapey);
        
        
    }


    @Override
    public void stop() {
        for (Entity spell : world.getEntities(SPELL)) {
            world.removeEntity(spell);
        }
    }

}
