package gregtech.api.multitileentity.multiblock.casing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Thaumcraft;

import com.gtnewhorizon.structurelib.structure.IStructureElementChain;

public class Glasses {

    /** support all Bart, Botania, Ic2, Thaumcraft glasses for multiblock structure **/
    public static <T> IStructureElementChain<T> chainAllGlasses() {
        return ofChain(
            // IndustrialCraft2 glass
            ofBlockUnlocalizedName(IndustrialCraft2.ID, "blockAlloyGlass", 0, true),

            // Botania glass
            ofBlockUnlocalizedName(Botania.ID, "manaGlass", 0, false),
            ofBlockUnlocalizedName(Botania.ID, "elfGlass", 0, false),

            // BartWorks glass
            ofBlockUnlocalizedName(BartWorks.ID, "BW_GlasBlocks", 0, true),
            ofBlockUnlocalizedName(BartWorks.ID, "BW_GlasBlocks2", 0, true),

            // warded glass
            ofBlockUnlocalizedName(Thaumcraft.ID, "blockCosmeticOpaque", 2, false));
    }

}
