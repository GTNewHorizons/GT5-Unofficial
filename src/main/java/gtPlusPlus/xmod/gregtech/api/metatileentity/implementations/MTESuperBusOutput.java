package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.gui.widgets.PhantomItemButton;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.Utils;

@IMetaTileEntity.SkipGenerateDescription
public class MTESuperBusOutput extends MTEHatchOutputBus {

    public MTESuperBusOutput(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, getSlots(tier));
    }

    public MTESuperBusOutput(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, getSlots(tier), description, textures);
    }

    /**
     * Returns a factor of 16 based on tier.
     *
     * @param aTier The tier of this bus.
     * @return (1 + aTier) * 16
     */
    public static int getSlots(int aTier) {
        return (1 + aTier) * 16;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESuperBusOutput(this.mName, this.mTier, mDescriptionArray, this.mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.hasInventoryBeenModified()) {
            this.fillStacksIntoFirstSlots();
        }
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    public void updateSlots() {
        for (int i = 0; i < this.mInventory.length; ++i) {
            if (this.mInventory[i] != null && this.mInventory[i].stackSize <= 0) {
                this.mInventory[i] = null;
            }
        }
        this.fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        GTUtility.compactInventory(this);
    }

    @Override
    public String[] getDescription() {
        return Utils.splitLocalizedFormattedWithAlkalus("gt.blockmachines.output_bus_super.desc", getSlots(this.mTier));
    }

    @Override
    public void addUIWidgets(Builder builder, UIBuildContext buildContext) {
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int row = 0; row * 4 < inventoryHandler.getSlots() - 1; row++) {
            int columnsToMake = Math.min(inventoryHandler.getSlots() - row * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                scrollable.widget(
                    new SlotWidget(inventoryHandler, row * 4 + column).setPos(column * 18, row * 18)
                        .setSize(18, 18));
            }
        }
        builder.widget(
            scrollable.setSize(18 * 4 + 4, 18 * 4)
                .setPos(52, 7));

        if (acceptsItemLock()) {
            builder.widget(
                new PhantomItemButton(this).setPos(getGUIWidth() - 25, 40)
                    .setBackground(PhantomItemButton.FILTER_BACKGROUND));
        }
    }
}
