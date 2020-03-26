package org.jetbrains.rider.test.perfomance.trees;

import org.jetbrains.rider.Player;
import org.jetbrains.rider.test.correctness.PlayerGenerator;
import org.jetbrains.rider.trees.PersistentAVLTreeSet;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * JMH Benchmark for PersistentAVLTreeSet class.
 */
@OutputTimeUnit(value = TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 30, time = 300, timeUnit = TimeUnit.MILLISECONDS) // YOU MAY CHANGE ME
@Measurement(iterations = 30, time = 300, timeUnit = TimeUnit.MILLISECONDS) // YOU MAY CHANGE ME
@Fork(value = 3) // YOU MAY CHANGE ME
@State(Scope.Benchmark)
public class PersistentAVLTreeSetBenchmark {
    private PersistentAVLTreeSet<Player> set;
    private List<String> playerNames;
    private Random random;

    @Setup
    public void trialSetup() {
        set = new PersistentAVLTreeSet<>();
        playerNames = new PlayerGenerator("ab", 1, 3, 1, 1).getAvailableNames(); // CHANGE ME, AS IT'S SUGGESTED IN PlayerStorageBenchmark CLASS
        random = new Random();
        for (String name : playerNames) {
            set.add(new Player(name, 1));
        }
    }

    @Benchmark
    public PersistentAVLTreeSet<Player> add() {
        int index = random.nextInt(playerNames.size());
        return set.add(new Player(playerNames.get(index), 1));
    }

    @Benchmark
    public Player get() {
        int index = random.nextInt(playerNames.size());
        return set.get(new Player(playerNames.get(index), 1));
    }

    @Benchmark
    public Boolean contains() {
        int index = random.nextInt(playerNames.size());
        return set.contains(new Player(playerNames.get(index), 1));
    }

    @Benchmark
    public Integer getPosition() {
        int index = random.nextInt(playerNames.size());
        return set.getPosition(new Player(playerNames.get(index), 1));
    }

    @Benchmark
    public PersistentAVLTreeSet<Player> remove() {
        int index = random.nextInt(playerNames.size());
        return set.remove(new Player(playerNames.get(index), 1));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(PersistentAVLTreeSetBenchmark.class.getSimpleName())
                .threads(1) // CHANGE ME, IF YOU KNOW THE CONSEQUENCES
                .build();
        new Runner(opt).run();
    }
}
