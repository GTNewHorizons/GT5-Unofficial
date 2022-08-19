package com.github.technus.tectech.thing.metaTileEntity.single;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.*;
import static com.github.technus.tectech.util.CommonValues.TRANSFER_AT;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_Energy;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_Container_DebugPowerGenerator;
import com.github.technus.tectech.thing.metaTileEntity.single.gui.GT_GUIContainer_DebugPowerGenerator;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * Created by Tec on 23.03.2017.
 */
public class GT_MetaTileEntity_DebugPowerGenerator extends GT_MetaTileEntity_TieredMachineBlock
        implements IConnectsToEnergyTunnel {
    private static GT_RenderedTexture GENNY;
    private boolean LASER = false;
    public int EUT = 0, AMP = 0;
    public boolean producing = true;

    public GT_MetaTileEntity_DebugPowerGenerator(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "");
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_DebugPowerGenerator(
            String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        TT_Utility.setTier(aTier, this);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DebugPowerGenerator(mName, mTier, mDescription, mTextures);
    }

    @Override
    public final void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        LASER = !LASER;
        GT_Utility.sendChatToPlayer(
                aPlayer,
                String.format(StatCollector.translateToLocal("tt.chat.debug.generator"), LASER ? "ON" : "OFF"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        GENNY = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/GENNY"));
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        return new ITexture[] {
            MACHINE_CASINGS_TT[mTier][aColorIndex + 1],
            aSide != aFacing
                    ? LASER
                            ? (aActive ? OVERLAYS_ENERGY_OUT_LASER_TT[mTier] : OVERLAYS_ENERGY_IN_LASER_TT[mTier])
                            : (aActive ? OVERLAYS_ENERGY_OUT_POWER_TT[mTier] : OVERLAYS_ENERGY_IN_POWER_TT[mTier])
                    : GENNY
        };
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return null;
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_DebugPowerGenerator(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_DebugPowerGenerator(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("eEUT", EUT);
        aNBT.setInteger("eAMP", AMP);
        aNBT.setBoolean("eLaser", LASER);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        EUT = aNBT.getInteger("eEUT");
        AMP = aNBT.getInteger("eAMP");
        LASER = aNBT.getBoolean("eLaser");
        producing = (long) AMP * EUT >= 0;
        getBaseMetaTileEntity().setActive(producing);
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            aBaseMetaTileEntity.setActive(producing);
            if (!LASER) {
                if (aBaseMetaTileEntity.isActive()) {
                    setEUVar(maxEUStore());
                } else {
                    setEUVar(0);
                }
            } else {
                byte Tick = (byte) (aTick % 20);
                if (aBaseMetaTileEntity.isActive() && TRANSFER_AT == Tick) {
                    setEUVar(maxEUStore());
                    moveAround(aBaseMetaTileEntity);
                } else if (TRANSFER_AT == Tick) {
                    setEUVar(0);
                }
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        }
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
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
    public String[] getDescription() {
        return new String[] {
            CommonValues.TEC_MARK_GENERAL,
            translateToLocal("gt.blockmachines.debug.tt.genny.desc.0"), // Power from nothing
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.debug.tt.genny.desc.3"), //
            EnumChatFormatting.BLUE
                    + translateToLocal("gt.blockmachines.debug.tt.genny.desc.1"), // Infinite Producer/Consumer
            EnumChatFormatting.BLUE
                    + translateToLocal("gt.blockmachines.debug.tt.genny.desc.2") // Since i wanted one...
        };
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isEnetOutput() {
        return !LASER;
    }

    @Override
    public boolean isEnetInput() {
        return !LASER;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return !producing && aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return producing && aSide != getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public long maxAmperesIn() {
        return producing ? 0 : Math.abs(AMP);
    }

    @Override
    public long maxAmperesOut() {
        return producing ? Math.abs(AMP) : 0;
    }

    @Override
    public long maxEUInput() {
        return producing ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public long maxEUOutput() {
        return producing ? Math.abs(EUT) : 0;
    }

    @Override
    public long maxEUStore() {
        return LASER ? Math.abs((long) EUT * AMP * 24) : Math.abs((long) EUT * AMP) << 2;
    }

    @Override
    public long getMinimumStoredEU() {
        return Math.abs((long) EUT * AMP);
    }

    @Override
    public int getProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyStored();
    }

    @Override
    public int maxProgresstime() {
        return (int) getBaseMetaTileEntity().getUniversalEnergyCapacity();
    }

    @Override
    public boolean canConnect(byte side) {
        return LASER && side != getBaseMetaTileEntity().getFrontFacing();
    }

    private void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        for (byte face = 0; face < 6; face++) {
            if (face == aBaseMetaTileEntity.getFrontFacing()) continue;
            byte opposite = GT_Utility.getOppositeSide(face);
            for (short dist = 1; dist < 1000; dist++) {
                IGregTechTileEntity tGTTileEntity =
                        aBaseMetaTileEntity.getIGregTechTileEntityAtSideAndDistance(face, dist);
                if (tGTTileEntity != null) {
                    IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                    if (aMetaTileEntity != null) {
                        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyTunnel
                                && opposite == tGTTileEntity.getFrontFacing()) {
                            if (maxEUOutput() > ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUInput()) {
                                aMetaTileEntity.doExplosion(maxEUOutput());
                            } else {
                                long diff = Math.min(
                                        AMP * 20L * maxEUOutput(),
                                        Math.min(
                                                ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity).maxEUStore()
                                                        - aMetaTileEntity
                                                                .getBaseMetaTileEntity()
                                                                .getStoredEU(),
                                                aBaseMetaTileEntity.getStoredEU()));
                                ((GT_MetaTileEntity_Hatch_EnergyTunnel) aMetaTileEntity)
                                        .setEUVar(aMetaTileEntity
                                                        .getBaseMetaTileEntity()
                                                        .getStoredEU()
                                                + diff);
                            }
                        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Energy) {
                            if (((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).connectionCount < 2) {
                            } else {
                                ((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).markUsed();
                            }
                        }
                    }
                }
            }
        }
    }
}
