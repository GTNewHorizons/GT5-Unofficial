package gregtech.loaders.materials;

import net.minecraft.enchantment.Enchantment;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TextureSet;

/// The legacy `Materials` fields built directly rather than through the MaterialLib port:
/// materials with no generated items, fluids, or composition of their own, and never referenced by a ported
/// material's composition/ore-byproduct/smelt-macerate-arc-smelt-direct-smelt links (`gen_materials.py`'s
/// marker-skip rule -- see `scripts/mu/gen_materials.py`'s `compute_ported`). MaterialLib carries no data for
/// these, so their original `MaterialBuilder` declarations are reproduced verbatim rather than derived; they
/// still need to exist because unchanged legacy code references some of them directly (e.g.
/// `Materials#setReRegistration`'s `AnyIron`/`AnyCopper`/`AnyBronze`/`AnyRubber`/`AnySyntheticRubber`/
/// `AnyCarbon`).
public class LegacyMarkerMaterials {

    private LegacyMarkerMaterials() {}

    public static void loadMarkers() {
        loadRandomMarkers();
        loadDontCareMarkers();
        loadUnknownComponentsMarkers();
        loadTiersMarkers();
        loadCircuitryMarkers();
        loadNotExactMarkers();
        loadSuperconductorsMarkers();
        loadGTNHMaterialsMarkers();
    }

    private static void loadRandomMarkers() {
        Materials.AnyBronze = loadAnyBronze();
        Materials.AnyCopper = loadAnyCopper();
        Materials.AnyCarbon = loadAnyCarbon();
        Materials.AnyIron = loadAnyIron();
        Materials.AnyRubber = loadAnyRubber();
        Materials.AnySyntheticRubber = loadAnySyntheticRubber();
        Materials.BrickNether = loadBrickNether();
        Materials.Cobblestone = loadCobblestone();
        Materials.Crystal = loadCrystal();
        Materials.Metal = loadMetal();
        Materials.Organic = loadOrganic();
        Materials.Quartz = loadQuartz();
        Materials.Unknown = loadUnknown();
    }

    private static Materials loadAnyBronze() {
        return new MaterialBuilder().setName("AnyBronze")
            .setDefaultLocalName("AnyBronze")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAnyCopper() {
        return new MaterialBuilder().setName("AnyCopper")
            .setDefaultLocalName("AnyCopper")
            .setChemicalFormula("Cu")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .setSmeltingInto(() -> Materials.Copper)
            .setMaceratingInto(() -> Materials.Copper)
            .setArcSmeltingIntoWithGas(() -> Materials.Oxygen, () -> Materials.AnnealedCopper)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAnyCarbon() {
        return new MaterialBuilder().setName("AnyCarbon")
            .setDefaultLocalName("AnyCarbon")
            .setChemicalFormula("C")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static Materials loadAnyIron() {
        return new MaterialBuilder().setName("AnyIron")
            .setDefaultLocalName("AnyIron")
            .setChemicalFormula("Fe")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .setSmeltingInto(() -> Materials.Iron)
            .setMaceratingInto(() -> Materials.Iron)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadAnyRubber() {
        return new MaterialBuilder().setName("AnyRubber")
            .setDefaultLocalName("AnyRubber")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .setMaceratingInto(() -> Materials.Rubber)
            .constructMaterial();
    }

    private static Materials loadAnySyntheticRubber() {
        return new MaterialBuilder().setName("AnySyntheticRubber")
            .setDefaultLocalName("AnySyntheticRubber")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    private static Materials loadBrickNether() {
        return new MaterialBuilder().setName("BrickNether")
            .setDefaultLocalName("BrickNether")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_DULL)
            .removeOrePrefix(OrePrefixes.ingot) // minecraft:netherbrick
            .constructMaterial();
    }

    private static Materials loadCobblestone() {
        return new MaterialBuilder().setName("Cobblestone")
            .setDefaultLocalName("Cobblestone")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static Materials loadCrystal() {
        return new MaterialBuilder().setName("Crystal")
            .setDefaultLocalName("Crystal")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_SHINY)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadMetal() {
        return new MaterialBuilder().setName("Metal")
            .setDefaultLocalName("Metal")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_METALLIC)
            .addSubTag(SubTag.METAL)
            .constructMaterial();
    }

    private static Materials loadOrganic() {
        return new MaterialBuilder().setName("Organic")
            .setDefaultLocalName("Organic")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_LEAF)
            .constructMaterial();
    }

    private static Materials loadQuartz() {
        return new MaterialBuilder().setName("Quartz")
            .setDefaultLocalName("Quartz")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_QUARTZ)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .constructMaterial();
    }

    private static Materials loadUnknown() {
        return new MaterialBuilder().setName("Unknown")
            .setDefaultLocalName("Unknown")
            .setUnifiable(false)
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static void loadDontCareMarkers() {
        Materials.Alfium = loadAlfium();
        Materials.Aquamarine = loadAquamarine();
        Materials.Ender = loadEnder();
        Materials.Fluix = loadFluix();
        Materials.Flux = loadFlux();
        Materials.InfusedTeslatite = loadInfusedTeslatite();
        Materials.IridiumSodiumOxide = loadIridiumSodiumOxide();
        Materials.Mutation = loadMutation();
        Materials.OsmiumTetroxide = loadOsmiumTetroxide();
        Materials.PurpleAlloy = loadPurpleAlloy();
        Materials.RubberTreeSap = loadRubberTreeSap();
        Materials.SodiumPeroxide = loadSodiumPeroxide();
    }

    private static Materials loadAlfium() {
        return new MaterialBuilder().setName("Alfium")
            .setDefaultLocalName("Alfium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadAquamarine() {
        return new MaterialBuilder().setName("Aquamarine")
            .setDefaultLocalName("Aquamarine")
            .addDustItems()
            .addGemItems()
            .constructMaterial();
    }

    private static Materials loadEnder() {
        return new MaterialBuilder().setName("Ender")
            .setDefaultLocalName("Ender")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadFluix() {
        return new MaterialBuilder().setName("Fluix")
            .setDefaultLocalName("Fluix")
            .addDustItems()
            .addGemItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.CRYSTALLISABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.QUARTZ)
            .constructMaterial();
    }

    private static Materials loadFlux() {
        return new MaterialBuilder().setName("Flux")
            .setDefaultLocalName("Flux")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadInfusedTeslatite() {
        return new MaterialBuilder().setName("InfusedTeslatite")
            .setDefaultLocalName("Infused Teslatite")
            .setARGB(0x0064b4ff)
            .constructMaterial();
    }

    private static Materials loadIridiumSodiumOxide() {
        return new MaterialBuilder().setName("IridiumSodiumOxide")
            .setDefaultLocalName("Iridium Sodium Oxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMutation() {
        return new MaterialBuilder().setName("Mutation")
            .setDefaultLocalName("Mutation")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadOsmiumTetroxide() {
        return new MaterialBuilder().setName("OsmiumTetroxide")
            .setDefaultLocalName("Osmium Tetroxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadPurpleAlloy() {
        return new MaterialBuilder().setName("PurpleAlloy")
            .setDefaultLocalName("Purple Alloy")
            .setARGB(0x0064b4ff)
            .constructMaterial();
    }

    private static Materials loadRubberTreeSap() {
        return new MaterialBuilder().setName("RubberTreeSap")
            .setDefaultLocalName("Rubber Tree Sap")
            .constructMaterial();
    }

    private static Materials loadSodiumPeroxide() {
        return new MaterialBuilder().setName("SodiumPeroxide")
            .setDefaultLocalName("Sodium Peroxide")
            .addDustItems()
            .constructMaterial();
    }

    private static void loadUnknownComponentsMarkers() {
        Materials.Adamite = loadAdamite();
        Materials.Agate = loadAgate();
        Materials.Ammonium = loadAmmonium();
        Materials.AncientDebris = loadAncientDebris();
        Materials.Andesite = loadAndesite();
        Materials.Bitumen = loadBitumen();
        Materials.Black = loadBlack();
        Materials.Bloodstone = loadBloodstone();
        Materials.Chimerite = loadChimerite();
        Materials.Chrysocolla = loadChrysocolla();
        Materials.Citrine = loadCitrine();
        Materials.Coral = loadCoral();
        Materials.CrystalFlux = loadCrystalFlux();
        Materials.Cyanite = loadCyanite();
        Materials.DarkStone = loadDarkStone();
        Materials.Demonite = loadDemonite();
        Materials.Draconic = loadDraconic();
        Materials.Drulloy = loadDrulloy();
        Materials.Energized = loadEnergized();
        Materials.Fluorite = loadFluorite();
        Materials.Infernal = loadInfernal();
        Materials.InfusedDull = loadInfusedDull();
        Materials.InfusedVis = loadInfusedVis();
        Materials.Invisium = loadInvisium();
        Materials.Limestone = loadLimestone();
        Materials.Magma = loadMagma();
        Materials.Mawsitsit = loadMawsitsit();
        Materials.Meteorite = loadMeteorite();
        Materials.Mimichite = loadMimichite();
        Materials.Moonstone = loadMoonstone();
        Materials.Nether = loadNether();
        Materials.Onyx = loadOnyx();
        Materials.Painite = loadPainite();
        Materials.Peanutwood = loadPeanutwood();
        Materials.Petroleum = loadPetroleum();
        Materials.Pewter = loadPewter();
        Materials.Randomite = loadRandomite();
        Materials.Sand = loadSand();
        Materials.Sunstone = loadSunstone();
        Materials.Tar = loadTar();
        Materials.Voidstone = loadVoidstone();
    }

    private static Materials loadAdamite() {
        return new MaterialBuilder().setName("Adamite")
            .setDefaultLocalName("Adamite")
            .setMiningLevel(3)
            .setColor(Dyes.dyeLightGray)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadAgate() {
        return new MaterialBuilder().setName("Agate")
            .setDefaultLocalName("Agate")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadAmmonium() {
        return new MaterialBuilder().setName("Ammonium")
            .setDefaultLocalName("Ammonium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadAncientDebris() {
        return new MaterialBuilder().setName("Debris")
            .setDefaultLocalName("Ancient Debris")
            .setARGB(0x351a0b)
            .constructMaterial();
    }

    private static Materials loadAndesite() {
        return new MaterialBuilder().setName("Andesite")
            .setDefaultLocalName("Andesite")
            .setMiningLevel(2)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadBitumen() {
        return new MaterialBuilder().setName("Bitumen")
            .setDefaultLocalName("Bitumen")
            .setMiningLevel(2)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadBlack() {
        return new MaterialBuilder().setName("Black")
            .setDefaultLocalName("Black")
            .setColor(Dyes.dyeBlack)
            .setARGB(0x00000000)
            .constructMaterial();
    }

    private static Materials loadBloodstone() {
        return new MaterialBuilder().setName("Bloodstone")
            .setDefaultLocalName("Bloodstone")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadChimerite() {
        return new MaterialBuilder().setName("Chimerite")
            .setDefaultLocalName("Chimerite")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadChrysocolla() {
        return new MaterialBuilder().setName("Chrysocolla")
            .setDefaultLocalName("Chrysocolla")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCitrine() {
        return new MaterialBuilder().setName("Citrine")
            .setDefaultLocalName("Citrine")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCoral() {
        return new MaterialBuilder().setName("Coral")
            .setDefaultLocalName("Coral")
            .setARGB(0x00ff80ff)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCrystalFlux() {
        return new MaterialBuilder().setName("CrystalFlux")
            .setDefaultLocalName("Flux Crystal")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setARGB(0x00643264)
            .addDustItems()
            .addGemItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadCyanite() {
        return new MaterialBuilder().setName("Cyanite")
            .setDefaultLocalName("Cyanite")
            .setColor(Dyes.dyeCyan)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDarkStone() {
        return new MaterialBuilder().setName("DarkStone")
            .setDefaultLocalName("Dark Stone")
            .setColor(Dyes.dyeBlack)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDemonite() {
        return new MaterialBuilder().setName("Demonite")
            .setDefaultLocalName("Demonite")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDraconic() {
        return new MaterialBuilder().setName("Draconic")
            .setDefaultLocalName("Draconic")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDrulloy() {
        return new MaterialBuilder().setName("Drulloy")
            .setDefaultLocalName("Drulloy")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadEnergized() {
        return new MaterialBuilder().setName("Energized")
            .setDefaultLocalName("Energized")
            .constructMaterial();
    }

    private static Materials loadFluorite() {
        return new MaterialBuilder().setName("Fluorite")
            .setDefaultLocalName("Fluorite")
            .setColor(Dyes.dyeGreen)
            .setMiningLevel(2)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadInfernal() {
        return new MaterialBuilder().setName("Infernal")
            .setDefaultLocalName("Infernal")
            .constructMaterial();
    }

    private static Materials loadInfusedDull() {
        return new MaterialBuilder().setName("InfusedDull")
            .setDefaultLocalName("Vacuus")
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00646464)
            .setTool(64, 3, 32.0f)
            .setArmorEnchantment(() -> Enchantment.blastProtection, 4)
            .setFuel(MaterialBuilder.FuelType.Magic, 160)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.VACUOS, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInfusedVis() {
        return new MaterialBuilder().setName("InfusedVis")
            .setDefaultLocalName("Auram")
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00ff00ff)
            .setTool(64, 3, 8.0f)
            .setToolEnchantment(() -> Enchantment.smite, 5)
            .setArmorEnchantment(() -> Enchantment.protection, 4)
            .setFuel(MaterialBuilder.FuelType.Magic, 240)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setOreMultiplier(2)
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.AURAM, 2)
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.MAGICAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.TRANSPARENT)
            .addSubTag(SubTag.UNBURNABLE)
            .constructMaterial();
    }

    private static Materials loadInvisium() {
        return new MaterialBuilder().setName("Invisium")
            .setDefaultLocalName("Invisium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadLimestone() {
        return new MaterialBuilder().setName("Limestone")
            .setDefaultLocalName("Limestone")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMagma() {
        return new MaterialBuilder().setName("Magma")
            .setDefaultLocalName("Magma")
            .setColor(Dyes.dyeOrange)
            .setARGB(0x00ff4000)
            .constructMaterial();
    }

    private static Materials loadMawsitsit() {
        return new MaterialBuilder().setName("Mawsitsit")
            .setDefaultLocalName("Mawsitsit")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMeteorite() {
        return new MaterialBuilder().setName("Meteorite")
            .setDefaultLocalName("Meteorite")
            .setColor(Dyes.dyePurple)
            .setARGB(0x0050233c)
            .setMiningLevel(1)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadMimichite() {
        return new MaterialBuilder().setName("Mimichite")
            .setDefaultLocalName("Mimichite")
            .setIconSet(TextureSet.SET_GEM_VERTICAL)
            .setMiningLevel(1)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addSubTag(SubTag.CRYSTAL)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadMoonstone() {
        return new MaterialBuilder().setName("Moonstone")
            .setDefaultLocalName("Moonstone")
            .setColor(Dyes.dyeWhite)
            .setMiningLevel(1)
            .addDustItems()
            .addOreItems()
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadNether() {
        return new MaterialBuilder().setName("Nether")
            .setDefaultLocalName("Nether")
            .constructMaterial();
    }

    private static Materials loadOnyx() {
        return new MaterialBuilder().setName("Onyx")
            .setDefaultLocalName("Onyx")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadPainite() {
        return new MaterialBuilder().setName("Painite")
            .setDefaultLocalName("Painite")
            .constructMaterial();
    }

    private static Materials loadPeanutwood() {
        return new MaterialBuilder().setName("Peanutwood")
            .setDefaultLocalName("Peanut Wood")
            .setMaceratingInto(() -> Materials.Wood)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.WOOD)
            .constructMaterial();
    }

    private static Materials loadPetroleum() {
        return new MaterialBuilder().setName("Petroleum")
            .setDefaultLocalName("Petroleum")
            .setMiningLevel(1)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadPewter() {
        return new MaterialBuilder().setName("Pewter")
            .setDefaultLocalName("Pewter")
            .constructMaterial();
    }

    private static Materials loadRandomite() {
        return new MaterialBuilder().setName("Randomite")
            .setDefaultLocalName("Randomite")
            .setMiningLevel(1)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadSand() {
        return new MaterialBuilder().setName("Sand")
            .setDefaultLocalName("Sand")
            .setColor(Dyes.dyeYellow)
            .setSmeltingInto(() -> Materials.Glass)
            .addSubTag(SubTag.NO_RECYCLING)
            .constructMaterial();
    }

    private static Materials loadSunstone() {
        return new MaterialBuilder().setName("Sunstone")
            .setDefaultLocalName("Sunstone")
            .setMiningLevel(1)
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .addOreItems()
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadTar() {
        return new MaterialBuilder().setName("Tar")
            .setDefaultLocalName("Tar")
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .constructMaterial();
    }

    private static Materials loadVoidstone() {
        return new MaterialBuilder().setName("Voidstone")
            .setDefaultLocalName("Voidstone")
            .setARGB(0xc8ffffff)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.VACUOS, 1)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static void loadTiersMarkers() {
        Materials.ULV = loadULV();
        Materials.LV = loadLV();
        Materials.MV = loadMV();
        Materials.HV = loadHV();
        Materials.EV = loadEV();
        Materials.IV = loadIV();
        Materials.LuV = loadLuV();
        Materials.ZPM = loadZPM();
        Materials.UV = loadUV();
        Materials.UHV = loadUHV();
        Materials.UEV = loadUEV();
        Materials.UIV = loadUIV();
        Materials.UMV = loadUMV();
        Materials.UXV = loadUXV();
        Materials.MAX = loadMAX();
    }

    private static Materials loadULV() {
        return new MaterialBuilder().setName("Primitive")
            .setDefaultLocalName("Primitive")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 1)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadLV() {
        return new MaterialBuilder().setName("Basic")
            .setDefaultLocalName("Basic")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 2)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadMV() {
        return new MaterialBuilder().setName("Good")
            .setDefaultLocalName("Good")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 3)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadHV() {
        return new MaterialBuilder().setName("Advanced")
            .setDefaultLocalName("Advanced")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 4)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadEV() {
        return new MaterialBuilder().setName("Data")
            .setDefaultLocalName("Data")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 5)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadIV() {
        return new MaterialBuilder().setName("Elite")
            .setDefaultLocalName("Elite")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 6)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadLuV() {
        return new MaterialBuilder().setName("Master")
            .setDefaultLocalName("Master")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 7)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadZPM() {
        return new MaterialBuilder().setName("Ultimate")
            .setDefaultLocalName("Ultimate")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 8)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadUV() {
        return new MaterialBuilder().setName("Superconductor")
            .setDefaultLocalName("Superconductor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 9)
            .constructMaterial();
    }

    private static Materials loadUHV() {
        return new MaterialBuilder().setName("Infinite")
            .setDefaultLocalName("Infinite")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 10)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadUEV() {
        return new MaterialBuilder().setName("Bio")
            .setDefaultLocalName("Bio")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 11)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadUIV() {
        return new MaterialBuilder().setName("Optical")
            .setDefaultLocalName("Optical")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 12)
            .constructMaterial();
    }

    private static Materials loadUMV() {
        return new MaterialBuilder().setName("Exotic")
            .setDefaultLocalName("Exotic")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 13)
            .constructMaterial();
    }

    private static Materials loadUXV() {
        return new MaterialBuilder().setName("Cosmic")
            .setDefaultLocalName("Cosmic")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 14)
            .constructMaterial();
    }

    private static Materials loadMAX() {
        return new MaterialBuilder().setName("Transcendent")
            .setDefaultLocalName("Transcendent")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 15)
            .constructMaterial();
    }

    private static void loadCircuitryMarkers() {
        Materials.Resistor = loadResistor();
        Materials.Diode = loadDiode();
        Materials.Transistor = loadTransistor();
        Materials.Capacitor = loadCapacitor();
        Materials.Inductor = loadInductor();
        Materials.Nano = loadNano();
        Materials.Piko = loadPiko();
    }

    private static Materials loadResistor() {
        return new MaterialBuilder().setName("Resistor")
            .setDefaultLocalName("Resistor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadDiode() {
        return new MaterialBuilder().setName("Diode")
            .setDefaultLocalName("Diode")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadTransistor() {
        return new MaterialBuilder().setName("Transistor")
            .setDefaultLocalName("Transistor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadCapacitor() {
        return new MaterialBuilder().setName("Capacitor")
            .setDefaultLocalName("Capacitor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadInductor() {
        return new MaterialBuilder().setName("Inductor")
            .setDefaultLocalName("Inductor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadNano() {
        return new MaterialBuilder().setName("Nano")
            .setDefaultLocalName("Bio")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 11)
            .constructMaterial();
    }

    private static Materials loadPiko() {
        return new MaterialBuilder().setName("Piko")
            .setDefaultLocalName("Bio")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 12)
            .constructMaterial();
    }

    private static void loadNotExactMarkers() {
        Materials.Cluster = loadCluster();
        Materials.Leather = loadLeather();
        Materials.Mud = loadMud();
        Materials.Peat = loadPeat();
        Materials.Red = loadRed();
        Materials.TNT = loadTNT();
        Materials.UnstableIngot = loadUnstableIngot();
    }

    private static Materials loadCluster() {
        return new MaterialBuilder().setName("Cluster")
            .setDefaultLocalName("Cluster")
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fffffff)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadLeather() {
        return new MaterialBuilder().setName("Leather")
            .setDefaultLocalName("Leather")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x7f969650)
            .addDustItems()
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static Materials loadMud() {
        return new MaterialBuilder().setName("Mud")
            .setDefaultLocalName("Mud")
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadPeat() {
        return new MaterialBuilder().setName("Peat")
            .setDefaultLocalName("Peat")
            .setColor(Dyes.dyeBrown)
            .addAspect(TCAspects.POTENTIA, 2)
            .addAspect(TCAspects.IGNIS, 2)
            .constructMaterial();
    }

    private static Materials loadRed() {
        return new MaterialBuilder().setName("Red")
            .setDefaultLocalName("Red")
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff0000)
            .constructMaterial();
    }

    private static Materials loadTNT() {
        return new MaterialBuilder().setName("TNT")
            .setDefaultLocalName("TNT")
            .setColor(Dyes.dyeRed)
            .addAspect(TCAspects.PERDITIO, 7)
            .addAspect(TCAspects.IGNIS, 4)
            .addSubTag(SubTag.FLAMMABLE)
            .addSubTag(SubTag.EXPLOSIVE)
            .addSubTag(SubTag.NO_SMELTING)
            .addSubTag(SubTag.NO_SMASHING)
            .constructMaterial();
    }

    private static Materials loadUnstableIngot() {
        return new MaterialBuilder().setName("Unstableingot")
            .setDefaultLocalName("Unstable")
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fffffff)
            .addAspect(TCAspects.PERDITIO, 4)
            .addSubTag(SubTag.TRANSPARENT)
            .constructMaterial();
    }

    private static void loadSuperconductorsMarkers() {
        Materials.SuperconductorMV = loadSuperconductorMV();
        Materials.SuperconductorHV = loadSuperconductorHV();
        Materials.SuperconductorEV = loadSuperconductorEV();
        Materials.SuperconductorIV = loadSuperconductorIV();
        Materials.SuperconductorLuV = loadSuperconductorLuV();
        Materials.SuperconductorZPM = loadSuperconductorZPM();
        Materials.SuperconductorUV = loadSuperconductorUV();
        Materials.SuperconductorUHV = loadSuperconductorUHV();
        Materials.SuperconductorUEV = loadSuperconductorUEV();
        Materials.SuperconductorUIV = loadSuperconductorUIV();
        Materials.SuperconductorUMV = loadSuperconductorUMV();
    }

    private static Materials loadSuperconductorMV() {
        return new MaterialBuilder().setName("SuperconductorMV")
            .setDefaultLocalName("Superconductor MV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setARGB(0x00555555)
            .addAspect(TCAspects.ELECTRUM, 6)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorHV() {
        return new MaterialBuilder().setName("SuperconductorHV")
            .setDefaultLocalName("Superconductor HV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00331900)
            .addAspect(TCAspects.ELECTRUM, 12)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorEV() {
        return new MaterialBuilder().setName("SuperconductorEV")
            .setDefaultLocalName("Superconductor EV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setARGB(0x00008700)
            .addAspect(TCAspects.ELECTRUM, 18)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorIV() {
        return new MaterialBuilder().setName("SuperconductorIV")
            .setDefaultLocalName("Superconductor IV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeMagenta)
            .setARGB(0x00330033)
            .addAspect(TCAspects.ELECTRUM, 24)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorLuV() {
        return new MaterialBuilder().setName("SuperconductorLuV")
            .setDefaultLocalName("Superconductor LuV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBrown)
            .setARGB(0x00994c00)
            .addAspect(TCAspects.ELECTRUM, 30)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorZPM() {
        return new MaterialBuilder().setName("SuperconductorZPM")
            .setDefaultLocalName("Superconductor ZPM")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x000a0a0a)
            .addAspect(TCAspects.ELECTRUM, 36)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUV() {
        return new MaterialBuilder().setName("SuperconductorUV")
            .setDefaultLocalName("Superconductor UV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00e0d207)
            .addAspect(TCAspects.ELECTRUM, 42)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUHV() {
        return new MaterialBuilder().setName("Superconductor")
            .setDefaultLocalName("Superconductor UHV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x002681bd)
            .addAspect(TCAspects.ELECTRUM, 48)
            .addSubTag(SubTag.NO_SMASHING)
            .addSubTag(SubTag.NO_SMELTING)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUEV() {
        return new MaterialBuilder().setName("SuperconductorUEV")
            .setDefaultLocalName("Superconductor UEV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00ae0808)
            .addAspect(TCAspects.ELECTRUM, 54)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUIV() {
        return new MaterialBuilder().setName("SuperconductorUIV")
            .setDefaultLocalName("Superconductor UIV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00e558b1)
            .addAspect(TCAspects.ELECTRUM, 60)
            .constructMaterial();
    }

    private static Materials loadSuperconductorUMV() {
        return new MaterialBuilder().setName("SuperconductorUMV")
            .setDefaultLocalName("Superconductor UMV")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00b526cd)
            .addAspect(TCAspects.ELECTRUM, 66)
            .constructMaterial();
    }

    private static void loadGTNHMaterialsMarkers() {
        Materials.Prismarine = loadPrismarine();
        Materials.SolutionBlueVitriol = loadSolutionBlueVitriol();
        Materials.SolutionNickelSulfate = loadSolutionNickelSulfate();
        Materials.Lodestone = loadLodestone();
        Materials.Luminite = loadLuminite();
    }

    private static Materials loadPrismarine() {
        return new MaterialBuilder().setName("Prismarine")
            .setDefaultLocalName("Prismarine")
            .addSubTag(SubTag.NO_ORE_PROCESSING)
            .constructMaterial();
    }

    private static Materials loadSolutionBlueVitriol() {
        return new MaterialBuilder().setName("SolutionBlueVitriol")
            .setDefaultLocalName("Blue Vitriol Solution")
            .constructMaterial();
    }

    private static Materials loadSolutionNickelSulfate() {
        return new MaterialBuilder().setName("SolutionNickelSulfate")
            .setDefaultLocalName("Nickel Sulfate Solution")
            .constructMaterial();
    }

    private static Materials loadLodestone() {
        return new MaterialBuilder().setName("Lodestone")
            .setDefaultLocalName("Lodestone")
            .setMiningLevel(0)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadLuminite() {
        return new MaterialBuilder().setName("Luminite")
            .setDefaultLocalName("Luminite")
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fafafa)
            .setMiningLevel(0)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }
}
