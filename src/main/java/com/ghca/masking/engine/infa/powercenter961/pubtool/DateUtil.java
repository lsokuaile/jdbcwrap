package com.ghca.masking.engine.infa.powercenter961.pubtool;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DateUtil {
	
	public static interface TimeUnit {
		public static final int SECOND = 0;
		public static final int MINUTE = 1;
		public static final int HOUR = 2;
		public static final int DAY = 3;
	}
	/** 关于年份的处理
	 * @author Administrator 
	 * @create July 14, 2010 3:50:19 PM
	 * @return int (yyyy)
	 * @description 返回当前年
	 */
	public static int returnCurrentYear() {
		try {
			Date date = new Date();
			Calendar ca = Calendar.getInstance();
			ca.setTime(date);
			return ca.get(Calendar.YEAR);
		} catch (Exception ex) {
		}
		return 0;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 3:51:01 PM
	 * @param inputDate
	 * @param patten
	 * @return
	 * @description 返回指定格式输入时间的年份
	 */
	public static String getYear(String inputDate, String patten) {
		Date date = DateUtil.convertStringToDate(inputDate, patten);
		String year = DateUtil.convertDateToString(date, "yyyy");
		return year;
	}

	/**
	 * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	 *
	 * @param nowTime 当前时间
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @return
	 * @author jqlin
	 */
	public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime()
				|| nowTime.getTime() == endTime.getTime()) {
			return true;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 3:52:12 PM
	 * @param inputDate
	 * @param patten
	 * @return
	 * @description 返回指定格式输入时间的上一个年份
	 */
	public static String getLastYear(String inputDate, String patten) {
		String year = DateUtil.returnCurrentUtilDateString("yyyy");
		int lastyear = Integer.parseInt(year) - 1;
		return String.valueOf(lastyear);
	}
	
	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 4:06:30 PM
	 * @param inputDate
	 * @param patten
	 * @return
	 * @description 返回指定格式输入时间的下一年
	 */
	public static String getNextYear(String inputDate, String patten) {

		int num = DateUtil.getMonthDaysNum(inputDate, patten);
		String newDate = DateUtil.getYear(inputDate, patten) + "-"
				+ DateUtil.getMonth(inputDate, patten) + "-" + num;
		System.out.println("nnewDate =" + newDate);
		Date date = DateUtil.AddTime(DateUtil.convertStringToDate(newDate,
				"yyyy-MM-dd"), TimeUnit.DAY, 1);
		System.out.println("date =" + date);
		String nextYear = DateUtil.getYear(DateUtil.convertDateToString(
				date, patten), patten);
		System.out.println("next year =" + nextYear);
		return nextYear;
	}
	
	/** 关于月份的处理
	 * @author Administrator 
	 * @create July 14, 2010 3:56:09 PM
	 * @return month(int)
	 * @description 根据系统时间返回当前月
	 */
	public static int returnCurrentMonth() {
		try {
			Date date = new Date();
			Calendar ca = Calendar.getInstance();
			ca.setTime(date);
			return ca.get(Calendar.MONTH) + 1;
		} catch (Exception ex) {
		}
		return 0;
	}
	
	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 11:26:27 AM
	 * @param inputDate
	 * @param datePatten
	 * @return String
	 * @description 返回两位输入日期的月份
	 */
	public static String getMonthNumber(String inputDate, String datePatten) {
		Date date = DateUtil.convertStringToDate(inputDate, datePatten);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int i = calendar.get(Calendar.MONTH) + 1;
		if (i < 10) {
			return "0" + i;
		} else {
			return "" + i;
		}
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 3:47:46 PM
	 * @param inputDate
	 * @param patten
	 * @return String
	 * @description 返回指定格式输入时间的月份
	 */
	public static String getMonth(String inputDate, String patten) {
		Date date = DateUtil.convertStringToDate(inputDate, patten);
		String month = DateUtil.convertDateToString(date, "MM");
		return month;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 4:00:11 PM
	 * @param inputDate
	 * @param patten
	 * @return
	 * @description 返回指定格式输入时间的上一个月份
	 */
	public static String getLastMonthNumber(String inputDate, String patten) {
		Date date = DateUtil.AddTime(DateUtil.convertStringToDate(
				inputDate, patten), TimeUnit.DAY, -DateUtil.getMonthDaysNum(
				inputDate, patten));
		String lastMonth = DateUtil.getMonthNumber(DateUtil
				.convertDateToString(date, patten), patten);
		return lastMonth;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 4:02:10 PM
	 * @param inputDate
	 * @param patten
	 * @return
	 * @description 返回指定格式输入时间的下一个月份
	 */
	public static String getNextMonthNumber(String inputDate, String patten) {

		int num = DateUtil.getMonthDaysNum(inputDate, patten);
		String newDate = DateUtil.getYear(inputDate, patten) + "-"
				+ DateUtil.getMonth(inputDate, patten) + "-" + num;
		Date date = DateUtil.AddTime(DateUtil.convertStringToDate(newDate,
				"yyyy-MM-dd"), TimeUnit.DAY, 1);
		String nextMonth = DateUtil.getMonthNumber(DateUtil
				.convertDateToString(date, patten), patten);
		return nextMonth;
	}
	
	/** 
	 * @author Administrator 
	 * @create July 14, 2010 4:07:29 PM
	 * @param months(int)
	 * @return String
	 * @description 得到当前月份数(String)
    */	
	public static String getCurrentYearMonth(int months){
		Calendar cal = Calendar.getInstance(); 
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM"); 
		cal.set(cal.MONTH, months);
		String yearmonth=formatter.format(cal.getTime()); 
		return yearmonth;
	 }
	   
	/**
     * @author Administrator 
     * @create Jul 15, 2010 5:06:03 PM
     * @param quarter
     * @return
     * @description 返回季度的第一个月
     */
    public static String getQuarterMinMonth(String quarter) {

        if ("1".equals(quarter)) {
            return "01";
        } else if ("2".equals(quarter)) {
            return "04";
        } else if ("3".equals(quarter)) {
            return "07";
        } else if ("4".equals(quarter)) {
            return "10";
        } else {
            return "01";
        }
    }
	    
	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 5:08:01 PM
	 * @param quarter
	 * @return
	 * @description 返回季度的最后一个月
	 */
	    public static String getQuarterMaxMonth(String quarter) {

	        if ("1".equals(quarter)) {
	            return "03";
	        } else if ("2".equals(quarter)) {
	            return "06";
	        } else if ("3".equals(quarter)) {
	            return "09";
	        } else if ("4".equals(quarter)) {
	            return "12";
	        } else {
	            return "01";
	        }
	    }
    /**
     * @author Administrator 
     * @create July 14, 2010 3:38:10 PM
     * @return Timestamp (yyyy-MM-dd hh:mm:ss.xxx)
     * @description
     */
	public static Timestamp returnCurrentSqlDateTime() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @author Administrator 
	 * @create July 14, 2010 3:43:00 PM
	 * @param datePatten
	 * @return Timestamp (yyyy-MM-dd)
	 * @description
	 */
	public static java.sql.Date returnCurrentSqlDate(String datePatten) {
		java.sql.Date sqlDate = java.sql.Date.valueOf(DateUtil
				.returnCurrentUtilDateString(datePatten));
		return sqlDate;
	}

	/**
	 * @author Administrator 
	 * @create July 14, 2010 3:57:15 PM
	 * @return int (day of month)
	 * @description 返回当前天
	 */
	public static int returnCurrentDayOfMonth() {
		try {
			Date date = new Date();
			Calendar ca = Calendar.getInstance();
			ca.setTime(date);
			return ca.get(Calendar.DAY_OF_MONTH);
		} catch (Exception ex) {
		}
		return 0;
	}

	/**
	 * @author Administrator 
	 * @create July 14, 2010 4:00:42 PM
	 * @param dataPatten 输入格式
	 * @return Date
	 * @description 返回符合输入格式(dataPatten)的当前时间
	 */
	public static Date returnCurrentUtilDate(String dataPatten) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(dataPatten);
			return sdf.parse(DateFormat.getDateInstance().format(new Date()));
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 10:53:53 AM
	 * @return String
	 * @description 返回String类型的当前时间
	 */
	public static String returnCurrentUtilDateString() {
		try {
			Date Now = new Date();
			return Now.toString();
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 9:12:48 AM
	 * @param dataPatten
	 * @return String
	 * @description 返回符合输入格式(dataPatten)的当前时间
	 */
	public static String returnCurrentUtilDateString(String dataPatten) {

		try {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(dataPatten);
			return sdf.format(now);
		} catch (Exception ex) {
			return null;
		}
	}
	// ******************************对系统当前时间的操作  end***************************************
	
	// ******************************时间格式转换操作  start***************************************
	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 10:01:32 AM
	 * @param inputDate
	 * @param dataPatten
	 * @return Date
	 * @description 将String类型的时间转换为Date型
	 */
	public static Date convertStringToDate(String inputDate, String dataPatten) {
		SimpleDateFormat sdf = new SimpleDateFormat(dataPatten);
		if(inputDate==null||inputDate.equals("")||inputDate.equals("null")){
			inputDate="1899-12-31";
		}
		Date date = null;
		try {
			date = sdf.parse(inputDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (date);
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 10:09:55 AM
	 * @param inputDate
	 * @param oldDatePatten
	 * @param newDatePatten
	 * @return String
	 * @description 将时间转换为指定格式
	 */
	public static String ConvertDateStrFormat(String inputDate,
			String oldDatePatten, String newDatePatten) {

		String str = DateUtil.convertDateToString(DateUtil
				.convertStringToDate(inputDate, oldDatePatten), newDatePatten);
		return str;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 10:18:40 AM
	 * @param inputDate
	 * @param dataPatten
	 * @return String
	 * @description 将Date型时间转换为String型
	 */
	public static String convertDateToString(Date inputDate, String dataPatten) {
		SimpleDateFormat sdf = new SimpleDateFormat(dataPatten);
		return sdf.format(inputDate);
	}

	// ******************************时间格式转换操作  end***************************************
	
	// ******************************时间运算的操作  start***************************************
	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 10:45:38 AM
	 * @param sourceDate
	 * @param subDate
	 * @return int
	 * @description 返回两个日期相差的天数
	 */
	public static int subtractDate(Date sourceDate, Date subDate) {
		int result = 0;
		long value = sourceDate.getTime() - subDate.getTime();
		value = value / 86400000;
		result = (int) value;
		return result;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 10:50:16 AM
	 * @param inputDate
	 * @param datePatten
	 * @return boolean
	 * @description 判断输入日期是否是周末(包括周六和周日)
	 */
	public static boolean isWeekend(String inputDate, String datePatten) {
		Date date = DateUtil.convertStringToDate(inputDate, datePatten);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		if (i == 6 || i == 0) {
			return true;
		} else
			return false;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 11:01:54 AM
	 * @param inputDate
	 * @param datePatten
	 * @return String
	 * @description 返回输入日期是星期几
	 */
	public static String getWeek(String inputDate, String datePatten) {
		Date date = DateUtil.convertStringToDate(inputDate, datePatten);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		if (i == 0)
			return "星期日";

		if (i == 1)
			return "星期一";

		if (i == 2)
			return "星期二";

		if (i == 3)
			return "星期三";

		if (i == 4)
			return "星期四";

		if (i == 5)
			return "星期五";

		if (i == 6)
			return "星期六";
		else
			return "";
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 11:07:07 AM
	 * @param inputDate
	 * @param datePatten
	 * @return String
	 * @description 返回输入日期所在周星期一的日期
	 */
	public static String getWeekStartDate(String inputDate, String datePatten) {
		Date date = DateUtil.convertStringToDate(inputDate, datePatten);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		if (i == 0)
			return DateUtil.addToDate(inputDate, datePatten, -6);
		else
			return DateUtil.addToDate(inputDate, datePatten, 1 - i);
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 11:18:30 AM
	 * @param inputDate
	 * @param datePatten
	 * @return String
	 * @description 返回输入日期所在周星期日的日期
	 */
	public static String getWeekEndDate(String inputDate, String datePatten) {
		Date date = DateUtil.convertStringToDate(inputDate, datePatten);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;

		if (i == 0)
			return inputDate;
		else
			return DateUtil.addToDate(inputDate, datePatten, 7 - i);
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 11:08:41 AM
	 * @param inputDate
	 * @param datePatten
	 * @return String
	 * @description 返回输入日期所在的月份名
	 */
	public static String getMonthName(String inputDate, String datePatten) {
		Date date = DateUtil.convertStringToDate(inputDate, datePatten);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int i = calendar.get(Calendar.MONTH) + 1;

		if (i == 1)
			return "1月";

		if (i == 2)
			return "2月";

		if (i == 3)
			return "3月";

		if (i == 4)
			return "4月";

		if (i == 5)
			return "5月";

		if (i == 6)
			return "6月";
		if (i == 7)
			return "7月";
		if (i == 8)
			return "8月";
		if (i == 9)
			return "9月";
		if (i == 10)
			return "10月";
		if (i == 11)
			return "11月";
		if (i == 12)
			return "12月";
		else
			return "";
	}
	
	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 11:34:00 AM
	 * @param inputDate
	 * @param datePatten
	 * @return int
	 * @description 返回输入日期所在的周数
	 */
	public static int getWeekInt(String inputDate, String datePatten) {
		Date date = DateUtil.convertStringToDate(inputDate, datePatten);
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.setTime(date);
		int i = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return i;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 11:40:42 AM
	 * @param paraDate
	 * @param offset 
	 * @param operand 操作数
	 * @return Date
	 * @description 返回输入日期的天(或小时或分或秒)做相应的加减操作后的日期
	 */
	public static Date AddTime(Date paraDate, int offset, int operand) {
		try {
			long Addcount = 1;
			Date RetDate = null;
			switch (offset) {
			case TimeUnit.SECOND:
				Addcount = 1000;
				break;

			case TimeUnit.MINUTE:
				Addcount = 60000;
				break;

			case TimeUnit.HOUR:
				Addcount = 3600000;
				break;

			case TimeUnit.DAY:
				Addcount = 86400000;
				break;
			}
			long rettime = paraDate.getTime() + Addcount * (long) operand;
			RetDate = new Date(rettime);
			Date date1 = RetDate;
			return date1;
		} catch (Exception ex) {
			Date date = null;
			return date;
		}
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 2:34:07 PM
	 * @param inputDate
	 * @param datePatten
	 * @param num
	 * @return String
	 * @description	返回规定格式日期加减天数后得到的日期
	 */
	public static String addToDate(String inputDate, String datePatten, int num) {
		String value = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(datePatten);
			Date date = sdf.parse(inputDate);
			Calendar xCalendar = GregorianCalendar.getInstance();
			xCalendar.setTime(date);
			xCalendar.add(Calendar.DATE, num);
			value = sdf.format(xCalendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 2:39:46 PM
	 * @param radomdate
	 * @param datepatten
	 * @return
	 * @description 返回输入日期所在月的第一天和最后一天
	 */
	public static String[] returnCurrentMonthBetween(String radomdate,
			String datepatten) {

		Date inputDate = DateUtil.convertStringToDate(radomdate, datepatten);
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(datepatten);
		cal.setTime(inputDate);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int maxDateNum = cal.getActualMaximum(Calendar.DATE);

		String monthstr = "";
		if (month < 10) {
			monthstr = "0" + month;
		} else {
			monthstr = "" + month;
		}
		String startMonthDay = year + "-" + monthstr + "-" + "01";
		Calendar enddate = new GregorianCalendar(year, month - 1, maxDateNum);
		String endMonthDay = formatter.format(enddate.getTime());
		startMonthDay = DateUtil.convertDateToString(DateUtil
				.convertStringToDate(startMonthDay, "yyyy-MM-dd"), datepatten);

		String xx[] = new String[] {};
		xx = new String[] { "" + startMonthDay, "" + endMonthDay };

		return xx;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 2:44:29 PM
	 * @param patten (yyyyMMdd)
	 * @return
	 * @description 返回规定格式的当前月所在的最后一天
	 */
	public static String returnCurrentMonthEndDate(String patten) {

		String currentDate = DateUtil.returnCurrentUtilDateString(patten);
		int maxDateNum = DateUtil.getMonthDaysNum(currentDate, patten);

		String newDate = DateUtil.getYear(currentDate, patten) + "-"
				+ DateUtil.getMonth(currentDate, patten) + "-" + maxDateNum;

		newDate = DateUtil
				.ConvertDateStrFormat(newDate, "yyyy-MM-dd", patten);
		return newDate;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 2:47:16 PM
	 * @param radomdate	(20100715)
	 * @param datepatten (yyyyMMdd)
	 * @return String[] 
	 * @description 按规定格式返回输入日期所在周的周一与周日日期
	 */
	public static String[] returnCurrentWeekBetween(String radomdate,
			String datepatten) {

		String weekday[] = new String[] {};
		int dayofweek = 0;
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat(datepatten);
			cal.setTime(formatter.parse(radomdate));

			dayofweek = cal.get(Calendar.DAY_OF_WEEK) - 1;
			String startdayofweek = "";
			String enddayofweek = "";
			if (dayofweek == 0) {
				startdayofweek = DateUtil.addToDate(radomdate, datepatten,
						-dayofweek + 1 - 7);
				enddayofweek = DateUtil.addToDate(radomdate, datepatten,
						dayofweek);
			} else {
				startdayofweek = DateUtil.addToDate(radomdate, datepatten,
						-dayofweek + 1);
				enddayofweek = DateUtil.addToDate(radomdate, datepatten,
						7 - dayofweek);
			}

			weekday = new String[] { "" + startdayofweek, "" + enddayofweek };

		} catch (Exception e) {
			e.printStackTrace();
		}
		return weekday;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 3:01:30 PM
	 * @param year  (2010)
	 * @param monthRegex  以","隔开的月份(1,2)
	 * @return LinkedHashMap
	 * @description 返回指定年份，指定月份的第一天至最后一天是星期几{20100101=星期五, 20100102=星期六, ……}
	 */
	public static LinkedHashMap generateDayAndWeek(String year,
			String monthRegex) {

		String[] monthArry = monthRegex.split(",");
		Calendar calendar = GregorianCalendar.getInstance();
		LinkedHashMap map = new LinkedHashMap();
		for (int i = 0; i < monthArry.length; i++) {
			LinkedHashMap monthmap = new LinkedHashMap();
			String month = "";
			if (monthArry[i].length() == 1) {
				month = "0" + monthArry[i];
			} else {
				month = monthArry[i];
			}
			Date date = DateUtil.convertStringToDate(year + month + "01",
					"yyyyMMdd");
			calendar.setTime(date);
			int maxdays = calendar.getActualMaximum(Calendar.DATE);

			//System.out.println("month = " + month);
			//System.out.println("maxdays = " + maxdays);

			/* cycle month to generate the month map */
			if (maxdays > 0) {
				for (int k = 1; k < maxdays + 1; k++) {
					String k_date_str = "";
					if (k < 10) {
						k_date_str = year + month + "0" + k;
					} else {
						k_date_str = year + month + k;
					}
					String week = DateUtil.getWeek(k_date_str,
							"yyyyMMdd");

					monthmap.put(k_date_str, week);
				}
			}//end if(maxdays > 0)
			map.put(monthArry[i], monthmap);
		}

		return map;
	}


	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 3:33:33 PM
	 * @param inputDate (20100714)
	 * @param patten	(yyyyMMdd)
	 * @return		(31)
	 * @description 返回输入时间所在月的天数
	 */
	public static int getMonthDaysNum(String inputDate, String patten) {
		Calendar calendar = GregorianCalendar.getInstance();

		Date date = DateUtil.convertStringToDate(inputDate, patten);
		calendar.setTime(date);
		int maxdays = calendar.getActualMaximum(Calendar.DATE);
		return maxdays;
	}


	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 3:46:57 PM
	 * @param inputDate
	 * @param patten
	 * @param c
	 * @return
	 * @description 返回输入日期所在月周末的日期
	 */
	public static String getAllMonthWeekendNumbers(String inputDate,
			String patten, char c) {

		int maxNum = DateUtil.getMonthDaysNum(inputDate, patten);
		String str = "";
		if (maxNum > 0) {
			for (int i = 1; i <= maxNum; i++) {
				String subDate = DateUtil.ConvertDateStrFormat(inputDate,
						patten, "yyyy-MM");
				String date = subDate + "-"
						+ DateUtil.getMonthNumber(String.valueOf(i),"MM");

				if (DateUtil.isWeekend(date, "yyyy-MM-dd")) {
					str += c + DateUtil.getMonthNumber(String.valueOf(i),"MM") + ",";
				}
			}

			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	/**
	 * @author Administrator 
	 * @create Jul 15, 2010 4:07:49 PM
	 * @param inputDate (2010-07-14)
	 * @param monthNum (3)
	 * @return	String (2010-10-14)
	 * @description 返回输入日期加上月份后的日期
	 */
	public static String getAddMonth(String inputDate, int monthNum) {
		String resultDate;
		resultDate = "";
		try {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			calendar.setTime(sdf.parse(inputDate));
			calendar.add(Calendar.MONTH, monthNum);
			Date date = calendar.getTime();
			resultDate = sdf.format(date);
		} catch (Exception ex) {
		} finally {
		}
		return resultDate;

	}

	/**
	 * @author Administrator 
	 * @create Jul 14, 2010 4:54:46 PM
	 * @param DateTime
	 * @return String
	 * @description 将格式为(14 Jul, 2010)或(14 Jul)的日期转化为(0714-2010)或(0714)
	 */
	public static String CoverValue(String DateTime)
	{
		//String date  = DateTime;
		String dayValue   = "";
		String monthValue = "";
		String timeValue = "";
		String coverDate = "";
		if(DateTime.length() > 6 && DateTime.length() == 12)
		{
			timeValue = DateTime.substring(7);
		}
		/*01-nov 06:29*/
		dayValue   = DateTime.substring(0,2);
		monthValue = DateTime.substring(3,6);
		/*System.out.println(dayValue);
		System.out.println(monthValue);*/
		
		String month = "";
		//String aa[] = new String[2];
		//aa = date.split("-");
		if(monthValue.toLowerCase().equals("jan"))
		{
			month ="01";
		}
		else if(monthValue.toLowerCase().equals("feb"))
		{
			month ="02";
		}
		else if(monthValue.toLowerCase().equals("mar"))
		{
			month ="03";
		}
		else if(monthValue.toLowerCase().equals("apr"))
		{
			month ="04";
		}
		else if(monthValue.toLowerCase().equals("may"))
		{
			month ="05";
		}
		else if(monthValue.toLowerCase().equals("jun"))
		{
			month ="06";
		}
		else if(monthValue.toLowerCase().equals("jul"))
		{
			month ="07";
		}
		else if(monthValue.toLowerCase().equals("aug"))
		{
			month ="08";
		}
		else if(monthValue.toLowerCase().equals("sep"))
		{
			month ="09";
		}
		else if(monthValue.toLowerCase().equals("oct"))
		{
			month ="10";
		}
		else if(monthValue.toLowerCase().equals("nov"))
		{
			month ="11";
		}
		else if(monthValue.toLowerCase().equals("dec"))
		{
			month ="12";
		}
		else
		{
			month = monthValue;
		}
		/*20070925-02:05:32*/
		if(DateTime.length() > 6)
		{
			coverDate = month + dayValue +"-"+timeValue;
		}else
		{
			coverDate = month + dayValue;
		}
		
		return coverDate;
	}
	
   /**
    * 对应界面上的日志;
    * 得到日志的提交时间 计算得到提交时间
    * 如果 周一显示周四数据 
    * 	  周二显示周五数据
    * 	  周三显示周一数据
    * 	  周四显示周二数据
    * 	  周五显示周三数据
    * 	  周六显示周四数据
    * 	  周日显示周四数据
    * @return
    */
  /* public static String getDefaultLogDate()
   {
	   Date date =	new Date();
	   *//**
	    * 间隔
	    *//*
	   int lengthDay = 2;
	   
	   if(date.getDay()==1 || date.getDay()==2)
	   {
		   lengthDay=4;
	   }
	   else if(date.getDay()==0)
	   {
		   lengthDay=3;
	   }
	   
	   return getCalCurrentDate(lengthDay);
   }*/
	public static String getDefaultLogDate()
	{
		return getCurrentFormate_Date();
	}
   /**
    * 当前的日期
    * 2011-05-06
    * @return
    */
   public static String getDefaultCurrentDate()
   {
	   Date date =	new Date();
	   /**
	    * 间隔
	    */
	   int lengthDay = 2;
	   
	   if(date.getDay()==1 || date.getDay()==2)
	   {
		   lengthDay=4;
	   }
	   else if(date.getDay()==0)
	   {
		   lengthDay=3;
	   }
	   
	   return getCalCurrentDate(lengthDay);
   }
   /**
    * 
    * @author Administrator 
    * @create July 14, 2010 4:05:27 PM
    * @param timeRecord (3)
    * @return String	(2010-07-11)
    * @description 返回当前时间加减天数后得到日期
    */
   public static String getCalCurrentDate(int timeRecord)
   {
	    Date d=new Date();   
	    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	    return df.format(new Date(d.getTime() - timeRecord * 24 * 60 * 60 * 1000));
   }
   // ******************************时间运算的操作  end***************************************
   
   public static String returnCurrentDateMode24() {
		try {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(now);
		} catch (Exception ex) {
			return null;
		}
	}
   public static String returnCurrentDateMode24_No_style() {
		try {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			return sdf.format(now);
		} catch (Exception ex) {
			return null;
		}
	}

   public static String getCurrentFormate_Date()
	{
		String date = returnCurrentDateMode12().substring(0,8);
		String date_formate="";
		date_formate=date.substring(0,4).concat("-")+date.substring(4,6).concat("-")
					+date.substring(6,8);
		return date_formate;
	}
   
   public static String returnCurrentDateMode12() {
		try {
			Date now = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
			return sdf.format(now);
		} catch (Exception ex) {
			return null;
		}
	}
   
   /**
    * 周一
    */
   public static String getWeeklyStartDay() 
   {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   
       // 得到当前日期   
       Calendar cal = Calendar.getInstance();   
       Date toDay = cal.getTime();   
 
       // 得到本周第一天日期   
       int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;   
       cal.add(Calendar.DATE, -day_of_week);   
       Date weekFirst = cal.getTime();   

       return formatter.format(weekFirst);
	   
   }
   /**
    * 周日...
    */
   public static String getWeeklyEndDay() 
   {
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");   
       // 得到当前日期   
       Calendar cal = Calendar.getInstance();   
       Date toDay = cal.getTime();   
 
       // 得到本周第一天日期   
       int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 2;   
       cal.add(Calendar.DATE, -day_of_week);   
       Date weekFirst = cal.getTime();   
       String weekFirststr = formatter.format(weekFirst);   
       
       // 得到本周最后一天   
       cal.add(Calendar.DATE, 6);   
       Date weekLast = cal.getTime();   

       return formatter.format(weekLast);  
   }
   public static String getMonthStartDay(){
	   return getCurrentFormate_Date().substring(0,8)+"01";
   }
   /**
    * 获得指定日期前一天的日期
    * 
    * @param specifiedDay format "yyyy-mm-dd"
    */
   public static String getSpecifiedDayBefore(String specifiedDay) {
       Calendar c = Calendar.getInstance();
       Date date = null;
       try {
           date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       c.setTime(date);
       int day = c.get(Calendar.DATE);
       c.set(Calendar.DATE, day - 1);

       String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
               .getTime());
       return dayBefore;
   }
   
   /**    
    * 得到本月的最后一天    
    *     
    * @return    
    */     
   public static String getMonthEndDay() {
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       Calendar c = Calendar.getInstance();
       c.add(Calendar.MONTH, 1);
       c.set(Calendar.DAY_OF_MONTH, 1);
       return getSpecifiedDayBefore(dateFormat.format(c.getTime()));
   }
   /**
    * 得到本月第一天
    * @return
    */
   public static String getMonthFirstDay(){
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   Calendar c = Calendar.getInstance();
	   c.set(Calendar.DATE, 1);
	   return dateFormat.format(c.getTime());
   }
   
   /**
    * 返回上个月第一天
    * @return
    */
   public static String getPreMonthFirstDay(){
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   Calendar c = Calendar.getInstance();
	   c.add(Calendar.MONTH, -1);
	   c.set(Calendar.DAY_OF_MONTH, 1);
	   return dateFormat.format(c.getTime());
   }
   
   /**
    * 返回下个月第一天 0时0点0分
    * @return
    * @author yinhao
    * @date 2013-4-18 下午05:12:40
    * @comment
    */
   public static Calendar getFirstDayOfNextMonth(){
	   Calendar c = Calendar.getInstance();
	   c.add(Calendar.MONTH, 1);
	   c.set(Calendar.DAY_OF_MONTH, 1);
	   c.set(Calendar.HOUR_OF_DAY, 0);
	   c.set(Calendar.MINUTE, 0);
	   c.set(Calendar.SECOND, 0);
	   c.set(Calendar.MILLISECOND, 0);
	   return c;
   }
  
   /**
    * 返回上月月底
    * @return
    */
   public static String getPreMonthEndDay(){
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   Calendar c = Calendar.getInstance();
	   c.set(Calendar.DAY_OF_MONTH, 1);
	   return getSpecifiedDayBefore(dateFormat.format(c.getTime()));
   }
   
   /**
    * 获取当前日期之前的第三个月月初（用于用户上网日志导出和非web应用程序日志导出）
    */
   public static String getBeforeThereMonthBegin(){
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   Calendar c = Calendar.getInstance();
	   c.add(Calendar.MONTH, -3);
	   c.set(Calendar.DAY_OF_MONTH, 1);
	   return dateFormat.format(c.getTime());
   }
   
   /**
    * 获取当前日期之前的第六个月月初（用于用户上网日志导出和非web应用程序日志导出）
    */
   public static String getBeforeSixMonthBegin(){
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   Calendar c = Calendar.getInstance();
	   c.add(Calendar.MONTH, -6);
	   c.set(Calendar.DAY_OF_MONTH, 1);
	   return dateFormat.format(c.getTime());
   }
   
   /**
    * 返回yyyy-MM-dd格式的日期
    * @param stringDate
    * @return
    */
   public static Date getDateFromString(String stringDate)throws Exception{
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   return dateFormat.parse(stringDate);
   }
   
   /**
    * 返回指定String日期的Calendar日期
    * @param date
    * @return
    */
   public static Calendar getCalendarFromString(String stringDate)throws Exception{
	   Calendar calendar = Calendar.getInstance();
	   calendar.setTime(getDateFromString(stringDate));
	   return calendar;
   }
   /**
    * 返回指定日期对于指定格式的日历
    * @param stringDate
    * @param patten
    * @return
    */
   public static Calendar getCalendarFromString(String stringDate,String patten){
	   Calendar calendar = Calendar.getInstance();
	   calendar.setTime(convertStringToDate(stringDate, patten));
	   return calendar;
   }
   
   /**
    * 返回指定Date日期的Calendar日期
    * @param date
    * @return
    */
   public static Calendar getCalendarFromDate(Date date)throws Exception{
	   Calendar calendar = Calendar.getInstance();
	   calendar.setTime(date);
	   return calendar;
   }
   /**
    * 日历翻到制定日期的前一天
    * @return
    * @throws Exception
    */
   public static Calendar getYesterday(String date)throws Exception{
	   Calendar calendar = getCalendarFromString(date);
	   calendar.add(Calendar.DAY_OF_MONTH, -1);
	   return calendar;
   }
   /**
    * 返回当前月内所有指定星期数
    * 1返回所有星期日，2返回所有星期一,3返回所有星期二，4返回所有星期三，5返回所有星期四，6返回所有星期五，7返回所有星期六
    * @param weekDay
    * @return
    */
   public static List<Calendar> getWeekdaysCalendar(int weekDay)throws Exception{
	   Calendar firstDay = getCalendarFromString(getMonthFirstDay());//本月第一天
	   List<Calendar> mondays = new ArrayList<Calendar>();
	   if(weekDay < firstDay.get(Calendar.DAY_OF_WEEK)){
		   firstDay.add(Calendar.DAY_OF_MONTH, 7);
		   firstDay.set(Calendar.DAY_OF_WEEK, weekDay);
	   }else if(weekDay >= firstDay.get(Calendar.DAY_OF_WEEK)){
		   firstDay.set(Calendar.DAY_OF_WEEK, weekDay);
	   }
	   
	   Calendar nextMonday = Calendar.getInstance();
	   nextMonday.set(Calendar.DAY_OF_MONTH, firstDay.get(Calendar.DAY_OF_MONTH));
	   while(nextMonday.get(Calendar.MONTH)==firstDay.get(Calendar.MONTH)){
		   Calendar day = Calendar.getInstance();
		   day.set(Calendar.DAY_OF_MONTH, nextMonday.get(Calendar.DAY_OF_MONTH));
		   mondays.add(day);
		   nextMonday.add(Calendar.DAY_OF_MONTH, 7);
	   }	
	   return mondays;
   }
   
   /**
    * 返回该日历日期上一周的周一
    * @param calendar
    * @return
    */
   public static Calendar getLastWeekMonday(Calendar calendar){
	   Calendar lastMonday = Calendar.getInstance();
	   lastMonday.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH)-1);
	   lastMonday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	   lastMonday.get(Calendar.DAY_OF_MONTH);
	   return lastMonday;
   }
   
   /**
    * 返回该日历日期上一周的周日
    * @param calendar
    * @return
    */
   public static Calendar getLastWeekSunday(Calendar calendar){
	   Calendar lastMonday = Calendar.getInstance();
	   lastMonday.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
	   if(lastMonday.get(Calendar.DAY_OF_WEEK) == 1){
		   lastMonday.add(Calendar.WEEK_OF_MONTH, -1);
	   }
	   lastMonday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	   return lastMonday;
   }
   
   /**
    * 返回给定日历参数所在周的周几
    * @param num 1返回周一，2返回周二    以此类推..  7返回周日
    * @author yinhao
    * @date 2013-4-18 上午11:18:18
    * @comment
    */
   public static Calendar getDayOfThisWeekByInteger(Calendar calendar,int num){
	   Calendar result = Calendar.getInstance();
	   result.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
	   if(1 == result.get(Calendar.DAY_OF_WEEK)){
		   result.add(Calendar.WEEK_OF_MONTH, -1);
	   }
	   if(7 == num){
		   result.add(Calendar.WEEK_OF_MONTH, 1);
	   }
	   int value = 0;
	   switch(num){
	   case 1:
		   value = Calendar.MONDAY;
		   break;
	   case 2:
		   value = Calendar.TUESDAY;
		   break;
	   case 3:
		   value = Calendar.WEDNESDAY;
		   break;
	   case 4:
		   value = Calendar.THURSDAY;
		   break;
	   case 5:
		   value = Calendar.FRIDAY;
		   break;
	   case 6:
		   value = Calendar.SATURDAY;
		   break;
	   case 7:
		   value = Calendar.SUNDAY;
		   break;
	   }
	   result.set(Calendar.DAY_OF_WEEK, value);
	   return result;
   }
   /**
    * 返回给定日历参数所在周的下周的周几
    * @param num  1返回下周周一，2返回下周周二    以此类推..  7返回周日
    * @return
    * @author yinhao
    * @date 2013-4-21 下午12:06:00
    * @comment
    */
   public static Calendar getDayOfNextWeekByInteger(Calendar calendar,int num){
	   Calendar result = Calendar.getInstance();
	   result.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
	   result.add(Calendar.WEEK_OF_MONTH, 1);
	   return getDayOfThisWeekByInteger(result, num);
   }
   
   /**
    * 返回给定日历参数所在周的上周的周几
    * @param calendar
    * @param num
    * @return
    * @author yinhao
    * @date 2013-5-10 上午10:55:20
    * @comment
    */
   public static Calendar getDayOfLastWeekByInteger(Calendar calendar,int num){
	   Calendar result = Calendar.getInstance();
	   result.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
	   result.add(Calendar.WEEK_OF_MONTH, -1);
	   return getDayOfThisWeekByInteger(result, num);
   }
   
   /**
    * 返回给定日历参数所在周的上周的周几
    * @param calendar
    * @param num
    * @return
    * @author yinhao
    * @date 2013-5-10 上午10:55:20
    * weerOrder 1  本周  0 上周
    * @comment
    */
   public static Calendar getDayWeekByInteger(Calendar calendar,int weekOrder,int num){

	   Calendar result = Calendar.getInstance();
	  // result.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
	   if(weekOrder==0){
		   result.set(Calendar.DAY_OF_MONTH-1, calendar.get(Calendar.DAY_OF_MONTH)-1);
		   result.add(Calendar.WEEK_OF_MONTH, -(1-weekOrder));
	   }else{
		   result.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		   result.add(Calendar.WEEK_OF_MONTH, -(1-weekOrder)); 
	   }
	  
	 //  result.add(Calendar.WEEK_OF_MONTH, -(1-weekOrder));
	   return getDayOfThisWeekByInteger(result, num);
   }
   
 
   /**
    * 返回上月的指定日期
    * @param num 1代表1号  2代表2号   以此类推
    * @return
    * @author yinhao
    * @date 2013-4-18 下午12:16:53
    * @comment
    */
   public static Calendar getDayOfLastMonthByInteger(int num){
	   Calendar result = Calendar.getInstance();
	   result.add(Calendar.MONTH, -1);
	   result.set(Calendar.DAY_OF_MONTH, num);
	   return result;
   }
   
   /**
    * 返回当月的指定日期
    * @param num 1代表1号  2代表2号   以此类推
    * @return
    * @author yinhao
    * @date 2013-4-18 下午12:16:53
    * @comment
    */
   public static Calendar getDayOfThisMonthByInteger(int num){
	   Calendar result = Calendar.getInstance();
	   result.set(Calendar.DAY_OF_MONTH, num);
	   return result;
   }
   
   /**
    * 返回下月的指定日期
    * @param calendar  1代表下个月1号  2代表下个月2号   以此类推
    * @param num
    * @return
    * @author yinhao
    * @date 2013-4-18 下午12:29:37
    * @comment
    */
   public static Calendar getDayOfNextMonthByInteger(int num){
	   Calendar result = Calendar.getInstance();
	   result.add(Calendar.MONTH, 1);
	   result.set(Calendar.DAY_OF_MONTH, num);
	   return result;
   }
   /**
    * 返回纯数字拼凑起来的当前时间，格式为yyyyMMddHHmiss
    * @return
    */
   public static String getRightNowForSimple(){
	   Date now = new Date();
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	   return sdf.format(now);
   }
   
   /**
    * 返回纯数字拼凑起来的当前时间，格式为yyyy-MM-dd HH24:mi:ss
    * @return
    */
   public static String getCurrentDateForDBFormate(){
	   Date now = new Date();
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   return sdf.format(now);
   }
   
   public static Date simpleDate(Date date) throws ParseException{
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   String dateStr = sdf.format(date);
	   return sdf.parse(dateStr);
   }
   
   public static String getStringFormDate(Date date)throws Exception{
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	   return sdf.format(date);
   }
   
   /**
    * 返回指定格式的日期字符串
    * @param date
    * @param pattern
    * @return
    * @throws Exception
    * @author yinhao
    * @date 2013-5-3 下午06:07:49
    * @comment
    */
   public static String getStringDateByPattern(Date date,String pattern)throws Exception{
	   SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	   return dateFormat.format(date);
   }
   
   
   /**
    * 返回指定的日期前后几天的日期
    * 示例 -1 即inputDate前一天
    *     +1 即inputDate后一天
    * @return
    */
   public static String getDateByCalBeforeAfterNum(String inputDate,String pattern,int num) {
	   try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			Calendar rightNow = Calendar.getInstance();
			rightNow.setTime(sdf.parse(inputDate));
			rightNow.add(Calendar.DAY_OF_MONTH, num);
			   //进行时间转换 
			 return sdf.format(rightNow.getTime()); 
			   
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
   
   /**    
    * 得到指定月的最后一天    
    *  @param dayStr
    *  传值必须是月份的1号如 2014-08-01
    * @return    
    */     
   public static String getSingleMonthEndDay(String dayStr,int mount) throws Exception {
	   if(dayStr.split("-").length==2){
		   dayStr+="-01";
	   }
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
       Calendar c = getCalendarFromString(dayStr);
       c.add(Calendar.MONTH,mount);
	   c.set(Calendar.DAY_OF_MONTH, 1);
       return getSpecifiedDayBefore(dateFormat.format(c.getTime()));
   }
   
   public static void main(String[] args) throws Exception{
	   // System.out.println(DateUtil.getCurrentFormate_Date());
	   
	   //processDate();
	   
	   //System.out.println(DateUtil.returnCurrentDateMode12());
	  /*System.out.println(processMonthDate("2014-01-01","M_0_7"));
	  System.out.println(processMonthDate("2014-01-21","M_0_11"));
	  System.out.println(processMonthDate("2014-01-01","M_0_28"));
	  System.out.println(processMonthDate("2014-01-01","M_0_END"));
	  System.out.println(processMonthDate("2014-01","M_1_8"));
	  System.out.println(processMonthDate("2014-01","M_1_18"));
	  System.out.println(processMonthDate("2014-01","M_1_END"));
	  System.out.println(processMonthDate("2014-01-01","M_1_END"));*/
	  
	   //测试周五的公式...
	   /*String beginStrDate = "2015-07-01";
	   String genFreqStartDay="W_0_1";
	   String genFreqEndDay="W_0_7";
	   String genOperDay="W_1_2";
	   for (int i = 0; i < 5; i++) { 
			
			ReportPlanDetailBean bean = new ReportPlanDetailBean();
			String startDate = DateUtil.processWeeklyDate(DateUtil.getDateByCalBeforeAfterNum(beginStrDate, "yyyy-mm-dd",(7*i)),genFreqStartDay);
			String endDate = DateUtil.processWeeklyDate(DateUtil.getDateByCalBeforeAfterNum(beginStrDate, "yyyy-mm-dd",(7*i)), genFreqEndDay);
			String operDate = DateUtil.processWeeklyDate(DateUtil.getDateByCalBeforeAfterNum(beginStrDate, "yyyy-mm-dd",(7*i)), genOperDay);
			System.out.println(startDate+"_"+endDate+"_"+operDate);
	   }*/
	   
	   String str = getCurrentDateForDBFormate();
	   System.out.println(str);
	   
	  //processDate();
	  
   }
   
   /**
    * 当前月所在的生成周期内容
    * M_0_1,M_0_END或者M_0_28 or M_1_END 
    * @throws Exception
    */
   public static String processMonthDate(String currDateStr,String monDayFormate) throws Exception {
	   String beforeMonthStr = getSingleMonthEndDay(currDateStr,0).substring(0,7);
	   //System.out.println("beforeMonthStr"+beforeMonthStr);
	   String tmpMonFormate[] = monDayFormate.split("_");
	   if(tmpMonFormate[1].equals("0")){
		 if(tmpMonFormate[2].equals("END")){
			 return getSingleMonthEndDay(currDateStr,0);
		 }else{
			 return beforeMonthStr+"-"+(tmpMonFormate[2].length()>1?tmpMonFormate[2]:"0"+tmpMonFormate[2]);
		 }
	   }else{
		   if(tmpMonFormate[2].equals("END")){
				 return getSingleMonthEndDay(currDateStr,1);
			 }else{
				 return currDateStr.substring(0,7)+"-"+(tmpMonFormate[2].length()>1?tmpMonFormate[2]:"0"+tmpMonFormate[2]);
			 } 
	   }
   }
   
   /**
    * 当前月所在的周期内容
    * @throws Exception
    */
   public static void processDate() throws Exception {
	   
	   System.out.println(DateUtil.returnCurrentDateMode12());
	   String startDateCurr = "W_0_1";
	   String endDateCurr   = "W_1_7";
	   String sDateCurr   = "W_1_7";
	   
	   String strl[] = DateUtil.returnCurrentWeekBetween("2015-07-01","yyyy-MM-dd");
	   for(int i=0;i<strl.length;i++){
		 System.out.println("--- $$ "+strl[i]);  
	   }
	   // 当周(周一的日期N N+(周几-1)) 算出时间
	   // MON W_1_7
	   
	   System.out.println( getDateByCalBeforeAfterNum(strl[0],"yyyy-MM-dd",(7-1)));
	   //上周(N-（8-周几)) 算出时间
	   System.out.println( getDateByCalBeforeAfterNum(strl[0],"yyyy-MM-dd",-(8-1)));
	   
	   String strl2[] = DateUtil.returnCurrentWeekBetween("2015-07-03","yyyy-MM-dd");
	   for(int i=0;i<strl2.length;i++){
		 System.out.println("---- " + strl2[i]);
	   }
	   
	   System.out.println(getDateByCalBeforeAfterNum("2015-07-01","yyyy-mm-dd",10));
	   
	  /* System.out.println( getDateByCalBeforeAfterNum("2015-01-01","yyyy-MM-dd",3));
	   System.out.println( getDateByCalBeforeAfterNum("2015-04-01","yyyy-MM-dd",-1));*/
	   
   }
   /**
    * 当前月所在的周期内容
    * @param beginDate 2014-08-01
    * @param W_0_1/W_1_7
    * @throws Exception
    */
   public static String processWeeklyDate(String beginDate,String dateFormate) throws Exception {
	   
	   //System.out.println(getCurrentFormate_Date());
	   String strl[] = DateUtil.returnCurrentWeekBetween(beginDate,"yyyy-MM-dd");
	  /* for(int i=0;i<strl.length;i++){
		 System.out.println(strl[i]);  
	   }*/
	   String splitArray[] = dateFormate.split("_");
	   if(splitArray[1].equals("0")){
		 //上周(N-（8-周几)) 算出时间
		  return getDateByCalBeforeAfterNum(strl[0],"yyyy-MM-dd",-(8-Integer.parseInt(splitArray[2])));
	   }else{
		   return getDateByCalBeforeAfterNum(strl[0],"yyyy-MM-dd",Integer.parseInt(splitArray[2])-1);
	   }
   }
   
   /**
    * 返回纯数字拼凑起来的当前时间，格式为yyyyMMddHHmiss
    * @return
    */
   public static String getRightNowForSimpleFormate(){
	   Date now = new Date();
	   SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	   return sdf.format(now);
   }
   
}
