package com.github.technus.tectech.elementalMatter.machine;

import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.elementalMatter.gui.GT_Container_Uncertainty;
import com.github.technus.tectech.elementalMatter.gui.GT_GUIContainer_Uncertainty;
import com.github.technus.tectech.elementalMatter.gui.GT_GUIContainer_UncertaintyAdv;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.XSTR;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by danie_000 on 15.12.2016.
 */
public class GT_MetaTileEntity_Hatch_Uncertainty extends GT_MetaTileEntity_Hatch {
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static float errorMargin=0.05f;
    public short[] matrix=new short[]{500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500};
    public byte selection=-1,mode=0,status=-128;
    private static final XSTR ran=new XSTR();

    public GT_MetaTileEntity_Hatch_Uncertainty(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Feeling certain, or not?");
        regenerate();
    }

    public GT_MetaTileEntity_Hatch_Uncertainty(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        regenerate();
    }

    @Override
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/UC");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/UC_ACTIVE");
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_Uncertainty(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        if(mTier>6) return new GT_GUIContainer_UncertaintyAdv(aPlayerInventory,aBaseMetaTileEntity);
        return new GT_GUIContainer_Uncertainty(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(ScreenON)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(ScreenOFF)};
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(aBaseMetaTileEntity.isServerSide() && (aTick&15)==0) {
            shift();
            compute();
            if(mode==0)aBaseMetaTileEntity.setActive(false);
            else aBaseMetaTileEntity.setActive(true);
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Hatch_Uncertainty(mName, mTier, mDescription, mTextures);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
            "Status: "+EnumChatFormatting.GOLD+status
        };
    }

    @Override
    public boolean isSimpleMachine() {
        return  true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mSel",selection);
        aNBT.setByte("mMode",mode);
        aNBT.setByte("mStatus",status);
        NBTTagCompound mat=new NBTTagCompound();
        for(int i=0;i<16;i++)
            mat.setShort(Integer.toString(i),matrix[i]);
        aNBT.setTag("mMat",mat);
    }

    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        selection=aNBT.getByte("mSel");
        mode=aNBT.getByte("mMode");
        status=aNBT.getByte("mStatus");
        NBTTagCompound mat=aNBT.getCompoundTag("mMat");
        for(int i=0;i<16;i++)
            matrix[i]=mat.getShort(Integer.toString(i));
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                commonValues.tecMark,
                mDescription,
                EnumChatFormatting.AQUA.toString()+EnumChatFormatting.BOLD+"Schrödinger equation in a box"
        };
    }

    private float massOffset(int sideLenY, int sideLenX, short... masses){
        int mass=0;
        int massI=0,massJ=0;
        for (int i = 0; i < sideLenY; i++) {
            for (int j = 0; j < sideLenY*sideLenX; j+=sideLenY) {
                mass+=masses[i+j];
                massI+=((float)i-((float)sideLenY/2f)+.5f)*masses[i+j];
                massJ+=((float)(j/sideLenY)-((float)sideLenX/2f)+.5f)*masses[i+j];
            }
        }
        return ((Math.abs(massI/(float)mass)/(float) sideLenY)+(Math.abs(massJ/(float)mass))/(float) sideLenX);
    }

    public void regenerate(){
        for(int i=0;i<matrix.length;i++)
            matrix[i]=(short)ran.nextInt(1000);
    }

    public byte compute(){
        int result=0;
        switch(mode){
            case 1://ooo oxo ooo
                result=(massOffset(4,4,matrix)<errorMargin)?0:1;
                break;
            case 2://ooo xox ooo
                result+=(massOffset(4,2,
                        matrix[0],matrix[4],
                        matrix[1],matrix[5],
                        matrix[2],matrix[6],
                        matrix[3],matrix[7])<errorMargin)?0:1;
                result+=(massOffset(4,2,
                        matrix[8 ],matrix[12],
                        matrix[9 ],matrix[13],
                        matrix[10],matrix[14],
                        matrix[11],matrix[15])<errorMargin)?0:2;
                break;
            case 3://oxo xox oxo
                result+=(massOffset(2,4,
                        matrix[0],matrix[4], matrix[8],matrix[12],
                        matrix[1],matrix[5], matrix[9],matrix[13])<errorMargin)?0:1;
                result+=(massOffset(4,2,
                        matrix[0],matrix[4],
                        matrix[1],matrix[5],
                        matrix[2],matrix[6],
                        matrix[3],matrix[7])<errorMargin)?0:2;
                result+=(massOffset(4,2,
                        matrix[8 ],matrix[12],
                        matrix[9 ],matrix[13],
                        matrix[10],matrix[14],
                        matrix[11],matrix[15])<errorMargin)?0:4;
                result+=(massOffset(2,4,
                        matrix[2],matrix[6], matrix[10],matrix[14],
                        matrix[3],matrix[7], matrix[11],matrix[15])<errorMargin)?0:8;
                break;
            case 4://xox ooo xox
                result+=(massOffset(2,2,
                        matrix[0],matrix[4],
                        matrix[1],matrix[5])<errorMargin)?0:1;
                result+=(massOffset(2,2,
                        matrix[8],matrix[12],
                        matrix[9],matrix[13])<errorMargin)?0:2;
                result+=(massOffset(2,2,
                        matrix[2],matrix[6],
                        matrix[3],matrix[7])<errorMargin)?0:4;
                result+=(massOffset(2,2,
                        matrix[10],matrix[14],
                        matrix[11],matrix[15])<errorMargin)?0:8;
                break;
            case 5://xox oxo xox
                result+=(massOffset(2,2,
                        matrix[0],matrix[4],
                        matrix[1],matrix[5])<errorMargin)?0:1;
                result+=(massOffset(2,2,
                        matrix[8],matrix[12],
                        matrix[9],matrix[13])<errorMargin)?0:2;
                result+=(massOffset(4,4,matrix)<errorMargin)?0:4;
                result+=(massOffset(2,2,
                        matrix[2],matrix[6],
                        matrix[3],matrix[7])<errorMargin)?0:8;
                result+=(massOffset(2,2,
                        matrix[10],matrix[14],
                        matrix[11],matrix[15])<errorMargin)?0:16;
                break;
        }
        return status=(byte)result;
    }

    private void shift(){
        final int i=ran.nextInt(16),j=ran.nextInt(2);
        matrix[i]+=(((matrix[i]&1)==0)?2:-2)*j;
        switch(matrix[i]){
            case 1002: matrix[i]-=3; break;
            case 1001: matrix[i]-=1; break;
            case -1:   matrix[i]+=1; break;
            case -2:   matrix[i]+=3; break;
        }
    }

    public byte update(int newMode){
        if(newMode==mode)return mode;
        if(newMode<0 || newMode>5) newMode=0;
        mode=(byte)newMode;
        regenerate();
        compute();
        return status;
    }

    //@Override
    //public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
    //    if(aSide == this.getBaseMetaTileEntity().getFrontFacing()) {
    //        changeMode(++mode);
    //        GT_Utility.sendChatToPlayer(aPlayer, "Equation mode: "+mode);
    //    }
    //}
}
