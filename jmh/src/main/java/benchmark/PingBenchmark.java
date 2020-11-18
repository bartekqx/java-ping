package benchmark;

import org.bartekqx.ping.client.tcp.TcpBlockingClient;
import org.bartekqx.ping.client.tcp.TcpSpinClient;
import org.bartekxq.ping.server.tcp.TcpBlockingPingServer;
import org.bartekxq.ping.server.tcp.TcpSpinPingServer;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PingBenchmark {

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
    public void tcpSpinPingTest(TcpSpinParams pingParams, Blackhole blackhole) throws IOException {
        pingParams.client.ping();
        blackhole.consume(10);
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    @Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
    @Measurement(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
    public void tcpBlockingPingTest(TcpBlockingParams pingParams, Blackhole blackhole) throws IOException {
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
            Thread.sleep(100);
            client.start();
        }

        @TearDown
        public void tearDown() throws IOException {
            client.close();
        }
    }

    @State(Scope.Benchmark)
    public static class TcpSpinParams {

        final TcpSpinPingServer server = new TcpSpinPingServer("localhost", 8091);
        final TcpSpinClient client = new TcpSpinClient(32, "localhost", 8091);

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
            Thread.sleep(100);
            client.start();
        }

        @TearDown
        public void tearDown() throws IOException {
            client.close();
        }
    }
}
