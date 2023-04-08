package gtPlusPlus.core.item.circuit;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

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

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizons.modularui.api.UIInfos;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.net.GT_Packet_UpdateItem;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.uifactory.SelectItemUIFactory;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class GTPP_IntegratedCircuit_Item extends Item implements INetworkUpdatableItem {

    private final List<ItemStack> ALL_VARIANTS = new ArrayList<>();

    private final String iconLocation;
    protected IIcon base;

    public GTPP_IntegratedCircuit_Item(String unlocalizedName, String iconLocation) {
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
    public void addInformation(ItemStack aStack, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
        try {
            aList.add("Configuration == " + aStack.getItemDamage());
            aList.add(
                    GT_LanguageManager.addStringLocalization(
                            new StringBuilder().append(getUnlocalizedName()).append(".tooltip.0").toString(),
                            "Right click to reconfigure"));
            aList.add(
                    GT_LanguageManager.addStringLocalization(
                            new StringBuilder().append(getUnlocalizedName()).append(".tooltip.1").toString(),
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
    public void getSubItems(Item aItem, CreativeTabs p_150895_2_, List aList) {
        aList.add(ItemUtils.simpleMetaStack(aItem, 0, 1));
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
        this.base = u.registerIcon(GTPlusPlus.ID + ":" + iconLocation);
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        return this.base;
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return this.base;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return this.base;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        return this.base;
    }

    @Override
    public boolean receive(ItemStack stack, EntityPlayerMP player, NBTTagCompound tag) {
        int meta = tag.hasKey("meta", Constants.NBT.TAG_BYTE) ? tag.getByte("meta") : -1;
        if (meta < 0 || meta > 24) return true;

        if (!player.capabilities.isCreativeMode) {
            Pair<Integer, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> toolIndex = findConfiguratorInInv(player);
            if (toolIndex == null) return true;

            ItemStack[] mainInventory = player.inventory.mainInventory;
            mainInventory[toolIndex.getKey()] = toolIndex.getValue().apply(mainInventory[toolIndex.getKey()], player);
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
            Pair<Integer, ?> configurator = findConfiguratorInInv(player);
            if (configurator == null) {
                int count;
                try {
                    count = Integer.parseInt(
                            StatCollector.translateToLocal("GT5U.item.programmed_circuit.no_screwdriver.count"));
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
            configuratorStack = player.inventory.mainInventory[configurator.getKey()];
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
                        GTPP_IntegratedCircuit_Item::onConfigured,
                        ALL_VARIANTS,
                        meta,
                        true).createWindow(buildContext));
    }

    private static void onConfigured(ItemStack stack) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("meta", (byte) stack.getItemDamage());
        GT_Values.NW.sendToServer(new GT_Packet_UpdateItem(tag));
    }

    private static Pair<Integer, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> findConfiguratorInInv(
            EntityPlayer player) {
        ItemStack[] mainInventory = player.inventory.mainInventory;
        for (int j = 0, mainInventoryLength = mainInventory.length; j < mainInventoryLength; j++) {
            ItemStack toolStack = mainInventory[j];

            if (!GT_Utility.isStackValid(toolStack)) continue;

            for (Map.Entry<Predicate<ItemStack>, BiFunction<ItemStack, EntityPlayerMP, ItemStack>> p : GregTech_API.sCircuitProgrammerList
                    .entrySet())
                if (p.getKey().test(toolStack)) return Pair.of(j, p.getValue());
        }
        return null;
    }
}
