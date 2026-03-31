package tectech.thing.metaTileEntity.single;

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
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.common.pollution.Pollution;
import tectech.TecTech;
import tectech.thing.gui.MTEDebugPollutorGui;
import tectech.util.CommonValues;

/**
 * Created by Tec on 23.03.2017.
 */
public class MTEDebugPollutor extends MTETieredMachineBlock {

    private static ITexture POLLUTOR;
    private int pollution = 0;
    private boolean isPolluting = true;

    public MTEDebugPollutor(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL, translateToLocal("gt.blockmachines.debug.tt.pollutor.desc.0"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.pollutor.desc.1"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.pollutor.desc.2") });
    }

    public MTEDebugPollutor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDebugPollutor(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        POLLUTOR = TextureFactory.of(Textures.BlockIcons.custom("iconsets/POLLUTOR"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
            (side == facing) ? POLLUTOR
                : isPolluting ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_LASER[mTier + 1]
                    : Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_LASER[mTier + 1] };
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
        aNBT.setInteger("ePollution", pollution);
        aNBT.setBoolean("eConsuming", isPolluting);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        pollution = aNBT.getInteger("ePollution");
        isPolluting = aNBT.getBoolean("eConsuming");
    }

    private int particleCooldown = 10;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        getBaseMetaTileEntity().setActive(pollution > 0);
        if (aBaseMetaTileEntity.isAllowedToWork() && aBaseMetaTileEntity.isActive()) {
            if (aBaseMetaTileEntity.isServerSide())
                Pollution.addPollution(aBaseMetaTileEntity, isPolluting ? pollution : -pollution);
            else {
                if (particleCooldown-- < 0) {
                    particleCooldown = 10; // the effects are overwhelming without a cooldown
                    for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                        if (side != aBaseMetaTileEntity.getFrontFacing()) {
                            TecTech.proxy.em_particle(aBaseMetaTileEntity, side);
                            TecTech.proxy.pollutor_particle(aBaseMetaTileEntity, side); // cleaning particles could be
                                                                                        // added?
                        }
                    }
                }
            }
        }
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
    public boolean isElectric() {
        return false;
    }

    public int getPollution() {
        return pollution;
    }

    public void setPollution(int pollution) {
        this.pollution = pollution;
    }

    public boolean isPolluting() {
        return isPolluting;
    }

    public void setPolluting(boolean polluting) {
        isPolluting = polluting;
        getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEDebugPollutorGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    protected boolean useMui2() {
        return false;
    }
}
