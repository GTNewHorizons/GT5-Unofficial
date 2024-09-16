package gregtech.api.net;

import java.nio.charset.StandardCharsets;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;
import gregtech.common.items.behaviors.BehaviourSprayColorInfinite;
import io.netty.buffer.ByteBuf;

public class GTPacketInfiniteSpraycan extends GTPacketNew {

    private Action action;
    private int newColor;
    private EntityPlayerMP player;

    public GTPacketInfiniteSpraycan() {
        super(true);
    }

    public GTPacketInfiniteSpraycan(Action action) {
        super(false);
        this.action = action;
        this.newColor = -1;
    }

    public GTPacketInfiniteSpraycan(Action action, int newColor) {
        super(false);
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
    public GTPacketNew decode(final ByteArrayDataInput aData) {
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
        ItemStack currentItemStack = player.inventory.getCurrentItem();
        if (currentItemStack != null && currentItemStack.getItem() instanceof MetaBaseItem item) {
            item.forEachBehavior(currentItemStack, behavior -> {
                if (behavior instanceof BehaviourSprayColorInfinite spraycanBehavior
                    && action.execute(spraycanBehavior, currentItemStack, player, newColor)) {
                    player.sendSlotContents(player.inventoryContainer, player.inventory.currentItem, currentItemStack);
                    return true;
                }

                return false;
            });
        }
    }

    public enum Action {

        INCREMENT_COLOR {

            @Override
            boolean execute(final BehaviourSprayColorInfinite behaviour, final ItemStack itemStack,
                final EntityPlayerMP player, final int newColor) {
                if (!behaviour.isLocked(itemStack)) {
                    behaviour.incrementColor(itemStack, player.isSneaking());
                    GTUtility.sendSoundToPlayers(
                        player.worldObj,
                        SoundResource.GT_SPRAYCAN_SHAKE,
                        1.0F,
                        1,
                        (int) player.posX,
                        (int) player.posY,
                        (int) player.posZ);

                    return true;
                }
                // TODO: Sad, ineffectual shake noise for when it's locked
                return false;
            }
        },
        LOCK_CAN {

            @Override
            boolean execute(final BehaviourSprayColorInfinite behavior, final ItemStack itemStack,
                final EntityPlayerMP player, final int newColor) {
                behavior.toggleLock(itemStack);
                return true;
            }
        },
        SET_COLOR {

            @Override
            boolean execute(final BehaviourSprayColorInfinite behavior, final ItemStack itemStack,
                final EntityPlayerMP player, final int newColor) {
                if (newColor != -1) {
                    behavior.setColor(itemStack, (byte) newColor);
                    return true;
                }
                return false;
            }
        };

        abstract boolean execute(final BehaviourSprayColorInfinite behavior, ItemStack itemStack, EntityPlayerMP player,
            final int newColor);
    }
}
