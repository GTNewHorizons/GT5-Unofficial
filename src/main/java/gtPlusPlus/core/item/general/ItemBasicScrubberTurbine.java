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

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;

public class ItemBasicScrubberTurbine extends Item {

    public IIcon[] icons = new IIcon[1];

    public ItemBasicScrubberTurbine() {
        super();
        this.setHasSubtypes(true);
        String unlocalizedName = "itemBasicTurbine";
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setMaxStackSize(1);
        GameRegistry.registerItem(this, unlocalizedName);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        this.icons[0] = reg.registerIcon(GTPlusPlus.ID + ":" + "itemBasicTurbine");
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.icons[0];
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < 3; i++) {
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
            return "Basic Turbine";
        }
        return super.getItemStackDisplayName(tItem);
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, int HEX_OxFFFFFF) {
        int meta = stack.getItemDamage();
        if (meta == 0) {
            HEX_OxFFFFFF = Utils.rgbtoHexValue(200, 200, 200);
        }
        if (meta == 1) {
            HEX_OxFFFFFF = Utils.rgbtoHexValue(255, 128, 0);
        }
        if (meta == 2) {
            HEX_OxFFFFFF = Utils.rgbtoHexValue(128, 128, 128);
        }
        return HEX_OxFFFFFF;
    }

    private static boolean createNBT(ItemStack rStack) {
        final NBTTagCompound tagMain = new NBTTagCompound();
        final NBTTagCompound tagNBT = new NBTTagCompound();
        tagNBT.setLong("Damage", 0);
        tagMain.setTag("BasicTurbine", tagNBT);
        rStack.setTagCompound(tagMain);
        return true;
    }

    public static final long getFilterDamage(final ItemStack aStack) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("BasicTurbine");
            if (aNBT != null) {
                return aNBT.getLong("Damage");
            }
        } else {
            createNBT(aStack);
        }
        return 0L;
    }

    public static final boolean setFilterDamage(final ItemStack aStack, final long aDamage) {
        NBTTagCompound aNBT = aStack.getTagCompound();
        if (aNBT != null) {
            aNBT = aNBT.getCompoundTag("BasicTurbine");
            if (aNBT != null) {
                aNBT.setLong("Damage", aDamage);
                return true;
            }
        }
        return false;
    }

    public int getMaxDurability(ItemStack aStack) {
        if (aStack != null) {
            int aMeta = aStack.getItemDamage();
            if (aMeta == 0) {
                return 2000;
            }
            if (aMeta == 1) {
                return 4000;
            }
            if (aMeta == 2) {
                return 6000;
            }
        }
        return 0;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            createNBT(stack);
        }
        double currentDamage = getFilterDamage(stack);
        double meta = getMaxDurability(stack);
        double durabilitypercent = currentDamage / meta;
        return durabilitypercent;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        list.add(EnumChatFormatting.GRAY + "An early tier Turbine for Atmospheric Reconditioning.");
        int maxDamage = getMaxDurability(stack);
        list.add(EnumChatFormatting.GRAY + "" + (maxDamage - getFilterDamage(stack)) + "/" + maxDamage + " uses left.");
        super.addInformation(stack, player, list, bool);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }
}
