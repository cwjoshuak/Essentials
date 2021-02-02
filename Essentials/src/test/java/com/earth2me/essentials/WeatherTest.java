package com.earth2me.essentials;

import com.earth2me.essentials.commands.Commandpweather;
import com.earth2me.essentials.commands.Commandweather;
import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import junit.framework.TestCase;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;

import java.io.IOException;
import java.util.Locale;
import org.junit.Test;
import org.junit.Assert;

public class WeatherTest {
    private final OfflinePlayer base1, base2;
    private final Essentials ess;
    private final FakeServer server;
    private final Commandpweather playerW;
    private final Commandweather serverW;

    //no clue where this testName is established
    public WeatherTest(){
        //super(testName);
        server = new FakeServer();
        server.createWorld("testWorld", World.Environment.NORMAL);
        ess = new Essentials(server);
        try {
            ess.setupForTesting(server);
        } catch (final InvalidDescriptionException ex) {
            Assert.fail("InvalidDescriptionException");
        } catch (final IOException ex) {
            Assert.fail("IOException");
        }
        base1 = server.createPlayer("testPlayer1");
        base2 = server.createPlayer("testPlayer2");
        server.addPlayer(base1);
        server.addPlayer(base2);
        ess.getUser(base1);
        ess.getUser(base2);   //possibly not needed, only returns User?

        playerW = new Commandpweather();
        serverW = new Commandweather();
    }

    private void runCommand(final String command, final User user, final String[] args) throws Exception {
        final IEssentialsCommand cmd;

        try {
            cmd = (IEssentialsCommand) Essentials.class.getClassLoader().loadClass("com.earth2me.essentials.commands.Command" + command).newInstance();
            cmd.setEssentials(ess);
            cmd.run(server, user, command, null, args);
        } catch (final NoChargeException ignored) {
        }

    }

    private void runConsoleCommand(final String command, final String[] args) throws Exception {
        final IEssentialsCommand cmd;

        final CommandSender sender = server.getConsoleSender();

        try {
            cmd = (IEssentialsCommand) Essentials.class.getClassLoader().loadClass("com.earth2me.essentials.commands.Command" + command).newInstance();
            cmd.setEssentials(ess);
            cmd.run(server, new CommandSource(sender), command, null, args);
        } catch (final NoChargeException ignored) {
        }

    }

    @Test
    public void userWeather(){
        final User user = ess.getUser(base1);

        //structure of call /pweather get *, or /pweather <weather condition> <target user>
        try{
            //playerW.run(server, null, null, new String[]{"sun", "testPlayer1"});
            runCommand("pweather",user, new String[] {"sun", "testPlayer1"});
            Assert.assertEquals(user.getBase().getPlayerWeather().toString().toLowerCase(Locale.ENGLISH), WeatherType.CLEAR.toString());

            //if works, repeat for all cases
            runCommand("pweather",user, new String[] {"clear", "testPlayer1"});
            Assert.assertEquals(user.getBase().getPlayerWeather().toString().toLowerCase(Locale.ENGLISH), WeatherType.CLEAR.toString());
            runCommand("pweather",user, new String[] {"storm", "testPlayer1"});
            Assert.assertEquals(user.getBase().getPlayerWeather().toString().toLowerCase(Locale.ENGLISH), WeatherType.DOWNFALL.toString());
            runCommand("pweather",user, new String[] {"thunder", "testPlayer1"});
            Assert.assertEquals(user.getBase().getPlayerWeather().toString().toLowerCase(Locale.ENGLISH), WeatherType.DOWNFALL.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void serverWeather(){
//        final User user = ess.getUser(base1);
//
//        //command label structure is unknown, assume arg structure is {"sun", "10"} where # is duration
//        try{
//            serverW.run(server, user, "/weather sun", null);
//            Assert.assertTrue(user.getWorld().isClearWeather());
//
//            //if above works, although haven't tested when arg != null
//            serverW.run(server, user, "/weather clear", null);
//            Assert.assertTrue(user.getWorld().isClearWeather());
//            serverW.run(server, user, "/weather storm", null);
//            Assert.assertFalse(user.getWorld().isClearWeather());
//            serverW.run(server, user, "/weather thunder", null);
//            Assert.assertFalse(user.getWorld().isClearWeather());
//            //test reset command
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
