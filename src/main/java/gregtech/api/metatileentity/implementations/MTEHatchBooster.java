package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING_GLOW;
import static gregtech.common.modularui2.util.CommonGuiComponents.gridTemplate1by1;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.render.TextureFactory;
import gregtech.common.tileentities.machines.multi.MTESuperConductorProcessor;

public class MTEHatchBooster extends MTEHatch {

    public MTEHatchBooster(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 5, 1, "Holds boosters for the SuperConductor Processor");
    }

    public MTEHatchBooster(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(inventoryHandler, 0).setFilter(MTESuperConductorProcessor::isValidBooster)
                .setAccess(true, true)
                .setPos(79, 34));
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_EMS_HOUSING)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_EMS_HOUSING_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_EMS_HOUSING)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_EMS_HOUSING_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public int getInventoryStackLimit() {
        return 3;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchBooster(mName, mTier, mDescriptionArray, mTextures);
    }

    public int getBoosterIDInSlot(int index) {
        if (index < this.getInventoryStackLimit()) {
            ItemStack slot = this.getStackInSlot(index);
            if (slot != null) {
                return switch (slot.getItemDamage()) {
                    case 32150 -> 2;
                    case 32151 -> 3;
                    case 32152 -> 4;
                    case 32153 -> 5;
                    case 32154 -> 6;
                    case 32155 -> 7;
                    case 32156 -> 8;
                    case 32157 -> 9;
                    case 32158 -> 10;
                    case 32159 -> 11;
                    case 32160 -> 12;
                    default -> -1;
                };
            }
        }
        return -1;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        syncManager.registerSlotGroup("item_inv", 1);
        return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .build()
            .child(
                gridTemplate1by1(
                    index -> new ItemSlot().slot(
                        new ModularSlot(inventoryHandler, index).slotGroup("item_inv")
                            .filter(MTESuperConductorProcessor::isValidBooster))));
    }
}
