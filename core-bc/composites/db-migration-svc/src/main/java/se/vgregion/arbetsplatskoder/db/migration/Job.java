package se.vgregion.arbetsplatskoder.db.migration;

import se.vgregion.arbetsplatskoder.db.migration.sql.SchemaInf;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by clalu4 on 2017-03-22.
 */
public class Job extends AbstractJob {

    public static void main(String[] args) {
        Job job = new Job();
        job.init(); // Connect to two databases. Old one 'legacy' and the 'new' one 'main'.
        /*job.copyTypesFromLegacyToDiscCache(); // This step takes som time.
        // Ignore it if you are in a hurry and if this already have been done (and the cached tables are up to date).
        job.dropTablesAlreadyInMain();
        job.createTablesInMainDatabase();
        job.copyDataToDiscCache();
        job.copyDataIntoMainDatabase();*/
        // job.findTableAndColumnNamesInsideFiles();
        job.createEntitySourceFiles();
    }

}
