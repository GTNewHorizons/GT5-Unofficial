package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.mechanics.constructable.IConstructable;
import com.github.technus.tectech.mechanics.structure.IHatchAdder;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_Container_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_GUIContainer_MultiMachineEM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.github.technus.tectech.mechanics.structure.Structure.adders;
import static gregtech.api.GregTech_API.sBlockCasings4;
import static net.minecraft.util.StatCollector.translateToLocal;

public class GT_MetaTileEntity_TM_proccessingStack extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    //region variables
    private boolean hasBeenPausedThisCycle = false;
    //endregion

    //region structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] base = new String[][]{
            {"   ",},
            {" 0 ",},
            {"   ",},
    };
    private static final String[][] slice = new String[][]{
            {"111",},
            {"101",},
            {"111",},
    };
    private static final String[][] cap = new String[][]{
            {"   ",},
            {" 0 ",},
            {"   ",},
    };
    private static final Block[] blockType = new Block[]{sBlockCasings4};
    private static final byte[] blockMeta = new byte[]{1};

    private static final IHatchAdder<GT_MetaTileEntity_TM_proccessingStack>[] addingMethods = adders(
            GT_MetaTileEntity_TM_proccessingStack::addClassicToMachineList);
    private static final short[] casingTextures = new short[]{49};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasings4};
    private static final byte[] blockMetaFallback = new byte[]{1};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.tm.proccessingStack.hint.0"),
            translateToLocal("gt.blockmachines.multimachine.tm.proccessingStack.hint.1"),
    };
    //endregion

    public GT_MetaTileEntity_TM_proccessingStack(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TM_proccessingStack(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TM_proccessingStack(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        return false;
        //return structureCheck_EM(shape, blockType, blockMeta, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 1, 0, 0);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        return true;
    }

    @Override
    public void outputAfterRecipe_EM() {

    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.BASS_MARK,
                translateToLocal("gt.blockmachines.multimachine.tm.proccessingStack.desc.0"),
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.multimachine.tm.proccessingStack.desc.1"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.multimachine.tm.proccessingStack.desc.2"),
        };
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, false, true, true);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachineEM(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "EMDisplay.png", false, true, true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][49], new TT_RenderedExtendedFacingTexture(aActive ? Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE : Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)};
        } else if (aSide == GT_Utility.getOppositeSide(aFacing)) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][49], aActive ? Textures.BlockIcons.casingTexturePages[0][52] : Textures.BlockIcons.casingTexturePages[0][53]};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[0][49]};
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        //StructureBuilderExtreme(shape, blockType, blockMeta, 2, 2, 0, getBaseMetaTileEntity(), this, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}