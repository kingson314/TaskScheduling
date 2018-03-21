package TestUnit.html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

public class Test {
	// public static void main(String[] args) throws ParserException,
	// MalformedURLException, IOException {
	// String url = "http://www.dailyfx.com.hk/calendar/index.html";
	// Parser parser = new Parser((URLConnection) (new URL(url))
	// .openConnection());
	//
	// for (NodeIterator i = parser.elements(); i.hasMoreNodes();) {
	// Node node = i.nextNode();
	// System.out.println(node.toHtml());
	// System.err.println("mhj------------------>");
	// }
	//
	// }
	public static Map<String, String> parseList(String url) {
		Map<String, String> rlt = new LinkedHashMap<String, String>();
		// NodeFilter filter = new CssSelectorNodeFilter(".className tr");
		// filter = new AndFilter(filter, new NotFilter(new HasChildFilter(
		// new CssSelectorNodeFilter("table"))));
		NodeFilter filter = new TagNameFilter("div_currency");
		Parser parser;
		try {
			parser = new Parser(url);
			parser.setEncoding("GB2312");
			NodeList list = parser.extractAllNodesThatMatch(filter);
			for (int i = 0; i < list.size(); i++) {
				Node tr = list.elementAt(i);
				parser = new Parser(tr.toHtml());
				// NodeList tds = parser
				// .extractAllNodesThatMatch(new CssSelectorNodeFilter(
				// "table"));

				String key = list.elementAt(0).toPlainTextString();
				String value = list.elementAt(1).toPlainTextString();
				System.out.println("[" + key.trim() + "]" + "   " + value.trim());
				rlt.put(key, value);
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return rlt;
	}

	// // 判断是否为HH:mm类型的字符串
	// private static boolean isTime(String str) {
	// boolean rs = false;
	// try {
	// if (str.indexOf(":") < 0)
	// return false;
	// Fun.strToDate(str, "HH:mm");
	// rs = true;
	// } catch (Exception e) {
	// }
	// return rs;
	// }

	// // 判断是否为yyyy-MM-dd格式
	// private static boolean isDate(String arg) {
	// boolean rs = false;
	// try {
	// if (arg.indexOf("-") < 0)
	// return false;
	// Fun.strToDate(arg, "yyyy-MM-dd");
	// rs = true;
	// } catch (Exception e) {
	// }
	// return rs;
	// }

	// 获取财经事件
	// public static void getEconomicEvent(String url, String date) {
	// try {
	// boolean isEvent = false;// 由于财经事件与假期使用了同样的class,故在此要分段截取
	// String[] arrExceptStr = new String[] { "&nbsp;" };
	// String[] arrIngoreStr = new String[] { "时间", "国家", "地点", "事件", "重要性",
	// "[]" };
	// String importance = "低中高";
	// boolean ingored = false;
	// Parser parser = new Parser(url);
	// parser.setEncoding("GB2312");
	//
	// NodeFilter[] arrNodeFilter = new NodeFilter[4];
	// arrNodeFilter[0] = new HasAttributeFilter("class", "w_60");
	// arrNodeFilter[1] = new HasAttributeFilter("class", "w_65");
	// arrNodeFilter[2] = new HasAttributeFilter("class", "w_525 text_l");
	// arrNodeFilter[3] = new HasAttributeFilter("class", "title");
	//
	// OrFilter orFilter = new OrFilter(arrNodeFilter);
	// NodeList nodeList = parser.parse(orFilter);
	// Node[] nodes = nodeList.toNodeArray();
	// String line = null;
	//		 
	// // 每次换时间时出现新一批数据
	// EconomicNationalDebt economicEvent = new EconomicNationalDebt();
	// List<EconomicNationalDebt> listEconomicEvent = new
	// ArrayList<EconomicNationalDebt>();
	// int newDataLineCount = 0;
	// for (int i = 0; i < nodes.length; i++) {
	// line = nodes[i].toPlainTextString();
	// if (line != null && !"".equals(line.trim())) {
	// // 过滤掉不需要的字符
	// for (String exceptStr : arrExceptStr) {
	// line = line.replace(exceptStr, "");
	// }
	// for (String ingoreStr : arrIngoreStr) {
	// ingored = false;
	// if (ingoreStr.equals(line.trim())) {
	// ingored = true;
	// break;
	// }
	// }
	// System.out.println(line);
	// if (ingored)
	// continue;
	// if (line.equals("财经大事")) {
	// isEvent = true;
	// continue;
	// }
	// if (!isEvent)
	// continue;
	//
	// if ((newDataLineCount) % 5 == 0) {
	// economicEvent = new EconomicNationalDebt();
	// economicEvent.setOccurDate(date);
	// economicEvent.setOccurTime(line.substring(5));
	// } else if ((newDataLineCount) % 5 == 1) {
	// economicEvent.setCountry(line);
	// } else if ((newDataLineCount) % 5 == 2) {
	// economicEvent.setSite(line);
	// } else if ((newDataLineCount) % 5 == 3) {
	// // 有可能没有重要性这一列
	// if (importance.indexOf(line) >= 0) {
	// economicEvent.setImportance(line);
	// } else {
	// economicEvent.setEvent(line);
	// listEconomicEvent.add(economicEvent);
	// newDataLineCount = 0;
	// continue;
	// }
	// } else if ((newDataLineCount) % 5 == 4) {
	// economicEvent.setEvent(line);
	// listEconomicEvent.add(economicEvent);
	// }
	// newDataLineCount += 1;
	// }
	// }
	// System.out.println(Json.objToJson(listEconomicEvent));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // 获取汇通财经事件
	// public static void getEconomicNationalDebt(String url, String date) {
	// try {
	// boolean isNationalDebt = false;// 由于财经事件与假期使用了同样的class,故在此要分段截取
	// String[] arrExceptStr = new String[] { "&nbsp;" };
	// String[] arrIngoreStr = new String[] { "时间", "国家", "地点", "事件", "重要性",
	// "[]" };
	// String importance = "低中高";
	// boolean ingored = false;
	// Parser parser = new Parser(url);
	// parser.setEncoding("GB2312");
	//
	// NodeFilter[] arrNodeFilter = new NodeFilter[4];
	// arrNodeFilter[0] = new HasAttributeFilter("class", "w_60");
	// arrNodeFilter[1] = new HasAttributeFilter("class", "w_65");
	// arrNodeFilter[2] = new HasAttributeFilter("class", "w_525 text_l");
	// arrNodeFilter[3] = new HasAttributeFilter("class", "title");
	//
	// OrFilter orFilter = new OrFilter(arrNodeFilter);
	// NodeList nodeList = parser.parse(orFilter);
	// Node[] nodes = nodeList.toNodeArray();
	// String line = null;
	// // 每次换时间时出现新一批数据
	// EconomicNationalDebt economicNationalDebt = new EconomicNationalDebt();
	// List<EconomicNationalDebt> listEconomicNationalDebt = new
	// ArrayList<EconomicNationalDebt>();
	// int newDataLineCount = 0;
	// for (int i = 0; i < nodes.length; i++) {
	// line = nodes[i].toPlainTextString();
	// if (line != null && !"".equals(line.trim())) {
	// // 过滤掉不需要的字符
	// for (String exceptStr : arrExceptStr) {
	// line = line.replace(exceptStr, "");
	// }
	// for (String ingoreStr : arrIngoreStr) {
	// ingored = false;
	// if (ingoreStr.equals(line.trim())) {
	// ingored = true;
	// break;
	// }
	// }
	// System.out.println(line);
	// if (ingored)
	// continue;
	// if (line.equals("国债发行预告")) {
	// isNationalDebt = true;
	// continue;
	// }
	// if (!isNationalDebt)
	// continue;
	//
	// if ((newDataLineCount) % 5 == 0) {
	// economicNationalDebt = new EconomicNationalDebt();
	// economicNationalDebt.setOccurDate(date);
	// economicNationalDebt.setOccurTime(line.substring(5));
	// } else if ((newDataLineCount) % 5 == 1) {
	// economicNationalDebt.setCountry(line);
	// } else if ((newDataLineCount) % 5 == 2) {
	// economicNationalDebt.setSite(line);
	// } else if ((newDataLineCount) % 5 == 3) {
	// // 有可能没有重要性这一列
	// if (importance.indexOf(line) >= 0) {
	// economicNationalDebt.setImportance(line);
	// } else {
	// economicNationalDebt.setEvent(line);
	// listEconomicNationalDebt.add(economicNationalDebt);
	// newDataLineCount = 0;
	// continue;
	// }
	// } else if ((newDataLineCount) % 5 == 4) {
	// economicNationalDebt.setEvent(line);
	// listEconomicNationalDebt.add(economicNationalDebt);
	// }
	// newDataLineCount += 1;
	// }
	// }
	// System.out.println(Json.objToJson(listEconomicNationalDebt));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // 获取汇通假期预告
	// public static void getEconomicHoliday(String url, String date) {
	// try {
	// boolean isHoliday = false;// 由于财经事件与假期使用了同样的class,故在此要分段截取
	// String[] arrExceptStr = new String[] { "&nbsp;" };
	// String[] arrIngoreStr = new String[] { "时间", "国家", "地点", "事件", "重要性",
	// "[]" };
	// boolean ingored = false;
	// Parser parser = new Parser(url);
	// parser.setEncoding("GB2312");
	//
	// NodeFilter[] arrNodeFilter = new NodeFilter[4];
	// arrNodeFilter[0] = new HasAttributeFilter("class", "w_60");
	// arrNodeFilter[1] = new HasAttributeFilter("class", "w_65");
	// arrNodeFilter[2] = new HasAttributeFilter("class", "w_525 text_l");
	// arrNodeFilter[3] = new HasAttributeFilter("class", "title");
	//
	// OrFilter orFilter = new OrFilter(arrNodeFilter);
	// NodeList nodeList = parser.parse(orFilter);
	// Node[] nodes = nodeList.toNodeArray();
	// String line = null;
	// // 每次换时间时出现新一批数据
	// EconomicHoliday economicHoliday = new EconomicHoliday();
	// List<EconomicHoliday> listEconomicHoliday = new
	// ArrayList<EconomicHoliday>();
	// int newDataLineCount = 0;
	// for (int i = 0; i < nodes.length; i++) {
	// line = nodes[i].toPlainTextString();
	// if (line != null && !"".equals(line.trim())) {
	// // 过滤掉不需要的字符
	// for (String exceptStr : arrExceptStr) {
	// line = line.replace(exceptStr, "");
	// }
	// for (String ingoreStr : arrIngoreStr) {
	// ingored = false;
	// if (ingoreStr.equals(line.trim())) {
	// ingored = true;
	// break;
	// }
	// }
	// if (ingored)
	// continue;
	// System.out.println(line);
	// if (line.equals("假期预告")) {
	// isHoliday = true;
	// continue;
	// }
	// if (!isHoliday)
	// continue;
	// if (line.equals("财经大事")) {
	// break;
	// }
	//
	// if ((newDataLineCount) % 4 == 0) {
	// economicHoliday = new EconomicHoliday();
	// economicHoliday.setOccurDate(date);
	// economicHoliday.setOccurTime(line.substring(5));
	// } else if ((newDataLineCount) % 4 == 1) {
	// economicHoliday.setCountry(line);
	// } else if ((newDataLineCount) % 4 == 2) {
	// economicHoliday.setSite(line);
	// } else if ((newDataLineCount) % 4 == 3) {
	// economicHoliday.setEvent(line);
	// listEconomicHoliday.add(economicHoliday);
	// }
	// newDataLineCount += 1;
	// }
	// }
	// System.out.println(Json.objToJson(listEconomicHoliday));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // 获取汇通各国经济数据指标
	// public static void getEconomicIndicator(String url) {
	// try {
	// String[] arrExceptStr = new String[] { "&nbsp;" };
	// Parser parser = new Parser(url);
	// parser.setEncoding("GB2312");
	// NodeFilter[] nodeFilter = new NodeFilter[1];
	// nodeFilter[0] = new TagNameFilter("a");
	// // nodeFilter[1] = new HasAttributeFilter("class", "");
	// // nodeFilter[0]=new NodeClassFilter(Span.class);
	//
	// OrFilter andFilter = new OrFilter(nodeFilter);
	// // NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
	// NodeList nodeList = parser.parse(andFilter);
	// Node[] nodes = nodeList.toNodeArray();
	// String line = null;
	// // 每次换时间时出现新一批数据
	// List<EconomicData> listEconomicData = new ArrayList<EconomicData>();
	// for (int i = 0; i < nodes.length; i++) {
	// line = nodes[i].toPlainTextString();
	// if (line != null && !"".equals(line.trim())) {
	// // 过滤掉不需要的字符
	// for (String exceptStr : arrExceptStr) {
	// line = line.replace(exceptStr, "");
	// }
	// System.out.println(line);
	// }
	// }
	// System.out.println(Json.objToJson(listEconomicData));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // 获取汇通经济数据
	// public static void getEconomicData(String url, String date) {
	// try {
	// String[] arrExceptStr = new String[] { "&nbsp;" };
	// Parser parser = new Parser(url);
	// parser.setEncoding("GB2312");
	// NodeFilter[] nodeFilter = new NodeFilter[1];
	// nodeFilter[0] = new TagNameFilter("td");
	// // nodeFilter[0]=new NodeClassFilter(Span.class);
	// // nodeFilter[1] = new HasAttributeFilter("class", "TableBlock");
	// AndFilter andFilter = new AndFilter(nodeFilter);
	// // NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
	// NodeList nodeList = parser.parse(andFilter);
	// Node[] nodes = nodeList.toNodeArray();
	// String line = null;
	// String time = "";
	// String country = "";
	// // 每次换时间时出现新一批数据
	// EconomicData economicData = new EconomicData();
	// List<EconomicData> listEconomicData = new ArrayList<EconomicData>();
	// int newDataLineCount = 0;
	// for (int i = 0; i < nodes.length; i++) {
	// line = nodes[i].toPlainTextString();
	// if (line != null && !"".equals(line.trim())) {
	// // 过滤掉不需要的字符
	// for (String exceptStr : arrExceptStr) {
	// line = line.replace(exceptStr, "");
	// // System.out.println(line);
	// }
	//
	// // 判断是否以HH:mm时间开头
	// if (isTime(line)) {
	// newDataLineCount = 0;
	// System.out.println(line);
	// time = line;
	// country = "";
	// // System.out.println(time+" "+ country);
	// } else {
	// newDataLineCount += 1;
	// if (newDataLineCount == 1) {
	// country = line;
	// } else if (newDataLineCount == 2) {
	// continue;
	// } else if ((newDataLineCount - 3) % 5 == 0) {
	// if ("---".equals(line)) {
	// newDataLineCount -= 1;
	// continue;
	// }
	// economicData = new EconomicData();
	// economicData.setPublishDate(date);
	// economicData.setPublishTime(time);
	// economicData.setCountry(country);
	// economicData.setIndicator(line);
	// } else if ((newDataLineCount - 3) % 5 == 1) {
	// economicData.setImportance(line);
	// } else if ((newDataLineCount - 3) % 5 == 2) {
	// economicData.setPreviousValue(line);
	// } else if ((newDataLineCount - 3) % 5 == 3) {
	// economicData.setPredictedValue(line);
	// } else if ((newDataLineCount - 3) % 5 == 4) {
	// economicData.setPublishedValue(line);
	// listEconomicData.add(economicData);
	// }
	// }
	// }
	// }
	// System.out.println(Json.objToJson(listEconomicData));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	//
	// // 获取福汇经济数据
	// private static void getEconomicData(String url) {
	// // String url="http://www.dailyfx.com.hk/calendar/index.html";
	// // String url = "X:/Projects/dailyfx.com/2014-03-12.htm";
	// try {
	// String[] arrExceptStr = new String[] { "&nbsp;", "\t", "\r" };
	// Parser parser = new Parser(url);
	// parser.setEncoding("UTF-8");
	// NodeFilter[] nodeFilter = new NodeFilter[2];
	// nodeFilter[0] = new TagNameFilter("div");
	// nodeFilter[1] = new HasAttributeFilter("id", "inside-calendar");
	// AndFilter andFilter = new AndFilter(nodeFilter);
	// NodeList nodeList = parser.parse(andFilter);
	// Node[] nodes = nodeList.toNodeArray();
	// String line = null;
	// int newDataLineCount = 0;
	// EconomicData economicData = null;
	// List<EconomicData> listEconomicData = new ArrayList<EconomicData>();
	// for (int i = 0; i < nodes.length; i++) {
	// line = nodes[i].toPlainTextString();
	//
	// if (line != null && !"".equals(line.trim())) {
	// for (String exceptStr : arrExceptStr) {
	// line = line.replace(exceptStr, "");
	// }
	// String[] lineArr = line.split("\n");
	// for (String item : lineArr) {
	// if("".equals(Fun.isNil(item)))continue;
	// // 判断是否以yyyy-MM-dd日期开头
	// if (isDate(item)) {
	// newDataLineCount = 0;
	// economicData = new EconomicData(1);
	// economicData.setPublishDate(item);
	// } else {
	// newDataLineCount += 1;
	// if (newDataLineCount % 7 == 1) {
	// economicData.setPublishTime(item);
	// } else if (newDataLineCount % 7 == 2) {
	// economicData.setIndicator(item);
	// } else if (newDataLineCount % 7 == 3) {
	// economicData.setImportance(item);
	// } else if (newDataLineCount % 7 == 4) {
	// economicData.setPreviousValue(item);
	// } else if (newDataLineCount % 7 == 5) {
	// economicData.setPredictedValue(item);
	// } else if (newDataLineCount % 7 == 6) {
	// economicData.setPublishedValue(item);
	// listEconomicData.add(economicData);
	// }
	// }
	// }
	// }
	// }
	//
	// for (EconomicData data : listEconomicData) {
	// System.out.println(data.toString());
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	public static void getBeanHtml(String url) {
		url = "http://baidu.com";
		try {
			Parser parser = new Parser();
			parser.setEncoding(parser.getEncoding());
			parser.setURL(url);
			NodeVisitor visitor = new NodeVisitor() {
				public void visitTag(Tag tag) {
					System.out.println("testVisitorAll() Tag name is :" + tag.getTagName() + "\n Class is :" + tag.getClass());
				}
			};
			parser.visitAllNodesWith(visitor);
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	public static String getHTML(String pageURL, String encoding) throws IOException {
		StringBuilder pageHTML = new StringBuilder();
		URL url = new URL(pageURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestProperty("User-Agent", "MSIE 7.0");
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
		String line = null;
		while ((line = br.readLine()) != null) {
			pageHTML.append(line);
			pageHTML.append("\r\n");
		}
		connection.disconnect();
		return pageHTML.toString();
	}

	// 下载html文件
	// public static void getHtmlFile() {
	// String baseDir = "E:/我的工程/汇通网/";
	// String url = "http://www.fx678.com/indexs/html/20131217.shtml";
	// Calendar dayStart = Calendar.getInstance();
	// dayStart.set(2012, 0, 0);
	// Calendar dayEnd = Calendar.getInstance();
	// dayEnd.set(2013, 11, 21);
	// while (dayStart.before(dayEnd)) {
	// dayStart.add(Calendar.DAY_OF_MONTH, 1);
	// url = "http://www.fx678.com/indexs/html/" +
	// Fun.dateToStr(dayStart.getTime(), "yyyyMMdd") + ".shtml";
	// String html;
	// try {
	// html = getHTML(url, "GB2312");
	// Fun.writeFile(baseDir + Fun.dateToStr(dayStart.getTime(), "yyyy") + "/" +
	// Fun.dateToStr(dayStart.getTime(), "yyyyMMdd") + ".shtml", html);
	// } catch (IOException e) {
	// System.out.println(Fun.dateToStr(dayStart.getTime(), "yyyyMMdd") +
	// ".shtml");
	// }
	// }
	// }
	public static StringBuilder gethtml(String url, String filter) throws ParserException {
		StringBuilder sb = new StringBuilder();

		String[] arrExceptStr = new String[] { "&nbsp;" };
		Parser parser = new Parser(url);
		parser.setEncoding("GB2312");
		// parser.setEncoding("UTF-8");
		NodeFilter[] nodeFilter = new NodeFilter[2];
		nodeFilter[0] = new TagNameFilter("div");
		// nodeFilter[0]=new NodeClassFilter(Span.class);
		nodeFilter[1] = new HasAttributeFilter("class", filter);
		AndFilter andFilter = new AndFilter(nodeFilter);
		// NodeList nodeList = parser.extractAllNodesThatMatch(andFilter);
		NodeList nodeList = parser.parse(andFilter);
		Node[] nodes = nodeList.toNodeArray();
		String line = null;
		for (int i = 0; i < nodes.length; i++) {
			line = nodes[i].toPlainTextString().replace("。", "。\n").replace("？", "？\n").replace("！", "！\n");
			if (line != null && !"".equals(line.trim())) {
				for (String exceptStr : arrExceptStr) {
					// System.out.println(line.replace(exceptStr, ""));
					sb.append(line.replace(exceptStr, ""));
				}
			}
		}
		return sb;
	}

	public static void main(String[] args) throws IOException, ParserException {
		// String url = "http://www.fx678.com/indexs/html/20140110.shtml";
		// String url = "E:/我的工程/汇通网/20120229.shtml";
		// gethtml(url);
		// getEconomicData(url);
		// getEconomicEvent(url, "2014-02-07");
		// getEconomicHoliday(url, "2014-02-07");
		// getEconomicNationalDebt(url, "2014-02-07");
		// getHtmlFile();
		// System.out.println(url.substring(url.lastIndexOf("/")+1));
		// url =
		// "http://www.fx678.com/indexchart/flash/economyData/indexchart.aspx?id=1232";
		// getEconomicIndicator(url);
		// 福汇数据
//		String url = "";
//		String title = Test.gethtml(url, "title").toString();
//		String content = Test.gethtml(url, "n_bd").toString();
	}
}
