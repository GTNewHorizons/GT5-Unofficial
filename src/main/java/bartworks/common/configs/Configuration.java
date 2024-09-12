package bartworks.common.configs;

import com.gtnewhorizon.gtnhlib.config.Config;
import gregtech.api.enums.Mods;

@Config(modid = Mods.Names.BART_WORKS, filename = "bartworks")
public class Configuration {

    public static final Mixins mixins = new Mixins();

    @Config.LangKey("GT5U.gui.config.mixins")
    @Config.RequiresMcRestart
    public static class Mixins{

        @Config.Comment("if true, patches the crafting manager to cache recipes in class: net.minecraft.item.crafting.CraftingManager")
        @Config.DefaultBoolean(false)
        public boolean enableCraftingManagerRecipeCaching=false;
    }
}
