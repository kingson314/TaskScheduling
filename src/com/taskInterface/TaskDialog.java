package com.taskInterface;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.swing.JPanel;

import com.app.AppVar;
import com.log.Log;
import com.monitor.MonitorGroupDao;
import com.taskClass.TaskClass;
import com.taskClass.TaskClassImp;

import common.component.SButton;
import common.component.SComboBox;
import common.component.SDialog;
import common.component.SLabel;
import common.component.SSplitPane;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.string.UtilString;
import consts.Const;
import consts.ImageContext;

public class TaskDialog extends SDialog {
	private static final long serialVersionUID = 1L;
	private JPanel pnlTaskMsg;
	private SComboBox cmbTasktype;
	private SLabel lTaskType;
	private STextField txtTaskName;
	private STextField txtTaskId;
	private SLabel lOverTimeSecond;
	private SComboBox cmbWarnType;
	private SLabel lWarnType;
	private SComboBox cmbMonitorGroup;
	private STextField txtOverTime;
	private SLabel lOverTime;
	private STextField txtTaskOrder;
	private SLabel ltaskOrder;
	private SButton btnOk;
	private SButton btnCancel;
	private JPanel pnlTool;
	private SComboBox cmbLogType;
	private SLabel lLogType;
	private SLabel lIntervalSecond;
	private STextField txtInterval;
	private SLabel lInterval;
	private STextField txtTaskMemo;
	private SLabel lTaskMemo;
	private JPanel pnlDefault;
	private JPanel pnlTask;
	private JPanel pnlCard;
	private SSplitPane SplTask;
	private SLabel lMonitorGroup;
	private SLabel lTaskId;
	private SLabel lTaskName;

	private int taskAddModSign = -1;
	private ITask task;
	private CardLayout cardLayout;
	// 任务类型面板
	private Map<String, ITaskPanel> maptaskPanel = new HashMap<String, ITaskPanel>();

	// public static void main(String[] args) {
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// JFrame frame = new JFrame();
	//
	// TaskDialog inst = new TaskDialog(frame);
	// inst.setVisible(true);
	// }
	// });
	// }

	// 根据任务jar穿件任务参数面板
	private void createPanel() {
		Set<Entry<String, TaskClass>> entrySet = TaskClassImp.mapTaskClass.entrySet();
		for (Map.Entry<String, TaskClass> entry : entrySet) {
			TaskClass taskClass = entry.getValue();
			ITaskPanel taskPanel = TaskClassImp.getTaskPanel(taskClass);
			JPanel tPanel = taskPanel.getPanel();
			maptaskPanel.put(taskClass.getTaskType(), taskPanel);
			pnlCard.add(tPanel, taskClass.getTaskType());
		}
	}

	// 构造
	public TaskDialog(int taskAddModsign, ITask itask) {
		super();
		try {
			this.task = itask;
			this.taskAddModSign = taskAddModsign;

			initGUI();
			createPanel();
			itask.setTaskOrder(UtilString.isNil(itask.getTaskOrder(), String.valueOf(task.getTaskId())));
			fillTaskComp(itask);
			String taskType = cmbTasktype.getSelectedItem().toString();
			TaskClass taskClass = TaskClassImp.mapTaskClass.get(taskType);
			// /增加任务类型时，需创建一个任务类模板，并维护此处
			cardLayout.show(pnlCard, taskClass.getTaskType());
		} catch (Exception e) {
			Log.logError("任务对话框构造错误:", e);
		}
	}

	// 填充任务对象
	public boolean fillTask(ITask task) {
		boolean rs = true;
		try {
			task.setTaskOrder(txtTaskOrder.getText() == null ? "" : txtTaskOrder.getText());
			task.setTaskName(txtTaskName.getText() == null ? "" : txtTaskName.getText());
			task.setMonitorGroup(cmbMonitorGroup.getSelectedItem() == null ? "" : cmbMonitorGroup.getSelectedItem().toString());

			task.setTaskMemo(txtTaskMemo.getText() == null ? "" : txtTaskMemo.getText());
			task.setWarnType(cmbWarnType.getSelectedItem() == null ? "全部" : cmbWarnType.getSelectedItem().toString());
			task.setTaskType(cmbTasktype.getSelectedItem().toString());
			task.setLogType(cmbLogType.getSelectedItem().toString());
			try {
				task.setInterval(Long.valueOf(txtInterval.getText() == null ? "0" : txtInterval.getText()));
			} catch (Exception e) {
				ShowMsg.showWarn("报警时间间隔请输入数字！");
				return false;
			}

			try {
				task.setOverTime(Long.valueOf(txtOverTime.getText() == null ? "0" : txtOverTime.getText()));
			} catch (Exception e) {
				ShowMsg.showWarn("任务超时阀值请输入数字！");
				return false;
			}

		} catch (Exception e) {
			Log.logError("任务对话框赋值任务对象错误:", e);
			return false;
		} finally {
		}
		return rs;
	}

	// 填充参数面板
	private void fillTaskComp(ITask task) {
		try {
			if (task != null) {
				// 任务头
				txtTaskOrder.setText(task.getTaskOrder() == null ? "" : task.getTaskOrder());
				txtOverTime.setText(String.valueOf(task.getOverTime() == null ? "0" : task.getOverTime()));
				txtTaskId.setText(String.valueOf(task.getTaskId()));
				cmbMonitorGroup.setSelectedItem(task.getMonitorGroup() == null ? "" : task.getMonitorGroup());

				txtTaskMemo.setText(task.getTaskMemo() == null ? "" : task.getTaskMemo());
				cmbTasktype.setSelectedItem(task.getTaskType() == null ? "" : task.getTaskType());
				txtTaskName.setText(task.getTaskName());
				cmbWarnType.setSelectedItem(task.getWarnType() == null ? "全部" : task.getWarnType());
				cmbLogType.setSelectedItem(task.getLogType() == null ? "" : task.getLogType());
				txtInterval.setText(task.getInterval() == null ? "0" : String.valueOf(task.getInterval()));

				// /增加任务类型时，需创建一个任务类模板，并维护此处
				String taskType = cmbTasktype.getSelectedItem().toString();
				if (this.taskAddModSign == 1)
					maptaskPanel.get(taskType).fillComp(task);
			}
		} catch (Exception e) {
			Log.logError("任务对话框复制任务控件错误:", e);
		} finally {
		}
	}

	// 初始化界面
	private void initGUI() {
		try {
			setTitle("任务编辑");
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.Task));
			setModal(true);
			setAlwaysOnTop(false);
			this.setBounds(0, 0, 648, 680);
			this.setResizable(false);
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);
			FlowLayout thisLayout = new FlowLayout();
			getContentPane().setLayout(thisLayout);
			{
				SplTask = new SSplitPane(0, 165, false);
				getContentPane().add(SplTask);
				SplTask.setEnabled(false);
				{
					pnlTaskMsg = new JPanel();
					SplTask.add(pnlTaskMsg, SSplitPane.TOP);
					pnlTaskMsg.setLayout(null);
					{
						lTaskId = new SLabel("\u4efb\u52a1\u6807\u5fd7");
						pnlTaskMsg.add(lTaskId, "Center");
						lTaskId.setBounds(28, 19, 77, 14);
					}
					{
						lTaskName = new SLabel("\u4efb\u52a1\u540d\u79f0");
						pnlTaskMsg.add(lTaskName);
						lTaskName.setBounds(28, 47, 77, 14);
					}
					{
						txtTaskId = new STextField();
						pnlTaskMsg.add(txtTaskId);
						txtTaskId.setBounds(111, 12, 148, 21);
						txtTaskId.setEditable(false);
					}
					{
						txtTaskName = new STextField();
						pnlTaskMsg.add(txtTaskName);
						txtTaskName.setBounds(111, 40, 148, 21);
					}
					{
						lTaskType = new SLabel("\u4efb\u52a1\u7c7b\u578b");
						pnlTaskMsg.add(lTaskType);
						lTaskType.setBounds(318, 47, 76, 14);
					}
					{
						String[] TASK_TYPE_Define = null;
						TASK_TYPE_Define = AppVar.TASK_TYPE;
						cmbTasktype = new SComboBox(TASK_TYPE_Define, 20);
						pnlTaskMsg.add(cmbTasktype);
						cmbTasktype.setBounds(400, 40, 186, 21);
						switch (this.taskAddModSign) {
						case 0: {
							cmbTasktype.setEnabled(true);
							break;
						}
						case 1: {
							cmbTasktype.setEnabled(false);
							break;
						}
						}
						cmbTasktype.addItemListener(new ItemListener() {
							public void itemStateChanged(ItemEvent evt) {
								String taskType = cmbTasktype.getSelectedItem().toString();
								TaskClass taskClass = TaskClassImp.mapTaskClass.get(taskType);
								cardLayout.show(pnlCard, taskClass.getTaskType());
								txtTaskName.setText(taskType);

							}
						});

					}
					{
						lMonitorGroup = new SLabel("\u76d1\u63a7\u7ec4");
						pnlTaskMsg.add(lMonitorGroup);
						lMonitorGroup.setBounds(28, 103, 77, 14);
					}
					{
						lTaskMemo = new SLabel("\u4efb\u52a1\u8bf4\u660e");
						pnlTaskMsg.add(lTaskMemo);
						lTaskMemo.setBounds(318, 75, 76, 14);
					}
					{
						txtTaskMemo = new STextField();
						pnlTaskMsg.add(txtTaskMemo);
						txtTaskMemo.setBounds(400, 69, 186, 21);
					}
					{
						lInterval = new SLabel("\u62a5\u8b66\u65f6\u95f4\u95f4\u9694");
						pnlTaskMsg.add(lInterval);
						lInterval.setBounds(318, 103, 76, 14);
					}
					{
						txtInterval = new STextField();
						pnlTaskMsg.add(txtInterval);
						txtInterval.setBounds(400, 98, 161, 21);
						txtInterval.setText("0");
					}
					{
						lIntervalSecond = new SLabel("(\u79d2)");
						pnlTaskMsg.add(lIntervalSecond);
						lIntervalSecond.setBounds(565, 103, 24, 14);
					}
					{
						lLogType = new SLabel("\u65e5\u5fd7\u7c7b\u578b");
						pnlTaskMsg.add(lLogType);
						lLogType.setBounds(28, 75, 77, 14);
					}
					{
						cmbLogType = new SComboBox(Log.TaskLogType);
						pnlTaskMsg.add(cmbLogType);
						cmbLogType.setBounds(111, 68, 148, 21);
					}
					{
						ltaskOrder = new SLabel("\u4efb\u52a1\u7f16\u53f7");
						pnlTaskMsg.add(ltaskOrder);
						ltaskOrder.setBounds(318, 19, 76, 14);
					}
					{
						txtTaskOrder = new STextField();
						pnlTaskMsg.add(txtTaskOrder);
						txtTaskOrder.setBounds(400, 12, 186, 21);
					}
					{
						lOverTime = new SLabel("\u4efb\u52a1\u8d85\u65f6\u9600\u503c");
						pnlTaskMsg.add(lOverTime);
						lOverTime.setBounds(318, 131, 77, 14);
					}
					{
						txtOverTime = new STextField();
						pnlTaskMsg.add(txtOverTime);
						txtOverTime.setBounds(400, 124, 161, 21);
					}
					{
						lOverTimeSecond = new SLabel("(\u79d2)");
						pnlTaskMsg.add(lOverTimeSecond);
						lOverTimeSecond.setBounds(565, 131, 24, 14);
					}
					{
						cmbMonitorGroup = new SComboBox(MonitorGroupDao.getInstance().getMonitorGroupCode());
						pnlTaskMsg.add(cmbMonitorGroup);
						cmbMonitorGroup.setBounds(111, 96, 148, 21);
					}
					{
						lWarnType = new SLabel("\u62a5\u8b66\u7c7b\u578b");
						pnlTaskMsg.add(lWarnType);
						lWarnType.setBounds(28, 131, 71, 14);
					}
					{
						cmbWarnType = new SComboBox(Const.WarnType);
						pnlTaskMsg.add(cmbWarnType);
						cmbWarnType.setBounds(111, 124, 148, 21);
					}

				}

				{
					pnlTask = new JPanel();
					pnlTask.setPreferredSize(new Dimension(615, 580));
					SplTask.add(pnlTask, SSplitPane.BOTTOM);
					pnlTask.setLayout(null);
					{
						pnlCard = new JPanel();
						pnlTask.add(pnlCard, TOP_ALIGNMENT);
						cardLayout = new CardLayout();
						pnlCard.setBounds(0, 0, 613, 560);
						pnlCard.setLayout(cardLayout);
						{
							pnlDefault = new JPanel();
							pnlCard.add(pnlDefault, "null");
							pnlDefault.setBounds(0, 0, 613, 560);
							// 增加任务类型时，需创建一个任务类模板，并维护此处
						}
					}
				}

			}
			{
				pnlTool = new JPanel();
				getContentPane().add(pnlTool);
				{
					btnOk = new SButton("确   定", ImageContext.Ok);
					pnlTool.add(btnOk);
					btnOk.setPreferredSize(new java.awt.Dimension(120, 25));
					btnOk.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnOk();
						}
					});
				}
				{
					btnCancel = new SButton("取  消", ImageContext.Exit);
					pnlTool.add(btnCancel);
					btnCancel.setPreferredSize(new java.awt.Dimension(121, 25));
					btnCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCacel();
						}
					});
				}
			}
		} catch (Exception e) {
			Log.logError("任务对话框初始化界面错误:", e);
		} finally {
		}
	}

	// 确定事件
	private void btnOk() {
		try {
			if (!fillTask(this.task))
				return;
			String taskType = this.cmbTasktype.getSelectedItem().toString();
			if (!maptaskPanel.get(taskType).fillTask(this.task))
				return;
			switch (this.taskAddModSign) {
			case 0: {
				TaskDao.getInstance().addTask(this.task);
				break;
			}
			case 1: {
				TaskDao.getInstance().modTask(this.task);
				break;
			}
			}
			this.dispose();
			TaskTab.getInstance().queryTask();
			// 任务组里修改时
			// TaskGroupDialog.getTaskTable(0);
			// TaskGroupDialog.getTaskTable(2);
		} catch (Exception e) {
			Log.logError("任务面板保存错误:", e);
		} finally {
		}
	};

	// 取消事件
	private void btnCacel() {
		try {
			this.dispose();
		} catch (Exception e) {
			Log.logError("任务面板退出错误:", e);
		} finally {
		}
	}

}
