package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_collider extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    public static long heliumPlasmaValue;

    private byte eTier = 0;

    protected long startupCost=10_000_000_000L;
    protected double currentVelocity=-1,targetVelocity=-1;

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"I0A0A0","I00000","I0A0A0",},
            {"H0000000","G001111100","H0000000",},
            {"F22223332222","F41155555114","F22223332222",},
            {"E2000000000002","E4155111115514","E2000000000002",},
            {"D20000E00002","D41511E11514","D20000E00002",},
            {"C2000I0002","C4151I1514","C2000I0002",},
            {"B2000K0002","B4151K1514","B2000K0002",},
            {"B200M002","A0151M1510","B200M002",},
            {"A0200M0020","A0151M1510","A0200M0020",},
            {"0020O0200","0151O1510","0020O0200",},
            {"A030O030","0151O1510","A030O030",},
            {"0030O0300","0151O1510","0030O0300",},
            {"A030O030","0151O1510","A030O030",},
            {"0020O0200","0151O1510","0020O0200",},
            {"A0200M0020","A0151M1510","A0200M0020",},
            {"B200M002","A0151M1510","B200M002",},
            {"B2000K0002","B4151K1514","B2000K0002",},
            {"C2000I0002","C4151I1514","C2000I0002",},
            {"D200002   200002","D415112 . 211514","D200002   200002",},
            {"E20!!22222!!02","E4155111115514","E20!!22222!!02",},
            {"F2222#\"#2222","F41155555114","F2222#\"#2222",},
    };
    private static final Block[] blockType = new Block[]{
            sBlockCasingsTT,
            sBlockCasingsTT,
            sBlockCasingsTT,
            QuantumGlassBlock.INSTANCE,
            sBlockCasingsTT,
            sBlockCasingsTT
    };
    private static final byte[] blockMeta1 = new byte[]{4, 7, 4, 0, 4, 8};
    private static final byte[] blockMeta2 = new byte[]{4, 7, 5, 0, 6, 9};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalInputToMachineList", "addElementalOutputToMachineList", "addElementalMufflerToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4, textureOffset + 4, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4, 4, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Input Hatches or Molecular Casing",
            "3 - Elemental Output Hatches or Molecular Casing",
            "4 - Elemental Overflow Hatches or Molecular Casing",
            "General - Another Controller facing opposite direction",
    };
    //endregion

    public GT_MetaTileEntity_EM_collider(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_collider(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_collider(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4], new TT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4]};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("eTier", eTier);//collider tier
        aNBT.setDouble("eCurrentVelocity",currentVelocity);
        aNBT.setDouble("eTargetVelocity",targetVelocity);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        eTier = aNBT.getByte("eTier");//collider tier
        currentVelocity=aNBT.getDouble("eCurrentVelocity");
        targetVelocity=aNBT.getDouble("eTargetVelocity");
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX*2;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ*2;
        if (iGregTechTileEntity.getBlockOffset(xDir, 0, zDir) != sBlockCasingsTT) {
            eTier = 0;
            return false;
        }

        if (iGregTechTileEntity.getMetaIDOffset(xDir, 0, zDir) == 8) {
            eTier = 1;
        } else if (iGregTechTileEntity.getMetaIDOffset(xDir, 0, zDir) == 9) {
            eTier = 2;
        } else {
            eTier = 0;
            return false;
        }

        boolean test;
        switch (eTier) {
            case 1:
                test = structureCheck_EM(shape, blockType, blockMeta1, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 11, 1, 18);
                break;
            case 2:
                test = structureCheck_EM(shape, blockType, blockMeta2, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 11, 1, 18);
                break;
            default:
                eTier = 0;
                return false;
        }
        if (test) {
            return true;
        }
        eTier = 0;
        return false;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        IGregTechTileEntity iGregTechTileEntity=getBaseMetaTileEntity();
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX*4;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY*4;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ*4;
        if(hintsOnly){
            TecTech.proxy.hint_particle(iGregTechTileEntity.getWorld(),
                    iGregTechTileEntity.getXCoord()+xDir,
                    iGregTechTileEntity.getYCoord()+yDir,
                    iGregTechTileEntity.getZCoord()+zDir,
                    TT_Container_Casings.sHintCasingsTT,12);
        } else{
            if(iGregTechTileEntity.getBlockOffset(xDir,0,zDir).getMaterial() == Material.air) {
                iGregTechTileEntity.getWorld().setBlock(iGregTechTileEntity.getXCoord() + xDir, iGregTechTileEntity.getYCoord()+yDir, iGregTechTileEntity.getZCoord() + zDir, TT_Container_Casings.sHintCasingsTT, 12, 2);
            }
        }
        if ((stackSize & 1) == 1) {
            StructureBuilderExtreme(shape, blockType, blockMeta1, 11, 1, 18, iGregTechTileEntity,this, hintsOnly);
        } else {
            StructureBuilderExtreme(shape, blockType, blockMeta2, 11, 1, 18, iGregTechTileEntity,this, hintsOnly);
        }
    }

    @Override
    protected void parametersLoadDefault_EM() {
        //setParameterPairIn_ClearOut(0,false,)
    }

    @Override
    public void parametersOutAndStatusesWrite_EM(boolean machineBusy) {
        //setStatusOfParameterIn(0,0,);
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        GT_MetaTileEntity_EM_collider partner=getPartner();
        if(partner==null){
            return false;
        }
        if(isMaster()){

        }else{

        }
        return false;
    }

    @Override
    protected void afterRecipeCheckFailed() {
        currentVelocity=-1;
        super.afterRecipeCheckFailed();
    }

    @Override
    public void stopMachine() {
        currentVelocity=-1;
        super.stopMachine();
    }

    protected GT_MetaTileEntity_EM_collider getPartner(){
        IGregTechTileEntity iGregTechTileEntity=getBaseMetaTileEntity();
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX*4;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY*4;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ*4;
        IGregTechTileEntity gregTechTileEntity=iGregTechTileEntity.getIGregTechTileEntityOffset(iGregTechTileEntity.getXCoord()+xDir,
                iGregTechTileEntity.getYCoord()+yDir,
                iGregTechTileEntity.getZCoord()+zDir);
        return gregTechTileEntity instanceof GT_MetaTileEntity_EM_collider && ((GT_MetaTileEntity_EM_collider) gregTechTileEntity).mMachine && gregTechTileEntity.getBackFacing()==iGregTechTileEntity.getFrontFacing() ? (GT_MetaTileEntity_EM_collider)gregTechTileEntity:null;
    }

    protected final boolean isMaster(){
        return getBaseMetaTileEntity().getFrontFacing()%2==0;
    }

    @Override
    public void outputAfterRecipe_EM(){
        if(outputEM!=null) {
            for(int i=0,lim=Math.min(outputEM.length,eOutputHatches.size());i<lim;i++){
                if(outputEM[i]!=null) {
                    eOutputHatches.get(i).getContainerHandler().putUnifyAll(outputEM[i]);
                    outputEM[i]=null;
                }
            }
        }
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Collide matter at extreme velocities.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Faster than light*!!!"
        };
    }
}
