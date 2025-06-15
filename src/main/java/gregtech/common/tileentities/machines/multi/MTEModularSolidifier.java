package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import ggfab.api.GGFabRecipeMaps;
import gregtech.api.objects.GTDualInputPattern;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.tileentities.machines.IDualInputInventoryWithPattern;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.misc.GTStructureChannels;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class MTEModularSolidifier extends MTEExtendedPowerMultiBlockBase<MTEModularSolidifier>
    implements ISurvivalConstructable {
private static int horizontaloffset = 4;
private static int verticaloffset = 1;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEModularSolidifier> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEModularSolidifier>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(
        new String[][]{
        {" ABBBBBA ","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A"," ABBBBBA "},
        {" AAA~AAA ","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A","A       A"," AAAAAAA "},
        {" AAAAAAA ","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA","AAAAAAAAA"," AAAAAAA "}
    }))
        //spotless:on
        .addElement(
            'A',
            buildHatchAdder(MTEModularSolidifier.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((BlockCasings13) GregTechAPI.sBlockCasings13).getTextureIndex(15))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTEModularSolidifier::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings13, 15))))
        .addElement('B', chainAllGlasses())
        .build();

    public MTEModularSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEModularSolidifier(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEModularSolidifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEModularSolidifier(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 15)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Solidifier, Tool Casting Machine")
            .addInfo("100% faster than singleblock machines of the same voltage")
            .addInfo("Gains 12 parallels per voltage tier")
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Wooden Casing", 1)
            .addOutputBus("Any Wooden Casing", 1)
            .addInputHatch("Any Wooden Casing", 1)
            .addOutputHatch("Any Wooden Casing", 1)
            .addEnergyHatch("Any Wooden Casing", 1)
            .addMaintenanceHatch("Any Wooden Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontaloffset, verticaloffset, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontaloffset, verticaloffset, 0, elementBudget, env, false, true);
    }


    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, horizontaloffset, verticaloffset, 0) && mCasingAmount >= 14;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic(){
            RecipeMap<?> currentRecipeMap = RecipeMaps.fluidSolidifierRecipes;

            // Override is needed so that switching recipe maps does not stop recipe locking.
            @Override
            protected RecipeMap<?> getCurrentRecipeMap() {
                lastRecipeMap = currentRecipeMap;
                return currentRecipeMap;
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                currentRecipeMap = RecipeMaps.fluidSolidifierRecipes;
                CheckRecipeResult result = super.process();
                if (result.wasSuccessful()) return result;

                currentRecipeMap = GGFabRecipeMaps.toolCastRecipes;
                return super.process();
            }
            //for cribuffers/proxies?
            @Override
            public boolean tryCachePossibleRecipesFromPattern(IDualInputInventoryWithPattern inv) {
                if (dualInvWithPatternToRecipeCache.containsKey(inv)) {
                    activeDualInv = inv;
                    return true;
                }

                GTDualInputPattern inputs = inv.getPatternInputs();
                setInputItems(inputs.inputItems);
                setInputFluids(inputs.inputFluid);
                Set<GTRecipe> recipes = findRecipeMatches(RecipeMaps.fluidSolidifierRecipes)
                    .collect(Collectors.toSet());
                if (recipes.isEmpty())
                    recipes = findRecipeMatches(GGFabRecipeMaps.toolCastRecipes).collect(Collectors.toSet());
                if (!recipes.isEmpty()) {
                    dualInvWithPatternToRecipeCache.put(inv, recipes);
                    activeDualInv = inv;
                    return true;
                }
                return false;
            }


        }.setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (12 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public @NotNull Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.fluidSolidifierRecipes, GGFabRecipeMaps.toolCastRecipes);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.fluidSolidifierRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}
