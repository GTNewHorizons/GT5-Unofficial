package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEIndustrialSifter extends MTEExtendedPowerMultiBlockBase<MTEIndustrialSifter>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 4;
    private static final int OFFSET_Z = 0;
    private int casingAmount;
    private static IStructureDefinition<MTEIndustrialSifter> STRUCTURE_DEFINITION = null;

    public MTEIndustrialSifter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialSifter(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialSifter(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Sifter")
            .addBulkMachineInfo(4, 5f, 0.75f)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(5, 6, 5, false)
            .addController("Front center, 2nd layer")
            .addCasing("45-49", "Industrial Sieve Casing", false)
            .addCasing("19", "Large Sieve Grate", false)
            .addCasing("16", "Steel Frame Box", false)
            .addEnergyHatch("1+", "Any casing", 1)
            .addMaintenanceHatch("1", "Any casing", 1)
            .addMufflerHatch("1", "Any casing", 1)
            .addInputAny("1+", "Any casing", 1)
            .addOutputAny("1+", "Any casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "VorTex")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialSifter> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialSifter>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] { { "BBBBB", "ABBBA", "A   A", "A   A", "AB~BA", "BBBBB" },
                        { "B   B", "BCCCB", " CCC ", "     ", "B   B", "BBBBB" },
                        { "B   B", "BCCCB", " CCC ", "  C  ", "B   B", "BBBBB" },
                        { "B   B", "BCCCB", " CCC ", "     ", "B   B", "BBBBB" },
                        { "BBBBB", "ABBBA", "A   A", "A   A", "ABBBA", "BBBBB" } })
                .addElement('A', ofFrame(Materials.Steel))
                .addElement(
                    'B',
                    buildHatchAdder(MTEIndustrialSifter.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(Casings.IndustrialSieveCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.IndustrialSieveCasing.asElement())))
                .addElement('C', Casings.LargeSieveGrate.asElement())
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
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        casingAmount = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) return;
        checkCasingMin(errors, casingAmount, 45);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasAnyInput(errors);
        checkHasAnyOutput(errors);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            TexturesGtBlock.oMCDIndustrialSifter,
            TexturesGtBlock.oMCDIndustrialSifterGlow,
            TexturesGtBlock.oMCDIndustrialSifterActive,
            TexturesGtBlock.oMCDIndustrialSifterActiveGlow);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.IndustrialSieveCasing.getCasingTexture();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.sifterRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -1;
    }

    @Override
    public void onPreTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive())
            && (aBaseMetaTileEntity.getFrontFacing() != ForgeDirection.UP)
            && (!aBaseMetaTileEntity.hasCoverAtSide(ForgeDirection.UP))
            && (!aBaseMetaTileEntity.getOpacityAtSide(ForgeDirection.UP))) {
            final Random tRandom = aBaseMetaTileEntity.getWorld().rand;
            if (tRandom.nextFloat() > 0.4) return;

            final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * 2;
            final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * 2;

            aBaseMetaTileEntity.getWorld()
                .spawnParticle(
                    "smoke",
                    (aBaseMetaTileEntity.getXCoord() + xDir + 2.1F) - (tRandom.nextFloat() * 3.2F),
                    aBaseMetaTileEntity.getYCoord() + 2.5f + (tRandom.nextFloat() * 1.2F),
                    (aBaseMetaTileEntity.getZCoord() + zDir + 2.1F) - (tRandom.nextFloat() * 3.2F),
                    0.0,
                    0.0,
                    0.0);
        }
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 5F)
            .setEuModifier(0.75F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialSifter;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_SIFTER_LOOP;
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
}
