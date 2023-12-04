package kubatech.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.Theme;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.ChangeableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedRow;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;
import com.kuba6000.mobsinfo.api.utils.ItemID;

import kubatech.api.helpers.GTHelper;
import kubatech.api.utils.ModUtils;

public class DynamicInventory<T> {

    int width, height;
    Supplier<Integer> slotsGetter;
    private int slots = 0;
    private int usedSlots = 0;
    List<T> inventory;
    TInventoryGetter<T> inventoryGetter;
    TInventoryInjector inventoryInjector = null;
    TInventoryExtractor<T> inventoryExtractor = null;
    TInventoryReplacerOrMerger inventoryReplacer = null;
    Supplier<Boolean> isEnabledGetter = null;
    boolean isEnabled = true;

    public DynamicInventory(int width, int height, Supplier<Integer> slotsGetter, List<T> inventory,
        TInventoryGetter<T> inventoryGetter) {
        this.width = width;
        this.height = height;
        this.slotsGetter = slotsGetter;
        this.inventory = inventory;
        this.inventoryGetter = inventoryGetter;
    }

    public DynamicInventory<T> allowInventoryInjection(TInventoryInjector inventoryInjector) {
        this.inventoryInjector = inventoryInjector;
        return this;
    }

    public DynamicInventory<T> allowInventoryExtraction(TInventoryExtractor<T> inventoryExtractor) {
        this.inventoryExtractor = inventoryExtractor;
        return this;
    }

    public DynamicInventory<T> allowInventoryReplace(TInventoryReplacerOrMerger inventoryReplacer) {
        this.inventoryReplacer = inventoryReplacer;
        return this;
    }

    public DynamicInventory<T> setEnabled(Supplier<Boolean> isEnabled) {
        this.isEnabledGetter = isEnabled;
        return this;
    }

    public UITexture getItemSlot() {
        return ModularUITextures.ITEM_SLOT;
    }

    @SuppressWarnings("UnstableApiUsage")
    public Widget asWidget(ModularWindow.Builder builder, UIBuildContext buildContext) {
        ChangeableWidget container = new ChangeableWidget(() -> createWidget(buildContext.getPlayer()));

        // TODO: Only reset the widget when there are more slot stacks, otherwise just refresh them somehow

        container.attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> {
            if (slots != slotsGetter.get()) {
                slots = slotsGetter.get();
                container.notifyChangeNoSync();
            }
            return slots;
        }, i -> {
            if (slots != i) {
                slots = i;
                container.notifyChangeNoSync();
            }
        }), builder)
            .attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> {
                if (usedSlots != inventory.size()) {
                    usedSlots = inventory.size();
                    container.notifyChangeNoSync();
                }
                return usedSlots;
            }, i -> {
                if (usedSlots != i) {
                    usedSlots = i;
                    container.notifyChangeNoSync();
                }
            }), builder)
            .attachSyncer(new FakeSyncWidget.ListSyncer<>(() -> {
                HashMap<ItemID, Integer> itemMap = new HashMap<>();
                HashMap<ItemID, ItemStack> stackMap = new HashMap<>();
                HashMap<ItemID, ArrayList<Integer>> realSlotMap = new HashMap<>();
                for (int i = 0, mStorageSize = inventory.size(); i < mStorageSize; i++) {
                    ItemStack stack = inventoryGetter.get(inventory.get(i));
                    ItemID id = ItemID.createNoCopy(stack, false);
                    itemMap.merge(id, 1, Integer::sum);
                    stackMap.putIfAbsent(id, stack);
                    realSlotMap.computeIfAbsent(id, unused -> new ArrayList<>())
                        .add(i);
                }
                List<GTHelper.StackableItemSlot> newDrawables = new ArrayList<>();
                for (Map.Entry<ItemID, Integer> entry : itemMap.entrySet()) {
                    newDrawables.add(
                        new GTHelper.StackableItemSlot(
                            entry.getValue(),
                            stackMap.get(entry.getKey()),
                            realSlotMap.get(entry.getKey())));
                }
                if (!Objects.equals(newDrawables, drawables)) {
                    drawables = newDrawables;
                    container.notifyChangeNoSync();
                }
                return drawables;
            }, l -> {
                drawables.clear();
                drawables.addAll(l);
                container.notifyChangeNoSync();
            }, (buffer, i) -> {
                try {
                    i.write(buffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, buffer -> {
                try {
                    return GTHelper.StackableItemSlot.read(buffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }), builder);
        if (isEnabledGetter != null) {
            container.attachSyncer(new FakeSyncWidget.BooleanSyncer(isEnabledGetter, i -> isEnabled = i), builder);
        }
        return container;
    }

    List<GTHelper.StackableItemSlot> drawables = new ArrayList<>();

    private Widget createWidget(EntityPlayer player) {
        Scrollable dynamicInventoryWidget = new Scrollable().setVerticalScroll();

        ArrayList<Widget> buttons = new ArrayList<>();

        if (!ModUtils.isClientThreaded()) {
            HashMap<ItemID, Integer> itemMap = new HashMap<>();
            HashMap<ItemID, ItemStack> stackMap = new HashMap<>();
            HashMap<ItemID, ArrayList<Integer>> realSlotMap = new HashMap<>();
            for (int i = 0, inventorySize = inventory.size(); i < inventorySize; i++) {
                ItemStack stack = inventoryGetter.get(inventory.get(i));
                ItemID id = ItemID.createNoCopy(stack, false);
                itemMap.merge(id, 1, Integer::sum);
                stackMap.putIfAbsent(id, stack);
                realSlotMap.computeIfAbsent(id, unused -> new ArrayList<>())
                    .add(i);
            }
            drawables = new ArrayList<>();
            for (Map.Entry<ItemID, Integer> entry : itemMap.entrySet()) {
                drawables.add(
                    new GTHelper.StackableItemSlot(
                        entry.getValue(),
                        stackMap.get(entry.getKey()),
                        realSlotMap.get(entry.getKey())));
            }
        }

        for (int ID = 0; ID < drawables.size(); ID++) {
            final int finalID = ID;
            buttons.add(new ButtonWidget() {

                @Override
                public void drawBackground(float partialTicks) {
                    super.drawBackground(partialTicks);
                    if (!isEnabled) {
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glEnable(GL11.GL_BLEND);
                        GlStateManager.colorMask(true, true, true, false);
                        ModularGui.drawSolidRect(1, 1, 16, 16, Color.withAlpha(Color.BLACK.normal, 0x80));
                        GlStateManager.colorMask(true, true, true, true);
                        GL11.glDisable(GL11.GL_BLEND);
                    }
                    // Copied from SlotWidget#draw
                    else if (isHovering() && !getContext().getCursor()
                        .hasDraggable()) {
                            GL11.glDisable(GL11.GL_LIGHTING);
                            GL11.glEnable(GL11.GL_BLEND);
                            GlStateManager.colorMask(true, true, true, false);
                            ModularGui.drawSolidRect(1, 1, 16, 16, Theme.INSTANCE.getSlotHighlight());
                            GlStateManager.colorMask(true, true, true, true);
                            GL11.glDisable(GL11.GL_BLEND);
                        }
                }
            }.setPlayClickSound(false)
                .setOnClick((clickData, widget) -> {
                    if (!(player instanceof EntityPlayerMP)) return;
                    if (!isEnabledGetter.get()) return;

                    if (clickData.mouseButton == 2) {
                        // special button handler goes here
                        if (drawables.size() <= finalID) return;
                        if (player.capabilities.isCreativeMode && player.inventory.getItemStack() == null) {
                            int realID = drawables.get(finalID).realSlots.get(0);
                            ItemStack stack = inventoryGetter.get(inventory.get(realID))
                                .copy();
                            stack.stackSize = stack.getMaxStackSize();
                            player.inventory.setItemStack(stack);
                            ((EntityPlayerMP) player).isChangingQuantityOnly = false;
                            ((EntityPlayerMP) player).updateHeldItem();
                            return;
                        }
                    } else if (clickData.shift) {
                        if (inventoryExtractor == null) return;
                        if (drawables.size() <= finalID) return;
                        int realID = drawables.get(finalID).realSlots.get(0);
                        T removed = inventoryExtractor.extract(realID);
                        if (removed != null) {
                            ItemStack stack = inventoryGetter.get(removed);
                            if (player.inventory.addItemStackToInventory(stack))
                                player.inventoryContainer.detectAndSendChanges();
                            else player.entityDropItem(stack, 0.f);
                            return;
                        }
                    } else {
                        ItemStack input = player.inventory.getItemStack();
                        if (input != null) {
                            if (drawables.size() > finalID) {
                                if (inventoryReplacer == null) return;
                                int realID = drawables.get(finalID).realSlots.get(0);
                                ItemStack removed = inventoryReplacer.replaceOrMerge(realID, input);
                                if (removed == null) return;
                                player.inventory.setItemStack(removed.stackSize == 0 ? null : removed);
                            } else {
                                if (inventoryInjector == null) return;
                                if (clickData.mouseButton == 1) {
                                    ItemStack copy = input.copy();
                                    copy.stackSize = 1;
                                    ItemStack leftover = inventoryInjector.inject(copy);
                                    if (leftover == null) return;
                                    input.stackSize--;
                                    if (input.stackSize > 0) {
                                        ((EntityPlayerMP) player).isChangingQuantityOnly = true;
                                        ((EntityPlayerMP) player).updateHeldItem();
                                        return;
                                    } else player.inventory.setItemStack(null);
                                } else {
                                    ItemStack leftover = inventoryInjector.inject(input);
                                    if (leftover == null) return;
                                    if (input.stackSize > 0) {
                                        ((EntityPlayerMP) player).isChangingQuantityOnly = true;
                                        ((EntityPlayerMP) player).updateHeldItem();
                                        return;
                                    } else player.inventory.setItemStack(null);
                                }
                            }
                            ((EntityPlayerMP) player).isChangingQuantityOnly = false;
                            ((EntityPlayerMP) player).updateHeldItem();
                            return;
                        }
                        if (drawables.size() > finalID) {
                            if (inventoryExtractor == null) return;
                            int realID = drawables.get(finalID).realSlots.get(0);
                            T removed = inventoryExtractor.extract(realID);
                            if (removed != null) {
                                ItemStack stack = inventoryGetter.get(removed);
                                player.inventory.setItemStack(stack);
                                ((EntityPlayerMP) player).isChangingQuantityOnly = false;
                                ((EntityPlayerMP) player).updateHeldItem();
                                return;
                            }
                        }
                    }
                })
                .setBackground(
                    () -> new IDrawable[] { getItemSlot(),
                        new ItemDrawable(drawables.size() > finalID ? drawables.get(finalID).stack : null)
                            .withFixedSize(16, 16, 1, 1),
                        new Text(
                            (drawables.size() > finalID && drawables.get(finalID).count > 1)
                                ? (drawables.get(finalID).count > 99 ? "+99"
                                    : String.valueOf(drawables.get(finalID).count))
                                : "").color(Color.WHITE.normal)
                                    .alignment(Alignment.TopLeft)
                                    .withOffset(1, 1),
                        new Text(
                            (drawables.size() > finalID && drawables.get(finalID).stack.stackSize > 1)
                                ? String.valueOf(drawables.get(finalID).stack.stackSize)
                                : "").color(Color.WHITE.normal)
                                    .shadow()
                                    .alignment(Alignment.BottomRight) })
                .dynamicTooltip(() -> {
                    if (drawables.size() > finalID) {
                        List<String> tip = new ArrayList<>(
                            Collections.singletonList(drawables.get(finalID).stack.getDisplayName()));
                        if (drawables.get(finalID).count > 1) tip.add(
                            EnumChatFormatting.DARK_PURPLE + "There are "
                                + drawables.get(finalID).count
                                + " identical slots");
                        return tip;
                    }
                    return Collections.emptyList();
                })
                .setSize(18, 18));
        }

        buttons.add(new ButtonWidget() {

            @Override
            public void drawBackground(float partialTicks) {
                super.drawBackground(partialTicks);
                if (!isEnabled) {
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glEnable(GL11.GL_BLEND);
                    GlStateManager.colorMask(true, true, true, false);
                    ModularGui.drawSolidRect(1, 1, 16, 16, Color.withAlpha(Color.BLACK.normal, 0x80));
                    GlStateManager.colorMask(true, true, true, true);
                    GL11.glDisable(GL11.GL_BLEND);
                }
                // Copied from SlotWidget#draw
                else if (isHovering() && !getContext().getCursor()
                    .hasDraggable()) {
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glEnable(GL11.GL_BLEND);
                        GlStateManager.colorMask(true, true, true, false);
                        ModularGui.drawSolidRect(1, 1, 16, 16, Theme.INSTANCE.getSlotHighlight());
                        GlStateManager.colorMask(true, true, true, true);
                        GL11.glDisable(GL11.GL_BLEND);
                    }
            }
        }.setPlayClickSound(false)
            .setOnClick((clickData, widget) -> {
                if (!(player instanceof EntityPlayerMP)) return;
                if (!isEnabledGetter.get()) return;
                ItemStack input = player.inventory.getItemStack();
                if (input != null) {
                    if (clickData.mouseButton == 1) {
                        ItemStack copy = input.copy();
                        copy.stackSize = 1;
                        ItemStack leftover = inventoryInjector.inject(copy);
                        if (leftover == null) return;
                        input.stackSize--;
                        if (input.stackSize > 0) {
                            ((EntityPlayerMP) player).isChangingQuantityOnly = true;
                            ((EntityPlayerMP) player).updateHeldItem();
                            return;
                        } else player.inventory.setItemStack(null);
                    } else {
                        ItemStack leftover = inventoryInjector.inject(input);
                        if (leftover == null) return;
                        if (input.stackSize > 0) {
                            ((EntityPlayerMP) player).isChangingQuantityOnly = true;
                            ((EntityPlayerMP) player).updateHeldItem();
                            return;
                        } else player.inventory.setItemStack(null);
                    }
                    ((EntityPlayerMP) player).isChangingQuantityOnly = false;
                    ((EntityPlayerMP) player).updateHeldItem();
                    return;
                }
            })
            .setBackground(
                () -> new IDrawable[] { getItemSlot(),
                    new Text(
                        (slots - usedSlots) <= 1 ? ""
                            : ((slots - usedSlots) > 99 ? "+99" : String.valueOf((slots - usedSlots))))
                                .color(Color.WHITE.normal)
                                .alignment(Alignment.TopLeft)
                                .withOffset(1, 1) })
            .dynamicTooltip(() -> {
                List<String> tip = new ArrayList<>(Collections.singleton(EnumChatFormatting.GRAY + "Empty slot"));
                if (slots - usedSlots > 1)
                    tip.add(EnumChatFormatting.DARK_PURPLE + "There are " + (slots - usedSlots) + " identical slots");
                return tip;
            })
            .setSize(18, 18));

        final int perRow = width / 18;
        for (int i = 0, imax = ((buttons.size() - 1) / perRow); i <= imax; i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            for (int j = 0, jmax = (i == imax ? (buttons.size() - 1) % perRow : (perRow - 1)); j <= jmax; j++) {
                final int finalI = i * perRow;
                final int finalJ = j;
                final int ID = finalI + finalJ;
                row.widget(buttons.get(ID));
            }
            dynamicInventoryWidget.widget(row.setPos(0, i * 18));
        }

        return dynamicInventoryWidget.setSize(width, height);
    }

    @FunctionalInterface
    public interface TInventoryGetter<T> {

        /**
         * Allows to get an ItemStack from the dynamic inventory
         *
         * @param from Dynamic inventory item from which we want to take an item out
         * @return ItemStack or null if inaccessible
         */
        ItemStack get(T from);
    }

    @FunctionalInterface
    public interface TInventoryInjector {

        /**
         * Allows to insert an item to the dynamic inventory
         *
         * @param what ItemStack which we are trying to insert
         * @return Leftover ItemStack (stackSize == 0 if everything has been inserted) or null
         */
        ItemStack inject(ItemStack what);
    }

    @FunctionalInterface
    public interface TInventoryExtractor<T> {

        /**
         * Allows to extract an item from the dynamic inventory
         *
         * @param where Index from where we want to take an item out
         * @return Item that we took out or null
         */
        T extract(int where);
    }

    @FunctionalInterface
    public interface TInventoryReplacerOrMerger {

        /**
         * Allows to replace an item in Dynamic Inventory
         *
         * @param where which index we want to replace
         * @param stack what stack we want to replace it with
         * @return Stack that we are left with or null
         */
        ItemStack replaceOrMerge(int where, ItemStack stack);
    }

}
