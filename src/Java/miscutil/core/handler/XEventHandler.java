package miscutil.core.handler;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import miscutil.core.util.Utils;
import miscutil.gregtech.metatileentity.implementations.base.GregtechMetaSafeBlockBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class XEventHandler {
	@SubscribeEvent
	public void onBreakBlock(BreakEvent event) {
		TileEntity entity = event.world.getTileEntity(event.x, event.y, event.z);
		EntityPlayer playerInternal = event.getPlayer();

		try{
			if (entity instanceof BaseTileEntity && !(entity instanceof BaseMetaPipeEntity)){
				IMetaTileEntity X = ((BaseMetaTileEntity)entity).getMetaTileEntity();
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
							Utils.messagePlayer(playerInternal, "Since you do not own this block, it has not been destroyed.");
							event.setCanceled(true);
						}
						//
					}
				}
			}
		}
		catch (NullPointerException e) {
			System.out.print("Caught a NullPointerException involving Safe Blocks. Cause: "+e.getCause());
		}














	}
}
