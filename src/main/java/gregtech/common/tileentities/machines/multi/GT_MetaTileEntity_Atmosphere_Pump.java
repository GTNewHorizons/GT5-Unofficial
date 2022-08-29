package gregtech.common.tileentities.machines.multi;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.apache.commons.lang3.tuple.Pair;

public class GT_MetaTileEntity_Atmosphere_Pump extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_Atmosphere_Pump> implements ISurvivalConstructable {


    private int AirpipeTier = -1;
    @SuppressWarnings("SpellCheckingInspection")
    private static final String [][] structure_string = new String[][]{{
        "       ",
        "       ",
        "       ",
        "       ",
        " E   E ",
        "HHHHHHH"
    },{
        "       ",
        "  HHH  ",
        " HE EH ",
        " HG GH ",
        "EHE~EHE",
        "HHHHHHH"
    },{
        "  ---  ", //ici
        " HHHHH ",
        " H   H ",
        " G   G ",
        " H   H ",
        "HHHHHHH"
    },{
        "  ---  ", //ici
        " HHHHH ",
        "   F   ",
        "   F   ",
        "   F   ",
        "HHHFHHH"
    },{
        "  ---  ", //ici
        "EHHHHHE",
        "       ",
        "       ",
        "       ",
        "HHHFHHH"
    },{
        "  ---  ", //ici
        "EHHHHHE",
        "       ",
        "       ",
        "       ",
        "HHHFHHH"
    },{
        "  ---  ", //ici
        " HHHHH ",
        "   F   ",
        "   F   ",
        "   F   ",
        "HHHFHHH"
    },{
        "  ---  ", //ici
        " HHHHH ",
        " H   H ",
        " G   G ",
        " H   H ",
        "HHHFHHH"
    },{
        "       ",
        "  HHH  ",
        " HE EH ",
        " HG GH ",
        "EHECEHE",
        "HHHFHHH"
    }, {
        "       ",
        "       ",
        "       ",
        "       ",
        " E   E ",
        "HHHHHHH"
    }};

    protected static final int WSTEN_TURBINE_CASING = 12;

    protected static final String STRUCTURE_PIECE_MAIN = "main";

    private static final IStructureDefinition<GT_MetaTileEntity_Atmosphere_Pump> STRUCTURE_DEFINITION =
        StructureDefinition.<GT_MetaTileEntity_Atmosphere_Pump>builder()
            .addShape(STRUCTURE_PIECE_MAIN, structure_string)
            .addElement('E',
                buildHatchAdder(GT_MetaTileEntity_Atmosphere_Pump.class)
                    .atLeast(
                        Energy,
                        Maintenance)
                    .casingIndex(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, WSTEN_TURBINE_CASING))
                    .dot(2)
                    .buildAndChain(GregTech_API.sBlockCasings4, WSTEN_TURBINE_CASING))
            .addElement('C', OutputHatch.newAny(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, WSTEN_TURBINE_CASING), 3))
            .addElement('H', ofBlock(GregTech_API.sBlockCasings4, WSTEN_TURBINE_CASING))
            .addElement('G', ofBlockUnlocalizedName("IC2", "blockFenceIron", 0, true))
            .addElement('F', ofBlocksTiered(
                (block, meta) -> block == GregTech_API.sBlockCasings8 ? meta : -1,
                ImmutableList.of(
                    Pair.of(GregTech_API.sBlockCasings8, 10),
                    Pair.of(GregTech_API.sBlockCasings8, 11),
                    Pair.of(GregTech_API.sBlockCasings8, 12),
                    Pair.of(GregTech_API.sBlockCasings8, 13),
                    Pair.of(GregTech_API.sBlockCasings8, 14)
                ),
                0,
                (t, meta) -> t.AirpipeTier = meta,
                t -> t.AirpipeTier
            ))
            .build();

    public GT_MetaTileEntity_Atmosphere_Pump(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_Atmosphere_Pump(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Atmosphere_Pump(mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Atmosphere Pump")
            .addInfo(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.BOLD.toString() + "Author: " + EnumChatFormatting.RESET.toString() + EnumChatFormatting.DARK_PURPLE + "POPlol333")
            .toolTipFinisher("Gregtech");

        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {

        mMaxProgresstime = 80;
        int airQuantity = 1000;


        if(!mOutputHatches.isEmpty()) {
            final int current = mOutputHatches.get(0).getFluidAmount();
            mOutputHatches.get(0).setFillableStack(Materials.Air.getGas(current+airQuantity));
        } else {
            mOutputHatches.get(0).setFillableStack(Materials.Air.getGas(airQuantity));
        }

        return true;
    }



    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        AirpipeTier = 0;

        //Check Structure
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 1)) return false;

        //Min 1 and max 2 energy hatch
        if (mEnergyHatches.size() == 0 || mEnergyHatches.size() > 2) return false;

        //Mandatory 1 output hatch and 1 maintenance hatch
        if (mMaintenanceHatches.size() != 1) return false;
        if (mOutputHatches.size() != 1) return false;

        //hatch are from the same tier
        byte tier_of_hatch = mEnergyHatches.get(0).mTier;
        for (GT_MetaTileEntity_Hatch_Energy energyHatch : mEnergyHatches) {
            if (energyHatch.mTier != tier_of_hatch) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
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
    public IStructureDefinition<GT_MetaTileEntity_Atmosphere_Pump> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        return 0;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly,3 , 4, 1);
    }


    @Override
    public ITexture[] getTexture(
        IGregTechTileEntity aBaseMetaTileEntity,
        byte aSide,
        byte aFacing,
        byte aColorIndex,
        boolean aActive,
        boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[]{
                    BlockIcons.casingTexturePages[0][GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, WSTEN_TURBINE_CASING)],
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ATMOSPHERE_PUMP_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ATMOSPHERE_PUMP_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build()
                };
            return new ITexture[] {
                BlockIcons.casingTexturePages[0][GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, WSTEN_TURBINE_CASING)],
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ATMOSPHERE_PUMP)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ATMOSPHERE_PUMP_GLOW)
                    .extFacing()
                    .glow()
                    .build()
            };
        }
        return new ITexture[] {Textures.BlockIcons.casingTexturePages[0][GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, WSTEN_TURBINE_CASING)]};
    }

    @Override //here if anyone want to actually use it
    public void onRandomDisplayTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onRandomDisplayTick(aBaseMetaTileEntity);
    }
}
