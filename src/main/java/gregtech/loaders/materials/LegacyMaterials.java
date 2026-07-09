package gregtech.loaders.materials;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.Property;
import com.ruling_0.materiallib.api.StandardProperties;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.MaterialIconRegistry.IconType;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TextureSet;
import gregtech.api.material.AspectRefStack;
import gregtech.api.material.FluidNames;
import gregtech.api.material.FluidRef;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialGenerationFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialRef;
import gregtech.api.material.MaterialRefStack;

/// Reconstructs legacy {@link Materials} instances from the stage-03 MaterialLib port, so
/// {@link MaterialsLegacyBridge} can rebuild every `Materials.<field>` that used to come from
/// `MaterialsInit.load()` without a second, independent declaration of material data.
///
/// {@link #build} reuses {@link MaterialBuilder} rather than the `Materials` constructor directly, so every
/// build-time side effect the legacy loader relied on (subtag container registration, per-prefix
/// generated/not-generated set membership, `HAS_COLOR`/`SMELTING_TO_FLUID` auto-tagging, aspect propagation
/// from composition) fires exactly as it did for `MaterialsInit`. A handful of fields are set on the returned
/// `Materials` instance instead of through the builder, because the legacy loader itself set them that way
/// (`setGasTemperature`, `setProcessingMaterialTierEU`, `setByProductMultiplier`, `setSmeltingMultiplier` are
/// all post-construction calls in `MaterialsInit`, not `MaterialBuilder` methods).
///
/// {@link #wireFluids} also points the legacy fluid fields (`mSolid`/`mFluid`/`mGas`/`mPlasma`/
/// `mStandardMoltenFluid`, hydro/steam-cracked arrays) at the Forge fluids MaterialLib registered for this
/// material under their legacy names, since this class runs at `Materials.init()`, before MaterialLib's
/// registered fluids are otherwise consumed.
///
/// Two legacy fields are deliberately never touched here because unchanged legacy code recomputes them
/// globally, after {@link MaterialsLegacyBridge#load} returns, from data this class already reproduces:
/// `mHandleMaterial` (`Materials#addToolValues`, a function of mass and subtags) and `mMaterialInto` (always
/// self for every material `MaterialsInit` ever built). `mByProductMultiplier`/`mSmeltingMultiplier` are set
/// here from the ported data, but `Materials#setMultipliers` -- a separate hardcoded list keyed by material
/// identity, unrelated to MaterialLib -- runs immediately afterward and takes precedence for its ~20
/// materials either way.
///
/// Known gaps, none of which are represented in MaterialLib's stage-03 data and are small enough in scope to
/// document rather than port: `mChemicalFormula` overrides and flavor text (tooltip-only, not read anywhere
/// else); a handful of materials whose dumped `oreByProducts` self-reference duplicates (`addOreByproduct`
/// called on the same material more than once) collapse to one entry here, since
/// `Materials#setOreByproducts`'s own `.distinct()` step means the duplicate count was never observable
/// (`Materials.mOreByProducts` iteration order/content is identical either way). `Copper`'s gas-conditional
/// arc-smelting recipe (`setArcSmeltingIntoWithGas`) is special-cased below rather than left as a gap, since
/// it has no MaterialLib data-level representation at all.
public class LegacyMaterials {

    private LegacyMaterials() {}

    private static final Map<String, TextureSet> TEXTURE_SETS_BY_NAME = indexTextureSets();

    private static Map<String, TextureSet> indexTextureSets() {
        Map<String, TextureSet> byName = new HashMap<>();
        for (Field field : TextureSet.class.getFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != TextureSet.class) continue;
            try {
                TextureSet set = (TextureSet) field.get(null);
                byName.put(set.mSetName, set);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to index " + field, e);
            }
        }
        return byName;
    }

    /// The five legacy materials whose icon set overlays a handful of icons onto a base set
    /// ({@link TextureSet#withCustomTextures}) rather than reusing a plain named constant. MaterialLib's
    /// texture dump only retains the resolved set name (`"CUSTOM/iron"`), not which icons were overridden or
    /// what the base set was, so these five are reproduced directly from the original `MaterialsInit`
    /// declarations rather than derived.
    private static TextureSet customIconSet(String name) {
        return switch (name) {
            case "Copper" -> TextureSet.SET_DULL
                .withCustomTextures("copper", IconType.ORE, IconType.ORE_SMALL, IconType.ORE_RAW);
            case "Gold" -> TextureSet.SET_SHINY
                .withCustomTextures("gold", IconType.ORE, IconType.ORE_SMALL, IconType.ORE_RAW);
            case "Iron" -> TextureSet.SET_METALLIC
                .withCustomTextures("iron", IconType.ORE, IconType.ORE_SMALL, IconType.ORE_RAW);
            case "Diamond" -> TextureSet.SET_DIAMOND.withCustomTextures("diamond", IconType.ORE, IconType.ORE_SMALL);
            case "Emerald" -> TextureSet.SET_EMERALD.withCustomTextures("emerald", IconType.ORE, IconType.ORE_SMALL);
            default -> null;
        };
    }

    private static TextureSet resolveIconSet(Material ml) {
        TextureSet custom = customIconSet(ml.getName());
        if (custom != null) return custom;
        String setName = ml.getProperty(StandardProperties.TEXTURE_SET)
            .getName();
        TextureSet resolved = TEXTURE_SETS_BY_NAME.get(setName);
        if (resolved == null)
            throw new IllegalStateException("No legacy TextureSet named " + setName + " for material " + ml.getName());
        return resolved;
    }

    private static Enchantment findEnchantment(String unlocalizedName) {
        for (Enchantment enchantment : Enchantment.enchantmentsList) {
            if (enchantment != null && unlocalizedName.equals(enchantment.getName())) return enchantment;
        }
        throw new IllegalStateException("No enchantment named " + unlocalizedName);
    }

    private static int orDefault(Integer value, int fallback) {
        return value != null ? value : fallback;
    }

    private static float orDefault(Float value, float fallback) {
        return value != null ? value : fallback;
    }

    /// Builds one legacy {@link Materials} instance from its ported {@link Material}. Pass 1 (scalar fields,
    /// generation flags, subtags, per-prefix exceptions) happens through {@link MaterialBuilder} in this
    /// method. Pass 2 (composition, ore byproducts, smelt/macerate/arc-smelt/direct-smelt links) is wired via
    /// the same deferred `Supplier<Materials>` mechanism `MaterialsInit` used, resolved once every material
    /// in {@link MaterialsLegacyBridge#load} has been built.
    public static Materials build(Material ml) {
        MaterialBuilder builder = new MaterialBuilder();
        String legacyName = ml.getProperty(GTMaterialProperties.LEGACY_NAME);
        builder.setName(legacyName != null ? legacyName : ml.getName());

        String localName = ml.getProperty(GTMaterialProperties.LOCAL_NAME);
        builder.setDefaultLocalName(localName != null ? localName : ml.getName());

        String elementName = ml.getProperty(GTMaterialProperties.ELEMENT);
        if (elementName != null) builder.setElement(Element.valueOf(elementName));

        builder.setIconSet(resolveIconSet(ml));

        String dye = ml.getProperty(GTMaterialProperties.DYE);
        if (dye != null) builder.setColor(Dyes.valueOf(dye));

        builder.setARGB(ml.getProperty(GTMaterialProperties.ARGB));
        Integer moltenArgb = ml.getProperty(GTMaterialProperties.MOLTEN_ARGB);
        if (moltenArgb != null) builder.setMoltenARGB(moltenArgb);

        builder.setTool(
            orDefault(ml.getProperty(GTMaterialProperties.DURABILITY), 0),
            orDefault(ml.getProperty(GTMaterialProperties.TOOL_QUALITY), 0),
            orDefault(ml.getProperty(GTMaterialProperties.TOOL_SPEED), 1.0f));

        String toolEnchantment = ml.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT);
        if (toolEnchantment != null) {
            int level = orDefault(ml.getProperty(GTMaterialProperties.TOOL_ENCHANTMENT_LEVEL), 1);
            builder.setToolEnchantment(() -> findEnchantment(toolEnchantment), level);
        }
        String armorEnchantment = ml.getProperty(GTMaterialProperties.ARMOR_ENCHANTMENT);
        if (armorEnchantment != null) {
            int level = orDefault(ml.getProperty(GTMaterialProperties.ARMOR_ENCHANTMENT_LEVEL), 1);
            builder.setArmorEnchantment(() -> findEnchantment(armorEnchantment), level);
        }

        Float steamMultiplier = ml.getProperty(GTMaterialProperties.STEAM_MULTIPLIER);
        Float gasMultiplier = ml.getProperty(GTMaterialProperties.GAS_MULTIPLIER);
        Float plasmaMultiplier = ml.getProperty(GTMaterialProperties.PLASMA_MULTIPLIER);
        if (steamMultiplier != null || gasMultiplier != null || plasmaMultiplier != null) {
            builder.setTurbine(
                orDefault(steamMultiplier, 1.0f),
                orDefault(gasMultiplier, 1.0f),
                orDefault(plasmaMultiplier, 1.0f));
        }

        Integer fuelType = ml.getProperty(GTMaterialProperties.FUEL_TYPE);
        Integer fuelPower = ml.getProperty(GTMaterialProperties.FUEL_POWER);
        if (fuelType != null || fuelPower != null) {
            builder.setFuel(MaterialBuilder.FuelType.values()[orDefault(fuelType, 0)], orDefault(fuelPower, 0));
        }

        EnumSet<GTMaterialGenerationFlag> generationFlags = ml.getProperty(GTMaterialProperties.GENERATION_FLAGS);
        if (generationFlags != null) {
            if (generationFlags.contains(GTMaterialGenerationFlag.DUST)) builder.addDustItems();
            if (generationFlags.contains(GTMaterialGenerationFlag.METAL)) builder.addMetalItems();
            if (generationFlags.contains(GTMaterialGenerationFlag.GEM)) builder.addGemItems();
            if (generationFlags.contains(GTMaterialGenerationFlag.ORE)) builder.addOreItems();
            if (generationFlags.contains(GTMaterialGenerationFlag.CELL)) builder.addCell();
            if (generationFlags.contains(GTMaterialGenerationFlag.PLASMA)) builder.addPlasma();
            if (generationFlags.contains(GTMaterialGenerationFlag.TOOL_HEAD)) builder.addToolHeadItems();
            if (generationFlags.contains(GTMaterialGenerationFlag.GEAR)) builder.addGearItems();
            if (generationFlags.contains(GTMaterialGenerationFlag.EMPTY)) builder.addEmpty();
        }
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_CORRESPONDING_FLUID))) builder.addFluid();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_CORRESPONDING_GAS))) builder.addGas();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_ELECTROLYZER_RECIPE)))
            builder.addElectrolyzerRecipe();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_CENTRIFUGE_RECIPE)))
            builder.addCentrifugeRecipe();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.CAN_BE_CRACKED))) builder.addCrackingRecipes();
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.HAS_GLOWING_ORE))) builder.hasGlowingOre();

        List<String> addedPrefixes = ml.getProperty(GTMaterialProperties.ADDED_PREFIXES);
        if (addedPrefixes != null) for (String prefixName : addedPrefixes) {
            builder.addOrePrefix(requirePrefix(prefixName, ml));
        }
        List<String> removedPrefixes = ml.getProperty(GTMaterialProperties.REMOVED_PREFIXES);
        if (removedPrefixes != null) for (String prefixName : removedPrefixes) {
            builder.removeOrePrefix(requirePrefix(prefixName, ml));
        }

        Integer meltingPoint = ml.getProperty(GTMaterialProperties.MELTING_POINT);
        if (meltingPoint != null) builder.setMeltingPoint(meltingPoint);
        Integer blastTemp = ml.getProperty(GTMaterialProperties.BLAST_TEMP);
        if (blastTemp != null) builder.setBlastFurnaceTemp(blastTemp);
        if (Boolean.TRUE.equals(ml.getProperty(GTMaterialProperties.BLAST_REQUIRED)))
            builder.setBlastFurnaceRequired(true);
        Boolean autoBlast = ml.getProperty(GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES);
        if (autoBlast != null) builder.setAutoGenerateBlastFurnaceRecipes(autoBlast);
        Boolean autoVacuum = ml.getProperty(GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES);
        if (autoVacuum != null) builder.setAutoGeneratedVacuumFreezerRecipe(autoVacuum);
        Boolean autoRecycle = ml.getProperty(GTMaterialProperties.AUTO_RECYCLE_RECIPES);
        if (autoRecycle != null) builder.setAutoGeneratedRecycleRecipes(autoRecycle);
        Float heatDamage = ml.getProperty(GTMaterialProperties.HEAT_DAMAGE);
        if (heatDamage != null) builder.setHeatDamage(heatDamage);

        Integer densityMultiplier = ml.getProperty(GTMaterialProperties.DENSITY_MULTIPLIER);
        Integer densityDivider = ml.getProperty(GTMaterialProperties.DENSITY_DIVIDER);
        if (densityMultiplier != null || densityDivider != null) {
            builder.setDensity(orDefault(densityMultiplier, 1), orDefault(densityDivider, 1));
        }

        Integer oreMultiplier = ml.getProperty(GTMaterialProperties.ORE_MULTIPLIER);
        if (oreMultiplier != null) builder.setOreMultiplier(oreMultiplier);

        if (Boolean.FALSE.equals(ml.getProperty(GTMaterialProperties.UNIFIABLE))) builder.setUnifiable(false);

        List<MaterialRefStack> composition = ml.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition != null) for (MaterialRefStack stack : composition) {
            builder.addMaterial(
                Materials.get(
                    stack.material()
                        .name()),
                (int) stack.amount());
        }

        List<AspectRefStack> aspects = ml.getProperty(GTMaterialProperties.ASPECTS);
        if (aspects != null) for (AspectRefStack aspect : aspects) {
            builder.addAspect(TCAspects.valueOf(aspect.name()), aspect.amount());
        }

        EnumSet<GTMaterialFlag> flags = ml.getProperty(GTMaterialProperties.FLAGS);
        if (flags != null) for (GTMaterialFlag flag : flags) {
            builder.addSubTag(SubTag.getNewSubTag(legacySubTagName(flag)));
        }

        addDeferredRef(ml, GTMaterialProperties.SMELT_INTO, builder::setSmeltingInto);
        addDeferredRef(ml, GTMaterialProperties.MACERATE_INTO, builder::setMaceratingInto);
        addDeferredRef(ml, GTMaterialProperties.ARC_SMELT_INTO, builder::setArcSmeltingInto);
        addDeferredRef(ml, GTMaterialProperties.DIRECT_SMELTING, builder::setDirectSmelting);

        List<MaterialRefStack> oreByProducts = ml.getProperty(GTMaterialProperties.ORE_BYPRODUCTS);
        if (oreByProducts != null) for (MaterialRefStack stack : oreByProducts) {
            String refName = stack.material()
                .name();
            builder.addOreByproduct(() -> Materials.get(refName));
        }

        // Copper's gas-conditional arc-smelting recipe (arc-smelts to AnnealedCopper only when Oxygen gas is
        // present): MaterialLib's stage-03 port has no data-level representation for a gas-conditional
        // arc-smelt target, so it is reproduced here verbatim from the deleted MaterialsInit's declaration.
        if ("Copper".equals(legacyName != null ? legacyName : ml.getName())) {
            builder.setArcSmeltingIntoWithGas(() -> Materials.Oxygen, () -> Materials.AnnealedCopper);
        }

        Materials material = builder.constructMaterial();

        Integer gasTemp = ml.getProperty(GTMaterialProperties.GAS_TEMP);
        if (gasTemp != null) material.setGasTemperature(gasTemp);
        Integer processingTier = ml.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        if (processingTier != null) material.setProcessingMaterialTierEU(processingTier);
        Integer byProductMultiplier = ml.getProperty(GTMaterialProperties.BYPRODUCT_MULTIPLIER);
        if (byProductMultiplier != null) material.setByProductMultiplier(byProductMultiplier);
        Integer smeltingMultiplier = ml.getProperty(GTMaterialProperties.SMELTING_MULTIPLIER);
        if (smeltingMultiplier != null) material.setSmeltingMultiplier(smeltingMultiplier);

        // MaterialsInit's only post-construction SubTag removal (Netherite has METAL among its generation
        // flags, so the MaterialBuilder constructor's own unconditional
        // `if (generateMetalItems) mSubTags.add(SMELTING_TO_FLUID)` re-adds what FLAGS already omitted).
        if ("Netherite".equals(material.mName)) material.remove(SubTag.SMELTING_TO_FLUID);

        wireFluids(material, ml);

        return material;
    }

    /// Points the legacy fluid fields at the Forge fluids [Materials2FluidShapes] registered for this
    /// material, in place of the per-material `GTFluidFactory` construction `GTProxy`'s `addAutoGenerated*`
    /// methods used before stage 06 (those methods now no-op for a material whose fields are already wired
    /// here, since MaterialLib -- resolving before GT's own preInit -- always registers first).
    private static void wireFluids(Materials material, Material ml) {
        FluidNames legacyFluids = ml.getProperty(GTMaterialProperties.LEGACY_FLUIDS);
        if (legacyFluids != null) {
            material.mSolid = resolveFluid(legacyFluids.solid(), material, true);
            material.mFluid = resolveFluid(legacyFluids.fluid(), material, true);
            material.mGas = resolveFluid(legacyFluids.gas(), material, true);
            material.mPlasma = resolveFluid(legacyFluids.plasma(), material, true);
            material.mStandardMoltenFluid = resolveFluid(legacyFluids.molten(), material, true);
        }
        List<FluidRef> hydroCracked = ml.getProperty(GTMaterialProperties.CRACKED_HYDRO_FLUIDS);
        if (hydroCracked != null) material.setHydroCrackedFluids(resolveFluids(hydroCracked, material));
        List<FluidRef> steamCracked = ml.getProperty(GTMaterialProperties.CRACKED_STEAM_FLUIDS);
        if (steamCracked != null) material.setSteamCrackedFluids(resolveFluids(steamCracked, material));
    }

    private static Fluid[] resolveFluids(List<FluidRef> refs, Materials material) {
        Fluid[] fluids = new Fluid[refs.size()];
        for (int i = 0; i < refs.size(); i++) fluids[i] = resolveFluid(refs.get(i), material, false);
        return fluids;
    }

    private static Fluid resolveFluid(FluidRef ref, Materials material, boolean trackInFluidMap) {
        if (ref == null) return null;
        Fluid fluid = FluidRegistry.getFluid(ref.name());
        if (fluid == null) {
            throw new IllegalStateException(
                "No Forge fluid named " + ref.name()
                    + " for material "
                    + material.mName
                    + "; MaterialLib should have registered every material fluid before Materials.init() runs");
        }
        if (trackInFluidMap) Materials.FLUID_MAP.put(fluid, material);
        return fluid;
    }

    /// A named legacy `Materials` instance with builder-default data, for `MaterialsLegacyBridge`'s bare-JUnit
    /// fallback where no MaterialLib registry exists to build from.
    public static Materials stub(String name) {
        return new MaterialBuilder().setName(name)
            .setDefaultLocalName(name)
            .constructMaterial();
    }

    private static OrePrefixes requirePrefix(String name, Material ml) {
        OrePrefixes prefix = OrePrefixes.getPrefix(name, null);
        if (prefix == null)
            throw new IllegalStateException("No legacy OrePrefixes named " + name + " for material " + ml.getName());
        return prefix;
    }

    private static void addDeferredRef(Material ml, Property<MaterialRef> property,
        Function<Supplier<Materials>, MaterialBuilder> setter) {
        MaterialRef ref = ml.getProperty(property);
        if (ref == null) return;
        String refName = ref.name();
        setter.apply(() -> Materials.get(refName));
    }

    private static String legacySubTagName(GTMaterialFlag flag) {
        return switch (flag) {
            case ANAEROBE_GAS -> "AnaerobeGas";
            case NOBLE_GAS -> "NobleGas";
            default -> flag.name();
        };
    }
}
