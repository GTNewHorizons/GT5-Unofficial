package com.github.technus.tectech.mechanics.avr;

import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.avrClone.AvrCore;
import com.github.technus.avrClone.registerPackages.IInterrupt;
import com.github.technus.avrClone.registerPackages.IRegister;
import com.github.technus.avrClone.registerPackages.IRegisterBit;
import com.github.technus.avrClone.registerPackages.RegisterPackageSync;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class SidedRedstone extends RegisterPackageSync<IGregTechTileEntity, SidedRedstone> {

    public static final RSINT RSINT = new RSINT();

    public SidedRedstone(int offset) {
        super(offset, Register.values().length);
        addRegisters(Register.values());
        addBits(RegisterBitsPCMSK.values());
        addBits(RegisterBitsPCFR.values());
        addBits(RegisterBitsPCINT.values());
        addBits(RegisterBitsPNEW.values());
        addBits(RegisterBitsPOLD.values());
        addInterrupts(RSINT);
    }

    @Override
    public void preSync(AvrCore core, IGregTechTileEntity iGregTechTileEntity) {
        int addr = this.getOffset();
        int sides = 0;
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            int val = iGregTechTileEntity.getInternalInputRedstoneSignal(side);
            sides |= (val > 0 ? 1 : 0) << side.ordinal();
            core.setDataValue(addr++, iGregTechTileEntity.getInputRedstoneSignal(side));
            core.setDataValue(addr++, val);
            addr++;
        }
        int sidesOld = core.getDataValue(Register.PNEW.getAddress(this));
        core.setDataValue(Register.POLD.getAddress(this), sidesOld);
        core.setDataValue(Register.PNEW.getAddress(this), sides);

        if (core.getInterruptEnable()) {
            int pcint = core.getDataValue(Register.PCINT.getAddress(this));
            int changesDetected = 0;
            switch (pcint & 0b1100) { // PCISC1 PCISC0
                case 0b0000: // low
                    changesDetected = ~sides & core.getDataValue(Register.PCMSK.getAddress(this));
                    break;
                case 0b0100: // any
                    changesDetected = (sides ^ sidesOld) & core.getDataValue(Register.PCMSK.getAddress(this));
                    break;
                case 0b1000: // falling
                    changesDetected = ~sides & sidesOld & core.getDataValue(Register.PCMSK.getAddress(this));
                    break;
                case 0b1100: // rising
                    changesDetected = sides & ~sidesOld & core.getDataValue(Register.PCMSK.getAddress(this));
                    break;
            }

            core.setDataValue(
                    Register.PCFR.getAddress(this),
                    core.getDataValue(Register.PCFR.getAddress(this) | changesDetected));

            if (changesDetected > 0) {
                if (core.getDataBitsOr(Register.PCINT.getAddress(this), RegisterBitsPCINT.PCEN.mask)) {
                    core.setDataBits(Register.PCINT.getAddress(this), RegisterBitsPCINT.PCIF.mask);
                }
            }
        }
    }

    @Override
    public void postSync(AvrCore core, IGregTechTileEntity iGregTechTileEntity) {
        int addr = this.getOffset();
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            iGregTechTileEntity.setOutputRedstoneSignal(side, (byte) core.getDataValue(addr)); // allows edge detection
                                                                                               // hack?
            addr += 3;
        }
    }

    public enum Register implements IRegister<SidedRedstone> {

        PIN0,
        PINT0,
        PORT0,
        PIN1,
        PINT1,
        PORT1,
        PIN2,
        PINT2,
        PORT2,
        PIN3,
        PINT3,
        PORT3,
        PIN4,
        PINT4,
        PORT4,
        PIN5,
        PINT5,
        PORT5,
        PCMSK,
        PCFR,
        PCINT,
        PNEW,
        POLD;

        public final int relativeOffset;

        Register() {
            this.relativeOffset = ordinal();
        }

        @Override
        public int getAddress(SidedRedstone registerPackage) {
            return registerPackage.getOffset() + relativeOffset;
        }
    }

    public enum RegisterBitsPCMSK implements IRegisterBit<SidedRedstone> {

        PCINT0,
        PCINT1,
        PCINT2,
        PCINT3,
        PCINT4,
        PCINT5;

        private final int bit, mask;

        RegisterBitsPCMSK() {
            bit = ordinal();
            mask = 1 << bit;
        }

        @Override
        public int getBitPosition() {
            return bit;
        }

        @Override
        public int getBitMask() {
            return mask;
        }

        @Override
        public int getOffset(SidedRedstone registerPackage) {
            return 18;
        }
    }

    public enum RegisterBitsPCFR implements IRegisterBit<SidedRedstone> {

        PCF0,
        PCF1,
        PCF2,
        PCF3,
        PCF4,
        PCF5;

        private final int bit, mask;

        RegisterBitsPCFR() {
            bit = ordinal();
            mask = 1 << bit;
        }

        @Override
        public int getBitPosition() {
            return bit;
        }

        @Override
        public int getBitMask() {
            return mask;
        }

        @Override
        public int getOffset(SidedRedstone registerPackage) {
            return 19;
        }
    }

    public enum RegisterBitsPCINT implements IRegisterBit<SidedRedstone> {

        PCIF,
        PCEN,
        PCISC0,
        PCISC1;

        private final int bit, mask;

        RegisterBitsPCINT() {
            bit = ordinal();
            mask = 1 << bit;
        }

        @Override
        public int getBitPosition() {
            return bit;
        }

        @Override
        public int getBitMask() {
            return mask;
        }

        @Override
        public int getOffset(SidedRedstone registerPackage) {
            return 20;
        }
    }

    public enum RegisterBitsPNEW implements IRegisterBit<SidedRedstone> {

        PNEW0,
        PNEW1,
        PNEW2,
        PNEW3,
        PNEW4,
        PNEW5;

        private final int bit, mask;

        RegisterBitsPNEW() {
            bit = ordinal();
            mask = 1 << bit;
        }

        @Override
        public int getBitPosition() {
            return bit;
        }

        @Override
        public int getBitMask() {
            return mask;
        }

        @Override
        public int getOffset(SidedRedstone registerPackage) {
            return 21;
        }
    }

    public enum RegisterBitsPOLD implements IRegisterBit<SidedRedstone> {

        POLD0,
        POLD1,
        POLD2,
        POLD3,
        POLD4,
        POLD5;

        private final int bit, mask;

        RegisterBitsPOLD() {
            bit = ordinal();
            mask = 1 << bit;
        }

        @Override
        public int getBitPosition() {
            return bit;
        }

        @Override
        public int getBitMask() {
            return mask;
        }

        @Override
        public int getOffset(SidedRedstone registerPackage) {
            return 22;
        }
    }

    public static class RSINT implements IInterrupt<SidedRedstone> {

        @Override
        public int getVector() {
            return 1;
        }

        @Override
        public boolean getTrigger(AvrCore core, SidedRedstone registerPackage) {
            return (core.getDataValue(Register.PCINT.getAddress(registerPackage)) & 1) == 1;
        }

        @Override
        public void setTrigger(AvrCore core, SidedRedstone registerPackage, boolean value) {
            int val = core.getDataValue(Register.PCINT.getAddress(registerPackage));
            core.setDataValue(
                    Register.PCINT.getAddress(registerPackage),
                    value ? val | RegisterBitsPCINT.PCIF.mask : val & ~RegisterBitsPCINT.PCIF.mask);
        }

        @Override
        public String name() {
            return "RSINT";
        }
    }
}
