package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone screwdriver: adjusts covers and machines, and answers to ProjectRed's screwdriver interface.
 * <p/>
 * One instance of this class is registered per tier. The electric tiers use {@link ToolScrewdriverElectricItem},
 * which stores energy instead of durability and so cannot extend this class (see {@link ToolElectricItemBase}); what
 * a screwdriver actually does -- including the cross-mod interface -- lives entirely in {@link ScrewdriverBehavior},
 * where both reach it. See {@link ToolItemBase} for everything these have in common with the other standalone tools.
 */
public class ToolScrewdriverItem extends ToolItemBase implements ScrewdriverBehavior {

    /**
     * @param unlocalizedName   appended to {@code gt.}; becomes both the registry name and the localization key root.
     * @param toolStats         the generic stats for this tier, reused verbatim from the old tool registry.
     * @param englishNameFormat the default display name, where {@code %material} is replaced by the material name.
     */
    public ToolScrewdriverItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            "Adjusts covers and machines",
            GregTechAPI.sScrewdriverList,
            ToolDictNames.craftingToolScrewdriver);
    }

    /* ---------- USE ---------- */

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        return screwdriverOnItemUseFirst(stack, player, world, x, y, z, ordinalSide, hitX, hitY, hitZ);
    }
}
