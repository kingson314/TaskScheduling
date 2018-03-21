package com.app;

import java.util.Date;

import module.datetype.HolidayDao;
import module.ftp.FtpDao;
import module.ftp.FtpSite;


import com.log.Log;

import common.util.conver.UtilConver;
import consts.Const;

/**
 * @author Shen Wei
 * @description Parse any input string which contains variables like:
 *              %type.value%. Bugs checking has already been done, please feel
 *              free to use it!
 */
public class Parser {

	// ftp格式化
	public static FtpSite parseFtpSite(String ftpPath) {
		if (ftpPath != null) {
			int startIndex, endIndex;
			if (!ftpPath.startsWith("ftp://")) {
				Log.logInfo("FTP站点模板解析错误,FTP协议头格式错误!");
				return null;
			}
			startIndex = ftpPath.indexOf("%");
			if (!(startIndex == ftpPath.indexOf("//") + 2)) {
				Log.logInfo("FTP站点模板解析错误,找不到FTP站点模板!");
				return null;
			}
			ftpPath = ftpPath.substring(startIndex + 1);
			if ((endIndex = ftpPath.indexOf("%")) == -1) {
				Log.logInfo("FTP站点模板解析错误,符号 % 需要在FTP站点模板中成对出现!");
				return null;
			}
			FtpSite ftpSite = getFtpSite(ftpPath.substring(0, endIndex));
			if (ftpSite == null)
				return null;

			ftpPath = ftpSite.getFtpFolder() + ftpPath.substring(endIndex + 1);
			// 申万 FTP client.changeDirectory(ftpSite.getFtpFolder());
			// foler="//" 会报501 syntax error in parameters or arguments
			// 参数错误导致语法错误
			ftpPath = ftpPath.trim();
			if (ftpPath.endsWith("//") || ftpPath.endsWith("//") || ftpPath.endsWith("//"))
				ftpPath = ftpPath.substring(0, ftpPath.length() - 2) + "/";
			ftpSite.setFtpFolder(ftpPath);
			return ftpSite;
		} else
			return null;
	}

	// 日期字符串格式化
	public static String parse(String path, Date date) {
		String result = path;
		if (path != null) {
			StringBuilder sb = new StringBuilder();
			int startIndex, endIndex;
			while ((startIndex = path.indexOf("%")) != -1) {
				if (startIndex > 0)
					sb.append(path.substring(0, startIndex));
				path = path.substring(startIndex + 1);
				if ((endIndex = path.indexOf("%")) == -1) {
					Log.logInfo("字符串模板解析错误,符号 % 需要在字符串模板中成对出现!");
					return null;
				}
				// 使用复品，以控制类型date+1d等情况 DateWraper改变内存值
				Date tmpDate = (Date) date.clone();
				sb.append(parseProtocol(path.substring(0, endIndex), tmpDate));
				path = path.substring(endIndex + 1);
			}
			sb.append(path);
			result = sb.toString();
		} else
			return null;
		return result;
	}

	// 格式化协议
	private static String parseProtocol(String path, Date date) {
		String ret;
		int index;
		if ((index = path.indexOf(":")) == -1) {
			Log.logInfo("字符串模板解析错误, 符号 : 在字符串模板中未出现!");
			return null;
		}
		String protocol = path.substring(0, index);
		if (protocol.startsWith(Const.PROTOCOL_DATE)) {
			int offset = 0, type = -1;
			if (protocol.equals(Const.PROTOCOL_DATE))
				offset = 0;
			else {
				try {
					String offsetStr = protocol.substring(Const.PROTOCOL_DATE.length(), protocol.length() - 1);
					String typeStr = protocol.substring(protocol.length() - 1, protocol.length());
					if (offsetStr.startsWith("+"))
						offset = Integer.parseInt(offsetStr.substring(1));
					else if (offsetStr.startsWith("-"))
						offset = -Integer.parseInt(offsetStr.substring(1));
					else {
						Log.logInfo("字符串模板解析错误, 未知日期模板偏移量类型!");
						return null;
					}
					if (typeStr.equals("D") || typeStr.equals("d"))
						type = Const.OFFSET_DAY;
					else if (typeStr.equals("T") || typeStr.equals("t"))
						type = Const.OFFSET_TRADING_DAY;
					else {
						Log.logInfo("字符串模板解析错误, 未知日期模板偏移量类型!");
						return null;
					}
				} catch (Exception e) {
					Log.logInfo("字符串模板解析错误, 日期模板偏移量格式错误!");
					return null;
				}
			}
			ret = parseDate(path.substring(index + 1), offset, type, date);
		} else if (protocol.equals(Const.PROTOCOL_FTP_NAME)) {
			ret = parseFtpName(path.substring(index + 1));
		} else {
			Log.logInfo("字符串模板解析错误, 字符串解析协议未知!");
			ret = null;
		}
		return ret;
	}

	// 获取ftp格式化信息
	private static FtpSite getFtpSite(String ftpStr) {
		FtpSite ftpSite;
		int index;
		if ((index = ftpStr.indexOf(":")) == -1) {
			Log.logInfo("FTP站点模板解析错误, 符号 : 在FTP站点模板中未出现!");
			return null;
		}
		String protocol = ftpStr.substring(0, index);
		if (protocol.equals(Const.PROTOCOL_FTP_NAME))
			ftpSite = getFtpSiteByName(ftpStr.substring(index + 1));
		else {
			Log.logInfo("FTP站点模板解析错误, FTP站点解析协议未知!");
			ftpSite = null;
		}
		return ftpSite;
	}

	// 根据标志获取ftp格式化信息
	private static FtpSite getFtpSiteByName(String ftpNameStr) {
		FtpSite ftpSite = FtpDao.getInstance().getFtpSiteByName(ftpNameStr);
		if (ftpSite == null) {
			Log.logInfo("FTP站点模板解析错误, 不存在的FTP站点名称!");
		}
		return ftpSite;
	}

	// 格式化日期格式+-N

	@SuppressWarnings("deprecation")
	private static String parseDate(String format, int offset, int type, Date inDate) {
		Date tmpDate = null;
		// 输入的日期，加上当前的时间作为动态的时间
		try {
			String nowTime = UtilConver.dateToStr(Const.fm_HHmmss);
			tmpDate = UtilConver.strToDate(UtilConver.dateToStr(inDate, Const.fm_yyyyMMdd) + " " + nowTime, Const.fm_yyyyMMdd_HHmmss);
		} catch (Exception e) {
			// 这种方式不能格式化毫秒
			Date nowDate = new Date();
			inDate.setHours(nowDate.getHours());
			inDate.setMinutes(nowDate.getMinutes());
			inDate.setSeconds(nowDate.getSeconds());
			tmpDate = (Date) inDate.clone();
		}
		switch (type) {
		case Const.OFFSET_DAY:
			tmpDate.setDate(inDate.getDate() + offset);
			break;
		case Const.OFFSET_TRADING_DAY:
			int count = Math.abs(offset);
			int step = offset > 0 ? 1 : -1;
			while (count > 0) {
				tmpDate.setDate(tmpDate.getDate() + step);
				// 只支持沪深交易日
				if (!HolidayDao.mapHoliday.get("CHEXCH").contains(UtilConver.dateToStr(tmpDate, Const.fm_yyyyMMdd))) {
					--count;
				}
			}
			break;
		default:
			// type == -1 即 %date:yyyyMMdd%
		}

		String dateStr = UtilConver.dateToStr(tmpDate, format);
		// 十六进制格式的日期用[]括起来
		StringBuilder sb = new StringBuilder();
		int startIndex, endIndex;
		while ((startIndex = dateStr.indexOf("[")) != -1) {
			if (startIndex > 0)
				sb.append(dateStr.substring(0, startIndex));
			dateStr = dateStr.substring(startIndex + 1);
			if ((endIndex = dateStr.indexOf("]")) == -1) {
				Log.logInfo("字符串模板解析错误, 符号 [ 与 ] 需要在字符串模板中成对出现!");
				return null;
			}
			String StrD = dateStr.substring(0, endIndex);
			Long longD = Long.parseLong(StrD);
			String StrH = Long.toHexString(longD).toUpperCase();
			sb.append(StrH);
			dateStr = dateStr.substring(endIndex + 1);
		}

		sb.append(dateStr);
		return sb.toString();
	}

	// PROTOCOL_FTP_NAME
	private static String parseFtpName(String ftpName) {
		return ftpName + "/";
	}

	// 自动补全/
	// public static String makeSlash(String path) {
	// if (path != null) {
	// StringBuilder sb = new StringBuilder();
	// int index;
	// while ((index = path.indexOf("/")) != -1) {
	// sb.append(path.substring(0, index + 1));
	// sb.append("/");
	// path = path.substring(index + 1);
	// }
	// sb.append(path);
	// return sb.toString();
	// } else
	// return null;
	// }

	// 自动整理路径
	public static String removeSlash(String path) {
		if (path != null) {
			StringBuilder sb = new StringBuilder();
			int index;
			while ((index = path.indexOf("//")) != -1) {
				sb.append(path.substring(0, index + 1));
				path = path.substring(index + 2);
			}
			sb.append(path);
			return sb.toString();
		} else
			return null;
	}
}
