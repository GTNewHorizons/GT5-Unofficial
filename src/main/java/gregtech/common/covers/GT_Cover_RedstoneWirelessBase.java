package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTech_API;
import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_NumericWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;

public abstract class GT_Cover_RedstoneWirelessBase extends GT_CoverBehavior {

    private static final int MAX_CHANNEL = 65535;
    private static final int PRIVATE_MASK = 0xFFFE0000;
    private static final int PUBLIC_MASK = 0x0000FFFF;
    private static final int CHECKBOX_MASK = 0x00010000;

    public GT_Cover_RedstoneWirelessBase(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean onCoverRemoval(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        boolean aForced) {
        GregTech_API.sWirelessRedstone.put(aCoverVariable, (byte) 0);
        return true;
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX,
        float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((side.offsetX != 0) && ((aY > 0.375D) && (aY < 0.625D)))) {
            GregTech_API.sWirelessRedstone.put(aCoverVariable.get(), (byte) 0);
            aCoverVariable.set(
                (aCoverVariable.get() & (PRIVATE_MASK | CHECKBOX_MASK))
                    | (((Integer) GT_Utility.stackToInt(aPlayer.inventory.getCurrentItem())).hashCode() & PUBLIC_MASK));
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("081", "Frequency: ") + aCoverVariable);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCoverRightclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((side.offsetX != 0) && ((aY > 0.375D) && (aY < 0.625D)))) {
            GregTech_API.sWirelessRedstone.put(aCoverVariable, (byte) 0);

            int val = GT_Utility.stackToInt(aPlayer.inventory.getCurrentItem())
                * (1 + aPlayer.inventory.getCurrentItem()
                    .getItemDamage());

            aCoverVariable = (aCoverVariable & (PRIVATE_MASK | CHECKBOX_MASK)) | (val & PUBLIC_MASK);

            aTileEntity.setCoverDataAtSide(side, aCoverVariable);
            GT_Utility
                .sendChatToPlayer(aPlayer, GT_Utility.trans("081", "Frequency: ") + (aCoverVariable & PUBLIC_MASK));
            return true;
        }
        return false;
    }

    @Override
    protected boolean isGUIClickableImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return false;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D))
            || ((side.offsetX == 0) || (((aY > 0.375D) && (aY < 0.625D)) || ((((aZ <= 0.375D) || (aZ >= 0.625D))))))) {
            GregTech_API.sWirelessRedstone.put(aCoverVariable, (byte) 0);
            final float[] tCoords = GT_Utility.getClickedFacingCoords(side, aX, aY, aZ);

            final short tAdjustVal = switch ((byte) ((byte) (int) (tCoords[0] * 2.0F)
                + 2 * (byte) (int) (tCoords[1] * 2.0F))) {
                case 0 -> -32;
                case 1 -> 32;
                case 2 -> -1024;
                case 3 -> 1024;
                default -> 0;
            };

            final int tPublicChannel = (aCoverVariable & PUBLIC_MASK) + tAdjustVal;

            if (tPublicChannel < 0) {
                aCoverVariable = aCoverVariable & ~PUBLIC_MASK;
            } else if (tPublicChannel > MAX_CHANNEL) {
                aCoverVariable = (aCoverVariable & (PRIVATE_MASK | CHECKBOX_MASK)) | MAX_CHANNEL;
            } else {
                aCoverVariable = (aCoverVariable & (PRIVATE_MASK | CHECKBOX_MASK)) | tPublicChannel;
            }
        }
        GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("081", "Frequency: ") + (aCoverVariable & PUBLIC_MASK));
        return aCoverVariable;
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public String getDescription(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return GT_Utility.trans("081", "Frequency: ") + aCoverVariable;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new RedstoneWirelessBaseUIFactory(buildContext).createWindow();
    }

    private class RedstoneWirelessBaseUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public RedstoneWirelessBaseUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getGUIWidth() {
            return 250;
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder
                .widget(
                    new CoverDataControllerWidget<>(
                        this::getCoverData,
                        this::setCoverData,
                        GT_Cover_RedstoneWirelessBase.this)

                            .addFollower(
                                new CoverDataFollower_NumericWidget<>(),
                                coverData -> (double) getFlagFrequency(convert(coverData)),
                                (coverData, state) -> new ISerializableObject.LegacyCoverData(
                                    state.intValue() | getFlagCheckbox(convert(coverData))),
                                widget -> widget.setBounds(0, MAX_CHANNEL)
                                    .setScrollValues(1, 1000, 10)
                                    .setFocusOnGuiOpen(true)
                                    .setPos(spaceX * 0, spaceY * 0 + 2)
                                    .setSize(spaceX * 4 - 3, 12))
                            .addFollower(
                                CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                coverData -> getFlagCheckbox(convert(coverData)) > 0,
                                (coverData, state) -> new ISerializableObject.LegacyCoverData(
                                    getFlagFrequency(convert(coverData)) | (state ? CHECKBOX_MASK : 0)),
                                widget -> widget.setPos(spaceX * 0, spaceY * 2))
                            .setPos(startX, startY))
                .widget(
                    new TextWidget(GT_Utility.trans("246", "Frequency")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 4, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GT_Utility.trans("602", "Use Private Frequency"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, startY + spaceY * 2 + 4));
        }

        private int getFlagFrequency(int coverVariable) {
            return coverVariable & PUBLIC_MASK;
        }

        private int getFlagCheckbox(int coverVariable) {
            return coverVariable & CHECKBOX_MASK;
        }
    }
}
