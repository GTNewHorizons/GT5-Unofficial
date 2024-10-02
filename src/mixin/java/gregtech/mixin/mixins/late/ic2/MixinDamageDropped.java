package gregtech.mixin.mixins.late.ic2;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { ic2.core.block.machine.BlockMachine.class, ic2.core.block.generator.block.BlockGenerator.class })
public class MixinDamageDropped extends Block {

    protected MixinDamageDropped(Material materialIn) {
        super(materialIn);
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

}
