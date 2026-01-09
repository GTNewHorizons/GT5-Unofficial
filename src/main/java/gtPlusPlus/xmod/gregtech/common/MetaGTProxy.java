package gtPlusPlus.xmod.gregtech.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TAE;
import gregtech.api.util.GTLanguageManager;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.handler.AchievementHandler;
import gtPlusPlus.core.util.minecraft.LangUtils;
import gtPlusPlus.xmod.gregtech.common.covers.CoverManager;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;

public class MetaGTProxy {

    public static AchievementHandler mAssemblyAchievements;

    public MetaGTProxy() {}

    public static void preInit() {
        fixIC2FluidNames();
        CoverManager.generateCustomCovers();
    }

    public static void init() {
        fixIC2FluidNames();
    }

    public static void postInit() {
        mAssemblyAchievements = new AchievementHandler();
        fixIC2FluidNames();

        // Finalise TAE
        TAE.finalizeTAE();
    }

    @SuppressWarnings("deprecation")
    public static void fixIC2FluidNames() {
        // Fix IC2 Hot Water name
        try {
            String aNewHeatedWaterName = "Heated Water";
            Logger.INFO("Renaming [IC2 Hotspring Water] --> [" + aNewHeatedWaterName + "].");
            LanguageRegistry.instance()
                .addStringLocalization("fluidHotWater", "Heated Water");
            LanguageRegistry.instance()
                .addStringLocalization("fluidHotWater", aNewHeatedWaterName);
            LanguageRegistry.instance()
                .addStringLocalization("ic2.fluidHotWater", aNewHeatedWaterName);
            GTLanguageManager.addStringLocalization("fluidHotWater", aNewHeatedWaterName);
            GTLanguageManager.addStringLocalization("ic2.fluidHotWater", aNewHeatedWaterName);

            Block b = BlocksItems.getFluidBlock(InternalName.fluidHotWater);
            if (b != null) {
                LanguageRegistry.addName(new ItemStack(b), aNewHeatedWaterName);
                LanguageRegistry.instance()
                    .addStringLocalization(b.getUnlocalizedName(), aNewHeatedWaterName);
                GTLanguageManager.addStringLocalization(b.getUnlocalizedName(), aNewHeatedWaterName);
            }
            Fluid f = BlocksItems.getFluid(InternalName.fluidHotWater);
            if (f != null) {
                LanguageRegistry.instance()
                    .addStringLocalization(f.getUnlocalizedName(), aNewHeatedWaterName);
                GTLanguageManager.addStringLocalization(f.getUnlocalizedName(), aNewHeatedWaterName);
                int aDam = FluidRegistry.getFluidID(f);
                ItemStack s = ItemList.Display_Fluid.getWithDamage(1, aDam);
                if (s != null) {
                    LanguageRegistry.addName(s, aNewHeatedWaterName);
                }
            }

            String[] aLangs = new String[] { "de_DE", "en_US", "en_GB", "en_IC", "es_AR", "es_ES", "es_MX", "es_UY",
                "es_VE", "fr_CA", "fr_FR", "it_IT", "ko_KR", "pt_BR", "pt_PT", "ru_RU", "sv_SE", "tr_TR", "zh_CN",
                "zh_TW", };
            String[] aLangValues = new String[] { "Erhitztes Wasser", "Heated Water", "Heated Water", "Heated Water",
                "Agua caliente", "Agua caliente", "Agua caliente", "Agua caliente", "Agua caliente", "Eau chauffée",
                "Eau chauffée", "Acqua riscaldata", "온수", "Água aquecida", "Água aquecida", "Вода с подогревом",
                "Uppvärmt vatten", "Isıtılmış Su", "热水", "热水", };
            for (int i = 0; i < aLangs.length; i++) {
                LangUtils.rewriteEntryForLanguageRegistry(aLangs[i], "fluidHotWater", aLangValues[i]);
                LangUtils.rewriteEntryForLanguageRegistry(aLangs[i], "ic2.fluidHotWater", aLangValues[i]);
            }
        } catch (Exception t) {
            t.printStackTrace();
        }
    }
}
