package gtPlusPlus.xmod.growthcraft.booze;

import cpw.mods.fml.common.registry.GameRegistry;
import growthcraft.api.cellar.Booze;
import growthcraft.api.cellar.CellarRegistry;
import growthcraft.cellar.block.BlockFluidBooze;
import growthcraft.cellar.item.*;
import growthcraft.cellar.utils.BoozeRegistryHelper;
import growthcraft.hops.GrowthCraftHops;
import gtPlusPlus.core.lib.LoadedMods;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class Register_Booze {

	public static BlockFluidBooze[]	jackDanielsWhiskeyFluids;
	public static Item				jackDaniels;
	public static Item				jackDanielsSeeds;
	public static Item				jackDanielsWhiskey;
	public static Item				jackDanielsWhiskeyBucket_deprecated;
	public static ItemBucketBooze[]	jackDanielsWhiskeyBuckets;
	public static Fluid[]			jackDanielsWhiskeyBooze;
	// private static int internalColour =
	// FluidRegistry.getFluid("fluidJackDaniels").getColor();
	private static int				internalColour	= 0000000;

	// Run me during Pre-Init
	public static void preInit() {
		if (LoadedMods.Growthcraft) {
			Register_Booze.start();
		}
	}

	private static void start() {
		Register_Booze.jackDaniels = GrowthCraftHops.hops;
		Register_Booze.jackDanielsSeeds = GrowthCraftHops.hopSeeds;

		Register_Booze.jackDanielsWhiskeyBooze = new Booze[5];
		Register_Booze.jackDanielsWhiskeyFluids = new BlockFluidBooze[Register_Booze.jackDanielsWhiskeyBooze.length];
		Register_Booze.jackDanielsWhiskeyBuckets = new ItemBucketBooze[Register_Booze.jackDanielsWhiskeyBooze.length];
		BoozeRegistryHelper.initializeBooze(Register_Booze.jackDanielsWhiskeyBooze,
				Register_Booze.jackDanielsWhiskeyFluids, Register_Booze.jackDanielsWhiskeyBuckets,
				"grc.jackDanielsWhiskey", Register_Booze.internalColour);

		Register_Booze.jackDanielsWhiskey = new ItemBoozeBottle(6, -0.5F, Register_Booze.jackDanielsWhiskeyBooze)
				.setColor(Register_Booze.internalColour).setTipsy(0.7F, 900).setPotionEffects(new int[] {
						Potion.digSpeed.id
		}, new int[] {
				3600
		});
		Register_Booze.jackDanielsWhiskeyBucket_deprecated = new ItemBoozeBucketDEPRECATED(
				Register_Booze.jackDanielsWhiskeyBooze).setColor(Register_Booze.internalColour);

		// GameRegistry.registerItem(jackDaniels, "grc.jackDaniels");
		// GameRegistry.registerItem(jackDanielsSeeds, "grc.jackDanielsSeeds");
		GameRegistry.registerItem(Register_Booze.jackDanielsWhiskey, "grc.jackDanielsWhiskey");
		GameRegistry.registerItem(Register_Booze.jackDanielsWhiskeyBucket_deprecated, "grc.jackDanielsWhiskey_bucket");

		BoozeRegistryHelper.registerBooze(Register_Booze.jackDanielsWhiskeyBooze,
				Register_Booze.jackDanielsWhiskeyFluids, Register_Booze.jackDanielsWhiskeyBuckets,
				Register_Booze.jackDanielsWhiskey, "grc.jackDanielsWhiskey",
				Register_Booze.jackDanielsWhiskeyBucket_deprecated);

		CellarRegistry.instance().brew().addBrewing(FluidRegistry.WATER, Items.wheat,
				Register_Booze.jackDanielsWhiskeyBooze[4], 200, 60, 0.4F);
		CellarRegistry.instance().brew().addBrewing(Register_Booze.jackDanielsWhiskeyBooze[4],
				Register_Booze.jackDaniels, Register_Booze.jackDanielsWhiskeyBooze[0], 350, 60, 0.1F);

	}

}
