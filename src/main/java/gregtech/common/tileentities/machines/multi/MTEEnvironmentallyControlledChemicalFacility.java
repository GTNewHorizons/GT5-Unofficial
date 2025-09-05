package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.GregTechAPI.sBlockCoilECCF;
import static gregtech.api.GregTechAPI.sBlockCoilECCF2;
import static gregtech.api.GregTechAPI.sBlockTintedGlass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GTRecipeConstants.ECCF_PRESSURE;
import static gregtech.api.util.GTRecipeConstants.ECCF_PRESSURE_DELTA;
import static gregtech.api.util.GTRecipeConstants.ECCF_TEMPERATURE;
import static gregtech.api.util.GTRecipeConstants.ECCF_TEMPERATURE_DELTA;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.getTier;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.DimensionConditions;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.blocks.BlockCasings12;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.multi.gui.MTEEnvironmentallyControlledChemicalFacilityGUI;
import gtPlusPlus.core.fluids.GTPPFluids;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEEnvironmentallyControlledChemicalFacility extends
    MTEExtendedPowerMultiBlockBase<MTEEnvironmentallyControlledChemicalFacility> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String HEAT_MODULE_L = "tempHeatL";
    private static final String COOL_MODULE_L = "tempCoolL";
    private static final String VACUUM_MODULE_R = "VacuumR";
    private static final String COMPRESSION_MODULE_R = "PressureR";
    private static final String PARALLEL_MODULE_R = "ParallelR";
    private static final String PARALLEL_MODULE_L = "ParallelL";
    private static final String ECCF_PRESSURE_NBT_TAG = "ECCFPressure";
    private static final String ECCF_TEMPERATURE_NBT_TAG = "ECCFTemperature";
    private static final int MODULE_OFFSET_V = 10;
    private static final int MODULE_OFFSET_LEFT = 10;
    private static final int MODULE_OFFSET_RIGHT = -6;
    private static final int MODULE_OFFSET_DEPTH = 0;
    private static final double TEMPERATURE_COEFF_DEFAULT = 0.2;
    private static final double PRESSURE_COEFF_DEFAULT = 0.2;
    private static final double LUBRICANT_DRAIN_AMOUNT = 5;
    private static final int TICK_INTERVAL = 20;

    // spotless:off
    private static final IStructureDefinition<MTEEnvironmentallyControlledChemicalFacility> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEnvironmentallyControlledChemicalFacility>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][]{
                {"           ","           ","   AAAAA   ","  AAAAAAA  ","  AAAAAAA  ","  AAAAAAA  ","  AAAAAAA  ","  AAAAAAA  ","   AAAAA   ","           ","           "},
                {"           ","   AAAAA   ","  AFFFFFA  "," AF JJJ FA "," AFJJJJJFA "," AFJJJJJFA "," AFJJJJJFA "," AF JJJ FA ","  AFFFFFA  ","   AAAAA   ","           "},
                {"   FFFFF   ","  FAAAAAF  "," FA     AF ","FA  JJJ  AF","FA J   J AF","FA J   J AF","FA J   J AF","FA  JJJ  AF"," FA     AF ","  FAAAAAF  ","   FFFFF   "},
                {"           ","   AAAAA   ","  A     A  "," A  JJJ  A "," A J   J A "," A J   J A "," A J   J A "," A  JJJ  A ","  A     A  ","   AAAAA   ","           "},
                {"           ","   AAGAA   ","  A     A  "," A  JJJ  A ","PPPJ   JPPP","PPPJ   JPPP","PPPJ   JPPP"," A  JJJ  A ","  A     A  ","   AAGAA   ","           "},
                {"           ","   AAGAA   ","  A     A  "," A  JJJ  A ","PPPJ   JPPP","           ","PPPJ   JPPP"," A  JJJ  A ","  A     A  ","   AAGAA   ","           "},
                {"           ","   AAGAA   ","  A     A  "," A  JJJ  A ","PPPJ   JPPP","PPPJ   JPPP","PPPJ   JPPP"," A  JJJ  A ","  A     A  ","   AAGAA   ","           "},
                {"           ","   AAAAA   ","  A     A  "," A  JJJ  A "," A J   J A "," A J   J A "," A J   J A "," A  JJJ  A ","  A     A  ","   AAAAA   ","           "},
                {"   FFFFF   ","  FAAAAAF  "," FA     AF ","FA  JJJ  AF","FA J   J AF","FA J   J AF","FA J   J AF","FA  JJJ  AF"," FA     AF ","  FAAAAAF  ","   FFFFF   "},
                {"           ","   AAAAA   ","  A     A  "," A  JJJ  A "," A JJJJJ A "," A JJJJJ A "," A JJJJJ A "," A  JJJ  A ","  A     A  ","   AAAAA   ","           "},
                {"   AA~AA   ","  AAAAAAA  "," AAAAAAAAA ","AAAAAAAAAAA","AAAAAAAAAAA","AAAAAAAAAAA","AAAAAAAAAAA","AAAAAAAAAAA"," AAAAAAAAA ","  AAAAAAA  ","   AAAAA   "}}))
        .addShape(
            HEAT_MODULE_L,
            transpose(new String[][]{
                {"     ","     ","     ","     ","     ","     ","     ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","     ","     ","     ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","     ","     ","     ","     ","     ","     ","     "},
                {"     ","     ","     ","  Q  "," QQQ "," QQQ "," QQQ ","  Q  ","     ","     ","     "},
                {"     ","     ","     "," QQQ ","  cQP","  cQP","  cQP"," QQQ ","     ","     ","     "},
                {"     ","     ","     "," QQQ ","  cQP","  HQ ","  cQP"," QQQ ","     ","     ","     "},
                {"     ","     ","     "," QQQ ","  cQP","  cQP","  cQP"," QQQ ","     ","     ","     "},
                {"     ","     ","     "," FQF "," QQQ "," QQQ "," QQQ "," FQF ","     ","     ","     "},
                {"     ","     ","     ","     "," FQF "," QQQ "," FQF ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," FQF "," QQQ "," FQF ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","EEEF ","EEEE ","EEEF ","     ","     ","     ","     "}}))
        .addShape(
            COOL_MODULE_L,
            transpose(new String[][]{
                {"     ","     ","     ","     ","     ","     ","     ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","     ","     ","     ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","     ","     ","     ","     ","     ","     ","     "},
                {"     ","     ","     ","  i  "," ipi "," ipi "," ipi ","  i  ","     ","     ","     "},
                {"     ","     ","     "," ipi "," giiP"," giiP"," giiP"," ipi ","     ","     ","     "},
                {"     ","     ","     "," ipi "," giiP"," Cii "," giiP"," ipi ","     ","     ","     "},
                {"     ","     ","     "," ipi "," giiP"," giiP"," giiP"," ipi ","     ","     ","     "},
                {"     ","     ","     "," lil "," iii "," iii "," iii "," lil ","     ","     ","     "},
                {"     ","     ","     ","     "," lil "," iii "," lil ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," lil "," iii "," lil ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","eeel ","eeee ","eeel ","     ","     ","     ","     "}
            }))
        .addShape(
            VACUUM_MODULE_R,
            transpose(new String[][]{
                {"     ","     ","     ","     ","     ","     ","     ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","  y  "," yyy ","  y  ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," yyy "," yyy "," yyy ","     ","     ","     ","     "},
                {"     ","     ","     ","  o  "," yyy "," y P "," yyy ","  o  ","     ","     ","     "},
                {"     ","     ","     ","  o  ","Pyyy ","Py y ","Pyyy ","  o  ","     ","     ","     "},
                {"     ","     ","     ","  o  ","Pyyo "," y V ","Pyyo ","  o  ","     ","     ","     "},
                {"     ","     ","     ","  o  ","Pyyo ","Py V ","Pyyo ","  o  ","     ","     ","     "},
                {"     ","     ","     ","  o  "," yyo "," y V "," yyo ","  o  ","     ","     ","     "},
                {"     ","     ","     ","  o  "," yyy "," y y "," yyy ","  o  ","     ","     ","     "},
                {"     ","     ","     ","  o  "," yyy "," y P "," yyy ","  o  ","     ","     ","     "},
                {"     ","     ","     ","     "," uuu "," uuu "," uuu ","     ","     ","     ","     "}})
        )
        .addShape(
            COMPRESSION_MODULE_R,
            transpose(new String[][]{
                {"     ","     ","     ","     ","     ","     ","     ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","  Y  "," YYY ","  Y  ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," YYY "," YYY "," YYY ","     ","     ","     ","     "},
                {"     ","     ","     ","  r  "," YYY "," Y P "," YYY ","  r  ","     ","     ","     "},
                {"     ","     ","     ","  r  ","PYYY ","PY Y ","PYYY ","  r  ","     ","     ","     "},
                {"     ","     ","     ","  r  ","PYYr "," Y K ","PYYr ","  r  ","     ","     ","     "},
                {"     ","     ","     ","  r  ","PYYr ","PY K ","PYYr ","  r  ","     ","     ","     "},
                {"     ","     ","     ","  r  "," YYr "," Y K "," YYr ","  r  ","     ","     ","     "},
                {"     ","     ","     ","  r  "," YYY "," Y Y "," YYY ","  r  ","     ","     ","     "},
                {"     ","     ","     ","  r  "," YYY "," Y P "," YYY ","  r  ","     ","     ","     "},
                {"     ","     ","     ","     "," UUU "," UUU "," UUU ","     ","     ","     ","     "}})
        )
        .addShape(
            PARALLEL_MODULE_L,
            transpose(new String[][]{
                {"     ","     ","     ","     ","  Q  "," QQQ ","  Q  ","     ","     ","     ","     "},
                {"     ","     ","     "," FFF ","FQQQF","FQWQF","FQQQF"," FFF ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," Q Q "," QQQ ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," Q Q "," QQQ ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," QGQP"," Q GP"," QGQP","     ","     ","     ","     "},
                {"     ","     ","     ","     "," QGQP"," Q G "," QGQP","     ","     ","     ","     "},
                {"     ","     ","     ","     "," QGQP"," Q GP"," QGQP","     ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," Q Q "," QQQ ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," Q Q "," QQQ ","     ","     ","     ","     "},
                {"     ","     ","     "," FFF ","FQQQF","FQWQF","FQQQF"," FFF ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," QQQ "," QQQ ","     ","     ","     ","     "}})
        )
        .addShape(
            PARALLEL_MODULE_R,
            transpose(new String[][]{
                {"     ","     ","     ","     ","  Q  "," QQQ ","  Q  ","     ","     ","     ","     "},
                {"     ","     ","     "," FFF ","FQQQF","FQIQF","FQQQF"," FFF ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," Q Q "," QQQ ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," Q Q "," QQQ ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","PQGQ ","PQ G ","PQGQ ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","PQGQ "," Q G ","PQGQ ","     ","     ","     ","     "},
                {"     ","     ","     ","     ","PQGQ ","PQ G ","PQGQ ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," Q Q "," QQQ ","     ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," Q Q "," QQQ ","     ","     ","     ","     "},
                {"     ","     ","     "," FFF ","FQQQF","FQIQF","FQQQF"," FFF ","     ","     ","     "},
                {"     ","     ","     ","     "," QQQ "," QQQ "," QQQ ","     ","     ","     ","     "}})
        )
        .addElement(
            'D',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).atLeast(InputHatch, OutputHatch)
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(15))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings12, 15))
        .addElement(
            'C',
            GTStructureChannels.ECCF_FREEZER.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getTierForBlock,
                    ImmutableList.of(
                        Pair.of(sBlockCoilECCF, 0),
                        Pair.of(sBlockCoilECCF, 1),
                        Pair.of(sBlockCoilECCF, 2),
                        Pair.of(sBlockCoilECCF, 3)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.freezerTier = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.freezerTier)))
        .addElement(
            'H',
            GTStructureChannels.ECCF_HEATER.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getTierForBlock,
                    ImmutableList.of(
                        Pair.of(sBlockCoilECCF, 4),
                        Pair.of(sBlockCoilECCF, 5),
                        Pair.of(sBlockCoilECCF, 6),
                        Pair.of(sBlockCoilECCF, 7)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.heaterTier = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.heaterTier)))
        .addElement(
            'K',
            GTStructureChannels.ECCF_COMPRESSOR.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getTierForBlock,
                    ImmutableList.of(
                        Pair.of(sBlockCoilECCF, 8),
                        Pair.of(sBlockCoilECCF, 9),
                        Pair.of(sBlockCoilECCF, 10),
                        Pair.of(sBlockCoilECCF, 11)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.compressorTier = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.compressorTier)))
        .addElement(
            'V',
            GTStructureChannels.ECCF_VACUUM.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getTierForBlock,
                    ImmutableList.of(
                        Pair.of(sBlockCoilECCF, 12),
                        Pair.of(sBlockCoilECCF, 13),
                        Pair.of(sBlockCoilECCF, 14),
                        Pair.of(sBlockCoilECCF, 15)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.vacuumTier = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.vacuumTier)))
        .addElement(
            'W',
            GTStructureChannels.ECCF_PARALLEL_L.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getTierForBlock,
                    ImmutableList
                        .of(Pair.of(sBlockCoilECCF2, 0), Pair.of(sBlockCoilECCF2, 1), Pair.of(sBlockCoilECCF2, 2)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.parallelModuleTierL = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.parallelModuleTierL)))
        .addElement(
            'I',
            GTStructureChannels.ECCF_PARALLEL_R.use(
                ofBlocksTiered(
                    MTEEnvironmentallyControlledChemicalFacility::getTierForBlock,
                    ImmutableList
                        .of(Pair.of(sBlockCoilECCF2, 0), Pair.of(sBlockCoilECCF2, 1), Pair.of(sBlockCoilECCF2, 2)),
                    -1,
                    (MTEEnvironmentallyControlledChemicalFacility t, Integer m) -> t.parallelModuleTierR = m,
                    (MTEEnvironmentallyControlledChemicalFacility t) -> t.parallelModuleTierR)))
        .addElement(
            'A',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class)
                .atLeast(OutputHatch, InputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(15))
                .dot(1)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings12, 15)))
        .addElement(
            'Y',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).hatchClass(MTEHatchInput.class)
                .adder(MTEEnvironmentallyControlledChemicalFacility::addLubricantInputToMachineList)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(3))
                .dot(2)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings10, 3)))
        .addElement(
            'y',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).hatchClass(MTEHatchInput.class)
                .adder(MTEEnvironmentallyControlledChemicalFacility::addLubricantInputToMachineList)
                .casingIndex(Casings.ThermalProcessingCasing.getTextureId())
                .dot(2)
                .buildAndChain(Casings.ThermalProcessingCasing.asElement()))
        .addElement(
            'U',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).hatchClass(MTEHatchEnergy.class)
                .adder(MTEEnvironmentallyControlledChemicalFacility::addPressureEnergyToMachineList)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(3))
                .dot(3)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings10, 3)))
        .addElement(
            'u',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).hatchClass(MTEHatchEnergy.class)
                .adder(MTEEnvironmentallyControlledChemicalFacility::addPressureEnergyToMachineList)
                .casingIndex(Casings.ThermalProcessingCasing.textureId)
                .dot(3)
                .buildAndChain(Casings.ThermalProcessingCasing.asElement()))
        .addElement(
            'E',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).hatchClass(MTEHatchInput.class)
                .adder(MTEEnvironmentallyControlledChemicalFacility::addCoolantInputToMachineList)
                .casingIndex(((BlockCasings12) GregTechAPI.sBlockCasings12).getTextureIndex(15))
                .dot(4)
                .buildAndChain(GregTechAPI.sBlockCasings12, 15))
        .addElement(
            'e',
            buildHatchAdder(MTEEnvironmentallyControlledChemicalFacility.class).hatchClass(MTEHatchInput.class)
                .adder(MTEEnvironmentallyControlledChemicalFacility::addCoolantInputToMachineList)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
                .dot(4)
                .buildAndChain(GregTechAPI.sBlockCasings8, 0))
        .addElement('F', ofFrame(Materials.Polybenzimidazole))
        .addElement('r', ofFrame(Materials.Nickel))
        .addElement('o', ofFrame(Materials.RedstoneAlloy))
        .addElement('l', ofFrame(Materials.Polytetrafluoroethylene))
        .addElement('G', ofBlockAnyMeta(sBlockTintedGlass, 2))
        .addElement('p', ofBlock(GregTechAPI.sBlockCasings8, 1)) // ptfe pipe casing
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings9, 0)) // pbi pipe casing
        .addElement('g', ofBlock(GregTechAPI.sBlockCasings3, 10)) // grate machine casing
        .addElement('Q', ofBlock(GregTechAPI.sBlockCasings12, 15)) // pbi machine casing
        .addElement('J', ofBlock(GregTechAPI.sBlockCasings2, 0)) // solid steel machine casing
        .addElement('i', ofBlock(GregTechAPI.sBlockCasings8, 0)) // chemically inert casing
        .addElement('c', ofBlock(GregTechAPI.sBlockCasings5, 0)) // cupronickel coil
        .build();
    // spotless:on

    public double currentTemp = 0;
    public double currentPressure = 0;
    public double pressureLeakValue = 0;
    public double pressureLossValue = 0;
    public double temperatureLossValue = 0;
    public double tempChange = 0;
    public double presChange = 0;
    public int freezerTier = -1;
    public int vacuumTier = -1;
    public int heaterTier = -1;
    public int compressorTier = -1;
    public int parallelModuleTierR = -1;
    public int parallelModuleTierL = -1;
    private boolean isHeaterModule;
    private boolean isFreezerModule;
    private boolean isVacuumModule;
    private boolean isCompressorModule;
    private double coeffTemp = TEMPERATURE_COEFF_DEFAULT;
    private double coeffPressure = PRESSURE_COEFF_DEFAULT;
    public double ambientPressure = 0;
    public double ambientTemp = 0;
    private double coolantTemp = 0;
    private int recipeEUt = 0;
    private int requiredTemp = 0;
    private int requiredPressure = 0;
    private final List<MTEHatchInput> mLubricantInputHatches = new ArrayList<>();
    private final List<MTEHatchInput> mCoolantInputHatches = new ArrayList<>();
    private final List<MTEHatchEnergy> mPressureEnergyHatches = new ArrayList<>();
    private int tempThreshold;
    private int pressureThreshold;
    private long drainAmountEU = 0;
    private int coolantInputHatchAmount = 0;
    public int deltaPressure;
    public int deltaTemp;

    public MTEEnvironmentallyControlledChemicalFacility(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEEnvironmentallyControlledChemicalFacility(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEEnvironmentallyControlledChemicalFacility> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEnvironmentallyControlledChemicalFacility(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            return aActive ? getActiveTextures() : getInactiveTextures();
        }
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 15)) };
    }

    private ITexture[] getActiveTextures() {
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 15)),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    private ITexture[] getInactiveTextures() {
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings12, 15)),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    // spotless:off
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Environmentally Controlled Chemical Facility, ECCF")
            .addInfo(EnumChatFormatting.GRAY + "Allows to produce some chemicals in a " + EnumChatFormatting.BLUE + "single step" + EnumChatFormatting.GRAY + ", but requires special conditions")
            .addInfo(EnumChatFormatting.GRAY + "Doesn't overclock, instead increases parallels by " + EnumChatFormatting.GOLD + "4 ^ (Energy Hatch Tier - Recipe tier) * Parallel Modules")
            .addNoTierSkips()
            .addSeparator()
            .addInfo(EnumChatFormatting.GRAY + "Conditions are shown in NEI and can be achieved by placing ECCF on another " + EnumChatFormatting.BLUE + "planet ")
            .addInfo(EnumChatFormatting.GRAY + "with required conditions or maintaining them using " + EnumChatFormatting.BLUE + "Modules")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "Module interaction")
            .addInfo(EnumChatFormatting.GRAY + "Every second, ECCF temperature drifts towards ambient, to: " + EnumChatFormatting.GOLD + "(current - ambient) * loss% + ambient")
            .addInfo(EnumChatFormatting.GRAY + "It drains " + EnumChatFormatting.YELLOW + "all coolants" + EnumChatFormatting.GRAY + " from the input hatch in the temperature module")
            .addInfo(EnumChatFormatting.GRAY + "And changes temperature to: " + EnumChatFormatting.GOLD + "current + (fluid amount / 10000) * " + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE + "T")
            .addInfo(EnumChatFormatting.GRAY + "ECCF drains energy hatch buffer of pressure module " + EnumChatFormatting.YELLOW + "every tick")
            .addInfo(EnumChatFormatting.GRAY + "Every second, ECCF pressure drifts towards ambient, to: " + EnumChatFormatting.GOLD + "(current - ambient) * loss% + ambient")
            .addInfo(EnumChatFormatting.GRAY + "Then applies pressure leaks: " + EnumChatFormatting.GOLD + "(current * " + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE + "VO" + EnumChatFormatting.GOLD + " + initial * (1 - " + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE + "VO" + EnumChatFormatting.GOLD + ")) - current")
            .addInfo(EnumChatFormatting.GRAY + "And applies pressure changes depending on the module type: " + EnumChatFormatting.GOLD + "current + (buffer EU ^ 0.7)")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE + "T" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "emperature")
            .addInfo("" + EnumChatFormatting.RED + EnumChatFormatting.BOLD + "Heating Module")
            .addInfo(formatHeater(Materials.Lava.mFluid))
            .addInfo(formatHeater(GTPPFluids.Pyrotheum))
            .addInfo(formatHeater(Materials.Helium.mPlasma))
            .addInfo(formatHeater(MaterialsUEVplus.RawStarMatter.mFluid))
            .addInfo("" + EnumChatFormatting.BLUE + EnumChatFormatting.BOLD + "Cooling Module")
            .addInfo(formatCoolant(GTModHandler.getIC2Coolant(0).getFluid()))
            .addInfo(formatCoolant(GTPPFluids.Cryotheum))
            .addInfo(formatCoolant(Materials.SuperCoolant.mFluid))
            .addInfo(formatCoolant(MaterialsUEVplus.SpaceTime.getMolten(0).getFluid()))
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + "Pressure")
            .addInfo(EnumChatFormatting.GRAY + "2 modules: " + EnumChatFormatting.DARK_AQUA + "Vacuum" + EnumChatFormatting.GRAY + " to decrease pressure and " + EnumChatFormatting.GOLD + "Compressor" + EnumChatFormatting.GRAY + " to increase pressure")
            .addInfo(EnumChatFormatting.GRAY + "Pressure module requires energy and " + EnumChatFormatting.YELLOW + "5L/s" + EnumChatFormatting.GRAY + " of " + EnumChatFormatting.YELLOW + "VO lubricants " + EnumChatFormatting.GRAY + "to operate")
            .addInfo(EnumChatFormatting.GRAY + "While the module is running, " + EnumChatFormatting.YELLOW + "0-80% " + EnumChatFormatting.GRAY + "of the non-ambient pressure is" + EnumChatFormatting.RED + " lost" + EnumChatFormatting.GRAY + " depending on the " + EnumChatFormatting.YELLOW + "VO lubricant " + EnumChatFormatting.GRAY + "used")
            .addSeparator()
            .addInfo("" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + EnumChatFormatting.UNDERLINE + "VO" + EnumChatFormatting.WHITE + EnumChatFormatting.BOLD + " Lubricants Values")
            .addInfo(formatLubricant("nothing", "No lubricant"))
            .addInfo(formatLubricant("vo17", "VO-17"))
            .addInfo(formatLubricant("vo43", "VO-43"))
            .addInfo(formatLubricant("vo75", "VO-75"))
            .addSeparator()
            .addTecTechHatchInfo()
            .beginStructureBlock(11, 11, 11, true)
            .addController("Front Center")
            .addCasingInfoMin("Polybenzimidazole Machine Casing", 0, false)
            .addInputBus("Any Polybenzimidazole Machine Casing", 1)
            .addOutputBus("Any Polybenzimidazole Machine Casing", 1)
            .addInputHatch("Any Polybenzimidazole Machine Casing", 1)
            .addOutputHatch("Any Polybenzimidazole Machine Casing", 1)
            .addEnergyHatch("Any Polybenzimidazole Machine Casing", 1)
            .addMaintenanceHatch("Any Polybenzimidazole Machine Casing", 1)
            .addSubChannelUsage(GTStructureChannels.ECCF_PARALLEL_L)
            .addSubChannelUsage(GTStructureChannels.ECCF_PARALLEL_R)
            .addSubChannelUsage(GTStructureChannels.ECCF_COMPRESSOR)
            .addSubChannelUsage(GTStructureChannels.ECCF_VACUUM)
            .addSubChannelUsage(GTStructureChannels.ECCF_HEATER)
            .addSubChannelUsage(GTStructureChannels.ECCF_FREEZER)
            .toolTipFinisher(EnumChatFormatting.BLUE + "VorTex" + EnumChatFormatting.GRAY + " & " + EnumChatFormatting.YELLOW + EnumChatFormatting.BOLD + "Rait_GamerGR");
        return tt;
    }

    private String formatCoolant(Fluid liquid){
     return String.format("§3%s§7: §b%,.0f K", liquid.getLocalizedName(), getCoolantTemp(liquid.getName()));
    }

    private String formatHeater(Fluid liquid){
        return String.format("§6%s§7: §e%,.0f K", liquid.getLocalizedName(), getCoolantTemp(liquid.getName()));
    }

    private String formatLubricant(String name, String displayName) {
        return String.format("§6%s §7- %.0f%% loss", displayName, getLeakPercentage(name) * 100);
    }
    //spotless:on

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 5, MODULE_OFFSET_V, 0);
        Pair<Integer, Integer> modules = getModulesFromStack(stackSize);
        buildModule(modules.getLeft(), stackSize, hintsOnly, MODULE_OFFSET_LEFT);
        buildModule(modules.getRight(), stackSize, hintsOnly, MODULE_OFFSET_RIGHT);
    }

    private void buildModule(int moduleType, ItemStack stackSize, boolean hintsOnly, int offsetX) {
        String moduleName = switch (moduleType) {
            case 1 -> HEAT_MODULE_L;
            case 2 -> COOL_MODULE_L;
            case 3 -> COMPRESSION_MODULE_R;
            case 4 -> VACUUM_MODULE_R;
            case 5 -> offsetX == MODULE_OFFSET_LEFT ? PARALLEL_MODULE_L : PARALLEL_MODULE_R;
            default -> null;
        };
        if (moduleName != null) {
            buildPiece(moduleName, stackSize, hintsOnly, offsetX, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        Pair<Integer, Integer> modules = getModulesFromStack(stackSize);
        if (mMachine && modules.getLeft() == 0 && modules.getRight() == 0) return -1;

        int built = survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            5,
            MODULE_OFFSET_V,
            0,
            elementBudget,
            env,
            false,
            true);
        built += buildSurvivalModule(modules.getLeft(), stackSize, elementBudget, env, MODULE_OFFSET_LEFT);
        built += buildSurvivalModule(modules.getRight(), stackSize, elementBudget, env, MODULE_OFFSET_RIGHT);
        return built;
    }

    private int buildSurvivalModule(int moduleType, ItemStack stackSize, int elementBudget,
        ISurvivalBuildEnvironment env, int offsetX) {
        String moduleName = switch (moduleType) {
            case 1 -> HEAT_MODULE_L;
            case 2 -> COOL_MODULE_L;
            case 3 -> COMPRESSION_MODULE_R;
            case 4 -> VACUUM_MODULE_R;
            case 5 -> offsetX == MODULE_OFFSET_LEFT ? PARALLEL_MODULE_L : PARALLEL_MODULE_R;
            default -> null;
        };
        if (moduleName == null) return 0;
        return survivialBuildPiece(
            moduleName,
            stackSize,
            offsetX,
            MODULE_OFFSET_V,
            MODULE_OFFSET_DEPTH,
            elementBudget,
            env,
            false,
            true);
    }

    private Pair<Integer, Integer> getModulesFromStack(ItemStack stackSize) {
        int moduleLeft = 0;
        int moduleRight = 0;
        if (stackSize.getTagCompound() != null) {
            NBTTagCompound channels = stackSize.getTagCompound()
                .getCompoundTag("channels");
            if (channels != null) {
                if (channels.getInteger("eccf_heater") > 0) moduleLeft = 1;
                if (channels.getInteger("eccf_freezer") > 0) moduleLeft = 2;
                if (channels.getInteger("eccf_parallel_left") > 0) moduleLeft = 5;
                if (channels.getInteger("eccf_compress") > 0) moduleRight = 3;
                if (channels.getInteger("eccf_vacuum") > 0) moduleRight = 4;
                if (channels.getInteger("eccf_parallel_right") > 0) moduleRight = 5;
            }
        }
        return Pair.of(moduleLeft, moduleRight);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        resetModuleTiers();
        getDimConditions();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 5, MODULE_OFFSET_V, 0)) return false;

        coolantInputHatchAmount = mCoolantInputHatches.size();
        isHeaterModule = checkPiece(HEAT_MODULE_L, MODULE_OFFSET_LEFT, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);
        if (coolantInputHatchAmount > 1) return false;
        coolantInputHatchAmount = mCoolantInputHatches.size();
        isFreezerModule = checkPiece(COOL_MODULE_L, MODULE_OFFSET_LEFT, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);
        if (coolantInputHatchAmount > 1) return false;
        isVacuumModule = checkPiece(VACUUM_MODULE_R, MODULE_OFFSET_RIGHT, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);
        isCompressorModule = checkPiece(
            COMPRESSION_MODULE_R,
            MODULE_OFFSET_RIGHT,
            MODULE_OFFSET_V,
            MODULE_OFFSET_DEPTH);
        checkPiece(PARALLEL_MODULE_L, MODULE_OFFSET_LEFT, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);
        checkPiece(PARALLEL_MODULE_R, MODULE_OFFSET_RIGHT, MODULE_OFFSET_V, MODULE_OFFSET_DEPTH);

        coeffPressure = getPresCoefficient(Math.max(vacuumTier, compressorTier));
        coeffTemp = getTempCoefficient(Math.max(freezerTier, heaterTier));
        return mExoticEnergyHatches.size() <= 1;
    }

    private void resetModuleTiers() {
        freezerTier = -1;
        heaterTier = -1;
        vacuumTier = -1;
        compressorTier = -1;
        parallelModuleTierL = -1;
        parallelModuleTierR = -1;
        isHeaterModule = false;
        isFreezerModule = false;
        isVacuumModule = false;
        isCompressorModule = false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setNoOverclock(true);
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                requiredTemp = recipe.getMetadataOrDefault(ECCF_TEMPERATURE, 0);
                requiredPressure = recipe.getMetadataOrDefault(ECCF_PRESSURE, 0);
                deltaPressure = recipe.getMetadataOrDefault(ECCF_PRESSURE_DELTA, 0);
                deltaTemp = recipe.getMetadataOrDefault(ECCF_TEMPERATURE_DELTA, 0);
                tempThreshold = (int) (1.5 * Math.pow(requiredTemp, 0.55));
                pressureThreshold = (int) (1.5 * Math.pow(requiredPressure, 0.55));
                recipeEUt = recipe.mEUt;

                if (Math.abs(currentTemp - requiredTemp) <= tempThreshold
                    && Math.abs(currentPressure - requiredPressure) <= pressureThreshold) {
                    return super.validateRecipe(recipe);
                }
                deltaTemp = 0;
                deltaPressure = 0;
                return CheckRecipeResultRegistry.RECIPE_CONDITIONS;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes)
            .setMaxTierSkips(0);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        getMaxParallelRecipes();
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(getMaxInputAmps());
    }

    @Override
    public int getMaxParallelRecipes() {
        return (int) Math.pow(4, getTier(this.getMaxInputVoltage()) - getTier(recipeEUt))
            * parallelModuleValue(parallelModuleTierL)
            * parallelModuleValue(parallelModuleTierR);
    }

    private int parallelModuleValue(int tier) {
        return switch (tier) {
            case 0 -> 4;
            case 1 -> 8;
            case 2 -> 16;
            default -> 1;
        };
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.ECCFRecipes, RecipeMaps.planetConditions);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.ECCFRecipes;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Nullable
    public static Integer getTierForBlock(Block block, int meta) {
        if (block == sBlockCoilECCF) {
            if (meta >= 0 && meta <= 3) return meta; // Freezer
            if (meta >= 4 && meta <= 7) return meta - 4; // Heater
            if (meta >= 8 && meta <= 11) return meta - 8; // Compressor
            if (meta >= 12 && meta <= 15) return meta - 12; // Vacuum
        } else if (block == sBlockCoilECCF2) {
            if (meta >= 0 && meta <= 3) return meta; // Parallel
        }
        return null;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setDouble("temperature", currentTemp);
        tag.setDouble("pressure", currentPressure);
        tag.setInteger("parallels", getMaxParallelRecipes());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.ECCF.temperature") + ": "
                + EnumChatFormatting.WHITE
                + String.format("%.2f K", tag.getDouble("temperature")));
        currentTip.add(
            StatCollector.translateToLocal("GT5U.ECCF.pressure") + ": "
                + EnumChatFormatting.WHITE
                + String.format("%.2f Pa", tag.getDouble("pressure")));
        currentTip.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.WHITE
                + tag.getInteger("parallels"));
    }

    @Override
    public String[] getInfoData() {
        super.getInfoData();
        Object[][] stats = { { "GT5U.ECCF.pressure", currentPressure, "Pa" },
            { "GT5U.ECCF.pressure_required", requiredPressure, "Pa" },
            { "GT5U.ECCF.pressure_threshold", pressureThreshold, "Pa" }, { "GT5U.ECCF.temperature", currentTemp, "K" },
            { "GT5U.ECCF.temperature_required", requiredTemp, "K" },
            { "GT5U.ECCF.temperature_threshold", tempThreshold, "K" } };
        return Arrays.stream(stats)
            .map(
                s -> StatCollector.translateToLocal((String) s[0]) + ": "
                    + EnumChatFormatting.GREEN
                    + GTUtility.formatNumbers((double) s[1])
                    + EnumChatFormatting.RESET
                    + " "
                    + s[2])
            .toArray(String[]::new);
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEEnvironmentallyControlledChemicalFacilityGUI(this);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setDouble(ECCF_PRESSURE_NBT_TAG, currentPressure);
        aNBT.setDouble(ECCF_TEMPERATURE_NBT_TAG, currentTemp);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        currentPressure = aNBT.getDouble(ECCF_PRESSURE_NBT_TAG);
        currentTemp = aNBT.getDouble(ECCF_TEMPERATURE_NBT_TAG);
        super.loadNBTData(aNBT);
    }

    private void getDimConditions() {
        WorldProvider provider = getBaseMetaTileEntity().getWorld().provider;
        DimensionConditions condition = DimensionConditions.fromDimensionName(provider.getDimensionName());
        ambientTemp = condition.getAmbientTemp();
        ambientPressure = condition.getAmbientPressure();
    }

    private double getTempCoefficient(int blockTier) {
        return switch (blockTier) {
            case 0 -> 0.5;
            case 1 -> 0.65;
            case 2 -> 0.85;
            case 3 -> 0.95;
            default -> TEMPERATURE_COEFF_DEFAULT;
        };
    }

    private double getPresCoefficient(int blockTier) {
        return switch (blockTier) {
            case 0 -> 0.4;
            case 1 -> 0.65;
            case 2 -> 0.8;
            case 3 -> 0.95;
            default -> PRESSURE_COEFF_DEFAULT;
        };
    }

    private double getCoolantTemp(String name) {
        return switch (name) {
            case "ic2coolant" -> 250;
            case "cryotheum" -> 25;
            case "supercoolant" -> 5;
            case "molten.spacetime" -> 0;
            case "lava" -> 1300;
            case "pyrotheum" -> 4000;
            case "plasma.helium" -> 10000;
            case "rawstarmatter" -> 10000000;
            default -> ambientTemp;
        };
    }

    public boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMeta = aTileEntity.getMetaTileEntity();
        if (!(aMeta instanceof MTEHatchInput hatch)) return false;
        hatch.updateTexture(aBaseCasingIndex);
        hatch.mRecipeMap = null;
        mCoolantInputHatches.add(hatch);
        coolantInputHatchAmount = mCoolantInputHatches.size();
        return true;
    }

    public boolean addLubricantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMeta = aTileEntity.getMetaTileEntity();
        if (!(aMeta instanceof MTEHatchInput hatch)) return false;
        hatch.updateTexture(aBaseCasingIndex);
        hatch.mRecipeMap = null;
        mLubricantInputHatches.add(hatch);
        return true;
    }

    public boolean addPressureEnergyToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMeta = aTileEntity.getMetaTileEntity();
        if (!(aMeta instanceof MTEHatchEnergy hatch)) return false;
        hatch.updateTexture(aBaseCasingIndex);
        mPressureEnergyHatches.add(hatch);
        return true;
    }

    private double getLeakPercentage(String lubricantType) {
        return switch (lubricantType) {
            case "vo17" -> 0.3;
            case "vo43" -> 0.1;
            case "vo75" -> 0;
            default -> 0.8;
        };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        initializeConditions();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        handleEnergyDrain();
        if (mMachine && aTick % TICK_INTERVAL == 0) {
            updateTemperature();
            updatePressure();
            applyRecipeConditions();
        }
    }

    private void handleEnergyDrain() {
        drainAmountEU = 0;
        for (MTEHatchEnergy hatch : mPressureEnergyHatches) {
            IGregTechTileEntity te = hatch.getBaseMetaTileEntity();
            long stored = te.getStoredEU();
            drainAmountEU += stored;
            te.decreaseStoredEnergyUnits(stored, false);
        }
    }

    private void initializeConditions() {
        if (currentTemp == 0 && currentPressure == 0) {
            currentPressure = ambientPressure;
            currentTemp = ambientTemp;
        }
    }

    private void updateTemperature() {
        temperatureLossValue = (currentTemp - ambientTemp) * coeffTemp + ambientTemp - currentTemp;
        currentTemp += temperatureLossValue;
        tempChange = 0;
        if (isFreezerModule || isHeaterModule) {
            for (MTEHatchInput hatch : mCoolantInputHatches) {
                FluidStack fluid = hatch.mFluid;
                if (fluid == null) continue;
                String coolantName = fluid.getFluid()
                    .getName();
                coolantTemp = getCoolantTemp(coolantName);
                boolean isCoolant = coolantName.equals("ic2coolant") || coolantName.equals("cryotheum")
                    || coolantName.equals("supercoolant")
                    || coolantName.equals("molten.spacetime");
                boolean isHot = coolantName.equals("pyrotheum") || coolantName.equals("plasma.helium")
                    || coolantName.equals("rawstarmatter")
                    || coolantName.equals("lava");
                if ((isFreezerModule && isCoolant) || (isHeaterModule && isHot)) {
                    double coolantTempImpact = fluid.amount / 10000.0 * coolantTemp;
                    double tempBeforeChange = currentTemp;
                    if (coolantTemp < ambientTemp) {
                        currentTemp = Math.max(currentTemp - coolantTempImpact, coolantTemp);
                    } else {
                        currentTemp = min(currentTemp + coolantTempImpact, coolantTemp);
                    }
                    tempChange += tempBeforeChange - currentTemp;
                    drain(hatch, fluid, true);
                }
            }
        }
    }

    private void updatePressure() {
        pressureLossValue = (currentPressure - ambientPressure) * coeffPressure + ambientPressure - currentPressure;
        currentPressure += pressureLossValue;
        presChange = 0;
        double leakCoeff = 0.8;
        if (isCompressorModule || isVacuumModule) {
            if (!mPressureEnergyHatches.isEmpty()) {
                drainAmountEU /= TICK_INTERVAL;
                presChange = Math.pow(drainAmountEU, 0.7) * (isCompressorModule ? 1 : -1);
                currentPressure = Math.max(currentPressure + presChange, 0);
                if (currentPressure == 0) presChange = 0;
                drainAmountEU = 0;
            }
            double minLeakCoeff = leakCoeff;
            for (MTEHatchInput hatch : mLubricantInputHatches) {
                FluidStack fluid = hatch.mFluid;
                if (fluid != null && fluid.amount >= LUBRICANT_DRAIN_AMOUNT) {
                    String type = fluid.getUnlocalizedName();
                    minLeakCoeff = Math.min(minLeakCoeff, getLeakPercentage(type));
                    hatch.drain((int) LUBRICANT_DRAIN_AMOUNT, true);
                }
            }
            leakCoeff = minLeakCoeff;
        }
        pressureLeakValue = (currentPressure * leakCoeff + ambientPressure * (1 - leakCoeff)) - currentPressure;
        currentPressure += pressureLeakValue + deltaPressure;
    }

    private void applyRecipeConditions() {
        currentTemp += deltaTemp;
        if (mMaxProgresstime != 0 && (Math.abs(currentTemp - requiredTemp) > tempThreshold
            || Math.abs(currentPressure - requiredPressure) > pressureThreshold)) {
            stopMachine(SimpleShutDownReason.ofCritical("conditions_range"));
            deltaPressure = 0;
            deltaTemp = 0;
        }
    }
}
