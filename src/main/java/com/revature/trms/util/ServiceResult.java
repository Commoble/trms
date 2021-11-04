package com.revature.trms.util;

import org.eclipse.jetty.http.HttpStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a (possibly null) value along with an HTTP status code explaining why
 * the value is null
 * @param <T> The type of the value held by the result
 */
public class ServiceResult<T>
{
	@Nullable
	private final T value;
	private final int status;

	public static <T> ServiceResult<T> either(@Nullable T value, int successStatus, int nullStatus)
	{
		return new ServiceResult<>(value, value == null ? nullStatus : successStatus);
	}

	public static <T> ServiceResult<T> of(@NotNull T value)
	{
		return new ServiceResult<>(value, HttpStatus.OK_200);
	}

	public static <T> ServiceResult<T> of(@NotNull T value, int status)
	{
		return new ServiceResult<>(value, status);
	}

	public static <T> ServiceResult<T> empty(int status)
	{
		return new ServiceResult<>(null, status);
	}

	public ServiceResult(@Nullable T value, int status)
	{
		this.value = value;
		this.status = status;
	}

	@Nullable
	public T getValue()
	{
		return this.value;
	}

	public int getStatus()
	{
		return this.status;
	}

	/**
	 * Applies a mapping function to this ServiceResult's value if value is not null.
	 * Does not change the status code.
	 * @param mapper Function to apply
	 * @param <T2> The new value type after applying the function
	 * @return A ServiceResult able to produce the new value if the value was already present
	 * or an empty result if value was already empty
	 */
	public <T2> ServiceResult<T2> map(Function<T, T2> mapper)
	{
		return this.getValue() == null
			? ServiceResult.empty(this.getStatus())
			: ServiceResult.of(mapper.apply(this.getValue()), this.getStatus());
	}

	/**
	 * Applies a flatmap to this ServiceResult's internal values if the value is not empty.
	 * The flatmap may change the status code if the value was not previously empty.
	 * @param flatMapper A flatmapping function that returns a new ServiceResult
	 * @param <T2> The new value type after applying the function
	 * @return A new ServiceResult. The status code may change if the value was not
	 * previously empty.
	 */
	public <T2> ServiceResult<T2> flatMap(Function<T, ServiceResult<T2>> flatMapper)
	{
		return this.getValue() == null
			? ServiceResult.empty(this.getStatus())
			: flatMapper.apply(this.getValue());
	}

	/**
	 * Filters the held value by the given predicate.
	 * Changes the status code if the predicate fails.
	 * @param predicate The predicate to apply to this result's value if non-empty
	 * @param newFailStatus The new status to use if the predicate fails
	 * @return The same ServiceResult if the predicate was successful,
	 * or an empty result with the new status code if it failed
	 */
	public ServiceResult<T> filter(Predicate<T> predicate, int newFailStatus)
	{
		T value = this.getValue();
		return this.getValue() == null
			? this
			: predicate.test(value)
				? this
				: ServiceResult.empty(newFailStatus);
	}

	/**
	 * Applies a consumer to our held value if it exists,
	 * or it doesn't if value is empty
	 * @param consumer Consumer to apply
	 */
	public void ifPresent(Consumer<T> consumer)
	{
		T value = this.getValue();
		if (value != null)
		{
			consumer.accept(value);
		}
	}

	/**
	 * Runs one of two functions depending on whether our value is empty
	 * @param ifPresent Consumer to run if value is not empty. Accepts the held value.
	 * @param ifNotPresent Runnable to run if value is empty.
	 */
	public void ifPresentOrElse(Consumer<T> ifPresent, Runnable ifNotPresent)
	{
		T value = this.getValue();
		if (value == null)
		{
			ifNotPresent.run();
		}
		else
		{
			ifPresent.accept(value);
		}
	}

	@Override
	public String toString()
	{
		return "ServiceResult{" +
			"value=" + value +
			", status=" + status +
			'}';
	}

}
