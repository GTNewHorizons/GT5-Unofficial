package gregtech.common.items.tools;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone hard hammer: crushes ores instead of harvesting them, and surveys stone for ore.
 * <p/>
 * There is no electric hard hammer -- the jackhammer is its own tool type -- so this is a single item. See
 * {@link ToolItemBase} for everything it has in common with the other standalone tools.
 */
public class ToolHardHammerItem extends ToolItemBase {

    /** Durability cost of one survey, in the unit where 100 is one durability point. */
    public static final int PROSPECT_COST = 100;

    public ToolHardHammerItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            "Crushes Ores instead of harvesting them",
            ToolDictNames.craftingToolHardHammer,
            GregTechAPI.sHardHammerList);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return ProspectingActions.prospect(
            getHarvestLevel(stack, ""),
            player,
            world,
            x,
            y,
            z,
            ForgeDirection.getOrientation(ordinalSide),
            hitX,
            hitY,
            hitZ,
            () -> player.capabilities.isCreativeMode || doDamage(stack, PROSPECT_COST));
    }

    @Override
    protected void addBehaviourToolTips(List<String> list, ItemStack stack) {
        list.add(translateToLocal("gt.behaviour.prospecting"));
    }
}
