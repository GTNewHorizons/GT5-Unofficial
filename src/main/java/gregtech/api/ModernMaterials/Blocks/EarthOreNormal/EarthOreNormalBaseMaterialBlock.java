package gregtech.api.ModernMaterials.Blocks.EarthOreNormal;

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

public class EarthOreNormalBaseMaterialBlock extends BaseMaterialBlock {

    public EarthOreNormalBaseMaterialBlock(int blockIDOffset, List<Integer> validIDs) {
        super(blockIDOffset, validIDs);
    }

    @Override
    public BlocksEnum getBlockEnum() {
        return BlocksEnum.EarthOreNormal;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        // Todo this particle is used to spawn the breaking effect. We should probably change it to something else, like the underlying stone texture, however this has issues because changing it here will cause the colour multiplier function to apply to that later. Non-critical.
        return this.blockIcon;
    }

    // Make block see through.
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/ore");
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
