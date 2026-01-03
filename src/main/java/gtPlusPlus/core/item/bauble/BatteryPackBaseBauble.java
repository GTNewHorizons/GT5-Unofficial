package gtPlusPlus.core.item.bauble;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import baubles.api.BaubleType;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.common.config.OPStuff;
import gtPlusPlus.core.creative.AddToCreativeTab;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class BatteryPackBaseBauble extends ElectricBaseBauble {

    public BatteryPackBaseBauble(int tier) {
        super(BaubleType.BELT, tier, GTValues.V[tier] * 20 * 300, "GTPP.BattPack.0" + tier + ".name");
        String aUnlocalName = "GTPP.BattPack.0" + tier + ".name";
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        if (GameRegistry.findItem(GTPlusPlus.ID, aUnlocalName) == null) {
            GameRegistry.registerItem(this, aUnlocalName);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List<ItemStack> itemList) {
        ItemStack itemStack = new ItemStack(this, 1);
        ItemStack charged;
        if (this.getEmptyItem(itemStack) == this) {
            charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, 0.0D, Integer.MAX_VALUE, true, false);
            itemList.add(charged);
        }
        if (this.getChargedItem(itemStack) == this) {
            charged = new ItemStack(this, 1);
            ElectricItem.manager.charge(charged, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false);
            itemList.add(charged);
        }
    }

    @Override
    public void onUpdate(final ItemStack itemStack, final World worldObj, final Entity player, final int p_77663_4_,
        final boolean p_77663_5_) {
        super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
    }

    @Override
    public String getItemStackDisplayName(final ItemStack p_77653_1_) {
        return (EnumChatFormatting.BLUE + super.getItemStackDisplayName(p_77653_1_) + EnumChatFormatting.GRAY);
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List<String> list,
        final boolean bool) {
        list.add("");
        String aString1 = StatCollector.translateToLocal("GTPP.battpack.tooltip.1");
        String aString2 = StatCollector.translateToLocal("GTPP.battpack.tooltip.2");
        String aString3 = StatCollector.translateToLocal("GTPP.battpack.tooltip.3");
        String aString4 = StatCollector.translateToLocal("GTPP.battpack.tooltip.4");

        String aEU = StatCollector.translateToLocal("GTPP.info.eu");
        String aEUT = aEU + "/t";

        list.add(EnumChatFormatting.GREEN + aString1 + EnumChatFormatting.GRAY);
        list.add(
            EnumChatFormatting.GREEN + aString2
                + " "
                + (int) getTransferLimit(stack)
                + aEUT
                + " "
                + aString3
                + EnumChatFormatting.GRAY);
        list.add(EnumChatFormatting.GREEN + aString4 + EnumChatFormatting.GRAY);
        super.addInformation(stack, aPlayer, list, bool);
    }

    @Override
    public String getToolTip(final ItemStack stack) {
        return ElectricItem.manager.getToolTip(stack);
    }

    @Override
    public boolean canEquip(final ItemStack arg0, final EntityLivingBase arg1) {
        return true;
    }

    @Override
    public boolean canUnequip(final ItemStack arg0, final EntityLivingBase arg1) {
        return true;
    }

    @Override
    public void onEquipped(final ItemStack arg0, final EntityLivingBase arg1) {}

    @Override
    public void onUnequipped(final ItemStack arg0, final EntityLivingBase arg1) {}

    @Override
    public void onWornTick(final ItemStack aBaubleStack, final EntityLivingBase aLivingBase) {
        if (!aLivingBase.worldObj.isRemote) {
            try {
                if (this.getCharge(aBaubleStack) >= getTransferLimit(aBaubleStack)) {
                    if (aLivingBase instanceof EntityPlayer aPlayer) {

                        // Armor
                        ItemStack[] inv = aPlayer.inventory.armorInventory;
                        chargeInventory(inv, inv.length, aBaubleStack);

                        // Hotbar Slots
                        inv = aPlayer.inventory.mainInventory;
                        chargeInventory(inv, InventoryPlayer.getHotbarSize(), aBaubleStack);
                    }
                }
            } catch (Exception ignored) {}
        }
    }

    private void chargeInventory(ItemStack[] inventory, int endIndex, ItemStack aBaubleStack) {
        for (int i = 0; i < endIndex && i < inventory.length; i++) {
            ItemStack aInvStack = inventory[i];
            if (aInvStack != null && aInvStack != aBaubleStack) {
                if (aInvStack.getItem() instanceof IElectricItem electricItem) {
                    // IElectricItem (EU)
                    double aTransferRate = electricItem.getTransferLimit(aInvStack);
                    double aItemCharge = ElectricItem.manager.getCharge(aInvStack);
                    if (aItemCharge >= 0 && aItemCharge != electricItem.getMaxCharge(aInvStack)) {
                        if (aItemCharge <= (electricItem.getMaxCharge(aInvStack) - aTransferRate)) {
                            if (ElectricItem.manager.getCharge(aBaubleStack) >= aTransferRate) {
                                if (ElectricItem.manager.getCharge(aInvStack)
                                    <= (electricItem.getMaxCharge(aInvStack) - aTransferRate)) {
                                    double d = ElectricItem.manager
                                        .charge(aInvStack, aTransferRate * 16, mTier, false, true);
                                    if (d > 0) {
                                        d = ElectricItem.manager
                                            .charge(aInvStack, aTransferRate * 16, mTier, false, false);
                                        ElectricItem.manager.discharge(aBaubleStack, d, mTier, false, true, false);
                                    }
                                }
                            }
                        }
                    }
                } else if (OPStuff.outputRF) {
                    // IEnergyContainerItem (RF)
                    if (aInvStack.getItem() instanceof IEnergyContainerItem energyItem) {
                        int aItemCharge = energyItem.getEnergyStored(aInvStack);
                        int aItemMaxCharge = energyItem.getMaxEnergyStored(aInvStack);
                        double aBaubleCharge = ElectricItem.manager.getCharge(aBaubleStack);
                        if (aItemCharge >= 0 && aItemCharge < aItemMaxCharge && aBaubleCharge > 0) {
                            int aMaxChargeAmount = aItemMaxCharge - aItemCharge;
                            double aMaxChargeAmountInEU = Math
                                .ceil(aMaxChargeAmount * 100.0D / OPStuff.howMuchRFWith100EUInInput);
                            double aActualChargeInEU = Math.min(aBaubleCharge, aMaxChargeAmountInEU);
                            int aActualCharge = (int) Math
                                .floor(aActualChargeInEU * OPStuff.howMuchRFWith100EUInInput / 100);

                            int aCharged = energyItem.receiveEnergy(aInvStack, aActualCharge, false);
                            double aDischarge = Math.ceil(aCharged * 100.0D / OPStuff.howMuchRFWith100EUInInput);
                            ElectricItem.manager.discharge(aBaubleStack, aDischarge, mTier, false, true, false);
                        }
                    }
                }
            }

            if (this.getCharge(aBaubleStack) <= 0) {
                break;
            }
        }
    }

    @Override
    public String getTextureNameForBauble() {
        return "chargepack/" + mTier;
    }
}
