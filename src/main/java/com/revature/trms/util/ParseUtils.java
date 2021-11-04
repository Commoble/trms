package com.revature.trms.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.revature.trms.requestmodels.NewApprovalRequestJson;
import com.revature.trms.requestmodels.NewAttachmentRequestJson;
import com.revature.trms.requestmodels.NewRequestRequestJson;
import org.jetbrains.annotations.Nullable;

public class ParseUtils
{
	/**
	 * @param input Nullable input string
	 * @param defaultInt Fallback value
	 * @return The default int if input was null or unparsable, otherwise returns the parsed int
	 */
	public static int safeParseInt(@Nullable String input, int defaultInt)
	{
		if (input == null)
		{
			return defaultInt;
		}
		try
		{
			return Integer.parseInt(input, 10);
		}
		catch(NumberFormatException e)
		{
			return defaultInt;
		}
	}

	public static long safeParseLong(@Nullable String input, long defaultLong)
	{
		if (input == null)
		{
			return defaultLong;
		}
		try
		{
			return Long.parseLong(input, 10);
		}
		catch (NumberFormatException e)
		{
			return defaultLong;
		}
	}

	public static String emptyStringIfNull(@Nullable String input)
	{
		return input == null ? "" : input;
	}

	public static NewRequestRequestJson parseRequestRequest(Gson gson, String jsonString)
	{
		@Nullable NewRequestRequestJson json = safeParseJson(gson, jsonString, NewRequestRequestJson.class);
		return json;
	}

	@Nullable
	public static NewApprovalRequestJson parseApprovalRequest(Gson gson, String jsonString)
	{
		@Nullable NewApprovalRequestJson approval = safeParseJson(gson, jsonString, NewApprovalRequestJson.class);
		return approval;
	}

	@Nullable
	public static NewAttachmentRequestJson parseAttachmentRequest(Gson gson, String jsonString)
	{
		@Nullable NewAttachmentRequestJson approval = safeParseJson(gson, jsonString, NewAttachmentRequestJson.class);
		return approval;
	}

	/**
	 *
	 * @param gson Gson instance to use to parse
	 * @param jsonString Json-format string to parse
	 * @param parseClass Class to parse as
	 * @param <T> Type to parse as
	 * @return The json object parsed as a new instance of the specified class,
	 * or null if the parsing failed
	 */
	@Nullable
	public static <T> T safeParseJson(Gson gson, String jsonString, Class<T> parseClass)
	{
		try
		{
			return gson.fromJson(jsonString, parseClass);
		}
		catch(JsonSyntaxException e)
		{
			return null;
		}
	}
}
