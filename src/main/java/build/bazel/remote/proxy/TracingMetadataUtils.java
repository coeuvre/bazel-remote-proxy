package build.bazel.remote.proxy;

import build.bazel.remote.execution.v2.RequestMetadata;
import io.grpc.ClientInterceptor;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.stub.MetadataUtils;
import javax.annotation.Nullable;

public final class TracingMetadataUtils {

  private static final Context.Key<RequestMetadata> CONTEXT_KEY =
      Context.key("remote-grpc-metadata");

  public static final Metadata.Key<RequestMetadata> METADATA_KEY =
      ProtoUtils.keyForProto(RequestMetadata.getDefaultInstance());

  /**
   * Fetches a {@link RequestMetadata} defined on the current context.
   *
   * @throws IllegalStateException when the metadata is not defined in the current context.
   */
  public static RequestMetadata fromCurrentContext() {
    RequestMetadata metadata = CONTEXT_KEY.get();
    if (metadata == null) {
      throw new IllegalStateException("RequestMetadata not set in current context.");
    }
    return metadata;
  }

  /**
   * Creates a {@link Metadata} containing the {@link RequestMetadata} defined on the current
   * context.
   *
   * @throws IllegalStateException when the metadata is not defined in the current context.
   */
  public static Metadata headersFromCurrentContext() {
    Metadata headers = new Metadata();
    headers.put(METADATA_KEY, fromCurrentContext());
    return headers;
  }

  /**
   * Extracts a {@link RequestMetadata} from a {@link Metadata} and returns it if it exists. If it
   * does not exist, returns {@code null}.
   */
  public static @Nullable
  RequestMetadata requestMetadataFromHeaders(Metadata headers) {
    return headers.get(METADATA_KEY);
  }

  public static ClientInterceptor attachMetadataFromContextInterceptor() {
    return MetadataUtils.newAttachHeadersInterceptor(headersFromCurrentContext());
  }

  /**
   * GRPC interceptor to add logging metadata to the GRPC context.
   */
  public static class ServerHeadersInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> Listener<ReqT> interceptCall(
        ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
      RequestMetadata meta = requestMetadataFromHeaders(headers);
      if (meta == null) {
        throw io.grpc.Status.INVALID_ARGUMENT
            .withDescription(
                "RequestMetadata not received from the client for "
                    + call.getMethodDescriptor().getFullMethodName())
            .asRuntimeException();
      }
      Context ctx = Context.current().withValue(CONTEXT_KEY, meta);
      return Contexts.interceptCall(ctx, call, headers, next);
    }
  }
}
