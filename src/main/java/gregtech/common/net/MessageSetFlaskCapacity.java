package gregtech.common.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.net.GT_Packet_New;
import gregtech.common.items.GT_VolumetricFlask;
import io.netty.buffer.ByteBuf;

@Deprecated
public final class MessageSetFlaskCapacity extends GT_Packet_New {

    private int capacity, dimID, playerID;

    public MessageSetFlaskCapacity() {
        super(true);
    }

    public MessageSetFlaskCapacity(int capacity, int dimID, int playerID) {
        super(false);
        this.capacity = capacity;
        this.dimID = dimID;
        this.playerID = playerID;
    }

    public MessageSetFlaskCapacity(int capacity, EntityPlayer p) {
        super(false);
        this.capacity = capacity;
        this.dimID = p.worldObj.provider.dimensionId;
        this.playerID = p.getEntityId();
    }

    @Override
    public byte getPacketID() {
        return 5;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(capacity);
        aOut.writeInt(dimID);
        aOut.writeInt(playerID);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new MessageSetFlaskCapacity(aData.readInt(), aData.readInt(), aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        World w = DimensionManager.getWorld(dimID);
        if (w != null && w.getEntityByID(playerID) instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer) w.getEntityByID(playerID)).getHeldItem();
            if ((stack != null) && (stack.stackSize > 0)) {
                Item item = stack.getItem();
                if ((item instanceof GT_VolumetricFlask)) ((GT_VolumetricFlask) item).setCapacity(stack, capacity);
            }
        }
    }
}
