package gtPlusPlus.preloader.asm.transformers;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.preloader.Preloader_Logger;
import gtPlusPlus.preloader.asm.AsmConfig;
import gtPlusPlus.preloader.asm.transformers.Preloader_ClassTransformer.OreDictionaryVisitor;

public class Preloader_Transformer_Handler implements IClassTransformer {

    private static final Set<String> IC2_WRENCH_PATCH_CLASS_NAMES = new HashSet<>();
    private static final String LWJGL_KEYBOARD = "org.lwjgl.input.Keyboard";
    private static final String MINECRAFT_GAMESETTINGS = "net.minecraft.client.settings.GameSettings";
    private static final String FORGE_CHUNK_MANAGER = "net.minecraftforge.common.ForgeChunkManager";
    private static final String FORGE_ORE_DICTIONARY = "net.minecraftforge.oredict.OreDictionary";
    private static final String COFH_ORE_DICTIONARY_ARBITER = "cofh.core.util.oredict.OreDictionaryArbiter";
    private static final String THAUMCRAFT_ITEM_WISP_ESSENCE = "thaumcraft.common.items.ItemWispEssence";

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
        // Fix LWJGL index array out of bounds on keybinding IDs
        if ((transformedName.equals(LWJGL_KEYBOARD) || transformedName.equals(MINECRAFT_GAMESETTINGS))
                && AsmConfig.enabledLwjglKeybindingFix
                // Do not transform if using lwjgl3
                && !ReflectionUtils.doesClassExist("org.lwjgl.system.Platform")) {
            boolean isClientSettingsClass = !transformedName.equals("org.lwjgl.input.Keyboard");
            Preloader_Logger.INFO("LWJGL Keybinding index out of bounds fix", "Transforming " + transformedName);
            return new ClassTransformer_LWJGL_Keyboard(basicClass, isClientSettingsClass).getWriter().toByteArray();
        }

        // Fix the OreDictionary - Forge
        if (transformedName.equals(FORGE_ORE_DICTIONARY) && AsmConfig.enableOreDictPatch) {
            Preloader_Logger.INFO("OreDictTransformer", "Transforming " + transformedName);
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            new ClassReader(basicClass).accept(new OreDictionaryVisitor(classWriter), 0);
            return classWriter.toByteArray();
        }

        // Fix the OreDictionary COFH
        if (transformedName.equals(COFH_ORE_DICTIONARY_ARBITER)
                && (AsmConfig.enableCofhPatch || CORE_Preloader.DEV_ENVIRONMENT)) {
            Preloader_Logger.INFO("COFH", "Transforming " + transformedName);
            return new ClassTransformer_COFH_OreDictionaryArbiter(basicClass).getWriter().toByteArray();
        }

        if (IC2_WRENCH_PATCH_CLASS_NAMES.contains(transformedName)) {
            Preloader_Logger.INFO("IC2 getHarvestTool Patch", "Transforming " + transformedName);
            return new ClassTransformer_IC2_GetHarvestTool(basicClass, !CORE_Preloader.DEV_ENVIRONMENT, transformedName)
                    .getWriter().toByteArray();
        }

        // Fix Thaumcraft stuff
        // Patching ItemWispEssence to allow invalid item handling
        if (transformedName.equals(THAUMCRAFT_ITEM_WISP_ESSENCE) && AsmConfig.enableTcAspectSafety) {
            Preloader_Logger.INFO("Thaumcraft WispEssence_Patch", "Transforming " + transformedName);
            return new ClassTransformer_TC_ItemWispEssence(basicClass, !CORE_Preloader.DEV_ENVIRONMENT).getWriter()
                    .toByteArray();
        }

        return basicClass;
    }
}
