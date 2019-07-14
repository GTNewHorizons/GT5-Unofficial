package com.github.technus.tectech.mechanics.anomaly;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.network.ChunkDataMessage;
import com.github.technus.tectech.loader.network.NetworkDispatcher;
import com.github.technus.tectech.chunkData.ChunkDataHandler;
import com.github.technus.tectech.chunkData.ChunkMetaDataHandler;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.HashMap;

public class AnomalyHandler implements ChunkMetaDataHandler {
    private static final String INTENSITY="intensity";

    @Override
    public String getTagName() {
        return "anomaly";
    }

    @Override
    public void mergeData(NBTTagCompound target, NBTTagCompound loadedData) {
        double intensity=target.getDouble(INTENSITY)+loadedData.getDouble(INTENSITY);
        target.setDouble(INTENSITY,intensity);
    }

    @Override
    public NBTTagCompound createData() {
        return new NBTTagCompound();
    }

    @Override
    public void tickServer(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.ServerTickEvent event) {
        
    }

    @Override
    public void tickClient(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.ClientTickEvent aEvent) {

    }

    @Override
    public void requestData(ChunkEvent.Load aEvent) {
        NetworkDispatcher.INSTANCE.sendToServer(new ChunkDataMessage.ChunkDataQuery(aEvent,this));
    }

    @Override
    public void pushData(int world, ChunkCoordIntPair chunk) {
        NetworkDispatcher.INSTANCE.sendToDimension(new ChunkDataMessage.ChunkDataData(world,chunk,this),world);
    }

    public void addAnomaly(IGregTechTileEntity iGregTechTileEntity, double amount) {
        if(iGregTechTileEntity.isServerSide()) {
            World w = iGregTechTileEntity.getWorld();
            addAnomaly(w.provider.dimensionId,
                    w.getChunkFromBlockCoords(
                            iGregTechTileEntity.getXCoord(),
                            iGregTechTileEntity.getZCoord())
                            .getChunkCoordIntPair(),
                    amount);
        }
    }

    public void addAnomaly(int world, ChunkCoordIntPair chunk,double amount) {
        NBTTagCompound old=TecTech.chunkDataHandler.getChunkData(this,world,chunk);
        if(old==null){
            NBTTagCompound data=new NBTTagCompound();
            data.setDouble(INTENSITY,amount);
            TecTech.chunkDataHandler.putChunkData(this,world,chunk,data);
            //todo update client on threshold reach
        }else {
            old.setDouble(INTENSITY,old.getDouble(INTENSITY)+amount);
            //todo update client on threshold change
        }
    }
}
