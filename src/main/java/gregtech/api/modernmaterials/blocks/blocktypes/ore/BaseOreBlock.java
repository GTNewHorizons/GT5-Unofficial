package gregtech.api.modernmaterials.blocks.blocktypes.ore;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modernmaterials.blocks.dumbbase.basematerialblock.BaseMaterialBlock;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import gregtech.api.modernmaterials.ModernMaterial;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.awt.*;
import java.util.HashSet;

public abstract class BaseOreBlock extends BaseMaterialBlock {

    protected IIcon metalOreTexture;
    protected IIcon gemOreTexture;
    protected IIcon dustOreTexture;

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        // Todo this particle is used to spawn the breaking effect. We should probably change it to something else, like
        // the underlying stone texture, however this has issues because changing it here will cause the colour
        // multiplier function to apply to that later. Non-critical.

        ModernMaterial material = ModernMaterial.getMaterialFromID(meta);
        if (material == null) return Blocks.stone.getIcon(0, 0);

        switch (material.getTextureType()) {
            // temp.
            case Metal_Shiny, Metal_Dull, Stone, Custom -> {
                return metalOreTexture;
            }
            case Dust -> {
                return dustOreTexture;
            }
            case Gem -> {
                return gemOreTexture;
            }
        }

        return null;
    }

    public BaseOreBlock(BlocksEnum blockEnum, HashSet<ModernMaterial> validIDs) {
        super(blockEnum, validIDs);
    }

    // Make block see through.
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int colorMultiplier(IBlockAccess worldIn, int x, int y, int z) {
        final int materialID = worldIn.getBlockMetadata(x, y, z);
        final ModernMaterial material = ModernMaterial.getMaterialIDToMaterialMap()
            .get(materialID);

        if (material == null) return new Color(100, 100, 0, 255).getRGB();
        return material.getColor()
            .getRGB();
    }


}
