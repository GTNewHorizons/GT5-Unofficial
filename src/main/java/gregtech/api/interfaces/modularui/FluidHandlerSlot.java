package gregtech.api.interfaces.modularui;

import static com.google.common.primitives.Ints.saturatedCast;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.widget.FluidInteractionUtil;
import com.gtnewhorizons.modularui.api.widget.IDragAndDropHandler;
import com.gtnewhorizons.modularui.api.widget.IHasStackUnderMouse;
import com.gtnewhorizons.modularui.api.widget.Interactable;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.Theme;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularGui;
import com.gtnewhorizons.modularui.common.widget.SyncedWidget;

import gregtech.api.fluid.FluidHandler;
import gregtech.api.fluid.FluidStackHolder;
import gregtech.api.util.GT_Utility;

public class FluidHandlerSlot extends SyncedWidget
    implements Interactable, IDragAndDropHandler, IHasStackUnderMouse, FluidInteractionUtil {

    private FluidHandler handler;
    private int tank;
    private Pos2d contentOffset = new Pos2d(1, 1);
    private final TextRenderer textRenderer = new TextRenderer();
    private @Nullable FluidStackHolder lastStoredFluid;
    private FluidStack lastStoredPhantomFluid;
    private boolean canDrainSlot = true;
    private boolean canFillSlot = true;
    private boolean phantom = false;
    private Consumer<FluidHandlerSlot> onClickContainer;
    private Consumer<Widget> onDragAndDropComplete;
    private boolean playClickSound = false;

    private FluidHandlerSlot() {
        super();
        this.textRenderer.setColor(Color.WHITE.normal);
        this.textRenderer.setShadow(true);
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
        return handler.getFluidInTank(tank);
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
        FluidStack fluid = handler.getFluidInTank(tank);
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
        FluidStackHolder currentFluid = handler.getFluidHolderInTank(tank);
        if (init || fluidChanged(currentFluid, this.lastStoredFluid)) {
            this.lastStoredFluid = currentFluid == null ? null : currentFluid.copy();
            this.syncToClient(5, (buffer) -> { FluidStackHolder.writeToBuffer(buffer, currentFluid); });
            this.markForUpdate();
        }
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) throws IOException {
        if (id == 5) {
            FluidStackHolder fluid = FluidStackHolder.readFromBuffer(buf);
            handler.setFluidInTank(tank, fluid);
            this.notifyTooltipChange();
        }
    }

    protected ItemStack transferFluid(Widget.ClickData clickData) {
        EntityPlayer player = this.getContext()
            .getPlayer();
        boolean processFullStack = clickData.mouseButton == 0;
        ItemStack heldItem = player.inventory.getItemStack();
        if (heldItem != null && heldItem.stackSize != 0) {
            ItemStack heldItemSizedOne = heldItem.copy();
            heldItemSizedOne.stackSize = 1;
            FluidStack currentFluid = handler.getFluidInTank(tank);
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

    private boolean canDrainSlot() {
        return canDrainSlot;
    }

    private boolean canFillSlot() {
        return canFillSlot;
    }

    protected ItemStack drainFluid(boolean processFullStack) {
        EntityPlayer player = this.getContext()
            .getPlayer();
        ItemStack heldItem = player.inventory.getItemStack();
        if (heldItem != null && heldItem.stackSize != 0) {
            ItemStack heldItemSizedOne = heldItem.copy();
            heldItemSizedOne.stackSize = 1;
            FluidStack currentFluid = handler.getFluidInTank(tank);
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

    protected ItemStack fillFluid(@NotNull FluidStack heldFluid, boolean processFullStack) {
        EntityPlayer player = this.getContext()
            .getPlayer();
        ItemStack heldItem = player.inventory.getItemStack();
        if (heldItem != null && heldItem.stackSize != 0) {
            ItemStack heldItemSizedOne = heldItem.copy();
            heldItemSizedOne.stackSize = 1;
            FluidStack currentFluid = handler.getFluidInTank(tank);
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
        return ModularUI.isGT5ULoaded ? GT_Utility.getFluidDisplayStack(handler.getFluidInTank(tank), false) : null;
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        if (id == 1) {
            this.onClickServer(ClickData.readPacket(buf), buf.readItemStackFromBuffer());
        } else if (id == 2) {
            if (this.phantom) {
                this.tryScrollPhantom(buf.readVarIntFromBuffer());
            }
        } else if (id == 4) {
            this.tryClickPhantom(ClickData.readPacket(buf), buf.readItemStackFromBuffer());
            if (this.onDragAndDropComplete != null) {
                this.onDragAndDropComplete.accept(this);
            }
        }

        this.markForUpdate();
    }

    protected void onClickServer(Widget.ClickData clickData, ItemStack clientVerifyToken) {
        ItemStack serverVerifyToken = this.tryClickContainer(clickData);
        if (this.onClickContainer != null) {
            this.onClickContainer.accept(this);
        }

        if (!ItemStack.areItemStacksEqual(clientVerifyToken, serverVerifyToken)) {
            ((EntityPlayerMP) this.getContext()
                .getPlayer()).sendContainerToPlayer(
                    this.getContext()
                        .getContainer());
        }

    }

    protected ItemStack tryClickContainer(Widget.ClickData clickData) {
        if (this.phantom) {
            this.tryClickPhantom(clickData);
            return null;
        } else {
            return this.transferFluid(clickData);
        }
    }

    protected void tryClickPhantom(Widget.ClickData clickData) {
        this.tryClickPhantom(
            clickData,
            this.getContext()
                .getCursor()
                .getItemStack());
    }

    protected void tryClickPhantom(Widget.ClickData clickData, ItemStack cursorStack) {
        FluidStack currentFluid = handler.getFluidInTank(tank);
        if (clickData.mouseButton == 0) {
            if (cursorStack == null) {
                if (this.canDrainSlot) {
                    handler.extractFluid(tank, clickData.shift ? Integer.MAX_VALUE : 1000, false);
                }
            } else {
                ItemStack heldItemSizedOne = cursorStack.copy();
                heldItemSizedOne.stackSize = 1;
                FluidStack heldFluid = this.getFluidForPhantomItem(heldItemSizedOne);
                if (currentFluid == null && heldFluid != null) {
                    if (this.canFillSlot) {

                        FluidStack fluidTemp = handler.insertFluid(tank, heldFluid.getFluid(), heldFluid.amount, false);
                        if (fluidTemp != null && fluidTemp.amount > 0) {
                            this.lastStoredPhantomFluid = heldFluid.copy();
                        }
                    }
                } else if (this.canDrainSlot) {
                    handler.extractFluid(tank, clickData.shift ? Integer.MAX_VALUE : 1000, false);
                }
            }
        } else if (clickData.mouseButton == 1) {
            if (this.canFillSlot) {
                FluidStack toFill;
                if (currentFluid != null) {
                    toFill = currentFluid.copy();
                    toFill.amount = 1000;
                    handler.insertFluid(tank, toFill.getFluid(), toFill.amount, false);
                } else if (this.lastStoredPhantomFluid != null) {
                    toFill = this.lastStoredPhantomFluid.copy();
                    toFill.amount = 1000;
                    handler.insertFluid(tank, toFill.getFluid(), toFill.amount, false);
                }
            }
        } else if (clickData.mouseButton == 2 && currentFluid != null && this.canDrainSlot) {
            handler.extractFluid(tank, clickData.shift ? Integer.MAX_VALUE : 1000, false);
        }

    }

    @Override
    public boolean handleDragAndDrop(ItemStack draggedStack, int button) {
        if (!this.isPhantom()) {
            return false;
        } else {
            Widget.ClickData clickData = ClickData.create(button, false);
            this.tryClickPhantom(clickData, draggedStack);
            this.syncToServer(4, (buffer) -> {
                try {
                    clickData.writeToPacket(buffer);
                    buffer.writeItemStackToBuffer(draggedStack);
                } catch (IOException var4) {
                    var4.printStackTrace();
                }

            });
            draggedStack.stackSize = 0;
            return true;
        }
    }

    private boolean isPhantom() {
        return phantom;
    }

    protected void replaceCursorItemStack(ItemStack resultStack) {
        EntityPlayer player = this.getContext()
            .getPlayer();
        int resultStackMaxStackSize = resultStack.getMaxStackSize();

        while (resultStack.stackSize > resultStackMaxStackSize) {
            ItemStack endItem = player.inventory.getItemStack();
            endItem.stackSize -= resultStackMaxStackSize;
            addItemToPlayerInventory(player, resultStack.splitStack(resultStackMaxStackSize));
        }

        if (player.inventory.getItemStack().stackSize == resultStack.stackSize) {
            player.inventory.setItemStack(resultStack);
        } else {
            ItemStack tStackHeld = player.inventory.getItemStack();
            tStackHeld.stackSize -= resultStack.stackSize;
            addItemToPlayerInventory(player, resultStack);
        }

    }

    protected static void addItemToPlayerInventory(EntityPlayer player, ItemStack stack) {
        if (stack != null) {
            if (!player.inventory.addItemStackToInventory(stack) && !player.worldObj.isRemote) {
                EntityItem dropItem = player.entityDropItem(stack, 0.0F);
                dropItem.delayBeforeCanPickup = 0;
            }

        }
    }

    protected void playSound(FluidStack fluid, boolean fill) {}

    public static boolean fluidChanged(@Nullable FluidStackHolder current, @Nullable FluidStackHolder cached) {
        return current == null ^ cached == null || current != null
            && (current.getStoredAmount() != cached.getStoredAmount() || !current.isFluidEqual(cached));
    }

    protected void tryScrollPhantom(int direction) {
        FluidStack currentFluid = handler.getFluidInTank(tank);
        FluidStack toFill;
        if (currentFluid == null) {
            if (direction > 0 && this.lastStoredPhantomFluid != null) {
                toFill = this.lastStoredPhantomFluid.copy();
                toFill.amount = direction;
                handler.insertFluid(tank, toFill.getFluid(), toFill.amount, false);
            }

        } else {
            if (direction > 0) {
                toFill = currentFluid.copy();
                toFill.amount = direction;
                handler.insertFluid(tank, toFill.getFluid(), toFill.amount, false);
            } else if (direction < 0) {
                handler.extractFluid(tank, -direction, false);
            }

        }
    }

    public Interactable.ClickResult onClick(int buttonId, boolean doubleClick) {
        if (!this.canFillSlot && !this.canDrainSlot) {
            return ClickResult.ACKNOWLEDGED;
        } else {
            ItemStack cursorStack = this.getContext()
                .getCursor()
                .getItemStack();
            if (!this.phantom && cursorStack == null) {
                return ClickResult.ACKNOWLEDGED;
            } else {
                Widget.ClickData clickData = ClickData.create(buttonId, doubleClick);
                ItemStack verifyToken = this.tryClickContainer(clickData);
                if (this.onClickContainer != null) {
                    this.onClickContainer.accept(this);
                }

                this.syncToServer(1, (buffer) -> {
                    clickData.writeToPacket(buffer);

                    try {
                        buffer.writeItemStackToBuffer(verifyToken);
                    } catch (IOException var4) {
                        var4.printStackTrace();
                    }

                });
                if (this.playClickSound) {
                    Interactable.playButtonClickSound();
                }

                return ClickResult.ACCEPT;
            }
        }
    }
}
