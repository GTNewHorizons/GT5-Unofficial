package gregtech.common.items.toolbox;

import java.util.Optional;
import java.util.function.BiFunction;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import gregtech.common.items.ItemGTToolbox;
import ic2.api.item.IElectricItemManager;

/**
 * A special {@link IElectricItemManager} for delegating electricity tasks to the toolbox's battery.
 */
public class ToolboxElectricManager implements IElectricItemManager {

    /**
     * Get the {@link IElectricItemManager} for the battery inside a toolbox.
     *
     * @param toolbox The toolbox to search
     * @param mapper  A function to run if a battery is found inside the toolbox.
     *                Arguments are the battery's {@link ItemStack} and its manager.
     * @param <U>     The return type of the mapper
     * @return An optional containing the results of the mapping function, or empty if no battery or manager was found.
     */
    private static <U> Optional<U> mapBatteryManager(final ItemStack toolbox,
        BiFunction<? super ItemStack, ? super IElectricItemManager, ? extends U> mapper) {
        final Optional<ItemStack> stack = ToolboxUtil.getBattery(toolbox);

        return stack.flatMap(ItemGTToolbox::getElectricManager)
            .map(manager -> mapper.apply(stack.get(), manager));
    }

    @Override
    public double charge(final ItemStack toolbox, final double charge, final int tier,
        final boolean ignoreTransferLimit, final boolean simulate) {

        // To prevent syncing issues, don't allow the toolbox to do any charging while open.
        if (!ToolboxUtil.canCharge(toolbox)) {
            return 0;
        }

        return mapBatteryManager(toolbox, (battery, manager) -> {
            final double amountCharged = manager.charge(battery, charge, tier, ignoreTransferLimit, simulate);
            if (amountCharged > 0) {
                ToolboxUtil.saveBattery(toolbox, battery);
            }

            return amountCharged;
        }).orElse(0d);
    }

    @Override
    public double discharge(final ItemStack toolbox, final double charge, final int tier,
        final boolean ignoreTransferLimit, final boolean batteryAlike, final boolean simulate) {

        // To prevent syncing issues, don't allow the toolbox to do any discharging while open.
        if (!ToolboxUtil.canCharge(toolbox)) {
            return 0;
        }

        return mapBatteryManager(toolbox, (battery, manager) -> {
            final double amountDischarged = manager
                .discharge(battery, charge, tier, ignoreTransferLimit, batteryAlike, simulate);
            if (amountDischarged > 0) {
                ToolboxUtil.saveBattery(toolbox, battery);
            }
            return amountDischarged;
        }).orElse(0d);
    }

    @Override
    public double getCharge(final ItemStack toolbox) {
        return mapBatteryManager(toolbox, (battery, manager) -> manager.getCharge(battery)).orElse(0d);
    }

    @Override
    public boolean canUse(final ItemStack toolbox, final double amount) {
        return mapBatteryManager(toolbox, (battery, manager) -> manager.canUse(battery, amount)).orElse(false);
    }

    @Override
    public boolean use(final ItemStack toolbox, final double amount, final EntityLivingBase entityLivingBase) {
        return mapBatteryManager(toolbox, (battery, manager) -> {
            final boolean used = manager.use(battery, amount, entityLivingBase);
            if (used) {
                ToolboxUtil.saveBattery(toolbox, battery);
            }

            return used;
        }).orElse(false);
    }

    @Override
    public void chargeFromArmor(final ItemStack toolbox, final EntityLivingBase entityLivingBase) {
        // TODO: add mutate stack
        ToolboxUtil.getBattery(toolbox)
            .ifPresent(
                battery -> ItemGTToolbox.getElectricManager(battery)
                    .ifPresent(manager -> {
                        manager.chargeFromArmor(battery, entityLivingBase);
                        ToolboxUtil.saveBattery(toolbox, battery);
                    }));
    }

    @Override
    public String getToolTip(final ItemStack toolbox) {
        return null;
    }

}
