package GoodGenerator.CrossMod.Thaumcraft;

import GoodGenerator.util.DescTextLocalization;
import GoodGenerator.util.ItemRefer;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.config.ConfigItems;

import java.util.Arrays;

import static thaumcraft.api.ThaumcraftApi.addArcaneCraftingRecipe;

public class Research{

    public static void addResearch() {
        DescTextLocalization.addText("research.ESSENTIA_GENERATOR.page",3);
        DescTextLocalization.addText("research.ESSENTIA_CELL.page",1);
        GregTech_API.sThaumcraftCompat.addResearch(
                "ESSENTIA_GENERATOR",
                "Combustion Engine in Magic World",
                "Will it cause Flux pollution?",
                new String[]{"INFUSION"},
                "ARTIFICE",
                ItemRefer.Large_Essentia_Generator,
                3, 0, -9, 3,
                Arrays.asList(
                        new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L)
                        ),
                null,
                new Object[]{
                        "research.ESSENTIA_GENERATOR.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_GENERATOR",
                                ItemList.Hull_HV.get(1),
                                new ItemStack[]{
                                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                                        new ItemStack(ConfigBlocks.blockJar, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Thaumium, 1L),
                                        GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 1L),
                                        new ItemStack(ConfigBlocks.blockWoodenDevice, 1),
                                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Manyullyn, 1L),
                                        Ic2Items.teslaCoil,
                                        ItemList.Sensor_MV.get(1)
                                },
                                ItemRefer.Large_Essentia_Generator,
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.POTENTIA, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.ELECTRUM, 64L),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 32),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 32)
                                )
                        ),
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_GENERATOR",
                                ItemList.Hatch_Input_HV.get(1),
                                new ItemStack[]{
                                        new ItemStack(ConfigBlocks.blockJar, 1),
                                        ItemRefer.Magic_Casing,
                                        new ItemStack(ConfigBlocks.blockTube, 1),
                                        ItemList.Electric_Pump_MV.get(1L)
                                },
                                ItemRefer.Essentia_Hatch,
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 64),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 32),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.COGNITIO, 32)
                                )
                        ),
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_GENERATOR",
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Thaumium, 1),
                                new ItemStack[]{
                                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Steel, 1),
                                        ItemList.Electric_Pump_MV.get(1L),
                                        new ItemStack(ConfigBlocks.blockTube, 1, 4),
                                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2),
                                },
                                ItemRefer.Essentia_Cell_T1,
                                4,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 32),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 32),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 16)
                                )
                        ),
                        addArcaneCraftingRecipe(
                                "ESSENTIA_GENERATOR",
                                ItemRefer.Magic_Casing,
                                new AspectList().add(Aspect.AIR, 50).add(Aspect.FIRE, 50).add(Aspect.ORDER, 50),
                                "SCS","GAG","SCS",
                                'S', new ItemStack(ConfigItems.itemResource,1,14),
                                'C', GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.Thaumium, 1),
                                'G', Ic2Items.reinforcedGlass,
                                'A', Ic2Items.advancedMachine
                        ),
                        "research.ESSENTIA_GENERATOR.page.1",
                        "research.ESSENTIA_GENERATOR.page.2"
                }
        );
        GregTech_API.sThaumcraftCompat.addResearch(
                "ESSENTIA_CELL",
                "Better Cells",
                "And higher efficiency.",
                new String[]{"ESSENTIA_GENERATOR"},
                "ARTIFICE",
                ItemRefer.Essentia_Cell_T3,
                2, 0, -10, 3,
                Arrays.asList(
                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PRAECANTATIO, 10L),
                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 10L)
                ),
                null,
                new Object[]{
                        "research.ESSENTIA_CELL.page.0",
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_CELL",
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Thaumium, 1),
                                new ItemStack[]{
                                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Enderium, 1),
                                        ItemList.Electric_Pump_HV.get(1L),
                                        ItemList.QuantumEye.get(1L),
                                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2),
                                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Platinum, 1),
                                        WerkstoffLoader.Salt.get(OrePrefixes.gemExquisite, 1)
                                },
                                ItemRefer.Essentia_Cell_T2,
                                5,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 64),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 64),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 32)
                                )
                        ),
                        GregTech_API.sThaumcraftCompat.addInfusionRecipe(
                                "ESSENTIA_CELL",
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Void, 1),
                                new ItemStack[]{
                                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Titanium, 1),
                                        ItemList.Electric_Pump_EV.get(1L),
                                        ItemList.QuantumStar.get(1L),
                                        new ItemStack(ConfigBlocks.blockCosmeticOpaque, 1, 2),
                                        new ItemStack(ConfigItems.itemResource,1,14),
                                        Ic2Items.iridiumPlate,
                                        Ic2Items.fluidregulator,
                                        new ItemStack(ConfigBlocks.blockCrystal, 1, 6),
                                },
                                ItemRefer.Essentia_Cell_T3,
                                6,
                                Arrays.asList(
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.AQUA, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 128),
                                        new TC_Aspects.TC_AspectStack(TC_Aspects.PERMUTATIO, 64)
                                )
                        ),
                }
        );
    }
}
