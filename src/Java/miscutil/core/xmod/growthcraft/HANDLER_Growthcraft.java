package miscutil.core.xmod.growthcraft;

import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.cellar.item.ItemBoozeBottle;
import growthcraft.cellar.item.ItemBoozeBucketDEPRECATED;
import growthcraft.cellar.item.ItemBucketBooze;
import growthcraft.cellar.utils.BoozeRegistryHelper;
import growthcraft.core.GrowthCraftCore;
import growthcraft.hops.GrowthCraftHops;
import miscutil.core.lib.LoadedMods;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class HANDLER_Growthcraft {
	
	  public static BlockFluidBooze[] jackDanielsWhiskeyFluids;
	  public static Item jackDaniels;
	  public static Item jackDanielsSeeds;
	  public static Item jackDanielsWhiskey;
	  public static Item jackDanielsWhiskeyBucket_deprecated;
	  public static ItemBucketBooze[] jackDanielsWhiskeyBuckets;
	  public static Fluid[] jackDanielsWhiskeyBooze;
	  //private static int internalColour = FluidRegistry.getFluid("fluidJackDaniels").getColor();
	  private static int internalColour = 0000000;

	  //Run me during Pre-Init
	public static void preInit(){
		if (LoadedMods.Growthcraft || GrowthCraftCore.instance != null){
			start();			
		}
	}
	
	private static void start(){
		jackDaniels = GrowthCraftHops.hops;
	    jackDanielsSeeds = GrowthCraftHops.hopSeeds;
	    
	    jackDanielsWhiskeyBooze = new Booze[5];
	    jackDanielsWhiskeyFluids = new BlockFluidBooze[jackDanielsWhiskeyBooze.length];
	    jackDanielsWhiskeyBuckets = new ItemBucketBooze[jackDanielsWhiskeyBooze.length];
	   BoozeRegistryHelper.initializeBooze(jackDanielsWhiskeyBooze, jackDanielsWhiskeyFluids, jackDanielsWhiskeyBuckets, "grc.jackDanielsWhiskey", internalColour);
	    
	    jackDanielsWhiskey = new ItemBoozeBottle(6, -0.5F, jackDanielsWhiskeyBooze).setColor(internalColour).setTipsy(0.7F, 900).setPotionEffects(new int[] { Potion.digSpeed.id }, new int[] { 3600 });
	   jackDanielsWhiskeyBucket_deprecated = new ItemBoozeBucketDEPRECATED(jackDanielsWhiskeyBooze).setColor(internalColour);
	    
	    
	    //GameRegistry.registerItem(jackDaniels, "grc.jackDaniels");
	    //GameRegistry.registerItem(jackDanielsSeeds, "grc.jackDanielsSeeds");
	    GameRegistry.registerItem(jackDanielsWhiskey, "grc.jackDanielsWhiskey");
	    GameRegistry.registerItem(jackDanielsWhiskeyBucket_deprecated, "grc.jackDanielsWhiskey_bucket");
	    
	    BoozeRegistryHelper.registerBooze(jackDanielsWhiskeyBooze, jackDanielsWhiskeyFluids, jackDanielsWhiskeyBuckets, jackDanielsWhiskey, "grc.jackDanielsWhiskey", jackDanielsWhiskeyBucket_deprecated);
	    
	    CellarRegistry.instance().brew().addBrewing(FluidRegistry.WATER, Items.wheat, jackDanielsWhiskeyBooze[4], 200, 60, 0.4F);
	    CellarRegistry.instance().brew().addBrewing(jackDanielsWhiskeyBooze[4], jackDaniels, jackDanielsWhiskeyBooze[0], 350, 60, 0.1F);
	    
	    
	}
	
}
