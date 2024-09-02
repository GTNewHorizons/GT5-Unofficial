package gregtech.api.net;

import java.io.IOException;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;

public class GTPacketSound extends GTPacketNew {

    private int mX, mZ;
    private short mY;
    private String mSoundName;
    private float mSoundStrength, mSoundPitch;

    public GTPacketSound() {
        super(true);
    }

    public GTPacketSound(String aSoundName, float aSoundStrength, float aSoundPitch, int aX, short aY, int aZ) {
        super(false);
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
            byteOutputStream.writeInt(mX);
            byteOutputStream.writeShort(mY);
            byteOutputStream.writeInt(mZ);
        } catch (IOException e) {
            // this really shouldn't happen, but whatever
            e.printStackTrace(GTLog.err);
        }
    }

    @Override
    public GTPacketNew decode(ByteArrayDataInput aData) {
        return new GTPacketSound(
            aData.readUTF(),
            aData.readFloat(),
            aData.readFloat(),
            aData.readInt(),
            aData.readShort(),
            aData.readInt());
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
