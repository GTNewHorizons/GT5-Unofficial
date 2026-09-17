package gregtech.common.items.tools;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone jackhammer: LV, MV or HV. There is no hand jackhammer, so every tier is electric.
 * <p/>
 * See {@link ToolElectricItemBase} for the energy handling and {@link ToolItemBase} for the rest.
 */
public class ToolJackHammerItem extends ToolElectricItemBase {

    public ToolJackHammerItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip, long maxCharge, long voltage, int tier) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            englishTooltip,
            maxCharge,
            voltage,
            tier,
            GregTechAPI.sJackhammerList,
            ToolDictNames.craftingToolJackHammer,
            ToolDictNames.craftingToolHardHammer);
    }
}
