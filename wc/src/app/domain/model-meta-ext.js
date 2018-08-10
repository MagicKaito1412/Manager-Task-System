define(["lib/domain", "lib/formatters", "app/domain/model-meta", "i18n!app/domain/nls/resources"
], function (domain, formatters, metadata, resources) {
	"use strict";

	
	// TODO: здесь кастомизация метаданных (до buildModel)
	metadata.entities.TaskFilter = {
	        temp: true,
	        props: {
	            fullName: { descr: "ФИО", vt: "string", nullable: true, maxLen: 64 },
	            project: { descr: "Проект", vt: "object", ref: "Project", nullable: true, maxLen: 64 },
	            actual: { descr: "Актуальные", vt: "boolean", nullable: true },
	            status: { descr: "Статус", vt: "enum", ref: "Status",nullable: true },
	            open: { descr: "Открытые задачи", vt: "boolean", nullable: true }
	        	}
	    };

		return metadata;
});
