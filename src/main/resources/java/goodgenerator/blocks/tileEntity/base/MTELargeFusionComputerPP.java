package goodgenerator.blocks.tileEntity.base;

import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.util.AdvancedFusionOverclockDescriber;

public abstract class MTELargeFusionComputerPP extends MTELargeFusionComputer {

    public MTELargeFusionComputerPP(String name) {
        super(name);
    }

    public MTELargeFusionComputerPP(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    protected OverclockDescriber createOverclockDescriber() {
        return new AdvancedFusionOverclockDescriber((byte) tier(), capableStartupCanonical());
    }

}
