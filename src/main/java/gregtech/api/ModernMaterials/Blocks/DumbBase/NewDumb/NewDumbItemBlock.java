package gregtech.api.ModernMaterials.Blocks.DumbBase.NewDumb;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.Blocks.BlocksEnum;
import gregtech.api.ModernMaterials.Blocks.DumbBase.Base.BaseItemBlock;
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
    public boolean getHasSubtypes() {
        return true;
    }

    // Tooltip information.
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull ItemStack itemStack, EntityPlayer player, List<String> tooltipList, boolean aF3_H)  {
        tooltipList.add("TEST");
//
//        final ModernMaterial material = ModernMaterialUtilities.materialIDToMaterial.get(itemStack.getItemDamage());
//
//        for (String line : tooltipGenerator(itemStack.getItem(), material)) {
//            tooltipList.add(line);
//        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        final ModernMaterial associatedMaterial = ModernMaterialUtilities.materialIDToMaterial.get(itemStack.getItemDamage());

        NewDumb block = (NewDumb) Block.getBlockFromItem(itemStack.getItem());
        BlocksEnum blockEnum = block.getBlockEnum();

        return blockEnum.getLocalisedName(associatedMaterial);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack itemStack, int renderPass) {
        final ModernMaterial associatedMaterial = ModernMaterialUtilities.materialIDToMaterial.get(itemStack.getItemDamage());

        return associatedMaterial.getColor().getRGB();
    }

}
