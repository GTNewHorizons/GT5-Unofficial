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
import com.cleanroommc.modularui.api.value.IBoolValue;
import com.cleanroommc.modularui.api.value.IIntValue;
import com.cleanroommc.modularui.api.value.IStringValue;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.DAM;
import com.cleanroommc.modularui.utils.MathUtils;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.common.gui.modularui.widget.EnumCycleButtonWidget;
import gregtech.common.gui.modularui.widget.WidgetConfigurator;

public class SettingsPanelBuilder {

    private final List<ISettingRow<?>> rows = new ArrayList<>();
    private String syncName = "settingsPanel";

    public SettingsPanelBuilder setSyncName(String syncName) {
        this.syncName = syncName;
        return this;
    }

    public <S extends ValueSyncHandler<T, ?>, T> SettingsPanelBuilder addReadout(IKey label, S value,
        Function<T, IKey> format) {
        rows.add(new ReadoutSettingRow<>(label, value, format));

        return this;
    }

    public SettingsPanelBuilder addIntEditor(IKey label, IntSupplier getter, IntConsumer setter,
        MathUtils.UnaryIntOperator validator) {
        return addIntEditor(label, getter, setter, validator, null);
    }

    public SettingsPanelBuilder addIntEditor(IKey label, IntSupplier getter, IntConsumer setter,
        MathUtils.UnaryIntOperator validator, WidgetConfigurator<TextFieldWidget> configure) {
        return addIntEditor(label, new IntSyncValue(getter, setter).allowC2S(), validator, configure);
    }

    public SettingsPanelBuilder addIntEditor(IKey label, IStringValue<Integer> value,
        MathUtils.UnaryIntOperator validator) {
        return addIntEditor(label, value, validator, null);
    }

    public SettingsPanelBuilder addIntEditor(IKey label, IStringValue<Integer> value,
        MathUtils.UnaryIntOperator validator, WidgetConfigurator<TextFieldWidget> configure) {
        return addTextField(label, value, (panel, syncManager, widget) -> {
            widget.numbersInt(validator);
            widget.formatAsInteger(true);
            if (configure != null) configure.configure(panel, syncManager, widget);
        });
    }

    public SettingsPanelBuilder addLongEditor(IKey label, LongSupplier getter, LongConsumer setter,
        MathUtils.UnaryLongOperator validator) {
        return addLongEditor(label, getter, setter, validator, null);
    }

    public SettingsPanelBuilder addLongEditor(IKey label, LongSupplier getter, LongConsumer setter,
        MathUtils.UnaryLongOperator validator, WidgetConfigurator<TextFieldWidget> configure) {
        return addLongEditor(label, new LongSyncValue(getter, setter).allowC2S(), validator, configure);
    }

    public SettingsPanelBuilder addLongEditor(IKey label, IStringValue<Long> value,
        MathUtils.UnaryLongOperator validator) {

        return addLongEditor(label, value, validator, null);
    }

    public SettingsPanelBuilder addLongEditor(IKey label, IStringValue<Long> value,
        MathUtils.UnaryLongOperator validator, WidgetConfigurator<TextFieldWidget> configure) {
        return addTextField(label, value, (panel, syncManager, widget) -> {
            widget.numbersLong(validator);
            widget.formatAsInteger(true);
            if (configure != null) configure.configure(panel, syncManager, widget);
        });
    }

    public SettingsPanelBuilder addDoubleEditor(IKey label, DoubleSupplier getter, DoubleConsumer setter,
        DAM.UnaryDoubleOperator validator) {
        return addDoubleEditor(label, getter, setter, validator, null);
    }

    public SettingsPanelBuilder addDoubleEditor(IKey label, DoubleSupplier getter, DoubleConsumer setter,
        DAM.UnaryDoubleOperator validator, WidgetConfigurator<TextFieldWidget> configure) {
        return addDoubleEditor(label, new DoubleSyncValue(getter, setter).allowC2S(), validator, configure);
    }

    public SettingsPanelBuilder addDoubleEditor(IKey label, IStringValue<Double> value,
        DAM.UnaryDoubleOperator validator) {
        return addDoubleEditor(label, value, validator, null);
    }

    public SettingsPanelBuilder addDoubleEditor(IKey label, IStringValue<Double> value,
        DAM.UnaryDoubleOperator validator, WidgetConfigurator<TextFieldWidget> configure) {
        return addTextField(label, value, (panel, syncManager, widget) -> {
            widget.numbersDouble(validator);
            if (configure != null) configure.configure(panel, syncManager, widget);
        });
    }

    public SettingsPanelBuilder addStringEditor(IKey label, Supplier<String> getter, Consumer<String> setter) {
        return addStringEditor(label, getter, setter, null);
    }

    public SettingsPanelBuilder addStringEditor(IKey label, Supplier<String> getter, Consumer<String> setter,
        WidgetConfigurator<TextFieldWidget> configure) {
        return addStringEditor(label, new StringSyncValue(getter, setter).allowC2S(), configure);
    }

    public SettingsPanelBuilder addStringEditor(IKey label, IStringValue<String> value) {
        return addStringEditor(label, value, null);
    }

    public SettingsPanelBuilder addStringEditor(IKey label, IStringValue<String> value,
        WidgetConfigurator<TextFieldWidget> configure) {
        return addTextField(label, value, configure);
    }

    private SettingsPanelBuilder addTextField(IKey label, IStringValue<?> value,
        WidgetConfigurator<TextFieldWidget> configure) {
        rows.add(new TextFieldSettingRow(label, value, configure));

        return this;
    }

    public <E extends Enum<E>> SettingsPanelBuilder addEnumCycleButton(IKey label, Class<E> clazz, Supplier<E> getter,
        Consumer<E> setter) {
        return addEnumCycleButton(label, clazz, getter, setter, null);
    }

    public <E extends Enum<E>> SettingsPanelBuilder addEnumCycleButton(IKey label, Class<E> clazz, Supplier<E> getter,
        Consumer<E> setter, WidgetConfigurator<EnumCycleButtonWidget<E>> configure) {
        return addEnumCycleButton(label, clazz, new EnumSyncValue<>(clazz, getter, setter).allowC2S(), configure);
    }

    public <E extends Enum<E>> SettingsPanelBuilder addEnumCycleButton(IKey label, Class<E> clazz, IIntValue<?> value) {
        return addEnumCycleButton(label, clazz, value, null);
    }

    public <E extends Enum<E>> SettingsPanelBuilder addEnumCycleButton(IKey label, Class<E> clazz, IIntValue<?> value,
        WidgetConfigurator<EnumCycleButtonWidget<E>> configure) {
        rows.add(new EnumCycleSettingRow<>(label, clazz, value, configure));

        return this;
    }

    public SettingsPanelBuilder addPhantomFluidSlot(IKey label, Supplier<Fluid> getter, Consumer<Fluid> setter) {
        return addPhantomFluidSlot(label, getter, setter, null);
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

        return addPhantomFluidSlot(
            label,
            new FluidSlotSyncHandler(fakeTank).phantom(true)
                .controlsAmount(false),
            configure);
    }

    public SettingsPanelBuilder addPhantomFluidSlot(IKey label, FluidSlotSyncHandler value) {
        return addPhantomFluidSlot(label, value, null);
    }

    public SettingsPanelBuilder addPhantomFluidSlot(IKey label, FluidSlotSyncHandler value,
        WidgetConfigurator<FluidSlot> configure) {
        rows.add(new PhantomFluidSettingRow(label, value, configure));

        return this;
    }

    public SettingsPanelBuilder addToggleButton(IKey label, IBoolValue<?> value) {
        rows.add(new ToggleButtonSettingRow(label, value, null));

        return this;
    }

    public SettingsPanelBuilder addToggleButton(IKey label, IBoolValue<?> value,
        WidgetConfigurator<ToggleButton> configure) {
        rows.add(new ToggleButtonSettingRow(label, value, configure));

        return this;
    }

    public SettingsPanelBuilder addButton(IKey label) {
        return addButton(label, null);
    }

    public SettingsPanelBuilder addButton(IKey label, WidgetConfigurator<ButtonWidget<?>> configure) {
        rows.add(new ButtonSettingRow(label, configure));
        return this;
    }

    public SettingsPanel build(ModularPanel panel, PanelSyncManager syncManager, int maxHeight) {
        return new SettingsPanel(panel, syncManager, rows, syncName, maxHeight);
    }
}
