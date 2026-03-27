package gtPlusPlus.core.handler;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.github.bsideup.jabel.Desugar;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class BookHandler {

    public static Int2ObjectMap<BookTemplate> mBookMap = new Int2ObjectOpenHashMap<>();

    public static BookTemplate book_ThermalBoiler;
    public static BookTemplate book_MultiPowerStation;
    public static BookTemplate book_ModularBauble;
    public static BookTemplate book_NuclearManual;

    public static void run() {

        Logger.INFO("Writing books.");

        // Thermal Boiler
        book_ThermalBoiler = writeBookTemplate(
            0,
            "Manual_Thermal_Boiler",
            "Thermal Boiler Manual",
            "GregoriusT",
            new String[] { "Book.Thermal Boiler Manual.Page00", "Book.Thermal Boiler Manual.Page01",
                "Book.Thermal Boiler Manual.Page02", "Book.Thermal Boiler Manual.Page03",
                "Book.Thermal Boiler Manual.Page04", "Book.Thermal Boiler Manual.Page05",
                "Book.Thermal Boiler Manual.Page06", "Book.Thermal Boiler Manual.Page07",
                "Book.Thermal Boiler Manual.Page08", "Book.Thermal Boiler Manual.Page09",
                "Book.Thermal Boiler Manual.Page10", "Book.Thermal Boiler Manual.Page11",
                "Book.Thermal Boiler Manual.Page12", "Book.Thermal Boiler Manual.Page13",
                "Book.Thermal Boiler Manual.Page14", "Book.Thermal Boiler Manual.Page15" });

        // Test Novel
        book_MultiPowerStation = writeBookTemplate(
            1,
            "Manual_Multi_PowerStation",
            "Power Storage & You [Version 0.64]",
            "Alkalus",
            new String[] { "Book.Power Storage & You [Version 0.64].Page00",
                "Book.Power Storage & You [Version 0.64].Page01", "Book.Power Storage & You [Version 0.64].Page02",
                "Book.Power Storage & You [Version 0.64].Page03", "Book.Power Storage & You [Version 0.64].Page04",
                "Book.Power Storage & You [Version 0.64].Page05", "Book.Power Storage & You [Version 0.64].Page06",
                "Book.Power Storage & You [Version 0.64].Page07" });

        // Test Novel
        book_ModularBauble = writeBookTemplate(
            2,
            "Manual_Modular_Bauble",
            "How to: Modular Baubles",
            "Alkalus",
            new String[] { "Book.How to: Modular Baubles.Page00", "Book.How to: Modular Baubles.Page01",
                "Book.How to: Modular Baubles.Page02", "Book.How to: Modular Baubles.Page03",
                "Book.How to: Modular Baubles.Page04" });

        book_NuclearManual = writeBookTemplate(
            4,
            "Manual_NuclearStuff_1",
            "Nuclear Chemistry [FFPP]",
            "Alkalus",
            new String[] { "Book.Nuclear Chemistry [FFPP].Page00", "Book.Nuclear Chemistry [FFPP].Page01",
                "Book.Nuclear Chemistry [FFPP].Page02", "Book.Nuclear Chemistry [FFPP].Page03",
                "Book.Nuclear Chemistry [FFPP].Page04" });
    }

    public static void runLater() {
        // Multiblock Manuals

        // Thermal Boiler
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.itemCustomBook, 1, 0),
            new ItemStack[] { new ItemStack(Items.writable_book), new ItemStack(Items.lava_bucket) });

        // Power Substation
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.itemCustomBook, 1, 1),
            new ItemStack[] { new ItemStack(Items.writable_book),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tin, 1) });

        // Nuclear Manual
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(ModItems.itemCustomBook, 1, 4),
            new ItemStack[] { new ItemStack(Items.writable_book),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1) });

        for (int meta : mBookMap.keySet()) {
            ItemStack bookstack = new ItemStack(ModItems.itemCustomBook, 1, meta);
            GTOreDictUnificator.registerOre("bookWritten", bookstack);
            GTOreDictUnificator.registerOre("craftingBook", bookstack);
        }
    }

    public static BookTemplate writeBookTemplate(int meta, String aMapping, String aTitle, String aAuthor,
        String[] aPages) {
        for (int i = 0; i < aPages.length; i++) {
            aPages[i] = aPages[i].replaceAll("\n", "<BR>");
        }
        BookTemplate mTemp = new BookTemplate(meta, aMapping, aTitle, aAuthor, aPages);
        mBookMap.put(meta, mTemp);
        return mTemp;
    }

    @Desugar
    public record BookTemplate(int mMeta, String mMapping, String mTitle, String mAuthor, String[] mPages) {}
}
