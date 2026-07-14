package gregtech.loaders.materials;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Materials;
import gregtech.api.enums.materials2.Materials2Materials;

/// Rebuilds every MaterialLib-backed legacy `Materials` field from the resolved MaterialLib registry, via
/// [LegacyMaterials#build]. [LegacyMarkerMaterials#loadMarkers] separately rebuilds the legacy fields that carry
/// no MaterialLib data (never generated an item/fluid/composition and are unreferenced by a ported material).
///
/// [#load] walks the fixed [#BRIDGED_FIELD_NAMES] list, looking up each field's ML material individually via
/// [MaterialLibAPI#getMaterial], rather than walking [MaterialLibAPI#getMaterials] in registry order: MaterialLib's
/// registration order does not topologically sort by composition dependency, but [#BRIDGED_FIELD_NAMES]'s order
/// does, and that sort is load-bearing -- [LegacyMaterials#build] resolves a material's `COMPOSITION` references
/// inline via `Materials.get(name)`, so every material a composition can reference must already be built by the
/// time it is reached.
///
/// A `gregtech`-owned ML material's name usually equals the legacy field it fills; [#ML_NAME_TO_FIELD_OVERRIDES]
/// lists the exceptions (sanitized identifiers, GT5U disambiguation suffixes, and legacy aliases), inverted once
/// into [#FIELD_NAME_TO_ML_NAME_OVERRIDES] for the field-to-name direction both [#load] and [#loadStubs] look up.
///
/// A bare JUnit run never fires MaterialLib's `MaterialRegistrationEvent`, so [Materials2Materials] fields stay
/// null outside a real FML mod-loading lifecycle and the registry can't be queried. [#loadStubs] falls back to
/// building the same fields with builder-default data via [LegacyMaterials#stub] instead (fluids stay null in
/// this fallback too, since they only register during a real preInit). Field order does not matter there, since
/// a stub carries no composition to resolve.
public class MaterialsLegacyBridge {

    private static final Map<String, Field> FIELDS_BY_NAME = indexMaterialsFields();

    private static Map<String, Field> indexMaterialsFields() {
        Map<String, Field> byName = new HashMap<>();
        for (Field field : Materials.class.getFields()) {
            if (!Modifier.isStatic(field.getModifiers()) || field.getType() != Materials.class) continue;
            byName.put(field.getName(), field);
        }
        return byName;
    }

    /// ML material names that do not match the legacy `Materials` field they fill: sanitized identifiers
    /// (`Helium_3` -> `Helium3`), GT5U disambiguation suffixes (`Francium_GT5U` -> `Francium`), and legacy
    /// aliases (`Water` -> `Steam`, matching the fluid's field name rather than its own).
    // spotless:off
    private static final Map<String, String> ML_NAME_TO_FIELD_OVERRIDES = Map.ofEntries(
        Map.entry("1,1Dimethylhydrazine", "Dimethylhydrazine"),
        Map.entry("1,3Dimethylbenzene", "IIIDimethylbenzene"),
        Map.entry("1,4Dimethylbenzene", "IVDimethylbenzene"),
        Map.entry("2Butin14diol", "IIButinIIVdiol"),
        Map.entry("2Nitrochlorobenzene", "Nitrochlorobenzene"),
        Map.entry("3,3Diaminobenzidine", "Diaminobenzidin"),
        Map.entry("3,3Dichlorobenzidine", "Dichlorobenzidine"),
        Map.entry("4Nitroaniline", "IVNitroaniline"),
        Map.entry("AdvancedGlue", "GlueAdvanced"),
        Map.entry("Alumina", "Aluminiumoxide"),
        Map.entry("AluminiumHydroxide", "Aluminiumhydroxide"),
        Map.entry("Aluminiumfluoride", "AluminiumFluoride"),
        Map.entry("BiohMediumSterilized", "BioMediumSterilized"),
        Map.entry("BotaniaDragonstone", "Dragonstone"),
        Map.entry("BrightLumipodExtract", "LumipodExtract"),
        Map.entry("CacliumCarbide", "CalciumCarbide"),
        Map.entry("CaesiumHydroxide_GT5U", "CaesiumHydroxide"),
        Map.entry("CalciumHydride", "Calciumhydride"),
        Map.entry("ChargedCertusQuartz", "CertusQuartzCharged"),
        Map.entry("Chromiumtrioxide", "ChromiumTrioxide"),
        Map.entry("Cobalt(II)Acetate", "CobaltIIAcetate"),
        Map.entry("Cobalt(II)Naphthenate", "CobaltIINaphthenate"),
        Map.entry("Cobalt(II)Nitrate", "CobaltIINitrate"),
        Map.entry("CrackedRadox", "RadoxCracked"),
        Map.entry("CrudeSteel", "ClayCompound"),
        Map.entry("DarkAsh", "AshDark"),
        Map.entry("DilutedHydrochloricAcid_GT5U", "DilutedHydrochloricAcid"),
        Map.entry("DilutedXenoxene", "XenoxeneDiluted"),
        Map.entry("DimensionallyTranscendentCrudeCatalyst", "DTCC"),
        Map.entry("DimensionallyTranscendentExoticCatalyst", "DTEC"),
        Map.entry("DimensionallyTranscendentProsaicCatalyst", "DTPC"),
        Map.entry("DimensionallyTranscendentResidue", "DTR"),
        Map.entry("DimensionallyTranscendentResplendentCatalyst", "DTRC"),
        Map.entry("DimensionallyTranscendentStellarCatalyst", "DTSC"),
        Map.entry("DiphenylIsophtalate", "Diphenylisophthalate"),
        Map.entry("EnhancedGalgadorian", "GalgadorianEnhanced"),
        Map.entry("EthylTertButylEther", "AntiKnock"),
        Map.entry("EthyleneGlycol", "Ethyleneglycol"),
        Map.entry("Flerovium_GT5U", "Flerovium"),
        Map.entry("FoolsRuby", "Spinel"),
        Map.entry("Francium_GT5U", "Francium"),
        Map.entry("Fuel", "Diesel"),
        Map.entry("Gasoline", "GasolineRegular"),
        Map.entry("HeadedBauxiteSlurry", "HeatedBauxiteSlurry"),
        Map.entry("HeavyRadox", "RadoxHeavy"),
        Map.entry("HeeEndium", "Endium"),
        Map.entry("Helium_3", "Helium3"),
        Map.entry("HighOctaneGasoline", "GasolinePremium"),
        Map.entry("HydrochloricAcid_GT5U", "HydrochloricAcid"),
        Map.entry("HydrofluoricAcid_GT5U", "HydrofluoricAcid"),
        Map.entry("Isopropylbenzene", "Cumene"),
        Map.entry("LightRadox", "RadoxLight"),
        Map.entry("Longasssuperconductornameforuhvwire", "SuperconductorUHVBase"),
        Map.entry("Longasssuperconductornameforuvwire", "SuperconductorUVBase"),
        Map.entry("MTBEReactionMixture(Butane)", "MTBEMixtureAlt"),
        Map.entry("MTBEReactionMixture(Butene)", "MTBEMixture"),
        Map.entry("Magmatter", "MagMatter"),
        Map.entry("MagnetohydrodynamicallyConstrainedStarMatter", "MHDCSM"),
        Map.entry("NMethylpyrolidone", "NMethylIIPyrrolidone"),
        Map.entry("NULL", "_NULL"),
        Map.entry("NatruralGas", "NaturalGas"),
        Map.entry("Pentacadmiummagnesiumhexaoxid", "SuperconductorMVBase"),
        Map.entry("PhosphoricAcid_GT5U", "PhosphoricAcid"),
        Map.entry("Plastic", "Polyethylene"),
        Map.entry("PolyaluminiumChloride", "PolyAluminiumChloride"),
        Map.entry("PolyurethaneCatalystADust", "KevlarCatalyst"),
        Map.entry("PotassiumDichromate", "Potassiumdichromate"),
        Map.entry("PotassiumHydroxide_GT5U", "PotassiumHydroxide"),
        Map.entry("PotassiumNitrate", "PotassiumNitrade"),
        Map.entry("RadoxPoly", "RadoxPolymer"),
        Map.entry("RawGasoline", "GasolineRaw"),
        Map.entry("RawRadox", "RadoxRaw"),
        Map.entry("RawRubber", "RubberRaw"),
        Map.entry("SiliconSolarGrade", "SiliconSG"),
        Map.entry("Silicone", "RubberSilicone"),
        Map.entry("SodiumHydroxide_GT5U", "SodiumHydroxide"),
        Map.entry("SuperHeavyRadox", "RadoxSuperHeavy"),
        Map.entry("SuperLightRadox", "RadoxSuperLight"),
        Map.entry("TPVAlloy", "TPV"),
        Map.entry("Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid", "SuperconductorLuVBase"),
        Map.entry("Tetranaquadahdiindiumhexaplatiumosminid", "SuperconductorZPMBase"),
        Map.entry("Titaniumonabariumdecacoppereikosaoxid", "SuperconductorHVBase"),
        Map.entry("Uraniumtriplatinid", "SuperconductorEVBase"),
        Map.entry("Vanadiumtriindinid", "SuperconductorIVBase"),
        Map.entry("Water", "Steam"),
        Map.entry("activatednetherite", "ActivatedNetherite"),
        Map.entry("carbonactivateddirty", "DirtyActivatedCarbon"),
        Map.entry("dimensionallyshiftedsuperfluid", "DimensionallyShiftedSuperfluid"),
        Map.entry("exohalkonite", "ExoHalkonite"),
        Map.entry("hotexohalkonite", "HotExoHalkonite"),
        Map.entry("hotprotohalkonite", "HotProtoHalkonite"),
        Map.entry("lifeessence", "LifeEssence"),
        Map.entry("moltenexohalkonitebase", "MoltenExoHalkoniteBase"),
        Map.entry("nefariousgas", "NefariousGas"),
        Map.entry("nefariousoil", "NefariousOil"),
        Map.entry("netherair", "NetherAir"),
        Map.entry("nethersemifluid", "NetherSemiFluid"),
        Map.entry("pPhenylenediamine", "ParaPhenylenediamine"),
        Map.entry("phtalicacid", "PhthalicAcid"),
        Map.entry("poornetherwaste", "PoorNetherWaste"),
        Map.entry("prismarinecontaminatedhydrogenperoxide", "PrismarineContaminatedHydrogenPeroxide"),
        Map.entry("prismarinecontaminatednitrobenzenesolution", "PrismarineContaminatedNitrobenzeSolution"),
        Map.entry("prismarinerichnitrobenzenesolution", "PrismarineRichNitrobenzeneSolution"),
        Map.entry("prismarinesolution", "PrismarineSolution"),
        Map.entry("prismaticacid", "PrismaticAcid"),
        Map.entry("prismaticgas", "PrismaticGas"),
        Map.entry("prismaticnaquadah", "PrismaticNaquadah"),
        Map.entry("prismaticnaquadahcompositeslurry", "PrismaticNaquadahCompositeSlurry"),
        Map.entry("protohalkonite", "ProtoHalkonite"),
        Map.entry("protohalkonitebase", "MoltenProtoHalkoniteBase"),
        Map.entry("richnetherwaste", "RichNetherWaste"),
        Map.entry("sgcrystalslurry", "StargateCrystalSlurry"),
        Map.entry("spatialFluid", "Space"),
        Map.entry("stablebaryonicmatter", "StableBaryonicMatter"),
        Map.entry("temporalFluid", "Time")
    );
    // spotless:on

    private static final Map<String, String> FIELD_NAME_TO_ML_NAME_OVERRIDES = invert(ML_NAME_TO_FIELD_OVERRIDES);

    private static Map<String, String> invert(Map<String, String> map) {
        Map<String, String> inverted = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) inverted.put(entry.getValue(), entry.getKey());
        return inverted;
    }

    /// Legacy field names built from MaterialLib data, in [Materials2Materials]'s original declaration order.
    /// Needed by [#loadStubs], where the registry is empty and the fields to stub can't be discovered by walking
    /// it.
    // spotless:off
    private static final String[] BRIDGED_FIELD_NAMES = {
        "Carbon", "Hydrogen", "Oxygen", "Acetaldehyde", "AceticAcid", "Acetone", "Acetylene", "ActivatedCarbon",
        "ActivatedNetherite", "ActivatedWasteWater", "Adamantium", "Adluorite", "AgitatingSlurry", "Argon",
        "Nitrogen", "CarbonDioxide", "Deuterium", "Helium", "Methane", "NobleGases", "Air", "Alduorite",
        "Chlorine", "AllylChloride", "Aluminium", "Iron", "Silicon", "Almandine", "AluminiumBrass", "Fluorine",
        "AluminiumFluoride", "Aluminiumhydroxide", "Aluminiumoxide", "Magnesium", "Obsidian", "Steel", "Zinc",
        "Alumite", "Potassium", "Alunite", "Amalgatite", "Amber", "Americium", "SiliconDioxide", "Amethyst",
        "Ammonia", "Amordrine", "Calcium", "Andradite", "Copper", "AnnealedCopper", "AntiKnock", "Antimatter",
        "Antimony", "AntimonyTrioxide", "Phosphorus", "Phosphate", "Apatite", "AquaRegia", "Ardite", "Aredrite",
        "Arsenic", "ArsenicTrioxide", "Asbestos", "Ash", "AshDark", "Silver", "Magic", "Thaumium", "AstralSilver",
        "BandedIron", "Barium", "Sulfur", "Barite", "Calcite", "Flint", "Olivine", "Basalt", "Magnetite",
        "BasalticMineralSand", "Cerium", "Bastnasite", "Lead", "BatteryAlloy", "Titanium", "Rutile", "Bauxite",
        "BauxiteSlag", "BauxiteSlurry", "Diamond", "Bedrockium", "Sodium", "Steam", "Bentonite", "Benzene",
        "Beryllium", "BioDiesel", "BioMediumRaw", "BioMediumSterilized", "BiocatalyzedPropulsionFluid", "Biomass",
        "Biotite", "Bismuth", "BismuthBronze", "BismuthIIIOxide", "BisphenolA", "Gold", "BlackBronze",
        "BlackDwarfMatter", "BlackPlutonium", "Nickel", "BlackSteel", "Blaze", "Blizz", "BloodInfusedIron",
        "Electrum", "Mercury", "Pyrite", "Chrome", "Ruby", "Redstone", "Electrotine", "BlueAlloy", "Brass",
        "RoseGold", "BlueSteel", "BlueTopaz", "Bluestone", "Blutonium", "Bone", "Boron", "Borax", "Glass",
        "BorosilicateGlass", "BoundlessCosmicSolder", "Brick", "Tin", "Bronze", "BrownLimonite", "Butadiene",
        "Butane", "Butene", "Butyraldehyde", "Cadmium", "Caesium", "CaesiumHydroxide", "CalciumAcetateSolution",
        "CalciumCarbide", "CalciumDisilicide", "Calciumhydride", "CallistoIce", "CarbonMonoxide", "Cassiterite",
        "CassiteriteSand", "CastIron", "CertusQuartz", "CertusQuartzCharged", "Ceruclase", "Chalcopyrite",
        "Charcoal", "CharcoalByproducts", "Cheese", "Chili", "Chloramine", "Chlorite", "Chlorobenzene",
        "Chloroform", "Chloromethane", "ChlorosulfonicAcid", "Chocolate", "Chromite", "ChromiumDioxide",
        "ChromiumTrioxide", "Chrysotile", "Churitsu", "Cinnabar", "Lithium", "Clay", "Stone", "ClayCompound",
        "Coal", "CoalFuel", "Cobalt", "CobaltBrass", "CobaltHexahydrate", "CobaltIIAcetate", "CobaltIIHydroxide",
        "CobaltIINaphthenate", "CobaltIINitrate", "CobaltOxide", "Cobaltite", "Cocoa", "Coffee",
        "ComplexityCatalyst", "ComputationBase", "Concrete", "RedstoneAlloy", "ConductiveIron", "ConstructionFoam",
        "Palladium", "Platinum", "Cooperite", "Cordierite", "CosmicNeutronium", "Creon", "Creosote", "CrudeOil",
        "Cryolite", "Lazurite", "Sodalite", "Lapis", "Saltpeter", "Snow", "Cryotheum", "EnderPearl",
        "PulsatingIron", "CrystallineAlloy", "CrystallinePinkSlime", "Cumene", "CupricOxide", "Cupronickel",
        "DTCC", "DTEC", "DTPC", "DTR", "DTRC", "DTSC", "DamascusSteel", "DarkIron", "ElectricalSteel", "DarkSteel",
        "DarkThaumium", "Datolite", "DeepIron", "DenseSteam", "DenseSupercriticalSteam", "DenseSuperheatedSteam",
        "Desh", "Desichalkos", "DestabilizationSlurry", "Diaminobenzidin", "DiaminodiphenylmethanMixture",
        "Sapphire", "Diatomite", "Dichlorobenzene", "Dichlorobenzidine", "Dichlorosilane", "Diesel", "Dilithium",
        "DilutedHydrochloricAcid", "SulfuricAcid", "DilutedSulfuricAcid", "DimensionallyShiftedSuperfluid",
        "DimethylTerephthalate", "Dimethylamine", "Dimethylbenzene", "Dimethyldichlorosilane", "Dimethylhydrazine",
        "DinitrogenTetroxide", "Diphenylisophthalate", "DiphenylmethaneDiisocyanate",
        "DiphenylmethaneDiisocyanateMixture", "PhosphoricAcid", "DirtyActivatedCarbon", "Dolomite", "Draconium",
        "DraconiumAwakened", "Dragonstone", "Dreamwood", "Manganese", "Duralumin", "Duranium", "Dysprosium",
        "ElectrumFlux", "ElvenElementium", "Emerald", "Emery", "Empty", "Endstone", "Tungsten", "EndSteel",
        "EnderEye", "EnderiumBase", "Enderium", "Endium", "EnergeticAlloy", "EnergeticSilver",
        "EnrichedBacterialSludge", "EnrichedHolmium", "EntropicCatalyst", "Epichlorohydrin", "Epidote", "Epoxid",
        "EpoxidFiberReinforced", "Erbium", "Eternity", "Ethane", "Ethanol", "Ethenone", "Ethylene",
        "EthyleneOxide", "Ethyleneglycol", "Europium", "ExcitedDTCC", "ExcitedDTEC", "ExcitedDTPC", "ExcitedDTRC",
        "ExcitedDTSC", "ExoHalkonite", "FermentedBacterialSludge", "FermentedBiomass", "FerriteMixture",
        "Ferrosilite", "FierySteel", "Fireclay", "Firestone", "FishOil", "Flerovium", "FlocculationWasteLiquid",
        "Force", "Forcicium", "Forcillium", "Francium", "FranciumHydroxide", "FranciumSlurry", "FreshWater",
        "FryingOilHot", "FullersEarth", "Gadolinium", "GaiaSpirit", "Galena", "Galgadorian", "GalgadorianEnhanced",
        "Gallium", "GalliumArsenide", "GammaButyrolactone", "Pyrope", "Spessartine", "GarnetRed", "Grossular",
        "Uvarovite", "GarnetYellow", "GarnetSand", "Garnierite", "Gas", "GasolinePremium", "GasolineRaw",
        "GasolineRegular", "Glauconite", "GlauconiteSand", "Glowstone", "Glue", "GlueAdvanced", "Glycerol",
        "Glyceryl", "Grade1PurifiedWater", "Grade2PurifiedWater", "Grade3PurifiedWater", "Grade4PurifiedWater",
        "Grade5PurifiedWater", "Grade6PurifiedWater", "Grade7PurifiedWater", "Grade8PurifiedWater", "GraniteBlack",
        "PotassiumFeldspar", "GraniteRed", "GraniticMineralSand", "Graphene", "Graphite", "GravitonShard",
        "GreenSapphire", "GreenSapphireJuice", "Greenstone", "GrowthMediumRaw", "GrowthMediumSterilized",
        "Gunpowder", "Gypsum", "HSLA", "Molybdenum", "TungstenSteel", "Vanadium", "HSSG", "HSSE", "Iridium",
        "Osmium", "HSSS", "HeatedBauxiteSlurry", "HeavyFuel", "Helium3", "HellishMetal", "Hematite",
        "Hexachlorodisilane", "Hexanite", "Holmium", "HolyWater", "Honey", "HotExoHalkonite", "HotProtoHalkonite",
        "HydratedCoal", "HydricSulfide", "HydrochloricAcid", "HydrofluoricAcid", "HypochlorousAcid",
        "IIButinIIVdiol", "IIIDimethylbenzene", "IVDimethylbenzene", "IVNitroaniline", "Ice", "Ichorium",
        "Ilmenite", "IlmeniteSlag", "ImpureFranciumSolution", "InactiveCosmicSolder", "Indium",
        "IndiumGalliumPhosphide", "Infinity", "InfinityCatalyst", "InfusedAir", "InfusedEarth", "InfusedEntropy",
        "InfusedFire", "InfusedGold", "InfusedOrder", "InfusedWater", "Invar", "IronIIIChloride", "IronMagnetic",
        "Wood", "LiveRoot", "IronWood", "Isobutyraldehyde", "Isoprene", "Jade", "Jasper", "Kanthal", "Kaolinite",
        "Kevlar", "KevlarCatalyst", "Knightmetal", "Kyanite", "LPG", "Lanthanum", "Lava", "Ledox", "Lepidolite",
        "LifeEssence", "LightFuel", "Lignite", "LiquidAir", "LiquidCrystalKevlar", "LiquidNitrogen",
        "LiquidOxygen", "Livingrock", "Livingwood", "Lubricant", "LumipodExtract", "Lumium", "Lutetium", "MHDCSM",
        "MTBEMixture", "MTBEMixtureAlt", "MagMatter", "Magnalium", "Magnesia", "Magnesite", "Magnesiumchloride",
        "Malachite", "ManaDiamond", "Manasteel", "Manyullyn", "Marble", "Massicot", "McGuffium239", "MeatCooked",
        "MeatRaw", "Mellion", "Oriharukon", "MelodicAlloy", "Mercassium", "MetalMixture",
        "MetamorphicMineralMixture", "MeteoricIron", "MeteoricSteel", "Methanol", "MethylAcetate", "Methylamine",
        "Mica", "Migmatite", "Milk", "Mirabilite", "Mithril", "MoltenExoHalkoniteBase", "MoltenProtoHalkoniteBase",
        "Molybdenite", "RareEarth", "Monazite", "MysteriousCrystal", "Mytryl", "NMethylIIPyrrolidone", "Naphtha",
        "NaphthenicAcid", "Naquadah", "NaquadahAlloy", "NaquadahEnriched", "Naquadria", "NaturalGas",
        "NefariousGas", "NefariousOil", "Neodymium", "NeodymiumMagnetic", "NetherAir", "NetherBrick",
        "NetherQuartz", "NetherSemiFluid", "NetherStar", "Netherite", "Netherrack", "Neutronium", "Nichrome",
        "NickelAluminide", "NickelTetracarbonyl", "NickelZincFerrite", "Niobium", "NiobiumNitride",
        "NiobiumTitanium", "Niter", "NitrationMixture", "NitricAcid", "NitricOxide", "NitroCarbon",
        "NitroCoalFuel", "NitroFuel", "Nitrochlorobenzene", "NitrogenDioxide", "NitrousOxide", "ObsidianFlux",
        "Octane", "Oil", "OilExtraHeavy", "OilHeavy", "OilLight", "OilMedium", "Oilsands", "Opal",
        "OrganorhodiumCatalyst", "Orichalcum", "Osmiridium", "Osmonium", "Ozone", "Paper", "ParaPhenylenediamine",
        "Pentaerythritol", "Pentlandite", "Perlite", "PhasedGold", "PhasedIron", "Phenol", "Phoenixite",
        "PhononCrystalSolution", "PhononMedium", "PhosphorousPentoxide", "PhosphorusChlorineMixture",
        "PhosphorusPentachloride", "PhosphorusTrichloride", "PhthalicAcid", "PigIron", "Thorium", "Uranium",
        "Uraninite", "Pitchblende", "Plagioclase", "PlatinumGroupSludge", "Plutonium", "Plutonium241",
        "PoisonousSlurry", "Pollucite", "Pollution", "PolyAluminiumChloride", "Polybenzimidazole",
        "Polycaprolactam", "Polydimethylsiloxane", "Polyethylene", "PolyphenyleneSulfide", "Polystyrene",
        "Polytetrafluoroethylene", "PolyurethaneResin", "PolyvinylAcetate", "PolyvinylChloride", "PoorNetherWaste",
        "Potash", "PotassiumHydroxide", "PotassiumNitrade", "Potassiumdichromate", "Powellite", "Praseodymium",
        "PreActivatedCarbon", "PrimordialMatter", "PrismarineContaminatedHydrogenPeroxide",
        "PrismarineContaminatedNitrobenzeSolution", "PrismarineRichNitrobenzeneSolution", "PrismarineSolution",
        "PrismaticAcid", "PrismaticGas", "PrismaticNaquadah", "PrismaticNaquadahCompositeSlurry", "Promethium",
        "Propane", "Propene", "ProtoHalkonite", "Protomatter", "Pumice", "Pyrochlore", "Pyrolusite", "Pyrotheum",
        "Quantium", "QuarkGluonPlasma", "Quartzite", "QuartzSand", "Quicklime", "Radon", "RadoxCracked",
        "RadoxGas", "RadoxHeavy", "RadoxLight", "RadoxPolymer", "RadoxRaw", "RadoxSuperHeavy", "RadoxSuperLight",
        "RaneyNickelActivated", "RawStarMatter", "Styrene", "RawStyreneButadieneRubber", "Realgar", "RedAlloy",
        "RedMud", "SterlingSilver", "RedSteel", "Redrock", "Reinforced", "ReinforcedGlass", "RhodiumChloride",
        "RichNetherWaste", "RoastedAntimony", "RoastedArsenic", "RoastedCobalt", "RoastedCopper", "RoastedIron",
        "RoastedLead", "RoastedNickel", "RoastedZinc", "RockSalt", "Rubber", "RubberRaw", "RubberSilicone",
        "Rubidium", "Rubracium", "RubyJuice", "Salt", "SaltWater", "Samarium", "SamariumMagnetic", "SapphireJuice",
        "Scandium", "Scheelite", "SeedOil", "SeedOilHemp", "SeedOilLin", "Serpentine", "Shadow", "ShadowIron",
        "ShadowSteel", "Shijima", "Signalum", "Silane", "SiliconOil", "SiliconSG", "SiliconTetrachloride",
        "SiliconTetrafluoride", "Siltstone", "SixPhasedCopper", "SluiceJuice", "SluiceSand", "Soapstone",
        "SodaAsh", "SodiumAluminate", "SodiumBisulfate", "SodiumBorohydride", "SodiumCarbonate", "SodiumHydride",
        "SodiumHydroxide", "SodiumMethoxide", "SodiumOxide", "SodiumPersulfate", "SodiumPotassium",
        "SodiumSulfide", "SolderingAlloy", "SoulInfusedMedium", "SoulSand", "Soularium", "Space", "SpaceTime",
        "Sphalerite", "Spinel", "Spodumene", "StableBaryonicMatter", "StagnantWasteWater", "StainlessSteel",
        "StargateCrystalSlurry", "Staurolite", "SteelMagnetic", "Steeleaf", "StellarAlloy", "Stibnite",
        "Strontium", "StyreneButadieneRubber", "Sugar", "SulfurDichloride", "SulfurDioxide", "SulfurTrioxide",
        "SulfuricGas", "SulfuricHeavyFuel", "SulfuricLightFuel", "SulfuricNaphtha", "Sunnarium", "SuperCoolant",
        "SuperconductorEVBase", "SuperconductorHVBase", "SuperconductorIVBase", "SuperconductorLuVBase",
        "SuperconductorMVBase", "SuperconductorUEVBase", "Tritanium", "SuperconductorUHVBase",
        "SuperconductorUIVBase", "SuperconductorUMVBase", "SuperconductorUVBase", "SuperconductorZPMBase", "TPV",
        "Talc", "Tantalum", "Tantalite", "Tanzanite", "Tartarite", "Tellurium", "TengamAttuned", "TengamPurified",
        "TengamRaw", "Terbium", "TerephthalicAcid", "TerephthaloylChloride", "Terrasteel", "Teslatite",
        "Tetrafluoroethylene", "Tetrahedrite", "Tetranitromethane", "ThionylChloride", "ThoriumElutionAdsorbent",
        "Thulium", "Time", "TinAlloy", "Titaniumtetrachloride", "Toluene", "Topaz", "ToxicAir", "ToxicSlurry",
        "TranscendentMetal", "TricalciumPhosphate", "Trichlorosilane", "TrimethylBorate", "Trimethylamine",
        "Trinium", "Triphenylphosphene", "Tritium", "Trona", "Tungstate", "TungstenCarbide", "UUAmplifier",
        "UUMatter", "Ultimet", "UltraContaminatedGas", "UnformedHexanite", "Universium", "Unstable", "Uranium235",
        "VanadiumGallium", "VanadiumMagnetite", "VanadiumSteel", "Vermiculite", "VibrantAlloy", "Vinegar",
        "Vinteum", "VinylAcetate", "VinylChloride", "Vis", "VividAlloy", "Void", "VolcanicAsh", "Vulcanite",
        "Vyroxeres", "WeedEX9000", "Wheat", "WhiteDwarfMatter", "Wollastonite", "WoodGas", "WoodSealed", "WoodTar",
        "WoodVinegar", "Wulfenite", "Xenoxene", "XenoxeneDiluted", "Yellorium", "YellowLimonite", "Ytterbium",
        "Yttrium", "YttriumBariumCuprate", "Zectium", "Zeolite", "Zincite", "_NULL"
    };
    // spotless:on

    /// The two [#BRIDGED_FIELD_NAMES] whose bare-JUnit stub name is not their ML material name: MaterialLib's
    /// `LEGACY_NAME` for these predates identifier sanitization and still carries a space.
    private static final Map<String, String> STUB_NAME_OVERRIDES = Map
        .of("ComputationBase", "Computation Base", "UnformedHexanite", "Unformed Hexanite");

    public static void load() {
        LegacyMarkerMaterials.loadMarkers();
        // A bare JUnit run never fires MaterialLib's MaterialRegistrationEvent, so
        // Materials2Materials fields stay null outside a real FML mod-loading lifecycle.
        if (Materials2Materials.Iron == null) {
            loadStubs();
            return;
        }
        for (String fieldName : BRIDGED_FIELD_NAMES) {
            String mlName = mlNameOf(fieldName);
            Material material = MaterialLibAPI.getMaterial("gregtech", mlName);
            if (material == null) {
                throw new IllegalStateException(
                    "No gregtech MaterialLib material named " + mlName + " for legacy field Materials." + fieldName);
            }
            setField(FIELDS_BY_NAME.get(fieldName), LegacyMaterials.build(material));
        }
        Materials.Water = Materials.Steam;
        Materials.WroughtIron = Materials.CastIron;
    }

    private static void loadStubs() {
        for (String fieldName : BRIDGED_FIELD_NAMES) {
            String mlName = mlNameOf(fieldName);
            String stubName = STUB_NAME_OVERRIDES.getOrDefault(mlName, mlName);
            setField(FIELDS_BY_NAME.get(fieldName), LegacyMaterials.stub(stubName));
        }
        Materials.Water = Materials.Steam;
        Materials.WroughtIron = Materials.CastIron;
    }

    private static String mlNameOf(String fieldName) {
        return FIELD_NAME_TO_ML_NAME_OVERRIDES.getOrDefault(fieldName, fieldName);
    }

    private static void setField(Field field, Materials value) {
        try {
            field.set(null, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to set legacy field " + field.getName(), e);
        }
    }

    private MaterialsLegacyBridge() {}
}
