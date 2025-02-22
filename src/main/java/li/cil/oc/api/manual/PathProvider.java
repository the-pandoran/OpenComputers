package li.cil.oc.api.manual;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Allows providing paths for item stacks and blocks in the world.
 * <br>
 * This is used for generating NEI usage pages with a button opening the manual
 * on the page at the specified path, or for opening the manual when held in
 * hand and sneak-activating a block in the world.
 * <br>
 * This way you can easily make entries in your documentation available the
 * same way OpenComputers does it itself.
 * <br>
 * Note that you can use the special variable <tt>%LANGUAGE%</tt> in your
 * paths, for language agnostic paths. These will be resolved to the currently
 * set language, falling back to <tt>en_us</tt>, during actual content lookup.
 */
public interface PathProvider {
    /**
     * Get the path to the documentation page for the provided item stack.
     * <br>
     * Return <tt>null</tt> if there is no known page for this item, allowing
     * other providers to be queried.
     *
     * @param stack the stack to get the documentation path to.
     * @return the path to the page, <tt>null</tt> if none is known.
     */
    String pathFor(ItemStack stack);

    /**
     * Get the path to the documentation page for the provided block.
     * <br>
     * Return <tt>null</tt> if there is no known page for this item, allowing
     * other providers to be queried.
     *
     * @param world the world containing the block.
     * @param pos   the position coordinate of the block.
     * @return the path to the page, <tt>null</tt> if none is known.
     */
    String pathFor(Level world, BlockPos pos);
}
