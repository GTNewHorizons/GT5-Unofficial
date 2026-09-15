package detrav.net;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wital_000 on 20.03.2016.
 */
public abstract class DetravPacket {

    public abstract int getPacketID();

    public abstract void encode(OutputStream out) throws IOException;

    public abstract void process();
}
