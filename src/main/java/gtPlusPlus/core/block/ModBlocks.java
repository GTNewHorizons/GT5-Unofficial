package gtPlusPlus.core.block;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.general.BlockCompressedObsidian;
import gtPlusPlus.core.block.general.FluidTankInfinite;
import gtPlusPlus.core.block.general.HellFire;
import gtPlusPlus.core.block.general.LightGlass;
import gtPlusPlus.core.block.general.MiningExplosives;
import gtPlusPlus.core.block.general.antigrief.BlockWitherProof;
import gtPlusPlus.core.block.machine.CircuitProgrammer;
import gtPlusPlus.core.block.machine.DecayablesChest;
import gtPlusPlus.core.block.machine.FishTrap;
import gtPlusPlus.core.block.machine.Machine_PestKiller;
import gtPlusPlus.core.block.machine.Machine_PooCollector;
import gtPlusPlus.core.block.machine.Machine_ProjectTable;
import gtPlusPlus.core.block.machine.Machine_SuperJukebox;
import gtPlusPlus.core.block.machine.VolumetricFlaskSetter;
import gtPlusPlus.core.fluids.FluidRegistryHandler;

public final class ModBlocks {

    public static Block blockCircuitProgrammer;
    public static Block blockVolumetricFlaskSetter;

    public static Block blockFishTrap;
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

    public static Block blockMiningExplosive;

    public static Block blockHellfire;
    public static Block blockInfiniteFLuidTank;
    public static Block blockProjectTable;
    public static Block blockWitherGuard;
    public static Block blockCompressedObsidian;

    public static Block blockPlayerDoorWooden;
    public static Block blockPlayerDoorIron;
    public static Block blockPlayerDoorCustom_Glass;
    public static Block blockPlayerDoorCustom_Ice;
    public static Block blockPlayerDoorCustom_Cactus;

    public static Block blockCustomJukebox;

    public static Block blockPooCollector;

    public static Block blockPestKiller;

    public static void init() {
        Logger.INFO("Initializing Blocks.");

        registerBlocks();
    }

    public static void registerBlocks() {

        Logger.INFO("Registering Blocks.");
        MatterFabricatorEffectBlock = new LightGlass(false);

        // Fluids
        FluidRegistryHandler.registerFluids();

        // Workbench
        blockFishTrap = new FishTrap();
        blockInfiniteFLuidTank = new FluidTankInfinite();
        blockMiningExplosive = new MiningExplosives();
        blockHellfire = new HellFire();
        blockProjectTable = new Machine_ProjectTable();
        blockWitherGuard = new BlockWitherProof();
        blockCompressedObsidian = new BlockCompressedObsidian();

        blockCircuitProgrammer = new CircuitProgrammer();

        blockDecayablesChest = new DecayablesChest();

        blockCustomJukebox = new Machine_SuperJukebox();

        blockPooCollector = new Machine_PooCollector();

        blockPestKiller = new Machine_PestKiller();

        blockVolumetricFlaskSetter = new VolumetricFlaskSetter();

    }
}
