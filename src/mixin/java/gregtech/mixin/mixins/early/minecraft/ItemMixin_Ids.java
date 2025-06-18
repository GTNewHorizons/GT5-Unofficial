package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.mixin.ObjectWithId;

@Mixin(Item.class)
public class ItemMixin_Ids implements ObjectWithId {

    @Unique
    public int gt5u$itemId;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(CallbackInfo ci) {
        gt5u$itemId = NEXT_ID.getAndAdd(1);
    }

    @Override
    public int getId() {
        return gt5u$itemId;
    }
}
