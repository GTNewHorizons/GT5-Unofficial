package gtPlusPlus.xmod.forestry.bees.custom;

import forestry.api.apiculture.*;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.BeeDefinition;
import forestry.apiculture.genetics.BeeVariation;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.core.genetics.alleles.AlleleHelper;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary.Type;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;

public enum GTPP_Bee_Definition implements IBeeDefinition {


	SILICON(GTPP_Branch_Definition.ORGANIC, "Silicon", true, Utils.rgbtoHexValue(75, 75, 75), Utils.rgbtoHexValue(125, 125, 125)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(getSlagComb(), 0.10f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SILICON), 0.20f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.NORMAL);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(getGregtechBeeType("SLIMEBALL"), getGregtechBeeType("STICKYRESIN"), 10);
		}
	},

	RUBBER(GTPP_Branch_Definition.ORGANIC, "Rubber", true, Utils.rgbtoHexValue(55, 55, 55), Utils.rgbtoHexValue(75, 75, 75)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(getSlagComb(), 0.10f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.RUBBER), 0.30f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.NORMAL);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(getGregtechBeeType("SLIMEBALL"), getGregtechBeeType("STICKYRESIN"), 10);
		}
	},

	PLASTIC(GTPP_Branch_Definition.ORGANIC, "Plastic", true, Utils.rgbtoHexValue(245, 245, 245), Utils.rgbtoHexValue(175, 175, 175)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(getStoneComb(), 0.30f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.PLASTIC), 0.15f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.NORMAL);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(RUBBER.species, getGregtechBeeType("OIL"), 10);
		}
	},

	PTFE(GTPP_Branch_Definition.ORGANIC, "Ptfe", true, Utils.rgbtoHexValue(150, 150, 150), Utils.rgbtoHexValue(75, 75, 75)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(getStoneComb(), 0.30f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.PTFE), 0.10f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.NORMAL);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(RUBBER.species, PLASTIC.species, 10);
		}
	},

	PBS(GTPP_Branch_Definition.ORGANIC, "Pbs", true, Utils.rgbtoHexValue(33, 26, 24), Utils.rgbtoHexValue(23, 16, 14)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(getStoneComb(), 0.30f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.PBS), 0.10f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.NORMAL);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(PTFE.species, PLASTIC.species, 10);
		}
	},




	/**
	 * Fuels
	 */

	BIOMASS(GTPP_Branch_Definition.ORGANIC, "Biomass", true, Utils.rgbtoHexValue(33, 225, 24), Utils.rgbtoHexValue(23, 175, 14)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SAND), 0.40f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.BIOMASS), 0.20f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.NORMAL);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(getSpecies("Industrious"), getSpecies("Rural"), 10);
			tMutation.restrictBiomeType(Type.FOREST);
		}
	},

	ETHANOL(GTPP_Branch_Definition.ORGANIC, "Ethanol", true, Utils.rgbtoHexValue(255, 128, 0), Utils.rgbtoHexValue(220, 156, 32)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SAND), 0.40f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.ETHANOL), 0.20f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.NORMAL);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(BIOMASS.species, getSpecies("Farmerly"), 5);
			tMutation.restrictBiomeType(Type.FOREST);
		}
	},





















	/**
	 * Materials
	 */


	FLUORINE(GTPP_Branch_Definition.ORGANIC, "Fluorine", true, Utils.rgbtoHexValue(30, 230, 230), Utils.rgbtoHexValue(10, 150, 150)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(getStoneComb(), 0.40f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.FLUORINE), 0.05f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.COLD);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(getGregtechBeeType("LAPIS"), getGregtechBeeType("SAPPHIRE"), 5);
			tMutation.restrictBiomeType(Type.COLD);
		}
	},

	//Coke



	//Force
	FORCE(GTPP_Branch_Definition.METAL, "Force", true, Utils.rgbtoHexValue(250, 250, 20), Utils.rgbtoHexValue(200, 200, 5)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(getStoneComb(), 0.30f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SAND), 0.25f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.FORCE), 0.25f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SALT), 0.05f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.HOT);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(getGregtechBeeType("STEEL"), getGregtechBeeType("GOLD"), 10);
			tMutation.restrictBiomeType(Type.HOT);
		}
	},

	//Nikolite
	NIKOLITE(GTPP_Branch_Definition.METAL, "Nikolite", true, Utils.rgbtoHexValue(60, 180, 200), Utils.rgbtoHexValue(40, 150, 170)) {
		@Override
		protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
			beeSpecies.addProduct(getStoneComb(), 0.30f);
			beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.NIKOLITE), 0.05f);
			beeSpecies.setHumidity(EnumHumidity.NORMAL);
			beeSpecies.setTemperature(EnumTemperature.HOT);
		}

		@Override
		protected void setAlleles(IAllele[] template) {
			template = BeeDefinition.COMMON.getTemplate();
		}

		@Override
		protected void registerMutations() {
			IBeeMutationCustom tMutation = registerMutation(getGregtechBeeType("ALUMINIUM"), getGregtechBeeType("SILVER"), 8);
			tMutation.restrictBiomeType(Type.HOT);
		}
	},







	/*


	CLAY(GTPP_Branch_Definition.ORGANIC, "Clay", true, 0x19d0ec, 0xffdc16) {
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
    SLIMEBALL(GTPP_Branch_Definition.ORGANIC, "SlimeBall", true, 0x4E9E55, 0x00FF15) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 15), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STICKY), 0.30f);
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
    PEAT(GTPP_Branch_Definition.ORGANIC, "Peat", true, 0x906237, 0x58300B) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.LIGNIE), 0.30f);
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.15f);
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
    STICKYRESIN(GTPP_Branch_Definition.ORGANIC, "StickyResin", true, 0x2E8F5B, 0xDCC289) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f);
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
    COAL(GTPP_Branch_Definition.ORGANIC, "Coal", true, 0x666666, 0x525252) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.LIGNIE), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.COAL), 0.15f);
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
    OIL(GTPP_Branch_Definition.ORGANIC, "Oil", true, 0x4C4C4C, 0x333333) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.OIL), 0.15f);
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
    REDSTONE(GTPP_Branch_Definition.GEM, "Redstone", true, 0x7D0F0F, 0xD11919) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STONE), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.REDSTONE), 0.15f);
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
    LAPIS(GTPP_Branch_Definition.GEM, "Lapis", true, 0x1947D1, 0x476CDA) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STONE), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.LAPIS), 0.15f);
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
    CERTUS(GTPP_Branch_Definition.GEM, "CertusQuartz", true, 0x57CFFB, 0xBBEEFF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STONE), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.CERTUS), 0.15f);
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
    RUBY(GTPP_Branch_Definition.GEM, "Ruby", true, 0xE6005C, 0xCC0052) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STONE), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.RUBY), 0.15f);
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
    SAPPHIRE(GTPP_Branch_Definition.GEM, "Sapphire", true, 0x0033CC, 0x00248F) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STONE), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SAPPHIRE), 0.15f);
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
    DIAMOND(GTPP_Branch_Definition.GEM, "Diamond", true, 0xCCFFFF, 0xA3CCCC) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STONE), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.DIAMOND), 0.15f);
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
    OLIVINE(GTPP_Branch_Definition.GEM, "Olivine", true, 0x248F24, 0xCCFFCC) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STONE), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.OLIVINE), 0.15f);
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
    EMERALD(GTPP_Branch_Definition.GEM, "Emerald", true, 0x248F24, 0x2EB82E) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STONE), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.EMERALD), 0.15f);
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
    COPPER(GTPP_Branch_Definition.METAL, "Copper", true, 0xFF6600, 0xE65C00) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.COPPER), 0.15f);
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
    TIN(GTPP_Branch_Definition.METAL, "Tin", true, 0xD4D4D4, 0xDDDDDD) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.TIN), 0.15f);
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
    LEAD(GTPP_Branch_Definition.METAL, "Lead", true, 0x666699, 0xA3A3CC) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.LEAD), 0.15f);
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
    IRON(GTPP_Branch_Definition.METAL, "Iron", true, 0xDA9147, 0xDE9C59) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.IRON), 0.15f);
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
    STEEL(GTPP_Branch_Definition.METAL, "Steel", true, 0x808080, 0x999999) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.STEEL), 0.15f);
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
    NICKEL(GTPP_Branch_Definition.METAL, "Nickel", true, 0x8585AD, 0x8585AD) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.NICKEL), 0.15f);
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
    ZINC(GTPP_Branch_Definition.METAL, "Zinc", true, 0xF0DEF0, 0xF2E1F2) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.ZINC), 0.15f);
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
    SILVER(GTPP_Branch_Definition.METAL, "Silver", true, 0xC2C2D6, 0xCECEDE) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SILVER), 0.15f);
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
    GOLD(GTPP_Branch_Definition.METAL, "Gold", true, 0xEBC633, 0xEDCC47) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.GOLD), 0.15f);
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
    ALUMINIUM(GTPP_Branch_Definition.RAREMETAL, "Aluminium", true, 0xB8B8FF, 0xD6D6FF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.ALUMINIUM), 0.15f);
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
    TITANIUM(GTPP_Branch_Definition.RAREMETAL, "Titanium", true, 0xCC99FF, 0xDBB8FF) {
        @Override
        protected void setSpeciesProperties(IAlleleBeeSpeciesCustom beeSpecies) {
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.SLAG), 0.30f);
            beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CustomCombs.TITANIUM), 0.15f);
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
        }
    }*/

	;


	private final GTPP_Branch_Definition branch;
	private final IAlleleBeeSpeciesCustom species;

	private IAllele[] template;
	private IBeeGenome genome;

	GTPP_Bee_Definition(GTPP_Branch_Definition branch, String binomial, boolean dominant, int primary, int secondary) {
		String lowercaseName = this.toString().toLowerCase(Locale.ENGLISH);
		String species = "species" + WordUtils.capitalize(lowercaseName);

		String uid = "forestry." + species;
		String description = "for.description." + species;
		String name = "for.bees.species." + lowercaseName;

		this.branch = branch;
		this.species = BeeManager.beeFactory.createSpecies(uid, dominant, "Sengir", name, description, branch.getBranch(), binomial, primary, secondary);
	}

	public static void initBees() {
		for (GTPP_Bee_Definition bee : values()) {
			bee.init();
		}
		for (GTPP_Bee_Definition bee : values()) {
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
		IAllele[] template = getTemplate();
		Utils.LOG_INFO("[Bees-Debug] parent1: "+(parent1 != null));
		Utils.LOG_INFO("[Bees-Debug] parent2: "+(parent2 != null));
		Utils.LOG_INFO("[Bees-Debug] chance: "+(chance));
		Utils.LOG_INFO("[Bees-Debug] template: "+(template != null));
		return BeeManager.beeMutationFactory.createMutation(parent1, parent2, template, chance);
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

	private static ItemStack getSlagComb(){
		return issStackValid(ItemUtils.getSimpleStack(GTPP_Bees.Comb_Slag, 1));
	}
	private static ItemStack getStoneComb(){
		return issStackValid(ItemUtils.getSimpleStack(GTPP_Bees.Comb_Stone, 1));
	}

	private static ItemStack issStackValid(ItemStack result){
		if (result == null){
			return ItemUtils.getSimpleStack(ModItems.AAA_Broken);
		}
		return result;
	}

	public static IAlleleBeeSpecies getGregtechBeeType(String name){
		Class<?> gtBees;    	
		try {
			Class gtBeeTypes = Class.forName("gregtech.loaders.misc.GT_BeeDefinition");
			Enum gtBeeEnumObject = Enum.valueOf(gtBeeTypes, name); 	
			Field gtBeesField = FieldUtils.getDeclaredField(gtBeeTypes, "species", true);
			gtBeesField.setAccessible(true);
			ReflectionUtils.makeAccessible(gtBeesField);
			Object beeType = gtBeesField.get(gtBeeEnumObject);	    		    	
			return (IAlleleBeeSpecies) beeType;
		}		
		catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}    	
		return null;
	}
}
