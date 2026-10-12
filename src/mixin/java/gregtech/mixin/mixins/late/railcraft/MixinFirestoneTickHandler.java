package gregtech.mixin.mixins.late.railcraft;

import net.minecraft.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StackMaterialInfo;

import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.Shapes;
import mods.railcraft.common.items.firestone.FirestoneTickHandler;

/// Sets fires around players carrying Firestone dusts, crushed ores, gems, raw ores, ores or storage blocks, as
/// Railcraft does for its own firestone.
@Mixin(value = FirestoneTickHandler.class, remap = false)
public class MixinFirestoneTickHandler {

    @Inject(method = "shouldBurn", at = @At("HEAD"), cancellable = true)
    private void gt5u$burnFirestoneParts(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        StackMaterialInfo info = stack == null ? null : MaterialLibAPI.lookupStack(stack);
        if (info != null && info.material() == Materials.Firestone && gt5u$burns(info.shape())) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    private static boolean gt5u$burns(Shape shape) {
        return shape == Shapes.dustTiny || shape == Shapes.dustSmall
            || shape == Shapes.dust
            || shape == Shapes.dustImpure
            || shape == Shapes.dustPure
            || shape == Shapes.crushed
            || shape == Shapes.crushedPurified
            || shape == Shapes.crushedCentrifuged
            || shape == Shapes.gem
            || shape == Shapes.rawOre
            || shape == OreShapes.ore
            || shape == OreShapes.oreSmall
            || shape == BlockShapes.block;
    }
}
