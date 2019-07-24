package com.github.technus.tectech.thing.tileEntity;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MicroControllerTileEntity extends TileEntity {
    static {
        Instruction.random= TecTech.RANDOM;
    }
    private final AvrCore core;
    private int[] tempData;
    private boolean debugRun;
    private int delay;

    public MicroControllerTileEntity(){
        core=new AvrCore();
        core.setUsingImmersiveOperands(false);
        core.setInstructionRegistry(InstructionRegistry.INSTRUCTION_REGISTRY_OP);
        core.setDataMemory(1<<getBlockMetadata(),1<<getBlockMetadata());
        core.setCpuRegisters(0x30);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
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
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
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
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (tempData != null) {
            //todo discovery of components
            core.dataMemory = tempData;
            tempData = null;
        }
        if (core.active) {
            core.interruptsHandle();
            if (core.awoken) {
                delay=0;
                for (int i = 0, cycles = Math.min(1 << getBlockMetadata(), 512); i < cycles; i++) {
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
        }
    }
}
