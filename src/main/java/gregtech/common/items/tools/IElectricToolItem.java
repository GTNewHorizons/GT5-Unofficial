package gregtech.common.items.tools;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;

/**
 * Wires an electric tool's {@link ToolElectricStorage} up to the IC2 interfaces the rest of the game talks to.
 * <p/>
 * Every electric tool implements this by extending {@link ToolElectricItemBase}, which implements it once. What
 * used to justify a separate note here -- that a type with a hand version (an electric screwdriver being a
 * {@link ToolScrewdriverItem} first) could not also extend this because Java only allows one superclass -- no longer
 * applies: that behaviour now comes from composition (see {@link WrenchBehavior}), which frees every electric tool to
 * extend {@link ToolElectricItemBase} directly.
 * <p/>
 * Note what is deliberately <em>not</em> here: {@code getStoredCharge}, {@code getMaxStoredDamage} and the rest of
 * {@link gregtech.api.interfaces.IGTTool}. {@link ToolItemBase} already declares those as concrete methods, and a
 * class method always beats an interface default, so defaults for them would be silently ignored.
 * {@link ToolElectricItemBase} overrides them itself, which is why it -- not this interface -- is what every
 * electric tool actually extends.
 */
public interface IElectricToolItem extends ISpecialElectricItem, IElectricItemManager {

    ToolElectricStorage getElectricStorage();

    /* ---------- IElectricItem ---------- */

    @Override
    default double getMaxCharge(ItemStack stack) {
        return getElectricStorage().getMaxCharge(stack);
    }

    @Override
    default int getTier(ItemStack stack) {
        return getElectricStorage().getTier();
    }

    @Override
    default double getTransferLimit(ItemStack stack) {
        return getElectricStorage().getVoltage();
    }

    @Override
    default boolean canProvideEnergy(ItemStack stack) {
        // A tool is not a battery; it can be filled but never drained by a machine.
        return false;
    }

    @Override
    default String getToolTip(ItemStack stack) {
        // The item renders its own tooltip; returning null keeps IC2's handler out of it.
        return null;
    }

    @Override
    default IElectricItemManager getManager(ItemStack stack) {
        return this;
    }

    /* ---------- IElectricItemManager ---------- */

    @Override
    default double charge(ItemStack stack, double amount, int chargerTier, boolean ignoreTransferLimit,
        boolean simulate) {
        return getElectricStorage().charge(stack, amount, chargerTier, ignoreTransferLimit, simulate);
    }

    @Override
    default double discharge(ItemStack stack, double amount, int dischargerTier, boolean ignoreTransferLimit,
        boolean batteryAlike, boolean simulate) {
        return getElectricStorage()
            .discharge(stack, amount, dischargerTier, ignoreTransferLimit, batteryAlike, simulate);
    }

    @Override
    default double getCharge(ItemStack stack) {
        return getElectricStorage().getCharge(stack);
    }

    @Override
    default boolean canUse(ItemStack stack, double amount) {
        return getElectricStorage().canUse(stack, amount);
    }

    @Override
    default boolean use(ItemStack stack, double amount, EntityLivingBase player) {
        return getElectricStorage().use(stack, amount, player);
    }

    @Override
    default void chargeFromArmor(ItemStack stack, EntityLivingBase player) {
        getElectricStorage().chargeFromArmor(stack, player);
    }
}
