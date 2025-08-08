package gregtech.nei.dumper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import codechicken.nei.config.DataDumper;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;

public abstract class MultiBlockFeatureSupportDumper extends DataDumper {

    private final Function<IControllerWithOptionalFeatures, Boolean> isFeatureSupported;

    public MultiBlockFeatureSupportDumper(String name,
        Function<IControllerWithOptionalFeatures, Boolean> isFeatureSupported) {
        super("tools.dump.gt5u." + name);
        this.isFeatureSupported = isFeatureSupported;
    }

    @Override
    public String[] header() {
        return new String[] { "className" };
    }

    @Override
    public Iterable<String[]> dump(int mode) {
        List<String[]> list = new ArrayList<>();
        for (int i = 1; i < GregTechAPI.METATILEENTITIES.length; i++) {
            IMetaTileEntity mte = GregTechAPI.METATILEENTITIES[i];
            if (!(mte instanceof IControllerWithOptionalFeatures controller)) continue;
            if (!isFeatureSupported.apply(controller)) {
                list.add(
                    new String[] { controller.getClass()
                        .getName() });
            }
        }
        return list;
    }

    @Override
    public int modeCount() {
        return 1;
    }
}
