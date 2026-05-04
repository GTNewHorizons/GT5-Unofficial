package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
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
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import team.chisel.carving.Carving;

public class MTEIndustrialChisel extends MTEExtendedPowerMultiBlockBase<MTEIndustrialChisel>
    implements ISurvivalConstructable {

    private static final int OFFSET_X = 1;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<MTEIndustrialChisel> STRUCTURE_DEFINITION = null;
    private static ResourceLocation sChiselSound = null;

    private int casingAmount;

    public MTEIndustrialChisel(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialChisel(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialChisel(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Chisel")
            .addBulkMachineInfo(16, 3f, 0.75f)
            .addInfo("Factory Grade Auto Chisel")
            .addInfo("Without a circuit, chisels architecture blocks (reference block goes in CRIB or Controller)")
            .addInfo("Use a programmed circuit to select a specific chiseled output - check NEI for the order")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 5, 5, false)
            .addController("Front left, 3rd layer")
            .addCasingInfoMin("Sturdy Printer Casing", 40, false)
            .addCasingInfoExactly("Steel Frame Box", 37, false)
            .addCasingInfoExactly("Any Tiered Glass", 18, false)
            .addCasingInfoExactly("Steel Pipe Casing", 12, false)
            .addCasingInfoExactly("Steel Gear Box", 6, false)
            .addCasingInfoExactly("Iron Fence", 1, false)
            .addCasingInfoExactly("Cupronickel Coil Block", 1, false)
            .addInputBus("Any Sturdy Printer Casing", 1)
            .addOutputBus("Any Sturdy Printer Casing", 1)
            .addEnergyHatch("Any Sturdy Printer Casing", 1)
            .addMaintenanceHatch("Any Sturdy Printer Casing", 1)
            .addMufflerHatch("Any Sturdy Printer Casing", 1)
            .addStructureAuthors(EnumChatFormatting.GOLD + "IX")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialChisel> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialChisel>builder()
                .addShape(
                    STRUCTURE_PIECE_MAIN,
                    new String[][] { { " HHFFFF", " HHAAAF", " ~HAAAF", " HHAAAF", "FFFFFFF" },
                        { " HHDDDF", "H H   D", "H H   D", "H H   D", "FHHHHHF" },
                        { " HHCCCF", "H H B C", "H H   C", "H H   C", "FHHHEHF" },
                        { " HHDDDF", "H H   D", "H H   D", "H H   D", "FHHHHHF" },
                        { " HHFFFF", " HHAAAF", " HHAAAF", " HHAAAF", "FFFFFFF" } })
                .addElement('A', chainAllGlasses())
                .addElement('B', ofBlock(GameRegistry.findBlock(Mods.IndustrialCraft2.ID, "blockFenceIron"), 0))
                .addElement('C', Casings.SteelGearBoxCasing.asElement())
                .addElement('D', Casings.SteelPipeCasing.asElement())
                .addElement('E', Casings.CupronickelCoilBlock.asElement())
                .addElement('F', ofFrame(Materials.Steel))
                .addElement(
                    'H',
                    buildHatchAdder(MTEIndustrialChisel.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(Casings.SturdyPrinterCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.SturdyPrinterCasing.asElement())))
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
        return checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 40
            && !mMufflerHatches.isEmpty();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.SturdyPrinterCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialChiselActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialChiselActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.SturdyPrinterCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCAIndustrialChisel)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAIndustrialChiselGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.SturdyPrinterCasing.getCasingTexture() };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.industrialChiselRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            public boolean tryCachePossibleRecipesFromPattern(IDualInputInventoryWithPattern inv) {
                // Chisel recipes depend on CRIB shared circuit/manual slots, which can change independently.
                return true;
            }

            @Override
            protected CheckRecipeResult validateRecipe(GTRecipe recipe) {
                if (recipe.maxParallelCalculatedByInputs(1, inputFluids, inputItems) < 1) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                return super.validateRecipe(recipe);
            }
        }.noRecipeCaching()
            .setSpeedBonus(1F / 3F)
            .setEuModifier(0.75F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    protected void sendStartMultiBlockSoundLoop() {
        sendLoopStart(PROCESS_START_SOUND_INDEX);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (16 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    private static ResourceLocation getChiselSound() {
        if (sChiselSound == null) {
            sChiselSound = new ResourceLocation(Carving.chisel.getVariationSound(Blocks.stone, 0));
        }
        return sChiselSound;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        switch (aIndex) {
            case PROCESS_START_SOUND_INDEX -> GTUtility
                .doSoundAtClient(getChiselSound(), getTimeBetweenProcessSounds(), 1.0F, 1.0F, aX, aY, aZ);
            case INTERRUPT_SOUND_INDEX -> GTUtility
                .doSoundAtClient(SoundResource.IC2_MACHINES_INTERRUPT_ONE, 100, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialChisel;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
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
