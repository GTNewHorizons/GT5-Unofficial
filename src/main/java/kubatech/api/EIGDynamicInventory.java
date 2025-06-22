package kubatech.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
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

import kubatech.api.gui.AutoScalingStackSizeText;
import kubatech.api.helpers.GTHelper;
import kubatech.api.utils.ModUtils;

public class EIGDynamicInventory<T> {

    int width, height;
    Supplier<Integer> maxSeedCountGetter;
    Supplier<Integer> maxSeedTypeGetter;
    Supplier<Integer> usedSeedCountGetter;
    Supplier<Integer> usedSeedTypesGetter;
    private int maxSeedTypes = 0;
    private int maxSeedCount = 0;
    private int usedSeedTypes = 0;
    private int usedSeedCount = 0;
    List<T> inventory;
    TInventoryGetter<T> inventoryGetter;
    @Nullable
    TInventoryInjector inventoryInjector = null;
    @Nullable
    TInventoryExtractor<T> inventoryExtractor = null;
    @Nullable
    TInventoryReplacerOrMerger inventoryReplacer = null;
    @Nullable
    Supplier<Boolean> isEnabledGetter = null;
    boolean isEnabled = true;

    public EIGDynamicInventory(int width, int height, Supplier<Integer> maxSeedTypeGetter,
        Supplier<Integer> maxSeedCountGetter, Supplier<Integer> usedSeedTypesGetter,
        Supplier<Integer> usedSeedCountGetter, List<T> inventory, TInventoryGetter<T> inventoryGetter) {
        this.width = width;
        this.height = height;
        this.maxSeedTypeGetter = maxSeedTypeGetter;
        this.maxSeedCountGetter = maxSeedCountGetter;
        this.usedSeedTypesGetter = usedSeedTypesGetter;
        this.usedSeedCountGetter = usedSeedCountGetter;
        this.inventory = inventory;
        this.inventoryGetter = inventoryGetter;
    }

    public @NotNull EIGDynamicInventory<T> allowInventoryInjection(TInventoryInjector inventoryInjector) {
        this.inventoryInjector = inventoryInjector;
        return this;
    }

    public @NotNull EIGDynamicInventory<T> allowInventoryExtraction(TInventoryExtractor<T> inventoryExtractor) {
        this.inventoryExtractor = inventoryExtractor;
        return this;
    }

    public @NotNull EIGDynamicInventory<T> allowInventoryReplace(TInventoryReplacerOrMerger inventoryReplacer) {
        this.inventoryReplacer = inventoryReplacer;
        return this;
    }

    public @NotNull EIGDynamicInventory<T> setEnabled(Supplier<Boolean> isEnabled) {
        this.isEnabledGetter = isEnabled;
        return this;
    }

    public @NotNull UITexture getItemSlot() {
        return ModularUITextures.ITEM_SLOT;
    }

    @SuppressWarnings("UnstableApiUsage")
    public @NotNull Widget asWidget(ModularWindow.Builder builder, @NotNull UIBuildContext buildContext) {
        ChangeableWidget container = new ChangeableWidget(() -> createWidget(buildContext.getPlayer()));
        // TODO: Only reset the widget when there are more slot stacks, otherwise just refresh them somehow

        container
            // max seed types
            .attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> {
                int i = this.maxSeedTypeGetter.get();
                if (this.maxSeedTypes != i) {
                    this.maxSeedTypes = i;
                    container.notifyChangeNoSync();
                }
                return i;
            }, i -> {
                if (this.maxSeedTypes != i) {
                    this.maxSeedTypes = i;
                    container.notifyChangeNoSync();
                }
            }), builder)
            // used seed types
            .attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> {
                int i = this.usedSeedTypesGetter.get();
                if (this.usedSeedTypes != i) {
                    this.usedSeedTypes = i;
                    container.notifyChangeNoSync();
                }
                return i;
            }, i -> {
                if (this.usedSeedTypes != i) {
                    this.usedSeedTypes = i;
                    container.notifyChangeNoSync();
                }
            }), builder)
            // max seed count
            .attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> {
                int i = this.maxSeedCountGetter.get();
                if (this.maxSeedCount != i) {
                    this.maxSeedCount = i;
                    container.notifyChangeNoSync();
                }
                return i;
            }, i -> {
                if (this.maxSeedCount != i) {
                    this.maxSeedCount = i;
                    container.notifyChangeNoSync();
                }
            }), builder)
            // used seed count
            .attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> {
                int i = this.usedSeedCountGetter.get();
                if (this.usedSeedCount != i) {
                    this.usedSeedCount = i;
                    container.notifyChangeNoSync();
                }
                return i;
            }, i -> {
                if (this.usedSeedCount != i) {
                    this.usedSeedCount = i;
                    container.notifyChangeNoSync();
                }
            }), builder)

            .attachSyncer(new FakeSyncWidget.ListSyncer<>(() -> {
                List<GTHelper.StackableItemSlot> newDrawables = new ArrayList<>();
                for (int i = 0, mStorageSize = inventory.size(); i < mStorageSize; i++) {
                    T slot = inventory.get(i);
                    if (slot == null) {
                        continue;
                    }
                    ItemStack stack = inventoryGetter.get(slot);
                    newDrawables
                        .add(new GTHelper.StackableItemSlot(1, stack, new ArrayList<>(Collections.singletonList(i))));
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

    @NotNull
    List<GTHelper.StackableItemSlot> drawables = new ArrayList<>();

    private @NotNull Widget createWidget(EntityPlayer player) {
        Scrollable dynamicInventoryWidget = new Scrollable().setVerticalScroll();

        ArrayList<Widget> buttons = new ArrayList<>();

        if (!ModUtils.isClientThreaded()) {
            drawables = new ArrayList<>();
            for (int i = 0, inventorySize = inventory.size(); i < inventorySize; i++) {
                T slot = inventory.get(i);
                if (slot == null) {
                    continue;
                }
                ItemStack stack = inventoryGetter.get(slot);
                drawables.add(new GTHelper.StackableItemSlot(1, stack, new ArrayList<>(Collections.singleton(i))));
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
                        }
                    } else if (clickData.shift) {
                        if (inventoryExtractor == null) return;
                        if (drawables.size() <= finalID) return;
                        int realID = drawables.get(finalID).realSlots.get(0);
                        T toRemoveFrom = this.inventory.get(realID);
                        ItemStack removed = this.inventoryExtractor.extract(toRemoveFrom, (EntityPlayerMP) player);
                        if (removed != null) {
                            if (player.inventory.addItemStackToInventory(removed))
                                player.inventoryContainer.detectAndSendChanges();
                            else player.entityDropItem(removed, 0.f);
                        }
                    } else {
                        ItemStack input = player.inventory.getItemStack();
                        if (input != null) {
                            if (inventoryInjector == null) return;
                            if (clickData.mouseButton == 1) {
                                ItemStack copy = input.copy();
                                copy.stackSize = 1;
                                inventoryInjector.inject(copy);
                                if (copy.stackSize == 1) return;
                                input.stackSize--;
                            } else {
                                inventoryInjector.inject(input);
                            }
                            if (input.stackSize > 0) {
                                // clearing and updating the held item value like this is the only
                                // way I found to be able to reliably update the item count in the UI.
                                player.inventory.setItemStack(null);
                                ((EntityPlayerMP) player).updateHeldItem();
                                player.inventory.setItemStack(input);
                                ((EntityPlayerMP) player).updateHeldItem();
                                return;
                            } else player.inventory.setItemStack(null);
                            ((EntityPlayerMP) player).isChangingQuantityOnly = false;
                            ((EntityPlayerMP) player).updateHeldItem();
                            return;
                        }
                        if (drawables.size() > finalID) {
                            if (inventoryExtractor == null) return;
                            int realID = drawables.get(finalID).realSlots.get(0);
                            T toRemoveFrom = this.inventory.get(realID);
                            ItemStack removed = this.inventoryExtractor.extract(toRemoveFrom, (EntityPlayerMP) player);
                            if (removed != null) {
                                player.inventory.setItemStack(removed);
                                ((EntityPlayerMP) player).isChangingQuantityOnly = false;
                                ((EntityPlayerMP) player).updateHeldItem();
                            }
                        }
                    }
                })
                .setBackground(() -> {
                    ItemStack stack = drawables.get(finalID).stack;
                    float slotSize = 16.0f;
                    IDrawable itemDrawable = new ItemDrawable(stack).withFixedSize(slotSize, slotSize, 1, 1);
                    IDrawable stackSizeText = new AutoScalingStackSizeText(stack.stackSize).color(Color.WHITE.normal)
                        .shadow()
                        .alignment(Alignment.BottomRight)
                        .measure();

                    return new IDrawable[] { getItemSlot(), itemDrawable, stackSizeText };
                })
                .dynamicTooltip(() -> {
                    if (drawables.size() > finalID) {
                        ItemStack stack = drawables.get(finalID).stack;
                        List<String> tip = new LinkedList<>();
                        for (Object o : stack.getTooltip(player, false)) {
                            tip.add(o.toString());
                        }
                        if (!tip.isEmpty() && tip.get(0) != null) {
                            tip.set(0, stack.stackSize + " x " + tip.get(0));
                        }
                        return tip;
                    }
                    return Collections.emptyList();
                })
                .setSize(18, 18));
        }

        // only add the extra slot if we are still able to insert
        if (this.usedSeedCount < this.maxSeedCount) {
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
                            inventoryInjector.inject(copy);
                            if (copy.stackSize == 1) return;

                            input.stackSize--;
                        } else {
                            inventoryInjector.inject(input);
                        }
                        if (input.stackSize > 0) {
                            // clearing and updating the held item value like this is the only
                            // way i found to be able to reliably update the item count in the UI.
                            player.inventory.setItemStack(null);
                            ((EntityPlayerMP) player).updateHeldItem();
                            player.inventory.setItemStack(input);
                            ((EntityPlayerMP) player).updateHeldItem();
                            return;
                        } else player.inventory.setItemStack(null);
                        ((EntityPlayerMP) player).isChangingQuantityOnly = false;
                        ((EntityPlayerMP) player).updateHeldItem();
                    }
                })
                .setBackground(() -> {
                    IDrawable itemSlot = getItemSlot();

                    IDrawable stackSizeText = new AutoScalingStackSizeText(this.maxSeedCount - this.usedSeedCount)
                        .color(Color.WHITE.normal)
                        .shadow()
                        .alignment(Alignment.BottomRight)
                        .measure();

                    return new IDrawable[] { itemSlot, stackSizeText };
                })
                .dynamicTooltip(() -> {
                    List<String> tip = new ArrayList<>();
                    tip.add(
                        EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocalFormatted(
                            "kubatech.gui.tooltip.dynamic_inventory.eig.remaining_seed_types",
                            (this.maxSeedTypes - this.usedSeedTypes)));
                    tip.add(
                        EnumChatFormatting.DARK_GREEN + StatCollector.translateToLocalFormatted(
                            "kubatech.gui.tooltip.dynamic_inventory.eig.remaining_seed_capacity",
                            (this.maxSeedCount - this.usedSeedCount)));
                    return tip;
                })
                .setSize(18, 18));
        }

        final int perRow = width / 18;
        for (int i = 0, imax = ((buttons.size() - 1) / perRow); i <= imax; i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            for (int j = 0, jmax = (i == imax ? (buttons.size() - 1) % perRow : (perRow - 1)); j <= jmax; j++) {
                final int finalI = i * perRow;
                final int ID = finalI + j;
                row.widget(buttons.get(ID));
            }
            dynamicInventoryWidget.widget(row.setPos(0, i * 18));
        }
        dynamicInventoryWidget.setSize(width, height);
        return dynamicInventoryWidget;
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
         * @return Item that we took out or null
         */
        ItemStack extract(T container, EntityPlayerMP player);
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
