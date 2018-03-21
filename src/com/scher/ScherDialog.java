package com.scher;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JPanel;

import com.log.Log;
import com.settings.SettingsDao;
import com.taskInterface.ITask;
import com.taskInterface.TaskDao;
import com.taskgroup.TaskGroup;
import com.taskgroup.TaskGroupDao;

import common.component.SBorder;
import common.component.SButton;
import common.component.SCalendar;
import common.component.SCheckBox;
import common.component.SComboBox;
import common.component.SDialog;
import common.component.SLabel;
import common.component.SRadioButton;
import common.component.SSplitPane;
import common.component.STextField;
import common.component.ShowMsg;
import common.util.conver.UtilConver;
import common.util.string.UtilString;
import consts.Const;
import consts.ImageContext;
import consts.VariableApp;

/**
 * @author: fenggq
 * @Email:
 * @Company:
 * @Action:调度信息对话框
 * @DATE: Mar 15, 2012
 */
public class ScherDialog extends SDialog {
	private static final long serialVersionUID = 1L;
	private JPanel pnlBegin;
	private SRadioButton rbtnSecond;
	private SRadioButton rbntOnece;
	private JPanel pnlLoopCon;
	private SLabel l4;
	private STextField txtEndTime;
	private SRadioButton rbtnEndDate;
	private SRadioButton rbtnNoEnddate;
	private SLabel l3;
	private SRadioButton rbtnMonth;
	private SRadioButton rbtnWeek;
	private SRadioButton rbtnDay;
	private SRadioButton rbtnHour;
	private SRadioButton rbtnMinute;
	private SLabel l2;
	private STextField txtStartTime;
	private SLabel l1;
	private SRadioButton rbtnStartDate;
	private SRadioButton rbtnNow;
	private JPanel pnlEnd;
	private JPanel pnlLoop;
	private SLabel lOnece; // 一次
	private SLabel lSecond1; // 每秒
	private SLabel lSecond2;
	private STextField txtSecond;
	private SLabel lSecond3;
	private STextField txtSecond1;
	private SLabel lSecond;
	private SLabel lOnece1;
	private JPanel pnlOnece;
	private JPanel pnlSecond;
	private JPanel pnlMinute;
	private JPanel pnlHour;
	private JPanel pnlDay;
	private JPanel pnlWeek;
	private JPanel pnlMonth;
	private SSplitPane SplLoop;
	private STextField txtMinuteM;
	private SLabel lMinute;
	private SLabel lMinute1;
	private SLabel lMinute2;
	private STextField txtMinuteS;
	private SLabel lMinute3;
	private SLabel lHour;
	private STextField txtHourH;
	private SLabel lHour1;
	private SLabel lHour2;
	private STextField txtHhourM;
	private SLabel lhour3;
	private STextField txtHourS;
	private SLabel lHour4;
	private SLabel lDay;
	private STextField txtDay;
	private SLabel lDay1;
	private SComboBox cmbSpecialDate;
	private SLabel lSpecialDate;
	private JPanel pnlSpecialDate;
	private STextField txtSchOrder;
	private SLabel lSchOrder;
	private SComboBox cmbExecTime;
	private SLabel lExecTime;
	private SComboBox cmbDateType;
	private SLabel lDateType;
	private JPanel pnlDateTime;
	private SButton btnEndDate;
	private SButton btnBeginDate;
	private SButton btnCancle;
	private SButton btnOk;
	private SCheckBox chkDecember;
	private SCheckBox chkNovember;
	private SCheckBox chkOctober;
	private SCheckBox chkSeptember;
	private SCheckBox chkAugust;
	private SCheckBox chkJuly;
	private SCheckBox chkJune;
	private SCheckBox chkMay;
	private SCheckBox chkApril;
	private SCheckBox chkMarch;
	private SCheckBox chkFebruary;
	private SCheckBox chkJanuary;
	private SComboBox cmbMonthIndex;
	private SComboBox cmbMonthWeek;
	private SRadioButton rbtnMonthWeek;
	private SLabel lMonthDay;
	private STextField txtMonthDay;
	private SRadioButton rbtnMonthDay;
	private SCheckBox chkSunday;
	private SLabel lWeek;
	private STextField txtWeek;
	private SLabel lweek1;
	private SCheckBox chkSaturday;
	private SCheckBox chkMonday;
	private SCheckBox chkTuesday;
	private SCheckBox chkWednesday;
	private SCheckBox chkThursday;
	private SCheckBox chkFriday;
	private JPanel pnlSche;
	private SLabel lScheCode;
	private STextField txtScheCode;
	private SLabel lScheName;
	private STextField txtScheName;
	private SLabel lScheComent;
	private SLabel lSchetype;
	private STextField txtScheComent;
	private SComboBox cmbSchetype;
	private STextField txtEndDate;
	private STextField txtStartDate;

	private int AddModSign;
	private String TaskId;
	private String GroupId;
	private ScherParam scheParam;
	private SCheckBox chkNowDate;

	// public static void main(String[] args) {
	// SwingUtilities.invokeLater(new Runnable() {
	// public void run() {
	// ScherDialog inst = new ScherDialog(0, "0", "0");
	// inst.setVisible(true);
	// }
	// });
	// }
	// 构造函数
	public ScherDialog(int addModSign, String taskId, String groupId) {
		super();
		try {
			this.AddModSign = addModSign;
			this.TaskId = taskId;
			this.GroupId = groupId;
			initGUI();
			// 修改
			if (AddModSign == 1) {
				fillDialogFromScheParam();
			} else {// 新增
				fillDialog();
			}
			if (UtilString.isNil(txtSchOrder.getText()).length() < 1) {
				txtSchOrder.setText(txtScheCode.getText());
			}
		} catch (Exception e) {
			Log.logError("调度对话框构造错误:", e);
		} finally {
		}
	}

	// 初始化界面
	private void initGUI() {
		try {
			setTitle("调度编辑");
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(ImageContext.Sche));
			setModal(true);
			getContentPane().setLayout(null);
			this.setBounds(0, 0, 632, 697);
			this.setResizable(false);
			int w = (Toolkit.getDefaultToolkit().getScreenSize().width - this.getWidth()) / 2;
			int h = (Toolkit.getDefaultToolkit().getScreenSize().height - this.getHeight()) / 2;
			this.setLocation(w, h);
			{
				pnlSche = new JPanel();
				getContentPane().add(pnlSche);
				pnlSche.setLayout(null);
				pnlSche.setBorder(SBorder.getTitledBorder());
				pnlSche.setBounds(2, 0, 616, 116);
				pnlSche.setBorder(SBorder.getTitledBorder("调度信息"));
				{
					lScheCode = new SLabel("\u8c03\u5ea6\u6807\u5fd7");
					pnlSche.add(lScheCode);
					lScheCode.setBounds(25, 30, 58, 14);
					String schecde = "";
					if (AddModSign == 0) {
						schecde = String.valueOf(ScherDao.getInstance().getMaxScheID() + 1);
					} else if (AddModSign == 1) {
						schecde = ScherTab.getInstance().getTblSche().getValueAt(ScherTab.getInstance().getTblSche().getSelectedRow(), 2).toString();
					}
					txtScheCode = new STextField(schecde);
					txtScheCode.setEnabled(false);
					pnlSche.add(txtScheCode);
					txtScheCode.setBounds(90, 25, 190, 21);

					lScheName = new SLabel("调度名称");
					pnlSche.add(lScheName);
					lScheName.setBounds(25, 57, 58, 14);
					txtScheName = new STextField();
					pnlSche.add(txtScheName);
					txtScheName.setBounds(90, 52, 190, 21);

					lSchetype = new SLabel("调度类型");
					pnlSche.add(lSchetype);
					lSchetype.setBounds(316, 59, 58, 14);
					cmbSchetype = new SComboBox(Const.SCHE_Type);
					pnlSche.add(cmbSchetype);
					cmbSchetype.setBounds(378, 52, 190, 21);

					lScheComent = new SLabel("调度说明");
					pnlSche.add(lScheComent);
					lScheComent.setBounds(25, 84, 60, 14);
					txtScheComent = new STextField();
					pnlSche.add(txtScheComent);
					txtScheComent.setBounds(90, 79, 190, 21);
				}
				{
					lSchOrder = new SLabel("\u8c03\u5ea6\u7f16\u53f7");
					pnlSche.add(lSchOrder);
					lSchOrder.setBounds(316, 32, 58, 14);
				}
				{
					txtSchOrder = new STextField();
					pnlSche.add(txtSchOrder);
					txtSchOrder.setBounds(378, 25, 190, 21);
				}

				pnlBegin = new JPanel();
				getContentPane().add(pnlBegin);
				pnlBegin.setLayout(null);
				pnlBegin.setBorder(SBorder.getTitledBorder());
				pnlBegin.setBounds(2, 198, 616, 99);
				{
					rbtnNow = new SRadioButton("\u7acb\u5373\u5f00\u59cb");
					pnlBegin.add(rbtnNow);
					rbtnNow.setBounds(24, 8, 84, 22);
					rbtnNow.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							RadioButtonBeginSeleted(rbtnNow);
						}
					});
				}
				{
					rbtnStartDate = new SRadioButton("\u5f00\u59cb\u65e5\u671f");
					pnlBegin.add(rbtnStartDate);
					rbtnStartDate.setBounds(24, 38, 80, 18);
					rbtnStartDate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							RadioButtonBeginSeleted(rbtnStartDate);
						}
					});
				}
				{
					txtStartDate = new STextField();
					pnlBegin.add(txtStartDate);
					txtStartDate.setBounds(104, 35, 94, 21);
					txtStartDate.setEditable(false);
				}
				{
					l1 = new SLabel("\u5f00\u59cb\u65f6\u95f4");
					pnlBegin.add(l1);
					l1.setBounds(45, 69, 58, 14);
				}
				{
					txtStartTime = new STextField("");
					pnlBegin.add(txtStartTime);
					txtStartTime.setBounds(104, 62, 94, 21);
					txtStartTime.setEditable(false);
				}
				{
					l2 = new SLabel("(\u4f8b\u5982:000001)");
					pnlBegin.add(l2);
					l2.setBounds(204, 69, 131, 14);
				}
				{
					btnBeginDate = new SButton("..");
					pnlBegin.add(btnBeginDate);
					btnBeginDate.setBounds(204, 35, 22, 21);
					btnBeginDate.setEnabled(false);
					btnBeginDate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							new SCalendar(txtStartDate);
						}
					});
				}
			}
			{
				pnlLoop = new JPanel();
				getContentPane().add(pnlLoop);
				GridLayout JPLoopLayout = new GridLayout(1, 1);
				JPLoopLayout.setColumns(1);
				JPLoopLayout.setHgap(5);
				JPLoopLayout.setVgap(5);
				pnlLoop.setLayout(JPLoopLayout);
				pnlLoop.setBorder(SBorder.getTitledBorder());
				pnlLoop.setBounds(2, 294, 616, 220);
				{
					SplLoop = new SSplitPane(1, 111, false);
					SplLoop.setEnabled(false);
					pnlLoop.add(SplLoop);
					{
						pnlLoopCon = new JPanel();
						SplLoop.add(pnlLoopCon, SSplitPane.LEFT);
						pnlLoopCon.setLayout(null);
						pnlLoopCon.setBorder(SBorder.getTitledBorder());
						pnlLoopCon.setBounds(246, 12, 120, 192);
						pnlLoopCon.setPreferredSize(new java.awt.Dimension(106, 192));
						{
							rbntOnece = new SRadioButton("\u4e00\u6b21");
							pnlLoopCon.add(rbntOnece);
							rbntOnece.setBounds(24, 7, 66, 18);
							lOnece = new SLabel("运行一次(如果反复运行,则在同一天运行)");
							lOnece.setBounds(10, 10, 300, 20);
							rbntOnece.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									RadioButtonLoopSeleted(rbntOnece);
									// JPLoop_set.add(lonece);
								}
							});

						}
						{
							rbtnSecond = new SRadioButton("\u6bcf\u79d2");
							pnlLoopCon.add(rbtnSecond);
							rbtnSecond.setBounds(24, 33, 66, 18);

							lSecond1 = new SLabel("每隔");
							lSecond2 = new SLabel("秒");
							txtSecond = new STextField();
							rbtnSecond.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									RadioButtonLoopSeleted(rbtnSecond);
									// JPLoop_set.add(lSecond1);
									lSecond1.setBounds(10, 10, 30, 20);
									// JPLoop_set.add(jtxtSecond);
									txtSecond.setBounds(lSecond1.getWidth() + 10, 10, 50, 20);
									// JPLoop_set.add(lSecond2);
									lSecond2.setBounds(lSecond1.getWidth() + txtSecond.getWidth() + 10, 10, 50, 20);
								}
							});
						}
						{
							rbtnMinute = new SRadioButton("\u6bcf\u5206");
							pnlLoopCon.add(rbtnMinute);
							rbtnMinute.setBounds(24, 60, 66, 18);
							rbtnMinute.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									RadioButtonLoopSeleted(rbtnMinute);
								}
							});
						}
						{
							rbtnHour = new SRadioButton("\u6bcf\u65f6");
							pnlLoopCon.add(rbtnHour);
							rbtnHour.setBounds(24, 87, 66, 18);
							rbtnHour.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									RadioButtonLoopSeleted(rbtnHour);
								}
							});
						}
						{
							rbtnDay = new SRadioButton("\u6bcf\u65e5");
							pnlLoopCon.add(rbtnDay);
							rbtnDay.setBounds(24, 114, 66, 18);
							rbtnDay.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									RadioButtonLoopSeleted(rbtnDay);
								}
							});
						}
						{
							rbtnWeek = new SRadioButton("\u6bcf\u5468");
							pnlLoopCon.add(rbtnWeek);
							rbtnWeek.setBounds(24, 141, 66, 18);
							rbtnWeek.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									RadioButtonLoopSeleted(rbtnWeek);
								}
							});
						}
						{
							rbtnMonth = new SRadioButton("\u6bcf\u6708");
							pnlLoopCon.add(rbtnMonth);
							rbtnMonth.setBounds(24, 168, 66, 18);
							rbtnMonth.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									RadioButtonLoopSeleted(rbtnMonth);
								}
							});
						}
					}
					addOnecePanel();
				}
			}
			{
				pnlEnd = new JPanel();
				getContentPane().add(pnlEnd);
				pnlEnd.setLayout(null);
				pnlEnd.setBorder(SBorder.getTitledBorder());
				pnlEnd.setBounds(2, 514, 616, 102);
				{
					l3 = new SLabel("(\u4f8b\u5982:00:00:00)");
					pnlEnd.add(l3);
					l3.setBounds(202, 72, 131, 14);
				}
				{
					l4 = new SLabel("\u7ed3\u675f\u65f6\u95f4");
					pnlEnd.add(l4);
					l4.setBounds(45, 65, 62, 22);
				}
				{
					rbtnNoEnddate = new SRadioButton("\u65e0");
					pnlEnd.add(rbtnNoEnddate);
					rbtnNoEnddate.setBounds(24, 12, 84, 22);
					rbtnNoEnddate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							RadioButtonEndSeleted(rbtnNoEnddate);
						}
					});
				}
				{
					rbtnEndDate = new SRadioButton("\u7ed3\u675f\u65e5\u671f");
					pnlEnd.add(rbtnEndDate);
					rbtnEndDate.setBounds(24, 40, 80, 22);
					rbtnEndDate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							RadioButtonEndSeleted(rbtnEndDate);
						}
					});
				}
				{
					txtEndDate = new STextField();
					pnlEnd.add(txtEndDate);
					txtEndDate.setBounds(104, 44, 95, 21);
					txtEndDate.setEditable(false);
				}
				{
					txtEndTime = new STextField("");
					pnlEnd.add(txtEndTime);
					txtEndTime.setBounds(104, 69, 95, 21);
					txtEndTime.setEditable(false);
				}
				{
					btnEndDate = new SButton("..");
					pnlEnd.add(btnEndDate);
					btnEndDate.setBounds(205, 44, 22, 21);
					btnEndDate.setEnabled(false);
					btnEndDate.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							new SCalendar(txtEndDate);
						}
					});
				}
			}
			{
				btnOk = new SButton("确  定", ImageContext.Ok);
				getContentPane().add(btnOk);
				btnOk.setBounds(206, 629, 100, 25);
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnOk();
					}
				});
			}
			{
				btnCancle = new SButton("取  消", ImageContext.Exit);
				getContentPane().add(btnCancle);
				btnCancle.setBounds(318, 629, 100, 25);
				btnCancle.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnCancel();
					}
				});
			}
			{
				pnlDateTime = new JPanel();
				getContentPane().add(pnlDateTime);
				pnlDateTime.setBounds(2, 116, 616, 41);
				pnlDateTime.setBorder(SBorder.getTitledBorder());
				pnlDateTime.setLayout(null);
				{
					lDateType = new SLabel("\u65e5\u671f\u7c7b\u578b");
					pnlDateTime.add(lDateType);
					lDateType.setBounds(25, 16, 60, 14);
				}
				{
					cmbDateType = new SComboBox(UtilString.splitStr(VariableApp.systemParamsValue.getDateType(), 0));
					pnlDateTime.add(cmbDateType);
					cmbDateType.setSelectedIndex(0);
					cmbDateType.setBounds(90, 9, 150, 21);
				}
				{
					lExecTime = new SLabel("\u6267\u884c\u65f6\u6bb5");
					pnlDateTime.add(lExecTime);
					lExecTime.setBounds(316, 16, 58, 14);
				}
				{
					cmbExecTime = new SComboBox(SettingsDao.getInstance().getValueS("执行时段"));
					pnlDateTime.add(cmbExecTime);
					cmbExecTime.setSelectedIndex(0);
					cmbExecTime.setBounds(378, 9, 190, 21);
					cmbExecTime.setEditable(true);
				}
			}
			{
				pnlSpecialDate = new JPanel();
				getContentPane().add(pnlSpecialDate);
				pnlSpecialDate.setBorder(SBorder.getTitledBorder());
				pnlSpecialDate.setLayout(null);
				pnlSpecialDate.setBounds(2, 156, 616, 40);
				{
					lSpecialDate = new SLabel("特殊日期");
					pnlSpecialDate.add(lSpecialDate);
					lSpecialDate.setBounds(25, 12, 48, 18);
				}
				{
					cmbSpecialDate = new SComboBox(Const.SpecialDate);
					pnlSpecialDate.add(cmbSpecialDate);
					cmbSpecialDate.setSelectedIndex(0);
					cmbSpecialDate.setBounds(90, 9, 150, 21);
				}
				{
					chkNowDate = new SCheckBox("当前执行日期");
					pnlSpecialDate.add(chkNowDate);
					chkNowDate.setBounds(312, 9, 150, 21);
				}
			}
			rbtnNow.setSelected(true);
			rbntOnece.setSelected(true);
			rbtnNoEnddate.setSelected(true);
		} catch (Exception e) {
			Log.logError("调度对话框初始化界面错误:", e);
		} finally {
		}

	}

	// 新增时填充
	private void fillDialog() {
		if ("".equals(UtilString.isNil(this.TaskId))) {
			TaskGroup tg = TaskGroupDao.getInstance().getTaskGroupFromGroupID(Long.valueOf(this.GroupId));
			txtScheName.setText(tg.getGroupName());
			txtScheComent.setText(tg.getGroupMemo());
		} else {
			ITask task = TaskDao.getInstance().getMapTask(Long.valueOf(this.TaskId));
			txtScheName.setText(task.getTaskName());
			txtScheComent.setText(task.getTaskMemo());
		}
	}

	// 填充界面
	private void fillDialogFromScheParam() {
		try {
			ScherParam sp = ScherDao.getInstance().getScheParamsFromSchCde(
					Long.valueOf(ScherTab.getInstance().getTblSche().getValueAt(ScherTab.getInstance().getTblSche().getSelectedRow(), 2).toString()));
			txtSchOrder.setText(sp.getSchOrder() == null ? "" : sp.getSchOrder());
			txtScheCode.setText(sp.getSchCde() == null ? "" : sp.getSchCde());
			txtScheName.setText(sp.getSchNme());
			txtScheComent.setText(sp.getSchComent());
			cmbSchetype.setSelectedIndex(Integer.valueOf(sp.getSchType()));

			cmbDateType.setSelectedItem(sp.getDateType() == null ? "全部" : sp.getDateType());
			cmbExecTime.setSelectedItem(sp.getExecTime() == null ? "全部" : sp.getExecTime());
			cmbSpecialDate.setSelectedItem(sp.getSpecialDate() == null ? "全部" : sp.getSpecialDate());
			chkNowDate.setSelected(sp.isNowDate());
			rbtnNow.setSelected(false);
			rbntOnece.setSelected(false);
			if ("now".equals(sp.getStartWhen())) {
				rbtnNow.setSelected(true);
			} else {
				rbtnStartDate.setSelected(true);
				txtStartDate.setText(sp.getStartDate());
				txtStartTime.setText(sp.getStartTime());
				txtStartTime.setEditable(true);
				btnBeginDate.setEnabled(true);
			}

			if ("once".equals(sp.getRecurPrimary())) {
				rbntOnece.setSelected(true);
			} else if ("persecond".equals(sp.getRecurPrimary())) {
				rbtnSecond.setSelected(true);
				addSecondPanel();
				txtSecond1.setText(sp.getSecondN());
			} else if ("perminute".equals(sp.getRecurPrimary())) {
				rbtnMinute.setSelected(true);
				addMinutePanel();
				txtMinuteM.setText(sp.getMinuteN());
				txtMinuteS.setText(sp.getPerMinuteSecond());
			} else if ("hourly".equals(sp.getRecurPrimary())) {
				rbtnHour.setSelected(true);
				addHourPanel();
				txtHourH.setText(sp.getHourlyN());
				txtHhourM.setText(sp.getHourlyMinute());
				txtHourS.setText(sp.getHourlySecond());
			} else if ("dailyday".equals(sp.getRecurPrimary())) {
				rbtnDay.setSelected(true);
				addDayPanel();
				txtDay.setText(sp.getDailyN());
			} else if ("weekly".equals(sp.getRecurPrimary())) {
				rbtnWeek.setSelected(true);
				addWeekPanel();
				txtWeek.setText(sp.getWeeklyN());
				String week = sp.getWeek();
				// String[] sweek=new
				// String[]{"sun","mon","tue","wed","thu","fri","sat"};
				String[] weekSche = week.split(",");
				for (int i = 0; i < weekSche.length; i++) {
					if (weekSche[i].equals("sun"))
						chkSunday.setSelected(true);
					if (weekSche[i].equals("mon"))
						chkMonday.setSelected(true);
					if (weekSche[i].equals("tue"))
						chkTuesday.setSelected(true);
					if (weekSche[i].equals("wed"))
						chkWednesday.setSelected(true);
					if (weekSche[i].equals("thu"))
						chkThursday.setSelected(true);
					if (weekSche[i].equals("fri"))
						chkFriday.setSelected(true);
					if (weekSche[i].equals("sat"))
						chkSaturday.setSelected(true);
				}

			} else if ("monthly".equals(sp.getRecurPrimary())) {
				rbtnMonth.setSelected(true);
				addMonthPanel();
				if ("monthlyday".equals(sp.getRecurMonthly())) {
					rbtnMonthDay.setSelected(true);
					rbtnMonthWeek.setSelected(false);
					txtMonthDay.setText(sp.getMonthlyDay());
				} else if ("monthlynth".equals(sp.getRecurMonthly())) {
					rbtnMonthDay.setSelected(false);
					rbtnMonthWeek.setSelected(true);
					cmbMonthIndex.setSelectedIndex(Integer.valueOf(sp.getMonthlyNth()) - 1);
					cmbMonthWeek.setSelectedIndex(Integer.valueOf(sp.getMonthlyDOW()) - 1);

				}
				String month = sp.getMonth();
				String[] monthSche = month.split(",");
				for (int i = 0; i < monthSche.length; i++) {
					if (monthSche[i].equals("jan"))
						chkJanuary.setSelected(true);
					if (monthSche[i].equals("feb"))
						chkFebruary.setSelected(true);
					if (monthSche[i].equals("mar"))
						chkMarch.setSelected(true);
					if (monthSche[i].equals("apr"))
						chkApril.setSelected(true);
					if (monthSche[i].equals("may"))
						chkMay.setSelected(true);
					if (monthSche[i].equals("jun"))
						chkJune.setSelected(true);
					if (monthSche[i].equals("jul"))
						chkJuly.setSelected(true);
					if (monthSche[i].equals("aug"))
						chkAugust.setSelected(true);
					if (monthSche[i].equals("sep"))
						chkSeptember.setSelected(true);
					if (monthSche[i].equals("oct"))
						chkOctober.setSelected(true);
					if (monthSche[i].equals("nov"))
						chkNovember.setSelected(true);
					if (monthSche[i].equals("dec"))
						chkDecember.setSelected(true);
				}

			}
			rbtnNoEnddate.setSelected(false);
			rbtnEndDate.setSelected(false);
			txtEndTime.setEditable(false);
			if ("none".equals(sp.getEndDate())) {
				rbtnNoEnddate.setSelected(true);
			} else if ("by".equals(sp.getEndDate())) {
				rbtnEndDate.setSelected(true);
				txtEndDate.setText(sp.getEndBy());
				txtEndTime.setText(sp.getEndTime());
				txtEndTime.setEditable(true);
				btnEndDate.setEnabled(true);
			}
		} catch (Exception e) {
			Log.logError("调度对话框填充界面错误:", e);
		} finally {
		}
	}

	// 确定
	private void btnOk() {
		try {
			this.scheParam = new ScherParam();
			if (fillScheParamFromDialog(scheParam) == false) {
				return;
			}
			// 先停掉调度
			JobOperater.removeJob(scheParam.getSchCde());
			if (AddModSign == 0) {
				ScherDao.getInstance().addSche(this.scheParam);
				// 重启调度
				new ScherRunAction(this.scheParam).run();

			} else if (AddModSign == 1) {
				ScherParam sp = ScherDao.getInstance().getScheParamsFromSchCde(Long.valueOf(this.scheParam.getSchCde()));
				String status = sp.getStatus();
				ScherDao.getInstance().modSche(this.scheParam);
				if (status != null) {
					// 重启调度
					if (status.equals("正常"))
						new ScherRunAction(this.scheParam).run();
				}
			}
			JobOperater.refreshSche();
			this.dispose();
		} catch (Exception e) {
			Log.logError("调度对话框保存错误:", e);
		} finally {
		}
	}

	// 取消

	// 取消
	private void btnCancel() {
		try {
			this.dispose();
		} catch (Exception e) {
			Log.logError("调度对话框退出错误:", e);
		} finally {
		}
	}

	// 实例化调度对象

	// 实例化调度实例
	private boolean fillScheParamFromDialog(ScherParam scheParam) {
		boolean result = true;
		try {
			String addTime = UtilConver.dateToStr(Const.fm_yyyyMMdd);
			scheParam.setAddTime(addTime);
			scheParam.setSchOrder(txtSchOrder.getText() == null ? "" : txtSchOrder.getText());
			scheParam.setTaskID(this.TaskId);
			scheParam.setGroupID(this.GroupId);
			scheParam.setSchCde(txtScheCode.getText() == null ? "" : txtScheCode.getText());
			scheParam.setSchNme(txtScheName.getText() == null ? "" : txtScheName.getText());
			scheParam.setSchType(String.valueOf(cmbSchetype.getSelectedIndex()));
			scheParam.setSchComent(txtScheComent.getText() == null ? "" : txtScheComent.getText());
			scheParam.setDateType(cmbDateType.getSelectedItem().toString() == null ? "全部" : cmbDateType.getSelectedItem().toString());
			scheParam.setExecTime(cmbExecTime.getSelectedItem().toString() == null ? "全部" : cmbExecTime.getSelectedItem().toString());
			scheParam.setSpecialDate(cmbSpecialDate.getSelectedItem().toString() == null ? "全部" : cmbSpecialDate.getSelectedItem().toString());
			scheParam.setNowDate(chkNowDate.isSelected());
			if (rbtnNow.isSelected())
				scheParam.setStartWhen("now");
			if (rbtnStartDate.isSelected()) {
				String startTime = txtStartTime.getText() == null ? "" : txtStartTime.getText();
				if (startTime.trim().length() < 1) {
					ShowMsg.showMsg("请输入开始时间,格式为:(HHmmss)");
					return false;
				}
				scheParam.setStartWhen("dateTime");
				scheParam.setStartDate(txtStartDate.getText() == null ? "" : txtStartDate.getText());
				scheParam.setStartTime(startTime);
			}
			if (rbntOnece.isSelected()) {
				scheParam.setRecurPrimary("once");
			}
			if (rbtnSecond.isSelected()) {
				String second = txtSecond1.getText() == null ? "" : txtSecond1.getText();
				if (second.trim().length() < 1) {
					ShowMsg.showMsg("请输入每隔多少秒");
					return false;
				}
				scheParam.setRecurPrimary("persecond");
				scheParam.setSecondN(second);
			}
			if (rbtnMinute.isSelected()) {
				String MinuteN = txtMinuteM.getText() == null ? "" : txtMinuteM.getText();
				if (MinuteN.trim().length() < 1) {
					ShowMsg.showMsg("请输入每隔多少分");
					return false;
				}
				String MinuteSecond = txtMinuteS.getText() == null ? "" : txtMinuteS.getText();
				if (MinuteSecond.trim().length() < 1) {
					ShowMsg.showMsg("请输入在第几秒");
					return false;
				}
				scheParam.setRecurPrimary("perminute");
				scheParam.setMinuteN(MinuteN);
				scheParam.setPerMinuteSecond(MinuteSecond);
			}
			if (rbtnHour.isSelected()) {
				String HourlyN = txtHourH.getText() == null ? "" : txtHourH.getText();
				if (HourlyN.trim().length() < 1) {
					ShowMsg.showMsg("请输入每隔多少小时");
					return false;
				}

				String HourlyMinute = txtHhourM.getText() == null ? "" : txtHhourM.getText();
				if (HourlyMinute.trim().length() < 1) {
					ShowMsg.showMsg("请输入在第几分钟");
					return false;
				}

				String HourlySecond = txtHourS.getText() == null ? "" : txtHourS.getText();
				if (HourlySecond.trim().length() < 1) {
					ShowMsg.showMsg("请输入在第几秒");
					return false;
				}

				scheParam.setRecurPrimary("hourly");
				scheParam.setHourlyN(HourlyN);
				scheParam.setHourlyMinute(HourlyMinute);
				scheParam.setHourlySecond(HourlySecond);
			}
			if (rbtnDay.isSelected()) {
				String DailyN = txtDay.getText() == null ? "" : txtDay.getText();
				if (DailyN.trim().length() < 1) {
					ShowMsg.showMsg("请输入每隔多少天");
					return false;
				}
				scheParam.setRecurPrimary("dailyday");
				scheParam.setDailyN(DailyN);
			}
			if (rbtnWeek.isSelected()) {
				String WeeklyN = txtWeek.getText() == null ? "" : txtWeek.getText();

				scheParam.setRecurPrimary("weekly");
				scheParam.setWeeklyN(WeeklyN);
				String week = "";
				if (chkSunday.isSelected()) {
					week = week + "sun" + ",";
				}
				if (chkMonday.isSelected()) {
					week = week + "mon" + ",";
				}
				if (chkTuesday.isSelected()) {
					week = week + "tue" + ",";
				}
				if (chkWednesday.isSelected()) {
					week = week + "wed" + ",";
				}
				if (chkThursday.isSelected()) {
					week = week + "thu" + ",";
				}
				if (chkFriday.isSelected()) {
					week = week + "fri" + ",";
				}
				if (chkSaturday.isSelected()) {
					week = week + "sat" + ",";
				}
				if (week.length() < 1) {
					ShowMsg.showMsg("请至少勾选每周中的一天");
					return false;
				} else
					scheParam.setWeek(week);
			}

			if (rbtnMonth.isSelected()) {
				scheParam.setRecurPrimary("monthly");
				if (rbtnMonthDay.isSelected()) {
					String MonthlyDay = txtMonthDay.getText() == null ? "" : txtMonthDay.getText();
					if (MonthlyDay.trim().length() < 1) {
						ShowMsg.showMsg("请输入每月在第几日");
						return false;
					}
					scheParam.setRecurMonthly("monthlyday");
					scheParam.setMonthlyDay(MonthlyDay);
				}
				if (rbtnMonthWeek.isSelected()) {
					scheParam.setRecurMonthly("monthlynth");
					scheParam.setMonthlyNth(String.valueOf(cmbMonthIndex.getSelectedIndex() + 1));
					scheParam.setMonthlyDOW(String.valueOf(cmbMonthWeek.getSelectedIndex() + 1));
				}
				String month = "";
				if (chkJanuary.isSelected())
					month = month + "jan" + ",";
				if (chkFebruary.isSelected())
					month = month + "feb" + ",";
				if (chkMarch.isSelected())
					month = month + "mar" + ",";
				if (chkApril.isSelected())
					month = month + "apr" + ",";
				if (chkMay.isSelected())
					month = month + "may" + ",";
				if (chkJune.isSelected())
					month = month + "jun" + ",";
				if (chkJuly.isSelected())
					month = month + "jul" + ",";
				if (chkAugust.isSelected())
					month = month + "aug" + ",";
				if (chkSeptember.isSelected())
					month = month + "sep" + ",";
				if (chkOctober.isSelected())
					month = month + "oct" + ",";
				if (chkNovember.isSelected())
					month = month + "nov" + ",";
				if (chkDecember.isSelected())
					month = month + "dec" + ",";

				if (month.length() < 1) {
					ShowMsg.showMsg("请至少勾选一个月");
					return false;

				}
				scheParam.setMonth(month);
			}
			if (rbtnNoEnddate.isSelected()) {
				scheParam.setEndDate("none");
			}
			if (rbtnEndDate.isSelected()) {
				scheParam.setEndDate("by");

				scheParam.setEndBy(txtEndDate.getText() == null ? "" : txtEndDate.getText());
				String EndTime = txtEndTime.getText() == null ? "" : txtEndTime.getText();
				if (EndTime.trim().length() < 1) {
					ShowMsg.showMsg("请输入结束时间");
					return false;
				}
				scheParam.setEndTime(EndTime);

				if (rbtnStartDate.isSelected()) {
					int i = 0;
					if (scheParam.getStartDate().equals(""))
						i += 1;
					if (scheParam.getEndBy().equals(""))
						i += 1;
					if (i == 0 && (scheParam.getEndBy() + " " + scheParam.getEndTime()).compareTo(scheParam.getStartDate() + " " + scheParam.getStartTime()) < 0) {
						ShowMsg.showMsg("结束时间不能小于开始时间");
						return false;
					}
					if (scheParam.getEndBy().trim().length() <= 0) {
						ShowMsg.showMsg("请输入结束日期");
						return false;
					}
					if (scheParam.getStartDate().trim().length() > 0 && scheParam.getEndBy().trim().length() <= 0) {
						ShowMsg.showMsg("有开始日期，必须输入结束日期");
						return false;
					}

				}
				if (rbtnNow.isSelected()) {
					if (rbtnEndDate.isSelected()) {
						String nowdate = UtilConver.dateToStr(Const.fm_yyyyMMdd);
						String nowtime = UtilConver.dateToStr(Const.fm_HHmmss);
						int i = 0;
						if (scheParam.getEndBy() == null)
							i += 1;
						if (scheParam.getEndBy().equals(""))
							i += 1;
						if (i == 0 && (scheParam.getEndBy() + " " + scheParam.getEndTime()).compareTo(nowdate) < 0) {
							ShowMsg.showMsg("结束时间不能小于当前时间");
							return false;
						}
						if (i > 0 && scheParam.getEndTime().compareTo(nowtime) < 0) {
							ShowMsg.showMsg("结束时间不能小于当前时间");
							return false;
						}
					}
				}

				if (rbtnEndDate.isSelected()) {
					int i = 0;
					if (scheParam.getEndBy() == null)
						i += 1;
					if (scheParam.getEndBy().equals(""))
						i += 1;
					if (i == 0 && (scheParam.getEndBy() + " " + scheParam.getEndTime()).compareTo(UtilConver.dateToStr(new Date(), Const.fm_yyyyMMdd_HHmmss)) < 0) {
						ShowMsg.showMsg("结束时间不能小于当前时间");
						return false;
					}
				}
			}
			if (cmbSpecialDate.getSelectedIndex() != 0) {
				if (cmbDateType.getSelectedIndex() != 1) {
					ShowMsg.showMsg("特殊日期只支持沪深交易日，请选择日期类型为沪深交易日！");
					return false;
				}
			}
		} catch (Exception e) {
			Log.logError("调度对话框赋值调度对象错误:", e);
			return false;
		} finally {
		}
		return result;
	}

	// 频率选择控制

	// 频率选择控制
	private void RadioButtonLoopSeleted(SRadioButton radioButton) {
		try {
			rbntOnece.setSelected(false);
			rbtnSecond.setSelected(false);
			rbtnMinute.setSelected(false);
			rbtnHour.setSelected(false);
			rbtnDay.setSelected(false);
			rbtnWeek.setSelected(false);
			rbtnMonth.setSelected(false);
			radioButton.setSelected(true);

			if (radioButton.equals(rbntOnece))
				addOnecePanel();
			else if (radioButton.equals(rbtnSecond))
				addSecondPanel();
			else if (radioButton.equals(rbtnMinute))
				addMinutePanel();
			else if (radioButton.equals(rbtnHour))
				addHourPanel();
			else if (radioButton.equals(rbtnDay))
				addDayPanel();
			else if (radioButton.equals(rbtnWeek))
				addWeekPanel();
			else if (radioButton.equals(rbtnMonth))
				addMonthPanel();
		} catch (Exception e) {
			Log.logError("调度对话框选择调度时间类型错误:", e);
		} finally {
		}

	}

	// 开始选择控制

	// 开始时间选择控制
	private void RadioButtonBeginSeleted(SRadioButton radioButton) {
		try {
			rbtnStartDate.setSelected(false);
			rbtnNow.setSelected(false);
			radioButton.setSelected(true);
			if (rbtnStartDate.isSelected()) {
				btnBeginDate.setEnabled(true);
				txtStartTime.setEditable(true);
				txtStartTime.setText("00:00:00");
				// jtxtStartDate.setText(fromatdate.format(new Date()));
			} else {
				btnBeginDate.setEnabled(false);
				txtStartTime.setEditable(false);
				txtStartTime.setText("");
				txtStartDate.setText("");
			}
		} catch (Exception e) {
			Log.logError("调度对话框选择开始日期错误:", e);
		} finally {
		}
	}

	// 结束日期选择控制

	// 结束选择控制
	private void RadioButtonEndSeleted(SRadioButton radioButton) {
		try {
			rbtnEndDate.setSelected(false);
			rbtnNoEnddate.setSelected(false);
			radioButton.setSelected(true);
			if (rbtnEndDate.isSelected()) {
				btnEndDate.setEnabled(true);
				txtEndTime.setEditable(true);
				txtEndTime.setText("23:59:59");
			} else {
				btnEndDate.setEnabled(false);
				txtEndTime.setEditable(false);
				txtEndTime.setText("");
				txtEndDate.setText("");
			}
		} catch (Exception e) {
			Log.logError("调度对话框选择结束日期错误:", e);
		} finally {
		}
	}

	// 月份选择控制

	// 月份选择控制
	private void RadioButtonMonthSeleted(SRadioButton radioButton) {
		try {
			rbtnMonthDay.setSelected(false);
			rbtnMonthWeek.setSelected(false);
			radioButton.setSelected(true);
		} catch (Exception e) {
			Log.logError("调度对话框选择月份错误:", e);
		} finally {
		}
	}

	// 添加每周面板
	private void addWeekPanel() {
		try {
			if (pnlWeek == null)
				pnlWeek = new JPanel();

			SplLoop.add(pnlWeek, SSplitPane.RIGHT);
			pnlWeek.setPreferredSize(new java.awt.Dimension(312, 192));
			pnlWeek.setLayout(null);
			{
				if (lWeek == null)
					lWeek = new SLabel("每隔");
				pnlWeek.add(lWeek);
				lWeek.setBounds(22, 16, 39, 14);
				lWeek.setVisible(false);
			}
			{
				if (txtWeek == null)
					txtWeek = new STextField();
				pnlWeek.add(txtWeek);
				txtWeek.setBounds(53, 9, 59, 21);
				txtWeek.setVisible(false);
			}
			{
				if (lweek1 == null)
					lweek1 = new SLabel("周");
				pnlWeek.add(lweek1);
				lweek1.setBounds(120, 16, 20, 14);
				lweek1.setVisible(false);
			}
			{
				if (chkSunday == null)
					chkSunday = new SCheckBox("星期天");
				pnlWeek.add(chkSunday);
				chkSunday.setBounds(30, 45, 68, 18);
			}
			{
				if (chkMonday == null)
					chkMonday = new SCheckBox("星期一");
				pnlWeek.add(chkMonday);
				chkMonday.setBounds(95, 45, 68, 18);
			}
			{
				if (chkTuesday == null)
					chkTuesday = new SCheckBox("星期二");
				pnlWeek.add(chkTuesday);
				chkTuesday.setBounds(160, 45, 68, 18);
			}
			{
				if (chkWednesday == null)
					chkWednesday = new SCheckBox("星期三");
				pnlWeek.add(chkWednesday);
				chkWednesday.setBounds(225, 45, 68, 18);
			}
			{
				if (chkThursday == null)
					chkThursday = new SCheckBox("星期四");
				pnlWeek.add(chkThursday);
				chkThursday.setBounds(30, 65, 68, 18);
			}
			{
				if (chkFriday == null)
					chkFriday = new SCheckBox("星期五");
				pnlWeek.add(chkFriday);
				chkFriday.setBounds(95, 65, 68, 18);
			}
			{
				if (chkSaturday == null)
					chkSaturday = new SCheckBox("星期六");
				pnlWeek.add(chkSaturday);
				chkSaturday.setBounds(160, 65, 68, 18);
			}
		} catch (Exception e) {
			Log.logError("调度对话框初始化每周界面面板错误:", e);
		} finally {
		}
	}

	// 添加每月面板

	// 每月
	private void addMonthPanel() {
		// 每月
		try {
			if (pnlMonth == null)
				pnlMonth = new JPanel();
			SplLoop.add(pnlMonth, SSplitPane.RIGHT);
			pnlMonth.setPreferredSize(new java.awt.Dimension(312, 192));
			pnlMonth.setLayout(null);
			{
				if (rbtnMonthDay == null)
					rbtnMonthDay = new SRadioButton(" 在");
				rbtnMonthDay.setSelected(true);
				pnlMonth.add(rbtnMonthDay);
				rbtnMonthDay.setBounds(26, 12, 51, 21);
				rbtnMonthDay.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						RadioButtonMonthSeleted(rbtnMonthDay);
					}
				});
			}
			{
				if (txtMonthDay == null)
					txtMonthDay = new STextField();
				pnlMonth.add(txtMonthDay);
				txtMonthDay.setBounds(83, 12, 40, 21);
			}
			{
				if (lMonthDay == null)
					lMonthDay = new SLabel("日");
				pnlMonth.add(lMonthDay);
				lMonthDay.setBounds(126, 19, 20, 14);
			}
			{
				if (rbtnMonthWeek == null)
					rbtnMonthWeek = new SRadioButton(" 在");
				pnlMonth.add(rbtnMonthWeek);
				rbtnMonthWeek.setBounds(26, 40, 51, 21);
				rbtnMonthWeek.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						RadioButtonMonthSeleted(rbtnMonthWeek);
					}
				});
			}
			{
				cmbMonthWeek = new SComboBox(new String[] { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" });
				pnlMonth.add(cmbMonthWeek);
				cmbMonthWeek.setBounds(158, 40, 72, 21);
			}
			{
				cmbMonthIndex = new SComboBox(new String[] { "第一个", "第二个", "第三个", "第四个", "最后一个" });
				pnlMonth.add(cmbMonthIndex);
				cmbMonthIndex.setBounds(79, 40, 72, 21);
			}
			{
				if (chkJanuary == null)
					chkJanuary = new SCheckBox("\u4e00\u6708");
				pnlMonth.add(chkJanuary);
				chkJanuary.setBounds(24, 73, 57, 18);
			}
			{
				if (chkFebruary == null)
					chkFebruary = new SCheckBox("\u4e8c\u6708");
				pnlMonth.add(chkFebruary);
				chkFebruary.setBounds(86, 73, 57, 18);
			}
			{
				if (chkMarch == null)
					chkMarch = new SCheckBox("\u4e09\u6708");
				pnlMonth.add(chkMarch);
				chkMarch.setBounds(149, 73, 57, 18);
			}
			{
				if (chkApril == null)
					chkApril = new SCheckBox("\u56db\u6708");
				pnlMonth.add(chkApril);
				chkApril.setBounds(211, 73, 57, 18);
			}
			{
				if (chkMay == null)
					chkMay = new SCheckBox("\u4e94\u6708");
				pnlMonth.add(chkMay);
				chkMay.setBounds(274, 73, 57, 18);
			}
			{
				if (chkJune == null)
					chkJune = new SCheckBox("\u516d\u6708");
				pnlMonth.add(chkJune);
				chkJune.setBounds(337, 73, 57, 18);
			}
			{
				if (chkJuly == null)
					chkJuly = new SCheckBox("\u4e03\u6708");
				pnlMonth.add(chkJuly);
				chkJuly.setBounds(24, 97, 57, 18);
			}
			{
				if (chkAugust == null)
					chkAugust = new SCheckBox("\u516b\u6708");
				pnlMonth.add(chkAugust);
				chkAugust.setBounds(86, 97, 57, 18);
			}
			{
				if (chkSeptember == null)
					chkSeptember = new SCheckBox("\u4e5d\u6708");
				pnlMonth.add(chkSeptember);
				chkSeptember.setBounds(149, 97, 57, 18);
			}
			{
				if (chkOctober == null)
					chkOctober = new SCheckBox("\u5341\u6708");
				pnlMonth.add(chkOctober);
				chkOctober.setBounds(211, 97, 57, 18);
			}
			{
				if (chkNovember == null)
					chkNovember = new SCheckBox("\u5341\u4e00\u6708");
				pnlMonth.add(chkNovember);
				chkNovember.setBounds(274, 97, 67, 18);
			}
			{
				if (chkDecember == null)
					chkDecember = new SCheckBox("\u5341\u4e8c\u6708");
				pnlMonth.add(chkDecember);
				chkDecember.setBounds(337, 97, 67, 18);
			}
		} catch (Exception e) {
			Log.logError("调度对话框初始化每月界面面板错误:", e);
		} finally {
		}
	}

	// 每次

	// 添加执行一次面板
	private void addOnecePanel() {
		// 一次
		try {
			if (pnlOnece == null)
				pnlOnece = new JPanel();

			SplLoop.add(pnlOnece, SSplitPane.RIGHT);
			pnlOnece.setPreferredSize(new java.awt.Dimension(312, 192));
			pnlOnece.setLayout(null);
			{
				if (lOnece1 == null)
					lOnece1 = new SLabel("运行一次(如果反复运行,则在同一天运行)");
				pnlOnece.add(lOnece1);
				lOnece1.setBounds(24, 12, 343, 14);
			}
		} catch (Exception e) {
			Log.logError("调度对话框初始化一次性界面面板错误:", e);
		} finally {
		}
	}

	// 添加每秒面板
	private void addSecondPanel() {
		// 每秒
		try {
			if (pnlSecond == null)
				pnlSecond = new JPanel();
			SplLoop.add(pnlSecond, SSplitPane.RIGHT);
			pnlSecond.setPreferredSize(new java.awt.Dimension(312, 192));
			pnlSecond.setLayout(null);
			{
				if (lSecond == null)
					lSecond = new SLabel("每隔");
				pnlSecond.add(lSecond);
				lSecond.setBounds(22, 16, 39, 14);
			}
			{
				if (txtSecond1 == null)
					txtSecond1 = new STextField();
				pnlSecond.add(txtSecond1);
				txtSecond1.setBounds(53, 9, 59, 21);
			}
			{
				if (lSecond3 == null)
					lSecond3 = new SLabel("秒");
				pnlSecond.add(lSecond3);
				lSecond3.setBounds(120, 16, 20, 14);
			}
		} catch (Exception e) {
			Log.logError("调度对话框初始化每秒界面面板错误:", e);
		} finally {
		}
	}

	// 每分

	// 添加每分钟面板
	private void addMinutePanel() {
		// 每分
		try {
			if (pnlMinute == null)
				pnlMinute = new JPanel();
			SplLoop.add(pnlMinute, SSplitPane.RIGHT);
			pnlMinute.setPreferredSize(new java.awt.Dimension(312, 192));
			pnlMinute.setLayout(null);
			{
				if (lMinute == null)
					lMinute = new SLabel("每隔");
				pnlMinute.add(lMinute);
				lMinute.setBounds(22, 16, 39, 14);
			}
			{
				if (txtMinuteM == null)
					txtMinuteM = new STextField();
				pnlMinute.add(txtMinuteM);
				txtMinuteM.setBounds(53, 9, 59, 21);
			}
			{
				if (lMinute1 == null)
					lMinute1 = new SLabel("分");
				pnlMinute.add(lMinute1);
				lMinute1.setBounds(120, 16, 20, 14);
			}
			{
				if (lMinute2 == null)
					lMinute2 = new SLabel("在");
				pnlMinute.add(lMinute2);
				lMinute2.setBounds(22, 43, 25, 14);
			}
			{
				if (txtMinuteS == null)
					txtMinuteS = new STextField();
				pnlMinute.add(txtMinuteS);
				txtMinuteS.setBounds(53, 36, 59, 21);
			}
			{
				if (lMinute3 == null)
					lMinute3 = new SLabel("秒");
				pnlMinute.add(lMinute3);
				lMinute3.setBounds(120, 43, 20, 14);
			}
		} catch (Exception e) {
			Log.logError("调度对话框初始化每分界面面板错误:", e);
		} finally {
		}
	}

	// 每时

	// 添加每小时面板
	private void addHourPanel() {
		// 每时
		try {
			if (pnlHour == null)
				pnlHour = new JPanel();
			SplLoop.add(pnlHour, SSplitPane.RIGHT);
			pnlHour.setPreferredSize(new java.awt.Dimension(312, 192));
			pnlHour.setLayout(null);
			{
				if (lHour == null)
					lHour = new SLabel("每隔");
				pnlHour.add(lHour);
				lHour.setBounds(22, 16, 39, 14);
			}
			{
				if (txtHourH == null)
					txtHourH = new STextField();
				pnlHour.add(txtHourH);
				txtHourH.setBounds(53, 9, 59, 21);
			}
			{
				if (lHour1 == null)
					lHour1 = new SLabel("小时");
				pnlHour.add(lHour1);
				lHour1.setBounds(120, 16, 35, 14);
			}
			{
				if (lHour2 == null)
					lHour2 = new SLabel("在");
				pnlHour.add(lHour2);
				lHour2.setBounds(22, 43, 25, 14);
			}
			{
				if (txtHhourM == null)
					txtHhourM = new STextField();
				pnlHour.add(txtHhourM);
				txtHhourM.setBounds(53, 36, 59, 21);
			}
			{
				if (lhour3 == null)
					lhour3 = new SLabel("分");
				pnlHour.add(lhour3);
				lhour3.setBounds(120, 43, 20, 14);
			}

			{
				if (txtHourS == null)
					txtHourS = new STextField();
				pnlHour.add(txtHourS);
				txtHourS.setBounds(150, 36, 59, 21);
			}
			{
				if (lHour4 == null)
					lHour4 = new SLabel("秒");
				pnlHour.add(lHour4);
				lHour4.setBounds(210, 43, 20, 14);
			}
		} catch (Exception e) {
			Log.logError("调度对话框每时界面面板错误:", e);
		} finally {
		}
	}

	// 每天

	// 添加每天面板
	private void addDayPanel() {
		// 每日
		try {
			if (pnlDay == null)
				pnlDay = new JPanel();
			SplLoop.add(pnlDay, SSplitPane.RIGHT);
			pnlDay.setPreferredSize(new java.awt.Dimension(312, 192));
			pnlDay.setLayout(null);
			{
				if (lDay == null)
					lDay = new SLabel("每隔");
				pnlDay.add(lDay);
				lDay.setBounds(22, 16, 39, 14);
			}
			{
				if (txtDay == null)
					txtDay = new STextField();
				pnlDay.add(txtDay);
				txtDay.setBounds(53, 9, 59, 21);
			}
			{
				if (lDay1 == null)
					lDay1 = new SLabel("天");
				pnlDay.add(lDay1);
				lDay1.setBounds(120, 16, 20, 14);
			}
		} catch (Exception e) {
			Log.logError("调度对话框初始化每天界面面板错误:", e);
		} finally {
		}
	}

}
