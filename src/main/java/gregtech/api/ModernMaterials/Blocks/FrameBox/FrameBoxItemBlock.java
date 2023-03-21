package gregtech.api.ModernMaterials.Blocks.FrameBox;

import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.DumbItemBlock;
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
}
