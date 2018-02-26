package gtPlusPlus.core.handler.events;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GregtechMetaSafeBlock;

public class UnbreakableBlockManager{

	private static boolean hasRun = false;

	public final BaseMetaTileEntity getmTileEntity() {
		return mTileEntity;
	}


	public final void setmTileEntity(final BaseMetaTileEntity mTileEntity/*, EntityPlayer aPlayer*/) {
		UnbreakableBlockManager.mTileEntity = mTileEntity;
		if (!hasRun){
			hasRun = true;
			this.makeIndestructible(/*aPlayer*/);
		}
		else {
			Logger.WARNING("Why do you run twice?");
		}
	}


	//BaseMetaTileEntity
	//GregtechMetaSafeBlock
	private static BaseMetaTileEntity mTileEntity = null;


	private void makeIndestructible(/*EntityPlayer aPlayer*/){


		Logger.WARNING("Initializing the code to set this TE to -1 hardness and make it indestructible.");
		final int X = mTileEntity.xCoord; //(GregtechMetaSafeBlock) this.mTileEntity.getXCoord();
		final int Y = mTileEntity.yCoord;
		final int Z = mTileEntity.zCoord;
		Logger.WARNING("Grabbing TileEntity @ [x,y,z] |"+X+"|"+Y+"|"+Z+"|");

		try{
			final GregtechMetaSafeBlock MetaSafeBlock = ((GregtechMetaSafeBlock) UnbreakableBlockManager.mTileEntity.getMetaTileEntity());
			final TileEntity BaseMetaTileEntity = mTileEntity.getTileEntity(X, Y, Z);
			//MetaSafeBlockBase.
			final World TE_WORLD = MetaSafeBlock.getBaseMetaTileEntity().getWorld();
			Logger.WARNING("Checking new State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
			final TileEntity entity = BaseMetaTileEntity;
			innerInvincible(MetaSafeBlock, entity, TE_WORLD, /*aPlayer,*/ X, Y, Z);
		}
		catch (final NullPointerException e) {
			System.out.print("Caught a NullPointerException involving Safe Blocks. Cause: ");
			e.printStackTrace();
		}
	}


	private static void innerInvincible(final GregtechMetaSafeBlock MetaSafeBlock, final TileEntity entity, final World TE_WORLD, /*EntityPlayer aPlayer,*/ final int X, final int Y, final int Z){
		if (entity != null){
			Logger.WARNING("Checking new State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
			Logger.WARNING("Grabbed TE: "+entity.toString());




			if ((entity instanceof BaseTileEntity) && !(entity instanceof BaseMetaPipeEntity)){
				final IMetaTileEntity I = ((BaseMetaTileEntity)entity).getMetaTileEntity();
				Logger.WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
				Logger.WARNING("I Details: "+I.getMetaName()+" | "+I.getTileEntityBaseType()+" | "+I.toString());





				if (I instanceof GregtechMetaSafeBlock){
					Logger.WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);

					final Block ThisBlock = I.getBaseMetaTileEntity().getBlock(X, Y, Z);
					Logger.WARNING("Block Details: "+ThisBlock.toString());


					if (((GregtechMetaSafeBlock)I).bUnbreakable){
						ThisBlock.setHardness(Integer.MAX_VALUE);
						//ThisBlock.setResistance(18000000.0F);
						ThisBlock.setResistance(-1);
						ThisBlock.setBlockUnbreakable();
						Logger.WARNING("Changing State of Flag. Old Value="+MetaSafeBlock.bUnbreakable+" Expected Value=true");
						MetaSafeBlock.bUnbreakable = true;
						//entity.markDirty();
						Logger.WARNING("Checking new State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
						Logger.ERROR("New Indestructible Flag enabled.");
						//GT_Utility.sendChatToPlayer(aPlayer, "Block is now unbreakable.");
					}




					else {
						ThisBlock.setHardness(1);
						ThisBlock.setResistance(1.0F);
						Logger.WARNING("Changing State of Flag. Old Value="+MetaSafeBlock.bUnbreakable+" Expected Value=false");
						MetaSafeBlock.bUnbreakable = false;
						//entity.markDirty();
						Logger.WARNING("Checking new State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
						Logger.ERROR("New Indestructible Flag disabled.");
						//GT_Utility.sendChatToPlayer(aPlayer, "Block is now breakable.");
					}

					//entity.markDirty();

					Logger.WARNING("Block Hardness: "+ThisBlock.getBlockHardness(TE_WORLD, X, Y, Z));
					Logger.WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
					hasRun = false;

				}
				else {
					Logger.WARNING("I is not an instanceof MetaSafeBlockBase");
					Logger.WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
				}
			} else {
				Logger.WARNING("TE is not an instanceof BaseTileEntity or may be a pipe.");
				Logger.WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
			}
		}else {
			Logger.WARNING("Did not grab a TE instance to make a block instance from.");
			Logger.WARNING("Checking State of Flag[nUnbreakable]. Value="+MetaSafeBlock.bUnbreakable);
		}
	}

}
