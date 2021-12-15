package gtPlusPlus.australia.world;

import net.minecraft.util.ChunkCoordinates;

public class AustraliaPortalPosition extends ChunkCoordinates {
	public long field_85087_d;
	final AustraliaTeleporterDimensionMod field_85088_e;

	public AustraliaPortalPosition(AustraliaTeleporterDimensionMod gladesTeleporter, int par2, int par3, int par4, long par5) {
		super(par2, par3, par4);
		this.field_85088_e = gladesTeleporter;
		this.field_85087_d = par5;
	}
}