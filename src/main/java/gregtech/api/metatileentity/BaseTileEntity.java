package gregtech.api.metatileentity;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;
import static gregtech.api.enums.GT_Values.COMPASS_DIRECTIONS;
import static gregtech.api.enums.GT_Values.GT;
import static gregtech.api.enums.GT_Values.NW;
import static gregtech.api.enums.GT_Values.SIDE_DOWN;
import static gregtech.api.enums.GT_Values.SIDE_UP;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ITileWithModularUI;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.metatileentity.IConfigurationCircuitSupport;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddInventorySlots;
import gregtech.api.interfaces.modularui.IGetBackground;
import gregtech.api.interfaces.modularui.IGetFluidSlotBackground;
import gregtech.api.interfaces.modularui.IGetGregtechLogo;
import gregtech.api.interfaces.modularui.IGetSlotBackground;
import gregtech.api.interfaces.modularui.IGetTabIconSet;
import gregtech.api.interfaces.tileentity.IGTEnet;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.interfaces.tileentity.IIC2Enet;
import gregtech.api.net.GT_Packet_Block_Event;
import gregtech.api.net.GT_Packet_SetConfigurationCircuit;
import gregtech.api.util.GT_TooltipDataCache;
import gregtech.api.util.GT_Util;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.uifactory.SelectItemUIFactory;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

/**
 * The Functions my old TileEntities and my BaseMetaTileEntities have in common.
 * <p/>
 * Basically everything a TileEntity should have.
 */
public abstract class BaseTileEntity extends TileEntity
        implements IHasWorldObjectAndCoords,
                IIC2Enet,
                IGTEnet,
                ITileWithModularUI,
                IAddGregtechLogo,
                IGetGregtechLogo,
                IGetSlotBackground,
                IGetFluidSlotBackground,
                IGetBackground,
                IGetTabIconSet,
                IAddInventorySlots {
    protected boolean mInventoryChanged = false;

    /**
     * Buffers adjacent TileEntities for faster access
     * <p/>
     * "this" means that there is no TileEntity, while "null" means that it doesn't know if there is even a TileEntity and still needs to check that if needed.
     */
    private final TileEntity[] mBufferedTileEntities = new TileEntity[6];
    /**
     * If this TileEntity checks for the Chunk to be loaded before returning World based values.
     * The AdvPump hacks this to false to ensure everything runs properly even when far Chunks are not actively loaded.
     * But anything else should not cause worfin' Chunks, uhh I mean orphan Chunks.
     */
    public boolean ignoreUnloadedChunks = true;
    /**
     * This Variable checks if this TileEntity is dead, because Minecraft is too stupid to have proper TileEntity unloading.
     */
    public boolean isDead = false;

    private final ChunkCoordinates mReturnedCoordinates = new ChunkCoordinates();

    public static byte getSideForPlayerPlacing(Entity aPlayer, byte aDefaultFacing, boolean[] aAllowedFacings) {
        if (aPlayer != null) {
            if (aPlayer.rotationPitch >= 65 && aAllowedFacings[SIDE_UP]) return SIDE_UP;
            if (aPlayer.rotationPitch <= -65 && aAllowedFacings[SIDE_DOWN]) return SIDE_DOWN;
            final byte rFacing =
                    COMPASS_DIRECTIONS[MathHelper.floor_double(0.5D + 4.0F * aPlayer.rotationYaw / 360.0F) & 0x3];
            if (aAllowedFacings[rFacing]) return rFacing;
        }
        for (final byte tSide : ALL_VALID_SIDES) if (aAllowedFacings[tSide]) return tSide;
        return aDefaultFacing;
    }

    private void clearNullMarkersFromTileEntityBuffer() {
        for (int i = 0; i < mBufferedTileEntities.length; i++)
            if (mBufferedTileEntities[i] == this) mBufferedTileEntities[i] = null;
    }

    /**
     * Called automatically when the Coordinates of this TileEntity have been changed
     */
    protected final void clearTileEntityBuffer() {
        Arrays.fill(mBufferedTileEntities, null);
    }

    @Override
    public final World getWorld() {
        return worldObj;
    }

    @Override
    public final int getXCoord() {
        return xCoord;
    }

    @Override
    public final short getYCoord() {
        return (short) yCoord;
    }

    @Override
    public final int getZCoord() {
        return zCoord;
    }

    @Override
    public ChunkCoordinates getCoords() {
        mReturnedCoordinates.posX = xCoord;
        mReturnedCoordinates.posY = yCoord;
        mReturnedCoordinates.posZ = zCoord;
        return mReturnedCoordinates;
    }

    @Override
    public final int getOffsetX(byte aSide, int aMultiplier) {
        return xCoord + ForgeDirection.getOrientation(aSide).offsetX * aMultiplier;
    }

    @Override
    public final short getOffsetY(byte aSide, int aMultiplier) {
        return (short) (yCoord + ForgeDirection.getOrientation(aSide).offsetY * aMultiplier);
    }

    @Override
    public final int getOffsetZ(byte aSide, int aMultiplier) {
        return zCoord + ForgeDirection.getOrientation(aSide).offsetZ * aMultiplier;
    }

    @Override
    public final boolean isServerSide() {
        return !worldObj.isRemote;
    }

    @Override
    public final boolean isClientSide() {
        return worldObj.isRemote;
    }

    @Override
    @Deprecated
    public final boolean openGUI(EntityPlayer aPlayer) {
        return openGUI(aPlayer, 0);
    }

    @Override
    @Deprecated
    public final boolean openGUI(EntityPlayer aPlayer, int aID) {
        if (aPlayer == null) return false;
        aPlayer.openGui(GT, aID, worldObj, xCoord, yCoord, zCoord);
        return true;
    }

    @Override
    public boolean isInvalidTileEntity() {
        return isInvalid();
    }

    @Override
    public int getRandomNumber(int aRange) {
        return ThreadLocalRandom.current().nextInt(aRange);
    }

    @Override
    public final BiomeGenBase getBiome(int aX, int aZ) {
        return worldObj.getBiomeGenForCoords(aX, aZ);
    }

    @Override
    public final BiomeGenBase getBiome() {
        return getBiome(xCoord, zCoord);
    }

    @Override
    public final Block getBlockOffset(int aX, int aY, int aZ) {
        return getBlock(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final Block getBlockAtSide(byte aSide) {
        return getBlockAtSideAndDistance(aSide, 1);
    }

    @Override
    public final Block getBlockAtSideAndDistance(byte aSide, int aDistance) {
        return getBlock(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final byte getMetaIDOffset(int aX, int aY, int aZ) {
        return getMetaID(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final byte getMetaIDAtSide(byte aSide) {
        return getMetaIDAtSideAndDistance(aSide, 1);
    }

    @Override
    public final byte getMetaIDAtSideAndDistance(byte aSide, int aDistance) {
        return getMetaID(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final byte getLightLevelOffset(int aX, int aY, int aZ) {
        return getLightLevel(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final byte getLightLevelAtSide(byte aSide) {
        return getLightLevelAtSideAndDistance(aSide, 1);
    }

    @Override
    public final byte getLightLevelAtSideAndDistance(byte aSide, int aDistance) {
        return getLightLevel(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final boolean getOpacityOffset(int aX, int aY, int aZ) {
        return getOpacity(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final boolean getOpacityAtSide(byte aSide) {
        return getOpacityAtSideAndDistance(aSide, 1);
    }

    @Override
    public final boolean getOpacityAtSideAndDistance(byte aSide, int aDistance) {
        return getOpacity(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final boolean getSkyOffset(int aX, int aY, int aZ) {
        return getSky(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final boolean getSkyAtSide(byte aSide) {
        return getSkyAtSideAndDistance(aSide, 1);
    }

    @Override
    public final boolean getSkyAtSideAndDistance(byte aSide, int aDistance) {
        return getSky(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final boolean getAirOffset(int aX, int aY, int aZ) {
        return getAir(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final boolean getAirAtSide(byte aSide) {
        return getAirAtSideAndDistance(aSide, 1);
    }

    @Override
    public final boolean getAirAtSideAndDistance(byte aSide, int aDistance) {
        return getAir(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final TileEntity getTileEntityOffset(int aX, int aY, int aZ) {
        return getTileEntity(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final TileEntity getTileEntityAtSideAndDistance(byte aSide, int aDistance) {
        if (aDistance == 1) return getTileEntityAtSide(aSide);
        return getTileEntity(getOffsetX(aSide, aDistance), getOffsetY(aSide, aDistance), getOffsetZ(aSide, aDistance));
    }

    @Override
    public final IInventory getIInventory(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IInventory) return (IInventory) tTileEntity;
        return null;
    }

    @Override
    public final IInventory getIInventoryOffset(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntityOffset(aX, aY, aZ);
        if (tTileEntity instanceof IInventory) return (IInventory) tTileEntity;
        return null;
    }

    @Override
    public final IInventory getIInventoryAtSide(byte aSide) {
        final TileEntity tTileEntity = getTileEntityAtSide(aSide);
        if (tTileEntity instanceof IInventory) return (IInventory) tTileEntity;
        return null;
    }

    @Override
    public final IInventory getIInventoryAtSideAndDistance(byte aSide, int aDistance) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(aSide, aDistance);
        if (tTileEntity instanceof IInventory) return (IInventory) tTileEntity;
        return null;
    }

    @Override
    public final IFluidHandler getITankContainer(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IFluidHandler) return (IFluidHandler) tTileEntity;
        return null;
    }

    @Override
    public final IFluidHandler getITankContainerOffset(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntityOffset(aX, aY, aZ);
        if (tTileEntity instanceof IFluidHandler) return (IFluidHandler) tTileEntity;
        return null;
    }

    @Override
    public final IFluidHandler getITankContainerAtSide(byte aSide) {
        final TileEntity tTileEntity = getTileEntityAtSide(aSide);
        if (tTileEntity instanceof IFluidHandler) return (IFluidHandler) tTileEntity;
        return null;
    }

    @Override
    public final IFluidHandler getITankContainerAtSideAndDistance(byte aSide, int aDistance) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(aSide, aDistance);
        if (tTileEntity instanceof IFluidHandler) return (IFluidHandler) tTileEntity;
        return null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntity(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntity(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity) return (IGregTechTileEntity) tTileEntity;
        return null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityOffset(int aX, int aY, int aZ) {
        final TileEntity tTileEntity = getTileEntityOffset(aX, aY, aZ);
        if (tTileEntity instanceof IGregTechTileEntity) return (IGregTechTileEntity) tTileEntity;
        return null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityAtSide(byte aSide) {
        final TileEntity tTileEntity = getTileEntityAtSide(aSide);
        if (tTileEntity instanceof IGregTechTileEntity) return (IGregTechTileEntity) tTileEntity;
        return null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(byte aSide, int aDistance) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(aSide, aDistance);
        if (tTileEntity instanceof IGregTechTileEntity) return (IGregTechTileEntity) tTileEntity;
        return null;
    }

    @Override
    public final Block getBlock(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return Blocks.air;
        return worldObj.getBlock(aX, aY, aZ);
    }

    public Block getBlock(ChunkCoordinates aCoords) {
        if (worldObj == null) return Blocks.air;
        if (ignoreUnloadedChunks
                && crossedChunkBorder(aCoords)
                && !worldObj.blockExists(aCoords.posX, aCoords.posY, aCoords.posZ)) return Blocks.air;
        return worldObj.getBlock(aCoords.posX, aCoords.posY, aCoords.posZ);
    }

    @Override
    public final byte getMetaID(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return 0;
        return (byte) worldObj.getBlockMetadata(aX, aY, aZ);
    }

    @Override
    public final byte getLightLevel(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return 0;
        return (byte) (worldObj.getLightBrightness(aX, aY, aZ) * 15);
    }

    @Override
    public final boolean getSky(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return true;
        return worldObj.canBlockSeeTheSky(aX, aY, aZ);
    }

    @Override
    public final boolean getOpacity(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return false;
        return GT_Utility.isOpaqueBlock(worldObj, aX, aY, aZ);
    }

    @Override
    public final boolean getAir(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return true;
        return GT_Utility.isBlockAir(worldObj, aX, aY, aZ);
    }

    @Override
    public TileEntity getTileEntity(int aX, int aY, int aZ) {
        if (ignoreUnloadedChunks && crossedChunkBorder(aX, aZ) && !worldObj.blockExists(aX, aY, aZ)) return null;
        return worldObj.getTileEntity(aX, aY, aZ);
    }

    @Override
    public final TileEntity getTileEntityAtSide(byte aSide) {
        if (aSide < 0 || aSide >= 6 || mBufferedTileEntities[aSide] == this) return null;
        final int tX = getOffsetX(aSide, 1);
        final int tY = getOffsetY(aSide, 1);
        final int tZ = getOffsetZ(aSide, 1);
        if (crossedChunkBorder(tX, tZ)) {
            mBufferedTileEntities[aSide] = null;
            if (ignoreUnloadedChunks && !worldObj.blockExists(tX, tY, tZ)) return null;
        }
        if (mBufferedTileEntities[aSide] == null) {
            mBufferedTileEntities[aSide] = worldObj.getTileEntity(tX, tY, tZ);
            if (mBufferedTileEntities[aSide] == null) {
                mBufferedTileEntities[aSide] = this;
                return null;
            }
            return mBufferedTileEntities[aSide];
        }
        if (mBufferedTileEntities[aSide].isInvalid()) {
            mBufferedTileEntities[aSide] = null;
            return getTileEntityAtSide(aSide);
        }
        if (mBufferedTileEntities[aSide].xCoord == tX
                && mBufferedTileEntities[aSide].yCoord == tY
                && mBufferedTileEntities[aSide].zCoord == tZ) {
            return mBufferedTileEntities[aSide];
        }
        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
    }

    @Override
    public boolean isDead() {
        return isDead || isInvalidTileEntity();
    }

    @Override
    public void validate() {
        clearNullMarkersFromTileEntityBuffer();
        super.validate();
    }

    @Override
    public void invalidate() {
        leaveEnet();
        clearNullMarkersFromTileEntityBuffer();
        super.invalidate();
    }

    @Override
    public void onChunkUnload() {
        leaveEnet();
        clearNullMarkersFromTileEntityBuffer();
        super.onChunkUnload();
        isDead = true;
    }

    @Override
    public void updateEntity() {
        // Well if the TileEntity gets ticked it is alive.
        isDead = false;
    }

    public final void onAdjacentBlockChange(int aX, int aY, int aZ) {
        clearNullMarkersFromTileEntityBuffer();
    }

    public void updateNeighbours(int mStrongRedstone, int oStrongRedstone) {
        final Block thisBlock = getBlockOffset(0, 0, 0);
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            final int x1 = xCoord + dir.offsetX, y1 = yCoord + dir.offsetY, z1 = zCoord + dir.offsetZ;

            if (worldObj.blockExists(x1, y1, z1)) {
                worldObj.notifyBlockOfNeighborChange(x1, y1, z1, thisBlock);

                // update if it was / is strong powered.
                if (((((mStrongRedstone | oStrongRedstone) >>> dir.ordinal()) & 1) != 0)
                        && getBlock(x1, y1, z1).isNormalCube()) {
                    final int skipUpdateSide = dir.getOpposite()
                            .ordinal(); // Don't update this block. Still updates diagonal blocks twice if conditions
                    // meet.

                    for (final ForgeDirection dir2 : ForgeDirection.VALID_DIRECTIONS) {
                        final int x2 = x1 + dir2.offsetX, y2 = y1 + dir2.offsetY, z2 = z1 + dir2.offsetZ;
                        if (dir2.ordinal() != skipUpdateSide && worldObj.blockExists(x2, y2, z2))
                            worldObj.notifyBlockOfNeighborChange(x2, y2, z2, thisBlock);
                    }
                }
            }
        }
    }

    @Override
    public final void sendBlockEvent(byte aID, byte aValue) {
        NW.sendPacketToAllPlayersInRange(
                worldObj, new GT_Packet_Block_Event(xCoord, (short) yCoord, zCoord, aID, aValue), xCoord, zCoord);
    }

    protected boolean crossedChunkBorder(int aX, int aZ) {
        return aX >> 4 != xCoord >> 4 || aZ >> 4 != zCoord >> 4;
    }

    public final boolean crossedChunkBorder(ChunkCoordinates aCoords) {
        return aCoords.posX >> 4 != xCoord >> 4 || aCoords.posZ >> 4 != zCoord >> 4;
    }

    public final void setOnFire() {
        GT_Utility.setCoordsOnFire(worldObj, xCoord, yCoord, zCoord, false);
    }

    public final void setToFire() {
        worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.fire);
    }

    @Override
    public void markDirty() {
        // Avoid sending neighbor updates, just mark the chunk as dirty to make sure it gets saved
        final Chunk chunk = worldObj.getChunkFromBlockCoords(xCoord, zCoord);
        if (chunk != null) {
            chunk.setChunkModified();
        }
    }

    @Deprecated
    public String trans(String aKey, String aEnglish) {
        return GT_Utility.trans(aKey, aEnglish);
    }

    protected Supplier<Boolean> getValidator() {
        return () -> !this.isDead();
    }

    public boolean useModularUI() {
        return false;
    }

    @Override
    public ModularWindow createWindow(UIBuildContext buildContext) {
        if (!useModularUI()) return null;

        buildContext.setValidator(getValidator());
        final ModularWindow.Builder builder = ModularWindow.builder(getGUIWidth(), getGUIHeight());
        builder.setBackground(getBackground());
        builder.setGuiTint(getGUIColorization());
        if (doesBindPlayerInventory()) {
            builder.bindPlayerInventory(buildContext.getPlayer(), 7, getSlotBackground());
        }
        addUIWidgets(builder, buildContext);
        addTitleToUI(builder);
        addCoverTabs(builder, buildContext);
        final IConfigurationCircuitSupport csc = getConfigurationCircuitSupport();
        if (csc != null && csc.allowSelectCircuit()) {
            addConfigurationCircuitSlot(builder);
        } else {
            addGregTechLogo(builder);
        }
        return builder.build();
    }

    /*
     * IC2 Energy Compat
     */
    protected TileIC2EnergySink ic2EnergySink = null;
    protected boolean joinedIc2Enet = false;

    protected void createIc2Sink() {
        if (ic2EnergySink == null && isServerSide() && shouldJoinIc2Enet()) {
            ic2EnergySink = new TileIC2EnergySink((IGregTechTileEntity) this);
        }
    }

    @Override
    public void doEnetUpdate() {
        leaveEnet();
        joinEnet();
    }

    protected void joinEnet() {
        if (joinedIc2Enet || !shouldJoinIc2Enet()) return;

        if (ic2EnergySink == null) createIc2Sink();

        if (ic2EnergySink != null) {
            MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(ic2EnergySink));
            joinedIc2Enet = true;
        }
    }

    protected void leaveEnet() {
        if (joinedIc2Enet && ic2EnergySink != null && isServerSide()) {
            joinedIc2Enet = false;
            MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(ic2EnergySink));
        }
    }

    // === GUI stuff ===

    public ItemStackHandler getInventoryHandler() {
        return null;
    }

    protected GT_TooltipDataCache mTooltipCache = new GT_TooltipDataCache();

    // Tooltip localization keys
    public static final String BATTERY_SLOT_TOOLTIP = "GT5U.machines.battery_slot.tooltip",
            BATTERY_SLOT_TOOLTIP_ALT = "GT5U.machines.battery_slot.tooltip.alternative",
            UNUSED_SLOT_TOOLTIP = "GT5U.machines.unused_slot.tooltip",
            SPECIAL_SLOT_TOOLTIP = "GT5U.machines.special_slot.tooltip",
            FLUID_INPUT_TOOLTIP = "GT5U.machines.fluid_input_slot.tooltip",
            FLUID_OUTPUT_TOOLTIP = "GT5U.machines.fluid_output_slot.tooltip",
            STALLED_STUTTERING_TOOLTIP = "GT5U.machines.stalled_stuttering.tooltip",
            STALLED_VENT_TOOLTIP = "GT5U.machines.stalled_vent.tooltip",
            FLUID_TRANSFER_TOOLTIP = "GT5U.machines.fluid_transfer.tooltip",
            ITEM_TRANSFER_TOOLTIP = "GT5U.machines.item_transfer.tooltip",
            POWER_SOURCE_KEY = "GT5U.machines.powersource.",
            NEI_TRANSFER_STEAM_TOOLTIP = "GT5U.machines.nei_transfer.steam.tooltip",
            NEI_TRANSFER_VOLTAGE_TOOLTIP = "GT5U.machines.nei_transfer.voltage.tooltip";

    public static final int TOOLTIP_DELAY = 5;

    /**
     * Override this to add {@link com.gtnewhorizons.modularui.api.widget.Widget}s for your UI.
     */
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {}

    public String getLocalName() {
        return "Unknown";
    }

    protected void addTitleToUI(ModularWindow.Builder builder) {
        addTitleToUI(builder, getLocalName());
    }

    protected void addTitleToUI(ModularWindow.Builder builder, String title) {
        if (GT_Mod.gregtechproxy.mTitleTabStyle == 2) {
            addTitleItemIconStyle(builder, title);
        } else {
            addTitleTextStyle(builder, title);
        }
    }

    protected void addTitleTextStyle(ModularWindow.Builder builder, String title) {
        final int TAB_PADDING = 3;
        final int TITLE_PADDING = 2;
        int titleWidth = 0, titleHeight = 0;
        if (NetworkUtils.isClient()) {
            final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            //noinspection unchecked
            final List<String> titleLines =
                    fontRenderer.listFormattedStringToWidth(title, getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2);
            titleWidth = titleLines.size() > 1
                    ? getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2
                    : fontRenderer.getStringWidth(title);
            //noinspection PointlessArithmeticExpression
            titleHeight = titleLines.size() * fontRenderer.FONT_HEIGHT + (titleLines.size() - 1) * 1;
        }

        final DrawableWidget tab = new DrawableWidget();
        final TextWidget text = new TextWidget(title)
                .setDefaultColor(getTitleColor())
                .setTextAlignment(Alignment.CenterLeft)
                .setMaxWidth(titleWidth);
        if (GT_Mod.gregtechproxy.mTitleTabStyle == 1) {
            tab.setDrawable(getTabIconSet().titleNormal)
                    .setPos(0, -(titleHeight + TAB_PADDING) + 1)
                    .setSize(getGUIWidth(), titleHeight + TAB_PADDING * 2);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight + TAB_PADDING);
        } else {
            tab.setDrawable(getTabIconSet().titleDark)
                    .setPos(0, -(titleHeight + TAB_PADDING * 2) + 1)
                    .setSize(titleWidth + (TAB_PADDING + TITLE_PADDING) * 2, titleHeight + TAB_PADDING * 2 - 1);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight);
        }
        builder.widget(tab).widget(text);
    }

    protected void addTitleItemIconStyle(ModularWindow.Builder builder, String title) {
        builder.widget(new MultiChildWidget()
                .addChild(new DrawableWidget()
                        .setDrawable(getTabIconSet().titleNormal)
                        .setPos(0, 0)
                        .setSize(24, 24))
                .addChild(new ItemDrawable(getStackForm(1)).asWidget().setPos(4, 4))
                .addTooltip(title)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(0, -24 + 3));
    }

    @Override
    public BaseTileEntity.GT_GuiTabIconSet getTabIconSet() {
        return new BaseTileEntity.GT_GuiTabIconSet(
                GT_UITextures.TAB_COVER_NORMAL,
                GT_UITextures.TAB_COVER_HIGHLIGHT,
                GT_UITextures.TAB_COVER_DISABLED,
                GT_UITextures.TAB_TITLE,
                GT_UITextures.TAB_TITLE_DARK);
    }

    protected int getTitleColor() {
        return COLOR_TITLE.get();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(new DrawableWidget()
                .setDrawable(getGregTechLogo())
                .setSize(17, 17)
                .setPos(152, 63));
    }

    @Override
    public IDrawable getGregTechLogo() {
        return GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT;
    }

    @Override
    public UITexture getBackground() {
        return GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT;
    }

    protected int getGUIWidth() {
        return 176;
    }

    protected int getGUIHeight() {
        return 166;
    }

    protected boolean doesBindPlayerInventory() {
        return true;
    }

    @Override
    public IDrawable getSlotBackground() {
        return ModularUITextures.ITEM_SLOT;
    }

    @Override
    public IDrawable getFluidSlotBackground() {
        return ModularUITextures.FLUID_SLOT;
    }

    @Override
    public void add1by1Slot(ModularWindow.Builder builder, IDrawable... background) {
        final ItemStackHandler inventoryHandler = getInventoryHandler();
        if (inventoryHandler == null) return;

        if (background.length == 0) {
            background = new IDrawable[] {getSlotBackground()};
        }
        builder.widget(SlotGroup.ofItemHandler(inventoryHandler, 1)
                .startFromSlot(0)
                .endAtSlot(0)
                .background(background)
                .build()
                .setPos(79, 34));
    }

    @Override
    public void add2by2Slots(ModularWindow.Builder builder, IDrawable... background) {
        final ItemStackHandler inventoryHandler = getInventoryHandler();
        if (inventoryHandler == null) return;

        if (background.length == 0) {
            background = new IDrawable[] {getSlotBackground()};
        }
        builder.widget(SlotGroup.ofItemHandler(inventoryHandler, 2)
                .startFromSlot(0)
                .endAtSlot(3)
                .background(background)
                .build()
                .setPos(70, 25));
    }

    @Override
    public void add3by3Slots(ModularWindow.Builder builder, IDrawable... background) {
        final ItemStackHandler inventoryHandler = getInventoryHandler();
        if (inventoryHandler == null) return;

        if (background.length == 0) {
            background = new IDrawable[] {getSlotBackground()};
        }
        builder.widget(SlotGroup.ofItemHandler(inventoryHandler, 3)
                .startFromSlot(0)
                .endAtSlot(8)
                .background(background)
                .build()
                .setPos(61, 16));
    }

    @Override
    public void add4by4Slots(ModularWindow.Builder builder, IDrawable... background) {
        final ItemStackHandler inventoryHandler = getInventoryHandler();
        if (inventoryHandler == null) return;

        if (background.length == 0) {
            background = new IDrawable[] {getSlotBackground()};
        }
        builder.widget(SlotGroup.ofItemHandler(inventoryHandler, 4)
                .startFromSlot(0)
                .endAtSlot(15)
                .background(background)
                .build()
                .setPos(52, 7));
    }

    public void addCoverTabs(ModularWindow.Builder builder, UIBuildContext buildContext) {
        /* Do nothing */
    }

    /**
     * Defines a set of textures a tab line can use to render its tab backgrounds
     */
    public static class GT_GuiTabIconSet {
        protected final UITexture coverNormal;
        protected final UITexture coverHighlight;
        protected final UITexture coverDisabled;
        protected final UITexture coverNormalFlipped;
        protected final UITexture coverHighlightFlipped;
        protected final UITexture coverDisabledFlipped;
        protected final AdaptableUITexture titleNormal;
        protected final AdaptableUITexture titleDark;

        public GT_GuiTabIconSet(
                UITexture coverNormal,
                UITexture coverHighlight,
                UITexture coverDisabled,
                AdaptableUITexture titleNormal,
                AdaptableUITexture titleDark) {
            this.coverNormal = coverNormal;
            this.coverHighlight = coverHighlight;
            this.coverDisabled = coverDisabled;
            this.coverNormalFlipped = coverNormal.getFlipped(true, false);
            this.coverHighlightFlipped = coverHighlight.getFlipped(true, false);
            this.coverDisabledFlipped = coverDisabled.getFlipped(true, false);
            this.titleNormal = titleNormal;
            this.titleDark = titleDark;
        }
    }

    public IConfigurationCircuitSupport getConfigurationCircuitSupport() {
        if (!(this instanceof IConfigurationCircuitSupport)) return null;
        return (IConfigurationCircuitSupport) this;
    }

    protected void addConfigurationCircuitSlot(ModularWindow.Builder builder) {
        final ItemStackHandler inventoryHandler = getInventoryHandler();
        if (inventoryHandler == null) return;

        if (!(this instanceof IInventory)) return;
        final IInventory inv = (IInventory) this;

        final IConfigurationCircuitSupport ccs = getConfigurationCircuitSupport();
        if (ccs == null) return;

        final AtomicBoolean dialogOpened = new AtomicBoolean(false);
        builder.widget(
                new SlotWidget(new BaseSlot(inventoryHandler, ccs.getCircuitSlot(), true)) {
                    @Override
                    protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                        final ItemStack newCircuit;
                        if (clickData.shift) {
                            if (clickData.mouseButton == 0) {
                                if (NetworkUtils.isClient() && !dialogOpened.get()) {
                                    openSelectCircuitDialog(getContext(), dialogOpened);
                                }
                                return;
                            } else {
                                newCircuit = null;
                            }
                        } else {
                            final List<ItemStack> tCircuits = ccs.getConfigurationCircuits();
                            final int index = GT_Utility.findMatchingStackInList(tCircuits, cursorStack);
                            if (index < 0) {
                                int curIndex = GT_Utility.findMatchingStackInList(
                                                tCircuits, inv.getStackInSlot(ccs.getCircuitSlot()))
                                        + 1;
                                if (clickData.mouseButton == 0) {
                                    curIndex += 1;
                                } else {
                                    curIndex -= 1;
                                }
                                curIndex = Math.floorMod(curIndex, tCircuits.size() + 1) - 1;
                                newCircuit = curIndex < 0 ? null : tCircuits.get(curIndex);
                            } else {
                                // set to whatever it is
                                newCircuit = tCircuits.get(index);
                            }
                        }
                        inv.setInventorySlotContents(ccs.getCircuitSlot(), newCircuit);
                    }

                    @Override
                    protected void phantomScroll(int direction) {
                        phantomClick(new ClickData(direction > 0 ? 1 : 0, false, false, false));
                    }

                    @Override
                    public List<String> getExtraTooltip() {
                        return Arrays.asList(
                                EnumChatFormatting.DARK_GRAY
                                        + EnumChatFormatting.getTextWithoutFormattingCodes(
                                                StatCollector.translateToLocal(
                                                        "GT5U.machines.select_circuit.tooltip.1")),
                                EnumChatFormatting.DARK_GRAY
                                        + EnumChatFormatting.getTextWithoutFormattingCodes(
                                                StatCollector.translateToLocal(
                                                        "GT5U.machines.select_circuit.tooltip.2")),
                                EnumChatFormatting.DARK_GRAY
                                        + EnumChatFormatting.getTextWithoutFormattingCodes(
                                                StatCollector.translateToLocal(
                                                        "GT5U.machines.select_circuit.tooltip.3")));
                    }
                }.setOverwriteItemStackTooltip(list -> {
                            list.removeIf(line ->
                                    line.contains(StatCollector.translateToLocal("gt.integrated_circuit.tooltip.0"))
                                            || line.contains(
                                                    StatCollector.translateToLocal("gt.integrated_circuit.tooltip.1")));
                            return list;
                        })
                        .disableShiftInsert()
                        .setHandlePhantomActionClient(true)
                        .setBackground(getSlotBackground(), GT_UITextures.OVERLAY_SLOT_INT_CIRCUIT)
                        .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.select_circuit.tooltip"))
                        .setTooltipShowUpDelay(TOOLTIP_DELAY)
                        .setPos(ccs.getCircuitSlotX() - 1, ccs.getCircuitSlotY() - 1));
    }

    protected void openSelectCircuitDialog(ModularUIContext uiContext, AtomicBoolean dialogOpened) {
        if (!(this instanceof IConfigurationCircuitSupport)) return;
        final IConfigurationCircuitSupport ccs = (IConfigurationCircuitSupport) this;

        if (!(this instanceof IInventory)) return;
        final IInventory inv = (IInventory) this;

        final List<ItemStack> circuits = ccs.getConfigurationCircuits();
        uiContext.openClientWindow(player -> new SelectItemUIFactory(
                        StatCollector.translateToLocal("GT5U.machines.select_circuit"),
                        getStackForm(0),
                        this::onCircuitSelected,
                        circuits,
                        GT_Utility.findMatchingStackInList(circuits, inv.getStackInSlot(ccs.getCircuitSlot())))
                .setAnotherWindow(true, dialogOpened)
                .setGuiTint(getGUIColorization())
                .createWindow(new UIBuildContext(player)));
    }

    protected void onCircuitSelected(ItemStack selected) {
        if (!(this instanceof IConfigurationCircuitSupport)) return;
        final IConfigurationCircuitSupport ccs = (IConfigurationCircuitSupport) this;

        if (!(this instanceof IInventory)) return;
        final IInventory inv = (IInventory) this;

        GT_Values.NW.sendToServer(new GT_Packet_SetConfigurationCircuit(this, selected));
        // we will not do any validation on client side
        // it doesn't get to actually decide what inventory contains anyway
        inv.setInventorySlotContents(ccs.getCircuitSlot(), selected);
    }

    protected int getTextColorOrDefault(String textType, int defaultColor) {
        return defaultColor;
    }

    protected Supplier<Integer> COLOR_TITLE = () -> getTextColorOrDefault("title", 0x404040);
    protected Supplier<Integer> COLOR_TITLE_WHITE = () -> getTextColorOrDefault("title_white", 0xfafaff);
    protected Supplier<Integer> COLOR_TEXT_WHITE = () -> getTextColorOrDefault("text_white", 0xfafaff);
    protected Supplier<Integer> COLOR_TEXT_GRAY = () -> getTextColorOrDefault("text_gray", 0x404040);
    protected Supplier<Integer> COLOR_TEXT_RED = () -> getTextColorOrDefault("text_red", 0xff0000);

    public int getGUIColorization() {
        return GT_Util.getRGBaInt(Dyes.dyeWhite.getRGBA());
    }

    public ItemStack getStackForm(long aAmount) {
        return null;
    }
}
