package gregtech.common.tileentities.machines.multi.compressor;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicTextWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

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

    public int mode = 1;

    @Override
    public String[] getDescription() {
        return new String[] { "gt.te.black_hole_util_hatch.desc" };
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

    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> mode = (mode == 1) ? 2 : 1)
                .setPlayClickSound(true)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add((mode == 1) ? GTUITextures.OVERLAY_BUTTON_REDSTONE_ON : GTUITextures.OVERLAY_BUTTON_ANALOG);
                    return ret.toArray(new IDrawable[0]);
                })
                .attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> mode, (val) -> mode = val), builder)
                .setPos(10, 8)
                .setSize(16, 16))
            .widget(
                new DynamicTextWidget(
                    () -> new Text(
                        StatCollector
                            .translateToLocal((mode == 1) ? "GT5U.gui.text.static_mode" : "GT5U.gui.text.pulse_mode")))
                                .setPos(31, 13));
    }
}
