package gtPlusPlus.core.handler.events;

import static gtPlusPlus.core.lib.CORE.configSwitches.chanceToDropDrainedShard;
import static gtPlusPlus.core.lib.CORE.configSwitches.chanceToDropFluoriteOre;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.oredict.OreDictionary;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BlockEventHandler {
	private Random random = new Random();

	@SubscribeEvent
	public void onBlockLeftClicked(PlayerInteractEvent event) {
		/*if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) return;

		ItemStack heldItem = event.entityPlayer.getHeldItem();
		Block block = event.world.getBlock(event.x, event.y, event.z);

		// If the block clicked was Stone, the player was holding an Iron Pickaxe and a random integer from 0 (inclusive) to 2 (exclusive) is 0 (50% chance)
		if (block == Blocks.stone && heldItem != null && heldItem.getItem() == Items.iron_pickaxe && random.nextInt(2) == 0) {
			ForgeDirection direction = ForgeDirection.getOrientation(event.face); // Convert the numeric face to a ForgeDirection
			int fireX = event.x + direction.offsetX, fireY = event.y + direction.offsetY, fireZ = event.z + direction.offsetZ; // Offset the block's coordinates according to the direction

			if (event.world.isAirBlock(fireX, fireY, fireZ)) { // If the block at the new coordinates is Air
				event.world.setBlock(fireX, fireY, fireZ, Blocks.fire); // Replace it with Fire
				event.useBlock = Event.Result.DENY; // Prevent the Fire from being extinguished (also prevents Block#onBlockClicked from being called)
			}
		}*/
	}

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		/*if (event.entityLiving instanceof EntityPig && event.source instanceof EntityDamageSource) {
			// getEntity will return the Entity that caused the damage,even for indirect damage sources like arrows/fireballs
			// (where it will return the Entity that shot the projectile rather than the projectile itself)
			Entity sourceEntity = event.source.getEntity();
			ItemStack heldItem = sourceEntity instanceof EntityLiving ? ((EntityLiving) sourceEntity).getHeldItem() :
				sourceEntity instanceof EntityPlayer ? ((EntityPlayer) sourceEntity).getHeldItem() : null;

				if (heldItem != null && heldItem.getItem() == Items.iron_pickaxe) {
					System.out.println("EntityPig drops event");
					event.drops.clear();
					event.entityLiving.dropItem(Items.diamond, 64);
				}
		}*/
	}
	
	

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		
	}
	

	ArrayList<ItemStack> oreLimestone = OreDictionary.getOres("oreLimestone");
	ArrayList<ItemStack> blockLimestone = OreDictionary.getOres("limestone");
	ItemStack fluoriteOre = ItemUtils.getSimpleStack(Item.getItemFromBlock(ModBlocks.blockOreFluorite));

	//Used to handle Thaumcraft Shards when TC is not installed.
	@SubscribeEvent
	public void harvestDrops(BlockEvent.HarvestDropsEvent event) {
		//Spawn Dull Shards (Can spawn from Tree Logs, Grass or Stone. Stone going to be the most common source.)
		if ((event.block == Blocks.stone || event.block == Blocks.sandstone || event.block == Blocks.log || event.block == Blocks.log2 || event.block == Blocks.grass) 
				&& !LoadedMods.Thaumcraft && chanceToDropDrainedShard != 0) {
			//small chance for one to spawn per stone mined. 1 per 3 stacks~ //TODO MAKE A CONFIG OPTION
			if (MathUtils.randInt(1, chanceToDropDrainedShard) == 1){
				//Let's sort out a lucky charm for the player.
				int FancyChance = MathUtils.randInt(1, 4);
				if (MathUtils.randInt(1, 100) < 90){
					event.drops.add(new ItemStack(ModItems.shardDull));	
				}
				//Make a Fire Shard
				else if (FancyChance == 1){
					event.drops.add(new ItemStack(ModItems.shardIgnis));	
				}
				//Make a Water Shard.
				else if (FancyChance == 2){
					event.drops.add(new ItemStack(ModItems.shardAqua));	
				}
				//Make an Earth Shard.
				else if (FancyChance == 3){
					event.drops.add(new ItemStack(ModItems.shardTerra));	
				}
				//Make an Air Shard.
				else if (FancyChance == 4){
					event.drops.add(new ItemStack(ModItems.shardAer));	
				}				
			}	
			else {
				Utils.LOG_WARNING("invalid chance");
			}
		}
		
		//Spawns Fluorite from Lime Stone
		if (chanceToDropFluoriteOre != 0){
		if (!oreLimestone.isEmpty() || !blockLimestone.isEmpty()){
			if (!oreLimestone.isEmpty())
			for (ItemStack temp : oreLimestone){
				if (ItemUtils.getSimpleStack(Item.getItemFromBlock(event.block)) == temp) {
					if (MathUtils.randInt(1, chanceToDropFluoriteOre) == 1){
						event.drops.add(fluoriteOre.copy());							
					}
				}
			}
			if (!oreLimestone.isEmpty())
				for (ItemStack temp : blockLimestone){
					if (ItemUtils.getSimpleStack(Item.getItemFromBlock(event.block)) == temp) {
						if (MathUtils.randInt(1, chanceToDropFluoriteOre) == 1){
							event.drops.add(fluoriteOre.copy());							
						}
					}
				}
		}
		if (event.block == Blocks.sandstone){
			if (MathUtils.randInt(1, chanceToDropFluoriteOre*20) == 1){
				event.drops.add(fluoriteOre.copy());							
			}
		}
		}
	}

	@SubscribeEvent
	public void logsHarvest(BlockEvent.HarvestDropsEvent event) {
		/*if (event.block instanceof BlockLog) {
			// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2444501-harvestdropevent-changing-drops-of-vanilla-blocks

			//			Utils.sendServerMessage("Logs! Harvester: %s Drops: %s", event.harvester != null ? event.harvester.getCommandSenderName() : "<none>", event.drops.stream().map(ItemStack.toString()).collect(Collectors.joining(", ")));
			if (event.harvester != null) {
				ItemStack heldItem = event.harvester.getHeldItem();
				if (heldItem == null || heldItem.getItem().getHarvestLevel(heldItem, "axe") < 1) {
					event.drops.clear();
					Utils.sendServerMessage("Harvester had wrong tool, clearing drops");
				} else {
					Utils.sendServerMessage("Harvester had correct tool, not clearing drops");
				}
			} else {
				event.drops.clear();
				Utils.sendServerMessage("No harvester, clearing drops");
			}
		}*/
	}
}
