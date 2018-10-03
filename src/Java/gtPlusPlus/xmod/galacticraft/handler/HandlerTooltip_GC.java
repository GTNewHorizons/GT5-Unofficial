package gtPlusPlus.xmod.galacticraft.handler;

import java.lang.reflect.Field;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import gtPlusPlus.core.item.chemistry.RocketFuels;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.Fluid;

public class HandlerTooltip_GC {

	private static Item mItemBlock;
	private static Block mBlock;
	private static Class<?> oMainClass;
	private static Class<?> oFuelLoaderClass;
	private static String[] mFuelNames;

	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		if (LoadedMods.GalacticraftCore) {

			if (mBlock == null) {
				try {
					Class<?> GCBlocks = Class.forName("micdoodle8.mods.galacticraft.core.blocks.GCBlocks");
					if (GCBlocks != null) {
						oMainClass = GCBlocks;

						Class<?> GCFuelLoader = Class
								.forName("micdoodle8.mods.galacticraft.core.blocks.BlockFuelLoader");

						if (GCFuelLoader != null) {
							oFuelLoaderClass = GCFuelLoader;
						}

						Field aField = ReflectionUtils.getField(GCBlocks, "fuelLoader");
						if (aField != null) {
							Block aBlock = (Block) aField.get(null);
							if (aBlock != null) {
								mBlock = aBlock;
								mItemBlock = Item.getItemFromBlock(mBlock);
							}
						}
					}
				} catch (Throwable t) {
				}
			}			
			if (mFuelNames == null || mFuelNames.length == 0) {
				mFuelNames = new String[RocketFuels.mValidRocketFuels.size()];
				int slot = 0;
				for (Fluid f : RocketFuels.mValidRocketFuels.values()) {
					mFuelNames[slot++] = f.getLocalizedName();
				}
			}
			if (mItemBlock != null) {
				Item aTempItem = event.itemStack.getItem();
				Block aTempBlock = Block.getBlockFromItem(aTempItem);
				if (aTempItem == mItemBlock || oFuelLoaderClass.isInstance(aTempBlock)
						|| event.itemStack.getUnlocalizedName().toLowerCase().contains("fuelloader")) {					
					int aTier = 0;
					for (String s : mFuelNames) {
						if (s != null) {
							event.toolTip.add("Tier "+aTier+": "+s);
						}
					}				
				}
			}
		}
	}
}
