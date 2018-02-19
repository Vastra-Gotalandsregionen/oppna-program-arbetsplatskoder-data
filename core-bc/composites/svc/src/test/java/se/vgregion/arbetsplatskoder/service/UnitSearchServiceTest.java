package se.vgregion.arbetsplatskoder.service;

import org.junit.Before;
import org.junit.Test;
import se.vgregion.arbetsplatskoder.domain.json.Attributes;
import se.vgregion.arbetsplatskoder.domain.json.RolesRoot;
import se.vgregion.arbetsplatskoder.domain.json.Unit;
import se.vgregion.arbetsplatskoder.domain.json.UnitsRoot;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UnitSearchServiceTest {

    private final UnitSearchService unitSearchService = new UnitSearchService();

    @Before
    public void init() throws Exception {

        ArrayList<Unit> units = new ArrayList<>();

        Unit u1 = new Unit();
        Unit u2 = new Unit();
        Unit u3 = new Unit();

        Attributes a1 = new Attributes();
        Attributes a2 = new Attributes();
        Attributes a3 = new Attributes();

        a1.setOu(new String[]{"foo bar xyz"});
        a2.setOu(new String[]{"apa bepa cepa"});
        a3.setOu(new String[]{"xxx yyy zzz"});

        u1.setAttributes(a1);
        u2.setAttributes(a2);
        u3.setAttributes(a3);

        u1.setDn("u1");
        u2.setDn("u2");
        u3.setDn("u3");

        units.add(u1);
        units.add(u2);
        units.add(u3);

        UnitsRoot unitsRoot = new UnitsRoot();
        unitsRoot.setUnits(units);

        RolesRoot rolesRoot = new RolesRoot();
        rolesRoot.setUnits(new ArrayList<>());

        Field unitsRootField = unitSearchService.getClass().getDeclaredField("unitsRoot");
        Field rolesRootField = unitSearchService.getClass().getDeclaredField("rolesRoot");

        unitsRootField.setAccessible(true);
        rolesRootField.setAccessible(true);

        unitsRootField.set(unitSearchService, unitsRoot);
        rolesRootField.set(unitSearchService, rolesRoot);
    }

    @Test
    public void searchUnits() throws Exception {

        List<Unit> result = unitSearchService.searchUnits("apa cepa");

        assertEquals(1, result.size());
        assertEquals("u2", result.get(0).getDn());
    }

}