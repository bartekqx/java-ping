package benchmark;

import org.bartekqx.ping.client.TcpBlockingClient;
import org.bartekxq.ping.server.tcp.TcpBlockingPingServer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PingBenchmark {

    @Benchmark
    @BenchmarkMode(Mode.SampleTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
    public void blockingTcpPingTest(TcpBlockingParams pingParams, Blackhole blackhole) throws IOException {
        pingParams.client.ping();
        blackhole.consume(10);
    }

    @State(Scope.Benchmark)
    public static class TcpBlockingParams {

        final TcpBlockingPingServer server = new TcpBlockingPingServer("localhost", 8090);
        final TcpBlockingClient client = new TcpBlockingClient("localhost", 8090);

        @Setup
        public void setup() throws IOException, InterruptedException {
            Thread t1 = new Thread(() -> {
                try {
                    server.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t1.start();
            Thread.sleep(10);
            client.start();
        }

    }
}
