package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import javax.annotation.Nonnull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import kubatech.loaders.MobHandlerLoader;
import kubatech.network.CustomTileEntityPacket;
import kubatech.tileentity.gregtech.multiblock.GT_MetaTileEntity_ExtremeEntityCrusher;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.common.util.ForgeDirection;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.common.blocks.*;
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

    private static final String STRUCTUR_PIECE_MAIN = "main";

    private static IStructureDefinition<GregtechMetaTileEntity_WaterPump> STRUCTURE_DEFINITION = null;

    private final String[][] shape = new String[][] { { " A ", " A ", "AAA", " A " }, { " A ", "   ", "A A", " A " },
        { "C~C", "CCC", "CCC", "CCC" } };

    private static final int horizontalOffSet = 1;
    private static final int verticalOffSet = 2;
    private static final int depthOffSet = 0;

    private int mCasing;

    public Fluid getFluidToGenerate() {
        return FluidRegistry.WATER;
    }

    public int getAmountOfFluidToGenerate() {
        return 5_000;
    }
    public FluidStack mFluid;

    public FluidStack setFillableStack(FluidStack aFluid) {
        mFluid = aFluid;
        return mFluid;
    }
    public boolean canTankBeFilled() {
        return true;
    }

    public FluidStack getFillableStack() {
        return mFluid;
    }

















    protected static String getNickname() {
        return "EvgenWarGold";
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTUR_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) return false;
        if (mCasing >= 10) {
            return true;
        }
        return false;
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

                .addShape(STRUCTUR_PIECE_MAIN, transpose(shape))
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
        this.buildPiece(STRUCTUR_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivialBuildPiece(
            STRUCTUR_PIECE_MAIN,
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
        if (sideDirection == UP) {
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

        }.setEuModifier(0F).setMaxParallelSupplier(this::getMaxParallelRecipes);
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
        this.mOutputFluids = new FluidStack[] { FluidRegistry.getFluidStack("xpjuice", 5000) };
        this.mMaxProgresstime = 20;
        addFluidToHatch(2);
        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public boolean addFluidToHatch(long aTick) {
        int aFillAmount = this.fill(FluidUtils.getFluidStack(getFluidToGenerate(), getAmountOfFluidToGenerate()), true);
        return aFillAmount > 0;
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (aFluid == null || aFluid.getFluid()
            .getID() <= 0 || aFluid.amount <= 0 || aFluid.getFluid() != getFluidToGenerate() || !canTankBeFilled()) {
            return 0;
        }

        if (getFillableStack() == null || getFillableStack().getFluid()
            .getID() <= 0) {
            if (aFluid.amount <= getCapacity()) {
                if (doFill) {
                    setFillableStack(aFluid.copy());
                    getBaseMetaTileEntity().markDirty();
                }
                return aFluid.amount;
            }
            if (doFill) {
                setFillableStack(aFluid.copy());
                getFillableStack().amount = getCapacity();
                getBaseMetaTileEntity().markDirty();
            }
            return getCapacity();
        }

        if (!getFillableStack().isFluidEqual(aFluid)) return 0;

        int space = getCapacity() - getFillableStack().amount;
        if (aFluid.amount <= space) {
            if (doFill) {
                getFillableStack().amount += aFluid.amount;
                getBaseMetaTileEntity().markDirty();
            }
            return aFluid.amount;
        }
        if (doFill) getFillableStack().amount = getCapacity();
        return space;
    }


}
