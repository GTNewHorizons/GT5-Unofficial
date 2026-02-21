package gregtech.common.tileentities.machines.multi.pcb;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GTValues.AuthorBlueWeabo;
import static gregtech.api.enums.GTValues.Authorguid118;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_GLOW;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEPCBBioChamber extends MTEPCBUpgradeBase<MTEPCBBioChamber>
    implements ISurvivalConstructable, INEIPreviewModifier {

    private static final String STRUCTURE_PIECE_BIO_CHAMBER = "bioUpgrade";
    private static final String[][] structure = new String[][] {
        // spotless:off
            {"     ","     ","F   F","FGGGF","FGGGF","FGGGF","FS~SF"},
            {"     ","     "," SSS ","G   G","G   G","G   G","SSSSS"},
            {"     ","  S  "," SSS ","G   G","G   G","G   G","SSSSS"},
            {"  S  ","     "," SSS ","G   G","G   G","G   G","SSSSS"},
            {"  S  ","     ","F   F","FGGGF","FGGGF","FGGGF","FSSSF"},
            {"  S  ","     ","     ","     ","     ","     ","     "},
            {"  S  ","     ","     ","     ","     ","     ","     "},
            {"  S  ","     ","F   F","FGGGF","FGGGF","FGGGF","FSSSF"},
            {"  S  ","     "," SSS ","G   G","G   G","G   G","SSSSS"},
            {"     ","  S  "," SSS ","G   G","G   G","G   G","SSSSS"},
            {"     ","     "," SSS ","G   G","G   G","G   G","SSSSS"},
            {"     ","     ","F   F","FGGGF","FGGGF","FGGGF","FSSSF"}
            //spotless:on
    };
    private static final IStructureDefinition<MTEPCBBioChamber> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPCBBioChamber>builder()
        .addShape(STRUCTURE_PIECE_BIO_CHAMBER, structure)
        // Damascus steel frame box
        .addElement('F', ofFrame(Materials.DamascusSteel))
        // any glass
        .addElement('G', chainAllGlasses())
        // Clean stainless steel casing
        .addElement('S', ofBlock(GregTechAPI.sBlockCasings4, 1))
        .build();

    public MTEPCBBioChamber(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPCBBioChamber(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_BIO_CHAMBER, stackSize, hintsOnly, 2, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = survivalBuildPiece(STRUCTURE_PIECE_BIO_CHAMBER, stackSize, 2, 6, 0, elementBudget, env, true);
        if (built == -1) {
            GTUtility.sendChatToPlayer(env.getActor(), EnumChatFormatting.GREEN + "Auto placing done!");
            return 0;
        }
        return built;
    }

    @Override
    public IStructureDefinition<MTEPCBBioChamber> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("PCB Factory Upgrade")
            .addInfo(EnumChatFormatting.GRAY + "It enables nanites to construct organic circuitry.")
            .addInfo(EnumChatFormatting.GRAY + "Required for Bioware and Wetware boards.")
            .addInfo(
                EnumChatFormatting.GRAY + "Place the controller block within "
                    + EnumChatFormatting.RED
                    + MTEPCBFactory.UPGRADE_RANGE
                    + EnumChatFormatting.GRAY
                    + " blocks of the PCB Factory")
            .addInfo(EnumChatFormatting.GRAY + "Left click the PCB Factory controller with a data stick,")
            .addInfo(EnumChatFormatting.GRAY + "then right click this controller to link.")
            .addInfo(EnumChatFormatting.GRAY + "Can connect to many PCB Factories!")
            .addController("Front Center")
            .addCasingInfoExactlyColored(
                "Clean Stainless Steel Machine Casing",
                EnumChatFormatting.GRAY,
                68,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Damascus Steel Frame Box",
                EnumChatFormatting.GRAY,
                40,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Any Tiered Glass",
                EnumChatFormatting.GRAY,
                72,
                EnumChatFormatting.GOLD,
                false)
            .addStructureInfo(EnumChatFormatting.GRAY + "Does not require maintenance or power.")
            .toolTipFinisher(AuthorBlueWeabo, Authorguid118);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPCBBioChamber(this.mName);
    }

    // TODO use a different texture, though idk how that works
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PURIFICATION_PLANT)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_PURIFICATION_PLANT_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1)) };
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check self
        return checkPiece(STRUCTURE_PIECE_BIO_CHAMBER, 2, 6, 0);
    }

}
