define([
	"core", 
	"i18n!app/nls/resources"
], function (core, resources) {
	"use strict";

	core.createAreaModule(function (app, area) {
		area.title = resources["area.main"];

		// Create 'dashboard' state for default area and
		// associate 'Dashboard' part with this state:
		area.addState({ name: "dashboard", title: "Главная страница" }, "Dashboard");

		// Auto-create an areaState for each persistent entity in the domain model
/*		core.lang.forEach(app.model.meta.entities, function (entity) {
			if (entity.temp || entity.abstract) { return; } // ignore temporary and abstract
			area.addState({ name: entity.name, title: entity.descr }, "ObjectList:" + entity.name);
		});*/
		
		// You can manually create areaStates instead of auto-creating, e.g.:
		area.addState({ name: "Пользователи", title: "Пользователи" }, "ObjectList:User");
		area.addState({ name: "Задачи", title: "Задачи" }, "ObjectList:Task");
		area.addState({ name: "Проекты", title: "Проекты" }, "ObjectList:Project");
		area.addState({ name: "Журнал изменений", title: "Журнал" }, "ObjectList:ChangeLog");
		area.addState({ name: "Неподтверждённые данные", title: "Неподтверждённые задачи" }, "ObjectList:EditeTask");

		area.setDefaultState("dashboard");
	});
});