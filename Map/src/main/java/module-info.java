module Map {
    exports dk.sdu.mmmi.cbse.mapsystem;

    requires Common;
    requires javafx.graphics;

    provides dk.sdu.mmmi.cbse.common.services.IGamePluginService
            with dk.sdu.mmmi.cbse.mapsystem.MapPlugin;
}
