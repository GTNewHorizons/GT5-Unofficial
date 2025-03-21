package gregtech.common.covers;

import static gregtech.api.enums.GTValues.E;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerSlotWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class CoverFluidfilter extends CoverBehaviorBase {

    // Uses the lower 3 bits of the cover variable, so we have 8 options to work with (0-7)
    private final int FILTER_INPUT_DENY_OUTPUT = 0; // 000
    private final int INVERT_INPUT_DENY_OUTPUT = 1; // 001
    private final int FILTER_INPUT_ANY_OUTPUT = 2; // 010
    private final int INVERT_INPUT_ANY_OUTPUT = 3; // 011
    private final int DENY_INPUT_FILTER_OUTPUT = 4; // 100
    private final int DENY_INPUT_INVERT_OUTPUT = 5; // 101
    private final int ANY_INPUT_FILTER_OUTPUT = 6; // 110
    private final int ANY_INPUT_INVERT_OUTPUT = 7; // 111

    private int mFluidID;
    private int mFilterMode;

    public CoverFluidfilter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public int getFluidId() {
        return mFluidID;
    }

    public CoverFluidfilter setFluidId(int fluidId) {
        this.mFluidID = fluidId;
        return this;
    }

    public int getFilterMode() {
        return mFilterMode;
    }

    public CoverFluidfilter setFilterMode(int filterMode) {
        this.mFilterMode = filterMode;
        return this;
    }

    @Override
    protected void initializeData() {
        this.mFluidID = -1;
        this.mFilterMode = 0;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        if (nbt instanceof NBTTagCompound tNBT) {
            mFilterMode = tNBT.getInteger("mFilterMode");
            if (tNBT.hasKey("mFluid", NBT.TAG_STRING)) mFluidID = FluidRegistry.getFluidID(tNBT.getString("mFluid"));
            else mFluidID = -1;
        } else {
            initializeData();
        }
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        mFilterMode = byteData.readByte();
        mFluidID = byteData.readInt();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tNBT = new NBTTagCompound();
        tNBT.setInteger("mFilterMode", mFilterMode);
        if (mFluidID >= 0) tNBT.setString(
            "mFluid",
            FluidRegistry.getFluid(mFluidID)
                .getName());
        return tNBT;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(mFilterMode)
            .writeInt(mFluidID);
    }

    @Override
    public String getDescription() {
        final Fluid fluid = FluidRegistry.getFluid(mFluidID);
        if (fluid == null) return E;

        final FluidStack sFluid = new FluidStack(fluid, 1000);
        return (String.format("Filtering Fluid: %s - %s", sFluid.getLocalizedName(), getFilterMode(mFilterMode)));
    }

    public String getFilterMode(int aFilterMode) {
        return switch (aFilterMode) {
            case FILTER_INPUT_DENY_OUTPUT -> GTUtility.trans("043", "Filter input, Deny output");
            case INVERT_INPUT_DENY_OUTPUT -> GTUtility.trans("044", "Invert input, Deny output");
            case FILTER_INPUT_ANY_OUTPUT -> GTUtility.trans("045", "Filter input, Permit any output");
            case INVERT_INPUT_ANY_OUTPUT -> GTUtility.trans("046", "Invert input, Permit any output");
            case DENY_INPUT_FILTER_OUTPUT -> GTUtility.trans("307", "Deny input, Filter output");
            case DENY_INPUT_INVERT_OUTPUT -> GTUtility.trans("308", "Deny input, Invert output");
            case ANY_INPUT_FILTER_OUTPUT -> GTUtility.trans("309", "Permit any input, Filter output");
            case ANY_INPUT_INVERT_OUTPUT -> GTUtility.trans("310", "Permit any input, Invert output");
            default -> ("UNKNOWN");
        };
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mFilterMode = (mFilterMode + (aPlayer.isSneaking() ? -1 : 1)) % 8;
        if (mFilterMode < 0) {
            mFilterMode = 7;
        }

        GTUtility.sendChatToPlayer(aPlayer, getFilterMode(mFilterMode));
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (coverSide == ForgeDirection.UNKNOWN) return false;
        if (((aX > 0.375D) && (aX < 0.625D)) || ((coverSide.offsetX != 0) && ((aY > 0.375D) && (aY < 0.625D)))
            || (coverSide.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) != 0 && aZ > 0.375D && aZ < 0.625D
            || (coverSide.offsetZ != 0)) {
            final ItemStack tStack = aPlayer.inventory.getCurrentItem();
            if (tStack == null) return true;

            final FluidStack tFluid = GTUtility.getFluidForFilledItem(tStack, true);
            if (tFluid != null) {
                final int aFluid = tFluid.getFluidID();
                mFluidID = aFluid;
                final FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluid), 1000);
                GTUtility
                    .sendChatToPlayer(aPlayer, GTUtility.trans("047", "Filter Fluid: ") + sFluid.getLocalizedName());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        if (aFluid == null) return true;

        int aFilterMode = mFilterMode;
        int aFilterFluid = mFluidID;

        if (aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == DENY_INPUT_INVERT_OUTPUT) return false;
        else if (aFilterMode == ANY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT) return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == FILTER_INPUT_ANY_OUTPUT;
        else return aFilterMode == INVERT_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        if (aFluid == null) return true;

        int aFilterMode = mFilterMode;
        int aFilterFluid = mFluidID;

        if (aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_DENY_OUTPUT) return false;
        else if (aFilterMode == FILTER_INPUT_ANY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT) return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_FILTER_OUTPUT;
        else return aFilterMode == DENY_INPUT_INVERT_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FluidFilterUIFactory(buildContext).createWindow();
    }

    private static class FluidFilterUIFactory extends UIFactory<CoverFluidfilter> {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public FluidFilterUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected CoverFluidfilter adaptCover(Cover cover) {
            if (cover instanceof CoverFluidfilter adapterCover) {
                return adapterCover;
            }
            return null;
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder.widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCover,
                    (id, coverData) -> !getClickable(id, coverData.getFilterMode()),
                    (id, coverData) -> coverData.setFilterMode(getNewFilterMode(id, coverData.getFilterMode())),
                    getUIBuildContext())
                        .addToggleButton(
                            0,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_IMPORT)
                                .addTooltip(GTUtility.trans("232", "Filter Input"))
                                .setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                                .addTooltip(GTUtility.trans("233", "Filter Output"))
                                .setPos(spaceX * 1, spaceY * 0))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT)
                                .addTooltip(GTUtility.trans("234", "Block Output"))
                                .setPos(spaceX * 0, spaceY * 2))
                        .addToggleButton(
                            3,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT)
                                .addTooltip(GTUtility.trans("235", "Allow Output"))
                                .setPos(spaceX * 1, spaceY * 2))
                        .addToggleButton(
                            4,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_WHITELIST)
                                .addTooltip(GTUtility.trans("236", "Whitelist Fluid"))
                                .setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(
                            5,
                            CoverDataFollowerToggleButtonWidget.ofDisableable(),
                            widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLACKLIST)
                                .addTooltip(GTUtility.trans("237", "Blacklist Fluid"))
                                .setPos(spaceX * 1, spaceY * 1))
                        .addFollower(
                            new CoverDataFollowerSlotWidget<CoverFluidfilter>(new ItemStackHandler(), 0, true) {

                                @Override
                                protected void putClickedStack(ItemStack stack, int mouseButton) {
                                    if (stack != null && GTUtility.getFluidFromContainerOrFluidDisplay(stack) == null)
                                        return;
                                    super.putClickedStack(
                                        GTUtility.getFluidDisplayStack(
                                            GTUtility.getFluidFromContainerOrFluidDisplay(stack),
                                            false),
                                        mouseButton);
                                }
                            },
                            this::getFluidDisplayItem,
                            (coverData, stack) -> {
                                if (stack == null) {
                                    coverData.setFluidId(-1);
                                } else {
                                    FluidStack fluid = GTUtility.getFluidFromDisplayStack(stack);
                                    if (fluid != null && fluid.getFluid() != null) {
                                        coverData.setFluidId(
                                            fluid.getFluid()
                                                .getID());
                                    }
                                }
                                return coverData;
                            },
                            widget -> widget.setBackground(ModularUITextures.FLUID_SLOT)
                                .setPos(0, spaceY * 3 + 2))
                        .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("238", "Filter Direction")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 2, 3 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("239", "Filter Type")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 2, 3 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("240", "Block Flow")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 2, 3 + startY + spaceY * 2))
                .widget(TextWidget.dynamicString(() -> {
                    CoverFluidfilter cover = getCover();
                    if (cover != null) {
                        ItemStack fluidDisplay = getFluidDisplayItem(cover);
                        if (fluidDisplay != null) {
                            return fluidDisplay.getDisplayName();
                        }
                    }
                    return GTUtility.trans("315", "Filter Empty");
                })
                    .setSynced(false)
                    .setDefaultColor(COLOR_TITLE.get())
                    .setPos(startX + spaceX + 3, 4 + startY + spaceY * 3));
        }

        private int getNewFilterMode(int id, int filterMode) {
            return switch (id) {
                case 0 -> (filterMode & 0x3);
                case 1 -> (filterMode | 0x4);
                case 2 -> (filterMode & 0x5);
                case 3 -> (filterMode | 0x2);
                case 4 -> (filterMode & 0x6);
                case 5 -> (filterMode | 0x1);
                default -> filterMode;
            };
        }

        private boolean getClickable(int id, int filterMode) {
            return switch (id) {
                case 0, 1 -> (filterMode >> 2 & 0x1) != (id & 0x1);
                case 2, 3 -> (filterMode >> 1 & 0x1) != (id & 0x1);
                case 4, 5 -> (filterMode & 0x1) != (id & 0x1);
                default -> false;
            };
        }

        private ItemStack getFluidDisplayItem(CoverFluidfilter coverData) {
            if (coverData == null) return null;
            Fluid fluid = FluidRegistry.getFluid(coverData.getFluidId());
            return GTUtility.getFluidDisplayStack(fluid);
        }
    }
}
