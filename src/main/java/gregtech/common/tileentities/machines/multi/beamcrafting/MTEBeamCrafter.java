package gregtech.common.tileentities.machines.multi.beamcrafting;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAMCRAFTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAMCRAFTER_ACTIVE;
import static gregtech.api.recipe.RecipeMaps.BEAMCRAFTER_METADATA;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.misc.GTStructureChannels;
import gregtech.loaders.postload.recipes.beamcrafter.BeamCrafterMetadata;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;

public class MTEBeamCrafter
    extends MTEExtendedPowerMultiBlockBase<MTEBeamCrafter>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.
    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();

    private static final IStructureDefinition<MTEBeamCrafter> STRUCTURE_DEFINITION = StructureDefinition.<gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamCrafter>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "                 ",
                " BBB         BBB ",
                " BCB         BCB ",
                " BBB         BBB ",
                "                 "
            },{
                " BBB         BBB ",
                "B   B       B   B",
                "B   B       B   B",
                "B   B       B   B",
                " BBB         BBB "
            },{
                " BBB         BBB ",
                "B   B       B   B",
                "B   A       A   B",
                "B   B       B   B",
                " BBB         BBB "
            },{
                " BBB         BBB ",
                "B   B       B   B",
                "B   A       A   B",
                "B   B       B   B",
                " BBB         BBB "
            },{
                " BBB         BBB ",
                "B   BB     BB   B",
                "B   AA     AA   B",
                "B   BB     BB   B",
                " BBB         BBB "
            },{
                "  BBB       BBB  ",
                " B   BB   BB   B ",
                " A   AA   AA   A ",
                " B   BB   BB   B ",
                "  BBB       BBB  "
            },{
                "  BBBB     BBBB  ",
                " B    BBBBB    B ",
                " A    AB~BA    A ",
                " B    BBBBB    B ",
                "  BBBB     BBBB  "
            },{
                "   BBBBBBBBBBB   ",
                "  B           B  ",
                "  A           A  ",
                "  B           B  ",
                "   BBBBBBBBBBB   "
            },{
                "    BBBBBBBBB    ",
                "   B         B   ",
                "   A         A   ",
                "   B         B   ",
                "    BBBBBBBBB    "
            },{
                "      BBBBB      ",
                "    BB     BB    ",
                "    AA     AA    ",
                "    BB     BB    ",
                "      BBBBB      "
            },{
                "                 ",
                "      BBBBB      ",
                "      BBBBB      ",
                "      BBBBB      ",
                "                 "
            }})
        //spotless:on
        .addElement(
            'B', // collider casing
            buildHatchAdder(MTEBeamCrafter.class)
                .atLeast(Energy, ExoticEnergy, Maintenance, InputBus, InputHatch, OutputBus, OutputHatch)
                .casingIndex(((BlockCasings13) GregTechAPI.sBlockCasings13).getTextureIndex(10))
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings13, 10))
        .addElement('A', chainAllGlasses())
        .addElement(
            'C',
            buildHatchAdder(MTEBeamCrafter.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(2)
                .adder(MTEBeamCrafter::addBeamLineInputHatch)
                .build()) // beamline input hatch
        .build();

    private boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }

    public MTEBeamCrafter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBeamCrafter(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEBeamCrafter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBeamCrafter(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAMCRAFTER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAMCRAFTER_ACTIVE)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAMCRAFTER)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Beam Crafter, Beam Assembler")
            .addInfo("Collides two beams of particles into target materials to modify ")
            .addInfo("their subatomic structure")
            .addSeparator()
            .addInfo("The Input Beams can be supplied to either Beamline Input Hatch")
            .addInfo(
                "The particle shown on the left in NEI is " + EnumChatFormatting.AQUA
                    + "Particle 1"
                    + EnumChatFormatting.GRAY
                    + ", and the one ")
            .addInfo("on the right is " + EnumChatFormatting.GOLD + "Particle 2" + EnumChatFormatting.GRAY)
            .addSeparator()
            .addInfo("Recipes will not start unless both beams have sufficiently high")
            .addInfo(EnumChatFormatting.YELLOW + "Beam Energies")
            .addSeparator()
            .addInfo(
                "Processing speed is determined purely by the Input " + EnumChatFormatting.RED
                    + "Beam Rates"
                    + EnumChatFormatting.GRAY)
            .addInfo(
                "Each particle " + EnumChatFormatting.RED
                    + "Amount "
                    + EnumChatFormatting.GRAY
                    + "provided corresponds to one tick of progress")
            .addInfo(
                "Once the required " + EnumChatFormatting.RED
                    + "Amount "
                    + EnumChatFormatting.GRAY
                    + "of a specific "
                    + EnumChatFormatting.AQUA
                    + "Part"
                    + EnumChatFormatting.GOLD
                    + "icle "
                    + EnumChatFormatting.GRAY
                    + "have been input,")
            .addInfo("excess particles of that type are voided")
            .addSeparator()
            .beginStructureBlock(17, 5, 11, false)
            .addController("Front Center")
            .addCasingInfoMin("Collider Casing", 224, false)
            .addCasingInfoExactly("Any Tiered Glass", 26, false)
            .addInputBus("Any Collider Casing", 1)
            .addOutputBus("Any Collider Casing", 1)
            .addInputHatch("Any Collider Casing", 1)
            .addOutputHatch("Any Collider Casing", 1)
            .addEnergyHatch("Any Collider Casing", 1)
            .addMaintenanceHatch("Any Collider Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addTecTechHatchInfo()
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 8, 2, 6);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 8, 2, 6, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 8, 2, 6);
    }

    @Nullable
    private BeamInformation getNthInputParticle(int n) {
        int i = 0;
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (i == n) {
                if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
                return in.dataPacket.getContent();
            }
            i += 1;
        }
        return null;
    }

    private boolean isInputParticleInRecipe(BeamInformation inputParticle_A, BeamInformation inputParticle_B,
                                            BeamCrafterMetadata metadata) {

        int particleID_x = metadata.particleID_A;
        int particleID_y = metadata.particleID_B;
        float minEnergy_x = metadata.minEnergy_A;
        float minEnergy_y = metadata.minEnergy_B;

        int inputParticleID_A = inputParticle_A.getParticleId();
        int inputParticleID_B = inputParticle_B.getParticleId();
        float inputEnergy_A = inputParticle_A.getEnergy();
        float inputEnergy_B = inputParticle_B.getEnergy();

        // possibilities: (A = x, B = y); (A = y, B = x)

        return ((inputParticleID_A == particleID_x && inputParticleID_B == particleID_y
            && inputEnergy_A > minEnergy_x
            && inputEnergy_B > minEnergy_y)
            || (inputParticleID_A == particleID_y && inputParticleID_B == particleID_x
                && inputEnergy_A > minEnergy_y
                && inputEnergy_B > minEnergy_x));

    }

    private int currentRecipeCurrentAmountA = 0;
    private int currentRecipeCurrentAmountB = 0;
    private int currentRecipeMaxAmountA = 0;
    private int currentRecipeMaxAmountB = 0;

    @Override
    protected void incrementProgressTime() {

        BeamInformation inputParticle_A = this.getNthInputParticle(0);
        BeamInformation inputParticle_B = this.getNthInputParticle(1);

        int particleRateA = inputParticle_A.getRate();
        int particleRateB = inputParticle_B.getRate();

        this.currentRecipeCurrentAmountA += particleRateA;
        if (this.currentRecipeCurrentAmountA <= currentRecipeMaxAmountA) {
            mProgresstime += particleRateA;
        }
        this.currentRecipeCurrentAmountB += particleRateB;
        if (this.currentRecipeCurrentAmountB <= currentRecipeMaxAmountB) {
            mProgresstime += particleRateB;
        }
    }

    private GTRecipe lastRecipe;

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {

        this.currentRecipeCurrentAmountA = 0;
        this.currentRecipeCurrentAmountB = 0;
        this.currentRecipeMaxAmountA = 0;
        this.currentRecipeMaxAmountB = 0;

        ArrayList<ItemStack> tItems = this.getStoredInputs();
        ItemStack[] inputItems = tItems.toArray(new ItemStack[0]);
        ArrayList<FluidStack> tFluids = this.getStoredFluids();
        FluidStack[] inputFluids = tFluids.toArray(new FluidStack[0]);

        long tVoltageActual = GTValues.VP[(int) this.getInputVoltageTier()];

        GTRecipe tRecipe = RecipeMaps.beamcrafterRecipes.findRecipeQuery()
            .items(inputItems)
            .fluids(inputFluids)
            .voltage(tVoltageActual)
            .filter((GTRecipe recipe) -> {
                BeamCrafterMetadata metadata = recipe.getMetadata(BEAMCRAFTER_METADATA);
                if (metadata == null) return false;

                BeamInformation inputParticle_A = this.getNthInputParticle(0);
                BeamInformation inputParticle_B = this.getNthInputParticle(1);

                if ((inputParticle_A != null) || (inputParticle_B != null)) {
                    return isInputParticleInRecipe(inputParticle_A, inputParticle_B, metadata);
                }
                return false;
            })
            .cachedRecipe(this.lastRecipe)
            .find();
        if (tRecipe == null) return CheckRecipeResultRegistry.NO_RECIPE;

        BeamCrafterMetadata metadata = tRecipe.getMetadata(BEAMCRAFTER_METADATA);
        if (metadata == null) return CheckRecipeResultRegistry.NO_RECIPE;

        BeamInformation inputParticle_A = this.getNthInputParticle(0);
        BeamInformation inputParticle_B = this.getNthInputParticle(1);
        if (inputParticle_A == null || inputParticle_B == null) return CheckRecipeResultRegistry.NO_RECIPE;

        if (!isInputParticleInRecipe(inputParticle_A, inputParticle_B, metadata))
            return CheckRecipeResultRegistry.NO_RECIPE;

        this.currentRecipeMaxAmountA = metadata.amount_A;
        this.currentRecipeMaxAmountB = metadata.amount_B;
        // total time to finish recipe in ticks is the sum of the required Amounts
        this.mMaxProgresstime = this.currentRecipeMaxAmountA + this.currentRecipeMaxAmountB;

        if (this.mMaxProgresstime == Integer.MAX_VALUE - 1 && this.mEUt == Integer.MAX_VALUE - 1)
            return CheckRecipeResultRegistry.NO_RECIPE;

        if (!tRecipe.equals(this.lastRecipe)) this.lastRecipe = tRecipe;

        tRecipe.consumeInput(1, GTValues.emptyFluidStackArray, inputItems);
        ItemStack[] itemOutputArray = ArrayExt.copyItemsIfNonEmpty(tRecipe.mOutputs);
        this.mOutputItems = itemOutputArray;

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        mEUt = (int) -tVoltageActual;
        if (this.mEUt > 0) this.mEUt = (-this.mEUt);

        this.updateSlots();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.beamcrafterRecipes;
    }
}
