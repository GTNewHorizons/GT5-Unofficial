package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone soldering iron: LV, MV or HV. There is no hand soldering iron, so every tier is electric.
 * <p/>
 * Like the screwdriver it adjusts repeaters and comparators, which is why {@link ScrewdriverActions} is shared
 * between the two. Repairing burned-out circuits is not here: machines pull that off the stack themselves through
 * {@link GregTechAPI#sSolderingToolList} and {@code GTModHandler.useSolderingIron}, both of which work off the IC2
 * electric interfaces and so need no changes.
 * <p/>
 * See {@link ToolElectricItemBase} for the energy handling and {@link ToolItemBase} for the rest.
 */
public class ToolSolderingIronItem extends ToolElectricItemBase {

    /** Energy cost of one repeater or comparator adjustment, in the unit where 100 is one durability point. */
    public static final int ADJUST_COST = 100;

    public ToolSolderingIronItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip, long maxCharge, long voltage, int tier) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            englishTooltip,
            maxCharge,
            voltage,
            tier,
            GregTechAPI.sSolderingToolList,
            ToolDictNames.craftingToolSolderingIron);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return ScrewdriverActions.use(
            world,
            x,
            y,
            z,
            hitX,
            hitY,
            hitZ,
            () -> player.capabilities.isCreativeMode || doDamage(stack, ADJUST_COST));
    }
}
