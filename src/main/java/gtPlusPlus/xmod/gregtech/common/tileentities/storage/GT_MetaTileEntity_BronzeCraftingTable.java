package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_ItemStack;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;

public class GT_MetaTileEntity_BronzeCraftingTable extends GT_MetaTileEntity_AdvancedCraftingTable {

    public GT_MetaTileEntity_BronzeCraftingTable(final int aID, final String aName, final String aNameRegional,
            final int aTier, final String aDescription) {
        super(aID, aName, aNameRegional, aTier, aDescription);
    }

    public GT_MetaTileEntity_BronzeCraftingTable(final String aName, final int aTier, final String[] aDescription,
            final ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean isTransformerUpgradable() {
        return false;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BronzeCraftingTable(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.mTextures);
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GT_ItemStack aStack) {
        return GregTech_API.getCoverBehaviorNew(aStack.toStack()).isSimpleCover();
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return 0;
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    protected boolean isAdvanced() {
        return false;
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.BRONZE;
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.STEAM.apply(getSteamVariant());
    }

    @Override
    protected SlotWidget createElectricSlot(int index) {
        return (SlotWidget) new SlotWidget(inventoryHandler, index).setBackground(getGUITextureSet().getItemSlot());
    }

    @Override
    protected IDrawable getArrowOverlay() {
        return GTPP_UITextures.OVERLAY_SLOT_ARROW_BRONZE;
    }

    @Override
    protected IDrawable getParkOverlay() {
        return GTPP_UITextures.OVERLAY_SLOT_PARK_BRONZE;
    }

    @Override
    protected IDrawable getBlueprintOverlay() {
        return GTPP_UITextures.OVERLAY_SLOT_PAGE_PRINTED_BRONZE;
    }

    @Override
    protected IDrawable getCraftOutputOverlay() {
        return GTPP_UITextures.OVERLAY_SLOT_CRAFT_OUTPUT_BRONZE;
    }

    @Override
    protected IDrawable getButtonIcon() {
        return GTPP_UITextures.BUTTON_STANDARD_BRONZE;
    }

    @Override
    protected IDrawable getFlushOverlay() {
        return GTPP_UITextures.OVERLAY_BUTTON_FLUSH_BRONZE;
    }
}
