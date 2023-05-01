package com.github.technus.tectech.thing.metaTileEntity.single;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.MACHINE_CASINGS_TT;
import static com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_DataReader.READER_OFFLINE;
import static com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_DataReader.READER_ONLINE;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.avrClone.AvrCore;
import com.github.technus.avrClone.instructions.ExecutionEvent;
import com.github.technus.avrClone.instructions.Instruction;
import com.github.technus.avrClone.instructions.InstructionRegistry;
import com.github.technus.avrClone.instructions.exceptions.DebugEvent;
import com.github.technus.avrClone.instructions.exceptions.DelayEvent;
import com.github.technus.avrClone.memory.EepromMemory;
import com.github.technus.avrClone.memory.RemovableMemory;
import com.github.technus.avrClone.memory.program.ProgramMemory;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.avr.SidedRedstone;
import com.github.technus.tectech.util.TT_Utility;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;

public class GT_MetaTileEntity_MicroController extends GT_MetaTileEntity_TieredMachineBlock {

    public static final int OPS_PER_TICK_MAX = 512;

    static {
        Instruction.random = TecTech.RANDOM;
    }

    public AvrCore core;
    private int[] tempData;
    public boolean debugRun;
    private int delay;
    private int maxLoad;

    public static final SidedRedstone sidedRedstone = new SidedRedstone(0x16);

    public GT_MetaTileEntity_MicroController(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "AVR Micro-controller");
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_MicroController(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        TT_Utility.setTier(aTier, this);
        core = new AvrCore();
        core.setUsingImmersiveOperands(false);
        core.setInstructionRegistry(InstructionRegistry.INSTRUCTION_REGISTRY_OP);
        core.setDataMemory(Math.max(64, 1 << aTier), Math.max(64, 1 << aTier));
        core.setCpuRegisters(0x30);
        core.putDataBindings(sidedRedstone);
        maxLoad = Math.min(1 << mTier, OPS_PER_TICK_MAX);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_MicroController(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        NBTTagCompound avr = tag.getCompoundTag("avr");
        debugRun = avr.getBoolean("debugRun");
        delay = avr.getInteger("delay");
        core.active = avr.getBoolean("active");
        core.awoken = (avr.getBoolean("awoken"));
        core.programCounter = avr.getInteger("programCounter");
        if (avr.hasKey("instructions")) {
            InstructionRegistry registry = InstructionRegistry.REGISTRIES.get(avr.getString("instructionRegistry"));
            if (registry != null) {
                core.setProgramMemory(
                        new ProgramMemory(
                                registry,
                                avr.getBoolean("immersive"),
                                avr.getIntArray("instructions"),
                                avr.getIntArray("param0"),
                                avr.getIntArray("param1")));
            }
        }
        if (avr.hasKey("eepromSize")) {
            core.restoreEepromDefinition(EepromMemory.make(avr.getInteger("eepromSize")));
        }
        if (avr.hasKey("dataMemory")) {
            tempData = avr.getIntArray("dataMemory");
        }
        core.checkValid();
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        NBTTagCompound avr = new NBTTagCompound();
        avr.setBoolean("debugRun", debugRun);
        avr.setInteger("delay", delay);
        avr.setBoolean("active", core.active);
        avr.setBoolean("awoken", core.awoken);
        avr.setInteger("programCounter", core.programCounter);
        ProgramMemory programMemory = core.getProgramMemory();
        if (programMemory != null) {
            avr.setIntArray("instructions", programMemory.instructions);
            avr.setIntArray("param0", programMemory.param0);
            avr.setIntArray("param1", programMemory.param1);
            avr.setBoolean("immersive", programMemory.immersiveOperands);
            avr.setString("instructionRegistry", programMemory.registry.toString());
        }
        RemovableMemory<EepromMemory> eeprom = core.getEepromMemory();
        if (eeprom != null) {
            avr.setInteger("eepromSize", eeprom.getDefinition().getSize());
        }
        if (core.dataMemory != null) {
            avr.setIntArray("dataMemory", core.dataMemory);
        }
        tag.setTag("avr", avr);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (tempData != null) {
            // todo discovery of components
            core.dataMemory = tempData;
            tempData = null;
        }
        if (aBaseMetaTileEntity.isActive()) {
            sidedRedstone.preSync(core, aBaseMetaTileEntity);
            core.interruptsHandle();
            if (core.awoken) {
                delay = 0;
                for (int load = 0; load < maxLoad;) {
                    load += core.getInstruction().getCost(core);
                    ExecutionEvent executionEvent = core.cpuCycleForce();
                    if (executionEvent != null) {
                        if (executionEvent.throwable instanceof DelayEvent) {
                            delay = executionEvent.data[0];
                            break;
                        } else if (executionEvent.throwable instanceof DebugEvent) {
                            if (debugRun) {
                                aBaseMetaTileEntity.setActive(false);
                                break;
                            }
                        }
                    }
                }
            } else if (delay > 0) {
                delay--;
                if (delay == 0) {
                    core.awoken = true;
                }
            }
            sidedRedstone.postSync(core, aBaseMetaTileEntity);
        }
        core.active = aBaseMetaTileEntity.isActive();
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
            ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
            ItemStack itemStack) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
            int colorIndex, boolean aActive, boolean aRedstone) {
        if (aBaseMetaTileEntity.getWorld() == null) {
            if (side == facing) {
                return new ITexture[] { MACHINE_CASINGS_TT[mTier][colorIndex + 1],
                        aActive ? READER_ONLINE : READER_OFFLINE };
            }
            return new ITexture[] { MACHINE_CASINGS_TT[mTier][colorIndex + 1] };
        }
        if (side == aBaseMetaTileEntity.getFrontFacing()) {
            return new ITexture[] { MACHINE_CASINGS_TT[mTier][colorIndex + 1],
                    aActive ? READER_ONLINE : READER_OFFLINE };
        } else if (side == facing) {
            return new ITexture[] { MACHINE_CASINGS_TT[mTier][colorIndex + 1],
                    new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) };
        }
        return new ITexture[] { MACHINE_CASINGS_TT[mTier][colorIndex + 1] };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }
}
