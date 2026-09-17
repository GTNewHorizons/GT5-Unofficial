package gregtech.common.items.tools;

import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone mining drill: LV, MV or HV. There is no hand drill, so every tier is electric.
 * <p/>
 * See {@link ToolElectricItemBase} for the energy handling and {@link ToolItemBase} for the rest.
 */
public class ToolDrillItem extends ToolElectricItemBase {

    public ToolDrillItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat, long maxCharge,
        long voltage, int tier) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            "",
            maxCharge,
            voltage,
            tier,
            null,
            ToolDictNames.craftingToolMiningDrill);
    }
}
