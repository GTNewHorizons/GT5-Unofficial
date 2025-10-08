package gregtech.api.net;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTLog;
import gregtech.client.LMACraftingFX;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class GTPacketLMACraftingFX extends GTPacket {

    private int x;
    private int y;
    private int z;
    private int age;
    private IAEItemStack itemStack;

    public GTPacketLMACraftingFX() {
        super();
    }

    public GTPacketLMACraftingFX(int x, int y, int z, int age, IAEItemStack itemStack) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.age = age;
        this.itemStack = itemStack;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.LMA_CRAFTING_FX.id;
    }

    @Override
    public void encode(ByteBuf buffer) {
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(age);
        try {
            ByteBuf stackBuffer = Unpooled.buffer();
            itemStack.writeToPacket(stackBuffer);
            byte[] data = stackBuffer.array();
            buffer.writeInt(data.length);
            buffer.writeBytes(data);
        } catch (IOException e) {
            GTLog.out.println("Could not serialize LMA ItemStack");
            e.printStackTrace();
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        int x = buffer.readInt();
        int y = buffer.readInt();
        int z = buffer.readInt();
        int age = buffer.readInt();

        byte[] data = new byte[buffer.readInt()];
        buffer.readFully(data);
        ByteBuf stackBuffer = Unpooled.wrappedBuffer(data);

        IAEItemStack itemStack;
        try {
            itemStack = AEItemStack.loadItemStackFromPacket(stackBuffer);
        } catch (IOException e) {
            GTLog.out.println("Could not deserialize LMA ItemStack");
            throw new IllegalStateException(e);
        }

        return new GTPacketLMACraftingFX(x, y, z, age, itemStack);
    }

    @Override
    public void process(IBlockAccess world) {
        if (world != null) {
            spawnFX();
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnFX() {
        LMACraftingFX fx = new LMACraftingFX(
            Minecraft.getMinecraft().theWorld,
            this.x,
            this.y,
            this.z,
            this.age,
            this.itemStack);
        Minecraft.getMinecraft().effectRenderer.addEffect(fx);
    }
}
