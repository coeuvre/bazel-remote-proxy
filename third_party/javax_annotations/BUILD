package(default_visibility = ["//visibility:public"])

# javax.annotation.Generated is not included in the default root modules in 9,
# see: http://openjdk.java.net/jeps/320.
java_import(
    name = "javax_annotations",
    jars = ["javax.annotation-api-1.3.2.jar"],
    neverlink = 1,  # @Generated is source-retention
    srcjar = "javax.annotation-api-1.3.2-sources.jar",
)
