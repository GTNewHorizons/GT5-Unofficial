package gregtech.api.ModernMaterials.Blocks.FrameBox;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Simple.DumbItemBlock;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;


public class FrameBoxItemBlock extends DumbItemBlock {
    public FrameBoxItemBlock(Block block) {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        final ModernMaterial associatedMaterial = ModernMaterialUtilities.materialIDToMaterial.get(itemStack.getItemDamage());

        return getBlockEnum().getLocalisedName(associatedMaterial);
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
