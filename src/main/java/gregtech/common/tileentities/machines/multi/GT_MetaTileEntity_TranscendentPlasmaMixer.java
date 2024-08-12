package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorColen;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static gregtech.common.misc.WirelessNetworkManager.processInitialSettings;
import static gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_PlasmaForge.DIM_BRIDGE_CASING;
import static gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_PlasmaForge.DIM_INJECTION_CASING;
import static gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_PlasmaForge.DIM_TRANS_CASING;
import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import gregtech.api.GregTech_API;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;

public class GT_MetaTileEntity_TranscendentPlasmaMixer
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_TranscendentPlasmaMixer>
    implements ISurvivalConstructable {

    private static final String[][] structure = new String[][] {
        { " CAC ", " ABA ", " ABA ", " A~A ", " ABA ", " ABA ", " CAC " },
        { "CBBBC", "A   A", "A   A", "A   A", "A   A", "A   A", "CBBBC" },
        { "ABBBA", "B   B", "B   B", "B   B", "B   B", "B   B", "ABBBA" },
        { "CBBBC", "A   A", "A   A", "A   A", "A   A", "A   A", "CBBBC" },
        { " CAC ", " ABA ", " ABA ", " ABA ", " ABA ", " ABA ", " CAC " } };

    private static final String STRUCTURE_PIECE_MAIN = "MAIN";
    private static final IStructureDefinition<GT_MetaTileEntity_TranscendentPlasmaMixer> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_TranscendentPlasmaMixer>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement(
            'B',
            buildHatchAdder(GT_MetaTileEntity_TranscendentPlasmaMixer.class)
                .atLeast(InputHatch, OutputHatch, InputBus, Maintenance)
                .casingIndex(DIM_INJECTION_CASING)
                .dot(1)
                .buildAndChain(GregTech_API.sBlockCasings1, DIM_INJECTION_CASING))
        .addElement('A', ofBlock(GregTech_API.sBlockCasings1, DIM_TRANS_CASING))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings1, DIM_BRIDGE_CASING))
        .build();

    private UUID ownerUUID;

    public GT_MetaTileEntity_TranscendentPlasmaMixer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_TranscendentPlasmaMixer(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_TranscendentPlasmaMixer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Transcendent Mixer")
            .addInfo("Assisting in all your DTPF needs.")
            .addInfo("This multiblock will run in parallel according to the amount set")
            .addInfo("in the parallel menu. All inputs will scale, except time.")
            .addInfo("All EU is deducted from wireless EU networks only.")
            .addInfo(AuthorColen)
            .addSeparator()
            .beginStructureBlock(5, 7, 5, false)
            .addStructureInfo(GOLD + "1+ " + GRAY + "Input Hatch")
            .addStructureInfo(GOLD + "1+ " + GRAY + "Output Hatch")
            .addStructureInfo(GOLD + "1+ " + GRAY + "Input Bus")
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_TranscendentPlasmaMixer(mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][DIM_TRANS_CASING], TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_ON)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FUSION1_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][DIM_TRANS_CASING], TextureFactory.builder()
                .addIcon(OVERLAY_DTPF_OFF)
                .extFacing()
                .build() };
        }

        return new ITexture[] { casingTexturePages[0][DIM_TRANS_CASING] };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    int multiplier = 1;
    long mWirelessEUt = 0;

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.transcendentPlasmaMixerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            BigInteger recipeEU;

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
                BigInteger availableEU = getUserEU(ownerUUID);
                recipeEU = BigInteger.valueOf(10L * recipe.mEUt * recipe.mDuration);
                if (availableEU.compareTo(recipeEU) < 0) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(recipeEU);
                }
                maxParallel = availableEU.divide(recipeEU)
                    .min(BigInteger.valueOf(maxParallel))
                    .intValue();
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GT_Recipe recipe) {
                BigInteger finalConsumption = recipeEU.multiply(BigInteger.valueOf(-calculatedParallels));
                // This will void the inputs if wireless energy has dropped
                // below the required amount between validateRecipe and here.
                if (!addEUToGlobalEnergyMap(ownerUUID, finalConsumption)) {
                    return CheckRecipeResultRegistry.insufficientStartupPower(finalConsumption);
                }
                // Energy consumed all at once from wireless net.
                setCalculatedEut(0);
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return GT_OverclockCalculator.ofNoOverclock(recipe);
            }
        }.setMaxParallelSupplier(() -> multiplier);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        // The voltage is only used for recipe finding
        logic.setAvailableVoltage(Long.MAX_VALUE);
        logic.setAvailableAmperage(1);
        logic.setAmperageOC(false);
    }

    @Override
    protected long getActualEnergyUsage() {
        return mWirelessEUt;
    }

    private static final int HORIZONTAL_OFFSET = 2;
    private static final int VERTICAL_OFFSET = 3;
    private static final int DEPTH_OFFSET = 0;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        // Check the main structure
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET)) {
            return false;
        }

        // Maintenance hatch not required but left for compatibility.
        // Don't allow more than 1, no free casing spam!
        return (mMaintenanceHatches.size() <= 1);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPreTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide() && (aTick == 1)) {
            // Adds player to the wireless network if they do not already exist on it.
            ownerUUID = processInitialSettings(aBaseMetaTileEntity);
        }
    }

    private static final int PARALLEL_WINDOW_ID = 10;

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        buildContext.addSyncedWindow(PARALLEL_WINDOW_ID, this::createParallelWindow);
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            if (!widget.isClient()) {
                widget.getContext()
                    .openSyncedWindow(PARALLEL_WINDOW_ID);
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GT_UITextures.BUTTON_STANDARD);
                ret.add(GT_UITextures.OVERLAY_BUTTON_BATCH_MODE_ON);
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip(translateToLocal("GT5U.tpm.parallelwindow"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(174, 129)
            .setSize(16, 16));
        super.addUIWidgets(builder, buildContext);
    }

    protected ModularWindow createParallelWindow(final EntityPlayer player) {
        final int WIDTH = 158;
        final int HEIGHT = 52;
        final int PARENT_WIDTH = getGUIWidth();
        final int PARENT_HEIGHT = getGUIHeight();
        ModularWindow.Builder builder = ModularWindow.builder(WIDTH, HEIGHT);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.setDraggable(true);
        builder.setPos(
            (size, window) -> Alignment.Center.getAlignedPos(size, new Size(PARENT_WIDTH, PARENT_HEIGHT))
                .add(
                    Alignment.BottomRight.getAlignedPos(new Size(PARENT_WIDTH, PARENT_HEIGHT), new Size(WIDTH, HEIGHT))
                        .add(WIDTH - 3, 0)
                        .subtract(0, 10)));
        builder.widget(
            TextWidget.localised("GTPP.CC.parallel")
                .setPos(3, 4)
                .setSize(150, 20))
            .widget(
                new NumericWidget().setSetter(val -> multiplier = (int) val)
                    .setGetter(() -> multiplier)
                    .setBounds(1, Integer.MAX_VALUE)
                    .setDefaultValue(1)
                    .setScrollValues(1, 4, 64)
                    .setTextAlignment(Alignment.Center)
                    .setTextColor(Color.WHITE.normal)
                    .setSize(150, 18)
                    .setPos(4, 25)
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                    .attachSyncer(
                        new FakeSyncWidget.IntegerSyncer(() -> multiplier, (val) -> multiplier = val),
                        builder));
        return builder.build();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("eMultiplier", multiplier);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        multiplier = aNBT.getInteger("eMultiplier");
        super.loadNBTData(aNBT);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
