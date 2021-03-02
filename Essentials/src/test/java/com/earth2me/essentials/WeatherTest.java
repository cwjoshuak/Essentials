package com.earth2me.essentials;

import com.earth2me.essentials.commands.Commandpweather;
import com.earth2me.essentials.commands.Commandweather;
import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.craftbukkit.FakeWorld;
import org.bukkit.WeatherType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import java.io.IOException;
import java.util.UUID;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WeatherTest {
    private final OfflinePlayer base1;
    // private final OfflinePlayer base2;
    private final Essentials ess;
    private final Essentials ess1;
    private final FakeServer server;
    private final Commandpweather playerW;
    private final Commandweather serverW;

    @Mock
    Player mockedPlayer;

    FakeServer mockServer;
    FakeServer spyServer;
    World spyWorld;

    public WeatherTest(){
        //super(testName);
        mockServer = new FakeServer();
        // mockServer.createWorld("mockWorld", World.Environment.NORMAL);
        final World w = new FakeWorld("mockWorld", World.Environment.NORMAL);
        spyWorld = Mockito.spy(w);
        mockServer.addWorld(spyWorld);
        spyServer = Mockito.spy(mockServer);
        ess1 = new Essentials(spyServer);
        // spyServer.createWorld("mockWorld", World.Environment.NORMAL);
        server = new FakeServer();
        server.createWorld("testWorld", World.Environment.NORMAL);
        ess = new Essentials(server);
        setupMockPlayer();
        try {
            ess.setupForTesting(server);
            ess1.setupForTesting(spyServer);
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
        System.out.println("TEST:" +mockedPlayer);

        System.out.println("TEST: "+ess.getUser(mockedPlayer));

        playerW = new Commandpweather();
        serverW = new Commandweather();
    }

    private void setupMockPlayer() {
        mockedPlayer = Mockito.mock(Player.class);
        Mockito.when(mockedPlayer.getUniqueId()).thenReturn(UUID.randomUUID());
        Mockito.when(mockedPlayer.getName()).thenReturn("mockedPlayer");
        server.addPlayer(mockedPlayer);
        Mockito.when(mockedPlayer.isOnline()).thenReturn(true);
        Mockito.when(mockedPlayer.getWorld()).thenReturn(server.getWorld("testWorld"));
        Mockito.when(mockedPlayer.getPlayerWeather()).thenReturn(WeatherType.CLEAR, WeatherType.DOWNFALL);
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

    private void runConsoleCommandOnSpyServer(final String command, final String[] args) throws Exception {
        final IEssentialsCommand cmd;

        final CommandSender sender = server.getConsoleSender();

        try {
            cmd = (IEssentialsCommand) Essentials.class.getClassLoader().loadClass("com.earth2me.essentials.commands.Command" + command).newInstance();
            cmd.setEssentials(ess);
            cmd.run(spyServer, new CommandSource(sender), command, null, args);
        } catch (final NoChargeException ignored) {
        }

    }

    @Test
    public void setSun(){
        final User user = ess.getUser(base1);
        try {
            runConsoleCommand("weather", new String[]{server.getWorld("testWorld").getName(), "sun"});
            assertTrue(user.getWorld().isClearWeather());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void setClear(){
        final User user = ess.getUser(base1);
        try {
            runConsoleCommand("weather", new String[]{server.getWorld("testWorld").getName(), "clear"});
            assertTrue(user.getWorld().isClearWeather());
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

    @Test
    public void testSpyServerSetWeather() {

        try {
            runConsoleCommandOnSpyServer("weather", new String[]{spyServer.getWorld("mockWorld").getName(),"storm"});
            Mockito.verify(spyWorld).setStorm(true);
            assertTrue(spyServer.getWorld("mockWorld").hasStorm());

            runConsoleCommandOnSpyServer("weather", new String[]{spyServer.getWorld("mockWorld").getName(),"clear"});
            Mockito.verify(spyWorld).setStorm(false);
            assertFalse(spyServer.getWorld("mockWorld").hasStorm());
            assertTrue(spyServer.getWorld("mockWorld").isClearWeather());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
