package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.GregTechAPI.sBlockCasingsSteam;
import static gregtech.api.enums.GTValues.AuthorSteamIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BBF_INACTIVE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasingsSteam;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEHatchCustomFluidBase;

public class MTESteamBlastFurnace extends MTEBetterSteamMultiBase<MTESteamBlastFurnace>
    implements ISurvivalConstructable {

    public MTESteamBlastFurnace(String aName) {
        super(aName);
    }

    public MTESteamBlastFurnace(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return super.onRunningTick(aStack);
    }

    private void doProgress() {
        for (MTEHatchCustomFluidBase tHatch : validMTEList(mSteamInputFluids)) {
            FluidStack steamStack = tHatch.getFillableStack();
            if (steamStack != null) {
                int drain = Math.min(10, steamStack.amount);
                tHatch.drain(drain, true);
                if (steamStack.isFluidEqual(steam)) {
                    mProgresstime += drain;
                } else if (steamStack.isFluidEqual(shSteam)) {
                    mProgresstime += drain * 10;
                } else if (steamStack.getFluid()
                    .getName()
                    .equals("supercriticalsteam")) {
                        mProgresstime += drain * 100;
                    }
            }
        }
    }

    FluidStack steam = FluidUtils.getSteam(10);
    FluidStack shSteam = FluidUtils.getSuperHeatedSteam(10);

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mMaxProgresstime > 0) {
            if (onRunningTick(mInventory[1])) {
                markDirty();
                doProgress();
                if (mMaxProgresstime > 0 && mProgresstime >= mMaxProgresstime) {
                    if (mOutputItems != null) {
                        for (ItemStack tStack : mOutputItems) {
                            if (tStack != null) {
                                addOutput(tStack);
                            }
                        }
                        mOutputItems = null;
                    }
                    if (mOutputFluids != null) {
                        addFluidOutputs(mOutputFluids);
                        mOutputFluids = null;
                    }
                    mEfficiency = Math.max(
                        0,
                        Math.min(
                            mEfficiency + mEfficiencyIncrease,
                            getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
                    mOutputItems = null;
                    mProgresstime = 0;
                    mMaxProgresstime = 0;
                    mEfficiencyIncrease = 0;
                    mLastWorkingTick = mTotalRunTime;
                    if (aBaseMetaTileEntity.isAllowedToWork()) {
                        checkRecipe();
                    }
                }
            }
        } else {
            // Check if the machine is enabled in the first place!
            if (aBaseMetaTileEntity.isAllowedToWork()) {

                if (shouldCheckRecipeThisTick(aTick) || aBaseMetaTileEntity.hasWorkJustBeenEnabled()
                    || aBaseMetaTileEntity.hasInventoryBeenModified()) {
                    if (checkRecipe()) {
                        markDirty();
                    }
                }
                if (mMaxProgresstime <= 0) mEfficiency = Math.max(0, mEfficiency - 1000);
            }
        }
    }

    // Copying stuff out of MultiBlockBase because it's private, boringoid code
    // Also cut out everything to do with ME buses because those aren't real
    private final int randomTickOffset = (int) (Math.random() * 100 + 1);

    private boolean shouldCheckRecipeThisTick(long aTick) {
        // Perform more frequent recipe change after the machine just shuts down.
        long timeElapsed = mTotalRunTime - mLastWorkingTick;

        if (timeElapsed >= 100) return (mTotalRunTime + randomTickOffset) % 100 == 0;
        // Batch mode should be a lot less aggressive at recipe checking
        if (!isBatchModeEnabled()) {
            return timeElapsed == 5 || timeElapsed == 12
                || timeElapsed == 20
                || timeElapsed == 30
                || timeElapsed == 40
                || timeElapsed == 55
                || timeElapsed == 70
                || timeElapsed == 85;
        }
        return false;
    }

    @Override
    public String getMachineType() {
        return "";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Blast Furnace")
            .addInfo("Faster than Bricked Blast Furnace and can be automated")
            .addInfo(
                "Consumes up to " + EnumChatFormatting.WHITE
                    + "10 L/t"
                    + EnumChatFormatting.GRAY
                    + " of steam (any variety)")
            .addInfo("Recipe time is converted to total steam consumption")
            .addInfo("Steam: " + EnumChatFormatting.WHITE + "1L = 1s")
            .addInfo("Superheated Steam: " + EnumChatFormatting.WHITE + "1L = 10s")
            .addInfo("Supercritical Steam: " + EnumChatFormatting.WHITE + "1L = 100s")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "Blast furnaces are far too slow to create all of the steel you require.")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "Using breel and stronze to efficiently channel steam should help!")
            .addInfo("Author: " + AuthorSteamIsTheNumber)
            .toolTipFinisher();
        return tt;
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 12, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 12, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.primitiveBlastRecipes;
    }

    @Override
    public IStructureDefinition<MTESteamBlastFurnace> getStructureDefinition() {
        return StructureDefinition.<MTESteamBlastFurnace>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                (new String[][] {
                    { "          AAAAA          ", "          AAAAA          ", "          AAAAA          ",
                        "          AAAAA          ", "          AA~AA          ", "          AAAAA          ",
                        "          AAAAA          ", "          AAAAA          ", "          AAAAA          " },
                    { "         AAAAAAA         ", "         A     A         ", "         A     A         ",
                        "         A     A         ", "         A     A         ", "         A     A         ",
                        "         A     A         ", "         A     A         ", "         AAAAAAA         " },
                    { "        AAAAAAAAA        ", "        A       A        ", "        A       A        ",
                        "     A  A       A  A     ", " A  CCC A       A CCC  A ", "CCC CCC A       A CCC CCC",
                        "CCC CCC A       A CCC CCC", "CCC CCC A       A CCC CCC", "CCC CCC AAAAAAAAA CCC CCC" },
                    { "        AAAAAAAAA        ", "  BBBBBBB       BBBBBBB  ", " BB  B  A       A  B  BB ",
                        " B  ABA A       A AB   B ", "ABA CBC A       A CBC ABA", "CBC CBC A       A CBC CBC",
                        "CBC CBC A       A CBC CBC", "CBC CBC A       A CBC CBC", "CCC CCC AAAAAAAAA CCC CCC" },
                    { "        AAAAAAAAA        ", "        A       A        ", "        A       A        ",
                        "     A  A       A  AA    ", " A  CCC A       A CCC  A ", "CCC CCC A       A CCC CCC",
                        "CCC CCC A       A CCC CCC", "CCC CCC A       A CCC CCC", "CCC CCC AAAAAAAAA CCC CCC" },
                    { "         AAAAAAA         ", "         A     A         ", "         A     A         ",
                        "         A     A         ", "         A     A         ", "         A     A         ",
                        "         A     A         ", "         A     A         ", "         AAAAAAA         " },
                    { "          AAAAA          ", "          AAAAA          ", "          AAAAA          ",
                        "          AAAAA          ", "          AAAAA          ", "          AAAAA          ",
                        "          AAAAA          ", "          AAAAA          ", "          AAAAA          " } }))
            .addElement(
                'A',
                ofChain(
                    buildHatchAdder(MTESteamBlastFurnace.class).adder(MTEBetterSteamMultiBase::addToMachineList)
                        .hatchIds(31040, 15511)
                        .shouldReject(t -> !t.mSteamInputFluids.isEmpty())
                        .casingIndex(((BlockCasingsSteam) GregTechAPI.sBlockCasingsSteam).getTextureIndex(8))
                        .dot(1)
                        .build(),
                    buildHatchAdder(MTESteamBlastFurnace.class)
                        .atLeast(SteamHatchElement.InputBus_Steam, SteamHatchElement.OutputBus_Steam)
                        .casingIndex(((BlockCasingsSteam) GregTechAPI.sBlockCasingsSteam).getTextureIndex(8))
                        .dot(1)
                        .buildAndChain(),
                    ofBlock(sBlockCasingsSteam, 8)))
            .addElement('B', ofBlock(sBlockCasingsSteam, 7))
            .addElement('C', ofBlock(GregTechAPI.sBlockCasings4, 15))
            .build();
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return new String[0];
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 12, 4, 0);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamBlastFurnace(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(sBlockCasingsSteam, 8)),
                    TextureFactory.builder()
                        .addIcon(MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(sBlockCasingsSteam, 8)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_BBF_INACTIVE)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(sBlockCasingsSteam, 8)) };
        }
        return rTexture;
    }
}
