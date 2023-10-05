package gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.awt.*;
import java.util.List;

import static gregtech.api.enums.Mods.GregTech;

public class FrameBoxNewDumb extends NewDumb {
    public FrameBoxNewDumb(int blockIDOffset, List<Integer> validIDs) {
        super(blockIDOffset, validIDs);
    }

    @Override
    public BlocksEnum getBlockEnum() {
        return BlocksEnum.FrameBox;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta)
    {
        return this.blockIcon;
    }

    // Make block see through.
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/frameGt");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z)
    {
        final int materialID = getMaterialID(worldIn.getBlockMetadata(x, y, z));
        final ModernMaterial material = ModernMaterialUtilities.materialIDToMaterial.get(materialID);

        if (material == null) return new Color(100, 100, 0, 255).getRGB();
        return material.getColor().getRGB();
    }
}
