package gregtech.api.metatileentity.implementations;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.lwjgl.input.Keyboard;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

/**
 * A multiblock with tooltip {@link GT_Multiblock_Tooltip_Builder}
 */
public abstract class GT_MetaTileEntity_TooltipMultiBlockBase extends GT_MetaTileEntity_MultiBlockBase
    implements ISecondaryDescribable {

    private static final AtomicReferenceArray<GT_Multiblock_Tooltip_Builder> tooltips = new AtomicReferenceArray<>(
        GregTech_API.METATILEENTITIES.length);

    public GT_MetaTileEntity_TooltipMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TooltipMultiBlockBase(String aName) {
        super(aName);
    }

    protected GT_Multiblock_Tooltip_Builder getTooltip() {
        int tId = getBaseMetaTileEntity().getMetaTileID();
        GT_Multiblock_Tooltip_Builder tooltip = tooltips.get(tId);
        if (tooltip == null) {
            tooltip = createTooltip();
            tooltips.set(tId, tooltip);
        }
        return tooltip;
    }

    protected abstract GT_Multiblock_Tooltip_Builder createTooltip();

    @Override
    public String[] getDescription() {
        return getCurrentDescription();
    }

    @Override
    public boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    public String[] getPrimaryDescription() {
        return getTooltip().getInformation();
    }

    public String[] getSecondaryDescription() {
        return getTooltip().getStructureInformation();
    }
}
