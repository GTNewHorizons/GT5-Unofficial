package gregtech.common.items.tools;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.api.util.GTModHandler;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

/**
 * The energy half of an electric tool: where the charge lives on the stack, and what charging, discharging and
 * spending it do.
 * <p/>
 * This is composed into the item rather than inherited, because an electric tool's class already has to extend its own
 * tool type to pick up that type's behaviour and cross-mod interfaces -- an electric screwdriver is a screwdriver
 * first. {@link IElectricToolItem} turns an instance of this into the IC2 interfaces the game asks for.
 */
public final class ToolElectricStorage {

    private static final String CHARGE_KEY = "GT.ItemCharge";

    private final long maxCharge;
    private final long voltage;
    private final int tier;

    public ToolElectricStorage(long maxCharge, long voltage, int tier) {
        this.maxCharge = maxCharge;
        this.voltage = voltage;
        this.tier = tier;
    }

    public long getVoltage() {
        return voltage;
    }

    public int getTier() {
        return tier;
    }

    /**
     * @return this tool's energy capacity, which is its tier's, whatever battery the recipe used to build it.
     */
    public long getMaxCharge(ItemStack stack) {
        return maxCharge;
    }

    public long getCharge(ItemStack stack) {
        return ItemStackNBT.getLong(stack, CHARGE_KEY);
    }

    public void setCharge(ItemStack stack, long charge) {
        charge = Math.min(Math.max(charge, 0), getMaxCharge(stack));
        if (charge > 0) {
            ItemStackNBT.setLong(stack, CHARGE_KEY, charge);
        } else {
            ItemStackNBT.removeTag(stack, CHARGE_KEY);
        }
    }

    public void fillToFull(ItemStack stack) {
        setCharge(stack, getMaxCharge(stack));
    }

    /* ---------- TRANSFER ---------- */

    public double charge(ItemStack stack, double amount, int chargerTier, boolean ignoreTransferLimit,
        boolean simulate) {
        if (tier > chargerTier || stack.stackSize != 1) return 0;
        long transfer = ignoreTransferLimit ? (long) amount : Math.min(voltage, (long) amount);
        long before = getCharge(stack);
        long after = Math
            .min(getMaxCharge(stack), Long.MAX_VALUE - transfer >= before ? before + transfer : Long.MAX_VALUE);
        if (!simulate) setCharge(stack, after);
        return after - before;
    }

    public double discharge(ItemStack stack, double amount, int dischargerTier, boolean ignoreTransferLimit,
        boolean batteryAlike, boolean simulate) {
        if (tier > dischargerTier) return 0;
        // A tool is not a battery; it can be filled but never drained by a machine.
        if (batteryAlike) return 0;
        long before = getCharge(stack);
        long after = Math.max(0, before - (ignoreTransferLimit ? (long) amount : Math.min(voltage, (long) amount)));
        if (!simulate) setCharge(stack, after);
        return before - after;
    }

    public boolean canUse(ItemStack stack, double amount) {
        return getCharge(stack) >= amount;
    }

    /**
     * Spends energy on one action, topping up from the player's armour first, exactly as the metadata-based electric
     * tools did.
     */
    public boolean use(ItemStack stack, double amount, EntityLivingBase player) {
        chargeFromArmor(stack, player);
        if (player instanceof EntityPlayer entityPlayer && entityPlayer.capabilities.isCreativeMode) return true;
        double available = discharge(stack, amount, Integer.MAX_VALUE, true, false, true);
        boolean enough = Math.abs(available - amount) < .0000001;
        discharge(stack, amount, Integer.MAX_VALUE, true, false, false);
        chargeFromArmor(stack, player);
        return enough;
    }

    public void chargeFromArmor(ItemStack stack, EntityLivingBase player) {
        if (player == null || player.worldObj.isRemote) return;
        for (int i = 1; i < 5; i++) {
            ItemStack armor = player.getEquipmentInSlot(i);
            if (!GTModHandler.isElectricItem(armor)) continue;
            IElectricItem armorItem = (IElectricItem) armor.getItem();
            if (!armorItem.canProvideEnergy(armor) || armorItem.getTier(armor) < tier) continue;
            double charge = ElectricItem.manager.discharge(
                armor,
                charge(stack, Integer.MAX_VALUE - 1, Integer.MAX_VALUE, true, true),
                Integer.MAX_VALUE,
                true,
                true,
                false);
            if (charge > 0) {
                charge(stack, charge, Integer.MAX_VALUE, true, false);
                if (player instanceof EntityPlayer entityPlayer) {
                    Container container = entityPlayer.openContainer;
                    if (container != null) container.detectAndSendChanges();
                }
            }
        }
    }

    /* ---------- DISPLAY ---------- */

    /**
     * Adds the stored-energy line, which goes where a tool that wears out shows its durability. Without it an electric
     * tool would report nothing at all, since {@link gregtech.api.items.GTGenericItem} only knows how to describe a
     * damage bar and these have none.
     */
    public void addChargeToolTip(List<String> list, ItemStack stack) {
        list.add(
            EnumChatFormatting.AQUA
                + translateToLocalFormatted(
                    "gt.item.desc.eu_info",
                    formatNumber(getCharge(stack)),
                    formatNumber(getMaxCharge(stack)),
                    formatNumber(voltage))
                + EnumChatFormatting.GRAY);
    }
}
