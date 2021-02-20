package util;

public class Time {
	public static String fromIntegerWithTimeZone(long time) {
		String date = new java.text.SimpleDateFormat("yyyy-MM-dd/HH:mm:ss").format(time*1000L);
		date=date.replace('/', 'T');
		return date;
	}
	private static int[] monthsLen= {31,28,31,30,31,30,31,31,30,31,30,31};
	private static int[] monthsLenExtra= {31,29,31,30,31,30,31,31,30,31,30,31};
	public static String fromIntegerBroken(int time) {
		//int time=Integer.valueOf(postTime);
		int mYearsCount=1970+time/(86400*365);
		System.out.println(86400*365);
		System.out.println(time%(86400*365));
		mYearsCount=1970+time/(86400*365);
		int dextra = (mYearsCount-1970)/4;
		if(time%(86400*365)+dextra*86400>86400*365)mYearsCount--;
		boolean isExtraYear=Math.abs(mYearsCount-2012)%4==0;
		if(time%(86400*365)+(dextra+(isExtraYear?1:0))*86400>86400*365)mYearsCount--;
		int[] monthsLength=monthsLen;
		if(isExtraYear)monthsLength=monthsLenExtra;
		int d = time/86400;
		int dd = d-dextra;
		int dayOfYear = dd%365;
		if(isExtraYear)dayOfYear = dd%366;
		int dayOfMonth=dayOfYear;
		int month=0;
		while(dayOfMonth>monthsLength[month]) {
			dayOfMonth-=monthsLength[month];
			month++;
			//if(month==12&&dayOfMonth<)
			if(month==12) {
				System.out.println(dayOfMonth);
				dayOfMonth=1;
				mYearsCount++;
				break;
			}
		}
		int s=time-(d*86400);
		int hour = s/3600;
		int minute=s-(hour*3600);
		int secund=minute;
		minute=minute/60;
		secund=secund-minute*60;
		//2017-01-30T17:10:21+07:00
		String mon=Integer.toString(month+1);
		if(mon.length()==1)mon='0'+mon;
		String dm=Integer.toString(dayOfMonth);
		if(dm.length()==1)dm='0'+dm;
		String hr=Integer.toString(hour);
		if(hr.length()==1)hr='0'+hr;
		String min=Integer.toString(minute);
		if(min.length()==1)min='0'+min;
		String sec=Integer.toString(secund);
		if(sec.length()==1)sec='0'+sec;
		return
				Integer.toString(mYearsCount)
				+'-'
				+mon
				+'-'
				+dm
				+'T'
				+hr
				+':'
				+min
				+':'
				+sec
				;
	}
	public static int fromString(String time) {

		int indexOfSign=0, indexOfSign2=0; 
		indexOfSign=time.indexOf('-');
		int yearsCount = Integer.valueOf(time.substring(0,indexOfSign));
		indexOfSign2=time.indexOf('-',indexOfSign+1);
		int mon=Integer.valueOf(time.substring(indexOfSign+1,indexOfSign2));
		indexOfSign=time.indexOf('T');
		int dm=Integer.valueOf(time.substring(indexOfSign2+1,indexOfSign));
		indexOfSign2=time.indexOf(':');
		int hr=Integer.valueOf(time.substring(indexOfSign+1,indexOfSign2));
		indexOfSign=time.indexOf(':',indexOfSign2+1);
		int min=Integer.valueOf(time.substring(indexOfSign2+1,indexOfSign));
		int sec=Integer.valueOf(time.substring(indexOfSign+1));
		
		int secondsOfDay=sec+min*60+hr*3600;
		int secondsOfMonth=(dm-1)*86400+secondsOfDay;
		boolean isExtraYear=Math.abs(2012-yearsCount)%4==0;
		int secondsOfYear=secondsOfMonth;
		for(int i=0;i<mon-1;++i)secondsOfYear+=86400*monthsLen[i];
		if(isExtraYear&&mon>2)secondsOfYear+=86400;
		int extraYearsCount=(yearsCount-1-1970)/4;
		int totalSecondsAfter1970=(yearsCount-1970)*86400*365;
		totalSecondsAfter1970+=extraYearsCount*86400;
		totalSecondsAfter1970+=secondsOfYear;
		return totalSecondsAfter1970;
	}
}
