package com.earth2me.essentials;

import com.earth2me.essentials.commands.IEssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import net.ess3.api.events.AfkStatusChangeEvent;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;

import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AFKTest {
    private final OfflinePlayer base1;
    //    private final OfflinePlayer base2;
    private final Essentials ess;
    private final FakeServer server;

    public AFKTest() {
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
        server.addPlayer(base1);
        ess.getUser(base1);
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
    public void testCommandToggleAFK() {
        final User user = ess.getUser(base1);

        try {
            user.setAfk(false); // set initial state

            runCommand("afk", user, new String[] {});
            assertTrue(user.isAfk());

            runCommand("afk", user, new String[] {});
            assertFalse(user.isAfk());

            user.setAfk(true); // set opposite initial state
            runCommand("afk", user, new String[] {});
            assertFalse(user.isAfk());

            runCommand("afk", user, new String[] {});
            assertTrue(user.isAfk());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testActivityAfterAFK() {
        final User user = ess.getUser(base1);

        try {
            // loops all AfkStatusChangeEvent.Cause values:
            // ACTIVITY, MOVE, INTERACT, COMMAND, JOIN, CHAT, QUIT, UNKNOWN
            for(AfkStatusChangeEvent.Cause c : AfkStatusChangeEvent.Cause.values()) {
                user.setAfk(false); // set initial state

                runCommand("afk", user, new String[] {});
                assertTrue(user.isAfk());

                user.updateActivity(false, c);
                assertFalse(user.isAfk());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToggleAFKUserMethod() {
        final User user = ess.getUser(base1);
        try {
            user.setAfk(false); // set initial state

            user.toggleAfk(AfkStatusChangeEvent.Cause.UNKNOWN);
            assertTrue(user.isAfk());

            user.toggleAfk(AfkStatusChangeEvent.Cause.UNKNOWN);
            assertFalse(user.isAfk());

            user.setAfk(true); // set opposite initial state

            user.toggleAfk(AfkStatusChangeEvent.Cause.UNKNOWN);
            assertFalse(user.isAfk());

            user.toggleAfk(AfkStatusChangeEvent.Cause.UNKNOWN);
            assertTrue(user.isAfk());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testServerCommandToggleAFK() {
        final User user = ess.getUser(base1);

        try {
            user.setAfk(false); // set initial state

            runConsoleCommand("afk", new String[] {user.getName()});
            assertTrue(user.isAfk());

            runConsoleCommand("afk", new String[] {user.getName()});
            assertFalse(user.isAfk());

            user.setAfk(true); // set opposite initial state
            runConsoleCommand("afk", new String[] {user.getName()});
            assertFalse(user.isAfk());

            runConsoleCommand("afk", new String[] {user.getName()});
            assertTrue(user.isAfk());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
