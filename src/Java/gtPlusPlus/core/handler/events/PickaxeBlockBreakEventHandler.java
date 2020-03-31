package gtPlusPlus.core.handler.events;

import java.util.List;
import java.util.UUID;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkPosition;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines.GregtechMetaSafeBlockBase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.ExplosionEvent;

public class PickaxeBlockBreakEventHandler {
	@SubscribeEvent
	public void onBreakBlock(final BreakEvent event) {
		try{
			final TileEntity entity = event.world.getTileEntity(event.x, event.y, event.z);
			if (entity != null){
				if (entity instanceof BaseMetaTileEntity) {
					final EntityPlayer playerInternal = event.getPlayer();	
					final IMetaTileEntity X = ((BaseMetaTileEntity)entity).getMetaTileEntity();
					if(X instanceof GregtechMetaSafeBlockBase) {
						final UUID ownerUUID = ((GregtechMetaSafeBlockBase)X).ownerUUID;
						final UUID accessorUUID = playerInternal.getUniqueID();
						if (((GregtechMetaSafeBlockBase)X).bUnbreakable){
							if (accessorUUID == ownerUUID){
								PlayerUtils.messagePlayer(playerInternal, "Since you own this block, it has been destroyed.");
							}
							else {
								PlayerUtils.messagePlayer(playerInternal, "Since you do not own this block, it has not been destroyed.");
								event.setCanceled(true);
							}
						}
					}
				}
				
			}
		}
		catch (final NullPointerException e) {
			//System.out.print("Caught a NullPointerException involving Safe Blocks. Cause: "+e.getCause());
		}
	}
	
	@SubscribeEvent
	public void onExplode(ExplosionEvent.Detonate event) {
		try {
			@SuppressWarnings("unchecked")
			List<ChunkPosition> pos = event.explosion.affectedBlockPositions;
			for(int i = 0;i<pos.size();i++) {
				ChunkPosition blockpos = (ChunkPosition) pos.get(i);
				TileEntity entity = event.world.getTileEntity(blockpos.chunkPosX, blockpos.chunkPosY, blockpos.chunkPosZ);
				if (entity != null){
					if (entity instanceof BaseMetaTileEntity) {
						final IMetaTileEntity X = ((BaseMetaTileEntity)entity).getMetaTileEntity();
						if (X instanceof GregtechMetaSafeBlockBase) {
							pos.remove(i);
						}
					}
				}
			}
		}
		catch (final NullPointerException e) {
			
		}
		
	}


	@SubscribeEvent
	public void onPlayerInteraction(final PlayerInteractEvent aEvent) {
		if ((aEvent.entityPlayer != null) && (aEvent.entityPlayer.worldObj != null) && (aEvent.action != null) && (aEvent.world.provider != null) && !aEvent.entityPlayer.worldObj.isRemote && (aEvent.action != null) && (aEvent.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR)) {
			//Utils.LOG_ERROR("Test");
		}
	}
}
