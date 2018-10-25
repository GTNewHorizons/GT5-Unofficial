package gtPlusPlus;

import static gtPlusPlus.core.lib.CORE.ConfigSwitches.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.util.*;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.commands.CommandDebugChunks;
import gtPlusPlus.core.commands.CommandMath;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.config.ConfigHandler;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.handler.chunkloading.ChunkLoading;
import gtPlusPlus.core.handler.events.*;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.LocaleUtils;
import gtPlusPlus.core.util.minecraft.*;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.core.util.sys.*;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.xmod.gregtech.api.objects.GregtechBufferThread;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import gtPlusPlus.xmod.gregtech.loaders.GT_Material_Loader;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_BlastSmelterGT_GTNH;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechMiniRaFusion;
import gtPlusPlus.xmod.ob.SprinklerHandler;
import gtPlusPlus.xmod.thaumcraft.commands.CommandDumpAspects;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

@MCVersion(value = "1.7.10")
@Mod(modid = CORE.MODID, name = CORE.name, version = CORE.VERSION, dependencies = "required-after:Forge; after:TConstruct; after:PlayerAPI; after:dreamcraft; after:IC2; after:ihl; after:psychedelicraft; after:gregtech; after:Forestry; after:MagicBees; after:CoFHCore; after:Growthcraft; after:Railcraft; after:CompactWindmills; after:ForbiddenMagic; after:MorePlanet; after:PneumaticCraft; after:ExtraUtilities; after:Thaumcraft; after:rftools; after:simplyjetpacks; after:BigReactors; after:EnderIO; after:tectech; after:GTRedtech; after:beyondrealitycore; after:OpenBlocks; after:IC2NuclearControl; after:TGregworks; after:StevesCarts;")
public class GTplusplus implements ActionListener {

	//Mod Instance
	@Mod.Instance(CORE.MODID)
	public static GTplusplus instance;

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
		mChunkLoading = new ChunkLoading();
	}

	// Pre-Init
	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
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
		Core_Manager.preInit();
	}

	// Init
	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		mChunkLoading.init(event);
		proxy.init(event);
		proxy.registerNetworkStuff();

		//Set Variables for Fluorite Block handling
		Logger.INFO("Setting some Variables for the block break event handler.");
		BlockEventHandler.oreLimestone = OreDictionary.getOres("oreLimestone");
		BlockEventHandler.blockLimestone = OreDictionary.getOres("limestone");
		BlockEventHandler.fluoriteOre = FLUORIDES.FLUORITE.getOre(1);
		Core_Manager.init();

		//Used by foreign players to generate .lang files for translation.
		if (CORE.ConfigSwitches.dumpItemAndBlockData) {
			LocaleUtils.generateFakeLocaleFile();
		}

	}

	// Post-Init
	@Mod.EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		mChunkLoading.postInit(event);
		proxy.postInit(event);
		BookHandler.runLater();
		Core_Manager.postInit();
		SprinklerHandler.registerModFerts();

		Logger.INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.INFO("| Recipes succesfully Loaded: " + RegistrationHandler.recipesSuccess + " | Failed: "
				+ RegistrationHandler.recipesFailed + " |");
		Logger.INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Logger.INFO("Finally, we are finished. Have some cripsy bacon as a reward.");
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {

	}

	@EventHandler
	public synchronized void serverStarting(final FMLServerStartingEvent event) {
		mChunkLoading.serverStarting(event);
		event.registerServerCommand(new CommandMath());
		event.registerServerCommand(new CommandDebugChunks());
		if (LoadedMods.Thaumcraft) {
			event.registerServerCommand(new CommandDumpAspects());
		}
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
			}
			else {
				mInvalidCount[2]++;
			}
		}
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

	/**
	 * Capes
	 */

	public static final AutoMap<Pair<String, String>> mOrangeCapes = new AutoMap<Pair<String, String>>();
	public static final AutoMap<Pair<String, String>> mMiscCapes = new AutoMap<Pair<String, String>>();
	public static final AutoMap<Pair<String, String>> mBetaTestCapes = new AutoMap<Pair<String, String>>();
	public static final AutoMap<Pair<String, String>> mDevCapes = new AutoMap<Pair<String, String>>();
	public static final AutoMap<Pair<String, String>> mPatreonCapes = new AutoMap<Pair<String, String>>();

	public static void BuildCapeList() {
		//Basic Orange Cape (I give these away at times, just because)
		mOrangeCapes.put(new Pair<String, String>("ImmortalPharaoh7", "c8c479b2-7464-4b20-adea-b43ff1c10c53"));
		mOrangeCapes.put(new Pair<String, String>("Walmart_Employee", "7a56602b-9a67-44e3-95a5-270f887712c6"));
		mOrangeCapes.put(new Pair<String, String>("ArchonCerulean", "f773e61f-261f-41e7-a221-5dcace291ced"));
		mOrangeCapes.put(new Pair<String, String>("netmc", "c3ecbcc3-0d83-4da6-bb89-69f3f1a6e38b"));
		mOrangeCapes.put(new Pair<String, String>("twinsrock8", "c1239b45b-b3a3-4282-8143-c73778897dda"));
		mOrangeCapes.put(new Pair<String, String>("Ajes", "b1781fc7-35ca-4255-a21c-cdb1b7ea1853"));
		mOrangeCapes.put(new Pair<String, String>("LAGIdiot", "44f38ff8-aad7-49c3-acb3-d92317af9078"));
		mOrangeCapes.put(new Pair<String, String>("Snaggerr", "7e553c3b-b259-4c16-992a-c8c107401e74"));
		mOrangeCapes.put(new Pair<String, String>("Semmelx4", "651b3963-038f-4769-9f75-0eaca0c4e748"));
		//mOrangeCapes.put(new Pair<String, String>("aaaa", "1234"));
		//mOrangeCapes.put(new Pair<String, String>("aaaa", "1234"));
		//mOrangeCapes.put(new Pair<String, String>("aaaa", "1234"));

		//Misc
		mMiscCapes.put(new Pair<String, String>("doomsquirter", "3aee80ab-d982-4e6d-b8d0-7912bbd75f5d"));
		mMiscCapes.put(new Pair<String, String>("ukdunc", "17d57521-3a1e-4eb9-91e6-901a65c15e07"));
		mMiscCapes.put(new Pair<String, String>("JaidenC", "00b157e5-cd97-43a2-a080-460f550e93cd"));
		mMiscCapes.put(new Pair<String, String>("TheGiggitygoo", "9f996c78-bddc-4dec-a522-0df7267f11f3"));

		//Beta/Dev Tester Capes
		mBetaTestCapes.put(new Pair<String, String>("fobius", "ca399a5b-d1bb-46e3-af5b-5939817b5cf8"));
		mBetaTestCapes.put(new Pair<String, String>("cantankerousrex", ""));
		mBetaTestCapes.put(new Pair<String, String>("stephen_2015", "004ae3d8-ecaf-48eb-9e4e-224d42d31c78"));
		mBetaTestCapes.put(new Pair<String, String>("Dyonovan", "2f3a7dff-b1ec-4c05-8eed-63ad2a3ba73f"));
		mBetaTestCapes.put(new Pair<String, String>("Bear989Sr", "1964e3d1-6500-40e7-9ff2-e6161d41a8c2"));
		mBetaTestCapes.put(new Pair<String, String>("CrazyJ1984", "d84f9654-87ea-46a9-881f-c6aa45dd5af8"));
		mBetaTestCapes.put(new Pair<String, String>("AndreyKV", "9550c173-a8c5-4e7f-bf8d-b5ded56921ef"));
		mBetaTestCapes.put(new Pair<String, String>("Piky", "7822ae35-9d5a-4fe7-bd5f-d03006932a65"));

		//GTNH Beta Testers
		mBetaTestCapes.put(new Pair<String, String>("bartimaeusnek", "578c2d13-9358-4ae8-95e7-a30ab9f9f3c7"));
		mBetaTestCapes.put(new Pair<String, String>("Prewf", "634433ec-6256-44aa-97b3-a615be18ce23"));
		mBetaTestCapes.put(new Pair<String, String>("FallDark", "86aa136e-9b5e-45e3-8273-6684fd7c537d"));
		mBetaTestCapes.put(new Pair<String, String>("0lafe", "8b06bcf9-7a94-45f9-a01f-2fff73e7582d"));
		mBetaTestCapes.put(new Pair<String, String>("Dogehog", "499b751e-f106-41ae-8dfe-3b88a73958e0"));
		//mBetaTestCapes.put(new Pair<String, String>("cantankerousrex", ""));

		//Dev Capes
		mDevCapes.put(new Pair<String, String>("draknyte1", "5652713c-668e-47f3-853a-3fa959a9dfd3"));
		mDevCapes.put(new Pair<String, String>("crimsonhood17", "c4773470-2585-4bd7-82b3-8764ca6acd08"));


		/**
		 * Patreons
		 */

		mPatreonCapes.put(new Pair<String, String>("Baxterzz", "e8aa5500-7319-4453-822c-b96b29ab5981"));
		mPatreonCapes.put(new Pair<String, String>("leagris", "09752aa3-8b9c-4f8f-b04f-5421e799547d"));
		mPatreonCapes.put(new Pair<String, String>("Traumeister", "fd3f46ac-801a-4566-90b5-75cb362d261e"));
		mPatreonCapes.put(new Pair<String, String>("asturrial", "26c4881f-c708-4c5d-aa76-4419c3a1265b"));
	}

}
