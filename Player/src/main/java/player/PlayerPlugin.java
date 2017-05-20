package player;

import States.CharacterState;
import data.Entity;
import static data.EntityType.PLAYER;
import data.GameData;
import data.World;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import services.IEntityProcessingService;
import services.IGamePluginService;
import States.MovementState;
import static data.GameKeys.*;
import data.ImageManager;
import static data.SpellType.FIREBALL;
import static data.SpellType.TELEPORT1;
import data.componentdata.Body;
import data.componentdata.Body.Geometry;
import data.componentdata.Health;
import data.componentdata.Owner;
import data.componentdata.Position;
import data.componentdata.SpellBook;
import data.componentdata.Velocity;
import data.util.Vector2;

@ServiceProviders(value = {
    @ServiceProvider(service = IEntityProcessingService.class)
    ,
    @ServiceProvider(service = IGamePluginService.class)
})

public class PlayerPlugin implements IEntityProcessingService, IGamePluginService {

    private Entity player;
    public static final String CHARACTER_IMAGE_PATH = "assets/Characters.png";
    public static String CHARACTER_FINAL_IMAGE_PATH = "";
    private World world;

    @Override
    public void start(GameData gameData, World world) {
        CHARACTER_FINAL_IMAGE_PATH = PlayerPlugin.class.getResource(CHARACTER_IMAGE_PATH).getPath().replace("file:", "");
        ImageManager.createImage(CHARACTER_FINAL_IMAGE_PATH, false);
        this.world = world;
        createPlayer();
    }

    @Override
    public void process(GameData gameData, World world) {

        for (Entity p : world.getEntities(PLAYER)) {
            handleMove(p, gameData);
            handleTargetClick(p, gameData);
            handleShoot(p, gameData);
        }

        if (player.getCharState() == CharacterState.DEAD) {
            stop();
        }
    }

    private void createPlayer() {
        player = new Entity();
        player.setType(PLAYER);
        Position pos = new Position(3200, 0);
        Health health = new Health(100);
        Owner ow = new Owner(player.getID());
        SpellBook sb = new SpellBook(new Owner(player.getID()));
        Velocity v = new Velocity();
        v.setSpeed(100);
        player.add(ow);
        player.add(ImageManager.getImage(CHARACTER_FINAL_IMAGE_PATH));
        player.add(health);
        player.add(pos);
        player.add(sb);
        player.add(v);

        Body body = new Body(50, 50, Geometry.RECTANGLE);
        player.add(body);
        player.setMoveState(MovementState.STANDING);
        player.setCharState(CharacterState.IDLE);
        world.addEntity(player);

    }

    private void handleMove(Entity e, GameData gameData) {
        Position p = e.get(Position.class);
        Body b = e.get(Body.class);
        Velocity v = e.get(Velocity.class);
        Vector2 stopVector;
        
        if (gameData.getKeys().isPressed(RIGHT_MOUSE)) {
            gameData.getKeys().setKey(RIGHT_MOUSE, false);

            float endX = gameData.getMousePositionX() - (b.getWidth() / 2);
            float endY = gameData.getMousePositionY() - (b.getHeight() / 2);
            //stopVector = new Vector2(endX - e.getX()); skal laves som vector fra startx til endx, og saa sammenlignes med den anden vector
            v.setVector(new Vector2(p, new Position(endX, endY)));
            v.getVector().normalize();
            System.out.println(v.getVector().getMagnitude());
            if (v.getVector().getMagnitude() == 0) {
                
                e.setCharState(CharacterState.IDLE);
            }
            else {
                e.setAngle(v.getVector().getAngle());
                e.setCharState(CharacterState.MOVING);
                e.setRunningState(e.getAngle(), e);
            }
        }



        if (gameData.getKeys()
                .isPressed(ESCAPE)) {
            //leGameMenu.plsShowUp();

        }
        if (gameData.getKeys()
                .isPressed(Q)) {
            //lePotion.plsDrink();
        }
    }

    private void handleTargetClick(Entity e, GameData gameData) {
        if (gameData.getKeys().isPressed(NUM_1)) {
            SpellBook sb = e.get(SpellBook.class
            );
            sb.setChosenSpell(FIREBALL);
        }
        
        else if (gameData.getKeys().isPressed(NUM_2)) {
            //e.setChosenSpell(SpellType.SPELL2);
            SpellBook sb = e.get(SpellBook.class);
            sb.setChosenSpell(TELEPORT1);
            System.out.println("Teleport chosen");

        } else {
            return;
        }
        if (gameData.getKeys().isPressed(NUM_3)) {
            //Spell 3

        }
        if (gameData.getKeys().isPressed(NUM_4)) {
            //Spell 4

        }

    }

    private void handleShoot(Entity e, GameData gameData) {
        if (gameData.getKeys().isDown(LEFT_MOUSE) && e.get(SpellBook.class
        ).getChosenSpell() != null) {
            e.setMoveState(MovementState.STANDING);
            e.setCharState(CharacterState.CASTING);

        }
    }

    @Override
    public void stop() {
        // Remove entities
        world.removeEntity(player);
    }

}
