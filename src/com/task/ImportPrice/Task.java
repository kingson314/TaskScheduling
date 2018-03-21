package com.task.ImportPrice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import module.dbconnection.DbConnection;
import module.dbconnection.DbConnectionDao;

import com.log.Log;
import com.taskInterface.TaskAbstract;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;

//mt4中EA(getData)导出数据格式为:timeServer{HHmmss},Volume[0]
/**
 * @Description:解析导入行情数据，以1440周期的数据为导入数据
 * @date May 26, 2014
 * @author:fgq
 */
public class Task extends TaskAbstract {
	private String separetor = ",";// 文件中每行的分隔符
	private int cntBatch = 1000;// 批处理大小限制

	public void fireTask() {
		Connection con = null;
		DbConnection dbconn = null;
		BufferedReader buffReader = null;
		String[] lineArr = null; // 当前记录的数组
		String[] lineArrLast = null;// 上一条记录数组
		Map<Integer, PreparedStatement> mapInsertPs = null;// 明细以及各个周期插入表时使用的ps
		Map<Integer, Integer> mapInsertCnt = new HashMap<Integer, Integer>();// 明细以及各个周期插入表时的记录数
		Map<Integer, PriceLast> mapPriceLast = new HashMap<Integer, PriceLast>();// 记录每个周期上一条记录价格的实体类map
		File inFile = null;
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			inFile = new File(bean.getFilePath());
			// Log.logInfo(UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss)+":正在导入文件:"
			// +inFile.getName());
			if (!inFile.exists()) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件:" + inFile.getName() + " 不存在");
				return;
			}
			dbconn = DbConnectionDao.getInstance().getMapDbConn(bean.getDbName());
			if (dbconn == null) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("获取文件导入数据库连接错误");
				return;
			}
			con = UtilJDBCManager.getConnection(dbconn);
			if (bean.getFilePath().equals("")) {
				this.setTaskStatus("执行失败");
				this.setTaskMsg("文件路径为空");
				return;
			}

			/**************** 初始化 **************/
			initDelete(bean, con);// 初始化，删除当前日期对应周期的表记录以及明细记录
			mapInsertPs = initInsertPs(bean, con);// 初始化所以的插入PreparedStatement
			mapInsertCnt = initInsertCntMap(bean);// 初始化插入每个周期表的记录数Map,避免每次使用时做是否为null判断，只要是为了节约资源
			mapPriceLast = initPriceLastMap(bean);
			/***********************************/
			String id = "";// 记录id 36为uuid
			String time = "";// 本记录时间
			String minute = "";// 当前记录的分钟
			String minuteLast = "";// 上一条记录的分钟
			String hour = "";// 当前记录的小时
			String hourLast = "";// 上一条记录的小时
			int period = 0;// 周期，0表示明细表
			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			String sline = "";
			double close = 0.0;// 收盘价
			int beginHour = 0;// 第一条记录的开始小时，用于计算4小时周期时使用
			while ((sline = buffReader.readLine()) != null) {
				try {
					lineArr = sline.split(separetor);
					time = lineArr[0]; // 当前记录时间
					close = Double.valueOf(lineArr[1]);// 当前记录收盘价
					minute = time.substring(2, 4);// 当前记录分钟
					hour = time.substring(0, 2);// 当前记录小时

					if (bean.isPeriod1()) {
						period = 1;
						if (lineArrLast == null) {
							mapPriceLast.get(period).open = close;
							mapPriceLast.get(period).high = close;
							mapPriceLast.get(period).low = close;
							mapPriceLast.get(period).close = close;
							mapPriceLast.get(period).time = time;
						} else {
							minuteLast = mapPriceLast.get(period).time.substring(2, 4);
							mapPriceLast.get(period).close = Double.valueOf(lineArrLast[1]);
							mapPriceLast.get(period).volume = Integer.valueOf(lineArrLast[2]);
						}

						if (Integer.valueOf(minute) % period == 0) {
							if (!minute.equals(minuteLast) && !"".equals(minuteLast)) {
								addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
								executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
								mapPriceLast.get(period).volumeLastPeriod = mapPriceLast.get(period).volume;
								mapPriceLast.get(period).high = close;
								mapPriceLast.get(period).low = close;
								mapPriceLast.get(period).open = close;
								mapPriceLast.get(period).time = time;
							}
						}
						mapPriceLast.get(period).high = Math.max(mapPriceLast.get(period).high, close);
						mapPriceLast.get(period).low = Math.min(mapPriceLast.get(period).low, close);
					}

					if (bean.isPeriod5()) {
						period = 5;
						if (lineArrLast == null) {
							mapPriceLast.get(period).open = close;
							mapPriceLast.get(period).high = close;
							mapPriceLast.get(period).low = close;
							mapPriceLast.get(period).close = close;
							mapPriceLast.get(period).time = time;
						} else {
							minuteLast = mapPriceLast.get(period).time.substring(2, 4);
							mapPriceLast.get(period).close = Double.valueOf(lineArrLast[1]);
							mapPriceLast.get(period).volume = Integer.valueOf(lineArrLast[2]);
						}

						if (Integer.valueOf(minute) % period == 0) {
							if (!minute.equals(minuteLast) && !"".equals(minuteLast)) {
								addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
								executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
								mapPriceLast.get(period).volumeLastPeriod = mapPriceLast.get(period).volume;
								mapPriceLast.get(period).high = close;
								mapPriceLast.get(period).low = close;
								mapPriceLast.get(period).open = close;
								mapPriceLast.get(period).time = time;
							}
						}
						mapPriceLast.get(period).high = Math.max(mapPriceLast.get(period).high, close);
						mapPriceLast.get(period).low = Math.min(mapPriceLast.get(period).low, close);
					}

					if (bean.isPeriod15()) {
						period = 15;
						if (lineArrLast == null) {
							mapPriceLast.get(period).open = close;
							mapPriceLast.get(period).high = close;
							mapPriceLast.get(period).low = close;
							mapPriceLast.get(period).close = close;
							mapPriceLast.get(period).time = time;
						} else {
							minuteLast = mapPriceLast.get(period).time.substring(2, 4);
							mapPriceLast.get(period).close = Double.valueOf(lineArrLast[1]);
							mapPriceLast.get(period).volume = Integer.valueOf(lineArrLast[2]);
						}

						if (Integer.valueOf(minute) % period == 0) {
							if (!minute.equals(minuteLast) && !"".equals(minuteLast)) {
								addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
								executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
								mapPriceLast.get(period).volumeLastPeriod = mapPriceLast.get(period).volume;
								mapPriceLast.get(period).high = close;
								mapPriceLast.get(period).low = close;
								mapPriceLast.get(period).open = close;
								mapPriceLast.get(period).time = time;
							}
						}
						mapPriceLast.get(period).high = Math.max(mapPriceLast.get(period).high, close);
						mapPriceLast.get(period).low = Math.min(mapPriceLast.get(period).low, close);
					}

					if (bean.isPeriod30()) {
						period = 30;
						if (lineArrLast == null) {
							mapPriceLast.get(period).open = close;
							mapPriceLast.get(period).high = close;
							mapPriceLast.get(period).low = close;
							mapPriceLast.get(period).close = close;
							mapPriceLast.get(period).time = time;
						} else {
							minuteLast = mapPriceLast.get(period).time.substring(2, 4);
							mapPriceLast.get(period).close = Double.valueOf(lineArrLast[1]);
							mapPriceLast.get(period).volume = Integer.valueOf(lineArrLast[2]);
						}

						if (Integer.valueOf(minute) % period == 0) {
							if (!minute.equals(minuteLast) && !"".equals(minuteLast)) {
								addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
								executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
								mapPriceLast.get(period).volumeLastPeriod = mapPriceLast.get(period).volume;
								mapPriceLast.get(period).high = close;
								mapPriceLast.get(period).low = close;
								mapPriceLast.get(period).open = close;
								mapPriceLast.get(period).time = time;
							}
						}
						mapPriceLast.get(period).high = Math.max(mapPriceLast.get(period).high, close);
						mapPriceLast.get(period).low = Math.min(mapPriceLast.get(period).low, close);
					}
					//
					hour = time.substring(0, 2);
					if (bean.isPeriod60()) {
						period = 60;
						if (lineArrLast == null) {
							mapPriceLast.get(period).open = close;
							mapPriceLast.get(period).high = close;
							mapPriceLast.get(period).low = close;
							mapPriceLast.get(period).close = close;
							mapPriceLast.get(period).time = time;
						} else {
							hourLast = mapPriceLast.get(period).time.substring(0, 2);
							mapPriceLast.get(period).close = Double.valueOf(lineArrLast[1]);
							mapPriceLast.get(period).volume = Integer.valueOf(lineArrLast[2]);
						}

						if (Integer.valueOf(hour) % (period / 60) == 0) {
							if (!hour.equals(hourLast) && !"".equals(hourLast)) {
								addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
								executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
								mapPriceLast.get(period).volumeLastPeriod = mapPriceLast.get(period).volume;
								mapPriceLast.get(period).high = close;
								mapPriceLast.get(period).low = close;
								mapPriceLast.get(period).open = close;
								mapPriceLast.get(period).time = time;
							}
						}
						mapPriceLast.get(period).high = Math.max(mapPriceLast.get(period).high, close);
						mapPriceLast.get(period).low = Math.min(mapPriceLast.get(period).low, close);
					}

					if (bean.isPeriod240()) {
						period = 240;
						if (lineArrLast == null) {
							mapPriceLast.get(period).open = close;
							mapPriceLast.get(period).high = close;
							mapPriceLast.get(period).low = close;
							mapPriceLast.get(period).close = close;
							mapPriceLast.get(period).time = time;
							beginHour = Integer.valueOf(hour);
						} else {
							hourLast = mapPriceLast.get(period).time.substring(0, 2);
							mapPriceLast.get(period).close = Double.valueOf(lineArrLast[1]);
							mapPriceLast.get(period).volume = Integer.valueOf(lineArrLast[2]);
						}

						if ((Integer.valueOf(hour) - beginHour) % (period / 60) == 0) {
							if (!hour.equals(hourLast) && !"".equals(hourLast)) {
								addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
								executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
								mapPriceLast.get(period).volumeLastPeriod = mapPriceLast.get(period).volume;
								mapPriceLast.get(period).high = close;
								mapPriceLast.get(period).low = close;
								mapPriceLast.get(period).open = close;
								mapPriceLast.get(period).time = time;
							}
						}
						mapPriceLast.get(period).high = Math.max(mapPriceLast.get(period).high, close);
						mapPriceLast.get(period).low = Math.min(mapPriceLast.get(period).low, close);
					}

					if (bean.isPeriod1440()) {
						period = 1440;
						if (lineArrLast == null) {
							mapPriceLast.get(period).open = close;
							mapPriceLast.get(period).high = close;
							mapPriceLast.get(period).low = close;
							mapPriceLast.get(period).time = time;
						}
						mapPriceLast.get(period).close = close;
						mapPriceLast.get(period).high = Math.max(mapPriceLast.get(period).high, close);
						mapPriceLast.get(period).low = Math.min(mapPriceLast.get(period).low, close);
						mapPriceLast.get(period).volume = Integer.valueOf(lineArr[2]);
					}
					if (bean.isDetail()) {
						period = 0;
						id = addDetailBatch(mapInsertPs.get(0), time, close); // 明细表参数
						executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
					} else {
						id = UtilString.getUUID();
					}
					lineArrLast = lineArr.clone();
				} catch (Exception e) {
					Log.logError(inFile.getName() + ":" + mapInsertCnt.get(0) + "\n", e);
					continue;
				}
			}

			/*********** 补上最后一根可能不够整数周期的 给主表插入最后一条记录 ***********/
			int cntMain = 0;
			if (lineArrLast != null && lineArrLast.length > 0) {// 主表的记录数要加1
				cntMain = 1;
			}
			if (bean.isPeriod1()) {
				period = 1;
				mapPriceLast.get(period).volume = Integer.valueOf(lineArr[2]);
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}

			if (bean.isPeriod5()) {
				period = 5;
				mapPriceLast.get(period).volume = Integer.valueOf(lineArr[2]);
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}
			if (bean.isPeriod15()) {
				period = 15;
				mapPriceLast.get(period).close = close;
				mapPriceLast.get(period).volume = Integer.valueOf(lineArr[2]);
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}
			if (bean.isPeriod30()) {
				period = 30;
				mapPriceLast.get(period).close = close;
				mapPriceLast.get(period).volume = Integer.valueOf(lineArr[2]);
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}

			if (bean.isPeriod60()) {
				period = 60;
				mapPriceLast.get(period).close = close;
				mapPriceLast.get(period).volume = Integer.valueOf(lineArr[2]);
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}

			if (bean.isPeriod240()) {
				period = 240;
				mapPriceLast.get(period).close = close;
				mapPriceLast.get(period).volume = Integer.valueOf(lineArr[2]);
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}
			if (bean.isPeriod1440()) {
				period = 1440;
				mapPriceLast.get(period).close = close;
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}
			/********************************************************************/

			// 执行不足 cntBatch的ps
			executeForLessThanBatch(bean, mapInsertPs, mapInsertCnt, lineArrLast);
			this.setTaskStatus("执行成功");
			this.setTaskMsg(inFile.getName() + getMsg(bean, mapInsertCnt));
			buffReader.close();
		} catch (Exception e) {
			this.setTaskStatus("执行失败");
			this.setTaskMsg("文件[" + inFile.getName() + "]导入错误:", e);
		} finally {
			if (mapInsertPs != null) {
				for (Map.Entry<Integer, PreparedStatement> entry : mapInsertPs.entrySet()) {
					UtilSql.close(entry.getValue());
				}
			}
			UtilSql.close(con);
		}
	}

	/**
	 * @Description:执行不足 cntBatch的ps
	 * @int 返回新增的最后一条主表是不是存在，存在则返回值为1，否则为0
	 * @date 2014-5-30
	 * @author:fgq
	 * @throws SQLException
	 */
	private void executeForLessThanBatch(Bean bean, Map<Integer, PreparedStatement> mapInsertPs, Map<Integer, Integer> mapInsertCnt, String[] lineArrLast) throws SQLException {

		// 明细的ps直接执行
		if (bean.isDetail()) {
			int period = 0;
			executeBatch(mapInsertPs.get(period), -1);
		}
		if (bean.isPeriod1()) {
			int period = 1;
			executeBatch(mapInsertPs.get(period), -1);
		}
		if (bean.isPeriod5()) {
			int period = 5;
			executeBatch(mapInsertPs.get(period), -1);
		}
		if (bean.isPeriod15()) {
			int period = 15;
			executeBatch(mapInsertPs.get(period), -1);
		}
		if (bean.isPeriod30()) {
			int period = 30;
			executeBatch(mapInsertPs.get(period), -1);
		}
		if (bean.isPeriod60()) {
			int period = 60;
			executeBatch(mapInsertPs.get(period), -1);
		}
		if (bean.isPeriod240()) {
			int period = 240;
			executeBatch(mapInsertPs.get(period), -1);
		}
		if (bean.isPeriod1440()) {
			int period = 1440;
			executeBatch(mapInsertPs.get(period), -1);
		}
	}

	/**
	 * @Description:初始化所以的插入PreparedStatement
	 * @param bean
	 * @param con
	 * @return
	 * @throws SQLException
	 *             Map<Integer,PreparedStatement>
	 * @date 2014-5-30
	 * @author:fgq
	 */
	private Map<Integer, PreparedStatement> initInsertPs(Bean bean, Connection con) throws SQLException {
		Map<Integer, PreparedStatement> mapPs = new HashMap<Integer, PreparedStatement>();
		if (bean.isDetail()) {
			int period = 0;
			String insertDetailSql = "insert into tableName(timeserver,dateserver,timelocal,datelocal,close,id)values(?,?,?,?,?,?)";
			PreparedStatement psDetail = con.prepareStatement(insertDetailSql.replace("tableName", "price_" + bean.getSymbol() + "_detail"));
			mapPs.put(period, psDetail);// key=0 ：表示明细
		}
		String insertMainSql = "insert into tableName(timeserver,dateserver,timelocal,datelocal,open,close,high,low,volume,id)values(?,?,?,?,?,?,?,?,?,?)";
		if (bean.isPeriod1()) {
			int period = 1;
			PreparedStatement ps = con.prepareStatement(insertMainSql.replace("tableName", "price_" + bean.getSymbol() + period));
			mapPs.put(period, ps);
		}
		if (bean.isPeriod5()) {
			int period = 5;
			PreparedStatement ps = con.prepareStatement(insertMainSql.replace("tableName", "price_" + bean.getSymbol() + period));
			mapPs.put(period, ps);
		}
		if (bean.isPeriod15()) {
			int period = 15;
			PreparedStatement ps = con.prepareStatement(insertMainSql.replace("tableName", "price_" + bean.getSymbol() + period));
			mapPs.put(period, ps);
		}
		if (bean.isPeriod30()) {
			int period = 30;
			PreparedStatement ps = con.prepareStatement(insertMainSql.replace("tableName", "price_" + bean.getSymbol() + period));
			mapPs.put(period, ps);
		}
		if (bean.isPeriod60()) {
			int period = 60;
			PreparedStatement ps = con.prepareStatement(insertMainSql.replace("tableName", "price_" + bean.getSymbol() + period));
			mapPs.put(period, ps);
		}
		if (bean.isPeriod240()) {
			int period = 240;
			PreparedStatement ps = con.prepareStatement(insertMainSql.replace("tableName", "price_" + bean.getSymbol() + period));
			mapPs.put(period, ps);
		}
		if (bean.isPeriod1440()) {
			int period = 1440;
			PreparedStatement ps = con.prepareStatement(insertMainSql.replace("tableName", "price_" + bean.getSymbol() + period));
			mapPs.put(period, ps);
		}
		return mapPs;
	}

	/**
	 * @Description:初始化插入每个周期表的记录数 Map,避免每次使用时做是否为null判断，只要是为了节约资源
	 * @param bean
	 * @return Map<Integer,Integer>
	 * @date 2014-5-30
	 * @author:fgq
	 */
	private Map<Integer, Integer> initInsertCntMap(Bean bean) {
		Map<Integer, Integer> mapInsertCnt = new HashMap<Integer, Integer>();
		if (bean.isDetail()) {// 明细表记录数，用0作为key
			int period = 0;
			mapInsertCnt.put(period, 0);
		}
		if (bean.isPeriod1()) {
			int period = 1;
			mapInsertCnt.put(period, 0);
		}
		if (bean.isPeriod5()) {
			int period = 5;
			mapInsertCnt.put(period, 0);
		}
		if (bean.isPeriod15()) {
			int period = 15;
			mapInsertCnt.put(period, 0);
		}
		if (bean.isPeriod30()) {
			int period = 30;
			mapInsertCnt.put(period, 0);
		}
		if (bean.isPeriod60()) {
			int period = 60;
			mapInsertCnt.put(period, 0);
		}
		if (bean.isPeriod240()) {
			int period = 240;
			mapInsertCnt.put(period, 0);
		}
		if (bean.isPeriod1440()) {
			int period = 1440;
			mapInsertCnt.put(period, 0);
		}
		return mapInsertCnt;
	}

	/**
	 * @Description:初始化，删除当前日期对应周期的表记录以及明细记录
	 * @param bean
	 * @param con
	 * @throws SQLException void
	 * @date 2014-5-30
	 * @author:fgq
	 */
	private void initDelete(Bean bean, Connection con) throws SQLException {
		PreparedStatement ps = null;
		String deleteSql = "";
		// {// delete Detail
		// deleteSql = "delete  from price_" + bean.getSymbol() +
		// "_detail  where dateServer='" + this.getNowDate() + "'";
		// ps = con.prepareStatement(deleteSql);
		// ps.executeUpdate();
		// ps.clearBatch();
		// }

		if (bean.isPeriod1()) {
			deleteSql = "delete  from price_" + bean.getSymbol() + "1  where dateServer='" + this.getNowDate() + "'";
			ps = con.prepareStatement(deleteSql);
			ps.executeUpdate();
			ps.clearBatch();

		}
		if (bean.isPeriod5()) {
			deleteSql = "delete  from price_" + bean.getSymbol() + "5  where dateServer='" + this.getNowDate() + "'";
			ps = con.prepareStatement(deleteSql);
			ps.executeUpdate();
			ps.clearBatch();

		}
		if (bean.isPeriod15()) {
			deleteSql = "delete  from price_" + bean.getSymbol() + "15  where dateServer='" + this.getNowDate() + "'";
			ps = con.prepareStatement(deleteSql);
			ps.executeUpdate();
			ps.clearBatch();

		}
		if (bean.isPeriod30()) {
			deleteSql = "delete  from price_" + bean.getSymbol() + "30  where dateServer='" + this.getNowDate() + "'";
			ps = con.prepareStatement(deleteSql);
			ps.executeUpdate();
			ps.clearBatch();

		}
		if (bean.isPeriod60()) {
			deleteSql = "delete  from price_" + bean.getSymbol() + "60 where dateServer='" + this.getNowDate() + "'";
			ps = con.prepareStatement(deleteSql);
			ps.executeUpdate();
			ps.clearBatch();

		}
		if (bean.isPeriod240()) {
			deleteSql = "delete  from price_" + bean.getSymbol() + "240  where dateServer='" + this.getNowDate() + "'";
			ps = con.prepareStatement(deleteSql);
			ps.executeUpdate();
			ps.clearBatch();

		}
		if (bean.isPeriod1440()) {
			deleteSql = "delete  from price_" + bean.getSymbol() + "1440 where dateServer='" + this.getNowDate() + "'";
			ps = con.prepareStatement(deleteSql);
			ps.executeUpdate();
			ps.clearBatch();
		}
		UtilSql.close(ps);
	}

	private Map<Integer, PriceLast> initPriceLastMap(Bean bean) {
		Map<Integer, PriceLast> mapPriceLast = new HashMap<Integer, PriceLast>();
		if (bean.isPeriod1()) {
			int period = 1;
			mapPriceLast.put(period, new PriceLast());
		}
		if (bean.isPeriod5()) {
			int period = 5;
			mapPriceLast.put(period, new PriceLast());
		}
		if (bean.isPeriod15()) {
			int period = 15;
			mapPriceLast.put(period, new PriceLast());
		}
		if (bean.isPeriod30()) {
			int period = 30;
			mapPriceLast.put(period, new PriceLast());
		}
		if (bean.isPeriod60()) {
			int period = 60;
			mapPriceLast.put(period, new PriceLast());
		}
		if (bean.isPeriod240()) {
			int period = 240;
			mapPriceLast.put(period, new PriceLast());
		}
		if (bean.isPeriod1440()) {
			int period = 1440;
			mapPriceLast.put(period, new PriceLast());
		}
		return mapPriceLast;
	}

	/**
	 * @Description:参数化主表的插入PreparedStatement
	 * @param ps
	 * @param args
	 * @throws SQLException void
	 * @date 2014-5-30
	 * @author:fgq
	 */
	private void addMainBatch(PreparedStatement ps, PriceLast priceLast, String id) throws SQLException {
		// 使用yyyyMMdd
		String date = this.getNowDate();
		// 使用HHmmss
		ps.setString(1, priceLast.time);
		ps.setString(2, date);
		ps.setString(3, priceLast.time);
		ps.setString(4, date);
		ps.setDouble(5, priceLast.open);
		ps.setDouble(6, priceLast.close);
		ps.setDouble(7, priceLast.high);
		ps.setDouble(8, priceLast.low);
		ps.setInt(9, priceLast.volume - priceLast.volumeLastPeriod);
		ps.setString(10, id);
		ps.addBatch();
	}

	/**
	 * @Description:参数化明细表的插入PreparedStatement
	 * @param ps
	 * @param args
	 * @return uuid
	 * @throws SQLException void
	 * @date 2014-5-30
	 * @author:fgq
	 */
	private String addDetailBatch(PreparedStatement ps, String time, double close) throws SQLException {
		String id = UtilString.getUUID();
		// 使用yyyyMMdd
		String date = this.getNowDate();
		// 使用HHmmss
		ps.setString(1, time);
		ps.setString(2, date);
		ps.setString(3, time);
		ps.setString(4, date);
		ps.setDouble(5, close);
		ps.setString(6, id);
		ps.addBatch();
		return id;
	}

	/**
	 * @Description:批处理执行 PreparedStatement
	 * @param ps
	 * @param cnt
	 *            当前cnt>=0时 为当前记录数，当前cnt=-1时，直接执行batch
	 * @void
	 * @date 2014-5-30
	 * @author:fgq
	 * @throws SQLException
	 */
	private void executeBatch(PreparedStatement ps, int cnt) throws SQLException {
		if (cnt == -1) {
			ps.executeBatch();
			ps.clearBatch();
		} else if (cnt != 0 && cnt % cntBatch == 0) {
			ps.executeBatch();
			ps.clearBatch();
		}
	}

	/**
	 * @Description: 相应周期表新增一条记录时，记录数加1 ，并且返回当前的记录数
	 * @param mapInsertCnt
	 * @param period
	 * @param increment增量
	 * @return int
	 * @date 2014-5-30
	 * @author:fgq
	 */
	private int addCnt(Map<Integer, Integer> mapInsertCnt, int period, int increment) {
		int cnt = mapInsertCnt.get(period) + increment;
		mapInsertCnt.put(period, cnt);
		return cnt;
	}

	private String getMsg(Bean bean, Map<Integer, Integer> mapInsertCount) {
		StringBuilder sbMsg = new StringBuilder();
		// 明细信息
		if (bean.isDetail()) {
			int period = 0;
			sbMsg.append("\nprice_").append(bean.getSymbol()).append("_detail").append(":").append(mapInsertCount.get(period));
		}
		if (bean.isPeriod1()) {
			int period = 1;
			sbMsg.append("\nprice_").append(bean.getSymbol()).append(period).append(":").append(mapInsertCount.get(period));
		}
		if (bean.isPeriod5()) {
			int period = 5;
			sbMsg.append("\nprice_").append(bean.getSymbol()).append(period).append(":").append(mapInsertCount.get(period));
		}
		if (bean.isPeriod15()) {
			int period = 15;
			sbMsg.append("\nprice_").append(bean.getSymbol()).append(period).append(":").append(mapInsertCount.get(period));
		}
		if (bean.isPeriod30()) {
			int period = 30;
			sbMsg.append("\nprice_").append(bean.getSymbol()).append(period).append(":").append(mapInsertCount.get(period));
		}
		if (bean.isPeriod60()) {
			int period = 60;
			sbMsg.append("\nprice_").append(bean.getSymbol()).append(period).append(":").append(mapInsertCount.get(period));
		}
		if (bean.isPeriod240()) {
			int period = 240;
			sbMsg.append("\nprice_").append(bean.getSymbol()).append(period).append(":").append(mapInsertCount.get(period));
		}
		if (bean.isPeriod1440()) {
			int period = 1440;
			sbMsg.append("\nprice_").append(bean.getSymbol()).append(period).append(":").append(mapInsertCount.get(period));
		}
		return sbMsg.toString();
	}

	public void fireTask(String startTime, String groupId, String scheCod, String taskOrder) {
		fireTask();
	}

	class PriceLast {
		public String time;
		public double open;
		public double close;
		public double high;
		public double low;
		public int volume;
		public int volumeLastPeriod;

		public PriceLast() {
			time = "";
			open = 0;
			close = 0;
			high = 0;
			low = 0;
			volume = 0;
			volumeLastPeriod = 0;
		}
	}
}
