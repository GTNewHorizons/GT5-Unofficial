package com.github.technus.tectech.mechanics.anomaly;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.chunkData.ChunkDataHandler;
import com.github.technus.tectech.chunkData.ChunkMetaDataHandler;
import com.github.technus.tectech.loader.network.ChunkDataMessage;
import com.github.technus.tectech.loader.network.NetworkDispatcher;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.atom.dAtomDefinition;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.HashMap;

public class AnomalyHandler implements ChunkMetaDataHandler {
    private static final double MIN_POLLUTION= dAtomDefinition.getSomethingHeavy().getMass()*10000D;

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

    @Override
    public int pushPayloadSpreadPeriod() {
        return 100;
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
            if(amount>MIN_POLLUTION){

            }
        }else {
            double newAmount=old.getDouble(INTENSITY)+amount;
            old.setDouble(INTENSITY,newAmount);
            if(newAmount>MIN_POLLUTION){

            }
        }
    }
}
