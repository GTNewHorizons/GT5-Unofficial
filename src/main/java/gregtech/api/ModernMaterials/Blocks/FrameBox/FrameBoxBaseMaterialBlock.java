package gregtech.api.ModernMaterials.Blocks.FrameBox;

import java.awt.*;
import java.util.List;

import gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock.BaseMaterialBlock;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;

public class FrameBoxBaseMaterialBlock extends BaseMaterialBlock {

    public FrameBoxBaseMaterialBlock(int blockIDOffset, List<Integer> validIDs) {
        super(blockIDOffset, validIDs);
    }

    @Override
    public BlocksEnum getBlockEnum() {
        return BlocksEnum.FrameBox;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }

    // Make block see through.
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/frameGt");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z) {
        final int materialID = getMaterialID(worldIn.getBlockMetadata(x, y, z));
        final ModernMaterial material = ModernMaterialUtilities.materialIDToMaterial.get(materialID);

        if (material == null) return new Color(100, 100, 0, 255).getRGB();
        return material.getColor()
            .getRGB();
    }
}
