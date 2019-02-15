package com.util;


import java.sql.Timestamp;
import java.util.Locale;

//
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.ISODateTimeFormat;

public class TimeToString {

	public static String datetimeToString(Timestamp ts) {

		// System.out.print(ts);
		DateTime jodatime = new DateTime(ts);
		// System.out.println(jodatime);
		org.joda.time.format.DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
		fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
		String jodastr = fmt.withLocale(Locale.ENGLISH).print(jodatime);
		// System.out.println(jodastr);
		return jodastr;

		// OffsetDateTime odt = OffsetDateTime.of(ts, ZoneOffset.UTC);

		/*
		 * String timestr = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(ts);
		 * System.out.println(timestr); // DateTimeFormatter formatter =
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd // HH:mm:ss,nnnnnn");
		 * 
		 * int nano = ts.getNanos(); System.out.println(Integer.toString(nano)); String
		 * finaltimestr = timestr + Integer.toString(nano);
		 * System.out.println(finaltimestr); return finaltimestr;
		 */

		// DateTimeFormatter formatter =
		// DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss,nnnnnnnnn");
		// OffsetDateTime odt = OffsetDateTime.of(ts.toLocalDateTime(), ZoneOffset.UTC);
		// System.out.println(odt.format(formatter));
		// String finaltimestr = odt.format(formatter);
		// return finaltimestr;

	}

	public static String longToMonth(Object object) {
		// TODO Auto-generated method stub

		String monthstr = String.format("%02d", object);
		return monthstr;
	}

	// public static Date StringToTime(String timestr) {
	// // TODO Auto-generated method stub
	// DateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	// Date time = null;
	// try {
	// time = sdf.format(time);
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// System.out.println("Time: " + sdf.format(time));
	// return time;
	// }

}
