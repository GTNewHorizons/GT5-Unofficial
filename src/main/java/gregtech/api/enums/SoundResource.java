package gregtech.api.enums;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.IndustrialCraft2;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.ResourceLocation;

import com.google.common.collect.Maps;

/**
 * Enumerates known sounds with id and resource-location
 *
 * <p>
 * Note that the id serve no specific purpose, if for legacy compatibility of a plausible yet unimplemented network
 * packet weight optimization.
 * </p>
 */
public enum SoundResource {

    RANDOM_BREAK(0, "random.break"),
    RANDOM_ANVIL_USE(1, "random.anvil_use"),
    RANDOM_ANVIL_BREAK(2, "random.anvil_break"),
    RANDOM_CLICK(3, "random.click"),
    RANDOM_FIZZ(4, "random.fizz"),
    RANDOM_EXPLODE(5, "random.explode"),
    FIRE_IGNITE(6, "fire.ignite"),

    IC2_TOOLS_WRENCH(100, IndustrialCraft2.ID, "tools.Wrench"),
    IC2_TOOLS_RUBBER_TRAMPOLINE(101, IndustrialCraft2.ID, "tools.RubberTrampoline"),
    IC2_TOOLS_PAINTER(102, IndustrialCraft2.ID, "tools.Painter"),
    IC2_TOOLS_BATTERY_USE(103, IndustrialCraft2.ID, "tools.BatteryUse"),
    IC2_TOOLS_CHAINSAW_CHAINSAW_USE_ONE(104, IndustrialCraft2.ID, "tools.chainsaw.ChainsawUseOne"),
    IC2_TOOLS_CHAINSAW_CHAINSAW_USE_TWO(105, IndustrialCraft2.ID, "tools.chainsaw.ChainsawUseTwo"),
    IC2_TOOLS_DRILL_DRILL_SOFT(106, IndustrialCraft2.ID, "tools.drill.DrillSoft"),
    IC2_TOOLS_DRILL_DRILL_HARD(107, IndustrialCraft2.ID, "tools.drill.DrillHard"),
    IC2_TOOLS_OD_SCANNER(108, IndustrialCraft2.ID, "tools.ODScanner"),
    IC2_TOOLS_INSULATION_CUTTERS(109, IndustrialCraft2.ID, "tools.InsulationCutters"),

    IC2_MACHINES_EXTRACTOR_OP(200, IndustrialCraft2.ID, "machines.ExtractorOp"),
    IC2_MACHINES_MACERATOR_OP(201, IndustrialCraft2.ID, "machines.MaceratorOp"),
    IC2_MACHINES_INDUCTION_LOOP(202, IndustrialCraft2.ID, "machines.InductionLoop"),
    IC2_MACHINES_COMPRESSOR_OP(203, IndustrialCraft2.ID, "machines.CompressorOp"),
    IC2_MACHINES_RECYCLER_OP(204, IndustrialCraft2.ID, "machines.RecyclerOp"),
    IC2_MACHINES_MINER_OP(205, IndustrialCraft2.ID, "machines.MinerOp"),
    IC2_MACHINES_PUMP_OP(206, IndustrialCraft2.ID, "machines.PumpOp"),
    IC2_MACHINES_ELECTROFURNACE_LOOP(207, IndustrialCraft2.ID, "machines.ElectroFurnaceLoop"),
    @Deprecated
    DEPRECATED_DUPE_OF_IC2_MACHINES_INDUCTION_LOOP(208, IndustrialCraft2.ID, "machines.InductionLoop"),
    IC2_MACHINES_MACHINE_OVERLOAD(209, IndustrialCraft2.ID, "machines.MachineOverload"),
    IC2_MACHINES_INTERRUPT_ONE(210, IndustrialCraft2.ID, "machines.InterruptOne"),
    IC2_MACHINES_KA_CHING(211, IndustrialCraft2.ID, "machines.KaChing"),
    IC2_MACHINES_MAGNETIZER_LOOP(212, IndustrialCraft2.ID, "machines.MagnetizerLoop"),

    GT_MACHINES_FUSION_LOOP(230, GregTech.ID, "machines.FusionLoop"),
    GT_MACHINES_DISTILLERY_LOOP(231, GregTech.ID, "machines.DistilleryLoop"),
    GT_MACHINES_PLASMAFORGE_LOOP(232, GregTech.ID, "machines.PlasmaForgeLoop"),

    GT_MACHINES_PURIFICATIONPLANT_LOOP(233, GregTech.ID, "machines.PurificationPlantLoop"),

    GUI_BUTTON_DOWN(-1, GregTech.ID, "gui.buttonDown"),
    GUI_BUTTON_UP(-1, GregTech.ID, "gui.buttonUp"),

    /*
     * Other Minecraft Sounds that were missing
     */
    AMBIENT_CAVE_CAVE(-1, "ambient.cave.cave"),
    AMBIENT_WEATHER_RAIN(-1, "ambient.weather.rain"),
    AMBIENT_WEATHER_THUNDER(-1, "ambient.weather.thunder"),
    DAMAGE_FALLBIG(-1, "damage.fallbig"),
    DAMAGE_FALLSMALL(-1, "damage.fallsmall"),
    DAMAGE_HIT(-1, "damage.hit"),
    DAMAGE_HURTFLESH(-1, "damage.hurtflesh"),
    DIG_CLOTH(-1, "dig.cloth"),
    DIG_GRASS(-1, "dig.grass"),
    DIG_GRAVEL(-1, "dig.gravel"),
    DIG_SAND(-1, "dig.sand"),
    DIG_SNOW(-1, "dig.snow"),
    DIG_STONE(-1, "dig.stone"),
    DIG_WOOD(-1, "dig.wood"),
    FIRE_FIRE(-1, "fire.fire"),
    FIREWORKS_BLAST(-1, "fireworks.blast"),
    FIREWORKS_BLAST_FAR(-1, "fireworks.blast_far"),
    FIREWORKS_LARGEBLAST(-1, "fireworks.largeBlast"),
    FIREWORKS_LARGEBLAST_FAR(-1, "fireworks.largeBlast_far"),
    FIREWORKS_LAUNCH(-1, "fireworks.launch"),
    FIREWORKS_TWINKLE(-1, "fireworks.twinkle"),
    FIREWORKS_TWINKLE_FAR(-1, "fireworks.twinkle_far"),
    GAME_NEUTRAL_SWIM(-1, "game.neutral.swim"),
    GAME_TNT_PRIMED(-1, "game.tnt.primed"),
    LIQUID_LAVA(-1, "liquid.lava"),
    LIQUID_LAVAPOP(-1, "liquid.lavapop"),
    LIQUID_SPLASH(-1, "liquid.splash"),
    LIQUID_SWIM(-1, "liquid.swim"),
    LIQUID_WATER(-1, "liquid.water"),
    MINECART_BASE(-1, "minecart.base"),
    MINECART_INSIDE(-1, "minecart.inside"),
    MOB_BAT_DEATH(-1, "mob.bat.death"),
    MOB_BAT_HURT(-1, "mob.bat.hurt"),
    MOB_BAT_IDLE(-1, "mob.bat.idle"),
    MOB_BAT_LOOP(-1, "mob.bat.loop"),
    MOB_BAT_TAKEOFF(-1, "mob.bat.takeoff"),
    MOB_BLAZE_BREATHE(-1, "mob.blaze.breathe"),
    MOB_BLAZE_DEATH(-1, "mob.blaze.death"),
    MOB_BLAZE_HIT(-1, "mob.blaze.hit"),
    MOB_CAT_HISS(-1, "mob.cat.hiss"),
    MOB_CAT_HITT(-1, "mob.cat.hitt"),
    MOB_CAT_MEOW(-1, "mob.cat.meow"),
    MOB_CAT_PURR(-1, "mob.cat.purr"),
    MOB_CAT_PURREOW(-1, "mob.cat.purreow"),
    MOB_CHICKEN(-1, "mob.chicken"),
    MOB_CHICKEN_HURT(-1, "mob.chicken.hurt"),
    MOB_CHICKEN_PLOP(-1, "mob.chicken.plop"),
    MOB_CHICKEN_SAY(-1, "mob.chicken.say"),
    MOB_CHICKEN_STEP(-1, "mob.chicken.step"),
    MOB_COW(-1, "mob.cow"),
    MOB_COW_HURT(-1, "mob.cow.hurt"),
    MOB_COW_SAY(-1, "mob.cow.say"),
    MOB_COW_STEP(-1, "mob.cow.step"),
    MOB_CREEPER(-1, "mob.creeper"),
    MOB_CREEPER_DEATH(-1, "mob.creeper.death"),
    MOB_CREEPER_SAY(-1, "mob.creeper.say"),
    MOB_ENDERDRAGON_END(-1, "mob.enderdragon.end"),
    MOB_ENDERDRAGON_GROWL(-1, "mob.enderdragon.growl"),
    MOB_ENDERDRAGON_HIT(-1, "mob.enderdragon.hit"),
    MOB_ENDERDRAGON_WINGS(-1, "mob.enderdragon.wings"),
    MOB_ENDERMEN_DEATH(-1, "mob.endermen.death"),
    MOB_ENDERMEN_HIT(-1, "mob.endermen.hit"),
    MOB_ENDERMEN_IDLE(-1, "mob.endermen.idle"),
    MOB_ENDERMEN_PORTAL(-1, "mob.endermen.portal"),
    MOB_ENDERMEN_SCREAM(-1, "mob.endermen.scream"),
    MOB_ENDERMEN_STARE(-1, "mob.endermen.stare"),
    MOB_GHAST_AFFECTIONATE_SCREAM(-1, "mob.ghast.affectionate_scream"),
    MOB_GHAST_CHARGE(-1, "mob.ghast.charge"),
    MOB_GHAST_DEATH(-1, "mob.ghast.death"),
    MOB_GHAST_FIREBALL(-1, "mob.ghast.fireball"),
    MOB_GHAST_MOAN(-1, "mob.ghast.moan"),
    MOB_GHAST_SCREAM(-1, "mob.ghast.scream"),
    MOB_HORSE_ANGRY(-1, "mob.horse.angry"),
    MOB_HORSE_ARMOR(-1, "mob.horse.armor"),
    MOB_HORSE_BREATHE(-1, "mob.horse.breathe"),
    MOB_HORSE_DEATH(-1, "mob.horse.death"),
    MOB_HORSE_DONKEY_ANGRY(-1, "mob.horse.donkey.angry"),
    MOB_HORSE_DONKEY_DEATH(-1, "mob.horse.donkey.death"),
    MOB_HORSE_DONKEY_HIT(-1, "mob.horse.donkey.hit"),
    MOB_HORSE_DONKEY_IDLE(-1, "mob.horse.donkey.idle"),
    MOB_HORSE_GALLOP(-1, "mob.horse.gallop"),
    MOB_HORSE_HIT(-1, "mob.horse.hit"),
    MOB_HORSE_IDLE(-1, "mob.horse.idle"),
    MOB_HORSE_JUMP(-1, "mob.horse.jump"),
    MOB_HORSE_LAND(-1, "mob.horse.land"),
    MOB_HORSE_LEATHER(-1, "mob.horse.leather"),
    MOB_HORSE_SKELETON_DEATH(-1, "mob.horse.skeleton.death"),
    MOB_HORSE_SKELETON_HIT(-1, "mob.horse.skeleton.hit"),
    MOB_HORSE_SKELETON_IDLE(-1, "mob.horse.skeleton.idle"),
    MOB_HORSE_SOFT(-1, "mob.horse.soft"),
    MOB_HORSE_WOOD(-1, "mob.horse.wood"),
    MOB_HORSE_ZOMBIE_DEATH(-1, "mob.horse.zombie.death"),
    MOB_HORSE_ZOMBIE_HIT(-1, "mob.horse.zombie.hit"),
    MOB_HORSE_ZOMBIE_IDLE(-1, "mob.horse.zombie.idle"),
    MOB_IRONGOLEM_DEATH(-1, "mob.irongolem.death"),
    MOB_IRONGOLEM_HIT(-1, "mob.irongolem.hit"),
    MOB_IRONGOLEM_THROW(-1, "mob.irongolem.throw"),
    MOB_IRONGOLEM_WALK(-1, "mob.irongolem.walk"),
    MOB_MAGMACUBE_BIG(-1, "mob.magmacube.big"),
    MOB_MAGMACUBE_JUMP(-1, "mob.magmacube.jump"),
    MOB_MAGMACUBE_SMALL(-1, "mob.magmacube.small"),
    MOB_PIG(-1, "mob.pig"),
    MOB_PIG_DEATH(-1, "mob.pig.death"),
    MOB_PIG_SAY(-1, "mob.pig.say"),
    MOB_PIG_STEP(-1, "mob.pig.step"),
    MOB_SHEEP(-1, "mob.sheep"),
    MOB_SHEEP_SAY(-1, "mob.sheep.say"),
    MOB_SHEEP_SHEAR(-1, "mob.sheep.shear"),
    MOB_SHEEP_STEP(-1, "mob.sheep.step"),
    MOB_SILVERFISH_HIT(-1, "mob.silverfish.hit"),
    MOB_SILVERFISH_KILL(-1, "mob.silverfish.kill"),
    MOB_SILVERFISH_SAY(-1, "mob.silverfish.say"),
    MOB_SILVERFISH_STEP(-1, "mob.silverfish.step"),
    MOB_SKELETON(-1, "mob.skeleton"),
    MOB_SKELETON_DEATH(-1, "mob.skeleton.death"),
    MOB_SKELETON_HURT(-1, "mob.skeleton.hurt"),
    MOB_SKELETON_SAY(-1, "mob.skeleton.say"),
    MOB_SKELETON_STEP(-1, "mob.skeleton.step"),
    MOB_SLIME(-1, "mob.slime"),
    MOB_SLIME_ATTACK(-1, "mob.slime.attack"),
    MOB_SLIME_BIG(-1, "mob.slime.big"),
    MOB_SLIME_SMALL(-1, "mob.slime.small"),
    MOB_SPIDER(-1, "mob.spider"),
    MOB_SPIDER_DEATH(-1, "mob.spider.death"),
    MOB_SPIDER_SAY(-1, "mob.spider.say"),
    MOB_SPIDER_STEP(-1, "mob.spider.step"),
    MOB_VILLAGER_DEATH(-1, "mob.villager.death"),
    MOB_VILLAGER_HAGGLE(-1, "mob.villager.haggle"),
    MOB_VILLAGER_HIT(-1, "mob.villager.hit"),
    MOB_VILLAGER_IDLE(-1, "mob.villager.idle"),
    MOB_VILLAGER_NO(-1, "mob.villager.no"),
    MOB_VILLAGER_YES(-1, "mob.villager.yes"),
    MOB_WITHER_DEATH(-1, "mob.wither.death"),
    MOB_WITHER_HURT(-1, "mob.wither.hurt"),
    MOB_WITHER_IDLE(-1, "mob.wither.idle"),
    MOB_WITHER_SHOOT(-1, "mob.wither.shoot"),
    MOB_WITHER_SPAWN(-1, "mob.wither.spawn"),
    MOB_WOLF_BARK(-1, "mob.wolf.bark"),
    MOB_WOLF_DEATH(-1, "mob.wolf.death"),
    MOB_WOLF_GROWL(-1, "mob.wolf.growl"),
    MOB_WOLF_HOWL(-1, "mob.wolf.howl"),
    MOB_WOLF_HURT(-1, "mob.wolf.hurt"),
    MOB_WOLF_PANTING(-1, "mob.wolf.panting"),
    MOB_WOLF_SHAKE(-1, "mob.wolf.shake"),
    MOB_WOLF_STEP(-1, "mob.wolf.step"),
    MOB_WOLF_WHINE(-1, "mob.wolf.whine"),
    MOB_ZOMBIE(-1, "mob.zombie"),
    MOB_ZOMBIE_DEATH(-1, "mob.zombie.death"),
    MOB_ZOMBIE_HURT(-1, "mob.zombie.hurt"),
    MOB_ZOMBIE_INFECT(-1, "mob.zombie.infect"),
    MOB_ZOMBIE_METAL(-1, "mob.zombie.metal"),
    MOB_ZOMBIE_REMEDY(-1, "mob.zombie.remedy"),
    MOB_ZOMBIE_SAY(-1, "mob.zombie.say"),
    MOB_ZOMBIE_STEP(-1, "mob.zombie.step"),
    MOB_ZOMBIE_UNFECT(-1, "mob.zombie.unfect"),
    MOB_ZOMBIE_WOOD(-1, "mob.zombie.wood"),
    MOB_ZOMBIE_WOODBREAK(-1, "mob.zombie.woodbreak"),
    MOB_ZOMBIEPIG_ZPIG(-1, "mob.zombiepig.zpig"),
    MOB_ZOMBIEPIG_ZPIGANGRY(-1, "mob.zombiepig.zpigangry"),
    MOB_ZOMBIEPIG_ZPIGDEATH(-1, "mob.zombiepig.zpigdeath"),
    MOB_ZOMBIEPIG_ZPIGHURT(-1, "mob.zombiepig.zpighurt"),
    MUSIC_GAME_CALM(-1, "music.game.calm"),
    MUSIC_GAME_CREATIVE_CREATIVE(-1, "music.game.creative.creative"),
    MUSIC_GAME_END_BOSS(-1, "music.game.end.boss"),
    MUSIC_GAME_END_CREDITS(-1, "music.game.end.credits"),
    MUSIC_GAME_END_END(-1, "music.game.end.end"),
    MUSIC_GAME_HAL(-1, "music.game.hal"),
    MUSIC_GAME_NETHER_NETHER(-1, "music.game.nether.nether"),
    MUSIC_GAME_NUANCE(-1, "music.game.nuance"),
    MUSIC_GAME_PIANO(-1, "music.game.piano"),
    MUSIC_MENU_MENU(-1, "music.menu.menu"),
    NOTE_BASS(-1, "note.bass"),
    NOTE_BASSATTACK(-1, "note.bassattack"),
    NOTE_BD(-1, "note.bd"),
    NOTE_HARP(-1, "note.harp"),
    NOTE_HAT(-1, "note.hat"),
    NOTE_PLING(-1, "note.pling"),
    NOTE_SNARE(-1, "note.snare"),
    PORTAL_PORTAL(-1, "portal.portal"),
    PORTAL_TRAVEL(-1, "portal.travel"),
    PORTAL_TRIGGER(-1, "portal.trigger"),
    RANDOM_ANVIL_LAND(-1, "random.anvil_land"),
    RANDOM_BOW(-1, "random.bow"),
    RANDOM_BOWHIT(-1, "random.bowhit"),
    RANDOM_BREATH(-1, "random.breath"),
    RANDOM_BURP(-1, "random.burp"),
    RANDOM_CHESTCLOSED(-1, "random.chestclosed"),
    RANDOM_CHESTOPEN(-1, "random.chestopen"),
    RANDOM_CLASSIC_HURT(-1, "random.classic_hurt"),
    RANDOM_DOOR_CLOSE(-1, "random.door_close"),
    RANDOM_DOOR_OPEN(-1, "random.door_open"),
    RANDOM_DRINK(-1, "random.drink"),
    RANDOM_DRR(-1, "random.drr"),
    RANDOM_EAT(-1, "random.eat"),
    RANDOM_FUSE(-1, "random.fuse"),
    RANDOM_GLASS(-1, "random.glass"),
    RANDOM_HURT(-1, "random.hurt"),
    RANDOM_LEVELUP(-1, "random.levelup"),
    RANDOM_ORB(-1, "random.orb"),
    RANDOM_POP(-1, "random.pop"),
    RANDOM_SPLASH(-1, "random.splash"),
    RANDOM_SUCCESSFUL_HIT(-1, "random.successful_hit"),
    RANDOM_WOOD_CLICK(-1, "random.wood_click"),
    RECORDS_11(-1, "records.11"),
    RECORDS_13(-1, "records.13"),
    RECORDS_BLOCKS(-1, "records.blocks"),
    RECORDS_CAT(-1, "records.cat"),
    RECORDS_CHIRP(-1, "records.chirp"),
    RECORDS_FAR(-1, "records.far"),
    RECORDS_MALL(-1, "records.mall"),
    RECORDS_MELLOHI(-1, "records.mellohi"),
    RECORDS_STAL(-1, "records.stal"),
    RECORDS_STRAD(-1, "records.strad"),
    RECORDS_WAIT(-1, "records.wait"),
    RECORDS_WARD(-1, "records.ward"),
    STEP_CLOTH(-1, "step.cloth"),
    STEP_GRASS(-1, "step.grass"),
    STEP_GRAVEL(-1, "step.gravel"),
    STEP_LADDER(-1, "step.ladder"),
    STEP_SAND(-1, "step.sand"),
    STEP_SNOW(-1, "step.snow"),
    STEP_STONE(-1, "step.stone"),
    STEP_WOOD(-1, "step.wood"),
    TILE_PISTON_IN(-1, "tile.piston.in"),
    TILE_PISTON_OUT(-1, "tile.piston.out"),

    NONE(-1, "");

    /**
     * Internal mapping by {@code int} id
     */
    private static final Map<Integer, SoundResource> ID_SOUND_MAP = new ConcurrentHashMap<>();
    /**
     * Internal mapping by {@code String} ResourceLocation
     */
    private static final Map<String, SoundResource> RESOURCE_STR_SOUND_MAP = new ConcurrentHashMap<>();

    static {
        EnumSet.allOf(SoundResource.class)
            .forEach(sound -> { if (sound.id >= 0) ID_SOUND_MAP.put(sound.id, sound); });
        EnumSet.allOf(SoundResource.class)
            .forEach(sound -> RESOURCE_STR_SOUND_MAP.put(sound.resourceLocation.toString(), sound));
    }

    /**
     * This Sound's identifier
     */
    public final int id;

    /**
     * The {@link ResourceLocation} of this {@link SoundResource}
     */
    public final ResourceLocation resourceLocation;

    SoundResource(final int id, final ResourceLocation resourceLocation) {
        this.id = id;
        this.resourceLocation = resourceLocation;
    }

    SoundResource(final int id, final String resourcePath) {
        this(id, new ResourceLocation(resourcePath));
    }

    SoundResource(final int id, final String resourceDomain, final String resourcePath) {
        this(id, new ResourceLocation(resourceDomain.toLowerCase(Locale.ENGLISH), resourcePath));
    }

    /**
     * @param id The Sounds identifier
     * @return The {@link SoundResource}
     */
    public static SoundResource get(int id) {
        return ID_SOUND_MAP.get(id);
    }

    /**
     * @param resourceStr The {@link ResourceLocation}'s String of the {@link SoundResource}
     * @return The {@link SoundResource}
     */
    public static SoundResource get(String resourceStr) {
        return RESOURCE_STR_SOUND_MAP.get(resourceStr);
    }

    /**
     * Provides a backward-compatible Sounds {@code Map<Integer, String>} sound list
     *
     * @return The map for the deprecated {@link gregtech.api.GregTech_API#sSoundList}
     * @deprecated This method is planned for removal.
     *             <p>
     *             Use this {@link SoundResource} enum instead.
     *             </p>
     */
    @Deprecated
    public static Map<Integer, String> asSoundList() {
        return Maps.transformValues(ID_SOUND_MAP, SoundResource::toString);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String toString() {
        return this.resourceLocation.toString();
    }
}
