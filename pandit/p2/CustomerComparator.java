
package p2;
import java.util.Comparator;


public class CustomerComparator implements Comparator<Customer>{

	@Override
	public int compare(Customer c1, Customer c2) {
		// Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        if (c1.getPriority() > c2.getPriority())
        {
            return -1;
        }
        if (c1.getPriority() < c2.getPriority())
        {
            return 1;
        }
        return 0;

	}



}
