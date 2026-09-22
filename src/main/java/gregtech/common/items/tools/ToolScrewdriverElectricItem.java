package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * An electric screwdriver: LV, MV or HV.
 * <p/>
 * These run on EU only, via {@link ToolElectricItemBase} -- the old metadata-based electric screwdrivers carried a
 * durability bar as well, but only lost a point of it on one action in twenty-five, so it was noise on top of the
 * energy cost; the energy cost per action is unchanged, since {@code GTModHandler.damageOrDechargeItem} always
 * reached these through {@code IDamagableItem} and so spent 100 units per use either way.
 * <p/>
 * This cannot extend {@link ToolScrewdriverItem}, because it already has to extend {@link ToolElectricItemBase} for
 * its energy model. What a screwdriver actually does lives entirely in {@link ScrewdriverBehavior} instead, shared
 * with {@link ToolScrewdriverItem} by composition rather than inheritance.
 */
public class ToolScrewdriverElectricItem extends ToolElectricItemBase implements ScrewdriverBehavior {

    /** Energy one action costs: an adjustment, which used to cost 100. */
    public static final long EU_PER_USE = 100;

    @Override
    public long getEnergyCostPerUse() {
        return EU_PER_USE;
    }

    public ToolScrewdriverElectricItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        long maxCharge, long voltage, int tier) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            "Adjusts covers and machines",
            maxCharge,
            voltage,
            tier,
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
