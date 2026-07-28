package gregtech.api.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2ParentMods;
import gregtech.api.material.MU;

public class AssemblyLineServer {

    public static LinkedHashMap<String, String> lServerNames = new LinkedHashMap<>();

    /// The material-name token substituted into a generated item or ore block's server-side name, or null
    /// when the id slot never yielded items -- empty, or its material's parent mod absent
    /// (`Materials2ParentMods#hasParentMod`), which left the slot without items or names. These tokens feed
    /// assembly-line data packets, so they must be byte-identical to the legacy internal name;
    /// [MU#legacyName] resolves exactly that string, including the LEGACY_NAME divergents.
    private static @Nullable String generatedMaterialName(int id) {
        Material material = MU.byId(id);
        if (material == null) return null;
        if (!Materials2ParentMods.hasParentMod(material)) return null;
        return MU.legacyName(material);
    }

    public static void fillMap(FMLPreInitializationEvent aEvent) {
        final Configuration conf = GTLanguageManager.sEnglishFile;
        final ConfigCategory cat = conf.getCategory("languagefile");
        final HashMap<String, Property> internal = new HashMap<>(cat.getValues());
        final LinkedHashMap<String, String> internal2 = new LinkedHashMap<>();
        final LinkedHashMap<String, String> internal3 = new LinkedHashMap<>();
        final LinkedHashMap<String, String> internal4 = new LinkedHashMap<>();
        for (Map.Entry<String, Property> entry : internal.entrySet()) {
            try {
                String s = entry.getValue()
                    .getString()
                    .replaceAll("%", "");

                if (entry.getKey()
                    .contains("metaitem") && s.contains("material")) internal2.put(entry.getKey(), s);
                else if (entry.getKey()
                    .contains("blockmachines") && s.contains("material")) internal3.put(entry.getKey(), s);
                else if ((entry.getKey()
                    .contains("blockores")
                    || (entry.getKey()
                        .contains("blockmetal")
                        || entry.getKey()
                            .contains("blockgem")))
                    && s.contains("material")) internal4.put(entry.getKey(), s);
                else lServerNames.put(entry.getKey(), s);
            } catch (Exception ignored) {}
        }
        for (Map.Entry<String, String> entry : internal2.entrySet()) {
            try {
                if (entry.getKey()
                    .contains("name")) {
                    int i = Integer.parseInt(
                        entry.getKey()
                            .substring(
                                "gt.metaitem.01.".length(),
                                entry.getKey()
                                    .length() - ".name".length()));
                    i = i % 1000;
                    String materialName = generatedMaterialName(i);
                    if (materialName != null) lServerNames.put(
                        entry.getKey(),
                        entry.getValue()
                            .replace("material", materialName));
                    else lServerNames.put(entry.getKey(), null);
                }
            } catch (Exception ignored) {}
        }
        for (Map.Entry<String, String> entry : internal3.entrySet()) {
            try {
                if (entry.getKey()
                    .contains("cable"))
                    lServerNames.put(
                        entry.getKey(),
                        entry.getValue()
                            .replace(
                                "material",
                                entry.getKey()
                                    .substring(
                                        "gt.blockmachines.cable.".length(),
                                        entry.getKey()
                                            .length() - ".01.name".length())));
                else if (entry.getKey()
                    .contains("gt_frame_"))
                    lServerNames.put(
                        entry.getKey(),
                        entry.getValue()
                            .replace(
                                "material",
                                entry.getKey()
                                    .substring(
                                        "gt.blockmachines.gt_frame_".length(),
                                        entry.getKey()
                                            .length() - ".name".length())));
                else if (entry.getKey()
                    .contains("gt_pipe_")) {
                        if (!entry.getKey()
                            .contains("_huge")
                            && !entry.getKey()
                                .contains("_large")
                            && !entry.getKey()
                                .contains("_nonuple")
                            && !entry.getKey()
                                .contains("_quadruple")
                            && !entry.getKey()
                                .contains("_small")
                            && !entry.getKey()
                                .contains("_tiny"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - ".name".length())));
                        else if (entry.getKey()
                            .contains("_huge")
                            || entry.getKey()
                                .contains("_tiny"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - "_tiny.name".length())));
                        else if (entry.getKey()
                            .contains("_large")
                            || entry.getKey()
                                .contains("_small"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - "_large.name".length())));
                        else if (entry.getKey()
                            .contains("_nonuple"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - "_nonuple.name".length())));
                        else if (entry.getKey()
                            .contains("_quadruple"))
                            lServerNames.put(
                                entry.getKey(),
                                entry.getValue()
                                    .replace(
                                        "material",
                                        entry.getKey()
                                            .substring(
                                                "gt.blockmachines.gt_pipe_".length(),
                                                entry.getKey()
                                                    .length() - "_quadruple.name".length())));
                    } else if (entry.getKey()
                        .contains("wire"))
                        lServerNames.put(
                            entry.getKey(),
                            entry.getValue()
                                .replace(
                                    "material",
                                    entry.getKey()
                                        .substring(
                                            "gt.blockmachines.wire.".length(),
                                            entry.getKey()
                                                .length() - ".01.name".length())));
                else lServerNames.put(entry.getKey(), entry.getValue());
            } catch (Exception ignored) {}
        }
        for (Map.Entry<String, String> entry : internal4.entrySet()) {
            try {
                if (entry.getKey()
                    .contains("blockores")) {
                    int i = Integer.parseInt(
                        entry.getKey()
                            .substring(
                                "gt.blockores.".length(),
                                entry.getKey()
                                    .length() - ".name".length()));
                    i = i % 1000;
                    String materialName = generatedMaterialName(i);
                    if (materialName != null) lServerNames.put(
                        entry.getKey(),
                        entry.getValue()
                            .replace("material", materialName));
                    else lServerNames.put(entry.getKey(), null);
                } else if (entry.getKey()
                    .contains("blockmetal")) {
                        Material[] mMats = null;
                        String t = entry.getKey()
                            .substring("gt.blockmetal".length());
                        t = t.substring(0, 1);
                        int i = Integer.parseInt(t);
                        switch (i) {
                            case 1 -> mMats = new Material[] { Materials2Materials.Adamantium,
                                Materials2Materials.Aluminium, Materials2Materials.Americium,
                                Materials2Materials.AnnealedCopper, Materials2Materials.Antimony,
                                Materials2Materials.Arsenic, Materials2Materials.AstralSilver,
                                Materials2Materials.BatteryAlloy, Materials2Materials.Beryllium,
                                Materials2Materials.Bismuth, Materials2Materials.BismuthBronze,
                                Materials2Materials.BlackBronze, Materials2Materials.BlackSteel,
                                Materials2Materials.BlueAlloy, Materials2Materials.BlueSteel,
                                Materials2Materials.Brass };
                            case 2 -> mMats = new Material[] { Materials2Materials.Bronze, Materials2Materials.Caesium,
                                Materials2Materials.Cerium, Materials2Materials.Chrome,
                                Materials2Materials.ChromiumDioxide, Materials2Materials.Cobalt,
                                Materials2Materials.CobaltBrass, Materials2Materials.Copper,
                                Materials2Materials.Cupronickel, Materials2Materials.DamascusSteel,
                                Materials2Materials.DarkIron, Materials2Materials.DeepIron, Materials2Materials.Desh,
                                Materials2Materials.Duranium, Materials2Materials.Dysprosium,
                                Materials2Materials.Electrum };
                            case 3 -> mMats = new Material[] { Materials2Materials.ElectrumFlux,
                                Materials2Materials.Enderium, Materials2Materials.Erbium, Materials2Materials.Europium,
                                Materials2Materials.FierySteel, Materials2Materials.Gadolinium,
                                Materials2Materials.Gallium, Materials2Materials.Holmium, Materials2Materials.HSLA,
                                Materials2Materials.Indium, Materials2Materials.InfusedGold, Materials2Materials.Invar,
                                Materials2Materials.Iridium, Materials2Materials.IronMagnetic,
                                Materials2Materials.IronWood, Materials2Materials.Kanthal };
                            case 4 -> mMats = new Material[] { Materials2Materials.Knightmetal,
                                Materials2Materials.Lanthanum, Materials2Materials.Lead, Materials2Materials.Lutetium,
                                Materials2Materials.Magnalium, Materials2Materials.Magnesium,
                                Materials2Materials.Manganese, Materials2Materials.MeteoricIron,
                                Materials2Materials.MeteoricSteel, Materials2Materials.Trinium,
                                Materials2Materials.Mithril, Materials2Materials.Molybdenum,
                                Materials2Materials.Naquadah, Materials2Materials.NaquadahAlloy,
                                Materials2Materials.NaquadahEnriched, Materials2Materials.Naquadria };
                            case 5 -> mMats = new Material[] { Materials2Materials.Neodymium,
                                Materials2Materials.NeodymiumMagnetic, Materials2Materials.Neutronium,
                                Materials2Materials.Nichrome, Materials2Materials.Nickel, Materials2Materials.Niobium,
                                Materials2Materials.NiobiumNitride, Materials2Materials.NiobiumTitanium,
                                Materials2Materials.Osmiridium, Materials2Materials.Osmium,
                                Materials2Materials.Palladium, Materials2Materials.PigIron,
                                Materials2Materials.Platinum, Materials2Materials.Plutonium,
                                Materials2Materials.Plutonium241, Materials2Materials.Praseodymium };
                            case 6 -> mMats = new Material[] { Materials2Materials.Promethium,
                                Materials2Materials.RedAlloy, Materials2Materials.RedSteel,
                                Materials2Materials.RoseGold, Materials2Materials.Rubidium,
                                Materials2Materials.Samarium, Materials2Materials.Scandium,
                                Materials2Materials.ShadowIron, Materials2Materials.ShadowSteel,
                                Materials2Materials.Silicon, Materials2Materials.Silver,
                                Materials2Materials.SolderingAlloy, Materials2Materials.StainlessSteel,
                                Materials2Materials.Steel, Materials2Materials.SteelMagnetic,
                                Materials2Materials.SterlingSilver };
                            case 7 -> mMats = new Material[] { Materials2Materials.Sunnarium,
                                Materials2Materials.Tantalum, Materials2Materials.Tellurium,
                                Materials2Materials.Terbium, Materials2Materials.Thaumium, Materials2Materials.Thorium,
                                Materials2Materials.Thulium, Materials2Materials.Tin, Materials2Materials.TinAlloy,
                                Materials2Materials.Titanium, Materials2Materials.Tritanium,
                                Materials2Materials.Tungsten, Materials2Materials.TungstenSteel,
                                Materials2Materials.Ultimet, Materials2Materials.Uranium,
                                Materials2Materials.Uranium235 };
                            case 8 -> mMats = new Material[] { Materials2Materials.Vanadium,
                                Materials2Materials.VanadiumGallium, Materials2Materials.CastIron,
                                Materials2Materials.Ytterbium, Materials2Materials.Yttrium,
                                Materials2Materials.YttriumBariumCuprate, Materials2Materials.Zinc,
                                Materials2Materials.TungstenCarbide, Materials2Materials.VanadiumSteel,
                                Materials2Materials.HSSG, Materials2Materials.HSSE, Materials2Materials.HSSS,
                                Materials2Materials.Steeleaf, Materials2Materials.Ichorium,
                                Materials2Materials.Firestone };
                        }
                        t = entry.getKey()
                            .substring(
                                "gt.blockmetal1.".length(),
                                entry.getKey()
                                    .length() - ".name".length());
                        i = Integer.parseInt(t);
                        lServerNames.put(entry.getKey(), "Block of " + MU.internalName(mMats[i]));
                    } else if (entry.getKey()
                        .contains("blockgem")) {
                            Material[] mMats = null;
                            String t = entry.getKey()
                                .substring("gt.blockgem".length());
                            t = t.substring(0, 1);
                            int i = Integer.parseInt(t);
                            switch (i) {
                                case 1 -> mMats = new Material[] { Materials2Materials.InfusedAir,
                                    Materials2Materials.Amber, Materials2Materials.Amethyst,
                                    Materials2Materials.InfusedWater, Materials2Materials.BlueTopaz,
                                    Materials2Materials.CertusQuartz, Materials2Materials.Dilithium,
                                    Materials2Materials.EnderEye, Materials2Materials.EnderPearl,
                                    Materials2Materials.FoolsRuby, Materials2Materials.Force,
                                    Materials2Materials.Forcicium, Materials2Materials.Forcillium,
                                    Materials2Materials.GreenSapphire, Materials2Materials.InfusedFire,
                                    Materials2Materials.Jasper, Materials2Materials.ManaDiamond,
                                    Materials2Materials.BotaniaDragonstone };
                                case 2 -> mMats = new Material[] { Materials2Materials.Lazurite,
                                    Materials2Materials.Lignite, Materials2Materials.Monazite,
                                    Materials2Materials.Niter, Materials2Materials.Olivine, Materials2Materials.Opal,
                                    Materials2Materials.InfusedOrder, Materials2Materials.InfusedEntropy,
                                    Materials2Materials.Phosphorus, Materials2Materials.Quartzite,
                                    Materials2Materials.GarnetRed, Materials2Materials.Ruby,
                                    Materials2Materials.Sapphire, Materials2Materials.Sodalite,
                                    Materials2Materials.Tanzanite, Materials2Materials.InfusedEarth };
                                case 3 -> mMats = new Material[] { Materials2Materials.Topaz,
                                    Materials2Materials.Vinteum, Materials2Materials.GarnetYellow,
                                    Materials2Materials.NetherStar, Materials2Materials.Charcoal,
                                    Materials2Materials.Blaze };
                            }
                            t = entry.getKey()
                                .substring(
                                    "gt.blockgem1.".length(),
                                    entry.getKey()
                                        .length() - ".name".length());
                            i = Integer.parseInt(t);
                            lServerNames.put(entry.getKey(), "Block of " + MU.internalName(mMats[i]));
                        }
            } catch (Exception ignored) {}
        }
    }
}
