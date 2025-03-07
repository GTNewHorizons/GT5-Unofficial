package gregtech.common.tileentities.machines.basic;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.cleanroommc.modularui.widgets.ItemSlot;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import gregtech.api.items.ItemAugmentBase;
import gregtech.api.items.ItemAugmentCore;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;

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

    private void setTagFromItem(ItemStack armorItem, ItemStack modItem) {
        NBTTagCompound tag = getOrCreateNbtCompound(armorItem);

        // Sanity check, filter on the item slots should already verify this
        if (!(modItem.getItem() instanceof ItemAugmentBase augment && armorItem.getItem() instanceof MechArmorBase armor)) {
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

        //TODO: frames

        if (augment instanceof ItemAugmentCore core) {
            armorItem.getTagCompound().setInteger("core", core.getCoreid());
        }

        augment.getAttachedBehaviors().forEach(behavior -> behavior.addBehaviorNBT(armorItem, tag));

        if (--modItem.stackSize == 0) inputHandler.setStackInSlot(0, null);

        /*
        else if (modItem.isItemEqual(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1))) {
            tag.setString("frame", "Copper");
            tag.setShort("frameR", Materials.Copper.mRGBa[0]);
            tag.setShort("frameG", Materials.Copper.mRGBa[1]);
            tag.setShort("frameB", Materials.Copper.mRGBa[2]);
        } else if (modItem.isItemEqual(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1))) {
            tag.setString("frame", "Iron");
            tag.setShort("frameR", Materials.Iron.mRGBa[0]);
            tag.setShort("frameG", Materials.Iron.mRGBa[1]);
            tag.setShort("frameB", Materials.Iron.mRGBa[2]);
        } else if (modItem.isItemEqual(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1))) {
            tag.setString("frame", "Gold");
            tag.setShort("frameR", Materials.Gold.mRGBa[0]);
            tag.setShort("frameG", Materials.Gold.mRGBa[1]);
            tag.setShort("frameB", Materials.Gold.mRGBa[2]);
        }
         */
    }

    final static int MOD_WINDOW_ID = 250;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(inputHandler, 0).setFilter((x) -> x.getItem() instanceof ItemAugmentBase).setAccess(true, true)
                .setPos(79, 34))
            .widget(
                new SlotWidget(inputHandler, 1).setFilter((x) -> x.getItem() instanceof MechArmorBase).setAccess(true, true)
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

        buildContext.addSyncedWindow(MOD_WINDOW_ID, this::createEquipmentGrid);
    }

    private ModularWindow createEquipmentGrid(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(170, 85)
            .setDraggable(false);
        SlotGroup slots = createSlotGrid(4, 4);
        builder.widget(slots);
        updateTextures(builder, slots);
        return builder.build();
    }

    private void updateTextures(ModularWindow.Builder builder, SlotGroup slots) {
        for (int slot = 0; slot < equipmentGridHandler.getSlots(); slot++) {
            ItemStack item = equipmentGridHandler.getStackInSlot(slot);
            if (item != null) builder.widget(
                new DrawableWidget()
                    .setDrawable(new ItemDrawable(item))
                    .setSize(32, 32)
                    .setPos(getSlotOffset(slots, slot)));
        }
    }

    private Pos2d getSlotOffset(SlotGroup slots, int slot) {
        int x = slots.getPos().x + ((slot % 4) * 16);
        int y = slots.getPos().y + ((slot / 4) * 16);

        return new Pos2d(x + 5, y + 2);
    }

    private final ItemStackHandler equipmentGridHandler = new ItemStackHandler(16);

    private SlotGroup createSlotGrid(int length, int height, IDrawable... background) {
        if (background.length == 0) {
            background = new IDrawable[] { getGUITextureSet().getItemSlot() };
        }
        return (SlotGroup) SlotGroup.ofItemHandler(equipmentGridHandler, 4)
                .startFromSlot(0)
                .endAtSlot(15)
                .background(background)
                .applyForWidget(widget -> {
                    widget.setFilter(this::slotFilter);

                })
                .build()
                .setPos(52, 7);
    }

    private boolean slotFilter(ItemStack item) {
        return item.getItem() instanceof ItemAugmentBase;
    }
}
