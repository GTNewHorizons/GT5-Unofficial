package gregtech.mixin.mixins.early.minecraft.accessors;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomFishable;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.accessors.WeightedRandomFishableAccessor;

@Mixin(WeightedRandomFishable.class)
public class WeightedRandomFishableMixin implements WeightedRandomFishableAccessor {

    @Final
    @Shadow
    private ItemStack field_150711_b;

    @Override
    public ItemStack gt5u$getLoot() {
        return field_150711_b;
    }
}
