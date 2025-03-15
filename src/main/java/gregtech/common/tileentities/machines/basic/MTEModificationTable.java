package gregtech.common.tileentities.machines.basic;

import static gregtech.api.registries.MechanicalArmorRegistry.getMechanicalAugment;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.gui.modularui.GTUITextures;
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

    private final ItemStackHandler inputHandler = new ItemStackHandler(2);
    private ItemStackHandler equipmentGridHandler = new ItemStackHandler(16);

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

        if (--modItem.stackSize == 0) inputHandler.setStackInSlot(0, null);

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

    public NBTTagList saveSlotsToNBT(MultiChildWidget parent, MechArmorBase armor) {
        NBTTagList augments = armor.getStack()
            .getTagCompound()
            .getTagList("augments", Constants.NBT.TAG_STRING);
        NBTTagList newAugments = new NBTTagList();
        List<Widget> slots = parent.getChildren();

        for (int i = 0; i < slots.size(); i++) {
            Widget ugh = slots.get(i);
            SlotWidget slot = (SlotWidget) ugh;
            if (!(ugh instanceof SlotWidget)) continue;
            BaseSlot mcSlot = slot.getMcSlot();
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

    final static int MOD_WINDOW_ID = 250;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(inputHandler, 0).setFilter((x) -> x.getItem() instanceof ItemAugmentBase)
                .setAccess(true, true)
                .setPos(79, 34))
            .widget(
                new SlotWidget(inputHandler, 1).setFilter((x) -> x.getItem() instanceof MechArmorBase)
                    .setAccess(true, true)
                    .setPos(79, 50)
                    .setBackground(() -> new IDrawable[] { new ItemDrawable(new ItemStack(Items.iron_helmet)) }))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                ItemStack coreItem = inputHandler.getStackInSlot(0);
                ItemStack armorItem = inputHandler.getStackInSlot(1);

                if (coreItem == null || armorItem == null) return;
                setTagFromItem(armorItem, coreItem);
            })
                .setPos(20, 20)
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add(GTUITextures.OVERLAY_BUTTON_CHECKMARK);
                    return ret.toArray(new IDrawable[0]);
                }))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                ItemStack armorItem = inputHandler.getStackInSlot(1);
                if (armorItem == null || !(armorItem.getItem() instanceof MechArmorBase)) return;
                if (!widget.isClient()) widget.getContext()
                    .openSyncedWindow(MOD_WINDOW_ID);
            })
                .setPos(36, 20)
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM);
                    return ret.toArray(new IDrawable[0]);
                }));
        buildContext
            .addSyncedWindow(MOD_WINDOW_ID, player -> createEquipmentGrid(player, inputHandler.getStackInSlot(1)));
    }

    // TODO handle NPE when you insert armor without frame data
    private ModularWindow createEquipmentGrid(final EntityPlayer player, ItemStack armorItem) {

        MechArmorBase mechArmorBase = (MechArmorBase) armorItem.getItem();

        NBTTagList data = armorItem.getTagCompound()
            .getTagList("augments", Constants.NBT.TAG_STRING);
        String[] frameData = getMechanicalAugment(
            armorItem.getTagCompound()
                .getInteger("frame")).size;

        ModularWindow.Builder builder = ModularWindow
            .builder((frameData[0].length() * 18) + 10, (frameData.length * 18) + 10);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        builder.setGuiTint(this.getGUIColorization());
        builder.setDraggable(true);

        int maxSlots = getTotalSlots(frameData);
        equipmentGridHandler.setSize(maxSlots + 1);

        MultiChildWidget slots = createSlotWidgets(frameData);
        for (int i = 0; i < slots.getChildren()
            .size(); i++) {
            final int tempI = i;
            ((SlotWidget) slots.getChildren()
                .get(i)).setFilter((ItemStack stack) -> canPutStack(stack, tempI, frameData, slots, maxSlots));
        }
        populateSlotWidgets(data, slots, builder);

        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                // remaking NBT every time is probably bad practice, but the other option involves looking through the
                // whole thing anyway
                armorItem.getTagCompound()
                    .setTag("augments", saveSlotsToNBT(slots, mechArmorBase));
                widget.getContext()
                    .closeWindow(MOD_WINDOW_ID);
            }
        })
            .setPos(5, slots.getPos().y - 10)
            .setSize(16, 16)
            .setBackground(() -> {
                List<UITexture> tex = new ArrayList<>();
                tex.add(GTUITextures.BUTTON_STANDARD);
                tex.add(GTUITextures.OVERLAY_BUTTON_DISABLE);
                return tex.toArray(new IDrawable[0]);
            }));
        // updateTextures(builder, slots, frameData);
        builder.widget(slots);
        return builder.build();
    }

    private MultiChildWidget createSlotWidgets(String[] frameData) {
        MultiChildWidget parent = new MultiChildWidget();
        int slotIndex = 0;
        for (int i = 0; i < frameData.length; i++) {
            String row = frameData[i];
            for (int j = 0; j < row.length(); j++) {
                char frameSlot = row.toCharArray()[j];
                if (frameSlot == '#') {
                    Widget slot = new SlotWidget(new BaseSlot(equipmentGridHandler, slotIndex) {

                        @Override
                        public int getSlotStackLimit() {
                            return 1;
                        }

                        @Override
                        // TODO add some kind of checking
                        public void onSlotChanged() {
                            super.onSlotChanged();
                        }
                    }).setPos((18 * j) + 5, (18 * i) + 5);
                    parent.addChild(slot);
                    slotIndex++;
                }
            }
        }
        return parent;
    }

    private void populateSlotWidgets(NBTTagList dataList, MultiChildWidget slots, ModularWindow.Builder builder) {
        for (int i = 0; i < dataList.tagCount(); i++) {
            String data = dataList.getStringTagAt(i);
            if (data.isEmpty()) continue;
            ItemAugmentBase augment = getAugmentFromAugmentNBT(new NBTTagString(data));
            int index = getIndexFromAugmentNBT(new NBTTagString(data));

            if (slots.getChildren()
                .get(index) instanceof SlotWidget slot) {
                slot.getMcSlot()
                    .putStack(new ItemStack(augment, 1));
                // Create the texture overlay for "big" augment
                builder.widget(
                    new DrawableWidget().setDrawable(GTUITextures.OVERLAY_EQ_GRID_FIREIMMUNITY)
                        .setPos(slot.getPos())
                        .setSize(augment.size[0].length() * 18, augment.size.length * 18));
            }
        }
    }

    // TODO remake this method
    private void updateTextures(ModularWindow.Builder builder, MultiChildWidget slots, String[] frameData) {
        for (int slot = 0; slot < equipmentGridHandler.getSlots(); slot++) {
            ItemStack item = equipmentGridHandler.getStackInSlot(slot);
            if (item == null) return;
            if (!(item.getItem() instanceof ItemAugmentBase augment)) return;
            if (!augment.isSimpleAugment()) return;
            for (int i = 0; i < augment.size.length; i++) {
                String row = augment.size[i];
                for (int j = 0; j < row.length(); j++) {
                    builder.widget(
                        new DrawableWidget().setDrawable(new ItemDrawable(item))
                            .setSize(16, 16)
                            .setPos(
                                slots.getChildren()
                                    .get(slot)
                                    .getPos().x + (18 * (i * frameData[0].length())),
                                slots.getChildren()
                                    .get(slot)
                                    .getPos().y + (18 * j)));
                    // .setPos(getSlotOffset(slots, slot, frameData)));
                }
            }
        }
    }

    private Pos2d getSlotOffset(MultiChildWidget slots, int slot, String[] frameData) {
        int x = slots.getPos().x + ((slot % 4) * 16);
        int y = slots.getPos().y + ((slot / 4) * 16);

        return new Pos2d(x + 5, y + 2);
    }

    public boolean canPutStack(ItemStack stack, int augIndex, String[] frameData, MultiChildWidget parent,
        int maxSlots) {
        if (!(stack.getItem() instanceof ItemAugmentBase augment) || !augment.isSimpleAugment()) return false;
        int totalAugIndex = getOtherIndex(frameData, augIndex);
        List<Widget> slots = parent.getChildren();
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
                if (((SlotWidget) slots.get(augIndex + ((i * frameData[0].length()) + j))).getMcSlot()
                    .getHasStack()) return false;
            }
        }
        return true;
    }
}
