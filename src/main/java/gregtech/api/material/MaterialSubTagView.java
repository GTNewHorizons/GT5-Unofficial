package gregtech.api.material;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.ISubTagContainer;

/// Transitional MaterialLib view over a [Material], letting a legacy [ICondition] of [ISubTagContainer] -- the
/// [gregtech.api.enums.OrePrefixes#mCondition] a prefix carries -- evaluate directly against MaterialLib state
/// without rewriting the conditions themselves. Every [SubTag] a prefix condition tests carries a same-named
/// [GTMaterialFlag], so membership routes through the material's MaterialLib FLAGS property via
/// [MU#hasSubTag].
///
/// Condition evaluation only ever calls [#contains]: [ICondition.And], [ICondition.Not], [ICondition.Or],
/// [ICondition.Nor], [ICondition.Nand], [ICondition.Xor], and [ICondition.Equal] each recurse into their
/// operands, whose leaves are [SubTag]s, and `SubTag#isTrue` calls only `contains`. The mutating
/// [ISubTagContainer] methods therefore never fire during evaluation and are inert here.
public final class MaterialSubTagView implements ISubTagContainer {

    public final Material material;

    public MaterialSubTagView(@Nullable Material material) {
        this.material = material;
    }

    @Override
    public boolean contains(SubTag aTag) {
        return MU.hasSubTag(material, aTag);
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
