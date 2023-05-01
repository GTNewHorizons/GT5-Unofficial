package com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi;

import static com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.EssentiaCompat.essentiaContainerCompat;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_ITEM_DIMINISHED;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofTileAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_quantizer;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import thaumcraft.api.aspects.Aspect;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_essentiaDequantizer extends GT_MetaTileEntity_MultiblockBase_EM
        implements IConstructable {

    // region structure
    // use multi A energy inputs, use less power the longer it runs
    private static final String[] description = new String[] {
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.hint.0"), // 1 - Classic Hatches or High
                                                                                      // Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.hint.1"), // 2 - Elemental Input Hatch
            translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.hint.2"), // 3 - Elemental Overflow Hatches
                                                                                      // or Elemental
            // Casing
            translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.hint.3"), // General - Some sort of Essentia
                                                                                      // Storage
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_essentiaDequantizer> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_essentiaDequantizer>builder()
            .addShape(
                    "main",
                    new String[][] { { "DDD", "D~D", "DDD" }, { "E E", " * ", "E E" }, { "ABA", "BCB", "ABA" },
                            { "FFF", "FBF", "FFF" }, { "BEB", "EGE", "BEB" } })
            .addElement('A', ofBlock(sBlockCasingsTT, 0)).addElement('B', ofBlock(sBlockCasingsTT, 4))
            .addElement('C', ofBlock(sBlockCasingsTT, 8))
            .addElement(
                    'D',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_essentiaDequantizer::addClassicToMachineList,
                            textureOffset,
                            1,
                            sBlockCasingsTT,
                            0))
            .addElement('E', ofBlock(QuantumGlassBlock.INSTANCE, 0))
            .addElement(
                    'F',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_essentiaDequantizer::addElementalMufflerToMachineList,
                            textureOffset + 4,
                            3,
                            sBlockCasingsTT,
                            4))
            .addElement(
                    'G',
                    ofHatchAdder(
                            GT_MetaTileEntity_EM_essentiaDequantizer::addElementalInputToMachineList,
                            textureOffset + 4,
                            2))
            .addElement('*', ofTileAdder(essentiaContainerCompat::check, StructureLibAPI.getBlockHint(), 12)).build();

    private String outputEssentiaName = "";
    // endregion

    public GT_MetaTileEntity_EM_essentiaDequantizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_essentiaDequantizer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_essentiaDequantizer(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 1, 1, 0);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        TileEntity container = essentiaContainerCompat.getContainer(this);
        if (eInputHatches.size() < 1 || container == null) {
            stopMachine();
            return false;
        }

        EMInstanceStackMap inputHatchContainer = eInputHatches.get(0).getContentHandler();
        if (inputHatchContainer == null || !inputHatchContainer.hasStacks()) {
            return false;
        }

        EMInstanceStack stack = inputHatchContainer.getRandom();
        if (stack.getAmount() < EM_COUNT_PER_ITEM_DIMINISHED) {
            cleanStackEM_EM(inputHatchContainer.removeKey(stack.getDefinition()));
            mEUt = (int) -V[6];
        } else {
            outputEssentiaName = essentiaContainerCompat.getEssentiaName(stack.getDefinition());
            Aspect aspect = Aspect.getAspect(outputEssentiaName);
            if (aspect == null) {
                outputEssentiaName = "";
                cleanStackEM_EM(inputHatchContainer.removeKey(stack.getDefinition()));
                mEUt = (int) -V[7];
            } else {
                inputHatchContainer.removeAmount(stack.getDefinition().getStackForm(EM_COUNT_PER_MATERIAL_AMOUNT));
                if (aspect.isPrimal()) {
                    mEUt = (int) -V[8];
                } else {
                    mEUt = (int) -V[10];
                }
            }
        }
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        eAmpereFlow = 1;
        return true;
    }

    @Override
    public void outputAfterRecipe_EM() {
        TileEntity container = essentiaContainerCompat.getContainer(this);
        if (container == null) {
            stopMachine();
        } else {
            if (!essentiaContainerCompat.putInContainer(container, outputEssentiaName)) {
                stopMachine();
            }
        }
        outputEssentiaName = "";
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.name")) // Machine Type:
                                                                                                  // Essentia
                                                                                                  // Dequantizer
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.desc.0")) // Controller block
                                                                                                   // of the
                // Essentia Dequantizer
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.desc.1")) // Transforms
                                                                                                   // elemental matter
                // back into essentia
                .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
                .addSeparator().beginStructureBlock(3, 3, 5, false)
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalInput"),
                        translateToLocal("tt.keyword.Structure.BackCenter"),
                        2) // Elemental Input Hatch: Back center
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalOverflow"),
                        translateToLocal("tt.keyword.Structure.AnyOuterMolecularCasing4th"),
                        3) // Elemental Overflow Hatch: Any outer Molecular Casing on the 4th slice
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.EssentiaStorage"),
                        translateToLocal("tt.keyword.Structure.AnyHighPowerCasingFront"),
                        1) // Essentia Storage: Any High Power Casing on the front side
                .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasingFront"), 1) // Energy Hatch:
                                                                                                     // Any High Power
                                                                                                     // Casing on the
                                                                                                     // front side
                .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasingFront"), 1) // Maintenance
                                                                                                          // Hatch: Any
                                                                                                          // High Power
                                                                                                          // Casing on
                                                                                                          // the front
                                                                                                          // side
                .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return GT_MetaTileEntity_EM_quantizer.activitySound;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 1, 1, 0, stackSize, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setString("eOutputEssentia", outputEssentiaName);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        outputEssentiaName = aNBT.getString("eOutputEssentia");
    }

    @Override
    public IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }
}
