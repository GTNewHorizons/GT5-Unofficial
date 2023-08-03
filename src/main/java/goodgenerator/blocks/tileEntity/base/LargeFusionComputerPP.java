package goodgenerator.blocks.tileEntity.base;

import gregtech.api.logic.ProcessingLogic;

public abstract class LargeFusionComputerPP extends LargeFusionComputer {

    public LargeFusionComputerPP(String name) {
        super(name);
    }

    public LargeFusionComputerPP(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return super.createProcessingLogic().setOverclock(2, 2);
    }

    @Override
    protected long getSingleHatchPower() {
        // Multiply by 8 so that we can have the original input power per hatch
        return super.getSingleHatchPower() * 8;
    }

}
