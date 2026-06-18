package gregtech.common.gui.mui1.cover;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverFluidfilter;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerSlotWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class FluidFilterUIFactory extends CoverUIFactory<CoverFluidfilter> {

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
                            .addTooltip(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.FilterInput"))
                            .setPos(spaceX * 0, spaceY * 0))
                    .addToggleButton(
                        1,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_EXPORT)
                            .addTooltip(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.FilterOutput"))
                            .setPos(spaceX * 1, spaceY * 0))
                    .addToggleButton(
                        2,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLOCK_INPUT)
                            .addTooltip(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.BlockOutput"))
                            .setPos(spaceX * 0, spaceY * 2))
                    .addToggleButton(
                        3,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_ALLOW_INPUT)
                            .addTooltip(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.AllowOutput"))
                            .setPos(spaceX * 1, spaceY * 2))
                    .addToggleButton(
                        4,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_WHITELIST)
                            .addTooltip(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.WhitelistFluid"))
                            .setPos(spaceX * 0, spaceY * 1))
                    .addToggleButton(
                        5,
                        CoverDataFollowerToggleButtonWidget.ofDisableable(),
                        widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_BLACKLIST)
                            .addTooltip(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.BlacklistFluid"))
                            .setPos(spaceX * 1, spaceY * 1))
                    .addFollower(new CoverDataFollowerSlotWidget<CoverFluidfilter>(new ItemStackHandler(), 0, true) {

                        @Override
                        protected void putClickedStack(ItemStack stack, int mouseButton) {
                            if (stack != null && GTUtility.getFluidFromContainerOrFluidDisplay(stack) == null) return;
                            super.putClickedStack(
                                GTUtility
                                    .getFluidDisplayStack(GTUtility.getFluidFromContainerOrFluidDisplay(stack), false),
                                mouseButton);
                        }
                    }, this::getFluidDisplayItem, (coverData, stack) -> {
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
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.Direction"))
                    .setPos(startX + spaceX * 2, 3 + startY + spaceY * 0))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.Type"))
                    .setPos(startX + spaceX * 2, 3 + startY + spaceY * 1))
            .widget(
                new TextWidget(StatCollector.translateToLocal("gt.interact.desc.FluidFilter.BlockFlow"))
                    .setPos(startX + spaceX * 2, 3 + startY + spaceY * 2))
            .widget(TextWidget.dynamicString(() -> {
                CoverFluidfilter cover = getCover();
                if (cover != null) {
                    ItemStack fluidDisplay = getFluidDisplayItem(cover);
                    if (fluidDisplay != null) {
                        return fluidDisplay.getDisplayName();
                    }
                }
                return (StatCollector.translateToLocal("gt.interact.desc.FluidFilter.Empty"));
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
