package pers.gwyog.gtneioreplugin.util;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

@SuppressWarnings("unused")
public class Oremix {

    @CsvCustomBindByName(column = "Moon", converter = XtoBool.class)
    public boolean mo;

    @CsvCustomBindByName(column = "End Asteroids", converter = XtoBool.class)
    public boolean ea;

    @CsvCustomBindByName(column = "AstroidBelt", converter = XtoBool.class)
    public boolean as;

    @CsvCustomBindByName(column = "Barnard C", converter = XtoBool.class)
    public boolean bc;

    @CsvCustomBindByName(column = "Barnard E", converter = XtoBool.class)
    public boolean be;

    @CsvCustomBindByName(column = "Barnard F", converter = XtoBool.class)
    public boolean bf;

    @CsvCustomBindByName(column = "Mars", converter = XtoBool.class)
    public boolean ma;

    @CsvCustomBindByName(column = "Callisto", converter = XtoBool.class)
    public boolean ca;

    @CsvCustomBindByName(column = "Centauri Bb", converter = XtoBool.class)
    public boolean cb;

    @CsvCustomBindByName(column = "Ceres", converter = XtoBool.class)
    public boolean ce;

    @CsvCustomBindByName(column = "Twilight Forest", converter = XtoBool.class)
    public boolean tf;

    @CsvCustomBindByName(column = "Deep Dark", converter = XtoBool.class)
    public boolean dd;

    @CsvCustomBindByName(column = "Phobos", converter = XtoBool.class)
    public boolean ph;

    @CsvCustomBindByName(column = "Deimos", converter = XtoBool.class)
    public boolean de;

    @CsvCustomBindByName(column = "Europa", converter = XtoBool.class)
    public boolean eu;

    @CsvCustomBindByName(column = "Ganymede", converter = XtoBool.class)
    public boolean ga;

    @CsvCustomBindByName(column = "Io", converter = XtoBool.class)
    public boolean io;

    @CsvCustomBindByName(column = "Venus", converter = XtoBool.class)
    public boolean ve;

    @CsvCustomBindByName(column = "Mercury", converter = XtoBool.class)
    public boolean me;

    @CsvCustomBindByName(column = "Enceladus", converter = XtoBool.class)
    public boolean en;

    @CsvCustomBindByName(column = "Titan", converter = XtoBool.class)
    public boolean ti;

    @CsvCustomBindByName(column = "Miranda", converter = XtoBool.class)
    public boolean mi;

    @CsvCustomBindByName(column = "Oberon", converter = XtoBool.class)
    public boolean ob;

    @CsvCustomBindByName(column = "Triton", converter = XtoBool.class)
    public boolean tr;

    @CsvCustomBindByName(column = "Proteus", converter = XtoBool.class)
    public boolean pr;

    @CsvCustomBindByName(column = "Pluto", converter = XtoBool.class)
    public boolean pl;

    @CsvCustomBindByName(column = "Kuiper Belt", converter = XtoBool.class)
    public boolean kb;

    @CsvCustomBindByName(column = "Haumea", converter = XtoBool.class)
    public boolean ha;

    @CsvCustomBindByName(column = "Makemake", converter = XtoBool.class)
    public boolean make;

    @CsvCustomBindByName(column = "Vega B", converter = XtoBool.class)
    public boolean vb;

    @CsvCustomBindByName(column = "T Ceti E", converter = XtoBool.class)
    public boolean tcetie;

    @CsvCustomBindByName(column = "Anubis", required = false, converter = XtoBool.class)
    public boolean an;

    @CsvCustomBindByName(column = "Horus", required = false, converter = XtoBool.class)
    public boolean ho;

    @CsvCustomBindByName(column = "Neper", required = false, converter = XtoBool.class)
    public boolean np;

    @CsvCustomBindByName(column = "Maahes", required = false, converter = XtoBool.class)
    public boolean mh;

    @CsvCustomBindByName(column = "Mehen Belt", required = false, converter = XtoBool.class)
    public boolean mb;

    @CsvCustomBindByName(column = "Seth", required = false, converter = XtoBool.class)
    public boolean se;

    @CsvCustomBindByName(column = "Ore Name", required = true, converter = Veinrenamer.class)
    private String oreName;

    @CsvBindByName(column = "Primary")
    private String primary = "";

    @CsvBindByName(column = "Secondary")
    private String secondary = "";

    @CsvBindByName(column = "Inbetween")
    private String inbetween = "";

    @CsvBindByName(column = "Around")
    private String around = "";

    @CsvBindByName(column = "ID	")
    private String mix = "";

    @CsvBindByName(column = "Tier")
    private String tier = "";

    @CsvBindByName(column = "Height")
    private String height = "";

    @CsvBindByName(column = "Density")
    private int density;

    @CsvBindByName(column = "Size")
    private int size;

    @CsvBindByName(column = "Weight")
    private int weight;

    @CsvCustomBindByName(column = "Overworld", converter = XtoBool.class)
    private boolean overworld;

    @CsvCustomBindByName(column = "Nether", converter = XtoBool.class)
    private boolean nether;

    @CsvCustomBindByName(column = "End", converter = XtoBool.class)
    private boolean end;

    private int miny, maxy;

    public Oremix() {}

    public boolean isMo() {
        return mo;
    }

    public void setMo(boolean mo) {
        this.mo = mo;
    }

    public boolean isEa() {
        return ea;
    }

    public void setEa(boolean ea) {
        this.ea = ea;
    }

    public boolean isAs() {
        return as;
    }

    public void setAs(boolean as) {
        this.as = as;
    }

    public boolean isBc() {
        return bc;
    }

    public void setBc(boolean bc) {
        this.bc = bc;
    }

    public boolean isBe() {
        return be;
    }

    public void setBe(boolean be) {
        this.be = be;
    }

    public boolean isBf() {
        return bf;
    }

    public void setBf(boolean bf) {
        this.bf = bf;
    }

    public boolean isMa() {
        return ma;
    }

    public void setMa(boolean ma) {
        this.ma = ma;
    }

    public boolean isCa() {
        return ca;
    }

    public void setCa(boolean ca) {
        this.ca = ca;
    }

    public boolean isCb() {
        return cb;
    }

    public void setCb(boolean cb) {
        this.cb = cb;
    }

    public boolean isCe() {
        return ce;
    }

    public void setCe(boolean ce) {
        this.ce = ce;
    }

    public boolean isTf() {
        return tf;
    }

    public void setTf(boolean tf) {
        this.tf = tf;
    }

    public boolean isDd() {
        return dd;
    }

    public void setDd(boolean dd) {
        this.dd = dd;
    }

    public boolean isPh() {
        return ph;
    }

    public void setPh(boolean ph) {
        this.ph = ph;
    }

    public boolean isDe() {
        return de;
    }

    public void setDe(boolean de) {
        this.de = de;
    }

    public boolean isEu() {
        return eu;
    }

    public void setEu(boolean eu) {
        this.eu = eu;
    }

    public boolean isGa() {
        return ga;
    }

    public void setGa(boolean ga) {
        this.ga = ga;
    }

    public boolean isIo() {
        return io;
    }

    public void setIo(boolean io) {
        this.io = io;
    }

    public boolean isVe() {
        return ve;
    }

    public void setVe(boolean ve) {
        this.ve = ve;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public boolean isEn() {
        return en;
    }

    public void setEn(boolean en) {
        this.en = en;
    }

    public boolean isTi() {
        return ti;
    }

    public void setTi(boolean ti) {
        this.ti = ti;
    }

    public boolean isMi() {
        return mi;
    }

    public void setMi(boolean mi) {
        this.mi = mi;
    }

    public boolean isOb() {
        return ob;
    }

    public void setOb(boolean ob) {
        this.ob = ob;
    }

    public boolean isTr() {
        return tr;
    }

    public void setTr(boolean tr) {
        this.tr = tr;
    }

    public boolean isPr() {
        return pr;
    }

    public void setPr(boolean pr) {
        this.pr = pr;
    }

    public boolean isPl() {
        return pl;
    }

    public void setPl(boolean pl) {
        this.pl = pl;
    }

    public boolean isKb() {
        return kb;
    }

    public void setKb(boolean kb) {
        this.kb = kb;
    }

    public boolean isHa() {
        return ha;
    }

    public void setHa(boolean ha) {
        this.ha = ha;
    }

    public boolean isMake() {
        return make;
    }

    public void setMake(boolean make) {
        this.make = make;
    }

    public boolean isVb() {
        return vb;
    }

    public void setVb(boolean vb) {
        this.vb = vb;
    }

    public boolean isTcetie() {
        return tcetie;
    }

    public void setTcetie(boolean tcetie) {
        this.tcetie = tcetie;
    }

    public boolean isAn() {
        return an;
    }

    public void setAn(boolean an) {
        this.an = an;
    }

    public boolean isHo() {
        return ho;
    }

    public void setHo(boolean ho) {
        this.ho = ho;
    }

    public boolean isNp() {
        return np;
    }

    public void setNp(boolean np) {
        this.np = np;
    }

    public boolean isMh() {
        return mh;
    }

    public void setMh(boolean mh) {
        this.mh = mh;
    }

    public boolean isMb() {
        return mb;
    }

    public void setMb(boolean mb) {
        this.mb = mb;
    }

    public boolean isSe() {
        return se;
    }

    public void setSe(boolean se) {
        this.se = se;
    }

    public String getOreName() {
        return this.oreName;
    }

    public void setOreName(String s) {
        this.oreName = s;
    }

    public String getPrimary() {
        return this.primary;
    }

    public void setPrimary(String s) {
        this.primary = s;
    }

    public String getSecondary() {
        return this.secondary;
    }

    public void setSecondary(String s) {
        this.secondary = s;
    }

    public String getInbetween() {
        return this.inbetween;
    }

    public void setInbetween(String s) {
        this.inbetween = s;
    }

    public String getAround() {
        return this.around;
    }

    public void setAround(String s) {
        this.around = s;
    }

    public String getMix() {
        return this.mix;
    }

    public void setMix(String s) {
        this.mix = s;
    }

    public String getTier() {
        return this.tier;
    }

    public void setTier(String s) {
        this.tier = s;
    }

    public String getHeight() {
        return this.height;
    }

    public void setHeight(String s) {
        this.height = s;
    }

    public int getDensity() {
        return this.density;
    }

    public void setDensity(int i) {
        this.density = i;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int i) {
        this.size = i;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int i) {
        this.weight = i;
    }

    public int getMinY() {
        calculateminmax();
        return this.miny;
    }

    public void setMinY(int i) {
        this.miny = i;
    }

    public int getMaxY() {
        calculateminmax();
        return this.maxy;
    }

    public void setMaxY(int i) {
        this.maxy = i;
    }

    public boolean getOverworld() {
        return this.overworld;
    }

    public void setOverworld(boolean s) {
        this.overworld = s;
    }

    public boolean getNether() {
        return this.nether;
    }

    public void setNether(boolean s) {
        this.nether = s;
    }

    public boolean getEnd() {
        return this.end;
    }

    public void setEnd(boolean s) {
        this.end = s;
    }

    private void calculateminmax() {
        this.miny = Integer.parseInt(this.height.split("-")[0]);
        this.maxy = Integer.parseInt(this.height.split("-")[1]);
    }

    public String getHeightcalced() {
        return this.miny + "-" + this.maxy;
    }
}
