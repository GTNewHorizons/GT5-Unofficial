package goodgenerator.blocks.myFluids;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.crossmod.nei.NEIConfig;

public class FluidsBuilder {

    public FluidsBuilder() {}

    public static void Register() {
        crackedNaquadahGas_Lightly();
        crackedNaquadahGas_Moderately();
        crackedNaquadahGas_Heavily();
        crackedLightNaquadahFuel_Lightly();
        crackedLightNaquadahFuel_Moderately();
        crackedLightNaquadahFuel_Heavily();
        crackedHeavyNaquadahFuel_Lightly();
        crackedHeavyNaquadahFuel_Moderately();
        crackedHeavyNaquadahFuel_Heavily();
        crackedNaquadahAsphalt_Lightly();
        crackedNaquadahAsphalt_Moderately();
        crackedNaquadahAsphalt_Heavily();
        combustionPromotor();
        coalTar();
        supercriticalSteam();
    }

    public static void crackedNaquadahGas_Lightly() {
        Fluid tmp = BaseFluid.BuildFluid("lightlyCrackedNaquadahGas");
        tmp.setGaseous(true)
            .setTemperature(800);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("lightlyCrackedNaquadahGas");
        tmp2.setBlockName("lightlyCrackedNaquadahGas");
        GameRegistry.registerBlock(tmp2, "lightlyCrackedNaquadahGas");
        NEIConfig.hide(tmp2);
    }

    public static void crackedNaquadahGas_Moderately() {
        Fluid tmp = BaseFluid.BuildFluid("moderatelyCrackedNaquadahGas");
        tmp.setGaseous(true)
            .setTemperature(800);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("moderatelyCrackedNaquadahGas");
        tmp2.setBlockName("moderatelyCrackedNaquadahGas");
        GameRegistry.registerBlock(tmp2, "moderatelyCrackedNaquadahGas");
        NEIConfig.hide(tmp2);
    }

    public static void crackedNaquadahGas_Heavily() {
        Fluid tmp = BaseFluid.BuildFluid("heavilyCrackedNaquadahGas");
        tmp.setGaseous(true)
            .setTemperature(800);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("heavilyCrackedNaquadahGas");
        tmp2.setBlockName("heavilyCrackedNaquadahGas");
        GameRegistry.registerBlock(tmp2, "heavilyCrackedNaquadahGas");
        NEIConfig.hide(tmp2);
    }

    public static void crackedLightNaquadahFuel_Lightly() {
        Fluid tmp = BaseFluid.BuildFluid("lightlyCrackedLightNaquadahFuel");
        tmp.setGaseous(false)
            .setTemperature(1200);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("lightlyCrackedLightNaquadahFuel");
        tmp2.setBlockName("lightlyCrackedLightNaquadahFuel");
        GameRegistry.registerBlock(tmp2, "lightlyCrackedLightNaquadahFuel");
        NEIConfig.hide(tmp2);
    }

    public static void crackedLightNaquadahFuel_Moderately() {
        Fluid tmp = BaseFluid.BuildFluid("moderatelyCrackedLightNaquadahFuel");
        tmp.setGaseous(false)
            .setTemperature(1200);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("moderatelyCrackedLightNaquadahFuel");
        tmp2.setBlockName("moderatelyCrackedLightNaquadahFuel");
        GameRegistry.registerBlock(tmp2, "moderatelyCrackedLightNaquadahFuel");
        NEIConfig.hide(tmp2);
    }

    public static void crackedLightNaquadahFuel_Heavily() {
        Fluid tmp = BaseFluid.BuildFluid("heavilyCrackedLightNaquadahFuel");
        tmp.setGaseous(false)
            .setTemperature(1200);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("heavilyCrackedLightNaquadahFuel");
        tmp2.setBlockName("heavilyCrackedLightNaquadahFuel");
        GameRegistry.registerBlock(tmp2, "heavilyCrackedLightNaquadahFuel");
        NEIConfig.hide(tmp2);
    }

    public static void crackedHeavyNaquadahFuel_Lightly() {
        Fluid tmp = BaseFluid.BuildFluid("lightlyCrackedHeavyNaquadahFuel");
        tmp.setGaseous(false)
            .setTemperature(1200);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("lightlyCrackedHeavyNaquadahFuel");
        tmp2.setBlockName("lightlyCrackedHeavyNaquadahFuel");
        GameRegistry.registerBlock(tmp2, "lightlyCrackedHeavyNaquadahFuel");
        NEIConfig.hide(tmp2);
    }

    public static void crackedHeavyNaquadahFuel_Moderately() {
        Fluid tmp = BaseFluid.BuildFluid("moderatelyCrackedHeavyNaquadahFuel");
        tmp.setGaseous(false)
            .setTemperature(1200);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("moderatelyCrackedHeavyNaquadahFuel");
        tmp2.setBlockName("moderatelyCrackedHeavyNaquadahFuel");
        GameRegistry.registerBlock(tmp2, "moderatelyCrackedHeavyNaquadahFuel");
        NEIConfig.hide(tmp2);
    }

    public static void crackedHeavyNaquadahFuel_Heavily() {
        Fluid tmp = BaseFluid.BuildFluid("heavilyCrackedHeavyNaquadahFuel");
        tmp.setGaseous(false)
            .setTemperature(1200);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("heavilyCrackedHeavyNaquadahFuel");
        tmp2.setBlockName("heavilyCrackedHeavyNaquadahFuel");
        GameRegistry.registerBlock(tmp2, "heavilyCrackedHeavyNaquadahFuel");
        NEIConfig.hide(tmp2);
    }

    public static void crackedNaquadahAsphalt_Lightly() {
        Fluid tmp = BaseFluid.BuildFluid("lightlyCrackedNaquadahAsphalt");
        tmp.setGaseous(false)
            .setTemperature(1800)
            .setDensity(20000)
            .setViscosity(20000);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("lightlyCrackedNaquadahAsphalt");
        tmp2.setBlockName("lightlyCrackedNaquadahAsphalt");
        GameRegistry.registerBlock(tmp2, "lightlyCrackedNaquadahAsphalt");
        NEIConfig.hide(tmp2);
    }

    public static void crackedNaquadahAsphalt_Moderately() {
        Fluid tmp = BaseFluid.BuildFluid("moderatelyCrackedNaquadahAsphalt");
        tmp.setGaseous(false)
            .setTemperature(1800)
            .setDensity(20000)
            .setViscosity(20000);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("moderatelyCrackedNaquadahAsphalt");
        tmp2.setBlockName("moderatelyCrackedNaquadahAsphalt");
        GameRegistry.registerBlock(tmp2, "moderatelyCrackedNaquadahAsphalt");
        NEIConfig.hide(tmp2);
    }

    public static void crackedNaquadahAsphalt_Heavily() {
        Fluid tmp = BaseFluid.BuildFluid("heavilyCrackedNaquadahAsphalt");
        tmp.setGaseous(false)
            .setTemperature(1800)
            .setDensity(20000)
            .setViscosity(20000);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("heavilyCrackedNaquadahAsphalt");
        tmp2.setBlockName("heavilyCrackedNaquadahAsphalt");
        GameRegistry.registerBlock(tmp2, "heavilyCrackedNaquadahAsphalt");
        NEIConfig.hide(tmp2);
    }

    public static void combustionPromotor() {
        Fluid tmp = BaseFluid.BuildFluid("combustionPromotor");
        tmp.setGaseous(false)
            .setTemperature(300);
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("combustionPromotor");
        tmp2.setBlockName("combustionPromotor");
        GameRegistry.registerBlock(tmp2, "combustionPromotor");
        NEIConfig.hide(tmp2);
    }

    public static void coalTar() {
        Fluid tmp = BaseFluid.BuildFluid("fluid.coalTar");
        tmp.setGaseous(false)
            .setTemperature(450)
            .setUnlocalizedName("fluid.coalTar");
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("coalTar");
        tmp2.setBlockTextureName("coalTar");
        GameRegistry.registerBlock(tmp2, "coalTar");
        NEIConfig.hide(tmp2);
    }

    public static void supercriticalSteam() {
        Fluid tmp = BaseFluid.BuildFluid("supercriticalSteam");
        tmp.setGaseous(true)
            .setTemperature(648)
            .setUnlocalizedName("supercriticalSteam");
        BaseFluid tmp2 = new BaseFluid(tmp, Material.water);
        tmp2.SetTexture("supercriticalSteam");
        tmp2.setBlockTextureName("supercriticalSteam");
        GameRegistry.registerBlock(tmp2, "supercriticalSteam");
        NEIConfig.hide(tmp2);
    }
}
