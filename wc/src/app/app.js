define(["require", "exports", "core", "app/domain/model-ext", "lib/interop/DataFacadeSmart", "lib/interop/BackendInterop", "lib/data/DataStoreFactory", "lib/ui/menu/AppNavMenu", "lib/ui/AppToolbar", "xhtmpl!app/ui/templates/app.hbs", "app/iconProvider", "app/default-menus", "app/default-parts", "app/parts", "app/area-default"], function (require, exports, core, model, DataFacadeSmart, BackendInterop, DataStoreFactory, AppNavMenu, AppToolbar, templateApp) {
    "use strict";
    var Application = (function (_super) {
        __extends(Application, _super);
        function Application() {
            var _this = _super.call(this, xconfig, { template: templateApp }) || this;
            _this.model = model;
            return _this;
        }
        Application.prototype.createDataFacade = function () {
            return DataStoreFactory
                .create(xconfig.appName, /*db version:*/ 1, model.meta)
                .then(function (store) {
                return new DataFacadeSmart(new BackendInterop(xconfig), null, // eventPublisher, Application will initialize it while running
                store, {});
            });
        };
        Application.prototype.preinitialize = function () {
            var that = this;
            that.appToolbar = new AppToolbar(that, {});
        };
        Application.prototype.initialize = function () {
            var that = this;
            // top navigation menu (switching areas):
            // NOTE: AppNavToolbar cannot be created in preinitialize and rendered in template as it depends on AreaManager which is initialized after template rendered
            that.navMenu = new AppNavMenu(that.areaManager);
            that.appToolbar.appNavMenu(that.navMenu);
        };
        return Application;
    }(core.Application));
    return Application;
});
//# sourceMappingURL=app.js.map