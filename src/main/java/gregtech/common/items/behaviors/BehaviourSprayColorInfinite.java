package gregtech.common.items.behaviors;

import static gregtech.api.enums.GTValues.AuthorQuerns;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.net.GTPacketInfiniteSpraycan;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.config.other.ConfigGeneral;

public class BehaviourSprayColorInfinite extends BehaviourSprayColor {

    private static final byte REMOVE_COLOR = (byte) Dyes.VALUES.length;
    private static final String COLOR_NBT_TAG = "current_color";

    private final String tooltipInfinite = GTLanguageManager
        .addStringLocalization("gt.behaviour.paintspray.infinite.tooltip", "Infinite uses");
    private final String tooltipSwitchHint = GTLanguageManager.addStringLocalization(
        "gt.behaviour.paintspray.infinite.hint.tooltip",
        "Left click to change color (sneak to reverse direction)");

    private byte mCurrentColor;

    public BehaviourSprayColorInfinite(ItemStack sprayCan) {
        super(sprayCan, sprayCan, sprayCan, ConfigGeneral.sprayCanChainRange, 0);
        this.mTooltip = "";
        mCurrentColor = 0;
    }

    @Override
    protected long getUses(ItemStack aStack, NBTTagCompound tNBT) {
        return ConfigGeneral.sprayCanChainRange;
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
        if (mCurrentColor == REMOVE_COLOR) {
            return BehaviourSprayColorRemover.removeColor(aWorld, aX, aY, aZ, side, player);
        }
        return super.colorize(aWorld, aX, aY, aZ, side, player);
    }

    public boolean onLeftClick(MetaBaseItem item, ItemStack aStack, EntityPlayer aPlayer) {
        GTValues.NW.sendToServer(new GTPacketInfiniteSpraycan(aPlayer.isSneaking()));

        return true;
    }

    private static byte clampColor(byte newColor) {
        if (newColor > REMOVE_COLOR) {
            newColor = 0;
        } else if (newColor < 0) {
            newColor = REMOVE_COLOR;
        }
        return newColor;
    }

    public void setNewColor(final ItemStack aStack, final boolean wasSneaking) {
        final NBTTagCompound tag = aStack.hasTagCompound() ? aStack.getTagCompound() : new NBTTagCompound();
        byte color = 0;

        if (tag.hasKey(COLOR_NBT_TAG)) {
            color = tag.getByte(COLOR_NBT_TAG);
        }

        color = clampColor((byte) (color + (wasSneaking ? -1 : 1)));

        tag.setByte(COLOR_NBT_TAG, color);
        mCurrentColor = color;
        aStack.setTagCompound(tag);

        if (mCurrentColor == REMOVE_COLOR) {
            aStack.setStackDisplayName("Infinite Spray Can (Solvent)");
        } else {
            aStack.setStackDisplayName("Infinite Spray Can (" + Dyes.get(mCurrentColor).mName + ")");
        }
    }

    @Override
    public List<String> getAdditionalToolTips(final MetaBaseItem aItem, final List<String> aList,
        final ItemStack aStack) {
        if (mCurrentColor == REMOVE_COLOR) {
            aList.add(
                GTLanguageManager.addStringLocalization(
                    "gt.behavior.paintspray.infinite.remover.tooltip",
                    "Current color: Solvent (clears color)"));
        } else {
            final Dyes currentDye = Dyes.get(mCurrentColor);
            final String diamondSymbol = " " + currentDye.formatting + "♦" + EnumChatFormatting.RESET + " ";
            aList.add(
                GTLanguageManager.addStringLocalization(
                    "gt.behaviour.paintspray.infinite." + mCurrentColor + ".tooltip",
                    "Current color:" + diamondSymbol + currentDye.mName + diamondSymbol));
        }
        aList.add(tooltipInfinite);
        aList.add(tooltipSwitchHint);
        aList.add(mTooltipChain);
        aList.add(AuthorQuerns);

        return aList;
    }
}
