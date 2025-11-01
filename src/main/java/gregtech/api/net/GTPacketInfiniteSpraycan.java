package gregtech.api.net;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourSprayColorInfinite;
import gregtech.crossmod.backhand.Backhand;
import io.netty.buffer.ByteBuf;

public class GTPacketInfiniteSpraycan extends GTPacket {

    private Action action;
    private int newColor;
    private EntityPlayerMP player;

    public GTPacketInfiniteSpraycan() {
        super();
    }

    public GTPacketInfiniteSpraycan(Action action) {
        super();
        this.action = action;
        this.newColor = -1;
    }

    public GTPacketInfiniteSpraycan(Action action, int newColor) {
        super();
        this.action = action;
        this.newColor = newColor;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.INFINITE_SPRAYCAN.id;
    }

    @Override
    public void encode(final ByteBuf aOut) {
        final byte[] name = action.name()
            .getBytes(StandardCharsets.UTF_8);
        aOut.writeInt(newColor);
        aOut.writeInt(name.length);
        aOut.writeBytes(name);
    }

    @Override
    public GTPacket decode(final ByteArrayDataInput aData) {
        final int newColor = aData.readInt();
        final int length = aData.readInt();
        final byte[] name = new byte[length];
        aData.readFully(name, 0, length);

        return new GTPacketInfiniteSpraycan(Action.valueOf(new String(name, StandardCharsets.UTF_8)), newColor);
    }

    @Override
    public void setINetHandler(final INetHandler aHandler) {
        player = ((NetHandlerPlayServer) aHandler).playerEntity;
    }

    @Override
    public void process(final IBlockAccess aWorld) {
        getSpraycanItemStack().ifPresent(itemStack -> {
            if (itemStack.getItem() instanceof final MetaBaseItem item) {
                item.forEachBehavior(
                    itemStack,
                    behavior -> behavior instanceof BehaviourSprayColorInfinite spraycanBehavior
                        && action.execute(spraycanBehavior, itemStack, player, newColor));
            }
        });
    }

    @NotNull
    private Optional<ItemStack> getSpraycanItemStack() {
        final ItemStack mainhandItemStack;
        final ItemStack offhandItemStack;

        mainhandItemStack = itemStackIsSpraycan(player.inventory.getCurrentItem()) ? player.inventory.getCurrentItem()
            : null;
        offhandItemStack = itemStackIsSpraycan(Backhand.getOffhandItem(player)) ? Backhand.getOffhandItem(player)
            : null;

        if (mainhandItemStack == null && offhandItemStack == null) {
            return Optional.empty();
        }

        // Prefer offhand for setting color
        if (action == Action.SET_COLOR) {
            // noinspection ReplaceNullCheck
            if (offhandItemStack != null) {
                return Optional.of(offhandItemStack);
            }
            return Optional.of(mainhandItemStack);
        }

        // noinspection ReplaceNullCheck
        if (mainhandItemStack != null) {
            return Optional.of(mainhandItemStack);
        }
        return Optional.of(offhandItemStack);
    }

    private static boolean itemStackIsSpraycan(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }

        if (itemStack.getItem() instanceof final MetaBaseItem item) {
            return item.forEachBehavior(itemStack, BehaviourSprayColorInfinite.class::isInstance);
        }

        return false;
    }

    public enum Action {

        INCREMENT_COLOR {

            @Override
            boolean execute(final BehaviourSprayColorInfinite behaviour, final ItemStack itemStack,
                final EntityPlayerMP player, final int newColor) {
                if (!BehaviourSprayColorInfinite.isLocked(itemStack)) {
                    behaviour.incrementColor(itemStack, player.isSneaking());
                    playSound(player, SoundResource.GT_SPRAYCAN_SHAKE);

                    return true;
                }
                return false;
            }
        },
        LOCK_CAN {

            @Override
            boolean execute(final BehaviourSprayColorInfinite behavior, final ItemStack itemStack,
                final EntityPlayerMP player, final int newColor) {
                if (behavior.toggleLock(itemStack)) {
                    playSound(player, SoundResource.GT_SPRAYCAN_LOCK);
                } else {
                    playSound(player, SoundResource.GT_SPRAYCAN_UNLOCK);
                }
                return true;
            }
        },
        SET_COLOR {

            @Override
            boolean execute(final BehaviourSprayColorInfinite behavior, final ItemStack itemStack,
                final EntityPlayerMP player, final int newColor) {
                if (newColor != -1) {
                    behavior.setColor(itemStack, (byte) newColor);
                    playSound(player, SoundResource.GT_SPRAYCAN_SHAKE);
                    return true;
                }
                return false;
            }
        },
        TOGGLE_SHAKE_LOCK {

            @Override
            boolean execute(final BehaviourSprayColorInfinite behavior, final ItemStack itemStack,
                final EntityPlayerMP player, final int newColor) {
                if (behavior.togglePreventShake(itemStack)) {
                    playSound(player, SoundResource.GT_SPRAYCAN_LOCK);
                } else {
                    playSound(player, SoundResource.GT_SPRAYCAN_UNLOCK);
                }
                return true;
            }
        };

        private static void playSound(final EntityPlayerMP player, SoundResource sound) {
            GTUtility.sendSoundToPlayers(player.worldObj, sound, 1.0F, 1, player.posX, player.posY, player.posZ);
        }

        abstract boolean execute(final BehaviourSprayColorInfinite behavior, ItemStack itemStack, EntityPlayerMP player,
            final int newColor);
    }
}
