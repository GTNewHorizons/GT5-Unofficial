package gregtech.api.net;

import java.io.IOException;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;

public class GTPacketSound extends GTPacket {

    private double mX, mY, mZ;
    private String mSoundName;
    private float mSoundStrength, mSoundPitch;

    public GTPacketSound() {
        super();
    }

    public GTPacketSound(String aSoundName, float aSoundStrength, float aSoundPitch, double aX, double aY, double aZ) {
        super();
        mX = aX;
        mY = aY;
        mZ = aZ;
        mSoundName = aSoundName;
        mSoundStrength = aSoundStrength;
        mSoundPitch = aSoundPitch;
    }

    @Override
    public void encode(ByteBuf aOut) {
        try (ByteBufOutputStream byteOutputStream = new ByteBufOutputStream(aOut)) {
            byteOutputStream.writeUTF(mSoundName);
            byteOutputStream.writeFloat(mSoundStrength);
            byteOutputStream.writeFloat(mSoundPitch);
            byteOutputStream.writeDouble(mX);
            byteOutputStream.writeDouble(mY);
            byteOutputStream.writeDouble(mZ);
        } catch (IOException e) {
            // this really shouldn't happen, but whatever
            e.printStackTrace(GTLog.err);
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        return new GTPacketSound(
            aData.readUTF(),
            aData.readFloat(),
            aData.readFloat(),
            aData.readDouble(),
            aData.readDouble(),
            aData.readDouble());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (mSoundName != null) {
            GTUtility.doSoundAtClient(new ResourceLocation(mSoundName), 1, mSoundStrength, mSoundPitch, mX, mY, mZ);
        }
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SOUND.id;
    }
}
