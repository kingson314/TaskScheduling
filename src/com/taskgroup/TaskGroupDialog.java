package com.taskgroup;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;

import com.app.AppVar;
import com.log.Log;
import com.taskInterface.TaskTable;

import common.component.SBorder;
import common.component.SButton;
import common.component.SComboBox;
import common.component.SDialog;
import common.component.SLabel;
import common.component.SScrollPane;
import common.component.STextField;
import common.util.string.UtilString;
import common.util.swing.UtilComponent;
import consts.Const;
import consts.ImageContext;

public class TaskGroupDialog extends SDialog {

	private static final long serialVersionUID = 1L;
	private JPanel pnlGroup;
	private SLabel lGroupOrder;
	private STextField jtxtGroupOrder;
	private SScrollPane ScrlTask;
	private SScrollPane ScrlTaskGroup;
	private SButton btnCancel;
	private SButton btnOK;
	private JPanel pnlToolBar;
	private STextField txtTaskName;
	private JPanel pnlTaskTable;
	private JPanel pnlTaskGroupTable;
	private JPanel pnlTask;
	private SButton btnTaskQuery;
	private SComboBox cmbTaskType;
	private SButton btnAdd;
	private JToolBar tbTask;
	private SButton btnDown;
	private SButton btnUp;
	private SButton btnDel;
	private JToolBar tbGroup;
	private SComboBox cmbExecType;
	private JPanel pnlGroupTask;
	private SComboBox cmbErrorDeal;
	private STextField txtMemo;
	private STextField txtGroupName;
	private STextField txtGroupId;
	private SLabel SLabel7;
	private SLabel SLabel6;
	private SLabel SLabel5;
	private SLabel SLabel4;
	private SLabel SLabel3;
	private SLabel SLabel2;
	private SLabel SLabel1;
	private JTable tblTask;
	private JTable tblGroupTask;
	private int taskGroupAddModSign = -1;
	private TaskGroup tg;

	// public static void main(String[] args) {
	// TaskGroupDialog tg = new TaskGroupDialog(0, -1l);
	// tg.setVisible(true);
	// }

	// 构造
	public TaskGroupDialog(int taskGroupAddModSign, Long GroupID) {
		super();
		try {
			this.taskGroupAddModSign = taskGroupAddModSign;
			tg = new TaskGroup();
			if (taskGroupAddModSign == 0) {
				tg.setGroupId(TaskGroupDao.getInstance().getMaxGroupID() + 1);
				tg.setGroupOrder(String.valueOf(tg.getGroupId()));
				TaskGroupDao.getInstance().addTaskGroup(tg);
			} else if (taskGroupAddModSign == 1) {
				// System.out.println(taskGroupAddModSign);
				tg = TaskGroupDao.getInstance().getTaskGroupFromGroupID(GroupID);
			}
			initGUI();
			fillCom();
		} catch (Exception e) {
			Log.logError("任务组对话框构造错误:", e);
		} finally {
		}

	}

	// 实例化任务组对象
	private void fillTaskGroup() {
		try {
			tg.setGroupOrder(UtilString.isNil(jtxtGroupOrder.getText()));
			tg.setGroupName(UtilString.isNil(txtGroupName.getText()));
			tg.setErrorDeal(cmbErrorDeal.getSelectedIndex());
			tg.setExecType(cmbExecType.getSelectedIndex());
			tg.setGroupMemo(UtilString.isNil(txtMemo.getText()));
		} catch (Exception e) {
			Log.logError("任务组对话框赋值任务组对象错误:", e);
		} finally {
		}
	}

	// 填充任务组面板
	private void fillCom() {
		try {
			jtxtGroupOrder.setText(tg.getGroupOrder() == null ? "" : tg.getGroupOrder());
			txtGroupId.setText(tg.getGroupId().toString());
			txtGroupName.setText(tg.getGroupName() == null ? "" : tg.getGroupName());
			cmbErrorDeal.setSelectedIndex(tg.getErrorDeal());
			cmbExecType.setSelectedIndex(tg.getExecType());
			txtMemo.setText(tg.getGroupMemo() == null ? "" : tg.getGroupMemo());
			// jtable_taskGroup=
		} catch (Exception e) {
			Log.logError("任务组对话框赋值控件错误:", e);
		} finally {
		}
	}

	// 初始化界面
	private void initGUI() {
		try {
			setTitle("任务组编辑");
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.TaskGroup));
			setModal(true);
			setAlwaysOnTop(false);
			this.setResizable(false);
			getContentPane().setLayout(null);
			this.setSize(730, 670);
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);
			{
				pnlGroup = new JPanel();
				this.add(pnlGroup, "North");
				pnlGroup.setBorder(SBorder.getTitledBorder());
				pnlGroup.setLayout(null);
				pnlGroup.setBounds(2, 0, 718, 100);
				{
					cmbErrorDeal = new SComboBox(Const.TASK_Group_ErrorType);
					pnlGroup.add(cmbErrorDeal);
					cmbErrorDeal.setBounds(440, 42, 150, 21);
				}
				{
					SLabel1 = new SLabel("\u4efb\u52a1\u7ec4\u6807\u5fd7");
					pnlGroup.add(SLabel1);
					SLabel1.setBounds(61, 18, 69, 14);
				}
				{
					SLabel2 = new SLabel("\u5f02\u5e38\u5904\u7406");
					pnlGroup.add(SLabel2);
					SLabel2.setBounds(366, 49, 57, 14);
				}
				{
					SLabel3 = new SLabel("\u6267\u884c\u987a\u5e8f");
					pnlGroup.add(SLabel3);
					SLabel3.setBounds(381, 56, 67, 14);
					SLabel3.setVisible(false);
				}
				{
					SLabel4 = new SLabel("\u4efb\u52a1\u7ec4\u540d\u79f0");
					pnlGroup.add(SLabel4);
					SLabel4.setBounds(61, 46, 67, 14);
				}
				{
					SLabel5 = new SLabel("\u5907      \u6ce8");
					pnlGroup.add(SLabel5);
					SLabel5.setBounds(61, 75, 68, 14);
				}
				{
					txtGroupId = new STextField();
					txtGroupId.setEditable(false);
					pnlGroup.add(txtGroupId);
					txtGroupId.setBounds(133, 11, 150, 21);
				}
				{
					txtGroupName = new STextField();
					pnlGroup.add(txtGroupName);
					txtGroupName.setBounds(133, 42, 150, 21);
				}
				{
					txtMemo = new STextField();
					pnlGroup.add(txtMemo);
					txtMemo.setBounds(133, 73, 150, 21);
				}
				{
					cmbExecType = new SComboBox(Const.TASK_Group_ExecType);
					pnlGroup.add(cmbExecType);
					cmbExecType.setBounds(455, 49, 150, 21);
					cmbExecType.setVisible(false);
				}
				{
					jtxtGroupOrder = new STextField();
					pnlGroup.add(jtxtGroupOrder);
					jtxtGroupOrder.setText(tg.getGroupName() == null ? "" : tg.getGroupName());
					jtxtGroupOrder.setBounds(440, 11, 150, 21);
				}
				{
					lGroupOrder = new SLabel("\u4efb\u52a1\u7ec4\u7f16\u53f7");
					pnlGroup.add(lGroupOrder);
					lGroupOrder.setBounds(366, 18, 67, 14);
				}
			}
			{
				pnlGroupTask = new JPanel();
				getContentPane().add(pnlGroupTask);
				pnlGroupTask.setBounds(2, 100, 718, 255);
				pnlGroupTask.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
				pnlGroupTask.setLayout(null);
				{
					tbGroup = new JToolBar();
					tbGroup.setEnabled(false);
					pnlGroupTask.add(tbGroup);
					tbGroup.setBounds(0, 0, 718, 30);
					{
						btnDel = new SButton("\u5220    \u9664", ImageContext.Del);
						tbGroup.add(btnDel);
						btnDel.setPreferredSize(new java.awt.Dimension(88, 26));
						btnDel.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								delGroupTask();
							}
						});
					}
					{
						tbGroup.addSeparator(new Dimension(20, 30));
					}
					{
						btnUp = new SButton("\u4e0a    \u79fb", ImageContext.Up);
						tbGroup.add(btnUp);
						btnUp.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								upGroupTask();
							}
						});
					}
					{
						tbGroup.addSeparator(new Dimension(5, 30));
					}
					{
						btnDown = new SButton("\u4e0b    \u79fb", ImageContext.Down);
						tbGroup.add(btnDown);
						btnDown.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								downGroupTask();
							}
						});
					}
				}
				{
					pnlTaskGroupTable = new JPanel();
					GridLayout jPanelTaskTableLayout = new GridLayout(1, 1);
					jPanelTaskTableLayout.setColumns(1);
					jPanelTaskTableLayout.setHgap(5);
					jPanelTaskTableLayout.setVgap(5);
					pnlTaskGroupTable.setLayout(jPanelTaskTableLayout);
					pnlGroupTask.add(pnlTaskGroupTable);
					pnlTaskGroupTable.setBounds(0, 30, 718, 225);
					{

						getTaskTable(0);
					}
				}
			}
			{
				pnlTask = new JPanel();
				getContentPane().add(pnlTask);
				pnlTask.setBounds(2, 360, 718, 230);
				pnlTask.setBorder(new LineBorder(new java.awt.Color(0, 0, 0), 1, false));
				pnlTask.setLayout(null);
				{
					tbTask = new JToolBar();
					tbTask.setEnabled(false);
					pnlTask.add(tbTask);
					tbTask.setBounds(0, 0, 718, 30);
					{
						btnAdd = new SButton("\u6dfb    \u52a0", ImageContext.Add);
						tbTask.add(btnAdd);
						btnAdd.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								addGroupTask();
							}
						});
					}
					{
						tbTask.addSeparator(new Dimension(60, 30));
					}
					{
						SLabel6 = new SLabel("\u4efb\u52a1\u7c7b\u578b   ");
						tbTask.add(SLabel6);
					}
					{
						cmbTaskType = new SComboBox(AppVar.TASK_TYPE_Combobox, 20);
						tbTask.add(cmbTaskType);
						cmbTaskType.setMaximumSize(new java.awt.Dimension(150, 20));
						cmbTaskType.setMinimumSize(new java.awt.Dimension(150, 20));
					}
					{
						tbTask.addSeparator(new Dimension(5, 30));
					}
					{
						SLabel7 = new SLabel("   \u4efb\u52a1\u540d\u79f0   ");
						tbTask.add(SLabel7);
					}
					{

						txtTaskName = new STextField();
						tbTask.add(txtTaskName);
						txtTaskName.setPreferredSize(new java.awt.Dimension(24, 20));
						txtTaskName.setMaximumSize(new java.awt.Dimension(120, 20));
						txtTaskName.setMinimumSize(new java.awt.Dimension(24, 20));
						txtTaskName.setEditable(true);
					}

					{
						tbTask.addSeparator(new Dimension(5, 30));
					}
					{
						btnTaskQuery = new SButton("  \u67e5    \u8be2   ", ImageContext.Query);
						tbTask.add(btnTaskQuery);
						btnTaskQuery.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								try {
									btnTaskQuery.setEnabled(false);
									queryGroupTask();
								} finally {
									btnTaskQuery.setEnabled(true);
								}
							}
						});
					}
				}
				{
					pnlTaskTable = new JPanel();
					GridLayout jPanelTaskTableLayout1 = new GridLayout(1, 1);
					jPanelTaskTableLayout1.setColumns(1);
					jPanelTaskTableLayout1.setHgap(5);
					jPanelTaskTableLayout1.setVgap(5);
					pnlTaskTable.setLayout(jPanelTaskTableLayout1);
					pnlTask.add(pnlTaskTable);
					pnlTaskTable.setBounds(0, 30, 718, 200);
					{
						getTaskTable(1);
					}
				}
			}
			{
				pnlToolBar = new JPanel();
				getContentPane().add(pnlToolBar);
				pnlToolBar.setBounds(2, 600, 718, 40);
				pnlToolBar.setBorder(SBorder.getTitledBorder());
				{
					btnOK = new SButton("\u786e   \u5b9a", ImageContext.Ok);
					pnlToolBar.add(btnOK);
					btnOK.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnOk();
						}
					});
					btnOK.setPreferredSize(new java.awt.Dimension(112, 25));
					btnOK.setSize(112, 25);
				}
				{
					btnCancel = new SButton("\u53d6  \u6d88", ImageContext.Exit);
					pnlToolBar.add(btnCancel);
					btnCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnCancel();
						}
					});
					btnCancel.setPreferredSize(new java.awt.Dimension(112, 25));
					btnCancel.setSize(112, 25);
				}
			}
		} catch (Exception e) {
			Log.logError("任务组对话框初始化界面错误:", e);
		} finally {
		}
	}

	// 获取任务表格
	public void getTaskTable(int type) {
		try {
			if (type == 0) {// 组任务
				Map<String, String> map = new HashMap<String, String>();
				map.put("Type", "任务组任务");
				if (tg == null)
					return;
				map.put("GroupId", String.valueOf(tg.getGroupId()));
				tblGroupTask = new TaskTable(map).getJtable();
				if (ScrlTaskGroup == null) {
					ScrlTaskGroup = new SScrollPane(tblGroupTask);
					pnlTaskGroupTable.add(ScrlTaskGroup);
				} else
					ScrlTaskGroup.setViewportView(tblGroupTask);
			} else if (type == 1) {// 全部任务
				Map<String, String> map = new HashMap<String, String>();
				map.put("Type", "全部");
				tblTask = new TaskTable(map).getJtable();
				if (ScrlTask == null) {
					ScrlTask = new SScrollPane(tblTask);
					pnlTaskTable.add(ScrlTask);
				} else
					ScrlTask.setViewportView(tblTask);

			} else if (type == 2) {// 任务过滤
				Map<String, String> map = new HashMap<String, String>();
				map.put("Type", "任务过滤");
				if (cmbTaskType == null || txtTaskName == null)
					return;
				map.put("任务类型", cmbTaskType.getSelectedItem().toString());
				map.put("任务名称", txtTaskName.getText());
				tblTask = new TaskTable(map).getJtable();
				if (ScrlTask == null) {
					ScrlTask = new SScrollPane(tblTask);
					pnlTaskTable.add(ScrlTask);
				} else
					ScrlTask.setViewportView(tblTask);
			}
		} catch (Exception e) {
			Log.logError("任务组对话框获取任务列表错误:", e);
		} finally {
		}
	}

	// 确定
	private void btnOk() {
		try {
			fillTaskGroup();
			TaskGroupDao.getInstance().modTaskGroup(tg);
			TaskGroupTab.getInstance().queryTaskGroup();
			this.dispose();
		} catch (Exception e) {
			Log.logError("任务组对话确定按钮事件错误:", e);
		} finally {
		}
	}

	// 取消
	private void btnCancel() {
		try {
			if (this.taskGroupAddModSign == 0)
				TaskGroupDao.getInstance().delTaskGroup(tg.getGroupId());
			this.dispose();
		} catch (Exception e) {
			Log.logError("任务组对话框取消按钮事件错误:", e);
		} finally {
		}
	}

	// 删除任务组任务
	private void delGroupTask() {
		try {
			int len = (int) UtilComponent.getTableSelectedCount(tblGroupTask);
			Long[] taskOrder = new Long[len];
			int index = 0;
			for (int i = 0; i < tblGroupTask.getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(tblGroupTask.getValueAt(i, 0).toString());
				if (selected) {
					taskOrder[index] = Long.valueOf(tblGroupTask.getValueAt(i, 11).toString());
					index += 1;
				}
			}
			if (index > 0)
				TaskGroupDetailDao.getInstance().delTaskGroupDetail(tg.getGroupId(), taskOrder);
			getTaskTable(0);
		} catch (Exception e) {
			Log.logError("任务组对话框删除任务组任务错误:", e);
		} finally {
		}
	}

	// 查询任务组任务
	private void queryGroupTask() {
		try {
			getTaskTable(2);
		} catch (Exception e) {
			Log.logError("任务组对话框查询任务组任务错误:", e);
		} finally {
		}
	}

	// 添加任务组任务
	private void addGroupTask() {
		try {
			Long[] taskID = new Long[(int) UtilComponent.getTableSelectedCount(tblTask)];
			int index = 0;
			for (int i = 0; i < tblTask.getRowCount(); i++) {
				Boolean selected = Boolean.valueOf(tblTask.getValueAt(i, 0).toString());
				if (selected) {
					taskID[index] = Long.valueOf(tblTask.getValueAt(i, 2).toString());
					index += 1;
				}
			}
			TaskGroupDetailDao.getInstance().addTaskGroupDetail(tg.getGroupId(), taskID);
			getTaskTable(0);
		} catch (Exception e) {
			Log.logError("任务组对话框增加任务组任务错误:", e);
		} finally {
		}
	}

	// 下移任务组任务
	private void downGroupTask() {
		try {
			if (tblGroupTask.getSelectedRow() >= 0) {
				TaskGroupDetailDao.getInstance().downTaskGroupDetail(tg.getGroupId(), Long.valueOf(tblGroupTask.getValueAt(tblGroupTask.getSelectedRow(), 11).toString()));
				getTaskTable(0);
			}
		} catch (Exception e) {
			Log.logError("任务组对话框下移任务组任务错误:", e);
		} finally {
		}
	}

	// 上移任务组任务
	private void upGroupTask() {
		try {
			if (tblGroupTask.getSelectedRow() >= 0) {
				TaskGroupDetailDao.getInstance().upTaskGroupDetail(tg.getGroupId(), Long.valueOf(tblGroupTask.getValueAt(tblGroupTask.getSelectedRow(), 11).toString()));
				getTaskTable(0);
			}
		} catch (Exception e) {
			Log.logError("任务组对话框上移任务组任务错误:", e);
		} finally {
		}
	}
}
