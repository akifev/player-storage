package org.jetbrains.rider.test.perfomance;

import org.jetbrains.rider.PlayerStorage;
import org.jetbrains.rider.Storage;
import org.jetbrains.rider.test.correctness.PlayerGenerator;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@OutputTimeUnit(value = TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 30, time = 1000, timeUnit = TimeUnit.MILLISECONDS) // YOU MAY CHANGE ME
@Measurement(iterations = 30, time = 1000, timeUnit = TimeUnit.MILLISECONDS) // YOU MAY CHANGE ME
//@OperationsPerInvocation(14) // 14 users
//@OperationsPerInvocation(62) // 62 users
//@OperationsPerInvocation(1022) // 1022 users
@OperationsPerInvocation(8190) // 8190 users
//@OperationsPerInvocation(65534) // 65534 users
//@OperationsPerInvocation(524286) // 524286 users
@Fork(value = 3) // YOU MAY CHANGE ME
@State(Scope.Benchmark)
public class PlayerStorageBenchmark {
    private Storage playerStorage;
    private List<String> playerNames;

    @Setup
    public void trialSetup() {
//        playerNames = new PlayerGenerator("ab", 1, 3, 1, 1).getAvailableNames(); // 14 users
//        playerNames = new PlayerGenerator("ab", 1, 6, 1, 1).getAvailableNames(); // 62 users
//        playerNames = new PlayerGenerator("ab", 1, 9, 1, 1).getAvailableNames(); // 1022 users
        playerNames = new PlayerGenerator("ab", 1, 12, 1, 1).getAvailableNames(); // 8190 users
//        playerNames = new PlayerGenerator("ab", 1, 15, 1, 1).getAvailableNames(); // 65534 users
//        playerNames = new PlayerGenerator("ab", 1, 18, 1, 1).getAvailableNames(); // 524286 users
    }

    @Benchmark
    public Storage registerPlayerResult() {
        playerStorage = new PlayerStorage();

        filling();

        return playerStorage;
    }

    @Benchmark
    public Storage unregisterPlayer() {
        playerStorage = new PlayerStorage();

        filling();

        for (String name : playerNames) {
            playerStorage.unregisterPlayer(name);
        }

        return playerStorage;
    }

    @Benchmark
    public Storage getPlayerRank() {
        playerStorage = new PlayerStorage();

        filling();

        for (String name : playerNames) {
            playerStorage.getPlayerRank(name);
        }

        return playerStorage;
    }

    @Benchmark
    public Storage rollback() {
        playerStorage = new PlayerStorage();

        filling();

        for (String ignored : playerNames) {
            playerStorage.rollback(1);
        }

        return playerStorage;
    }

    private void filling() {
        Random random = ThreadLocalRandom.current();

        for (String name : playerNames) {
            playerStorage.registerPlayerResult(name, random.nextInt(10));
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(PlayerStorageBenchmark.class.getSimpleName())
                .threads(1) // CHANGE ME, IF YOU KNOW THE CONSEQUENCES
                .build();
        new Runner(opt).run();
    }
}