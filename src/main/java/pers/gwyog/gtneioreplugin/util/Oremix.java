package pers.gwyog.gtneioreplugin.util;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;

public class Oremix {
	
	@CsvCustomBindByName(column = "Ore Name", required = true, converter = Veinrenamer.class)
	private String oreName;
	@CsvBindByName(column = "Primary", required = false)
	private String primary = "";
	@CsvBindByName(column = "Secondary", required = false)
	private String secondary = "";
	@CsvBindByName(column = "Inbetween", required = false)
	private String inbetween = "";
	@CsvBindByName(column = "Around", required = false)
	private String around = "";
	@CsvBindByName(column = "ID	", required = false)
	private String mix = "";
	@CsvBindByName(column = "Tier", required = false)
	private String tier = "";
	@CsvBindByName(column = "Height", required = false)
	private String height = "";
	@CsvBindByName(column = "Density", required = false)
	private int density;
	@CsvBindByName(column = "Size", required = false)
	private int size;
	@CsvBindByName(column = "Weight", required = false)
	private int weight;
	@CsvCustomBindByName(column = "Overworld", required = false, converter = XtoBool.class)
	private boolean overworld;
	@CsvCustomBindByName(column = "Nether", required = false, converter = XtoBool.class)
	private boolean nether;
	@CsvCustomBindByName(column = "End", required = false, converter = XtoBool.class)
	private boolean end;
	@CsvCustomBindByName(column = "Moon", required = false, converter = XtoBool.class)
	public boolean mo;
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

	@CsvCustomBindByName(column = "End Asteroids", required = false, converter = XtoBool.class)
	public boolean ea;
	@CsvCustomBindByName(column = "AstroidBelt", required = false, converter = XtoBool.class)
	public boolean as;
	@CsvCustomBindByName(column = "Barnard C", required = false, converter = XtoBool.class)
	public boolean bc;
	@CsvCustomBindByName(column = "Barnard E", required = false, converter = XtoBool.class)
	public boolean be;
	@CsvCustomBindByName(column = "Barnard F", required = false, converter = XtoBool.class)
	public boolean bf;
	@CsvCustomBindByName(column = "Mars", required = false, converter = XtoBool.class)
	public boolean ma;
	@CsvCustomBindByName(column = "Callisto", required = false, converter = XtoBool.class)
	public boolean ca;
	@CsvCustomBindByName(column = "Centauri Bb", required = false, converter = XtoBool.class)
	public boolean cb;
	@CsvCustomBindByName(column = "Ceres", required = false, converter = XtoBool.class)
	public boolean ce;
	@CsvCustomBindByName(column = "Twilight Forest", required = false, converter = XtoBool.class)
	public boolean tf;
	@CsvCustomBindByName(column = "Deep Dark", required = false, converter = XtoBool.class)
	public boolean dd;
	@CsvCustomBindByName(column = "Phobos", required = false, converter = XtoBool.class)
	public boolean ph;
	@CsvCustomBindByName(column = "Deimos", required = false, converter = XtoBool.class)
	public boolean de;
	@CsvCustomBindByName(column = "Europa", required = false, converter = XtoBool.class)
	public boolean eu;
	@CsvCustomBindByName(column = "Ganymede", required = false, converter = XtoBool.class)
	public boolean ga;
	@CsvCustomBindByName(column = "Io", required = false, converter = XtoBool.class)
	public boolean io;
	@CsvCustomBindByName(column = "Venus", required = false, converter = XtoBool.class)
	public boolean ve;
	@CsvCustomBindByName(column = "Mercury", required = false, converter = XtoBool.class)
	public boolean me;
	@CsvCustomBindByName(column = "Enceladus", required = false, converter = XtoBool.class)
	public boolean en;
	@CsvCustomBindByName(column = "Titan", required = false, converter = XtoBool.class)
	public boolean ti;
	@CsvCustomBindByName(column = "Miranda", required = false, converter = XtoBool.class)
	public boolean mi;
	@CsvCustomBindByName(column = "Oberon", required = false, converter = XtoBool.class)
	public boolean ob;
	@CsvCustomBindByName(column = "Triton", required = false, converter = XtoBool.class)
	public boolean tr;
	@CsvCustomBindByName(column = "Proteus", required = false, converter = XtoBool.class)
	public boolean pr;
	@CsvCustomBindByName(column = "Pluto", required = false, converter = XtoBool.class)
	public boolean pl;
	@CsvCustomBindByName(column = "Kuiper Belt", required = false, converter = XtoBool.class)
	public boolean kb;
	@CsvCustomBindByName(column = "Haumea", required = false, converter = XtoBool.class)
	public boolean ha;
	@CsvCustomBindByName(column = "Makemake", required = false, converter = XtoBool.class)
	public boolean make;
	@CsvCustomBindByName(column = "Vega B", required = false, converter = XtoBool.class)
	public boolean vb;
	@CsvCustomBindByName(column = "T Ceti E", required = false, converter = XtoBool.class)
	public boolean tcetie;


	
	
	public Oremix() {
	}
	
	public void setOreName(String s) {
		this.oreName = s;
	}
	public void setPrimary(String s) {
		this.primary = s;
	}
	public void setSecondary(String s) {
		this.secondary = s;
	}
	public void setInbetween(String s) {
		this.inbetween = s;
	}
	public void setAround(String s) {
		this.around = s;
	}
	public void setMix(String s) {
		this.mix = s;
	}
	public void setTier(String s) {
		this.tier = s;
	}
	public void setHeight(String s) {
		this.height = s;
	}
	public void setDensity(int i) {
		this.density = i;
	}
	public void setSize(int i) {
		this.size = i;
	}
	public void setWeight(int i) {
		this.weight = i;
	}
	public void setOverworld(boolean s) {
		this.overworld = s;
	}
	public void setNether(boolean s) {
		this.nether = s;
	}
	public void setEnd(boolean s) {
		this.end = s;
	}
	
	public String getOreName() {
		return this.oreName;
	}
	
	public String getPrimary() {
		return this.primary;
	}
	public String getSecondary() {
		return this.secondary;
	}
	public String getInbetween() {
		return this.inbetween;
	}
	public String getAround() {
		return this.around;
	}
	public String getMix() {
		return this.mix;
	}
	public String getTier() {
		return this.tier;
	}
	public String getHeight() {
		return this.height;
	}
	public int getDensity() {
		return this.density;
	}
	public int getSize() {
		return this.size;
	}
	public int getWeight() {
		return this.weight;
	}
	public int getMinY() {
		calculateminmax();
		return this.miny;
	}
	public int getMaxY() {
		calculateminmax();
		return this.maxy;
	}
	public boolean getOverworld() {
		return this.overworld;
	}
	public boolean getNether() {
		return this.nether;
	}
	public boolean getEnd() {
		return this.end;
	}
	
	private int miny,maxy;
	
	private void calculateminmax() {
		this.miny=Integer.parseInt(this.height.split("-")[0]);
		this.maxy=Integer.parseInt(this.height.split("-")[1]);
	}
	
	public void setMinY(int i) {
		this.miny=i;
	}
	public void setMaxY(int i) {
		this.maxy=i;
	}
	
	public String getHeightcalced() {
		return new String (this.miny+"-"+this.maxy);
	}
	
}
