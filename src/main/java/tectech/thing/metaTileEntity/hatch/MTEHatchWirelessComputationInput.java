package tectech.thing.metaTileEntity.hatch;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.common.WirelessComputationPacket;
import tectech.thing.gui.TecTechUITextures;

public class MTEHatchWirelessComputationInput extends MTEHatchDataInput implements IAddGregtechLogo, IAddUIWidgets {

    public long requiredComputation = 10000;

    private String clientLocale = "en_US";

    public MTEHatchWirelessComputationInput(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchWirelessComputationInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchWirelessComputationInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        try {
            EntityPlayerMP player = (EntityPlayerMP) aPlayer;
            clientLocale = (String) FieldUtils.readField(player, "translator", true);
        } catch (Exception e) {
            clientLocale = "en_US";
        }
        if (!aPlayer.isUsingItem()) {
            GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && q == null) {
            q = WirelessComputationPacket.downloadData(aBaseMetaTileEntity.getOwnerUuid(), requiredComputation, aTick);
        }

    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_TECTECH_LOGO)
                .setSize(18, 18)
                .setPos(151, 63));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        requiredComputation = aNBT.getLong("requiredComputation");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("requiredComputation", requiredComputation);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            TextWidget.localised("tt.wirelessInputData.config.text")
                .setPos(20, 12)
                .setSize(140, 14))
            .widget(
                new TextFieldWidget().setSetterInt(val -> requiredComputation = val)
                    .setGetterLong(() -> requiredComputation)
                    .setNumbers(1, Integer.MAX_VALUE)
                    .setOnScrollNumbers(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(70, 18)
                    .setPos(54, 36)
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD));
    }

    private static String[] tooltips;

    @Override
    public String[] getDescription() {
        if (tooltips == null) {
            tooltips = new String[] { "Wireless Computation Data Input for Multiblocks" };
        }
        return tooltips;
    }
}
