package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.tileentities.machines.IHatchWatcher;

/**
 * Handles texture changes internally. No special calls are necessary other than updateTexture in add***ToMachineList.
 */
public abstract class MTEHatch extends MTEBasicTank implements ICasingTextureProvider {

    public enum ConnectionType {
        CABLE,
        WIRELESS,
        LASER
    }

    private int texturePage = 0;
    private int textureIndex = 0;

    private ItemStack ae2CraftingIcon;

    /**
     * Controllers watching this hatch for new ingredients (see {@link ISmartInputHatch}). When this hatch's contents
     * change we notify them so they can run an immediate recipe check instead of waiting for their periodic poll.
     */
    private final List<IHatchWatcher> watchers = new ArrayList<>();

    public MTEHatch(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String aDescription,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEHatch(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEHatch(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public static int getSlots(int aTier) {
        return (aTier + 1) * (aTier + 1);
    }

    /**
     * Registers a controller to be notified when this hatch gains new ingredients. Input hatches/busses implement
     * {@link ISmartInputHatch} so the controller registers itself here during structure assembly; the inherited
     * implementation satisfies that interface.
     */
    public void addWatcher(IHatchWatcher watcher) {
        watchers.add(watcher);
    }

    public void removeWatcher(IHatchWatcher watcher) {
        watchers.remove(watcher);
    }

    /** Asks every registered controller to run a recipe check on its next tick. */
    protected void notifyWatchers() {
        for (int i = 0; i < watchers.size(); i++) {
            watchers.get(i)
                .scheduleRecipeCheckImmediate();
        }
    }

    /**
     * Notifies watchers when this hatch's inventory or tank changed this tick. Relies on
     * {@link IGregTechTileEntity#hasInventoryBeenModified()}, which every relevant insertion path sets (item slots via
     * {@code setInventorySlotContents}/{@code decrStackSize}, the GUI/AE handler via
     * {@link MetaTileEntity#onContentsChanged(int)}, and fluids via {@code fill}). Consuming ingredients also sets the
     * flag, but that only ever costs a single redundant idle check at the tail of a run. Input hatches/busses should
     * call this from {@code onPostTick}.
     */
    protected void detectInventoryChange() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base != null && base.hasInventoryBeenModified()) {
            notifyWatchers();
        }
    }

    private int getOffsetTier() {
        return mTier < 4 ? 0 : mTier - 1;
    }

    public int getOffsetX() {
        return getOffsetTier() * 2;
    }

    public int getOffsetY() {
        return getOffsetTier() * 16;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    public abstract ITexture[] getTexturesActive(ITexture aBaseTexture);

    public abstract ITexture[] getTexturesInactive(ITexture aBaseTexture);

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {

        try {
            ITexture background = getCasingTexture();

            if (background == null) {
                background = Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1];
            }

            if (side != aFacing) {
                return new ITexture[] { background };
            } else {
                if (aActive) {
                    return getTexturesActive(background);
                } else {
                    return getTexturesInactive(background);
                }
            }
        } catch (NullPointerException npe) {
            return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[0][0] };
        }
    }

    @Override
    public ITexture getCasingTexture() {
        if (texturePage > 0 || textureIndex > 0) {
            return Textures.BlockIcons.casingTexturePages[texturePage][textureIndex];
        }
        return null;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("texturePage", texturePage);
        aNBT.setInteger("textureIndex", textureIndex);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        texturePage = aNBT.getInteger("texturePage");
        textureIndex = aNBT.getInteger("textureIndex");

        updateTexture(texturePage << 7 | textureIndex);
    }

    /**
     * Sets texture with page and index, called on add to machine list
     *
     * @param id (page<<7)+index of the texture
     */
    public final void updateTexture(int id) {
        int newTexturePage = id >> 7;
        int newTextureIndex = id & 127;
        if (newTexturePage == texturePage && newTextureIndex == textureIndex) return;
        texturePage = newTexturePage;
        textureIndex = newTextureIndex;

        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base.isServerSide()) {
            base.issueTileUpdate();
        } else {
            base.issueTextureUpdate();
        }
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound data = new NBTTagCompound();

        data.setInteger("texturePage", texturePage);
        data.setInteger("textureIndex", textureIndex);

        return data;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        texturePage = data.getInteger("texturePage");
        textureIndex = data.getInteger("textureIndex");
    }

    /**
     * Sets the icon for the owning multiblock used for AE2 crafting display of attached interfaces, called on add to
     * machine list
     */
    public final void updateCraftingIcon(ItemStack icon) {
        this.ae2CraftingIcon = icon;
    }

    @Override
    public ItemStack getMachineCraftingIcon() {
        return this.ae2CraftingIcon;
    }

    /**
     * Some multiblocks restrict hatches by tier. This method allows hatches to specify custom tier used for structure
     * check, while keeping {@link #mTier} for other uses.
     *
     * @return Tier used for multiblock structure
     */
    public byte getTierForStructure() {
        return mTier;
    }

    /**
     * Get the maximum amount of amperes to work with, which excludes the additional amps in for loss
     *
     * @return Working amps
     */
    public long maxWorkingAmperesIn() {
        return maxAmperesIn();
    }

    /**
     * Get the type of connection this hatch allows
     *
     * @return Connection type
     */
    public ConnectionType getConnectionType() {
        return ConnectionType.CABLE;
    }

    @Override
    public boolean doesFillContainers() {
        return false;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return false;
    }

    @Override
    public boolean canTankBeEmptied() {
        return false;
    }

    public int getCircuitSlot() {
        return -1;
    }

    public static String[] formatEnergyInfoDesc(boolean isDynamo, int tier, int amp, String key, Object... formatted) {
        return MTEHatch.formatEnergyInfoDesc(null, null, isDynamo, tier, amp, key, formatted);
    }

    public static String[] formatEnergyInfoDesc(String suffixTooltip, boolean isDynamo, int tier, int amp, String key,
        Object... formatted) {
        return MTEHatch.formatEnergyInfoDesc(null, suffixTooltip, isDynamo, tier, amp, key, formatted);
    }

    public static String[] formatEnergyInfoDesc(String[] author, String suffixTooltip, boolean isDynamo, int tier,
        int amp, String key, Object... formatted) {
        final List<String> additionalTooltips = new LinkedList<>();
        if (suffixTooltip != null) {
            Collections.addAll(additionalTooltips, suffixTooltip);
        }
        additionalTooltips.add(
            GTUtility.translate(
                "gt.tileentity.throughput",
                EnumChatFormatting.YELLOW + formatNumber(amp * GTValues.V[tier]) + EnumChatFormatting.RESET + " EU/t"));
        additionalTooltips.add(
            GTUtility.translate(
                isDynamo ? "gt.tileentity.eup_out" : "gt.tileentity.eup_in",
                TooltipHelper.voltageText(GTValues.V[tier])));
        additionalTooltips.add(GTUtility.translate("gt.tileentity.amperage", TooltipHelper.ampText(amp)));
        if (author != null) {
            additionalTooltips.add(GTAuthors.buildAuthorsWithFormat(author));
        }
        final String[] suffixs = additionalTooltips.toArray(new String[0]);
        if (formatted.length == 0) {
            return GTSplit.splitLocalizedWithSuffix(key, suffixs);
        }
        return GTSplit.splitLocalizedFormattedWithSuffix(key, suffixs, formatted);
    }

    public static void addColorChannelInfo(List<String> tooltip, byte color) {
        if (color >= 0 && color < 16) {
            tooltip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.hatch.color_channel",
                    Dyes.VALUES[color].formatting + Dyes.VALUES[color].getLocalizedDyeName()));
        }
    }

    @Override
    protected boolean useMui2() {
        return false;
    }
}
