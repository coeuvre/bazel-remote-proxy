package build.bazel.remote.proxy;

import build.bazel.remote.execution.v2.ExecuteRequest;
import build.bazel.remote.execution.v2.ExecutionGrpc;
import build.bazel.remote.execution.v2.WaitExecutionRequest;
import com.google.longrunning.Operation;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

public class ProxyExecutionService extends ExecutionGrpc.ExecutionImplBase {

  private final Channel proxyChannel;

  public ProxyExecutionService(Channel proxyChannel) {
    this.proxyChannel = proxyChannel;
  }

  private ExecutionGrpc.ExecutionStub proxyStub() {
    return ExecutionGrpc.newStub(proxyChannel)
        .withInterceptors(TracingMetadataUtils.attachMetadataFromContextInterceptor());
  }

  @Override
  public void execute(ExecuteRequest request, StreamObserver<Operation> responseObserver) {
    proxyStub().execute(request, responseObserver);
  }

  @Override
  public void waitExecution(WaitExecutionRequest request,
      StreamObserver<Operation> responseObserver) {
    proxyStub().waitExecution(request, responseObserver);
  }
}
