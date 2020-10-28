package build.bazel.remote.proxy;

import com.google.common.base.Preconditions;

public class Options {
  private final int port;

  private final String proxyHost;

  private final int proxyPort;

  public static Options fromArgs(String[] args) {
    Builder builder = new Builder();
    for (int i = 0; i < args.length; ++i) {
      switch (args[i]) {
        case "--port":
          Preconditions.checkElementIndex(i + 1, args.length, "port");
          String portStr = args[++i];
          builder.setPort(portStr);
          break;
        case "--proxy_host":
          Preconditions.checkElementIndex(i + 1, args.length, "proxy_host");
          String proxyHost = args[++i];
          builder.setProxyHost(proxyHost);
          break;
        case "--proxy_port":
          Preconditions.checkElementIndex(i + 1, args.length, "proxy_port");
          String proxyPort = args[++i];
          builder.setProxyPort(proxyPort);
          break;
        default:
          break;
      }
    }
    return builder.build();
  }

  static class Builder {
    private Integer port;

    private String proxyHost;

    private Integer proxyPort;

    public Builder setPort(String portStr) {
      this.port = Integer.parseInt(portStr);
      return this;
    }

    public Builder setProxyHost(String proxyHost) {
      this.proxyHost = proxyHost;
      return this;
    }

    public Builder setProxyPort(String proxyPortStr) {
      this.proxyPort = Integer.parseInt(proxyPortStr);
      return this;
    }

    public Options build() {
      Preconditions.checkNotNull(port, "port");
      Preconditions.checkNotNull(proxyHost, "proxyHost");
      Preconditions.checkNotNull(proxyPort, "proxyPort");
      return new Options(port, proxyHost, proxyPort);
    }
  }

  public Options(int port, String proxyHost, int proxyPort) {
    this.port = port;
    this.proxyHost = proxyHost;
    this.proxyPort = proxyPort;
  }

  public int getPort() {
    return port;
  }

  public String getProxyHost() {
    return proxyHost;
  }

  public int getProxyPort() {
    return proxyPort;
  }
}
