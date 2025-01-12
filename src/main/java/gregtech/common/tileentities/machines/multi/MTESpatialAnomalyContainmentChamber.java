package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GTValues.AuthorNoc;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.metadata.SpatialAnomalyTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.blocks.BlockCasings9;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.thing.gui.TecTechUITextures;

public class MTESpatialAnomalyContainmentChamber
    extends MTEExtendedPowerMultiBlockBase<MTESpatialAnomalyContainmentChamber> implements ISurvivalConstructable {

    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private boolean active = false;
    private int numberOfFoci;
    private ItemStack catalyst;
    private MTEHatchInput stabilizerHatch;
    private final FluidStack stabilizer = (Materials.Vyroxeres.getMolten(1));

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTESpatialAnomalyContainmentChamber> STRUCTURE_DEFINITION = StructureDefinition
        .<MTESpatialAnomalyContainmentChamber>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{
                    { "  B    ", " BB  B ", "B BBBBB", "  B~B  ", "BBBBB B", " B  BB ", "    B  "},
                    { " BB  B ", "BAAAAAB", "BA   A ", " A   A ", " A   AB", "BAAAAAB", " B  BB "},
                    { "  BBBBB", " A   AB", "B     B", "B     B", "B     B", "BA   A ", "BBBBB  "},
                    { "  BCB  ", " A   A ", "B     B", "C     C", "B     B", " A   A ", "  BCB  "},
                    { "BBBBB  ", "BA   A ", "B     B", "B     B", "B     B", " A   AB", "  BBBBB"},
                    { " B  BB ", "BAAAAAB", " A   AB", " A   A ", "BA   A ", "BAAAAAB", " BB  B "},
                    { "    B  ", " B  BB ", "BBBBB B", "  BDB  ", "B BBBBB", " BB  B ", "  B    "}})
        //spotless:on
        .addElement(
            'C',
            buildHatchAdder(MTESpatialAnomalyContainmentChamber.class).atLeast(InputBus, OutputBus, InputHatch)
                .casingIndex(((BlockCasings9) GregTechAPI.sBlockCasings9).getTextureIndex(7))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        MTESpatialAnomalyContainmentChamber::onCasingAdded,
                        ofBlock(GregTechAPI.sBlockCasings9, 7))))
        .addElement('A', ofBlock(GregTechAPI.sBlockGlass1, 1))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 10))
        .addElement(
            'D',
            buildHatchAdder(MTESpatialAnomalyContainmentChamber.class).atLeast(InputHatch)
                .adder(MTESpatialAnomalyContainmentChamber::addStabilizerHatch)
                .casingIndex(((BlockCasings9) GregTechAPI.sBlockCasings9).getTextureIndex(7))
                .dot(2)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasings9, 7)))
        .build();

    private byte mAnomalyTier = 0;

    public MTESpatialAnomalyContainmentChamber(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESpatialAnomalyContainmentChamber(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTESpatialAnomalyContainmentChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESpatialAnomalyContainmentChamber(this.mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/GODFORGE_MODULE_ACTIVE");
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/SCREEN_OFF");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
                    TextureFactory.builder()
                        .addIcon(ScreenON)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(ScreenON)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)),
                    TextureFactory.builder()
                        .addIcon(ScreenOFF)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 10)) };
        }
        return rTexture;
    }

    private boolean addStabilizerHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof MTEHatchInput hatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            stabilizerHatch = hatch;
            return true;
        }
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Brewery")
            .addInfo("50% faster than singleblock machines of the same voltage")
            .addInfo("Gains 4 parallels per voltage tier")
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
            .addCasingInfoExactly("Any Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Wooden Casing", 1)
            .addOutputBus("Any Wooden Casing", 1)
            .addInputHatch("Any Wooden Casing", 1)
            .toolTipFinisher(AuthorNoc);
        return tt;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 3, 3, 0) && mCasingAmount >= 1;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (!aBaseMetaTileEntity.isServerSide()) return;

        if (!active || aTick % 20 != 0) return;

        if (drain(stabilizerHatch, stabilizer, false)) {
            drain(stabilizerHatch, stabilizer, true);
        } else {
            mAnomalyTier = 0;
            active = false;
            this.catalyst.stackSize = Math.round((float) (numberOfFoci / 2));
            numberOfFoci = 0;
            if (this.catalyst.stackSize != 0) mInventory[getControllerSlotIndex()] = this.catalyst;
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        // Void contents of active recipe without crashing machine if it becomes unstable
        if (!active) {
            stopMachine(SimpleShutDownReason.ofCritical("anomaly_unstable"));
            return false;
        }

        return super.onRunningTick(aStack);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(0L);
        logic.setAvailableAmperage(1L);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @Nonnull CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                maxParallel = 2 * numberOfFoci;

                int requiredRecipeTier = recipe.getMetadataOrDefault(SpatialAnomalyTierKey.INSTANCE, 0);
                if (requiredRecipeTier > mAnomalyTier) {
                    switch (requiredRecipeTier) {
                        case 1:
                            return SimpleCheckRecipeResult.ofFailure("no_anomaly.0");
                        case 2:
                            return SimpleCheckRecipeResult.ofFailure("no_anomaly.1");
                        case 3:
                            return SimpleCheckRecipeResult.ofFailure("no_anomaly.2");
                        default:
                            return CheckRecipeResultRegistry.NO_RECIPE;
                    }
                } else {
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
            }
        };
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            ItemStack controllerStack = this.getControllerSlot();
            if (clickData.mouseButton == 0) {
                if (controllerStack != null && !active) {
                    if (controllerStack.isItemEqual(GregtechItemList.Laser_Lens_Special.get(1))) {
                        mAnomalyTier = 1;
                    } else if (controllerStack.isItemEqual(GregtechItemList.Compressed_Fusion_Reactor.get(1))) {
                        mAnomalyTier = 2;
                    } else if (controllerStack.isItemEqual(ItemList.EnergisedTesseract.get(1))) {
                        mAnomalyTier = 3;
                    }
                    if (mAnomalyTier != 0) {
                        numberOfFoci = controllerStack.stackSize;
                        this.catalyst = controllerStack;
                        mInventory[getControllerSlotIndex()] = null;
                        active = true;
                    }
                } else if (controllerStack == null && active) {
                    mAnomalyTier = 0;
                    numberOfFoci = 0;
                    active = false;
                    mInventory[getControllerSlotIndex()] = this.catalyst;
                    this.catalyst = null;
                    System.out.println("a");
                }
            }
        })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                if (active) {
                    ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_SAFE_VOID_ON);
                } else {
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add(TecTechUITextures.OVERLAY_BUTTON_SAFE_VOID_OFF);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip(translateToLocal("GT5U.SACC.anomalybutton"))
            .addTooltip(EnumChatFormatting.GRAY + translateToLocal("GT5U.SACC.anomalybuttontooltip.0"))
            .addTooltip(EnumChatFormatting.GRAY + translateToLocal("GT5U.SACC.anomalybuttontooltip.1"))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(174, 112)
            .setSize(16, 16))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> active, val -> active = val));
        super.addUIWidgets(builder, buildContext);
    }

    public int getMaxParallelRecipes() {
        return (16);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.SpatialAnomalyRecipes;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
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
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}
