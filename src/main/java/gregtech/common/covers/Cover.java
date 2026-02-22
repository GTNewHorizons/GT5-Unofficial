package gregtech.common.covers;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.covers.CoverContext;
import gregtech.api.covers.CoverFactory;
import gregtech.api.covers.CoverPlacer;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.modularui2.CoverGuiData;
import gregtech.api.modularui2.GTGuiTheme;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTModularScreen;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.CoverUIFactory;
import gregtech.common.text.ClientTickRateFormatter;
import io.netty.buffer.ByteBuf;

public class Cover implements IGuiHolder<CoverGuiData> {

    // One minute
    public static final int MAX_TICK_RATE_ADDITION = 1200;
    private static final String NBT_DATA = "d";
    private static final String NBT_TICK_RATE_ADDITION = "tra";
    protected final ForgeDirection coverSide;
    protected final int coverID;
    protected final WeakReference<ICoverable> coveredTile;

    private final ITexture coverFGTexture;
    protected boolean needsUpdate = false;
    protected int tickRateAddition = 0;

    public Cover(@NotNull CoverContext context, ITexture coverFGTexture) {
        this.coverSide = context.getSide();
        this.coverID = GTUtility.stackToInt(context.getCoverItem());
        this.coveredTile = new WeakReference<>(context.getCoverable());
        this.coverFGTexture = coverFGTexture;
        setTickRateAddition(getDefaultTickRateAddition());
    }

    private int getDefaultTickRateAddition() {
        if (!allowsTickRateAddition()) return 0;
        return getDefaultTickRate() - this.getMinimumTickRate();
    }

    public final void readFromNbt(NBTTagCompound nbt) {
        if (nbt.hasKey(NBT_TICK_RATE_ADDITION)) {
            setTickRateAddition(nbt.getInteger(NBT_TICK_RATE_ADDITION));
        }
        if (nbt.hasKey(NBT_DATA)) {
            readDataFromNbt(nbt.getTag(NBT_DATA));
        }
    }

    protected void readDataFromNbt(NBTBase nbt) {}

    public final void readFromPacket(ByteArrayDataInput byteData) {
        setTickRateAddition(byteData.readInt());
        readDataFromPacket(byteData);
    }

    protected void readDataFromPacket(ByteArrayDataInput byteData) {}

    public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger(NBT_TICK_RATE_ADDITION, tickRateAddition);
        nbt.setTag(NBT_DATA, saveDataToNbt());
        return nbt;
    }

    protected @Nonnull NBTBase saveDataToNbt() {
        return new NBTTagCompound();
    }

    public final void writeToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(tickRateAddition);
        writeDataToByteBuf(byteBuf);
    }

    protected void writeDataToByteBuf(ByteBuf byteBuf) {}

    /**
     * Get the special foreground cover texture associated with this cover. Return null if one should use the texture
     * passed to {@link CoverRegistry#registerCover(ItemStack, ITexture, CoverFactory, CoverPlacer)} or its overloads.
     * <br>
     * This texture will be overlaid on top of the block's base texture for that face.
     */
    public ITexture getOverlayTexture() {
        return coverFGTexture;
    }

    public void doCoverThings(byte aRedstone, long aTickTimer) {}

    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {}

    public boolean isValid() {
        return coverID != 0 && coverSide != ForgeDirection.UNKNOWN;
    }

    /**
     * This cover id should only be used to get the {@link CoverPlacer} from the {@link CoverRegistry}, or to compare 2
     * covers to see if they're of the same type.
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

    /**
     * Get the special cover texture associated with this cover. Return null if one should use the texture passed to
     * {@link CoverRegistry#registerCover(ItemStack, ITexture, CoverFactory, CoverPlacer)} or its overloads. <br>
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
     * Called when Base TE being unloaded.
     */
    public void onCoverUnload() {}

    /**
     * Called upon Base TE being destroyed (once getDrops is called), thus getting called only when destroyed in
     * survival.
     */
    public void onBaseTEDestroyed() {}

    /**
     * Gives a small Text for the status of the Cover.
     */
    public String getDescription() {
        return "";
    }

    // region GUI

    @Override
    @SideOnly(Side.CLIENT)
    public ModularScreen createScreen(CoverGuiData data, ModularPanel mainPanel) {
        return new GTModularScreen(mainPanel, getUITheme());
    }

    /**
     * Specifies theme of this GUI. You don't need to touch this unless you really want to go fancy.
     */
    protected GTGuiTheme getUITheme() {
        return GTGuiThemes.COVER;
    }

    /**
     * In order to migrate from MUI1 to MUI2 smoothly, we support both of them for the time being. Since we have
     * functionality to open cover window on top of machine GUI, we need to make cover GUI capable of operating on both
     * ways. So don't forget to also implement {@link #createWindow}.
     *
     * @param guiData     information about the creation context, ignored for covers since we've already used it to
     *                    locate the right cover instance
     * @param syncManager sync handler where widget sync handlers should be registered
     * @return UI panel to show
     */
    @Override
    public final ModularPanel buildUI(CoverGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return getCoverGui().createStandalonePanel(syncManager, uiSettings, guiData);
    }

    /**
     * Use this method to get a panel representing this cover that you can open from another MUI2 UI.
     *
     * @param panelName   the unique name of this panel in the context of your UI.
     * @param syncManager sync handler where widget sync handlers should be registered
     * @return UI panel to show
     */
    public final ModularPanel buildPopUpUI(CoverGuiData guiData, String panelName, PanelSyncManager syncManager,
        UISettings uiSettings) {
        return getCoverGui().createBasePanel(panelName, syncManager, uiSettings, guiData);
    }

    /**
     * Override this method to provide a different GUI implementation for your cover in MUI2.
     *
     * @return The variant of CoverBaseGui that can build a GUI for this cover
     */
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverBaseGui<>(this);
    }

    // endregion

    // region Legacy MUI1 GUI

    /**
     * You also need to implement {@link #buildUI} if you want to implement cover GUI.
     */
    protected ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new CoverUIFactory<>(buildContext).createWindow();
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

    public ModularWindow createCoverWindow(EntityPlayer player) {
        final CoverUIBuildContext buildContext = new CoverUIBuildContext(
            player,
            coverID,
            coverSide,
            coveredTile.get(),
            true);
        return createWindow(buildContext);
    }

    /**
     * If it lets you rightclick the Machine normally
     */
    public final boolean isGUIClickable() {
        return CoverRegistry.getCoverPlacer(GTUtility.intToStack(coverID))
            .isGuiClickable();
    }

    public boolean hasCoverGUI() {
        return false;
    }

    // endregion

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
            if (GTGuis.GLOBAL_SWITCH_MUI2) {
                gregtech.api.modularui2.CoverUIFactory.INSTANCE
                    .open((EntityPlayerMP) aPlayer, coverID, coverable, coverSide);
            } else {
                GTUIInfos.openCoverUI(coverable, aPlayer, coverSide);
            }
            return true;
        }
        return false;
    }

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

    public final void setTickRateAddition(int newValue) {
        tickRateAddition = clamp(newValue);
    }

    private static int clamp(int input) {
        return Math.min(MAX_TICK_RATE_ADDITION, Math.max(0, input));
    }

    /**
     * @return If {@link #tickRateAddition} cannot go any higher
     */
    public boolean isTickRateAdditionMax() {
        // Mimic adjustTickRateMultiplier logic
        int simulatedTickRateAddition = clamp(tickRateAddition + 20);
        return tickRateAddition
            == clamp(simulatedTickRateAddition - ((getMinimumTickRate() + simulatedTickRateAddition) % 20));
    }

    /**
     * Returns information about the cover's tick rate.
     *
     * @return An instance of tick rate components
     */
    @NotNull
    public ClientTickRateFormatter getCurrentTickRateFormatted() {
        return new ClientTickRateFormatter(getTickRate());
    }

    /**
     * Gets the Tick Rate for doCoverThings of the Cover
     * <p/>
     * 0 = No Ticks! Yes, 0 is Default, you have to override this
     */
    public int getMinimumTickRate() {
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

    // endregion
}
