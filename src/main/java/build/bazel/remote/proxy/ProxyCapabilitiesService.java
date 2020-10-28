package build.bazel.remote.proxy;

import build.bazel.remote.execution.v2.CapabilitiesGrpc;
import build.bazel.remote.execution.v2.GetCapabilitiesRequest;
import build.bazel.remote.execution.v2.ServerCapabilities;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

class ProxyCapabilitiesService extends CapabilitiesGrpc.CapabilitiesImplBase {
  private final Channel proxyChannel;

  public ProxyCapabilitiesService(Channel proxyChannel) {
    this.proxyChannel = proxyChannel;
  }

  private CapabilitiesGrpc.CapabilitiesStub proxyStub() {
    return CapabilitiesGrpc.newStub(proxyChannel);
  }

  @Override
  public void getCapabilities(GetCapabilitiesRequest request,
      StreamObserver<ServerCapabilities> responseObserver) {
    proxyStub().getCapabilities(request, responseObserver);
  }
}
