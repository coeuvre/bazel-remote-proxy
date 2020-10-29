package build.bazel.remote.proxy;

import com.google.common.collect.ImmutableList;
import io.grpc.Channel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import java.io.IOException;

public class ProxyServer {

  private final Server server;

  ProxyServer(Options options) {
    NettyChannelBuilder builder = NettyChannelBuilder
        .forAddress(options.getProxyHost(), options.getProxyPort())
        .negotiationType(NegotiationType.PLAINTEXT);
    Channel proxyChannel = builder.build();

    ServerInterceptor headersInterceptor = new TracingMetadataUtils.ServerHeadersInterceptor();

    ServerBuilder<?> serverBuilder = ServerBuilder.forPort(options.getPort());
    ImmutableList.of(
        new ProxyCapabilitiesService(proxyChannel),
        new ProxyActionCacheService(proxyChannel),
        new ProxyContentAddressableStorageService(proxyChannel),
        new ProxyByteStreamService(proxyChannel),
        new ProxyExecutionService(proxyChannel)
    ).forEach(bindableService -> serverBuilder
        .addService(ServerInterceptors.intercept(bindableService, headersInterceptor)));

    this.server = serverBuilder.build();
  }

  public void serve() {
    try {
      server.start();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      server.awaitTermination();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    Options options = Options.fromArgs(args);
    ProxyServer proxyServer = new ProxyServer(options);
    proxyServer.serve();
  }
}