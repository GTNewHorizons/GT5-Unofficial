package gtPlusPlus.everglades.world;

import net.minecraft.util.ChunkCoordinates;

public class EvergladesPortalPosition extends ChunkCoordinates {
	public long field_85087_d;
	final TeleporterDimensionMod field_85088_e;

	public EvergladesPortalPosition(TeleporterDimensionMod gladesTeleporter, int par2, int par3, int par4, long par5) {
		super(par2, par3, par4);
		this.field_85088_e = gladesTeleporter;
		this.field_85087_d = par5;
	}
}