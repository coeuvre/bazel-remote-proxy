package build.bazel.remote.proxy;

import com.google.bytestream.ByteStreamGrpc;
import com.google.bytestream.ByteStreamProto.QueryWriteStatusRequest;
import com.google.bytestream.ByteStreamProto.QueryWriteStatusResponse;
import com.google.bytestream.ByteStreamProto.ReadRequest;
import com.google.bytestream.ByteStreamProto.ReadResponse;
import com.google.bytestream.ByteStreamProto.WriteRequest;
import com.google.bytestream.ByteStreamProto.WriteResponse;
import io.grpc.Channel;
import io.grpc.stub.StreamObserver;

public class ProxyByteStreamService extends ByteStreamGrpc.ByteStreamImplBase {

  private final Channel proxyChannel;

  public ProxyByteStreamService(Channel proxyChannel) {
    this.proxyChannel = proxyChannel;
  }

  private ByteStreamGrpc.ByteStreamStub proxyStub() {
    return ByteStreamGrpc.newStub(proxyChannel)
        .withInterceptors(TracingMetadataUtils.attachMetadataFromContextInterceptor());
  }

  @Override
  public void read(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
    proxyStub().read(request, responseObserver);
  }

  @Override
  public StreamObserver<WriteRequest> write(StreamObserver<WriteResponse> responseObserver) {
    return proxyStub().write(responseObserver);
  }

  @Override
  public void queryWriteStatus(QueryWriteStatusRequest request,
      StreamObserver<QueryWriteStatusResponse> responseObserver) {
    proxyStub().queryWriteStatus(request, responseObserver);
  }
}
