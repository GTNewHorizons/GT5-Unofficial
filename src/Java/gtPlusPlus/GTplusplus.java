package gtPlusPlus;

import static gtPlusPlus.core.lib.CORE.ConfigSwitches.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Timer;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.launchwrapper.Launch;

import gregtech.api.enums.Materials;
import gregtech.api.util.FishPondFakeRecipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.api.objects.minecraft.ChunkManager;
import gtPlusPlus.core.commands.CommandMath;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.config.ConfigHandler;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.handler.analytics.SegmentAnalytics;
import gtPlusPlus.core.handler.analytics.SegmentHelper;
import gtPlusPlus.core.handler.events.BlockEventHandler;
import gtPlusPlus.core.handler.events.LoginEventHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.LocaleUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.core.util.sys.GeoUtils;
import gtPlusPlus.core.util.sys.NetworkUtils;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import gtPlusPlus.xmod.gregtech.loaders.GT_Material_Loader;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_BlastSmelterGT_GTNH;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Recycling;
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

	// Loads Textures
	@SideOnly(value = Side.CLIENT)
	public static void loadTextures() {
		Logger.INFO("Loading some textures on the client.");
		// Tools
		Logger.WARNING("Processing texture: " + TexturesGtTools.SKOOKUM_CHOOCHER.getTextureFile().getResourcePath());

		// Blocks
		Logger.WARNING("Processing texture: " + TexturesGtBlock.Casing_Machine_Dimensional.getTextureFile().getResourcePath());
	}

	// Pre-Init
	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Logger.INFO("Loading " + CORE.name + " V" + CORE.VERSION);


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

		// FirstCall();
		Utils.registerEvent(new LoginEventHandler());
		Logger.INFO("Login Handler Initialized");



		proxy.preInit(event);
		Core_Manager.preInit();
	}

	// Init
	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
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
		proxy.postInit(event);
		BookHandler.runLater();
		Core_Manager.postInit();
		RecipeGen_Recycling.executeGenerators();	

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
	public void serverStarting(final FMLServerStartingEvent event) {

		if (CORE.DEVENV)
			event.registerServerCommand(new CommandMath());

		//Chunk Loading
		Timer h = ChunkManager.createChunkQueue();


	}

	@Mod.EventHandler
	public void serverStopping(final FMLServerStoppingEvent event) {
		//Flush all data to Server at the end of the day.
		if (SegmentAnalytics.sAnalyticsMasterList.size() > 0){
			for (SegmentAnalytics sa : SegmentAnalytics.sAnalyticsMasterList.values()){
				sa.flushDataFinal();
				SegmentAnalytics.LOG("Cleaned up Analytics Data for player "+sa.mLocalName+".");
			}
		}

		//Chunkload Handler
		if (ChunkManager.mChunkLoaderManagerMap.size() > 0) {
			Logger.INFO("Clearing Chunk Loaders.");
			ChunkManager.mChunkLoaderManagerMap.clear();
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
		RecipeGen_BlastSmelterGT_GTNH.generateGTNHBlastSmelterRecipesFromEBFList();
		FishPondFakeRecipe.generateFishPondRecipes();

		//Large Centrifuge generation
		for (GT_Recipe x : GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes.mRecipeList) {
			if (x != null) {
				CORE.RA.addMultiblockCentrifugeRecipe(x.mInputs, x.mFluidInputs, x.mFluidOutputs, x.mOutputs, x.mDuration, x.mEUt);
			}
		}

		//Large Electrolyzer generation
		for (GT_Recipe x : GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes.mRecipeList) {
			if (x != null) {
				CORE.RA.addMultiblockElectrolyzerRecipe(x.mInputs, x.mFluidInputs, x.mFluidOutputs, x.mOutputs, x.mDuration, x.mEUt);
			}
		}

		//Advanced Vacuum Freezer generation
		for (GT_Recipe x : GT_Recipe.GT_Recipe_Map.sVacuumRecipes.mRecipeList) {
			if (x != null && RecipeUtils.doesGregtechRecipeHaveEqualCells(x)) {			
				CORE.RA.addAdvancedFreezerRecipe(x.mInputs, x.mFluidInputs, x.mFluidOutputs, x.mOutputs, (x.mDuration/2), x.mEUt);
			}
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


	private static final void initAnalytics(){
		SegmentAnalytics.isEnabled = CORE.ConfigSwitches.enableUpdateChecker;
		if (!Utils.isServer() && PlayerUtils.isPlayerAlkalus()){
			SegmentAnalytics.isEnabled = true;
		}		
		new SegmentHelper();
	}

	private static final boolean setupMaterialBlacklist(){		
		int ID = 0;
		Material.invalidMaterials.put(ID++, Materials._NULL);
		Material.invalidMaterials.put(ID++, Materials.Clay);
		Material.invalidMaterials.put(ID++, Materials.Phosphorus);
		Material.invalidMaterials.put(ID++, Materials.Steel);
		Material.invalidMaterials.put(ID++, Materials.Bronze);
		Material.invalidMaterials.put(ID++, Materials.Hydrogen);
		//Infused TC stuff
		Material.invalidMaterials.put(ID++, Materials.InfusedAir);	
		Material.invalidMaterials.put(ID++, Materials.InfusedEarth);	
		Material.invalidMaterials.put(ID++, Materials.InfusedFire);	
		Material.invalidMaterials.put(ID++, Materials.InfusedWater);

		//EIO Materials
		Material.invalidMaterials.put(ID++, Materials.SoulSand);
		Material.invalidMaterials.put(ID++, Materials.EnderPearl);
		Material.invalidMaterials.put(ID++, Materials.EnderEye);
		Material.invalidMaterials.put(ID++, Materials.Redstone);
		Material.invalidMaterials.put(ID++, Materials.Glowstone);
		Material.invalidMaterials.put(ID++, Materials.Soularium);
		Material.invalidMaterials.put(ID++, Materials.PhasedIron);

		if (Material.invalidMaterials.size() > 0){
			return true;
		}
		return false;

	}

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
		mOrangeCapes.put(new Pair<String, String>("asturrial", "26c4881f-c708-4c5d-aa76-4419c3a1265b"));
		mOrangeCapes.put(new Pair<String, String>("netmc", "c3ecbcc3-0d83-4da6-bb89-69f3f1a6e38b"));
		mOrangeCapes.put(new Pair<String, String>("twinsrock8", "c1239b45b-b3a3-4282-8143-c73778897dda"));
		mOrangeCapes.put(new Pair<String, String>("Ajes", "b1781fc7-35ca-4255-a21c-cdb1b7ea1853"));
		mOrangeCapes.put(new Pair<String, String>("Piky", "7822ae35-9d5a-4fe7-bd5f-d03006932a65"));
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
		
		//Dev Capes
		mDevCapes.put(new Pair<String, String>("draknyte1", "5652713c-668e-47f3-853a-3fa959a9dfd3"));
		mDevCapes.put(new Pair<String, String>("crimsonhood17", "c4773470-2585-4bd7-82b3-8764ca6acd08"));

		
		/**
		 * Patreons
		 */
		
		mPatreonCapes.put(new Pair<String, String>("Baxterzz", "e8aa5500-7319-4453-822c-b96b29ab5981"));
		mPatreonCapes.put(new Pair<String, String>("leagris", "09752aa3-8b9c-4f8f-b04f-5421e799547d"));
		mPatreonCapes.put(new Pair<String, String>("Traumeister", "fd3f46ac-801a-4566-90b5-75cb362d261e"));
	}

}
