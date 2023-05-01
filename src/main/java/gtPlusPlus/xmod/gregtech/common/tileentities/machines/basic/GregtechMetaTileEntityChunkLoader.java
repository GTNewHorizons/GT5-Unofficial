package gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static net.minecraftforge.common.ForgeChunkManager.getMaxChunkDepthFor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.MapMaker;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.chunkloading.GTPP_ChunkManager;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregtechMetaTileEntityChunkLoader extends GT_MetaTileEntity_BasicMachine {

    public GregtechMetaTileEntityChunkLoader(int aID, String aName, String aNameRegional, int aTier) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                4,
                "Loads " + getMaxChunksToLoadForTier(aTier) + " chunks when powered",
                0,
                0,
                "Recycler.png",
                "",
                new ITexture[] {});
    }

    public GregtechMetaTileEntityChunkLoader(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
            String aGUIName, String aNEIName) {
        super(aName, aTier, 4, aDescription, aTextures, 0, 0, aGUIName, aNEIName);
    }

    public static int getMaxChunksToLoadForTier(int aTier) {
        if (aTier < 4) {
            return Math.min(3 * 3, getMaxChunkDepthFor(GTPlusPlus.ID));
        }
        if (aTier < 6) {
            return Math.min(7 * 7, getMaxChunkDepthFor(GTPlusPlus.ID));
        }
        if (aTier < 8) {
            return Math.min(15 * 15, getMaxChunkDepthFor(GTPlusPlus.ID));
        } else {
            return 0;
        }
    }

    public static int getChunkRadiusForTier(int aTier) {
        if (aTier < 4) {
            return Math.min(1, (int) Math.floor(Math.sqrt(getMaxChunkDepthFor(GTPlusPlus.ID))));
        }
        if (aTier < 6) {
            return Math.min(3, (int) Math.floor(Math.sqrt(getMaxChunkDepthFor(GTPlusPlus.ID))));
        }
        if (aTier < 8) {
            return Math.min(7, (int) Math.floor(Math.sqrt(getMaxChunkDepthFor(GTPlusPlus.ID))));
        } else {
            return 0;
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return false;
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Loads " + getMaxChunksToLoadForTier(this.mTier) + " chunks when powered", "Consumes 2A",
                "Behaves Identically to a Railcraft World Anchor", CORE.GT_Tooltip.get() };
    }

    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        final ITexture[][][] rTextures = new ITexture[10][17][];
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = this.getFront(i);
            rTextures[1][i + 1] = this.getBack(i);
            rTextures[2][i + 1] = this.getBottom(i);
            rTextures[3][i + 1] = this.getTop(i);
            rTextures[4][i + 1] = this.getSides(i);
            rTextures[5][i + 1] = this.getFrontActive(i);
            rTextures[6][i + 1] = this.getBackActive(i);
            rTextures[7][i + 1] = this.getBottomActive(i);
            rTextures[8][i + 1] = this.getTopActive(i);
            rTextures[9][i + 1] = this.getSidesActive(i);
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
            final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[(aActive ? 5 : 0)
                + (side == facing ? 0
                        : side == facing.getOpposite() ? 1
                                : side == ForgeDirection.DOWN ? 2 : side == ForgeDirection.UP ? 3 : 4)][aColorIndex
                                        + 1];
    }

    public ITexture[] getFront(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier]) };
    }

    public ITexture[] getBack(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier]) };
    }

    public ITexture[] getBottom(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier]) };
    }

    public ITexture[] getTop(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier]) };
    }

    public ITexture[] getSides(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier]) };
    }

    public ITexture[] getFrontActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier + 1]) };
    }

    public ITexture[] getBackActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier + 1]) };
    }

    public ITexture[] getBottomActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier + 1]) };
    }

    public ITexture[] getTopActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier + 1]) };
    }

    public ITexture[] getSidesActive(final byte aColor) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[this.mTier][aColor + 1],
                new GT_RenderedTexture(TexturesGtBlock.Casing_Material_MaragingSteel),
                new GT_RenderedTexture(TexturesGtBlock.TIERED_MACHINE_HULLS[mTier + 1]) };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntityChunkLoader(
                this.mName,
                this.mTier,
                this.mDescription,
                this.mTextures,
                this.mGUIName,
                this.mNEIName);
    }

    @Override
    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getInfoData() {
        return super.getInfoData();
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public boolean canInsertItem(final int p_102007_1_, final ItemStack p_102007_2_, final int p_102007_3_) {
        return false;
    }

    @Override
    public boolean canExtractItem(final int p_102008_1_, final ItemStack p_102008_2_, final int p_102008_3_) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public boolean isUseableByPlayer(final EntityPlayer p_70300_1_) {
        return true;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            if (aBaseMetaTileEntity.getXCoord() != prevX || aBaseMetaTileEntity.getYCoord() != prevY
                    || aBaseMetaTileEntity.getZCoord() != prevZ) {
                releaseTicket();
                prevX = aBaseMetaTileEntity.getXCoord();
                prevY = aBaseMetaTileEntity.getYCoord();
                prevZ = aBaseMetaTileEntity.getZCoord();
            }

            if (hasActiveTicket() && (getTicket().world != aBaseMetaTileEntity.getWorld() || refreshTicket
                    || !aBaseMetaTileEntity.isAllowedToWork())) {
                releaseTicket();
            }

            if (++updateCycle % updateCycleLength == 0) {
                updateCycle = 0;
                if (canChunkload()) {
                    setEUVar(getEUVar() - getEnergyConsumption());
                }
            }

            if (!hasActiveTicket()) {
                requestTicket();
            }
        }
    }

    @Override
    public long maxAmperesIn() {
        return 4;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 2;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 256;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        releaseTicket();
    }

    public long getEnergyConsumption() {
        return GT_Values.VP[mTier] * 2 * updateCycleLength;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        prevX = aNBT.getInteger("prevX");
        prevY = aNBT.getInteger("prevY");
        prevZ = aNBT.getInteger("prevZ");

        NBTTagCompound uuidNBT = aNBT.getCompoundTag("uuid");
        uuid = new UUID(uuidNBT.getLong("most"), uuidNBT.getLong("least"));
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("prevX", prevX);
        aNBT.setInteger("prevY", prevY);
        aNBT.setInteger("prevZ", prevZ);

        if (uuid != null) {
            NBTTagCompound uuidNBT = new NBTTagCompound();
            uuidNBT.setLong("most", uuid.getMostSignificantBits());
            uuidNBT.setLong("least", uuid.getLeastSignificantBits());
            aNBT.setTag("uuid", uuidNBT);
        }
    }

    public boolean canChunkload() {
        return getBaseMetaTileEntity().isAllowedToWork() && getEUVar() >= getEnergyConsumption();
    }

    /*
     * Chunkloading Vars
     */

    private Set<ChunkCoordIntPair> mLoadedChunks = new HashSet<>();
    private static final Map<UUID, Ticket> tickets = new MapMaker().makeMap();
    private boolean refreshTicket;
    private int updateCycle = 0;
    private static final int updateCycleLength = 20;
    private UUID uuid;
    private int prevX, prevY, prevZ;
    private boolean hasTicket;

    public boolean addChunkToLoadedList(ChunkCoordIntPair aActiveChunk) {
        return mLoadedChunks.add(aActiveChunk);
    }

    public boolean removeChunkFromLoadedList(ChunkCoordIntPair aActiveChunk) {
        return mLoadedChunks.remove(aActiveChunk);
    }

    public Set<ChunkCoordIntPair> getManagedChunks() {
        return mLoadedChunks;
    }

    public int getChunkloaderTier() {
        return mTier;
    }

    public void forceChunkLoading(Ticket ticket) {
        setTicket(ticket);
        setupChunks();

        if (mLoadedChunks != null) {
            for (ChunkCoordIntPair chunk : mLoadedChunks) {
                ForgeChunkManager.forceChunk(ticket, chunk);
            }
        }
    }

    public void setupChunks() {
        if (!hasTicket) {
            mLoadedChunks = null;
        } else {
            mLoadedChunks = GTPP_ChunkManager.getChunksAround(
                    getBaseMetaTileEntity().getXCoord() >> 4,
                    getBaseMetaTileEntity().getZCoord() >> 4,
                    getChunkRadiusForTier(mTier));
        }
    }

    protected Ticket getTicketFromForge() {
        return ForgeChunkManager
                .requestTicket(GTplusplus.instance, getBaseMetaTileEntity().getWorld(), ForgeChunkManager.Type.NORMAL);
    }

    public boolean hasActiveTicket() {
        return getTicket() != null;
    }

    protected void releaseTicket() {
        refreshTicket = false;
        setTicket(null);
    }

    protected void requestTicket() {
        if (canChunkload()) {
            Ticket chunkTicket = getTicketFromForge();
            if (chunkTicket != null) {
                setTicketData(chunkTicket);
                forceChunkLoading(chunkTicket);
            }
        }
    }

    protected void setTicketData(Ticket chunkTicket) {
        if (chunkTicket != null) {
            chunkTicket.getModData().setInteger("xCoord", getBaseMetaTileEntity().getXCoord());
            chunkTicket.getModData().setInteger("yCoord", getBaseMetaTileEntity().getYCoord());
            chunkTicket.getModData().setInteger("zCoord", getBaseMetaTileEntity().getZCoord());
            chunkTicket.setChunkListDepth(getMaxChunksToLoadForTier(mTier));
        }
    }

    public void setTicket(Ticket t) {
        Ticket ticket = getTicket();
        if (ticket != t) {
            if (ticket != null) {
                if (ticket.world == getBaseMetaTileEntity().getWorld()) {
                    for (ChunkCoordIntPair chunk : ticket.getChunkList()) {
                        if (ForgeChunkManager.getPersistentChunksFor(getBaseMetaTileEntity().getWorld()).keys()
                                .contains(chunk)) {
                            ForgeChunkManager.unforceChunk(ticket, chunk);
                        }
                    }
                    ForgeChunkManager.releaseTicket(ticket);
                }
                tickets.remove(getUUID());
            }
        }
        hasTicket = t != null;
        if (hasTicket) {
            tickets.put(getUUID(), t);
        }
    }

    public Ticket getTicket() {
        return tickets.get(getUUID());
    }

    public UUID getUUID() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
        return uuid;
    }
}
