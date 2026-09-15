package gregtech.nei.dumper;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

public class MetaTileEntityDumper extends GregTechIDDumper {

    public MetaTileEntityDumper() {
        super("metatileentity");
    }

    @Override
    public String[] header() {
        return new String[] { "id", "stackName", "className", };
    }

    @Override
    protected Iterable<String[]> dump(Mode mode) {
        List<String[]> list = new ArrayList<>();
        for (int i = 1; i < GregTechAPI.METATILEENTITIES.length; i++) {
            IMetaTileEntity mte = GregTechAPI.METATILEENTITIES[i];
            if (mode == Mode.FREE && mte == null) {
                list.add(new String[] { String.valueOf(i), "", "", });
            } else if (mode == Mode.USED && mte != null) {
                list.add(
                    new String[] { String.valueOf(i), mte.getStackForm(1)
                        .getDisplayName(),
                        mte.getClass()
                            .getSimpleName() });
            }
        }
        return list;
    }
}
