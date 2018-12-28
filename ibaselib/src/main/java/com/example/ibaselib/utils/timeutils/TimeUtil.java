package com.example.ibaselib.utils.timeutils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.text.TextUtils;

public class TimeUtil {

	/**
	 * 将2015-10-18-16-47-30格式时间转换为 2015年10月18日 16:47
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String formatTime1(String dateTime) {
		if (!TextUtils.isEmpty(dateTime)) {
			String[] strs = dateTime.split("-");
			String newStr = strs[0] + "年" + strs[1] + "月" + strs[2] + "日    " + strs[3] + ":" + strs[4];
			return newStr;
		} else {
			return "";
		}
	}

	/**
	 * 将本地的时间转换为云端的时间
	 */
	public static String convertNativeTimeToCloudTime(String timeStr) {
		if (!TextUtils.isEmpty(timeStr)) {

			String[] tempStrs = timeStr.split("-");
			int year = Integer.parseInt(tempStrs[0]);
			int month = Integer.parseInt(tempStrs[1]);
			int day = Integer.parseInt(tempStrs[2]);
			String hour = "00";
			String minute = "00";
			String second = "00";
			if (tempStrs.length == 6) {
				hour = tempStrs[3];
				minute = tempStrs[4];
				second = tempStrs[5];
			}
			if (minute.length() == 1) {
				minute = "0" + minute;
			}
			if (second.length() == 1) {
				second = "0" + second;
			}
			String finalTime = year + "/" + month + "/" + day + " " + hour + ":" + minute + ":" + second;
			return finalTime;
		} else {
			return "";
		}
	}

	/**
	 * 将2015-10-18-16-47-30格式时间转换为 2015-10-18 16:47
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String formatTime2(String dateTime) {
		if (!TextUtils.isEmpty(dateTime)) {
			String[] strs = dateTime.split("-");
			String newStr = strs[0] + "-" + strs[1] + "-" + strs[2] + "  " + strs[3] + ":" + strs[4];
			return newStr;
		} else {
			return "";
		}
	}

	/**
	 * 将2015-10-18-16-47-30格式时间转换为 2015-10-18
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String formatTime3(String dateTime) {
		if (!TextUtils.isEmpty(dateTime)) {
			String[] strs = dateTime.split("-");
			String newStr = strs[0] + "-" + strs[1] + "-" + strs[2];
			return newStr;
		} else {
			return "";
		}
	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回短时间格式 yyyy-MM-dd
	 */
	public static Date getNowDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(8);
		Date currentTime_2 = formatter.parse(dateString, pos);
		return currentTime_2;
	}

	/**
	 * 获取现在时间
	 * 
	 * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	 */
	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 返回格式 yyyy-MM-dd
	 */
	public static String getStringDateShort() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String date = formatter.format(currentTime);
		return date;
	}

	/**
	 * 将日期格式转为毫秒数
	 * 
	 * @param in
	 *            格式为 2014-09-30
	 * @return 返回格式为 1345185923140
	 */
	public static long dateToLong(String in) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse(in);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 将日期格式转为毫秒数
	 * 
	 * @param in
	 *            格式为 2014-09-30-12-00-00
	 * @return 返回格式为 1345185923140
	 */
	public static long dateToLongNew(String in) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		try {
			Date date = format.parse(in);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 将日期格式转为毫秒数
	 * 
	 * @param in
	 *            格式为 2014年9月30日
	 * @return 返回格式为 1345185923140
	 */
	public static long dateToLong3(String in) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年M月dd日");
		try {
			Date date = format.parse(in);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 将日期格式转为毫秒数
	 * 
	 * @param in
	 *            格式为 2014-09-30 09:50
	 * @return 返回格式为 1345185923140
	 */
	public static long dateToLong1(String in) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = format.parse(in);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 将日期格式转为毫秒数
	 * 
	 * @param in
	 *            格式为 2014年09月30日 09:50
	 * @return 返回格式为 1345185923140
	 */
	public static long dateToLong2(String in) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		try {
			Date date = format.parse(in);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 将日期格式转为毫秒数
	 * 
	 * @param in
	 *            格式为 2014-09-30 09:50:30
	 * @return 返回格式为 1345185923140
	 */
	public static long dateToLong4(String in) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = format.parse(in);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return cal.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 将毫秒数转为日期
	 * 
	 * @param millis
	 *            格式为1345185923140L
	 * @return 返回格式为 年-月-日 时：分：秒
	 */
	public static String longToDate(long millis) {
		Date date = new Date(millis);
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String sb = format.format(gc.getTime());
		System.out.println(sb);
		return sb;
	}

	/**
	 * 将long数转为日期
	 * 
	 * @param millis
	 *            格式为1345185923140
	 * @return 返回格式为 年-月-日 时：分
	 */
	public static String longToDate1(long millis) {
		Date date = new Date(millis);
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String sb = format.format(gc.getTime());
		System.out.println(sb);
		return sb;
	}

	/**
	 * 将毫秒数转为日期
	 * 
	 * @param millis
	 *            格式为1345185923140L
	 * @return 返回格式为 年-月-日
	 */
	public static String longToDate2(long millis) {
		Date date = new Date(millis);
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String sb = format.format(gc.getTime());
		System.out.println(sb);
		return sb;
	}

	/**
	 * 将毫秒数转为日期
	 * 
	 * @param millis
	 *            格式为1345185923140L
	 * @return 返回格式为 2014年09月07日 10:30
	 */
	public static String longToDate3(long millis) {
		Date date = new Date(millis);
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		String sb = format.format(gc.getTime());
		System.out.println(sb);
		return sb;
	}

	/**
	 * 将毫秒数转为日期
	 * 
	 * @param millis
	 *            格式为1345185923140L
	 * @return 返回格式为 2014年09月07日
	 */
	public static String longToDate4(long millis) {
		Date date = new Date(millis);
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		String sb = format.format(gc.getTime());
		System.out.println(sb);
		return sb;
	}

	/**
	 * 将毫秒数转为日期
	 * 
	 * @param millis
	 *            格式为1345185923140L
	 * @return 返回格式为 2014-09-07-10-30-10
	 */
	public static String longToDate5(long millis) {
		Date date = new Date(millis);
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String sb = format.format(gc.getTime());
		System.out.println(sb);
		return sb;
	}

	/**
	 * 将毫秒数转为日期
	 * 
	 * @param millis
	 *            格式为1345185923140L
	 * @return 返回格式为 20140907103010
	 */
	public static String longToDate6(long millis) {
		Date date = new Date(millis);
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String sb = format.format(gc.getTime());
		System.out.println(sb);
		return sb;
	}

	public static String longToDate7(long millis) {
		Date date = new Date(millis);
		Calendar gc = Calendar.getInstance();
		gc.setTime(date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String sb = format.format(gc.getTime());
		System.out.println(sb);
		return sb;
	}

	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		System.out.println(m.matches() + "---");

		return m.matches();
	}

//	public static String getCalendarByInintData(String initDateTime) {
//		if (null != initDateTime && initDateTime.length() > 0) {
//			// 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
//			String date = DateTimePickDialogUtil.spliteString(initDateTime, "日", "index", "front");
//			String time = DateTimePickDialogUtil.spliteString(initDateTime, "日", "index", "back");
//
//			String yearStr = DateTimePickDialogUtil.spliteString(date, "年", "index", "front");
//			String monthAndDay = DateTimePickDialogUtil.spliteString(date, "年", "index", "back");
//
//			String monthStr = DateTimePickDialogUtil.spliteString(monthAndDay, "月", "index", "front");
//			String dayStr = DateTimePickDialogUtil.spliteString(monthAndDay, "月", "index", "back");
//
//			String hourStr = DateTimePickDialogUtil.spliteString(time, ":", "index", "front");
//			String minuteStr = DateTimePickDialogUtil.spliteString(time, ":", "index", "back");
//
//			int currentYear = Integer.valueOf(yearStr.trim()).intValue();
//			int currentMonth = Integer.valueOf(monthStr.trim()).intValue();
//			int currentDay = Integer.valueOf(dayStr.trim()).intValue();
//			int currentHour = Integer.valueOf(hourStr.trim()).intValue();
//			int currentMinute = Integer.valueOf(minuteStr.trim()).intValue();
//
//			String addMonth = String.valueOf(currentMonth);
//			if (currentMonth < 10) {
//				addMonth = "0" + addMonth;
//			}
//
//			String addDay = String.valueOf(currentDay);
//			if (currentDay < 10) {
//				addDay = "0" + addDay;
//			}
//
//			String addHour = String.valueOf(currentHour);
//			if (currentHour < 10) {
//				addHour = "0" + addHour;
//			}
//
//			String addMinute = String.valueOf(currentMinute);
//			if (currentMinute < 10) {
//				addMinute = "0" + addMinute;
//			}
//
//			String returnStr = currentYear + "-" + addMonth + "-" + addDay + "-" + addHour + "-" + addMinute + "-" + "00";
//			return returnStr;
//		} else {
//			return "";
//		}
//	}

	public static String formatTime(String dateTimeStr) {
		if ((null != dateTimeStr) && (dateTimeStr.length() > 0)) {
			String[] strs = dateTimeStr.split("-");
			String newStr = strs[3] + ":" + strs[4];
			return newStr;
		} else {
			return null;
		}
	}
}
