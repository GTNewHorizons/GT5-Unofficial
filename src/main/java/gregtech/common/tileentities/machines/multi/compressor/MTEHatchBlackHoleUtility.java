package gregtech.common.tileentities.machines.multi.compressor;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchBlackHoleUtilityGui;
import gregtech.common.tileentities.machines.multi.MTEHatchRedstoneBase;

public class MTEHatchBlackHoleUtility extends MTEHatchRedstoneBase {

    private static final IIconContainer textureFont = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR;
    private static final IIconContainer textureFont_Glow = Textures.BlockIcons.OVERLAY_HATCH_HEAT_SENSOR_GLOW;

    public MTEHatchBlackHoleUtility(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Optional hatch for Pseudostable Black Hole Containment Field.");
    }

    public MTEHatchBlackHoleUtility(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
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
    public boolean supportsInvertedSignal() {
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mode = aNBT.getInteger("mode");
        super.loadNBTData(aNBT);
        if (mode == 2) {
            setRedstoneSignal(false);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mode", mode);
        super.saveNBTData(aNBT);
    }

    public void cycleStart() {
        setRedstoneSignal(true);
    }

    public void cycleMiddle() {
        if (mode == 2) {
            setRedstoneSignal(false);
        }
    }

    public void blackHoleClosed() {
        setRedstoneSignal(false);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchBlackHoleUtility(mName, mTier, mDescriptionArray, mTextures);
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
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchBlackHoleUtilityGui(this).build(data, syncManager, uiSettings);
    }
}
