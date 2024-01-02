package gregtech.api.modernmaterials.blocks.blocktypes.blockof;

import java.awt.Color;
import java.util.HashSet;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialBlock;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;

public class BlockOfBaseMaterialBlock extends BaseMaterialBlock {

    public BlockOfBaseMaterialBlock(BlocksEnum blockEnum, HashSet<ModernMaterial> validMaterials) {
        super(blockEnum, validMaterials);
    }

    @Override
    public BlocksEnum getBlockEnum() {
        return BlocksEnum.SolidBlock;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return this.blockIcon;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.blockIcon = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/block5");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z) {
        final int materialID = worldIn.getBlockMetadata(x, y, z);
        final ModernMaterial material = ModernMaterial.getMaterialIDToMaterialMap()
            .get(materialID);

        if (material == null) return new Color(0, 0, 0, 255).getRGB();
        return material.getColor()
            .getRGB();
    }
}
