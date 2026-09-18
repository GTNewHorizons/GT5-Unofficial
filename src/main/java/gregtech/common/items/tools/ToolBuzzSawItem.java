package gregtech.common.items.tools;

import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone buzzsaw: LV, MV or HV. There is no hand buzzsaw, so every tier is electric.
 * <p/>
 * See {@link ToolElectricItemBase} for the energy handling and {@link ToolItemBase} for the rest.
 */
public class ToolBuzzSawItem extends ToolElectricItemBase {

    /** Energy one action costs: being consumed by a recipe, which used to cost 100. */
    public static final long EU_PER_USE = 100;

    @Override
    public long getEnergyCostPerUse() {
        return EU_PER_USE;
    }

    public ToolBuzzSawItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
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
