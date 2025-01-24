package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorSteamIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_STEAMGATE_CONTROLLER;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTESteamgate extends MTEEnhancedMultiBlockBase<MTESteamgate> implements ISurvivalConstructable {

    public MTESteamgate(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESteamgate(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamgate(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == facing) {
            rTexture = new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 0)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_STEAMGATE_CONTROLLER)
                    .extFacing()
                    .glow()
                    .build() };

        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 0)) };
        }
        return rTexture;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 8, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 8, 0, elementBudget, env, false, true);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public IStructureDefinition<MTESteamgate> getStructureDefinition() {
        return StructureDefinition.<MTESteamgate>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                (transpose(
                    new String[][] { { "  AABAA  " }, { " BA   AB " }, { "AA     AA" }, { "A       A" },
                        { "B       B" }, { "A       A" }, { "AA     AA" }, { " BA   AB " }, { "  AA~AA  " } })))
            .addElement('A', ofBlock(GregTechAPI.sBlockCasingsSteam, 0))
            .addElement('B', ofBlock(GregTechAPI.sBlockCasingsSteam, 1))
            .build();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addInfo(EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Impossible machine.")
            .addInfo("Created by: ")
            .addInfo(AuthorSteamIsTheNumber)
            .beginStructureBlock(3, 3, 3, false)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 4, 8, 0);
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
}
