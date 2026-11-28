package gregtech.nei.dumper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import codechicken.nei.config.DataDumper;
import gregtech.api.GregTechAPI;
import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IECMultiblock;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IControllerWithOptionalFeatures;

public abstract class MultiBlockFeatureSupportDumper extends DataDumper {

    private final Function<IControllerWithOptionalFeatures, Boolean> isFeatureSupported;
    private final Predicate<IECMultiblock> isECFeatureSupported;

    public MultiBlockFeatureSupportDumper(String name,
        Function<IControllerWithOptionalFeatures, Boolean> isFeatureSupported, ComponentKey<?> featrueKey) {
        this(name, isFeatureSupported, (multiblock) -> multiblock.hasComponent(featrueKey));
    }

    public MultiBlockFeatureSupportDumper(String name,
        Function<IControllerWithOptionalFeatures, Boolean> isFeatureSupported,
        Predicate<IECMultiblock> isECFeatureSupported) {
        super("tools.dump.gt5u." + name);
        this.isFeatureSupported = isFeatureSupported;
        this.isECFeatureSupported = isECFeatureSupported;
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
            boolean supported = false;
            if (mte instanceof IControllerWithOptionalFeatures controller) {
                supported = isFeatureSupported.apply(controller);
            } else if (mte instanceof IECMultiblock ecMulti) {
                supported = isECFeatureSupported.test(ecMulti);
            }
            if (!supported) {
                list.add(
                    new String[] { mte.getClass()
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
