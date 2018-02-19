package gtPlusPlus.core.handler.events;

import java.util.UUID;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.*;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.player.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines.GregtechMetaSafeBlockBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

public class PickaxeBlockBreakEventHandler {
	@SubscribeEvent
	public void onBreakBlock(final BreakEvent event) {
		try{
			final TileEntity entity = event.world.getTileEntity(event.x, event.y, event.z);
			if (entity != null){
				final EntityPlayer playerInternal = event.getPlayer();
				Logger.WARNING(entity.getClass().getSimpleName());
				if (entity.getClass().getSimpleName().equals("")){

				}
				if ((entity instanceof BaseMetaTileEntity) && !(entity instanceof BaseMetaPipeEntity)){
					final IMetaTileEntity X = ((BaseMetaTileEntity)entity).getMetaTileEntity();
					final Block ThisBlock = X.getBaseMetaTileEntity().getBlock(event.x, event.y, event.z);
					if (X instanceof GregtechMetaSafeBlockBase){

						final UUID ownerUUID = ((GregtechMetaSafeBlockBase)X).ownerUUID;
						final UUID accessorUUID = playerInternal.getUniqueID();
						Logger.WARNING("Owner UUID: "+ownerUUID);
						Logger.WARNING("Accessor UUID: "+accessorUUID);

						if (((GregtechMetaSafeBlockBase)X).bUnbreakable){

							Logger.INFO("UUID info. Accessor: "+accessorUUID + " | Owner: "+ownerUUID);

							if (accessorUUID == ownerUUID){
								PlayerUtils.messagePlayer(playerInternal, "Since you own this block, it has been destroyed.");
								event.setCanceled(false);
							}
							else {
								event.setCanceled(true);
								PlayerUtils.messagePlayer(playerInternal, "Since you do not own this block, it has not been destroyed.");
							}
							//
						}
					}
				}
			}

		}
		catch (final NullPointerException e) {
			System.out.print("Caught a NullPointerException involving Safe Blocks. Cause: "+e.getCause());
		}
	}


	@SubscribeEvent
	public void onPlayerInteraction(final PlayerInteractEvent aEvent) {
		if ((aEvent.entityPlayer != null) && (aEvent.entityPlayer.worldObj != null) && (aEvent.action != null) && (aEvent.world.provider != null) && !aEvent.entityPlayer.worldObj.isRemote && (aEvent.action != null) && (aEvent.action != PlayerInteractEvent.Action.RIGHT_CLICK_AIR)) {
			//Utils.LOG_ERROR("Test");
		}
	}
}
