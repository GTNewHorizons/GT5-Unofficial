package gregtech.api.metatileentity.implementations;

import static gregtech.api.metatileentity.BaseTileEntity.BATTERY_SLOT_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.BATTERY_SLOT_TOOLTIP_ALT;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HarvestTool;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTUtility;

public abstract class MTETieredMachineBlock extends MetaTileEntity {

    /**
     * Value between [0 - 9] to describe the Tier of this Machine. PLZ [0-15] works - READ! GT_Values class.
     */
    public final byte mTier;

    /**
     * A simple Description.
     */
    public final String[] mDescriptionArray;

    /**
     * Contains all Textures used by this Block.
     */
    public final ITexture[][][] mTextures;

    public MTETieredMachineBlock(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aInvSlotCount);
        mTier = (byte) Math.max(0, Math.min(aTier, 14));
        mDescriptionArray = aDescription == null ? GTValues.emptyStringArray : new String[] { aDescription };
        // must always be the last call!
        if (GTMod.GT.isClientSide()) mTextures = getTextureSet(aTextures);
        else mTextures = null;
    }

    public MTETieredMachineBlock(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aInvSlotCount);
        mTier = (byte) Math.max(0, Math.min(aTier, 15));
        mDescriptionArray = aDescription == null ? GTValues.emptyStringArray : aDescription;

        // must always be the last call!
        if (GTMod.GT.isClientSide()) mTextures = getTextureSet(aTextures);
        else mTextures = null;
    }

    public MTETieredMachineBlock(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aInvSlotCount);
        mTier = (byte) aTier;
        mDescriptionArray = aDescription == null ? GTValues.emptyStringArray : aDescription;
        mTextures = aTextures;
    }

    @Override
    public byte getTileEntityBaseType() {
        if (mTier <= 0) return HarvestTool.WrenchLevel0.toTileEntityBaseType();

        // Require better wrench every 4 tiers.
        return switch (1 + (mTier - 1) / 4) {
            case 1 -> HarvestTool.WrenchLevel1.toTileEntityBaseType();
            case 2 -> HarvestTool.WrenchLevel2.toTileEntityBaseType();
            default -> HarvestTool.WrenchLevel3.toTileEntityBaseType();
        };
    }

    @Override
    public long getInputTier() {
        return mTier;
    }

    @Override
    public long getOutputTier() {
        return mTier;
    }

    @Override
    public String[] getDescription() {
        return mDescriptionArray;
    }

    /**
     * Used Client Side to get a Texture Set for this Block. Called after setting the Tier and the Description so that
     * those two are accessible.
     *
     * @param aTextures is the optional Array you can give to the Constructor.
     */
    public abstract ITexture[][][] getTextureSet(ITexture[] aTextures);

    protected SlotWidget createChargerSlot(int x, int y) {
        final String batterySlotTooltipKey;
        final Object[] batterySlotTooltipArgs;
        final String pTier1 = GTUtility.getColoredTierNameFromTier(mTier);
        if (mTier == GTValues.VN.length - 1) {
            batterySlotTooltipKey = BATTERY_SLOT_TOOLTIP_ALT;
            batterySlotTooltipArgs = new String[] { pTier1 };
        } else {
            batterySlotTooltipKey = BATTERY_SLOT_TOOLTIP;
            batterySlotTooltipArgs = new String[] { pTier1, GTUtility.getColoredTierNameFromTier((byte) (mTier + 1)) };
        }
        return createChargerSlot(x, y, batterySlotTooltipKey, batterySlotTooltipArgs);
    }

    protected SlotWidget createChargerSlot(int x, int y, String tooltipKey, Object[] tooltipArgs) {
        return (SlotWidget) new SlotWidget(inventoryHandler, rechargerSlotStartIndex()).disableShiftInsert()
            .setGTTooltip(() -> mTooltipCache.getData(tooltipKey, tooltipArgs))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_CHARGER)
            .setPos(x, y);
    }
}
