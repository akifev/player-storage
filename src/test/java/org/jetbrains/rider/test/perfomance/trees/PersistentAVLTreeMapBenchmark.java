package org.jetbrains.rider.test.perfomance.trees;

import org.jetbrains.rider.test.correctness.PlayerGenerator;
import org.jetbrains.rider.trees.PersistentAVLTreeMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(value = TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 30, time = 300, timeUnit = TimeUnit.MILLISECONDS) // YOU MAY CHANGE ME
@Measurement(iterations = 30, time = 300, timeUnit = TimeUnit.MILLISECONDS) // YOU MAY CHANGE ME
@Fork(value = 3) // YOU MAY CHANGE ME
@State(Scope.Benchmark)
public class PersistentAVLTreeMapBenchmark {
    private PersistentAVLTreeMap<String, Integer> map;
    private List<String> playerNames;
    private Random random;

    @Setup
    public void trialSetup() {
        map = new PersistentAVLTreeMap<>();
        playerNames = new PlayerGenerator("ab", 1, 3, 1, 1).getAvailableNames(); // CHANGE ME, AS IT'S SUGGESTED IN PlayerStorageBenchmark CLASS
        random = new Random();
        for (String name : playerNames) {
            map.add(name, 1);
        }
    }

    @Benchmark
    public PersistentAVLTreeMap<String, Integer> add() {
        int index = random.nextInt(playerNames.size());
        return map.add(playerNames.get(index), 1);
    }

    @Benchmark
    public Integer getValue() {
        int index = random.nextInt(playerNames.size());
        return map.getValue(playerNames.get(index));
    }

    @Benchmark
    public PersistentAVLTreeMap<String, Integer> remove() {
        int index = random.nextInt(playerNames.size());
        return map.remove(playerNames.get(index));
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(PersistentAVLTreeMapBenchmark.class.getSimpleName())
                .threads(1) // CHANGE ME, IF YOU KNOW THE CONSEQUENCES
                .build();
        new Runner(opt).run();
    }
}
