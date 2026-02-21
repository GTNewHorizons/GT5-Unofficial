package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.GTAuthors;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.common.gui.modularui.hatch.MTEHatchChiselBusGui;
import gtPlusPlus.core.util.Utils;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchChiselBus extends MTEHatchInputBus implements IAddUIWidgets {

    public MTEHatchChiselBus(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier);
    }

    public MTEHatchChiselBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier), aDescription, aTextures);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchChiselBusGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < getSlots(this.mTier);
    }

    public static int getSlots(int aTier) {
        return (1 + aTier) * 16 + 1;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchChiselBus(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public boolean allowSelectCircuit() {
        return false;
    }

    @Override
    public String[] getDescription() {
        return Utils.splitLocalizedFormattedWithAuthor(
            "gt.blockmachines.input_bus_chisel.desc",
            GTAuthors.AuthorQuetz4l,
            getSlots(this.mTier) - 1);
    }

}
