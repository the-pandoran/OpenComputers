package li.cil.oc.api.internal;

import li.cil.oc.api.machine.MachineHost;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;

import java.util.UUID;

/**
 * General marker interface for autonomous agents such as robots and drones.
 */
public interface Agent extends MachineHost, Rotatable {
    /**
     * The equipment inventory of this agent.
     * <br>
     * For example, for the robot this is the tool slot as well as slots
     * provided by containers installed in the robot, if any.
     * <br>
     * If an agent has no equipment slots this will be a zero-sized inventory.
     */
    Container equipmentInventory();

    /**
     * The main inventory of this agent, which it (usually) also can
     * interact with on its own.
     * <br>
     * If an agent has no inventory slots this will be a zero-sized inventory.
     */
    Container mainInventory();

    /**
     * Provides access to the tanks of the agent.
     * <br>
     * If an agent has no tanks this will be a zero-sized multi-tank.
     */
    MultiTank tank();

    /**
     * Gets the index of the currently selected slot in the agent's inventory.
     */
    int selectedSlot();

    /**
     * Set the index of the currently selected slot.
     */
    void setSelectedSlot(int index);

    /**
     * Get the index of the currently selected tank.
     */
    int selectedTank();

    /**
     * Set the index of the currently selected tank.
     */
    void setSelectedTank(int index);

    /**
     * Returns the fake player used to represent the agent as an entity for
     * certain actions that require one.
     * <br>
     * This will automatically be positioned and rotated to represent the
     * agent's current position and rotation in the world. Use this to trigger
     * events involving the agent that require a player entity.
     * <br>
     * Note that this <em>may</em> be the common OpenComputers fake player.
     *
     * @return the fake player for the agent.
     */
    Player player();

    /**
     * Get the name of this agent.
     */
    String name();

    /**
     * Set the name of the agent.
     */
    void setName(String name);

    /**
     * The name of the player owning this agent, e.g. the player that placed it.
     */
    String ownerName();

    /**
     * The UUID of the player owning this agent, e.g. the player that placed it.
     */
    UUID ownerUUID();
}
