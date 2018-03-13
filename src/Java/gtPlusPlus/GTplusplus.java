package gtPlusPlus;

import static gtPlusPlus.core.lib.CORE.ConfigSwitches.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
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
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

import gtPlusPlus.api.objects.Logger;
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
import gtPlusPlus.core.util.sys.GeoUtils;
import gtPlusPlus.core.util.sys.NetworkUtils;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import gtPlusPlus.xmod.gregtech.loaders.GT_Material_Loader;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Recycling;
import net.minecraftforge.oredict.OreDictionary;

@MCVersion(value = "1.7.10")
@Mod(modid = CORE.MODID, name = CORE.name, version = CORE.VERSION, dependencies = "required-after:Forge; after:TConstruct; after:PlayerAPI; after:dreamcraft; after:IC2; after:ihl; after:psychedelicraft; after:gregtech; after:Forestry; after:MagicBees; after:CoFHCore; after:Growthcraft; after:Railcraft; after:CompactWindmills; after:ForbiddenMagic; after:MorePlanet; after:PneumaticCraft; after:ExtraUtilities; after:Thaumcraft; after:rftools; after:simplyjetpacks; after:BigReactors; after:EnderIO; after:tectech; after:GTRedtech; after:beyondrealitycore; after:OpenBlocks;")
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
		initAnalytics();
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
}
