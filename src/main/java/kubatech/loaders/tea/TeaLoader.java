package kubatech.loaders.tea;

import static kubatech.api.enums.ItemList.BlackTea;
import static kubatech.api.enums.ItemList.EarlGrayTea;
import static kubatech.api.enums.ItemList.GreenTea;
import static kubatech.api.enums.ItemList.LemonTea;
import static kubatech.api.enums.ItemList.MilkTea;
import static kubatech.api.enums.ItemList.OolongTea;
import static kubatech.api.enums.ItemList.PeppermintTea;
import static kubatech.api.enums.ItemList.PuerhTea;
import static kubatech.api.enums.ItemList.WhiteTea;
import static kubatech.api.enums.ItemList.YellowTea;

import kubatech.loaders.tea.components.Tea;

public class TeaLoader {

    public static void run() {

        Tea.init();

        Tea.createTea("black")
            .setCustomCup(BlackTea.get(1L));
        Tea.createTea("earl_gray")
            .setCustomCup(EarlGrayTea.get(1L));
        Tea.createTea("green")
            .setCustomCup(GreenTea.get(1L));
        Tea.createTea("lemon")
            .setCustomCup(LemonTea.get(1L));
        Tea.createTea("milk")
            .setCustomCup(MilkTea.get(1L));
        Tea.createTea("oolong")
            .setCustomCup(OolongTea.get(1L));
        Tea.createTea("peppermint")
            .setCustomCup(PeppermintTea.get(1L));
        Tea.createTea("pu-erh")
            .setCustomCup(PuerhTea.get(1L));
        Tea.createTea("white")
            .setCustomCup(WhiteTea.get(1L));
        Tea.createTea("yellow")
            .setCustomCup(YellowTea.get(1L));

        Tea.finish();
    }

}
