package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTECyclotron extends GTPPMultiBlockBase<MTECyclotron> implements ISurvivalConstructable {

    private int mCasing;
    private static IStructureDefinition<MTECyclotron> STRUCTURE_DEFINITION = null;

    public MTECyclotron(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECyclotron(String aName) {
        super(aName);
    }

    @Override
    public String getMachineType() {
        return "Particle Accelerator";
    }

    @Override
    public long maxEUStore() {
        return 1800000000L;
    }

    @Override
    public MetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTECyclotron(this.mName);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public IStructureDefinition<MTECyclotron> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTECyclotron>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "               ", "      hhh      ", "    hh   hh    ", "   h       h   ",
                                "  h         h  ", "  h         h  ", " h           h ", " h           h ",
                                " h           h ", "  h         h  ", "  h         h  ", "   h       h   ",
                                "    hh   hh    ", "      hhh      ", "               ", },
                            { "      hhh      ", "    hhccchh    ", "   hcchhhcch   ", "  hchh   hhch  ",
                                " hch       hch ", " hch       hch ", "hch         hch", "hch         hch",
                                "hch         hch", " hch       hch ", " hch       hch ", "  hchh   hhch  ",
                                "   hcch~hcch   ", "    hhccchh    ", "      hhh      ", },
                            { "               ", "      hhh      ", "    hh   hh    ", "   h       h   ",
                                "  h         h  ", "  h         h  ", " h           h ", " h           h ",
                                " h           h ", "  h         h  ", "  h         h  ", "   h       h   ",
                                "    hh   hh    ", "      hhh      ", "               ", } }))
                .addElement(
                    'h',
                    buildHatchAdder(MTECyclotron.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch, OutputHatch)
                        .casingIndex(44)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(getCasingBlock(), getCasingMeta()))))
                .addElement('c', ofBlock(getCyclotronCoil(), getCyclotronCoilMeta()))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 7, 1, 12);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 7, 1, 12, elementBudget, env, false, true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_FUSION_LOOP;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 7, 1, 12) && mCasing >= 40;
    }

    public Block getCasingBlock() {
        return ModBlocks.blockCasings2Misc;
    }

    public int getCasingMeta() {
        return 10;
    }

    public Block getCyclotronCoil() {
        return ModBlocks.blockCasings2Misc;
    }

    public int getCyclotronCoilMeta() {
        return 9;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Super Magnetic Speed Shooter")
            .addSeparator()
            .addInfo("Particles are accelerated over 186 revolutions to 80% light speed")
            .addInfo("Can produce a continuous beam current of 2.2 mA at 590 MeV")
            .addInfo("Which will be extracted from the Isochronous Cyclotron")
            .addSeparator()
            .addInfo("Similar structure to the Fusion Reactor")
            .addInfo("Any external casing can be a hatch/bus, unlike Fusion")
            .addInfo("Cyclotron Machine Casings around Cyclotron Coil Blocks")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addCasingInfoMin("Cyclotron Machine Casings", 40, false)
            .addCasingInfoMin("Cyclotron Coil", 32, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return getIconOverlay();
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return getIconGlowOverlay();
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return getIconOverlay();
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return getIconGlowOverlay();
    }

    @Override
    protected int getCasingTextureId() {
        return 44;
    }

    public IIconContainer getIconOverlay() {
        if (this.getBaseMetaTileEntity()
            .isActive()) {
            return TexturesGtBlock.Overlay_MatterFab_Active_Animated;
        }
        return TexturesGtBlock.Overlay_MatterFab_Animated;
    }

    public IIconContainer getIconGlowOverlay() {
        if (this.getBaseMetaTileEntity()
            .isActive()) {
            return TexturesGtBlock.Overlay_MatterFab_Active_Animated_Glow;
        }
        return TexturesGtBlock.Overlay_MatterFab_Animated_Glow;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.cyclotronRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult result = super.process();
                if (result.wasSuccessful()) {
                    for (ItemStack s : outputItems) {
                        if (s != null) {
                            if (s.getItem() instanceof IonParticles) {
                                long aCharge = IonParticles.getChargeState(s);
                                if (aCharge == 0) {
                                    IonParticles.setChargeState(
                                        s,
                                        MathUtils.getRandomFromArray(
                                            new int[] { -5, -5, -4, -4, -4, -3, -3, -3, -3, -3, -2, -2, -2, -2, -2, -2,
                                                -2, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                                1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4,
                                                5, 5, 5, 6, 6 }));
                                }
                            }
                        }
                    }
                }
                return result;
            }
        };
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!this.mOutputBusses.isEmpty()) {
            for (MTEHatchOutputBus g : this.mOutputBusses) {
                if (g != null) {
                    for (ItemStack s : g.mInventory) {
                        if (s != null) {
                            if (s.getItem() instanceof IonParticles) {
                                long aCharge = IonParticles.getChargeState(s);
                                if (aCharge == 0) {
                                    IonParticles.setChargeState(
                                        s,
                                        MathUtils.getRandomFromArray(
                                            new int[] { -5, -5, -4, -4, -4, -3, -3, -3, -3, -3, -2, -2, -2, -2, -2, -2,
                                                -2, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                                                1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4,
                                                5, 5, 5, 6, 6 }));
                                }
                            }
                        }
                    }
                }
            }
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public String[] getExtraInfoData() {
        int powerRequired = 0;
        if (this.mLastRecipe != null) {
            powerRequired = this.mLastRecipe.mEUt;
        }

        return new String[] { "COMET - Compact Cyclotron MK I", "EU Required: " + powerRequired + "EU/t",
            "Stored EU: " + this.getEUVar() + " / " + maxEUStore() };
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
