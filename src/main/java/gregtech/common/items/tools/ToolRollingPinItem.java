package gregtech.common.items.tools;

import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone rolling pin.
 * <p/>
 * See {@link ToolItemBase} for everything it has in common with the other standalone tools.
 */
public class ToolRollingPinItem extends ToolItemBase {

    public ToolRollingPinItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            englishTooltip,
            null,
            ToolDictNames.craftingToolRollingPin);
    }
}
