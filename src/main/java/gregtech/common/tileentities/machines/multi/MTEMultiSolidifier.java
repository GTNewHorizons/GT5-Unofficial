package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
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
import java.util.List;

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
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
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

    protected final String MS_LEFT_MID = mName + "leftmid";
    protected final String MS_RIGHT_MID = mName + "rightmid";
    protected final String MS_END = mName + "end";

    private final int PARALLELS_PER_WIDTH = 3;

    private byte glassTier = 0;
    private static final double decay_rate = 0.025;

    private final String STRUCTURE_PIECE_MAIN = "main";
    private final IStructureDefinition<MTEMultiSolidifier> STRUCTURE_DEFINITION = StructureDefinition
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
                    { "BBBBBBB", "       ", "D D D D", "       ", "AAAAAAA" },
                    { "AAAAAAA", "       ", "       ", "       ", "AAAAAAA" },
                    { "CCCBCCC", "       ", "F F F F", "       ", "CCCCCCC" },
                    { "BBB~BBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" } })))
        .addElement(
            'A',
            BorosilicateGlass
                .ofBoroGlass((byte) 0, (byte) 1, Byte.MAX_VALUE, (te, t) -> te.glassTier = t, te -> te.glassTier))
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
        tt.addMachineType("Fluid Solidifier")
            .addInfo("Controller Block for the Fluid Shaper")
            .addInfo("Speeds up to a maximum of 300% faster than singleblock machines while running")
            .addInfo("Decays at double the Rate that it Speeds up at")
            .addInfo("Only uses 80% of the EU/t normally required")
            .addInfo("Starts with 4 Parallels")
            .addInfo("Gain " + PARALLELS_PER_WIDTH + " Parallels per Width Expansion and Multiplied by Voltage Tier")
            .addInfo("Energy Hatch Based on Glass Tier, UMV Glass Unlocks all")
            .addInfo(EnumChatFormatting.BLUE + "Pretty Ⱄⱁⰾⰻⰴ, isn't it")
            .addInfo(AuthorOmdaCZ)
            .addInfo(
                "With the Help Of" + " "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "Four"
                    + EnumChatFormatting.RESET
                    + ", "
                    + EnumChatFormatting.AQUA
                    + "GDCloud"
                    + EnumChatFormatting.RESET
                    + ", "
                    + EnumChatFormatting.GOLD
                    + "Ba"
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "ps"
                    + EnumChatFormatting.RESET
                    + " & "
                    + EnumChatFormatting.DARK_AQUA
                    + "TheEpicGamer274")
            .addSeparator()
            .beginVariableStructureBlock(17, 33, 5, 5, 5, 5, true)
            .addController("Front Center bottom")
            .addCasingInfoMin("Solidifier Casing", 146, false)
            .addCasingInfoMin("Radiator Casing", 18, false)
            .addCasingInfoMin("Heat Proof Casing", 4, false)
            .addCasingInfoMin("Solid Steel Casing", 4, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 0);
        // max Width, minimal mid-pieces to build on each side
        int tTotalWidth = Math.min(stackSize.stackSize + 1, 6);
        for (int i = 1; i < tTotalWidth - 1; i++) {
            // horizontal offset 3 from controller and number of pieces times width of each piece
            buildPiece(MS_LEFT_MID, stackSize, hintsOnly, 3 + 2 * i, 4, 0);
            // the same but on other side of controller, for some reason -2 works right but -3 is weird
            buildPiece(MS_RIGHT_MID, stackSize, hintsOnly, -2 - 2 * i, 4, 0);
        }
        // trial and error numbers that work
        buildPiece(MS_END, stackSize, hintsOnly, (tTotalWidth + 2) * 2 - 4, 4, 0);
        buildPiece(MS_END, stackSize, hintsOnly, (-tTotalWidth - 2) * 2 + 4, 4, 0);
    }

    protected int mWidth;
    protected int nWidth;

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        mWidth = 0;
        nWidth = 0;
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int tTotalWidth = Math.min(stackSize.stackSize + 1, 6);
        for (int i = 1; i < tTotalWidth - 1; i++) {
            mWidth = i;
            nWidth = i;
            built = survivialBuildPiece(MS_LEFT_MID, stackSize, 3 + 2 * i, 4, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
            built = survivialBuildPiece(MS_RIGHT_MID, stackSize, -2 - 2 * i, 4, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        if (mWidth == tTotalWidth - 2) return survivialBuildPiece(
            MS_END,
            stackSize,
            (2 + tTotalWidth) * 2 - 4,
            4,
            0,
            elementBudget,
            env,
            false,
            true);
        else return survivialBuildPiece(
            MS_END,
            stackSize,
            (-2 - tTotalWidth) * 2 + 4,
            4,
            0,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public IStructureDefinition<MTEMultiSolidifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    protected int mCasing;
    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mWidth = 0;
        mCasingAmount = 0;

        if (checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) {
            while (mWidth < (6)) {
                if (checkPiece(MS_RIGHT_MID, (-2 * (mWidth + 1)) - 2, 4, 0)
                    && checkPiece(MS_LEFT_MID, (2 * (mWidth + 1)) + 3, 4, 0)) {
                    mWidth++;
                } else break;
            }
        } else return false;
        if (!checkPiece(MS_END, (-2 * mWidth) - 4, 4, 0) || !checkPiece(MS_END, (mWidth * 2) + 4, 4, 0)) {
            return false;
        }
        if (glassTier >= VoltageIndex.UMV) return true;
        for (int i = 0; i < this.mEnergyHatches.size(); ++i)
            if (this.mEnergyHatches.get(i).mTier > glassTier) return false;

        return mCasingAmount >= (100 + mWidth * 23);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                CheckRecipeResult check = super.process();
                if (check == CheckRecipeResultRegistry.NO_RECIPE) speedup = 1;
                return check;
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

    private float speedup = 1;
    private int tickcount = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        tickcount++;
        if (tickcount % 10 == 0 && speedup < 3) {
            tickcount = 0;
            speedup += 0.025F;
        }
        return super.onRunningTick(aStack);
    }

    private int tickcounts = 0;

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {

        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (mMaxProgresstime == 0 && speedup > 1) {
                tickcounts++;
                if (tickcounts % 5 == 0) {
                    tickcounts = 0;
                    speedup = (float) Math.max(1, speedup - decay_rate);
                }
            }
        }
    }

    public int getMaxParallelRecipes() {
        return 4 + (mWidth * PARALLELS_PER_WIDTH) * GTUtility.getTier(this.getMaxInputVoltage());
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
}
