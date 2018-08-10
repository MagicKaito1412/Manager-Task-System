import core = require("core");
import model = require("app/domain/model");

// TODO: здесь расширение классов доменной модели
     declare module "app/domain/model" {
        interface  User {
            fullName(): string;
        }
    }

    model.User.prototype.fullName = function () {
        return this.firstName() + " " + this.middleName() + " " + this.lastName();
    };
export = model;    
