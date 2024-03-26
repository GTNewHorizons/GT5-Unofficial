package gregtech.api.metatileentity;

import static gregtech.api.enums.GT_Values.GT;
import static gregtech.api.enums.GT_Values.NW;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import javax.annotation.Nullable;

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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
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

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddInventorySlots;
import gregtech.api.interfaces.modularui.IGetGUITextureSet;
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

/**
 * The Functions my old TileEntities and my BaseMetaTileEntities have in common.
 * <p/>
 * Basically everything a TileEntity should have.
 */
public abstract class BaseTileEntity extends TileEntity implements IHasWorldObjectAndCoords, IIC2Enet, IGTEnet,
    ITileWithModularUI, IAddGregtechLogo, IGetGUITextureSet, IAddInventorySlots {

    protected boolean mInventoryChanged = false;

    /**
     * Buffers adjacent TileEntities for faster access
     * <p/>
     * "this" means that there is no TileEntity, while "null" means that it doesn't know if there is even a TileEntity
     * and still needs to check that if needed.
     */
    private final TileEntity[] mBufferedTileEntities = new TileEntity[6];
    /**
     * If this TileEntity checks for the Chunk to be loaded before returning World based values. The AdvPump hacks this
     * to false to ensure everything runs properly even when far Chunks are not actively loaded. But anything else
     * should not cause worfin' Chunks, uhh I mean orphan Chunks.
     */
    public boolean ignoreUnloadedChunks = true;
    /**
     * This Variable checks if this TileEntity is dead, because Minecraft is too stupid to have proper TileEntity
     * unloading.
     */
    public boolean isDead = false;

    private final ChunkCoordinates mReturnedCoordinates = new ChunkCoordinates();

    public static ForgeDirection getSideForPlayerPlacing(Entity aPlayer, ForgeDirection defaultFacing,
        boolean[] aAllowedFacings) {
        ForgeDirection facingFromPlayer = GT_Utility.getSideFromPlayerFacing(aPlayer);
        if (facingFromPlayer != ForgeDirection.UNKNOWN && aAllowedFacings[facingFromPlayer.ordinal()])
            return facingFromPlayer;
        for (final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) if (aAllowedFacings[dir.ordinal()]) return dir;
        return defaultFacing;
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
    public final int getOffsetX(ForgeDirection side, int aMultiplier) {
        return xCoord + side.offsetX * aMultiplier;
    }

    @Override
    public final short getOffsetY(ForgeDirection side, int aMultiplier) {
        return (short) (yCoord + side.offsetY * aMultiplier);
    }

    @Override
    public final int getOffsetZ(ForgeDirection side, int aMultiplier) {
        return zCoord + side.offsetZ * aMultiplier;
    }

    @Override
    public final boolean isServerSide() {
        if (worldObj == null) {
            return FMLCommonHandler.instance()
                .getEffectiveSide() == Side.SERVER;
        }
        return !worldObj.isRemote;
    }

    @Override
    public final boolean isClientSide() {
        if (worldObj == null) {
            return FMLCommonHandler.instance()
                .getEffectiveSide() == Side.CLIENT;
        }
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
        return ThreadLocalRandom.current()
            .nextInt(aRange);
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
    public final Block getBlockAtSide(ForgeDirection side) {
        return getBlockAtSideAndDistance(side, 1);
    }

    @Override
    public final Block getBlockAtSideAndDistance(ForgeDirection side, int aDistance) {
        return getBlock(getOffsetX(side, aDistance), getOffsetY(side, aDistance), getOffsetZ(side, aDistance));
    }

    @Override
    public final byte getMetaIDOffset(int aX, int aY, int aZ) {
        return getMetaID(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final byte getMetaIDAtSide(ForgeDirection side) {
        return getMetaIDAtSideAndDistance(side, 1);
    }

    @Override
    public final byte getMetaIDAtSideAndDistance(ForgeDirection side, int aDistance) {
        return getMetaID(getOffsetX(side, aDistance), getOffsetY(side, aDistance), getOffsetZ(side, aDistance));
    }

    @Override
    public final byte getLightLevelOffset(int aX, int aY, int aZ) {
        return getLightLevel(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final byte getLightLevelAtSide(ForgeDirection side) {
        return getLightLevelAtSideAndDistance(side, 1);
    }

    @Override
    public final byte getLightLevelAtSideAndDistance(ForgeDirection side, int aDistance) {
        return getLightLevel(getOffsetX(side, aDistance), getOffsetY(side, aDistance), getOffsetZ(side, aDistance));
    }

    @Override
    public final boolean getOpacityOffset(int aX, int aY, int aZ) {
        return getOpacity(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final boolean getOpacityAtSide(ForgeDirection side) {
        return getOpacityAtSideAndDistance(side, 1);
    }

    @Override
    public final boolean getOpacityAtSideAndDistance(ForgeDirection side, int aDistance) {
        return getOpacity(getOffsetX(side, aDistance), getOffsetY(side, aDistance), getOffsetZ(side, aDistance));
    }

    @Override
    public final boolean getSkyOffset(int aX, int aY, int aZ) {
        return getSky(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final boolean getSkyAtSide(ForgeDirection side) {
        return getSkyAtSideAndDistance(side, 1);
    }

    @Override
    public final boolean getSkyAtSideAndDistance(ForgeDirection side, int aDistance) {
        return getSky(getOffsetX(side, aDistance), getOffsetY(side, aDistance), getOffsetZ(side, aDistance));
    }

    @Override
    public final boolean getAirOffset(int aX, int aY, int aZ) {
        return getAir(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final boolean getAirAtSide(ForgeDirection side) {
        return getAirAtSideAndDistance(side, 1);
    }

    @Override
    public final boolean getAirAtSideAndDistance(ForgeDirection side, int aDistance) {
        return getAir(getOffsetX(side, aDistance), getOffsetY(side, aDistance), getOffsetZ(side, aDistance));
    }

    @Override
    public final TileEntity getTileEntityOffset(int aX, int aY, int aZ) {
        return getTileEntity(xCoord + aX, yCoord + aY, zCoord + aZ);
    }

    @Override
    public final TileEntity getTileEntityAtSideAndDistance(ForgeDirection side, int aDistance) {
        if (aDistance == 1) return getTileEntityAtSide(side);
        return getTileEntity(getOffsetX(side, aDistance), getOffsetY(side, aDistance), getOffsetZ(side, aDistance));
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
    public final IInventory getIInventoryAtSide(ForgeDirection side) {
        final TileEntity tTileEntity = getTileEntityAtSide(side);
        if (tTileEntity instanceof IInventory) return (IInventory) tTileEntity;
        return null;
    }

    @Override
    public final IInventory getIInventoryAtSideAndDistance(ForgeDirection side, int aDistance) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(side, aDistance);
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
    public final IFluidHandler getITankContainerAtSide(ForgeDirection side) {
        final TileEntity tTileEntity = getTileEntityAtSide(side);
        if (tTileEntity instanceof IFluidHandler) return (IFluidHandler) tTileEntity;
        return null;
    }

    @Override
    public final IFluidHandler getITankContainerAtSideAndDistance(ForgeDirection side, int aDistance) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(side, aDistance);
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
    public final IGregTechTileEntity getIGregTechTileEntityAtSide(ForgeDirection side) {
        final TileEntity tTileEntity = getTileEntityAtSide(side);
        if (tTileEntity instanceof IGregTechTileEntity) return (IGregTechTileEntity) tTileEntity;
        return null;
    }

    @Override
    public final IGregTechTileEntity getIGregTechTileEntityAtSideAndDistance(ForgeDirection side, int aDistance) {
        final TileEntity tTileEntity = getTileEntityAtSideAndDistance(side, aDistance);
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
        if (ignoreUnloadedChunks && crossedChunkBorder(aCoords)
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
    public final TileEntity getTileEntityAtSide(ForgeDirection side) {
        final int ordinalSide = side.ordinal();
        if (side == ForgeDirection.UNKNOWN || mBufferedTileEntities[ordinalSide] == this) return null;
        final int tX = getOffsetX(side, 1);
        final int tY = getOffsetY(side, 1);
        final int tZ = getOffsetZ(side, 1);
        if (crossedChunkBorder(tX, tZ)) {
            mBufferedTileEntities[ordinalSide] = null;
            if (ignoreUnloadedChunks && !worldObj.blockExists(tX, tY, tZ)) return null;
        }
        if (mBufferedTileEntities[ordinalSide] == null) {
            mBufferedTileEntities[ordinalSide] = worldObj.getTileEntity(tX, tY, tZ);
            if (mBufferedTileEntities[ordinalSide] == null) {
                mBufferedTileEntities[ordinalSide] = this;
                return null;
            }
            return mBufferedTileEntities[ordinalSide];
        }
        if (mBufferedTileEntities[ordinalSide].isInvalid()) {
            mBufferedTileEntities[ordinalSide] = null;
            return getTileEntityAtSide(side);
        }
        if (mBufferedTileEntities[ordinalSide].xCoord == tX && mBufferedTileEntities[ordinalSide].yCoord == tY
            && mBufferedTileEntities[ordinalSide].zCoord == tZ) {
            return mBufferedTileEntities[ordinalSide];
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

    public final void onAdjacentBlockChange(int ignoredAX, int ignoredAY, int ignoredAZ) {
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
                        .ordinal(); // Don't update this block. Still updates
                                    // diagonal blocks twice if conditions
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
            worldObj,
            new GT_Packet_Block_Event(xCoord, (short) yCoord, zCoord, aID, aValue),
            xCoord,
            zCoord);
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

    /**
     * Gets items to be displayed for HoloInventory mod.
     *
     * @return null if default implementation should be used, i.e. {@link IInventory#getStackInSlot}.
     *         Otherwise, a list of items to be displayed. Null element may be contained.
     */
    @Nullable
    public List<ItemStack> getItemsForHoloGlasses() {
        return null;
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
        STALLED_STUTTERING_TOOLTIP = "GT5U.machines.stalled_stuttering.tooltip",
        STALLED_VENT_TOOLTIP = "GT5U.machines.stalled_vent.tooltip",
        FLUID_TRANSFER_TOOLTIP = "GT5U.machines.fluid_transfer.tooltip",
        ITEM_TRANSFER_TOOLTIP = "GT5U.machines.item_transfer.tooltip", POWER_SOURCE_KEY = "GT5U.machines.powersource.",
        BUTTON_FORBIDDEN_TOOLTIP = "GT5U.gui.button.forbidden",
        NEI_TRANSFER_STEAM_TOOLTIP = "GT5U.machines.nei_transfer.steam.tooltip",
        NEI_TRANSFER_VOLTAGE_TOOLTIP = "GT5U.machines.nei_transfer.voltage.tooltip";

    public static final int TOOLTIP_DELAY = 5;

    /**
     * Override this to add {@link com.gtnewhorizons.modularui.api.widget.Widget}s for your UI.
     */
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {}

    public void bindPlayerInventoryUI(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.bindPlayerInventory(buildContext.getPlayer(), 7, getGUITextureSet().getItemSlot());
    }

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
            final List<String> titleLines = fontRenderer
                .listFormattedStringToWidth(title, getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2);
            titleWidth = titleLines.size() > 1 ? getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2
                : fontRenderer.getStringWidth(title);
            // noinspection PointlessArithmeticExpression
            titleHeight = titleLines.size() * fontRenderer.FONT_HEIGHT + (titleLines.size() - 1) * 1;
        }

        final DrawableWidget tab = new DrawableWidget();
        final TextWidget text = new TextWidget(title).setDefaultColor(getTitleColor())
            .setTextAlignment(Alignment.CenterLeft)
            .setMaxWidth(titleWidth);
        if (GT_Mod.gregtechproxy.mTitleTabStyle == 1) {
            tab.setDrawable(getGUITextureSet().getTitleTabAngular())
                .setPos(0, -(titleHeight + TAB_PADDING) + 1)
                .setSize(getGUIWidth(), titleHeight + TAB_PADDING * 2);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight + TAB_PADDING);
        } else {
            tab.setDrawable(getGUITextureSet().getTitleTabDark())
                .setPos(0, -(titleHeight + TAB_PADDING * 2) + 1)
                .setSize(titleWidth + (TAB_PADDING + TITLE_PADDING) * 2, titleHeight + TAB_PADDING * 2 - 1);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight);
        }
        builder.widget(tab)
            .widget(text);
    }

    protected void addTitleItemIconStyle(ModularWindow.Builder builder, String title) {
        builder.widget(
            new MultiChildWidget().addChild(
                new DrawableWidget().setDrawable(getGUITextureSet().getTitleTabNormal())
                    .setPos(0, 0)
                    .setSize(24, 24))
                .addChild(
                    new ItemDrawable(getStackForm(1)).asWidget()
                        .setPos(4, 4))
                .addTooltip(title)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(0, -24 + 3));
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.DEFAULT;
    }

    protected int getTitleColor() {
        return COLOR_TITLE.get();
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                .setSize(17, 17)
                .setPos(152, 63));
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
    public void add1by1Slot(ModularWindow.Builder builder, IDrawable... background) {
        final ItemStackHandler inventoryHandler = getInventoryHandler();
        if (inventoryHandler == null) return;

        if (background.length == 0) {
            background = new IDrawable[] { getGUITextureSet().getItemSlot() };
        }
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 1)
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
            background = new IDrawable[] { getGUITextureSet().getItemSlot() };
        }
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 2)
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
            background = new IDrawable[] { getGUITextureSet().getItemSlot() };
        }
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 3)
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
            background = new IDrawable[] { getGUITextureSet().getItemSlot() };
        }
        builder.widget(
            SlotGroup.ofItemHandler(inventoryHandler, 4)
                .startFromSlot(0)
                .endAtSlot(15)
                .background(background)
                .build()
                .setPos(52, 7));
    }

    public void addCoverTabs(ModularWindow.Builder builder, UIBuildContext buildContext) {
        /* Do nothing */
    }

    public IConfigurationCircuitSupport getConfigurationCircuitSupport() {
        if (!(this instanceof IConfigurationCircuitSupport)) return null;
        return (IConfigurationCircuitSupport) this;
    }

    protected void addConfigurationCircuitSlot(ModularWindow.Builder builder) {
        final ItemStackHandler inventoryHandler = getInventoryHandler();
        if (inventoryHandler == null) return;

        if (!(this instanceof IInventory inv)) return;

        final IConfigurationCircuitSupport ccs = getConfigurationCircuitSupport();
        if (ccs == null) return;

        final AtomicBoolean dialogOpened = new AtomicBoolean(false);
        builder.widget(new SlotWidget(new BaseSlot(inventoryHandler, ccs.getCircuitSlot(), true)) {

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
                        int curIndex = GT_Utility
                            .findMatchingStackInList(tCircuits, inv.getStackInSlot(ccs.getCircuitSlot())) + 1;
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
                    EnumChatFormatting.DARK_GRAY + EnumChatFormatting.getTextWithoutFormattingCodes(
                        StatCollector.translateToLocal("GT5U.machines.select_circuit.tooltip.1")),
                    EnumChatFormatting.DARK_GRAY + EnumChatFormatting.getTextWithoutFormattingCodes(
                        StatCollector.translateToLocal("GT5U.machines.select_circuit.tooltip.2")),
                    EnumChatFormatting.DARK_GRAY + EnumChatFormatting.getTextWithoutFormattingCodes(
                        StatCollector.translateToLocal("GT5U.machines.select_circuit.tooltip.3")));
            }
        }.setOverwriteItemStackTooltip(list -> {
            list.removeIf(
                line -> line.contains(StatCollector.translateToLocal("gt.integrated_circuit.tooltip.0"))
                    || line.contains(StatCollector.translateToLocal("gt.integrated_circuit.tooltip.1")));
            return list;
        })
            .disableShiftInsert()
            .setHandlePhantomActionClient(true)
            .setBackground(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_INT_CIRCUIT)
            .setGTTooltip(() -> mTooltipCache.getData("GT5U.machines.select_circuit.tooltip"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(ccs.getCircuitSlotX() - 1, ccs.getCircuitSlotY() - 1));
    }

    protected void openSelectCircuitDialog(ModularUIContext uiContext, AtomicBoolean dialogOpened) {
        final IConfigurationCircuitSupport ccs = getConfigurationCircuitSupport();
        if (ccs == null) return;

        if (!(this instanceof IInventory inv)) return;

        final List<ItemStack> circuits = ccs.getConfigurationCircuits();
        uiContext.openClientWindow(
            player -> new SelectItemUIFactory(
                StatCollector.translateToLocal("GT5U.machines.select_circuit"),
                getStackForm(0),
                this::onCircuitSelected,
                circuits,
                GT_Utility.findMatchingStackInList(circuits, inv.getStackInSlot(ccs.getCircuitSlot())))
                    .setAnotherWindow(true, dialogOpened)
                    .setGuiTint(getGUIColorization())
                    .setCurrentGetter(() -> inv.getStackInSlot(ccs.getCircuitSlot()))
                    .createWindow(new UIBuildContext(player)));
    }

    protected void onCircuitSelected(ItemStack selected) {
        final IConfigurationCircuitSupport ccs = getConfigurationCircuitSupport();
        if (ccs == null) return;

        if (!(this instanceof IInventory inv)) return;

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
