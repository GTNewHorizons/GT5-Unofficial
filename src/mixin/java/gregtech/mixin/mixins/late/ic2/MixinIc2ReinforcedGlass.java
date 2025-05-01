package gregtech.mixin.mixins.late.ic2;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import ic2.core.block.BlockMetaData;
import ic2.core.block.BlockTexGlass;
import ic2.core.init.InternalName;

@Mixin(value = BlockTexGlass.class, remap = false)
public class MixinIc2ReinforcedGlass extends BlockMetaData {

    public MixinIc2ReinforcedGlass(InternalName internalName1) {
        super(internalName1, Material.glass);
        this.setHardness(5.0F);
        this.setResistance(180.0F);
        this.setStepSound(Block.soundTypeGlass);
    }

    @Override
    public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
        return true;
    }

}
