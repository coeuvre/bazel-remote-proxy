package build.bazel.remote.proxy;

import build.bazel.remote.execution.v2.ActionCacheGrpc;
import build.bazel.remote.execution.v2.ActionResult;
import build.bazel.remote.execution.v2.GetActionResultRequest;
import build.bazel.remote.execution.v2.UpdateActionResultRequest;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

public class ProxyActionCacheService extends ActionCacheGrpc.ActionCacheImplBase {

  private final Channel proxyChannel;

  public ProxyActionCacheService(Channel proxyChannel) {
    this.proxyChannel = proxyChannel;
  }

  private ActionCacheGrpc.ActionCacheStub proxyStub() {
    return ActionCacheGrpc.newStub(proxyChannel)
        .withInterceptors(TracingMetadataUtils.attachMetadataFromContextInterceptor());
  }

  @Override
  public void getActionResult(GetActionResultRequest request,
      StreamObserver<ActionResult> responseObserver) {
    proxyStub().getActionResult(request, responseObserver);
  }

  @Override
  public void updateActionResult(UpdateActionResultRequest request,
      StreamObserver<ActionResult> responseObserver) {
    proxyStub().updateActionResult(request, responseObserver);
  }
}
