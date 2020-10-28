package build.bazel.remote.proxy;

import io.grpc.Channel;
import io.grpc.Server;
import io.grpc.ServerBuilder;
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
    this.server = ServerBuilder.forPort(options.getPort())
        .addService(new ProxyCapabilitiesService(proxyChannel))
        .addService(new ProxyActionCacheService(proxyChannel))
        .addService(new ProxyContentAddressableStorageService(proxyChannel))
        .addService(new ProxyByteStreamService(proxyChannel))
        .build();
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