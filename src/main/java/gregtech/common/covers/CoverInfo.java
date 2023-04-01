package gregtech.common.covers;

import static gregtech.api.enums.GT_Values.SIDE_UNKNOWN;

import java.lang.ref.WeakReference;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.GregTech_API;
import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;

public final class CoverInfo {

    private static final String NBT_SIDE = "s", NBT_ID = "id", NBT_DATA = "d";

    public static final CoverInfo EMPTY_INFO = new CoverInfo(SIDE_UNKNOWN, null);
    private byte coverSide;
    private int coverID = 0;
    private GT_CoverBehaviorBase<?> coverBehavior = null;
    private ISerializableObject coverData = null;
    private final WeakReference<ICoverable> coveredTile;
    private boolean needsUpdate = false;

    public CoverInfo(byte aSide, ICoverable aTile) {
        coverSide = aSide;
        coveredTile = new WeakReference<>(aTile);
    }

    public CoverInfo(byte aSide, int aID, ICoverable aTile, ISerializableObject aCoverData) {
        coverSide = aSide;
        coverID = aID;
        coverBehavior = GregTech_API.getCoverBehaviorNew(aID);
        coverData = aCoverData == null ? coverBehavior.createDataObject() : aCoverData;
        coveredTile = new WeakReference<>(aTile);
    }

    public CoverInfo(ICoverable aTile, NBTTagCompound aNBT) {
        coverSide = aNBT.getByte(NBT_SIDE);
        coverID = aNBT.getInteger(NBT_ID);
        coverBehavior = GregTech_API.getCoverBehaviorNew(coverID);
        coverData = aNBT.hasKey(NBT_DATA) ? coverBehavior.createDataObject(aNBT.getTag(NBT_DATA))
                : coverBehavior.createDataObject();
        coveredTile = new WeakReference<>(aTile);
    }

    public boolean isValid() {
        return coverID != 0 && coverSide != SIDE_UNKNOWN;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound aNBT) {
        aNBT.setByte(NBT_SIDE, coverSide);
        aNBT.setInteger(NBT_ID, coverID);
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
        return getCoverBehavior().getTickRate(coverSide, coverID, coverData, coveredTile.get());
    }

    public byte getSide() {
        return coverSide;
    }

    public ICoverable getTile() {
        return coveredTile.get();
    }

    public boolean isRedstoneSensitive(long aTickTimer) {
        return getCoverBehavior().isRedstoneSensitive(coverSide, coverID, coverData, coveredTile.get(), aTickTimer);
    }

    public ISerializableObject doCoverThings(long aTickTimer, byte aRedstone) {
        return getCoverBehavior().doCoverThings(
                coverSide,
                aRedstone,
                coverID,
                coverData,
                coveredTile.get(),
                aTickTimer);
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
        return getCoverBehavior().onCoverRightClick(
                coverSide,
                coverID,
                coverData,
                coveredTile.get(),
                aPlayer,
                aX,
                aY,
                aZ);
    }

    public boolean onCoverShiftRightClick(EntityPlayer aPlayer) {
        return getCoverBehavior().onCoverShiftRightClick(coverSide, coverID, coverData, coveredTile.get(), aPlayer);
    }

    public ISerializableObject onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return getCoverBehavior().onCoverScrewdriverClick(
                coverSide,
                coverID,
                coverData,
                coveredTile.get(),
                aPlayer,
                aX,
                aY,
                aZ);
    }

    public Block getFacadeBlock() {
        return getCoverBehavior().getFacadeBlock(coverSide, coverID, coverData, coveredTile.get());
    }

    public int getFacadeMeta() {
        return getCoverBehavior().getFacadeMeta(coverSide, coverID, coverData, coveredTile.get());
    }
}
