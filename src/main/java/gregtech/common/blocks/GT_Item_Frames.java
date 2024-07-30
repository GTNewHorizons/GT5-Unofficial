package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.apache.commons.lang3.StringUtils;

import gregtech.api.enums.Materials;

public class GT_Item_Frames extends ItemBlock {

    public GT_Item_Frames(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    private Block block() {
        return this.field_150939_a;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.block()
            .getUnlocalizedName() + "."
            + getDamage(aStack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        String aName = super.getItemStackDisplayName(aStack);
        if (this.block() instanceof GT_Block_FrameBox) {
            aName = Materials.getLocalizedNameForItem(aName, aStack.getItemDamage() % 1000);
        }
        return aName;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        String formula = StatCollector.translateToLocal(
            this.block()
                .getUnlocalizedName() + '.'
                + getDamage(aStack)
                + ".tooltip");
        if (!StringUtils.isBlank(formula)) aList.add(formula);
    }
}
