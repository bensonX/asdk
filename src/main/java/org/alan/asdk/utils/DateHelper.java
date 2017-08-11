package org.alan.asdk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期工具类
 * @author Chow
 *
 * time: 2014年8月11日 下午2:35:36
 */
@Component
public class DateHelper {
	
	private static Logger logger = LoggerFactory.getLogger(FileHelper.class);
	/**
	 * 日期单位--日
	 */
	public final static int DATE = Calendar.DATE;
	/**
	 * 日期单位--月
	 */
	public final static int MONTH = Calendar.MONTH;
	/**
	 * 日期单位--年
	 */
	public final static int YEAR = Calendar.YEAR;
	
	/**
	 * 将日期转换为String格式
	 * @param date 要转换的日期
	 * @param format 格式
	 * @return
	 */
	public String format(Date date, String format){
		if (format==null) {
			format = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	/**
	 * 将String转换为Date格式
	 * @param time
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public Date parse(String time, String format){
		if (format==null) {
			format = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(time.trim());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("日期转换出错", e);
		}
		return null;
	}
	/**
	 * 增加或减少时间
	 * @param date 时间
	 * @param days 数量
	 * @param amount 单位
	 * @return
	 */
	public Date add(String date,int days,int amount,String format){
		return add(parse(date,format),days,amount);
	}
	public Date add(String date, int days , int amount){
		return add(date,days,amount,"yyyy-MM-dd");
	}
	/**
	 * 增加或减少时间
	 * @param date 时间
	 * @param days 数量
	 * @param amount 单位
	 * @return
	 */
	public Date add(Date date,int days,int amount){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(amount, days);
		return cal.getTime();
	}
	/**
	 * 计算两日之间的差值
	 * @param date1
	 * @param date2
	 * @param amount 差值单位(年/月/日)
	 * @return
	 */
	public int difference(Date date1 , Date date2,int amount){
		Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        int result = 0;
        c1.setTime(date1);
		c2.setTime(date2);
		result = c2.get(amount) - c1.get(amount);
		return result == 0 ? 1 : Math.abs(result);
	}
	public int daysBetween(Date smdate,Date bdate){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			smdate=sdf.parse(sdf.format(smdate));
			bdate=sdf.parse(sdf.format(bdate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days=(time2-time1)/(1000*3600*24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	public int daysBetween(String start , String end , String format){
		return daysBetween(parse(start,format),parse(end,format));
	}
	public int daysBetween(String start , String end){
		return daysBetween(start,end,"yyyy-MM-dd");
	}


	/**
	 * 计算两个日期的差值
	 * @param date1 第一个日期
	 * @param date2 第二个日期
	 * @param amount 差值单位(年/月/日)
	 * @param format 日期格式
	 * @return
	 */
	public int difference(String date1 , String date2 ,int amount, String format){
		if(format==null){
			format = "yyyy-MM-dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        int result = 0;
        try {
			c1.setTime(sdf.parse(date1));
			c2.setTime(sdf.parse(date2));
			if (amount==Calendar.DATE) {
				// 设置时间为0时
				c1.set(Calendar.HOUR_OF_DAY, 0);
				c1.set(Calendar.MINUTE, 0);
				c1.set(Calendar.SECOND, 0);
				c2.set(Calendar.HOUR_OF_DAY, 0);
				c2.set(Calendar.MINUTE, 0);
				c2.set(Calendar.SECOND, 0);
				result = ((int) (c2.getTime().getTime() / 1000) - (int) (c1.getTime().getTime() / 1000)) / 3600 / 24;
				return  Math.abs(result);   
			}
			result = c2.get(amount) - c1.get(amount);
			return result == 0 ? 1 : Math.abs(result);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
	}
	/**
	 * 获取时间是处于该年中的哪个周
	 * @param date 时间
	 * @return
	 */
	public int getWeekOfYear(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}
	/**
	 * 获取时间是处于该月中的哪个周
	 * @param date 时间
	 * @return
	 */
	public int getWeekOfMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.WEEK_OF_MONTH);
	}
	/**
	 * 获取该时间所在周的星期一是哪一天
	 * @return
	 */
	public Date getMondayOfWeek(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int _date = cal.get(Calendar.DATE)+1;
		int day = cal.get(Calendar.DAY_OF_WEEK)-1;
		cal.set(Calendar.DATE, _date-day);
		return cal.getTime();
	}
	/**
	 * 获取该时间所在周的星期日是哪一天
	 * @return
	 */
	public Date getSundayOfWeek(Date date){
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(date);
		int _date = cal.get(Calendar.DATE)+7;
		int day = cal.get(Calendar.DAY_OF_WEEK)-1;
		cal.set(Calendar.DATE, _date-day);
		return cal.getTime();
	}

	public Date parse(String time) {
		return parse(time , null);
	}

	public String format(Date time) {
		return  format(time,null);
	}
}
