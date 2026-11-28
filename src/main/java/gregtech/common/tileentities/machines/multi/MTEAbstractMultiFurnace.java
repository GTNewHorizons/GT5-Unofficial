package gregtech.common.tileentities.machines.multi;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.implementations.MTEMultiBlockECBase;
import gregtech.api.ec.implementations.MultiblockComponentBuilder;

public abstract class MTEAbstractMultiFurnace<T extends MTEAbstractMultiFurnace<T>> extends MTEMultiBlockECBase<T> {

    protected MTEAbstractMultiFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEAbstractMultiFurnace(String aName) {
        super(aName);
    }

    @Override
    protected void configureComponents(@NotNull MultiblockComponentBuilder builder) {
        super.configureComponents(builder);
        builder.enableExtendedPower()
            .enableCoils();
    }
}
