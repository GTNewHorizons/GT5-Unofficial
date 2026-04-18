package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cofh.asmhooks.block.BlockTickingWater;
import cofh.asmhooks.block.BlockWater;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;

public class MTEIndustrialChemicalBath extends MTEExtendedPowerMultiBlockBase<MTEIndustrialChemicalBath>
    implements ISurvivalConstructable {

    private int casingAmount;
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final Block DISTILLED_WATER_BLOCK = BlocksItems.getFluidBlock(InternalName.fluidDistilledWater);
    private static final String[][] structure = new String[][] { { "AABAA", "AEEEA", "AE~EA", "AEEEA" },
        { "  B  ", "EFDFE", "EFDFE", "EEEEE" }, { "     ", "EFFFE", "EFFFE", "EEEEE" },
        { "  B  ", "EFCFE", "EFCFE", "EEEEE" }, { "AABAA", "AEEEA", "AEEEA", "AEEEA" } };

    // Lazy allocation since ofFrame requires late-registering GTPP MaterialsAlloy
    private static IStructureDefinition<MTEIndustrialChemicalBath> STRUCTURE_DEFINITION = null;

    public MTEIndustrialChemicalBath(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialChemicalBath(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialChemicalBath(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Chemical Bath")
            .addBulkMachineInfo(4, 5f, 1f)
            .addInfo("Always requires an Input Hatch full of water to refill structure")
            .addInfo("Need to be filled with water")
            .addInfo("Will automatically fill water from input hatch")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 4, 5, false)
            .addController("Front center")
            .addCasingInfoMin("Wash Plant Casing", 30, false)
            .addCasingInfoExactly("Watertight Steel Frame Box", 20, false)
            .addCasingInfoExactly("Block of Zinc", 2, false)
            .addCasingInfoExactly("Block of Copper", 2, false)
            .addCasingInfoExactly("Chemically Inert Machine Casing", 4, false)
            .addInputBus("Any Wash Plant Casing", 1)
            .addOutputBus("Any Wash Plant Casing", 1)
            .addInputHatch("Any Wash Plant Casing", 1)
            .addOutputHatch("Any Wash Plant Casing", 1)
            .addEnergyHatch("Any Wash Plant Casing", 1)
            .addMaintenanceHatch("Any Wash Plant Casing", 1)
            .addMufflerHatch("Any Wash Plant Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "PCGMatt")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialChemicalBath> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialChemicalBath>builder()
                .addShape(STRUCTURE_PIECE_MAIN, structure)
                .addElement('A', ofFrame(MaterialsAlloy.AQUATIC_STEEL))
                .addElement('B', Casings.ChemicallyInertMachineCasing.asElement())
                .addElement('C', ofBlock(GregTechAPI.sBlockMetal2, 7))
                .addElement('D', ofBlock(GregTechAPI.sBlockMetal8, 6))
                .addElement(
                    'E',
                    buildHatchAdder(MTEIndustrialChemicalBath.class)
                        .atLeast(InputBus, InputHatch, OutputHatch, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(Casings.WashPlantCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.WashPlantCasing.asElement())))
                .addElement('F', ofChain(isAir(), ofAnyWater(true)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 30 && checkHatch();
    }

    public boolean checkHatch() {
        return !mMufflerHatches.isEmpty() && !mInputHatches.isEmpty();
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        // don't rotate a washer, water will flow out.
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] { Casings.WashPlantCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDIndustrialWashPlantActive)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialWashPlantActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.WashPlantCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDIndustrialWashPlant)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDIndustrialWashPlantGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.WashPlantCasing.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.chemicalBathRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (checkForWater()) {
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                return SimpleCheckRecipeResult.ofFailure("no_water");
            }
        }.noRecipeCaching()
            .setSpeedBonus(1F / 5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialWashPlant_ModeChemBath;
    }

    public boolean checkForWater() {
        IGregTechTileEntity base = this.getBaseMetaTileEntity();
        boolean allFilled = true;

        for (int localZ = 0; localZ < structure.length; localZ++) {
            String[] rows = structure[localZ];
            for (int localY = 0; localY < rows.length; localY++) {
                String row = rows[localY];
                for (int localX = 0; localX < row.length(); localX++) {

                    if (row.charAt(localX) != 'F') continue;

                    int[] abc = new int[] { localX - OFFSET_X, localY - OFFSET_Y, localZ - OFFSET_Z };

                    int[] xyz = new int[] { 0, 0, 0 };

                    this.getExtendedFacing()
                        .getWorldOffset(abc, xyz);

                    int worldX = base.getXCoord() + xyz[0];
                    int worldY = base.getYCoord() + xyz[1];
                    int worldZ = base.getZCoord() + xyz[2];

                    Block block = base.getWorld()
                        .getBlock(worldX, worldY, worldZ);

                    if (block == DISTILLED_WATER_BLOCK) {
                        continue;
                    }

                    allFilled = false;

                    boolean isAir = block == Blocks.air || block == Blocks.flowing_water;
                    boolean isCOFHWater = Mods.COFHCore.isModLoaded()
                        && (block instanceof BlockWater || block instanceof BlockTickingWater);
                    boolean isWater = block == Blocks.water || isCOFHWater;

                    if (!isAir && !isWater) {
                        return false;
                    }

                    ArrayList<FluidStack> stored = this.getStoredFluids();
                    if (stored == null) {
                        return false;
                    }

                    boolean processed = false;

                    for (FluidStack fs : stored) {

                        if (!fs.isFluidEqual(Materials.Water.getFluid(1))) {
                            continue;
                        }

                        if (fs.amount < 1000) {
                            continue;
                        }

                        fs.amount -= 1000;

                        base.getWorld()
                            .setBlock(worldX, worldY, worldZ, isAir ? Blocks.water : DISTILLED_WATER_BLOCK);

                        processed = true;
                        break;
                    }

                    if (!processed) {
                        allFilled = false;
                        continue;
                    }
                }
            }
        }
        return allFilled;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MULTI_ORE_WASHER_PLANT_LOOP;
    }
}
