package com.github.technus.tectech.mechanics.anomaly;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.chunkData.ChunkDataHandler;
import com.github.technus.tectech.chunkData.IChunkMetaDataHandler;
import com.github.technus.tectech.loader.network.ChunkDataMessage;
import com.github.technus.tectech.loader.network.NetworkDispatcher;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.atom.dAtomDefinition;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import crazypants.util.BaublesUtil;
import crazypants.util.GalacticraftUtil;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

public class AnomalyHandler implements IChunkMetaDataHandler {
    private static final double SWAP_THRESHOLD = dAtomDefinition.getSomethingHeavy().getMass() * 10000D;
    private static final double PER_PARTICLE=SWAP_THRESHOLD/32;
    private static final String INTENSITY = "intensity";
    private static final int MEAN_DELAY =50;

    @Override
    public String getTagName() {
        return "anomaly";
    }

    @Override
    public void mergeData(NBTTagCompound target, NBTTagCompound loadedData) {
        double intensity = target.getDouble(INTENSITY) + loadedData.getDouble(INTENSITY);
        target.setDouble(INTENSITY, intensity);
    }

    @Override
    public NBTTagCompound createData() {
        return new NBTTagCompound();
    }

    @Override
    public void tickWorld(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.WorldTickEvent aEvent) {
        if (TecTech.RANDOM.nextInt(MEAN_DELAY) == 0) {
            int dim = aEvent.world.provider.dimensionId;
            ArrayList<Chunk> worldDataArrayList = new ArrayList<>(1024);
            data.get(dim).forEach((chunkCoordIntPair, compound) -> {
                if (compound.getDouble(INTENSITY) >= SWAP_THRESHOLD) {
                    Chunk chunk = aEvent.world.getChunkFromChunkCoords(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos);
                    if (chunk.isChunkLoaded) {
                        worldDataArrayList.add(chunk);
                    }
                }
            });
            if (worldDataArrayList.size() >= 2) {
                Chunk a = worldDataArrayList.remove(TecTech.RANDOM.nextInt(worldDataArrayList.size()));
                Chunk b = worldDataArrayList.remove(TecTech.RANDOM.nextInt(worldDataArrayList.size()));
                double avg=.5* (data.get(dim).get(a.getChunkCoordIntPair()).getDouble(INTENSITY)+
                                data.get(dim).get(b.getChunkCoordIntPair()).getDouble(INTENSITY));
                data.get(dim).get(a.getChunkCoordIntPair()).setDouble(INTENSITY, Math.min(SWAP_THRESHOLD,avg * (TecTech.RANDOM.nextFloat()+.5F) * 0.5F));
                data.get(dim).get(b.getChunkCoordIntPair()).setDouble(INTENSITY, Math.min(SWAP_THRESHOLD,avg * (TecTech.RANDOM.nextFloat()+.5F) * 0.5F));
                data.get(dim).markForTransmissionToClient(a.getChunkCoordIntPair());
                data.get(dim).markForTransmissionToClient(b.getChunkCoordIntPair());
                swapSomething(a, b);
            }
        }
    }

    private void swapSomething(Chunk a,Chunk b){
        for(int i=0;i<128;i++){
            int x=TecTech.RANDOM.nextInt(16);
            int y=TecTech.RANDOM.nextInt(a.worldObj.getActualHeight());
            int z=TecTech.RANDOM.nextInt(16);
            Block aBlock=a.getBlock(x,y,z);
            Block bBlock=a.getBlock(x,y,z);
            int aMeta=a.getBlockMetadata(x,y,z);
            int bMeta=a.getBlockMetadata(x,y,z);
            if(a.getTileEntityUnsafe(x,y,z)==null&&b.getTileEntityUnsafe(x,y,z)==null){
                a.worldObj.setBlock((a.xPosition<<4)+x,y,(a.zPosition<<4)+z,bBlock,bMeta,3);
                b.worldObj.setBlock((b.xPosition<<4)+x,y,(b.zPosition<<4)+z,aBlock,aMeta,3);
            }else if(a.getTileEntityUnsafe(x,y,z)==null){
                a.worldObj.setBlockToAir((a.xPosition<<4)+x,y,(a.zPosition<<4)+z);
                b.worldObj.setBlock((b.xPosition<<4)+x,y,(b.zPosition<<4)+z,aBlock,aMeta,3);
            }else if(b.getTileEntityUnsafe(x,y,z)==null){
                a.worldObj.setBlock((a.xPosition<<4)+x,y,(a.zPosition<<4)+z,bBlock,bMeta,3);
                b.worldObj.setBlockToAir((b.xPosition<<4)+x,y,(b.zPosition<<4)+z);
            }else{
                a.worldObj.setBlockToAir((a.xPosition<<4)+x,y,(a.zPosition<<4)+z);
                b.worldObj.setBlockToAir((b.xPosition<<4)+x,y,(b.zPosition<<4)+z);
            }
        }
    }

    @Override
    public void tickPlayer(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.PlayerTickEvent aEvent) {
        if (aEvent.side.isClient()) {
            EntityClientPlayerMP player=Minecraft.getMinecraft().thePlayer;
            ChunkCoordIntPair pair = new ChunkCoordIntPair(player.chunkCoordX, player.chunkCoordZ);
            NBTTagCompound compound = data.get(player.worldObj.provider.dimensionId).get(pair);
            if (compound != null) {
                for (int i = 0, pow = (int)Math.min(32,compound.getDouble(INTENSITY) / PER_PARTICLE); i < pow; i++) {
                    TecTech.proxy.em_particle(player.worldObj,
                            player.posX - 32D + TecTech.RANDOM.nextFloat() * 64D,
                            player.posY - 32D + TecTech.RANDOM.nextFloat() * 64D,
                            player.posZ - 32D + TecTech.RANDOM.nextFloat() * 64D);
                }
            }

            data.get(player.worldObj.provider.dimensionId).forEach((chunkCoordIntPair, dat) -> {
                if (Math.abs(chunkCoordIntPair.getCenterXPos() - player.posX) + Math.abs(chunkCoordIntPair.getCenterZPosition() - player.posZ) < 256) {
                    for (int i = 0, pow = (int)Math.min(32,dat.getDouble(INTENSITY) / PER_PARTICLE); i < pow; i++) {
                        TecTech.proxy.em_particle(player.worldObj,
                                (chunkCoordIntPair.chunkXPos << 4) + TecTech.RANDOM.nextFloat() * 48D - 16D,
                                player.posY + TecTech.RANDOM.nextFloat() * 128D - 64D,
                                (chunkCoordIntPair.chunkZPos << 4) + TecTech.RANDOM.nextFloat() * 48D - 16D);
                    }
                }
            });
        }
    }

    @Override
    public void tickServer(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.ServerTickEvent event) {
        if(event.side.isServer()){

        }
    }

    @Override
    public void pullData(ChunkEvent.Load aEvent) {
        NetworkDispatcher.INSTANCE.sendToServer(new ChunkDataMessage.ChunkDataQuery(aEvent, this));
    }

    @Override
    public void pushData(int world, ChunkCoordIntPair chunk) {
        NetworkDispatcher.INSTANCE.sendToDimension(new ChunkDataMessage.ChunkDataData(world, chunk, this), world);
    }

    @Override
    public int pushPayloadSpreadPeriod() {
        return 100;
    }

    public void addAnomaly(IGregTechTileEntity iGregTechTileEntity, double amount) {
        if (iGregTechTileEntity.isServerSide()) {
            World w = iGregTechTileEntity.getWorld();
            addAnomaly(w.provider.dimensionId,
                    new ChunkCoordIntPair(
                            iGregTechTileEntity.getXCoord() >> 4,
                            iGregTechTileEntity.getZCoord() >> 4),
                    amount);
        }
    }

    public void addAnomaly(int world, ChunkCoordIntPair chunk, double amount) {
        amount=Math.abs(amount);
        NBTTagCompound old = TecTech.chunkDataHandler.getChunkData(this, world, chunk);
        if (old == null) {
            NBTTagCompound data = new NBTTagCompound();
            data.setDouble(INTENSITY, amount);
            TecTech.chunkDataHandler.putChunkData(this, world, chunk, data);
        } else {
            old.setDouble(INTENSITY, old.getDouble(INTENSITY) + amount);
        }
        TecTech.chunkDataHandler.getChunkData(this, world).markForTransmissionToClient(chunk);
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone evt) {
        if (evt.wasDeath && !evt.isCanceled()) {
            if (evt.original != null && evt.entityPlayer != null && !(evt.entityPlayer instanceof FakePlayer)) {
                int i;
                ItemStack item;
                for (i = 0; i < evt.original.field_71071_by.field_70462_a.length; ++i) {
                    item = evt.original.field_71071_by.field_70462_a[i];
                    if (this.isSoulBound(item) && this.addToPlayerInventory(evt.entityPlayer, item)) {
                        evt.original.field_71071_by.field_70462_a[i] = null;
                    }
                }

                for (i = 0; i < evt.original.field_71071_by.field_70460_b.length; ++i) {
                    item = evt.original.field_71071_by.field_70460_b[i];
                    if (this.isSoulBound(item) && this.addToPlayerInventory(evt.entityPlayer, item)) {
                        evt.original.field_71071_by.field_70460_b[i] = null;
                    }
                }
            }
        }
    }
}
