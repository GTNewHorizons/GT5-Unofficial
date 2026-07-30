package gregtech.api.enums.materials2;

import java.util.List;
import java.util.function.Function;
import java.util.function.ObjIntConsumer;

import net.minecraftforge.fluids.Fluid;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.material.FluidNames;
import gregtech.api.material.FluidRef;

/// Hand-maintained fluid [Shape] declarations, one per [MaterialFluidNames] slot
/// (solid/fluid/gas/plasma/molten) plus the six cracked-fluid slots. Each needs its own [FluidRef] extractor
/// rather than a uniform mapping.
///
/// Each shape's `FluidNamer` returns the exact legacy dumped fluid name and its `FluidConfigurer` reproduces
/// `GTFluid#configureFromStateTemperature`'s temperature/gaseous/density/viscosity/luminosity attributes, so a
/// material's fluid keeps its pre-cutover Forge registry name and behavior byte-identical -- fluid stacks
/// persist in world NBT by name.
public class FluidShapes {

    // spotless:off
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
        fluidLiquid = fluidShape(
            "fluidLiquid",
            "%s",
            legacySlot(FluidNames::fluid),
            FluidShapes::liquidAttrs);
        fluidGas = fluidShape("fluidGas", "%s", legacySlot(FluidNames::gas), FluidShapes::gasAttrs);
        fluidPlasma = fluidShape(
            "fluidPlasma",
            "%s Plasma",
            legacySlot(FluidNames::plasma),
            FluidShapes::plasmaAttrs);
        fluidMolten = fluidShape(
            "fluidMolten",
            "Molten %s",
            legacySlot(FluidNames::molten),
            FluidShapes::moltenAttrs);

        fluidHydroCracked1 = fluidShape(
            "fluidHydroCracked1",
            "Lightly Hydro-Cracked %s",
            crackedSlot(MaterialFluidNames::hydroCracked, 0),
            FluidShapes::gasAttrs);
        fluidHydroCracked2 = fluidShape(
            "fluidHydroCracked2",
            "Moderately Hydro-Cracked %s",
            crackedSlot(MaterialFluidNames::hydroCracked, 1),
            FluidShapes::gasAttrs);
        fluidHydroCracked3 = fluidShape(
            "fluidHydroCracked3",
            "Severely Hydro-Cracked %s",
            crackedSlot(MaterialFluidNames::hydroCracked, 2),
            FluidShapes::gasAttrs);
        fluidSteamCracked1 = fluidShape(
            "fluidSteamCracked1",
            "Lightly Steam-Cracked %s",
            crackedSlot(MaterialFluidNames::steamCracked, 0),
            FluidShapes::gasAttrs);
        fluidSteamCracked2 = fluidShape(
            "fluidSteamCracked2",
            "Moderately Steam-Cracked %s",
            crackedSlot(MaterialFluidNames::steamCracked, 1),
            FluidShapes::gasAttrs);
        fluidSteamCracked3 = fluidShape(
            "fluidSteamCracked3",
            "Severely Steam-Cracked %s",
            crackedSlot(MaterialFluidNames::steamCracked, 2),
            FluidShapes::gasAttrs);
    }

    private static Function<Material, FluidRef> legacySlot(Function<FluidNames, FluidRef> slot) {
        return material -> {
            FluidNames names = MaterialFluidNames.of(material.getName());
            return names == null ? null : slot.apply(names);
        };
    }

    private static Function<Material, FluidRef> crackedSlot(Function<String, List<FluidRef>> family, int index) {
        return material -> {
            List<FluidRef> refs = family.apply(material.getName());
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

    private FluidShapes() {}
}
