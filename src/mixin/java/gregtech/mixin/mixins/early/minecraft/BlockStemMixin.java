package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.IBlockStemAccesor;

@Mixin(value = BlockStem.class)
public class BlockStemMixin implements IBlockStemAccesor {

    @Shadow
    @Final
    private Block field_149877_a;

    @Override
    public Block getCropBlock() {
        return this.field_149877_a;
    }
}
