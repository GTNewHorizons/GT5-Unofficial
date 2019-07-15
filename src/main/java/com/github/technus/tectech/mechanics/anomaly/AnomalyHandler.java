package com.github.technus.tectech.mechanics.anomaly;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.chunkData.ChunkDataHandler;
import com.github.technus.tectech.chunkData.IChunkMetaDataHandler;
import com.github.technus.tectech.loader.MainLoader;
import com.github.technus.tectech.loader.network.ChunkDataMessage;
import com.github.technus.tectech.loader.network.NetworkDispatcher;
import com.github.technus.tectech.loader.network.PlayerDataMessage;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.atom.dAtomDefinition;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnomalyHandler implements IChunkMetaDataHandler {
    private static final double SWAP_THRESHOLD = dAtomDefinition.getSomethingHeavy().getMass() * 10000D;
    private static final int COUNT_DIV=32;
    private static final double PER_PARTICLE=SWAP_THRESHOLD/COUNT_DIV;
    private static final String INTENSITY = "intensity",SPACE_CANCER="space_cancer";
    private static final int MEAN_DELAY =50;

    private boolean fixMe=false;

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
                swapSomething(a, b,avg);
            }
        }
    }

    private void swapSomething(Chunk a,Chunk b,double avg) {
        for (int i = 0; i < 64; i++) {
            int x = TecTech.RANDOM.nextInt(16);
            int y = TecTech.RANDOM.nextInt(a.worldObj.getActualHeight());
            int z = TecTech.RANDOM.nextInt(16);
            Block aBlock = a.getBlock(x, y, z);
            Block bBlock = a.getBlock(x, y, z);
            int aMeta = a.getBlockMetadata(x, y, z);
            int bMeta = a.getBlockMetadata(x, y, z);
            if (a.getTileEntityUnsafe(x, y, z) == null && b.getTileEntityUnsafe(x, y, z) == null) {
                a.worldObj.setBlock((a.xPosition << 4) + x, y, (a.zPosition << 4) + z, bBlock, bMeta, 3);
                b.worldObj.setBlock((b.xPosition << 4) + x, y, (b.zPosition << 4) + z, aBlock, aMeta, 3);
            } else if (a.getTileEntityUnsafe(x, y, z) == null) {
                b.worldObj.setBlock((b.xPosition << 4) + x, y, (b.zPosition << 4) + z, aBlock, aMeta, 3);
                a.worldObj.setBlockToAir((a.xPosition << 4) + x, y, (a.zPosition << 4) + z);
                if (TecTech.RANDOM.nextBoolean()) {
                    a.worldObj.createExplosion(null, (a.xPosition << 4) + x + .5, y + .5, (a.zPosition << 4) + z + .5, (float) Math.log10(avg), true);
                    GT_Utility.sendSoundToPlayers(a.worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1, (a.xPosition << 4) + x, y, (a.zPosition << 4) + z);
                }
            } else if (b.getTileEntityUnsafe(x, y, z) == null) {
                a.worldObj.setBlock((a.xPosition << 4) + x, y, (a.zPosition << 4) + z, bBlock, bMeta, 3);
                b.worldObj.setBlockToAir((b.xPosition << 4) + x, y, (b.zPosition << 4) + z);
                if (TecTech.RANDOM.nextBoolean()) {
                    b.worldObj.createExplosion(null, (b.xPosition << 4) + x + .5, y + .5, (b.zPosition << 4) + z + .5, (float) Math.log10(avg), true);
                    GT_Utility.sendSoundToPlayers(b.worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1, (b.xPosition << 4) + x, y, (b.zPosition << 4) + z);
                }
            } else {
                if (TecTech.RANDOM.nextBoolean()) {
                    a.worldObj.setBlockToAir((a.xPosition << 4) + x, y, (a.zPosition << 4) + z);
                    a.worldObj.createExplosion(null, (a.xPosition << 4) + x + .5, y + .5, (a.zPosition << 4) + z + .5, (float) Math.log10(avg), true);
                    GT_Utility.sendSoundToPlayers(a.worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1, (a.xPosition << 4) + x, y, (a.zPosition << 4) + z);
                }
                if (TecTech.RANDOM.nextBoolean()) {
                    b.worldObj.setBlockToAir((b.xPosition << 4) + x, y, (b.zPosition << 4) + z);
                    b.worldObj.createExplosion(null, (b.xPosition << 4) + x + .5, y + .5, (b.zPosition << 4) + z + .5, (float) Math.log10(avg), true);
                    GT_Utility.sendSoundToPlayers(b.worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1, (b.xPosition << 4) + x, y, (b.zPosition << 4) + z);
                }
            }
        }
        int x = (b.xPosition - a.xPosition) << 4;
        int z = (b.xPosition - a.xPosition) << 4;
        List aE = a.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, Util.fromChunk(a));
        List bE = b.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, Util.fromChunk(b));
        aE.forEach(o -> {
            if (TecTech.RANDOM.nextBoolean()) {
                Vec3 pos = ((EntityLivingBase) o).getPosition(1);
                ((EntityLivingBase) o).setPositionAndUpdate(pos.xCoord + x, pos.yCoord, pos.zCoord + z);
                ((EntityLivingBase) o).attackEntityFrom(MainLoader.subspace,18);
                if(o instanceof EntityPlayer){
                    ((EntityPlayer) o).addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 200));
                }
            }
        });
        bE.forEach(o -> {
            if (TecTech.RANDOM.nextBoolean()) {
                Vec3 pos = ((EntityLivingBase) o).getPosition(1);
                ((EntityLivingBase) o).setPositionAndUpdate(pos.xCoord - x, pos.yCoord, pos.zCoord - z);
                ((EntityLivingBase) o).attackEntityFrom(MainLoader.subspace,18);
                if(o instanceof EntityPlayer){
                    ((EntityPlayer) o).addPotionEffect(new PotionEffect(Potion.digSpeed.id, 200));
                }
            }
        });
    }

    @Override
    public void tickPlayer(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.PlayerTickEvent aEvent) {
        if (aEvent.side.isClient()) {
            EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
            ChunkCoordIntPair pair = new ChunkCoordIntPair(player.chunkCoordX, player.chunkCoordZ);
            NBTTagCompound compound = data.get(player.worldObj.provider.dimensionId).get(pair);
            if (compound != null) {
                for (int i = 0, badness = (int) Math.min(COUNT_DIV, compound.getDouble(INTENSITY) / PER_PARTICLE); i < badness; i++) {
                    TecTech.proxy.em_particle(player.worldObj,
                            player.posX - 32D + TecTech.RANDOM.nextFloat() * 64D,
                            player.posY - 32D + TecTech.RANDOM.nextFloat() * 64D,
                            player.posZ - 32D + TecTech.RANDOM.nextFloat() * 64D);
                }
            }

            data.get(player.worldObj.provider.dimensionId).forEach((chunkCoordIntPair, dat) -> {
                if (Math.abs(chunkCoordIntPair.getCenterXPos() - player.posX) + Math.abs(chunkCoordIntPair.getCenterZPosition() - player.posZ) < 256) {
                    for (int i = 0, pow = (int) Math.min(32, dat.getDouble(INTENSITY) / PER_PARTICLE); i < pow; i++) {
                        TecTech.proxy.em_particle(player.worldObj,
                                (chunkCoordIntPair.chunkXPos << 4) + TecTech.RANDOM.nextFloat() * 48D - 16D,
                                player.posY + TecTech.RANDOM.nextFloat() * 128D - 64D,
                                (chunkCoordIntPair.chunkZPos << 4) + TecTech.RANDOM.nextFloat() * 48D - 16D);
                    }
                }
            });
        } else if (TecTech.RANDOM.nextInt(50) == 0) {
            EntityPlayer player = aEvent.player;
            ChunkCoordIntPair pair = new ChunkCoordIntPair(player.chunkCoordX, player.chunkCoordZ);
            NBTTagCompound compound = data.get(player.worldObj.provider.dimensionId).get(pair);
            NBTTagCompound playerTag = TecTech.playerPersistence.getDataOrSetToNewTag(player);
            if(player.capabilities.isCreativeMode){
                playerTag.setDouble(SPACE_CANCER, 0);
            }else {
                if (compound != null) {
                    int badness = (int) Math.min(COUNT_DIV, compound.getDouble(INTENSITY) / PER_PARTICLE);
                    if (badness > 0) {
                        playerTag.setDouble(SPACE_CANCER, Math.min(2, playerTag.getDouble(SPACE_CANCER) + 9.765625E-4f * badness));
                        player.attackEntityFrom(MainLoader.subspace,Math.max(1,badness/8f));
                    }
                } else if (playerTag.getDouble(SPACE_CANCER) > 0) {
                    playerTag.setDouble(SPACE_CANCER, Math.max(0, playerTag.getDouble(SPACE_CANCER) - 7.6293945E-6f));
                }
            }
            TecTech.playerPersistence.saveData(player);
            NetworkDispatcher.INSTANCE.sendTo(new PlayerDataMessage.PlayerDataData(player), (EntityPlayerMP) player);
        }
    }

    @Override
    public void tickRender(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.RenderTickEvent aEvent) {
        EntityClientPlayerMP player=Minecraft.getMinecraft().thePlayer;
        if(player!=null) {
            if(player.capabilities.isCreativeMode) {
                NBTTagCompound tagCompound = TecTech.playerPersistence.getDataOrSetToNewTag(Minecraft.getMinecraft().thePlayer);
                if (tagCompound != null) {
                    float cancer = tagCompound.getFloat(SPACE_CANCER);
                    if (cancer > 0) {
                        player.setAngles((TecTech.RANDOM.nextFloat() - .5F) * 4 * cancer, (TecTech.RANDOM.nextFloat() - .5F) * 4 * cancer);
                        player.setInvisible(fixMe = TecTech.RANDOM.nextFloat() * 2 < cancer);
                        if (cancer > 1.9f) {
                            player.setVelocity((TecTech.RANDOM.nextFloat() - .5F) * cancer * cancer / 2, (TecTech.RANDOM.nextFloat() - .5F) * cancer * cancer / 2, (TecTech.RANDOM.nextFloat() - .5F) * cancer * cancer / 2);
                        } else {
                            player.addVelocity((TecTech.RANDOM.nextFloat() - .5F) * cancer * cancer / 2, (TecTech.RANDOM.nextFloat() - .5F) * cancer * cancer / 2, (TecTech.RANDOM.nextFloat() - .5F) * cancer * cancer / 2);
                        }
                    }
                }
            }
            if (fixMe){
                player.setInvisible(false);
                fixMe=false;
            }
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
}
