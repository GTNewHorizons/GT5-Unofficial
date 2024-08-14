package gregtech.common.tileentities.machines.multi.nanochip;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;

public abstract class GT_MetaTileEntity_NanochipAssemblyModuleBase<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> implements ISurvivalConstructable {

    protected static final String STRUCTURE_PIECE_BASE = "base";
    protected static final String[][] base_structure = new String[][] { { "V~V" }, { "VVV" }, { "VVV" } };

    protected static final int BASE_STRUCTURE_OFFSET_X = 1;
    protected static final int BASE_STRUCTURE_OFFSET_Y = 0;
    protected static final int BASE_STRUCTURE_OFFSET_Z = 0;

    private boolean isConnected = false;

    public static <B> StructureDefinition.Builder<B> addBaseStructure(StructureDefinition.Builder<B> structure) {
        return structure.addShape(STRUCTURE_PIECE_BASE, base_structure)
            .addElement('V', ofBlock(GregTech_API.sBlockCasings4, 0));
    }

    /**
     * Create new nanochip assembly module
     *
     * @param aID           ID of this module
     * @param aName         Name of this module
     * @param aNameRegional Localized name of this module
     */
    protected GT_MetaTileEntity_NanochipAssemblyModuleBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_NanochipAssemblyModuleBase(String aName) {
        super(aName);
    }

    // Only checks the base structure piece
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        fixAllIssues();
        return checkPiece(
            STRUCTURE_PIECE_BASE,
            BASE_STRUCTURE_OFFSET_X,
            BASE_STRUCTURE_OFFSET_Y,
            BASE_STRUCTURE_OFFSET_Z);
    }

    public void connect() {
        isConnected = true;
    }

    public void disconnect() {
        isConnected = false;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // Does not get have maintenance issues
        return true;
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }
}
