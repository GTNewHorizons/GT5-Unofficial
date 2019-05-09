package gregtech.loaders.misc;

import forestry.api.apiculture.*;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.BeeDefinition;
import forestry.apiculture.genetics.BeeVariation;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.EnumAllele;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.items.CombType;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

import java.util.Arrays;
import java.util.Locale;

//import forestry.apiculture.items.EnumHoneyComb;
//import forestry.plugins.PluginApiculture;

public enum GT_BeeDefinition implements IBeeDefinition {
    CLAY(GT_BranchDefinition.ORGANIC, "Clay", true, 0x19d0ec, 0xffdc16) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addProduct(new ItemStack(Items.clay_ball, 1), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.MEADOWS.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies("Industrious"), getSpecies("Diligent"), 20);
        }
    },
    SLIMEBALL(GT_BranchDefinition.ORGANIC, "SlimeBall", true, 0x4E9E55, 0x00FF15) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 15), 0.30f);
            beeSpecies.addProduct(new ItemStack(Items.slime_ball, 1), 0.15f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STICKY), 0.30f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.MARSHY.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Marshy"), CLAY.species, 15);
        }
    },
    PEAT(GT_BranchDefinition.ORGANIC, "Peat", true, 0x906237, 0x58300B) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
        	beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LIGNIE), 0.30f);
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.15f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "peat", 1, 0), 0.15f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "mulch", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.RURAL.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Rural"), CLAY.species, 20);
        }
    },
    STICKYRESIN(GT_BranchDefinition.ORGANIC, "StickyResin", true, 0x2E8F5B, 0xDCC289) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STICKY), 0.15f);
            beeSpecies.addProduct(ItemList.IC2_Resin.get(1, new Object[0]), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.MEADOWS.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(SLIMEBALL.species, PEAT.species, 25);
        }
    },
    COAL(GT_BranchDefinition.ORGANIC, "Coal", true, 0x666666, 0x525252) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LIGNIE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.COAL), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.AUSTERE.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Industrious"), PEAT.species, 18);
        }
    },
    OIL(GT_BranchDefinition.ORGANIC, "Oil", true, 0x4C4C4C, 0x333333) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.OIL), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.MEADOWS.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(COAL.species, STICKYRESIN.species, 8);
        }
    },
    REDSTONE(GT_BranchDefinition.GEM, "Redstone", true, 0x7D0F0F, 0xD11919) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.REDSTONE), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.RAREEARTH), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Industrious"), getSpecies("Demonic"), 20);
        }
    },
    LAPIS(GT_BranchDefinition.GEM, "Lapis", true, 0x1947D1, 0x476CDA) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LAPIS), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Demonic"), getSpecies("Imperial"), 20);
        }
    },
    CERTUS(GT_BranchDefinition.GEM, "CertusQuartz", true, 0x57CFFB, 0xBBEEFF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CERTUS), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Hermitic"), LAPIS.species, 20);
        }
    },
    RUBY(GT_BranchDefinition.GEM, "Ruby", true, 0xE6005C, 0xCC0052) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.RUBY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(REDSTONE.species, DIAMOND.species, 10);
        }
    },
    SAPPHIRE(GT_BranchDefinition.GEM, "Sapphire", true, 0x0033CC, 0x00248F) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SAPPHIRE), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(CERTUS.species, LAPIS.species, 10);
        }
    },
    DIAMOND(GT_BranchDefinition.GEM, "Diamond", true, 0xCCFFFF, 0xA3CCCC) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.DIAMOND), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(CERTUS.species, COAL.species, 6);
        }
    },
    OLIVINE(GT_BranchDefinition.GEM, "Olivine", true, 0x248F24, 0xCCFFCC) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.OLIVINE), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(CERTUS.species, getSpecies("Ended"), 10);
        }
    },
    EMERALD(GT_BranchDefinition.GEM, "Emerald", true, 0x248F24, 0x2EB82E) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.EMERALD), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(OLIVINE.species, DIAMOND.species, 8);
        }
    },
    COPPER(GT_BranchDefinition.METAL, "Copper", true, 0xFF6600, 0xE65C00) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.COPPER), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GOLD), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Majestic"), CLAY.species, 25);
        }
    },
    TIN(GT_BranchDefinition.METAL, "Tin", true, 0xD4D4D4, 0xDDDDDD) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TIN), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ZINC), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(CLAY.species, getSpecies("Diligent"), 25);
        }
    },
    LEAD(GT_BranchDefinition.METAL, "Lead", true, 0x666699, 0xA3A3CC) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LEAD), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.WARM);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(COAL.species, COPPER.species, 25);
        }
    },
    IRON(GT_BranchDefinition.METAL, "Iron", true, 0xDA9147, 0xDE9C59) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.IRON), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.TIN), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(TIN.species, COPPER.species, 25);
        }
    },
    STEEL(GT_BranchDefinition.METAL, "Steel", true, 0x808080, 0x999999) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STEEL), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRON), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(IRON.species, COAL.species, 20);
        }
    },
    NICKEL(GT_BranchDefinition.METAL, "Nickel", true, 0x8585AD, 0x8585AD) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NICKEL), 0.15f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.02f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(IRON.species, COPPER.species, 25);
        }
    },
    ZINC(GT_BranchDefinition.METAL, "Zinc", true, 0xF0DEF0, 0xF2E1F2) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ZINC), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GALLIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(IRON.species, TIN.species, 20);
        }
    },
    SILVER(GT_BranchDefinition.METAL, "Silver", true, 0xC2C2D6, 0xCECEDE) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SILVER), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(LEAD.species, TIN.species, 20);
        }
    },
    GOLD(GT_BranchDefinition.METAL, "Gold", true, 0xEBC633, 0xEDCC47) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.GOLD), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NICKEL), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(LEAD.species, COPPER.species, 20);
        }
    },
    ALUMINIUM(GT_BranchDefinition.RAREMETAL, "Aluminium", true, 0xB8B8FF, 0xD6D6FF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ALUMINIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(NICKEL.species, ZINC.species, 18);
        }
    },
    TITANIUM(GT_BranchDefinition.RAREMETAL, "Titanium", true, 0xCC99FF, 0xDBB8FF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TITANIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ALMANDINE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(REDSTONE.species, ALUMINIUM.species, 5);
        	tMutation.requireResource(GregTech_API.sBlockMetal7, 9);
        }
    },
    CHROME(GT_BranchDefinition.RAREMETAL, "Chrome", true, 0xEBA1EB, 0xF2C3F2) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CHROME), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(TITANIUM.species, RUBY.species, 5);
        	tMutation.requireResource(GregTech_API.sBlockMetal2, 3);
        }
    },
    MANGANESE(GT_BranchDefinition.RAREMETAL, "Manganese", true, 0xD5D5D5, 0xAAAAAA) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MANGANESE), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(TITANIUM.species, ALUMINIUM.species, 5);
        	tMutation.requireResource(GregTech_API.sBlockMetal4, 6);
        }
    },
    TUNGSTEN(GT_BranchDefinition.RAREMETAL, "Tungsten", true, 0x5C5C8A, 0x7D7DA1) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TUNGSTEN), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Heroic"), MANGANESE.species, 5);
        	tMutation.requireResource("blockTungsten");
        }
    },
    PLATINUM(GT_BranchDefinition.RAREMETAL, "Platinum", true, 0xE6E6E6, 0xFFFFCC) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(DIAMOND.species, CHROME.species, 5);
        	tMutation.requireResource("blockPlatinum");
        }
    },
    IRIDIUM(GT_BranchDefinition.RAREMETAL, "Iridium", true, 0xDADADA, 0xD1D1E0) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TUNGSTEN), 0.15f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.15f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.COMMON.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(TUNGSTEN.species, PLATINUM.species, 5);
        	tMutation.requireResource("blockIridium");
        }
    },
    URANIUM(GT_BranchDefinition.RADIOACTIVE, "Uranium", true, 0x19AF19, 0x169E16) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.URANIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.AVENGING.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Avenging"), PLATINUM.species, 5);
        	tMutation.requireResource("blockUranium");
        }
    },
    PLUTONIUM(GT_BranchDefinition.RADIOACTIVE, "Plutonium", true, 0x335C33, 0x6B8F00) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLUTONIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.AVENGING.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(URANIUM.species, EMERALD.species, 5);
        	tMutation.requireResource("blockPlutonium");
        }
    },
    NAQUADAH(GT_BranchDefinition.RADIOACTIVE, "Naquadah", true, 0x003300, 0x002400) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NAQUADAH), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.AVENGING.getTemplate();
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(PLUTONIUM.species, IRIDIUM.species, 3);
        	tMutation.requireResource(GregTech_API.sBlockMetal4, 12);
        }
    },
    SANDWICH(GT_BranchDefinition.ORGANIC, "Sandwich", true, 0x32CD32, 0xDAA520) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
        	if(Loader.isModLoaded("ExtraBees")){
            beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 9), 0.15f);
        	}else {
        	beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.15f);	
        	}
            beeSpecies.addSpecialty(ItemList.Food_Sliced_Cucumber.get(1, new Object[0]), 0.05f);
            beeSpecies.addSpecialty(ItemList.Food_Sliced_Onion.get(1, new Object[0]), 0.05f);
            beeSpecies.addSpecialty(ItemList.Food_Sliced_Tomato.get(1, new Object[0]), 0.05f);
            beeSpecies.addSpecialty(ItemList.Food_Sliced_Cheese.get(1, new Object[0]), 0.05f);
            beeSpecies.addSpecialty(new ItemStack(Items.cooked_porkchop, 1, 0), 0.05f);
            beeSpecies.addSpecialty(new ItemStack(Items.cooked_beef, 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.RURAL.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies("Agrarian"), getSpecies("Rural"), 20);
        }
    },
    ASH(GT_BranchDefinition.ORGANIC, "Ash", true, 0x1e1a18, 0xc6c6c6) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
        	if(Loader.isModLoaded("ExtraBees")){
            beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 9), 0.15f);
        	}else {
        	beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.15f);
        	}
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ASH), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.MEADOWS.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(COAL.species, CLAY.species, 10);
            tMutation.restrictTemperature(EnumTemperature.HELLISH);
        }
    },
    APATITE(GT_BranchDefinition.ORGANIC, "Apatite", true, 0xc1c1f6, 0x676784) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
        	if(Loader.isModLoaded("ExtraBees")){
            beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 9), 0.15f);
        	}else {
        	beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.15f);
        	}
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.APATITE), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.WARM);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.MEADOWS.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(ASH.species, COAL.species, 10);
            tMutation.requireResource("blockApatite");
        }
    },

    FERTILIZER(GT_BranchDefinition.ORGANIC, "Fertilizer", true, 0x7fcef5, 0x654525) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
        	if(Loader.isModLoaded("ExtraBees")){
            beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 9), 0.15f);
        	}else {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.15f);
            }
            beeSpecies.addSpecialty(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Ash, 1), 0.2f);
            beeSpecies.addSpecialty(GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.DarkAsh, 1), 0.2f);
            beeSpecies.addSpecialty(ItemList.FR_Fertilizer.get(1, new Object[0]), 0.3f);
            beeSpecies.addSpecialty(ItemList.IC2_Fertilizer.get(1, new Object[0]), 0.3f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.WARM);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            template = BeeDefinition.MEADOWS.getTemplate();
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(ASH.species, APATITE.species, 8);
        }
    },
  //IC2
    COOLANT(GT_BranchDefinition.IC2, "Coolant", false, 0x144F5A, 0x2494A2) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 4), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.COOLANT), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.UP_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.SNOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectGlacial);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies("Icy"), getSpecies("Glacial"), 10);
            tMutation.requireResource(Block.getBlockFromItem(GT_ModHandler.getModItem("IC2", "fluidCoolant", 1).getItem()), 0);
            tMutation.restrictTemperature(EnumTemperature.ICY);
        }
    },
    ENERGY(GT_BranchDefinition.IC2, "Energy", false, 0xC11F1F, 0xEBB9B9) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
        	if(Loader.isModLoaded("ExtraBees")){
            beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 12), 0.30f);
        	}
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENERGY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.WARM);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectIgnition);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.DOWN_2);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.NETHER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.AVERAGE);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies("Demonic"), getSpecies("Rural"), 10);
            tMutation.requireResource(Block.getBlockFromItem(GT_ModHandler.getModItem("IC2", "fluidHotCoolant", 1).getItem()), 0);
        }
    },
    LAPOTRON(GT_BranchDefinition.IC2, "Lapotron", false, 0x6478FF, 0x1414FF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LAPIS), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENERGY), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LAPOTRON), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectIgnition);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.UP_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.SNOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.AVERAGE);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(LAPIS.species, ENERGY.species, 6);
            tMutation.requireResource("blockLapis");
            tMutation.restrictTemperature(EnumTemperature.ICY);
            if(Loader.isModLoaded("Galacticraft")){
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(28, "Moon"));//moon dim
            }
        }
    },
  //Alloy
    REDALLOY(GT_BranchDefinition.GTALLOY, "RedAlloy", false, 0xE60000, 0xB80000) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDALLOY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(COPPER.species, REDSTONE.species, 10);
            tMutation.requireResource("blockRedAlloy");
        }
    },
    REDSTONEALLOY(GT_BranchDefinition.GTALLOY, "RedStoneAlloy", false, 0xA50808, 0xE80000) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDSTONEALLOY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(REDSTONE.species, REDALLOY.species, 8);
            tMutation.requireResource("blockRedstoneAlloy");
        }
    },
    CONDUCTIVEIRON(GT_BranchDefinition.GTALLOY, "ConductiveIron", false, 0xCEADA3, 0x817671) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CONDUCTIVEIRON), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.WARM);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(REDSTONEALLOY.species, IRON.species, 8);
            tMutation.requireResource("blockConductiveIron");
        }
    },
    VIBRANTALLOY(GT_BranchDefinition.GTALLOY, "VibrantAlloy", false, 0x86A12D, 0xC4F2AE) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.VIBRANTALLOY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(ENERGETICALLOY.species, getSpecies("Phantasmal"), 6);
            tMutation.requireResource("blockVibrantAlloy");
            tMutation.restrictTemperature(EnumTemperature.HOT, EnumTemperature.HELLISH);
        }
    },
    ENERGETICALLOY(GT_BranchDefinition.GTALLOY, "EnergeticAlloy", false, 0xFF9933, 0xFFAD5C) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENERGETICALLOY), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(REDSTONEALLOY.species, getSpecies("Demonic"), 9);
            tMutation.requireResource("blockEnergeticAlloy");
        }
    },
    ELECTRICALSTEEL(GT_BranchDefinition.GTALLOY, "ElectricalSteel", false, 0x787878, 0xD8D8D8) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ELECTRICALSTEEL), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(STEEL.species, getSpecies("Demonic"), 9);
            tMutation.requireResource("blockElectricalSteel");
        }
    },
    DARKSTEEL(GT_BranchDefinition.GTALLOY, "DarkSteel", false, 0x252525, 0x443B44) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DARKSTEEL), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(ELECTRICALSTEEL.species, getSpecies("Demonic"), 7);
            tMutation.requireResource("blockDarkSteel");
        }
    },
    PULSATINGIRON(GT_BranchDefinition.GTALLOY, "PulsatingIron", false, 0x6DD284, 0x006600) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 7), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PULSATINGIRON), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(REDALLOY.species, getSpecies("Ended"), 9);
            tMutation.requireResource("blockPulsatingIron");
        }
    },
    STAINLESSSTEEL(GT_BranchDefinition.GTALLOY, "StainlessSteel", false, 0xC8C8DC, 0x778899) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STEEL), 0.10f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STAINLESSSTEEL), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CHROME), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT,  AlleleEffect.effectIgnition);
        }


        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(CHROME.species, STEEL.species, 9);
            tMutation.requireResource("blockStainlessSteel");
        }
    },
    ENDERIUM(GT_BranchDefinition.GTALLOY, "Enderium", false, 0x599087, 0x2E8B57) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENDERIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CHROME), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, GT_Bees.speedBlinding);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(PLATINUM.species, getSpecies("Phantasmal"), 3);
            tMutation.requireResource("blockEnderium");
        }
    },

    //Gem Line 2
    FLUIX(GT_BranchDefinition.GEM, "FluixDust", true, 0xA375FF, 0xB591FF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.FLUIX), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(REDSTONE.species, LAPIS.species, 7);
            tMutation.requireResource("blockFluix");
        }
    },
    REDGARNET(GT_BranchDefinition.GEM, "RedGarnet", false, 0xBD4C4C, 0xECCECE) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDGARNET), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PYROPE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.WARM);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(DIAMOND.species, RUBY.species, 4);
            tMutation.requireResource("blockGarnetRed");
        }
    },
    YELLOWGARNET(GT_BranchDefinition.GEM, "YellowGarnet", false, 0xA3A341, 0xEDEDCE) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.YELLOWGARNET), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GROSSULAR), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.WARM);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(EMERALD.species, REDGARNET.species, 3);
            tMutation.requireResource("blockGarnetYellow");
        }
    },
    
    //Metal Line 2
    ARSENIC(GT_BranchDefinition.METAL, "Arsenic", true, 0x736C52, 0x292412) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ARSENIC), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.WARM);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(ZINC.species, SILVER.species, 10);
            tMutation.requireResource("blockArsenic");
        }
    },
    
    //Rare Material Line 2
    OSMIUM(GT_BranchDefinition.RAREMETAL, "Osmium", false, 0x2B2BDA, 0x8B8B8B) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OSMIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(TUNGSTEN.species, PLATINUM.species, 5);
            tMutation.requireResource("blockOsmium");
        }
    },
    LITHIUM(GT_BranchDefinition.RAREMETAL, "Lithium", false, 0xF0328C, 0xE1DCFF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LITHIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALT), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(SALTY.species, ALUMINIUM.species, 5);
            tMutation.requireResource("oreLithium");
        }
    },
    SALTY(GT_BranchDefinition.RAREMETAL, "Salt", true, 0xF0C8C8, 0xFAFAFA) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALT), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LITHIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.WARM);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(CLAY.species, ALUMINIUM.species, 5);
            tMutation.requireResource("oreSalt");
        }
    },
    
    //Radioactive Line 2
    NAQUADRIA(GT_BranchDefinition.RADIOACTIVE, "Naquadria", false, 0x000000, 0x002400) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAQUADAH), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAQUADRIA), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(PLUTONIUM.species, IRIDIUM.species, 8);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 15);
        }
    },
    THORIUM(GT_BranchDefinition.RADIOACTIVE, "Thorium", false, 0x005000, 0x001E00) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.THORIUM), 0.75f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setNocturnal();
            beeSpecies.setIsSecret();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(COAL.species, URANIUM.species, 2);
            tMutation.requireResource(GregTech_API.sBlockMetal7, 5);
        }
    },
    LUTETIUM(GT_BranchDefinition.RADIOACTIVE, "Lutetium", false, 0xE6FFE6, 0xFFFFFF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LUTETIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            beeSpecies.setIsSecret();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(THORIUM.species, getSpecies("Phantasmal"), 1);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 3);
        }
    },
    AMERICIUM(GT_BranchDefinition.RADIOACTIVE, "Americium", false, 0xE6E6FF, 0xC8C8C8) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.AMERICUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
            beeSpecies.setIsSecret();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(LUTETIUM.species, CHROME.species, 4);
            tMutation.requireResource(GregTech_API.sBlockMetal1, 2);
        }
    },
    NEUTRONIUM(GT_BranchDefinition.RADIOACTIVE, "Neutronium", false, 0xFFF0F0, 0xFAFAFA) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEUTRONIUM), 0.0001f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
            beeSpecies.setHasEffect();
            beeSpecies.setIsSecret();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(NAQUADRIA.species, AMERICIUM.species, 1);
            tMutation.requireResource(GregTech_API.sBlockMetal5, 2);
        }
    },
    
    //Space Line
    SPACE(GT_BranchDefinition.SPACE, "Space", true, 0x003366, 0xC0C0C0) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.02f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies("Industrious"), getSpecies("Heroic"), 10);
            tMutation.restrictTemperature(EnumTemperature.ICY);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(26, "SpaceStation"));//SS Dim
        }
    },
    METEORICIRON(GT_BranchDefinition.SPACE, "MeteoricIron", true, 0x321928, 0x643250) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.04f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.METEORICIRON), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(SPACE.species, IRON.species, 9);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 7);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(28, "Moon"));//Moon Dim
        }
    },
    DESH(GT_BranchDefinition.SPACE, "Desh", false, 0x323232, 0x282828) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.06f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DESH), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectIgnition);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MARS.species, TITANIUM.species, 9);
            tMutation.requireResource(GregTech_API.sBlockMetal2, 12);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(29, "Mars"));//Mars Dim
        }
    },
    LEDOX(GT_BranchDefinition.SPACE, "Ledox", false, 0x0000CD, 0x0074FF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.10f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LEDOX), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectGlacial);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MOON.species, LEAD.species, 7);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(28, "Moon"));//Moon Dim
        }
    },
    QUANTIUM(GT_BranchDefinition.SPACE, "Quantium", false, 0x00FF00, 0x00D10B) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.16f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.QUANTIUM), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MARS.species, NEUTRONIUM.species, 6);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(29, "Mars"));//Mars Dim
        }
    },
    ORIHARUKON(GT_BranchDefinition.SPACE, "Oriharukon", false, 0x228B22, 0x677D68) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.26f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ORIHARUKON), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(AMERICIUM.species, MARS.species, 5);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(29, "Mars"));//Mars Dim
        }
    },
    MYSTERIOUSCRYSTAL(GT_BranchDefinition.SPACE, "MysteriousCrystal", false, 0x3CB371, 0x16856C) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.42f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MYSTERIOUSCRYSTAL), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(ASTEROID.species, EMERALD.species, 3);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteroid"));//Asteroid Dim
        }
    },
    BLACKPLUTONIUM(GT_BranchDefinition.SPACE, "BlackPlutonium", false, 0x000000, 0x323232) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.68f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.BLACKPLUTONIUM), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(ASTEROID.species, QUANTIUM.species, 2);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteroid"));//Asteroid Dim
        }
    },
    
    //Planet Line
    MOON(GT_BranchDefinition.PLANET, "Moon", false, 0x373735, 0x7E7E78) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MOON), 0.50f);
            if (Loader.isModLoaded("dreamcraft"))
            beeSpecies.addSpecialty(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Moon, 1L), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(SPACE.species, CLAY.species, 25);
            if (Loader.isModLoaded("GalacticraftCore"))
                tMutation.requireResource(GameRegistry.findBlock("GalacticraftCore", "tile.moonBlock"), 4);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(28, "Moon"));//Moon Dim
        }
    },
    MARS(GT_BranchDefinition.PLANET, "Mars", false, 0x220D05, 0x3A1505) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MARS), 0.50f);
            beeSpecies.addSpecialty(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Mars, 1L), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MOON.species, IRON.species, 20);
            if (Loader.isModLoaded("GalacticraftMars"))
                tMutation.requireResource(GameRegistry.findBlock("GalacticraftMars", "tile.mars"), 5);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(29, "Mars"));//Mars Dim
        }
    },
    ASTEROID(GT_BranchDefinition.PLANET, "Asteroid", false, 0x000000, 0x323232) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ASTEROID), 0.50f);
            beeSpecies.addSpecialty(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Asteroid, 1L), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MARS.species, STEEL.species, 20);
            if (Loader.isModLoaded("GalacticraftMars"))
            tMutation.requireResource(GameRegistry.findBlock("GalacticraftMars", "tile.asteroidsBlock"), 2);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteroid"));//Asteroid Dim
        }
    },
    
    ;


    private final GT_BranchDefinition branch;
    private final IAlleleBeeSpeciesCustom species;

    private IAllele[] template;
    private IBeeGenome genome;

    GT_BeeDefinition(GT_BranchDefinition branch, String binomial, boolean dominant, int primary, int secondary) {
        String lowercaseName = this.toString().toLowerCase(Locale.ENGLISH);
        String species = "species" + WordUtils.capitalize(lowercaseName);

        String uid = "forestry." + species;
        String description = "for.description." + species;
        String name = "for.bees.species." + lowercaseName;

        this.branch = branch;
        this.species = BeeManager.beeFactory.createSpecies(uid, dominant, "Sengir", name, description, branch.getBranch(), binomial, primary, secondary);
    }

    public static void initBees() {
        for (GT_BeeDefinition bee : values()) {
            bee.init();
        }
        for (GT_BeeDefinition bee : values()) {
            bee.registerMutations();
        }
    }

    private static IAlleleBeeSpecies getSpecies(String name) {
        return (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele((new StringBuilder()).append("forestry.species").append(name).toString());
    }

    protected abstract void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies);

    protected abstract void setAlleles(IAllele[] template);

    protected abstract void registerMutations();

    private void init() {
        setSpeciesProperties(species);

        template = branch.getTemplate();
        AlleleHelper.instance.set(template, EnumBeeChromosome.SPECIES, species);
        setAlleles(template);

        genome = BeeManager.beeRoot.templateAsGenome(template);

        BeeManager.beeRoot.registerTemplate(template);
    }

    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance) {
        return BeeManager.beeMutationFactory.createMutation(parent1, parent2, getTemplate(), chance);
    }

    @Override
    public final IAllele[] getTemplate() {
        return Arrays.copyOf(template, template.length);
    }

    @Override
    public final IBeeGenome getGenome() {
        return genome;
    }

    @Override
    public final IBee getIndividual() {
        return new Bee(genome);
    }

    @Override
    public final ItemStack getMemberStack(EnumBeeType beeType) {
        IBee bee = getIndividual();
        return BeeManager.beeRoot.getMemberStack(bee, beeType.ordinal());
    }

    public final IBeeDefinition getRainResist() {
        return new BeeVariation.RainResist(this);
    }

}