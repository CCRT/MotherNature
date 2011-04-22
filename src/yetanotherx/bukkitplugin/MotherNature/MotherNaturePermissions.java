package yetanotherx.bukkitplugin.MotherNature;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.anjocaido.groupmanager.GroupManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MotherNaturePermissions {

    /**
     * Permission handlers
     */
    private static PermissionHandler Permissions;
    private static GroupManager GroupManager;

    private static HandlerType handlerType;

    private enum HandlerType {
	PERMISSIONS,
	GROUPMANAGER,
	VANILLA
    }

    /**
     * Check if permissions is installed, and initiate it
     */
    public static void load( MotherNature parent ) {
	MotherNature.log.debug("Loading Permissions handler");

	Plugin perm_plugin = parent.getServer().getPluginManager().getPlugin("Permissions");
	Plugin gm_plugin = parent.getServer().getPluginManager().getPlugin("GroupManager");

	if( Permissions == null && GroupManager == null ) {

            if( gm_plugin != null ) {
		GroupManager = ((GroupManager) gm_plugin);
		handlerType = HandlerType.GROUPMANAGER;

		MotherNature.log.info("Using GroupManager version " + gm_plugin.getDescription().getVersion() + " for permission handling");
	    }

	    else if( perm_plugin != null ) {
		Permissions = ((Permissions) perm_plugin).getHandler();
		handlerType = HandlerType.PERMISSIONS;

		MotherNature.log.info("Using Permissions version " + perm_plugin.getDescription().getVersion() + " for permission handling");
	    }

            else {
		handlerType = HandlerType.VANILLA;

		MotherNature.log.info("Neither Permissions or GroupManager found. Using ops.txt for permission handling");
	    }
	}

    }

    public static boolean has( Player player, String permission, boolean restricted ) {
	switch( handlerType ) {
	case PERMISSIONS:
	    return Permissions.has(player, permission);
	case GROUPMANAGER:
	    return GroupManager.getWorldsHolder().getWorldPermissions(player).has(player, permission);
	default:
	    if( restricted ) {
		return player.isOp();
	    }
	    return true;

	}

    }

    public static boolean has( Player player, String permission ) {
	return has(player, permission, true);
    }

}
