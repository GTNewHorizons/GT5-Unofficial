package com.github.technus.tectech.thing.tileEntity;

import com.github.technus.avrClone.AvrCore;
import com.github.technus.avrClone.instructions.InstructionRegistry;
import com.github.technus.avrClone.memory.program.ProgramMemory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class MicroControllerTileEntity extends TileEntity {
    public final AvrCore core;

    public MicroControllerTileEntity(){
        core=new AvrCore(InstructionRegistry.INSTRUCTION_REGISTRY_OP,false);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        core.programCounter=tag.getInteger("programCounter");
        if(core.getProgramMemory()!=null){
            ProgramMemory programMemory=core.getProgramMemory();
            NBTTagCompound program=new NBTTagCompound();
            program.setIntArray("instructions",programMemory.instructions);
            program.setIntArray("param0",programMemory.param0);
            program.setIntArray("param1",programMemory.param1);
            tag.setTag("program",program);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("programCounter",core.programCounter);
        if(tag.hasKey("program")){
            NBTTagCompound program=tag.getCompoundTag("program");
            int[] instructions=program.getIntArray("instructions");
            int[] param0=program.getIntArray("param0");
            int[] param1=program.getIntArray("param1");
            //core.setProgramMemory(new ProgramMemory(instructions,param0,param1,core.getInstructionRegistry()));
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if(core.checkValid()){

        }
    }
}
