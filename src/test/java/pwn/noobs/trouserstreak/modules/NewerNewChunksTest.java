package pwn.noobs.trouserstreak.modules;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NewerNewChunksTest {

    private static Object invokePrivate(Object target, String name, Class<?>[] types, Object... args) throws Exception {
        Method m = target.getClass().getDeclaredMethod(name, types);
        m.setAccessible(true);
        return m.invoke(target, args);
    }

    private static void setPrivate(Object target, String name, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    @Test
    public void testIsWithinChunk() throws Exception {
        NewerNewChunks mod = new NewerNewChunks();
        ChunkPos cp = new ChunkPos(10, 20);
        BlockPos inside = new BlockPos((10 << 4) + 8, 64, (20 << 4) + 8);
        BlockPos outside = new BlockPos((11 << 4), 64, (21 << 4));

        boolean in = (boolean) invokePrivate(mod, "isWithinChunk", new Class[]{ChunkPos.class, BlockPos.class}, cp, inside);
        boolean out = (boolean) invokePrivate(mod, "isWithinChunk", new Class[]{ChunkPos.class, BlockPos.class}, cp, outside);

        assertTrue(in, "Position should be within chunk");
        assertFalse(out, "Position should be outside chunk");
    }

    @Test
    public void testIsForwardOrLateral() throws Exception {
        NewerNewChunks mod = new NewerNewChunks();
        ChunkPos start = new ChunkPos(0, 0);
        ChunkPos forward = new ChunkPos(0, 3); // NORTH has -Z; use SOUTH for +Z projection
        ChunkPos backward = new ChunkPos(0, -1);
        ChunkPos lateral = new ChunkPos(2, 0);

        boolean fwd = (boolean) invokePrivate(mod, "isForwardOrLateral",
                new Class[]{ChunkPos.class, ChunkPos.class, Direction.class},
                start, forward, Direction.SOUTH);
        boolean back = (boolean) invokePrivate(mod, "isForwardOrLateral",
                new Class[]{ChunkPos.class, ChunkPos.class, Direction.class},
                start, backward, Direction.SOUTH);
        boolean lat = (boolean) invokePrivate(mod, "isForwardOrLateral",
                new Class[]{ChunkPos.class, ChunkPos.class, Direction.class},
                start, lateral, Direction.SOUTH);

        assertTrue(fwd, "Forward candidate must be allowed");
        assertFalse(back, "Backward candidate must be disallowed");
        assertTrue(lat, "Lateral candidate must be allowed");
    }

    @Test
    public void testPickNextTargetPrefersForward() throws Exception {
        NewerNewChunks mod = new NewerNewChunks();
        ChunkPos start = new ChunkPos(0, 0);
        Set<ChunkPos> pool = new HashSet<>();
        ChunkPos forward1 = new ChunkPos(0, 1);
        ChunkPos forward2 = new ChunkPos(0, 2);
        ChunkPos backward = new ChunkPos(0, -1);
        ChunkPos lateral = new ChunkPos(1, 0);
        pool.add(forward1);
        pool.add(forward2);
        pool.add(backward);
        pool.add(lateral);

        Direction[] order = new Direction[]{Direction.SOUTH, Direction.EAST, Direction.NORTH, Direction.WEST};
        ChunkPos chosen = (ChunkPos) invokePrivate(mod, "pickNextTarget",
                new Class[]{ChunkPos.class, Set.class, int.class, int.class, int.class, Direction[].class, Direction.class},
                start, pool, 2, 8, 0, order, Direction.SOUTH);

        assertEquals(forward1, chosen, "Should pick forward chain candidate");
    }

    @Test
    public void testBacktrackCooldownAvoidsLastTarget() throws Exception {
        NewerNewChunks mod = new NewerNewChunks();
        ChunkPos start = new ChunkPos(0, 0);
        Set<ChunkPos> pool = new HashSet<>();
        ChunkPos last = new ChunkPos(1, 0);
        pool.add(last);

        // Set lastCompletedTarget to simulate recent completion
        setPrivate(mod, "lastCompletedTarget", last);
        setPrivate(mod, "lastCompletedAt", System.currentTimeMillis());

        Direction[] order = new Direction[]{Direction.EAST, Direction.SOUTH, Direction.NORTH, Direction.WEST};
        ChunkPos chosen = (ChunkPos) invokePrivate(mod, "pickNextTarget",
                new Class[]{ChunkPos.class, Set.class, int.class, int.class, int.class, Direction[].class, Direction.class},
                start, pool, 1, 4, 0, order, Direction.EAST);

        assertNull(chosen, "Should not pick lastCompletedTarget during cooldown");
    }
}

