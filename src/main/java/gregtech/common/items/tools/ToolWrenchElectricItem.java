package gregtech.common.items.tools;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.util.GTModHandler;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;

/**
 * An electric wrench: LV, MV or HV.
 * <p/>
 * These run on EU only. The old metadata-based electric wrenches carried a durability bar as well, but only lost a
 * point of it on one action in twenty-five, so it was noise on top of the energy cost; the energy cost per action is
 * unchanged from the old {@code IToolStats} numbers, which is where the balance actually lived.
 */
public class ToolWrenchElectricItem extends ToolWrenchItem implements ISpecialElectricItem, IElectricItemManager {

    private static final String CHARGE_KEY = "GT.ItemCharge";
    /**
     * Only present when this particular wrench holds less than its tier's default, which is how the three battery
     * variants of each electric wrench recipe stay distinct.
     */
    private static final String MAX_CHARGE_KEY = "GT.MaxCharge";

    private final long maxCharge;
    private final long voltage;
    private final int tier;

    public ToolWrenchElectricItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip, long maxCharge, long voltage, int tier) {
        super(unlocalizedName, toolStats, englishNameFormat, englishTooltip);
        this.maxCharge = maxCharge;
        this.voltage = voltage;
        this.tier = tier;
    }

    /**
     * @return this wrench's energy capacity: the value baked into the stack by its recipe, or this tier's default.
     */
    public long getMaxChargeValue(ItemStack stack) {
        long stored = ItemStackNBT.getLong(stack, MAX_CHARGE_KEY);
        return stored > 0 ? stored : maxCharge;
    }

    public long getDefaultMaxCharge() {
        return maxCharge;
    }

    public long getVoltage() {
        return voltage;
    }

    public int getTier() {
        return tier;
    }

    /**
     * Builds a wrench whose capacity differs from this tier's default, for the cheaper battery variants of the
     * crafting recipe.
     *
     * @return the stack, or null if the material has no metadata slot.
     */
    public ItemStack getToolWithMaterial(Materials material, long maxCharge) {
        ItemStack stack = getToolWithMaterial(material);
        if (stack == null) return null;
        if (maxCharge > 0 && maxCharge != this.maxCharge) ItemStackNBT.setLong(stack, MAX_CHARGE_KEY, maxCharge);
        return stack;
    }

    /* ---------- DISPLAY ---------- */

    /**
     * Shows the stored energy where the hand wrench shows its durability. Without this an electric wrench would
     * report nothing at all, since {@link gregtech.api.items.GTGenericItem} only knows how to describe a damage bar and
     * these have none.
     */
    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        if (getToolMaterial(stack) != Materials._NULL) {
            list.add(
                EnumChatFormatting.AQUA
                    + translateToLocalFormatted(
                        "gt.item.desc.eu_info",
                        formatNumber(getRealCharge(stack)),
                        formatNumber(getMaxChargeValue(stack)),
                        formatNumber(voltage))
                    + EnumChatFormatting.GRAY);
        }
        super.addAdditionalToolTips(list, stack, player);
    }

    /**
     * Hands out fully charged wrenches, so that one spawned from NEI or the creative tab is usable straight away
     * rather than being a flat battery.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        final int firstAdded = list.size();
        super.getSubItems(item, creativeTab, list);
        for (int i = firstAdded; i < list.size(); i++) {
            ItemStack stack = (ItemStack) list.get(i);
            setCharge(stack, getMaxChargeValue(stack));
        }
    }

    /* ---------- NO DURABILITY ---------- */

    @Override
    public long getStoredDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public long getMaxStoredDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public long getStoredCharge(ItemStack stack) {
        return getRealCharge(stack);
    }

    @Override
    public long getMaxStoredCharge(ItemStack stack) {
        return getMaxChargeValue(stack);
    }

    /**
     * Pays for one action out of the stored energy instead of out of durability. The amounts are the same ones the
     * metadata-based wrench passed to {@code doDamage}, which for an electric tool were already EU.
     */
    @Override
    public boolean doDamage(ItemStack stack, long amount) {
        return use(stack, amount, null);
    }

    /* ---------- CHARGE STORAGE ---------- */

    public long getRealCharge(ItemStack stack) {
        return ItemStackNBT.getLong(stack, CHARGE_KEY);
    }

    public boolean setCharge(ItemStack stack, long charge) {
        charge = Math.min(Math.max(charge, 0), getMaxChargeValue(stack));
        if (charge > 0) {
            ItemStackNBT.setLong(stack, CHARGE_KEY, charge);
        } else {
            ItemStackNBT.removeTag(stack, CHARGE_KEY);
        }
        return true;
    }

    /* ---------- IElectricItem ---------- */

    @Override
    public boolean canProvideEnergy(ItemStack stack) {
        // A wrench is not a battery; it can be filled but never drained by a machine.
        return false;
    }

    @Override
    public Item getChargedItem(ItemStack stack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack stack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack stack) {
        return getMaxChargeValue(stack);
    }

    @Override
    public int getTier(ItemStack stack) {
        return tier;
    }

    @Override
    public double getTransferLimit(ItemStack stack) {
        return voltage;
    }

    @Override
    public IElectricItemManager getManager(ItemStack stack) {
        return this;
    }

    @Override
    public String getToolTip(ItemStack stack) {
        // We render our own tooltip; returning null keeps IC2's handler out of it.
        return null;
    }

    @Override
    public boolean getShareTag() {
        return true;
    }

    /* ---------- IElectricItemManager ---------- */

    @Override
    public double charge(ItemStack stack, double amount, int chargerTier, boolean ignoreTransferLimit,
        boolean simulate) {
        if (tier > chargerTier || stack.stackSize != 1) return 0;
        long transfer = ignoreTransferLimit ? (long) amount : Math.min(voltage, (long) amount);
        long before = getRealCharge(stack);
        long after = Math
            .min(getMaxChargeValue(stack), Long.MAX_VALUE - transfer >= before ? before + transfer : Long.MAX_VALUE);
        if (!simulate) setCharge(stack, after);
        return after - before;
    }

    @Override
    public double discharge(ItemStack stack, double amount, int dischargerTier, boolean ignoreTransferLimit,
        boolean batteryAlike, boolean simulate) {
        if (tier > dischargerTier) return 0;
        if (batteryAlike) return 0;
        long before = getRealCharge(stack);
        long after = Math.max(0, before - (ignoreTransferLimit ? (long) amount : Math.min(voltage, (long) amount)));
        if (!simulate) setCharge(stack, after);
        return before - after;
    }

    @Override
    public double getCharge(ItemStack stack) {
        return getRealCharge(stack);
    }

    @Override
    public boolean canUse(ItemStack stack, double amount) {
        return getRealCharge(stack) >= amount;
    }

    @Override
    public boolean use(ItemStack stack, double amount, EntityLivingBase player) {
        chargeFromArmor(stack, player);
        if (player instanceof EntityPlayer entityPlayer && entityPlayer.capabilities.isCreativeMode) return true;
        double available = discharge(stack, amount, Integer.MAX_VALUE, true, false, true);
        boolean enough = Math.abs(available - amount) < .0000001;
        discharge(stack, amount, Integer.MAX_VALUE, true, false, false);
        chargeFromArmor(stack, player);
        return enough;
    }

    @Override
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
}
