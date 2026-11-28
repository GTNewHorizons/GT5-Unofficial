package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Predicate;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.api.interfaces.metatileentity.IFluidLockableMui2;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.modularui2.widget.FluidLockSlotWidget;

public abstract class MTEMachineWithFluidScreenBaseGui<T extends MTETieredMachineBlock>
    extends MTEMachineWithScreenBaseGui<T> {

    protected final FluidLockSlotWidget fluidLockSlotWidget;

    public MTEMachineWithFluidScreenBaseGui(T machine) {
        super(machine);
        if (supportsFilterScreen()) fluidLockSlotWidget = new FluidLockSlotWidget((IFluidLockableMui2) machine);
        else fluidLockSlotWidget = null;
    }

    protected abstract IFluidTank getMachineFluidTank();

    protected TextWidget<?> createScreenAmountLabel(ModularPanel panel, PanelSyncManager syncManager) {
        return IKey.lang("GT5U.machines.basic_tank.liquid_amount")
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);
    }

    protected TextWidget<?> createScreenAmount(ModularPanel panel, PanelSyncManager syncManager) {
        return IKey.dynamic(() -> formatNumber(getMachineFluidTank().getFluidAmount()))
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);
    }

    @Override
    protected FluidSlot createSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return new FluidSlot().syncHandler(new FluidSlotSyncHandler(getMachineFluidTank()).filter(getFluidSlotFilter()))
            .bottomRel(0)
            .rightRel(0)
            .background(GTGuiTextures.SLOT_FLUID_TANK);
    }

    protected abstract Predicate<FluidStack> getFluidSlotFilter();

    @Override
    protected FluidSlot createScreenIOWidget(ModularPanel panel, PanelSyncManager syncManager) {
        return new FluidSlot().size(16)
            .syncHandler(
                new FluidSlotSyncHandler(new RealCapacityFluidTank(getMachineFluidTank(), machine.getRealCapacity()))
                    .controlsAmount(false)
                    .canDrainSlot(false)
                    .canFillSlot(false))
            .alwaysShowFull(false)
            .background()
            .hoverBackground()
            .overlay(GTGuiTextures.PICTURE_GAUGE);
    }

    @Override
    protected boolean supportsFilterScreen() {
        return machine instanceof IFluidLockableMui2;
    }

    @Override
    protected TextWidget<?> createFilterScreenAmountLabel(ModularPanel panel, PanelSyncManager syncManager) {
        return IKey.lang("GT5U.machines.digitaltank.lockfluid.label")
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);
    }

    @Override
    protected TextWidget<?> createFilterScreenAmount(ModularPanel panel, PanelSyncManager syncManager) {
        return IKey.dynamic(() -> {
            if (fluidLockSlotWidget.getFluid() != null) {
                return fluidLockSlotWidget.getFluid()
                    .getLocalizedName();
            }
            return translateToLocal("GT5U.machines.digitaltank.lockfluid.empty");
        })
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE);
    }

    @Override
    protected Widget<?> createFilterSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return fluidLockSlotWidget.syncHandler(new FluidSlotSyncHandler(fluidLockSlotWidget).phantom(true))
            .bottomRel(0)
            .rightRel(0)
            .background(GTGuiTextures.SLOT_FLUID_TANK);
    }

    private record RealCapacityFluidTank(IFluidTank wrappedTank, int realCapacity) implements IFluidTank {

        @Override
        public FluidStack getFluid() {
            return wrappedTank.getFluid();
        }

        @Override
        public int getFluidAmount() {
            return wrappedTank.getFluidAmount();
        }

        @Override
        public int getCapacity() {
            return realCapacity;
        }

        @Override
        public FluidTankInfo getInfo() {
            return wrappedTank.getInfo();
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            return 0;
        }

        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            return null;
        }
    }
}
