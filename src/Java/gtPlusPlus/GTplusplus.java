package gtPlusPlus;

import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableAnimatedTurbines;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableCustomCapes;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.enableUpdateChecker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.util.FishPondFakeRecipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.Recipe_GT;
import gregtech.api.util.SemiFluidFuelHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.commands.CommandDebugChunks;
import gtPlusPlus.core.commands.CommandMath;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.config.ConfigHandler;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.handler.chunkloading.ChunkLoading;
import gtPlusPlus.core.handler.events.BlockEventHandler;
import gtPlusPlus.core.handler.events.LoginEventHandler;
import gtPlusPlus.core.handler.events.MissingMappingsEvent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.LocaleUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.core.util.sys.GeoUtils;
import gtPlusPlus.core.util.sys.NetworkUtils;
import gtPlusPlus.core.util.sys.SystemUtils;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.xmod.gregtech.api.objects.GregtechBufferThread;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import gtPlusPlus.xmod.gregtech.loaders.GT_Material_Loader;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_BlastSmelterGT_GTNH;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechMiniRaFusion;
import gtPlusPlus.xmod.thaumcraft.commands.CommandDumpAspects;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

@MCVersion(value = "1.7.10")
@Mod(modid = CORE.MODID, name = CORE.name, version = CORE.VERSION, dependencies = "required-after:Forge; after:TConstruct; after:PlayerAPI; after:dreamcraft; after:IC2; after:ihl; after:psychedelicraft; after:gregtech; after:Forestry; after:MagicBees; after:CoFHCore; after:Growthcraft; after:Railcraft; after:CompactWindmills; after:ForbiddenMagic; after:MorePlanet; after:PneumaticCraft; after:ExtraUtilities; after:Thaumcraft; after:rftools; after:simplyjetpacks; after:BigReactors; after:EnderIO; after:tectech; after:GTRedtech; after:beyondrealitycore; after:OpenBlocks; after:IC2NuclearControl; after:TGregworks; after:StevesCarts;")
public class GTplusplus implements ActionListener {

	public static enum INIT_PHASE {
		SUPER(null),
		PRE_INIT(SUPER),
		INIT(PRE_INIT),
		POST_INIT(INIT),
		SERVER_START(POST_INIT),
		STARTED(SERVER_START);		
		protected boolean mIsPhaseActive = false;
		private final INIT_PHASE mPrev;
		
		private INIT_PHASE(INIT_PHASE aPreviousPhase) {
			mPrev = aPreviousPhase;
		}
		
		public synchronized final boolean isPhaseActive() {			
			return mIsPhaseActive;
		}
		public synchronized final void setPhaseActive(boolean aIsPhaseActive) {
			if (mPrev != null && mPrev.isPhaseActive()) {
				mPrev.setPhaseActive(false);
			}
			mIsPhaseActive = aIsPhaseActive;
			if (CURRENT_LOAD_PHASE != this) {
				CURRENT_LOAD_PHASE = this;
			}
		}
	}
	
	public static INIT_PHASE CURRENT_LOAD_PHASE = INIT_PHASE.SUPER;
	
	//Mod Instance
	@Mod.Instance(CORE.MODID)
	public static GTplusplus instance;
	public static Meta_GT_Proxy instanceGtProxy;

	//Material Loader
	public static GT_Material_Loader mGregMatLoader;

	//GT_Proxy instance
	protected static Meta_GT_Proxy mGregProxy;

	//GT++ Proxy Instances
	@SidedProxy(clientSide = "gtPlusPlus.core.proxy.ClientProxy", serverSide = "gtPlusPlus.core.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	//Chunk handler
	public static ChunkLoading mChunkLoading;

	// Loads Textures
	@SideOnly(value = Side.CLIENT)
	public static void loadTextures() {
		Logger.INFO("Loading some textures on the client.");
		// Tools
		Logger.WARNING("Processing texture: " + TexturesGtTools.SKOOKUM_CHOOCHER.getTextureFile().getResourcePath());

		// Blocks
		Logger.WARNING("Processing texture: " + TexturesGtBlock.Casing_Machine_Dimensional.getTextureFile().getResourcePath());
	}
	
	public GTplusplus() {
		super();
		INIT_PHASE.SUPER.setPhaseActive(true);
		mChunkLoading = new ChunkLoading();
	}

	// Pre-Init
	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		INIT_PHASE.PRE_INIT.setPhaseActive(true);
		Logger.INFO("Loading " + CORE.name + " "+CORE.VERSION+" on Gregtech "+Utils.getGregtechVersionAsString());
		//Load all class objects within the plugin package.
		Core_Manager.veryEarlyInit();

		if(!Utils.isServer()){
			enableCustomCapes = true;
		}

		//Give this a go mate.
		//initAnalytics();
		setupMaterialBlacklist();
		//setupMaterialWhitelist();

		//HTTP Requests
		CORE.MASTER_VERSION = NetworkUtils.getContentFromURL("https://raw.githubusercontent.com/draknyte1/GTplusplus/master/Recommended.txt").toLowerCase();
		CORE.USER_COUNTRY = GeoUtils.determineUsersCountry();

		// Handle GT++ Config
		ConfigHandler.handleConfigFile(event);		

		//Check for Dev
		CORE.DEVENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		if (enableUpdateChecker){
			Logger.INFO("Latest is " + CORE.MASTER_VERSION + ". Updated? " + Utils.isModUpToDate());
		}
		//Utils.LOG_INFO("User's Country: " + CORE.USER_COUNTRY);

		Utils.registerEvent(new LoginEventHandler());
		Utils.registerEvent(new MissingMappingsEvent());
		Logger.INFO("Login Handler Initialized");



		mChunkLoading.preInit(event);
		proxy.preInit(event);
		Logger.INFO("Setting up our own GT_Proxy.");
		instanceGtProxy = Meta_GT_Proxy.instance;
		instanceGtProxy.preInit();
		Core_Manager.preInit();
	}

	// Init
	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		INIT_PHASE.INIT.setPhaseActive(true);
		mChunkLoading.init(event);
		proxy.init(event);
		proxy.registerNetworkStuff();
		instanceGtProxy.init();
		Core_Manager.init();

		//Used by foreign players to generate .lang files for translation.
		if (CORE.ConfigSwitches.dumpItemAndBlockData) {
			LocaleUtils.generateFakeLocaleFile();
		}

	}

	// Post-Init
	@Mod.EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		INIT_PHASE.POST_INIT.setPhaseActive(true);
		mChunkLoading.postInit(event);
		proxy.postInit(event);
		BookHandler.runLater();
		instanceGtProxy.postInit();
		Core_Manager.postInit();
		//SprinklerHandler.registerModFerts();

		//Set Variables for Fluorite Block handling
		Logger.INFO("Setting some Variables for the block break event handler.");
		BlockEventHandler.oreLimestone = OreDictionary.getOres("oreLimestone");
		BlockEventHandler.blockLimestone = OreDictionary.getOres("limestone");
		BlockEventHandler.fluoriteOre = FLUORIDES.FLUORITE.getOre(1);

		Logger.INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.INFO("| Recipes succesfully Loaded: " + RegistrationHandler.recipesSuccess + " | Failed: "
				+ RegistrationHandler.recipesFailed + " |");
		Logger.INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.INFO("Finally, we are finished. Have some cripsy bacon as a reward.");
	}

	@EventHandler
	public synchronized void serverStarting(final FMLServerStartingEvent event) {
		INIT_PHASE.SERVER_START.setPhaseActive(true);
		mChunkLoading.serverStarting(event);
		event.registerServerCommand(new CommandMath());
		event.registerServerCommand(new CommandDebugChunks());
		if (LoadedMods.Thaumcraft) {
			event.registerServerCommand(new CommandDumpAspects());
		}
		INIT_PHASE.STARTED.setPhaseActive(true);
	}

	@Mod.EventHandler
	public synchronized void serverStopping(final FMLServerStoppingEvent event) {
		mChunkLoading.serverStopping(event);
		if (GregtechBufferThread.mBufferThreadAllocation.size() > 0) {
			for (GregtechBufferThread i : GregtechBufferThread.mBufferThreadAllocation.values()) {
				i.destroy();
			}
			SystemUtils.invokeGC();
		}
		
	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {

	}


	/**
	 * This {@link EventHandler} is called after the {@link FMLPostInitializationEvent} stages of all loaded mods executes successfully.
	 * {@link #onLoadComplete(FMLLoadCompleteEvent)} exists to inject recipe generation after Gregtech and all other mods are entirely loaded and initialized.
	 * @param event - The {@link EventHandler} object passed through from FML to {@link #GTplusplus()}'s {@link #instance}.
	 */
	@Mod.EventHandler
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		proxy.onLoadComplete(event);
		generateGregtechRecipeMaps();
	}

	public static void tryPatchTurbineTextures() {
		if (enableAnimatedTurbines) {
			BlockIcons h = Textures.BlockIcons.GAS_TURBINE_SIDE_ACTIVE;
			BlockIcons h2 = Textures.BlockIcons.STEAM_TURBINE_SIDE_ACTIVE;
			try {		
				Logger.INFO("Trying to patch GT textures to make Turbines animated.");
				IIcon aIcon = TexturesGtBlock.Overlay_Machine_Turbine_Active.getIcon();
				if (ReflectionUtils.setField(h, "mIcon", aIcon)) {
					Logger.INFO("Patched Gas Turbine Icon.");
				}
				if (ReflectionUtils.setField(h2, "mIcon", aIcon)) {
					Logger.INFO("Patched Steam Turbine Icon.");
				}
			}
			catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void generateGregtechRecipeMaps() {

		int[] mValidCount = new int[] {0, 0, 0};
		int[] mInvalidCount = new int[] {0, 0, 0};
		int[] mOriginalCount = new int[] {0, 0, 0};

		RecipeGen_BlastSmelterGT_GTNH.generateGTNHBlastSmelterRecipesFromEBFList();
		FishPondFakeRecipe.generateFishPondRecipes();	
		GregtechMiniRaFusion.generateSlowFusionrecipes();
		SemiFluidFuelHandler.generateFuels();

		//Large Centrifuge generation
		mOriginalCount[0] = GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList.size();
		for (GT_Recipe x : GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList) {
			if (x != null) {
				if (ItemUtils.checkForInvalidItems(x.mInputs, x.mOutputs)) {
					if (CORE.RA.addMultiblockCentrifugeRecipe(x.mInputs, x.mFluidInputs, x.mFluidOutputs, x.mOutputs, x.mChances, x.mDuration, x.mEUt, x.mSpecialValue)) {
						mValidCount[0]++;
					}
					else {
						mInvalidCount[0]++;
					}
				}
				else {
					Logger.INFO("[Recipe] Error generating Large Centrifuge recipe.");
					Logger.INFO("Inputs: "+ItemUtils.getArrayStackNames(x.mInputs));
					Logger.INFO("Fluid Inputs: "+ItemUtils.getArrayStackNames(x.mFluidInputs));
					Logger.INFO("Outputs: "+ItemUtils.getArrayStackNames(x.mOutputs));
					Logger.INFO("Fluid Outputs: "+ItemUtils.getArrayStackNames(x.mFluidOutputs));
				}
			}
			else {
				mInvalidCount[0]++;
			}
		}

		if (Recipe_GT.Gregtech_Recipe_Map.sMultiblockCentrifugeRecipes_GT.mRecipeList.size() < 1) {
			for (GT_Recipe a : Recipe_GT.Gregtech_Recipe_Map.sMultiblockCentrifugeRecipes.mRecipeList) {
				Recipe_GT.Gregtech_Recipe_Map.sMultiblockCentrifugeRecipes_GT.add(a);
			}
		}
		
		//Large Electrolyzer generation
		mOriginalCount[1] = GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList.size();
		for (GT_Recipe x : GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList) {
			if (x != null) {
				if (ItemUtils.checkForInvalidItems(x.mInputs, x.mOutputs)) {
					if (CORE.RA.addMultiblockElectrolyzerRecipe(x.mInputs, x.mFluidInputs, x.mFluidOutputs, x.mOutputs, x.mChances, x.mDuration, x.mEUt, x.mSpecialValue)) {
						mValidCount[1]++;
					}
					else {
						mInvalidCount[1]++;
					}
				}
				else {
					Logger.INFO("[Recipe] Error generating Large Electrolyzer recipe.");
					Logger.INFO("Inputs: "+ItemUtils.getArrayStackNames(x.mInputs));
					Logger.INFO("Fluid Inputs: "+ItemUtils.getArrayStackNames(x.mFluidInputs));
					Logger.INFO("Outputs: "+ItemUtils.getArrayStackNames(x.mOutputs));
					Logger.INFO("Fluid Outputs: "+ItemUtils.getArrayStackNames(x.mFluidOutputs));
				}
			}
			else {
				mInvalidCount[1]++;
			}
		}
		
		if (Recipe_GT.Gregtech_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.mRecipeList.size() < 1) {
			for (GT_Recipe a : Recipe_GT.Gregtech_Recipe_Map.sMultiblockElectrolyzerRecipes.mRecipeList) {
				Recipe_GT.Gregtech_Recipe_Map.sMultiblockElectrolyzerRecipes_GT.add(a);
			}
		}

		//Advanced Vacuum Freezer generation
		mOriginalCount[2] = GT_Recipe.GT_Recipe_Map.sVacuumRecipes.mRecipeList.size();
		for (GT_Recipe x : GT_Recipe.GT_Recipe_Map.sVacuumRecipes.mRecipeList) {
			if (x != null && RecipeUtils.doesGregtechRecipeHaveEqualCells(x)) {	
				int mTime = (x.mDuration/2);
				int len = x.mFluidInputs.length;
				FluidStack[] y = new FluidStack[len + 1];
				int slot = y.length - 1;				
				int mr3 = 0;
				for (FluidStack f : x.mFluidInputs) {
					if (f != null) {
						y[mr3] = f;
					}
					mr3++;
				}
				y[slot] = FluidUtils.getFluidStack("cryotheum", mTime);				
				if (ItemUtils.checkForInvalidItems(x.mInputs, x.mOutputs)) {
					if (CORE.RA.addAdvancedFreezerRecipe(x.mInputs, y, x.mFluidOutputs, x.mOutputs, x.mChances, x.mDuration, x.mEUt, x.mSpecialValue)) {
						mValidCount[2]++;
					}
				}
				else {
					mInvalidCount[2]++;
				}
			}
			else {
				mInvalidCount[2]++;
			}
		}
		
		//Redo plasma recipes in Adv. Vac.
		//Meta_GT_Proxy.generatePlasmaRecipesForAdvVacFreezer();
		
		
		String[] machineName = new String[] {"Centrifuge", "Electrolyzer", "Vacuum Freezer"};
		for (int i=0;i<3;i++) {
			Logger.INFO("[Recipe] Generated "+mValidCount[i]+" recipes for the Industrial "+machineName[i]+". The original machine can process "+mOriginalCount[i]+" recipes, meaning "+mInvalidCount[i]+" are invalid for this Multiblock's processing in some way.");
		}
	}

	protected void dumpGtRecipeMap(final GT_Recipe_Map r) {
		final Collection<GT_Recipe> x = r.mRecipeList;
		Logger.INFO("Dumping " + r.mUnlocalizedName + " Recipes for Debug.");
		for (final GT_Recipe newBo : x) {
			Logger.INFO("========================");
			Logger.INFO("Dumping Input: " + ItemUtils.getArrayStackNames(newBo.mInputs));
			Logger.INFO("Dumping Inputs " + ItemUtils.getFluidArrayStackNames(newBo.mFluidInputs));
			Logger.INFO("Dumping Duration: " + newBo.mDuration);
			Logger.INFO("Dumping EU/t: " + newBo.mEUt);
			Logger.INFO("Dumping Output: " + ItemUtils.getArrayStackNames(newBo.mOutputs));
			Logger.INFO("Dumping Output: " + ItemUtils.getFluidArrayStackNames(newBo.mFluidOutputs));
			Logger.INFO("========================");
		}
	}

	private static final boolean setupMaterialBlacklist(){	
		Material.invalidMaterials.put(Materials._NULL);
		Material.invalidMaterials.put(Materials.Clay);
		Material.invalidMaterials.put(Materials.Phosphorus);
		Material.invalidMaterials.put(Materials.Steel);
		Material.invalidMaterials.put(Materials.Bronze);
		Material.invalidMaterials.put(Materials.Hydrogen);
		//Infused TC stuff
		Material.invalidMaterials.put(Materials.InfusedAir);	
		Material.invalidMaterials.put(Materials.InfusedEarth);	
		Material.invalidMaterials.put(Materials.InfusedFire);	
		Material.invalidMaterials.put(Materials.InfusedWater);
		//EIO Materials
		Material.invalidMaterials.put(Materials.SoulSand);
		Material.invalidMaterials.put(Materials.EnderPearl);
		Material.invalidMaterials.put(Materials.EnderEye);
		Material.invalidMaterials.put(Materials.Redstone);
		Material.invalidMaterials.put(Materials.Glowstone);
		Material.invalidMaterials.put(Materials.Soularium);
		Material.invalidMaterials.put(Materials.PhasedIron);

		if (Material.invalidMaterials.size() > 0){
			return true;
		}
		return false;

	}

	@SuppressWarnings("unused")
	private void setupMaterialWhitelist() {

		mGregMatLoader = new GT_Material_Loader();

		//Non GTNH Materials
		if (!CORE.GTNH){
			//Mithril - Random Dungeon Loot
			mGregMatLoader.enableMaterial(Materials.Mithril);			
		}	

		//Force - Alloying
		mGregMatLoader.enableMaterial(Materials.Force);		
	}

}
