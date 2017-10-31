package se.vgregion.arbetsplatskoder.db.migration;

/**
 * Created by clalu4 on 2017-03-22.
 */
public class Job extends AbstractJob {

    public static void main(String[] args) {
        Job job = new Job();
        job.init(); // Connect to two databases. Old one 'legacy' and the 'new' one 'main'.
        job.copyTypesFromLegacyToDiscCache(); // This step takes som time.
        // Ignore it if you are in a hurry and if this already have been done (and the cached tables are up to date).
        job.dropTablesAlreadyInMain();
        job.dropOtherJpaTables();
        job.createTablesInMainDatabase();
        job.copyDataToDiscCache();
        job.copyDataIntoMainDatabase();
        // job.findTableAndColumnNamesInsideFiles();
        // job.createEntitySourceFiles();

        /*

        After this:

        0, Possibly. If there are items in the _user and _user_prodn1 table: Use the
        se.vgregion.arbetsplatskoder.db.migration.util.Users.main method / functions to backup these items, then remove
        them and finally put them back into the db.

        1, Deploy the web-app so that jpa will modify the db.

        2, Run Levels.main. That will import/correct productions levels (prodn1-3 for the codes) from file in
        this project.

        3, Run se.vgregion.arbetsplatskoder.db.migration.ImportHistory.main. That will migrate historik-table to
        archived_data.

        4, Possibly. Put the _user:s back in the db - se step #0.

        */
    }

}
