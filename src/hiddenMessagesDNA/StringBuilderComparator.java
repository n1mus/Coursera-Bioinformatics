package hiddenMessagesDNA;
import java.util.Comparator;

public class StringBuilderComparator implements Comparator<StringBuilder> {
		
		
		@Override
		public
		int compare(StringBuilder s1, StringBuilder s2) {
			String str1 = s1.toString();
			String str2 = s2.toString();
			
			return str1.compareTo(str2);
			
		}
		
		/**
		 * not implemented
		 */
		public boolean equals(Object o) {
			return false;
		}



	}