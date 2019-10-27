package gregtech.api.util;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class GT_Assemblyline_Server {
    public static LinkedHashMap<String, String> lServerNames = new LinkedHashMap();
    private static LinkedHashMap<String, String> internal2 = new LinkedHashMap();
    private static LinkedHashMap<String, String> internal3 = new LinkedHashMap();
    private static LinkedHashMap<String, String> internal4 = new LinkedHashMap();
    private static LinkedHashMap<String, String> internal_meta = new LinkedHashMap();
    private static HashMap<String, Property> internal = new HashMap();

    public GT_Assemblyline_Server() {
    }

    public static void fillMap(FMLPreInitializationEvent aEvent) {
        System.out.println("SONCE FIX ENABLED");
        String s = new String(aEvent.getModConfigurationDirectory().getAbsolutePath());
        s = s.substring(0, aEvent.getModConfigurationDirectory().getAbsolutePath().length() - 6);
        s = s + "GregTech.lang";
        File f = new File(s);
        s = "";
        Configuration conf = new Configuration(f);
        ConfigCategory cat = conf.getCategory("languagefile");
        internal.putAll(cat.getValues());
        Iterator var5 = internal.entrySet().iterator();

        while(true) {
            Entry entry;
            while(var5.hasNext()) {
                entry = (Entry)var5.next();
                s = ((Property)entry.getValue()).getString().replaceAll("%", "");
                if (((String)entry.getKey()).contains("metaitem") && s.contains("material")) {
                    internal2.put((String)entry.getKey(), s);
                } else if (((String)entry.getKey()).contains("metaitem.01")) {
                    internal_meta.put((String)entry.getKey(), s);
                } else if (((String)entry.getKey()).contains("blockmachines") && s.contains("material")) {
                    internal3.put((String)entry.getKey(), s);
                } else if ((((String)entry.getKey()).contains("blockores") || ((String)entry.getKey()).contains("blockmetal") || ((String)entry.getKey()).contains("blockgem")) && s.contains("material")) {
                    internal4.put((String)entry.getKey(), s);
                } else {
                    lServerNames.put((String)entry.getKey(), s);
                }
            }

            var5 = internal_meta.entrySet().iterator();

            int i;
            while(var5.hasNext()) {
                entry = (Entry)var5.next();
                if (((String)entry.getKey()).contains("name")) {
                    try {
                        i = Integer.parseInt(((String)entry.getKey()).substring("gt.metaitem.01.".length(), ((String)entry.getKey()).length() - ".name".length()));
                        lServerNames.put((String)entry.getKey(),(String) entry.getValue());
                    } catch (Exception var14) {
                        ;
                    }
                }
            }

            var5 = internal2.entrySet().iterator();

            while(var5.hasNext()) {
                entry = (Entry)var5.next();
                if (((String)entry.getKey()).contains("name")) {
                    i = Integer.parseInt(((String)entry.getKey()).substring("gt.metaitem.01.".length(), ((String)entry.getKey()).length() - ".name".length()));
                    i %= 1000;

                    try {
                        lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", GregTech_API.sGeneratedMaterials[i].toString()));
                    } catch (Exception var13) {
                        ;
                    }
                }
            }

            var5 = internal3.entrySet().iterator();

            while(true) {
                while(var5.hasNext()) {
                    entry = (Entry)var5.next();
                    if (((String)entry.getKey()).contains("cable")) {
                        lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", ((String)entry.getKey()).substring("gt.blockmachines.cable.".length(), ((String)entry.getKey()).length() - ".01.name".length())));
                    } else if (((String)entry.getKey()).contains("gt_frame_")) {
                        lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", ((String)entry.getKey()).substring("gt.blockmachines.gt_frame_".length(), ((String)entry.getKey()).length() - ".name".length())));
                    } else if (((String)entry.getKey()).contains("gt_pipe_")) {
                        if (!((String)entry.getKey()).contains("_huge") && !((String)entry.getKey()).contains("_large") && !((String)entry.getKey()).contains("_nonuple") && !((String)entry.getKey()).contains("_quadruple") && !((String)entry.getKey()).contains("_small") && !((String)entry.getKey()).contains("_tiny")) {
                            lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", ((String)entry.getKey()).substring("gt.blockmachines.gt_pipe_".length(), ((String)entry.getKey()).length() - ".name".length())));
                        } else if (!((String)entry.getKey()).contains("_huge") && !((String)entry.getKey()).contains("_tiny")) {
                            if (!((String)entry.getKey()).contains("_large") && !((String)entry.getKey()).contains("_small")) {
                                if (((String)entry.getKey()).contains("_nonuple")) {
                                    lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", ((String)entry.getKey()).substring("gt.blockmachines.gt_pipe_".length(), ((String)entry.getKey()).length() - "_nonuple.name".length())));
                                } else if (((String)entry.getKey()).contains("_quadruple")) {
                                    lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", ((String)entry.getKey()).substring("gt.blockmachines.gt_pipe_".length(), ((String)entry.getKey()).length() - "_quadruple.name".length())));
                                }
                            } else {
                                lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", ((String)entry.getKey()).substring("gt.blockmachines.gt_pipe_".length(), ((String)entry.getKey()).length() - "_large.name".length())));
                            }
                        } else {
                            lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", ((String)entry.getKey()).substring("gt.blockmachines.gt_pipe_".length(), ((String)entry.getKey()).length() - "_tiny.name".length())));
                        }
                    } else if (((String)entry.getKey()).contains("wire")) {
                        lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", ((String)entry.getKey()).substring("gt.blockmachines.wire.".length(), ((String)entry.getKey()).length() - ".01.name".length())));
                    } else {
                        lServerNames.put((String)entry.getKey(), (String)entry.getValue());
                    }
                }

                var5 = internal4.entrySet().iterator();

                while(var5.hasNext()) {
                    entry = (Entry)var5.next();
                    if (((String)entry.getKey()).contains("blockores")) {
                        try {
                            i = Integer.parseInt(((String)entry.getKey()).substring("gt.blockores.".length(), ((String)entry.getKey()).length() - ".name".length()));
                            i %= 1000;
                            lServerNames.put((String)entry.getKey(), ((String)entry.getValue()).replace("material", GregTech_API.sGeneratedMaterials[i].toString()));
                        } catch (Exception var11) {
                            ;
                        }
                    } else {
                        String t;
                        Materials[] mMats;
                        if (((String)entry.getKey()).contains("blockmetal")) {
                            mMats = null;
                            t = ((String)entry.getKey()).substring("gt.blockmetal".length());
                            t = t.substring(0, 1);
                            i = Integer.parseInt(t);
                            switch(i) {
				case 1:
					mMats=new Materials[]{
			                Materials.Adamantium,
			                Materials.Aluminium,
			                Materials.Americium,
			                Materials.AnnealedCopper,
			                Materials.Antimony,
			                Materials.Arsenic,
			                Materials.AstralSilver,
			                Materials.BatteryAlloy,
			                Materials.Beryllium,
			                Materials.Bismuth,
			                Materials.BismuthBronze,
			                Materials.BlackBronze,
			                Materials.BlackSteel,
			                Materials.BlueAlloy,
			                Materials.BlueSteel,
			                Materials.Brass
			                };
					break;
				case 2:
					mMats=new Materials[]{
			                Materials.Bronze,
			                Materials.Caesium,
			                Materials.Cerium,
			                Materials.Chrome,
			                Materials.ChromiumDioxide,
			                Materials.Cobalt,
			                Materials.CobaltBrass,
			                Materials.Copper,
			                Materials.Cupronickel,
			                Materials.DamascusSteel,
			                Materials.DarkIron,
			                Materials.DeepIron,
			                Materials.Desh,
			                Materials.Duranium,
			                Materials.Dysprosium,
			                Materials.Electrum
			                };
					break;
				case 3:
					mMats=new Materials[]{
			                Materials.ElectrumFlux,
			                Materials.Enderium,
			                Materials.Erbium,
			                Materials.Europium,
			                Materials.FierySteel,
			                Materials.Gadolinium,
			                Materials.Gallium,
			                Materials.Holmium,
			                Materials.HSLA,
			                Materials.Indium,
			                Materials.InfusedGold,
			                Materials.Invar,
			                Materials.Iridium,
			                Materials.IronMagnetic,
			                Materials.IronWood,
			                Materials.Kanthal
					};
					break;
				case 4:
					mMats=new Materials[]{
							Materials.Knightmetal,
			                Materials.Lanthanum,
			                Materials.Lead,
			                Materials.Lutetium,
			                Materials.Magnalium,
			                Materials.Magnesium,
			                Materials.Manganese,
			                Materials.MeteoricIron,
			                Materials.MeteoricSteel,
			                Materials.Midasium,
			                Materials.Mithril,
			                Materials.Molybdenum,
			                Materials.Naquadah,
			                Materials.NaquadahAlloy,
			                Materials.NaquadahEnriched,
			                Materials.Naquadria
			                };
					break;
				case 5:
					mMats=new Materials[]{
							Materials.Neodymium,
			                Materials.NeodymiumMagnetic,
			                Materials.Neutronium,
			                Materials.Nichrome,
			                Materials.Nickel,
			                Materials.Niobium,
			                Materials.NiobiumNitride,
			                Materials.NiobiumTitanium,
			                Materials.Osmiridium,
			                Materials.Osmium,
			                Materials.Palladium,
			                Materials.PigIron,
			                Materials.Platinum,
			                Materials.Plutonium,
			                Materials.Plutonium241,
			                Materials.Praseodymium
					};
					break;
				case 6:
					mMats=new Materials[]{
							Materials.Promethium,
			                Materials.RedAlloy,
			                Materials.RedSteel,
			                Materials.RoseGold,
			                Materials.Rubidium,
			                Materials.Samarium,
			                Materials.Scandium,
			                Materials.ShadowIron,
			                Materials.ShadowSteel,
			                Materials.Silicon,
			                Materials.Silver,
			                Materials.SolderingAlloy,
			                Materials.StainlessSteel,
			                Materials.Steel,
			                Materials.SteelMagnetic,
			                Materials.SterlingSilver
					};
					break;
				case 7:
					mMats=new Materials[]{
							Materials.Sunnarium,
			                Materials.Tantalum,
			                Materials.Tellurium,
			                Materials.Terbium,
			                Materials.Thaumium,
			                Materials.Thorium,
			                Materials.Thulium,
			                Materials.Tin,
			                Materials.TinAlloy,
			                Materials.Titanium,
			                Materials.Tritanium,
			                Materials.Tungsten,
			                Materials.TungstenSteel,
			                Materials.Ultimet,
			                Materials.Uranium,
			                Materials.Uranium235
					};
					break;
				case 8:
					mMats=new Materials[]{
							Materials.Vanadium,
			                Materials.VanadiumGallium,
			                Materials.WroughtIron,
			                Materials.Ytterbium,
			                Materials.Yttrium,
			                Materials.YttriumBariumCuprate,
			                Materials.Zinc,
			                Materials.TungstenCarbide,
			                Materials.VanadiumSteel,
			                Materials.HSSG,
			                Materials.HSSE,
			                Materials.HSSS,
			                Materials.EuropiumoxideMagnetic,
			                Materials.Steeleaf,
			                Materials.Blaze,
			                Materials.Ichorium
					};
				}
				t = ((String)entry.getKey()).substring("gt.blockmetal1.".length(), ((String)entry.getKey()).length() - ".name".length());
                            i = Integer.parseInt(t);

                            try {
                                lServerNames.put((String)entry.getKey(), "Block of " + mMats[i].toString());
                            } catch (ArrayIndexOutOfBoundsException var12) {
                                mMats = null;
                            }
                        } else if (((String)entry.getKey()).contains("blockgem")) {
                            mMats = null;
                            t = ((String)entry.getKey()).substring("gt.blockgem".length());
                            t = t.substring(0, 1);
                            i = Integer.parseInt(t);
                            switch(i) {
				case 1:
					mMats=new Materials[]{
							  Materials.InfusedAir,
				              Materials.Amber,
				              Materials.Amethyst,
				              Materials.InfusedWater,
				              Materials.BlueTopaz,
				              Materials.CertusQuartz,
				              Materials.Dilithium,
				              Materials.EnderEye,
				              Materials.EnderPearl,
				              Materials.FoolsRuby,
				              Materials.Force,
				              Materials.Forcicium,
				              Materials.Forcillium,
				              Materials.GreenSapphire,
				              Materials.InfusedFire,
				              Materials.Jasper
			                };
					break;
				case 2:
					mMats=new Materials[]{
							  Materials.Lazurite,
				              Materials.Lignite,
				              Materials.Monazite,
				              Materials.Niter,
				              Materials.Olivine,
				              Materials.Opal,
				              Materials.InfusedOrder,
				              Materials.InfusedEntropy,
				              Materials.Phosphorus,
				              Materials.Quartzite,
				              Materials.GarnetRed,
				              Materials.Ruby,
				              Materials.Sapphire,
				              Materials.Sodalite,
				              Materials.Tanzanite,
				              Materials.InfusedEarth
			                };
					break;
				case 3:
					mMats=new Materials[]{
							  Materials.Topaz,
			                  Materials.Vinteum,
			                  Materials.GarnetYellow,
			                  Materials.NetherStar,
			                  Materials.Charcoal,
			                  Materials.Blaze
					};
				}
				t = ((String)entry.getKey()).substring("gt.blockgem1.".length(), ((String)entry.getKey()).length() - ".name".length());
                            i = Integer.parseInt(t);
                            lServerNames.put((String)entry.getKey(), "Block of " + mMats[i].toString());
                            mMats = null;
                        }
                    }
                }

                internal = null;
                internal2 = null;
                internal3 = null;
                internal4 = null;
                return;
            }
        }
    }
}
