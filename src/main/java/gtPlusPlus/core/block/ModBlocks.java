package gtPlusPlus.core.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.fuel.BlockCactusCharcoal;
import gtPlusPlus.core.block.fuel.BlockCactusCoke;
import gtPlusPlus.core.block.fuel.BlockSugarCharcoal;
import gtPlusPlus.core.block.fuel.BlockSugarCoke;
import gtPlusPlus.core.block.general.BlockCompressedObsidian;
import gtPlusPlus.core.block.general.BlockFluidTankInfinite;
import gtPlusPlus.core.block.general.BlockHellFire;
import gtPlusPlus.core.block.general.BlockLightGlass;
import gtPlusPlus.core.block.general.BlockMiningExplosives;
import gtPlusPlus.core.block.general.antigrief.BlockWitherProof;
import gtPlusPlus.core.block.machine.BlockCircuitProgrammer;
import gtPlusPlus.core.block.machine.BlockDecayablesChest;
import gtPlusPlus.core.block.machine.BlockFishTrap;
import gtPlusPlus.core.block.machine.BlockFlaskSetter;
import gtPlusPlus.core.block.machine.BlockPestKiller;
import gtPlusPlus.core.block.machine.BlockPooCollector;
import gtPlusPlus.core.fluids.FluidRegistryHandler;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public final class ModBlocks {

    public static Block blockCircuitProgrammer;
    public static Block blockVolumetricFlaskSetter;

    public static Block blockDecayablesChest;

    public static Block blockCasingsMisc;
    public static Block blockCasings2Misc;
    public static Block blockCasings3Misc;
    public static Block blockCasings4Misc;
    public static Block blockCasings5Misc;
    public static Block blockCasings6Misc;
    public static Block blockCasingsTieredGTPP;
    public static Block blockSpecialMultiCasings;
    public static Block blockSpecialMultiCasings2;
    public static Block blockCustomMachineCasings;
    public static Block blockCustomPipeGearCasings;

    public static Block MatterFabricatorEffectBlock;

    public static Fluid fluidSludge = new Fluid("fluid.sludge");
    public static Block blockFluidSludge;

    public static Block blockHellfire;
    public static Block blockInfiniteFLuidTank;

    public static Block blockPooCollector;

    public static void init() {
        Logger.INFO("Initializing Blocks.");

        registerBlocks();
    }

    public static void registerBlocks() {

        Logger.INFO("Registering Blocks.");
        MatterFabricatorEffectBlock = new BlockLightGlass(false);

        // Fluids
        FluidRegistryHandler.registerFluids();

        GregtechItemList.FishTrap.set(new ItemStack(new BlockFishTrap()));
        blockInfiniteFLuidTank = new BlockFluidTankInfinite();
        GregtechItemList.MiningExplosives.set(new ItemStack(new BlockMiningExplosives()));
        blockHellfire = new BlockHellFire();
        GregtechItemList.WitherGuard.set(new ItemStack(new BlockWitherProof()));
        blockCircuitProgrammer = new BlockCircuitProgrammer();
        blockDecayablesChest = new BlockDecayablesChest();
        blockPooCollector = new BlockPooCollector();
        GregtechItemList.PestKiller.set(new ItemStack(new BlockPestKiller()));
        blockVolumetricFlaskSetter = new BlockFlaskSetter();

        // Compressed Blocks
        Block cactusCharcoal = new BlockCactusCharcoal();
        GregtechItemList.BlockCactusCharcoal.set(new ItemStack(cactusCharcoal));
        GregtechItemList.CompressedCactusCharcoal.set(new ItemStack(cactusCharcoal, 1, 1));
        GregtechItemList.DoubleCompressedCactusCharcoal.set(new ItemStack(cactusCharcoal, 1, 2));
        GregtechItemList.TripleCompressedCactusCharcoal.set(new ItemStack(cactusCharcoal, 1, 3));
        GregtechItemList.QuadrupleCompressedCactusCharcoal.set(new ItemStack(cactusCharcoal, 1, 4));
        GregtechItemList.QuintupleCompressedCactusCharcoal.set(new ItemStack(cactusCharcoal, 1, 5));

        Block cactusCoke = new BlockCactusCoke();
        GregtechItemList.BlockCactusCoke.set(new ItemStack(cactusCoke));
        GregtechItemList.CompressedCactusCoke.set(new ItemStack(cactusCoke, 1, 1));
        GregtechItemList.DoubleCompressedCactusCoke.set(new ItemStack(cactusCoke, 1, 2));
        GregtechItemList.TripleCompressedCactusCoke.set(new ItemStack(cactusCoke, 1, 3));
        GregtechItemList.QuadrupleCompressedCactusCoke.set(new ItemStack(cactusCoke, 1, 4));
        GregtechItemList.QuintupleCompressedCactusCoke.set(new ItemStack(cactusCoke, 1, 5));

        Block sugarCharcoal = new BlockSugarCharcoal();
        GregtechItemList.BlockSugarCharcoal.set(new ItemStack(sugarCharcoal));
        GregtechItemList.CompressedSugarCharcoal.set(new ItemStack(sugarCharcoal, 1, 1));
        GregtechItemList.DoubleCompressedSugarCharcoal.set(new ItemStack(sugarCharcoal, 1, 2));
        GregtechItemList.TripleCompressedSugarCharcoal.set(new ItemStack(sugarCharcoal, 1, 3));
        GregtechItemList.QuadrupleCompressedSugarCharcoal.set(new ItemStack(sugarCharcoal, 1, 4));
        GregtechItemList.QuintupleCompressedSugarCharcoal.set(new ItemStack(sugarCharcoal, 1, 5));

        Block sugarCoke = new BlockSugarCoke();
        GregtechItemList.BlockSugarCoke.set(new ItemStack(sugarCoke));
        GregtechItemList.CompressedSugarCoke.set(new ItemStack(sugarCoke, 1, 1));
        GregtechItemList.DoubleCompressedSugarCoke.set(new ItemStack(sugarCoke, 1, 2));
        GregtechItemList.TripleCompressedSugarCoke.set(new ItemStack(sugarCoke, 1, 3));
        GregtechItemList.QuadrupleCompressedSugarCoke.set(new ItemStack(sugarCoke, 1, 4));
        GregtechItemList.QuintupleCompressedSugarCoke.set(new ItemStack(sugarCoke, 1, 5));

        Block compressedObsidian = new BlockCompressedObsidian();
        GregtechItemList.CompressedObsidian.set(new ItemStack(compressedObsidian));
        GregtechItemList.DoubleCompressedObsidian.set(new ItemStack(compressedObsidian, 1, 1));
        GregtechItemList.TripleCompressedObsidian.set(new ItemStack(compressedObsidian, 1, 2));
        GregtechItemList.QuadrupleCompressedObsidian.set(new ItemStack(compressedObsidian, 1, 3));
        GregtechItemList.QuintupleCompressedObsidian.set(new ItemStack(compressedObsidian, 1, 4));
        GregtechItemList.InvertedObsidian.set(new ItemStack(compressedObsidian, 1, 5));

        // Compressed Glowstone shares the block with Compressed Obsidian
        GregtechItemList.CompressedGlowstone.set(new ItemStack(compressedObsidian, 1, 6));
        GregtechItemList.DoubleCompressedGlowstone.set(new ItemStack(compressedObsidian, 1, 7));
        GregtechItemList.TripleCompressedGlowstone.set(new ItemStack(compressedObsidian, 1, 8));
        GregtechItemList.QuadrupleCompressedGlowstone.set(new ItemStack(compressedObsidian, 1, 9));
        GregtechItemList.QuintupleCompressedGlowstone.set(new ItemStack(compressedObsidian, 1, 10));

        // Compressed Netherrack shares the block with Compressed Obsidian
        GregtechItemList.CompressedNetherrack.set(new ItemStack(compressedObsidian, 1, 11));
        GregtechItemList.DoubleCompressedNetherrack.set(new ItemStack(compressedObsidian, 1, 12));
        GregtechItemList.TripleCompressedNetherrack.set(new ItemStack(compressedObsidian, 1, 13));
    }
}
