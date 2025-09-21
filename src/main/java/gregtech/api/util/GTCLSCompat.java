package gregtech.api.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import alexiil.mods.load.MinecraftDisplayer;
import alexiil.mods.load.ProgressDisplayer;
import cpw.mods.fml.common.ProgressManager;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.common.OreDictEventContainer;
import gregtech.loaders.postload.GTPostLoad;

@SuppressWarnings("rawtypes, deprecation")
public class GTCLSCompat {

    private static Class cpwProgressBar;
    private static Field progressBarStep;

    static {
        try {
            cpwProgressBar = Class.forName("cpw.mods.fml.common.ProgressManager$ProgressBar");
        } catch (ClassNotFoundException ex) {
            GTMod.GT_FML_LOGGER.catching(ex);
        }
        try {
            progressBarStep = cpwProgressBar.getDeclaredField("step");
            progressBarStep.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            GTMod.GT_FML_LOGGER.catching(ex);
        }
    }

    private GTCLSCompat() {}

    private static <T> void registerAndReportProgression(String materialsType, Collection<T> materials,
        ProgressManager.ProgressBar progressBar, Function<T, Object> getName, Consumer<T> action) {
        int sizeStep = materials.size();
        final long progressionReportsEvery = 100;
        final long bakingMsgEvery = 1000;
        long nextProgressionReportAt = 0;
        long nextBakingMsgAt = 0;
        int currentStep = 0;

        for (T m : materials) {
            long now = System.currentTimeMillis();

            if (nextProgressionReportAt < now) {
                nextProgressionReportAt = now + progressionReportsEvery;
                String materialName = getName.apply(m)
                    .toString();
                try {
                    ProgressDisplayer.displayProgress(materialName, (float) currentStep / sizeStep);
                } catch (IOException e) {
                    GTMod.GT_FML_LOGGER.error("While updating progression", e);
                }
                try {
                    progressBarStep.set(progressBar, currentStep);
                } catch (IllegalAccessException iae) {
                    GTMod.GT_FML_LOGGER.error("While updating intermediate progression steps number", iae);
                }
                progressBar.step(materialName);
            }
            if (nextBakingMsgAt < now) {
                nextBakingMsgAt = now + bakingMsgEvery;
                GTMod.GT_FML_LOGGER
                    .info(String.format("%s - Baking: %d%%", materialsType, currentStep * 100 / sizeStep));
            }
            action.accept(m);
            currentStep += 1;
        }
        GTMod.GT_FML_LOGGER.info(String.format("%s - Baking: Done", materialsType));
        try {
            progressBarStep.set(progressBar, currentStep);
        } catch (IllegalAccessException iae) {
            GTMod.GT_FML_LOGGER.error("While updating final progression steps number", iae);
        }
    }

    public static void stepMaterialsCLS(Collection<OreDictEventContainer> mEvents,
        ProgressManager.ProgressBar progressBar) {
        MinecraftDisplayer.isRegisteringGTmaterials = true;
        registerAndReportProgression(
            "GregTech materials",
            mEvents,
            progressBar,
            m -> m.mMaterial,
            OreDictEventContainer::registerRecipes);
        ProgressManager.pop(progressBar);
        MinecraftDisplayer.isRegisteringGTmaterials = false;
    }

    public static void doActualRegistrationCLS(ProgressManager.ProgressBar progressBar,
        Set<Materials> replacedVanillaItemsSet) {
        MinecraftDisplayer.isReplacingVanillaMaterials = true;
        registerAndReportProgression(
            "Vanilla materials",
            replacedVanillaItemsSet,
            progressBar,
            m -> m.mDefaultLocalName,
            GTPostLoad::doActualRegistration);
    }

    public static void pushToDisplayProgress() {
        MinecraftDisplayer.isReplacingVanillaMaterials = false;
        try {
            ProgressDisplayer
                .displayProgress("Post Initialization: loading GregTech", MinecraftDisplayer.getLastPercent());
        } catch (IOException e) {
            GTMod.GT_FML_LOGGER.error("Exception caught when updating loading screen", e);
        }
    }
}
