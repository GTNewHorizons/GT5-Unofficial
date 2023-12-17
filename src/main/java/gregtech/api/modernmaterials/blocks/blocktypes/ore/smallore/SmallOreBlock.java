package gregtech.api.modernmaterials.blocks.blocktypes.ore.smallore;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modernmaterials.ModernMaterial;
import gregtech.api.modernmaterials.blocks.blocktypes.ore.BaseOreBlock;
import gregtech.api.modernmaterials.blocks.registration.BlocksEnum;
import net.minecraft.client.renderer.texture.IIconRegister;

import java.util.HashSet;

public class SmallOreBlock extends BaseOreBlock {

    public SmallOreBlock(BlocksEnum blockEnum, HashSet<ModernMaterial> validIDs) {
        super(blockEnum, validIDs);
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        blockIcon = reg.registerIcon("gregtech:ModernMaterialsIcons/Blocks/smallOre");
    }

}
