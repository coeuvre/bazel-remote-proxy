workspace(name = "bazel_remote_proxy")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "com_google_protobuf",
    sha256 = "b37e96e81842af659605908a421960a5dc809acbc888f6b947bc320f8628e5b1",
    strip_prefix = "protobuf-3.12.0",
    urls = ["https://github.com/protocolbuffers/protobuf/archive/v3.12.0.zip"],
)
load("@com_google_protobuf//:protobuf_deps.bzl", "protobuf_deps")
protobuf_deps()

http_archive(
    name = "io_grpc_grpc_java",
    sha256 = "44ef8771324a796e3bc25a6a23d50c4f4978625ccc3ccde7e0d0d56e974a6d0a",
    strip_prefix = "grpc-java-1.32.2",
    url = "https://github.com/grpc/grpc-java/archive/v1.32.2.zip",
)
load("@io_grpc_grpc_java//:repositories.bzl", "grpc_java_repositories")
grpc_java_repositories()

# required by remoteapis
http_archive(
    name = "googleapis",
    sha256 = "7b6ea252f0b8fb5cd722f45feb83e115b689909bbb6a393a873b6cbad4ceae1d",
    strip_prefix = "googleapis-143084a2624b6591ee1f9d23e7f5241856642f4d",
    urls = ["https://github.com/googleapis/googleapis/archive/143084a2624b6591ee1f9d23e7f5241856642f4d.zip"],
    build_file = "@//third_party/googleapis:BUILD.googleapis",
)

http_archive(
    name = "remoteapis",
    sha256 = "21ad15be502ef529ca07fdda56d25d6678647b954d41f08a040241ea5e43dce1",
    strip_prefix = "remote-apis-b5123b1bb2853393c7b9aa43236db924d7e32d61",
    url = "https://github.com/bazelbuild/remote-apis/archive/b5123b1bb2853393c7b9aa43236db924d7e32d61.zip",
    patch_args = ["-p1"],
    patches = ["@//third_party/remoteapis:repository_rules.patch"],
    build_file = "@//third_party/remoteapis:BUILD.remoteapis"
)
load("@remoteapis//:repository_rules.bzl", "switched_rules_by_language")
switched_rules_by_language(
    name = "bazel_remote_apis_imports",
    java = True,
)

