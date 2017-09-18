package se.vgregion.arbetsplatskoder.repository;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import se.vgregion.arbetsplatskoder.db.service.Crud;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Viewapkforsesamlmn;

import java.nio.file.Files;
import java.nio.file.Paths;

@Deprecated
public class IntegrationExportRepository {

    public static boolean isExportConfigPresentInEnvironment() {
        return Files.exists(Paths.get(System.getProperty("user.home"), ".app", "arbetsplatskoder", "export.data.jdbc.properties"));
    }

    public Crud getCrud() {
        ApplicationContext context =
            new ClassPathXmlApplicationContext("db-export-data-context.xml");
        Crud crud = context.getBean(Crud.class);
        return crud;
    }

    public static void main(String[] args) {
        ApplicationContext context =
            new ClassPathXmlApplicationContext("db-export-data-context.xml");
        Crud crud = context.getBean(Crud.class);
        System.out.println(crud);
        crud.create(new Viewapkforsesamlmn());
        System.out.println(crud.execute("delete from viewapkforsesamlmn"));
    }

}
