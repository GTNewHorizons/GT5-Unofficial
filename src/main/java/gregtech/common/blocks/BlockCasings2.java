package gregtech.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.render.TextureFactory;
import gregtech.common.misc.GTStructureChannels;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings2 extends BlockCasingsAbstract {

    public BlockCasings2() {
        super(ItemCasings.class, "gt.blockcasings2", MaterialCasings.INSTANCE, 16);

        // Special handler for Pyrolyse Oven Casing on hatches...
        Textures.BlockIcons.casingTexturePages[0][22] = TextureFactory.of(
            Block.getBlockFromItem(
                ItemList.Casing_ULV.get(1)
                    .getItem()),
            0,
            ForgeDirection.UNKNOWN,
            Dyes.MACHINE_METAL.getRGBA());

        register(0, ItemList.Casing_SolidSteel, "Solid Steel Machine Casing");
        register(1, ItemList.Casing_FrostProof, "Frost Proof Machine Casing");
        register(2, ItemList.Casing_Gearbox_Bronze, "Bronze Gear Box Casing");
        register(3, ItemList.Casing_Gearbox_Steel, "Steel Gear Box Casing");
        register(4, ItemList.Casing_Gearbox_Titanium, "Titanium Gear Box Casing");
        register(5, ItemList.Casing_Gearbox_TungstenSteel, "Assembly Line Casing");
        register(6, ItemList.Casing_Processor, "Processor Machine Casing");
        register(7, ItemList.Casing_DataDrive, "Data Drive Machine Casing");
        register(8, ItemList.Casing_ContainmentField, "Containment Field Machine Casing", BLAST_PROOF);
        register(9, ItemList.Casing_Assembler, "Assembler Machine Casing");
        register(10, ItemList.Casing_Pump, "Pump Machine Casing");
        register(11, ItemList.Casing_Motor, "Motor Machine Casing");
        register(12, ItemList.Casing_Pipe_Bronze, "Bronze Pipe Casing");
        register(13, ItemList.Casing_Pipe_Steel, "Steel Pipe Casing");
        register(14, ItemList.Casing_Pipe_Titanium, "Titanium Pipe Casing");
        register(15, ItemList.Casing_Pipe_TungstenSteel, "Tungstensteel Pipe Casing");

        for (int i = 0; i < 4; i++) {
            GTStructureChannels.PIPE_CASING.registerAsIndicator(new ItemStack(this, 1, i + 12), i + 1);
        }
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return aMeta == 6 ? ((1 << 7) + 96) : aMeta + 16;
    }

    @Override
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 1 -> Textures.BlockIcons.MACHINE_CASING_FROST_PROOF.getIcon();
            case 2 -> Textures.BlockIcons.MACHINE_CASING_GEARBOX_BRONZE.getIcon();
            case 3 -> Textures.BlockIcons.MACHINE_CASING_GEARBOX_STEEL.getIcon();
            case 4 -> Textures.BlockIcons.MACHINE_CASING_GEARBOX_TITANIUM.getIcon();
            case 5 -> Textures.BlockIcons.MACHINE_CASING_GEARBOX_TUNGSTENSTEEL.getIcon();
            case 6 -> Textures.BlockIcons.MACHINE_CASING_PROCESSOR.getIcon();
            case 7 -> Textures.BlockIcons.MACHINE_CASING_DATA_DRIVE.getIcon();
            case 8 -> Textures.BlockIcons.MACHINE_CASING_CONTAINMENT_FIELD.getIcon();
            case 9 -> Textures.BlockIcons.MACHINE_CASING_ASSEMBLER.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_PUMP.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_MOTOR.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_PIPE_BRONZE.getIcon();
            case 13 -> Textures.BlockIcons.MACHINE_CASING_PIPE_STEEL.getIcon();
            case 14 -> Textures.BlockIcons.MACHINE_CASING_PIPE_TITANIUM.getIcon();
            case 15 -> Textures.BlockIcons.MACHINE_CASING_PIPE_TUNGSTENSTEEL.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL.getIcon();
        };
    }

    @Override
    public float getExplosionResistance(Entity aTNT, World aWorld, int aX, int aY, int aZ, double eX, double eY,
        double eZ) {
        if (aWorld.getBlockMetadata(aX, aY, aZ) == 8) {
            return Blocks.bedrock.getExplosionResistance(aTNT);
        }

        return super.getExplosionResistance(aTNT, aWorld, aX, aY, aZ, eX, eY, eZ);
    }
}
