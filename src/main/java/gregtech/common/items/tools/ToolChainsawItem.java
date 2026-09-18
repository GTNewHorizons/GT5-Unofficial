package gregtech.common.items.tools;

import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone chainsaw: LV, MV or HV. There is no hand chainsaw, so every tier is electric.
 * <p/>
 * See {@link ToolElectricItemBase} for the energy handling and {@link ToolItemBase} for the rest.
 */
public class ToolChainsawItem extends ToolElectricItemBase {

    /** Energy one action costs: a block break, which used to be 100 per point of hardness. */
    public static final long EU_PER_USE = 100;

    @Override
    public long getEnergyCostPerUse() {
        return EU_PER_USE;
    }

    public ToolChainsawItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
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
            ToolDictNames.craftingToolSaw);
    }
}
