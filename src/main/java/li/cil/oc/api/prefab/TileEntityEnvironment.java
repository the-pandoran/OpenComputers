package li.cil.oc.api.prefab;

import li.cil.oc.api.Network;
import li.cil.oc.api.network.Environment;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.network.Visibility;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

/**
 * TileEntities can implement the {@link li.cil.oc.api.network.Environment}
 * interface to allow them to interact with the component network, by providing
 * a {@link li.cil.oc.api.network.Node} and connecting it to said network.
 * <br>
 * Nodes in such a network can communicate with each other, or just use the
 * network as an index structure to find other nodes connected to them.
 */
@SuppressWarnings("UnusedDeclaration")
public abstract class TileEntityEnvironment extends BlockEntity implements Environment {
    private static final String TAG_NODE = "oc:node";

    /**
     * This must be set in subclasses to the node that is used to represent
     * this tile entity.
     * <br>
     * You must only create new nodes using the factory method in the network
     * API, {@link li.cil.oc.api.Network#newNode(Environment, Visibility)}.
     * <br>
     * For example:
     * <pre>
     * // The first parameters to newNode is the host() of the node, which will
     * // usually be this tile entity. The second one is it's reachability,
     * // which determines how other nodes in the same network can query this
     * // node. See {@link li.cil.oc.api.network.Network#nodes(li.cil.oc.api.network.Node)}.
     * node = Network.newNode(this, Visibility.Network)
     *       // This call allows the node to consume energy from the
     *       // component network it is in and act as a consumer, or to
     *       // inject energy into that network and act as a producer.
     *       // If you do not need energy remove this call.
     *       .withConnector()
     *       // This call marks the tile entity as a component. This means you
     *       // can mark methods in it using the {@link li.cil.oc.api.machine.Callback}
     *       // annotation, making them callable from user code. The first
     *       // parameter is the name by which the component will be known in
     *       // the computer, in this case it could be accessed as
     *       // <tt>component.example</tt>. The second parameter is the
     *       // component's visibility. This is like the node's reachability,
     *       // but only applies to computers. For example, network cards can
     *       // only be <em>seen</em> by the computer they're installed in, but
     *       // can be <em>reached</em> by all other network cards in the same
     *       // network. If you do not need callbacks remove this call.
     *       .withComponent("example", Visibility.Neighbors)
     *       // Finalizes the construction of the node and returns it.
     *       .create();
     * </pre>
     */
    protected Node node;

    // ----------------------------------------------------------------------- //

    public TileEntityEnvironment(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Node node() {
        return node;
    }

    @Override
    public void onConnect(final Node node) {
        // This is called when the call to Network.joinOrCreateNetwork(this) in
        // tick was successful, in which case `node == this`.
        // This is also called for any other node that gets connected to the
        // network our node is in, in which case `node` is the added node.
        // If our node is added to an existing network, this is called for each
        // node already in said network.
    }

    @Override
    public void onDisconnect(final Node node) {
        // This is called when this node is removed from its network when the
        // tile entity is removed from the world (see onChunkUnloaded() and
        // setRemoved()), in which case `node == this`.
        // This is also called for each other node that gets removed from the
        // network our node is in, in which case `node` is the removed node.
        // If a net-split occurs this is called for each node that is no longer
        // connected to our node.
    }

    @Override
    public void onMessage(final Message message) {
        // This is used to deliver messages sent via node.sendToXYZ. Handle
        // messages at your own discretion. If you do not wish to handle a
        // message you should *not* throw an exception, though.
    }

    // ----------------------------------------------------------------------- //

    @Override
    public void onLoad() {
        Network.joinOrCreateNetwork(this);
    }

    @Override
    public void onChunkUnloaded() {
        super.onChunkUnloaded();
        // Make sure to remove the node from its network when its environment,
        // meaning this tile entity, gets unloaded.
        if (node != null) node.remove();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        // Make sure to remove the node from its network when its environment,
        // meaning this tile entity, gets unloaded.
        if (node != null) node.remove();
    }

    // ----------------------------------------------------------------------- //

    @Override
    public void load(final CompoundTag nbt) {
        super.load(nbt);
        // The host check may be superfluous for you. It's just there to allow
        // some special cases, where getNode() returns some node managed by
        // some other instance (for example when you have multiple internal
        // nodes in this tile entity).
        if (node != null && node.host() == this) {
            // This restores the node's address, which is required for networks
            // to continue working without interruption across loads. If the
            // node is a power connector this is also required to restore the
            // internal energy buffer of the node.
            node.loadData(nbt.getCompound(TAG_NODE));
        }
    }

    @Override
    public void saveAdditional(final CompoundTag nbt) {
        super.saveAdditional(nbt);
        // See load() regarding host check.
        if (node != null && node.host() == this) {
            final CompoundTag nodeNbt = new CompoundTag();
            node.saveData(nodeNbt);
            nbt.put(TAG_NODE, nodeNbt);
        }
    }
}
