package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Function;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;

import gregtech.common.gui.modularui.widget.WidgetConfigurator;
import gregtech.common.gui.modularui.widget.settings.SettingsPanelBuilder;

public class FluidParameter extends Parameter<Fluid, FluidSlotSyncHandler> {

    public FluidParameter(Fluid value, String langKey, String nbtKey, Object... langArgs) {
        super(value, langKey, nbtKey, langArgs);
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {
        if (this.getValue() != null) tag.setString(
            this.getNbtKey(),
            this.getValue()
                .getName());
        else tag.removeTag(this.getNbtKey());
    }

    @Override
    public void loadNBT(NBTTagCompound tag) {
        if (!tag.hasKey(this.getNbtKey())) this.setValue(null);
        else this.setValue(FluidRegistry.getFluid(tag.getString(this.getNbtKey())));
    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {
        super.saveToParameterCard(tag);
        tag.setString("type", "fluid");

        Fluid fluid = this.getValue();
        tag.setString("value", fluid == null ? "" : fluid.getName());
        tag.setString("displayName", fluid == null ? "" : fluid.getLocalizedName());
    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {
        String fluidName = tag.getString("value");
        this.setValue(fluidName.isEmpty() ? null : FluidRegistry.getFluid(fluidName));
    }

    @Override
    protected FluidSlotSyncHandler createSyncHandler() {
        return new FluidSlotSyncHandler(new IFluidTank() {

            @Override
            public FluidStack getFluid() {
                Fluid fluid = FluidParameter.this.getValue();
                return fluid == null ? null : new FluidStack(fluid, 1);
            }

            @Override
            public int getFluidAmount() {
                return 1;
            }

            @Override
            public int getCapacity() {
                return 1;
            }

            @Override
            public FluidTankInfo getInfo() {
                return new FluidTankInfo(this);
            }

            @Override
            public int fill(FluidStack resource, boolean doFill) {
                setValue(resource.getFluid());
                return 0;
            }

            @Override
            public FluidStack drain(int maxDrain, boolean doDrain) {
                setValue(null);
                return null;
            }
        }).phantom(true)
            .controlsAmount(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addToSettingsPanel(SettingsPanelBuilder builder, IKey label, WidgetConfigurator<?> configure,
        String prefix, Function<Parameter<?, ?>, WidgetConfigurator<?>> configurator) {
        builder.addPhantomFluidSlot(label, this.getSyncHandler(), (WidgetConfigurator<FluidSlot>) configure);
    }
}
