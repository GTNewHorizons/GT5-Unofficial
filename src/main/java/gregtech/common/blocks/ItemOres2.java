package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringUtils;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;

public class ItemOres2 extends ItemBlock {

    public ItemOres2(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.field_150939_a.getUnlocalizedName() + "." + BlockOres2.withNaturalFlag(stack.getItemDamage(), false);
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String aName = super.getItemStackDisplayName(stack);
        return Materials.getLocalizedNameForItem(aName, BlockOres2.getMaterialId(stack.getItemDamage()));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> desc, boolean advancedTooltips) {
        String formula = StatCollector.translateToLocal(getUnlocalizedName(stack) + ".tooltip");
        if (!StringUtils.isBlank(formula)) desc.add(formula);
    }
}
