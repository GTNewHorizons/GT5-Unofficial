package com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
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
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.EssentiaCompat.essentiaContainerCompat;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_essentiaQuantizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region structure
    //use multi A energy inputs, use less power the longer it runs
    private static final IStructureDefinition<GT_MetaTileEntity_EM_essentiaQuantizer> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_essentiaQuantizer>builder()
            .addShape("main", new String[][]{
                    {"&&&", "&~&", "&&&",},
                    {"0 0", " * ", "0 0",},
                    {"121", "232", "121",},
                    {"$$$", "$1$", "$$$",},
                    {"010", "1!1", "010",},})
            .addElement('0', ofBlockAnyMeta(QuantumGlassBlock.INSTANCE))
            .addElement('1', ofBlock(sBlockCasingsTT, 4))
            .addElement('2', ofBlock(sBlockCasingsTT, 0))
            .addElement('3', ofBlock(sBlockCasingsTT, 8))
            .addElement('&', ofHatchAdderOptional(GT_MetaTileEntity_EM_essentiaQuantizer::addClassicToMachineList, textureOffset, 1, sBlockCasingsTT, 0))
            .addElement('!', ofHatchAdderOptional(GT_MetaTileEntity_EM_essentiaQuantizer::addElementalOutputToMachineList, textureOffset + 4, 2, sBlockCasingsTT, 4))
            .addElement('$', ofHatchAdderOptional(GT_MetaTileEntity_EM_essentiaQuantizer::addElementalMufflerToMachineList, textureOffset + 4, 2, sBlockCasingsTT, 4))
            .addElement('*', ofTileAdder(essentiaContainerCompat::check, StructureLibAPI.getBlockHint(),12))
            .build();

    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.essentiatoem.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.essentiatoem.hint.1"),//2 - Elemental Output Hatch
            translateToLocal("gt.blockmachines.multimachine.em.essentiatoem.hint.2"),//3 - Elemental Overflow Hatches or Elemental Casing
            translateToLocal("gt.blockmachines.multimachine.em.essentiatoem.hint.3"),//General - Some sort of Essentia Storage
    };
    //endregion

    public GT_MetaTileEntity_EM_essentiaQuantizer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_essentiaQuantizer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_essentiaQuantizer(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return structureCheck_EM("main", 1, 1, 0);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        IEMDefinition definition = essentiaContainerCompat.getFromContainer(essentiaContainerCompat.getContainer(this));
        if (definition != null) {
            mMaxProgresstime = 20;
            mEfficiencyIncrease = 10000;
            eAmpereFlow = 1;
            outputEM = new EMInstanceStackMap[]{
                    new EMInstanceStackMap(new EMInstanceStack(definition, EM_COUNT_PER_MATERIAL_AMOUNT))
            };
            if (definition instanceof EMPrimalAspectDefinition) {
                mEUt = (int) -V[8];
            } else {
                mEUt = (int) -V[10];
            }
            return true;
        }
        return false;
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (eOutputHatches.size() < 1) {
            stopMachine();
            return;
        }
        eOutputHatches.get(0).getContentHandler().putUnifyAll(outputEM[0]);
        outputEM = null;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.essentiatoem.desc.0"),//Conveniently convert regular stuff into quantum form.
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.essentiatoem.desc.1")//To make it more inconvenient.
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return GT_MetaTileEntity_EM_quantizer.activitySound;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        IGregTechTileEntity iGregTechTileEntity = getBaseMetaTileEntity();
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (hintsOnly) {
            TecTech.proxy.hint_particle(iGregTechTileEntity.getWorld(),
                    iGregTechTileEntity.getXCoord() + xDir,
                    iGregTechTileEntity.getYCoord() + yDir,
                    iGregTechTileEntity.getZCoord() + zDir,
                    StructureLibAPI.getBlockHint(), 12);
        } else {
            if (iGregTechTileEntity.getBlockOffset(xDir, 0, zDir).getMaterial() == Material.air) {
                iGregTechTileEntity.getWorld().setBlock(
                        iGregTechTileEntity.getXCoord() + xDir,
                        iGregTechTileEntity.getYCoord() + yDir,
                        iGregTechTileEntity.getZCoord() + zDir,
                        StructureLibAPI.getBlockHint(), 12, 2);
            }
        }
        structureBuild_EM("main", 1, 1, 0, stackSize, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}