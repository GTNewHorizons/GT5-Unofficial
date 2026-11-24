package gregtech.api.ec.component.multiblock;

import gregtech.common.tileentities.machines.multi.beamcrafting.MTEHatchAdvancedOutputBeamline;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchElementalDataOrbHolder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import gtnhlanth.common.hatch.MTEBusInputFocus;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

public final class CustomHatchKeys {

    public static final CustomHatchKey<MTEHatchCustomFluidBase> CRYOTHEUM = CustomHatchKey
        .builder("cryotheum", MTEHatchCustomFluidBase.class)
        .matcher(hatch -> hatch.mLockedFluid == TFFluids.fluidCryotheum)
        .build();
    public static final CustomHatchKey<MTEHatchCustomFluidBase> PYROTHEUM = CustomHatchKey
        .builder("pyrotheum", MTEHatchCustomFluidBase.class)
        .matcher(hatch -> hatch.mLockedFluid == TFFluids.fluidPyrotheum)
        .build();
    public static final CustomHatchKey<MTEHatchInputBeamline> BEAMLINE_INPUT = CustomHatchKey
        .builder("beamline_input", MTEHatchInputBeamline.class)
        .build();
    public static final CustomHatchKey<MTEHatchOutputBeamline> BEAMLINE_OUTPUT = CustomHatchKey
        .builder("beamline_output", MTEHatchOutputBeamline.class)
        .matcher(hatch -> !(hatch instanceof MTEHatchAdvancedOutputBeamline))
        .build();
    public static final CustomHatchKey<MTEBusInputFocus> INPUT_FOCUS = CustomHatchKey
        .builder("input_focus", MTEBusInputFocus.class)
        .build();
    public static final CustomHatchKey<MTEHatchElementalDataOrbHolder> ELEMENTAL_DATA_ORB_HOLDERS = CustomHatchKey
        .builder("elemental_data_orb_holder", MTEHatchElementalDataOrbHolder.class)
        .build();
}
