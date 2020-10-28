package build.bazel.remote.proxy;

import build.bazel.remote.execution.v2.CapabilitiesGrpc.CapabilitiesImplBase;
import build.bazel.remote.execution.v2.GetCapabilitiesRequest;
import build.bazel.remote.execution.v2.ServerCapabilities;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;

public class ProxyServer {

  private final Server server;

  static class CapabilitiesImpl extends CapabilitiesImplBase {

    @Override
    public void getCapabilities(GetCapabilitiesRequest request,
        StreamObserver<ServerCapabilities> responseObserver) {
      super.getCapabilities(request, responseObserver);
    }
  }

  ProxyServer(ServerBuilder<?> serverBuilder) {
    this.server = serverBuilder.addService(new CapabilitiesImpl()).build();
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
    ProxyServer proxyServer = new ProxyServer(ServerBuilder.forPort(9092));
    proxyServer.serve();
  }
}