package detrav.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.FluidStack;

import detrav.DetravScannerMod;
import detrav.items.behaviours.BehaviourDetravToolElectricProspector;
import detrav.items.behaviours.BehaviourDetravToolProspector;

/**
 * Sends one scanner chat line about an ore or a fluid, so the client builds its name in the client language.
 */
public class ScannerChatPacket extends DetravPacket {

    private static final byte ORE = 0;
    private static final byte FLUID = 1;

    private final byte type;
    private final NBTTagCompound stack;
    private final int value;
    private final int dist;

    private ScannerChatPacket(byte type, NBTTagCompound stack, int value, int dist) {
        this.type = type;
        this.stack = stack;
        this.value = value;
        this.dist = dist;
    }

    /** Ore line: name, distance and sample size, see {@link BehaviourDetravToolProspector#foundMessage}. */
    public static ScannerChatPacket ore(ItemStack ore, int value, int dist) {
        return new ScannerChatPacket(ORE, ore.writeToNBT(new NBTTagCompound()), value, dist);
    }

    /** Fluid line with an amount, see {@link BehaviourDetravToolElectricProspector#amountMessage}. */
    public static ScannerChatPacket fluid(FluidStack fluid, int value) {
        return new ScannerChatPacket(FLUID, fluid.writeToNBT(new NBTTagCompound()), value, 0);
    }

    @Override
    public int getPacketID() {
        return 1;
    }

    @Override
    public void encode(OutputStream out) throws IOException {
        DataOutputStream tOut = new DataOutputStream(out);
        tOut.writeByte(type);
        tOut.writeInt(value);
        tOut.writeInt(dist);
        CompressedStreamTools.write(stack, tOut);
        tOut.flush();
    }

    public static ScannerChatPacket decode(InputStream in) throws IOException {
        DataInputStream aData = new DataInputStream(in);
        byte type = aData.readByte();
        int value = aData.readInt();
        int dist = aData.readInt();
        NBTTagCompound stack = CompressedStreamTools.read(aData);
        return new ScannerChatPacket(type, stack, value, dist);
    }

    @Override
    public void process() {
        IChatComponent message;
        if (type == ORE) {
            ItemStack ore = ItemStack.loadItemStackFromNBT(stack);
            if (ore == null) return;
            message = BehaviourDetravToolProspector
                .foundMessage(value, new ChatComponentText(ore.getDisplayName()), dist);
        } else {
            FluidStack fluid = FluidStack.loadFluidStackFromNBT(stack);
            if (fluid == null) return;
            message = BehaviourDetravToolElectricProspector
                .amountMessage(value, new ChatComponentText(fluid.getLocalizedName()));
        }
        DetravScannerMod.proxy.addClientChatMessage(message);
    }
}
