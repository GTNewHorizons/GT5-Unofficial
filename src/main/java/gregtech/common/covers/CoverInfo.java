package gregtech.common.covers;

import java.lang.ref.WeakReference;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.GregTech_API;
import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;

public final class CoverInfo {

    private static final String NBT_SIDE = "s", NBT_ID = "id", NBT_DATA = "d", NBT_TICK_RATE_ADDITION = "tra";

    // One minute
    public static final int MAX_TICK_RATE_ADDITION = 1200;

    public static final CoverInfo EMPTY_INFO = new CoverInfo(ForgeDirection.UNKNOWN, null);
    private final ForgeDirection coverSide;
    private int coverID = 0;
    private GT_CoverBehaviorBase<?> coverBehavior = null;
    private ISerializableObject coverData = null;
    private final WeakReference<ICoverable> coveredTile;
    private boolean needsUpdate = false;

    private int tickRateAddition = 0;

    public CoverInfo(ForgeDirection side, ICoverable aTile) {
        coverSide = side;
        coveredTile = new WeakReference<>(aTile);
    }

    public CoverInfo(ForgeDirection side, int aID, ICoverable aTile, ISerializableObject aCoverData) {
        coverSide = side;
        coverID = aID;
        coverBehavior = GregTech_API.getCoverBehaviorNew(aID);
        coverData = aCoverData == null ? coverBehavior.createDataObject() : aCoverData;
        coveredTile = new WeakReference<>(aTile);
    }

    public CoverInfo(ICoverable aTile, NBTTagCompound aNBT) {
        coverSide = ForgeDirection.getOrientation(aNBT.getByte(NBT_SIDE));
        coverID = aNBT.getInteger(NBT_ID);
        coverBehavior = GregTech_API.getCoverBehaviorNew(coverID);
        coverData = aNBT.hasKey(NBT_DATA) ? coverBehavior.createDataObject(aNBT.getTag(NBT_DATA))
            : coverBehavior.createDataObject();
        coveredTile = new WeakReference<>(aTile);
        tickRateAddition = aNBT.hasKey(NBT_TICK_RATE_ADDITION) ? aNBT.getInteger(NBT_TICK_RATE_ADDITION) : 0;
    }

    public boolean isValid() {
        return coverID != 0 && coverSide != ForgeDirection.UNKNOWN;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound aNBT) {
        aNBT.setByte(NBT_SIDE, (byte) coverSide.ordinal());
        aNBT.setInteger(NBT_ID, coverID);
        aNBT.setInteger(NBT_TICK_RATE_ADDITION, tickRateAddition);
        if (coverData != null) aNBT.setTag(NBT_DATA, coverData.saveDataToNBT());

        return aNBT;
    }

    public int getCoverID() {
        return coverID;
    }

    public boolean needsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean aUpdate) {
        needsUpdate = aUpdate;
    }

    public GT_CoverBehaviorBase<?> getCoverBehavior() {
        if (coverBehavior != null) return coverBehavior;
        return GregTech_API.sNoBehavior;
    }

    public ISerializableObject getCoverData() {
        if (coverData != null) return coverData;
        return GregTech_API.sNoBehavior.createDataObject();
    }

    public boolean onCoverRemoval(boolean aForced) {
        return getCoverBehavior().onCoverRemoval(coverSide, coverID, coverData, coveredTile.get(), aForced);
    }

    public ItemStack getDrop() {
        return getCoverBehavior().getDrop(coverSide, coverID, coverData, coveredTile.get());
    }

    public ItemStack getDisplayStack() {
        return getCoverBehavior().getDisplayStack(coverID, coverData);
    }

    public boolean isDataNeededOnClient() {
        return getCoverBehavior().isDataNeededOnClient(coverSide, coverID, coverData, coveredTile.get());
    }

    public void onDropped() {
        getCoverBehavior().onDropped(coverSide, coverID, coverData, coveredTile.get());
    }

    public void setCoverData(ISerializableObject aData) {
        coverData = aData;
    }

    public ITexture getSpecialCoverFGTexture() {
        return getCoverBehavior().getSpecialCoverFGTexture(coverSide, coverID, coverData, coveredTile.get());
    }

    public ITexture getSpecialCoverTexture() {
        return getCoverBehavior().getSpecialCoverTexture(coverSide, coverID, coverData, coveredTile.get());
    }

    public int getTickRate() {
        return getMinimumTickRate() + tickRateAddition;
    }

    public ForgeDirection getSide() {
        return coverSide;
    }

    public ICoverable getTile() {
        return coveredTile.get();
    }

    public boolean isRedstoneSensitive(long aTickTimer) {
        return getCoverBehavior().isRedstoneSensitive(coverSide, coverID, coverData, coveredTile.get(), aTickTimer);
    }

    public ISerializableObject doCoverThings(long aTickTimer, byte aRedstone) {
        return getCoverBehavior()
            .doCoverThings(coverSide, aRedstone, coverID, coverData, coveredTile.get(), aTickTimer);
    }

    public void onBaseTEDestroyed() {
        getCoverBehavior().onBaseTEDestroyed(coverSide, coverID, coverData, coveredTile.get());
    }

    public void updateCoverBehavior() {
        coverBehavior = GregTech_API.getCoverBehaviorNew(coverID);
    }

    public void preDataChanged(int aCoverID, ISerializableObject aCoverData) {
        getCoverBehavior().preDataChanged(coverSide, coverID, aCoverID, coverData, aCoverData, coveredTile.get());
    }

    public void onDataChanged() {
        getCoverBehavior().onDataChanged(coverSide, coverID, coverData, coveredTile.get());
    }

    public String getBehaviorDescription() {
        return getCoverBehavior().getDescription(coverSide, coverID, coverData, null);
    }

    public ModularWindow createWindow(EntityPlayer player) {
        final GT_CoverUIBuildContext buildContext = new GT_CoverUIBuildContext(
            player,
            coverID,
            coverSide,
            coveredTile.get(),
            true);
        return getCoverBehavior().createWindow(buildContext);
    }

    public boolean isGUIClickable() {
        return getCoverBehavior().isGUIClickable(coverSide, coverID, coverData, coveredTile.get());
    }

    public boolean hasCoverGUI() {
        return getCoverBehavior().hasCoverGUI();
    }

    public boolean useModularUI() {
        return getCoverBehavior().useModularUI();
    }

    public boolean letsItemsIn(int aSlot) {
        return getCoverBehavior().letsItemsIn(coverSide, coverID, coverData, aSlot, coveredTile.get());
    }

    public boolean letsItemsOut(int aSlot) {
        return getCoverBehavior().letsItemsOut(coverSide, coverID, coverData, aSlot, coveredTile.get());
    }

    public boolean letsFluidIn(Fluid aFluid) {
        return letsFluidIn(aFluid, coveredTile.get());
    }

    public boolean letsFluidOut(Fluid aFluid) {
        return letsFluidOut(aFluid, coveredTile.get());
    }

    public boolean letsFluidIn(Fluid aFluid, ICoverable tile) {
        return getCoverBehavior().letsFluidIn(coverSide, coverID, coverData, aFluid, tile);
    }

    public boolean letsFluidOut(Fluid aFluid, ICoverable tile) {
        return getCoverBehavior().letsFluidOut(coverSide, coverID, coverData, aFluid, tile);
    }

    public boolean letsEnergyIn() {
        return getCoverBehavior().letsEnergyIn(coverSide, coverID, coverData, coveredTile.get());
    }

    public boolean letsEnergyOut() {
        return getCoverBehavior().letsEnergyOut(coverSide, coverID, coverData, coveredTile.get());
    }

    public boolean alwaysLookConnected() {
        return getCoverBehavior().alwaysLookConnected(coverSide, coverID, coverData, coveredTile.get());
    }

    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return getCoverBehavior()
            .onCoverRightClick(coverSide, coverID, coverData, coveredTile.get(), aPlayer, aX, aY, aZ);
    }

    public boolean onCoverShiftRightClick(EntityPlayer aPlayer) {
        return getCoverBehavior().onCoverShiftRightClick(coverSide, coverID, coverData, coveredTile.get(), aPlayer);
    }

    public ISerializableObject onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return getCoverBehavior()
            .onCoverScrewdriverClick(coverSide, coverID, coverData, coveredTile.get(), aPlayer, aX, aY, aZ);
    }

    public void onCoverJackhammer(EntityPlayer aPlayer) {
        adjustTickRateMultiplier(aPlayer.isSneaking());

        GT_Utility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocalFormatted("gt.cover.info.chat.tick_rate", getCurrentTickRateFormatted()));
    }

    /**
     * Adjusts the tick rate by one step.
     *
     * @param isDecreasing If true, lower one step.
     */
    public void adjustTickRateMultiplier(final boolean isDecreasing) {
        final int currentTickRate = getTickRate();
        final int stepAmount = currentTickRate == 20 ? (isDecreasing ? 5 : 20) : (currentTickRate < 20 ? 5 : 20);

        tickRateAddition = clamp(tickRateAddition + (isDecreasing ? -1 : 1) * stepAmount);
        tickRateAddition = clamp(tickRateAddition - ((tickRateAddition + getMinimumTickRate()) % stepAmount));
    }

    /**
     * Returns information about the cover's tick rate.
     *
     * @return An instance of tick rate components
     */
    @NotNull
    public CoverInfo.ClientTickRateFormatter getCurrentTickRateFormatted() {
        return new ClientTickRateFormatter(getTickRate());
    }

    public int getMinimumTickRate() {
        return getCoverBehavior().getTickRate(coverSide, coverID, coverData, coveredTile.get());
    }

    public int getTickRateAddition() {
        return tickRateAddition;
    }

    public void setTickRateAddition(final int tickRateAddition) {
        this.tickRateAddition = clamp(tickRateAddition);
    }

    public Block getFacadeBlock() {
        return getCoverBehavior().getFacadeBlock(coverSide, coverID, coverData, coveredTile.get());
    }

    public int getFacadeMeta() {
        return getCoverBehavior().getFacadeMeta(coverSide, coverID, coverData, coveredTile.get());
    }

    @NotNull
    public List<String> getAdditionalTooltip(ISerializableObject data) {
        return getCoverBehavior().getAdditionalTooltip(data);
    }

    private static int clamp(int input) {
        return Math.min(MAX_TICK_RATE_ADDITION, Math.max(0, input));
    }

    public static final class ClientTickRateFormatter {

        /** A translation key for the type of time units being used (e.g.: "tick", "seconds".) */
        private final String unitI18NKey;
        /** A number representing a quantity of time. */
        private final int tickRate;

        /**
         * Converts a given tick rate into a human-friendly format.
         * 
         * @param tickRate The rate at which something ticks, in ticks per operation.
         */
        public ClientTickRateFormatter(final int tickRate) {
            if (tickRate < 20) {
                this.unitI18NKey = tickRate == 1 ? "gt.time.tick.singular" : "gt.time.tick.plural";
                this.tickRate = tickRate;
            } else {
                this.unitI18NKey = tickRate == 20 ? "gt.time.second.singular" : "gt.time.second.plural";
                this.tickRate = tickRate / 20;
            }
        }

        public String toString() {
            return StatCollector.translateToLocalFormatted(
                "gt.cover.info.format.tick_rate",
                tickRate,
                StatCollector.translateToLocal(unitI18NKey));
        }
    }
}
