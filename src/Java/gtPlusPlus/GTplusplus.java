package gtPlusPlus;

import static gtPlusPlus.core.lib.CORE.DEBUG;
import static gtPlusPlus.core.lib.CORE.ConfigSwitches.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.Recipe_GT.Gregtech_Recipe_Map;
import gtPlusPlus.api.analytics.SegmentAnalytics;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.commands.CommandMath;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.config.ConfigHandler;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.handler.events.BlockEventHandler;
import gtPlusPlus.core.handler.events.LoginEventHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.geo.GeoUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.networking.NetworkUtils;
import gtPlusPlus.xmod.gregtech.common.Meta_GT_Proxy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtTools;
import net.minecraft.item.Item;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.oredict.OreDictionary;

@MCVersion(value = "1.7.10")
@Mod(modid = CORE.MODID, name = CORE.name, version = CORE.VERSION, dependencies = "required-after:Forge; after:PlayerAPI; after:dreamcraft; after:IC2; after:ihl; after:psychedelicraft; after:gregtech; after:Forestry; after:MagicBees; after:CoFHCore; after:Growthcraft; after:Railcraft; after:CompactWindmills; after:ForbiddenMagic; after:MorePlanet; after:PneumaticCraft; after:ExtraUtilities; after:Thaumcraft; after:rftools; after:simplyjetpacks; after:BigReactors; after:EnderIO;")
public class GTplusplus implements ActionListener {

	//Mod Instance
	@Mod.Instance(CORE.MODID)
	public static GTplusplus instance;

	//GT_Proxy instance
	protected static Meta_GT_Proxy gregtechproxy;

	//GT++ Proxy Instances
	@SidedProxy(clientSide = "gtPlusPlus.core.proxy.ClientProxy", serverSide = "gtPlusPlus.core.proxy.ServerProxy")
	public static CommonProxy proxy;

	// Loads Textures
	@SideOnly(value = Side.CLIENT)
	public static void loadTextures() {
		Utils.LOG_INFO("Loading some textures on the client.");
		// Tools
		Utils.LOG_WARNING("Processing texture: " + TexturesGtTools.SKOOKUM_CHOOCHER.getTextureFile().getResourcePath());

		// Blocks
		Utils.LOG_WARNING("Processing texture: " + TexturesGtBlock.Casing_Machine_Dimensional.getTextureFile().getResourcePath());
	}

	// Pre-Init
	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Utils.LOG_INFO("Loading " + CORE.name + " V" + CORE.VERSION);

		if(!Utils.isServer()){
			enableCustomCapes = true;
		}

		//Give this a go mate.
		initAnalytics();

		//HTTP Requests
		CORE.MASTER_VERSION = NetworkUtils.getContentFromURL("https://raw.githubusercontent.com/draknyte1/GTplusplus/master/Recommended.txt").toLowerCase();
		CORE.USER_COUNTRY = GeoUtils.determineUsersCountry();

		// Handle GT++ Config
		ConfigHandler.handleConfigFile(event);

		//Check for Dev
		CORE.DEVENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
		if (enableUpdateChecker){
			Utils.LOG_INFO("Latest is " + CORE.MASTER_VERSION + ". Updated? " + Utils.isModUpToDate());
		}
		//Utils.LOG_INFO("User's Country: " + CORE.USER_COUNTRY);

		// FirstCall();
		Utils.registerEvent(new LoginEventHandler());
		Utils.LOG_INFO("Login Handler Initialized");



		proxy.preInit(event);
	}

	// Init
	@Mod.EventHandler
	public void init(final FMLInitializationEvent event) {
		proxy.init(event);
		proxy.registerNetworkStuff();

		//Set Variables for Fluorite Block handling
		Utils.LOG_INFO("Setting some Variables for the block break event handler.");
		BlockEventHandler.oreLimestone = OreDictionary.getOres("oreLimestone");
		BlockEventHandler.blockLimestone = OreDictionary.getOres("limestone");
		BlockEventHandler.fluoriteOre = ItemUtils.getSimpleStack(Item.getItemFromBlock(ModBlocks.blockOreFluorite));

	}

	// Post-Init
	@Mod.EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		proxy.postInit(event);

		if (DEBUG) {
			this.dumpGtRecipeMap(Gregtech_Recipe_Map.sChemicalDehydratorRecipes);
			this.dumpGtRecipeMap(Gregtech_Recipe_Map.sCokeOvenRecipes);
			this.dumpGtRecipeMap(Gregtech_Recipe_Map.sMatterFab2Recipes);
			this.dumpGtRecipeMap(Gregtech_Recipe_Map.sAlloyBlastSmelterRecipes);
		}
		BookHandler.runLater();
		Utils.LOG_INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Utils.LOG_INFO("| Recipes succesfully Loaded: " + RegistrationHandler.recipesSuccess + " | Failed: "
				+ RegistrationHandler.recipesFailed + " |");
		Utils.LOG_INFO("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Utils.LOG_INFO("Finally, we are finished. Have some cripsy bacon as a reward.");
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {

	}

	@EventHandler
	public void serverStarting(final FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandMath());
	}

	@Mod.EventHandler
	public void serverStopping(final FMLServerStoppingEvent event) {

	}

	@Override
	public void actionPerformed(final ActionEvent arg0) {

	}

	protected void dumpGtRecipeMap(final GT_Recipe_Map r) {
		final Collection<GT_Recipe> x = r.mRecipeList;
		Utils.LOG_INFO("Dumping " + r.mUnlocalizedName + " Recipes for Debug.");
		for (final GT_Recipe newBo : x) {
			Utils.LOG_INFO("========================");
			Utils.LOG_INFO("Dumping Input: " + ItemUtils.getArrayStackNames(newBo.mInputs));
			Utils.LOG_INFO("Dumping Inputs " + ItemUtils.getFluidArrayStackNames(newBo.mFluidInputs));
			Utils.LOG_INFO("Dumping Duration: " + newBo.mDuration);
			Utils.LOG_INFO("Dumping EU/t: " + newBo.mEUt);
			Utils.LOG_INFO("Dumping Output: " + ItemUtils.getArrayStackNames(newBo.mOutputs));
			Utils.LOG_INFO("Dumping Output: " + ItemUtils.getFluidArrayStackNames(newBo.mFluidOutputs));
			Utils.LOG_INFO("========================");
		}
	}


	private static final void initAnalytics(){
		CORE.mAnalytics = new SegmentAnalytics();
	}
}
