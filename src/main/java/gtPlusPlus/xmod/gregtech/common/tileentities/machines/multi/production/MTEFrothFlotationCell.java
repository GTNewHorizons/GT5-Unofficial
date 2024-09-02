package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

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
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;

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
        return "Flotation Cell";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Process that milled ore!")
            .addInfo("You can only ever process one type of material per controller")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(7, 9, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Inconel Reinforced Casing", 68, false)
            .addCasingInfoMin("Flotation Casing", 52, false)
            .addInputBus("Bottom Casing", 1)
            .addInputHatch("Bottom Casing", 1)
            .addOutputHatch("Bottom Casing", 1)
            .addEnergyHatch("Bottom Casing", 1)
            .addMaintenanceHatch("Bottom Casing", 1)
            .toolTipFinisher(GTPPCore.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCDFrothFlotationCellActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCDFrothFlotationCell;
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
    public int getMaxParallelRecipes() {
        return 1;
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
                        .dot(1)
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
        return survivialBuildPiece(mName, stackSize, 3, 3, 0, elementBudget, env, false, true);
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
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return GTPPCore.ConfigSwitches.pollutionPerSecondMultiFrothFlotationCell;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                /*
                 * Material checks Makes sure we can only ever use one type of material in this flotation cell. We used
                 * to depend on Alk's hash, but it's unreliable and user-hostile So we're using unlocalized name of
                 * material now.
                 */
                Material foundMaterial = FlotationRecipeHandler
                    .getMaterialOfMilledProduct(FlotationRecipeHandler.findMilledStack(recipe));
                String foundMaterialName = null;
                if (foundMaterial != null) {
                    foundMaterialName = foundMaterial.getUnlocalizedName();
                }

                if (foundMaterialName == null) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }

                // Set material locked for this controller
                if (lockedMaterialName == null) {
                    lockedMaterialName = foundMaterialName;
                }

                // Check material match
                if (!Objects.equals(lockedMaterialName, foundMaterialName)) {
                    return SimpleCheckRecipeResult.ofFailure("machine_locked_to_different_recipe");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
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
    public String[] getExtraInfoData() {
        return new String[] { "Locked material: " + lockedMaterialName };
    }

    @Override
    public boolean isRecipeLockingEnabled() {
        return lockedMaterialName != null && !lockedMaterialName.equals("");
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(new FakeSyncWidget.StringSyncer(() -> lockedMaterialName, val -> lockedMaterialName = val));
    }
}
