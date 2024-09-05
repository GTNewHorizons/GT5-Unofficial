package gregtech.api.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import cpw.mods.fml.common.ProgressManager;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.common.GTProxy;
import gregtech.loaders.postload.GTPostLoad;

@SuppressWarnings("rawtypes, unchecked, deprecation")
public class GTCLSCompat {

    private static Class alexiilMinecraftDisplayer;
    private static Class alexiilProgressDisplayer;
    private static Class cpwProgressBar;

    private static Method getLastPercent;
    private static Method displayProgress;

    private static Field isReplacingVanillaMaterials;
    private static Field isRegisteringGTmaterials;
    private static Field progressBarStep;

    static {
        // CLS
        try {
            alexiilMinecraftDisplayer = Class.forName("alexiil.mods.load.MinecraftDisplayer");
            alexiilProgressDisplayer = Class.forName("alexiil.mods.load.ProgressDisplayer");
        } catch (ClassNotFoundException ex) {
            GTMod.GT_FML_LOGGER.catching(ex);
        }

        try {
            cpwProgressBar = Class.forName("cpw.mods.fml.common.ProgressManager$ProgressBar");
        } catch (ClassNotFoundException ex) {
            GTMod.GT_FML_LOGGER.catching(ex);
        }

        Optional.ofNullable(alexiilMinecraftDisplayer)
            .ifPresent(e -> {
                try {
                    getLastPercent = e.getMethod("getLastPercent");
                    isReplacingVanillaMaterials = e.getField("isReplacingVanillaMaterials");
                    isRegisteringGTmaterials = e.getField("isRegisteringGTmaterials");
                } catch (NoSuchMethodException | NoSuchFieldException ex) {
                    GTMod.GT_FML_LOGGER.catching(ex);
                }
            });

        Optional.ofNullable(alexiilProgressDisplayer)
            .ifPresent(e -> {
                try {
                    displayProgress = e.getMethod("displayProgress", String.class, float.class);
                } catch (NoSuchMethodException ex) {
                    GTMod.GT_FML_LOGGER.catching(ex);
                }
            });

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
                    displayProgress.invoke(null, materialName, (float) currentStep / sizeStep);
                } catch (IllegalAccessException | InvocationTargetException iae) {
                    GTMod.GT_FML_LOGGER.error("While updating progression", iae);
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

    public static void stepMaterialsCLS(Collection<GTProxy.OreDictEventContainer> mEvents,
        ProgressManager.ProgressBar progressBar) throws IllegalAccessException {
        try {
            isRegisteringGTmaterials.set(null, true);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            GTMod.GT_FML_LOGGER.catching(e);
        }
        registerAndReportProgression(
            "GregTech materials",
            mEvents,
            progressBar,
            m -> m.mMaterial,
            GTProxy::registerRecipes);
        ProgressManager.pop(progressBar);
        isRegisteringGTmaterials.set(null, false);
    }

    public static void doActualRegistrationCLS(ProgressManager.ProgressBar progressBar,
        Set<Materials> replacedVanillaItemsSet) {
        try {
            isReplacingVanillaMaterials.set(null, true);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            GTMod.GT_FML_LOGGER.catching(e);
        }
        registerAndReportProgression(
            "Vanilla materials",
            replacedVanillaItemsSet,
            progressBar,
            m -> m.mDefaultLocalName,
            GTPostLoad::doActualRegistration);
    }

    public static void pushToDisplayProgress() throws InvocationTargetException, IllegalAccessException {
        isReplacingVanillaMaterials.set(null, false);
        displayProgress.invoke(null, "Post Initialization: loading GregTech", getLastPercent.invoke(null));
    }
}
