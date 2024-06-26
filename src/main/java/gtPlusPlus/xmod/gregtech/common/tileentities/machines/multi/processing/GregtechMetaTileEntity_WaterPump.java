package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_WASHER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_WASHER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.TEXTURE_METAL_PANEL_E;
import static gregtech.api.enums.Textures.BlockIcons.TEXTURE_METAL_PANEL_E_A;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.common.blocks.GT_Block_Casings9;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;

public class GregtechMetaTileEntity_WaterPump extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntity_WaterPump>
    implements ISurvivalConstructable {

    public GregtechMetaTileEntity_WaterPump(String aName) {
        super(aName);
    }

    public GregtechMetaTileEntity_WaterPump(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_WaterPump(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Water Pump";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static IStructureDefinition<GregtechMetaTileEntity_WaterPump> STRUCTURE_DEFINITION = null;

    // spotless:off
    private final String[][] shape = new String[][] {
        { " A ", " A ", "AAA", " A " },
        { " A ", "   ", "A A", " A " },
        { "C~C", "CCC", "CCC", "CCC" } };

    // spotless:on

    private static final int horizontalOffSet = 1;
    private static final int verticalOffSet = 2;
    private static final int depthOffSet = 0;

    private static int COUNT_OF_WATER = 10_000;
    private static final int PROGRESSION_TIME = 20;

    private FluidStack[] getWater() {
        return new FluidStack[] { FluidRegistry.getFluidStack("water", getHumidity()) };
    }

    private int mCasing;

    protected static String getNickname() {
        return "EvgenWarGold";
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;

        return mCasing >= 10;
    }

    private boolean canSeeSky() {
        return this.getBaseMetaTileEntity()
            .getWorld()
            .canBlockSeeTheSky(
                this.getBaseMetaTileEntity()
                    .getXCoord(),
                this.getBaseMetaTileEntity()
                    .getYCoord() + 2,
                this.getBaseMetaTileEntity()
                    .getZCoord());
    }

    private int getHumidity() {
        float rate = 0f;
        BiomeGenBase biomeRate = this.getBaseMetaTileEntity()
            .getWorld()
            .getBiomeGenForCoords(getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getZCoord());
        float humidity = biomeRate.rainfall;
        rate += humidity;
        rate *= COUNT_OF_WATER;
        return (int) rate;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntity_WaterPump> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {

            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_WaterPump>builder()

                .addShape(STRUCTURE_PIECE_MAIN, transpose(shape))
                .addElement('A', ofFrame(Materials.Bronze))
                .addElement(
                    'C',
                    buildHatchAdder(GregtechMetaTileEntity_WaterPump.class).atLeast(OutputHatch)
                        .casingIndex(((GT_Block_Casings9) GregTech_API.sBlockCasings9).getTextureIndex(2))
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(GregTech_API.sBlockCasings9, 2))))
                .build();

        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            horizontalOffSet,
            verticalOffSet,
            depthOffSet,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == ForgeDirection.UP) {
            return new ITexture[] { TextureFactory.of(TEXTURE_METAL_PANEL_E_A) };
        }
        if (sideDirection == facingDirection) {
            return new ITexture[] { TextureFactory.of(TEXTURE_METAL_PANEL_E),
                TextureFactory.of(
                    TextureFactory.of(OVERLAY_FRONT_STEAM_WASHER),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_STEAM_WASHER_ACTIVE)
                        .glow()
                        .build()) };
        }
        return new ITexture[] { TextureFactory.of(TEXTURE_METAL_PANEL_E) };
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

        }.setEuModifier(0F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Steam Washer")
            .addInfo("Runs recipes up to LV tier")
            .addInfo("Washing up to Tier 1 - 8 and Tier 2 - 16 things at a time")
            .addInfo("Multi consumes x2 amount of steam on Tier 2")
            .addSeparator()
            .beginStructureBlock(5, 5, 9, false)
            .addInputHatch("Any casing", 1)
            .toolTipFinisher(getNickname());
        return tt;
    }

    @SuppressWarnings("unlikely-arg-type")
    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        if (!canSeeSky()) {
            return CheckRecipeResultRegistry.NO_SEE_SKY;
        }

        this.mMaxProgresstime = PROGRESSION_TIME;
        mOutputFluids = getWater();

        VoidProtectionHelper voidProtection = new VoidProtectionHelper().setMachine(this)
            .setFluidOutputs(mOutputFluids)
            .build();

        if (voidProtection.isFluidFull()) {
            mOutputFluids = new FluidStack[] {};
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        } else {
            this.updateSlots();
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
    }

    @Override
    public boolean isBatchModeEnabled() {
        return false;
    }
}
