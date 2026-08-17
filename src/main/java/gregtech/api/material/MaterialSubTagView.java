package gregtech.api.material;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.ISubTagContainer;

/// MaterialLib view over a [Material], letting a legacy [ICondition] of [ISubTagContainer] -- the
/// [gregtech.api.enums.OrePrefixes#mCondition] a prefix carries -- evaluate against MaterialLib state. Every
/// [SubTag] a prefix condition tests carries a same-named [GTMaterialFlag], so membership routes through the
/// material's MaterialLib FLAGS property via [MaterialUtils#hasSubTag].
///
/// Condition evaluation only ever calls [#contains]; the mutating [ISubTagContainer] methods are inert.
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
