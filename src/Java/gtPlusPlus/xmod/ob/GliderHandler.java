package gtPlusPlus.xmod.ob;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class GliderHandler {

	private static final AutoMap<Integer> mDimensionalBlacklist = new AutoMap<Integer>();

	@SubscribeEvent
	public void onItemUsageEx(final PlayerInteractEvent event) {		
		if (event != null && event.entityPlayer != null) {

			if (event.action != Action.RIGHT_CLICK_BLOCK && event.action != Action.RIGHT_CLICK_AIR) {
				Logger.WARNING("[OpenBlocks] Wrong type of PlayerInteractEvent, skipping.");
			}
			if (event.entityPlayer.worldObj.isRemote) {
				return;
			}

			ItemStack aItem = event.entityPlayer.getItemInUse();
			if (!ItemUtils.checkForInvalidItems(aItem)) {
				Logger.WARNING("[OpenBlocks] Item in use was invalid, trying currentlyEquipped.");
				aItem = event.entityPlayer.getCurrentEquippedItem();
			}
			if (!ItemUtils.checkForInvalidItems(aItem)) {
				Logger.WARNING("[OpenBlocks] Item in use was invalid, trying heldItem.");
				aItem = event.entityPlayer.getHeldItem();
			}			
			if (ItemUtils.checkForInvalidItems(aItem)) {
				Class aItemGliderClass = ReflectionUtils.getClass("openblocks.common.item.ItemHangGlider");
				if (aItemGliderClass.isInstance(aItem.getItem())) {
					if (!canPlayerGlideInThisDimension(event.entityPlayer)){
						event.setCanceled(true);	
						PlayerUtils.messagePlayer(event.entityPlayer, "Glider is blacklisted in this dimension.");
						Logger.WARNING("[OpenBlocks] "+event.entityPlayer.getCommandSenderName()+" tried to use glider in dimension "+event.entityPlayer.getEntityWorld().provider.dimensionId+".");
					}
					else {
						Logger.WARNING("[OpenBlocks] "+event.entityPlayer.getCommandSenderName()+" used glider in dimension "+event.entityPlayer.getEntityWorld().provider.dimensionId+".");						
					}
				}
				else {
					Logger.WARNING("[OpenBlocks] Item was not a glider.");						
				}
			}
			else {
				Logger.WARNING("[OpenBlocks] Bad Item in player hand.");						
			}			
		}
		else {
			Logger.WARNING("[OpenBlocks] Bad event or player.");						
		}		


	}

	private static final boolean canPlayerGlideInThisDimension(EntityPlayer aPlayer) {
		World aWorld = aPlayer.worldObj;
		if (aWorld == null) {
			return false;
		}
		else {
			if (aWorld.provider == null) {
				return false;
			}
			else {
				int aDimID = aWorld.provider.dimensionId;
				for (int i : mDimensionalBlacklist) {
					if (i == aDimID) {
						return false;
					}
				}
			}
		}		
		return true;		
	}

	static final void populateBlacklist() {		
		if (!mDimensionalBlacklist.isEmpty()) {
			return;
		}		
		File aBlacklist = gtPlusPlus.core.util.data.FileUtils.getFile("config/GTplusplus/", "GliderBlacklist", "cfg");
		List<String> lines = new ArrayList<String>();
		try {
			lines = org.apache.commons.io.FileUtils.readLines(aBlacklist, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (lines.isEmpty()) {
			FileWriter fw;
			try {
				String aInfoTip = "# Add one dimension ID per line. Lines with a # are comments and are ignored.";
				fw = new FileWriter(aBlacklist);
				fw.write(aInfoTip);			 
				fw.close();
				lines.add(aInfoTip);
			} catch (IOException e) {				
				e.printStackTrace();
			}			
		}
		if (!lines.isEmpty()) {
			for (String s : lines) {
				if (s != null && !s.equals("") && !s.contains("#")) {
					s = StringUtils.remove(s, " ");
					s = StringUtils.trim(s);
					s = StringUtils.remove(s, ",");
					Integer g = Integer.decode(s);
					if (g != null) {
						mDimensionalBlacklist.add(g);
						Logger.INFO("[OpenBlocks] Added Dimension with ID '"+g+"' to Blacklist for Glider.");
					}
				}
			}			
		}		
	}


}
