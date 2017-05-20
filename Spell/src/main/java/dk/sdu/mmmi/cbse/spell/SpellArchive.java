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
        spellArchive.put(SpellType.FIREBALL, new Spell(world, SpellType.FIREBALL, SpellList.FIREBALL_DMG, SpellList.FIREBALL_IMAGE, SpellList.FIREBALL_STATIC, SpellList.FIREBALL_EXPIRATION, SpellList.FIREBALL_SPEED, SpellList.FIREBALL_ACCELERATION, SpellList.FIREBALL_COOLDOWN, SpellList.FIREBALL_BOUNCE, SpellList.FIREBALL_WIDTH, SpellList.FIREBALL_HEIGHT, SpellList.FIREBALL_SPRITE_WIDTH, SpellList.FIREBALL_SPRITE_HEIGHT, SpellList.FIREBALL_FRAMES, SpellList.FIREBALL_FRAME_SPEED));
        spellArchive.put(SpellType.FROSTBOLT, new Spell(world, SpellType.FROSTBOLT, SpellList.FROSTBOLT_DMG, SpellList.FROSTBOLT_IMAGE, SpellList.FROSTBOLT_STATIC, SpellList.FROSTBOLT_EXPIRATION, SpellList.FROSTBOLT_SPEED, SpellList.FROSTBOLT_ACCELERATION, SpellList.FROSTBOLT_COOLDOWN, SpellList.FROSTBOLT_BOUNCE, SpellList.FROSTBOLT_WIDTH, SpellList.FROSTBOLT_HEIGHT, SpellList.FROSTBOLT_SPRITE_WIDTH, SpellList.FROSTBOLT_SPRITE_HEIGHT, SpellList.FROSTBOLT_FRAMES, SpellList.FROSTBOLT_FRAME_SPEED));
        spellArchive.put(SpellType.TELEPORT1, new Spell(world, SpellType.TELEPORT1, SpellList.TELEPORT_IMAGE, SpellList.TELEPORT_EXPIRATION, SpellList.TELEPORT_COOLDOWN, SpellList.TELEPORT_WIDTH, SpellList.TELEPORT_HEIGHT, SpellList.TELEPORT_SPRITE_WIDTH, SpellList.TELEPORT_SPRITE_HEIGHT, SpellList.TELEPORT_FRAMES, SpellList.FIREBALL_FRAME_SPEED));
    }

    public static TreeMap getSpellArchive() {
        return spellArchive;
    }

    public Spell getSpell(SpellType st){
        return (Spell) spellArchive.get(st);
    }

}
