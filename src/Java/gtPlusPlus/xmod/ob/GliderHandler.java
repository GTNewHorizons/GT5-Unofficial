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
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;

public class GliderHandler {
	
	private static final AutoMap<Integer> mDimensionalBlacklist = new AutoMap<Integer>();
	
	@SubscribeEvent
	public void onItemUsage(final PlayerUseItemEvent event) {		
		if (event != null) {			
			ItemStack aItem = event.item;			
			if (ItemUtils.checkForInvalidItems(aItem)) {
				Class aItemGliderClass = ReflectionUtils.getClass("openblocks.common.item.ItemHangGlider");
				if (aItemGliderClass.isInstance(aItem.getItem())) {
					if (!canPlayerGlideInThisDimension(event.entityPlayer)){
						event.setCanceled(true);						
					}
				}
			}			
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
