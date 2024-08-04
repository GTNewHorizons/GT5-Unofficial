package gregtech.common.tileentities.machines.multi.purification;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.client.GT_TooltipHandler;

public class GT_MetaTileEntity_LensHousing extends GT_MetaTileEntity_Hatch_InputBus {

    public GT_MetaTileEntity_LensHousing(int id, String name, String nameRegional) {
        super(
            id,
            name,
            nameRegional,
            GT_TooltipHandler.Tier.UV.ordinal(),
            1,
            new String[] { "Holds a lens for UV laser focusing." });
    }

    public GT_MetaTileEntity_LensHousing(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_LensHousing(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
