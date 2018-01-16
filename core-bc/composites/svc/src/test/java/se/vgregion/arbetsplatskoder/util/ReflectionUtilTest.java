package se.vgregion.arbetsplatskoder.util;

import org.junit.Test;
import se.vgregion.arbetsplatskoder.domain.jpa.migrated.Data;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ReflectionUtilTest {

    @Test
    public void getDeclaredFieldSuperClass() throws NoSuchFieldException {
        Field tillDatum = ReflectionUtil.getDeclaredField("tillDatum", Data.class);

        assertNotNull(tillDatum);
    }

    @Test
    public void getDeclaredFieldThisClass() throws NoSuchFieldException {
        Field tillDatum = ReflectionUtil.getDeclaredField("prodn1", Data.class);

        assertNotNull(tillDatum);
    }

    @Test
    public void getDeclaredFieldNotFound() throws NoSuchFieldException {
        Field tillDatum = ReflectionUtil.getDeclaredField("asdfasdfaea", Data.class);

        assertNull(tillDatum);
    }
}