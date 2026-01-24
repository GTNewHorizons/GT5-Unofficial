package gregtech.api.util.locser;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.math.BigDecimal;

import net.minecraft.network.PacketBuffer;

import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

public class LocSerNumber implements ILocSer {

    protected BigDecimal number = BigDecimal.ZERO;

    public LocSerNumber() {}

    public LocSerNumber(Number number) {
        this.number = NumberFormatUtil.bigDecimalConverter(number);
    }

    @Override
    public void encode(PacketBuffer out) {
        NetworkUtils.writeStringSafe(out, getId());
        NetworkUtils.writeStringSafe(
            out,
            number.stripTrailingZeros()
                .toPlainString());
    }

    @Override
    public void decode(PacketBuffer in) {
        this.number = new BigDecimal(NetworkUtils.readStringSafe(in));
    }

    @Override
    public String localize() {
        return formatNumber(number);
    }

    @Override
    public String getId() {
        return "gt:number";
    }
}
