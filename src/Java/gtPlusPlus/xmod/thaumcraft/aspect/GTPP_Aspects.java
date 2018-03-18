package gtPlusPlus.xmod.thaumcraft.aspect;

import java.util.List;

import thaumcraft.api.aspects.Aspect;

public enum GTPP_Aspects {
	
	//Standard TC and GT Aspects
	AER(1), ALIENIS(20), AQUA(3), ARBOR(1), AURAM(16), BESTIA(6), 
	COGNITIO(2), CORPUS(2), ELECTRUM(24), EXANIMIS(32), FABRICO(2),
	FAMES(2), GELUM(1), GRANUM(4), HERBA(2), HUMANUS(8), IGNIS(4),
	INSTRUMENTUM(4), ITER(6), LIMUS(3), LUCRUM(32), LUX(4), MACHINA(16),
	MAGNETO(24), MESSIS(3), METALLUM(8), METO(2), MORTUUS(16), MOTUS(4),
	NEBRISUM(48), ORDO(8), PANNUS(6), PERDITIO(2), PERFODIO(4),
	PERMUTATIO(12), POTENTIA(16), PRAECANTATIO(16), RADIO(48),
	SANO(24), SENSUS(4), SPIRITUS(24), STRONTIO(64), TELUM(6),
	TERRA(1), TEMPESTAS(64), TENEBRAE(24), TUTAMEN(12), VACUOS(6),
	VENENUM(16), VICTUS(4), VINCULUM(16), VITIUM(48), VITREUS(3), VOLATUS(12),
	
	//Custom Aspects
	CUSTOM_3(24), CUSTOM_4(24), CUSTOM_2(48), CUSTOM_5(48), CUSTOM_1(64);

	public Aspect mAspect;
	public int mValue;

	private GTPP_Aspects(final int aValue) {
		this.mValue = aValue;
	}

	public static class TC_AspectStack_Ex {
		public GTPP_Aspects mAspect;
		public long mAmount;

		public TC_AspectStack_Ex(final GTPP_Aspects aAspect, final long aAmount) {
			this.mAspect = aAspect;
			this.mAmount = aAmount;
		}

		public TC_AspectStack_Ex copy() {
			return new TC_AspectStack_Ex(this.mAspect, this.mAmount);
		}

		public TC_AspectStack_Ex copy(final long aAmount) {
			return new TC_AspectStack_Ex(this.mAspect, aAmount);
		}

		public List<TC_AspectStack_Ex> addToAspectList(final List<TC_AspectStack_Ex> aList) {
			if (this.mAmount == 0L) {
				return aList;
			}
			for (final TC_AspectStack_Ex tAspect : aList) {
				if (tAspect.mAspect == this.mAspect) {
					final TC_AspectStack_Ex tc_AspectStack = tAspect;
					tc_AspectStack.mAmount += this.mAmount;
					return aList;
				}
			}
			aList.add(this.copy());
			return aList;
		}

		public boolean removeFromAspectList(final List<TC_AspectStack_Ex> aList) {
			for (final TC_AspectStack_Ex tAspect : aList) {
				if (tAspect.mAspect == this.mAspect && tAspect.mAmount >= this.mAmount) {
					final TC_AspectStack_Ex tc_AspectStack = tAspect;
					tc_AspectStack.mAmount -= this.mAmount;
					if (tAspect.mAmount == 0L) {
						aList.remove(tAspect);
					}
					return true;
				}
			}
			return false;
		}
	}
}