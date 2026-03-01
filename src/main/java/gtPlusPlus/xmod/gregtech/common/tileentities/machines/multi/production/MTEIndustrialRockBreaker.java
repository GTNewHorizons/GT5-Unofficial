package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

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
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialRockBreaker extends GTPPMultiBlockBase<MTEIndustrialRockBreaker>
    implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<MTEIndustrialRockBreaker> STRUCTURE_DEFINITION = null;

    public MTEIndustrialRockBreaker(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialRockBreaker(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialRockBreaker(this.mName);
    }

    @Override
    public String getMachineType() {
        return "gt.recipe.rockbreaker";
    }

    private static final String casingBaseName = GregtechItemList.Casing_ThermalCentrifuge.get(0)
        .getDisplayName();
    private static final String casingMiddleName = GregtechItemList.Casing_ThermalContainment.get(0)
        .getDisplayName();
    private static final String anyBaseCasing = GTUtility.nestParams("GT5U.MBTT.HatchInfo", casingBaseName);

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addBulkMachineInfo(8, 3f, 0.75f)
            .addInfo("gt.boldarnator.tips")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 4, 3, true)
            .addController("front_bottom_middle")
            .addCasingInfoMin(casingBaseName, 9, false)
            .addCasingInfoExactly(casingMiddleName, 16, false)
            .addInputBus(anyBaseCasing, 1)
            .addInputHatch(anyBaseCasing, 1)
            .addOutputBus(anyBaseCasing, 1)
            .addEnergyHatch(anyBaseCasing, 1)
            .addMaintenanceHatch(anyBaseCasing, 1)
            .addMufflerHatch(anyBaseCasing, 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialRockBreaker> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialRockBreaker>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "HHH", "H-H", "HHH" }, { "HHH", "H-H", "HHH" },
                            { "C~C", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialRockBreaker.class)
                        .atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(TAE.GTPP_INDEX(16))
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 0))))
                .addElement('H', ofBlock(ModBlocks.blockCasings2Misc, 11))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        boolean aCheckPiece = checkPiece(mName, 1, 3, 0);
        boolean aCasingCount = mCasing >= 9;
        boolean aCheckHatch = checkHatch();
        log(aCheckPiece + ", " + aCasingCount + ", " + aCheckHatch);
        return aCheckPiece && aCasingCount && aCheckHatch;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_INDUCTION_LOOP;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialRockBreakerActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialRockBreakerActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialRockBreaker;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialRockBreakerGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(16);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.multiblockRockBreakerRecipes;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (inputFluids.length == 0) return CheckRecipeResultRegistry.NO_RECIPE;
                boolean aHasWater = false;
                boolean aHasLava = false;
                for (FluidStack aFluid : inputFluids) {
                    if (aFluid.getFluid() == FluidRegistry.WATER) {
                        aHasWater = true;
                    } else if (aFluid.getFluid() == FluidRegistry.LAVA) {
                        aHasLava = true;
                    }
                }
                if (!aHasWater) {
                    return SimpleCheckRecipeResult.ofFailure("no_water");
                }
                if (!aHasLava) {
                    return SimpleCheckRecipeResult.ofFailure("no_lava");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setSpeedBonus(1 / 3.0)
            .setEuModifier(0.75)
            .setMaxParallelSupplier(this::getTrueParallel);

    }

    @Override
    public int getMaxParallelRecipes() {
        return 8 * GTUtility.getTier(this.getMaxInputVoltage());
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialRockBreaker;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("maxParallelRecipes", getMaxParallelRecipes());
    }
}
