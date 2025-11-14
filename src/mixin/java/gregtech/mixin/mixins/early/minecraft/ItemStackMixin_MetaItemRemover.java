package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gregtech.api.registries.RemovedMetaRegistry;

@Mixin(ItemStack.class)
public class ItemStackMixin_MetaItemRemover {

    @Inject(method = "loadItemStackFromNBT", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings("unused")
    private static void gt5u$RemoveInvalidMetaItems(NBTTagCompound p_77949_0_, CallbackInfoReturnable<ItemStack> cir) {
        var id = p_77949_0_.getShort("id");
        var meta = p_77949_0_.getInteger("Damage");

        if (RemovedMetaRegistry.contains(id, meta)) {
            cir.setReturnValue(null);
        }
    }
}
