package gregtech.common.covers;

import java.lang.ref.WeakReference;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;

import gregtech.api.covers.CoverContext;
import gregtech.api.covers.CoverFactory;
import gregtech.api.covers.CoverPlacer;
import gregtech.api.covers.CoverRegistration;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;

public abstract class Cover {

    // One minute
    public static final int MAX_TICK_RATE_ADDITION = 1200;

    protected final ForgeDirection coverSide;
    protected final int coverID;
    protected final WeakReference<ICoverable> coveredTile;

    protected boolean needsUpdate = false;
    protected int tickRateAddition = 0;

    public Cover(CoverContext context) {
        coverSide = context.getSide();
        coverID = context.getCoverId();
        coveredTile = new WeakReference<>(context.getCoverable());
    }

    public boolean isValid() {
        return coverID != 0 && coverSide != ForgeDirection.UNKNOWN;
    }

    public abstract NBTTagCompound writeToNBT(NBTTagCompound aNBT);

    public abstract void writeToByteBuf(ByteBuf aOut);

    /**
     * This cover id should only be used to get the {@link CoverRegistration} from the {@link CoverRegistry}, or to
     * compare 2 covers to see if they're of the same type.
     */
    public int getCoverID() {
        return coverID;
    }

    public boolean needsUpdate() {
        return needsUpdate;
    }

    public void setNeedsUpdate(boolean aUpdate) {
        needsUpdate = aUpdate;
    }

    public boolean allowsTickRateAddition() {
        return true;
    }

    public abstract ISerializableObject getCoverData();

    /**
     * Called when the cover is initially attached to a machine.
     *
     * @param player    The attaching player
     * @param coverItem An {@link ItemStack} containing the cover
     */
    public void onPlayerAttach(EntityPlayer player, ItemStack coverItem) {
        // Do nothing by default.
    }

    /**
     * Gets the initial tick rate for doCoverThings of the Cover
     * <p/>
     * Defaults to getMinimumTickRate(), override for different initial and minimum tick rates
     */
    public int getDefaultTickRate() {
        return getMinimumTickRate();
    }

    /**
     * Get the ItemStack representation of this cover. Default to {@code int2Stack(aCoverID)}
     */
    public ItemStack asItemStack() {
        return GTUtility.intToStack(coverID);
    }

    /**
     * Return whether cover data needs to be synced to client upon tile entity creation or cover placement.
     * <p>
     * Note if you want to sync the data afterwards you will have to manually do it by calling
     * {@link ICoverable#issueCoverUpdate(ForgeDirection)} This option only affects the initial sync.
     */
    public boolean isDataNeededOnClient() {
        return false;
    }

    /**
     * Called upon cover being removed. Called on both server and client.
     */
    public void onCoverRemoval() {}

    public abstract boolean acceptsDataObject(Object data);

    public abstract void setCoverData(ISerializableObject aData);

    /**
     * Get the special foreground cover texture associated with this cover. Return null if one should use the texture
     * passed to {@link CoverRegistry#registerCover(ItemStack, ITexture, CoverFactory, CoverPlacer)} or its
     * overloads.
     * <br>
     * This texture will be overlaid on top of the block's base texture for that face.
     */
    public abstract ITexture getOverlayTexture();

    /**
     * Get the special cover texture associated with this cover. Return null if one should use the texture passed to
     * {@link CoverRegistry#registerCover(ItemStack, ITexture, CoverFactory, CoverPlacer)} or its overloads.
     * <br>
     * This texture takes up the entire face on which it is rendered.
     */
    public ITexture getSpecialFaceTexture() {
        return null;
    }

    /**
     * Gets the Tick Rate for doCoverThings of the Cover
     */
    public final int getTickRate() {
        return getMinimumTickRate() + tickRateAddition;
    }

    public ForgeDirection getSide() {
        return coverSide;
    }

    public ICoverable getTile() {
        return coveredTile.get();
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    /**
     * Needs to return true for Covers, which have a Redstone Output on their Facing.
     */
    public boolean manipulatesSidedRedstoneOutput() {
        return false;
    }

    /**
     * Called to determine the incoming Redstone Signal of a Machine. Returns the original Redstone per default. The
     * Cover should @letsRedstoneGoIn or the aInputRedstone Parameter is always 0.
     */
    public byte getRedstoneInput(byte aInputRedstone) {
        return letsRedstoneGoIn() ? aInputRedstone : 0;
    }

    /**
     * If it lets RS-Signals into the Block
     * <p/>
     * This is just Informative so that Machines know if their Redstone Input is blocked or not
     */
    public boolean letsRedstoneGoIn() {
        return false;
    }

    /**
     * If it lets RS-Signals out of the Block
     */
    public boolean letsRedstoneGoOut() {
        return false;
    }

    /**
     * Called by updateEntity inside the covered TileEntity.
     */
    public abstract ISerializableObject doCoverThings(byte aRedstone, long aTickTimer);

    /**
     * Called when Base TE being unloaded.
     */
    public void onCoverUnload() {}

    /**
     * Called upon Base TE being destroyed (once getDrops is called), thus getting called only when destroyed in
     * survival.
     */
    public void onBaseTEDestroyed() {}

    /**
     * Called before receiving data from network. Use {@link ICoverable#isClientSide()} to determine the side.
     */
    public void preDataChanged(int newCoverId, ISerializableObject newCoverVariable) {}

    /**
     * Called upon receiving data from network. Use {@link ICoverable#isClientSide()} to determine the side.
     */
    public void onDataChanged() {}

    /**
     * Gives a small Text for the status of the Cover.
     */
    public String getDescription() {
        return "";
    }

    public ModularWindow createCoverWindow(EntityPlayer player) {
        final CoverUIBuildContext buildContext = new CoverUIBuildContext(
            player,
            coverID,
            coverSide,
            coveredTile.get(),
            true);
        return createWindow(buildContext);
    }

    protected abstract ModularWindow createWindow(CoverUIBuildContext buildContext);

    /**
     * If it lets you rightclick the Machine normally
     */
    public final boolean isGUIClickable() {
        return CoverRegistry.getCoverPlacer(coverID)
            .isGuiClickable();
    }

    public boolean hasCoverGUI() {
        return false;
    }

    /**
     * If it lets Energy into the Block
     */
    public boolean letsEnergyIn() {
        return false;
    }

    /**
     * If it lets Energy out of the Block
     */
    public boolean letsEnergyOut() {
        return false;
    }

    /**
     * If it lets Liquids into the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    public boolean letsFluidIn(Fluid fluid) {
        return false;
    }

    /**
     * If it lets Liquids out of the Block, aFluid can be null meaning if this is generally allowing Fluids or not.
     */
    public boolean letsFluidOut(Fluid fluid) {
        return false;
    }

    /**
     * If it lets Items into the Block, aSlot = -1 means if it is generally accepting Items (return false for no
     * reaction at all), aSlot = -2 means if it would accept for all Slots Impl(return true to skip the Checks for each
     * Slot).
     */
    public boolean letsItemsIn(int slot) {
        return false;
    }

    /**
     * If it lets Items out of the Block, aSlot = -1 means if it is generally accepting Items (return false for no
     * reaction at all), aSlot = -2 means if it would accept for all Slots Impl(return true to skip the Checks for each
     * Slot).
     */
    public boolean letsItemsOut(int slot) {
        return false;
    }

    /**
     * if this Cover should let Pipe Connections look connected even if it is not the case.
     */
    public boolean alwaysLookConnected() {
        return false;
    }

    /**
     * Called when someone rightclicks this Cover.
     * <p/>
     * return true, if something actually happens.
     */
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        return false;
    }

    /**
     * Called when someone shift-rightclicks this Cover with no tool. Doesn't call @onCoverRightclick in this Case.
     */
    public boolean onCoverShiftRightClick(EntityPlayer aPlayer) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && hasCoverGUI() && aPlayer instanceof EntityPlayerMP) {
            GTUIInfos.openCoverUI(coverable, aPlayer, coverSide);
            return true;
        }
        return false;
    }

    /**
     * Called when someone rightclicks this Cover with a Screwdriver. Doesn't call @onCoverRightclick in this Case.
     * <p/>
     * return the new Value of the Cover Variable
     */
    public abstract void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ);

    public void onCoverJackhammer(EntityPlayer aPlayer) {
        adjustTickRateMultiplier(aPlayer.isSneaking());

        GTUtility.sendChatToPlayer(
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

        setTickRateAddition(tickRateAddition + (isDecreasing ? -1 : 1) * stepAmount);
        setTickRateAddition(tickRateAddition - (getTickRate() % stepAmount));
    }

    protected void setTickRateAddition(int newValue) {
        tickRateAddition = Math.min(MAX_TICK_RATE_ADDITION, Math.max(0, newValue));
    }

    /**
     * Returns information about the cover's tick rate.
     *
     * @return An instance of tick rate components
     */
    @NotNull
    public Cover.ClientTickRateFormatter getCurrentTickRateFormatted() {
        return new ClientTickRateFormatter(getTickRate());
    }

    /**
     * Gets the Tick Rate for doCoverThings of the Cover
     * <p/>
     * 0 = No Ticks! Yes, 0 is Default, you have to override this
     */
    protected int getMinimumTickRate() {
        return 0;
    }

    public int getTickRateAddition() {
        return tickRateAddition;
    }

    /**
     * Get target facade block. Does not affect rendering of **this** block. It is only used as a hint for other block
     * in case of CTM
     *
     * @return null if none, otherwise return facade target block
     */
    public Block getFacadeBlock() {
        return null;
    }

    /**
     * Get target facade block. Does not affect rendering of **this** block. It is only used as a hint for other block
     * in case of CTM
     *
     * @return 0 if none, otherwise return facade target meta
     */
    public int getFacadeMeta() {
        return 0;
    }

    /**
     * Override to add to the tooltip generated when a user hovers over the cover on the left side of a machine's UI.
     *
     * @return A list of new tooltip entries. Entries are inserted at the top, just after the name and direction line.
     */
    @NotNull
    public List<String> getAdditionalTooltip() {
        return ImmutableList.of();
    }

    public boolean allowsCopyPasteTool() {
        return true;
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

    public ModularUIContainer createCoverContainer(EntityPlayer player) {
        ICoverable tile = this.coveredTile.get();
        if (tile == null) return null;
        final CoverUIBuildContext buildContext = new CoverUIBuildContext(
            player,
            this.coverID,
            this.coverSide,
            tile,
            false);
        final ModularWindow window = this.createWindow(buildContext);
        if (window == null) return null;
        return new ModularUIContainer(new ModularUIContext(buildContext, tile::markDirty), window);
    }

}
