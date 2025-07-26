package gregtech.mixin.mixins.late.forestry;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import forestry.api.arboriculture.EnumWoodType;
import forestry.arboriculture.genetics.TreeDefinition;
import gregtech.mixin.interfaces.accessors.TreeDefinitionAccessor;

@Mixin(value = TreeDefinition.class, remap = false)
public class TreeDefinitionMixin implements TreeDefinitionAccessor {

    @Final
    @Shadow
    private EnumWoodType woodType;
    @Final
    @Shadow
    private ItemStack vanillaWood;

    @Override
    public EnumWoodType gt5u$getWoodType() {
        return woodType;
    }

    @Override
    public ItemStack gt5u$getVanillaWood() {
        return vanillaWood;
    }
}
