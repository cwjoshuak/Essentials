package com.earth2me.essentials;

import junit.framework.TestCase;
import org.bukkit.World;
import org.bukkit.plugin.InvalidDescriptionException;

import java.io.IOException;

public class WeatherTest extends TestCase {
    private final OfflinePlayer base1;
    private final Essentials ess;
    private final FakeServer server;

    //no clue where this testname is established
    public WeatherTest(final String testName){
        super(testName);
        server = new FakeServer();
        server.createWorld("testWorld", World.Environment.NORMAL);
        ess = new Essentials(server);
        try {
            ess.setupForTesting(server);
        } catch (final InvalidDescriptionException ex) {
            fail("InvalidDescriptionException");
        } catch (final IOException ex) {
            fail("IOException");
        }
        base1 = server.createPlayer("testPlayer1");
        server.addPlayer(base1);
        ess.getUser(base1);
    }

    public void userWeather(){}

    public void serverWeather(){}
}
