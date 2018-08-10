package ru.croc.ctp.mts.datasources

import ru.croc.ctp.jxfw.core.generator.meta.XFWDataSource
import ru.croc.ctp.jxfw.core.datasource.DataSourceLoader
import ru.croc.ctp.mts.domain.Task

/**
 * Class implements filter by {@code fullname}, {@code actual}, {@code status}, {@code open} fields.
 */
@XFWDataSource("TaskByFilter")
abstract class TaskByFilter implements DataSourceLoader<Task, String> {
	/**
	 * User's full name.
	 */
	public String fullName

	/**
	 * Actuality of current project.
	 */
	public Boolean actual

	/**
	 * Status of task.
	 */
	public Integer status

	/**
	 * Flag on open projects.
	 */
	public Boolean open
}
