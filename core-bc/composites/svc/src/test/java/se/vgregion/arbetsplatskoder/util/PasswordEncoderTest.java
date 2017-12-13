package se.vgregion.arbetsplatskoder.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class PasswordEncoderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordEncoderTest.class);

    @Test
    public void encodeAndMatchPassword() throws Exception {
        String password = "asdf";

        PasswordEncoder instance = PasswordEncoder.getInstance();

        String encoded = instance.encodePassword(password);

        LOGGER.info("Encoded password: " + encoded);

        assertTrue(instance.matches(password, encoded));
    }

    @Test
    public void previouslyCreatedEncodedMatches() throws Exception {
        String encoded = "$2a$10$i.ltueMhmrwDyBMuPzVHT.7k4dQ7e1FECCkmvxPrGEG.xCruHLMk.";

        assertTrue(PasswordEncoder.getInstance().matches("asdf", encoded));
    }
}