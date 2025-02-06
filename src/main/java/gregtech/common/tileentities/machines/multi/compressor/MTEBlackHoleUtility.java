package gregtech.common.tileentities.machines.multi.compressor;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

public class MTEBlackHoleUtility extends MTEHatch {

    protected float threshold = 0;
    protected boolean inverted = false;
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
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
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
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    public int mode = 1;

    @Override
    public String[] getDescription() {
        return new String[] { "Optional hatch for Pseudostable Black Hole Containment Field.",
            "Mode 1: Emit a constant redstone signal when a black hole is open",
            "Mode 2: Emit a pulse every second while a black hole is open",
            "Pulse is perfectly synced to the internal timing of the machine",
            "Right click to open the GUI and change settings." };
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mode = aNBT.getInteger("mode");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mode", mode);
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
                    ret.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_DEFAULT);
                    return ret.toArray(new IDrawable[0]);
                })
                .attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> mode, (val) -> mode = val), builder)
                .addTooltip(StatCollector.translateToLocal("GT5U.gui.button.mode_switch"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(10, 8)
                .setSize(16, 16))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.heat_sensor"))
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(90, 30));
    }
}
