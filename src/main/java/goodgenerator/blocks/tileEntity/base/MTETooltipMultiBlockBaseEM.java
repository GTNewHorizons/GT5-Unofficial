package goodgenerator.blocks.tileEntity.base;

import gregtech.api.interfaces.ISecondaryDescribable;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public abstract class MTETooltipMultiBlockBaseEM extends TTMultiblockBase implements ISecondaryDescribable {

    protected MTETooltipMultiBlockBaseEM(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTETooltipMultiBlockBaseEM(String aName) {
        super(aName);
    }
}
