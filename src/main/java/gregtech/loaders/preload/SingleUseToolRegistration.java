package gregtech.loaders.preload;

import static gregtech.api.enums.ToolDictNames.craftingToolCrowbar;
import static gregtech.api.enums.ToolDictNames.craftingToolFile;
import static gregtech.api.enums.ToolDictNames.craftingToolHardHammer;
import static gregtech.api.enums.ToolDictNames.craftingToolSaw;
import static gregtech.api.enums.ToolDictNames.craftingToolScrewdriver;
import static gregtech.api.enums.ToolDictNames.craftingToolSoftMallet;
import static gregtech.api.enums.ToolDictNames.craftingToolWireCutter;
import static gregtech.api.enums.ToolDictNames.craftingToolWrench;
import static gregtech.common.items.IDMetaTool01.CROWBAR;
import static gregtech.common.items.IDMetaTool01.FILE;
import static gregtech.common.items.IDMetaTool01.HARDHAMMER;
import static gregtech.common.items.IDMetaTool01.SAW;
import static gregtech.common.items.IDMetaTool01.SCREWDRIVER;
import static gregtech.common.items.IDMetaTool01.SOFTMALLET;
import static gregtech.common.items.IDMetaTool01.WIRECUTTER;
import static gregtech.common.items.IDMetaTool01.WRENCH;
import static gregtech.common.items.MetaGeneratedTool01.INSTANCE;
import static gregtech.loaders.postload.SingleUseToolRecipeLoader.addSingleUseToolType;

import gregtech.api.enums.OrePrefixes;

public class SingleUseToolRegistration implements Runnable {

    public void run() {
        long plate = OrePrefixes.plate.getMaterialAmount();
        long ingot = OrePrefixes.ingot.getMaterialAmount();
        long screw = OrePrefixes.screw.getMaterialAmount();
        long rod = OrePrefixes.stick.getMaterialAmount();

        addSingleUseToolType(craftingToolFile, INSTANCE.mToolStats.get((short) FILE.ID), 2 * plate);
        addSingleUseToolType(craftingToolWrench, INSTANCE.mToolStats.get((short) WRENCH.ID), 6 * ingot);
        addSingleUseToolType(craftingToolCrowbar, INSTANCE.mToolStats.get((short) CROWBAR.ID), 3 * rod);
        addSingleUseToolType(
            craftingToolWireCutter,
            INSTANCE.mToolStats.get((short) WIRECUTTER.ID),
            3 * plate + 2 * rod + screw);
        addSingleUseToolType(craftingToolHardHammer, INSTANCE.mToolStats.get((short) HARDHAMMER.ID), 6 * ingot);
        addSingleUseToolType(craftingToolSoftMallet, INSTANCE.mToolStats.get((short) SOFTMALLET.ID), 6 * ingot);
        addSingleUseToolType(craftingToolScrewdriver, INSTANCE.mToolStats.get((short) SCREWDRIVER.ID), 2 * rod);
        addSingleUseToolType(craftingToolSaw, INSTANCE.mToolStats.get((short) SAW.ID), 2 * plate);
    }
}
