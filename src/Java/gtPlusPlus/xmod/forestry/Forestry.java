package gtPlusPlus.xmod.forestry;

import static cpw.mods.fml.common.registry.GameRegistry.findBlock;
import static cpw.mods.fml.common.registry.GameRegistry.findItem;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import cofh.mod.ChildMod;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.CustomProperty;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

@ChildMod(parent = CORE.MODID, mod = @Mod(modid = "Gregtech++|CompatForestry",
name = "GT++ Compat: Forestry",
version = CORE.VERSION,
dependencies = "after:Miscutils;after:Forestry",
customProperties = @CustomProperty(k = "cofhversion", v = "true")))
public class Forestry {

	private static final String name = "Forestry";

	@EventHandler
	public void load(final FMLInitializationEvent e) {

		try {
			initForestry();
		} catch (final Throwable $) {
			final ModContainer This = FMLCommonHandler.instance().findContainerFor(this);
			LogManager.getLogger(This.getModId()).log(Level.ERROR, "There was a problem loading " + This.getName(), $);
		}
	}

	private static void initForestry() {

		Item item;

		item = findItem(name, "sapling");
		Block block = findBlock(name, "saplingGE");
		if ((item != null) && (block != null)) {
			//ForestrySapling sapling = new ForestrySapling(item, block);
			//MFRRegistry.registerPlantable(sapling);
			//MFRRegistry.registerFertilizable(sapling);
		} else {
			block = findBlock(name, "soil");
		}
		if (block != null) {
			//ForestryBogEarth bog = new ForestryBogEarth(block);
			//MFRRegistry.registerPlantable(bog);
			//MFRRegistry.registerFertilizable(bog);
			//MFRRegistry.registerHarvestable(bog);
			//MFRRegistry.registerFruit(bog);
		} else {
			for (int i = 1; true; ++i) {
				block = findBlock(name, "log" + i);
				l: if (block == null) {
					if (i > 1) {
						Utils.LOG_WARNING("Forestry logs null at " + i + ".");
					} else {
						block = findBlock(name, "logs");
						if (block != null) {
							break l;
						}
						Utils.LOG_WARNING("Forestry logs null!");
					}
					break;
				}
				//MFRRegistry.registerHarvestable(new HarvestableWood(block));
				//MFRRegistry.registerFruitLogBlock(block);
			}
		}

		for (int i = 1; true; ++i) {
			block = findBlock(name, "fireproofLog" + i);
			l: if (block == null) {
				if (i > 1) {
					Utils.LOG_WARNING("Forestry logs null at " + i + ".");
				} else {
					block = findBlock(name, "logsFireproof");
					if (block != null) {
						break l;
					}
					Utils.LOG_WARNING("Forestry logs null!");
				}
				break;
			}
			//MFRRegistry.registerHarvestable(new HarvestableWood(block));
			//MFRRegistry.registerFruitLogBlock(block);
		}

		block = findBlock(name, "leaves");
		if (block != null) {
			//ForestryLeaf leaf = new ForestryLeaf(block);
			//MFRRegistry.registerFertilizable(leaf);
			//MFRRegistry.registerHarvestable(leaf);
			//MFRRegistry.registerFruit(leaf);
		} else {
			Utils.LOG_WARNING("Forestry leaves null!");
		}

		block = findBlock(name, "pods");
		item = findItem(name, "grafterProven");
		if (block != null) {
			//ForestryPod pod = new ForestryPod(block, item);
			//MFRRegistry.registerFertilizable(pod);
			//MFRRegistry.registerHarvestable(pod);
			//MFRRegistry.registerFruit(pod);
		} else {
			Utils.LOG_WARNING("Forestry pods null!");
		}
	}

	@EventHandler
	public static void postInit(final FMLPostInitializationEvent e) {
		//MFRRegistry.registerLiquidDrinkHandler("bioethanol", new DrinkHandlerBiofuel());
		//TileEntityUnifier.updateUnifierLiquids();
	}

}
