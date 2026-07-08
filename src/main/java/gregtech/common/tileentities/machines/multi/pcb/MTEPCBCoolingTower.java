package gregtech.common.tileentities.machines.multi.pcb;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GTAuthors.AuthorBlueWeabo;
import static gregtech.api.enums.GTAuthors.Authorguid118;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_GLOW;
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
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEPCBCoolingTower extends MTEPCBUpgradeBase<MTEPCBCoolingTower>
    implements ISurvivalConstructable, INEIPreviewModifier, ICasingTextureProvider {

    private static final String STRUCTURE_PIECE_COOLING_TOWER_T1 = "CoolingTowerUpgradeT1";
    private static final String STRUCTURE_PIECE_COOLING_TOWER_T2 = "CoolingTowerUpgradeT2";
    private static final String[][] tier_1 = new String[][] {
        // spotless:off
            {"EKKKE", "E   E", "E   E", "E   E", "E   E", "EOOOE", "E   E", "E   E", "ENNNE", "EG~GE"},
            {"K   K", " KKK ", " NNN ", " KKK ", " KKK ", "OKKKO", " KKK ", " KKK ", "NKKKN", "GGGGG"},
            {"K   K", " K K ", " N N ", " K K ", " K K ", "OK KO", " K K ", " K K ", "NK KN", "GGMGG"},
            {"K   K", " KKK ", " NNN ", " KKK ", " KKK ", "OKKKO", " KKK ", " KKK ", "NKKKN", "GGGGG"},
            {"EKKKE", "E   E", "E   E", "E   E", "E   E", "EOOOE", "E   E", "E   E", "ENNNE", "EGGGE"}
            //spotless:on
    };
    private static final String[][] tier_2 = new String[][] {
        // spotless:off
            {"RGGGR", "R   R", "R   R", "R   R", "R   R", "R   R", "R   R", "R   R", "RNNNR", "RG~GR"},
            {"G   G", " GGG ", " NNN ", " QQQ ", " QQQ ", " QQQ ", " QQQ ", " QQQ ", "NQQQN", "GGGGG"},
            {"G   G", " GTG ", " NTN ", " QTQ ", " QTQ ", " QTQ ", " QTQ ", " QTQ ", "NQTQN", "GGSGG"},
            {"G   G", " GGG ", " NNN ", " QQQ ", " QQQ ", " QQQ ", " QQQ ", " QQQ ", "NQQQN", "GGGGG"},
            {"RGGGR", "R   R", "R   R", "R   R", "R   R", "R   R", "R   R", "R   R", "RNNNR", "RGGGR"}
            //spotless:on
    };

    private static final IStructureDefinition<MTEPCBCoolingTower> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPCBCoolingTower>builder()
        .addShape(STRUCTURE_PIECE_COOLING_TOWER_T1, tier_1)
        .addShape(STRUCTURE_PIECE_COOLING_TOWER_T2, tier_2)
        .addElement(
            'M',
            InputHatch.withAdder(MTEPCBCoolingTower::addCoolantInputToMachineList)
                .newAnyOrCasing(
                    GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12),
                    2,
                    GregTechAPI.sBlockCasings8,
                    12))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 12))
        .addElement('N', ofBlock(GregTechAPI.sBlockCasings2, 15))
        .addElement('K', ofBlock(GregTechAPI.sBlockCasings8, 10))
        .addElement('E', ofFrame(Materials.DamascusSteel))
        .addElement('O', ofBlock(GregTechAPI.sBlockCasings8, 4))
        .addElement(
            'S',
            InputHatch.withAdder(MTEPCBCoolingTower::addCoolantInputToMachineList)
                .newAnyOrCasing(
                    GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12),
                    2,
                    GregTechAPI.sBlockCasings8,
                    12))
        .addElement('R', ofFrame(Materials.Americium))
        .addElement('Q', ofBlock(GregTechAPI.sBlockCasings8, 14))
        .addElement('T', ofBlock(GregTechAPI.sBlockCasings1, 15))

        .build();

    public boolean isTier1 = true;

    protected MTEHatchInput mCoolantInputHatch;

    private final int COOLANT_CONSUMPTION_PER_SEC = 10;

    public MTEPCBCoolingTower(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPCBCoolingTower(String aName) {
        super(aName);
    }

    public boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            mCoolantInputHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (stackSize.stackSize == 1) buildPiece(STRUCTURE_PIECE_COOLING_TOWER_T1, stackSize, hintsOnly, 2, 9, 0);
        else buildPiece(STRUCTURE_PIECE_COOLING_TOWER_T2, stackSize, hintsOnly, 2, 9, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = 0;
        if (stackSize.stackSize == 1)
            built = survivalBuildPiece(STRUCTURE_PIECE_COOLING_TOWER_T1, stackSize, 2, 9, 0, elementBudget, env, true);
        else built = survivalBuildPiece(STRUCTURE_PIECE_COOLING_TOWER_T2, stackSize, 2, 9, 0, elementBudget, env, true);
        if (built == -1) {
            GTUtility.sendChatTrans(env.getActor(), "GT5U.chat.auto_place.done");
            return 0;
        }
        return built;
    }

    @Override
    public IStructureDefinition<MTEPCBCoolingTower> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("PCB Factory Upgrade")
            .addInfo("The Cooling Tower enables overclocking of the PCB Factory")
            .addInfo("Tier 1 gives a normal overclock")
            .addInfo("Tier 2 gives a " + EnumChatFormatting.GOLD + "perfect" + EnumChatFormatting.GRAY + " overclock")
            .addInfo(
                "It requires " + COOLANT_CONSUMPTION_PER_SEC
                    + "L/s of cooling fluid per active and connected PCB Factory")
            .addInfo("Tier 1 Cooling Fluid is: " + EnumChatFormatting.GOLD + "Distilled Water")
            .addInfo("Tier 2 Cooling Fluid is: " + EnumChatFormatting.GOLD + "Super Coolant")
            .addInfo(
                EnumChatFormatting.GRAY + "Place the controller block within "
                    + EnumChatFormatting.RED
                    + MTEPCBFactory.UPGRADE_RANGE
                    + EnumChatFormatting.GRAY
                    + " blocks of the PCB Factory")
            .addInfo(EnumChatFormatting.GRAY + "Left click the PCB Factory controller with a data stick,")
            .addInfo(EnumChatFormatting.GRAY + "then right click this controller to link.")
            .addInfo(EnumChatFormatting.GRAY + "Can connect to many PCB Factories!")
            .beginStructureBlock(5, 5, 10, false)
            .addController("Front bottom center")
            .addInputHatch("1", "Bottom center casing", 1)
            .addStructureInfo("")
            .addStructureInfo(StatCollector.translateToLocal("GT5U.MBTT.Tiers.One"))
            .addCasing("68", "Radiant Naquadah Alloy Casing", false)
            .addCasing("40", "Damascus Steel Frame Box", false)
            .addCasing("20", "Tungstensteel Pipe Casing", false)
            .addCasing("19", "Reinforced Photolithographic Framework Casing", false)
            .addCasing("12", "Extreme Engine Intake Casing", false)
            .addStructureInfo("")
            .addStructureInfo(StatCollector.translateToLocal("GT5U.MBTT.Tiers.Two"))
            .addCasing("48", "Infinity Cooled Casing", false)
            .addCasing("40", "Americium Frame Box", false)
            .addCasing("39", "Reinforced Photolithographic Framework Casing", false)
            .addCasing("20", "Tungstensteel Pipe Casing", false)
            .addCasing("8", "Superconducting Coil Block", false)
            .addStructureInfo("")
            .addStructureFooter("Does not require maintenance or power")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.DataStick.PCB"))
            .addMasterChannel(StatCollector.translateToLocal("channels.gregtech.master.structuretier"))
            .toolTipFinisher(AuthorBlueWeabo, Authorguid118);
        return tt;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        // Check self, last known tier first.
        checkFactories();
        mCoolantInputHatch = null;

        if (checkPiece(
            isTier1 ? STRUCTURE_PIECE_COOLING_TOWER_T1 : STRUCTURE_PIECE_COOLING_TOWER_T2,
            2,
            9,
            0,
            errors)) {
            checkHasCoolantInputHatch(errors);
            return;
        }
        errors.clear();
        mCoolantInputHatch = null;
        if (checkPiece(
            !isTier1 ? STRUCTURE_PIECE_COOLING_TOWER_T1 : STRUCTURE_PIECE_COOLING_TOWER_T2,
            2,
            9,
            0,
            errors)) {
            isTier1 = !isTier1;
            checkHasCoolantInputHatch(errors);
            return;
        }
        errors.clear();
        errors.add(StructureErrorRegistry.UNKNOWN_TIER);
    }

    private void checkHasCoolantInputHatch(List<StructureError> errors) {
        if (mCoolantInputHatch == null) {
            errors.add(StructureErrors.missingHatch(HatchElement.InputHatch));
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPCBCoolingTower(this.mName);
    }

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
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12));
    }
}
