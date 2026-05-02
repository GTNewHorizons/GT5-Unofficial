package tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchUncertaintyGui;
import gregtech.mixin.interfaces.accessors.EntityPlayerMPAccessor;
import tectech.TecTech;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 15.12.2016.
 */
public class MTEHatchUncertainty extends MTEHatch {

    private static IIconContainer ScreenON;
    private static IIconContainer ScreenOFF;
    private final short[] matrix = new short[] { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
        500, 500 };
    public byte selection = -1, mode = 0, status = (byte) 0b11111111; // all 8 bits set
    private boolean stopChecking = false;
    private String clientLocale = "en_US";

    public MTEHatchUncertainty(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "");
        regenerate();
    }

    public MTEHatchUncertainty(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        regenerate();
    }

    public short getMatrixElement(int index) {
        return matrix[index];
    }

    public void setMatrixElemet(short matrixElement, int index) {
        matrix[index] = matrixElement;
    }

    public byte getSelection() {
        return selection;
    }

    public void setSelection(byte selection) {
        this.selection = selection;
    }

    public byte getMode() {
        return mode;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        ScreenOFF = Textures.BlockIcons.custom("iconsets/UC");
        ScreenON = Textures.BlockIcons.custom("iconsets/UC_ACTIVE");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(ScreenON) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(ScreenOFF) };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        // Changed from calling every 15 ticks; based on local testing new shift() implementation fades ~20.97x faster
        if (aBaseMetaTileEntity.isServerSide() && aTick % 320 == 0) {
            if (mode == 0) {
                aBaseMetaTileEntity.setActive(false);
                status = (byte) 0b11111111;
            } else {
                aBaseMetaTileEntity.setActive(true);
                if (!stopChecking) { // No point in making calculations if the entire matrix has faded to 0
                    shift();
                    compute();
                }
            }
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEHatchUncertainty(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            translateToLocalFormatted("tt.keyword.Status", clientLocale) + ": " + EnumChatFormatting.GOLD + status };
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
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
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
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
        if (aPlayer instanceof EntityPlayerMPAccessor) {
            clientLocale = ((EntityPlayerMPAccessor) aPlayer).gt5u$getTranslator();
        }
        openGui(aPlayer);
        return true;
    }

    @Override
    public String[] getDescription() {
        String[] description = new String[4];
        description[0] = CommonValues.TEC_MARK_EM;
        description[1] = translateToLocal("gt.blockmachines.hatch.certain.desc.0");
        description[2] = EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
            + translateToLocal("gt.blockmachines.hatch.certain.desc.1");
        if (mTier < 6) {
            description[3] = EnumChatFormatting.DARK_RED + translateToLocal("gt.blockmachines.hatch.certain.desc.2");
        }
        return description;
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
            case 1: // ooo oxo ooo
                result = balanceCheck(4, matrix) ? 0 : 1;
                break;
            case 2: // ooo xox ooo
                result += balanceCheck(
                    4,
                    matrix[0],
                    matrix[4],
                    matrix[1],
                    matrix[5],
                    matrix[2],
                    matrix[6],
                    matrix[3],
                    matrix[7]) ? 0 : 1;
                result += balanceCheck(
                    4,
                    matrix[8],
                    matrix[12],
                    matrix[9],
                    matrix[13],
                    matrix[10],
                    matrix[14],
                    matrix[11],
                    matrix[15]) ? 0 : 2;
                break;
            case 3: // oxo xox oxo
                result += balanceCheck(
                    2,
                    matrix[0],
                    matrix[4],
                    matrix[8],
                    matrix[12],
                    matrix[1],
                    matrix[5],
                    matrix[9],
                    matrix[13]) ? 0 : 1;
                result += balanceCheck(
                    4,
                    matrix[0],
                    matrix[4],
                    matrix[1],
                    matrix[5],
                    matrix[2],
                    matrix[6],
                    matrix[3],
                    matrix[7]) ? 0 : 2;
                result += balanceCheck(
                    4,
                    matrix[8],
                    matrix[12],
                    matrix[9],
                    matrix[13],
                    matrix[10],
                    matrix[14],
                    matrix[11],
                    matrix[15]) ? 0 : 4;
                result += balanceCheck(
                    2,
                    matrix[2],
                    matrix[6],
                    matrix[10],
                    matrix[14],
                    matrix[3],
                    matrix[7],
                    matrix[11],
                    matrix[15]) ? 0 : 8;
                break;
            case 4: // xox ooo xox
                result += balanceCheck(2, matrix[0], matrix[4], matrix[1], matrix[5]) ? 0 : 1;
                result += balanceCheck(2, matrix[8], matrix[12], matrix[9], matrix[13]) ? 0 : 2;
                result += balanceCheck(2, matrix[2], matrix[6], matrix[3], matrix[7]) ? 0 : 4;
                result += balanceCheck(2, matrix[10], matrix[14], matrix[11], matrix[15]) ? 0 : 8;
                break;
            case 5: // xox oxo xox
                result += balanceCheck(2, matrix[0], matrix[4], matrix[1], matrix[5]) ? 0 : 1;
                result += balanceCheck(2, matrix[8], matrix[12], matrix[9], matrix[13]) ? 0 : 2;
                result += balanceCheck(4, matrix) ? 0 : 4;
                result += balanceCheck(2, matrix[2], matrix[6], matrix[3], matrix[7]) ? 0 : 8;
                result += balanceCheck(2, matrix[10], matrix[14], matrix[11], matrix[15]) ? 0 : 16;
                break;
        }
        return status = (byte) result;
    }

    private void shift() {
        stopChecking = true;
        for (int i = 0; i < 16; i++) {
            matrix[i] = (short) Math.max(0, matrix[i] - 1);
            if (matrix[i] != 0 && stopChecking) {
                stopChecking = false;
            }
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
        stopChecking = false;
        regenerate();
        compute();
        return status;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchUncertaintyGui(this).build(guiData, syncManager, uiSettings);
    }
}
