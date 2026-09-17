package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone wire cutter: cuts cables, and renames AE2 machines.
 * <p/>
 * One instance of this class is registered per tier. The electric tiers use {@link ToolWireCutterElectricItem},
 * which stores energy instead of durability. See {@link ToolItemBase} for everything these have in common with the
 * other standalone tools.
 */
public class ToolWireCutterItem extends ToolItemBase {

    public ToolWireCutterItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            englishTooltip,
            GregTechAPI.sWireCutterList,
            ToolDictNames.craftingToolWireCutter);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return WireCutterActions
            .use(player, world, x, y, z, ForgeDirection.getOrientation(ordinalSide), hitX, hitY, hitZ);
    }
}
