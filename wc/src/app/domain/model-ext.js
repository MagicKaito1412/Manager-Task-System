define(["require", "exports", "app/domain/model"], function (require, exports, model) {
    "use strict";
    model.User.prototype.fullName = function () {
        return this.firstName() + " " + this.middleName() + " " + this.lastName();
    };
    return model;
});
//# sourceMappingURL=model-ext.js.map