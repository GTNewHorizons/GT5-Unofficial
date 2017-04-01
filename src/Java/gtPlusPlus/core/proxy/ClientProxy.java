package gtPlusPlus.core.proxy;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.client.renderer.RenderMiningExplosivesPrimed;
import gtPlusPlus.core.common.CommonProxy;
import gtPlusPlus.core.common.compat.COMPAT_PlayerAPI;
import gtPlusPlus.core.entity.EntityPrimedMiningExplosive;
import gtPlusPlus.core.handler.render.FirepitRender;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.tileentities.general.TileEntityFirepit;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.particles.EntityParticleFXMysterious;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;

public class ClientProxy extends CommonProxy{

	/*private final HashSet<String> mCapeList = new HashSet<String>();
	private final CapeHandler mCapeRenderer;

	ClientProxy(){
		mCapeRenderer = new CapeHandler(mCapeList);
	}
	 */

	@SideOnly(Side.CLIENT)
	public static String playerName = "";

	@Override
	public void preInit(final FMLPreInitializationEvent e) {
		super.preInit(e);
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

		super.init(e);
	}

	@Override
	public void postInit(final FMLPostInitializationEvent e) {
		super.postInit(e);
	}

	@Override
	public void registerRenderThings(){
		//MinecraftForgeClient.registerItemRenderer(ModItems.FluidCell.getItem(), new RenderLiquidCell());
		//RenderingRegistry.registerEntityRenderingHandler(EntityBloodSteelMob.class, new RenderBloodSteelMob(new ModelBloodSteelMob(), 0));
		//RenderingRegistry.registerEntityRenderingHandler(EntityBloodSteelHostileMob.class, new RenderBloodSteelMobHostile(new ModelBloodSteelMob(), 0));
		//RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, new RenderSnowball(ModItems.tutGrenade));		
		Utils.LOG_INFO("Registering Custom Renderer for Mining Explosives.");
	    RenderingRegistry.registerEntityRenderingHandler(EntityPrimedMiningExplosive.class, new RenderMiningExplosivesPrimed());

		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBloodSteelChest.class, new BloodSteelChestRenderer());
		//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.tutChest), new ItemRenderBloodSteelChest());
	    Utils.LOG_INFO("Registering Custom Renderer for the Fire Pit.");
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFirepit.class, new FirepitRender());
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

	/*@SubscribeEvent
    public void receiveRenderSpecialsEvent(net.minecraftforge.client.event.RenderPlayerEvent.Specials.Pre aEvent) {
        mCapeRenderer.receiveRenderSpecialsEvent(aEvent);
    }*/

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



}
