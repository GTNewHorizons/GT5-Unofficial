package bartworks.common.configs;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.BART_WORKS, filename = "bartworks")
public class Configuration {

    public static final Mixins mixins = new Mixins();
    public static final CrossModInteractions crossModInteractions = new CrossModInteractions();

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
}
