package gregtech.mixin.mixins.late.betterfoliage;

import net.minecraft.block.Block;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import gregtech.common.pollution.ColorOverrideType;
import gregtech.common.pollution.Pollution;
import mods.octarinecore.client.render.BlockContext;

@SuppressWarnings("UnusedMixin")
@Mixin(BlockContext.class)
public abstract class MixinBlockContextPollution {

    @Shadow(remap = false)
    private int x;

    @Shadow(remap = false)
    private int z;

    @Final
    @Shadow(remap = false)
    public abstract Block getBlock();

    @WrapMethod(method = "getBlockColor", remap = false)
    public int gt5u$getBlockColor(Operation<Integer> original) {
        ColorOverrideType type = Pollution.standardBlocks.matchesID(getBlock());
        return type != null ? type.getColor(original.call(), x, z) : original.call();
    }
}
