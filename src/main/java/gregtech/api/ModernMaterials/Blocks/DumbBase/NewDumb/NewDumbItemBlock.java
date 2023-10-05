package gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.ModernMaterialUtilities;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.tooltipGenerator;

public class NewDumbItemBlock extends ItemBlock {
    public NewDumbItemBlock(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH_ORES); // todo add new tabs.
    }

    @Override
    public int getMetadata(int metadata)
    {
        return metadata;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    // Tooltip information.
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack itemStack, EntityPlayer player, List<String> tooltipList, boolean aF3_H)  {
        final ModernMaterial material = ModernMaterialUtilities.materialIDToMaterial.get(getMaterialID(itemStack));
        tooltipList.addAll(tooltipGenerator(itemStack.getItem(), material));
    }

    @Override
    public String getItemStackDisplayName(@NotNull ItemStack itemStack) {

        final ModernMaterial associatedMaterial = ModernMaterialUtilities.materialIDToMaterial.get(getMaterialID(itemStack));

        NewDumb block = (NewDumb) Block.getBlockFromItem(itemStack.getItem());
        BlocksEnum blockEnum = block.getBlockEnum();

        return blockEnum.getLocalisedName(associatedMaterial);
    }

    public static int getMaterialID(@NotNull final ItemStack itemStack) {
        NewDumb newDumb = (NewDumb) Block.getBlockFromItem(itemStack.getItem());
        return newDumb.getMaterialID(itemStack.getItemDamage());
    }

}
