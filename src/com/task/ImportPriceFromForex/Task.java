package com.task.ImportPriceFromForex;

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

import common.util.conver.UtilConver;
import common.util.jdbc.UtilJDBCManager;
import common.util.jdbc.UtilSql;
import common.util.json.UtilJson;
import common.util.string.UtilString;
import consts.Const;

/**
 * @Description:解析导入行情数据，以1分钟周期的数据为导入数据
 * @数据来源：http://www.forextester.com/data/datasources.html
 * @数据格式：<Symbol>,<YYYYMMDD>,<HHMMSS>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>
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
		Map<Integer, PreparedStatement> mapInsertPs = null;// 明细以及各个周期插入表时使用的ps
		Map<Integer, Integer> mapInsertCnt = new HashMap<Integer, Integer>();// 明细以及各个周期插入表时的记录数
		Map<Integer, Price> mapPriceLast = new HashMap<Integer, Price>();// 记录每个周期上一条记录价格的实体类map
		File inFile = null;
		try {
			Bean bean = (Bean) UtilJson.getJsonBean(this.getParserJsonStr(), Bean.class);
			inFile = new File(bean.getFilePath());
			Log.logInfo(UtilConver.dateToStr(Const.fm_yyyyMMdd_HHmmss) + ":正在导入文件:" + inFile.getName());
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
			String hour = "";// 当前记录的小时
			int period = 0;// 周期，0表示明细表
			buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
			String sline = "";
			while ((sline = buffReader.readLine()) != null) {
				try {
					// <TICKER>,<DTYYYYMMDD>,<TIME>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>
					lineArr = sline.split(separetor);
					time = lineArr[2]; // 当前记录时间
					minute = time.substring(2, 4);// 当前记录分钟
					hour = time.substring(0, 2);// 当前记录小时
					id = UtilString.getUUID();
					if (bean.isPeriod1()) {
						period = 1;
						if (Integer.valueOf(minute) % period == 0) {
							Price price = new Price(lineArr);
							mapPriceLast.put(period, price);
							addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
							executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
						}
					}

					if (bean.isPeriod5()) {
						period = 5;
						Price price = null;
						if (mapPriceLast.get(period).open == 0) {
							price = new Price(lineArr);
						}  else {
							price = new Price(lineArr,mapPriceLast.get(period).open);
						}
						price = this.getCurPrice(mapPriceLast.get(period), price);
						if (Integer.valueOf(minute) % period == 0 && !minute.equals(mapPriceLast.get(period).timeServer.substring(2, 4))) {
							addMainBatch(mapInsertPs.get(period), price, id);
							executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
							mapPriceLast.put(period, new Price(lineArr[1],lineArr[2],Double.valueOf(lineArr[6])));//  :重新初始化时最后一根线的收盘价就是下一根线的开盘价
						} else {
							mapPriceLast.put(period, price);
						}
					}

					if (bean.isPeriod15()) {
						period = 15;
						Price price = null;
						if (mapPriceLast.get(period).open == 0) {
							price = new Price(lineArr);
						}  else {
							price = new Price(lineArr,mapPriceLast.get(period).open);
						} 
						price = this.getCurPrice(mapPriceLast.get(period), price);
						if (Integer.valueOf(minute) % period == 0 && !minute.equals(mapPriceLast.get(period).timeServer.substring(2, 4))) {
							addMainBatch(mapInsertPs.get(period), price, id);
							executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
							mapPriceLast.put(period, new Price(lineArr[1],lineArr[2],Double.valueOf(lineArr[6])));//  :重新初始化时最后一根线的收盘价就是下一根线的开盘价
						} else {
							mapPriceLast.put(period, price);
						}
					}
					if (bean.isPeriod30()) {
						period = 30;
						Price price = null;
						if (mapPriceLast.get(period).open == 0) {
							price = new Price(lineArr);
						}  else {
							price = new Price(lineArr,mapPriceLast.get(period).open);
						}
						price = this.getCurPrice(mapPriceLast.get(period), price);
						if (Integer.valueOf(minute) % period == 0 && !minute.equals(mapPriceLast.get(period).timeServer.substring(2, 4))) {
							addMainBatch(mapInsertPs.get(period), price, id);
							executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
							mapPriceLast.put(period, new Price(lineArr[1],lineArr[2],Double.valueOf(lineArr[6])));//  :重新初始化时最后一根线的收盘价就是下一根线的开盘价
						} else {
							mapPriceLast.put(period, price);
						}
					}
					//
					hour = time.substring(0, 2);
					if (bean.isPeriod60()) {
						period = 60;
						String lasthour = mapPriceLast.get(period).timeServer.substring(0, 2);// 当前记录小时
						Price price = null;
						if (mapPriceLast.get(period).open == 0) {
							price = new Price(lineArr);
						}  else {
							price = new Price(lineArr,mapPriceLast.get(period).open);
						}
						price = this.getCurPrice(mapPriceLast.get(period), price);
						if (!lasthour.equals(hour)) {
							addMainBatch(mapInsertPs.get(period), price, id);
							executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
							mapPriceLast.put(period, new Price(lineArr[1],lineArr[2],Double.valueOf(lineArr[6])));//  :重新初始化时最后一根线的收盘价就是下一根线的开盘价
						} else {
							mapPriceLast.put(period, price);
						}
					}

					if (bean.isPeriod240()) {
						period = 240;
						Price price = null;
						if (mapPriceLast.get(period).open == 0) {
							price = new Price(lineArr);
						}  else {
							price = new Price(lineArr,mapPriceLast.get(period).open);
						}
						price = this.getCurPrice(mapPriceLast.get(period), price);
						if (Integer.valueOf(hour) % (period / 60) == 0 && !hour.equals(mapPriceLast.get(period).timeServer.substring(0, 2))) {
							addMainBatch(mapInsertPs.get(period), price, id);
							executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
							mapPriceLast.put(period, new Price(lineArr[1],lineArr[2],Double.valueOf(lineArr[6])));//  :重新初始化时最后一根线的收盘价就是下一根线的开盘价
						} else {
							mapPriceLast.put(period, price);
						}
					}

					if (bean.isPeriod1440()) {
						period = 1440;
						String lastDate = mapPriceLast.get(period).dateServer;// 当前记录小时
						String nowDate = lineArr[1];
						Price price = new Price(lineArr);
						price = this.getCurPrice(mapPriceLast.get(period), price);
						if (!nowDate.equals(lastDate)) {
							addMainBatch(mapInsertPs.get(period), price, id);
							executeBatch(mapInsertPs.get(period), addCnt(mapInsertCnt, period, 1));
							mapPriceLast.put(period, new Price(lineArr[1],lineArr[2],Double.valueOf(lineArr[6])));//  :重新初始化时最后一根线的收盘价就是下一根线的开盘价
						}
						mapPriceLast.put(period, price);
					}
				} catch (Exception e) {
					// Log.logError(inFile.getName()+ ":" + mapInsertCnt.get(0)
					// + "\n", e);
					continue;
				}
			}

			/*********** 补上最后一根可能不够整数周期的 给主表插入最后一条记录 ***********/
			int cntMain = 1;
			if (bean.isPeriod5()) {
				period = 5;
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}
			if (bean.isPeriod15()) {
				period = 15;
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}
			if (bean.isPeriod30()) {
				period = 30;
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}

			if (bean.isPeriod60()) {
				period = 60;
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}

			if (bean.isPeriod240()) {
				period = 240;
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}
			if (bean.isPeriod1440()) {
				period = 1440;
				addMainBatch(mapInsertPs.get(period), mapPriceLast.get(period), id);
				addCnt(mapInsertCnt, period, cntMain);
			}
			/********************************************************************/
			// 执行不足 cntBatch的ps
			executeForLessThanBatch(bean, mapInsertPs, mapInsertCnt);
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
	private void executeForLessThanBatch(Bean bean, Map<Integer, PreparedStatement> mapInsertPs, Map<Integer, Integer> mapInsertCnt) throws SQLException {
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

	private Map<Integer, Price> initPriceLastMap(Bean bean) {
		Map<Integer, Price> mapPriceLast = new HashMap<Integer, Price>();
		if (bean.isPeriod1()) {
			int period = 1;
			mapPriceLast.put(period, new Price());
		}
		if (bean.isPeriod5()) {
			int period = 5;
			mapPriceLast.put(period, new Price());
		}
		if (bean.isPeriod15()) {
			int period = 15;
			mapPriceLast.put(period, new Price());
		}
		if (bean.isPeriod30()) {
			int period = 30;
			mapPriceLast.put(period, new Price());
		}
		if (bean.isPeriod60()) {
			int period = 60;
			mapPriceLast.put(period, new Price());
		}
		if (bean.isPeriod240()) {
			int period = 240;
			mapPriceLast.put(period, new Price());
		}
		if (bean.isPeriod1440()) {
			int period = 1440;
			mapPriceLast.put(period, new Price());
		}
		return mapPriceLast;
	}

	/**
	 * @Description:根据现在的Price和上一条就的Price的计算当前的Price 的Hight,low,Volume
	 * @param lastPrice
	 * @param price
	 * @return Price
	 * @date 2014-6-22
	 * @author:fgq
	 */
	private Price getCurPrice(Price lastPrice, Price price) {
		price.high = Math.max(price.high, lastPrice.high);
		price.low = Math.min(price.low, lastPrice.low);
		price.volume = lastPrice.volume + price.volume;
		return price;
	}

	/**
	 * @Description:参数化主表的插入PreparedStatement
	 * @param ps
	 * @param args
	 * @throws SQLException void
	 * @date 2014-5-30
	 * @author:fgq
	 */
	private void addMainBatch(PreparedStatement ps, Price price, String id) throws SQLException {
		// timeserver,dateserver,timelocal,datelocal,open,close,high,low,volume,id
		ps.setString(1, price.timeServer);
		ps.setString(2, price.dateServer);
		ps.setString(3, price.timeLocal);
		ps.setString(4, price.dateLocal);
		ps.setDouble(5, price.open);
		ps.setDouble(6, price.close);
		ps.setDouble(7, price.high);
		ps.setDouble(8, price.low);
		ps.setDouble(9, price.volume);
		ps.setString(10, id);
		ps.addBatch();
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

}
