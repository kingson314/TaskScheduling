package com.taskgroup;

//任务组Bean
public class TaskGroup {
	// 任务组顺序号
	private String groupOrder;
	// 任务组id
	private Long groupId;
	// 任务组名称
	private String groupName;
	// 任务组备注
	private String groupMemo;
	// 任务组错误处理方式
	private int errorDeal;
	// 任务组执行类型(已废弃)
	private int execType;

	public String getGroupOrder() {
		return groupOrder;
	}

	public void setGroupOrder(String groupOrder) {
		this.groupOrder = groupOrder;
	}

	public int getExecType() {
		return execType;
	}

	public void setExecType(int execType) {
		this.execType = execType;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupMemo() {
		return groupMemo;
	}

	public void setGroupMemo(String groupMemo) {
		this.groupMemo = groupMemo;
	}

	public int getErrorDeal() {
		return errorDeal;
	}

	public void setErrorDeal(int errorDeal) {
		this.errorDeal = errorDeal;
	}

}
