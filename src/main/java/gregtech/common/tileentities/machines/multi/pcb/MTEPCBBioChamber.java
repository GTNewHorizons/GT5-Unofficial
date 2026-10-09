package gregtech.common.tileentities.machines.multi.pcb;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GTAuthors.AuthorBlueWeabo;
import static gregtech.api.enums.GTAuthors.Authorguid118;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_GLOW;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
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
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;

public class MTEPCBBioChamber extends MTEPCBUpgradeBase<MTEPCBBioChamber>
    implements ISurvivalConstructable, INEIPreviewModifier, ICasingTextureProvider {

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
            GTUtility.sendChatTrans(env.getActor(), "GT5U.chat.auto_place.done");
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
            .addInfo(EnumChatFormatting.GRAY + "Enables nanites to construct organic circuitry")
            .addInfo(EnumChatFormatting.GRAY + "Unlocks the recipes for Wetware and Bioware circuit boards")
            .addInfo(
                EnumChatFormatting.GRAY + "Place the controller block within "
                    + EnumChatFormatting.RED
                    + MTEPCBFactory.UPGRADE_RANGE
                    + EnumChatFormatting.GRAY
                    + " blocks of the PCB Factory")
            .addInfo(EnumChatFormatting.GRAY + "Left click the PCB Factory controller with a data stick,")
            .addInfo(EnumChatFormatting.GRAY + "then right click this controller to link.")
            .addInfo(EnumChatFormatting.GRAY + "Can connect to many PCB Factories!")
            .beginStructureBlock(5, 7, 12, true)
            .addController("Front bottom center")
            .addCasing("72", "Any Tiered Glass", false)
            .addCasing("67", "Clean Stainless Steel Machine Casing", false)
            .addCasing("40", "Damascus Steel Frame Box", false)
            .addStructureInfo("")
            .addStructureFooter("Does not require maintenance or power")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DataStick.PCB"))
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(AuthorBlueWeabo, Authorguid118);
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPCBBioChamber(this.mName);
    }

    // TODO use a different texture, though idk how that works
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_PURIFICATION_PLANT,
            OVERLAY_FRONT_PURIFICATION_PLANT_GLOW,
            OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE,
            OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1));
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        // Check self
        checkPiece(STRUCTURE_PIECE_BIO_CHAMBER, 2, 6, 0, errors);
    }

}
