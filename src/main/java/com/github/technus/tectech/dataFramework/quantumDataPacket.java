package com.github.technus.tectech.dataFramework;

import com.github.technus.tectech.vec3pos;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Tec on 05.04.2017.
 */
public class quantumDataPacket {
    public static byte maxHistory =64;

    public long computation=0;
    public Set<vec3pos> trace=new TreeSet<>();

    public quantumDataPacket(vec3pos pos,long computation){
        this.computation=computation;
        trace.add(pos);
    }

    public quantumDataPacket(quantumDataPacket q,long computation){
        this.computation=computation;
        trace.addAll(q.trace);
    }

    public quantumDataPacket(NBTTagCompound nbt){
        computation=nbt.getLong("qComputation");
        for(int i=0;i<nbt.getByte("qHistory");i++){
            trace.add(new vec3pos(
                    nbt.getInteger("qX"+i),
                    nbt.getShort("qY"+i),
                    nbt.getInteger("qZ"+i)
            ));
        }
    }

    public NBTTagCompound toNbt(){
        NBTTagCompound nbt=new NBTTagCompound();
        nbt.setLong("qComputation",computation);
        nbt.setByte("qHistory", (byte)trace.size());
        int i=0;
        for(vec3pos v:trace){
            nbt.setInteger("qX"+i,v.x);
            nbt.setShort("qY"+i,v.y);
            nbt.setInteger("qZ"+i,v.z);
            i++;
        }
        return nbt;
    }

    public boolean contains(vec3pos v){
        return trace.contains(v);
    }

    public boolean check(){
        return trace.size()<=maxHistory;
    }

    public quantumDataPacket unifyTraceWith(quantumDataPacket p){
        trace.addAll(p.trace);
        return check()?this:null;
    }

    public quantumDataPacket unifyPacketWith(quantumDataPacket p){
        computation+=p.computation;
        trace.addAll(p.trace);
        return check()?this:null;
    }

    public long computationIfNotContained(vec3pos pos){
        if(trace.contains(pos))return 0;
        return computation;
    }
}
