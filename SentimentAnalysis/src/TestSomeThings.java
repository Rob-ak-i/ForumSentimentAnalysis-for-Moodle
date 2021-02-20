import util.Time;

public class TestSomeThings {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//DataTable 
		testtime("2012-12-31T23:59:59");
	}
	public static void testtime(String time) {
		System.out.println(time);
		int inttime =Time.fromString(time);
		System.out.println(inttime);
		String strtime=Time.fromIntegerWithTimeZone(inttime);
		System.out.println(strtime);
		
	}

}
