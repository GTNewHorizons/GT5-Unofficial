package gregtech.api.structure;

import java.util.function.Function;

import com.gtnewhorizon.structurelib.structure.IStructureElement;

import gregtech.api.casing.ICasing;
import gregtech.api.casing.ICasingGroup;
import gregtech.api.interfaces.IHatchElement;

public class CasingInfo<MTE> implements ICasing.CasingElementContext<MTE> {

    /** The number of casings that can be replaced with hatches. -1 for all. */
    public int maxHatches;
    public int dot;
    public ICasing casing;
    public IHatchElement<? super MTE>[] hatches;
    public IStructureChannels channel;
    public ICasingGroup casingGroup;

    public Function<ICasingGroup, IStructureElement<MTE>> elementOverride;
    public Function<IStructureElement<MTE>, IStructureElement<MTE>> elementWrapper;
    public Function<MTE, IStructureInstance<MTE>> instanceExtractor;

    @Override
    public ICasingGroup getGroup() {
        return casingGroup;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IStructureInstance<MTE> getInstance(MTE mte) {
        if (instanceExtractor != null) return instanceExtractor.apply(mte);

        return (IStructureInstance<MTE>) ((IStructureProvider<?>) mte).getStructureInstance();
    }
}
