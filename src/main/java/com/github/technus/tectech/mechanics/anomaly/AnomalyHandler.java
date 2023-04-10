package com.github.technus.tectech.mechanics.anomaly;

import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED;
import static com.github.technus.tectech.util.TT_Utility.crossProduct3D;
import static com.github.technus.tectech.util.TT_Utility.normalize3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkEvent;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.MainLoader;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.data.ChunkDataHandler;
import com.github.technus.tectech.mechanics.data.ChunkDataMessage;
import com.github.technus.tectech.mechanics.data.IChunkMetaDataHandler;
import com.github.technus.tectech.mechanics.data.PlayerDataMessage;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition;
import com.github.technus.tectech.util.TT_Utility;

import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;

public class AnomalyHandler implements IChunkMetaDataHandler {

    private static final double SWAP_THRESHOLD = EMAtomDefinition.getSomethingHeavy().getMass() * 1000D
            * EM_COUNT_PER_MATERIAL_AMOUNT_DIMINISHED; // can be const as it is computed later...
    private static final int COUNT_DIV = 32;
    private static final double PER_PARTICLE = SWAP_THRESHOLD / COUNT_DIV;
    private static final String INTENSITY = "intensity", SPACE_CANCER = "space_cancer", SPACE_CHARGE = "space_charge",
            SPACE_MASS = "space_mass";
    private static final int MEAN_DELAY = 50;
    private static final double CANCER_EFFECTIVENESS = 1 / EM_COUNT_PER_MATERIAL_AMOUNT;
    private static final double MASS_EFFECTIVENESS = 1 / EM_COUNT_PER_MATERIAL_AMOUNT;
    private static final double CHARGE_EFFECTIVENESS = 10 / EM_COUNT_PER_MATERIAL_AMOUNT;
    private static final double CHARGE_EXPLOSIVENESS = 5 / EM_COUNT_PER_MATERIAL_AMOUNT;

    private boolean fixMe = false;
    private final List<EntityPlayer> playersWithCharge = new ArrayList<>();
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
            ChunkDataHandler.ChunkHashMap chunkHashMap = data.get(dim);
            chunkHashMap.forEach((chunkCoordIntPair, compound) -> {
                if (compound.getDouble(INTENSITY) > SWAP_THRESHOLD) {
                    Chunk chunk = aEvent.world
                            .getChunkFromChunkCoords(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos);
                    if (chunk.isChunkLoaded) {
                        worldDataArrayList.add(chunk);
                    }
                }
            });
            if (worldDataArrayList.size() >= 2) {
                Chunk a = worldDataArrayList.remove(TecTech.RANDOM.nextInt(worldDataArrayList.size()));
                Chunk b = worldDataArrayList.get(TecTech.RANDOM.nextInt(worldDataArrayList.size()));
                ChunkCoordIntPair aCoords = a.getChunkCoordIntPair();
                ChunkCoordIntPair bCoords = b.getChunkCoordIntPair();

                double newValue = (chunkHashMap.get(aCoords).getDouble(INTENSITY)
                        + chunkHashMap.get(bCoords).getDouble(INTENSITY)) / 2 - SWAP_THRESHOLD / 8;
                float split = TecTech.RANDOM.nextFloat();

                chunkHashMap.get(aCoords).setDouble(INTENSITY, newValue * split);
                chunkHashMap.get(bCoords).setDouble(INTENSITY, newValue * (1 - split));
                chunkHashMap.markForTransmissionToClient(aCoords);
                chunkHashMap.markForTransmissionToClient(bCoords);
                swapSomething(a, b, newValue);
            }
            worldDataArrayList.clear();
        }
        for (Object o : aEvent.world.playerEntities) {
            if (o instanceof EntityPlayer && !((EntityPlayer) o).capabilities.isCreativeMode) {
                if (getCharge((EntityPlayer) o) != 0) {
                    playersWithCharge.add((EntityPlayer) o);
                }
            }
        }
        if (playersWithCharge.size() > 0) {
            outer: for (EntityPlayer other : playersWithCharge) {
                double fieldOther = getCharge(other);
                for (EntityPlayer player : playersWithCharge) {
                    if (other == player) {
                        continue outer;
                    }
                    double field = getCharge(player);
                    double absDifference = Math.abs(field - fieldOther);
                    if (absDifference != 0) {
                        if (player.getDistanceSqToEntity(other) < 1) {
                            double avg = (fieldOther + field) / 2;
                            if (TecTech.configTecTech.BOOM_ENABLE) {
                                other.worldObj.createExplosion(
                                        other,
                                        other.posX,
                                        other.posY,
                                        other.posZ,
                                        (float) Math.min(CHARGE_EXPLOSIVENESS * absDifference, 25),
                                        true);
                                player.worldObj.createExplosion(
                                        player,
                                        player.posX,
                                        player.posY,
                                        player.posZ,
                                        (float) Math.min(CHARGE_EXPLOSIVENESS * absDifference, 25),
                                        true);
                            }
                            GT_Utility.sendSoundToPlayers(
                                    other.worldObj,
                                    GregTech_API.sSoundList.get(209),
                                    1.0F,
                                    -1,
                                    (int) other.posX,
                                    (int) other.posY,
                                    (int) other.posZ);
                            GT_Utility.sendSoundToPlayers(
                                    player.worldObj,
                                    GregTech_API.sSoundList.get(209),
                                    1.0F,
                                    -1,
                                    (int) player.posX,
                                    (int) player.posY,
                                    (int) player.posZ);
                            setCharge(player, avg);
                            setCharge(other, avg);
                        }
                    }
                }
            }
            playersWithCharge.clear();
        }
    }

    private void swapSomething(Chunk a, Chunk b, double mass) {
        float explosionPower = (float) Math.log(mass / EM_COUNT_PER_MATERIAL_AMOUNT);
        for (int i = 0; i < Math.min((int) explosionPower, 64); i++) {
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
            if (TecTech.configTecTech.BOOM_ENABLE) {
                if (TecTech.RANDOM.nextBoolean()) {
                    a.worldObj.createExplosion(
                            null,
                            (a.xPosition << 4) + x + .5,
                            y + .5,
                            (a.zPosition << 4) + z + .5,
                            explosionPower * TecTech.RANDOM.nextFloat(),
                            true);
                }
                GT_Utility.sendSoundToPlayers(
                        a.worldObj,
                        GregTech_API.sSoundList.get(209),
                        1.0F,
                        -1,
                        (a.xPosition << 4) + x,
                        y,
                        (a.zPosition << 4) + z);
                if (TecTech.RANDOM.nextBoolean()) {
                    b.worldObj.createExplosion(
                            null,
                            (b.xPosition << 4) + x + .5,
                            y + .5,
                            (b.zPosition << 4) + z + .5,
                            explosionPower * TecTech.RANDOM.nextFloat(),
                            true);
                }
                GT_Utility.sendSoundToPlayers(
                        b.worldObj,
                        GregTech_API.sSoundList.get(209),
                        1.0F,
                        -1,
                        (b.xPosition << 4) + x,
                        y,
                        (b.zPosition << 4) + z);
            }
        }
        int x = (b.xPosition - a.xPosition) << 4;
        int z = (b.xPosition - a.xPosition) << 4;
        List<EntityLivingBase> aE = a.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, TT_Utility.fromChunk(a));
        List<EntityLivingBase> bE = b.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, TT_Utility.fromChunk(b));
        for (EntityLivingBase entityLivingBase : aE) {
            if (TecTech.RANDOM.nextBoolean()) {
                if (entityLivingBase instanceof EntityPlayer) {
                    EntityPlayer player = ((EntityPlayer) entityLivingBase);
                    if (!player.capabilities.isCreativeMode) {
                        player.setPositionAndUpdate(
                                entityLivingBase.posX + x,
                                entityLivingBase.posY,
                                entityLivingBase.posZ + z);
                        player.attackEntityFrom(
                                MainLoader.subspace,
                                8 + TecTech.RANDOM.nextInt((int) (explosionPower / 4)));
                        player.addPotionEffect(new PotionEffect(Potion.hunger.id, 1200));
                        player.addPotionEffect(new PotionEffect(Potion.weakness.id, 1000));
                        player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 800));
                        player.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 600));
                        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 400));
                        player.addPotionEffect(new PotionEffect(Potion.poison.id, 200));
                        player.addPotionEffect(new PotionEffect(Potion.blindness.id, 100));
                        player.addPotionEffect(new PotionEffect(Potion.wither.id, 60));
                        addCharge(player, -mass * TecTech.RANDOM.nextFloat());
                    }
                } else {
                    entityLivingBase.setPositionAndUpdate(
                            entityLivingBase.posX + x,
                            entityLivingBase.posY,
                            entityLivingBase.posZ + z);
                    entityLivingBase.attackEntityFrom(
                            MainLoader.subspace,
                            8 + TecTech.RANDOM.nextInt((int) (explosionPower / 4)));
                }
            }
        }
        for (EntityLivingBase o : bE) {
            if (TecTech.RANDOM.nextBoolean()) {
                if (o instanceof EntityPlayer) {
                    EntityPlayer player = ((EntityPlayer) o);
                    if (!player.capabilities.isCreativeMode) {
                        player.setPositionAndUpdate(o.posX - x, o.posY, o.posZ - z);
                        player.attackEntityFrom(
                                MainLoader.subspace,
                                8 + TecTech.RANDOM.nextInt((int) -(explosionPower / 4)));
                        player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 800));
                        player.addPotionEffect(new PotionEffect(Potion.digSpeed.id, 600));
                        player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 400));
                        player.addPotionEffect(new PotionEffect(Potion.jump.id, 200));
                        player.addPotionEffect(new PotionEffect(Potion.resistance.id, 100));
                        player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 60));
                        addCharge(player, mass * TecTech.RANDOM.nextFloat());
                    }
                } else {
                    o.setPositionAndUpdate(o.posX + x, o.posY, o.posZ + z);
                    o.attackEntityFrom(MainLoader.subspace, 8 + TecTech.RANDOM.nextInt((int) (explosionPower / 4)));
                }
            }
        }
    }

    @Override
    public void tickPlayer(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.PlayerTickEvent aEvent) {
        if (aEvent.side.isClient()) {
            EntityPlayer player = TecTech.proxy.getPlayer();
            ChunkCoordIntPair pair = new ChunkCoordIntPair(player.chunkCoordX, player.chunkCoordZ);
            NBTTagCompound compound = data.get(player.worldObj.provider.dimensionId).get(pair);
            if (compound != null) {
                for (int i = 0, badness = (int) Math.min(COUNT_DIV, compound.getDouble(INTENSITY) / PER_PARTICLE); i
                        < badness; i++) {
                    TecTech.proxy.em_particle(
                            player.worldObj,
                            player.posX + TecTech.RANDOM.nextGaussian() * 64D,
                            player.posY + TecTech.RANDOM.nextGaussian() * 64D,
                            player.posZ + TecTech.RANDOM.nextGaussian() * 64D);
                }
            }

            for (Map.Entry<ChunkCoordIntPair, NBTTagCompound> entry : data.get(player.worldObj.provider.dimensionId)
                    .entrySet()) {
                ChunkCoordIntPair chunkCoordIntPair = entry.getKey();
                NBTTagCompound dat = entry.getValue();
                if (Math.abs(chunkCoordIntPair.getCenterXPos() - player.posX)
                        + Math.abs(chunkCoordIntPair.getCenterZPosition() - player.posZ) < 256) {
                    for (int i = 0, pow = (int) Math.min(COUNT_DIV, dat.getDouble(INTENSITY) / PER_PARTICLE); i
                            < pow; i++) {
                        TecTech.proxy.em_particle(
                                player.worldObj,
                                chunkCoordIntPair.getCenterXPos() + TecTech.RANDOM.nextGaussian() * 32D,
                                player.posY + TecTech.RANDOM.nextFloat() * 128D - 64D,
                                chunkCoordIntPair.getCenterZPosition() + TecTech.RANDOM.nextGaussian() * 32D);
                    }
                }
            }
        } else if (TecTech.RANDOM.nextInt(50) == 0) {
            EntityPlayer player = aEvent.player;
            ChunkCoordIntPair pair = new ChunkCoordIntPair(player.chunkCoordX, player.chunkCoordZ);
            NBTTagCompound compound = data.get(player.worldObj.provider.dimensionId).get(pair);
            NBTTagCompound playerTag = TecTech.playerPersistence.getDataOrSetToNewTag(player);
            boolean saveRequired = false;
            if (!player.capabilities.isCreativeMode) {
                double cancer = getCancer(player);
                if (compound != null) {
                    int badness = (int) Math.min(COUNT_DIV, compound.getDouble(INTENSITY) / PER_PARTICLE);
                    if (badness > 0) {
                        playerTag.setDouble(SPACE_CANCER, Math.min(2, cancer + 9.765625E-4f * badness));
                        player.attackEntityFrom(MainLoader.subspace, Math.max(1, badness / 8f));
                        saveRequired = true;
                    }
                } else if (playerTag.getDouble(SPACE_CANCER) > 0 && !player.isDead) {
                    if (player.ticksExisted % 10 == 0) {
                        playerTag.setDouble(SPACE_CANCER, Math.max(0, cancer - 7.6293945E-5f));
                        saveRequired = true;
                    }
                }
            }

            if (saveRequired) {
                TecTech.playerPersistence.saveData(player);
                NetworkDispatcher.INSTANCE
                        .sendTo(new PlayerDataMessage.PlayerDataData(player), (EntityPlayerMP) player);
            }
        }
    }

    @Override
    public void tickRender(HashMap<Integer, ChunkDataHandler.ChunkHashMap> data, TickEvent.RenderTickEvent aEvent) {
        EntityPlayer player = TecTech.proxy.getPlayer();
        if (player != null) {
            if (!player.capabilities.isCreativeMode) {
                double cancer = getCancer(player) * CANCER_EFFECTIVENESS;
                if (cancer > 0) {
                    player.setInvisible(fixMe = TecTech.RANDOM.nextFloat() * 2 < cancer);
                    player.setAngles(
                            (TecTech.RANDOM.nextFloat() - .5F) * 36 * (float) cancer,
                            (TecTech.RANDOM.nextFloat() - .5F) * 36 * (float) cancer);
                    cancer *= cancer / 2F;
                    if (cancer > 1.75f) {
                        player.setVelocity(
                                (TecTech.RANDOM.nextFloat() - .5F) * cancer,
                                (TecTech.RANDOM.nextFloat() - .5F) * cancer,
                                (TecTech.RANDOM.nextFloat() - .5F) * cancer);
                    } else {
                        player.addVelocity(
                                (TecTech.RANDOM.nextFloat() - .5F) * cancer,
                                (TecTech.RANDOM.nextFloat() - .5F) * cancer,
                                (TecTech.RANDOM.nextFloat() - .5F) * cancer);
                    }
                }

                double charge = getCharge(player);
                if (charge != 0) {
                    for (Object o : player.worldObj.playerEntities) {
                        if (o instanceof EntityPlayer && !((EntityPlayer) o).capabilities.isCreativeMode) {
                            EntityPlayer otherPlayer = (EntityPlayer) o;
                            double chargeOther = getCharge(otherPlayer);
                            if (chargeOther != 0 && player != o) {
                                double reaction = chargeOther * charge;
                                if (reaction != 0) {
                                    double distanceSq = otherPlayer.getDistanceSqToEntity(player);
                                    if (distanceSq >= 1) {
                                        double effect = CHARGE_EFFECTIVENESS * reaction
                                                / (distanceSq * distanceSq * distanceSq);
                                        double dX = (player.posX - otherPlayer.posX) * effect;
                                        double dY = (player.posY - otherPlayer.posY) * effect;
                                        double dZ = (player.posZ - otherPlayer.posZ) * effect;
                                        player.addVelocity(dX, dY, dZ);
                                        otherPlayer.addVelocity(-dX, -dY, -dZ);
                                    }
                                }
                            }
                        }
                    }
                }

                double mass = getMass(player);
                if (mass != 0) {
                    for (Object o : player.worldObj.playerEntities) {
                        if (o instanceof EntityPlayer && !((EntityPlayer) o).capabilities.isCreativeMode) {
                            EntityPlayer otherPlayer = (EntityPlayer) o;
                            double massOther = getMass(otherPlayer);
                            if (massOther != 0 && player != o) {
                                double reaction = massOther * mass;
                                if (reaction != 0) {
                                    double distanceSq = otherPlayer.getDistanceSqToEntity(player);
                                    if (distanceSq >= 1) {
                                        double effect = MASS_EFFECTIVENESS * reaction
                                                / (distanceSq * distanceSq * distanceSq);
                                        double effect1 = effect / mass;
                                        double effect2 = effect / massOther;
                                        double[] dPos = new double[] { player.posX - otherPlayer.posX,
                                                player.posY - otherPlayer.posY, player.posZ - otherPlayer.posZ };
                                        double[] vel = new double[] { player.motionX, player.motionY, player.motionZ };
                                        double[] out = new double[3];
                                        crossProduct3D(dPos, vel, out);
                                        crossProduct3D(out, dPos, vel);
                                        normalize3D(vel, out);
                                        player.addVelocity(effect1 * out[0], effect1 * out[1], effect1 * out[2]);
                                        otherPlayer
                                                .addVelocity(effect2 * -out[0], effect2 * -out[1], effect2 * -out[2]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (fixMe) {
                player.setInvisible(false);
                fixMe = false;
            }
        }
    }

    @Override
    public void pullData(ChunkEvent.Load aEvent) {
        NetworkDispatcher.INSTANCE.sendToServer(new ChunkDataMessage.ChunkDataQuery(aEvent, this));
    }

    @Override
    public void pushData(World world, ChunkCoordIntPair chunk) {
        NetworkDispatcher.INSTANCE.sendToDimension(
                new ChunkDataMessage.ChunkDataData(world.provider.dimensionId, chunk, this),
                world.provider.dimensionId);
    }

    @Override
    public int pushPayloadSpreadPeriod() {
        return 100;
    }

    public void addAnomaly(IGregTechTileEntity gtTile, double amount) {
        addAnomaly(gtTile.getWorld(), gtTile.getXCoord(), gtTile.getZCoord(), amount);
    }

    public void addAnomaly(TileEntity tileEntity, double amount) {
        addAnomaly(tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.zCoord, amount);
    }

    public void addAnomaly(World w, int x, int z, double amount) {
        if (!w.isRemote) {
            addAnomaly(w.provider.dimensionId, new ChunkCoordIntPair(x >> 4, z >> 4), amount);
        }
    }

    public void addAnomaly(int world, ChunkCoordIntPair chunk, double amount) {
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

    public double getCharge(EntityPlayer player) {
        return getDouble(player, SPACE_CHARGE);
    }

    public void setCharge(EntityPlayer player, double amount) {
        setDouble(player, SPACE_CHARGE, amount);
    }

    public void addCharge(EntityPlayer player, double amount) {
        addDouble(player, SPACE_CHARGE, amount);
    }

    public double getMass(EntityPlayer player) {
        return getDouble(player, SPACE_MASS);
    }

    public void setMass(EntityPlayer player, double amount) {
        setDouble(player, SPACE_MASS, amount);
    }

    public void addMass(EntityPlayer player, double amount) {
        addDouble(player, SPACE_MASS, amount);
    }

    public double getCancer(EntityPlayer player) {
        return getDouble(player, SPACE_CANCER);
    }

    public void setCancer(EntityPlayer player, double amount) {
        setDouble(player, SPACE_CANCER, amount);
    }

    public void addCancer(EntityPlayer player, double amount) {
        addDouble(player, SPACE_CANCER, amount);
    }

    public double getDouble(EntityPlayer player, String name) {
        return TecTech.playerPersistence.getDataOrSetToNewTag(player).getDouble(name);
    }

    public void setDouble(EntityPlayer player, String name, double amount) {
        TecTech.playerPersistence.getDataOrSetToNewTag(player).setDouble(name, amount);
        TecTech.playerPersistence.saveData(player);
        NetworkDispatcher.INSTANCE.sendToAll(new PlayerDataMessage.PlayerDataData(player));
    }

    public void addDouble(EntityPlayer player, String name, double amount) {
        NBTTagCompound data = TecTech.playerPersistence.getDataOrSetToNewTag(player);
        data.setDouble(name, amount + data.getDouble(name));
        TecTech.playerPersistence.saveData(player);
        NetworkDispatcher.INSTANCE.sendToAll(new PlayerDataMessage.PlayerDataData(player));
    }
}
