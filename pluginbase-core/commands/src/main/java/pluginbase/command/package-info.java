/**
 * Contains all the classes necessary for adding platform independent commands to a Minecraft plugin.
 * <br>
 * Your plugin will need a CommandProvider in order to register commands.  This command provider's generic type must
 * match the generic type used for your commands.  See {@link pluginbase.command.Command}.
 * <br>
 * Specific server implementation modules may be available, ex: Commands-Bukkit.  These implementations will provide
 * a CommandProvider which should be all you need to get started.
 * <br>
 * Provided by module com.dumptruckman.minecraft.pluginbase:Commands.
 */
package pluginbase.command;