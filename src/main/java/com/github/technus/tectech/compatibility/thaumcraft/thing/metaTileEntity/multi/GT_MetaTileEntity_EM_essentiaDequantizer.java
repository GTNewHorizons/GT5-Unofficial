package com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi;

import com.github.technus.tectech.TecTech;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;

import static com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.EssentiaCompat.essentiaContainerCompat;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_ITEM_DIMINISHED;
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
public class GT_MetaTileEntity_EM_essentiaDequantizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region structure
    //use multi A energy inputs, use less power the longer it runs
    private static final IStructureDefinition<GT_MetaTileEntity_EM_essentiaDequantizer> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_essentiaDequantizer>builder()
            .addShape("main", new String[][]{
                    {"&&&", "&~&", "&&&",},
                    {"0 0", " * ", "0 0",},
                    {"121", "232", "121",},
                    {"$$$", "$2$", "$$$",},
                    {"202", "0!0", "202",},})
            .addElement('0', ofBlockAnyMeta(QuantumGlassBlock.INSTANCE))
            .addElement('1', ofBlock(sBlockCasingsTT, 0))
            .addElement('2', ofBlock(sBlockCasingsTT, 4))
            .addElement('3', ofBlock(sBlockCasingsTT, 8))
            .addElement('&', ofHatchAdderOptional(GT_MetaTileEntity_EM_essentiaDequantizer::addClassicToMachineList, textureOffset, 1, sBlockCasingsTT, 0))
            .addElement('!', ofHatchAdderOptional(GT_MetaTileEntity_EM_essentiaDequantizer::addElementalInputToMachineList, textureOffset + 4, 2, sBlockCasingsTT, 4))
            .addElement('$', ofHatchAdderOptional(GT_MetaTileEntity_EM_essentiaDequantizer::addElementalMufflerToMachineList, textureOffset + 4, 2, sBlockCasingsTT, 4))
            .addElement('*', ofTileAdder(essentiaContainerCompat::check, StructureLibAPI.getBlockHint(),12))
            .build();

    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.hint.0"),//1 - Classic Hatches or High Power Casing
            translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.hint.1"),//2 - Elemental Input Hatch
            translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.hint.2"),//3 - Elemental Overflow Hatches or Elemental Casing
            translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.hint.3"),//General - Some sort of Essentia Storage
    };

    private String outputEssentiaName;
    //endregion

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
                outputEssentiaName = null;
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
        outputEssentiaName = null;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.desc.0"),//Transform quantum form back to...
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.em.emtoessentia.desc.1")//regular one, but why?
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
        int                 xDir                = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int                 yDir                = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY;
        int                 zDir                = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
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
}