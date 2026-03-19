package tectech.thing.metaTileEntity.single;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Textures.BlockIcons.custom;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import tectech.thing.gui.MTEBuckConverterGui;
import tectech.util.CommonValues;

public class MTEBuckConverter extends MTETieredMachineBlock {

    private static ITexture BUCK, BUCK_ACTIVE;
    private int voltage = 8;
    private byte voltageTier = 0;
    private boolean isUsingTiers = false;
    private int amperage = 1;

    public MTEBuckConverter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL, translateToLocal("gt.blockmachines.machine.tt.buck.desc.0"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.machine.tt.buck.desc.1"), });
    }

    public MTEBuckConverter(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBuckConverter(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        BUCK = TextureFactory.of(custom("iconsets/BUCK"));
        BUCK_ACTIVE = TextureFactory.of(custom("iconsets/BUCK_ACTIVE"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        boolean isWorking = aBaseMetaTileEntity.isAllowedToWork();
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
            side == facing ? (isWorking ? BUCK_ACTIVE : BUCK)
                : (side == facing.getOpposite() ? Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 1]
                    : (isWorking ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_16A[mTier + 1]
                        : Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_16A[mTier + 1])) };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("eVoltage", voltage);
        aNBT.setByte("eVoltageTier", voltageTier);
        aNBT.setBoolean("eUsingTiers", isUsingTiers);
        aNBT.setInteger("eAmperage", amperage);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        // compatibility with previous version
        voltage = Math.max(aNBT.hasKey("eEUT") ? aNBT.getInteger("eEUT") : aNBT.getInteger("eVoltage"), 1);
        voltageTier = aNBT.getByte("eVoltageTier");
        isUsingTiers = aNBT.getBoolean("eUsingTiers");
        amperage = Math.max(aNBT.hasKey("eAMP") ? aNBT.getInteger("eAMP") : aNBT.getInteger("eAmperage"), 1);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return getBaseMetaTileEntity().isAllowedToWork() && side != getBaseMetaTileEntity().getFrontFacing()
            && side != getBaseMetaTileEntity().getBackFacing();
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public long maxAmperesOut() {
        return getBaseMetaTileEntity().isAllowedToWork() ? Math.min(amperage, 64) : 0;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUOutput() {
        return getBaseMetaTileEntity().isAllowedToWork() ? Math.min(getActualVoltage(), maxEUInput()) : 0;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] << 4;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] << 2;
    }

    @Override
    public int getProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyCapacity();
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public byte getVoltageTier() {
        return voltageTier;
    }

    public void setVoltageTier(byte voltageTier) {
        this.voltageTier = voltageTier;
    }

    public boolean isUsingTiers() {
        return isUsingTiers;
    }

    public void setUsingTiers(boolean usingTiers) {
        isUsingTiers = usingTiers;
    }

    public int getAmperage() {
        return amperage;
    }

    public void setAmperage(int amperage) {
        this.amperage = amperage;
    }

    public long getActualVoltage() {
        return (isUsingTiers ? GTValues.V[voltageTier] : voltage);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEBuckConverterGui(this).build(data, syncManager, uiSettings);
    }
}
