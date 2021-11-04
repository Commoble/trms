package com.revature.trms.requestmodels;

public class PatchRequestRequestJson
{
	private boolean award =false;

	public PatchRequestRequestJson()
	{

	}

	public boolean isAward()
	{
		return this.award;
	}

	public void setAward(boolean award)
	{
		this.award = award;
	}
}
