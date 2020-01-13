package gtPlusPlus.api.helpers;

import java.util.HashMap;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.WeightedCollection;
import gtPlusPlus.api.objects.minecraft.multi.SpecialMultiBehaviour;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.util.SpecialBehaviourTooltipHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class GregtechPlusPlus_API {

	public static class Multiblock_API {

		private static final HashMap<String, SpecialMultiBehaviour> mSpecialBehaviourItemMap = new HashMap<String, SpecialMultiBehaviour>();
		
		/**
		 * Register a special behaviour for GT++ Multis to listen use.
		 * @param aBehaviour - An Object which has extended {@link SpecialMultiBehaviour}'s base implementation.
		 * @return - Did this behaviour register properly?
		 */
		public static boolean registerSpecialMultiBehaviour(SpecialMultiBehaviour aBehaviour) {
			if (aBehaviour.getTriggerItem() == null || aBehaviour.getTriggerItemTooltip() == null || aBehaviour.getTriggerItemTooltip().length() <= 0) {
				Logger.INFO("Failed to attach custom multiblock logic to "+ItemUtils.getItemName(aBehaviour.getTriggerItem()));
				return false;
			}
			mSpecialBehaviourItemMap.put("UniqueKey_"+aBehaviour.hashCode(), aBehaviour);
			SpecialBehaviourTooltipHandler.addTooltipForItem(aBehaviour.getTriggerItem(), aBehaviour.getTriggerItemTooltip());	
			Logger.INFO("Attached custom multiblock logic to "+ItemUtils.getItemName(aBehaviour.getTriggerItem()));		
			return true;
		}		

		public static final HashMap<String, SpecialMultiBehaviour> getSpecialBehaviourItemMap() {
			return mSpecialBehaviourItemMap;
		}
		

	}

	public static class VoidMiner_API {	

		private static final HashMap<Integer, HashMap<String, WeightedCollection<Block>>> mMinerLootCache;		

		static {
			mMinerLootCache = new HashMap<Integer, HashMap<String, WeightedCollection<Block>>>();		
		}		


		/**
		 * 
		 * Registers an ore block for a dimension. Uses a default weight of 100.
		 * @param aDim - The Dimension ID
		 * @param aOredictName - The OreDict name of the Ore to be mined.
		 * @return - If there was a valid Block found in the OreDict for the provided name.
		 */
		public static boolean registerOreForVoidMiner(int aDim, String aOredictName) {
			return registerOreForVoidMiner(aDim, aOredictName, 100);
		}

		/**
		 * 
		 * Registers an ore block for a dimension. Uses a default weight of 100.
		 * @param aDim - The Dimension ID
		 * @param aOredictName - The OreDict name of the Ore to be mined.
		 * @param aWeight - The weight of this ore Block.
		 * @return - If there was a valid Block found in the OreDict for the provided name.
		 */
		public static boolean registerOreForVoidMiner(int aDim, String aOredictName, int aWeight) {
			Block b = null;
			ItemStack[] aValidItems = ItemUtils.validItemsForOreDict(aOredictName);
			for (ItemStack g : aValidItems) {
				if (g != null) {
					b = Block.getBlockFromItem(g.getItem());
					if (b != null) {
						break;
					}
				}
			}
			if (b != null) {
				registerOreForVoidMiner(aDim, b, aWeight);
				return true;
			}
			return false;
		}


		/**
		 * Registers an ore block for a dimension. Uses a default weight of 100.
		 * @param aDim - The Dimension ID
		 * @param aOreBlock - The Ore Block to be mined.
		 */
		public static void registerOreForVoidMiner(int aDim, Block aOreBlock) {
			registerOreForVoidMiner(aDim, aOreBlock, 100);
		}

		/**
		 * Registers an ore block for a dimension.
		 * @param aDim - The Dimension ID
		 * @param aOreBlock - The Ore Block to be mined.
		 * @param aWeight - The weight of this ore Block.
		 */
		public static void registerOreForVoidMiner(int aDim, Block aOreBlock, int aWeight) {
			GregtechPlusPlus_API_Internal.writeBlockToDimensionInCache(aDim, 0, aOreBlock, aWeight);		
		}	

		/**
		 * Registers a surface block for a dimension. Uses a default weight of 100.
		 * @param aDim - The Dimension ID
		 * @param aDirtBlock - The Dirt/Grass Block to be mined.
		 */
		public static void registerEarthSurfaceForVoidMiner(int aDim, Block aDirtBlock) {
			registerEarthSurfaceForVoidMiner(aDim, aDirtBlock, 100);	
		}

		/**
		 * Registers a surface block for a dimension.
		 * @param aDim - The Dimension ID
		 * @param aDirtBlock - The Dirt/Grass Block to be mined.
		 * @param aWeight - The weight of this Dirt/Grass Block.
		 */	
		public static void registerEarthSurfaceForVoidMiner(int aDim, Block aDirtBlock, int aWeight) {
			GregtechPlusPlus_API_Internal.writeBlockToDimensionInCache(aDim, 0, aDirtBlock, aWeight);		
		}	

		/**
		 * Registers a stone block for a dimension. Uses a default weight of 100.
		 * @param aDim - The Dimension ID
		 * @param aStoneBlock - The Stone Block to be mined.
		 */
		public static void registerEarthStoneForVoidMiner(int aDim, Block aStoneBlock) {
			registerEarthStoneForVoidMiner(aDim, aStoneBlock, 100);
		}

		/**
		 * Registers a stone block for a dimension.
		 * @param aDim - The Dimension ID
		 * @param aStoneBlock - The Stone Block to be mined.
		 * @param aWeight - The weight of this Stone Block.
		 */	
		public static void registerEarthStoneForVoidMiner(int aDim, Block aStoneBlock, int aWeight) {
			GregtechPlusPlus_API_Internal.writeBlockToDimensionInCache(aDim, 0, aStoneBlock, aWeight);		
		}




		public static WeightedCollection<Block> getAllRegisteredOresForDimension(int aDim) {
			return mMinerLootCache.get(aDim).get("ore");
		}

		public static WeightedCollection<Block> getAllRegisteredDirtTypesForDimension(int aDim) {
			return mMinerLootCache.get(aDim).get("dirt");
		}

		public static WeightedCollection<Block> getAllRegisteredStoneTypesForDimension(int aDim) {
			return mMinerLootCache.get(aDim).get("stone");
		}	

		public static final HashMap<Integer, HashMap<String, WeightedCollection<Block>>> getVoidMinerLootCache() {
			return mMinerLootCache;
		}

	}


	private static class GregtechPlusPlus_API_Internal {

		private static void writeBlockToDimensionInCache(int aDim, int aType, Block aBlock, int aWeight) {			
			HashMap<String, WeightedCollection<Block>> aDimMap = VoidMiner_API.mMinerLootCache.get(aDim);
			if (aDimMap == null) {
				aDimMap = new HashMap<String, WeightedCollection<Block>>();
			}			
			WeightedCollection<Block> aMappedBlocks = getBlockMap(aType, aDimMap);
			aMappedBlocks.put(aWeight, aBlock);

		}

		private static WeightedCollection<Block> getBlockMap(int aType, HashMap<String, WeightedCollection<Block>> aDimMap){
			WeightedCollection<Block> aMappedBlocks;
			String aTypeName = ((aType == 0) ? "ore" : (aType == 1) ? "dirt" : (aType == 2) ? "stone" : "error");
			aMappedBlocks = aDimMap.get(aTypeName);
			if (aMappedBlocks == null) {
				aMappedBlocks = new WeightedCollection<Block>();
			}			
			return aMappedBlocks;
		}

	}


}
