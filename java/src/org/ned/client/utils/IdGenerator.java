package org.ned.client.utils;

import java.util.Random;

public class IdGenerator {
    private static Random rng = new Random();

    private IdGenerator(){};

    public static String getId() {
        int id = 1000000 + rng.nextInt(8999999);
        String sid = String.valueOf(id);
        return ("id" + sid);
    }
}
