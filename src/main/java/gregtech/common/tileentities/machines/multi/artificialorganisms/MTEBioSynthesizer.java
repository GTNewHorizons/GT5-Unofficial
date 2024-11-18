package gregtech.common.tileentities.machines.multi.artificialorganisms;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GTValues.AuthorFourIsTheNumber;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.tileentities.machines.multi.artificialorganisms.hatches.MTEHatchBioInput;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class MTEBioSynthesizer extends MTEExtendedPowerMultiBlockBase<MTEBioSynthesizer> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEBioSynthesizer> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEBioSynthesizer>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] { { "BBB", "AAA", "B~B" }, { "BBB", "A A", "BBB" }, { "BBB", "AAA", "BBB" } })
        .addElement(
            'B',
            ofChain(
                buildHatchAdder(MTEBioSynthesizer.class).adder(MTEBioSynthesizer::addBioHatch)
                    .hatchClass(MTEHatchBioInput.class)
                    .shouldReject(t -> !(t.bioHatch == null))
                    .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                    .dot(2)
                    .buildAndChain(
                        onElementPass(MTEBioSynthesizer::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings2, 0))),
                buildHatchAdder(MTEBioSynthesizer.class)
                    .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch, OutputHatch)
                    .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(0))
                    .dot(1)
                    .buildAndChain(
                        onElementPass(MTEBioSynthesizer::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings2, 0)))))
        .addElement('A', Glasses.chainAllGlasses())
        .build();

    MTEHatchBioInput bioHatch;

    private int AOsInUse = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return super.onRunningTick(aStack);
    }

    public MTEBioSynthesizer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBioSynthesizer(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEBioSynthesizer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBioSynthesizer(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Bio Synthesizer")
            .addInfo("Uses Artificial Organisms to form organic materials")
            .addInfo(AuthorFourIsTheNumber)
            .addSeparator()
            .beginStructureBlock(7, 5, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 85, false)
            .addCasingInfoExactly("Steel Pipe Casing", 24, false)
            .addInputBus("Any Solid Steel Casing", 1)
            .addOutputBus("Any Solid Steel Casing", 1)
            .addInputHatch("Any Solid Steel Casing", 1)
            .addOutputHatch("Any Solid Steel Casing", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        bioHatch = null;
        mEnergyHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0)) return false;
        return mCasingAmount >= 0;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                ArtificialOrganism currentOrganism = getAO();
                if (currentOrganism == null) return SimpleCheckRecipeResult.ofFailure("missing_ao");
                else if (currentOrganism.getCount() <= 0) return SimpleCheckRecipeResult.ofFailure("insufficient_ao");
                else if (currentOrganism.getIntelligence() <= 0)
                    return SimpleCheckRecipeResult.ofFailure("ao_too_stupid");

                AOsInUse = currentOrganism.consumeAOs(50);
                return super.validateRecipe(recipe);
            }

            @Override
            public ProcessingLogic clear() {
                ArtificialOrganism currentOrganism = getAO();
                if (currentOrganism != null) {
                    AOsInUse -= currentOrganism.replenishAOs(AOsInUse);
                }
                return super.clear();
            }
        }.setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    private ArtificialOrganism getAO() {
        if (bioHatch != null) return bioHatch.getAO();
        return null;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (bioHatch != null) {
            if (bioHatch.getAO() != null) {
                PlayerUtils.messagePlayer(
                    aPlayer,
                    "Current AO: " + bioHatch.getAO()
                        .toString());
            }
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.assemblylineVisualRecipes;
    }

    public int getMaxParallelRecipes() {
        return (8 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    private boolean addBioHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchBioInput) {
                ((MTEHatchBioInput) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                if (bioHatch == null) {
                    bioHatch = (MTEHatchBioInput) aMetaTileEntity;
                    return true;
                }
            }
        }
        return false;
    }
}
