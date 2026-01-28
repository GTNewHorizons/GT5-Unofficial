package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEFrothFlotationCell extends GTPPMultiBlockBase<MTEFrothFlotationCell> implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<MTEFrothFlotationCell> STRUCTURE_DEFINITION = null;

    public MTEFrothFlotationCell(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEFrothFlotationCell(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEFrothFlotationCell(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Flotation Cell, FCR";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Process that milled ore!")
            .addInfo("You can only ever process one type of material per controller")
            .addPerfectOCInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 9, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Inconel Reinforced Casing", 68, false)
            .addCasingInfoMin("Flotation Casing", 52, false)
            .addInputBus("Bottom Casing", 1)
            .addInputHatch("Bottom Casing", 1)
            .addOutputHatch("Bottom Casing", 1)
            .addEnergyHatch("Bottom Casing", 1)
            .addMaintenanceHatch("Bottom Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_HUM;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDFrothFlotationCellActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCDFrothFlotationCellActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDFrothFlotationCell;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCDFrothFlotationCellGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(2, 1);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.flotationCellRecipes;
    }

    @Override
    public IStructureDefinition<MTEFrothFlotationCell> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEFrothFlotationCell>builder()
                .addShape(
                    mName,
                    new String[][] { { "       ", "       ", "   X   ", "  X~X  ", "   X   ", "       ", "       " },
                        { "       ", "   F   ", "  FFF  ", " FF FF ", "  FFF  ", "   F   ", "       " },
                        { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                        { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                        { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                        { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                        { "       ", "   F   ", "  F F  ", " F   F ", "  F F  ", "   F   ", "       " },
                        { "  CCC  ", " CCCCC ", "CCCCCCC", "CCCCCCC", "CCCCCCC", " CCCCC ", "  CCC  " },
                        { "  CCC  ", " CCCCC ", "CCCCCCC", "CCCCCCC", "CCCCCCC", " CCCCC ", "  CCC  " }, })
                .addElement(
                    'C',
                    buildHatchAdder(MTEFrothFlotationCell.class)
                        .atLeast(InputBus, InputHatch, OutputHatch, Maintenance, Energy)
                        .casingIndex(getCasingTextureId())
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 1))))
                .addElement('F', ofBlock(ModBlocks.blockSpecialMultiCasings, 9))
                .addElement('X', ofBlock(ModBlocks.blockCasings3Misc, 1))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 3, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 3, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 3, 3, 0) && mCasing >= 68 - 4 && checkHatch();
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d == ForgeDirection.UP;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiFrothFlotationCell;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                // Make sure we lock to a specific milled ore, checked via oredict
                String milledName = getMilledStackName(recipe);
                if (milledName == null) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }

                // Set material locked for this controller
                // "milled" check is to clear old save data since the name caching system changed
                if (lockedMaterialName == null || !lockedMaterialName.startsWith("milled")) {
                    lockedMaterialName = milledName;
                }

                // Ensure oredict matches
                if (!Objects.equals(lockedMaterialName, milledName)) {
                    return SimpleCheckRecipeResult.ofFailure("machine_locked_to_different_recipe");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            private String getMilledStackName(GTRecipe recipe) {
                if (recipe == null || recipe.mInputs == null) {
                    return null;
                }

                for (ItemStack stack : recipe.mInputs) {
                    for (int oreID : OreDictionary.getOreIDs(stack)) {
                        String oredict = OreDictionary.getOreName(oreID);
                        if (oredict.startsWith(OrePrefixes.milled.toString())) {
                            return oredict;
                        }
                    }
                }
                return null;
            }
        }.enablePerfectOverclock();
    }

    /*
     * Handle NBT
     */

    private String lockedMaterialName = null;

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (lockedMaterialName != null) {
            aNBT.setString("lockedMaterialName", lockedMaterialName);
        }
        super.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (lockedMaterialName != null) {
            aNBT.setString("lockedMaterialName", lockedMaterialName);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("lockedMaterialName", Constants.NBT.TAG_STRING)) {
            lockedMaterialName = aNBT.getString("lockedMaterialName");
        }
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (stack.hasTagCompound() && stack.getTagCompound()
            .hasKey("lockedMaterialName")) {
            tooltip.add(
                StatCollector.translateToLocal("tooltip.flotationCell.lockedTo") + " "
                    + StatCollector.translateToLocal(
                        stack.getTagCompound()
                            .getString("lockedMaterialName")));
        }
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add("Locked material: " + lockedMaterialName);
    }

    @Override
    public boolean isRecipeLockingEnabled() {
        return lockedMaterialName != null && !lockedMaterialName.isEmpty();
    }

    @Override
    protected boolean useMui2() {
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(new FakeSyncWidget.StringSyncer(() -> lockedMaterialName, val -> lockedMaterialName = val));
    }
}
