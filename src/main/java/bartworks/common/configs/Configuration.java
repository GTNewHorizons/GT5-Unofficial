package bartworks.common.configs;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.BART_WORKS, filename = "bartworks")
public class Configuration {

    public static final Mixins mixins = new Mixins();
    public static final CrossModInteractions crossModInteractions = new CrossModInteractions();

    public static final Multiblocks multiblocks = new Multiblocks();

    public static final System system = new System();

    public static final SingleBlocks singleBlocks = new SingleBlocks();

    @Config.LangKey("GT5U.gui.config.mixins")
    @Config.RequiresMcRestart
    public static class Mixins{

        @Config.Comment("if true, patches the crafting manager to cache recipes in class: net.minecraft.item.crafting.CraftingManager")
        @Config.DefaultBoolean(false)
        public boolean enableCraftingManagerRecipeCaching=false;
    }

    public static class CrossModInteractions{
        @Config.Comment("The Dim ID for Ross128b")
        @Config.DefaultInt(64)
        public int ross128BID;

        @Config.Comment("The Dim ID for Ross128ba (Ross128b's Moon)")
        @Config.DefaultInt(63)
        public int ross128BAID;

        @Config.Comment("The Rocket Tier for Ross128b")
        @Config.DefaultInt(3)
        public int ross128btier;

        @Config.Comment("The Rocket Tier for Ross128a")
        @Config.DefaultInt(5)
        public int ross128batier;

        @Config.Comment("Higher Values mean lesser Ruins.")
        @Config.DefaultInt(512)
        public int ross128bRuinChance;

        @Config.Comment("If the Ross128 System should be activated, DO NOT CHANGE AFTER WORLD GENERATION")
        @Config.DefaultBoolean(true)
        public boolean Ross128Enabled;

        @Config.Comment("1 = Moon Lander, 2 = Landing Balloons, 3 = Asteroid Lander")
        @Config.DefaultEnum("AsteroidsLander")
        public LanderType landerType;

        @Config.Comment("True disables the magical Forest Biome on Ross for more performance during World generation.")
        @Config.DefaultBoolean(false)
        public boolean disableMagicalForest;
    }

    public static class Multiblocks{
        @Config.Comment("This will set Up the Energy per LESU Cell")
        @Config.DefaultInt(1_000_000)
        public int energyPerCell;

        @Config.Comment("This switch enables the Direct Steam Mode of the DEHP. If enabled it will take in Waterand output steam. If disabled it will Input IC2Coolant and output hot coolant")
        @Config.DefaultBoolean(false)
        public boolean DEHPDirectSteam;

        @Config.Comment("This changes the Maximum Recipes per Operation to the specified value")
        @Config.DefaultInt(256)
        public int megaMachinesMax;

        @Config.Comment("This are the maximum parallel Operations the BioVat can do, when the output is half full.")
        @Config.DefaultInt(1_000)
        public int bioVatMaxParallelBonus;

        @Config.Comment({
            "This is a blacklist for the Void Miner, blacklisted ores will not enter the drop prize pool.",
            "Please fill in the Unique Identifier of Ore and connect Damage with a colon,",
            "For example: gregtech:gt.blockores:32"
        })
        @Config.DefaultStringList({})
        public String[] voidMinerBlacklist;

        @Config.Comment("This switch completely disables piston animation in Electric Implosion Compressor multiblock")
        @Config.DefaultBoolean(false)
        public boolean disablePistonInEIC;
    }


    public static class System{
        @Config.Comment("Enables the classic Mode (all recipes in normal machines are doable in MV")
        @Config.DefaultBoolean(false)
        public boolean classicMode;

        @Config.Comment("If you wish to enable extra tooltips")
        @Config.DefaultBoolean(true)
        public boolean tooltips;

        @Config.Comment("Enables the Teslastaff, an Item used to destroy Electric Armors")
        @Config.DefaultBoolean(false)
        public boolean teslastaff;

        @Config.Comment("This switch sets the lowest unnerfed Circuit Recipe Tier. -1 to disable it completely.")
        @Config.DefaultInt(5)
        public int cutoffTier;

        @Config.Comment("This switch disables extra gas recipes for the EBF, i.e. Xenon instead of Nitrogen")
        @Config.DefaultBoolean(false)
        public boolean disableExtraGassesForEBF;

        @Config.Comment("This switch disable the generation of bolted casings")
        @Config.DefaultBoolean(false)
        public boolean disableBoltedBlocksCasing;

        @Config.Comment("This switch disable the generation of rebolted casings")
        @Config.DefaultBoolean(false)
        public boolean disableReboltedBlocksCasing;

        @Config.Comment("Enables or Disables the debug log.")
        @Config.DefaultBoolean(false)
        public boolean debugLog;
    }

    public static class SingleBlocks{
        @Config.Comment("mL Water per Sec for the StirlingPump")
        @Config.DefaultInt(150)
        public int mbWaterperSec;
    }
}
