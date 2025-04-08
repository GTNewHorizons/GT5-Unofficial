package gregtech.api.structure;

import java.util.function.Function;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.structure.IStructureElement;

import gregtech.api.casing.ICasing;
import gregtech.api.casing.ICasingGroup;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;

public class CasingInfo<MTE extends MTEMultiBlockBase & IAlignment & IStructureProvider<MTE>> {

    /** The number of casings that can be replaced with hatches. -1 for all. */
    public int maxHatches;
    public int dot;
    public ICasing casing;
    public IHatchElement<? super MTE>[] hatches;
    public String channel;
    public ICasingGroup casingGroup;

    public Function<ICasingGroup, IStructureElement<MTE>> elementOverride;
    public Function<IStructureElement<MTE>, IStructureElement<MTE>> elementWrapper;
}
