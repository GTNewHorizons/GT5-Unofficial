package gtPlusPlus.xmod.forestry.bees.registry;

import static forestry.api.apiculture.EnumBeeChromosome.*;
import static forestry.api.core.EnumHumidity.ARID;
import static forestry.api.core.EnumHumidity.DAMP;
import static forestry.api.core.EnumTemperature.*;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Locale;
import java.util.function.Consumer;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.text.WordUtils;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import forestry.api.apiculture.*;
import forestry.api.core.EnumHumidity;
import forestry.api.core.EnumTemperature;
import forestry.api.genetics.*;
import forestry.apiculture.genetics.*;
import forestry.apiculture.genetics.alleles.AlleleEffect;
import forestry.core.genetics.alleles.AlleleHelper;
import forestry.core.genetics.alleles.EnumAllele.*;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.bees.GT_AlleleBeeSpecies;
import gregtech.common.bees.GT_Bee_Mutation;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GT_BeeDefinition;
import gregtech.loaders.misc.GT_BranchDefinition;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.ItemStack;



public enum GTPP_BeeDefinition implements IBeeDefinition {
	
    DIVIDED(GT_BranchDefinition.THAUMIC, "Unstable", true, new Color(0xF0F0F0), new Color(0xDCDCDC),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem("ExtraBees", "honeyComb", 1, 61), 0.20f);
                beeSpecies.addSpecialty(GTPP_Bees.combs.getStackForType(CombType.DIVIDED), 0.125f);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWER);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORT);
                AlleleHelper.instance.set(template, FLOWERING, Flowering.SLOW);
                AlleleHelper.instance.set(template, NOCTURNAL, true);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(DIAMOND, IRON, 3);
                if (Loader.isModLoaded("ExtraUtilities"))
                    tMutation.requireResource(GameRegistry.findBlock("ExtraUtilities", "decorativeBlock1"), 5);
            }
    ),
    NEUTRONIUM(GT_BranchDefinition.RADIOACTIVE, "Neutronium", false, new Color(0xFFF0F0), new Color(0xFAFAFA),
            beeSpecies -> {
                beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CombType.NEUTRONIUM), 0.0001f);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(HELLISH);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, SPEED, Speed.SLOWEST);
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGEST);
                AlleleHelper.instance.set(template, NOCTURNAL, true);
            },
            dis -> {
                IMutationCustom tMutation = dis.registerMutation(NAQUADRIA, AMERICIUM, 1, 2).setIsSecret();
                tMutation.requireResource(GregTech_API.sBlockMetal5, 2);
            }
    ),
    DRAGONESSENCE(GT_BranchDefinition.HEE, "Dragonessence", true, new Color(0xFFA12B), new Color(0x911ECE),
            beeSpecies -> {
                beeSpecies.addProduct(GT_ModHandler.getModItem(GT_Values.MOD_ID_FR, "beeCombs", 1, 8), 0.30f);
                beeSpecies.addSpecialty(GTPP_Bees.combs.getStackForType(CombType.DRAGONESSENCE), 0.10f);
                beeSpecies.setHumidity(ARID);
                beeSpecies.setTemperature(EnumTemperature.NORMAL);
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.LONGER);
                AlleleHelper.instance.set(template, EFFECT, AlleleEffect.effectBeatific);
                AlleleHelper.instance.set(template, TEMPERATURE_TOLERANCE, Tolerance.BOTH_3);
                AlleleHelper.instance.set(template, HUMIDITY_TOLERANCE, Tolerance.BOTH_3);
            },
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(GT_BeeDefinition.ECTOPLASMA, GT_BeeDefinition.ARCANESHARDS, 4);
                tMutation.restrictHumidity(ARID);
                if (Loader.isModLoaded("HardcoreEnderExpansion"))
                    tMutation.requireResource(GameRegistry.findBlock("HardcoreEnderExpansion", "essence_altar"), 1);
                tMutation.addMutationCondition(new GTPP_Bees.DimensionMutationCondition(1, "End"));//End Dim
            }
    ),
    TRINIUM(GT_BranchDefinition.SPACE, "Trinium", false, new Color(0xB0E0E6), new Color(0xC8C8D2),
            beeSpecies -> {
                beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CombType.TRINIUM), 0.75f);
                beeSpecies.addSpecialty(GTPP_Bees.combs.getStackForType(CombType.QUANTIUM), 0.10f);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(COLD);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> AlleleHelper.instance.set(template, SPEED, GTPP_Bees.speedBlinding),
            new Consumer<GTPP_BeeDefinition>() {
                @Override
                public void accept(GTPP_BeeDefinition dis) {
                    IBeeMutationCustom tMutation = dis.registerMutation(ENCELADUS, IRIDIUM, 4);
                    tMutation.requireResource(GregTech_API.sBlockMetal4, 9);
                    tMutation.addMutationCondition(new GTPP_Bees.DimensionMutationCondition(41, "Enceladus"));//Enceladus Dim
                }
            }
    ),

    //Infinity Line
    COSMICNEUTRONIUM(GT_BranchDefinition.PLANET, "CosmicNeutronium", false, new Color(0x484848), new Color(0x323232),
            beeSpecies -> {
                beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CombType.COSMICNEUTRONIUM), 0.25f);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(ICY);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(NEUTRONIUM, BARNARDAF, 7, 10);
                if (Loader.isModLoaded("Avaritia"))
                    tMutation.requireResource(GameRegistry.findBlock("Avaritia", "Resource_Block"), 0);
            }
    ),
    INFINITYCATALYST(GT_BranchDefinition.PLANET, "InfinityCatalyst", false, new Color(0xFFFFFF), new Color(0xFFFFFF),
            beeSpecies -> {
                beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CombType.INFINITYCATALYST), 0.0000005f);
                beeSpecies.setHumidity(DAMP);
                beeSpecies.setTemperature(HELLISH);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> {
                AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST);
                AlleleHelper.instance.set(template, EFFECT, getEffect(EXTRABEES, "blindness"));
            },
            dis -> {
                IMutationCustom tMutation = dis.registerMutation(DOB, COSMICNEUTRONIUM, 3, 10).setIsSecret();
                if (Loader.isModLoaded("Avaritia"))
                    tMutation.requireResource(GameRegistry.findBlock("Avaritia", "Resource_Block"), 1);
            }
    ),
    INFINITY(GT_BranchDefinition.PLANET, "Infinity", false, new Color(0xFFFFFF), new Color(0xFFFFFF),
            beeSpecies -> {
                beeSpecies.addProduct(GTPP_Bees.combs.getStackForType(CombType.INFINITY), 0.00000005f);
                beeSpecies.setHumidity(EnumHumidity.NORMAL);
                beeSpecies.setTemperature(ICY);
                beeSpecies.setNocturnal();
                beeSpecies.setHasEffect();
            },
            template -> AlleleHelper.instance.set(template, LIFESPAN, Lifespan.SHORTEST),
            dis -> {
                IBeeMutationCustom tMutation = dis.registerMutation(INFINITYCATALYST, COSMICNEUTRONIUM, 1, 100);
                if (Loader.isModLoaded("avaritiaddons"))
                    tMutation.requireResource(GameRegistry.findBlock("avaritiaddons", "InfinityChest"), 0);
            }
    ),
    ;
    private final GT_BranchDefinition branch;
    private final GT_AlleleBeeSpecies species;
    private final Consumer<GT_AlleleBeeSpecies> mSpeciesProperties;
    private final Consumer<IAllele[]> mAlleles;
    private final Consumer<GTPP_BeeDefinition> mMutations;
    private IAllele[] template;
    private IBeeGenome genome;

    GTPP_BeeDefinition(GT_BranchDefinition branch,
                     String binomial,
                     boolean dominant,
                     Color primary,
                     Color secondary,
                     Consumer<GT_AlleleBeeSpecies> aSpeciesProperties,
                     Consumer<IAllele[]> aAlleles,
                     Consumer<GTPP_BeeDefinition> aMutations
    ) {
        this.mAlleles = aAlleles;
        this.mMutations = aMutations;
        this.mSpeciesProperties = aSpeciesProperties;
        String lowercaseName = this.toString().toLowerCase(Locale.ENGLISH);
        String species = WordUtils.capitalize(lowercaseName);

        String uid = "gtpp.bee.species" + species;
        String description = "for.description." + species;
        String name = "for.bees.species." + lowercaseName;
        GT_LanguageManager.addStringLocalization("for.bees.species." + lowercaseName, species, true);

        this.branch = branch;
        this.species = new GT_AlleleBeeSpecies(uid, dominant, name, "GT++", description, branch.getBranch(), binomial, primary, secondary);
    }

    public static void initBees() {
        for (GTPP_BeeDefinition bee : values()) {
            bee.init();
        }
        for (GTPP_BeeDefinition bee : values()) {
            bee.registerMutations();
        }
    }

    protected static IAlleleBeeEffect getEffect(byte modid, String name) {
        String s;
        switch (modid) {
            case GTPP_Bees.EXTRABEES:
                s = "extrabees.effect." + name;
                break;
            case GTPP_Bees.GENDUSTRY:
                s = "gendustry.effect." + name;
                break;
            case GTPP_Bees.MAGICBEES:
                s = "magicbees.effect" + name;
                break;
            case GTPP_Bees.GREGTECH:
                s = "gregtech.effect" + name;
                break;
            default:
                s = "forestry.effect" + name;
                break;

        }
        return (IAlleleBeeEffect) AlleleManager.alleleRegistry.getAllele(s);
    }

    protected static IAlleleFlowers getFlowers(byte modid, String name) {
        String s;
        switch (modid) {
            case GTPP_Bees.EXTRABEES:
                s = "extrabees.flower." + name;
                break;
            case GTPP_Bees.GENDUSTRY:
                s = "gendustry.flower." + name;
                break;
            case GTPP_Bees.MAGICBEES:
                s = "magicbees.flower" + name;
                break;
            case GTPP_Bees.GREGTECH:
                s = "gregtech.flower" + name;
                break;
            default:
                s = "forestry.flowers" + name;
                break;

        }
        return (IAlleleFlowers) AlleleManager.alleleRegistry.getAllele(s);
    }

    protected static IAlleleBeeSpecies getSpecies(byte modid, String name) {
        String s;
        switch (modid) {
            case GTPP_Bees.EXTRABEES:
                s = "extrabees.species." + name;
                break;
            case GTPP_Bees.GENDUSTRY:
                s = "gendustry.bee." + name;
                break;
            case GTPP_Bees.MAGICBEES:
                s = "magicbees.species" + name;
                break;
            case GTPP_Bees.GREGTECH:
                s = "gregtech.species" + name;
                break;
            default:
                s = "forestry.species" + name;
                break;

        }
        IAlleleBeeSpecies ret = (IAlleleBeeSpecies) AlleleManager.alleleRegistry.getAllele(s);
        return ret;
    }


    protected final void setSpeciesProperties(GT_AlleleBeeSpecies beeSpecies) {
        this.mSpeciesProperties.accept(beeSpecies);
    }

    protected final void setAlleles(IAllele[] template) {
        this.mAlleles.accept(template);
    }

    protected final void registerMutations() {
        this.mMutations.accept(this);
    }

    private void init() {
        setSpeciesProperties(species);

        template = branch.getTemplate();
        AlleleHelper.instance.set(template, SPECIES, species);
        setAlleles(template);

        genome = BeeManager.beeRoot.templateAsGenome(template);

        BeeManager.beeRoot.registerTemplate(template);
    }

    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    protected final IBeeMutationCustom registerMutation(GTPP_BeeDefinition parent1, IAlleleBeeSpecies parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GTPP_BeeDefinition parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }

    protected final IBeeMutationCustom registerMutation(GTPP_BeeDefinition parent1, GTPP_BeeDefinition parent2, int chance) {
        return registerMutation(parent1, parent2, chance, 1f);
    }
    
    protected final IBeeMutationCustom registerMutation(String parent1, String parent2, int chance) {
        return registerMutation(getGregtechBeeType(parent1), getGregtechBeeType(parent2), chance, 1f);
    }

    /**
     * Diese neue Funtion erlaubt Mutationsraten unter 1%. Setze dazu die Mutationsrate als Bruch mit chance / chancedivider
     * This new function allows Mutation percentages under 1%. Set them as a fraction with chance / chancedivider
     */
    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, IAlleleBeeSpecies parent2, int chance, float chancedivider) {
        return new GT_Bee_Mutation(parent1, parent2, this.getTemplate(), chance, chancedivider);
    }

    protected final IBeeMutationCustom registerMutation(GTPP_BeeDefinition parent1, IAlleleBeeSpecies parent2, int chance, float chancedivider) {
        return registerMutation(parent1.species, parent2, chance, chancedivider);
    }

    protected final IBeeMutationCustom registerMutation(IAlleleBeeSpecies parent1, GTPP_BeeDefinition parent2, int chance, float chancedivider) {
        return registerMutation(parent1, parent2.species, chance, chancedivider);
    }

    protected final IBeeMutationCustom registerMutation(GTPP_BeeDefinition parent1, GTPP_BeeDefinition parent2, int chance, float chancedivider) {
        return registerMutation(parent1.species, parent2, chance, chancedivider);
    }

    protected final IBeeMutationCustom registerMutation(String parent1, String parent2, int chance, float chancedivider) {
        return registerMutation(getGregtechBeeType(parent1), getGregtechBeeType(parent2), chance, chancedivider);
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
        return BeeManager.beeRoot.getMemberStack(getIndividual(), beeType.ordinal());
    }

    public final IBeeDefinition getRainResist() {
        return new BeeVariation.RainResist(this);
    }
    
	public static IAlleleBeeSpecies getGregtechBeeType(String name){
		Class<?> gtBees;    	
		try {
			Class gtBeeTypes = Class.forName("gregtech.loaders.misc.GT_BeeDefinition");
			Enum gtBeeEnumObject = Enum.valueOf(gtBeeTypes, name); 	
			Field gtBeesField = FieldUtils.getDeclaredField(gtBeeTypes, "species", true);
			gtBeesField.setAccessible(true);
			ReflectionUtils.makeFieldAccessible(gtBeesField);
			Object beeType = gtBeesField.get(gtBeeEnumObject);	    		    	
			return (IAlleleBeeSpecies) beeType;
		}		
		catch (ClassNotFoundException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}    	
		return null;
	}
}
