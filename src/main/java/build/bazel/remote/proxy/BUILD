java_binary(
    name = "proxy",
    srcs = ["ProxyServer.java"],
    deps = [
        "@io_grpc_grpc_java//api",
        "@io_grpc_grpc_java//stub",
        "@io_grpc_grpc_java//netty",
        "@io_grpc_grpc_java//protobuf",
        "@com_google_guava_guava//jar",
        "@com_google_protobuf//:protobuf_java",
        "@remoteapis//:remote_execution_java_grpc",
        "@remoteapis//:remote_execution_java_proto",
    ],
    main_class = "build.bazel.remote.proxy.ProxyServer",
)