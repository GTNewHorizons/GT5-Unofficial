package gtPlusPlus.core.item.circuit;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.common.items.ItemIntegratedCircuit.findConfiguratorInInv;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;

import com.gtnewhorizons.modularui.api.UIInfos;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.net.GTPacketUpdateItem;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.gui.modularui.uifactory.SelectItemUIFactory;
import gtPlusPlus.core.util.math.MathUtils;

// TODO Remove after 2.8
@Deprecated
public class GTPPIntegratedCircuitItem extends Item implements INetworkUpdatableItem {

    private final List<ItemStack> ALL_VARIANTS = new ArrayList<>();

    private final String iconLocation;
    protected final IIcon[] iconDamage = new IIcon[25];

    public GTPPIntegratedCircuitItem(String unlocalizedName, String iconLocation) {
        this.setHasSubtypes(true);
        this.setNoRepair();
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setUnlocalizedName(unlocalizedName);
        this.iconLocation = iconLocation;
        GameRegistry.registerItem(this, this.getUnlocalizedName());
        ALL_VARIANTS.add(new ItemStack(this, 0, 0));
        for (int i = 1; i <= 24; i++) {
            ItemStack aStack = new ItemStack(this, 0, i);
            ALL_VARIANTS.add(aStack);
        }
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer p_77624_2_, List<String> aList, boolean p_77624_4_) {
        try {
            aList.add(
                StatCollector.translateToLocalFormatted(
                    "gtpp.tooltip.integrated_circuit.configuration",
                    aStack.getItemDamage()));
            aList.add(
                GTLanguageManager
                    .addStringLocalization(getUnlocalizedName() + ".tooltip.0", "Right click to reconfigure"));
            aList.add(
                GTLanguageManager.addStringLocalization(
                    getUnlocalizedName() + ".tooltip.1",
                    "Needs a screwdriver or circuit programming tool"));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        super.addInformation(aStack, p_77624_2_, aList, p_77624_4_);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return EnumRarity.common;
    }

    @Override
    public void getSubItems(Item aItem, CreativeTabs p_150895_2_, List<ItemStack> aList) {
        aList.add(new ItemStack(aItem, 1, 0));
    }

    @Override
    public boolean isRepairable() {
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
    public void registerIcons(final IIconRegister u) {
        for (int i = 0; i < iconDamage.length; i++) {
            this.iconDamage[i] = u.registerIcon(GTPlusPlus.ID + ":" + iconLocation + "/" + i);
        }
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        return this.iconDamage[MathUtils.balance(meta, 0, 24)];
    }

    @Override
    public boolean receive(ItemStack stack, EntityPlayerMP player, NBTTagCompound tag) {
        int meta = tag.hasKey("meta", Constants.NBT.TAG_BYTE) ? tag.getByte("meta") : -1;
        if (meta < 0 || meta > 24) return true;

        if (!player.capabilities.isCreativeMode) {
            findConfiguratorInInv(player, true); // damage the tool
        }
        stack.setItemDamage(meta);

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        // nothing on server side or fake player
        if (player instanceof FakePlayer || !world.isRemote) return stack;
        // check if any screwdriver
        ItemStack configuratorStack;
        if (player.capabilities.isCreativeMode) {
            configuratorStack = null;
        } else {
            configuratorStack = findConfiguratorInInv(player, false);
            if (configuratorStack == null) {
                int count;
                try {
                    count = Integer
                        .parseInt(StatCollector.translateToLocal("GT5U.item.programmed_circuit.no_screwdriver.count"));
                } catch (NumberFormatException e) {
                    player.addChatComponentMessage(
                        new ChatComponentText(
                            "Error in translation GT5U.item.programmed_circuit.no_screwdriver.count: "
                                + e.getMessage()));
                    count = 1;
                }
                player.addChatComponentMessage(
                    new ChatComponentTranslation(
                        "GT5U.item.programmed_circuit.no_screwdriver." + XSTR.XSTR_INSTANCE.nextInt(count)));
                return stack;
            }
        }
        openSelectorGui(configuratorStack, stack.getItemDamage(), player);
        return stack;
    }

    private void openSelectorGui(ItemStack configurator, int meta, EntityPlayer player) {
        UIInfos.openClientUI(
            player,
            buildContext -> new SelectItemUIFactory(
                StatCollector.translateToLocal("GT5U.item.programmed_circuit.select.header"),
                configurator,
                GTPPIntegratedCircuitItem::onConfigured,
                ALL_VARIANTS,
                meta,
                true).createWindow(buildContext));
    }

    private static void onConfigured(ItemStack stack) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("meta", (byte) stack.getItemDamage());
        GTValues.NW.sendToServer(new GTPacketUpdateItem(tag));
    }
}
