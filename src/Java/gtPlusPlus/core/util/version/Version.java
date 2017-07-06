package gtPlusPlus.core.util.version;

public class Version {

		final int Major;
		final int Minor;
		final int Minor2;
		final String Suffix;
		final int SuffixID;	    

		public Version(int Major, int Minor, int Minor2, SUFFIX Suffix){
			this.Major = Major;
			this.Minor = Minor;
			this.Minor2 = Minor2;
			this.Suffix = Suffix.getSuffix();
			this.SuffixID = Suffix.ordinal();
		}

		public boolean isCurrentVersionNewer(Version this, Version comparingTo){

			if (this.Major > comparingTo.Major){
				return true;
			}
			else if (this.Major < comparingTo.Major){
				return false;
			}
			else {
				if (this.Minor > comparingTo.Minor){
					return true;
				}
				else if (this.Minor < comparingTo.Minor){
					return false;
				}
				else {
					if (this.Minor2 > comparingTo.Minor2){
						return true;
					}
					else if (this.Minor2 < comparingTo.Minor2){
						return false;
					}
					else {
						if (this.SuffixID > comparingTo.SuffixID){
							return true;
						}
						else if (this.SuffixID < comparingTo.SuffixID){
							return false;
						}
						else {
							return false;
						}
					}
				}
			}
		}

		public boolean isVersionSame(Version comparingTo){
			if (this.SuffixID == comparingTo.SuffixID){
				return true;
			}
			else {
				return false;
			}
		}
		
		public boolean isUpdateRequired(Version comparingTo){
			if (isVersionSame(comparingTo)){
				return false;
			}
			else if (isCurrentVersionNewer(comparingTo)){
				return false;
			}
			else {
				return true;
			}
		}
		
		public String getVersionAsString(){
			return (this.Major+"."+this.Minor+"."+this.Minor2+"-"+this.Suffix);
		}


	public static enum SUFFIX {
		Alpha("Alpha"),
		Beta("Beta"),
		Prerelease("Pre-Release"),
		Release("Release");

		private String nameSuffix;
		private SUFFIX (final String suffix){
			this.nameSuffix = suffix;
		}

		public String getSuffix(){
			return this.nameSuffix;
		}

	}

}

