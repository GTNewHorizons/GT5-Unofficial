package com.github.technus.tectech.mechanics.anomaly;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.loader.MainLoader;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.data.ChunkDataHandler;
import com.github.technus.tectech.mechanics.data.ChunkDataMessage;
import com.github.technus.tectech.mechanics.data.IChunkMetaDataHandler;
import com.github.technus.tectech.mechanics.data.PlayerDataMessage;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.dAtomDefinition;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnomalyHandler implements IChunkMetaDataHandler {
    public static final double SWAP_THRESHOLD = dAtomDefinition.getSomethingHeavy().getMass() * 10000D;
    public static final int COUNT_DIV=32;
    public static final double PER_PARTICLE=SWAP_THRESHOLD/COUNT_DIV;
    public static final String INTENSITY = "intensity",SPACE_CANCER="space_cancer", SPACE_CHARGE ="space_charge";
    public static final int MEAN_DELAY =50;
    private static final float CHARGE_EFFECTIVENESS = 10;
    private static final float CHARGE_EXPLOSIVENESS = 5;

    private boolean fixMe=false;
    private final ArrayList<EntityPlayer> playersWithCharge = new ArrayList<>();
    private final ArrayList<Chunk> worldDataArrayList = new ArrayList<>(512);

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
                double avg = .5 * (data.get(dim).get(a.getChunkCoordIntPair()).getDouble(INTENSITY) +
                        data.get(dim).get(b.getChunkCoordIntPair()).getDouble(INTENSITY));
                data.get(dim).get(a.getChunkCoordIntPair()).setDouble(INTENSITY, Math.min(SWAP_THRESHOLD, avg * (TecTech.RANDOM.nextFloat() + .5F) * 0.5F));
                data.get(dim).get(b.getChunkCoordIntPair()).setDouble(INTENSITY, Math.min(SWAP_THRESHOLD, avg * (TecTech.RANDOM.nextFloat() + .5F) * 0.5F));
                data.get(dim).markForTransmissionToClient(a.getChunkCoordIntPair());
                data.get(dim).markForTransmissionToClient(b.getChunkCoordIntPair());
                swapSomething(a, b, (float) Math.min(Math.log10(avg), 20));
            }
            worldDataArrayList.clear();
        }
        for (Object o : aEvent.world.playerEntities) {
            if (o instanceof EntityPlayer && !((EntityPlayer) o).capabilities.isCreativeMode) {
                float charge = TecTech.playerPersistence.getDataOrSetToNewTag((EntityPlayer) o).getFloat(SPACE_CHARGE);
                if (charge != 0) {
                    playersWithCharge.add((EntityPlayer) o);
                }
            }
        }
        if (playersWithCharge.size() > 0) {
            outer:
            for (EntityPlayer other : playersWithCharge) {
                float fieldOther = TecTech.playerPersistence.getDataOrSetToNewTag(other).getFloat(SPACE_CHARGE);
                for (EntityPlayer player : playersWithCharge) {
                    if (other == player) {
                        continue outer;
                    }
                    float field = TecTech.playerPersistence.getDataOrSetToNewTag(player).getFloat(SPACE_CHARGE);
                    float difference = Math.abs(field - fieldOther);
                    if (difference != 0) {
                        if (player.getDistanceSqToEntity(other) < 1) {
                            float avg = (fieldOther + field) / 2;
                            addAnomaly(other.worldObj.provider.dimensionId, new ChunkCoordIntPair(other.chunkCoordX, other.chunkCoordZ), Math.min(SWAP_THRESHOLD, PER_PARTICLE * difference));
                            other.worldObj.createExplosion(other, other.posX, other.posY, other.posZ, Math.min(CHARGE_EXPLOSIVENESS * difference, 25), true);
                            player.worldObj.createExplosion(player, player.posX, player.posY, player.posZ, Math.min(CHARGE_EXPLOSIVENESS * difference, 25), true);
                            TecTech.playerPersistence.getDataOrSetToNewTag(player).setFloat(SPACE_CHARGE, avg);
                            TecTech.playerPersistence.getDataOrSetToNewTag(other).setFloat(SPACE_CHARGE, avg);
                            TecTech.playerPersistence.saveData(player);
                            TecTech.playerPersistence.saveData(other);
                            NetworkDispatcher.INSTANCE.sendToAll(new PlayerDataMessage.PlayerDataData(player));
                            NetworkDispatcher.INSTANCE.sendToAll(new PlayerDataMessage.PlayerDataData(other));
                        }
                    }
                }
            }
            playersWithCharge.clear();
        }
    }

    private void swapSomething(Chunk a,Chunk b,float power) {
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
            } else if (b.getTileEntityUnsafe(x, y, z) == null) {
                a.worldObj.setBlock((a.xPosition << 4) + x, y, (a.zPosition << 4) + z, bBlock, bMeta, 3);
                b.worldObj.setBlockToAir((b.xPosition << 4) + x, y, (b.zPosition << 4) + z);
            } else {
                a.worldObj.setBlockToAir((a.xPosition << 4) + x, y, (a.zPosition << 4) + z);
                b.worldObj.setBlockToAir((b.xPosition << 4) + x, y, (b.zPosition << 4) + z);
            }
            if (TecTech.RANDOM.nextBoolean()) {
                a.worldObj.createExplosion(null, (a.xPosition << 4) + x + .5, y + .5, (a.zPosition << 4) + z + .5, power, true);
                GT_Utility.sendSoundToPlayers(a.worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1, (a.xPosition << 4) + x, y, (a.zPosition << 4) + z);
            }
            if (TecTech.RANDOM.nextBoolean()) {
                b.worldObj.createExplosion(null, (b.xPosition << 4) + x + .5, y + .5, (b.zPosition << 4) + z + .5, power, true);
                GT_Utility.sendSoundToPlayers(b.worldObj, GregTech_API.sSoundList.get(209), 1.0F, -1, (b.xPosition << 4) + x, y, (b.zPosition << 4) + z);
            }
        }
        int x = (b.xPosition - a.xPosition) << 4;
        int z = (b.xPosition - a.xPosition) << 4;
        List<EntityLivingBase> aE = a.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, Util.fromChunk(a));
        List<EntityLivingBase> bE = b.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, Util.fromChunk(b));
        aE.forEach(o -> {
            if (TecTech.RANDOM.nextBoolean()) {
                o.setPositionAndUpdate(o.posX + x, o.posY, o.posZ + z);
                o.attackEntityFrom(MainLoader.subspace,8+TecTech.RANDOM.nextInt(8));
                if(o instanceof EntityPlayer){
                    EntityPlayer player=((EntityPlayer) o);
                    if(!player.capabilities.isCreativeMode) {
                        player.addPotionEffect(new PotionEffect(Potion.hunger.id, 1200));
                        player.addPotionEffect(new PotionEffect(Potion.weakness.id, 1000));
                        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 800));
                        player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 600));
                        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 400));
                        player.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
                        player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100));
                        player.addPotionEffect(new PotionEffect(Potion.wither.id, 60));
                        TecTech.playerPersistence.getDataOrSetToNewTag(player).setFloat(SPACE_CHARGE,
                                TecTech.playerPersistence.getDataOrSetToNewTag(player).getFloat(SPACE_CHARGE)-(float)Math.abs(TecTech.RANDOM.nextGaussian()));
                        TecTech.playerPersistence.saveData(player);
                        NetworkDispatcher.INSTANCE.sendToAll(new PlayerDataMessage.PlayerDataData(player));
                    }
                }
            }
        });
        bE.forEach(o -> {
            if (TecTech.RANDOM.nextBoolean()) {
                o.setPositionAndUpdate(o.posX - x, o.posY, o.posZ - z);
                o.attackEntityFrom(MainLoader.subspace,8+TecTech.RANDOM.nextInt(8));
                if(o instanceof EntityPlayer){
                    EntityPlayer player=((EntityPlayer) o);
                    if(!player.capabilities.isCreativeMode) {
                        player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 800));
                        player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 600));
                        player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 400));
                        player.addPotionEffect(new PotionEffect(Potion.jump.id, 200));
                        player.addPotionEffect(new PotionEffect(Potion.resistance.id, 100));
                        player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 60));
                        TecTech.playerPersistence.getDataOrSetToNewTag(player).setFloat(SPACE_CHARGE,
                                TecTech.playerPersistence.getDataOrSetToNewTag(player).getFloat(SPACE_CHARGE)+(float)Math.abs(TecTech.RANDOM.nextGaussian()));

                        TecTech.playerPersistence.saveData(player);
                        NetworkDispatcher.INSTANCE.sendToAll(new PlayerDataMessage.PlayerDataData(player));
                    }
                }
            }
        });
    }

    @Override
    public void tickPlayer(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.PlayerTickEvent aEvent) {
        if (aEvent.side.isClient()) {
            EntityPlayer player = TecTech.proxy.getPlayer();
            ChunkCoordIntPair pair = new ChunkCoordIntPair(player.chunkCoordX, player.chunkCoordZ);
            NBTTagCompound compound = data.get(player.worldObj.provider.dimensionId).get(pair);
            if (compound != null) {
                for (int i = 0, badness = (int) Math.min(COUNT_DIV, compound.getDouble(INTENSITY) / PER_PARTICLE); i < badness; i++) {
                    TecTech.proxy.em_particle(player.worldObj,
                            player.posX + TecTech.RANDOM.nextGaussian() * 64D,
                            player.posY + TecTech.RANDOM.nextGaussian() * 64D,
                            player.posZ + TecTech.RANDOM.nextGaussian() * 64D);
                }
            }

            data.get(player.worldObj.provider.dimensionId).forEach((chunkCoordIntPair, dat) -> {
                if (Math.abs(chunkCoordIntPair.getCenterXPos() - player.posX) + Math.abs(chunkCoordIntPair.getCenterZPosition() - player.posZ) < 256) {
                    for (int i = 0, pow = (int) Math.min(32, dat.getDouble(INTENSITY) / PER_PARTICLE); i < pow; i++) {
                        TecTech.proxy.em_particle(player.worldObj,
                                chunkCoordIntPair.getCenterXPos() + TecTech.RANDOM.nextGaussian() * 32D,
                                player.posY + TecTech.RANDOM.nextFloat() * 128D - 64D,
                                chunkCoordIntPair.getCenterZPosition() + TecTech.RANDOM.nextGaussian() * 32D);
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
                } else if (playerTag.getDouble(SPACE_CANCER) > 0 && !player.isDead) {
                    playerTag.setDouble(SPACE_CANCER, Math.max(0, playerTag.getDouble(SPACE_CANCER) - 7.6293945E-6f));
                }
            }
            TecTech.playerPersistence.saveData(player);
            NetworkDispatcher.INSTANCE.sendTo(new PlayerDataMessage.PlayerDataData(player), (EntityPlayerMP) player);
        }
    }

    @Override
    public void tickRender(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.RenderTickEvent aEvent) {
        EntityPlayer player=TecTech.proxy.getPlayer();
        if(player!=null) {
            if(!player.capabilities.isCreativeMode) {
                NBTTagCompound tagCompound = TecTech.playerPersistence.getDataOrSetToNewTag(player);
                if (tagCompound != null) {
                    float cancer = tagCompound.getFloat(SPACE_CANCER);
                    if (cancer > 0) {
                        player.setInvisible(fixMe = TecTech.RANDOM.nextFloat() * 2 < cancer);
                        player.setAngles((TecTech.RANDOM.nextFloat() - .5F) * 36 * cancer, (TecTech.RANDOM.nextFloat() - .5F) * 36 * cancer);
                        cancer*=cancer/2F;
                        if (cancer > 1.75f) {
                            player.setVelocity((TecTech.RANDOM.nextFloat() - .5F) * cancer, (TecTech.RANDOM.nextFloat() - .5F) * cancer, (TecTech.RANDOM.nextFloat() - .5F) * cancer);
                        } else {
                            player.addVelocity((TecTech.RANDOM.nextFloat() - .5F) * cancer, (TecTech.RANDOM.nextFloat() - .5F) * cancer, (TecTech.RANDOM.nextFloat() - .5F) * cancer);
                        }
                    }
                }

                float charge = TecTech.playerPersistence.getDataOrSetToNewTag(player).getFloat(SPACE_CHARGE);
                if(charge!=0) {
                    for (Object o : player.worldObj.playerEntities) {
                        if (o instanceof EntityPlayer && !((EntityPlayer) o).capabilities.isCreativeMode) {
                            EntityPlayer otherPlayer=(EntityPlayer)o;
                            float chargeOther = TecTech.playerPersistence.getDataOrSetToNewTag(otherPlayer).getFloat(SPACE_CHARGE);
                            if (chargeOther != 0 && player != o) {
                                float reaction = chargeOther * charge;
                                if (reaction !=0) {
                                    double distanceSq = otherPlayer.getDistanceSqToEntity(player);
                                    if (distanceSq >= 1) {
                                        double effect = CHARGE_EFFECTIVENESS * reaction / (distanceSq * distanceSq * distanceSq);
                                        double dX = player.posX - otherPlayer.posX;
                                        double dY = player.posY - otherPlayer.posY;
                                        double dZ = player.posZ - otherPlayer.posZ;
                                        player.addVelocity(effect * dX, effect * dY, effect * dZ);
                                        otherPlayer.addVelocity(-effect * dX, -effect * dY, -effect * dZ);
                                    }
                                }
                            }
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
    public void pushData(World world, ChunkCoordIntPair chunk) {
        NetworkDispatcher.INSTANCE.sendToDimension(new ChunkDataMessage.ChunkDataData(world.provider.dimensionId, chunk, this), world.provider.dimensionId);
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
