package gregtech.common.tileentities.machines.multiblock;

import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ITEM_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ITEM_OUT;
import static gregtech.api.util.GT_StructureUtilityMuTE.ofMuTECasings;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.multiblock.base.Controller;
import gregtech.api.task.tasks.PollutionTask;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.multiblock.logic.CokeOvenProcessingLogic;

public class CokeOven extends Controller<CokeOven, CokeOvenProcessingLogic> {

    private static IStructureDefinition<CokeOven> STRUCTURE_DEFINITION = null;
    private static final Vec3Impl OFFSET = new Vec3Impl(1, 1, 0);
    private static final String MAIN = "Main";
    private static final int POLLUTION_AMOUNT = 10;

    public CokeOven() {
        super();
        setElectric(false);
        new PollutionTask<>(this).setPollutionPerSecond(POLLUTION_AMOUNT);
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        buildState.startBuilding(getStartingStructureOffset());
        buildPiece(MAIN, trigger, hintsOnly, buildState.stopBuilding());
    }

    @Override
    public boolean checkMachine() {
        buildState.startBuilding(getStartingStructureOffset());
        return checkPiece(MAIN, buildState.stopBuilding());
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        buildState.startBuilding(getStartingStructureOffset());
        return survivalBuildPiece(MAIN, trigger, buildState.stopBuilding(), elementBudget, env, false);
    }

    @Override
    public short getCasingRegistryID() {
        return 0;
    }

    @Override
    public int getCasingMeta() {
        return GT_MultiTileCasing.CokeOven.getId();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Coke Oven")
            .addInfo("Used for charcoal")
            .beginStructureBlock(3, 3, 3, true)
            .addCasingInfoExactly("Coke Oven Bricks", 25, false)
            .addPollutionAmount(POLLUTION_AMOUNT)
            .toolTipFinisher(GTValues.AuthorBlueWeabo);
        return tt;
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return OFFSET;
    }

    @Override
    public IStructureDefinition<CokeOven> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<CokeOven>builder()
                .addShape(
                    MAIN,
                    new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A-A", "AAA" }, { "AAA", "AAA", "AAA" } })
                .addElement('A', ofMuTECasings(ITEM_IN | ITEM_OUT, GT_MultiTileCasing.CokeOven.getCasing()))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean hasFluidInput() {
        return false;
    }

    @Override
    protected void addTitleTextStyle(ModularWindow.Builder builder, String title) {
        final int TAB_PADDING = 3;
        final int TITLE_PADDING = 2;
        int titleWidth = 0, titleHeight = 0;
        if (NetworkUtils.isClient()) {
            final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            final List<String> titleLines = fontRenderer
                .listFormattedStringToWidth(title, getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2);
            titleWidth = titleLines.size() > 1 ? getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2
                : fontRenderer.getStringWidth(title);
            titleHeight = titleLines.size() * fontRenderer.FONT_HEIGHT + (titleLines.size() - 1);
        }

        final DrawableWidget tab = new DrawableWidget();
        final TextWidget text = new TextWidget(title).setDefaultColor(getTitleColor())
            .setTextAlignment(Alignment.CenterLeft)
            .setMaxWidth(titleWidth);
        if (GTMod.gregtechproxy.mTitleTabStyle == 1) {
            tab.setDrawable(getGUITextureSet().getTitleTabAngular())
                .setPos(0, -(titleHeight + TAB_PADDING) + 1)
                .setSize(getGUIWidth(), titleHeight + TAB_PADDING * 2);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight + TAB_PADDING);
        } else {
            tab.setDrawable(getGUITextureSet().getTitleTabDark())
                .setPos(0, -(titleHeight + TAB_PADDING * 2) + 1)
                .setSize(titleWidth + (TAB_PADDING + TITLE_PADDING) * 2, titleHeight + TAB_PADDING * 2 - 1);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight);
        }
        builder.widget(tab)
            .widget(text);
    }

    @Override
    public String getLocalName() {
        return StatCollector.translateToLocal(getTileEntityName());
    }

    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.cokeOven";
    }

    @Override
    @Nonnull
    protected CokeOvenProcessingLogic createProcessingLogic() {
        return new CokeOvenProcessingLogic();
    }
}
