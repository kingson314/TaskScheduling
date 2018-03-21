package com.taskgroup;

//任务组任务明细Bean
public class TaskGroupDetail {
	// 标志
	private Long id;
	// 任务组id
	private Long groupId;
	// 任务id
	private Long taskId;
	// 任务顺序号
	private Long taskOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getTaskOrder() {
		return taskOrder;
	}

	public void setTaskOrder(Long taskOrder) {
		this.taskOrder = taskOrder;
	}
}
