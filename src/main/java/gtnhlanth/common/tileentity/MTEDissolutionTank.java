package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
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
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.loaders.ItemRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
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
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.api.recipe.LanthanidesRecipeMaps;
import gtnhlanth.util.DescTextLocalization;

public class MTEDissolutionTank extends MTEEnhancedMultiBlockBase<MTEDissolutionTank>
    implements ISurvivalConstructable {

    private int casingAmount = 0;
    // Old limit from tooltip: 42, it does not even allow 2 input hatch so it is lowered to reasonable amount.
    private static final int MIN_CASINGS = 30;

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
                .buildAndChain(
                    onElementPass(MTEDissolutionTank::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings4, 1))))
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(mName, 2, 3, 0, errors)) return;
        checkCasingMin(errors, casingAmount, MIN_CASINGS);
        checkHasEnergyHatch(errors);
        checkOneMaintenanceHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    private void onCasingAdded() {
        casingAmount++;
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

    private boolean checkRatio(GTRecipe tRecipe, List<FluidStack> tFluidInputs) {
        FluidStack majorGenericFluid = tRecipe.mFluidInputs[0];
        FluidStack minorGenericFluid = tRecipe.mFluidInputs[1];

        int majorAmount = 0;
        int minorAmount = 0;

        for (int i = 0; i < tFluidInputs.size(); i++) {
            FluidStack f = tFluidInputs.get(i);
            if (f.getUnlocalizedName()
                .equals(majorGenericFluid.getUnlocalizedName())) {
                majorAmount += f.amount;
            } else if (f.getUnlocalizedName()
                .equals(minorGenericFluid.getUnlocalizedName())) {
                    minorAmount += f.amount;
                }
        }

        return majorAmount == minorAmount * tRecipe.mSpecialValue;
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
        tt.addMachineType(StatCollector.translateToLocal("gtnhlanth.tt.disstank.machinetype"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.disstank.info1"))
            .addInfo(StatCollector.translateToLocal("gtnhlanth.tt.disstank.info2"))
            .beginStructureBlock(5, 5, 5, true)
            .addController("Front center, 2nd layer")
            .addCasing("30-44", Casings.CleanStainlessSteelMachineCasing.getLocalizedName(), false)
            .addCasing("24", "Any Tiered Glass", false)
            .addCasing("9", Casings.HeatProofMachineCasing.getLocalizedName(), false)
            .addEnergyHatch("1+", "Any stainless steel casing", 1)
            .addMaintenanceHatch("1", "Any stainless steel casing", 1)
            .addInputAny("1+", "Any stainless steel casing", 1)
            .addOutputAny("1+", "Any stainless steel casing", 1)
            .addAir("Interior of the structure")
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }
}
