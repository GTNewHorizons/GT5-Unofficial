package gregtech.common.tileentities.machines.multi.purification;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.client.GTTooltipHandler;

public class MTEHatchLensHousing extends MTEHatchInputBus {

    public MTEHatchLensHousing(int id, String name, String nameRegional) {
        super(
            id,
            name,
            nameRegional,
            GTTooltipHandler.Tier.UV.ordinal(),
            1,
            new String[] { "Holds a lens for UV laser focusing." });
    }

    public MTEHatchLensHousing(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchLensHousing(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public int getCircuitSlot() {
        return -1;
    }

    @Override
    public boolean allowSelectCircuit() {
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        getBaseMetaTileEntity().add1by1Slot(builder);
    }
}
