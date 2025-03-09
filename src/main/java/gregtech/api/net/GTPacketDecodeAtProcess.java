package gregtech.api.net;

import java.util.function.BiConsumer;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

public class GTPacketDecodeAtProcess extends GTPacket {

    private final ByteArrayDataInput dataInput;
    private final BiConsumer<ByteArrayDataInput, IBlockAccess> decodingProcessor;

    public GTPacketDecodeAtProcess(ByteArrayDataInput dataInput,
        BiConsumer<ByteArrayDataInput, IBlockAccess> decodingProcessor) {
        this.dataInput = dataInput;
        this.decodingProcessor = decodingProcessor;
    }

    @Override
    public void process(IBlockAccess world) {
        this.decodingProcessor.accept(dataInput, world);
    }

    @Override
    public byte getPacketID() {
        throw new RuntimeException("For processing only!");
    }

    @Override
    public void encode(ByteBuf buffer) {
        throw new RuntimeException("For processing only!");
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buffer) {
        throw new RuntimeException("For processing only!");
    }
}
