package com.earth2me.essentials;

import com.earth2me.essentials.commands.Commandpweather;
import com.earth2me.essentials.commands.Commandweather;
import junit.framework.TestCase;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.plugin.InvalidDescriptionException;

import java.io.IOException;
import java.util.Locale;

public class WeatherTest extends TestCase {
    private final OfflinePlayer base1, base2;
    private final Essentials ess;
    private final FakeServer server;
    private final Commandpweather playerW;
    private final Commandweather serverW;

    //no clue where this testName is established
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
        base2 = server.createPlayer("testPlayer2");
        server.addPlayer(base1);
        server.addPlayer(base2);
        ess.getUser(base1);
        //ess.getUser(base2);   //possibly not needed, only returns User?

        playerW = new Commandpweather();
        serverW = new Commandweather();
    }

    public void userWeather(){
        final User user = ess.getUser(base1);

        //structure of call /pweather get *, or /pweather <weather condition> <target user>
        try{
            playerW.run(server, null, null, new String[]{"sun", "testPlayer1"});
            //need to change sender from null to player identifier?

            assertEquals(user.getBase().getPlayerWeather().toString().toLowerCase(Locale.ENGLISH), WeatherType.CLEAR.toString());

            //if works, repeat for all cases

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serverWeather(){
        final User user = ess.getUser(base1);

        //command label structure is unknown, assume arg structure is {"sun", "10"} where # is duration
        try{
            serverW.run(server, user, "/weather sun", null);
            assertEquals(user.getWorld().isClearWeather(), true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
