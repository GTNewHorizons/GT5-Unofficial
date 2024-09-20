package gregtech.common.items.behaviors;

import static gregtech.api.enums.GTValues.AuthorQuerns;
import static net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.GTNHLib;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.widget.Widget;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.net.GTPacketInfiniteSpraycan;
import gregtech.api.util.ColoredBlockContainer;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Other;
import gregtech.common.gui.modularui.uifactory.SelectItemUIFactory;

public class BehaviourSprayColorInfinite extends BehaviourSprayColor {

    private static final byte REMOVE_COLOR = (byte) Dyes.VALUES.length;

    private static final List<ItemStack> COLOR_SELECTIONS;
    public static final String COLOR_NBT_TAG = "current_color";
    public static final String LOCK_NBT_TAG = "is_locked";
    public static final String SEPARATOR = "-----------------------------------------";

    private byte mCurrentColor;

    static {
        final ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();

        for (int i = 0; i < 16; i++) {
            builder.add(new ItemStack(Items.dye, 1, i));
        }

        builder.add(ItemList.Spray_Color_Remover.get(1));
        COLOR_SELECTIONS = builder.build();
    }

    public BehaviourSprayColorInfinite(ItemStack sprayCan) {
        super(sprayCan, sprayCan, sprayCan, Other.sprayCanChainRange, 0);
        this.mTooltip = "";
        mCurrentColor = 0;
    }

    // region Base Method Overrides
    @Override
    protected long getUses(ItemStack itemStack, NBTTagCompound tNBT) {
        return Other.sprayCanChainRange;
    }

    @Override
    protected void setRemainingUses(ItemStack itemStack, NBTTagCompound tNBT, long tUses) {
        // Infinite spray can; do nothing
    }

    @Override
    protected byte getColor() {
        if (mCurrentColor == REMOVE_COLOR) {
            throw new RuntimeException("Attempting to get invalid color");
        }
        return mCurrentColor;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack itemStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if ((aWorld.isRemote) || (itemStack.stackSize != 1)) {
            return false;
        }

        if (itemStack.hasTagCompound()) {
            final NBTTagCompound tag = itemStack.getTagCompound();
            if (tag.hasKey(COLOR_NBT_TAG)) {
                mCurrentColor = tag.getByte(COLOR_NBT_TAG);
            }
        }

        return super.onItemUseFirst(aItem, itemStack, aPlayer, aWorld, aX, aY, aZ, side, hitX, hitY, hitZ);
    }

    @Override
    protected boolean colorize(World aWorld, int aX, int aY, int aZ, ForgeDirection side, EntityPlayer player) {
        ColoredBlockContainer block = ColoredBlockContainer.getInstance(aWorld, aX, aY, aZ, side, player);
        if (mCurrentColor == REMOVE_COLOR) {
            return block.removeColor();
        }
        return block.setColor(getColor());
    }

    @Override
    public List<String> getAdditionalToolTips(final MetaBaseItem aItem, final List<String> aList,
        final ItemStack itemStack) {
        aList.add(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.tooltip.infinite"));
        aList.add(mTooltipChain);
        aList.add(SEPARATOR);
        aList.add(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.tooltip.more_info"));
        aList.add(SEPARATOR);
        aList.add(AuthorQuerns);

        return aList;
    }

    @Override
    public List<String> getAdditionalToolTipsWhileSneaking(final MetaBaseItem aItem, final List<String> aList,
        final ItemStack aStack) {
        aList.add(SEPARATOR);
        aList.add(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.tooltip.switch"));
        aList.add(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.tooltip.gui"));
        aList.add(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.tooltip.pick"));
        aList.add(StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.tooltip.lock"));
        aList.add(SEPARATOR);
        aList.add(AuthorQuerns);

        return aList;
    }
    // endregion

    // region Raw Mouse Event Handlers
    @Override
    public boolean onLeftClick(MetaBaseItem item, ItemStack itemStack, EntityPlayer aPlayer) {
        if (isLocked(itemStack)) {
            displayLockedMessage();
        } else {
            sendPacket(GTPacketInfiniteSpraycan.Action.INCREMENT_COLOR);
        }
        return true;
    }

    @Override
    public boolean onMiddleClick(final MetaBaseItem item, final ItemStack itemStack, final EntityPlayer player) {
        if (player.isSneaking()) {
            sendPacket(GTPacketInfiniteSpraycan.Action.LOCK_CAN);
        } else if (isLocked(itemStack)) {
            displayLockedMessage();
        } else {
            final MovingObjectPosition position = GTUtility.getPlayerLookingTarget(player);

            if (position != null && position.typeOfHit == BLOCK) {
                final ColoredBlockContainer block = ColoredBlockContainer.getInstance(player, position);
                if (block.getColor()
                    .isPresent()) {
                    sendPacket(
                        GTPacketInfiniteSpraycan.Action.SET_COLOR,
                        block.getColor()
                            .get());
                    return true;
                }
            }

            openGUI(player, itemStack);
        }

        return true;
    }
    // endregion

    // region GUI
    private void openGUI(final EntityPlayer player, final ItemStack itemStack) {
        UIInfos.openClientUI(
            player,
            buildContext -> new DyeSelectGUI(
                StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.gui.header"),
                itemStack,
                selectedStack -> sendPacket(
                    GTPacketInfiniteSpraycan.Action.SET_COLOR,
                    selectedStack.getItem() == Items.dye ? selectedStack.getItemDamage() : REMOVE_COLOR),
                COLOR_SELECTIONS,
                getColor(itemStack),
                true).createWindow(buildContext));
    }

    private byte getColor(ItemStack sprayCan) {
        if (sprayCan.hasTagCompound()) {
            final NBTTagCompound tag = sprayCan.getTagCompound();
            if (tag.hasKey(COLOR_NBT_TAG)) {
                return tag.getByte(COLOR_NBT_TAG);
            }
        }

        return REMOVE_COLOR;
    }

    private static void displayLockedMessage() {
        GTNHLib.proxy.printMessageAboveHotbar(
            StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.gui.lock_error"),
            120,
            true,
            true);
    }
    // endregion

    // region Networking
    private static void sendPacket(GTPacketInfiniteSpraycan.Action action) {
        GTValues.NW.sendToServer(new GTPacketInfiniteSpraycan(action));
    }

    private static void sendPacket(@SuppressWarnings("SameParameterValue") GTPacketInfiniteSpraycan.Action action,
        int newColor) {
        GTValues.NW.sendToServer(new GTPacketInfiniteSpraycan(action, newColor));
    }
    // endregion

    // region Server Actions
    public void incrementColor(final ItemStack itemStack, final boolean wasSneaking) {
        if (isLocked(itemStack)) {
            return;
        }

        final NBTTagCompound tag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        byte color = 0;

        if (tag.hasKey(COLOR_NBT_TAG)) {
            color = tag.getByte(COLOR_NBT_TAG);
        }

        byte newColor = (byte) (color + (wasSneaking ? -1 : 1));
        if (newColor > REMOVE_COLOR) {
            newColor = 0;
        } else if (newColor < 0) {
            newColor = REMOVE_COLOR;
        }
        color = newColor;

        setColor(itemStack, color);
    }

    public void setColor(final ItemStack itemStack, final byte color) {
        if (isLocked(itemStack)) {
            return;
        }

        final NBTTagCompound tag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();

        tag.setByte(COLOR_NBT_TAG, color);
        mCurrentColor = color;
        itemStack.setTagCompound(tag);

        setItemStackName(itemStack);
    }

    public boolean toggleLock(final ItemStack itemStack) {
        final NBTTagCompound tag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        final boolean newLockStatus = !tag.getBoolean(LOCK_NBT_TAG);

        tag.setBoolean(LOCK_NBT_TAG, newLockStatus);
        itemStack.setTagCompound(tag);
        setItemStackName(itemStack);

        return newLockStatus;
    }

    private void setItemStackName(final ItemStack itemStack) {
        final boolean isLocked = isLocked(itemStack);
        final char lBracket = isLocked ? '[' : '(';
        final char rBracket = isLocked ? ']' : ')';

        if (mCurrentColor == REMOVE_COLOR) {
            itemStack.setStackDisplayName(String.format("Infinite Spray Can %cSolvent%c", lBracket, rBracket));
        } else {
            itemStack.setStackDisplayName(
                String.format("Infinite Spray Can %c" + Dyes.get(mCurrentColor).mName + "%c", lBracket, rBracket));
        }
    }
    // endregion

    public static Dyes getDye(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            final byte color = itemStack.getTagCompound()
                .getByte(COLOR_NBT_TAG);
            if (color != REMOVE_COLOR) {
                return Dyes.getDyeFromIndex(color);
            }
        }

        return Dyes.MACHINE_METAL;
    }

    public boolean isLocked(final ItemStack itemStack) {
        return itemStack.hasTagCompound() && itemStack.getTagCompound()
            .getBoolean(LOCK_NBT_TAG);
    }

    private static class DyeSelectGUI extends SelectItemUIFactory {

        public DyeSelectGUI(final String header, final ItemStack headerItem, final Consumer<ItemStack> selectedCallback,
            final List<ItemStack> stacks, final int selected, final boolean noDeselect) {
            super(header, headerItem, selectedCallback, stacks, selected, noDeselect);
        }

        @Override
        public void setSelected(final int selected, Widget widget) {
            super.setSelected(selected, widget);
            widget.getWindow()
                .closeWindow();
        }

        @Override
        protected List<String> getItemTooltips(final int index) {
            return ImmutableList.of(
                index == REMOVE_COLOR ? StatCollector.translateToLocal("gt.behaviour.paintspray.infinite.gui.solvent")
                    : Dyes.getDyeFromIndex((short) index).mName);
        }
    }
}
