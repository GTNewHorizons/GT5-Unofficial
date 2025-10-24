package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING_GLOW;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.MTESuperConductorProcessor;


public class MTEHatchBooster extends MTEHatch implements ISidedInventory {

    public MTEHatchBooster(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 5, 3, "Holds boosters for the SuperConductor Processor");
    }

    public MTEHatchBooster(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int side) {
        int[] slots = new int[]{0, 1, 2};
        return slots;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStack, int ordinalSide) {

        for (ItemStack stack : this.mInventory) {
            if (GTUtility.areStacksEqual(stack, itemStack)) return false;
        }
        return true;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStack, int ordinalSide) {
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
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        for (ItemStack stack : this.mInventory) {
            if (GTUtility.areStacksEqual(stack, itemStack)) return false;
        }
        return MTESuperConductorProcessor.isValidBooster(itemStack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 4;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        syncManager.registerSlotGroup("item_inv", 3);
        return GTGuis.mteTemplatePanelBuilder(this, data, syncManager, uiSettings)
            .build()
            .child(
                SlotGroupWidget.builder()
                    .matrix("III")
                    .key(
                        'I',
                        index -> new ItemSlot().slot(
                            new ModularSlot(inventoryHandler, index).slotGroup("item_inv")
                                .filter(item -> this.isItemValidForSlot(index, item))))
                    .build()
                    .alignX(Alignment.CENTER)
                    .alignY(0.25f));
    }
}
