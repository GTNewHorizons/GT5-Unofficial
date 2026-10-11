package gregtech.crossmod.forgemultipart;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;

/// Registers the storage blocks of [#MATERIALS] as ForgeMultipart microblock materials. Each is registered as
/// `ml:<Material>:block` rather than under its block and metadata, since MaterialLib numbers its blocks per install.
public final class MicroblockMaterials {

    private static final Material[] MATERIALS = { Materials.Adamantium, Materials.Aluminium, Materials.Americium,
        Materials.AnnealedCopper, Materials.Antimony, Materials.Arsenic, Materials.AstralSilver, Materials.BatteryAlloy,
        Materials.Beryllium, Materials.Bismuth, Materials.BismuthBronze, Materials.BlackBronze, Materials.BlackSteel,
        Materials.BlueAlloy, Materials.BlueSteel, Materials.Brass, Materials.Bronze, Materials.Caesium,
        Materials.Cerium, Materials.Chrome, Materials.ChromiumDioxide, Materials.Cobalt, Materials.CobaltBrass,
        Materials.Copper, Materials.Cupronickel, Materials.DamascusSteel, Materials.DarkIron, Materials.DeepIron,
        Materials.Desh, Materials.Duranium, Materials.Dysprosium, Materials.Electrum, Materials.ElectrumFlux,
        Materials.Enderium, Materials.Erbium, Materials.Europium, Materials.FierySteel, Materials.Gadolinium,
        Materials.Gallium, Materials.Holmium, Materials.Indium, Materials.InfusedGold, Materials.Invar,
        Materials.Iridium, Materials.IronMagnetic, Materials.IronWood, Materials.Kanthal, Materials.Knightmetal,
        Materials.Lanthanum, Materials.Lead, Materials.Lutetium, Materials.Magnalium, Materials.Magnesium,
        Materials.Manganese, Materials.MeteoricIron, Materials.MeteoricSteel, Materials.Trinium, Materials.Mithril,
        Materials.Molybdenum, Materials.Naquadah, Materials.NaquadahAlloy, Materials.NaquadahEnriched,
        Materials.Naquadria, Materials.Neodymium, Materials.NeodymiumMagnetic, Materials.Neutronium, Materials.Nichrome,
        Materials.Nickel, Materials.Niobium, Materials.NiobiumNitride, Materials.NiobiumTitanium, Materials.Osmiridium,
        Materials.Osmium, Materials.Palladium, Materials.PigIron, Materials.Platinum, Materials.Plutonium,
        Materials.Plutonium241, Materials.Praseodymium, Materials.Promethium, Materials.RedAlloy, Materials.RedSteel,
        Materials.RoseGold, Materials.Rubidium, Materials.Samarium, Materials.Scandium, Materials.ShadowIron,
        Materials.ShadowSteel, Materials.Silicon, Materials.Silver, Materials.SolderingAlloy, Materials.StainlessSteel,
        Materials.Steel, Materials.SteelMagnetic, Materials.SterlingSilver, Materials.Sunnarium, Materials.Tantalum,
        Materials.Tellurium, Materials.Terbium, Materials.Thaumium, Materials.Thorium, Materials.Thulium, Materials.Tin,
        Materials.TinAlloy, Materials.Titanium, Materials.Tritanium, Materials.Tungsten, Materials.TungstenSteel,
        Materials.Ultimet, Materials.Uranium, Materials.Uranium235, Materials.Vanadium, Materials.VanadiumGallium,
        Materials.CastIron, Materials.Ytterbium, Materials.Yttrium, Materials.YttriumBariumCuprate, Materials.Zinc,
        Materials.TungstenCarbide, Materials.VanadiumSteel, Materials.HSSG, Materials.HSSE, Materials.HSSS,
        Materials.Steeleaf, Materials.Ichorium, Materials.Firestone, Materials.InfusedAir, Materials.Amber,
        Materials.Amethyst, Materials.InfusedWater, Materials.BlueTopaz, Materials.CertusQuartz, Materials.Dilithium,
        Materials.EnderEye, Materials.EnderPearl, Materials.FoolsRuby, Materials.Force, Materials.Forcicium,
        Materials.Forcillium, Materials.GreenSapphire, Materials.InfusedFire, Materials.Jasper, Materials.Lazurite,
        Materials.Lignite, Materials.Monazite, Materials.Niter, Materials.Olivine, Materials.Opal,
        Materials.InfusedOrder, Materials.InfusedEntropy, Materials.TricalciumPhosphate, Materials.Quartzite,
        Materials.GarnetRed, Materials.Ruby, Materials.Sapphire, Materials.Sodalite, Materials.Tanzanite,
        Materials.InfusedEarth, Materials.Topaz, Materials.Vinteum, Materials.GarnetYellow, Materials.NetherStar,
        Materials.Charcoal, Materials.Blaze, Materials.Bismutite, Materials.CubicZirconia, Materials.FluorBuergerite,
        Materials.ChromoAluminoPovondraite, Materials.VanadioOxyDravite, Materials.Olenite, Materials.RedZircon,
        Materials.Salt, Materials.Spodumene, Materials.RockSalt, Materials.Fayalite, Materials.Forsterite,
        Materials.Hedenbergite, Materials.Prasiolite, Materials.MagnetoResonatic, Materials.Californium,
        Materials.Calcium, Materials.BArTiMaEuSNeK, Materials.Ruthenium, Materials.Rhodium,
        Materials.RhodiumPlatedPalladium, Materials.Tiberium, Materials.Ruridit, Materials.Fluorspar,
        Materials.HighDurabilityCompoundSteel, Materials.AdemicSteel };

    private MicroblockMaterials() {}

    public static void register() {
        for (Material material : MATERIALS) {
            ItemStack stack = MaterialLibAPI.getStack(material, BlockShapes.block, 1);
            Block block = Block.getBlockFromItem(stack.getItem());
            MicroMaterialRegistry
                .registerMaterial(new BlockMicroMaterial(block, stack.getItemDamage()), name(material));
        }
    }

    /// Points the microblock material saves recorded for a legacy storage block slot at `material`'s name.
    public static void remapLegacy(String legacyBlock, int meta, Material material) {
        String name = name(material);
        if (name != null) MicroMaterialRegistry.remapName(BlockMicroMaterial.materialKey(legacyBlock, meta), name);
    }

    /// Points the microblock material saves recorded for a legacy block slot at another block's slot.
    public static void remapLegacy(String legacyBlock, int legacyMeta, String block, int meta) {
        MicroMaterialRegistry.remapName(
            BlockMicroMaterial.materialKey(legacyBlock, legacyMeta),
            BlockMicroMaterial.materialKey(block, meta));
    }

    /// The microblock material name of `material`'s storage block, or null when it has none.
    public static String name(Material material) {
        for (Material registered : MATERIALS) {
            if (registered == material) return "ml:" + MaterialUtils.internalName(material) + ":block";
        }
        return null;
    }
}
