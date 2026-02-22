package gregtech.api.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEHatchMuffler;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.common.tileentities.machines.multi.purification.MTEHatchLensHousing;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;

public enum HatchElement implements IHatchElement<MTEMultiBlockBase> {

    Muffler("GT5U.MBTT.MufflerHatch", MTEMultiBlockBase::addMufflerToMachineList, MTEHatchMuffler.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mMufflerHatches.size();
        }
    },
    Maintenance("GT5U.MBTT.MaintenanceHatch", MTEMultiBlockBase::addMaintenanceToMachineList,
        MTEHatchMaintenance.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mMaintenanceHatches.size();
        }
    },
    InputHatch("GT5U.MBTT.InputHatch", MTEMultiBlockBase::addInputHatchToMachineList, MTEHatchInput.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mInputHatches.size();
        }
    },
    InputBus("GT5U.MBTT.InputBus", MTEMultiBlockBase::addInputBusToMachineList, MTEHatchInputBus.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mInputBusses.size();
        }

        @Override
        public List<Class<? extends IMetaTileEntity>> mteBlacklist() {
            return ImmutableList.of(MTEHatchLensHousing.class, MTEHatchSteamBusInput.class);
        }
    },
    OutputHatch("GT5U.MBTT.OutputHatch", MTEMultiBlockBase::addOutputHatchToMachineList, MTEHatchOutput.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mOutputHatches.size();
        }
    },
    OutputBus("GT5U.MBTT.OutputBus", MTEMultiBlockBase::addOutputBusToMachineList, MTEHatchOutputBus.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mOutputBusses.size();
        }

        @Override
        public List<Class<? extends IMetaTileEntity>> mteBlacklist() {
            return ImmutableList.of(MTEHatchSteamBusOutput.class);
        }
    },
    Energy("GT5U.MBTT.EnergyHatch", MTEMultiBlockBase::addEnergyInputToMachineList, MTEHatchEnergy.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mEnergyHatches.size();
        }
    },
    Dynamo("GT5U.MBTT.DynamoHatch", MTEMultiBlockBase::addDynamoToMachineList, MTEHatchDynamo.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mDynamoHatches.size();
        }
    },
    ExoticEnergy("GT5U.MBTT.MultiampEnergyHatch", MTEMultiBlockBase::addExoticEnergyInputToMachineList) {

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return ExoticEnergyInputHelper.getAllClasses();
        }

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.getExoticEnergyHatches()
                .size();
        }
    },
    MultiAmpEnergy("GT5U.MBTT.MultiampEnergyHatch", MTEMultiBlockBase::addMultiAmpEnergyInputToMachineList) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.getExoticEnergyHatches()
                .size();
        }
    },
    ExoticDynamo("GT5U.MBTT.ExoticEnergyDynamo", MTEMultiBlockBase::addExoticDynamoToMachineList) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.getExoticDynamoHatches()
                .size();
        }
    };

    private final String name;
    private final List<Class<? extends IMetaTileEntity>> mteClasses;
    private final IGTHatchAdder<MTEMultiBlockBase> adder;

    @SafeVarargs
    HatchElement(String name, IGTHatchAdder<MTEMultiBlockBase> adder, Class<? extends IMetaTileEntity>... mteClasses) {
        this.name = name;
        this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        this.adder = adder;
    }

    @Override
    public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
        return mteClasses;
    }

    @Override
    public String getDisplayName() {
        return GTUtility.translate(name);
    }

    public IGTHatchAdder<? super MTEMultiBlockBase> adder() {
        return adder;
    }
}
