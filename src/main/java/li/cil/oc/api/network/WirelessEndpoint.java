package li.cil.oc.api.network;

import net.minecraft.world.level.Level;

/**
 * Interface for wireless endpoints that can be registered with the internal
 * wireless network registry.
 * <br>
 * These can be added to the wireless network via the <tt>Network</tt> API, to
 * allow them to receive packets like wireless network cards and access points
 * do (and handle or forward them as they see fit).
 * <br>
 * If the position of the endpoint can change, it must be updated manually via
 * {@link li.cil.oc.api.Network#updateWirelessNetwork(WirelessEndpoint)}.
 */
public interface WirelessEndpoint {
    /**
     * The X coordinate of the endpoint in the world, in block coordinates.
     */
    int x();

    /**
     * The Y coordinate of the endpoint in the world, in block coordinates.
     */
    int y();

    /**
     * The Z coordinate of the endpoint in the world, in block coordinates.
     */
    int z();

    /**
     * The world this endpoint lives in.
     */
    Level world();

    /**
     * Makes the endpoint receive a single packet.
     *
     * @param packet the packet to receive.
     * @param sender the endpoint that sent the message. This is not
     *               necessarily the original sender of the packet, just
     *               the last point it went through, such as an access
     *               point, for example.
     */
    void receivePacket(Packet packet, WirelessEndpoint sender);
}
