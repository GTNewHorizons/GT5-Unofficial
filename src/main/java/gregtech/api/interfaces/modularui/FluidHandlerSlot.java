package gregtech.api.interfaces.modularui;

import static com.google.common.primitives.Ints.saturatedCast;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizons.modularui.ModularUI;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.NumberFormat;
import com.gtnewhorizons.modularui.api.drawable.GuiHelper;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.TextRenderer;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.widget.Interactable;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.Theme;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;

import gregtech.api.fluid.FluidHandler;
import gregtech.api.util.GT_Utility;

public class FluidHandlerSlot extends FluidSlotWidget {

    private FluidHandler handler;
    private int tank;
    private Pos2d contentOffset = new Pos2d(1, 1);
    private final TextRenderer textRenderer = new TextRenderer();
    private @Nullable FluidStack lastStoredFluid;

    private FluidHandlerSlot() {
        super(null);
    }

    public FluidHandlerSlot(FluidHandler handler, int tank) {
        this();
        this.handler = handler;
        this.tank = tank;
    }

    public void onInit() {
        if (this.isClient()) {
            this.textRenderer.setShadow(true);
            this.textRenderer.setScale(0.5F);
        }

        if (this.getBackground() == null) {
            this.setBackground(new IDrawable[] { ModularUITextures.FLUID_SLOT });
        }

    }

    public FluidStack getContent() {
        return handler.getFluidInSlot(tank);
    }

    public void draw(float partialTicks) {
        FluidStack content = getContent();
        if (content != null) {
            float y = (float) this.contentOffset.y;
            float height = (float) (this.size.height - this.contentOffset.y * 2);
            GuiHelper.drawFluidTexture(
                content,
                (float) this.contentOffset.x,
                y,
                (float) (this.size.width - this.contentOffset.x * 2),
                height,
                0.0F);
        }

        if (content != null) {
            String s = NumberFormat.formatLong(handler.getTankAmount(tank)) + "L";
            this.textRenderer
                .setAlignment(Alignment.CenterLeft, (float) (this.size.width - this.contentOffset.x) - 1.0F);
            this.textRenderer
                .setPos((int) ((float) this.contentOffset.x + 0.5F), (int) ((float) this.size.height - 4.5F));
            this.textRenderer.draw(s);
        }

        if (this.isHovering() && !this.getContext()
            .getCursor()
            .hasDraggable()) {
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GlStateManager.colorMask(true, true, true, false);
            ModularGui.drawSolidRect(1.0F, 1.0F, 16.0F, 16.0F, Theme.INSTANCE.getSlotHighlight());
            GlStateManager.colorMask(true, true, true, true);
            GL11.glDisable(3042);
        }
    }

    @Override
    public void buildTooltip(List<Text> tooltip) {
        FluidStack fluid = handler.getFluidInSlot(tank);
        if (fluid != null) {
            this.addFluidNameInfo(tooltip, fluid);
            tooltip.add(
                Text.localised(
                    "modularui.fluid.amount",
                    new Object[] { handler.getTankAmount(tank), handler.getTankCapacity(tank) }));
            this.addAdditionalFluidInfo(tooltip, fluid);
        } else {
            tooltip.add(
                Text.localised("modularui.fluid.empty", new Object[0])
                    .format(EnumChatFormatting.WHITE));
            tooltip.add(Text.localised("modularui.fluid.capacity", handler.getTankCapacity(tank)));
        }
        tooltip.add(Text.EMPTY);
        if (Interactable.hasShiftDown()) {
            tooltip.add(Text.localised("modularui.fluid.click_combined", new Object[0]));
        } else {
            tooltip.add(Text.localised("modularui.tooltip.shift", new Object[0]));
        }
    }

    @Override
    public void detectAndSendChanges(boolean init) {
        FluidStack currentFluid = handler.getFluidInSlot(tank);
        if (init || fluidChanged(currentFluid, this.lastStoredFluid)) {
            this.lastStoredFluid = currentFluid == null ? null : currentFluid.copy();
            this.syncToClient(5, (buffer) -> { NetworkUtils.writeFluidStack(buffer, currentFluid); });
            this.markForUpdate();
        }
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) throws IOException {
        if (id == 5) {
            FluidStack fluidStack = NetworkUtils.readFluidStack(buf);
            handler.extractFluid(tank, Integer.MAX_VALUE, false);
            handler.insertFluid(
                tank,
                fluidStack != null ? fluidStack.getFluid() : null,
                fluidStack != null ? fluidStack.amount : 0,
                false);
            this.notifyTooltipChange();
        }
    }

    @Override
    protected ItemStack transferFluid(Widget.ClickData clickData) {
        EntityPlayer player = this.getContext()
            .getPlayer();
        boolean processFullStack = clickData.mouseButton == 0;
        ItemStack heldItem = player.inventory.getItemStack();
        if (heldItem != null && heldItem.stackSize != 0) {
            ItemStack heldItemSizedOne = heldItem.copy();
            heldItemSizedOne.stackSize = 1;
            FluidStack currentFluid = handler.getFluidInSlot(tank);
            FluidStack heldFluid = this.getFluidForRealItem(heldItemSizedOne);
            if (heldFluid != null && heldFluid.amount <= 0) {
                heldFluid = null;
            }

            if (currentFluid == null) {
                if (!canFillSlot()) {
                    return null;
                } else {
                    return heldFluid == null ? null : this.fillFluid(heldFluid, processFullStack);
                }
            } else if (heldFluid != null && currentFluid.amount < handler.getTankCapacity(tank)) {
                if (canFillSlot()) {
                    return this.fillFluid(heldFluid, processFullStack);
                } else {
                    return !canDrainSlot() ? null : this.drainFluid(processFullStack);
                }
            } else {
                return !canDrainSlot() ? null : this.drainFluid(processFullStack);
            }
        } else {
            return null;
        }
    }

    @Override
    protected ItemStack drainFluid(boolean processFullStack) {
        EntityPlayer player = this.getContext()
            .getPlayer();
        ItemStack heldItem = player.inventory.getItemStack();
        if (heldItem != null && heldItem.stackSize != 0) {
            ItemStack heldItemSizedOne = heldItem.copy();
            heldItemSizedOne.stackSize = 1;
            FluidStack currentFluid = handler.getFluidInSlot(tank);
            if (currentFluid == null) {
                return null;
            } else {
                currentFluid = currentFluid.copy();
                int originalFluidAmount = currentFluid.amount;
                ItemStack filledContainer = this.fillFluidContainer(currentFluid, heldItemSizedOne);
                if (filledContainer != null) {
                    int filledAmount = originalFluidAmount - currentFluid.amount;
                    if (filledAmount < 1) {
                        ModularUI.logger.warn(
                            "Item {} returned filled item {}, but no fluid was actually drained.",
                            new Object[] { heldItemSizedOne.getDisplayName(), filledContainer.getDisplayName() });
                        return null;
                    }

                    handler.extractFluid(tank, filledAmount, false);
                    if (processFullStack) {
                        int additionalParallel = Math.min(heldItem.stackSize - 1, currentFluid.amount / filledAmount);
                        handler.extractFluid(tank, filledAmount * additionalParallel, false);
                        filledContainer.stackSize += additionalParallel;
                    }

                    this.replaceCursorItemStack(filledContainer);
                    this.playSound(currentFluid, false);
                }

                return filledContainer;
            }
        } else {
            return null;
        }
    }

    @Override
    protected ItemStack fillFluid(@NotNull FluidStack heldFluid, boolean processFullStack) {
        EntityPlayer player = this.getContext()
            .getPlayer();
        ItemStack heldItem = player.inventory.getItemStack();
        if (heldItem != null && heldItem.stackSize != 0) {
            ItemStack heldItemSizedOne = heldItem.copy();
            heldItemSizedOne.stackSize = 1;
            FluidStack currentFluid = handler.getFluidInSlot(tank);
            if (currentFluid != null && !currentFluid.isFluidEqual(heldFluid)) {
                return null;
            } else {
                long freeSpace = handler.getTankCapacity(tank) - handler.getTankAmount(tank);
                if (freeSpace <= 0) {
                    return null;
                } else {
                    ItemStack itemStackEmptied = null;
                    int fluidAmountTaken = 0;
                    if (freeSpace >= heldFluid.amount) {
                        itemStackEmptied = this.getContainerForFilledItemWithoutIFluidContainerItem(heldItemSizedOne);
                        fluidAmountTaken = heldFluid.amount;
                    }

                    FluidStack copiedFluidStack;
                    if (itemStackEmptied == null && heldItemSizedOne.getItem() instanceof IFluidContainerItem) {
                        IFluidContainerItem container = (IFluidContainerItem) heldItemSizedOne.getItem();
                        copiedFluidStack = container.drain(heldItemSizedOne, saturatedCast(freeSpace), true);
                        if (copiedFluidStack != null && copiedFluidStack.amount > 0) {
                            itemStackEmptied = heldItemSizedOne;
                            fluidAmountTaken = copiedFluidStack.amount;
                        }
                    }

                    if (itemStackEmptied == null) {
                        return null;
                    } else {
                        long parallel = processFullStack ? Math.min(freeSpace / fluidAmountTaken, heldItem.stackSize)
                            : 1;
                        copiedFluidStack = heldFluid.copy();
                        long totalFluidAmountTaken = fluidAmountTaken * parallel;
                        handler.insertFluid(tank, copiedFluidStack.getFluid(), totalFluidAmountTaken, false);
                        itemStackEmptied.stackSize = saturatedCast(parallel);
                        this.replaceCursorItemStack(itemStackEmptied);
                        this.playSound(heldFluid, true);
                        return itemStackEmptied;
                    }
                }
            }
        } else {
            return null;
        }
    }

    @Override
    public ItemStack getStackUnderMouse() {
        return ModularUI.isGT5ULoaded ? GT_Utility.getFluidDisplayStack(handler.getFluidInSlot(tank), false) : null;
    }
}
