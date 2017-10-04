package se.vgregion.arbetsplatskoder.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class SambaFileClientTest {


    public static void main(String[] args) throws IOException {
        System.out.println(
            Arrays.asList(
                SambaFileClient.listPathContent(
                    "secret",
                    "secret",
                    "secret",
                    "secret"
                )
            )
        );
    }

}