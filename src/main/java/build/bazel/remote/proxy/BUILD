java_binary(
    name = "proxy",
    srcs = glob(["*.java"]),
    deps = [
        "@io_grpc_grpc_java//api",
        "@io_grpc_grpc_java//stub",
        "@io_grpc_grpc_java//netty",
        "@io_grpc_grpc_java//protobuf",
        "@io_netty_netty_common//jar",
        "@com_google_guava_guava//jar",
        "@com_google_protobuf//:protobuf_java",
        "@remoteapis//:build_bazel_remote_execution_v2_remote_execution_java_proto",
        "@remoteapis//:build_bazel_remote_execution_v2_remote_execution_java_grpc",
        "@remoteapis//:build_bazel_semver_java_proto",
        "@googleapis//:google_bytestream_bytestream_java_proto",
        "@googleapis//:google_bytestream_bytestream_java_grpc",
    ],
    main_class = "build.bazel.remote.proxy.ProxyServer",
)