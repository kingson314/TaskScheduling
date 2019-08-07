package com.task.Http;

import javax.swing.JPanel;
import com.log.Log;
import com.taskInterface.ITask;
import com.taskInterface.ITaskPanel;
import common.component.SLabel;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

public class Panel implements ITaskPanel {
	private JPanel pnlMain;
	private STextField txtUrl;
	private SLabel lUrl;
	private SLabel lParams;
	private STextField txtParams;
	public Panel() {
	}

	public JPanel getPanel() {

		pnlMain = new JPanel();
		try {
			pnlMain.setBounds(0, 0, 623, 480);
			pnlMain.setLayout(null);

			{
				lUrl = new SLabel("URL");
				pnlMain.add(lUrl);
				lUrl.setBounds(21, 58, 70, 14);
				lUrl.setFont(Const.tfont);
			}
			{
				txtUrl = new STextField();
				pnlMain.add(txtUrl);
				txtUrl.setBounds(103, 51, 429, 21);
			}
			{
				lParams = new SLabel("参数");
				pnlMain.add(lParams);
				lParams.setBounds(21, 88, 70, 14);
				lParams.setFont(Const.tfont);
			}
			{
				txtParams = new STextField();
				pnlMain.add(txtParams);
				txtParams.setBounds(103, 81, 429, 21);
			}
		} catch (Exception e) {
			Log.logError("初始化面板错误:", e);
			return pnlMain;
		} finally {
		}
		return pnlMain;
	}

	// 参数验证
	private boolean paramValidate(Bean bean) {
		if ("".equals(UtilString.isNil(bean.getUrl()))) {
			ShowMsg.showWarn("Url不能为空");
			return false;
		}
		return true;
	}

	// 实例化task
	public boolean fillTask(ITask task) {
		try {
			Bean bean = new Bean();
			bean.setParams(UtilString.isNil(txtParams.getText()));
			bean.setUrl(UtilString.isNil(txtUrl.getText()));
			 
			if (!paramValidate(bean))
				return false;
			task.setJsonStr(UtilJson.getJsonStr(bean));
		} catch (Exception e) {
			ShowMsg.showWarn(e.getMessage());
			return false;
		} finally {
		}
		return true;
	}

	// 填充面板
	public void fillComp(ITask task) {
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(task.getJsonStr(), Bean.class);
			txtUrl.setText(bean.getUrl());
			txtParams.setText(bean.getParams());
		} catch (Exception e) {
			Log.logError("填充控件错误:", e);
		} finally {
		}
	}
}
