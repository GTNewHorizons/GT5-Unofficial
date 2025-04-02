package gregtech.common.tileentities.machines.basic;

import static gregtech.api.gui.modularui.GTUITextures.OVERLAY_EQ_GRID_FIREIMMUNITY;
import static gregtech.api.registries.MechanicalArmorRegistry.getMechanicalAugment;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.drawable.GuiTextures;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ButtonWidget;
import com.cleanroommc.modularui.widgets.ItemSlot;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.ItemAugmentBase;
import gregtech.api.items.ItemAugmentCore;
import gregtech.api.items.ItemAugmentFrame;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.common.items.armor.MechArmorBase;

public class MTEModificationTable extends MTEBasicMachine implements IAddUIWidgets {

    public MTEModificationTable(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 1, "", 1, 1);
    }

    public MTEModificationTable(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEModificationTable(mName, mTier, mDescriptionArray, mTextures);
    }

    private ItemStackHandler gridInventoryHandler = new ItemStackHandler(16);

    private void setTagFromItem(ItemStack armorItem, ItemStack modItem) {
        NBTTagCompound tag = getOrCreateNbtCompound(armorItem);

        // Sanity check, filter on the item slots should already verify this
        if (!(modItem.getItem() instanceof ItemAugmentBase augment
            && armorItem.getItem() instanceof MechArmorBase armor)) {
            return;
        }

        // Verify behaviors meet requirements
        for (IArmorBehavior requiredBehavior : augment.getRequiredBehaviors()) {
            if (!tag.hasKey(requiredBehavior.getMainNBTTag())) return;
        }
        for (IArmorBehavior incompatibleBehavior : augment.getIncompatibleBehaviors()) {
            if (tag.hasKey(incompatibleBehavior.getMainNBTTag())) return;
        }

        // At this point the modification should be successful, verification has passed

        if (augment instanceof ItemAugmentFrame frame) {
            armorItem.getTagCompound()
                .setInteger("frame", frame.getId());
        }
        if (augment instanceof ItemAugmentCore core) {
            armorItem.getTagCompound()
                .setInteger("core", core.getId());
        }

        augment.getAttachedBehaviors()
            .forEach(behavior -> behavior.addBehaviorNBT(armorItem, tag));

        if (--modItem.stackSize == 0) inventoryHandler.setStackInSlot(0, null);

        /*
         * else if (modItem.isItemEqual(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1))) {
         * tag.setString("frame", "Copper");
         * tag.setShort("frameR", Materials.Copper.mRGBa[0]);
         * tag.setShort("frameG", Materials.Copper.mRGBa[1]);
         * tag.setShort("frameB", Materials.Copper.mRGBa[2]);
         * } else if (modItem.isItemEqual(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1))) {
         * tag.setString("frame", "Iron");
         * tag.setShort("frameR", Materials.Iron.mRGBa[0]);
         * tag.setShort("frameG", Materials.Iron.mRGBa[1]);
         * tag.setShort("frameB", Materials.Iron.mRGBa[2]);
         * } else if (modItem.isItemEqual(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1))) {
         * tag.setString("frame", "Gold");
         * tag.setShort("frameR", Materials.Gold.mRGBa[0]);
         * tag.setShort("frameG", Materials.Gold.mRGBa[1]);
         * tag.setShort("frameB", Materials.Gold.mRGBa[2]);
         * }
         */
    }

    /*
     * returns: if successful or not
     */
    public boolean setGridSlot(int targetSlot, ItemStack armor, ItemAugmentBase augment) {
        NBTTagList augments = armor.getTagCompound()
            .getTagList("augments", Constants.NBT.TAG_STRING);
        ItemAugmentBase frame = getMechanicalAugment(
            armor.getTagCompound()
                .getInteger("frame"));

        if (isSlotOccupied(targetSlot, augments)) {
            augments.appendTag(new NBTTagString(serializeAugmentToString(augment, targetSlot)));
            return true;
        }
        return false;
    }

    public NBTTagList saveSlotsToNBT(ListWidget parent, MechArmorBase armor) {
        NBTTagList augments = armor.getStack()
            .getTagCompound()
            .getTagList("augments", Constants.NBT.TAG_STRING);
        NBTTagList newAugments = new NBTTagList();
        List<Widget> slots = parent.getChildren();

        for (int i = 0; i < slots.size(); i++) {
            Widget ugh = slots.get(i);
            ItemSlot slot = (ItemSlot) ugh;
            if (!(ugh instanceof ItemSlot)) continue;
            Slot mcSlot = slot.getVanillaSlot();
            if (mcSlot.getHasStack()) {
                if (mcSlot.getStack()
                    .getItem() instanceof ItemAugmentBase aug) {
                    if (!aug.isSimpleAugment()) continue;
                    String currentAugmentInfo = serializeAugmentToString(aug, i);
                    newAugments.appendTag(new NBTTagString(currentAugmentInfo));
                }
            }
        }

        return newAugments;
    }

    // TODO figure out a better name for this method
    public String serializeAugmentToString(ItemAugmentBase augment, int slot) {
        if (!augment.isSimpleAugment()) return null;
        StringBuilder sb = new StringBuilder();
        sb.append("augId:");
        sb.append(augment.getId());
        sb.append("|");
        sb.append("index:");
        sb.append(slot);

        return sb.toString();
    }

    public static Integer getIndexFromAugmentNBT(NBTTagString tag) {
        // Obfuscated method simply gets the string from the tag object
        String raw = tag.func_150285_a_();
        if (!raw.startsWith("augId")) return null;

        String[] data = raw.split("\\|");
        return Integer.valueOf(data[1].split(":")[1]);
    }

    public static ItemAugmentBase getAugmentFromAugmentNBT(NBTTagString tag) {
        // Obfuscated method simply gets the string from the tag object
        String raw = tag.func_150285_a_();
        if (!raw.startsWith("augId")) return null;

        String[] data = raw.split("\\|");
        return getMechanicalAugment(Integer.valueOf(data[0].split(":")[1]));
    }

    public Boolean isSlotOccupied(int targetSlot, NBTTagList grid) {
        for (Object s : grid.tagList) {
            NBTTagString string = (NBTTagString) s;
            if (!(s instanceof NBTTagString)) return null;
            if (getIndexFromAugmentNBT(string) == targetSlot) {
                return false;
            }
        }
        return true;
    }

    public int getTotalSlots(String[] frameData) {
        int totalSlots = 0;
        for (String row : frameData) {
            for (char slot : row.toCharArray()) {
                if (slot == '#') {
                    totalSlots++;
                }
            }
        }
        return totalSlots;
    }

    // TODO figure out a better name for this method
    public int getOtherIndex(String[] frameData, int index) {
        int otherIndex = 0;
        int temp = 0;
        out: for (String row : frameData) {
            for (char slot : row.toCharArray()) {
                otherIndex++;
                if (slot == '#') temp++;
                if (temp == index) break out;
            }
        }
        return otherIndex;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    // @Override
    // public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
    // super.addUIWidgets(builder, buildContext);
    // }

    final static int MOD_WINDOW_ID = 250;

    @Override
    // TODO background on the button children
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager) {
        ModularPanel ui = new ModularPanel("gt:modification_table");
        IPanelHandler popupPanel = syncManager
            .panel("popup_panel", (manager, syncHandler) -> createMUI2Grid(inventoryHandler.getStackInSlot(1)), true);
        syncManager.registerSlotGroup("main_group", 2);
        ui.child(
            new ItemSlot()
                .slot(
                    new ModularSlot(inventoryHandler, 0).filter((x) -> x.getItem() instanceof ItemAugmentBase)
                        .accessibility(true, true))
                .pos(79, 34));
        ui.child(
            new ItemSlot().slot(
                new ModularSlot(inventoryHandler, 1).filter((x) -> x.getItem() instanceof MechArmorBase)
                    .accessibility(true, true)
                    .slotGroup("main_group"))
                .pos(79, 50)
                .background(new ItemDrawable(new ItemStack(Items.iron_helmet))));
        ui.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            ItemStack coreItem = inventoryHandler.getStackInSlot(0);
            ItemStack armorItem = inventoryHandler.getStackInSlot(1);

            if (coreItem == null || armorItem == null) return false;
            setTagFromItem(armorItem, coreItem);
            return true;
        })
            .pos(20, 20)
            .size(16, 16));
        ui.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            ItemStack armorItem = inventoryHandler.getStackInSlot(1);
            if (armorItem == null || !(armorItem.getItem() instanceof MechArmorBase)) return false;
            popupPanel.openPanel();
            return true;
        })
            .pos(36, 20)
            .size(16, 16));

        return ui.bindPlayerInventory();
    }

    // TODO handle NPE when you insert armor without frame data
    private ModularPanel createMUI2Grid(ItemStack armorItem) {
        ModularPanel ui = new ModularPanel("gt:modification_table_grid");

        MechArmorBase mechArmorBase = (MechArmorBase) armorItem.getItem();

        NBTTagList data = armorItem.getTagCompound()
            .getTagList("augments", Constants.NBT.TAG_STRING);
        String[] frameData = getMechanicalAugment(
            armorItem.getTagCompound()
                .getInteger("frame")).size;

        ui.background(GuiTextures.MC_BACKGROUND);
        int maxSlots = getTotalSlots(frameData);
        gridInventoryHandler.setSize(maxSlots + 1);

        ListWidget slots = createSlotWidgets(frameData);
        List children = slots.getChildren();
        for (int i = 0; i < children.size(); i++) {
            ItemSlot slot = (ItemSlot) children.get(i);
            if (!(children.get(i) instanceof ItemSlot)) continue;
            final int tempI = i;
            ((ItemSlot) children.get(i)).getSlot()
                .filter((stack) -> canPutStack(stack, tempI, frameData, slots, maxSlots));
        }
        populateSlotWidgets(data, slots, ui);

        ui.child(new ButtonWidget<>().onMousePressed(mouseButton -> {
            armorItem.getTagCompound()
                .setTag("augments", saveSlotsToNBT(slots, mechArmorBase));
            ui.closeIfOpen(false);
            return true;
            // TODO add background
        })
            .pos(5, -10)
            .size(16, 16));

        ui.child(slots);
        return ui;
    }

    private ListWidget createSlotWidgets(String[] frameData) {
        ListWidget parent = new ListWidget();
        int slotIndex = 0;
        for (int i = 0; i < frameData.length; i++) {
            String row = frameData[i];
            for (int j = 0; j < row.length(); j++) {
                char frameSlot = row.toCharArray()[j];
                if (frameSlot == '#') {
                    Widget slot = new ItemSlot().slot(new ModularSlot(gridInventoryHandler, slotIndex) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }

                        @Override
                        // TODO add some kind of checking
                        public void onSlotChanged() {
                            super.onSlotChanged();
                        }
                    })
                        .pos((18 * j) + 5, (18 * i) + 5);
                    parent.addChild(slot, slotIndex);
                    slotIndex++;
                }
            }
        }
        return parent;
    }

    private void populateSlotWidgets(NBTTagList dataList, ListWidget slots, ModularPanel panel) {
        for (int i = 0; i < dataList.tagCount(); i++) {
            String data = dataList.getStringTagAt(i);
            if (data.isEmpty()) continue;
            ItemAugmentBase augment = getAugmentFromAugmentNBT(new NBTTagString(data));
            int index = getIndexFromAugmentNBT(new NBTTagString(data));

            if (slots.getChildren()
                .get(index) instanceof ItemSlot slot) {
                slot.getVanillaSlot()
                    .putStack(new ItemStack(augment, 1));
                // Create the texture overlay for "big" augment
                panel.child(
                    OVERLAY_EQ_GRID_FIREIMMUNITY.asWidget()
                        .pos(slot.getSlot().xDisplayPosition, slot.getSlot().yDisplayPosition));
            }
        }
    }

    public boolean canPutStack(ItemStack stack, int augIndex, String[] frameData, ListWidget slots, int maxSlots) {
        if (!(stack.getItem() instanceof ItemAugmentBase augment) || !augment.isSimpleAugment()) return false;
        int totalAugIndex = getOtherIndex(frameData, augIndex);
        String[] size = augment.size;

        for (int i = 0; i < size.length; i++) {
            String augRow = size[i];
            for (int j = 0; j < augRow.length(); j++) {
                char augSlot = augRow.toCharArray()[j];
                if (augSlot != '#') continue;

                char[] frameSlots = frameData[i].toCharArray();
                if ((j + (totalAugIndex % frameSlots.length)) > maxSlots) return false;
                // Desired slot is not a slot in the frameData
                if (frameData[i].toCharArray()[j + (totalAugIndex % frameSlots.length)] != '#') return false;

                // Look over the slots to see if any other augment (or link) currently holding this spot
                // TODO rename this terrible variable name
                int lookForIndex = augIndex + ((i * frameData[0].length()) + j);
                if (lookForIndex > maxSlots) return false;
                if (((ItemSlot) slots.getChildren()
                    .get(augIndex + ((i * frameData[0].length()) + j))).getVanillaSlot()
                        .getHasStack())
                    return false;
            }
        }
        return true;
    }
}
