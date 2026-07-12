package gregtech.api.enums.materials2;

import java.util.List;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;

import net.minecraftforge.fluids.Fluid;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Property;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.material.FluidNames;
import gregtech.api.material.FluidRef;
import gregtech.api.material.GTMaterialProperties;

/// Hand-maintained fluid [Shape] declarations (unlike [Materials2Shapes], not `gen_shapes.py`-generated: there
/// are few, and each needs a per-slot [FluidRef] extractor rather than a uniform mapping). One shape per legacy
/// [GTMaterialProperties#LEGACY_FLUIDS] slot (solid/fluid/gas/plasma/molten) plus the six cracked-fluid slots.
///
/// Each shape's `FluidNamer` returns the exact legacy dumped fluid name and its `FluidConfigurer` reproduces
/// `GTFluid#configureFromStateTemperature`'s temperature/gaseous/density/viscosity/luminosity attributes, so a
/// material's fluid keeps its pre-cutover Forge registry name and behavior byte-identical -- fluid stacks
/// persist in world NBT by name.
public class Materials2FluidShapes {

    // spotless:off
    public static Shape fluidSolid;
    public static Shape fluidLiquid;
    public static Shape fluidGas;
    public static Shape fluidPlasma;
    public static Shape fluidMolten;
    public static Shape fluidHydroCracked1;
    public static Shape fluidHydroCracked2;
    public static Shape fluidHydroCracked3;
    public static Shape fluidSteamCracked1;
    public static Shape fluidSteamCracked2;
    public static Shape fluidSteamCracked3;
    // spotless:on

    public static void init() {
        fluidSolid = fluidShape(
            "fluidSolid",
            "Solid %s",
            legacySlot(FluidNames::solid),
            Materials2FluidShapes::solidAttrs);
        fluidLiquid = fluidShape(
            "fluidLiquid",
            "%s",
            legacySlot(FluidNames::fluid),
            Materials2FluidShapes::liquidAttrs);
        fluidGas = fluidShape("fluidGas", "%s", legacySlot(FluidNames::gas), Materials2FluidShapes::gasAttrs);
        fluidPlasma = fluidShape(
            "fluidPlasma",
            "%s Plasma",
            legacySlot(FluidNames::plasma),
            Materials2FluidShapes::plasmaAttrs);
        fluidMolten = fluidShape(
            "fluidMolten",
            "Molten %s",
            legacySlot(FluidNames::molten),
            Materials2FluidShapes::moltenAttrs);

        fluidHydroCracked1 = fluidShape(
            "fluidHydroCracked1",
            "Lightly Hydro-Cracked %s",
            crackedSlot(GTMaterialProperties.CRACKED_HYDRO_FLUIDS, 0),
            Materials2FluidShapes::gasAttrs);
        fluidHydroCracked2 = fluidShape(
            "fluidHydroCracked2",
            "Moderately Hydro-Cracked %s",
            crackedSlot(GTMaterialProperties.CRACKED_HYDRO_FLUIDS, 1),
            Materials2FluidShapes::gasAttrs);
        fluidHydroCracked3 = fluidShape(
            "fluidHydroCracked3",
            "Severely Hydro-Cracked %s",
            crackedSlot(GTMaterialProperties.CRACKED_HYDRO_FLUIDS, 2),
            Materials2FluidShapes::gasAttrs);
        fluidSteamCracked1 = fluidShape(
            "fluidSteamCracked1",
            "Lightly Steam-Cracked %s",
            crackedSlot(GTMaterialProperties.CRACKED_STEAM_FLUIDS, 0),
            Materials2FluidShapes::gasAttrs);
        fluidSteamCracked2 = fluidShape(
            "fluidSteamCracked2",
            "Moderately Steam-Cracked %s",
            crackedSlot(GTMaterialProperties.CRACKED_STEAM_FLUIDS, 1),
            Materials2FluidShapes::gasAttrs);
        fluidSteamCracked3 = fluidShape(
            "fluidSteamCracked3",
            "Severely Steam-Cracked %s",
            crackedSlot(GTMaterialProperties.CRACKED_STEAM_FLUIDS, 2),
            Materials2FluidShapes::gasAttrs);
    }

    private static Function<Material, FluidRef> legacySlot(Function<FluidNames, FluidRef> slot) {
        return material -> {
            FluidNames names = material.getProperty(GTMaterialProperties.LEGACY_FLUIDS);
            return names == null ? null : slot.apply(names);
        };
    }

    private static Function<Material, FluidRef> crackedSlot(Property<List<FluidRef>> property, int index) {
        return material -> {
            List<FluidRef> refs = material.getProperty(property);
            return refs == null ? null : refs.get(index);
        };
    }

    private static Shape fluidShape(String name, String displayFormat, Function<Material, FluidRef> ref,
        ObjIntConsumer<Fluid> attrs) {
        return MaterialLibAPI.newFluidShape("gregtech", name)
            .displayName(displayFormat)
            .fluidName((shape, material) -> requireRef(ref, shape, material).name())
            .configureFluid((material, fluid) -> {
                FluidRef fluidRef = requireRef(ref, null, material);
                fluid.setTemperature(fluidRef.temperature());
                attrs.accept(fluid, fluidRef.temperature());
            })
            .iconPath((shape, material) -> requireRef(ref, shape, material).texture())
            .build();
    }

    private static FluidRef requireRef(Function<Material, FluidRef> ref, Shape shape, Material material) {
        FluidRef fluidRef = ref.apply(material);
        if (fluidRef == null) {
            throw new IllegalStateException(
                "No legacy fluid data for " + material.getKey() + " in " + (shape != null ? shape : "fluid shape"));
        }
        return fluidRef;
    }

    private static void solidAttrs(Fluid fluid, int temperature) {
        fluid.setGaseous(false)
            .setViscosity(10000);
    }

    private static void liquidAttrs(Fluid fluid, int temperature) {
        fluid.setGaseous(false)
            .setViscosity(1000);
    }

    private static void gasAttrs(Fluid fluid, int temperature) {
        fluid.setGaseous(true)
            .setDensity(-100)
            .setViscosity(200);
    }

    private static void plasmaAttrs(Fluid fluid, int temperature) {
        fluid.setGaseous(true)
            .setDensity(55536)
            .setViscosity(10)
            .setLuminosity(15);
    }

    /// Mirrors `GTFluid#configureFromStateTemperature`'s `MOLTEN` case, which falls through into `LIQUID`'s
    /// gaseous/viscosity after computing its own temperature-dependent luminosity.
    private static void moltenAttrs(Fluid fluid, int temperature) {
        int luminosity = temperature >= 3500 ? 15 : (temperature < 1000 ? 0 : 14 * (temperature - 1000) / 2500 + 1);
        fluid.setGaseous(false)
            .setViscosity(1000)
            .setLuminosity(luminosity);
    }

    private Materials2FluidShapes() {}
}
