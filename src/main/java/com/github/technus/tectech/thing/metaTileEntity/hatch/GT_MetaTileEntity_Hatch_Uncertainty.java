package com.github.technus.tectech.thing.metaTileEntity.hatch;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.Util;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_Container_Uncertainty;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_Uncertainty;
import com.github.technus.tectech.thing.metaTileEntity.hatch.gui.GT_GUIContainer_UncertaintyAdv;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.reflect.FieldUtils;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

/**
 * Created by danie_000 on 15.12.2016.
 */
public class GT_MetaTileEntity_Hatch_Uncertainty extends GT_MetaTileEntity_Hatch {
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    public short[] matrix = new short[]{500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500};
    public byte selection = -1, mode = 0, status = -128;//all 8 bits set

    private String clientLocale = "en_US";

    public GT_MetaTileEntity_Hatch_Uncertainty(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "");
        Util.setTier(aTier,this);
        regenerate();
    }

    public GT_MetaTileEntity_Hatch_Uncertainty(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        regenerate();
    }

    @Override
    @SideOnly(Side.CLIENT)
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
        if (mTier > 7) {
            return new GT_GUIContainer_UncertaintyAdv(aPlayerInventory, aBaseMetaTileEntity);
        }
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
        if (aBaseMetaTileEntity.isServerSide() && (aTick & 15) == 0) {
            if (mode == 0) {
                aBaseMetaTileEntity.setActive(false);
                status = -128;
            } else {
                aBaseMetaTileEntity.setActive(true);
                shift();
                compute();
            }
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
                translateToLocalFormatted("tt.keyword.Status", clientLocale) + ": " + EnumChatFormatting.GOLD + status
        };
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mSel", selection);
        aNBT.setByte("mMode", mode);
        aNBT.setByte("mStatus", status);
        NBTTagCompound mat = new NBTTagCompound();
        for (int i = 0; i < 16; i++) {
            mat.setShort(Integer.toString(i), matrix[i]);
        }
        aNBT.setTag("mMat", mat);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        selection = aNBT.getByte("mSel");
        mode = aNBT.getByte("mMode");
        status = aNBT.getByte("mStatus");
        NBTTagCompound mat = aNBT.getCompoundTag("mMat");
        for (int i = 0; i < 16; i++) {
            matrix[i] = mat.getShort(Integer.toString(i));
        }
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
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                translateToLocal("gt.blockmachines.hatch.certain.desc.0"),//Feeling certain, or not?
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + translateToLocal("gt.blockmachines.hatch.certain.desc.1")//Schrödinger equation in a box
        };
    }

    private boolean balanceCheck(int sideLenY, short... masses) {
        float inequality = 0;
        for (int i = 0; i < masses.length >> 1; i++) {
            inequality += Math.abs(masses[i] - masses[masses.length - i - 1]);
        }
        return inequality < masses.length << 7;
    }

    public void regenerate() {
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = (short) TecTech.RANDOM.nextInt(1000);
        }
    }

    public byte compute() {
        int result = 0;
        switch (mode) {
            case 1://ooo oxo ooo
                result = balanceCheck(4, matrix) ? 0 : 1;
                break;
            case 2://ooo xox ooo
                result += balanceCheck(4,
                        matrix[0], matrix[4],
                        matrix[1], matrix[5],
                        matrix[2], matrix[6],
                        matrix[3], matrix[7]) ? 0 : 1;
                result += balanceCheck(4,
                        matrix[8], matrix[12],
                        matrix[9], matrix[13],
                        matrix[10], matrix[14],
                        matrix[11], matrix[15]) ? 0 : 2;
                break;
            case 3://oxo xox oxo
                result += balanceCheck(2,
                        matrix[0], matrix[4], matrix[8], matrix[12],
                        matrix[1], matrix[5], matrix[9], matrix[13]) ? 0 : 1;
                result += balanceCheck(4,
                        matrix[0], matrix[4],
                        matrix[1], matrix[5],
                        matrix[2], matrix[6],
                        matrix[3], matrix[7]) ? 0 : 2;
                result += balanceCheck(4,
                        matrix[8], matrix[12],
                        matrix[9], matrix[13],
                        matrix[10], matrix[14],
                        matrix[11], matrix[15]) ? 0 : 4;
                result += balanceCheck(2,
                        matrix[2], matrix[6], matrix[10], matrix[14],
                        matrix[3], matrix[7], matrix[11], matrix[15]) ? 0 : 8;
                break;
            case 4://xox ooo xox
                result += balanceCheck(2,
                        matrix[0], matrix[4],
                        matrix[1], matrix[5]) ? 0 : 1;
                result += balanceCheck(2,
                        matrix[8], matrix[12],
                        matrix[9], matrix[13]) ? 0 : 2;
                result += balanceCheck(2,
                        matrix[2], matrix[6],
                        matrix[3], matrix[7]) ? 0 : 4;
                result += balanceCheck(2,
                        matrix[10], matrix[14],
                        matrix[11], matrix[15]) ? 0 : 8;
                break;
            case 5://xox oxo xox
                result += balanceCheck(2,
                        matrix[0], matrix[4],
                        matrix[1], matrix[5]) ? 0 : 1;
                result += balanceCheck(2,
                        matrix[8], matrix[12],
                        matrix[9], matrix[13]) ? 0 : 2;
                result += balanceCheck(4, matrix) ? 0 : 4;
                result += balanceCheck(2,
                        matrix[2], matrix[6],
                        matrix[3], matrix[7]) ? 0 : 8;
                result += balanceCheck(2,
                        matrix[10], matrix[14],
                        matrix[11], matrix[15]) ? 0 : 16;
                break;
        }
        return status = (byte) result;
    }

    private void shift() {
        int i = TecTech.RANDOM.nextInt(16), j = TecTech.RANDOM.nextInt(128);
        matrix[i] += ((matrix[i] & 1) == 0 ? 2 : -2) * j >> 5;
        matrix[i] += j == 0 ? 1 : 0;
        if (matrix[i] < 0) {
            matrix[i] = 0;
        } else if (matrix[i] > 1000) {
            matrix[i] = 999;
        }
    }

    public byte update(int newMode) {
        if (newMode == mode) {
            return status;
        }
        if (newMode < 0 || newMode > 5) {
            newMode = 0;
        }
        mode = (byte) newMode;
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
