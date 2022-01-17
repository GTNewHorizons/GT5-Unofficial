package com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.structure.Structure;
import com.github.technus.tectech.mechanics.structure.adders.IHatchAdder;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_quantizer;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.util.CommonValues;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;

import static com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.EssentiaCompat.essentiaContainerCompat;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationInfo.AVOGADRO_CONSTANT;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationInfo.AVOGADRO_CONSTANT_DIMINISHED;
import static com.github.technus.tectech.mechanics.structure.Structure.adders;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.util.CommonValues.V;
import static gregtech.api.enums.GT_Values.E;
import static net.minecraft.util.StatCollector.translateToLocal;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_essentiaDequantizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"   ", " . ", "   ",},
            {"0A0", E, "0A0",},
            {"121", "232", "121",},
            {"\"\"\"", "\"2\"", "\"\"\"",},
            {"202", "0!0", "202",},
    };
    private static final Block[] blockType = new Block[]{QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{0, 0, 4, 8};
    private static final IHatchAdder<GT_MetaTileEntity_EM_essentiaDequantizer>[] addingMethods = adders(
            GT_MetaTileEntity_EM_essentiaDequantizer::addClassicToMachineList,
            GT_MetaTileEntity_EM_essentiaDequantizer::addElementalInputToMachineList,
            GT_MetaTileEntity_EM_essentiaDequantizer::addElementalMufflerToMachineList);
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4, 4};
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
        return essentiaContainerCompat.check(this) && structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
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
        if (stack.getAmount() < AVOGADRO_CONSTANT_DIMINISHED) {
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
                inputHatchContainer.removeAmount(stack.getDefinition().getStackForm(AVOGADRO_CONSTANT));
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
        }else{
            if(!essentiaContainerCompat.putInContainer(container,outputEssentiaName)){
                stopMachine();
            }
        }
        outputEssentiaName=null;
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
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if (hintsOnly) {
            TecTech.proxy.hint_particle(iGregTechTileEntity.getWorld(),
                    iGregTechTileEntity.getXCoord() + xDir,
                    iGregTechTileEntity.getYCoord() + yDir,
                    iGregTechTileEntity.getZCoord() + zDir,
                    TT_Container_Casings.sHintCasingsTT, 12);
        } else {
            if (iGregTechTileEntity.getBlockOffset(xDir, 0, zDir).getMaterial() == Material.air) {
                iGregTechTileEntity.getWorld().setBlock(iGregTechTileEntity.getXCoord() + xDir, iGregTechTileEntity.getYCoord() + yDir, iGregTechTileEntity.getZCoord() + zDir, TT_Container_Casings.sHintCasingsTT, 12, 2);
            }
        }
        Structure.builder(shape, blockType, blockMeta, 1, 1, 0, iGregTechTileEntity, getExtendedFacing(), hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setString("eOutputEssentia",outputEssentiaName);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        outputEssentiaName=aNBT.getString("eOutputEssentia");
    }
}