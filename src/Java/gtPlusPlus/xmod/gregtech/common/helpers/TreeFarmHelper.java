package gtPlusPlus.xmod.gregtech.common.helpers;

import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableTreeFarmerParticles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.Event.Result;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.common.items.GT_MetaGenerated_Item_02;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.slots.SlotBuzzSaw.SAWTOOL;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.AutoMap;
import gtPlusPlus.core.util.array.BlockPos;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.particles.BlockBreakParticles;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fluids.FluidStack;

public class TreeFarmHelper {

	public static final FluidStack fertT1 = FluidUtils.getFluidStack("fluid.fertiliser", 3);
	public static final FluidStack fertT2 = FluidUtils.getFluidStack("fluid.un18fertiliser", 2);
	public static final FluidStack fertT3 = FluidUtils.getFluidStack("fluid.un32fertiliser", 1);

	public static ITexture[][][] getTextureSet() {
		final ITexture[][][] rTextures = new ITexture[10][17][];
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = TreeFarmHelper.getFront(i);
			rTextures[1][i + 1] = TreeFarmHelper.getBack(i);
			rTextures[2][i + 1] = TreeFarmHelper.getBottom(i);
			rTextures[3][i + 1] = TreeFarmHelper.getTop(i);
			rTextures[4][i + 1] = TreeFarmHelper.getSides(i);
			rTextures[5][i + 1] = TreeFarmHelper.getFrontActive(i);
			rTextures[6][i + 1] = TreeFarmHelper.getBackActive(i);
			rTextures[7][i + 1] = TreeFarmHelper.getBottomActive(i);
			rTextures[8][i + 1] = TreeFarmHelper.getTopActive(i);
			rTextures[9][i + 1] = TreeFarmHelper.getSidesActive(i);
		}
		return rTextures;
	}

	public static ITexture[] getFront(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getBack(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getBottom(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getTop(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getSides(final byte aColor) {
		return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[2][aColor + 1]};
	}

	public static ITexture[] getFrontActive(final byte aColor) {
		return getFront(aColor);
	}

	public static ITexture[] getBackActive(final byte aColor) {
		return getBack(aColor);
	}

	public static ITexture[] getBottomActive(final byte aColor) {
		return getBottom(aColor);
	}

	public static ITexture[] getTopActive(final byte aColor) {
		return getTop(aColor);
	}

	public static ITexture[] getSidesActive(final byte aColor) {
		return getSides(aColor);
	}

	public static boolean applyBonemeal(final EntityPlayer player, final World world, final int intX, final int intY, final int intZ, final short multiplier){
		final Block block = world.getBlock(intX, intY, intZ);


		int roll;
		int rollNeeded;

		if (multiplier==1){
			roll = MathUtils.randInt(1, 15);
			rollNeeded = 15;
		}
		else if (multiplier==2){
			roll = MathUtils.randInt(1, 10);
			rollNeeded = 10;
		}
		else {
			roll = MathUtils.randInt(1, 5);
			rollNeeded = 5;
		}

		if (roll != rollNeeded){
			return false;
		}

		//EntityPlayer player = FakePlayerFactory.getMinecraft((WorldServer)world);
		if (!world.isRemote){
			if (enableTreeFarmerParticles){
				world.playAuxSFX(2005, intX, intY, intZ, 0);
			}
		}
		final BonemealEvent event = new BonemealEvent(player, world, block, intX, intY, intZ);
		if (MinecraftForge.EVENT_BUS.post(event)){
			Logger.MACHINE_INFO("Not sure why this returned false");
			return false;
		}
		if (event.getResult() == Result.ALLOW){
			if (!world.isRemote){
				world.playAuxSFX(2005, intX, intY, intZ, 0);
			}
			return true;
		}
		if (block instanceof IGrowable){
			final IGrowable igrowable = (IGrowable)block;
			if (igrowable.func_149851_a(world, intX, intY, intZ, world.isRemote)){
				if (!world.isRemote){
					if (igrowable.func_149852_a(world, CORE.RANDOM, intX, intY, intZ)){
						igrowable.func_149853_b(world, CORE.RANDOM, intX, intY, intZ);
					}
				}
				return true;
			}
		}
		return false;
	}

	public static boolean cleanUp(final IGregTechTileEntity aBaseMetaTileEntity){
		Logger.MACHINE_INFO("called cleanUp()");
		int cleanedUp = 0;
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 11;
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 11;

		for (int h=1;h<175;h++){
			for (int i = -11; i <= 11; i++) {
				for (int j = -11; j <= 11; j++) {

					final Block testBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);


					if
					((
							((i == -8) || (i == 8)) ||
							((i == -9) || (i == 9)) ||
							((i == -10) || (i == 10)) ||
							((i == -11) || (i == 11))
							)
							&&
							(
									((j == -8) || (j == 8)) ||
									((j == -9) || (j == 9)) ||
									((j == -10) || (j == 10)) ||
									((j == -11) || (j == 11))
									)){

						if (!testBlock.getUnlocalizedName().toLowerCase().contains("air") || !testBlock.getUnlocalizedName().toLowerCase().contains("pumpkin")) {
							Logger.WARNING("5:"+testBlock.getUnlocalizedName());
						} else {
							aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getXCoord()+xDir+i, aBaseMetaTileEntity.getYCoord()+h, aBaseMetaTileEntity.getZCoord()+zDir+j, Blocks.bookshelf);
						}
					}


					//If not in the middle - don't know how else to check this one without lots of !=
					if (
							(i != 7) && (i != -7) && (j != 7) && (j != -7) &&
							(i != 6) && (i != -6) && (j != 6) && (j != -6) &&
							(i != 5) && (i != -5) && (j != 5) && (j != -5) &&
							(i != 4) && (i != -4) && (j != 4) && (j != -4) &&
							(i != 3) && (i != -3) && (j != 3) && (j != -3) &&
							(i != 2) && (i != -2) && (j != 2) && (j != -2) &&
							(i != 1) && (i != -1) && (j != 1) && (j != -1) &&
							(i != 0) && (j != 0)
							){

						if (!testBlock.getUnlocalizedName().toLowerCase().contains("air") || !testBlock.getUnlocalizedName().toLowerCase().contains("pumpkin")) {
							Logger.WARNING("0:"+testBlock.getUnlocalizedName());
						} else {
							aBaseMetaTileEntity.getWorld().setBlock(aBaseMetaTileEntity.getXCoord()+xDir+i, aBaseMetaTileEntity.getYCoord()+h, aBaseMetaTileEntity.getZCoord()+zDir+j, Blocks.melon_block);
						}


						if (isLeaves(testBlock) || isWoodLog(testBlock)){
							Logger.WARNING("1:"+testBlock.getUnlocalizedName());
							int posiX, posiY, posiZ;
							posiX = aBaseMetaTileEntity.getXCoord()+xDir+i;
							posiY = aBaseMetaTileEntity.getYCoord()+h;
							posiZ = aBaseMetaTileEntity.getZCoord()+zDir+j;
							//Utils.LOG_MACHINE_INFO("Cleaning Up some leftovers.");
							cleanedUp++;
							aBaseMetaTileEntity.getWorld().setBlockToAir(posiX, posiY, posiZ);
							new BlockBreakParticles(aBaseMetaTileEntity.getWorld(), posiX, posiY, posiZ, Blocks.dirt);
						}
						else {
							//Utils.LOG_WARNING("2:"+testBlock.getUnlocalizedName());
						}
					}
					else {
						//Utils.LOG_WARNING("1");
					}


				}

			}
		}
		Logger.MACHINE_INFO("cleaning up | "+cleanedUp );
		return true;
	}

	public static SAWTOOL isCorrectMachinePart(final ItemStack aStack) {
		if (aStack != null){
			//Utils.LOG_WARNING("Found "+aStack.getDisplayName()+" in the GUI slot.");
			if ((aStack.getItem() instanceof GT_MetaGenerated_Item_02) || (aStack.getItem() instanceof GT_MetaGenerated_Tool)){
				if (OrePrefixes.craftingTool.contains(aStack)){
					if (aStack.getDisplayName().toLowerCase().contains("saw") || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01")){
						if (aStack.getItemDamage() == 10){
							return SAWTOOL.SAW;
						}
						else if (aStack.getItemDamage() == 140  || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01.140")){
							return SAWTOOL.BUZZSAW;
						}
						else if (aStack.getItemDamage() == 110  || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01.110")){
							return SAWTOOL.CHAINSAW;
						}
						else if (aStack.getItemDamage() == 112  || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01.112")){
							return SAWTOOL.CHAINSAW;
						}
						else if (aStack.getItemDamage() == 114  || aStack.getDisplayName().toLowerCase().contains("gt.metatool.01.114")){
							return SAWTOOL.CHAINSAW;
						}
						else {
							return SAWTOOL.NONE;
						}
					}
				}
			}
		}
		return SAWTOOL.NONE;
	}

	public static boolean isHumusLoaded = false;
	public static boolean isForestryLogsLoaded = false;
	public static boolean isForestryFenceLoaded = false;
	public static boolean isForestrySaplingsLoaded = false;
	public static boolean isForestryLeavesLoaded = false;
	public static Block blockHumus;

	public static boolean isForestryValid(){
		if (!LoadedMods.Forestry){
			return false;
		}
		if (ReflectionUtils.doesClassExist("forestry.core.blocks.BlockSoil")){
			isHumusLoaded = true;
		}
		if (ReflectionUtils.doesClassExist("forestry.arboriculture.blocks.BlockLog")){
			isForestryLogsLoaded = true;
		}
		if (ReflectionUtils.doesClassExist("forestry.arboriculture.blocks.BlockArbFence")){
			isForestryFenceLoaded = true;
		}
		if (ReflectionUtils.doesClassExist("forestry.arboriculture.blocks.BlockSapling")){
			isForestrySaplingsLoaded = true;
		}
		if (ReflectionUtils.doesClassExist("forestry.arboriculture.blocks.BlockForestryLeaves")){
			isForestryLeavesLoaded = true;
		}
		return true;
	}

	@Optional.Method(modid = "Forestry")
	public static Block getHumus(){
		if(blockHumus != null){
			return blockHumus;
		}
		else if (ReflectionUtils.doesClassExist("forestry.core.blocks.BlockSoil")){
			try {
				final Class<?> humusClass = Class.forName("forestry.core.blocks.BlockSoil");
				final ItemStack humusStack = ItemUtils.getCorrectStacktype("Forestry:soil", 1);
				if (humusClass != null){
					blockHumus = Block.getBlockFromItem(humusStack.getItem());
					return Block.getBlockFromItem(humusStack.getItem());
				}
			} catch (final ClassNotFoundException e) {}
		}
		return null;
	}

	public static boolean isWoodLog(final Block log){
		final String tTool = log.getHarvestTool(0);

		if ((log == Blocks.log) || (log == Blocks.log2)){
			return true;
		}

		//Forestry/General Compat
		if (log.getClass().getName().toLowerCase().contains("blocklog")){
			return true;
		}

		//IC2 Rubber Tree Compat
		if (log.getClass().getName().toLowerCase().contains("rubwood") || log.getClass().getName().toLowerCase().contains("rubleaves")){
			return true;
		}

		return  (OrePrefixes.log.contains(new ItemStack(log, 1))&& ((tTool != null) && (tTool.equals("axe")))) || (log.getMaterial() != Material.wood) ? false : (OrePrefixes.fence.contains(new ItemStack(log, 1)) ? false : true);
	}

	public static boolean isLeaves(final Block log){
		if (log.getUnlocalizedName().toLowerCase().contains("leaf")){
			return true;
		}
		if (log.getUnlocalizedName().toLowerCase().contains("leaves")){
			return true;
		}
		if (log.getLocalizedName().toLowerCase().contains("leaf")){
			return true;
		}
		if (log.getLocalizedName().toLowerCase().contains("leaves")){
			return true;
		}
		return  OrePrefixes.leaves.contains(new ItemStack(log, 1)) || (log.getMaterial() == Material.leaves);
	}

	public static boolean isSapling(final Block log){
		if (log != null){
			if (OrePrefixes.sapling.contains(new ItemStack(log, 1))){
				Logger.WARNING(""+log.getLocalizedName());
			}
			if (log.getLocalizedName().toLowerCase().contains("sapling")){
				Logger.WARNING(""+log.getLocalizedName());
				return true;
			}
		}
		return  OrePrefixes.sapling.contains(new ItemStack(log, 1));
	}

	public static boolean isDirtBlock(final Block dirt){
		return  (dirt == Blocks.dirt ? true : (dirt == Blocks.grass ? true : (getHumus() == null ? false : (dirt == blockHumus ? true : false))));
	}

	public static boolean isFenceBlock(final Block fence){
		return  (fence == Blocks.fence ? true : (fence == Blocks.fence_gate ? true : (fence == Blocks.nether_brick_fence ? true : (OrePrefixes.fence.contains(new ItemStack(fence, 1)) ? true : false))));
	}

	public static boolean isAirBlock(final Block air){

		if (air.getLocalizedName().toLowerCase().contains("air")){
			return true;
		}

		if (air.getClass().getName().toLowerCase().contains("residual") || air.getClass().getName().toLowerCase().contains("heat")){
			return true;
		}

		//Utils.LOG_INFO("Found "+air.getLocalizedName());

		return (air == Blocks.air ? true : (air instanceof BlockAir ? true : false));
	}

	/*public static boolean isSaplingBlock(Block sapling){
		return (sapling == Blocks.sapling ? true : (sapling == Blocks.))
	}*/

	public static BlockPos checkForLogsInGrowArea(final IGregTechTileEntity aBaseMetaTileEntity) {
		final int xDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 7;
		final int zDir = net.minecraftforge.common.util.ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 7;
		for (int i = -7; i <= 7; i++) {
			for (int j = -7; j <= 7; j++) {
				for (int h = 0; h <= 1; h++) {
					//Farm Floor inner 14x14
					if (((i != -7) && (i != 7)) && ((j != -7) && (j != 7))) {
						if (h == 1) {
							if (TreeFarmHelper.isWoodLog(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j))) {
								//Logger.INFO("Found a Log");
								return new BlockPos(aBaseMetaTileEntity.getXCoord()+xDir + i, aBaseMetaTileEntity.getYCoord()+h, aBaseMetaTileEntity.getZCoord()+zDir + j);
							}
						}
					}
				}
			}
		}
		return null;
	}



	public static ItemStack[] findTreeFromBase(World world, BlockPos h) {
		Map<String, BlockPos> mResultsAroundBaseLayer = findTreeViaBranching(world, h);		
		Map<String, Map<String, BlockPos>> mOtherResults = new ConcurrentHashMap<String, Map<String, BlockPos>>();
		String hash = Utils.calculateChecksumMD5(mResultsAroundBaseLayer);
		mOtherResults.put(hash, mResultsAroundBaseLayer);		
		Logger.INFO("Initial Search found "+mResultsAroundBaseLayer.size()+" blocks to search around.");
		for (BlockPos j : mResultsAroundBaseLayer.values()) {
			Map<String, BlockPos> x = findTreeViaBranching(world, j);
			hash = Utils.calculateChecksumMD5(x);
			if (hash != null && !mOtherResults.containsKey(hash)) {
				Logger.INFO("Branching.");
				mOtherResults.put(hash, x);		
			}			
		}
		if (mOtherResults.size() > 0) {
			TreeCutter harvestManager = new TreeCutter(world);
			for (Map<String, BlockPos> a : mOtherResults.values()) {
				for (BlockPos p : a.values()) {
					harvestManager.queue(p);					
				}
			}
			if (harvestManager.isValid) {
				ItemStack[] loot = harvestManager.getDrops();
				if (loot.length > 0) {
					Logger.INFO("Returning Drops from harvestManager Queue.");
					return loot;
				}
			}
		}
		return new ItemStack[] {};
	}

	public static Map<String, BlockPos> findTreeViaBranching(World world, BlockPos h) {		
		Map<String, BlockPos> results = new ConcurrentHashMap<String, BlockPos>();
		final Block block = world.getBlock(h.xPos, h.yPos, h.zPos);	
		Logger.INFO("Searching around "+h.getLocationString());
		int xRel = h.xPos, yRel = h.yPos, zRel = h.zPos;		
		//if (TreeFarmHelper.isWoodLog(block)) {			
		for (int a=-2;a<2;a++) {
			for (int b=-2;b<2;b++) {
				for (int c=-2;c<2;c++) {						
					//Check block
					//Logger.INFO("Looking at X: "+(xRel+a)+" | Y: "+(yRel+b)+" | Z: "+(zRel+c));
					Block log = world.getBlock(xRel+a, yRel+b, zRel+c);					
					BlockPos P = new BlockPos(xRel+a, yRel+b, zRel+c); 
					String hash = Utils.calculateChecksumMD5(P);
					if (TreeFarmHelper.isWoodLog(log) || TreeFarmHelper.isLeaves(log)) {	
						if (hash != null && !results.containsKey(hash)) {
							results.put(hash, P);
						}
					}
				}
			}
		}
		//}
		if (results.isEmpty()) {
			//Logger.INFO("Returning Empty Branch Iteration.");	
			return new HashMap<String, BlockPos>();
		}
		else {
			Logger.INFO("Returning Valid Branch Iteration.");
			return results;
		}
	}



	/**
	 * Tree Cutting
	 */

	public static class TreeCutter {

		private final World mWorld;
		private Map<String, BlockPos> mQueue = new ConcurrentHashMap<String, BlockPos>();
		private AutoMap<ItemStack[]> mDrops = new AutoMap<ItemStack[]>();	
		private boolean isValid = true;

		public TreeCutter(World world) {
			this.mWorld = world;
		}		

		public boolean queue(BlockPos pos) {
			if (isValid && pos != null) {
				Logger.INFO("Queued: "+pos.getLocationString());
				String hash = Utils.calculateChecksumMD5(pos);			
				if (hash != null && !mQueue.containsKey(hash)) {
					mQueue.put(hash, pos);
					return true;
				}
			}
			return false;
		}

		private boolean emptyQueue() {
			if (isValid) {
				Logger.INFO("Emptying Queue.");
				if (this.mQueue.size() > 0) {				
					int totalRemoved = 0;
					for (BlockPos h : mQueue.values()) {
						final Block block = mWorld.getBlock(h.xPos, h.yPos, h.zPos);
						if (block != null) {
							final int dropMeta = mWorld.getBlockMetadata(h.xPos, h.yPos, h.zPos);
							final ArrayList<ItemStack> blockDrops = block.getDrops(mWorld, h.xPos, h.yPos, h.zPos, dropMeta, 0);
							final ItemStack[] drops = ItemUtils.getBlockDrops(blockDrops);
							mDrops.put(drops);
							//Remove drop that was added to the bus.
							mWorld.setBlockToAir(h.xPos, h.yPos, h.zPos);
							//new BlockBreakParticles(mWorld, h.xPos, h.yPos, h.zPos, block);
							totalRemoved++;						
						}					
					}				
					if (totalRemoved > 0 && mDrops.size() > 0) {
						return true;
					}				
				}
			}
			return false;
		}

		public ItemStack[] getDrops() {			
			//If Queue is successfully cleared and drops are created, let us continue.
			if (isValid && emptyQueue()) {				
				AutoMap<ItemStack> mCollective = new AutoMap<ItemStack>();
				//Iterate ALL of the arrays, add output to a collective.
				for (ItemStack[] i : this.mDrops) {
					//Array is not null.
					if (i != null) {
						//Iterate this array.
						for (int d=0;d<i.length;d++) {
							//Put Output into collective if valid
							if (i[d] != null && i[d].stackSize > 0) {
								mCollective.put(i[d]);	
							}													
						}
					}
				}	
				//Build an ItemStack array.
				ItemStack[] drops = new ItemStack[mCollective.size()];
				for (int m=0;m<drops.length;m++) {
					drops[m] = mCollective.get(m);
				}
				//Return drops array if it's valid.
				if (drops.length > 0) {
					isValid = false;
					return drops;
				}
			}			
			//Invalid or no drops, return empty array.
			isValid = false;
			return new ItemStack[] {};
		}		

	}










}
