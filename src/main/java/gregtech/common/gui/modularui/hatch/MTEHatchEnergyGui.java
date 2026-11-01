package gregtech.common.gui.modularui.hatch;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;

public class MTEHatchEnergyGui extends MTEHatchBaseGui<MTEHatchEnergy> {

    // size of the circuit diagram in the background, and the slot positions on it
    private static final int FUSE_BOX_WIDTH = 160, FUSE_BOX_HEIGHT = 76;
    private static final int[] FUSE_POSITION = { 28, 7 };
    private static final int[][] BACKUP_CAPACITOR_POSITIONS = { { 78, 36 }, { 112, 36 } };

    public MTEHatchEnergyGui(MTEHatchEnergy hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> parent = super.createContentSection(panel, syncManager);

        parent.child(
            GTGuiTextures.PICTURE_ENERGY_HATCH_FUSE_BOX.asWidget()
                .size(FUSE_BOX_WIDTH, FUSE_BOX_HEIGHT)
                .center());
        parent.child(
            placeOnFuseBox(new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, MTEHatchEnergy.FUSE_SLOT) {

                @Override
                public boolean canTakeStack(EntityPlayer player) {
                    return !machine.hasFuse();
                }
            }.singletonSlotGroup())
                .background(GTGuiTextures.OVERLAY_SLOT_FUSE), FUSE_POSITION));
        for (int i = 0; i < MTEHatchEnergy.BACKUP_CAPACITOR_SLOTS.length; i++) {
            parent.child(
                placeOnFuseBox(
                    new ItemSlot()
                        .slot(
                            new ModularSlot(machine.inventoryHandler, MTEHatchEnergy.BACKUP_CAPACITOR_SLOTS[i])
                                .singletonSlotGroup())
                        .background(GTGuiTextures.OVERLAY_SLOT_BACKUP_CAPACITOR),
                    BACKUP_CAPACITOR_POSITIONS[i]));
        }

        return parent;
    }

    /**
     * Places a slot on the centred circuit diagram, at its position relative to the top left of the diagram.
     */
    private static ItemSlot placeOnFuseBox(ItemSlot slot, int[] position) {
        return slot.leftRel(0.5f, position[0] - FUSE_BOX_WIDTH / 2, 0f)
            .topRel(0.5f, position[1] - FUSE_BOX_HEIGHT / 2, 0f);
    }

    @Override
    protected boolean supportsBottomRowOverlap() {
        return true;
    }
}
