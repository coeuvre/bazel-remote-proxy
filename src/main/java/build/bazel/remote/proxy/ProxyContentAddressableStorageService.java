package build.bazel.remote.proxy;

import build.bazel.remote.execution.v2.BatchReadBlobsRequest;
import build.bazel.remote.execution.v2.BatchReadBlobsResponse;
import build.bazel.remote.execution.v2.BatchUpdateBlobsRequest;
import build.bazel.remote.execution.v2.BatchUpdateBlobsResponse;
import build.bazel.remote.execution.v2.ContentAddressableStorageGrpc;
import build.bazel.remote.execution.v2.FindMissingBlobsRequest;
import build.bazel.remote.execution.v2.FindMissingBlobsResponse;
import build.bazel.remote.execution.v2.GetTreeRequest;
import build.bazel.remote.execution.v2.GetTreeResponse;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

public class ProxyContentAddressableStorageService extends
    ContentAddressableStorageGrpc.ContentAddressableStorageImplBase {

  private final Channel proxyChannel;

  public ProxyContentAddressableStorageService(Channel proxyChannel) {
    this.proxyChannel = proxyChannel;
  }

  private ContentAddressableStorageGrpc.ContentAddressableStorageStub proxyStub() {
    return ContentAddressableStorageGrpc.newStub(proxyChannel)
        .withInterceptors(TracingMetadataUtils.attachMetadataFromContextInterceptor());
  }

  @Override
  public void findMissingBlobs(FindMissingBlobsRequest request,
      StreamObserver<FindMissingBlobsResponse> responseObserver) {
    proxyStub().findMissingBlobs(request, responseObserver);
  }

  @Override
  public void batchUpdateBlobs(BatchUpdateBlobsRequest request,
      StreamObserver<BatchUpdateBlobsResponse> responseObserver) {
    proxyStub().batchUpdateBlobs(request, responseObserver);
  }

  @Override
  public void batchReadBlobs(BatchReadBlobsRequest request,
      StreamObserver<BatchReadBlobsResponse> responseObserver) {
    proxyStub().batchReadBlobs(request, responseObserver);
  }

  @Override
  public void getTree(GetTreeRequest request, StreamObserver<GetTreeResponse> responseObserver) {
    proxyStub().getTree(request, responseObserver);
  }
}
