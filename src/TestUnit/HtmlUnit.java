package TestUnit;

import java.io.IOException;
import java.net.MalformedURLException;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;

public class HtmlUnit {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @throws FailingHttpStatusCodeException 
	 */
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// 得到浏览器对象，直接New一个就能得到，现在就好比说你得到了一个浏览器了  
	    WebClient webclient = new WebClient();  
	    webclient.waitForBackgroundJavaScript(600 * 1000);
	    Page htmlpage = webclient.getPage("http://www.kxt.com/rili");  
	    System.out.println(htmlpage.toString());  

	}

}
