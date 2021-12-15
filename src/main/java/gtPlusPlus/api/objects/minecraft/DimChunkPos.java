package gtPlusPlus.api.objects.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class DimChunkPos {

	public final int dimension;
	public final int xPos;
	public final int zPos;
	public final Chunk mainChunk;


	public DimChunkPos(World world, BlockPos block){
		this.dimension = world.provider.dimensionId;
		this.mainChunk = world.getChunkFromBlockCoords(block.xPos, block.zPos);
		this.xPos = this.mainChunk.xPosition;
		this.zPos = this.mainChunk.zPosition;
	}
	

	public DimChunkPos(TileEntity tile){
		this.dimension = tile.getWorldObj().provider.dimensionId;
		this.mainChunk = tile.getWorldObj().getChunkFromBlockCoords(tile.xCoord, tile.zCoord);
		this.xPos = this.mainChunk.xPosition;
		this.zPos = this.mainChunk.zPosition;
	}
	
	public DimChunkPos(int dim, int x, int z){
		this.dimension = dim;
		this.xPos = x;
		this.zPos = z;		
		Chunk h = Minecraft.getMinecraft().getIntegratedServer().worldServerForDimension(dim).getChunkFromChunkCoords(xPos, zPos);
		if (h == null) {
			this.mainChunk = null;
		}
		else {
			this.mainChunk = h;
		}		
	}
	
	public Chunk getChunk() {
		if (this.mainChunk != null) {
			return this.mainChunk;
		}
		Chunk h = Minecraft.getMinecraft().getIntegratedServer().worldServerForDimension(this.dimension).getChunkFromChunkCoords(xPos, zPos);
		return h;
	}

}
