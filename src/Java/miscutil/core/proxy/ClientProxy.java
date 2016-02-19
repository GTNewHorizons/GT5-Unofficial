package miscutil.core.proxy;

import miscutil.core.common.CommonProxy;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy{

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		// TODO Auto-generated method stub
		super.preInit(e);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		// TODO Auto-generated method stub
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		// TODO Auto-generated method stub
		super.postInit(e);
	}
	
	public void registerRenderThings(){
		//RenderingRegistry.registerEntityRenderingHandler(EntityBloodSteelMob.class, new RenderBloodSteelMob(new ModelBloodSteelMob(), 0));
		//RenderingRegistry.registerEntityRenderingHandler(EntityBloodSteelHostileMob.class, new RenderBloodSteelMobHostile(new ModelBloodSteelMob(), 0));
		//RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, new RenderSnowball(ModItems.tutGrenade));
		
		//ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBloodSteelChest.class, new BloodSteelChestRenderer());
		//MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.tutChest), new ItemRenderBloodSteelChest());
	}
	
	public int addArmor(String armor){
		return RenderingRegistry.addNewArmourRendererPrefix(armor);
	}

}
