package kubatech.tileentity.gregtech.hatch;

import static gregtech.common.modularui2.util.CommonGuiComponents.gridTemplate1by1;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.modularui2.GTGuis;
import gregtech.client.GTTooltipHandler;

public class MTEElectrodeHatch extends MTEHatchInputBus {

    public MTEElectrodeHatch(int id, String name, String nameRegional) {
        super(
            id,
            name,
            nameRegional,
            GTTooltipHandler.Tier.IV.ordinal(),
            1,
            new String[] { "Holds an electrode." });
    }

    public MTEElectrodeHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEElectrodeHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
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
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        syncManager.registerSlotGroup("item_inv", 1);
        return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .build()
            .child(
                gridTemplate1by1(
                    index -> new ItemSlot().slot(new ModularSlot(inventoryHandler, index).slotGroup("item_inv"))));
    }
}
