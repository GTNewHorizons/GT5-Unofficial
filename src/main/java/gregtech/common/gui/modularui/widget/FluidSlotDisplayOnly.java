package gregtech.common.gui.modularui.widget;

import static com.cleanroommc.modularui.ModularUI.isGT5ULoaded;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.input.Keyboard;

import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.drawable.GuiDraw;
import com.cleanroommc.modularui.drawable.text.TextRenderer;
import com.cleanroommc.modularui.integration.nei.NEIDragAndDropHandler;
import com.cleanroommc.modularui.integration.nei.NEIIngredientProvider;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetSlotTheme;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.utils.GlStateManager;
import com.cleanroommc.modularui.utils.NumberFormat;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.widget.Widget;

import gregtech.api.util.GTUtility;

public class FluidSlotDisplayOnly extends Widget<FluidSlotDisplayOnly>
    implements Interactable, NEIDragAndDropHandler, NEIIngredientProvider {

    public static final int DEFAULT_SIZE = 18;

    private static final String UNIT_BUCKET = "B";
    private static final String UNIT_LITER = "L";

    private static final IFluidTank EMPTY = new FluidTank(0);
    private static final NumberFormat.Params SLOT_FORMAT_PARAMS = NumberFormat.DEFAULT.copyToBuilder()
        .maxLength(3)
        .build();

    private final TextRenderer textRenderer = new TextRenderer();
    private FluidDisplaySyncHandler syncHandler;
    private int contentOffsetX = 1, contentOffsetY = 1;
    private boolean alwaysShowFull = true;
    @Nullable
    private IDrawable overlayTexture = null;
    private Supplier<Double> capacityPercentSupplier = null;

    public FluidSlotDisplayOnly() {
        size(DEFAULT_SIZE);
        tooltip().setAutoUpdate(true);// .setHasTitleMargin(true);
        tooltipBuilder(this::addToolTip);
    }

    public FluidSlotDisplayOnly(Supplier<Double> capacitySupplier) {
        this();
        this.capacityPercentSupplier = capacitySupplier;
    }

    protected void addToolTip(RichTooltip tooltip) {
        FluidStack fluid = this.syncHandler.getValue();
        if (fluid != null) {
            tooltip.addFromFluid(fluid);
        } else {
            tooltip.addLine(IKey.lang("modularui2.fluid.empty"));
        }
    }

    public void addAdditionalFluidInfo(RichTooltip tooltip, FluidStack fluidStack) {
        tooltip.addAdditionalInfoFromFluid(fluidStack);
    }

    public String formatFluidAmount(double amount) {
        return NumberFormat.format(getBaseUnitAmount(amount), SLOT_FORMAT_PARAMS);
    }

    protected double getBaseUnitAmount(double amount) {
        return amount;
    }

    protected String getBaseUnit() {
        return UNIT_LITER;
    }

    @Override
    public void onInit() {
        textRenderer.setShadow(true);
        textRenderer.setScale(0.5f);
        this.textRenderer.setColor(Color.WHITE.main);
    }

    @Override
    public boolean isValidSyncHandler(SyncHandler syncHandler) {
        this.syncHandler = castIfTypeElseNull(syncHandler, FluidDisplaySyncHandler.class);
        return this.syncHandler != null;
    }

    @Override
    public void draw(ModularGuiContext context, WidgetTheme widgetTheme) {
        FluidStack content = getFluidStack();
        if (content != null) {
            float y = this.contentOffsetY + getArea().height
                * (1 - (float) (this.capacityPercentSupplier == null ? 1 : this.capacityPercentSupplier.get()));
            float height = getArea().height
                * (float) (this.capacityPercentSupplier == null ? 1 : this.capacityPercentSupplier.get());
            GuiDraw.drawFluidTexture(
                content,
                this.contentOffsetX,
                y,
                getArea().width - this.contentOffsetX * 2,
                height - this.contentOffsetY * 2,
                0);
        }
        if (this.overlayTexture != null) {
            this.overlayTexture.drawAtZero(context, getArea(), widgetTheme);
        }
        if (content != null && this.syncHandler.controlsAmount()) {
            String s = NumberFormat.format(getBaseUnitAmount(content.amount), SLOT_FORMAT_PARAMS) + getBaseUnit();
            this.textRenderer.setAlignment(Alignment.CenterRight, getArea().width - this.contentOffsetX - 1f);
            this.textRenderer.setPos((int) (this.contentOffsetX + 0.5f), (int) (getArea().height - 5.5f));
            this.textRenderer.draw(s);
        }
        if (isHovering()) {
            GlStateManager.colorMask(true, true, true, false);
            GuiDraw.drawRect(1, 1, getArea().w() - 2, getArea().h() - 2, getSlotHoverColor());
            GlStateManager.colorMask(true, true, true, true);
        }
    }

    @Override
    public WidgetSlotTheme getWidgetThemeInternal(ITheme theme) {
        return theme.getFluidSlotTheme();
    }

    public int getSlotHoverColor() {
        WidgetTheme theme = getWidgetTheme(getContext().getTheme());
        if (theme instanceof WidgetSlotTheme slotTheme) {
            return slotTheme.getSlotHoverColor();
        }
        return ITheme.getDefault()
            .getFluidSlotTheme()
            .getSlotHoverColor();
    }

    @NotNull
    @Override
    public Result onMouseTapped(int mouseButton) {
        return Result.SUCCESS;
    }

    @Override
    public boolean onMouseScroll(ModularScreen.UpOrDown scrollDirection, int amount) {
        return false;
    }

    @Override
    public @NotNull Result onKeyPressed(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_LSHIFT || keyCode == Keyboard.KEY_RSHIFT) {
            markTooltipDirty();
        }
        return Interactable.super.onKeyPressed(typedChar, keyCode);
    }

    @Override
    public boolean onKeyRelease(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_LSHIFT || keyCode == Keyboard.KEY_RSHIFT) {
            markTooltipDirty();
        }
        return Interactable.super.onKeyRelease(typedChar, keyCode);
    }

    @Nullable
    public FluidStack getFluidStack() {
        return this.syncHandler == null ? null : this.syncHandler.getValue();
    }

    /**
     * Set the offset in x and y (on both sides) at which the fluid should be rendered.
     * Default is 1 for both.
     *
     * @param x x offset
     * @param y y offset
     */
    public FluidSlotDisplayOnly contentOffset(int x, int y) {
        this.contentOffsetX = x;
        this.contentOffsetY = y;
        return this;
    }

    /**
     * @param alwaysShowFull if the fluid should be rendered as full or as the partial amount.
     */
    public FluidSlotDisplayOnly alwaysShowFull(boolean alwaysShowFull) {
        this.alwaysShowFull = alwaysShowFull;
        return this;
    }

    /**
     * @param overlayTexture texture that is rendered on top of the fluid
     */
    public FluidSlotDisplayOnly overlayTexture(@Nullable IDrawable overlayTexture) {
        this.overlayTexture = overlayTexture;
        return this;
    }

    public FluidSlotDisplayOnly syncHandler(Supplier<FluidStack> fluidGetter) {
        return syncHandler(new FluidDisplaySyncHandler(fluidGetter));
    }

    public FluidSlotDisplayOnly syncHandler(FluidDisplaySyncHandler syncHandler) {
        setSyncHandler(syncHandler);
        this.syncHandler = syncHandler;
        return this;
    }

    @Override
    public boolean handleDragAndDrop(@NotNull ItemStack draggedStack, int button) {
        return false;
    }

    protected void setPhantomValue(@NotNull ItemStack draggedStack) {
        this.syncHandler.setValue(FluidContainerRegistry.getFluidForFilledItem(draggedStack));
    }

    @Override
    public @Nullable ItemStack getStackForNEI() {
        if (isGT5ULoaded) {
            return GTUtility.getFluidDisplayStack(getFluidStack(), false);
        }
        return null;
    }
}
