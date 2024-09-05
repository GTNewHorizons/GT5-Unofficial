package gregtech.api.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import gregtech.api.util.IGTHatchAdder;

public enum HatchElement implements IHatchElement<MTEMultiBlockBase> {

    Muffler(MTEMultiBlockBase::addMufflerToMachineList, MTEHatchMuffler.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mMufflerHatches.size();
        }
    },
    Maintenance(MTEMultiBlockBase::addMaintenanceToMachineList, MTEHatchMaintenance.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mMaintenanceHatches.size();
        }
    },
    InputHatch(MTEMultiBlockBase::addInputHatchToMachineList, MTEHatchInput.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mInputHatches.size();
        }
    },
    InputBus(MTEMultiBlockBase::addInputBusToMachineList, MTEHatchInputBus.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mInputBusses.size();
        }
    },
    OutputHatch(MTEMultiBlockBase::addOutputHatchToMachineList, MTEHatchOutput.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mOutputHatches.size();
        }
    },
    OutputBus(MTEMultiBlockBase::addOutputBusToMachineList, MTEHatchOutputBus.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mOutputBusses.size();
        }
    },
    Energy(MTEMultiBlockBase::addEnergyInputToMachineList, MTEHatchEnergy.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mEnergyHatches.size();
        }
    },
    Dynamo(MTEMultiBlockBase::addDynamoToMachineList, MTEHatchDynamo.class) {

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.mDynamoHatches.size();
        }
    },
    ExoticEnergy(MTEMultiBlockBase::addExoticEnergyInputToMachineList) {

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return ExoticEnergyInputHelper.getAllClasses();
        }

        @Override
        public long count(MTEMultiBlockBase t) {
            return t.getExoticEnergyHatches()
                .size();
        }
    },;

    private final List<Class<? extends IMetaTileEntity>> mteClasses;
    private final IGTHatchAdder<MTEMultiBlockBase> adder;

    @SafeVarargs
    HatchElement(IGTHatchAdder<MTEMultiBlockBase> adder, Class<? extends IMetaTileEntity>... mteClasses) {
        this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        this.adder = adder;
    }

    @Override
    public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
        return mteClasses;
    }

    public IGTHatchAdder<? super MTEMultiBlockBase> adder() {
        return adder;
    }
}
