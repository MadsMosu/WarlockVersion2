package dk.sdu.mmmi.cbse.spell;

import data.SpellType;
import data.World;
import java.util.TreeMap;
import data.SpellList;

/**
 *
 * @author mads1
 */
public class SpellArchive {

    private static final TreeMap spellArchive = new TreeMap<SpellType, Spell>();

    public SpellArchive(World world) {
        spellArchive.put(SpellType.FIREBALL, new Spell(world, SpellType.FIREBALL, SpellList.FIREBALL_DMG, "assets/fireballsprite.png", SpellList.FIREBALL_STATIC, SpellList.FIREBALL_EXPIRATION, SpellList.FIREBALL_SPEED, SpellList.FIREBALL_ACCELERATION, SpellList.FIREBALL_COOLDOWN, SpellList.FIREBALL_BOUNCE, SpellList.FIREBALL_WIDTH, SpellList.FIREBALL_HEIGHT, SpellList.FIREBALL_SPRITE_WIDTH, SpellList.FIREBALL_SPRITE_HEIGHT, SpellList.FIREBALL_FRAMES, SpellList.FIREBALL_FRAME_SPEED));
        spellArchive.put(SpellType.TELEPORT1, new Spell(world, SpellType.TELEPORT1, "assets/teleportsprite1.png", SpellList.TELEPORT_EXPIRATION, SpellList.TELEPORT_COOLDOWN, SpellList.TELEPORT_WIDTH, SpellList.TELEPORT_HEIGHT, SpellList.TELEPORT_SPRITE_WIDTH, SpellList.TELEPORT_SPRITE_HEIGHT, SpellList.TELEPORT_FRAMES, SpellList.FIREBALL_FRAME_SPEED));
    }

    public static TreeMap getSpellArchive() {
        return spellArchive;
    }

    public static Spell getSpell(SpellType st){
        return (Spell) spellArchive.get(st);
    }

}
