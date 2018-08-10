define([ "core", "xhtmpl!./ui/templates/dashboard.hbs",
		"lib/ui/handlebars/View", "lib/ui/list/ObjectList" ], function(core,
		dashboardTemplate, View) {
	"use strict";

	core.createModule(function(app) {

		app.registerPart("Dashboard", function() {
			return View.create({
				template : dashboardTemplate
			});
		});

		app.registerPart("ObjectList:Task", function() {
			return core.ui.ObjectList.create(app, {
				dataSource : new core.data.DataSource(app, {
					name : "TaskByFilter"
				}),
				entityType : "Task",
				title : "Задачи",
				autoLoad : true,
				filter : "ObjectFilter:Task",
				inlineEdit : true,
				columns : [
					{
						name : "numberTaskInProject",
						title : "Номер задачи в проекте"
					},
						{
							name : "taskPerformer",
							title : "Исполнитель",
							getter : function() {
								return this.taskPerformer().lastName() + " "
										+ this.taskPerformer().firstName()
										+ " "
										+ this.taskPerformer().middleName();
							}
						}, {
							name : "project",
							title : "Проект",
							getter : function() {
								return this.project().title();
							}
						}, {
							name : "status",
							title : "Статус"
						}, {
							name : "description",
							title : "Описание"
						},
						{
							name : "startline",
							title : "Дата создания",
						},
						{
							name : "deadline",
							title : "Выполнить до"
						}, {
							name : "wastedTime",
							title : "Затраченное время"
						} ]
			});
		});

		app.registerPart("ObjectFilter:Task", function(options) {
			return core.ui.ObjectFilter.create({
				type : "TaskFilter",
				pages : [ {
					properties : [ "fullName", "project", "actual", "status",
							"open" ]
				} ]
			});
		});

		app.registerPart("ObjectEditor:Task", function(options) {
			core.lang.append(options, {
				pages : [
						{
							title : "Основные",
							properties : [ "description", "status", "deadline",
									"type", "project", "taskPerformer" ]
						}, {
							title : "Дополнительные сведения",
							properties : [ {
								name : "numberTaskInProject",
								readOnly : true
							}, {
								name : "startline",
								readOnly : true
							}, {
								name : "wastedTime",
								readOnly : true
							}, {
								name : "changedDate",
								readOnly : true
							}, {
								name : "author",
								readOnly : true
							}, {
								name : "lastEditor",
								readOnly : true
							} ]
						}, {
							title : "Связанные задачи",
							properties : [ {
								name : "lowerPerformentTask",
								readOnly : true
							}, "tasks" ]
						} ]
			});

			return core.ui.ObjectEditor.create(options);
		});

		app.registerPart("ObjectEditor:Project", function(options) {
			core.lang.append(options, {
				pages : [ {
					title : "Основные",
					properties : [ "title", "codeProject", "title", {
						name : "taskCounter",
						readOnly : true
					} ]
				}, {
					title : "Задачи проекта",
					properties : [ "projectTasks" ]
				}, {
					title : "Участники проекта",
					properties : [ "crew" ]
				} ]
			});
			return core.ui.ObjectEditor.create(options);
		});

		app.registerPart("ObjectList:User", function() {
			return core.ui.ObjectList.create(app, {

				entityType : "User",
				title : "Пользователи",
				autoLoad : true,
				inlineEdit : true,
				columns : [ {
					name : "lastName",
					title : "Фамилия",
				}, {
					name : "firstName",
					title : "Имя"
				}, {
					name : "middleName",
					title : "Отчество"
				}, {
					name : "login",
					title : "Логин"
				} ]
			});
		});

		app.registerPart("ObjectEditor:User", function(options) {
			core.lang.append(options, {
				pages : [ {
					title : "Основные",
					properties : [ {
						name : "lastName",
						title : "Фамилия",
					}, {
						name : "firstName",
						title : "Имя"
					}, {
						name : "middleName",
						title : "Отчество"
					}, {
						name : "login",
						title : "Логин"
					}, "isAdmin", "password" ]
				}, {
					title : "Исполняемые задачи",
					properties : [ {
						name : "performentTasks",
						readOnly : true,
						title : "Исполняемые задачи"
					} ]
				}, {
					title : "Роль в проектах",
					properties : [ {
						name : "roleUserProject",
						readOnly : true,
						title : "Роль в проектах"
					} ]
				} ]
			});
			return core.ui.ObjectEditor.create(options);
		});

		app.registerPart("ObjectEditor:EditeTask", function(options) {
			core.lang.append(options, {
				pages : [ {
					title : "Основные",
					properties : [ {
						name : "description",
						readOnly : true
					}, {
						name : "status",
						readOnly : true
					}, {
						name : "deadline",
						readOnly : true
					}, {
						name : "type",
						readOnly : true
					}, {
						name : "project",
						readOnly : true
					}, {
						name : "taskPerformer",
						readOnly : true
					}, "isCheck" ]
				} ]
			});
			return core.ui.ObjectEditor.create(options);
		});

		app.registerPart("ObjectEditor:ChangeLog", function(options) {
			core.lang.append(options, {
				pages : [ {
					title : "Основные",
					properties : [ {
						name : "changedDate",
						readOnly : true
					}, {
						name : "typeAction",
						readOnly : true
					}, {
						name : "typeEntity",
						readOnly : true
					}, {
						name : "entityId",
						readOnly : true
					}, {
						name : "author",
						readOnly : true
					} ]
				} ]
			});
			return core.ui.ObjectEditor.create(options);
		});

	});
});
