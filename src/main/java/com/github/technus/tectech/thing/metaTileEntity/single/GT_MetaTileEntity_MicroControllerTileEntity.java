package com.github.technus.tectech.thing.metaTileEntity.single;

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
import com.github.technus.tectech.Util;
import com.github.technus.tectech.mechanics.avr.SidedRedstone;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_DataReader.READER_OFFLINE;
import static com.github.technus.tectech.thing.metaTileEntity.single.GT_MetaTileEntity_DataReader.READER_ONLINE;

public class GT_MetaTileEntity_MicroControllerTileEntity extends GT_MetaTileEntity_TieredMachineBlock {
    static {
        Instruction.random= TecTech.RANDOM;
    }
    private AvrCore core;
    private int[] tempData;
    private boolean debugRun;
    private int delay;

    public static final SidedRedstone sidedRedstone=new SidedRedstone(0x1b);

    public GT_MetaTileEntity_MicroControllerTileEntity(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "AVR Micro-controller");
        Util.setTier(aTier,this);
    }

    public GT_MetaTileEntity_MicroControllerTileEntity(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        Util.setTier(aTier,this);
        core=new AvrCore();
        core.setUsingImmersiveOperands(false);
        core.setInstructionRegistry(InstructionRegistry.INSTRUCTION_REGISTRY_OP);
        core.setDataMemory(1<<aTier,1<<aTier);
        core.setCpuRegisters(0x30);
        core.putDataBindings(sidedRedstone);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_MicroControllerTileEntity(mName, mTier, mDescription, mTextures);
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        debugRun=tag.getBoolean("debugRun");
        delay=tag.getInteger("delay");
        core.active =tag.getBoolean("active");
        core.awoken=(tag.getBoolean("awoken"));
        core.programCounter=tag.getInteger("programCounter");
        if(tag.hasKey("instructions")){
            int[] instructions=tag.getIntArray("instructions");
            int[] param0=tag.getIntArray("param0");
            int[] param1=tag.getIntArray("param1");
            core.setProgramMemory(new ProgramMemory(
                    core.getInstructionRegistry(),core.isUsingImmersiveOperands(),
                    instructions,param0,param1));
        }
        if(tag.hasKey("eepromSize")){
            core.setEepromDefinition(EepromMemory.make(tag.getInteger("eepromSize")));
        }
        if(tag.hasKey("dataMemory")){
            tempData=tag.getIntArray("dataMemory");
        }
        core.checkValid();
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        tag.setBoolean("debugRun",debugRun);
        tag.setInteger("delay",delay);
        tag.setBoolean("active",core.active);
        tag.setBoolean("awoken",core.awoken);
        tag.setInteger("programCounter",core.programCounter);
        ProgramMemory programMemory=core.getProgramMemory();
        if(programMemory!=null){
            tag.setIntArray("instructions",programMemory.instructions);
            tag.setIntArray("param0",programMemory.param0);
            tag.setIntArray("param1",programMemory.param1);
        }
        RemovableMemory<EepromMemory> eeprom=core.getEepromMemory();
        if(eeprom!=null){
            tag.setInteger("eepromSize",eeprom.getDefinition().getSize());
        }
        if(core.dataMemory!=null){
            tag.setIntArray("dataMemory",core.dataMemory);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (tempData != null) {
            //todo discovery of components
            core.dataMemory = tempData;
            tempData = null;
        }
        if (core.active) {
            sidedRedstone.preSync(core,aBaseMetaTileEntity);
            core.interruptsHandle();
            if (core.awoken) {
                delay=0;
                for (int i = 0, cycles = Math.min(1 << mTier, 512); i < cycles; i++) {
                    ExecutionEvent executionEvent = core.cpuCycleForce();
                    if (executionEvent != null) {
                        if (executionEvent.throwable instanceof DelayEvent) {
                            delay = executionEvent.data[0];
                            break;
                        } else if (debugRun && executionEvent.throwable instanceof DebugEvent) {
                            core.active = false;
                            break;
                        }
                    }
                }
            }else if(delay>0){
                delay--;
                if(delay==0){
                    core.awoken=true;
                }
            }
            sidedRedstone.postSync(core,aBaseMetaTileEntity);
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if(aBaseMetaTileEntity.getWorld()==null){
            if(aSide==aFacing){
                return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], aActive ? READER_ONLINE : READER_OFFLINE};
            }
            return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1]};
        }
        if(aSide==aBaseMetaTileEntity.getFrontFacing()){
            return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], aActive ? READER_ONLINE : READER_OFFLINE};
        }else if(aSide==aFacing){
            return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT)};
        }
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1]};
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }
}
