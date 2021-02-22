package com.earth2me.essentials;

import com.earth2me.essentials.commands.Commandpweather;
import com.earth2me.essentials.commands.Commandweather;
import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import java.io.IOException;
import org.junit.Test;
import org.junit.Assert;

public class WeatherTest {
    private final OfflinePlayer base1;
    // private final OfflinePlayer base2;
    private final Essentials ess;
    private final FakeServer server;
    private final Commandpweather playerW;
    private final Commandweather serverW;

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
        // base2 = server.createPlayer("testPlayer2");
        server.addPlayer(base1);
        // server.addPlayer(base2);
        ess.getUser(base1);
        // ess.getUser(base2);   //possibly not needed, only returns User?

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
    public void setSun(){
        final User user = ess.getUser(base1);
        try {
            runConsoleCommand("weather", new String[]{server.getWorld("testWorld").getName(), "sun"});
            Assert.assertTrue(user.getWorld().isClearWeather());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setClear(){
        final User user = ess.getUser(base1);
        try {
            runConsoleCommand("weather", new String[]{server.getWorld("testWorld").getName(), "clear"});
            Assert.assertTrue(user.getWorld().isClearWeather());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setStorm(){
        final User user = ess.getUser(base1);
        try{
            runConsoleCommand("weather", new String[]{server.getWorld("testWorld").getName(),"storm"});
            Assert.assertFalse(user.getWorld().isClearWeather());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
