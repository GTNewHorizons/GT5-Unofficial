package gregtech.loaders.postload;

import java.util.Map;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials.Materials;

/// The materials of the legacy storage-block batches, keyed by registry name. A saved block's metadata indexes
/// its batch; a null entry is a slot whose material is gone. [PosteaTransformers] migrates these blocks to
/// MaterialLib storage blocks.
final class LegacyStorageBlockTable {

    static final Map<String, Material[]> BATCHES = Map.ofEntries(
        Map.entry(
            "gregtech:gt.blockmetal1",
            new Material[] { Materials.Adamantium, Materials.Aluminium, Materials.Americium, Materials.AnnealedCopper,
                Materials.Antimony, Materials.Arsenic, Materials.AstralSilver, Materials.BatteryAlloy,
                Materials.Beryllium, Materials.Bismuth, Materials.BismuthBronze, Materials.BlackBronze,
                Materials.BlackSteel, Materials.BlueAlloy, Materials.BlueSteel, Materials.Brass }),
        Map.entry(
            "gregtech:gt.blockmetal2",
            new Material[] { Materials.Bronze, Materials.Caesium, Materials.Cerium, Materials.Chrome,
                Materials.ChromiumDioxide, Materials.Cobalt, Materials.CobaltBrass, Materials.Copper,
                Materials.Cupronickel, Materials.DamascusSteel, Materials.DarkIron, Materials.DeepIron, Materials.Desh,
                Materials.Duranium, Materials.Dysprosium, Materials.Electrum }),
        Map.entry(
            "gregtech:gt.blockmetal3",
            new Material[] { Materials.ElectrumFlux, Materials.Enderium, Materials.Erbium, Materials.Europium,
                Materials.FierySteel, Materials.Gadolinium, Materials.Gallium, Materials.Holmium, null,
                Materials.Indium, Materials.InfusedGold, Materials.Invar, Materials.Iridium, Materials.IronMagnetic,
                Materials.IronWood, Materials.Kanthal }),
        Map.entry(
            "gregtech:gt.blockmetal4",
            new Material[] { Materials.Knightmetal, Materials.Lanthanum, Materials.Lead, Materials.Lutetium,
                Materials.Magnalium, Materials.Magnesium, Materials.Manganese, Materials.MeteoricIron,
                Materials.MeteoricSteel, Materials.Trinium, Materials.Mithril, Materials.Molybdenum, Materials.Naquadah,
                Materials.NaquadahAlloy, Materials.NaquadahEnriched, Materials.Naquadria }),
        Map.entry(
            "gregtech:gt.blockmetal5",
            new Material[] { Materials.Neodymium, Materials.NeodymiumMagnetic, Materials.Neutronium, Materials.Nichrome,
                Materials.Nickel, Materials.Niobium, Materials.NiobiumNitride, Materials.NiobiumTitanium,
                Materials.Osmiridium, Materials.Osmium, Materials.Palladium, Materials.PigIron, Materials.Platinum,
                Materials.Plutonium, Materials.Plutonium241, Materials.Praseodymium }),
        Map.entry(
            "gregtech:gt.blockmetal6",
            new Material[] { Materials.Promethium, Materials.RedAlloy, Materials.RedSteel, Materials.RoseGold,
                Materials.Rubidium, Materials.Samarium, Materials.Scandium, Materials.ShadowIron, Materials.ShadowSteel,
                Materials.Silicon, Materials.Silver, Materials.SolderingAlloy, Materials.StainlessSteel,
                Materials.Steel, Materials.SteelMagnetic, Materials.SterlingSilver }),
        Map.entry(
            "gregtech:gt.blockmetal7",
            new Material[] { Materials.Sunnarium, Materials.Tantalum, Materials.Tellurium, Materials.Terbium,
                Materials.Thaumium, Materials.Thorium, Materials.Thulium, Materials.Tin, Materials.TinAlloy,
                Materials.Titanium, Materials.Tritanium, Materials.Tungsten, Materials.TungstenSteel, Materials.Ultimet,
                Materials.Uranium, Materials.Uranium235 }),
        Map.entry(
            "gregtech:gt.blockmetal8",
            new Material[] { Materials.Vanadium, Materials.VanadiumGallium, Materials.CastIron, Materials.Ytterbium,
                Materials.Yttrium, Materials.YttriumBariumCuprate, Materials.Zinc, Materials.TungstenCarbide,
                Materials.VanadiumSteel, Materials.HSSG, Materials.HSSE, Materials.HSSS, Materials.Steeleaf,
                Materials.Ichorium, Materials.Firestone, Materials.Shadow }),
        Map.entry(
            "gregtech:gt.blockgem1",
            new Material[] { Materials.InfusedAir, Materials.Amber, Materials.Amethyst, Materials.InfusedWater,
                Materials.BlueTopaz, Materials.CertusQuartz, Materials.Dilithium, Materials.EnderEye,
                Materials.EnderPearl, Materials.Spinel, Materials.Force, Materials.Forcicium, Materials.Forcillium,
                Materials.GreenSapphire, Materials.InfusedFire, Materials.Jasper }),
        Map.entry(
            "gregtech:gt.blockgem2",
            new Material[] { Materials.Lazurite, Materials.Lignite, Materials.Monazite, Materials.Niter,
                Materials.Olivine, Materials.Opal, Materials.InfusedOrder, Materials.InfusedEntropy,
                Materials.TricalciumPhosphate, Materials.Quartzite, Materials.GarnetRed, Materials.Ruby,
                Materials.Sapphire, Materials.Sodalite, Materials.Tanzanite, Materials.InfusedEarth }),
        Map.entry(
            "gregtech:gt.blockgem3",
            new Material[] { Materials.Topaz, Materials.Vinteum, Materials.GarnetYellow, Materials.NetherStar,
                Materials.Charcoal, Materials.Blaze }),
        Map.entry(
            "gregtech:gt.blockmetal9",
            new Material[] { Materials.Cryolite, Materials.SiliconSG, Materials.NickelAluminide, Materials.SpaceTime,
                Materials.TranscendentMetal, Materials.Oriharukon, Materials.WhiteDwarfMatter,
                Materials.BlackDwarfMatter, Materials.Universium, Materials.Eternity, Materials.MagMatter,
                Materials.SixPhasedCopper, Materials.HellishMetal, Materials.MHDCSM, Materials.Hexanite }),
        Map.entry("gregtech:gt.blockmetal10", new Material[] { Materials.Shijima, Materials.Churitsu }));

    private LegacyStorageBlockTable() {}
}
