package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;

public class ItemAirFilter extends Item {

    public IIcon[] icons = new IIcon[2];

    public ItemAirFilter() {
        super();
        this.setHasSubtypes(true);
        String unlocalizedName = "itemAirFilter";
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setMaxStackSize(1);
        GameRegistry.registerItem(this, unlocalizedName);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.icons[0] = reg.registerIcon(GTPlusPlus.ID + ":itemAirFilter1");
        this.icons[1] = reg.registerIcon(GTPlusPlus.ID + ":itemAirFilter2");
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[meta];
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 2; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + stack.getItemDamage();
    }

    @Override
    public String getItemStackDisplayName(final ItemStack tItem) {

        if (tItem == null) {
            return StatCollector.translateToLocal("item.GTPP.air_filter.name");
        }

        if (tItem.getItemDamage() == 0) {
            return StatCollector.translateToLocal("item.GTPP.air_filter.name.t1");
        } else if (tItem.getItemDamage() == 1) {
            return StatCollector.translateToLocal("item.GTPP.air_filter.name.t2");
            // damage value 1 is t2 while 0 is t1
        } else {
            return StatCollector.translateToLocal("item.GTPP.air_filter.name");
        }
    }

    private static boolean createNBT(ItemStack rStack) {
        final NBTTagCompound tagMain = new NBTTagCompound();
        final NBTTagCompound tagNBT = new NBTTagCompound();
        tagNBT.setLong("Damage", 0);
        tagMain.setTag("AirFilter", tagNBT);
        rStack.setTagCompound(tagMain);
        return true;
    }

    public static long getFilterMaxDamage(final ItemStack aStack) {
        if (aStack.getItemDamage() == 0) return 50;
        else if (aStack.getItemDamage() == 1) return 2500;
        else return 0;// should not happen
    }

    public static long getFilterDamage(final ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("AirFilter");
            if (aNBT != null) {
                return aNBT.getLong("Damage");
            }
        } else {
            createNBT(aStack);
        }
        return 0L;
    }

    public static boolean setFilterDamage(final ItemStack aStack, final long aDamage) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("AirFilter");
            if (aNBT != null) {
                aNBT.setLong("Damage", aDamage);
                return true;
            }
        }
        return false;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            createNBT(stack);
        }
        double currentDamage = getFilterDamage(stack);
        double meta = getFilterMaxDamage(stack);
        return currentDamage / meta;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        list.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("GTPP.tooltip.air_filter.0"));
        long maxDamage = getFilterMaxDamage(stack);
        list.add(
            EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                "GTPP.tooltip.air_filter.1",
                (maxDamage - getFilterDamage(stack)),
                maxDamage));
        super.addInformation(stack, player, list, bool);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
}
