package gtPlusPlus.api.interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface IGregtechPacketEntity {

	public void writePacketData(DataOutputStream data) throws IOException;

	public void readPacketData(DataInputStream data) throws IOException;
	
}
