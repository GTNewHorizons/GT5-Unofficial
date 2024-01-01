package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.ItemList;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;

public class ItemLavaFilter extends CoreItem {

    public IIcon[] mIcon = new IIcon[1];

    public ItemLavaFilter() {
        super(
                "itemLavaFilter",
                AddToCreativeTab.tabMachines,
                1,
                99,
                new String[] { "Lava Filter" },
                EnumRarity.common,
                EnumChatFormatting.BLACK,
                false,
                null);
        setGregtechItemList();
    }

    private boolean setGregtechItemList() {
        ItemList.Component_LavaFilter.set(this);
        return ItemList.Component_LavaFilter.get(1) != null ? true : false;
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.mIcon[0] = reg.registerIcon(GTPlusPlus.ID + ":" + "itemLavaFilter");
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.mIcon[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("item.itemLavaFilter.tooltip"));

        int maxDurability = stack.getMaxDamage() + 1;
        int durability = maxDurability - stack.getItemDamage();

        EnumChatFormatting formatting = EnumChatFormatting.GRAY;
        if (durability > maxDurability * 0.8) {
            formatting = EnumChatFormatting.GRAY;
        } else if (durability > maxDurability * 0.6) {
            formatting = EnumChatFormatting.GREEN;
        } else if (durability > maxDurability * 0.4) {
            formatting = EnumChatFormatting.YELLOW;
        } else if (durability > maxDurability * 0.2) {
            formatting = EnumChatFormatting.GOLD;
        } else if (durability > 0) {
            formatting = EnumChatFormatting.RED;
        }
        list.add("Uses remaining: " + formatting + durability + EnumChatFormatting.GRAY + " / " + maxDurability);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }
}
