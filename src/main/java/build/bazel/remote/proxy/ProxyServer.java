package build.bazel.remote.proxy;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.AbstractScheduledService;
import io.grpc.Channel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import java.io.IOException;

public class ProxyServer {

  private final ServerBuilder<?> serverBuilder;

  ProxyServer(Options options) {
    NettyChannelBuilder builder = NettyChannelBuilder
        .forAddress(options.getProxyHost(), options.getProxyPort())
        .negotiationType(NegotiationType.PLAINTEXT);
    Channel proxyChannel = builder.build();

    ServerInterceptor headersInterceptor = new TracingMetadataUtils.ServerHeadersInterceptor();

    serverBuilder = ServerBuilder.forPort(options.getPort());
    ImmutableList.of(
        new ProxyCapabilitiesService(proxyChannel),
        new ProxyActionCacheService(proxyChannel),
        new ProxyContentAddressableStorageService(proxyChannel),
        new ProxyByteStreamService(proxyChannel),
        new ProxyExecutionService(proxyChannel)
    ).forEach(bindableService -> serverBuilder
        .addService(ServerInterceptors.intercept(bindableService, headersInterceptor)));
  }

  public void serve() {
    while (true) {
      Server server = serverBuilder.build();
      try {
        server.start();
      } catch (IOException e) {
        e.printStackTrace();
      }

      try {
        // Shutdown the server to send GOAWAY frame
        Thread.sleep(10000);
        server.shutdown();
        server.awaitTermination();
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }
  }

  public static void main(String[] args) {
    Options options = Options.fromArgs(args);
    ProxyServer proxyServer = new ProxyServer(options);
    proxyServer.serve();
  }
}