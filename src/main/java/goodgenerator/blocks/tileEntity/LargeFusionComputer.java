package goodgenerator.blocks.tileEntity;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_ChunkManager;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;

public class LargeFusionComputer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    private boolean isLoadedChunk;

    public LargeFusionComputer(String name) {
        super(name);
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return null;
    }

    public LargeFusionComputer(int id, String name, String nameRegional) {
        super(id,name,nameRegional);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {

    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[0];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return null;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && !aBaseMetaTileEntity.isAllowedToWork()) {
            // if machine has stopped, stop chunkloading
            GT_ChunkManager.releaseTicket((TileEntity)aBaseMetaTileEntity);
            this.isLoadedChunk = false;
        }
        else if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isAllowedToWork() && !this.isLoadedChunk) {
            //load a 3x3 area when machine is running
            GT_ChunkManager.releaseTicket((TileEntity)aBaseMetaTileEntity);
            GT_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity, new ChunkCoordIntPair(getChunkX() + 1, getChunkZ() + 1));
            GT_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity, new ChunkCoordIntPair(getChunkX() + 1, getChunkZ()));
            GT_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity, new ChunkCoordIntPair(getChunkX() + 1, getChunkZ() - 1));
            GT_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity, new ChunkCoordIntPair(getChunkX() - 1, getChunkZ() + 1));
            GT_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity, new ChunkCoordIntPair(getChunkX() - 1, getChunkZ()));
            GT_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity, new ChunkCoordIntPair(getChunkX() - 1, getChunkZ() - 1));
            GT_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity, new ChunkCoordIntPair(getChunkX(), getChunkZ() + 1));
            GT_ChunkManager.requestChunkLoad((TileEntity)aBaseMetaTileEntity, new ChunkCoordIntPair(getChunkX(), getChunkZ() - 1));
            this.isLoadedChunk = true;
        }
    }

    @Override
    public void onRemoval() {
        if (this.isLoadedChunk)
            GT_ChunkManager.releaseTicket((TileEntity)getBaseMetaTileEntity());
        super.onRemoval();
    }

    public int getChunkX() {
        return getBaseMetaTileEntity().getXCoord() >> 4;
    }

    public int getChunkZ() {
        return getBaseMetaTileEntity().getZCoord() >> 4;
    }

}
