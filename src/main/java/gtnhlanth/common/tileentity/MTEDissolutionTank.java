package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtnhlanth.api.recipe.LanthanidesRecipeMaps;
import gtnhlanth.util.DescTextLocalization;

public class MTEDissolutionTank extends MTEEnhancedMultiBlockBase<MTEDissolutionTank>
    implements ISurvivalConstructable, ISecondaryDescribable {

    private final IStructureDefinition<MTEDissolutionTank> multiDefinition = StructureDefinition
        .<MTEDissolutionTank>builder()
        .addShape(
            mName,
            transpose(
                new String[][] { { " sss ", "sssss", "sssss", "sssss", " sss " },
                    { "sgggs", "g---g", "g---g", "g---g", "sgggs" }, { "sgggs", "g---g", "g---g", "g---g", "sgggs" },
                    { "ss~ss", "shhhs", "shhhs", "shhhs", "sssss" }, { "s   s", "     ", "     ", "     ", "s   s" } }))
        .addElement(
            's',
            buildHatchAdder(MTEDissolutionTank.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(49)
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings4, 1))
        .addElement('h', ofBlock(GregTechAPI.sBlockCasings1, 11))
        .addElement('g', chainAllGlasses())
        .build();

    public MTEDissolutionTank(String name) {
        super(name);
    }

    public MTEDissolutionTank(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public IStructureDefinition<MTEDissolutionTank> getStructureDefinition() {
        return multiDefinition;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(mName, 2, 3, 0) && mMaintenanceHatches.size() == 1;
    }

    private boolean addGlass(Block block, int meta) {
        return block == ItemRegistry.bw_glasses[0];
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return LanthanidesRecipeMaps.dissolutionTankRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                if (!checkRatio(recipe, Arrays.asList(inputFluids))) {
                    stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                    return SimpleCheckRecipeResult.ofFailurePersistOnShutdown("dissolution_ratio");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

        };
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
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    private boolean checkRatio(GTRecipe tRecipe, List<FluidStack> tFluidInputs) {
        FluidStack majorGenericFluid = tRecipe.mFluidInputs[0];
        FluidStack minorGenericFluid = tRecipe.mFluidInputs[1];

        int majorAmount;
        int minorAmount;

        FluidStack fluidInputOne = tFluidInputs.get(0);
        FluidStack fluidInputTwo = tFluidInputs.get(1);

        if (fluidInputOne.getUnlocalizedName()
            .equals(majorGenericFluid.getUnlocalizedName())) {
            if (fluidInputTwo.getUnlocalizedName()
                .equals(minorGenericFluid.getUnlocalizedName())) {
                // majorInput = fluidInputOne;
                majorAmount = fluidInputOne.amount;
                // minorInput = fluidInputTwo;
                minorAmount = fluidInputTwo.amount;
                // GTLog.out.print("in first IF");
            } else return false; // No valid other input

        } else if (fluidInputTwo.getUnlocalizedName()
            .equals(majorGenericFluid.getUnlocalizedName())) {
                if (fluidInputOne.getUnlocalizedName()
                    .equals(minorGenericFluid.getUnlocalizedName())) {
                    // majorInput = fluidInputTwo;
                    majorAmount = fluidInputTwo.amount;
                    // minorInput = fluidInputOne;
                    minorAmount = fluidInputOne.amount;
                    // GTLog.out.print("in second if");
                } else return false;

            } else return false;

        return majorAmount / tRecipe.mSpecialValue == minorAmount;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTEDissolutionTank(this.mName);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(mName, itemStack, b, 2, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 2, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("DissolutionTank.hint", 4);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex,
        boolean active, boolean redstone) {

        if (side == facing) {
            if (active) return new ITexture[] { casingTexturePages[0][49], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][49], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][49] };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Dissolution Tank")
            .addInfo("Input Water and Fluid, output Fluid")
            .addInfo("Fluids must be input in the exact ratio as listed in NEI")
            .beginStructureBlock(5, 5, 5, true)
            .addController("Front bottom")
            .addCasingInfoExactly("Clean Stainless Steel Machine Casing", 42, false)
            .addCasingInfoExactly("Any Borosilicate Glass", 24, false)
            .addCasingInfoExactly("Heat Proof Machine Casing", 9, false)
            .addInputHatch("Any Stainless Steel Casing")
            .addInputBus("Any Stainless Steel Casing")
            .addOutputHatch("Any Stainless Steel Casing")
            .addOutputBus("Any Stainless Steel Casing")
            .addMaintenanceHatch("Any Stainless Steel Casing")
            .toolTipFinisher();

        return tt;
    }

}
