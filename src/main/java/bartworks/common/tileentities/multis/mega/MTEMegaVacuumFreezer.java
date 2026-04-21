/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.tileentities.multis.mega;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.tooltip.TooltipHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class MTEMegaVacuumFreezer extends MegaMultiBlockBase<MTEMegaVacuumFreezer> implements ISurvivalConstructable {

    public MTEMegaVacuumFreezer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaVacuumFreezer(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaVacuumFreezer(this.mName);
    }

    private int mCasingFrostProof = 0;
    private int mTier = 1;

    private enum SubspaceCoolingFluid {

        SpaceTime(Materials.SpaceTime, 1, 75),
        Space(Materials.Space, 1, 50),
        Eternity(Materials.Eternity, 1, 25),

        ;

        private final Fluid fluid;
        private final int perfectOverclocks;
        // Consumption per second of runtime
        private final int amount;
        private final static Map<Fluid, SubspaceCoolingFluid> FLUID_MAP = new Object2ObjectOpenHashMap<>();
        private final static SubspaceCoolingFluid[] values = values();

        SubspaceCoolingFluid(Materials material, int perfectOverclocks, int amount) {
            this.perfectOverclocks = perfectOverclocks;
            this.amount = amount;
            this.fluid = material.mFluid == null ? material.mStandardMoltenFluid : material.mFluid;
            assert this.fluid != null : "Subspace cooling fluid " + material.getLocalizedName()
                + " has no fluid associated with it!";
        }

        public static SubspaceCoolingFluid find(FluidStack stack) {
            if (FLUID_MAP.isEmpty()) {
                Arrays.stream(values)
                    .forEach(fluid -> FLUID_MAP.put(fluid.fluid, fluid));
            }
            return FLUID_MAP.get(stack.getFluid());
        }

        public FluidStack getStack() {
            return new FluidStack(this.fluid, this.amount);
        }
    }

    private SubspaceCoolingFluid currentCoolingFluid = null;

    private static final int CASING_INDEX = 17;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_T2 = "main_t2";

    private static final String[][] structure = transpose(
        new String[][] {
            { "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA",
                "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA",
                "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAA~AAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "A             A",
                "A             A", "A             A", "A             A", "A             A", "AAAAAAAAAAAAAAA" },
            { "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA",
                "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA",
                "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA", "AAAAAAAAAAAAAAA" } });
    private static final String[][] structure_tier2 = new String[][] {
        { "AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "ABAAAAAAAAAAABA", "ABABBBBBBBBBABA", "ABABAAAAAAABABA",
            "ABABABBBBBABABA", "ABABABAAABABABA", "ABABABA~ABABABA", "ABABABAAABABABA", "ABABABBBBBABABA",
            "ABABAAAAAAABABA", "ABABBBBBBBBBABA", "ABAAAAAAAAAAABA", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "B             B", "B             B", "B             B",
            "B             B", "B             B", "B             B", "B             B", "B             B",
            "B             B", "B             B", "B             B", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "A             A", "A             A",
            "A             A", "A             A", "A             A", "A             A", "A             A",
            "A             A", "A             A", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "B             B", "B             B",
            "B             B", "B             B", "B             B", "B             B", "B             B",
            "B             B", "B             B", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "B             B", "A             A",
            "A             A", "A             A", "A             A", "A             A", "A             A",
            "A             A", "B             B", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "B             B", "A             A",
            "B             B", "B             B", "B             B", "B             B", "B             B",
            "A             A", "B             B", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "B             B", "A             A",
            "B             B", "A             A", "A             A", "A             A", "B             B",
            "A             A", "B             B", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "B             B", "A             A",
            "B             B", "A             A", "A             A", "A             A", "B             B",
            "A             A", "B             B", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "B             B", "A             A",
            "B             B", "A             A", "A             A", "A             A", "B             B",
            "A             A", "B             B", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "B             B", "A             A",
            "B             B", "B             B", "B             B", "B             B", "B             B",
            "A             A", "B             B", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "B             B", "A             A",
            "A             A", "A             A", "A             A", "A             A", "A             A",
            "A             A", "B             B", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "B             B", "B             B",
            "B             B", "B             B", "B             B", "B             B", "B             B",
            "B             B", "B             B", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "A             A", "A             A", "A             A",
            "A             A", "A             A", "A             A", "A             A", "A             A",
            "A             A", "A             A", "A             A", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "B             B", "B             B", "B             B", "B             B",
            "B             B", "B             B", "B             B", "B             B", "B             B",
            "B             B", "B             B", "B             B", "B             B", "AAAAAAAAAAAAAAA" },
        { "AAAAAAAAAAAAAAA", "ABBBBBBBBBBBBBA", "ABAAAAAAAAAAABA", "ABABBBBBBBBBABA", "ABABAAAAAAABABA",
            "ABABABBBBBABABA", "ABABABAAABABABA", "ABABABAAABABABA", "ABABABAAABABABA", "ABABABBBBBABABA",
            "ABABAAAAAAABABA", "ABABBBBBBBBBABA", "ABAAAAAAAAAAABA", "ABBBBBBBBBBBBBA", "AAAAAAAAAAAAAAA" } };

    private static final IStructureDefinition<MTEMegaVacuumFreezer> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMegaVacuumFreezer>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addShape(STRUCTURE_PIECE_MAIN_T2, structure_tier2)
        .addElement(
            'A',
            buildHatchAdder(MTEMegaVacuumFreezer.class)
                .atLeast(Energy.or(ExoticEnergy), InputHatch, InputBus, OutputHatch, OutputBus, Maintenance)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(onElementPass(x -> x.mCasingFrostProof++, Casings.FrostProofMachineCasing.asElement())))
        // Infinity Cooled Casing
        .addElement('B', Casings.InfinityCooledCasing.asElement())
        .build();

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Vacuum Freezer, MVF")
            .addInfo(
                TooltipHelper.coloredText(
                    TooltipHelper.italicText("\"Handles all things cooling!\""),
                    EnumChatFormatting.DARK_GRAY))
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addSeparator()
            .addTecTechHatchInfo()
            .addUnlimitedTierSkips()
            .addSeparator()
            .addInfo("Upgrade to Tier 2 to unlock " + EnumChatFormatting.DARK_AQUA + "Subspace Cooling.")
            .addInfo(
                "Will gain " + EnumChatFormatting.GOLD
                    + "perfect overclocks "
                    + EnumChatFormatting.GRAY
                    + "by "
                    + EnumChatFormatting.GREEN
                    + "consuming "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "coolants:")
            .addInfo(getCoolantTextFormatted("Molten Spacetime", "75", 1))
            .addInfo(getCoolantTextFormatted("Spatially Enlarged Fluid", "50", 2))
            .addInfo(getCoolantTextFormatted("Molten Eternity", "25", 3))
            .addSeparator()
            .addInfo(
                EnumChatFormatting.DARK_AQUA + "Reinforcing the structure allows the injection of exotic coolants,")
            .addInfo(
                EnumChatFormatting.DARK_AQUA + "enabling the capture of heat energy from miniature tears in spacetime,")
            .addInfo(EnumChatFormatting.DARK_AQUA + "massively increasing the efficiency of the cooling process.")
            .beginStructureBlock(15, 15, 15, true)
            .addController("Front center")
            .addEnergyHatch("Any Frost Proof Machine Casing", 1)
            .addMaintenanceHatch("Any Frost Proof Machine Casing", 1)
            .addInputHatch("Any Frost Proof Machine Casing", 1)
            .addOutputHatch("Any Frost Proof Machine Casing", 1)
            .addInputBus("Any Frost Proof Machine Casing", 1)
            .addOutputBus("Any Frost Proof Machine Casing", 1)
            .addStructureInfo(
                EnumChatFormatting.BLUE + "Base Multi (Tier "
                    + EnumChatFormatting.DARK_PURPLE
                    + 1
                    + EnumChatFormatting.BLUE
                    + "):")
            .addCasingInfoMinColored(
                "Frost Proof Machine Casing",
                EnumChatFormatting.GRAY,
                800,
                EnumChatFormatting.GOLD,
                false)
            .addStructureInfo(
                EnumChatFormatting.BLUE + "Tier "
                    + EnumChatFormatting.DARK_PURPLE
                    + 2
                    + EnumChatFormatting.BLUE
                    + " (Upgrades from Tier "
                    + EnumChatFormatting.DARK_PURPLE
                    + 1
                    + EnumChatFormatting.BLUE
                    + "):")
            .addCasingInfoMinColored(
                "Frost Proof Machine Casing",
                EnumChatFormatting.GRAY,
                700,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Infinity Cooled Casing",
                EnumChatFormatting.GRAY,
                384,
                EnumChatFormatting.GOLD,
                false)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEMegaVacuumFreezer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        if (aStack.stackSize == 1) {
            this.buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, 7, 7, 0);
        } else {
            this.buildPiece(STRUCTURE_PIECE_MAIN_T2, aStack, aHintsOnly, 7, 7, 0);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        if (stackSize.stackSize == 1) {
            return this.survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 7, 0, realBudget, env, true);
        } else {
            return this.survivalBuildPiece(STRUCTURE_PIECE_MAIN_T2, stackSize, 7, 7, 0, realBudget, env, true);
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.vacuumFreezerRecipes;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(BATCH_MODE_NBT_KEY)) {
            this.batchMode = aNBT.getBoolean("mUseMultiparallelMode");
        }
    }

    private SubspaceCoolingFluid findSubspaceCoolingFluid() {
        for (FluidStack stack : getStoredFluids()) {
            SubspaceCoolingFluid fluid = SubspaceCoolingFluid.find(stack);
            if (fluid != null) return fluid;
        }
        return null;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                // Check if the freezer is T2
                if (mTier == 1) return super.createOverclockCalculator(recipe);

                // First try to detect the current fluid used for subspace cooling.
                currentCoolingFluid = findSubspaceCoolingFluid();

                return super.createOverclockCalculator(recipe)
                    .setMachineHeat(currentCoolingFluid == null ? 0 : currentCoolingFluid.perfectOverclocks * 1800)
                    .setRecipeHeat(0)
                    .setHeatOC(true)
                    .setHeatDiscount(false);
            }
        }.noRecipeCaching()
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return Configuration.Multiblocks.megaMachinesMax;
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);
        // Every second while running, consume subspace coolant fluid
        if (mMaxProgresstime > 0 && aTick % 20 == 0) {
            // Subspace cooling only allowed for T2 freezer
            if (mTier == 2) {
                // Try to drain the coolant fluid if it exists. If failed, stop the machine with an error
                if (this.currentCoolingFluid != null) {
                    FluidStack fluid = this.currentCoolingFluid.getStack();
                    startRecipeProcessing();
                    if (!depleteInput(fluid)) stopMachine(ShutDownReasonRegistry.outOfFluid(fluid));
                    updateSlots();
                    endRecipeProcessing();
                }
            }
        }
    }

    // -------------- TEC TECH COMPAT ----------------

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mCasingFrostProof = 0;
        this.mTier = 1;
        // If check for T1 fails, also do a check for T2 structure
        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, 7, 7, 0)) {
            // Reset mCasing in between checks, so they don't count again
            this.mCasingFrostProof = 0;
            if (!this.checkPiece(STRUCTURE_PIECE_MAIN_T2, 7, 7, 0)) {
                return false;
            }
            // Structure is Tier 2
            this.mTier = 2;
        }
        return this.mMaintenanceHatches.size() == 1 && this.mCasingFrostProof >= 700;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] rTexture;
        if (side == facing) {
            if (aActive) {
                rTexture = new ITexture[] { casingTexturePages[0][17], TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { casingTexturePages[0][17], TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_VACUUM_FREEZER)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_VACUUM_FREEZER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { casingTexturePages[0][17] };
        }
        return rTexture;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add(StatCollector.translateToLocalFormatted("BW.infoData.mega_vacuum_freezer.tier", mTier));
        if (mTier == 2) {
            if (currentCoolingFluid != null) {
                info.add(
                    StatCollector.translateToLocalFormatted(
                        "BW.infoData.mega_vacuum_freezer.subspace_cooling.active",
                        currentCoolingFluid.getStack()
                            .getLocalizedName()));
            } else {
                info.add(StatCollector.translateToLocal("BW.infoData.mega_vacuum_freezer.subspace_cooling.inactive"));
            }
        }
        return info.toArray(new String[] {});
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MULTI_MEGA_VACUUM_FREEZER_LOOP;
    }

    private String getCoolantTextFormatted(String fluidType, String litersConsumed, int ocboost) {
        return String.format(
            "%s%s L/s%s : %s%d %s: %s%s",
            EnumChatFormatting.GREEN,
            litersConsumed,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.GOLD,
            ocboost,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.LIGHT_PURPLE,
            fluidType);
    }
}
