package gregtech.loaders.misc;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.*;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.AlleleManager;
import forestry.api.genetics.IAllele;
import forestry.api.genetics.IAlleleFlowers;
import forestry.apiculture.genetics.Bee;
import forestry.apiculture.genetics.BeeVariation;
import forestry.apiculture.genetics.IBeeDefinition;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.EnumAllele;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.bees.GT_AlleleBeeSpecies;
import gregtech.common.bees.GT_Bee_Mutation;
import gregtech.common.items.CombType;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.BiomeDictionary;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.Locale;

public enum GT_BeeDefinition implements IBeeDefinition {

    //organic
    CLAY(GT_BranchDefinition.ORGANIC, "Clay", true, 0xC8C8DA, 0x0000FF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addProduct(new ItemStack(Items.clay_ball, 1), 0.15f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("BiomesOPlenty", "mudball", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            //Example
            //beeSpecies.setIsSecret();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.VANILLA);
            //Exaples
            //template = BeeDefinition.CULTIVATED.getTemplate();
            //AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FASTEST);
            //tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(-1));
            //tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(50));
            //tMutation.requireResource(Blocks.coal_block, 0);
            //AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.DOWN_1);
            //tMutation.requireResource(GameRegistry.findBlock("minecraft", "sand"), 1);
            //AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, GT_Bees.superLife);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY, "Industrious"), getSpecies(FORRESTRY, "Diligent"), 10);
            tMutation.requireResource("blockClay");
        }
    },
    SLIMEBALL(GT_BranchDefinition.ORGANIC, "SlimeBall", true, 0x4E9E55, 0x00FF15) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 15), 0.30f);
            beeSpecies.addProduct(new ItemStack(Items.slime_ball, 1), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STICKY), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            if (Loader.isModLoaded("TConstruct")) {
                beeSpecies.addProduct(GT_ModHandler.getModItem("TConstruct", "strangeFood", 1, 0), 0.10f);
                beeSpecies.addSpecialty(GT_ModHandler.getModItem("TConstruct", "slime.gel", 1, 2), 0.01f);
            }

        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.MUSHROOMS);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            //AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, getFlowers(EXTRABEES, "water"));
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Marshy"), CLAY.species, 7);
        	if (Loader.isModLoaded("TConstruct"))
                tMutation.requireResource(GameRegistry.findBlock("TConstruct", "slime.gel"), 1);
        }
    },
    PEAT(GT_BranchDefinition.ORGANIC, "Peat", true, 0x906237, 0x58300B) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LIGNIE), 0.30f);
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.15f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "peat", 1, 0), 0.15f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "mulch", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.WHEAT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FASTER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Rural"), CLAY.species, 10);
        }
    },
    STICKYRESIN(GT_BranchDefinition.ORGANIC, "StickyResin", true, 0x2E8F5B, 0xDCC289) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.STICKY), 0.15f);
            beeSpecies.addSpecialty(ItemList.IC2_Resin.get(1, new Object[0]), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(SLIMEBALL.species, PEAT.species, 15);
            tMutation.requireResource("logRubber");
        }
    },
    COAL(GT_BranchDefinition.ORGANIC, "Coal", true, 0x666666, 0x525252) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LIGNIE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.COAL), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.CACTI);
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.DOWN_2);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.DOWN_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectCreeper);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Industrious"), PEAT.species, 9);
            tMutation.requireResource("blockCoal");
        }
    },
    OIL(GT_BranchDefinition.ORGANIC, "Oil", true, 0x4C4C4C, 0x333333) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 0), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OIL), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.NORMAL);
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            //AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, getFlowers(EXTRABEES, "water"));
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE);

        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(COAL.species, STICKYRESIN.species, 4);
        }
    },
    SANDWICH(GT_BranchDefinition.ORGANIC, "Sandwich", true, 0x32CD32, 0xDAA520) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            //beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 9), 0.15f);
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
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_2);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectFertile);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TERRITORY, EnumAllele.Territory.LARGE);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.WHEAT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.FASTER);

        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Agrarian"), getSpecies(FORRESTRY,"Demonic"), 10);
        }
    },

    //IC2
    COOLANT(GT_BranchDefinition.IC2, "Coolant", false, 0x144F5A, 0x2494A2) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Icy"), getSpecies(FORRESTRY,"Glacial"), 10);
            tMutation.requireResource(Block.getBlockFromItem(GT_ModHandler.getModItem("IC2", "fluidCoolant", 1).getItem()), 0);
            tMutation.restrictTemperature(EnumTemperature.ICY);
        }
    },
    ENERGY(GT_BranchDefinition.IC2, "Energy", false, 0xC11F1F, 0xEBB9B9) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            //beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 12), 0.30f);
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
            IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Demonic"), getSpecies(FORRESTRY,"Phantasmal"), 10);
            tMutation.requireResource(Block.getBlockFromItem(GT_ModHandler.getModItem("IC2", "fluidHotCoolant", 1).getItem()), 0);
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(128, "Boneyard Biome"));//Boneyard Biome
        }
    },
    LAPOTRON(GT_BranchDefinition.IC2, "Lapotron", false, 0x6478FF, 0x1414FF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(28, "Moon"));//moon dim
        }
    },
    //Alloy
    REDALLOY(GT_BranchDefinition.GTALLOY, "RedAlloy", false, 0xE60000, 0xB80000) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
	/*
    REDSTONEALLOY(GT_BranchDefinition.GTALLOY, "RedStoneAlloy", false, 0xA50808, 0xE80000) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(ENERGETICALLOY.species, getSpecies(FORRESTRY,"Phantasmal"), 6);
            tMutation.requireResource("blockVibrantAlloy");
            tMutation.restrictTemperature(EnumTemperature.HOT, EnumTemperature.HELLISH);
        }
    },
    ENERGETICALLOY(GT_BranchDefinition.GTALLOY, "EnergeticAlloy", false, 0xFF9933, 0xFFAD5C) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(REDSTONEALLOY.species, getSpecies(FORRESTRY,"Demonic"), 9);
            tMutation.requireResource("blockEnergeticAlloy");
        }
    },
    ELECTRICALSTEEL(GT_BranchDefinition.GTALLOY, "ElectricalSteel", false, 0x787878, 0xD8D8D8) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(STEEL.species, getSpecies(FORRESTRY,"Demonic"), 9);
            tMutation.requireResource("blockElectricalSteel");
        }
    },
    DARKSTEEL(GT_BranchDefinition.GTALLOY, "DarkSteel", false, 0x252525, 0x443B44) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(ELECTRICALSTEEL.species, getSpecies(FORRESTRY,"Demonic"), 7);
            tMutation.requireResource("blockDarkSteel");
        }
    },
    PULSATINGIRON(GT_BranchDefinition.GTALLOY, "PulsatingIron", false, 0x6DD284, 0x006600) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(REDALLOY.species, getSpecies(FORRESTRY,"Ended"), 9);
            tMutation.requireResource("blockPulsatingIron");
        }
    },
	*/
    STAINLESSSTEEL(GT_BranchDefinition.GTALLOY, "StainlessSteel", false, 0xC8C8DC, 0x778899) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ENDERIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CHROME), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, GT_Bees.speedBlinding);
            //AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, getEffect(EXTRABEES, "teleport"));
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(PLATINUM.species, getSpecies(FORRESTRY,"Phantasmal"), 3);
            tMutation.requireResource("blockEnderium");
        }
    },
    /*//thaumic
    THAUMIUMDUST(GT_BranchDefinition.THAUMIC, "ThaumiumDust", true, 0x7A007A, 0x5C005C) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.THAUMIUMDUST), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_2);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectExploration);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.UP_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.JUNGLE);
          }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies(MAGICBEES,"TCFire"), getSpecies(FORRESTRY,"Edenic"), 10);
            tMutation.requireResource("blockThaumium");
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(192, "Magical Forest"));//magical forest
        }
    },
    THAUMIUMSHARD(GT_BranchDefinition.THAUMIC, "ThaumiumShard", true, 0x9966FF, 0xAD85FF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.THAUMIUMDUST), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.THAUMIUMSHARD), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.UP_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.SNOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectGlacial);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(THAUMIUMDUST.species, getSpecies(MAGICBEES,"TCWater"), 10);
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(192, "Magical Forest"));//magical forest
        }
    },
    AMBER(GT_BranchDefinition.THAUMIC, "Amber", true, 0xEE7700, 0x774B15) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.AMBER), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(THAUMIUMDUST.species, STICKYRESIN.species, 10);
            tMutation.requireResource("blockAmber");
        }
    },
    QUICKSILVER(GT_BranchDefinition.THAUMIC, "Quicksilver", true, 0x7A007A, 0x5C005C) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.QUICKSILVER), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.UP_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.UP_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.JUNGLE);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectMiasmic);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(THAUMIUMDUST.species, SILVER.species, 10);
        }
    },
    SALISMUNDUS(GT_BranchDefinition.THAUMIC, "SalisMundus", true, 0xF7ADDE, 0x592582) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.UP_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.UP_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.JUNGLE);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectMiasmic);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(THAUMIUMDUST.species, THAUMIUMSHARD.species, 8);
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(192, "Magical Forest"));//magical forest
        }
    },
    TAINTED(GT_BranchDefinition.THAUMIC, "Tainted", true, 0x904BB8, 0xE800FF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 3), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.TAINTED), 0.20f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TOLERANT_FLYER, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.LOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(THAUMIUMDUST.species, THAUMIUMSHARD.species, 7);
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(193, "Tainted Land"));//Tainted Land
        }
    },
    MITHRIL(GT_BranchDefinition.THAUMIC, "Mithril", true, 0xF0E68C, 0xFFFFD2) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MITHRIL), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TOLERANT_FLYER, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.LOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(IO.species, PLATINUM.species, 7);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 10);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(36, "IO"));//IO Dim
        }
    },
    ASTRALSILVER(GT_BranchDefinition.THAUMIC, "AstralSilver", true, 0xAFEEEE, 0xE6E6FF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SILVER), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ASTRALSILVER), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TOLERANT_FLYER, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FERTILITY, EnumAllele.Fertility.LOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.BOTH_1);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, getFlowers(EXTRABEES, "rock"));
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(SILVER.species, IRON.species, 3);
            tMutation.requireResource(GregTech_API.sBlockMetal1, 6);
        }
    },
    THAUMINITE(GT_BranchDefinition.THAUMIC, "Thauminite", true, 0x2E2D79, 0x7581E0) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem("MagicBees", "comb", 1, 19), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.THAUMINITE), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies(MAGICBEES,"TCOrder"), THAUMIUMDUST.species, 8);
            if (Loader.isModLoaded("thaumicbases"))
                tMutation.requireResource(GameRegistry.findBlock("thaumicbases", "thauminiteBlock"), 0);
        }
    },
    SHADOWMETAL(GT_BranchDefinition.THAUMIC, "ShadowMetal", true, 0x100322, 0x100342) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem("MagicBees", "comb", 1, 20), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SHADOWMETAL), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.NONE);
            AlleleHelper.instance.set(template, EnumBeeChromosome.HUMIDITY_TOLERANCE, EnumAllele.Tolerance.NONE);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies(MAGICBEES,"TCChaos"), getSpecies(MAGICBEES,"TCVoid"), 6);
            if (Loader.isModLoaded("TaintedMagic"))
                tMutation.requireResource(GameRegistry.findBlock("TaintedMagic", "BlockShadowmetal"), 0);
        }
    },
    DIVIDED(GT_BranchDefinition.THAUMIC, "Unstable", true, 0xF0F0F0, 0xDCDCDC) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 61), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DIVIDED), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.SLOW);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(DIAMOND.species, IRON.species, 3);
            if (Loader.isModLoaded("ExtraUtilities"))
                tMutation.requireResource(GameRegistry.findBlock("ExtraUtilities", "decorativeBlock1"), 5);
        }
    },
    SPARKELING(GT_BranchDefinition.THAUMIC, "NetherStar", true, 0x7A007A, 0xFFFFFF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_ModHandler.getModItem("MagicBees", "miscResources", 1, 3), 0.20f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SPARKELING), 0.125f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.TEMPERATURE_TOLERANCE, EnumAllele.Tolerance.DOWN_2);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.CAVE_DWELLING, true);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWER_PROVIDER, EnumAllele.Flowers.NETHER);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORT);
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, AlleleEffect.effectAggressive);
            AlleleHelper.instance.set(template, EnumBeeChromosome.FLOWERING, EnumAllele.Flowering.AVERAGE);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies(MAGICBEES,"Withering"), getSpecies(MAGICBEES, "Draconic"), 1);
            tMutation.requireResource(GregTech_API.sBlockGem3, 3);
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(9, "END Biome"));//sky end biome
        }
    },
    */
//gems
    REDSTONE(GT_BranchDefinition.GEM, "Redstone", true, 0x7D0F0F, 0xD11919) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDSTONE), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.RAREEARTH), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Industrious"), getSpecies(FORRESTRY,"Demonic"), 10);
            tMutation.requireResource("blockRedstone");
        }
    },
    LAPIS(GT_BranchDefinition.GEM, "Lapis", true, 0x1947D1, 0x476CDA) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LAPIS), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Demonic"), getSpecies(FORRESTRY,"Imperial"), 10);
            tMutation.requireResource("blockLapis");
        }
    },
    CERTUS(GT_BranchDefinition.GEM, "CertusQuartz", true, 0x57CFFB, 0xBBEEFF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CERTUS), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Hermitic"), LAPIS.species, 10);
            tMutation.requireResource("blockCertusQuartz");
        }
    },
    FLUIX(GT_BranchDefinition.GEM, "FluixDust", true, 0xA375FF, 0xB591FF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
    RUBY(GT_BranchDefinition.GEM, "Ruby", false, 0xE6005C, 0xCC0052) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.RUBY), 0.15f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.REDGARNET), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(REDSTONE.species, DIAMOND.species, 5);
            tMutation.requireResource("blockRuby");
        }
    },
    SAPPHIRE(GT_BranchDefinition.GEM, "Sapphire", true, 0x0033CC, 0x00248F) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SAPPHIRE), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(CERTUS.species, LAPIS.species, 5);
            tMutation.requireResource(GregTech_API.sBlockGem2, 13);
        }
    },
    DIAMOND(GT_BranchDefinition.GEM, "Diamond", false, 0xCCFFFF, 0xA3CCCC) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.DIAMOND), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(CERTUS.species, COAL.species, 3);
            tMutation.requireResource("blockDiamond");
        }
    },
    OLIVINE(GT_BranchDefinition.GEM, "Olivine", true, 0x248F24, 0xCCFFCC) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OLIVINE), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MAGNESIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(CERTUS.species, getSpecies(FORRESTRY,"Ended"), 5);
        }
    },
    EMERALD(GT_BranchDefinition.GEM, "Emerald", false, 0x248F24, 0x2EB82E) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.EMERALD), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ALUMINIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(OLIVINE.species, DIAMOND.species, 4);
            tMutation.requireResource("blockEmerald");
        }
    },
    REDGARNET(GT_BranchDefinition.GEM, "RedGarnet", false, 0xBD4C4C, 0xECCECE) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
	/*
    FIRESTONE(GT_BranchDefinition.GEM, "Firestone", false, 0xC00000, 0xFF0000) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STONE), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.FIRESTONE), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.WARM);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.NOCTURNAL, true);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(REDSTONE.species, RUBY.species, 4);
            tMutation.requireResource("blockFirestone");
        }
    },
	*/
    //Metal Line
    COPPER(GT_BranchDefinition.METAL, "Copper", true, 0xFF6600, 0xE65C00) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.COPPER), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GOLD), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Majestic"), CLAY.species, 13);
            tMutation.requireResource("blockCopper");
        }
    },
    TIN(GT_BranchDefinition.METAL, "Tin", true, 0xD4D4D4, 0xDDDDDD) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TIN), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ZINC), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(CLAY.species, getSpecies(FORRESTRY,"Diligent"), 13);
            tMutation.requireResource("blockTin");
        }
    },
    LEAD(GT_BranchDefinition.METAL, "Lead", true, 0x666699, 0xA3A3CC) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LEAD), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.WARM);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(COAL.species, COPPER.species, 13);
            tMutation.requireResource("blockLead");
        }
    },
    IRON(GT_BranchDefinition.METAL, "Iron", true, 0xDA9147, 0xDE9C59) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.IRON), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.TIN), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(TIN.species, COPPER.species, 13);
            tMutation.requireResource("blockIron");
        }
    },
    STEEL(GT_BranchDefinition.METAL, "Steel", true, 0x808080, 0x999999) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.STEEL), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRON), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.WARM);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(IRON.species, COAL.species, 10);
            tMutation.requireResource("blockSteel");
            tMutation.restrictTemperature(EnumTemperature.HOT);

        }
    },
    NICKEL(GT_BranchDefinition.METAL, "Nickel", true, 0x8585AD, 0x8585AD) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NICKEL), 0.15f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.02f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }
        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(IRON.species, COPPER.species, 13);
            tMutation.requireResource("blockNickel");
        }
    },
    ZINC(GT_BranchDefinition.METAL, "Zinc", true, 0xF0DEF0, 0xF2E1F2) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ZINC), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.GALLIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(IRON.species, TIN.species, 13);
            tMutation.requireResource("blockZinc");
        }
    },
    SILVER(GT_BranchDefinition.METAL, "Silver", true, 0xC2C2D6, 0xCECEDE) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SILVER), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SULFUR), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(LEAD.species, TIN.species, 10);
            tMutation.requireResource("blockSilver");
        }
    },
    GOLD(GT_BranchDefinition.METAL, "Gold", true, 0xEBC633, 0xEDCC47) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.GOLD), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NICKEL), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.WARM);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(LEAD.species, COPPER.species, 13);
            tMutation.requireResource("blockGold");
            tMutation.restrictTemperature(EnumTemperature.HOT);
        }
    },
    ARSENIC(GT_BranchDefinition.METAL, "Arsenic", true, 0x736C52, 0x292412) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
    //Rare Metals
    ALUMINIUM(GT_BranchDefinition.RAREMETAL, "Aluminium", true, 0xB8B8FF, 0xD6D6FF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.ALUMINIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.BAUXITE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(NICKEL.species, ZINC.species, 9);
            tMutation.requireResource("blockAluminium");
        }
    },
    TITANIUM(GT_BranchDefinition.RAREMETAL, "Titanium", true, 0xCC99FF, 0xDBB8FF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TITANIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ALMANDINE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(REDSTONE.species, ALUMINIUM.species, 5);
            tMutation.requireResource(GregTech_API.sBlockMetal7, 9);
        }
    },
    CHROME(GT_BranchDefinition.RAREMETAL, "Chrome", true, 0xEBA1EB, 0xF2C3F2) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CHROME), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MAGNESIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(TITANIUM.species, RUBY.species, 5);
        	tMutation.requireResource(GregTech_API.sBlockMetal2, 3);
        }
    },
    MANGANESE(GT_BranchDefinition.RAREMETAL, "Manganese", true, 0xD5D5D5, 0xAAAAAA) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MANGANESE), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRON), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(TITANIUM.species, ALUMINIUM.species, 5);
        	tMutation.requireResource(GregTech_API.sBlockMetal4, 6);
        }
    },
    TUNGSTEN(GT_BranchDefinition.RAREMETAL, "Tungsten", false, 0x5C5C8A, 0x7D7DA1) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TUNGSTEN), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MOLYBDENUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Heroic"), MANGANESE.species, 5);
        	tMutation.requireResource(GregTech_API.sBlockMetal7, 11);
        }
    },
    PLATINUM(GT_BranchDefinition.RAREMETAL, "Platinum", false, 0xE6E6E6, 0xFFFFCC) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLATINUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.02f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(DIAMOND.species, CHROME.species, 5);
        	tMutation.requireResource(GregTech_API.sBlockMetal5, 12);
        }
    },
    IRIDIUM(GT_BranchDefinition.RAREMETAL, "Iridium", false, 0xDADADA, 0xD1D1E0) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.IRIDIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.OSMIUM), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(TUNGSTEN.species, PLATINUM.species, 5);
            tMutation.requireResource(GregTech_API.sBlockMetal3, 12);
        }
    },
    OSMIUM(GT_BranchDefinition.RAREMETAL, "Osmium", false, 0x2B2BDA, 0x8B8B8B) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            tMutation.requireResource(GregTech_API.sBlockMetal5, 9);
        }
    },
    LITHIUM(GT_BranchDefinition.RAREMETAL, "Lithium", false, 0xF0328C, 0xE1DCFF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            tMutation.requireResource("blockLithium");
        }
    },
    SALTY(GT_BranchDefinition.RAREMETAL, "Salt", true, 0xF0C8C8, 0xFAFAFA) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LITHIUM), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SALT), 0.05f);
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
            tMutation.requireResource("blockSalt");
        }
    },
    /*ELECTROTINE(GT_BranchDefinition.RAREMETAL, "Electrotine", false, 0x1E90FF, 0x3CB4C8) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.ELECTROTINE), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.REDSTONE), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWER);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(REDSTONE.species, GOLD.species, 5);
            tMutation.requireResource("blockElectrotine");
        }
    },
    */
    //radiactive
    URANIUM(GT_BranchDefinition.RADIOACTIVE, "Uranium", true, 0x19AF19, 0x169E16) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.URANIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Avenging"), PLATINUM.species, 2);
        	tMutation.requireResource(GregTech_API.sBlockMetal7, 14);
        }
    },
    PLUTONIUM(GT_BranchDefinition.RADIOACTIVE, "Plutonium", true, 0x570000, 0x240000) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.LEAD), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.PLUTONIUM), 0.15f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.SLOWEST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.LONGEST);
        }

        @Override
        protected void registerMutations() {
        	IBeeMutationCustom tMutation = registerMutation(URANIUM.species, EMERALD.species, 2);
        	tMutation.requireResource(GregTech_API.sBlockMetal5, 13);
        }
    },

    NAQUADAH(GT_BranchDefinition.RADIOACTIVE, "Naquadah", false, 0x003300, 0x002400) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SLAG), 0.30f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAQUADAH), 0.15f);
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
        	IBeeMutationCustom tMutation = registerMutation(PLUTONIUM.species, IRIDIUM.species, 1);
        	tMutation.requireResource(GregTech_API.sBlockMetal4, 12);
        }
    },

    NAQUADRIA(GT_BranchDefinition.RADIOACTIVE, "Naquadria", false, 0x000000, 0x002400) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(PLUTONIUM.species, IRIDIUM.species, 8,10);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 15);
        }
    },
    /*DOB(GT_BranchDefinition.RADIOACTIVE, "DOB", false, 0x003300, 0x002400) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.DOB), 0.75f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
           beeSpecies.setIsSecret();

       }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(NAQUADAH.species, THAUMIUMSHARD.species, 1);
            tMutation.requireResource(GameRegistry.findBlock("AdvancedSolarPanel", "BlockAdvSolarPanel"), 2);
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(9, "END Biome"));//sky end biome
        }
    },
    */
    THORIUM(GT_BranchDefinition.RADIOACTIVE, "Thorium", false, 0x005000, 0x001E00) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(THORIUM.species, getSpecies(FORRESTRY,"Industrious"), 1);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 3);
        }
    },
    AMERICUM(GT_BranchDefinition.RADIOACTIVE, "Americum", false, 0xE6E6FF, 0xC8C8C8) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(LUTETIUM.species, CHROME.species, 3, 4);
            tMutation.requireResource(GregTech_API.sBlockMetal1, 2);
        }
    },
    NEUTRONIUM(GT_BranchDefinition.RADIOACTIVE, "Neutronium", false, 0xFFF0F0, 0xFAFAFA) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(NAQUADRIA.species, AMERICUM.species, 1,2);
            tMutation.requireResource(GregTech_API.sBlockMetal5, 2);
        }
    },
    /*//Twilight
    NAGA(GT_BranchDefinition.TWILIGHT, "Naga", true, 0x0D5A0D, 0x28874B) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.02f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.NAGA), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies(MAGICBEES,"Eldritch"), getSpecies(FORRESTRY,"Imperial"), 8);
            tMutation.restrictHumidity(EnumHumidity.DAMP);
        }
    },
    LICH(GT_BranchDefinition.TWILIGHT, "Lich", true, 0xC5C5C5, 0x5C605E) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.04f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LICH), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(getSpecies(MAGICBEES,"Supernatural"), NAGA.species, 7);
            tMutation.restrictHumidity(EnumHumidity.ARID);
        }
    },
    HYDRA(GT_BranchDefinition.TWILIGHT, "Hydra", true, 0x872836, 0xB8132C) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.06f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.HYDRA), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(LICH.species, getSpecies(MAGICBEES,"TCFire"), 6);
            tMutation.addMutationCondition(new GT_Bees.BiomeIDMutationCondition(138, "Undergarden"));//undergarden biome
        }
    },
    URGHAST(GT_BranchDefinition.TWILIGHT, "UrGhast", true, 0xA7041C, 0x7C0618) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.08f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.URGHAST), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
            beeSpecies.setHasEffect();
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, EnumAllele.Speed.FAST);
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(HYDRA.species, THAUMIUMDUST.species, 5);
            tMutation.requireResource(GameRegistry.findBlock("Thaumcraft", "blockCosmeticSolid"), 4);
            tMutation.restrictTemperature(EnumTemperature.HELLISH);
        }
    },
    SNOWQUEEN(GT_BranchDefinition.TWILIGHT, "SnowQueen", true, 0xD02001, 0x9C0018) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SALISMUNDUS), 0.15f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.SNOWQUEEN), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setHasEffect();
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(URGHAST.species, SALISMUNDUS.species, 4);
            tMutation.requireResource(GameRegistry.findBlock("thaumicbases", "blockSalisMundus"), 0);
            tMutation.restrictTemperature(EnumTemperature.ICY);
        }
    },
    */
    
    SPACE(GT_BranchDefinition.SPACE, "Space", true, 0x003366, 0xC0C0C0) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(getSpecies(FORRESTRY,"Industrious"), getSpecies(FORRESTRY,"Heroic"), 10);
            tMutation.restrictTemperature(EnumTemperature.ICY);
        }
    },
    METEORICIRON(GT_BranchDefinition.SPACE, "MeteoricIron", true, 0x321928, 0x643250) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
    };
    /*DESH(GT_BranchDefinition.SPACE, "Desh", false, 0x323232, 0x282828) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.10f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.LEDOX), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, getEffect(EXTRABEES, "freezing"));
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(CALLISTO.species, LEAD.species, 7);
            tMutation.requireResource(GameRegistry.findBlock("dreamcraft", "tile.Ledox"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(35, "Europa"));//Europa Dim
        }
    },
    CALLISTOICE(GT_BranchDefinition.SPACE, "CallistoIce", false, 0x0074FF, 0x1EB1FF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.10f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.CALLISTOICE), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.EFFECT, getEffect(EXTRABEES, "freezing"));
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(CALLISTO.species, getSpecies(EXTRABEES,"freezing"), 7);
            tMutation.requireResource(GameRegistry.findBlock("dreamcraft", "tile.CallistoColdIce"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(45, "Callisto"));//Callisto Dim
        }
    },
    MYTRYL(GT_BranchDefinition.SPACE, "Mytryl", false, 0xDAA520, 0xF26404) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SPACE), 0.16f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.MYTRYL), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(IO.species, MITHRIL.species, 6);
            tMutation.requireResource(GameRegistry.findBlock("dreamcraft", "tile.Mytryl"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(36, "IO"));//IO Dim
        }
    },
    QUANTIUM(GT_BranchDefinition.SPACE, "Quantium", false, 0x00FF00, 0x00D10B) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(VENUS.species, OSMIUM.species, 6);
            tMutation.requireResource(GameRegistry.findBlock("dreamcraft", "tile.Quantinum"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(39, "Venus"));//Venus Dim
        }
    },
    ORIHARUKON(GT_BranchDefinition.SPACE, "Oriharukon", false, 0x228B22, 0x677D68) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(LEAD.species, OBERON.species, 5);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "metalsblock"), 6);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(46, "Oberon"));//Oberon Dim
        }
    },
    MYSTERIOUSCRYSTAL(GT_BranchDefinition.SPACE, "MysteriousCrystal", false, 0x3CB371, 0x16856C) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(ENCELADUS.species, EMERALD.species, 3);
            tMutation.requireResource(GameRegistry.findBlock("dreamcraft", "tile.MysteriousCrystal"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(41, "Enceladus"));//Enceladus Dim
        }
    },
    BLACKPLUTONIUM(GT_BranchDefinition.SPACE, "BlackPlutonium", false, 0x000000, 0x323232) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
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
            IBeeMutationCustom tMutation = registerMutation(PLUTO.species, PLUTONIUM.species, 2);
            tMutation.requireResource(GameRegistry.findBlock("dreamcraft", "tile.BlackPlutonium"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(49, "Pluto"));//Pluto Dim
        }
    },
    TRINIUM(GT_BranchDefinition.SPACE, "Trinium", false, 0xB0E0E6, 0xC8C8D2) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TRINIUM), 0.75f);
            beeSpecies.addSpecialty(GT_Bees.combs.getStackForType(CombType.QUANTIUM), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.SPEED, GT_Bees.speedBlinding);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(ENCELADUS.species, IRIDIUM.species, 4);
            tMutation.requireResource(GregTech_API.sBlockMetal4, 9);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(41, "Enceladus"));//Enceladus Dim
        }
    },
    //Planet Line
    MERCURY(GT_BranchDefinition.PLANET, "Mercury", false, 0x4A4033, 0xB5A288) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MERCURY), 0.50f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(JUPITER.species, TUNGSTEN.species, 25, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "mercuryblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(37, "Mercury"));//Mercury Dim
        }
    },
    VENUS(GT_BranchDefinition.PLANET, "Venus", false, 0x4A4033, 0xB5A288) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.VENUS), 0.50f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(JUPITER.species, MITHRIL.species, 25, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "venusblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(39, "Venus"));//Venus Dim
        }
    },
    MOON(GT_BranchDefinition.PLANET, "Moon", false, 0x373735, 0x7E7E78) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MOON), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.MoonStoneDust", 1, 0), 0.10f);
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
            tMutation.requireResource(GameRegistry.findBlock("GalacticraftCore", "tile.moonBlock"), 4);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(28, "Moon"));//Moon Dim
        }
    },
    MARS(GT_BranchDefinition.PLANET, "Mars", false, 0x220D05, 0x3A1505) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MARS), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.MarsStoneDust", 1, 0), 0.10f);
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
            tMutation.requireResource(GameRegistry.findBlock("GalacticraftMars", "tile.mars"), 5);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(29, "Mars"));//Mars Dim
        }
    },
    PHOBOS(GT_BranchDefinition.PLANET, "Phobos", true, 0x220D05, 0x7a5706) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MARS), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.PhobosStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MARS.species, MOON.species, 20);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "phobosblocks"), 2);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(38, "Phobos"));//Phobos Dim
        }
    },
    DEIMOS(GT_BranchDefinition.PLANET, "Deimos", true, 0x220D05, 0x7a3206) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MARS), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.DeimosStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MARS.species, SPACE.species, 20);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "deimosblocks"), 1);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(40, "Deimos"));//Deimos Dim
        }
    },
    CERES(GT_BranchDefinition.PLANET, "Ceres", true, 0x3ca5b7, 0x1e7267) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.CeresStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MARS.species, METEORICIRON.species, 20);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "ceresblocks"), 1);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(42, "Ceres"));//Ceres Dim
        }
    },
    JUPITER(GT_BranchDefinition.PLANET, "Jupiter", false, 0x734B2E, 0xD0CBC4) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.CallistoStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.CallistoIceDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.IoStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.EuropaStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.EuropaIceDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.GanymedStoneDust", 1, 0), 0.05f);
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
            IBeeMutationCustom tMutation = registerMutation(MARS.species, DESH.species, 15);
            tMutation.requireResource(GameRegistry.findBlock("dreamcraft", "tile.Ledox"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteriods"));//Asteriods Dim
        }
    },
    IO(GT_BranchDefinition.PLANET, "IO", true, 0x734B2E, 0xe5701b) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.IoStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(JUPITER.species, getSpecies(EXTRABEES, "volcanic"), 15);
            tMutation.restrictTemperature(EnumTemperature.HELLISH);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "ioblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(36, "IO"));//IO Dim
        }
    },
    EUROPA(GT_BranchDefinition.PLANET, "Europa", true, 0x5982ea, 0x0b36a3) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.EuropaStoneDust", 1, 0), 0.10f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.EuropaIceDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(JUPITER.species, IRON.species, 15);
            tMutation.restrictTemperature(EnumTemperature.ICY);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "europagrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(35, "Europa"));//Europa Dim
        }
    },
    GANYMEDE(GT_BranchDefinition.PLANET, "Ganymede", true, 0x3d1b10, 0x190c07) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.GanymedStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.COLD);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(JUPITER.species, TITANIUM.species, 15);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "ganymedeblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(43, "Ganymede"));//Ganymede Dim
        }
    },
    CALLISTO(GT_BranchDefinition.PLANET, "Callisto", true, 0x0f333d, 0x0d84a5) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.JUPITER), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.CallistoStoneDust", 1, 0), 0.10f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.CallistoIceDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(JUPITER.species, getSpecies(EXTRABEES, "artic"), 15);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "callistoblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(45, "Callisto"));//Callisto Dim
        }
    },
    SATURN(GT_BranchDefinition.PLANET, "Saturn", false, 0xD2A472, 0xF8C37B) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SATURN), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.TitanStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.EnceladusStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.EnceladusIceDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(JUPITER.species, LEDOX.species, 25, 2);
            tMutation.requireResource(GameRegistry.findBlock("dreamcraft", "tile.Quantinum"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteriods"));//Asteriods Dim
        }
    },
    ENCELADUS(GT_BranchDefinition.PLANET, "Enceladus", true, 0xD2A472, 0x193fa0) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SATURN), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.EnceladusStoneDust", 1, 0), 0.10f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.EnceladusIceDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(SATURN.species, CHROME.species, 25, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "enceladusblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(41, "Enceladus"));//Enceladus Dim
        }
    },
    TITAN(GT_BranchDefinition.PLANET, "Titan", true, 0xa0641b, 0x7c1024) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.SATURN), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.TitanStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(SATURN.species, NICKEL.species, 25, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "titanblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(44, "Titan"));//Titan Dim
        }
    },
    URANUS(GT_BranchDefinition.PLANET, "Uranus", false, 0x75C0C9, 0x84D8EC) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.URANUS), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.MirandaStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.OberonStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(SATURN.species, TRINIUM.species, 10);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "metalsblock"), 6);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteroids"));//Asteroids Dim
        }
    },
    MIRANDA(GT_BranchDefinition.PLANET, "Miranda", true, 0x75C0C9, 0x0d211c) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.URANUS), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.MirandaStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(URANUS.species, TIN.species, 10);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "mirandablocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(86, "Miranda"));//Miranda Dim
        }
    },
    OBERON(GT_BranchDefinition.PLANET, "Oberon", true, 0x4A4033, 0xB5A288) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.URANUS), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.OberonStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.ICY);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(URANUS.species, IRIDIUM.species, 10);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "oberonblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(46, "Oberon"));//Oberon Dim
        }
    },
    NEPTUN(GT_BranchDefinition.PLANET, "Neptun", false, 0x334CFF, 0x576DFF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEPTUN), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.ProteusStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.TritonStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
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
            IBeeMutationCustom tMutation = registerMutation(URANUS.species, ORIHARUKON.species, 7);
            tMutation.requireResource(GameRegistry.findBlock("dreamcraft", "tile.MysteriousCrystal"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(30, "Asteroids"));//Asteroids Dim
        }
    },
    PROTEUS(GT_BranchDefinition.PLANET, "Proteus", true, 0x334CFF, 0x592610) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEPTUN), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.ProteusStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(NEPTUN.species, COPPER.species, 7);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "proteusblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(47, "Proteus"));//Proteus Dim
        }
    },
    TRITON(GT_BranchDefinition.PLANET, "Triton", true, 0x334CFF, 0x421118) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.NEPTUN), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.TritonStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(NEPTUN.species, GOLD.species, 7);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "tritonblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(48, "Triton"));//Triton Dim
        }
    },
    PLUTO(GT_BranchDefinition.PLANET, "Pluto", false, 0x34271E, 0x69503D) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.PLUTO), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.PlutoStoneDust", 1, 0), 0.10f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.PlutoIceDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(NEPTUN.species, PLUTONIUM.species, 5);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "plutoblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(49, "Pluto"));//Pluto Dim
        }
    },
    HAUMEA(GT_BranchDefinition.PLANET, "Haumea", false, 0x1C1413, 0x392B28) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.HAUMEA), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.HaumeaStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.ICY);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(PLUTO.species, NAQUADAH.species, 7, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "haumeablocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(83, "Haumea"));//Haumea Dim
        }
    },
    MAKEMAKE(GT_BranchDefinition.PLANET, "MakeMake", false, 0x301811, 0x120A07) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.MAKEMAKE), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.MakeMakeStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(PLUTO.species, NAQUADRIA.species, 7, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "makemakegrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(25, "MakeMake"));//MakeMake Dim
        }
    },
    CENTAURI(GT_BranchDefinition.PLANET, "Centauri", false, 0x2F2A14, 0xB06B32) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CENTAURI), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.CentauriASurfaceDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MAKEMAKE.species, DESH.species, 3);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "acentauribbgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(33, "Kuiper Belt"));//Kuiper Belt Dim
        }
    },
    ACENTAURI(GT_BranchDefinition.PLANET, "aCentauri", false, 0x2F2A14, 0xa01e14) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.CENTAURI), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.CentauriASurfaceDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(CENTAURI.species, INFINITYCATALYST.species, 3);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "acentauribbgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(31, "aCentauri"));//aCentauri Dim
        }
    },
    TCETI(GT_BranchDefinition.PLANET, "tCeti", false, 0x46241A, 0x7B412F) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TCETI), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.TCetiEStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MAKEMAKE.species, HAUMEA.species, 5, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "tcetieblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(33, "Kuiper Belt"));//Kuiper Belt Dim
        }
    },
    TCETIE(GT_BranchDefinition.PLANET, "tCetiE", false, 0x2d561b, 0x0c0f60) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.TCETI), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.TCetiEStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.NORMAL);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(TCETI.species, getSpecies(MAGICBEES, "TCWater"), 5, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "tcetieblocks"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(85, "tCeti E"));//tCeti E Dim
        }
    },
    BARNARDA(GT_BranchDefinition.PLANET, "Barnarda", false, 0x0D5A0D, 0xE6C18D) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BARNARDA), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.BarnardaEStoneDust", 1, 0), 0.05f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.BarnardaFStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MAKEMAKE.species, THORIUM.species, 3, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "barnardaEgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(33, "Kuiper Belt"));//Kuiper Belt Dim
        }
    },
    BARNARDAC(GT_BranchDefinition.PLANET, "BarnardaC", false, 0x0D5A0D, 0x473f0a) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BARNARDA), 0.25f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(BARNARDA.species, AMERICUM.species, 3, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "barnardaEgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(32, "Barnarda C"));//Barnarda C Dim
        }
    },
    BARNARDAE(GT_BranchDefinition.PLANET, "BarnardaE", false, 0x0D5A0D, 0x4c1f0a) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BARNARDA), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.BarnardaEStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(BARNARDA.species, DIVIDED.species, 3, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "barnardaEgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(81, "Barnard E"));//"Barnard E Dim
        }
    },
    BARNARDAF(GT_BranchDefinition.PLANET, "BarnardaF", false, 0x0D5A0D, 0x1e0b49) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.BARNARDA), 0.25f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.BarnardaFStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.HOT);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(BARNARDA.species, NEUTRONIUM.species, 3, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "barnardaFgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(82, "Barnard F"));//"Barnard F Dim
        }
    },
    VEGA(GT_BranchDefinition.PLANET, "Vega", false, 0x1A2036, 0xB5C0DE) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.VEGA), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.VegaBStoneDust", 1, 0), 0.05f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.COLD);
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(MAKEMAKE.species, NAQUADAH.species, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "vegabgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(33, "Kuiper Belt"));//Kuiper Belt Dim
        }
    },
    VEGAB(GT_BranchDefinition.PLANET, "VegaB", false, 0x1A2036, 0x81e261) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.VEGA), 0.50f);
            beeSpecies.addSpecialty(GT_ModHandler.getModItem("dreamcraft", "item.VegaBStoneDust", 1, 0), 0.10f);
            beeSpecies.setHumidity(EnumHumidity.ARID);
            beeSpecies.setTemperature(EnumTemperature.COLD);
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(VEGA.species, NAQUADRIA.species, 2);
            tMutation.requireResource(GameRegistry.findBlock("GalaxySpace", "vegabgrunt"), 0);
            tMutation.addMutationCondition(new GT_Bees.DimensionMutationCondition(84, "VegaB"));//VegaB Dim
        }
    },
    //Infinity Line
    COSMICNEUTRONIUM(GT_BranchDefinition.PLANET, "CosmicNeutronium", false, 0x484848, 0x323232) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.COSMICNEUTRONIUM), 0.25f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(NEUTRONIUM.species, BARNARDAF.species, 7,10);
            tMutation.requireResource(GameRegistry.findBlock("Avaritia", "Resource_Block"), 0);
        }
    },
    INFINITYCATALYST(GT_BranchDefinition.PLANET, "InfinityCatalyst", false, 0xFFFFFF, 0xFFFFFF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFINITYCATALYST), 0.0000005f);
            beeSpecies.setHumidity(EnumHumidity.DAMP);
            beeSpecies.setTemperature(EnumTemperature.HELLISH);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(DOB.species, COSMICNEUTRONIUM.species, 3, 10);
            tMutation.requireResource(GameRegistry.findBlock("Avaritia", "Resource_Block"), 1);
        }
    },
    INFINITY(GT_BranchDefinition.PLANET, "Infinity", false, 0xFFFFFF, 0xFFFFFF) {
        @Override
        protected void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
            beeSpecies.addProduct(GT_Bees.combs.getStackForType(CombType.INFINITY), 0.00000005f);
            beeSpecies.setHumidity(EnumHumidity.NORMAL);
            beeSpecies.setTemperature(EnumTemperature.ICY);
            beeSpecies.setNocturnal();
            beeSpecies.setHasEffect();
        }

        @Override
        protected void setAlleles(IAllele[] template) {
            AlleleHelper.instance.set(template, EnumBeeChromosome.LIFESPAN, EnumAllele.Lifespan.SHORTEST);
        }

        @Override
        protected void registerMutations() {
            IBeeMutationCustom tMutation = registerMutation(INFINITYCATALYST.species, COSMICNEUTRONIUM.species, 1, 100);
            tMutation.requireResource(GameRegistry.findBlock("avaritiaddons", "InfinityChest"), 0);
        }
        
    };
    */
    private final GT_BranchDefinition branch;
    private final GT_AlleleBeeSpecies species;
    protected final static byte FORRESTRY = 0;
    protected final static byte EXTRABEES = 1;
    protected final static byte GENDUSTRY = 2;
    protected final static byte MAGICBEES = 3;
    protected final static byte GREGTECH = 4;


    private IAllele[] template;
    private IBeeGenome genome;

    GT_BeeDefinition(GT_BranchDefinition branch, String binomial, boolean dominant, int primary, int secondary) {
        String lowercaseName = this.toString().toLowerCase(Locale.ENGLISH);
        String species = "species" + WordUtils.capitalize(lowercaseName);

        String uid = "gregtech." + species;//TODO String uid = "gendustry.bee." + species;
        String description = "for.description." + species;
        String name = "for.bees.species." + lowercaseName;


        this.branch = branch;
        this.species = new GT_AlleleBeeSpecies(uid, dominant, name, "GTNH", description, branch.getBranch(), binomial, primary, secondary);
    }

    public static void initBees() {
        for (GT_BeeDefinition bee : values()) {
            bee.init();
        }
        for (GT_BeeDefinition bee : values()) {
            bee.registerMutations();
        }
    }

    protected static IAlleleBeeEffect getEffect(byte modid, String name) {
        String s;
        switch(modid) {
            case EXTRABEES: s = new StringBuilder().append("extrabees.effect.").append(name).toString();break;
            case GENDUSTRY: s = new StringBuilder().append("gendustry.effect.").append(name).toString();break;
            case MAGICBEES: s = new StringBuilder().append("magicbees.effect").append(name).toString();break;
            case GREGTECH: s = new StringBuilder().append("gregtech.effect").append(name).toString();break;
            default: s = new StringBuilder().append("forestry.effect").append(name).toString();break;

        }
        return (IAlleleBeeEffect) AlleleManager.alleleRegistry.getAllele(s);
    }

    protected static IAlleleFlowers getFlowers(byte modid, String name) {
        String s;
        switch(modid) {
            case EXTRABEES: s = new StringBuilder().append("extrabees.flower.").append(name).toString();break;
            case GENDUSTRY: s = new StringBuilder().append("gendustry.flower.").append(name).toString();break;
            case MAGICBEES: s = new StringBuilder().append("magicbees.flower").append(name).toString();break;
            case GREGTECH: s = new StringBuilder().append("gregtech.flower").append(name).toString();break;
            default: s = new StringBuilder().append("forestry.flowers").append(name).toString();break;

        }
        return (IAlleleFlowers) AlleleManager.alleleRegistry.getAllele(s);
    }

    protected static IAlleleBeeSpecies getSpecies(byte modid, String name) {
        String s;
        switch(modid) {
            case EXTRABEES: s = new StringBuilder().append("extrabees.species.").append(name).toString();break;
            case GENDUSTRY: s = new StringBuilder().append("gendustry.bee.").append(name).toString();break;
            case MAGICBEES: s = new StringBuilder().append("magicbees.species").append(name).toString();break;
            case GREGTECH: s = new StringBuilder().append("gregtech.species").append(name).toString();break;
            default: s = new StringBuilder().append("forestry.species").append(name).toString();break;

        }
        IAlleleBeeSpecies ret = (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(s);
        if (ret == null){
            ret = NAQUADRIA.species;
        }

        return ret;
    }


    protected abstract void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies);

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
        return new GT_Bee_Mutation(parent1,parent2,this.getTemplate(),chance,1);
    }

    /**
     * Diese neue Funtion erlaubt Mutationsraten unter 1%. Setze dazu die Mutationsrate als Bruch mit chance / chancedivider
     * This new function allows Mutation percentages under 1%. Set them as a fraction with chance / chancedivider
     * @param parent1
     * @param parent2
     * @param chance
     * @param chancedivider
     * @return
     */
    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance, int chancedivider) {
        return new GT_Bee_Mutation(parent1,parent2,this.getTemplate(),chance,chancedivider);
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
