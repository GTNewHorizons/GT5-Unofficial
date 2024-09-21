package gtPlusPlus.preloader.asm.transformers;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import gregtech.asm.GTCorePlugin;
import gtPlusPlus.core.config.ASMConfiguration;
import gtPlusPlus.preloader.PreloaderLogger;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer.OreDictionaryVisitor;

public class Preloader_Transformer_Handler implements IClassTransformer {

    private static final Set<String> IC2_WRENCH_PATCH_CLASS_NAMES = new HashSet<>();
    private static final String FORGE_ORE_DICTIONARY = "net.minecraftforge.oredict.OreDictionary";

    static {
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.BlockTileEntity");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.machine.BlockMachine");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.machine.BlockMachine2");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.machine.BlockMachine3");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.kineticgenerator.block.BlockKineticGenerator");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.heatgenerator.block.BlockHeatGenerator");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.generator.block.BlockGenerator");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.reactor.block.BlockReactorAccessHatch");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.reactor.block.BlockReactorChamber");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.reactor.block.BlockReactorFluidPort");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.reactor.block.BlockReactorRedstonePort");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.reactor.block.BlockReactorVessel");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.personal.BlockPersonal.class");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.wiring.BlockChargepad.class");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.wiring.BlockElectric.class");
        IC2_WRENCH_PATCH_CLASS_NAMES.add("ic2.core.block.wiring.BlockLuminator.class");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {

        // Fix the OreDictionary - Forge
        if (transformedName.equals(FORGE_ORE_DICTIONARY) && ASMConfiguration.debug.enableOreDictPatch) {
            PreloaderLogger.INFO("OreDictTransformer", "Transforming " + transformedName);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            new ClassReader(basicClass).accept(new OreDictionaryVisitor(classWriter), 0);
            return classWriter.toByteArray();
        }

        if (IC2_WRENCH_PATCH_CLASS_NAMES.contains(transformedName)) {
            PreloaderLogger.INFO("IC2 getHarvestTool Patch", "Transforming " + transformedName);
            return new ClassTransformer_IC2_GetHarvestTool(basicClass, !GTCorePlugin.isDevEnv(), transformedName)
                .getWriter()
                .toByteArray();
        }

        return basicClass;
    }
}
