package gtPlusPlus.xmod.gregtech.api.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.V;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;

public abstract class GTMetaItemBase extends GTGenericItem
    implements ISpecialElectricItem, IElectricItemManager, IFluidContainerItem {

    /**
     * Creates the Item using these Parameters.
     *
     * @param aUnlocalized         The Unlocalized Name of this Item.
     * @param aGeneratedPrefixList The OreDict Prefixes you want to have generated.
     */
    public GTMetaItemBase(final String aUnlocalized) {
        super(aUnlocalized, "Generated Item", null);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public abstract Long[] getElectricStats(ItemStack aStack);

    public abstract Long[] getFluidContainerStats(ItemStack aStack);

    @Override
    public final void addInformation(final ItemStack aStack, final EntityPlayer aPlayer, List aList,
        final boolean aF3_H) {
        final String tKey = this.getUnlocalizedName(aStack) + ".tooltip",
            tString = GTLanguageManager.getTranslation(tKey);
        if (GTUtility.isStringValid(tString) && !tKey.equals(tString)) {
            aList.add(tString);
        }

        Long[] tStats = this.getElectricStats(aStack);
        if (tStats != null) {
            if (tStats[3] > 0) {
                aList.add(
                    EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                        "item.itemBaseEuItem.tooltip.1",
                        formatNumber(tStats[3]),
                        (tStats[2] >= 0 ? tStats[2] : 0)) + EnumChatFormatting.GRAY);
            } else {
                final long tCharge = this.getRealCharge(aStack);
                if ((tStats[3] == -2) && (tCharge <= 0)) {
                    aList.add(
                        EnumChatFormatting.AQUA + StatCollector.translateToLocal("item.itemBaseEuItem.tooltip.2")
                            + EnumChatFormatting.GRAY);
                } else {
                    aList.add(
                        EnumChatFormatting.AQUA
                            + StatCollector.translateToLocalFormatted(
                                "item.itemBaseEuItem.tooltip.3",
                                formatNumber(tCharge),
                                formatNumber(Math.abs(tStats[0])),
                                formatNumber(
                                    V[(int) (tStats[2] >= 0 ? tStats[2] < V.length ? tStats[2] : V.length - 1 : 1)]))
                            + EnumChatFormatting.GRAY);
                }
            }
        }

        tStats = this.getFluidContainerStats(aStack);
        if ((tStats != null) && (tStats[0] > 0)) {
            final FluidStack tFluid = this.getFluidContent(aStack);
            aList.add(
                EnumChatFormatting.BLUE
                    + ((tFluid == null ? "No Fluids Contained" : GTUtility.getFluidName(tFluid, true)))
                    + EnumChatFormatting.GRAY);
            aList.add(
                EnumChatFormatting.BLUE
                    + (formatNumber(tFluid == null ? 0 : tFluid.amount) + "L / " + formatNumber(tStats[0]) + "L")
                    + EnumChatFormatting.GRAY);
        }

        this.addAdditionalToolTips(aList, aStack);
    }

    @Override
    public final boolean canProvideEnergy(final ItemStack aStack) {
        final Long[] tStats = this.getElectricStats(aStack);
        if (tStats == null) {
            return false;
        }
        return (tStats[3] > 0) || ((aStack.stackSize == 1) && ((tStats[3] == -2) || (tStats[3] == -3)));
    }

    @Override
    public final double getMaxCharge(final ItemStack aStack) {
        final Long[] tStats = this.getElectricStats(aStack);
        if (tStats == null) {
            return 0;
        }
        return Math.abs(tStats[0]);
    }

    @Override
    public final double getTransferLimit(final ItemStack aStack) {
        final Long[] tStats = this.getElectricStats(aStack);
        if (tStats == null) {
            return 0;
        }
        return Math.max(tStats[1], tStats[3]);
    }

    @Override
    public final double charge(final ItemStack aStack, final double aCharge, final int aTier,
        final boolean aIgnoreTransferLimit, final boolean aSimulate) {
        final Long[] tStats = this.getElectricStats(aStack);
        if ((tStats == null) || (tStats[2] > aTier)
            || !((tStats[3] == -1) || (tStats[3] == -3) || ((tStats[3] < 0) && (aCharge == Integer.MAX_VALUE)))
            || (aStack.stackSize != 1)) {
            return 0;
        }
        final long tChargeBefore = this.getRealCharge(aStack),
            tNewCharge = aCharge == Integer.MAX_VALUE ? Long.MAX_VALUE
                : Math.min(
                    Math.abs(tStats[0]),
                    tChargeBefore + (aIgnoreTransferLimit ? (long) aCharge : Math.min(tStats[1], (long) aCharge)));
        if (!aSimulate) {
            this.setCharge(aStack, tNewCharge);
        }
        return tNewCharge - tChargeBefore;
    }

    @Override
    public final double discharge(final ItemStack aStack, final double aCharge, final int aTier,
        final boolean aIgnoreTransferLimit, final boolean aBatteryAlike, final boolean aSimulate) {
        final Long[] tStats = this.getElectricStats(aStack);
        if ((tStats == null) || (tStats[2] > aTier)) {
            return 0;
        }
        if (aBatteryAlike && !this.canProvideEnergy(aStack)) {
            return 0;
        }
        if (tStats[3] > 0) {
            if ((aCharge < tStats[3]) || (aStack.stackSize < 1)) {
                return 0;
            }
            if (!aSimulate) {
                aStack.stackSize--;
            }
            return tStats[3];
        }
        final long tChargeBefore = this.getRealCharge(aStack), tNewCharge = Math
            .max(0, tChargeBefore - (aIgnoreTransferLimit ? (long) aCharge : Math.min(tStats[1], (long) aCharge)));
        if (!aSimulate) {
            this.setCharge(aStack, tNewCharge);
        }
        return tChargeBefore - tNewCharge;
    }

    @Override
    public final double getCharge(final ItemStack aStack) {
        return this.getRealCharge(aStack);
    }

    @Override
    public final boolean canUse(final ItemStack aStack, final double aAmount) {
        return this.getRealCharge(aStack) >= aAmount;
    }

    @Override
    public final boolean use(final ItemStack aStack, final double aAmount, final EntityLivingBase aPlayer) {
        this.chargeFromArmor(aStack, aPlayer);
        if ((aPlayer instanceof EntityPlayer) && ((EntityPlayer) aPlayer).capabilities.isCreativeMode) {
            return true;
        }
        final double tTransfer = this.discharge(aStack, aAmount, Integer.MAX_VALUE, true, false, true);
        if (tTransfer == aAmount) {
            this.discharge(aStack, aAmount, Integer.MAX_VALUE, true, false, false);
            this.chargeFromArmor(aStack, aPlayer);
            return true;
        }
        this.discharge(aStack, aAmount, Integer.MAX_VALUE, true, false, false);
        this.chargeFromArmor(aStack, aPlayer);
        return false;
    }

    @Override
    public final void chargeFromArmor(final ItemStack aStack, final EntityLivingBase aPlayer) {
        if ((aPlayer == null) || aPlayer.worldObj.isRemote) {
            return;
        }
        for (int i = 1; i < 5; i++) {
            final ItemStack tArmor = aPlayer.getEquipmentInSlot(i);
            if (GTModHandler.isElectricItem(tArmor)) {
                final IElectricItem tArmorItem = (IElectricItem) tArmor.getItem();
                if (tArmorItem.canProvideEnergy(tArmor) && (tArmorItem.getTier(tArmor) >= this.getTier(aStack))) {
                    final double tCharge = ElectricItem.manager.discharge(
                        tArmor,
                        this.charge(aStack, Integer.MAX_VALUE - 1, Integer.MAX_VALUE, true, true),
                        Integer.MAX_VALUE,
                        true,
                        true,
                        false);
                    if (tCharge > 0) {
                        this.charge(aStack, tCharge, Integer.MAX_VALUE, true, false);
                        if (aPlayer instanceof EntityPlayer) {
                            final Container tContainer = ((EntityPlayer) aPlayer).openContainer;
                            if (tContainer != null) {
                                tContainer.detectAndSendChanges();
                            }
                        }
                    }
                }
            }
        }
    }

    public final long getRealCharge(final ItemStack aStack) {
        final Long[] tStats = this.getElectricStats(aStack);
        if (tStats == null) {
            return 0;
        }
        if (tStats[3] > 0) {
            return (int) (long) tStats[3];
        }
        final NBTTagCompound tNBT = aStack.getTagCompound();
        return tNBT == null ? 0 : tNBT.getLong("GT.ItemCharge");
    }

    public final boolean setCharge(final ItemStack aStack, long aCharge) {
        final Long[] tStats = this.getElectricStats(aStack);
        if ((tStats == null) || (tStats[3] > 0)) {
            return false;
        }
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
        }
        tNBT.removeTag("GT.ItemCharge");
        aCharge = Math.min(tStats[0] < 0 ? Math.abs(tStats[0] / 2) : aCharge, Math.abs(tStats[0]));
        if (aCharge > 0) {
            aStack.setItemDamage(this.getChargedMetaData(aStack));
            tNBT.setLong("GT.ItemCharge", aCharge);
        } else {
            aStack.setItemDamage(this.getEmptyMetaData(aStack));
        }
        if (tNBT.hasNoTags()) {
            aStack.setTagCompound(null);
        } else {
            aStack.setTagCompound(tNBT);
        }
        this.isItemStackUsable(aStack);
        return true;
    }

    public short getChargedMetaData(final ItemStack aStack) {
        return (short) aStack.getItemDamage();
    }

    public short getEmptyMetaData(final ItemStack aStack) {
        return (short) aStack.getItemDamage();
    }

    @Override
    public FluidStack getFluid(final ItemStack aStack) {
        return this.getFluidContent(aStack);
    }

    @Override
    public int getCapacity(final ItemStack aStack) {
        final Long[] tStats = this.getFluidContainerStats(aStack);
        return tStats == null ? 0 : (int) Math.max(0, tStats[0]);
    }

    @Override
    public int fill(final ItemStack aStack, final FluidStack aFluid, final boolean doFill) {
        if ((aStack == null) || (aStack.stackSize != 1)) {
            return 0;
        }

        final ItemStack tStack = GTUtility.fillFluidContainer(aFluid, aStack, false, false);
        if (tStack != null) {
            aStack.setItemDamage(tStack.getItemDamage());
            aStack.func_150996_a(tStack.getItem());
            return GTUtility.getFluidForFilledItem(tStack, false).amount;
        }

        final Long[] tStats = this.getFluidContainerStats(aStack);
        if ((tStats == null) || (tStats[0] <= 0)
            || (aFluid == null)
            || (aFluid.getFluid()
                .getID() <= 0)
            || (aFluid.amount <= 0)) {
            return 0;
        }

        FluidStack tFluid = this.getFluidContent(aStack);

        if ((tFluid == null) || (tFluid.getFluid()
            .getID() <= 0)) {
            if (aFluid.amount <= tStats[0]) {
                if (doFill) {
                    this.setFluidContent(aStack, aFluid);
                }
                return aFluid.amount;
            }
            if (doFill) {
                tFluid = aFluid.copy();
                tFluid.amount = (int) (long) tStats[0];
                this.setFluidContent(aStack, tFluid);
            }
            return (int) (long) tStats[0];
        }

        if (!tFluid.isFluidEqual(aFluid)) {
            return 0;
        }

        final int space = (int) (long) tStats[0] - tFluid.amount;
        if (aFluid.amount <= space) {
            if (doFill) {
                tFluid.amount += aFluid.amount;
                this.setFluidContent(aStack, tFluid);
            }
            return aFluid.amount;
        }
        if (doFill) {
            tFluid.amount = (int) (long) tStats[0];
            this.setFluidContent(aStack, tFluid);
        }
        return space;
    }

    @Override
    public FluidStack drain(final ItemStack aStack, final int maxDrain, final boolean doDrain) {
        if ((aStack == null) || (aStack.stackSize != 1)) {
            return null;
        }

        FluidStack tFluid = GTUtility.getFluidForFilledItem(aStack, false);
        if ((tFluid != null) && (maxDrain >= tFluid.amount)) {
            final ItemStack tStack = GTUtility.getContainerItem(aStack, false);
            if (tStack == null) {
                aStack.stackSize = 0;
                return tFluid;
            }
            aStack.setItemDamage(tStack.getItemDamage());
            aStack.func_150996_a(tStack.getItem());
            return tFluid;
        }

        final Long[] tStats = this.getFluidContainerStats(aStack);
        if ((tStats == null) || (tStats[0] <= 0)) {
            return null;
        }

        tFluid = this.getFluidContent(aStack);
        if (tFluid == null) {
            return null;
        }

        int used = maxDrain;
        if (tFluid.amount < used) {
            used = tFluid.amount;
        }
        if (doDrain) {
            tFluid.amount -= used;
            this.setFluidContent(aStack, tFluid);
        }

        final FluidStack drained = tFluid.copy();
        drained.amount = used;
        return drained;
    }

    public FluidStack getFluidContent(final ItemStack aStack) {
        final Long[] tStats = this.getFluidContainerStats(aStack);
        if ((tStats == null) || (tStats[0] <= 0)) {
            return GTUtility.getFluidForFilledItem(aStack, false);
        }
        final NBTTagCompound tNBT = aStack.getTagCompound();
        return tNBT == null ? null : FluidStack.loadFluidStackFromNBT(tNBT.getCompoundTag("GT.FluidContent"));
    }

    public void setFluidContent(final ItemStack aStack, final FluidStack aFluid) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
        } else {
            tNBT.removeTag("GT.FluidContent");
        }
        if ((aFluid != null) && (aFluid.amount > 0)) {
            tNBT.setTag("GT.FluidContent", aFluid.writeToNBT(new NBTTagCompound()));
        }
        if (tNBT.hasNoTags()) {
            aStack.setTagCompound(null);
        } else {
            aStack.setTagCompound(tNBT);
        }
        this.isItemStackUsable(aStack);
    }

    @Override
    public int getItemStackLimit(final ItemStack aStack) {
        Long[] tStats = this.getElectricStats(aStack);
        if ((tStats != null) && ((tStats[3] == -1) || (tStats[3] == -3)) && (this.getRealCharge(aStack) > 0)) {
            return 1;
        }
        tStats = this.getFluidContainerStats(aStack);
        if (tStats != null) {
            return (int) (long) tStats[1];
        }
        return 64;
    }

    @Override
    public final Item getChargedItem(final ItemStack itemStack) {
        return this;
    }

    @Override
    public final Item getEmptyItem(final ItemStack itemStack) {
        return this;
    }

    @Override
    public final int getTier(final ItemStack aStack) {
        final Long[] tStats = this.getElectricStats(aStack);
        return (int) (tStats == null ? Integer.MAX_VALUE : tStats[2]);
    }

    @Override
    public final String getToolTip(final ItemStack aStack) {
        return null;
    } // This has its own ToolTip Handler, no need to let the IC2 Handler screw us up at this Point

    @Override
    public final IElectricItemManager getManager(final ItemStack aStack) {
        return this;
    } // We are our own Manager

    @Override
    public final boolean getShareTag() {
        return true;
    } // just to be sure.

    @Override
    public boolean isBookEnchantable(final ItemStack aStack, final ItemStack aBook) {
        return false;
    }

}
