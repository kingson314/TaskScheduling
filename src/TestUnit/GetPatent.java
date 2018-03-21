//package TestUnit;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import org.htmlparser.Node;
//import org.htmlparser.NodeFilter;
//import org.htmlparser.Parser;
//import org.htmlparser.filters.AndFilter;
//import org.htmlparser.filters.HasAttributeFilter;
//import org.htmlparser.filters.NotFilter;
//import org.htmlparser.filters.TagNameFilter;
//import org.htmlparser.util.NodeList;
//import org.htmlparser.util.ParserException;
//
//public class GetPatent {
//
//	/**
//	 * @param args
//	 * @throws ParserException 
//	 * @throws IOException 
//	 */
//	public static void main(String[] args) throws ParserException, IOException {
//		//申请信息
//		String url = "http://cpquery.sipo.gov.cn/txnQueryBibliographicData.do?select-key:shenqingh=2015110170056&select-key:zhuanlilx=1&select-key:backPage=http%3A%2F%2Fcpquery.sipo.gov.cn%2FtxnQueryOrdinaryPatents.do%3Fselect-key%3Ashenqingh%3D2015110170056%26select-key%3Azhuanlimc%3D%26select-key%3Ashenqingrxm%3D%26select-key%3Azhuanlilx%3D%26select-key%3Ashenqingr_from%3D%26select-key%3Ashenqingr_to%3D%26inner-flag%3Aopen-type%3Dwindow%26inner-flag%3Aflowno%3D1466571732276&inner-flag:open-type=window&inner-flag:flowno=1466572878108";
//		//审查信息
//		
//		//费用信息
//		//发文信息
//		//公布公告
//		//专利登记薄
//		
//		//同族案件信息
//		//System.out.println(getHTML(url,"UTF-8"));
//		getPatent(url,"UTF-8");
//		System.out.println("********************************");
//	}
//	public static void getPatent(String url, String encoding) throws ParserException {
//		String[] arrExceptStr = new String[] { "&nbsp;" };
//		NodeFilter filterTable = new TagNameFilter("span");
//		NodeFilter filterClass= new HasAttributeFilter("title");
//		AndFilter andFilter = new AndFilter(filterTable,filterClass);
//		
//		NodeFilter notFilterClass= new HasAttributeFilter("class","nlkfqirnlfjer1dfgzxcyiuro");
//		NotFilter notFilter=new NotFilter(notFilterClass);
//		
//		Parser parser = new Parser(url);
//		parser.setEncoding(encoding);
//		NodeList nodeList = parser.parse(andFilter);
//		
//		Node[] nodes = nodeList.toNodeArray();
//		String line = null;
//		for (int i = 0; i < nodes.length; i++) {
//			line = nodes[i].toPlainTextString();
//			if (line != null && !"".equals(line.trim())) {
//				for (String exceptStr : arrExceptStr) {
//					line = line.replace(exceptStr, "");
//				}
//				System.out.println(line);
//			}
//		}
//	}
//	// 下载html内容
//	private static String getHTML(String pageURL, String encoding) throws IOException {
//		StringBuilder pageHTML = new StringBuilder();
//		URL url = new URL(pageURL);
//		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//		connection.setRequestProperty("User-Agent", "MSIE 7.0");
//		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding));
//		String line = null;
//		while ((line = br.readLine()) != null) {
//			pageHTML.append(line);
//			pageHTML.append("\r\n");
//		}
//		connection.disconnect();
//		return pageHTML.toString();
//	}
//	
//	
//
//}
