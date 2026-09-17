package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.Optional;
import forestry.api.arboriculture.IToolGrafter;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone branch cutter: harvests saplings from leaves, and answers to Forestry's grafter interface.
 * <p/>
 * See {@link ToolItemBase} for everything it has in common with the other standalone tools.
 */
@Optional.Interface(iface = "forestry.api.arboriculture.IToolGrafter", modid = Mods.ModIDs.FORESTRY)
public class ToolBranchCutterItem extends ToolItemBase implements IToolGrafter {

    public ToolBranchCutterItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            englishTooltip,
            null,
            ToolDictNames.craftingToolBranchCutter);
    }

    @Override
    public float getSaplingModifier(ItemStack stack, World world, EntityPlayer player, int x, int y, int z) {
        if (getToolMaterial(stack) == Materials._NULL) return 0.0F;
        return Math.min(100.0F, (1 + getHarvestLevel(stack, "")) * 20.0F);
    }
}
