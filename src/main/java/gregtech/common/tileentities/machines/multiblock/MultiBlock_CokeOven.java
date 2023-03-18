package gregtech.common.tileentities.machines.multiblock;

import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.NOTHING;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.GT_Mod;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.multiblock.base.MultiBlockController;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;

public class MultiBlock_CokeOven extends MultiBlockController<MultiBlock_CokeOven> {

    private static IStructureDefinition<MultiBlock_CokeOven> STRUCTURE_DEFINITION = null;
    private static final int NORMAL_RECIPE_TIME = 1800;
    private static final int WOOD_ORE_ID = OreDictionary.getOreID("logWood");
    private static final int COAL_ORE_ID = OreDictionary.getOreID("coal");
    private static final int COAL_BLOCK_ORE_ID = OreDictionary.getOreID("blockCoal");
    private static final int SUGARCANE_ORE_ID = OreDictionary.getOreID("sugarcane");
    private static final int CACTUS_ORE_ID = OreDictionary.getOreID("blockCactus");
    private static final int CACTUS_CHARCOAL_ORE_ID = OreDictionary.getOreID("itemCharcoalCactus");
    private static final int SUGAR_CHARCOAL_ORE_ID = OreDictionary.getOreID("itemCharcoalSugar");
    private static final Vec3Impl offset = new Vec3Impl(1, 1, 0);
    private static final String MAIN = "Main";
    private int timeMultiplier = 1;

    public MultiBlock_CokeOven() {
        super();
        setElectric(false);
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
    public short getCasingMeta() {
        return GT_MultiTileCasing.CokeOven.getId();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Coke Oven").addInfo("Used for charcoal").beginStructureBlock(3, 3, 3, true)
                .addCasingInfoExactly("Coke Oven Bricks", 25, false).toolTipFinisher("BlueWeabo");
        return tt;
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return offset;
    }

    @Override
    public IStructureDefinition<MultiBlock_CokeOven> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MultiBlock_CokeOven>builder().addShape(
                    MAIN,
                    new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A-A", "AAA" }, { "AAA", "AAA", "AAA" } })
                    .addElement('A', addMultiTileCasing("gt.multitileentity.casings", getCasingMeta(), NOTHING))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected boolean checkRecipe() {
        if (getInventoriesForOutput().getStackInSlot(0) != null || getInventoriesForOutput().getStackInSlot(0).stackSize >= 64) {
            return false;
        }
        timeMultiplier = 1;
        ItemStack[] inputs = getAllItemInputs();
        if (inputs == null || inputs[0] == null) {
            return false;
        }
        ItemStack input = inputs[0];
        int originalStackSize = input.stackSize;
        ItemStack output = startRecipe(input);
        if (output == null || !output.isItemEqual(getInventoriesForOutput().getStackInSlot(0))) {
            return false;
        }

        setDuration(NORMAL_RECIPE_TIME * timeMultiplier);
        setItemOutputs(output);
        input.stackSize -= 1;

        return originalStackSize > input.stackSize;
    }

    protected ItemStack startRecipe(ItemStack input) {
        for (int oreId : OreDictionary.getOreIDs(input)) {
            if (oreId == COAL_ORE_ID) {
                return GT_OreDictUnificator.get("fuelCoke", null, 1);
            } else if (oreId == COAL_BLOCK_ORE_ID) {
                timeMultiplier = 9;
                return GT_ModHandler.getModItem("Railcraft", "cube", 1, 0);
            } else if (oreId == WOOD_ORE_ID) {
                return new ItemStack(Items.coal, 1, 1);
            } else if (oreId == SUGARCANE_ORE_ID) {
                return GT_OreDictUnificator.get("itemCharcoalSugar", null, 1);
            } else if (oreId == SUGAR_CHARCOAL_ORE_ID) {
                return GT_OreDictUnificator.get("itemCokeSugar", null, 1);
            } else if (oreId == CACTUS_ORE_ID) {
                return GT_OreDictUnificator.get("itemCharcoalCactus", null, 1);
            } else if (oreId == CACTUS_CHARCOAL_ORE_ID) {
                return GT_OreDictUnificator.get("itemCokeCactus", null, 1);
            }
        }
        return null;
    }

    @Override
    protected boolean hasFluidInput() {
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder,
            UIBuildContext buildContext) {
        
    }

    @Override
    protected void addTitleTextStyle(com.gtnewhorizons.modularui.api.screen.ModularWindow.Builder builder,
            String title) {
        final int TAB_PADDING = 3;
        final int TITLE_PADDING = 2;
        int titleWidth = 0, titleHeight = 0;
        if (NetworkUtils.isClient()) {
            final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            // noinspection unchecked
            final List<String> titleLines = fontRenderer
                    .listFormattedStringToWidth(title, getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2);
            titleWidth = titleLines.size() > 1 ? getGUIWidth() - (TAB_PADDING + TITLE_PADDING) * 2
                    : fontRenderer.getStringWidth(title);
            // noinspection PointlessArithmeticExpression
            titleHeight = titleLines.size() * fontRenderer.FONT_HEIGHT + (titleLines.size() - 1) * 1;
        }
    
        final DrawableWidget tab = new DrawableWidget();
        final TextWidget text = new TextWidget(title).setDefaultColor(getTitleColor())
                .setTextAlignment(Alignment.CenterLeft).setMaxWidth(titleWidth);
        if (GT_Mod.gregtechproxy.mTitleTabStyle == 1) {
            tab.setDrawable(getGUITextureSet().getTitleTabAngular()).setPos(0, -(titleHeight + TAB_PADDING) + 1)
                    .setSize(getGUIWidth(), titleHeight + TAB_PADDING * 2);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight + TAB_PADDING);
        } else {
            tab.setDrawable(getGUITextureSet().getTitleTabDark()).setPos(0, -(titleHeight + TAB_PADDING * 2) + 1)
                    .setSize(titleWidth + (TAB_PADDING + TITLE_PADDING) * 2, titleHeight + TAB_PADDING * 2 - 1);
            text.setPos(TAB_PADDING + TITLE_PADDING, -titleHeight);
        }
        builder.widget(tab).widget(text);
    }
}
