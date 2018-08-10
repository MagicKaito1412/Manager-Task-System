package ru.croc.ctp.mts.domain

import java.time.LocalDateTime
import java.util.Set
import javax.persistence.FetchType
import javax.persistence.JoinColumn

import ru.croc.ctp.jxfw.core.generator.meta.XFWObject
import ru.croc.ctp.jxfw.core.domain.meta.XFWElementLabel

import ru.croc.ctp.jxfw.core.generator.meta.XFWProtected

import ru.croc.ctp.jxfw.core.domain.meta.XFWEnumId
import ru.croc.ctp.jxfw.core.generator.meta.XFWEnum
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.Column

import ru.croc.ctp.jxfw.core.domain.meta.persistence.XFWManyToOne
import javax.persistence.Lob
import ru.croc.ctp.jxfw.core.domain.meta.persistence.XFWOneToMany
import javax.validation.constraints.Size
import ru.croc.ctp.jxfw.core.domain.meta.XFWDefaultValue
import com.google.common.annotations.GwtCompatible

/**
 * Class of user of the system.
 */
@XFWObject
@XFWElementLabel("Пользователь")
class User {

	@XFWElementLabel("Админ")
	@Column(nullable=false)
	Boolean isAdmin

	@XFWElementLabel("Имя")
	@Column(nullable=false, length=255)
	String firstName

	@XFWElementLabel("Фамилия")
	@Column(nullable=false, length=255)
	String lastName

	@XFWElementLabel("Отчество")
	@Column(nullable=false, length=255)
	String middleName

	@XFWElementLabel("Логин")
	@Column(nullable=false, length=30)
	String login

	@XFWElementLabel("Пароль")
	@Column(nullable=false, length=255)
	@Size(min=6, message="Пароль должен быть больше 6 символов!")
	@XFWProtected
	String password

	@XFWOneToMany(mappedBy="taskPerformer", targetEntity=Task, fetch=FetchType.LAZY, orphanRemoval=true)
	@XFWElementLabel("Исполняемые задачи")
	Set<Task> performentTasks

	@XFWOneToMany(mappedBy="projectUser", targetEntity=ProjectRole, fetch=FetchType.LAZY, orphanRemoval=true)
	@XFWElementLabel("Роль в проектах")
	Set<ProjectRole> roleUserProject
}

/**
 * Class of user's tasks in project.
 */
@XFWObject
@XFWElementLabel("Задача")
class Task {
	@XFWElementLabel("Номер задачи в проекте")
	@Column(nullable=true)
	String numberTaskInProject

	@Lob
	@XFWElementLabel("Описание")
	String description

	@XFWElementLabel("Статус")
	@Column(nullable=false)
	Status status

	@XFWElementLabel("Выполнить до")
	@Column(nullable=false)
	LocalDateTime deadline

	@XFWElementLabel("Дата создания")
	@Column(nullable=true)
	LocalDateTime startline

	@XFWElementLabel("Затраченное время")
	String wastedTime

	@XFWElementLabel("Дата последнего изменения")
	LocalDateTime changedDate

	@XFWElementLabel("Тип задачи")
	@Column(nullable=false)
	TaskType type

	@XFWManyToOne(optional=false)
	@XFWElementLabel("Проект")
	Project project

	@XFWManyToOne
	@XFWElementLabel("Автор")
	@JoinColumn(nullable=true)
	User author

	@XFWManyToOne(optional=false)
	@XFWElementLabel("Исполнитель")
	User taskPerformer

	@XFWManyToOne
	@JoinColumn(nullable=true)
	@XFWElementLabel("Последний редактор")
	User lastEditor

	@XFWManyToOne
	@XFWElementLabel("Нижестоящая задача")
	@JoinColumn(nullable=true)
	Task lowerPerformentTask

	@XFWOneToMany(mappedBy="lowerPerformentTask", targetEntity=Task, fetch=FetchType.LAZY, orphanRemoval=true)
	@XFWElementLabel("Связанные задачи")
	Set<Task> tasks

	@XFWElementLabel("Подтверждено")
	Boolean isCheck

}

/**
 * Class of task, that needs in edition.
 */
@GwtCompatible(serializable=true)
@XFWObject
@XFWElementLabel("Задачи на подтверждение")
class EditeTask {

	@XFWElementLabel("Id задачи")
	String taskId

	@XFWElementLabel("Описание")
	String description

	@XFWElementLabel("Статус")
	@Column(nullable=true)
	String status

	@XFWElementLabel("Выполнить до")
	LocalDateTime deadline

	@XFWElementLabel("Тип задачи")
	@Column(nullable=true)
	TaskType type

	@XFWElementLabel("Проект")
	Project project

	@XFWElementLabel("Исполнитель")
	@Column(nullable=true)
    User taskPerformer 
     
	@XFWElementLabel("Подтверждено")
	Boolean isCheck
}

/**
 * Class about user's projects.
 */
@XFWObject
@XFWElementLabel("Проект")
class Project {
	@Lob
	@XFWElementLabel("Описание")
	String description

	@XFWElementLabel("Название")
	@Column(nullable=false)
	String title

	@XFWElementLabel("Код проекта")
	@Column(nullable=false)
	String codeProject

	@XFWElementLabel("Количество задач в проекте")
	@XFWDefaultValue(value="0")
	Integer taskCounter

	@XFWOneToMany(mappedBy="project", targetEntity=Task, fetch=FetchType.LAZY, orphanRemoval=true)
	@XFWElementLabel("Задачи проекта")
	Set<Task> projectTasks

	@XFWOneToMany(mappedBy="numberProject", targetEntity=ProjectRole, fetch=FetchType.LAZY, orphanRemoval=true)
	@XFWElementLabel("Участники проекта")
	Set<ProjectRole> crew
}

/**
 * Class shows any changes in system.
 */
@XFWObject
@XFWElementLabel("Журнал изменений")
class ChangeLog {

	@XFWElementLabel("Дата изменения")
	@Column(nullable=false)
	LocalDateTime changedDate

	@XFWElementLabel("Тип действий")
	@Column(nullable=false)
	TypeAction typeAction

	@XFWElementLabel("Тип сущности")
	@Column(nullable=false)
	String typeEntity

	@XFWElementLabel("ID сущности")
	String entityId

	@XFWElementLabel("Автор")
	@Column(nullable=false)
	String author

}

/**
 * Dependent class connects User and Project entities and shows user's role in project.
 */
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@XFWObject
@XFWElementLabel("Роль пользователя в проекте")
class ProjectRole {

	@XFWElementLabel("Название роли")
	UserRole userRole

	@XFWElementLabel("Проект")
	@XFWManyToOne(targetEntity=Project)
	@JoinColumn(nullable=true)
	Project numberProject

	@XFWElementLabel("Участник проекта")
	@XFWManyToOne(targetEntity=User)
	@JoinColumn(nullable=true)
	User projectUser

}

/**
 * Enum class enumerate user's possible roles.
 */
@XFWEnum
enum UserRole {

	@XFWElementLabel("Аналитик")
    @XFWEnumId(2)
    Analyst,

	@XFWElementLabel("Разработчик")
    @XFWEnumId(4)
    Developer,

	@XFWElementLabel("Ведущий разработчик")
    @XFWEnumId(16)
    LeadDeveloper,

	@XFWElementLabel("Менеджер")
    @XFWEnumId(32)
    Manager,

	@XFWElementLabel("Тестировщик")
    @XFWEnumId(64)
    Tester
}

/**
 * Enum class enumerate task's possible statuses.
 */
@XFWEnum
enum Status {

	@XFWElementLabel("Зарегистрирован")
    @XFWEnumId(2)
    REGISTRED,

	@XFWElementLabel("В реализации")
    @XFWEnumId(4)
    INPROCESS,

	@XFWElementLabel("На проверке")
    @XFWEnumId(8)
    CHECKING,

	@XFWElementLabel("На исправлении ошибок")
    @XFWEnumId(16)
    FIXPROBLEMS,

	@XFWElementLabel("Закрыт")
    @XFWEnumId(32)
    CLOSED
}

/**
 * Enum class enumerate task's possible types.
 */
@XFWEnum
enum TaskType {

	@XFWElementLabel("Задача")
    @XFWEnumId(2)
    Task,

	@XFWElementLabel("Ошибка")
    @XFWEnumId(4)
    Mistake,

	@XFWElementLabel("Требование")
    @XFWEnumId(8)
    Demand
}

/**
 * Enum class enumerate type of possible actions in ChangeLog.
 */
@XFWEnum
enum TypeAction {

	@XFWElementLabel("Удалить")
    @XFWEnumId(2)
    RemoveAct,

	@XFWElementLabel("Добавить")
    @XFWEnumId(4)
    AddAct,

	@XFWElementLabel("Обновить")
    @XFWEnumId(8)
    UpdateAct
}
