package gregtech.api.metatileentity.implementations;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.lwjgl.input.Keyboard;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.util.MultiblockTooltipBuilder;

/**
 * A multiblock with tooltip {@link MultiblockTooltipBuilder}
 */
public abstract class MTETooltipMultiBlockBase extends MTEMultiBlockBase implements ISecondaryDescribable {

    private static final AtomicReferenceArray<MultiblockTooltipBuilder> tooltips = new AtomicReferenceArray<>(
        GregTechAPI.METATILEENTITIES.length);

    public MTETooltipMultiBlockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTETooltipMultiBlockBase(String aName) {
        super(aName);
    }

    protected abstract MultiblockTooltipBuilder createTooltip();

    protected final MultiblockTooltipBuilder getTooltip() {
        if (GTValues.debugTooltips) {
            return createTooltip();
        }
        int tId = getBaseMetaTileEntity().getMetaTileID();
        MultiblockTooltipBuilder tooltip = tooltips.get(tId);
        if (tooltip == null) {
            tooltip = createTooltip();
            tooltips.set(tId, tooltip);
        }
        return tooltip;
    }

    @Override
    public final String[] getDescription() {
        return getCurrentDescription();
    }

    @Override
    public final String[] getPrimaryDescription() {
        return getTooltip().getInformation();
    }

    @Override
    public final String[] getSecondaryDescription() {
        return getTooltip().getStructureInformation();
    }

    @Override
    public final boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }
}
