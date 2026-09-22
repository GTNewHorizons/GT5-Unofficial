package gregtech.common.items.tools;

import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * An electric file: LV, MV or HV.
 * <p/>
 * These run on EU only, via {@link ToolElectricItemBase} -- the old metadata-based ones carried a durability bar as
 * well, but only lost a point of it on one action in twenty-five, so it was noise on top of the energy cost; the
 * energy cost per action is unchanged, since {@code GTModHandler.damageOrDechargeItem} always reached these through
 * {@code IDamagableItem} and so spent 100 units per use either way.
 * <p/>
 * No machine responds to a file and it has no right-click behaviour or cross-mod interface (see {@link ToolFileItem}
 * for the hand version), so unlike the wrench, screwdriver and wire cutter families there is nothing beyond the
 * energy model to share -- this extends {@link ToolElectricItemBase} directly and adds only the per-action cost.
 */
public class ToolFileElectricItem extends ToolElectricItemBase {

    /** Energy one action costs: being consumed by a recipe, which used to cost 400. */
    public static final long EU_PER_USE = 400;

    @Override
    public long getEnergyCostPerUse() {
        return EU_PER_USE;
    }

    public ToolFileElectricItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip, long maxCharge, long voltage, int tier) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            englishTooltip,
            maxCharge,
            voltage,
            tier,
            null,
            ToolDictNames.craftingToolFile);
    }
}
