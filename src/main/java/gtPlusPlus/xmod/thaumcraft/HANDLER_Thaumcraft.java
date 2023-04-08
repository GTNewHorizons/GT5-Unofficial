package gtPlusPlus.xmod.thaumcraft;

import static gregtech.api.enums.Mods.Thaumcraft;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.thaumcraft.aspect.GTPP_AspectCompat;
import gtPlusPlus.xmod.thaumcraft.aspect.GTPP_AspectStack;

public class HANDLER_Thaumcraft {

    public static GTPP_AspectCompat sThaumcraftCompat;
    public static Item mResearchNotes;
    public static final AutoMap<Pair<ItemStack, GTPP_AspectStack[]>> sItemsToGetAspects = new AutoMap<Pair<ItemStack, GTPP_AspectStack[]>>();

    public static void init() {
        if (Thaumcraft.isModLoaded()) {
            try {
                mResearchNotes = (Item) ReflectionUtils
                        .getField(ReflectionUtils.getClass("thaumcraft.common.config.ConfigItems"), "itemResearchNotes")
                        .get(null);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                mResearchNotes = Items.paper;
            }
        }
    }
}
