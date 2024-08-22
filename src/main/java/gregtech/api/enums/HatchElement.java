package gregtech.api.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.Hatch_Energy;
import gregtech.api.metatileentity.implementations.Hatch_Input;
import gregtech.api.metatileentity.implementations.Hatch_InputBus;
import gregtech.api.metatileentity.implementations.Hatch_Maintenance;
import gregtech.api.metatileentity.implementations.Hatch_Muffler;
import gregtech.api.metatileentity.implementations.Hatch_Output;
import gregtech.api.metatileentity.implementations.Hatch_OutputBus;
import gregtech.api.metatileentity.implementations.MultiBlockBase;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.IGT_HatchAdder;

public enum HatchElement implements IHatchElement<MultiBlockBase> {

    Muffler(MultiBlockBase::addMufflerToMachineList, Hatch_Muffler.class) {

        @Override
        public long count(MultiBlockBase t) {
            return t.mMufflerHatches.size();
        }
    },
    Maintenance(MultiBlockBase::addMaintenanceToMachineList, Hatch_Maintenance.class) {

        @Override
        public long count(MultiBlockBase t) {
            return t.mMaintenanceHatches.size();
        }
    },
    InputHatch(MultiBlockBase::addInputHatchToMachineList, Hatch_Input.class) {

        @Override
        public long count(MultiBlockBase t) {
            return t.mInputHatches.size();
        }
    },
    InputBus(MultiBlockBase::addInputBusToMachineList, Hatch_InputBus.class) {

        @Override
        public long count(MultiBlockBase t) {
            return t.mInputBusses.size();
        }
    },
    OutputHatch(MultiBlockBase::addOutputHatchToMachineList, Hatch_Output.class) {

        @Override
        public long count(MultiBlockBase t) {
            return t.mOutputHatches.size();
        }
    },
    OutputBus(MultiBlockBase::addOutputBusToMachineList, Hatch_OutputBus.class) {

        @Override
        public long count(MultiBlockBase t) {
            return t.mOutputBusses.size();
        }
    },
    Energy(MultiBlockBase::addEnergyInputToMachineList, Hatch_Energy.class) {

        @Override
        public long count(MultiBlockBase t) {
            return t.mEnergyHatches.size();
        }
    },
    Dynamo(MultiBlockBase::addDynamoToMachineList, Hatch_Dynamo.class) {

        @Override
        public long count(MultiBlockBase t) {
            return t.mDynamoHatches.size();
        }
    },
    ExoticEnergy(MultiBlockBase::addExoticEnergyInputToMachineList) {

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return ExoticEnergyInputHelper.getAllClasses();
        }

        @Override
        public long count(MultiBlockBase t) {
            return t.getExoticEnergyHatches()
                .size();
        }
    },;

    private final List<Class<? extends IMetaTileEntity>> mteClasses;
    private final IGT_HatchAdder<MultiBlockBase> adder;

    @SafeVarargs
    HatchElement(IGT_HatchAdder<MultiBlockBase> adder, Class<? extends IMetaTileEntity>... mteClasses) {
        this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        this.adder = adder;
    }

    @Override
    public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
        return mteClasses;
    }

    public IGT_HatchAdder<? super MultiBlockBase> adder() {
        return adder;
    }
}
