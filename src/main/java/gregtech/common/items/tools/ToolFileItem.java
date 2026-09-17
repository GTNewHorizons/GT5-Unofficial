package gregtech.common.items.tools;

import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;

/**
 * A standalone file.
 * <p/>
 * One instance of this class is registered per tier. The electric tiers use {@link ToolFileElectricItem}, which
 * stores energy instead of durability. No machine responds to a file and it has no right-click behaviour, so this is
 * as thin as a standalone tool gets -- see {@link ToolItemBase} for everything it does.
 */
public class ToolFileItem extends ToolItemBase {

    public ToolFileItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat, String englishTooltip) {
        super(unlocalizedName, toolStats, englishNameFormat, englishTooltip, null, ToolDictNames.craftingToolFile);
    }
}
