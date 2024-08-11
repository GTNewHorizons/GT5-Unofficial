package gregtech.common.tileentities.machines.multi.compressor;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.GT_Values.Ollie;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings2;
import gregtech.common.items.GT_MetaGenerated_Item_01;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_BlackHoleCompressor
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_BlackHoleCompressor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_BlackHoleCompressor> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_BlackHoleCompressor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (new String[][] { { "AAA", "A~A", "AAA" }, { "AAA", "A A", "AAA" }, { "AAA", "AAA", "AAA" } }))
        .addElement(
            'A',
            buildHatchAdder(GT_MetaTileEntity_BlackHoleCompressor.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch, OutputHatch)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_BlackHoleCompressor::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings2, 0))))
        .build();

    private boolean blackholeOn = false;
    private int catalyzingCounter = 0;
    private float blackHoleStability = 100;

    private final FluidStack blackholeCatalyzingCost = (MaterialsUEVplus.SpaceTime).getMolten(1);
    private int catalyzingCostModifier = 1;

    public GT_MetaTileEntity_BlackHoleCompressor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_BlackHoleCompressor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_BlackHoleCompressor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BlackHoleCompressor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)) };
        }
        return rTexture;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Compressor/Advanced Neutronium Compressor")
            .addInfo("Controller Block for the Semi-Stable Black Hole Containment Field")
            .addInfo("Uses the immense power of the event horizon to compress things")
            .addInfo("No longer requires heat management to perform perfect compression")
            .addInfo("Insert a Black Hole Activation Catalyst to open a black hole")
            .addInfo("The black hole will begin its life at 100% stability and slowly decay")
            .addInfo("Running recipes in the machine will slow the decay significantly")
            .addInfo(
                "The decay can be " + EnumChatFormatting.BOLD
                    + "halted"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " by inserting spacetime")
            .addInfo("Requires increasingly more spacetime to maintain!")
            .addInfo("Once the black hole becomes unstable, it will void all inputs for recipes which require it")
            .addInfo("Insert a Black Hole Deactivation Catalyst to close the black hole")
            .addInfo(AuthorFourIsTheNumber + EnumChatFormatting.RESET + " & " + Ollie)
            .addSeparator()
            .beginStructureBlock(7, 5, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 85, false)
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
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mEnergyHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0)) return false;
        if (mCasingAmount < 0) return false;

        return true;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GT_Utility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setBoolean("blackholeOn", blackholeOn);
        tag.setFloat("blackHoleStability", blackHoleStability);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("blackholeOn")) {
            if (tag.getFloat("blackHoleStability") > 0) {
                currentTip.add(EnumChatFormatting.DARK_PURPLE + "Black Hole Active");
                currentTip.add(
                    EnumChatFormatting.DARK_PURPLE + " Stability: "
                        + EnumChatFormatting.BOLD
                        + Math.round(tag.getFloat("blackHoleStability"))
                        + "%");
            } else {
                currentTip.add(EnumChatFormatting.RED + "BLACK HOLE UNSTABLE");
            }
        } else currentTip.add(EnumChatFormatting.DARK_PURPLE + "Black Hole Offline");
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected Stream<GT_Recipe> findRecipeMatches(@Nullable RecipeMap<?> map) {

                // Loop through all items and look for the Activation and Deactivation Catalysts
                // Deactivation resets stability to 100 and catalyzing cost to 1
                for (ItemStack inputItem : inputItems) {
                    if (inputItem.getItem() instanceof GT_MetaGenerated_Item_01) {
                        if (inputItem.getItemDamage() == 32418 && !blackholeOn) {
                            inputItem.stackSize -= 1;
                            blackholeOn = true;
                            break;
                        } else if (inputItem.getItemDamage() == 32419 && blackholeOn) {
                            inputItem.stackSize -= 1;
                            blackholeOn = false;
                            blackHoleStability = 100;
                            catalyzingCostModifier = 1;
                            break;
                        }
                    }
                }

                Stream<GT_Recipe> compressorRecipes = RecipeMaps.compressorRecipes.findRecipeQuery()
                    .items(inputItems)
                    .cachedRecipe(lastRecipe)
                    .findAll();
                Stream<GT_Recipe> neutroniumRecipes = RecipeMaps.neutroniumCompressorRecipes.findRecipeQuery()
                    .items(inputItems)
                    .cachedRecipe(lastRecipe)
                    .findAll();
                compressorRecipes = Stream.concat(compressorRecipes, neutroniumRecipes);
                return compressorRecipes;
            }

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {

                // Default speed bonus
                setSpeedBonus(1F);

                // If recipe needs a black hole and one is not open, just wait
                // If the recipe doesn't require black hole, incur a 0.5x speed penalty
                // If recipe doesn't require black hole but one is open, give 5x speed bonus
                if (recipe.mSpecialValue > 0) {
                    if (!blackholeOn) return CheckRecipeResultRegistry.NO_BLACK_HOLE;
                } else {
                    if (blackHoleStability <= 0) setSpeedBonus(2F);
                    else if (blackholeOn) setSpeedBonus(0.2F);
                }
                return super.validateRecipe(recipe);
            }

            @Nonnull
            protected CheckRecipeResult onRecipeStart(@Nonnull GT_Recipe recipe) {
                // If recipe needs a black hole and one is active but unstable, continuously void items
                if (blackHoleStability <= 0 && recipe.mSpecialValue > 0) {
                    return CheckRecipeResultRegistry.UNSTABLE_BLACK_HOLE;
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        };
        // .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aTick % 20 == 0) {
            if (blackholeOn && blackHoleStability >= 0) {
                float stabilityDecrease = 1F;
                // If the machine is running, reduce stability loss by 25%
                if (this.mProgresstime != 0) {
                    stabilityDecrease = 0.75F;
                }
                // Search all hatches for catalyst fluid
                // If found enough, drain it and reduce stability loss to 0
                // Every 30 drains, double the cost
                FluidStack totalCost = new FluidStack(blackholeCatalyzingCost, catalyzingCostModifier);
                for (GT_MetaTileEntity_Hatch_Input hatch : mInputHatches) {
                    if (drain(hatch, totalCost, false)) {
                        drain(hatch, totalCost, true);
                        catalyzingCounter += 1;
                        stabilityDecrease = 0;
                        if (catalyzingCounter >= 30) {
                            catalyzingCostModifier *= 2;
                            catalyzingCounter = 0;
                        }
                    }
                }
                if (blackHoleStability >= 0) blackHoleStability -= stabilityDecrease;
                else blackHoleStability = 0;
            }
        }
    }

    public int getMaxParallelRecipes() {
        return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.neutroniumCompressorRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.compressorRecipes, RecipeMaps.neutroniumCompressorRecipes);
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
