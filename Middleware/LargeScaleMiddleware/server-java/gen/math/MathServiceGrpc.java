package math;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.71.0)",
    comments = "Source: math.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MathServiceGrpc {

  private MathServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "math.MathService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<math.Math.NumberRequest,
      math.Math.BoolResponse> getIsPerfectNumberMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "IsPerfectNumber",
      requestType = math.Math.NumberRequest.class,
      responseType = math.Math.BoolResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<math.Math.NumberRequest,
      math.Math.BoolResponse> getIsPerfectNumberMethod() {
    io.grpc.MethodDescriptor<math.Math.NumberRequest, math.Math.BoolResponse> getIsPerfectNumberMethod;
    if ((getIsPerfectNumberMethod = MathServiceGrpc.getIsPerfectNumberMethod) == null) {
      synchronized (MathServiceGrpc.class) {
        if ((getIsPerfectNumberMethod = MathServiceGrpc.getIsPerfectNumberMethod) == null) {
          MathServiceGrpc.getIsPerfectNumberMethod = getIsPerfectNumberMethod =
              io.grpc.MethodDescriptor.<math.Math.NumberRequest, math.Math.BoolResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "IsPerfectNumber"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  math.Math.NumberRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  math.Math.BoolResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MathServiceMethodDescriptorSupplier("IsPerfectNumber"))
              .build();
        }
      }
    }
    return getIsPerfectNumberMethod;
  }

  private static volatile io.grpc.MethodDescriptor<math.Math.GcdRequest,
      math.Math.GcdResponse> getGcdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Gcd",
      requestType = math.Math.GcdRequest.class,
      responseType = math.Math.GcdResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<math.Math.GcdRequest,
      math.Math.GcdResponse> getGcdMethod() {
    io.grpc.MethodDescriptor<math.Math.GcdRequest, math.Math.GcdResponse> getGcdMethod;
    if ((getGcdMethod = MathServiceGrpc.getGcdMethod) == null) {
      synchronized (MathServiceGrpc.class) {
        if ((getGcdMethod = MathServiceGrpc.getGcdMethod) == null) {
          MathServiceGrpc.getGcdMethod = getGcdMethod =
              io.grpc.MethodDescriptor.<math.Math.GcdRequest, math.Math.GcdResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Gcd"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  math.Math.GcdRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  math.Math.GcdResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MathServiceMethodDescriptorSupplier("Gcd"))
              .build();
        }
      }
    }
    return getGcdMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MathServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MathServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MathServiceStub>() {
        @java.lang.Override
        public MathServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MathServiceStub(channel, callOptions);
        }
      };
    return MathServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static MathServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MathServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MathServiceBlockingV2Stub>() {
        @java.lang.Override
        public MathServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MathServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return MathServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MathServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MathServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MathServiceBlockingStub>() {
        @java.lang.Override
        public MathServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MathServiceBlockingStub(channel, callOptions);
        }
      };
    return MathServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MathServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MathServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MathServiceFutureStub>() {
        @java.lang.Override
        public MathServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MathServiceFutureStub(channel, callOptions);
        }
      };
    return MathServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void isPerfectNumber(math.Math.NumberRequest request,
        io.grpc.stub.StreamObserver<math.Math.BoolResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getIsPerfectNumberMethod(), responseObserver);
    }

    /**
     */
    default void gcd(math.Math.GcdRequest request,
        io.grpc.stub.StreamObserver<math.Math.GcdResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGcdMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service MathService.
   */
  public static abstract class MathServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MathServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service MathService.
   */
  public static final class MathServiceStub
      extends io.grpc.stub.AbstractAsyncStub<MathServiceStub> {
    private MathServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MathServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MathServiceStub(channel, callOptions);
    }

    /**
     */
    public void isPerfectNumber(math.Math.NumberRequest request,
        io.grpc.stub.StreamObserver<math.Math.BoolResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getIsPerfectNumberMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void gcd(math.Math.GcdRequest request,
        io.grpc.stub.StreamObserver<math.Math.GcdResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGcdMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service MathService.
   */
  public static final class MathServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<MathServiceBlockingV2Stub> {
    private MathServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MathServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MathServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public math.Math.BoolResponse isPerfectNumber(math.Math.NumberRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIsPerfectNumberMethod(), getCallOptions(), request);
    }

    /**
     */
    public math.Math.GcdResponse gcd(math.Math.GcdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGcdMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service MathService.
   */
  public static final class MathServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MathServiceBlockingStub> {
    private MathServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MathServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MathServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public math.Math.BoolResponse isPerfectNumber(math.Math.NumberRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getIsPerfectNumberMethod(), getCallOptions(), request);
    }

    /**
     */
    public math.Math.GcdResponse gcd(math.Math.GcdRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGcdMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service MathService.
   */
  public static final class MathServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<MathServiceFutureStub> {
    private MathServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MathServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MathServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<math.Math.BoolResponse> isPerfectNumber(
        math.Math.NumberRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getIsPerfectNumberMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<math.Math.GcdResponse> gcd(
        math.Math.GcdRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGcdMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_IS_PERFECT_NUMBER = 0;
  private static final int METHODID_GCD = 1;

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
        case METHODID_IS_PERFECT_NUMBER:
          serviceImpl.isPerfectNumber((math.Math.NumberRequest) request,
              (io.grpc.stub.StreamObserver<math.Math.BoolResponse>) responseObserver);
          break;
        case METHODID_GCD:
          serviceImpl.gcd((math.Math.GcdRequest) request,
              (io.grpc.stub.StreamObserver<math.Math.GcdResponse>) responseObserver);
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
          getIsPerfectNumberMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              math.Math.NumberRequest,
              math.Math.BoolResponse>(
                service, METHODID_IS_PERFECT_NUMBER)))
        .addMethod(
          getGcdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              math.Math.GcdRequest,
              math.Math.GcdResponse>(
                service, METHODID_GCD)))
        .build();
  }

  private static abstract class MathServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MathServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return math.Math.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("MathService");
    }
  }

  private static final class MathServiceFileDescriptorSupplier
      extends MathServiceBaseDescriptorSupplier {
    MathServiceFileDescriptorSupplier() {}
  }

  private static final class MathServiceMethodDescriptorSupplier
      extends MathServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    MathServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (MathServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MathServiceFileDescriptorSupplier())
              .addMethod(getIsPerfectNumberMethod())
              .addMethod(getGcdMethod())
              .build();
        }
      }
    }
    return result;
  }
}
