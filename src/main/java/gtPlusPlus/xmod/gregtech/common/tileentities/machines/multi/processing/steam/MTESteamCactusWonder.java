package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_EXTRACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_EXTRACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_EXTRACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_STEAM_EXTRACTOR_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.BlockCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.BlockCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.CompressedCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.DoubleCompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.DoubleCompressedCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuadrupleCompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuadrupleCompressedCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuintupleCompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.QuintupleCompressedCactusCoke;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TripleCompressedCactusCharcoal;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.TripleCompressedCactusCoke;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings1;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTESteamCactusWonder extends MTEBetterSteamMultiBase<MTESteamCactusWonder>
    implements ISurvivalConstructable {

    public MTESteamCactusWonder(String aName) {
        super(aName);
    }

    public MTESteamCactusWonder(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTESteamCactusWonder(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Temple of Cacti Blessings";
    }

    private int currentSteam;
    private ItemStack currentOffer;
    private long fueledAmount = 0;
    private static ItemStack[] possibleInputs = { CactusCharcoal.get(1), BlockCactusCharcoal.get(1),
        CompressedCactusCharcoal.get(1), DoubleCompressedCactusCharcoal.get(1), TripleCompressedCactusCharcoal.get(1),
        QuadrupleCompressedCactusCharcoal.get(1), QuintupleCompressedCactusCharcoal.get(1), CactusCoke.get(1),
        BlockCactusCoke.get(1), CompressedCactusCoke.get(1), DoubleCompressedCactusCoke.get(1),
        TripleCompressedCactusCoke.get(1), QuadrupleCompressedCactusCoke.get(1), QuintupleCompressedCactusCoke.get(1) };
    private static long[] totalValue = { 8_000l, 90_000l, 1_012_500l, 11_390_625l, 128_144_531l, 1_441_625_977l,
        16_218_292_236l, 16_000l, 180_000l, 2_025_000l, 22_781_250l, 256_289_063l, 2_883_251_953l, 32_436_584_473l };
    private static int[] steamType = { 1, 1, 1, 2, 2, 3, 3, 1, 1, 1, 2, 2, 3, 3 };

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "nei";

    private static final String[][] structure = transpose(
        new String[][] {
            { "         ", "         ", "   CCC   ", "  CCCCC  ", "  CCCCC  ", "  CCCCC  ", "   CCC   ", "         ",
                "         " },
            { "         ", "  E   E  ", " E AAA E ", "  A   A  ", "  A   A  ", "  A   A  ", " E AAA E ", "  E   E  ",
                "         " },
            { "         ", "  E   E  ", " E AAA E ", "  ABBBA  ", "  ABBBA  ", "  ABBBA  ", " E AAA E ", "  E   E  ",
                "         " },
            { "         ", "  E   E  ", " E AAA E ", "  A   A  ", "  A   A  ", "  A   A  ", " E AAA E ", "  E   E  ",
                "         " },
            { "         ", "  E   E  ", " E CCC E ", "  C   C  ", "  C   C  ", "  C   C  ", " E CCC E ", "  E   E  ",
                "         " },
            { " DDD DDD ", "DDFDDDFDD", "DFDCCCDFD", "DDC   CDD", " DC   CD ", "DDC   CDD", "DFDCCCDFD", "DDFDDDFDD",
                " DDD DDD " },
            { "         ", "  E   E  ", " E AAA E ", "  A   A  ", "  A   A  ", "  A   A  ", " E AAA E ", "  E   E  ",
                "         " },
            { "         ", "  E   E  ", " E AAA E ", "  ABBBA  ", "  ABBBA  ", "  ABBBA  ", " E AAA E ", "  E   E  ",
                "         " },
            { "         ", "  E   E  ", " E A~A E ", "  A   A  ", "  A   A  ", "  A   A  ", " E AAA E ", "  E   E  ",
                "         " },
            { "         ", "  E   E  ", " E CCC E ", "  CCCCC  ", "  CCCCC  ", "  CCCCC  ", " E CCC E ", "  E   E  ",
                "         " },
            { "  CCCCC  ", " CFCCCFC ", "CFCCCCCFC", "CCCCCCCCC", "CCCCCCCCC", "CCCCCCCCC", "CFCCCCCFC", " CFCCCFC ",
                "  CCCCC  " } });

    public IStructureDefinition<MTESteamCactusWonder> getStructureDefinition() {
        return StructureDefinition.<MTESteamCactusWonder>builder()
            .addShape(STRUCTURE_PIECE_MAIN, structure)
            .addShape(
                STRUCTURE_PIECE_MAIN_SURVIVAL,
                Arrays.stream(structure)
                    .map(
                        sa -> Arrays.stream(sa)
                            .map(s -> s.replaceAll("E", " "))
                            .toArray(String[]::new))
                    .toArray(String[][]::new))
            .addElement('A', chainAllGlasses())
            .addElement('B', ofBlock(GregTechAPI.sBlockCasings2, 12))
            .addElement(
                'C',
                ofChain(
                    buildHatchAdder(MTESteamCactusWonder.class).atLeast(SteamHatchElement.InputBus_Steam, OutputHatch)
                        .casingIndex(10)
                        .dot(1)
                        .buildAndChain(),
                    ofBlock(GregTechAPI.sBlockCasings3, 13)))
            .addElement('D', ofFrame(Materials.Breel))
            .addElement('E', ofBlock(Blocks.cactus, 0))
            .addElement('F', ofBlock(Blocks.sand, 0))
            .build();
    }

    private static final int HORIZONTAL_OFF_SET = 4;
    private static final int VERTICAL_OFF_SET = 8;
    private static final int DEPTH_OFF_SET = 2;

    private int mCounCasing = 0;

    private void onCasingAdded() {
        mCounCasing++;
    }

    protected void updateHatchTexture() {
        for (MTEHatch h : mSteamInputs) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mSteamOutputs) h.updateTexture(getCasingTextureID());
        for (MTEHatch h : mSteamInputFluids) h.updateTexture(getCasingTextureID());
    }

    private int getCasingTextureID() {
        return ((BlockCasings1) GregTechAPI.sBlockCasings1).getTextureIndex(10);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        ITexture[] rTexture;
        if (side == facing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_STEAM_EXTRACTOR_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_STEAM_EXTRACTOR_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_STEAM_EXTRACTOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_STEAM_EXTRACTOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings1, 10)) };
        }
        return rTexture;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece(
            STRUCTURE_PIECE_MAIN_SURVIVAL,
            stackSize,
            HORIZONTAL_OFF_SET,
            VERTICAL_OFF_SET,
            DEPTH_OFF_SET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
        return true;
    }

    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isAllowedToWork()) {
            if (aTick % 20 == 0) {
                addFuel();
            }
            outputSteam();
        }

    }

    private void addFuel() {
        ArrayList<ItemStack> storedInputs = getStoredInputs();
        for (ItemStack stack : storedInputs) {
            for (int i = 0; i < 14; i++) {
                if (stack.isItemEqual(possibleInputs[i])) {
                    if (currentOffer == null) {
                        currentOffer = stack;
                        fueledAmount += totalValue[i] * stack.stackSize;
                        currentSteam = steamType[i];
                        this.depleteInput(stack);
                    } else if (stack.isItemEqual(currentOffer)) {
                        fueledAmount += totalValue[i] * stack.stackSize;
                        this.depleteInput(stack);
                    }
                }
            }

        }
    }

    private void outputSteam() {
        if (fueledAmount > 0) {
            if (currentSteam == 1) {
                addOutput(FluidUtils.getSteam((int) Math.min(3200, fueledAmount)));
                fueledAmount -= (int) Math.min(3200, fueledAmount);
            } else if (currentSteam == 2) {
                addOutput(FluidUtils.getSuperHeatedSteam((int) Math.min(6400, fueledAmount)));
                fueledAmount -= (int) Math.min(6400, fueledAmount);
            } else if (currentSteam == 3) {
                addOutput(FluidRegistry.getFluidStack("supercriticalsteam", (int) Math.min(25600, fueledAmount)));
                fueledAmount -= (int) Math.min(25600, fueledAmount);
            }

            if (fueledAmount <= 0) {
                fueledAmount = 0;
                currentOffer = null;
            }
        }
    }

    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> EnumChatFormatting.WHITE + "Offer Value: "
                            + EnumChatFormatting.YELLOW
                            + numberFormat.format(fueledAmount))
                    .setTextAlignment((Alignment.CenterLeft)))
            .widget(new FakeSyncWidget.LongSyncer(() -> fueledAmount, val -> fueledAmount = val));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.cactusWonderFakeRecipes;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Temple of Cacti Blessing")
            .addInfo("Burns Cactus Coke and Charcoal for increasingly efficient amounts of steam.")
            .addInfo("Every second the cactus wonder will consume all offers stored")
            .addInfo("The god of cacti will save their value and return it as steam blessings to her faithful zealots.")
            .addInfo("Can only take one type of offer at once.")
            .addInfo("Needs Fully Grown Cacti on the Sand Blocks to form")
            .addInfo(EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Cactus")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("fuel", fueledAmount);
        aNBT.setInteger("steam", currentSteam);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        fueledAmount = aNBT.getLong("fuel");
        currentSteam = aNBT.getInteger("steam");
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.IC2_MACHINES_MACERATOR_OP;
    }

}
