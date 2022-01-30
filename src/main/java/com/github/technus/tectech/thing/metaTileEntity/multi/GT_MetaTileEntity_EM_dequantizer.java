package com.github.technus.tectech.thing.metaTileEntity.multi;

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
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;

import static com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition.STABLE_RAW_LIFE_TIME;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition.refMass;
import static com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition.refUnstableMass;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_dequantizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region structure
    //use multi A energy inputs, use less power the longer it runs
    private static final IStructureDefinition<GT_MetaTileEntity_EM_dequantizer> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_dequantizer>builder()
            .addShape("main", new String[][]{
                    {"&&&", "&~&", "&&&",},
                    {"010", "111", "010",},
                    {"$$$", "$1$", "$$$",},
                    {"121", "2!2", "121",},
            })
            .addElement('0', ofBlock(sBlockCasingsTT, 0))
            .addElement('1', ofBlock(sBlockCasingsTT, 4))
            .addElement('2', ofBlockAnyMeta(QuantumGlassBlock.INSTANCE))
            .addElement('&', ofHatchAdderOptional(GT_MetaTileEntity_EM_dequantizer::addClassicToMachineList, textureOffset, 1, sBlockCasingsTT, 0))
            .addElement('!', ofHatchAdder(GT_MetaTileEntity_EM_dequantizer::addElementalInputToMachineList, textureOffset + 4, 2))
            .addElement('$', ofHatchAdderOptional(GT_MetaTileEntity_EM_dequantizer::addElementalMufflerToMachineList, textureOffset + 4, 3,sBlockCasingsTT,4))
            .build();

    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.emtomatter.hint.0"),//1 - Classic Hatches or High Power Casing"
            translateToLocal("gt.blockmachines.multimachine.em.emtomatter.hint.1"),//2 - Elemental Input Hatch
            translateToLocal("gt.blockmachines.multimachine.em.emtomatter.hint.2"),//3 - Elemental Overflow Hatches or Molecular Casing
    };
    //endregion

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
                EMDequantizationInfo emDequantizationInfo = TecTech.transformationInfo.getInfoMap().get(stack.getDefinition());
                if(emDequantizationInfo!=null && emDequantizationInfo.getStack()!=null && map.removeAllAmounts(emDequantizationInfo.getInput())){
                    Object out=emDequantizationInfo.getStack();
                    if(out instanceof ItemStack){
                        mOutputItems=new ItemStack[]{emDequantizationInfo.getItem()};
                    }else if(out instanceof FluidStack){
                        mOutputFluids=new FluidStack[]{emDequantizationInfo.getFluid()};
                    }else if(out instanceof OreDictionaryStack){
                        ArrayList<ItemStack> items = OreDictionary.getOres(OreDictionary.getOreName(emDequantizationInfo.getOre().getOreId()));
                        if (items != null && !items.isEmpty()) {
                            mOutputItems = new ItemStack[]{items.get(0)};
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
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.emtomatter.desc.0"),//Transform quantum form back to...
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.emtomatter.desc.1")//regular one, but why?
        };
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
}