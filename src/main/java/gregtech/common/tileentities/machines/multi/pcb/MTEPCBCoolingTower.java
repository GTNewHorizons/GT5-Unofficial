package gregtech.common.tileentities.machines.multi.pcb;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.GTValues.AuthorBlueWeabo;
import static gregtech.api.enums.GTValues.Authorguid118;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_PURIFICATION_PLANT_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
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
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEPCBCoolingTower extends MTEPCBUpgradeBase<MTEPCBCoolingTower>
    implements ISurvivalConstructable, INEIPreviewModifier {

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
            buildHatchAdder(MTEPCBCoolingTower.class).hatchClass(MTEHatchInput.class)
                .adder(MTEPCBCoolingTower::addCoolantInputToMachineList)
                .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12))
                .hint(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 12))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 12))
        .addElement('N', ofBlock(GregTechAPI.sBlockCasings2, 15))
        .addElement('K', ofBlock(GregTechAPI.sBlockCasings8, 10))
        .addElement('E', ofFrame(Materials.DamascusSteel))
        .addElement('O', ofBlock(GregTechAPI.sBlockCasings8, 4))
        .addElement(
            'S',
            buildHatchAdder(MTEPCBCoolingTower.class).hatchClass(MTEHatchInput.class)
                .adder(MTEPCBCoolingTower::addCoolantInputToMachineList)
                .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12))
                .hint(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 12))
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
            GTUtility.sendChatToPlayer(env.getActor(), EnumChatFormatting.GREEN + "Auto placing done!");
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
            .addStructureInfo("Tier 1")
            .addController("Front Center")
            .addCasingInfoExactlyColored(
                "Reinforced Photolithographic Framework Casing",
                EnumChatFormatting.GRAY,
                19,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Tungstensteel Pipe Casing",
                EnumChatFormatting.GRAY,
                20,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Damascus Steel Frame Box",
                EnumChatFormatting.GRAY,
                40,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Radiant Naquadah Alloy Casing",
                EnumChatFormatting.GRAY,
                68,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Extreme Engine Intake Casing",
                EnumChatFormatting.GRAY,
                12,
                EnumChatFormatting.GOLD,
                false)
            .addInputHatch("Coolant Hatch")
            .addStructureInfo("Tier 2")
            .addController("Front Center")
            .addCasingInfoExactlyColored(
                "Reinforced Photolithographic Framework Casing",
                EnumChatFormatting.GRAY,
                39,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Americium Steel Frame Box",
                EnumChatFormatting.GRAY,
                40,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Tungstensteel Pipe Casing",
                EnumChatFormatting.GRAY,
                20,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Infinity Cooled Casing",
                EnumChatFormatting.GRAY,
                48,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Superconducting Coil Block",
                EnumChatFormatting.GRAY,
                8,
                EnumChatFormatting.GOLD,
                false)
            .addInputHatch("Coolant Hatch")
            .addStructureInfoSeparator()
            .addStructureInfo(EnumChatFormatting.GRAY + "Does not require maintenance or power.")
            .toolTipFinisher(AuthorBlueWeabo, Authorguid118);
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // Check self, last known tier first.
        checkFactories();

        if (checkPiece(isTier1 ? STRUCTURE_PIECE_COOLING_TOWER_T1 : STRUCTURE_PIECE_COOLING_TOWER_T2, 2, 9, 0))
            return true;
        else {
            if (checkPiece(!isTier1 ? STRUCTURE_PIECE_COOLING_TOWER_T1 : STRUCTURE_PIECE_COOLING_TOWER_T2, 2, 9, 0)) {
                isTier1 = false;
                return true;
            }
        }
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPCBCoolingTower(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12)),
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
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12)),
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
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12)) };
    }
}
