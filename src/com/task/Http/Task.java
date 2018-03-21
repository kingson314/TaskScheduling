package com.task.Http;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.taskInterface.TaskAbstract;
import common.util.json.UtilJson;

public class Task extends TaskAbstract {
	public void fireTask() {
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			String rs = http(bean.getUrl(), bean.getParams().trim());
			this.setTaskStatus("执行成功");
			this.setTaskMsg(this.getNowDate() + " 返回结果:" + rs);
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("错误:", e);
		}
	}

	public void fireTask(final String startTime, final String groupId, final String scheCod, final String taskOrder) {
		fireTask();
	}

	public String http(String url, String params) throws HttpException, IOException {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		if(params.length()>0){
			String[] paramsArr = params.split("|");
			if(paramsArr.length>0){
				NameValuePair[] arrNameValuePair = new NameValuePair[paramsArr.length];
				if(paramsArr.length>0){
					for (int i = 0; i < paramsArr.length; i++) {
						String[] pArr = paramsArr[i].split("=");
						arrNameValuePair[i] = new NameValuePair(pArr[0], pArr[1]);
					}
				}
				postMethod.setRequestBody(arrNameValuePair);
			}
		}
		httpClient.executeMethod(postMethod);
		return postMethod.getResponseBodyAsString();
	}
}
