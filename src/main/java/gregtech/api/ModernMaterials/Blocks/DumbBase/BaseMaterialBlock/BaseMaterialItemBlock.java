package gregtech.api.ModernMaterials.Blocks.DumbBase.BaseMaterialBlock;

import static gregtech.api.ModernMaterials.ModernMaterialUtilities.tooltipGenerator;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.ModernMaterials.Blocks.Registration.BlocksEnum;
import gregtech.api.ModernMaterials.ModernMaterial;

public class BaseMaterialItemBlock extends ItemBlock {

    public BaseMaterialItemBlock(@NotNull final Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH_ORES); // todo add new tabs.
    }

    @Override
    public int getMetadata(final int metadata) {
        return metadata;
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    // Tooltip information.
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(@NotNull final ItemStack itemStack, final EntityPlayer player, List<String> tooltipList,
        boolean aF3_H) {
        final ModernMaterial material = ModernMaterial.getMaterialIDToMaterialMap()
            .get(itemStack.getItemDamage());
        tooltipList.addAll(tooltipGenerator(itemStack.getItem(), material));
    }

    @Override
    public String getItemStackDisplayName(@NotNull final ItemStack itemStack) {

        ModernMaterial material = getMaterial(itemStack);

        // todo custom name overrides, for e.g. names that change over time.

        BaseMaterialBlock block = (BaseMaterialBlock) Block.getBlockFromItem(itemStack.getItem());
        BlocksEnum blockEnum = block.getBlockEnum();

        return blockEnum.getLocalisedName(material);
    }

    public BlocksEnum getPart() {
        return ((BaseMaterialBlock) field_150939_a).getBlockEnum();
    }

    public static ModernMaterial getMaterial(@NotNull final ItemStack itemStack) {
        return ModernMaterial.getMaterialFromID(itemStack.getItemDamage());
    }

}
