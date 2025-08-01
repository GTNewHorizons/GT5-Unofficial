package bartworks.common.configs;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.ModIDs.BART_WORKS, filename = "bartworks")
@Config.LangKeyPattern(pattern = "GT5U.gui.config.%cat.%field", fullyQualified = true)
@Config.RequiresMcRestart
public class Configuration {

    public static final Mixins mixins = new Mixins();

    public static final CrossModInteractions crossModInteractions = new CrossModInteractions();

    public static final Multiblocks multiblocks = new Multiblocks();

    public static final Tooltip tooltip = new Tooltip();

    public static final SingleBlocks singleBlocks = new SingleBlocks();

    public static final RossRuinMetas rossRuinMetas = new RossRuinMetas();

    @Config.Comment("Mixins section.")
    public static class Mixins {

        @Config.Comment("if true, patches the crafting manager to cache recipes in class: net.minecraft.item.crafting.CraftingManager")
        @Config.DefaultBoolean(false)
        public boolean enableCraftingManagerRecipeCaching = false;
    }

    @Config.Comment("Crossmod interactions section.")
    public static class CrossModInteractions {

        @Config.Comment("The Dim ID for Ross128b")
        @Config.DefaultInt(64)
        public int ross128BID;

        @Config.Comment("The Dim ID for Ross128ba (Ross128b's Moon)")
        @Config.DefaultInt(63)
        public int ross128BAID;

        @Config.Ignore()
        public static int ross128btier = 3;

        @Config.Ignore()
        public static int ross128batier = 5;

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

    @Config.Comment("Multiblocks section.")
    public static class Multiblocks {

        @Config.Comment("This will set Up the Energy per LESU Cell")
        @Config.DefaultInt(20_000_000)
        public int energyPerCell;

        @Config.Ignore()
        public static int megaMachinesMax = 256;

        @Config.Ignore()
        public static int bioVatMaxParallelBonus = 1_000;

        @Config.Comment({
            "This is a blacklist for the Void Miner, blacklisted ores will not enter the drop prize pool.",
            "Please fill in the Unique Identifier of Ore and connect Damage with a colon,",
            "For example: gregtech:gt.blockores:32" })
        @Config.DefaultStringList({})
        public String[] voidMinerBlacklist;

        @Config.Comment("This switch completely disables piston animation in Electric Implosion Compressor multiblock")
        @Config.DefaultBoolean(false)
        public boolean disablePistonInEIC;
    }

    @Config.Comment("Tooltip section.")
    public static class Tooltip {

        @Config.Comment("If true, add glass tier in tooltips.")
        @Config.DefaultBoolean(true)
        public boolean addGlassTierInTooltips;
    }

    @Config.Comment("Single blocks section.")
    public static class SingleBlocks {

        @Config.Comment("mL Water per Sec for the StirlingPump")
        @Config.DefaultInt(150)
        public int mbWaterperSec;
    }

    @Config.Comment("Ross' ruins machine metaIDs section.")
    public static class RossRuinMetas {

        @Config.Ignore()
        public static final byte maxTierRoss = 5;

        public HighPressureSteam highPressureSteam = new HighPressureSteam();
        public LV lv = new LV();
        public MV mv = new MV();
        public HV hv = new HV();
        public EV ev = new EV();

        @Config.Comment("Possible machines for high pressure steam ruins.")
        public static class HighPressureSteam {

            @Config.Comment("MetaIDs of the GT machines for the buffer slots")
            @Config.DefaultIntList({ 5133, 5123 })
            public int[] buffers;

            @Config.Comment("MetaIDs of the GT machines for the cable slots")
            @Config.DefaultIntList({ 5133, 5123 })
            public int[] cables;

            @Config.Comment("MetaIDs of the GT machines for the generator slots")
            @Config.DefaultIntList({ 100, 101, 102, 105 })
            public int[] generators;

            @Config.Comment("MetaIDs of the GT machines for the machine slots")
            @Config.DefaultIntList({ 103, 104, 106, 107, 109, 110, 112, 113, 115, 116, 118, 119 })
            public int[] machines;
        }

        @Config.Comment("Possible machines for LV ruins.")
        public static class LV {

            @Config.Comment("MetaIDs of the GT machines for the buffer slots")
            @Config.DefaultIntList({ 161, 171, 181, 191 })
            public int[] buffers;

            @Config.Comment("MetaIDs of the GT machines for the cable slots")
            @Config.DefaultIntList({ 1210, 1230, 1250, 1270, 1290 })
            public int[] cables;

            @Config.Comment("MetaIDs of the GT machines for the generator slots")
            @Config.DefaultIntList({ 1110, 1115, 1120, 1127 })
            public int[] generators;

            @Config.Comment("MetaIDs of the GT machines for the machine slots")
            @Config.DefaultIntList({ 201, 211, 221, 231, 241, 251, 261, 271, 281, 291, 301, 311, 321, 331, 341, 351,
                361, 371, 381, 391, 401, 411, 421, 431, 441, 451, 461, 471, 481, 491, 501, 511, 521, 531, 541, 551, 561,
                571, 581, 591, 601, 611, 621, 631, 641, 651, 661, 671 })
            public int[] machines;
        }

        @Config.Comment("Possible machines for MV ruins.")
        public static class MV {

            @Config.Comment("MetaIDs of the GT machines for the buffer slots")
            @Config.DefaultIntList({ 162, 172, 182, 192 })
            public int[] buffers;

            @Config.Comment("MetaIDs of the GT machines for the cable slots")
            @Config.DefaultIntList({ 1310, 1330, 1350, 1370, 1390 })
            public int[] cables;

            @Config.Comment("MetaIDs of the GT machines for the generator slots")
            @Config.DefaultIntList({ 1111, 12726, 1116, 1121, 1128 })
            public int[] generators;

            @Config.Comment("MetaIDs of the GT machines for the machine slots")
            @Config.DefaultIntList({ 202, 212, 222, 232, 242, 252, 262, 272, 282, 292, 302, 312, 322, 332, 342, 352,
                362, 372, 382, 392, 402, 412, 422, 432, 442, 452, 462, 472, 482, 492, 502, 512, 522, 532, 542, 552, 562,
                572, 582, 592, 602, 612, 622, 632, 642, 652, 662, 672 })
            public int[] machines;
        }

        @Config.Comment("Possible machines for HV ruins.")
        public static class HV {

            @Config.Comment("MetaIDs of the GT machines for the buffer slots")
            @Config.DefaultIntList({ 163, 173, 183, 193 })
            public int[] buffers;

            @Config.Comment("MetaIDs of the GT machines for the cable slots")
            @Config.DefaultIntList({ 1410, 1430, 1450, 1470, 1490 })
            public int[] cables;

            @Config.Comment("MetaIDs of the GT machines for the generator slots")
            @Config.DefaultIntList({ 1112, 12727, 1117, 1122, 1129 })
            public int[] generators;

            @Config.Comment("MetaIDs of the GT machines for the machine slots")
            @Config.DefaultIntList({ 203, 213, 223, 233, 243, 253, 263, 273, 283, 293, 303, 313, 323, 333, 343, 353,
                363, 373, 383, 393, 403, 413, 423, 433, 443, 453, 463, 473, 483, 493, 503, 513, 523, 533, 543, 553, 563,
                573, 583, 593, 603, 613, 623, 633, 643, 653, 663, 673 })
            public int[] machines;
        }

        @Config.Comment("Possible machines for EV ruins.")
        public static class EV {

            @Config.Comment("MetaIDs of the GT machines for the buffer slots")
            @Config.DefaultIntList({ 164, 174, 184, 194 })
            public int[] buffers;

            @Config.Comment("MetaIDs of the GT machines for the cable slots")
            @Config.DefaultIntList({ 1510, 1530, 1550, 1570, 1590 })
            public int[] cables;

            @Config.Comment("MetaIDs of the GT machines for the generator slots")
            @Config.DefaultIntList({ 12728, 1190, 1130, 12685 })
            public int[] generators;

            @Config.Comment("MetaIDs of the GT machines for the machine slots")
            @Config.DefaultIntList({ 204, 214, 224, 234, 244, 254, 264, 274, 284, 294, 304, 314, 324, 334, 344, 354,
                364, 374, 384, 394, 404, 414, 424, 434, 444, 454, 464, 474, 484, 494, 504, 514, 524, 534, 544, 554, 564,
                574, 584, 594, 604, 614, 624, 634, 644, 654, 664, 674 })
            public int[] machines;
        }
    }
}
