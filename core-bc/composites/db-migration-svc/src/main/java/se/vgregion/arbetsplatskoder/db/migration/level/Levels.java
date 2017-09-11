package se.vgregion.arbetsplatskoder.db.migration.level;

import se.vgregion.arbetsplatskoder.db.migration.AbstractJob;
import se.vgregion.arbetsplatskoder.db.migration.sql.ConnectionExt;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Column;
import se.vgregion.arbetsplatskoder.db.migration.sql.meta.Schema;

import java.util.Map;

public class Levels extends AbstractLevels {

  public Levels(ConnectionExt connection) {
    super(connection);
  }

  public static void main(String[] args) {
    Levels levels = new Levels(AbstractJob.getMainConnectionExt());
    levels.undoPreviousLevelCreation();
    /*levels.copyLevelsFromFileIntoDatabase();
    levels.pairCodeWithNewLevels();
    levels.markOldLevelsAsObsolete();
    levels.compareDatabaseWithFileContent();*/

    levels.runImportWork();
    levels.disableProdnsNotInImport();
    levels.commit();
  }

}
