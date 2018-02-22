package gtPlusPlus.core.util.data;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UUIDUtils {

	//UUID Methods below created by https://gist.github.com/jeffjohnson9046
	//https://gist.github.com/jeffjohnson9046/c663dd22bbe6bb0b3f5e

	public static byte[] getBytesFromUUID(UUID uuid) {
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		return bb.array();
	}

	public static UUID getUUIDFromBytes(byte[] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		Long high = byteBuffer.getLong();
		Long low = byteBuffer.getLong();
		return new UUID(high, low);
	}

}
