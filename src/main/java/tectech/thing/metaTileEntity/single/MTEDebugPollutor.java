package tectech.thing.metaTileEntity.single;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Consumer;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.common.pollution.Pollution;
import tectech.TecTech;
import tectech.util.CommonValues;

/**
 * Created by Tec on 23.03.2017.
 */
public class MTEDebugPollutor extends MTETieredMachineBlock implements IAddUIWidgets, IAddGregtechLogo {

    private static ITexture POLLUTOR;
    public int pollution = 0;
    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

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
        POLLUTOR = TextureFactory.of(new Textures.BlockIcons.CustomIcon("iconsets/POLLUTOR"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { Textures.BlockIcons.MACHINE_CASINGS[mTier][colorIndex + 1],
            (side == facing) ? POLLUTOR : Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI_LASER[mTier + 1] };
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
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        pollution = aNBT.getInteger("ePollution");
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (pollution > 0) {
                Pollution.addPollution(aBaseMetaTileEntity, pollution);
            }
        } else if (aBaseMetaTileEntity.isClientSide() && aBaseMetaTileEntity.isActive()) {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (side != aBaseMetaTileEntity.getFrontFacing()) {
                    TecTech.proxy.em_particle(aBaseMetaTileEntity, side);
                    TecTech.proxy.pollutor_particle(aBaseMetaTileEntity, side);
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

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY)
                .setSize(17, 17)
                .setPos(113, 56));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setSize(90, 72)
                .setPos(43, 4))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocal("tt.gui.text.debug_pollutor.pollution") + ": "
                            + numberFormat.format(pollution))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 8));

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_LARGE, val -> pollution -= val, 512, 64, 7, 4);
        addChangeNumberButton(
            builder,
            GTUITextures.OVERLAY_BUTTON_MINUS_LARGE,
            val -> pollution /= val,
            512,
            64,
            7,
            22);

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> pollution -= val, 16, 1, 25, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_MINUS_SMALL, val -> pollution /= val, 16, 2, 25, 22);

        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> pollution += val, 16, 1, 133, 4);
        addChangeNumberButton(builder, GTUITextures.OVERLAY_BUTTON_PLUS_SMALL, val -> pollution *= val, 16, 2, 133, 22);

        addChangeNumberButton(
            builder,
            GTUITextures.OVERLAY_BUTTON_PLUS_LARGE,
            val -> pollution += val,
            512,
            64,
            151,
            4);
        addChangeNumberButton(
            builder,
            GTUITextures.OVERLAY_BUTTON_PLUS_LARGE,
            val -> pollution *= val,
            512,
            64,
            151,
            22);

    }

    private void addChangeNumberButton(ModularWindow.Builder builder, IDrawable overlay, Consumer<Integer> setter,
        int changeNumberShift, int changeNumber, int xPos, int yPos) {
        builder.widget(
            new ButtonWidget()
                .setOnClick((clickData, widget) -> setter.accept(clickData.shift ? changeNumberShift : changeNumber))
                .setBackground(GTUITextures.BUTTON_STANDARD, overlay)
                .setSize(18, 18)
                .setPos(xPos, yPos));
    }
}
