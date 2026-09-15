package gtPlusPlus.xmod.gregtech.common.blocks;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public abstract class GregtechMetaItemCasingsAbstract extends ItemBlock {

    public GregtechMetaItemCasingsAbstract(final Block par1) {
        super(par1);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(final int aMeta) {
        return aMeta;
    }

    @Override
    public String getUnlocalizedName(final ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + this.getDamage(aStack);
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List<String> tooltip,
        final boolean aF3_H) {
        super.addInformation(stack, player, tooltip, aF3_H);
        tooltip.add(translateToLocal("gt.casing.no-mob-spawning"));
        tooltip.add(translateToLocal("gt.casing.not-tile-entity"));
    }
}
