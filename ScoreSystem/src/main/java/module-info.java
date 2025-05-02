import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.scoresystem.ScorePlugin;
import dk.sdu.mmmi.cbse.scoresystem.ScoreServiceImpl;

module ScoreSystem {
    exports dk.sdu.mmmi.cbse.scoresystem;
    requires Common;
    uses dk.sdu.mmmi.cbse.common.services.IScoreService;
    provides dk.sdu.mmmi.cbse.common.services.IScoreService with ScoreServiceImpl;
    provides IGamePluginService with ScorePlugin;

}