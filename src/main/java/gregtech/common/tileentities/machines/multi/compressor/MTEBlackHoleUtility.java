package gregtech.common.tileentities.machines.multi.compressor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEBlackHoleUtilityGui;

public class MTEBlackHoleUtility extends MTEHatch {

    private boolean isOn = false;

    private static final IIconContainer textureFont = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR;
    private static final IIconContainer textureFont_Glow = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR_GLOW;

    public MTEBlackHoleUtility(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Optional hatch for Pseudostable Black Hole Containment Field.");
    }

    public MTEBlackHoleUtility(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection Side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().setActive(true);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        openGui(aPlayer);
        return true;
    }

    // 1 -> static
    // 2 -> pulse
    public int mode = 1;

    public boolean getMode() {
        return mode == 1;
    }

    public void setMode(boolean mode) {
        this.mode = mode ? 1 : 2;
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Optional hatch for Pseudostable Black Hole Containment Field.",
            "Static Mode: Emit a constant redstone signal when a black hole is open",
            "Pulse Mode: Emit a pulse every second while a black hole is open",
            "Pulse is perfectly synced to the internal timing of the machine",
            "Right click to open the GUI and change settings." };
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mode = aNBT.getInteger("mode");
        pulseTimer = aNBT.getInteger("pulseTimer");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mode", mode);
        aNBT.setInteger("pulseTimer", pulseTimer);
        super.saveNBTData(aNBT);
    }

    /**
     * Updates redstone update based on black hole status
     */
    public void updateRedstoneOutput(boolean machineOn) {
        isOn = machineOn;
        if (mode == 2) pulseTimer = 5;
    }

    // Redstone pulse will be 5 ticks (0.25s)
    int pulseTimer = 5;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mode == 2) {
            if (pulseTimer > 0) {
                pulseTimer--;
            } else isOn = false;
        }
        if (isOn) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setStrongOutputRedstoneSignal(side, (byte) 15);
            }
        } else {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setStrongOutputRedstoneSignal(side, (byte) 0);
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBlackHoleUtility(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(textureFont), TextureFactory.builder()
            .addIcon(textureFont_Glow)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(textureFont) };
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEBlackHoleUtilityGui(this).build(data, syncManager, uiSettings);
    }
}
