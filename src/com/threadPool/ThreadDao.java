package com.threadPool;

import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import common.util.string.UtilString;

public class ThreadDao {
	private static ThreadDao dao = null;

	public static ThreadDao getInstance() {
		if (dao == null)
			dao = new ThreadDao();
		return dao;
	}

	// 查询线程表格数组
	@SuppressWarnings("unchecked")
	public Vector<?> getThreadVector(java.util.Map<String, String> map) {
		Vector<Vector<String>> tableValue = new Vector<Vector<String>>();
		Iterator<?> it = ThreadPool.MapFuture.entrySet().iterator();
		String threadStatus = map.get("线程状态");
		String scheCod = map.get("调度标志");
		while (it.hasNext()) {
			Entry<String, Future<?>> entry = (Entry<String, Future<?>>) it.next();
			String key = entry.getKey();
			Future<?> future = entry.getValue();
			String[] keyValue = key.split("/|");
			if ("0".equals(keyValue[0]))
				continue;
			if (!UtilString.isNil(scheCod).equals("")) {
				if (!keyValue[0].equals(scheCod))
					continue;
			}
			if ("-1".equals(keyValue[0]))
				keyValue[0] = "手工";
			Vector rowValue = new Vector();

			rowValue.add(false);

			for (int i = 0; i < keyValue.length; i++) {
				rowValue.add(keyValue[i]);
			}
			int count = 5 - keyValue.length;

			for (int i = 0; i < count; i++) {
				rowValue.add("");
			}
			String status = "";
			if (future.isCancelled()) {
				status = "取消";
			} else if (future.isDone()) {
				status = "完成";
			} else
				status = "运行中";
			if (!UtilString.isNil(threadStatus).equals("")) {
				if (!status.equals(threadStatus))
					continue;
			}
			rowValue.add(status);
			tableValue.add(rowValue);
		}
		return tableValue;
	}
}
