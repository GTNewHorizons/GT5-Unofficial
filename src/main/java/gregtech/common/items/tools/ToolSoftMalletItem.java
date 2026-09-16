package gregtech.common.items.tools;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone soft mallet: toggles GregTech machines on and off, and taps the few vanilla blocks that respond to it.
 * <p/>
 * There is no electric soft mallet, so unlike the wrench this is a single item. See {@link ToolItemBase} for
 * everything it has in common with the other standalone tools.
 */
public class ToolSoftMalletItem extends ToolItemBase {

    /**
     * Tool modes. Mode 0 toggles whatever the machine is currently doing; these two force it one way, so that a mallet
     * swept across a row of machines leaves them all in the same state.
     * <p/>
     * These keep the values {@code BehaviourSoftMallet} used, because {@code BaseMetaTileEntity} reads them off saved
     * stacks that were written under the old numbering.
     */
    public static final int MODE_ACTIVATE = 1;

    public static final int MODE_DEACTIVATE = 2;

    /** Durability cost of one use, in the unit where 100 is one durability point. */
    public static final int USE_COST = 100;

    /**
     * @param unlocalizedName   appended to {@code gt.}; becomes both the registry name and the localization key root.
     * @param toolStats         the generic stats, reused verbatim from the old tool registry.
     * @param englishNameFormat the default display name, where {@code %material} is replaced by the material name.
     */
    public ToolSoftMalletItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            "",
            ToolDictNames.craftingToolSoftMallet,
            GregTechAPI.sSoftMalletList);
    }

    /* ---------- USE ---------- */

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return SoftMalletActions.use(this, stack, player, world, x, y, z, hitX, hitY, hitZ, USE_COST);
    }

    /* ---------- DISPLAY ---------- */

    @Override
    protected void addBehaviourToolTips(List<String> list, ItemStack stack) {
        list.add(translateToLocal("gt.softmallet.tooltip"));
        list.add(translateToLocal("gt.softmallet.tooltip.mode"));
    }
}
