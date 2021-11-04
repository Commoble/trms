package com.revature.trms.repositories;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Data Access Object that manages database access for basic CRUD operations
 * for a particular table represented by a model class
 * @param <T> The model class that represents the table this DAO manages
 */
public interface CrudDao<T>
{
	/**
	 * Adds a record to the table in the database
	 * @param data The new data to add. ID is ignored.
	 * @return The record in the database, with the ID matching the database. Null if update failed.
	 */
	@Nullable
	public T add(T data);

	/**
	 * Retrieves all records from the table in the database.
	 * @return List of all records. If the query failed, returns an empty list.
	 */
	public List<T> getAll();

	/**
	 * Retreives a record by ID.
	 * @param id The ID of the record to get
	 * @return The record in the database, or null if the ID doesn't exist or the query failed
	 */
	@Nullable
	public T get(int id);

	/**
	 * Updates a record in the database.
	 * @param data The new data. Should have an ID matching the record to update.
	 * @return The new data in the database, if successful.
	 * Returns null if the existing record did not exist or the update otherwise failed.
	 */
	@Nullable
	public T update(T data);

	/**
	 * Deletes a record in the database.
	 * @param id The ID of the record to delete.
	 * @return True if the record was successfully deleted, false if not.
	 */
	public boolean delete(int id);
}
