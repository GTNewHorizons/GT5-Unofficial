package gtPlusPlus.xmod.sc2.modules.workers.tools;

import net.minecraft.block.BlockCrops;
import net.minecraft.init.Items;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.common.helpers.TreeFarmHelper;
import gtPlusPlus.xmod.sc2.slots.SlotExoticSeed;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.common.IPlantable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.inventory.IInventory;
import vswe.stevescarts.Slots.SlotBase;
import vswe.stevescarts.Modules.ModuleBase;
import vswe.stevescarts.Modules.Workers.Tools.ModuleFarmer;
import vswe.stevescarts.Carts.MinecartModular;
import java.util.ArrayList;
import vswe.stevescarts.Modules.ICropModule;

public abstract class ModuleExoticFarmer extends ModuleFarmer {

	//Detect Forestry
	protected boolean isForestryLoaded = false;
	private final Block forestryHumus;

	private ArrayList<ICropModule> plantModulesExotic;

	public ModuleExoticFarmer(final MinecartModular cart) {
		super(cart);
		isForestryLoaded = LoadedMods.Forestry;
		if (isForestryLoaded) {
			forestryHumus = TreeFarmHelper.getHumus();
		}
		else {
			forestryHumus = null;
		}
	}

	public void init() {
		super.init();
		this.plantModulesExotic = new ArrayList<ICropModule>();
		for (final ModuleBase module : this.getCart().getModules()) {
			if (module instanceof ICropModule) {
				this.plantModulesExotic.add((ICropModule) module);
			}
		}
	}

	public byte getWorkPriority() {
		return 90;
	}

	protected SlotBase getSlot(final int slotId, int x, final int y) {
		if (x == 0) {
			return super.getSlot(slotId, x, y);
		}
		--x;
		return (SlotBase) new SlotExoticSeed((IInventory) this.getCart(), this, slotId, 8 + x * 18, 28 + y * 18);
	}

	public boolean work() {
		final Vec3 next = this.getNextblock();
		final int x = (int) next.xCoord;
		final int y = (int) next.yCoord;
		final int z = (int) next.zCoord;
		for (int i = -this.getRange(); i <= this.getRange(); ++i) {
			for (int j = -this.getRange(); j <= this.getRange(); ++j) {
				final int coordX = x + i;
				final int coordY = y - 1;
				final int coordZ = z + j;
				if (this.farm(coordX, coordY, coordZ)) {
					return true;
				}
				if (this.till(coordX, coordY, coordZ)) {
					return true;
				}
				if (this.plant(coordX, coordY, coordZ)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean till(final int x, final int y, final int z) {
		final Block b = this.getCart().worldObj.getBlock(x, y, z);
		if (this.getCart().worldObj.isAirBlock(x, y + 1, z) && (b == Blocks.grass || b == Blocks.dirt)) {
			if (this.doPreWork()) {
				this.startWorking(10);
				return true;
			}
			this.stopWorking();
			this.getCart().worldObj.setBlock(x, y, z, (isForestryLoaded ? forestryHumus : Blocks.farmland));
		}
		return false;
	}

	protected boolean plant(final int x, final int y, final int z) {
		int hasSeeds = -1;
		final Block soilblock = this.getCart().worldObj.getBlock(x, y, z);
		if (soilblock != null) {
			for (int i = 0; i < this.getInventorySize(); ++i) {
				if (this.getStack(i) != null && this.isSeedValidHandler(this.getStack(i))) {
					final Block cropblock = this.getCropFromSeedHandler(this.getStack(i));
					if (cropblock != null && cropblock instanceof IPlantable
							&& this.getCart().worldObj.isAirBlock(x, y + 1, z)
							&& soilblock.canSustainPlant((IBlockAccess) this.getCart().worldObj, x, y, z,
									ForgeDirection.UP, (IPlantable) cropblock)) {
						hasSeeds = i;
						break;
					}
				}
			}
			if (hasSeeds != -1) {
				if (this.doPreWork()) {
					this.startWorking(25);
					return true;
				}
				this.stopWorking();
				final Block cropblock2 = this.getCropFromSeedHandler(this.getStack(hasSeeds));
				this.getCart().worldObj.setBlock(x, y + 1, z, cropblock2);
				final ItemStack stack = this.getStack(hasSeeds);
				--stack.stackSize;
				if (this.getStack(hasSeeds).stackSize <= 0) {
					this.setStack(hasSeeds, (ItemStack) null);
				}
			}
		}
		return false;
	}

	protected boolean farm(final int x, final int y, final int z) {
		if (!this.isBroken()) {
			final Block block = this.getCart().worldObj.getBlock(x, y + 1, z);
			final int m = this.getCart().worldObj.getBlockMetadata(x, y + 1, z);
			if (this.isReadyToHarvestHandler(x, y + 1, z)) {
				if (this.doPreWork()) {
					final int efficiency = (this.enchanter != null) ? this.enchanter.getEfficiencyLevel() : 0;
					final int workingtime = (int) (this.getBaseFarmingTime()
							/ Math.pow(1.2999999523162842, efficiency));
					ReflectionUtils.invokeVoid(this, "setFarming", new Class[] {int.class}, new Object[] {((int) workingtime * 4)});
					this.startWorking(workingtime);
					return true;
				}
				this.stopWorking();
				ArrayList<ItemStack> stuff;
				if (this.shouldSilkTouch(block, x, y, z, m)) {
					stuff = new ArrayList<ItemStack>();
					final ItemStack stack = this.getSilkTouchedItem(block, m);
					if (stack != null) {
						stuff.add(stack);
					}
				} else {
					final int fortune = (this.enchanter != null) ? this.enchanter.getFortuneLevel() : 0;
					stuff = (ArrayList<ItemStack>) block.getDrops(this.getCart().worldObj, x, y + 1, z, m, fortune);
				}
				for (final ItemStack iStack : stuff) {
					this.getCart().addItemToChest(iStack);
					if (iStack.stackSize != 0) {
						final EntityItem entityitem = new EntityItem(this.getCart().worldObj, this.getCart().posX,
								this.getCart().posY, this.getCart().posZ, iStack);
						entityitem.motionX = (x - this.getCart().x()) / 10.0f;
						entityitem.motionY = 0.15000000596046448;
						entityitem.motionZ = (z - this.getCart().z()) / 10.0f;
						this.getCart().worldObj.spawnEntityInWorld((Entity) entityitem);
					}
				}
				this.getCart().worldObj.setBlockToAir(x, y + 1, z);
				this.damageTool(3);
			}
		}
		return false;
	}

	protected int getBaseFarmingTime() {
		return 15;
	}

	public boolean isSeedValidHandler(final ItemStack seed) {
		for (final ICropModule module : this.plantModulesExotic) {
			if (module.isSeedValid(seed)) {
				return true;
			}
		}
		return false;
	}

	protected Block getCropFromSeedHandler(final ItemStack seed) {
		for (final ICropModule module : this.plantModulesExotic) {
			if (module.isSeedValid(seed)) {
				return module.getCropFromSeed(seed);
			}
		}
		return null;
	}

	protected boolean isReadyToHarvestHandler(final int x, final int y, final int z) {
		for (final ICropModule module : this.plantModulesExotic) {
			if (module.isReadyToHarvest(x, y, z)) {
				return true;
			}
		}
		return false;
	}

	public boolean isSeedValid(final ItemStack seed) {			
		return getBlockFromPams(seed) != null || seed.getItem() == Items.wheat_seeds || seed.getItem() == Items.potato || seed.getItem() == Items.carrot;
	}

	public Block getCropFromSeed(final ItemStack seed) {
		Block pamCrop = getBlockFromPams(seed);		
		if (pamCrop != null) {
			return pamCrop;
		}
		if (seed.getItem() == Items.carrot) {
			return Blocks.carrots;
		}
		if (seed.getItem() == Items.potato) {
			return Blocks.potatoes;
		}
		if (seed.getItem() == Items.wheat_seeds) {
			return Blocks.wheat;
		}
		return null;
	}

	
	Class<?> mPamItemRegistry;
	Class<?> mPamBlockRegistry;

	private synchronized Block getBlockFromPams(ItemStack seed) {
		try {
			return getBlockFromPamsInternal(seed);
		}
		catch (Throwable t) {}
		return null;
	}
	
	private synchronized Block getBlockFromPamsInternal(ItemStack seed) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {	
		
		if (!LoadedMods.PamsHarvestcraft) {
			return null;
		}
		if (mPamItemRegistry == null) {
			mPamItemRegistry = Class.forName("com.pam.harvestcraft.mPamItemRegistry");
		}
		if (mPamBlockRegistry == null) {
			mPamBlockRegistry = Class.forName("com.pam.harvestcraft.mPamBlockRegistry");
		}
		
		if (mPamItemRegistry == null | mPamBlockRegistry == null) {
			return null;
		}
		

		if (seed == mPamItemRegistry.getDeclaredField("blackberryseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamblackberryCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("blueberryseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamblueberryCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("candleberryseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcandleberryCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("raspberryseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamraspberryCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("strawberryseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamstrawberryCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("grapeseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamgrapeCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("cactusfruitseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcactusfruitCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("asparagusseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamasparagusCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("barleyseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pambarleyCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("oatsseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamoatsCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("ryeseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamryeCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("cornseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcornCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("bambooshootseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pambambooshootCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("cantaloupeseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcantaloupeCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("cucumberseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcucumberCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("wintersquashseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamwintersquashCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("zucchiniseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamzucchiniCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("beetseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pambeetCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("onionseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamonionCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("parsnipseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamparsnipCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("peanutseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pampeanutCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("radishseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamradishCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("rutabagaseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamrutabagaCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("sweetpotatoseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamsweetpotatoCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("turnipseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamturnipCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("rhubarbseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamrhubarbCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("celeryseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamceleryCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("garlicseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamgarlicCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("gingerseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamgingerCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("spiceleafseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamspiceleafCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("tealeafseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamteaCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("coffeebeanseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcoffeeCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("mustardseedsseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pammustardCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("broccoliseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pambroccoliCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("cauliflowerseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcauliflowerCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("leekseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamleekCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("lettuceseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamlettuceCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("scallionseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamscallionCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("artichokeseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamartichokeCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("brusselsproutseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pambrusselsproutCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("cabbageseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcabbageCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("spinachseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamspinachCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("whitemushroomseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamwhitemushroomCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("beanseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pambeanCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("soybeanseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamsoybeanCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("bellpepperseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pambellpepperCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("chilipepperseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamchilipepperCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("eggplantseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pameggplantCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("okraseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamokraCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("peasseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pampeasCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("tomatoseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamtomatoCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("cottonseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcottonCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("pineappleseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pampineappleCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("kiwiseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamkiwiCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("curryleafseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamcurryleafCrop").get(null);
		}
		if (seed == mPamItemRegistry.getDeclaredField("sesameseedsseedItem").get(null)) {
			return (Block) mPamBlockRegistry.getDeclaredField("pamsesameseedsCrop").get(null);
		}
		return null;
	}

	public boolean isReadyToHarvest(final int x, final int y, final int z) {
		final Block block = this.getCart().worldObj.getBlock(x, y, z);
		final int m = this.getCart().worldObj.getBlockMetadata(x, y, z);
		return block instanceof BlockCrops && m == 7;
	}

}