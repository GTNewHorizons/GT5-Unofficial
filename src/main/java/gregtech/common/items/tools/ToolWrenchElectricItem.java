package gregtech.common.items.tools;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * An electric wrench: LV, MV or HV.
 * <p/>
 * These run on EU only, via {@link ToolElectricItemBase} -- the old metadata-based electric wrenches carried a
 * durability bar as well, but only lost a point of it on one action in twenty-five, so it was noise on top of the
 * energy cost; the energy cost per action is unchanged from the old {@code IToolStats} numbers, which is where the
 * balance actually lived.
 * <p/>
 * This cannot extend {@link ToolWrenchItem} the way the hand wrench's durability model would suggest, because it
 * already has to extend {@link ToolElectricItemBase} for its energy model and Java allows only one superclass. What
 * a wrench actually does -- rotating blocks, the cross-mod interfaces -- lives entirely in {@link WrenchBehavior}
 * instead, shared with {@link ToolWrenchItem} by composition rather than inheritance.
 */
public class ToolWrenchElectricItem extends ToolElectricItemBase implements WrenchBehavior {

    /** Energy one action costs: a rotation, which used to cost 100. */
    public static final long EU_PER_USE = 100;

    @Override
    public long getEnergyCostPerUse() {
        return EU_PER_USE;
    }

    public ToolWrenchElectricItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip, long maxCharge, long voltage, int tier) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            englishTooltip,
            maxCharge,
            voltage,
            tier,
            GregTechAPI.sWrenchList,
            ToolDictNames.craftingToolWrench);
    }

    /* ---------- USE ---------- */

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        return wrenchOnItemUseFirst(stack, player, world, x, y, z, ordinalSide, hitX, hitY, hitZ);
    }

    /* ---------- DISPLAY ---------- */

    @Override
    protected void addBehaviourToolTips(List<String> list, ItemStack stack) {
        addWrenchBehaviourToolTip(list);
    }
}
