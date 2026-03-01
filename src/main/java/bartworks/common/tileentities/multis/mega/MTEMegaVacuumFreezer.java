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

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
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
import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.BlockCasingsAbstract;

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

    private static final String anyCasing = GTUtility.nestParams(
        "GT5U.MBTT.HatchInfo",
        ItemList.Casing_FrostProof.get(1)
            .getDisplayName());

    private static class SubspaceCoolingFluid {

        public Materials material;
        public int perfectOverclocks;
        // Consumption per second of runtime
        public long amount;

        public SubspaceCoolingFluid(Materials material, int perfectOverclocks, long amount) {
            this.material = material;
            this.perfectOverclocks = perfectOverclocks;
            this.amount = amount;
        }

        public FluidStack getStack() {
            FluidStack stack = material.getFluid(amount);
            // FUCK THIS FUCK THIS FUCK THIS
            if (stack == null) {
                return material.getMolten(amount);
            }
            return stack;
        }
    }

    private static final ArrayList<SubspaceCoolingFluid> SUBSPACE_COOLING_FLUIDS = new ArrayList<>(
        Arrays.asList(
            new SubspaceCoolingFluid(Materials.SpaceTime, 1, 75),
            new SubspaceCoolingFluid(Materials.Space, 2, 50),
            new SubspaceCoolingFluid(Materials.Eternity, 3, 25)));

    private SubspaceCoolingFluid currentCoolingFluid = null;

    private static final int CASING_INDEX = 17;
    private static final int CASING_INDEX_T2 = ((BlockCasingsAbstract) GregTechAPI.sBlockCasings8).getTextureIndex(14);
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
                .buildAndChain(onElementPass(x -> x.mCasingFrostProof++, ofBlock(GregTechAPI.sBlockCasings2, 1))))
        // Infinity Cooled Casing
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 14))
        .build();

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("machtype.mvf")
            .addInfo("gt.mvf.tips.1")
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addSeparator()
            .addTecTechHatchInfo()
            .addUnlimitedTierSkips()
            .addSeparator()
            .addInfo(
                "gt.mvf.tips.2",
                Materials.SpaceTime.getLocalizedName(),
                FluidRegistry.getFluidStack("spatialfluid", 1)
                    .getLocalizedName(),
                Materials.Eternity.getLocalizedName())
            .addInfo("gt.mvf.tips.3")
            .beginStructureBlock(15, 15, 15, true)
            .addController("front_center")
            .addEnergyHatch(anyCasing, 1)
            .addMaintenanceHatch(anyCasing, 1)
            .addInputHatch(anyCasing, 1)
            .addOutputHatch(anyCasing, 1)
            .addInputBus(anyCasing, 1)
            .addOutputBus(anyCasing, 1)
            .addStructureInfo("gt.mvf.info.t1_multi")
            .addCasingInfoMin(
                ItemList.Casing_FrostProof.get(1)
                    .getDisplayName(),
                800)
            .addStructureInfo("gt.mvf.info.t2_multi")
            .addCasingInfoMin(
                ItemList.Casing_FrostProof.get(1)
                    .getDisplayName(),
                700)
            .addCasingInfoExactly(
                ItemList.InfinityCooledCasing.get(1)
                    .getDisplayName(),
                384)
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
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        if (stackSize.stackSize == 1) {
            return this
                .survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 7, 0, realBudget, source, actor, false, true);
        } else {
            return this.survivalBuildPiece(
                STRUCTURE_PIECE_MAIN_T2,
                stackSize,
                7,
                7,
                0,
                realBudget,
                source,
                actor,
                false,
                true);
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

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            this.batchMode = !this.batchMode;
            if (this.batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return super.onWireCutterRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ, aTool);
    }

    public SubspaceCoolingFluid findSubspaceCoolingFluid() {
        // Loop over all hatches and find the first match with a valid fluid
        for (MTEHatchInput hatch : mInputHatches) {
            Optional<SubspaceCoolingFluid> fluid = SUBSPACE_COOLING_FLUIDS.stream()
                .filter(candidate -> drain(hatch, candidate.getStack(), false))
                .findFirst();
            if (fluid.isPresent()) return fluid.get();
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
                    for (MTEHatchInput hatch : mInputHatches) {
                        if (drain(hatch, fluid, false)) {
                            drain(hatch, fluid, true);
                            return;
                        }
                    }
                    // If we exited this loop without returning from the function, no matching fluid was found, so
                    // stop the machine - we ran out of coolant
                    stopMachine(ShutDownReasonRegistry.outOfFluid(fluid));
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
}
