package gtPlusPlus.core.item.general.books;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Mods;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.NBTUtils;

public class ItemBaseBook extends ItemWritableBook {

    public ItemBaseBook() {
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setMaxStackSize(1);
        this.setTextureName(Mods.GTPlusPlus.ID + ":" + "itemBook");
        this.setUnlocalizedName("itembookgt");
        GameRegistry.registerItem(this, "bookGT");
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for (int meta : BookHandler.mBookMap.keySet()) {
            ItemStack bookstack = new ItemStack(item, 1, meta);

            /*
             * bookstack = Utils.getWrittenBook( bookstack, i, mBookMap.get(i).mMapping, mBookMap.get(i).mTitle,
             * mBookMap.get(i).mAuthor, mBookMap.get(i).mPages);
             */

            NBTUtils.createIntegerTagCompound(bookstack, "stats", "mMeta", meta);
            list.add(bookstack);
        }
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {
        if (NBTUtils.hasKey(tItem, "title")) {
            return NBTUtils.getString(tItem, "title");
        }
        BookHandler.BookTemplate book = BookHandler.mBookMap.get(tItem.getItemDamage());
        if (book != null) {
            return EnumChatFormatting.ITALIC + StatCollector.translateToLocal("Book." + book.mTitle() + ".Name");
        }
        return "GT++ Storybook";
    }

    @Override
    public void addInformation(ItemStack tItem, EntityPlayer player, List<String> list, boolean bool) {
        BookHandler.BookTemplate bookTemplate = BookHandler.mBookMap.get(tItem.getItemDamage());
        if (bookTemplate == null) return;
        if (NBTUtils.hasKey(tItem, "author")) {
            list.add(
                EnumChatFormatting.GRAY + StatCollector
                    .translateToLocalFormatted("gtpp.tooltip.book.author", NBTUtils.getString(tItem, "author")));
        } else if (bookTemplate.mAuthor() != null) {
            list.add(
                EnumChatFormatting.WHITE
                    + StatCollector.translateToLocalFormatted("gtpp.tooltip.book.author", bookTemplate.mAuthor()));
        }
        if (NBTUtils.hasKey(tItem, "title")) {
            list.add(
                EnumChatFormatting.GRAY + StatCollector
                    .translateToLocalFormatted("gtpp.tooltip.book.pages.s", NBTUtils.getString(tItem, "pages")));
        } else if (bookTemplate.mPages() != null) {
            list.add(
                EnumChatFormatting.WHITE + StatCollector
                    .translateToLocalFormatted("gtpp.tooltip.book.pages.d", bookTemplate.mPages().length));
        }
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
        if (player.worldObj.isRemote) {
            BookHandler.BookTemplate bookTemplate = BookHandler.mBookMap.get(item.getItemDamage());
            if (bookTemplate == null) return item;
            ItemStack bookstack = Utils.getWrittenBook(
                null,
                bookTemplate.mMeta(),
                bookTemplate.mMapping(),
                bookTemplate.mTitle(),
                bookTemplate.mAuthor(),
                bookTemplate.mPages());
            if (bookstack != null) {
                Minecraft.getMinecraft()
                    .displayGuiScreen(new GuiScreenBook(player, bookstack, false));
            }
        }
        return item;
    }
}
