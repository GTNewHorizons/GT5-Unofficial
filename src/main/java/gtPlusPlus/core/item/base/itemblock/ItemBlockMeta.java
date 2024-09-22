package gtPlusPlus.core.item.base.itemblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.interfaces.ITileTooltip;

public class ItemBlockMeta extends ItemBlockWithMetadata {

    private final Block mBlock;
    private HashMap<Integer, ArrayList<String>> aTooltips = new LinkedHashMap<>();

    public ItemBlockMeta(final Block aBlock) {
        super(aBlock, aBlock);
        this.mBlock = aBlock;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        if (aBlock instanceof ITileTooltip aTooltip) {
            // aTooltips.put(aTooltip.getTooltipID(), aTooltip.getTooltipMap());
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        Block aThis = Block.getBlockFromItem(stack.getItem());
        if (aThis != null) {
            if (!aTooltips.isEmpty()) {
                ArrayList<String> h = aTooltips.get(stack.getItemDamage());
                if (h != null && !h.isEmpty()) {
                    list.addAll(h);
                }
            }
        }
        super.addInformation(stack, aPlayer, list, bool);
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(final int p_77617_1_) {
        return this.mBlock.getIcon(2, p_77617_1_);
    }

    /**
     * Returns the metadata of the block which this Item (ItemBlock) can place
     */
    @Override
    public int getMetadata(final int p_77647_1_) {
        return p_77647_1_;
    }

    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return this.getUnlocalizedName() + "." + stack.getItemDamage();
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean getIsRepairable(ItemStack p_82789_1_, ItemStack p_82789_2_) {
        return false;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 0;
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        return 0;
    }
}
