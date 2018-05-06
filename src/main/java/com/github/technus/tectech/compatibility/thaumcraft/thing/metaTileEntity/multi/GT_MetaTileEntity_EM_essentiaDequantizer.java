package com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.ePrimalAspectDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.GT_MetaTileEntity_EM_quantizer;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.CommonValues.V;
import static com.github.technus.tectech.compatibility.thaumcraft.thing.metaTileEntity.multi.EssentiaCompat.essentiaContainerCompat;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static gregtech.api.enums.GT_Values.E;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_essentiaDequantizer extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"   "," . ","   ",},
            {"0A0",E,"0A0",},
            {"121","232","121",},
            {"\"\"\"","\"2\"","\"\"\"",},
            {"202","0!0","202",},
    };
    private static final Block[] blockType = new Block[]{QuantumGlassBlock.INSTANCE, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMeta = new byte[]{0,0,4,8};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalInputToMachineList", "addElementalMufflerToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Input Hatch",
            "3 - Elemental Overflow Hatches or Elemental Casing",
            "General - Some sort of Essentia Storage",
    };
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
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound(){
        return GT_MetaTileEntity_EM_quantizer.activitySound;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return essentiaContainerCompat.check(this) && structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 1, 0);
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        IGregTechTileEntity iGregTechTileEntity=getBaseMetaTileEntity();
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ;
        if(hintsOnly){
            TecTech.proxy.hint_particle(iGregTechTileEntity.getWorld(),
                    iGregTechTileEntity.getXCoord()+xDir,
                    iGregTechTileEntity.getYCoord()+yDir,
                    iGregTechTileEntity.getZCoord()+zDir,
                    TT_Container_Casings.sHintCasingsTT,12);
        } else{
            if(iGregTechTileEntity.getBlockOffset(xDir,0,zDir).getMaterial() == Material.air) {
                iGregTechTileEntity.getWorld().setBlock(iGregTechTileEntity.getXCoord() + xDir, iGregTechTileEntity.getYCoord() + yDir, iGregTechTileEntity.getZCoord() + zDir, TT_Container_Casings.sHintCasingsTT, 12, 2);
            }
        }
        StructureBuilderExtreme(shape, blockType, blockMeta,1, 1, 0, iGregTechTileEntity,this,hintsOnly);
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        TileEntity container = essentiaContainerCompat.getContainer(this);
        if (eInputHatches.size() < 1 || container ==null) {
            stopMachine();
            return false;
        }
        cElementalInstanceStackMap inputHatchContainer=eInputHatches.get(0).getContainerHandler();
        if(inputHatchContainer.hasStacks()){
            cElementalInstanceStack stack = inputHatchContainer.getFirst();
            inputHatchContainer.removeAmount(false,new cElementalInstanceStack(stack.definition,1));
            if(!essentiaContainerCompat.putElementalInstanceStack(container,stack)) {
                cleanStackEM_EM(stack);
            }
            mMaxProgresstime = 20;
            mEfficiencyIncrease = 10000;
            eAmpereFlow=1;
            if (stack.definition instanceof ePrimalAspectDefinition) {
                mEUt = (int) -V[8];
            } else {
                mEUt = (int) -V[10];
            }
            return true;
        }
        return false;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Transform quantum form back to...",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "regular one, but why?"
        };
    }
}
