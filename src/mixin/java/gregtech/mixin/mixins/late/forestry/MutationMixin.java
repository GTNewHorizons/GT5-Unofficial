package gregtech.mixin.mixins.late.forestry;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import forestry.api.genetics.IMutationCondition;
import forestry.core.genetics.mutations.Mutation;
import gregtech.mixin.interfaces.accessors.MutationAccessor;

@Mixin(Mutation.class)
public class MutationMixin implements MutationAccessor {

    @Final
    @Shadow(remap = false)
    private List<IMutationCondition> mutationConditions;

    @Override
    public List<IMutationCondition> gt5u$getMutationConditions() {
        return mutationConditions;
    }
}
