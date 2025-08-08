package gregtech.mixin.mixins.late.ic2;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(
    value = { ic2.core.block.machine.BlockMachine2.class, ic2.core.block.machine.BlockMachine3.class,
        ic2.core.block.wiring.BlockElectric.class })
public class MixinItemDropped extends Block {

    protected MixinItemDropped(Material materialIn) {
        super(materialIn);
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return Item.getItemFromBlock((Block) (Object) this);
    }

}
