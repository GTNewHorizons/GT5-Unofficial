package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GTValues.AuthorOmdaCZ;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import ggfab.api.GGFabRecipeMaps;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEMultiSolidifier extends MTEExtendedPowerMultiBlockBase<MTEMultiSolidifier>
    implements ISurvivalConstructable {

    private static final String MS_LEFT_MID = "leftmid";
    private static final String MS_RIGHT_MID = "rightmid";
    private static final String MS_END = "end";

    private static final int BASE_PARALLELS = 2;
    private static final int PARALLELS_PER_WIDTH = 3;
    private static final double DECAY_RATE = 0.025;

    private byte glassTier = 0;
    protected int width;
    private int casingAmount;
    private float speedup = 1;
    private int runningTickCounter = 0;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMultiSolidifier> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMultiSolidifier>builder()
        .addShape(
            MS_LEFT_MID,
            (transpose(
                new String[][] { { "  ", "BB", "BB", "BB", }, { "  ", "AA", "D ", "AA", }, { "  ", "AA", "  ", "AA", },
                    { "  ", "CC", "FC", "CC", }, { "  ", "BB", "BB", "BB", } })))
        .addShape(
            MS_RIGHT_MID,
            (transpose(
                new String[][] { { "  ", "BB", "BB", "BB" }, { "  ", "AA", " D", "AA" }, { "  ", "AA", "  ", "AA" },
                    { "  ", "CC", "CF", "CC" }, { "  ", "BB", "BB", "BB" } })))
        .addShape(
            MS_END,
            (transpose(
                new String[][] { { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" },
                    { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" } })))
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (transpose(
                new String[][] { { "       ", "BBBBBBB", "BBBBBBB", "BBBBBBB", "       " },
                    { "BBBBBBB", "       ", "D D D D", "       ", "BBBBBBB" },
                    { "AAAAAAA", "       ", "       ", "       ", "AAAAAAA" },
                    { "CCCBCCC", "       ", "F F F F", "       ", "CCCCCCC" },
                    { "BBB~BBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" } })))
        .addElement(
            'A',
            withChannel(
                "glass",
                BorosilicateGlass
                    .ofBoroGlass((byte) 0, (byte) 1, Byte.MAX_VALUE, (te, t) -> te.glassTier = t, te -> te.glassTier)))
        .addElement(
            'B',
            buildHatchAdder(MTEMultiSolidifier.class).atLeast(InputBus, InputHatch, OutputBus, Maintenance, Energy)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(13))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTEMultiSolidifier::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 13))))

        .addElement('C', ofBlock(GregTechAPI.sBlockCasings10, 14))
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings1, 11))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings4, 1))
        .build();

    public MTEMultiSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMultiSolidifier(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMultiSolidifier(this.mName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 13)),
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
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 13)),
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
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 13)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Solidifier, Tool Casting Machine")
            .addInfo(
                "Can use " + EnumChatFormatting.YELLOW
                    + "Solidifier Hatches"
                    + EnumChatFormatting.GRAY
                    + " to hold different molds")
            .addInfo("Speeds up to a maximum of 200% faster than singleblock machines while running")
            .addInfo("Decays at double the rate that it speeds up at")
            .addInfo("Only uses 80% of the EU/t normally required")
            .addInfo("Processes " + BASE_PARALLELS + " items per voltage tier")
            .addInfo("Processes an additional " + PARALLELS_PER_WIDTH + " items per voltage tier per width expansion")
            .addInfo("Energy hatch limited by glass tier, UMV Glass unlocks all")
            .addInfo(EnumChatFormatting.BLUE + "Pretty Ⱄⱁⰾⰻⰴ, isn't it")
            .beginVariableStructureBlock(9, 33, 5, 5, 5, 5, true)
            .addController("Front Center bottom")
            .addCasingInfoRange("Solidifier Casing", 91, 211, false)
            .addCasingInfoRange("Solidifier Radiator", 13, 73, false)
            .addCasingInfoRange("Heat Proof Machine Casing", 4, 16, false)
            .addCasingInfoRange("Clean Stainless Steel Machine Casing", 4, 16, false)
            .addCasingInfoRange("Glass", 21, 117, true)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .toolTipFinisher(AuthorOmdaCZ);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 0);
        // max Width, minimal mid-pieces to build on each side
        int totalWidth = Math.min(stackSize.stackSize - 1, 6);
        for (int i = 0; i < totalWidth; i++) {
            // pieces are 2 wide so offset 5 from controller and number of pieces times width of each piece
            buildPiece(MS_LEFT_MID, stackSize, hintsOnly, 5 + 2 * i, 4, 0);
            // the extra offset to account for piece width isn't needed in this direction
            buildPiece(MS_RIGHT_MID, stackSize, hintsOnly, -4 - 2 * i, 4, 0);
        }
        buildPiece(MS_END, stackSize, hintsOnly, 4 + 2 * totalWidth, 4, 0);
        buildPiece(MS_END, stackSize, hintsOnly, -4 - 2 * totalWidth, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int totalWidth = Math.min(stackSize.stackSize - 1, 6);
        for (int i = 0; i < totalWidth; i++) {
            built = survivialBuildPiece(MS_LEFT_MID, stackSize, 5 + 2 * i, 4, 0, elementBudget, env, false, true);
            built += survivialBuildPiece(MS_RIGHT_MID, stackSize, -4 - 2 * i, 4, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        built = survivialBuildPiece(MS_END, stackSize, -4 - 2 * totalWidth, 4, 0, elementBudget, env, false, true);
        built += survivialBuildPiece(MS_END, stackSize, 4 + 2 * totalWidth, 4, 0, elementBudget, env, false, true);
        return built;
    }

    @Override
    public IStructureDefinition<MTEMultiSolidifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        casingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        width = 0;
        casingAmount = 0;
        glassTier = 0;

        if (checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) {
            while (width < (6)) {
                if (checkPiece(MS_RIGHT_MID, -4 - 2 * width, 4, 0) && checkPiece(MS_LEFT_MID, 5 + 2 * width, 4, 0)) {
                    width++;
                } else break;
            }
        } else return false;
        if (!checkPiece(MS_END, -4 - 2 * width, 4, 0) || !checkPiece(MS_END, 4 + 2 * width, 4, 0)) {
            return false;
        }

        for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
            if (glassTier < VoltageIndex.UMV & mEnergyHatch.mTier > glassTier) {
                return false;
            }
        }

        return casingAmount >= (91 + width * 20);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            RecipeMap<?> currentRecipeMap = RecipeMaps.fluidSolidifierRecipes;

            // Override is needed so that switching recipe maps does not stop recipe locking.
            @Override
            protected RecipeMap<?> preProcess() {
                lastRecipeMap = currentRecipeMap;

                if (maxParallelSupplier != null) {
                    maxParallel = maxParallelSupplier.get();
                }
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

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                setSpeedBonus(1F / speedup);
                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes)
            .setEuModifier(0.8F);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        runningTickCounter++;
        if (runningTickCounter % 10 == 0 && speedup < 3) {
            runningTickCounter = 0;
            speedup += 0.025F;
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        if (mMaxProgresstime == 0 && speedup > 1) {
            if (aTick % 5 == 0) {
                speedup = (float) Math.max(1, speedup - DECAY_RATE);
            }
        }
    }

    public int getMaxParallelRecipes() {
        return (BASE_PARALLELS + (width * PARALLELS_PER_WIDTH)) * GTUtility.getTier(this.getMaxInputVoltage());
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
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("speedup")) speedup = aNBT.getFloat("speedup");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setFloat("speedup", speedup);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setFloat("speedup", speedup);
        tag.setInteger("parallels", getMaxParallelRecipes());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.multiblock.speed") + ": "
                + EnumChatFormatting.WHITE
                + String.format("%.1f%%", 100 * tag.getFloat("speedup")));
        currentTip.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.WHITE
                + tag.getInteger("parallels"));
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

    @NotNull
    @Override
    protected CheckRecipeResult doCheckRecipe() {
        CheckRecipeResult result = CheckRecipeResultRegistry.NO_RECIPE;

        // check crafting input hatches first
        if (supportsCraftingMEBuffer()) {
            for (IDualInputHatch dualInputHatch : mDualInputHatches) {
                for (var it = dualInputHatch.inventories(); it.hasNext();) {
                    IDualInputInventory slot = it.next();
                    processingLogic.setInputItems(slot.getItemInputs());
                    processingLogic.setInputFluids(slot.getFluidInputs());
                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        result = foundResult;
                    }
                }
            }
        }

        // Logic for GT_MetaTileEntity_Hatch_Solidifier
        for (MTEHatchInput solidifierHatch : mInputHatches) {
            if (solidifierHatch instanceof MTEHatchSolidifier hatch) {
                ItemStack mold = hatch.getMold();
                FluidStack fluid = solidifierHatch.getFluid();

                if (mold != null && fluid != null) {
                    List<ItemStack> inputItems = new ArrayList<>();
                    inputItems.add(mold);

                    processingLogic.setInputItems(inputItems.toArray(new ItemStack[0]));
                    processingLogic.setInputFluids(fluid);

                    CheckRecipeResult foundResult = processingLogic.process();
                    if (foundResult.wasSuccessful()) {
                        return foundResult;
                    }
                    if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                        // Recipe failed in interesting way, so remember that and continue searching
                        result = foundResult;
                    }
                }
            }
        }
        processingLogic.clear();
        processingLogic.setInputFluids(getStoredFluids());
        // Default logic
        for (MTEHatchInputBus bus : mInputBusses) {
            if (bus instanceof MTEHatchCraftingInputME) {
                continue;
            }
            List<ItemStack> inputItems = new ArrayList<>();
            for (int i = bus.getSizeInventory() - 1; i >= 0; i--) {
                ItemStack stored = bus.getStackInSlot(i);
                if (stored != null) {
                    inputItems.add(stored);
                }
            }
            if (canUseControllerSlotForRecipe() && getControllerSlot() != null) {
                inputItems.add(getControllerSlot());
            }
            processingLogic.setInputItems(inputItems.toArray(new ItemStack[0]));
            CheckRecipeResult foundResult = processingLogic.process();
            if (foundResult.wasSuccessful()) {
                return foundResult;
            }
            if (foundResult != CheckRecipeResultRegistry.NO_RECIPE) {
                // Recipe failed in interesting way, so remember that and continue searching
                result = foundResult;
            }
        }
        return result;
    }

    @Override
    public boolean isInputSeparationEnabled() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ) {
        batchMode = !batchMode;
        if (batchMode) {
            GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOn"));
        } else {
            GTUtility.sendChatToPlayer(aPlayer, StatCollector.translateToLocal("misc.BatchModeTextOff"));
        }
        return true;
    }
}
