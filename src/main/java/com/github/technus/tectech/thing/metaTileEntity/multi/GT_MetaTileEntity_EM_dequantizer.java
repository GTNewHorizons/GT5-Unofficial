package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition.STABLE_RAW_LIFE_TIME;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition.refMass;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition.refUnstableMass;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.IEMStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMDequantizationInfo;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.OreDictionaryStack;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_dequantizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    // region structure
    // use multi A energy inputs, use less power the longer it runs

    private static final IStructureDefinition<GT_MetaTileEntity_EM_dequantizer> STRUCTURE_DEFINITION =
            IStructureDefinition.<GT_MetaTileEntity_EM_dequantizer>builder()
                    .addShape("main", transpose(new String[][] {
                        {"CCC", "ABA", "EEE", "BDB"},
                        {"C~C", "BBB", "EBE", "DFD"},
                        {"CCC", "ABA", "EEE", "BDB"}
                    }))
                    .addElement('A', ofBlock(sBlockCasingsTT, 0))
                    .addElement('B', ofBlock(sBlockCasingsTT, 4))
                    .addElement('D', ofBlock(QuantumGlassBlock.INSTANCE, 0))
                    .addElement(
                            'C',
                            ofHatchAdderOptional(
                                    GT_MetaTileEntity_EM_dequantizer::addClassicToMachineList,
                                    textureOffset,
                                    1,
                                    sBlockCasingsTT,
                                    0))
                    .addElement(
                            'F',
                            ofHatchAdder(
                                    GT_MetaTileEntity_EM_dequantizer::addElementalInputToMachineList,
                                    textureOffset + 4,
                                    2))
                    .addElement(
                            'E',
                            ofHatchAdderOptional(
                                    GT_MetaTileEntity_EM_dequantizer::addElementalMufflerToMachineList,
                                    textureOffset + 4,
                                    3,
                                    sBlockCasingsTT,
                                    4))
                    .build();

    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal(
                "gt.blockmachines.multimachine.em.emtomatter.hint.0"), // 1 - Classic Hatches or High Power Casing"
        translateToLocal("gt.blockmachines.multimachine.em.emtomatter.hint.1"), // 2 - Elemental Input Hatch
        translateToLocal(
                "gt.blockmachines.multimachine.em.emtomatter.hint.2"), // 3 - Elemental Overflow Hatches or Molecular
        // Casing
    };
    // endregion

    public GT_MetaTileEntity_EM_dequantizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_dequantizer(String aName) {
        super(aName);
    }

    private void startRecipe(IEMStack from, long energy) {
        mMaxProgresstime = 20;
        mEfficiencyIncrease = 10000;
        double mass = from.getMass();
        double euMult = Math.abs(mass / refMass);
        eAmpereFlow = (int) Math.ceil(Math.sqrt(Math.sqrt(euMult)));
        if (mass > refUnstableMass || from.getDefinition().getRawTimeSpan(energy) < STABLE_RAW_LIFE_TIME) {
            mEUt = (int) -V[8];
        } else {
            mEUt = (int) -V[6];
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_dequantizer(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 1, 1, 0);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_InputElemental in : eInputHatches) {
            EMInstanceStackMap map = in.getContentHandler();
            for (EMInstanceStack stack : map.valuesToArray()) {
                EMDequantizationInfo emDequantizationInfo =
                        TecTech.transformationInfo.getInfoMap().get(stack.getDefinition());
                if (emDequantizationInfo != null
                        && emDequantizationInfo.getStack() != null
                        && map.removeAllAmounts(emDequantizationInfo.getInput())) {
                    Object out = emDequantizationInfo.getStack();
                    if (out instanceof ItemStack) {
                        mOutputItems = new ItemStack[] {emDequantizationInfo.getItem()};
                    } else if (out instanceof FluidStack) {
                        mOutputFluids = new FluidStack[] {emDequantizationInfo.getFluid()};
                    } else if (out instanceof OreDictionaryStack) {
                        ArrayList<ItemStack> items = OreDictionary.getOres(OreDictionary.getOreName(
                                emDequantizationInfo.getOre().getOreId()));
                        if (items != null && !items.isEmpty()) {
                            mOutputItems = new ItemStack[] {items.get(0)};
                        }
                    }
                    startRecipe(emDequantizationInfo.getInput(), stack.getEnergy());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal(
                        "gt.blockmachines.multimachine.em.emtomatter.name")) // Machine Type: Matter Dequantizer
                .addInfo(translateToLocal(
                        "gt.blockmachines.multimachine.em.emtomatter.desc.0")) // Controller block of the Matter
                // Dequantizer
                .addInfo(translateToLocal(
                        "gt.blockmachines.multimachine.em.emtomatter.desc.1")) // Transforms elemental matter
                // back into items
                .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
                .addSeparator()
                .beginStructureBlock(3, 3, 4, false)
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalInput"),
                        translateToLocal("tt.keyword.Structure.BackCenter"),
                        2) // Elemental Input Hatch: Back center
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalOverflow"),
                        translateToLocal("tt.keyword.Structure.AnyOuterMolecularCasing3rd"),
                        3) // Elemental Overflow Hatch: Any outer Molecular Casing on the 3rd slice
                .addOutputBus(
                        translateToLocal("tt.keyword.Structure.AnyHighPowerCasingFront"),
                        1) // Output Bus: Any High Power Casing on the front side
                .addEnergyHatch(
                        translateToLocal("tt.keyword.Structure.AnyHighPowerCasingFront"),
                        1) // Energy Hatch: Any High Power Casing on the front side
                .addMaintenanceHatch(
                        translateToLocal("tt.keyword.Structure.AnyHighPowerCasingFront"),
                        1) // Maintenance Hatch: Any High Power Casing on the front side
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
    public IStructureDefinition<GT_MetaTileEntity_EM_dequantizer> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}
