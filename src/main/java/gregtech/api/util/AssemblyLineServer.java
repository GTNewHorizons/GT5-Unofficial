package gregtech.api.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;

public class AssemblyLineServer {

    public static LinkedHashMap<String, String> lServerNames = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> internal2 = new LinkedHashMap<>(), internal3 = new LinkedHashMap<>(),
        internal4 = new LinkedHashMap<>();
    private static HashMap<String, Property> internal = new HashMap<>();

    public static void fillMap(FMLPreInitializationEvent aEvent) {
        Configuration conf = GTLanguageManager.sEnglishFile;

        ConfigCategory cat = conf.getCategory("languagefile");
        internal.putAll(cat.getValues());
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
                    if (GregTechAPI.sGeneratedMaterials[i] != null) lServerNames.put(
                        entry.getKey(),
                        entry.getValue()
                            .replace("material", GregTechAPI.sGeneratedMaterials[i].toString()));
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
                    if (GregTechAPI.sGeneratedMaterials[i] != null) lServerNames.put(
                        entry.getKey(),
                        entry.getValue()
                            .replace("material", GregTechAPI.sGeneratedMaterials[i].toString()));
                    else lServerNames.put(entry.getKey(), null);
                } else if (entry.getKey()
                    .contains("blockmetal")) {
                        Materials[] mMats = null;
                        String t = entry.getKey()
                            .substring("gt.blockmetal".length());
                        t = t.substring(0, 1);
                        int i = Integer.parseInt(t);
                        switch (i) {
                            case 1 -> mMats = new Materials[] { Materials.Adamantium, Materials.Aluminium,
                                Materials.Americium, Materials.AnnealedCopper, Materials.Antimony, Materials.Arsenic,
                                Materials.AstralSilver, Materials.BatteryAlloy, Materials.Beryllium, Materials.Bismuth,
                                Materials.BismuthBronze, Materials.BlackBronze, Materials.BlackSteel,
                                Materials.BlueAlloy, Materials.BlueSteel, Materials.Brass };
                            case 2 -> mMats = new Materials[] { Materials.Bronze, Materials.Caesium, Materials.Cerium,
                                Materials.Chrome, Materials.ChromiumDioxide, Materials.Cobalt, Materials.CobaltBrass,
                                Materials.Copper, Materials.Cupronickel, Materials.DamascusSteel, Materials.DarkIron,
                                Materials.DeepIron, Materials.Desh, Materials.Duranium, Materials.Dysprosium,
                                Materials.Electrum };
                            case 3 -> mMats = new Materials[] { Materials.ElectrumFlux, Materials.Enderium,
                                Materials.Erbium, Materials.Europium, Materials.FierySteel, Materials.Gadolinium,
                                Materials.Gallium, Materials.Holmium, Materials.HSLA, Materials.Indium,
                                Materials.InfusedGold, Materials.Invar, Materials.Iridium, Materials.IronMagnetic,
                                Materials.IronWood, Materials.Kanthal };
                            case 4 -> mMats = new Materials[] { Materials.Knightmetal, Materials.Lanthanum,
                                Materials.Lead, Materials.Lutetium, Materials.Magnalium, Materials.Magnesium,
                                Materials.Manganese, Materials.MeteoricIron, Materials.MeteoricSteel, Materials.Trinium,
                                Materials.Mithril, Materials.Molybdenum, Materials.Naquadah, Materials.NaquadahAlloy,
                                Materials.NaquadahEnriched, Materials.Naquadria };
                            case 5 -> mMats = new Materials[] { Materials.Neodymium, Materials.NeodymiumMagnetic,
                                Materials.Neutronium, Materials.Nichrome, Materials.Nickel, Materials.Niobium,
                                Materials.NiobiumNitride, Materials.NiobiumTitanium, Materials.Osmiridium,
                                Materials.Osmium, Materials.Palladium, Materials.PigIron, Materials.Platinum,
                                Materials.Plutonium, Materials.Plutonium241, Materials.Praseodymium };
                            case 6 -> mMats = new Materials[] { Materials.Promethium, Materials.RedAlloy,
                                Materials.RedSteel, Materials.RoseGold, Materials.Rubidium, Materials.Samarium,
                                Materials.Scandium, Materials.ShadowIron, Materials.ShadowSteel, Materials.Silicon,
                                Materials.Silver, Materials.SolderingAlloy, Materials.StainlessSteel, Materials.Steel,
                                Materials.SteelMagnetic, Materials.SterlingSilver };
                            case 7 -> mMats = new Materials[] { Materials.Sunnarium, Materials.Tantalum,
                                Materials.Tellurium, Materials.Terbium, Materials.Thaumium, Materials.Thorium,
                                Materials.Thulium, Materials.Tin, Materials.TinAlloy, Materials.Titanium,
                                Materials.Tritanium, Materials.Tungsten, Materials.TungstenSteel, Materials.Ultimet,
                                Materials.Uranium, Materials.Uranium235 };
                            case 8 -> mMats = new Materials[] { Materials.Vanadium, Materials.VanadiumGallium,
                                Materials.WroughtIron, Materials.Ytterbium, Materials.Yttrium,
                                Materials.YttriumBariumCuprate, Materials.Zinc, Materials.TungstenCarbide,
                                Materials.VanadiumSteel, Materials.HSSG, Materials.HSSE, Materials.HSSS,
                                Materials.Steeleaf, Materials.Ichorium, Materials.Firestone };
                        }
                        t = entry.getKey()
                            .substring(
                                "gt.blockmetal1.".length(),
                                entry.getKey()
                                    .length() - ".name".length());
                        i = Integer.parseInt(t);
                        lServerNames.put(entry.getKey(), "Block of " + mMats[i].toString());
                        mMats = null;
                    } else if (entry.getKey()
                        .contains("blockgem")) {
                            Materials[] mMats = null;
                            String t = entry.getKey()
                                .substring("gt.blockgem".length());
                            t = t.substring(0, 1);
                            int i = Integer.parseInt(t);
                            switch (i) {
                                case 1 -> mMats = new Materials[] { Materials.InfusedAir, Materials.Amber,
                                    Materials.Amethyst, Materials.InfusedWater, Materials.BlueTopaz,
                                    Materials.CertusQuartz, Materials.Dilithium, Materials.EnderEye,
                                    Materials.EnderPearl, Materials.FoolsRuby, Materials.Force, Materials.Forcicium,
                                    Materials.Forcillium, Materials.GreenSapphire, Materials.InfusedFire,
                                    Materials.Jasper, MaterialsBotania.ManaDiamond,
                                    MaterialsBotania.BotaniaDragonstone };
                                case 2 -> mMats = new Materials[] { Materials.Lazurite, Materials.Lignite,
                                    Materials.Monazite, Materials.Niter, Materials.Olivine, Materials.Opal,
                                    Materials.InfusedOrder, Materials.InfusedEntropy, Materials.Phosphorus,
                                    Materials.Quartzite, Materials.GarnetRed, Materials.Ruby, Materials.Sapphire,
                                    Materials.Sodalite, Materials.Tanzanite, Materials.InfusedEarth };
                                case 3 -> mMats = new Materials[] { Materials.Topaz, Materials.Vinteum,
                                    Materials.GarnetYellow, Materials.NetherStar, Materials.Charcoal, Materials.Blaze };
                            }
                            t = entry.getKey()
                                .substring(
                                    "gt.blockgem1.".length(),
                                    entry.getKey()
                                        .length() - ".name".length());
                            i = Integer.parseInt(t);
                            lServerNames.put(entry.getKey(), "Block of " + mMats[i].toString());
                            mMats = null;
                        }
            } catch (Exception ignored) {}
        }

        internal = null;
        internal2 = null;
        internal3 = null;
        internal4 = null;
    }
}
