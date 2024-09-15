package gregtech.api.multitileentity.multiblock.casing;

<<<<<<< HEAD
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
=======
>>>>>>> a3c50d9014 (Revert deletion of Glasses)
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockUnlocalizedName;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.Botania;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Thaumcraft;

import com.gtnewhorizon.structurelib.structure.IStructureElementChain;

<<<<<<< HEAD
import gregtech.api.GregTechAPI;

=======
>>>>>>> a3c50d9014 (Revert deletion of Glasses)
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

<<<<<<< HEAD
            // Tinted Industrial Glass
            ofBlockAnyMeta(GregTechAPI.sBlockTintedGlass, 0),

=======
>>>>>>> a3c50d9014 (Revert deletion of Glasses)
            // warded glass
            ofBlockUnlocalizedName(Thaumcraft.ID, "blockCosmeticOpaque", 2, false));
    }

}
