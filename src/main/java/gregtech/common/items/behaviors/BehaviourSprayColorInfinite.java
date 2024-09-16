package gregtech.common.items.behaviors;

import static gregtech.api.enums.GTValues.AuthorQuerns;
import static net.minecraft.util.MovingObjectPosition.MovingObjectType.BLOCK;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.net.GTPacketInfiniteSpraycan;
import gregtech.api.util.ColoredBlockContainer;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Other;

public class BehaviourSprayColorInfinite extends BehaviourSprayColor {

    private static final byte REMOVE_COLOR = (byte) Dyes.VALUES.length;
    public static final String COLOR_NBT_TAG = "current_color";
    public static final String LOCK_NBT_TAG = "is_locked";

    private final String tooltipInfinite = GTLanguageManager
        .addStringLocalization("gt.behaviour.paintspray.infinite.tooltip", "Infinite uses");
    private final String tooltipSwitchHint = GTLanguageManager.addStringLocalization(
        "gt.behaviour.paintspray.infinite.hint.tooltip",
        "Left click to change color (sneak to reverse direction)");

    private byte mCurrentColor;

    public BehaviourSprayColorInfinite(ItemStack sprayCan) {
        super(sprayCan, sprayCan, sprayCan, Other.sprayCanChainRange, 0);
        this.mTooltip = "";
        mCurrentColor = 0;
    }

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

    @Override
    protected long getUses(ItemStack aStack, NBTTagCompound tNBT) {
        return Other.sprayCanChainRange;
    }

    @Override
    protected void setRemainingUses(ItemStack aStack, NBTTagCompound tNBT, long tUses) {
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
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if ((aWorld.isRemote) || (aStack.stackSize != 1)) {
            return false;
        }

        if (aStack.hasTagCompound()) {
            final NBTTagCompound tag = aStack.getTagCompound();
            if (tag.hasKey(COLOR_NBT_TAG)) {
                mCurrentColor = tag.getByte(COLOR_NBT_TAG);
            }
        }

        return super.onItemUseFirst(aItem, aStack, aPlayer, aWorld, aX, aY, aZ, side, hitX, hitY, hitZ);
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
    public boolean onLeftClick(MetaBaseItem item, ItemStack aStack, EntityPlayer aPlayer) {
        sendPacket(GTPacketInfiniteSpraycan.Action.INCREMENT_COLOR);

        return true;
    }

    @Override
    public boolean onMiddleClick(final MetaBaseItem item, final ItemStack itemStack, final EntityPlayer player) {
        if (player.isSneaking()) {
            sendPacket(GTPacketInfiniteSpraycan.Action.LOCK_CAN);
            return true;
        } else if (!isLocked(itemStack)) {
            MovingObjectPosition position = GTUtility.getPlayerLookingTarget(player);
            if (position != null && position.typeOfHit == BLOCK) {
                ColoredBlockContainer block = ColoredBlockContainer.getInstance(player, position);
                if (block.getColor()
                    .isPresent()) {
                    sendPacket(
                        GTPacketInfiniteSpraycan.Action.SET_COLOR,
                        block.getColor()
                            .get());
                    return true;
                }
            }

            // TODO: GUI fallback
        }

        return false;
    }

    private static void sendPacket(GTPacketInfiniteSpraycan.Action action) {
        GTValues.NW.sendToServer(new GTPacketInfiniteSpraycan(action));
    }

    private static void sendPacket(@SuppressWarnings("SameParameterValue") GTPacketInfiniteSpraycan.Action action,
        int newColor) {
        GTValues.NW.sendToServer(new GTPacketInfiniteSpraycan(action, newColor));
    }

    public void incrementColor(final ItemStack itemStack, final boolean wasSneaking) {
        if (isLocked(itemStack)) {
            return;
        }

        final NBTTagCompound tag = itemStack.hasTagCompound() ? itemStack.getTagCompound() : new NBTTagCompound();
        byte color = 0;

        if (tag.hasKey(COLOR_NBT_TAG)) {
            color = tag.getByte(COLOR_NBT_TAG);
        }

        color = clampColor((byte) (color + (wasSneaking ? -1 : 1)));

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

        if (mCurrentColor == REMOVE_COLOR) {
            itemStack.setStackDisplayName("Infinite Spray Can (Solvent)");
        } else {
            itemStack.setStackDisplayName("Infinite Spray Can (" + Dyes.get(mCurrentColor).mName + ")");
        }
    }

    public void toggleLock(final ItemStack aStack) {
        final NBTTagCompound tag = aStack.hasTagCompound() ? aStack.getTagCompound() : new NBTTagCompound();
        tag.setBoolean(LOCK_NBT_TAG, !tag.getBoolean(LOCK_NBT_TAG));
        aStack.setTagCompound(tag);
    }

    public boolean isLocked(final ItemStack aStack) {
        return aStack.hasTagCompound() && aStack.getTagCompound()
            .getBoolean(LOCK_NBT_TAG);
    }

    @Override
    public List<String> getAdditionalToolTips(final MetaBaseItem aItem, final List<String> aList,
        final ItemStack aStack) {
        aList.add(tooltipInfinite);
        aList.add(tooltipSwitchHint);
        aList.add(mTooltipChain);
        aList.add(AuthorQuerns);

        return aList;
    }

    private static byte clampColor(byte newColor) {
        if (newColor > REMOVE_COLOR) {
            newColor = 0;
        } else if (newColor < 0) {
            newColor = REMOVE_COLOR;
        }
        return newColor;
    }
}
