package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbItemBlock;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbTileEntity;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.awt.*;


public class FrameBoxItemBlock extends DumbItemBlock {
    public FrameBoxItemBlock(Block block) {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        final ModernMaterial associatedMaterial = ModernMaterialUtilities.materialIDToMaterial.get(itemStack.getItemDamage());

        final String s = getBlockEnum().getLocalisedName(associatedMaterial);

        return s;
    }

    @Override
    public BlocksEnum getBlockEnum() {
        return BlocksEnum.FrameBox;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
        final ModernMaterial associatedMaterial = ModernMaterialUtilities.materialIDToMaterial.get(itemStack.getItemDamage());

        return associatedMaterial.getColor().getRGB();
    }

}
