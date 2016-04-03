package miscutil.core.handler;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import miscutil.core.util.Utils;
import miscutil.gregtech.metatileentity.implementations.base.GregtechMetaSafeBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class XEventHandler {
	@SubscribeEvent
	public void onBreakBlock(BreakEvent event) {
		try{
			TileEntity entity = event.world.getTileEntity(event.x, event.y, event.z);
			if (entity != null && !entity.equals(null)){
				EntityPlayer playerInternal = event.getPlayer();
				Utils.LOG_WARNING(entity.getClass().getSimpleName());
				if (entity.getClass().getSimpleName().equals("")){

				}
				if (entity instanceof BaseTileEntity && !(entity instanceof BaseMetaPipeEntity)){
					IMetaTileEntity X = ((BaseMetaTileEntity)entity).getMetaTileEntity();
					Block ThisBlock = X.getBaseMetaTileEntity().getBlock(event.x, event.y, event.z);
					if (X instanceof GregtechMetaSafeBlockBase){

						String ownerUUID = ((GregtechMetaSafeBlockBase)X).ownerUUID;
						String accessorUUID = playerInternal.getUniqueID().toString();
						Utils.LOG_WARNING("Owner UUID: "+ownerUUID);
						Utils.LOG_WARNING("Accessor UUID: "+accessorUUID);					

						if (((GregtechMetaSafeBlockBase)X).bUnbreakable){
							if (accessorUUID.equals(ownerUUID)){							
								Utils.messagePlayer(playerInternal, "Since you own this block, it has been destroyed.");
								event.setCanceled(false);
							}
							else {
								event.setCanceled(true);
								Utils.messagePlayer(playerInternal, "Since you do not own this block, it has not been destroyed.");
							}
							//
						}
					}
				}
			}

		}
		catch (NullPointerException e) {
			System.out.print("Caught a NullPointerException involving Safe Blocks. Cause: "+e.getCause());
		}
	}
	
	
	@SubscribeEvent
	public void onPlayerInteraction(PlayerInteractEvent aEvent) {
	   if (aEvent.entityPlayer != null && aEvent.entityPlayer.worldObj != null && aEvent.action != null && aEvent.world.provider != null && !aEvent.entityPlayer.worldObj.isRemote && aEvent.action != null && aEvent.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
		   Utils.LOG_ERROR("Test");
	   }
	  }
}
