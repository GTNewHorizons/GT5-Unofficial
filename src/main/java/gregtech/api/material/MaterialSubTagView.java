package gregtech.api.material;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.ISubTagContainer;

/// Lets a prefix's [gregtech.api.enums.OrePrefixes#mCondition] test a [Material]. Each [SubTag] maps to the
/// same-named [GTMaterialFlag] (see [MaterialUtils#hasSubTag(Material, SubTag)]).
///
/// The mutating [ISubTagContainer] methods do nothing.
public final class MaterialSubTagView implements ISubTagContainer {

    public final Material material;

    public MaterialSubTagView(@Nullable Material material) {
        this.material = material;
    }

    @Override
    public boolean contains(SubTag aTag) {
        return MaterialUtils.hasSubTag(material, aTag);
    }

    @Override
    public ISubTagContainer add(SubTag... aTags) {
        return this;
    }

    @Override
    public boolean remove(SubTag aTag) {
        return false;
    }
}
