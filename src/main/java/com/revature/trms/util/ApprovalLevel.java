package com.revature.trms.util;

import org.jetbrains.annotations.Nullable;

public enum ApprovalLevel
{
	NONE(null, RequestStatus.NEEDS_SUPERVISOR_APPROVAL),
	SUPERVISOR(RequestStatus.NEEDS_SUPERVISOR_APPROVAL, RequestStatus.NEEDS_DEPARTMENT_APPROVAL),
	DEPARTMENT(RequestStatus.NEEDS_DEPARTMENT_APPROVAL, RequestStatus.NEEDS_BENEFITS_COORDINATOR_APPROVAL),
	BENCO(RequestStatus.NEEDS_BENEFITS_COORDINATOR_APPROVAL, RequestStatus.NEEDS_PROOF_OF_COMPLETION);

	private final @Nullable RequestStatus approvableStatus;
	private final RequestStatus nextStatus;

	ApprovalLevel(@Nullable RequestStatus approvableStatus, RequestStatus nextStatus)
	{
		this.approvableStatus = approvableStatus;
		this.nextStatus = nextStatus;
	}

	public RequestStatus getApprovableStatus()
	{
		return this.approvableStatus;
	}

	public RequestStatus getNextStatus()
	{
		return this.nextStatus;
	}
}
