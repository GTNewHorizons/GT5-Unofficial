package gregtech.api.modernmaterials.blocks.blocktypes.ore.normalore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.blocks.blocktypes.ore.BaseOreBlock;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import java.util.HashSet;

public class NormalOreBlock extends BaseOreBlock {

    protected static IIcon metalNormalOreTexture;
    protected static IIcon gemNormalOreTexture;
    protected static IIcon dustNormalOreTexture;

    public NormalOreBlock(BlocksEnum blockEnum, HashSet<ModernMaterial> validIDs) {
        super(blockEnum, validIDs);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        metalNormalOreTexture = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/metalOre");
        gemNormalOreTexture = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/gemOre");
        dustNormalOreTexture = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/dustOre");
    }

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
                return metalNormalOreTexture;
            }
            case Dust -> {
                return dustNormalOreTexture;
            }
            case Gem -> {
                return gemNormalOreTexture;
            }
        }

        return null;
    }

}
