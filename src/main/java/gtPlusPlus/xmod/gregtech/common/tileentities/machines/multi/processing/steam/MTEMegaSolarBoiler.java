package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.SOLAR_CELL_TOP;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

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
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings1;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEMegaSolarBoiler extends MTEBetterSteamMultiBase<MTEMegaSolarBoiler> implements ISurvivalConstructable {

    public MTEMegaSolarBoiler(String aName) {
        super(aName);
    }

    public MTEMegaSolarBoiler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTEMegaSolarBoiler(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Solar Boiler";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    // spotless:off

    private static final String[][] structure = transpose(
        new String[][]{
            {" BBBBB         BBBBB ","BAAAAAB       BAAAAAB","BAAAAAB       BAAAAAB","BAAAAAB       BAAAAAB","BAAAAAB       BAAAAAB","BAAAAAB       BAAAAAB"," BBBBB         BBBBB "},
            {"  DDD           DDD  "," FFFFF         FFFFF ","DFFFFFD       DFFFFFD","DFFFFFD       DFFFFFD","DFFFFFD       DFFFFFD"," FFFFF         FFFFF ","  DDD           DDD  "},
            {"   B   CCCCCCC   B   "," CCCCC CAAAAAC CCCCC "," CBBBC CAAAAAC CBBBC ","BCBBBCBCAAAAACBCBBBCB"," CBBBC CAAAAAC CBBBC "," CCCCC CAAAAAC CCCCC ","   B   CCCCCCC   B   "},
            {"       C     C       ","       CFFFFFC       ","   EE  CFFFFFC  EE   ","  EEEEECFFFFFCEEEE   ","   EE  CFFFFFC  EE   ","       CFFFFFC       ","       CCCCCCC       "},
            {"       C     C       ","       CGG~GGC       ","    EEECBBBBBCEEE    ","   EEEECBBBBBCEEEE   ","    EEECBBBBBCEEE    ","       CBBBBBC       ","       CGGGGGC       "}
        });

    // spotless:on

    public IStructureDefinition<MTEMegaSolarBoiler> getStructureDefinition() {
        return StructureDefinition.<MTEMegaSolarBoiler>builder()
            .addShape(STRUCTURE_PIECE_MAIN, structure)
            .addElement('A', chainAllGlasses())
            .addElement('B', ofBlock(GregTechAPI.sBlockCasings1, 10))
            .addElement(
                'G',
                ofChain(
                    buildHatchAdder(MTEMegaSolarBoiler.class)
                        .atLeast(SteamHatchElement.InputBus_Steam, InputHatch, OutputHatch)
                        .casingIndex(10)
                        .dot(1)
                        .buildAndChain(),
                    ofBlock(GregTechAPI.sBlockCasings1, 10)))
            .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 0))
            .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 12))
            .addElement('E', ofBlock(GregTechAPI.sBlockCasings2, 13))
            .addElement('F', ofBlock(GregTechAPI.sBlockCasingsSteam, 9))
            .build();
    }

    private static final int HORIZONTAL_OFF_SET = 10;
    private static final int VERTICAL_OFF_SET = 4;
    private static final int DEPTH_OFF_SET = 1;

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
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 9)),
                    TextureFactory.builder()
                        .addIcon(SOLAR_CELL_TOP)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(SOLAR_CELL_TOP)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 9)),
                    TextureFactory.builder()
                        .addIcon(SOLAR_CELL_TOP)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(SOLAR_CELL_TOP)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 9)) };
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
            STRUCTURE_PIECE_MAIN,
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

    private String state;

    public void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.runMachine(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isAllowedToWork()) {
            if (aBaseMetaTileEntity.getWorld()
                .isDaytime() || !depleteInputReal(Materials.Water.getFluid(30), true)) {
                addOutput(FluidUtils.getSteam(4800));
                depleteInputReal(Materials.Water.getFluid(30));
                state = "Boiling! :D";
            } else {
                state = "Idle :(";
            }

        }

    }

    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements.widget(
            new TextWidget().setStringSupplier(() -> EnumChatFormatting.GREEN + state)
                .setTextAlignment((Alignment.CenterLeft)))
            .widget(new FakeSyncWidget.StringSyncer(() -> state, val -> state = val));
    }

    public boolean depleteInputReal(FluidStack aLiquid) {
        return depleteInputReal(aLiquid, false);
    }

    public boolean depleteInputReal(FluidStack aLiquid, boolean simulate) {
        if (aLiquid == null) return false;
        for (MTEHatchInput tHatch : validMTEList(mInputHatches)) {
            setHatchRecipeMap(tHatch);
            FluidStack tLiquid = tHatch.drain(ForgeDirection.UNKNOWN, aLiquid, false);
            if (tLiquid != null && tLiquid.amount >= aLiquid.amount) {
                if (simulate) {
                    return true;
                }
                tLiquid = tHatch.drain(ForgeDirection.UNKNOWN, aLiquid, true);
                return tLiquid != null && tLiquid.amount >= aLiquid.amount;
            }
        }
        return false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Solar Boiler")
            .addInfo("Maybe an Eye of Harmony could provide enough silver for this monstrosity")
            .addInfo("Produces 96.000 L/s of Steam")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "More steam/s than unplayed games in your steam library")
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
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.IC2_MACHINES_MACERATOR_OP;
    }

}
