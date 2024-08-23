package com.github.technus.tectech.thing.metaTileEntity.single;

import static com.github.technus.tectech.thing.metaTileEntity.Textures.MACHINE_CASINGS_TT;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Consumer;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.StructureUtility;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UIInfos;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_TieredMachineBlock;
import gregtech.api.objects.GT_RenderedTexture;

/**
 * Created by Tec on 23.03.2017.
 */
public class GT_MetaTileEntity_DebugStructureWriter extends GT_MetaTileEntity_TieredMachineBlock
    implements IAddUIWidgets, IAddGregtechLogo {

    private static GT_RenderedTexture MARK;
    public short[] numbers = new short[6];
    public boolean size = false;
    public String[] result = new String[] { "Undefined" };

    public GT_MetaTileEntity_DebugStructureWriter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { CommonValues.TEC_MARK_GENERAL, translateToLocal("gt.blockmachines.debug.tt.writer.desc.0"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.writer.desc.1"),
                EnumChatFormatting.BLUE + translateToLocal("gt.blockmachines.debug.tt.writer.desc.2") });
        TT_Utility.setTier(aTier, this);
    }

    public GT_MetaTileEntity_DebugStructureWriter(String aName, int aTier, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
        TT_Utility.setTier(aTier, this);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_DebugStructureWriter(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        MARK = new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("iconsets/MARK"));
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[] { MACHINE_CASINGS_TT[mTier][colorIndex + 1],
            side != facing ? new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_TELEPORTER_ACTIVE) : MARK };
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
        for (int i = 0; i < numbers.length; i++) {
            aNBT.setShort("eData" + i, numbers[i]);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = aNBT.getShort("eData" + i);
        }
    }

    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        aBaseMetaTileEntity.disableWorking();
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isAllowedToWork()) {

            String pseudoJavaCode = StructureUtility.getPseudoJavaCode(
                aBaseMetaTileEntity.getWorld(),
                ExtendedFacing.of(aBaseMetaTileEntity.getFrontFacing()),
                aBaseMetaTileEntity.getXCoord(),
                aBaseMetaTileEntity.getYCoord(),
                aBaseMetaTileEntity.getZCoord(),
                numbers[0],
                numbers[1],
                numbers[2],
                te -> te.getClass()
                    .getCanonicalName(),
                numbers[3],
                numbers[4],
                numbers[5],
                false);
            TecTech.LOGGER.info(pseudoJavaCode);
            result = pseudoJavaCode.split("\\n");
            aBaseMetaTileEntity.disableWorking();
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        IGregTechTileEntity aBaseMetaTileEntity = getBaseMetaTileEntity();

        String pseudoJavaCode = StructureUtility.getPseudoJavaCode(
            aBaseMetaTileEntity.getWorld(),
            ExtendedFacing.of(aBaseMetaTileEntity.getFrontFacing()),
            aBaseMetaTileEntity.getXCoord(),
            aBaseMetaTileEntity.getYCoord(),
            aBaseMetaTileEntity.getZCoord(),
            numbers[0],
            numbers[1],
            numbers[2],
            te -> te.getClass()
                .getCanonicalName(),
            numbers[3],
            numbers[4],
            numbers[5],
            false);
        TecTech.LOGGER.info(pseudoJavaCode);
        result = pseudoJavaCode.split("\\n");
        aBaseMetaTileEntity.disableWorking();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GT_UIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return result;
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT_GRAY)
                .setSize(17, 17)
                .setPos(113, 56));
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setSize(90, 72)
                .setPos(43, 4))
            .widget(
                new TextWidget().setStringSupplier(() -> size ? "Structure size" : "My position")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 8))
            .widget(
                new TextWidget().setStringSupplier(() -> size ? "(Changing scan size)" : "(Moving origin)")
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 16))
            .widget(
                new TextWidget().setStringSupplier(() -> "A: " + numbers[size ? 3 : 0])
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 24))
            .widget(
                new TextWidget().setStringSupplier(() -> "B: " + numbers[size ? 4 : 1])
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 32))
            .widget(
                new TextWidget().setStringSupplier(() -> "C: " + numbers[size ? 5 : 2])
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(46, 40));

        addChangeNumberButtons(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_LARGE, -512, -64, 7);
        addChangeNumberButtons(builder, GT_UITextures.OVERLAY_BUTTON_MINUS_SMALL, -16, -1, 25);
        addChangeNumberButtons(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_SMALL, 16, 1, 133);
        addChangeNumberButtons(builder, GT_UITextures.OVERLAY_BUTTON_PLUS_LARGE, 512, 16, 151);
    }

    private void addChangeNumberButtons(ModularWindow.Builder builder, IDrawable overlay, int addNumberShift,
        int addNumber, int xPos) {
        addChangeNumberButton(
            builder,
            overlay,
            val -> numbers[size ? 3 : 0] += val,
            addNumberShift,
            addNumber,
            xPos,
            4);
        addChangeNumberButton(
            builder,
            overlay,
            val -> numbers[size ? 4 : 1] += val,
            addNumberShift,
            addNumber,
            xPos,
            22);
        addChangeNumberButton(
            builder,
            overlay,
            val -> numbers[size ? 5 : 2] += val,
            addNumberShift,
            addNumber,
            xPos,
            40);
        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> { size = !size; })
                .setBackground(GT_UITextures.BUTTON_STANDARD, overlay)
                .setSize(18, 18)
                .setPos(xPos, 58));
    }

    private void addChangeNumberButton(ModularWindow.Builder builder, IDrawable overlay, Consumer<Integer> setter,
        int changeNumberShift, int changeNumber, int xPos, int yPos) {
        builder.widget(
            new ButtonWidget()
                .setOnClick(
                    (clickData, widget) -> { setter.accept(clickData.shift ? changeNumberShift : changeNumber); })
                .setBackground(GT_UITextures.BUTTON_STANDARD, overlay)
                .setSize(18, 18)
                .setPos(xPos, yPos));
    }
}
