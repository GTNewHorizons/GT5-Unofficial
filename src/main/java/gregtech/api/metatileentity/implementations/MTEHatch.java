package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

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
     * Latched flag indicating new ingredients arrived since the controller last polled it. Lets a multiblock run a
     * recipe check the moment new ingredients arrive instead of waiting for its periodic poll. Consumed (and reset) by
     * {@link #justUpdated()}.
     */
    private boolean justUpdated = false;

    /**
     * Total contents seen on the previous tick, used by {@link #detectInventoryChange()} to distinguish ingredients
     * being inserted (which may enable a new recipe) from ingredients being consumed (which never can).
     */
    private long lastContentAmount = 0;

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
     * Latches a signal if this hatch gained contents since last tick. Input hatches and busses should call this from
     * {@code onPostTick}. Only a net <em>increase</em> latches: consuming ingredients lowers the tracked amount without
     * triggering, so the controller never runs an expensive recipe check on consumption alone. The signal is latched
     * (rather than read live) so it survives the arbitrary tile-entity tick ordering between this hatch and its
     * controlling multiblock.
     */
    protected void detectInventoryChange() {
        long amount = getContentAmount();
        if (amount > lastContentAmount) {
            justUpdated = true;
        }
        lastContentAmount = amount;
    }

    /**
     * @return a monotonic measure of this hatch's stored contents (summed item stack sizes plus stored fluid). Only its
     *         direction of change matters, not its absolute value. Subclasses holding fluids or non-standard storage
     *         should override to include those amounts.
     */
    protected long getContentAmount() {
        long amount = 0;
        if (mInventory != null) {
            for (ItemStack stack : mInventory) {
                if (stack != null) amount += stack.stackSize;
            }
        }
        return amount;
    }

    /**
     * Forces the next controller poll to run a recipe check. Use for changes that aren't a monotonic increase in stored
     * contents (e.g. a configuration circuit being changed), which {@link #detectInventoryChange()} cannot see.
     */
    protected void markJustUpdated() {
        justUpdated = true;
    }

    /**
     * @return {@code true} if new items and/or fluids were inserted since the last call, which triggers an immediate
     *         recipe check on the controlling multiblock. Resets the flag.
     */
    public boolean justUpdated() {
        boolean ret = justUpdated;
        justUpdated = false;
        return ret;
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
