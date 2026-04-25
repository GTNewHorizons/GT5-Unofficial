package gregtech.common.gui.modularui.widget.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.value.sync.IValueSyncHandler;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.common.gui.modularui.widget.EnumCycleButtonWidget;
import gregtech.common.gui.modularui.widget.WidgetConfigurator;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class SettingsPanelBuilder {

    private final List<ISettingRow<?>> rows = new ArrayList<>();

    private Function<SettingsPanel, Integer> dividerPosition;
    private String syncName = "settings-panel";

    public SettingsPanelBuilder setDividerPosition(int dividerPosition) {
        this.dividerPosition = ignored -> dividerPosition;
        return this;
    }

    public SettingsPanelBuilder setDividerPosition(Function<SettingsPanel, Integer> dividerPosition) {
        this.dividerPosition = dividerPosition;
        return this;
    }

    public SettingsPanelBuilder setSyncName(String syncName) {
        this.syncName = syncName;
        return this;
    }

    public SettingsPanelBuilder addHeader(IKey header) {
        rows.add(new HeaderSettingRow(header));

        return this;
    }

    public <S extends SyncHandler & IValueSyncHandler<T>, T> SettingsPanelBuilder addReadout(IKey label, S value,
        Function<T, IKey> format) {
        rows.add(new ReadoutSettingRow<>(label, value, format));

        return this;
    }

    public SettingsPanelBuilder addIntEditor(IKey label, IntSupplier getter, IntConsumer setter) {
        addIntEditor(label, getter, setter, null);

        return this;
    }

    public SettingsPanelBuilder addIntEditor(IKey label, IntSupplier getter, IntConsumer setter,
        WidgetConfigurator<TextFieldWidget> configure) {
        addTextField(label, (panel, syncManager, textField) -> {
            textField.value(new IntSyncValue(getter, setter));
            textField.setFormatAsInteger(true);
            textField.setNumbers();
            if (configure != null) configure.configure(panel, syncManager, textField);
        });

        return this;
    }

    public SettingsPanelBuilder addLongEditor(IKey label, LongSupplier getter, LongConsumer setter) {
        addLongEditor(label, getter, setter, null);

        return this;
    }

    public SettingsPanelBuilder addLongEditor(IKey label, LongSupplier getter, LongConsumer setter,
        WidgetConfigurator<TextFieldWidget> configure) {
        addTextField(label, (panel, syncManager, textField) -> {
            textField.value(new LongSyncValue(getter, setter));
            textField.setFormatAsInteger(true);
            textField.setNumbers();
            if (configure != null) configure.configure(panel, syncManager, textField);
        });

        return this;
    }

    public SettingsPanelBuilder addDoubleEditor(IKey label, DoubleSupplier getter, DoubleConsumer setter) {
        addDoubleEditor(label, getter, setter, null);

        return this;
    }

    public SettingsPanelBuilder addDoubleEditor(IKey label, DoubleSupplier getter, DoubleConsumer setter,
        WidgetConfigurator<TextFieldWidget> configure) {
        addTextField(label, (panel, syncManager, textField) -> {
            textField.value(new DoubleSyncValue(getter, setter) {

                @Override
                public String getStringValue() {
                    return NumberFormatUtil.formatNumber(this.getValue());
                }
            });
            textField.setNumbers();
            if (configure != null) configure.configure(panel, syncManager, textField);
        });

        return this;
    }

    public SettingsPanelBuilder addStringEditor(IKey label, Supplier<String> getter, Consumer<String> setter) {
        addStringEditor(label, getter, setter, null);

        return this;
    }

    public SettingsPanelBuilder addStringEditor(IKey label, Supplier<String> getter, Consumer<String> setter,
        WidgetConfigurator<TextFieldWidget> configure) {
        addTextField(label, (panel, syncManager, textField) -> {
            textField.value(new StringSyncValue(getter, setter));
            if (configure != null) configure.configure(panel, syncManager, textField);
        });

        return this;
    }

    public SettingsPanelBuilder addTextField(IKey label, WidgetConfigurator<TextFieldWidget> configure) {
        rows.add(
            new TextFieldSettingRow(
                label,
                (panel, syncManager, textField) -> {
                    if (configure != null) configure.configure(panel, syncManager, textField);
                }));

        return this;
    }

    public <E extends Enum<E>> SettingsPanelBuilder addEnumCycleButton(IKey label, Class<E> clazz, Supplier<E> getter,
        Consumer<E> setter) {
        rows.add(new EnumCycleSettingRow<>(label, clazz, getter, setter, null));

        return this;
    }

    public <E extends Enum<E>> SettingsPanelBuilder addEnumCycleButton(IKey label, Class<E> clazz, Supplier<E> getter,
        Consumer<E> setter, WidgetConfigurator<EnumCycleButtonWidget<E>> configure) {
        rows.add(new EnumCycleSettingRow<>(label, clazz, getter, setter, configure));

        return this;
    }

    public SettingsPanelBuilder addPhantomFluidSlot(IKey label, Supplier<Fluid> getter, Consumer<Fluid> setter) {
        addPhantomFluidSlot(label, getter, setter, null);

        return this;
    }

    public SettingsPanelBuilder addPhantomFluidSlot(IKey label, Supplier<Fluid> getter, Consumer<Fluid> setter,
        WidgetConfigurator<FluidSlot> configure) {
        IFluidTank fakeTank = new IFluidTank() {

            @Override
            public FluidStack getFluid() {
                Fluid fluid = getter.get();

                return fluid == null ? null : new FluidStack(fluid, 1000);
            }

            @Override
            public int getFluidAmount() {
                Fluid fluid = getter.get();

                return fluid == null ? 0 : 1000;
            }

            @Override
            public int getCapacity() {
                return 1000;
            }

            @Override
            public FluidTankInfo getInfo() {
                return new FluidTankInfo(getFluid(), getCapacity());
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {
                if (doFill) setter.accept(resource == null ? null : resource.getFluid());

                return resource == null ? 0 : resource.amount;
            }

            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                if (doDrain) setter.accept(null);

                return null;
            }
        };

        rows.add(
            new PhantomFluidSettingRow(
                label,
                configure,
                new FluidSlotSyncHandler(fakeTank).phantom(true)
                    .controlsAmount(false)));

        return this;
    }

    public SettingsPanelBuilder addPhantomFluidSlot(IKey label, FluidSlotSyncHandler value,
        WidgetConfigurator<FluidSlot> configure) {
        rows.add(new PhantomFluidSettingRow(label, configure, value));

        return this;
    }

    public SettingsPanel build(ModularPanel panel, PanelSyncManager syncManager) {
        return new SettingsPanel(panel, syncManager, rows, dividerPosition, syncName);
    }
}
