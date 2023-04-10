package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.recipe.TT_recipe.E_RECIPE_ID;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_crafting.crafter;
import static com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine.machine;
import static com.github.technus.tectech.util.CommonValues.TEC_MARK_SHORT;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.github.technus.tectech.util.CommonValues.VN;
import static com.github.technus.tectech.util.TT_Utility.areBitsSet;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMDefinitionStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMPrimitiveDefinition;
import com.github.technus.tectech.recipe.TT_recipe;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.item.ElementalDefinitionScanStorage_EM;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_scanner extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    // region variables
    public static final int SCAN_DO_NOTHING = 0, SCAN_GET_NOMENCLATURE = 1 << 0, SCAN_GET_DEPTH_LEVEL = 1 << 1,
            SCAN_GET_AMOUNT = 1 << 2, SCAN_GET_CHARGE = 1 << 3, SCAN_GET_MASS = 1 << 4, SCAN_GET_ENERGY = 1 << 5,
            SCAN_GET_ENERGY_LEVEL = 1 << 6, SCAN_GET_TIMESPAN_INFO = 1 << 7, SCAN_GET_ENERGY_STATES = 1 << 8,
            SCAN_GET_COLORABLE = 1 << 9, SCAN_GET_COLOR_VALUE = 1 << 10, SCAN_GET_AGE = 1 << 11,
            SCAN_GET_TIMESPAN_MULT = 1 << 12, SCAN_GET_CLASS_TYPE = 1 << 13, SCAN_GET_TOO_BIG = 1 << 14; // should be
                                                                                                         // the sum of
                                                                                                         // all flags +1

    private TT_recipe.TT_EMRecipe.TT_EMRecipe eRecipe;
    private EMDefinitionStack objectResearched;
    private EMInstanceStackMap objectsScanned;
    private String machineType;
    private long computationRemaining, computationRequired;
    private int[] scanComplexity;

    private String clientLocale = "en_US";
    // endregion

    // region structure
    private static final String[] description = new String[] {
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.scanner.hint.0"), // 1 - Classic Hatches or High Power
                                                                                 // Casing
            translateToLocal("gt.blockmachines.multimachine.em.scanner.hint.1"), // 2 - Elemental Input Hatches or
                                                                                 // Molecular Casing
            translateToLocal("gt.blockmachines.multimachine.em.scanner.hint.2"), // 3 - Elemental Output Hatches or
                                                                                 // Molecular Casing
            translateToLocal("gt.blockmachines.multimachine.em.scanner.hint.3"), // 4 - Elemental Overflow Hatches or
                                                                                 // Molecular
            // Casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_scanner> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_scanner>builder()
            .addShape(
                    "main",
                    transpose(
                            new String[][] { { "CCCCC", "BBBBB", "BBDBB", "BDDDB", "BDDDB", "BDDDB", "BBDBB", "EEEEE" },
                                    { "CAAAC", "BBBBB", "BDDDB", "D---D", "D---D", "D---D", "BDDDB", "EBBBE" },
                                    { "CA~AC", "BBBBB", "DDDDD", "D---D", "D- -D", "D---D", "DDGDD", "EBFBE" },
                                    { "CAAAC", "BBBBB", "BDDDB", "D---D", "D---D", "D---D", "BDDDB", "EBBBE" },
                                    { "CCCCC", "BBBBB", "BBDBB", "BDDDB", "BDDDB", "BDDDB", "BBDBB", "EEEEE" } }))
            .addElement('A', ofBlock(sBlockCasingsTT, 0)).addElement('B', ofBlock(sBlockCasingsTT, 4))
            .addElement('D', ofBlock(QuantumGlassBlock.INSTANCE, 0))
            .addElement(
                    'C',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_scanner::addClassicToMachineList,
                            textureOffset,
                            1,
                            sBlockCasingsTT,
                            0))
            .addElement(
                    'F',
                    ofHatchAdder(GT_MetaTileEntity_EM_scanner::addElementalInputToMachineList, textureOffset + 4, 2))
            .addElement(
                    'G',
                    ofHatchAdder(GT_MetaTileEntity_EM_scanner::addElementalOutputToMachineList, textureOffset + 4, 3))
            .addElement(
                    'E',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_scanner::addElementalMufflerToMachineList,
                            textureOffset + 4,
                            4,
                            sBlockCasingsTT,
                            4))
            .build();
    // endregion

    // region parameters
    private static final INameFunction<GT_MetaTileEntity_EM_scanner> CONFIG_NAME = (base, p) -> "Config at Depth: "
            + (p.hatchId() * 2 + p.parameterId());
    private static final IStatusFunction<GT_MetaTileEntity_EM_scanner> CONFIG_STATUS = (base, p) -> {
        double v = p.get();
        if (Double.isNaN(v)) {
            return LedStatus.STATUS_WRONG;
        }
        v = (int) v;
        if (v == 0) return LedStatus.STATUS_NEUTRAL;
        if (v >= SCAN_GET_TOO_BIG) return LedStatus.STATUS_TOO_HIGH;
        if (v < 0) return LedStatus.STATUS_TOO_LOW;
        return LedStatus.STATUS_OK;
    };
    protected Parameters.Group.ParameterIn[] scanConfiguration;
    // endregion

    public GT_MetaTileEntity_EM_scanner(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        eDismantleBoom = true;
    }

    public GT_MetaTileEntity_EM_scanner(String aName) {
        super(aName);
        eDismantleBoom = true;
    }

    private void addComputationRequirements(int depthPlus, int capabilities) {
        if (areBitsSet(SCAN_GET_NOMENCLATURE, capabilities)) {
            computationRequired += depthPlus * 3L;
            eRequiredData += depthPlus;
        }
        if (areBitsSet(SCAN_GET_DEPTH_LEVEL, capabilities)) {
            computationRequired += depthPlus * 10L;
            eRequiredData += depthPlus;
        }
        if (areBitsSet(SCAN_GET_AMOUNT, capabilities)) {
            computationRequired += depthPlus * 64L;
            eRequiredData += depthPlus * 8L;
        }
        if (areBitsSet(SCAN_GET_CHARGE, capabilities)) {
            computationRequired += depthPlus * 128L;
            eRequiredData += depthPlus * 4L;
        }
        if (areBitsSet(SCAN_GET_MASS, capabilities)) {
            computationRequired += depthPlus * 256L;
            eRequiredData += depthPlus * 4L;
        }
        if (areBitsSet(SCAN_GET_ENERGY, capabilities)) {
            computationRequired += depthPlus * 256L;
            eRequiredData += depthPlus * 16L;
        }
        if (areBitsSet(SCAN_GET_ENERGY_LEVEL, capabilities)) {
            computationRequired += depthPlus * 256L;
            eRequiredData += depthPlus * 16L;
        }
        if (areBitsSet(SCAN_GET_TIMESPAN_INFO, capabilities)) {
            computationRequired += depthPlus * 1024L;
            eRequiredData += depthPlus * 32L;
        }
        if (areBitsSet(SCAN_GET_ENERGY_STATES, capabilities)) {
            computationRequired += depthPlus * 2048L;
            eRequiredData += depthPlus * 32L;
        }
        if (areBitsSet(SCAN_GET_COLORABLE, capabilities)) {
            computationRequired += depthPlus * 512L;
            eRequiredData += depthPlus * 48L;
        }
        if (areBitsSet(SCAN_GET_COLOR_VALUE, capabilities)) {
            computationRequired += depthPlus * 1024L;
            eRequiredData += depthPlus * 48L;
        }
        if (areBitsSet(SCAN_GET_AGE, capabilities)) {
            computationRequired += depthPlus * 2048L;
            eRequiredData += depthPlus * 64L;
        }
        if (areBitsSet(SCAN_GET_TIMESPAN_MULT, capabilities)) {
            computationRequired += depthPlus * 2048L;
            eRequiredData += depthPlus * 64L;
        }
        if (areBitsSet(SCAN_GET_CLASS_TYPE, capabilities)) {
            computationRequired += depthPlus * 2L;
            eRequiredData += depthPlus;
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_scanner(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if (!structureCheck_EM("main", 2, 2, 0)) {
            return false;
        }
        return eInputHatches.size() == 1 && eOutputHatches.size() == 1
                && eOutputHatches.get(0).getBaseMetaTileEntity().getFrontFacing()
                        == iGregTechTileEntity.getFrontFacing();
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        eRecipe = null;
        if (!eInputHatches.isEmpty() && eInputHatches.get(0).getContentHandler().hasStacks()
                && !eOutputHatches.isEmpty()) {
            EMInstanceStackMap researchEM = eInputHatches.get(0).getContentHandler();
            if (ItemList.Tool_DataOrb.isStackEqual(itemStack, false, true)) {
                GT_Recipe scannerRecipe = null;
                for (EMInstanceStack stackEM : researchEM.valuesToArray()) {
                    objectsScanned = null;
                    eRecipe = TT_recipe.TT_Recipe_Map_EM.sMachineRecipesEM.findRecipe(stackEM.getDefinition());
                    if (eRecipe != null) {
                        scannerRecipe = eRecipe.scannerRecipe;
                        machineType = machine;
                        objectResearched = new EMDefinitionStack(stackEM.getDefinition(), 1);
                        // cleanMassEM_EM(objectResearched.getMass());
                        researchEM.removeKey(objectResearched.getDefinition());
                        break;
                    }
                    eRecipe = TT_recipe.TT_Recipe_Map_EM.sCrafterRecipesEM.findRecipe(stackEM.getDefinition());
                    if (eRecipe != null) {
                        scannerRecipe = eRecipe.scannerRecipe;
                        machineType = crafter;
                        objectResearched = new EMDefinitionStack(stackEM.getDefinition(), 1);
                        // cleanMassEM_EM(objectResearched.getMass());
                        researchEM.removeKey(objectResearched.getDefinition());
                        break;
                    }
                    cleanStackEM_EM(stackEM);
                    researchEM.removeKey(stackEM.getDefinition());
                }
                if (eRecipe != null && scannerRecipe != null) { // todo make sure it werks
                    computationRequired = computationRemaining = scannerRecipe.mDuration * 20L;
                    mMaxProgresstime = 20; // const
                    mEfficiencyIncrease = 10000;
                    eRequiredData = (short) (scannerRecipe.mSpecialValue >>> 16);
                    eAmpereFlow = (short) (scannerRecipe.mSpecialValue & 0xFFFF);
                    mEUt = scannerRecipe.mEUt;
                    return true;
                }
            } else if (CustomItemList.scanContainer.isStackEqual(itemStack, false, true)) {
                eRecipe = null;
                if (researchEM.hasStacks()) {
                    objectsScanned = researchEM.takeAll();
                    cleanMassEM_EM(objectsScanned.getMass());

                    computationRequired = 0;
                    eRequiredData = 0;
                    eAmpereFlow = objectsScanned.size() + TecTech.RANDOM.next(objectsScanned.size());
                    mEUt = -(int) V[8];

                    // get depth scan complexity array
                    {
                        int[] scanComplexityTemp = new int[20];
                        for (int i = 0; i < 20; i++) {
                            scanComplexityTemp[i] = (int) scanConfiguration[i].get();
                        }
                        int maxDepth = 0;
                        for (int i = 0; i < 20; i++) {
                            if (scanComplexityTemp[i] != SCAN_DO_NOTHING) {
                                maxDepth = i;
                                addComputationRequirements(i + 1, scanComplexityTemp[i]);
                            }
                        }
                        maxDepth += 1; // from index to len
                        scanComplexity = new int[maxDepth];
                        System.arraycopy(scanComplexityTemp, 0, scanComplexity, 0, maxDepth);
                    }

                    computationRemaining = computationRequired *= 20;
                    mMaxProgresstime = 20; // const
                    mEfficiencyIncrease = 10000;
                    return true;
                }
            }
        }
        objectResearched = null;
        computationRemaining = 0;
        return false;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (eRecipe != null && ItemList.Tool_DataOrb.isStackEqual(mInventory[1], false, true)) {

            mInventory[1].setStackDisplayName(
                    GT_LanguageManager.getTranslation(eRecipe.mOutputs[0].getDisplayName()) + ' '
                            + machineType
                            + " Construction Data");
            NBTTagCompound tNBT = mInventory[1].getTagCompound(); // code above makes it not null

            tNBT.setString("eMachineType", machineType);
            tNBT.setTag(E_RECIPE_ID, objectResearched.toNBT(TecTech.definitionsRegistry));
            tNBT.setString(
                    "author",
                    TEC_MARK_SHORT + EnumChatFormatting.WHITE + ' ' + machineType + " EM Recipe Generator");
        } else if (objectsScanned != null && CustomItemList.scanContainer.isStackEqual(mInventory[1], false, true)) {
            ElementalDefinitionScanStorage_EM.setContent(mInventory[1], objectsScanned, scanComplexity);
        }
        objectResearched = null;
        computationRemaining = 0;
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.scanner.name")) // Machine Type: Elemental
                                                                                             // Scanner
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.scanner.desc.0")) // Controller block of the
                                                                                              // Elemental Scanner
                .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
                .addSeparator().beginStructureBlock(5, 5, 8, false)
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalInput"),
                        translateToLocal("tt.keyword.Structure.BackCenter"),
                        2) // Elemental Input Hatch: Back Center
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalOutput"),
                        translateToLocal("tt.keyword.Structure.AnyMolecularCasing3D"),
                        3) // Elemental Output Hatch: Any Molecular Casing with 3 dot
                .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing1D"), 1) // Energy Hatch: Any
                                                                                                  // High Power Casing
                                                                                                  // with 1 dot
                .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing1D"), 1) // Maintenance
                                                                                                       // Hatch: Any
                                                                                                       // High Power
                                                                                                       // Casing with 1
                                                                                                       // dot
                .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch_Energy tHatch : mEnergyHatches) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : eEnergyMulti) {
            if (GT_MetaTileEntity_MultiBlockBase.isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }

        return new String[] { translateToLocalFormatted("tt.keyphrase.Energy_Hatches", clientLocale) + ":",
                EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy)
                        + EnumChatFormatting.RESET
                        + " EU / "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(maxEnergy)
                        + EnumChatFormatting.RESET
                        + " EU",
                (mEUt <= 0 ? translateToLocalFormatted("tt.keyphrase.Probably_uses", clientLocale) + ": "
                        : translateToLocalFormatted("tt.keyphrase.Probably_makes", clientLocale) + ": ")
                        + EnumChatFormatting.RED
                        + GT_Utility.formatNumbers(Math.abs(mEUt))
                        + EnumChatFormatting.RESET
                        + " EU/t "
                        + translateToLocalFormatted("tt.keyword.at", clientLocale)
                        + " "
                        + EnumChatFormatting.RED
                        + GT_Utility.formatNumbers(eAmpereFlow)
                        + EnumChatFormatting.RESET
                        + " A",
                translateToLocalFormatted("tt.keyphrase.Tier_Rating", clientLocale) + ": "
                        + EnumChatFormatting.YELLOW
                        + VN[getMaxEnergyInputTier_EM()]
                        + EnumChatFormatting.RESET
                        + " / "
                        + EnumChatFormatting.GREEN
                        + VN[getMinEnergyInputTier_EM()]
                        + EnumChatFormatting.RESET
                        + " "
                        + translateToLocalFormatted("tt.keyphrase.Amp_Rating", clientLocale)
                        + ": "
                        + EnumChatFormatting.GREEN
                        + GT_Utility.formatNumbers(eMaxAmpereFlow)
                        + EnumChatFormatting.RESET
                        + " A",
                translateToLocalFormatted("tt.keyword.Problems", clientLocale) + ": "
                        + EnumChatFormatting.RED
                        + (getIdealStatus() - getRepairStatus())
                        + EnumChatFormatting.RESET
                        + " "
                        + translateToLocalFormatted("tt.keyword.Efficiency", clientLocale)
                        + ": "
                        + EnumChatFormatting.YELLOW
                        + mEfficiency / 100.0F
                        + EnumChatFormatting.RESET
                        + " %",
                translateToLocalFormatted("tt.keyword.PowerPass", clientLocale) + ": "
                        + EnumChatFormatting.BLUE
                        + ePowerPass
                        + EnumChatFormatting.RESET
                        + " "
                        + translateToLocalFormatted("tt.keyword.SafeVoid", clientLocale)
                        + ": "
                        + EnumChatFormatting.BLUE
                        + eSafeVoid,
                translateToLocalFormatted("tt.keyphrase.Computation_Available", clientLocale) + ": "
                        + EnumChatFormatting.GREEN
                        + GT_Utility.formatNumbers(eAvailableData)
                        + EnumChatFormatting.RESET
                        + " / "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(eRequiredData)
                        + EnumChatFormatting.RESET,
                translateToLocalFormatted("tt.keyphrase.Computation_Remaining", clientLocale) + ":",
                EnumChatFormatting.GREEN + GT_Utility.formatNumbers(computationRemaining / 20L)
                        + EnumChatFormatting.RESET
                        + " / "
                        + EnumChatFormatting.YELLOW
                        + GT_Utility.formatNumbers(computationRequired / 20L) };
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
    }

    @Override
    protected void parametersInstantiation_EM() {
        scanConfiguration = new Parameters.Group.ParameterIn[20];
        for (int i = 0; i < 10; i++) {
            Parameters.Group hatch = parametrization.getGroup(i);
            scanConfiguration[i * 2] = hatch.makeInParameter(0, 0, CONFIG_NAME, CONFIG_STATUS);
            scanConfiguration[i * 2 + 1] = hatch.makeInParameter(1, 0, CONFIG_NAME, CONFIG_STATUS);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("eComputationRemaining", computationRemaining);
        aNBT.setLong("eComputationRequired", computationRequired);
        if (objectResearched != null) {
            aNBT.setTag("eObject", objectResearched.toNBT(TecTech.definitionsRegistry));
        } else {
            aNBT.removeTag("eObject");
        }
        if (scanComplexity != null) {
            aNBT.setIntArray("eScanComplexity", scanComplexity);
        } else {
            aNBT.removeTag("eScanComplexity");
        }
        if (objectsScanned != null) {
            aNBT.setTag("eScanObjects", objectsScanned.toNBT(TecTech.definitionsRegistry));
        } else {
            aNBT.removeTag("eScanObjects");
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        computationRemaining = aNBT.getLong("eComputationRemaining");
        computationRequired = aNBT.getLong("eComputationRequired");
        if (aNBT.hasKey("eObject")) {
            objectResearched = EMDefinitionStack.fromNBT(TecTech.definitionsRegistry, aNBT.getCompoundTag("eObject"));
            if (objectResearched.getDefinition() == EMPrimitiveDefinition.nbtE__) {
                objectResearched = null;
            }
        } else {
            objectResearched = null;
        }
        if (aNBT.hasKey("eScanComplexity")) {
            scanComplexity = aNBT.getIntArray("eScanComplexity");
        } else {
            scanComplexity = null;
        }
        try {
            if (aNBT.hasKey("eScanObjects")) {
                objectsScanned = EMInstanceStackMap
                        .fromNBT(TecTech.definitionsRegistry, aNBT.getCompoundTag("eScanObjects"));
            }
        } catch (EMException e) {
            objectsScanned = new EMInstanceStackMap();
        }
    }

    @Override
    public void stopMachine() {
        super.stopMachine();
        computationRequired = computationRemaining = 0;
        objectResearched = null;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (computationRemaining > 0 && objectResearched != null) {
                eRecipe = null;
                if (ItemList.Tool_DataOrb.isStackEqual(mInventory[1], false, true)) {
                    eRecipe = TT_recipe.TT_Recipe_Map_EM.sMachineRecipesEM.findRecipe(objectResearched.getDefinition());
                    if (eRecipe != null) {
                        machineType = machine;
                    } else {
                        eRecipe = TT_recipe.TT_Recipe_Map_EM.sCrafterRecipesEM
                                .findRecipe(objectResearched.getDefinition());
                        if (eRecipe != null) {
                            machineType = crafter;
                        }
                    }
                }
                if (eRecipe == null) {
                    objectResearched = null;
                    eRequiredData = 0;
                    computationRequired = computationRemaining = 0;
                    mMaxProgresstime = 0;
                    mEfficiencyIncrease = 0;
                }
            }
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (computationRemaining <= 0) {
            computationRemaining = 0;
            mProgresstime = mMaxProgresstime;
            return true;
        } else {
            computationRemaining -= eAvailableData;
            mProgresstime = 1;
            return super.onRunningTick(aStack);
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        super.onRightclick(aBaseMetaTileEntity, aPlayer);

        if (!aBaseMetaTileEntity.isClientSide() && aPlayer instanceof EntityPlayerMP) {
            try {
                EntityPlayerMP player = (EntityPlayerMP) aPlayer;
                clientLocale = (String) FieldUtils.readField(player, "translator", true);
            } catch (Exception e) {
                clientLocale = "en_US";
            }
        } else {
            return true;
        }
        return true;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isActive() && (aTick & 0x2) == 0 && aBaseMetaTileEntity.isClientSide()) {
            int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX * 4
                    + aBaseMetaTileEntity.getXCoord();
            int yDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetY * 4
                    + aBaseMetaTileEntity.getYCoord();
            int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ * 4
                    + aBaseMetaTileEntity.getZCoord();
            aBaseMetaTileEntity.getWorld().markBlockRangeForRenderUpdate(xDir, yDir, zDir, xDir, yDir, zDir);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        structureBuild_EM("main", 2, 2, 0, trigger, hintsOnly);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_scanner> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}
