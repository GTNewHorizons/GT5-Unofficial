package miscutil.core.handler;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import miscutil.core.util.Utils;
import miscutil.gregtech.metatileentity.implementations.GregtechMetaSafeBlock;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class UnbreakableBlockManager{

	private static boolean hasRun = false;

	public final BaseMetaTileEntity getmTileEntity() {
		return mTileEntity;
	}


	public final void setmTileEntity(BaseMetaTileEntity mTileEntity/*, EntityPlayer aPlayer*/) {
		UnbreakableBlockManager.mTileEntity = mTileEntity;
		if (!hasRun){
			hasRun = true;
			makeIndestructible(/*aPlayer*/);
		}
		else {
			Utils.LOG_WARNING("Why do you run twice?");	
		}
	}


	//BaseMetaTileEntity
	//GregtechMetaSafeBlock
	private static BaseMetaTileEntity mTileEntity = null;


	private void makeIndestructible(/*EntityPlayer aPlayer*/){


		Utils.LOG_WARNING("Initializing the code to set this TE to -1 hardness and make it indestructible.");
		int X = ((BaseMetaTileEntity)mTileEntity).xCoord; //(GregtechMetaSafeBlock) this.mTileEntity.getXCoord();
		int Y = ((BaseMetaTileEntity)mTileEntity).yCoord;
		int Z = ((BaseMetaTileEntity)mTileEntity).zCoord;
		Utils.LOG_WARNING("Grabbing TileEntity @ [x,y,z] |"+X+"|"+Y+"|"+Z+"|");



		try{	
			GregtechMetaSafeBlock MetaSafeBlock = ((GregtechMetaSafeBlock) this.mTileEntity.getMetaTileEntity());
			TileEntity BaseMetaTileEntity = ((BaseMetaTileEntity)mTileEntity).getTileEntity(X, Y, Z);
			//MetaSafeBlockBase.
			World TE_WORLD = MetaSafeBlock.getBaseMetaTileEntity().getWorld();
			Utils.LOG_WARNING("Checking new State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
			TileEntity entity = BaseMetaTileEntity;
			innerInvincible(MetaSafeBlock, entity, TE_WORLD, /*aPlayer,*/ X, Y, Z);		
		}
		catch (NullPointerException e) {
			System.out.print("Caught a NullPointerException involving Safe Blocks. Cause: ");
			e.printStackTrace();
		}
	}


	private static void innerInvincible(GregtechMetaSafeBlock MetaSafeBlock, TileEntity entity, World TE_WORLD, /*EntityPlayer aPlayer,*/ int X, int Y, int Z){
		if (entity != null && !entity.equals(null)){
			Utils.LOG_WARNING("Checking new State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
			Utils.LOG_WARNING("Grabbed TE: "+entity.toString());




			if (entity instanceof BaseTileEntity && !(entity instanceof BaseMetaPipeEntity)){
				IMetaTileEntity I = ((BaseMetaTileEntity)entity).getMetaTileEntity();
				Utils.LOG_WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
				Utils.LOG_WARNING("I Details: "+I.getMetaName()+" | "+I.getTileEntityBaseType()+" | "+I.toString());
				




				if (I instanceof GregtechMetaSafeBlock){				
					Utils.LOG_WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);

					Block ThisBlock = I.getBaseMetaTileEntity().getBlock(X, Y, Z);
					Utils.LOG_WARNING("Block Details: "+ThisBlock.toString());


					if (((GregtechMetaSafeBlock)I).bUnbreakable){
						ThisBlock.setHardness(Integer.MAX_VALUE);
						//ThisBlock.setResistance(18000000.0F);
						ThisBlock.setResistance(-1);
						ThisBlock.setBlockUnbreakable();
						Utils.LOG_WARNING("Changing State of Flag. Old Value="+MetaSafeBlock.bUnbreakable+" Expected Value=true");
						MetaSafeBlock.bUnbreakable = true;
						//entity.markDirty();
						Utils.LOG_WARNING("Checking new State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
						Utils.LOG_ERROR("New Indestructible Flag enabled.");
						//GT_Utility.sendChatToPlayer(aPlayer, "Block is now unbreakable.");
					}




					else {
						ThisBlock.setHardness(1);
						ThisBlock.setResistance(1.0F);
						Utils.LOG_WARNING("Changing State of Flag. Old Value="+MetaSafeBlock.bUnbreakable+" Expected Value=false");
						MetaSafeBlock.bUnbreakable = false;
						//entity.markDirty();
						Utils.LOG_WARNING("Checking new State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
						Utils.LOG_ERROR("New Indestructible Flag disabled.");
						//GT_Utility.sendChatToPlayer(aPlayer, "Block is now breakable.");
					}

					//entity.markDirty();

					Utils.LOG_WARNING("Block Hardness: "+ThisBlock.getBlockHardness(TE_WORLD, X, Y, Z));
					Utils.LOG_WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
					hasRun = false;

				}
				else {
					Utils.LOG_WARNING("I is not an instanceof MetaSafeBlockBase");
					Utils.LOG_WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
				}
			} else {
				Utils.LOG_WARNING("TE is not an instanceof BaseTileEntity or may be a pipe.");
				Utils.LOG_WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
			}
		}else {
			Utils.LOG_WARNING("Did not grab a TE instance to make a block instance from.");
			Utils.LOG_WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
		}
	}

}
