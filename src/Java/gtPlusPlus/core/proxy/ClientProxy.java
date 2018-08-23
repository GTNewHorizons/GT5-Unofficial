package gtPlusPlus.core.proxy;

import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.australia.entity.model.ModelBoar;
import gtPlusPlus.australia.entity.model.ModelDingo;
import gtPlusPlus.australia.entity.model.ModelOctopus;
import gtPlusPlus.australia.entity.render.RenderAustralianSpider;
import gtPlusPlus.australia.entity.render.RenderBoar;
import gtPlusPlus.australia.entity.render.RenderDingo;
import gtPlusPlus.australia.entity.render.RenderOctopus;
import gtPlusPlus.australia.entity.type.EntityAustralianSpiderBase;
import gtPlusPlus.australia.entity.type.EntityBoar;
import gtPlusPlus.australia.entity.type.EntityDingo;
import gtPlusPlus.australia.entity.type.EntityOctopus;
import gtPlusPlus.core.client.model.ModelGiantChicken;
import gtPlusPlus.core.client.renderer.CustomOreBlockRenderer;
import gtPlusPlus.core.client.renderer.RenderGiantChicken;
import gtPlusPlus.core.client.renderer.RenderMiningExplosivesPrimed;
import gtPlusPlus.core.client.renderer.RenderPlasmaBolt;
import gtPlusPlus.core.client.renderer.RenderSickBlaze;
import gtPlusPlus.core.client.renderer.RenderStaballoyConstruct;
import gtPlusPlus.core.client.renderer.RenderToxinball;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.common.compat.COMPAT_PlayerAPI;
import gtPlusPlus.core.entity.EntityPrimedMiningExplosive;
import gtPlusPlus.core.entity.EntityTeslaTowerLightning;
import gtPlusPlus.core.entity.monster.EntityGiantChickenBase;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.entity.projectile.EntityHydrofluoricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntitySulfuricAcidPotion;
import gtPlusPlus.core.entity.projectile.EntityToxinballSmall;
import gtPlusPlus.core.handler.render.FirepitRender;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.tileentities.general.TileEntityFirepit;
import gtPlusPlus.core.util.minecraft.particles.EntityParticleFXMysterious;
import gtPlusPlus.xmod.gregtech.common.render.GTPP_CapeRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;

public class ClientProxy extends CommonProxy implements Runnable{

	private final GTPP_CapeRenderer mCapeRenderer;
	@SideOnly(Side.CLIENT)
	public static boolean mFancyGraphics = false;

	public ClientProxy(){
		mCapeRenderer = new GTPP_CapeRenderer();
		//Get Graphics Mode.
		mFancyGraphics = Minecraft.isFancyGraphicsEnabled();
	}

	@SubscribeEvent
	public void receiveRenderSpecialsEvent(net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre aEvent) {
		if (ConfigSwitches.enableCustomCapes){
			mCapeRenderer.receiveRenderSpecialsEvent(aEvent);
		}
	}

	@SideOnly(Side.CLIENT)
	public static String playerName = "";

	@Override
	public void preInit(final FMLPreInitializationEvent e) {
		super.preInit(e);
		if (ConfigSwitches.enableCustomCapes){
			onPreLoad();			
		}
		//Do this weird things for textures.
		GTplusplus.loadTextures();
		//We boot up the sneak manager.
		if (LoadedMods.PlayerAPI){
			this.init_PlayerAPI_PRE();
		}
	}

	@Override
	public void init(final FMLInitializationEvent e) {
		if (LoadedMods.PlayerAPI){
			this.init_PlayerAPI_INIT();
		}

		/**
		 * Custom Block Renderers
		 */
		new CustomOreBlockRenderer();

		super.init(e);
	}

	@Override
	public void postInit(final FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	public void registerRenderThings(){

		// Standard GT++

		/**
		 * Entities
		 */

		RenderingRegistry.registerEntityRenderingHandler(EntityPrimedMiningExplosive.class, new RenderMiningExplosivesPrimed());
		RenderingRegistry.registerEntityRenderingHandler(EntitySickBlaze.class, new RenderSickBlaze());
		RenderingRegistry.registerEntityRenderingHandler(EntityStaballoyConstruct.class, new RenderStaballoyConstruct());
		RenderingRegistry.registerEntityRenderingHandler(EntityToxinballSmall.class, new RenderToxinball(1F));
		RenderingRegistry.registerEntityRenderingHandler(EntitySulfuricAcidPotion.class, new RenderSnowball(ModItems.itemSulfuricPotion));
		RenderingRegistry.registerEntityRenderingHandler(EntityHydrofluoricAcidPotion.class, new RenderSnowball(ModItems.itemHydrofluoricPotion));
		RenderingRegistry.registerEntityRenderingHandler(EntityTeslaTowerLightning.class, new RenderPlasmaBolt());
		RenderingRegistry.registerEntityRenderingHandler(EntityGiantChickenBase.class, new RenderGiantChicken(new ModelGiantChicken(), 1f));

		/**
		 * Tiles
		 */

		Logger.INFO("Registering Custom Renderer for the Fire Pit.");
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFirepit.class, new FirepitRender());


		//GT++ Australia

		/**
		 * Entities
		 */

		RenderingRegistry.registerEntityRenderingHandler(EntityAustralianSpiderBase.class, new RenderAustralianSpider());
		RenderingRegistry.registerEntityRenderingHandler(EntityBoar.class, new RenderBoar(new ModelBoar(), new ModelBoar(0.5F), 0.7F));
		RenderingRegistry.registerEntityRenderingHandler(EntityDingo.class, new RenderDingo(new ModelDingo(), new ModelDingo(), 0.5F));
		RenderingRegistry.registerEntityRenderingHandler(EntityOctopus.class, new RenderOctopus(new ModelOctopus(), 0.7F));
	}

	@Override
	public int addArmor(final String armor){
		return RenderingRegistry.addNewArmourRendererPrefix(armor);
	}

	@Override
	public void generateMysteriousParticles(final Entity theEntity)
	{
		final double motionX = theEntity.worldObj.rand.nextGaussian() * 0.02D;
		final double motionY = theEntity.worldObj.rand.nextGaussian() * 0.02D;
		final double motionZ = theEntity.worldObj.rand.nextGaussian() * 0.02D;
		final EntityFX particleMysterious = new EntityParticleFXMysterious(

				theEntity.worldObj,
				(theEntity.posX + (theEntity.worldObj.rand.nextFloat() * theEntity.width

						* 2.0F)) - theEntity.width,
				theEntity.posY + 0.5D + (theEntity.worldObj.rand.nextFloat()

						* theEntity.height),
				(theEntity.posZ + (theEntity.worldObj.rand.nextFloat() * theEntity.width

						* 2.0F)) - theEntity.width,

				motionX,

				motionY,

				motionZ);
		Minecraft.getMinecraft().effectRenderer.addEffect(particleMysterious);
	}

	@Override
	public void serverStarting(final FMLServerStartingEvent e)
	{

	}

	@Optional.Method(modid = "PlayerAPI")
	private void init_PlayerAPI_PRE(){
		//Register player instance
		COMPAT_PlayerAPI.clientProxy.initPre();
	}

	@Optional.Method(modid = "PlayerAPI")
	private void init_PlayerAPI_INIT(){
		//Register player instance
		COMPAT_PlayerAPI.clientProxy.Init();
	}



	public void onPreLoad() {
		/*if (ConfigSwitches.enableCustomCapes){
			String arr$[] = {
					"draknyte1", "fobius"
			};
			int len$ = arr$.length;
			for (int i$ = 0; i$ < len$; i$++) {
				String tName = arr$[i$];
				mCapeList.add(tName.toLowerCase());
			}
			(new Thread(this)).start();
		}*/
	}

	@Override
	public void run() {
		/*try {
			if (ConfigSwitches.enableCustomCapes){
				Logger.INFO("GT++ Mod: Downloading Cape List.");
				@SuppressWarnings("resource")
				Scanner tScanner = new Scanner(new URL("https://github.com/draknyte1/GTplusplus/blob/master/SupporterList.txt").openStream());
				while (tScanner.hasNextLine()) {
					String tName = tScanner.nextLine();
					if (!this.mCapeList.contains(tName.toLowerCase())) {
						this.mCapeList.add(tName.toLowerCase());
					}
				}
			}
		} catch (Throwable e) {
			Logger.INFO("Failed to download GT++ cape list.");
		}*/
	}

	@Override
	public void onLoadComplete(FMLLoadCompleteEvent event) {
		GTplusplus.tryPatchTurbineTextures();
		super.onLoadComplete(event);
	}

}
