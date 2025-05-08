package stat;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.71.0)",
    comments = "Source: stat.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class StatServiceGrpc {

  private StatServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "stat.StatService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<stat.Stat.AverageRequest,
      stat.Stat.AverageResponse> getAverageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Average",
      requestType = stat.Stat.AverageRequest.class,
      responseType = stat.Stat.AverageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<stat.Stat.AverageRequest,
      stat.Stat.AverageResponse> getAverageMethod() {
    io.grpc.MethodDescriptor<stat.Stat.AverageRequest, stat.Stat.AverageResponse> getAverageMethod;
    if ((getAverageMethod = StatServiceGrpc.getAverageMethod) == null) {
      synchronized (StatServiceGrpc.class) {
        if ((getAverageMethod = StatServiceGrpc.getAverageMethod) == null) {
          StatServiceGrpc.getAverageMethod = getAverageMethod =
              io.grpc.MethodDescriptor.<stat.Stat.AverageRequest, stat.Stat.AverageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Average"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stat.Stat.AverageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stat.Stat.AverageResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StatServiceMethodDescriptorSupplier("Average"))
              .build();
        }
      }
    }
    return getAverageMethod;
  }

  private static volatile io.grpc.MethodDescriptor<stat.Stat.MedianRequest,
      stat.Stat.MedianResponse> getMedianMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Median",
      requestType = stat.Stat.MedianRequest.class,
      responseType = stat.Stat.MedianResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<stat.Stat.MedianRequest,
      stat.Stat.MedianResponse> getMedianMethod() {
    io.grpc.MethodDescriptor<stat.Stat.MedianRequest, stat.Stat.MedianResponse> getMedianMethod;
    if ((getMedianMethod = StatServiceGrpc.getMedianMethod) == null) {
      synchronized (StatServiceGrpc.class) {
        if ((getMedianMethod = StatServiceGrpc.getMedianMethod) == null) {
          StatServiceGrpc.getMedianMethod = getMedianMethod =
              io.grpc.MethodDescriptor.<stat.Stat.MedianRequest, stat.Stat.MedianResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Median"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stat.Stat.MedianRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  stat.Stat.MedianResponse.getDefaultInstance()))
              .setSchemaDescriptor(new StatServiceMethodDescriptorSupplier("Median"))
              .build();
        }
      }
    }
    return getMedianMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StatServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StatServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StatServiceStub>() {
        @java.lang.Override
        public StatServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StatServiceStub(channel, callOptions);
        }
      };
    return StatServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static StatServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StatServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StatServiceBlockingV2Stub>() {
        @java.lang.Override
        public StatServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StatServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return StatServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StatServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StatServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StatServiceBlockingStub>() {
        @java.lang.Override
        public StatServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StatServiceBlockingStub(channel, callOptions);
        }
      };
    return StatServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StatServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StatServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StatServiceFutureStub>() {
        @java.lang.Override
        public StatServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StatServiceFutureStub(channel, callOptions);
        }
      };
    return StatServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void average(stat.Stat.AverageRequest request,
        io.grpc.stub.StreamObserver<stat.Stat.AverageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAverageMethod(), responseObserver);
    }

    /**
     */
    default void median(stat.Stat.MedianRequest request,
        io.grpc.stub.StreamObserver<stat.Stat.MedianResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getMedianMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service StatService.
   */
  public static abstract class StatServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return StatServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service StatService.
   */
  public static final class StatServiceStub
      extends io.grpc.stub.AbstractAsyncStub<StatServiceStub> {
    private StatServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StatServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StatServiceStub(channel, callOptions);
    }

    /**
     */
    public void average(stat.Stat.AverageRequest request,
        io.grpc.stub.StreamObserver<stat.Stat.AverageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAverageMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void median(stat.Stat.MedianRequest request,
        io.grpc.stub.StreamObserver<stat.Stat.MedianResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getMedianMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service StatService.
   */
  public static final class StatServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<StatServiceBlockingV2Stub> {
    private StatServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StatServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StatServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public stat.Stat.AverageResponse average(stat.Stat.AverageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAverageMethod(), getCallOptions(), request);
    }

    /**
     */
    public stat.Stat.MedianResponse median(stat.Stat.MedianRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMedianMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service StatService.
   */
  public static final class StatServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<StatServiceBlockingStub> {
    private StatServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StatServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StatServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public stat.Stat.AverageResponse average(stat.Stat.AverageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAverageMethod(), getCallOptions(), request);
    }

    /**
     */
    public stat.Stat.MedianResponse median(stat.Stat.MedianRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getMedianMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service StatService.
   */
  public static final class StatServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<StatServiceFutureStub> {
    private StatServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StatServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StatServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<stat.Stat.AverageResponse> average(
        stat.Stat.AverageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAverageMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<stat.Stat.MedianResponse> median(
        stat.Stat.MedianRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getMedianMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_AVERAGE = 0;
  private static final int METHODID_MEDIAN = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_AVERAGE:
          serviceImpl.average((stat.Stat.AverageRequest) request,
              (io.grpc.stub.StreamObserver<stat.Stat.AverageResponse>) responseObserver);
          break;
        case METHODID_MEDIAN:
          serviceImpl.median((stat.Stat.MedianRequest) request,
              (io.grpc.stub.StreamObserver<stat.Stat.MedianResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getAverageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              stat.Stat.AverageRequest,
              stat.Stat.AverageResponse>(
                service, METHODID_AVERAGE)))
        .addMethod(
          getMedianMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              stat.Stat.MedianRequest,
              stat.Stat.MedianResponse>(
                service, METHODID_MEDIAN)))
        .build();
  }

  private static abstract class StatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StatServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return stat.Stat.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("StatService");
    }
  }

  private static final class StatServiceFileDescriptorSupplier
      extends StatServiceBaseDescriptorSupplier {
    StatServiceFileDescriptorSupplier() {}
  }

  private static final class StatServiceMethodDescriptorSupplier
      extends StatServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    StatServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (StatServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StatServiceFileDescriptorSupplier())
              .addMethod(getAverageMethod())
              .addMethod(getMedianMethod())
              .build();
        }
      }
    }
    return result;
  }
}
