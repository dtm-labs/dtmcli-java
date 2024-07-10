package pub.dtm.client.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * The dtm service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.0)",
    comments = "Source: src/main/proto/dtmgimp.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DtmGrpc {

  private DtmGrpc() {}

  public static final java.lang.String SERVICE_NAME = "dtmgimp.Dtm";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      pub.dtm.client.grpc.Dtmgimp.DtmGidReply> getNewGidMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "NewGid",
      requestType = com.google.protobuf.Empty.class,
      responseType = pub.dtm.client.grpc.Dtmgimp.DtmGidReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      pub.dtm.client.grpc.Dtmgimp.DtmGidReply> getNewGidMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, pub.dtm.client.grpc.Dtmgimp.DtmGidReply> getNewGidMethod;
    if ((getNewGidMethod = DtmGrpc.getNewGidMethod) == null) {
      synchronized (DtmGrpc.class) {
        if ((getNewGidMethod = DtmGrpc.getNewGidMethod) == null) {
          DtmGrpc.getNewGidMethod = getNewGidMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, pub.dtm.client.grpc.Dtmgimp.DtmGidReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "NewGid"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmGidReply.getDefaultInstance()))
              .setSchemaDescriptor(new DtmMethodDescriptorSupplier("NewGid"))
              .build();
        }
      }
    }
    return getNewGidMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest,
      com.google.protobuf.Empty> getSubmitMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Submit",
      requestType = pub.dtm.client.grpc.Dtmgimp.DtmRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest,
      com.google.protobuf.Empty> getSubmitMethod() {
    io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest, com.google.protobuf.Empty> getSubmitMethod;
    if ((getSubmitMethod = DtmGrpc.getSubmitMethod) == null) {
      synchronized (DtmGrpc.class) {
        if ((getSubmitMethod = DtmGrpc.getSubmitMethod) == null) {
          DtmGrpc.getSubmitMethod = getSubmitMethod =
              io.grpc.MethodDescriptor.<pub.dtm.client.grpc.Dtmgimp.DtmRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Submit"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DtmMethodDescriptorSupplier("Submit"))
              .build();
        }
      }
    }
    return getSubmitMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest,
      com.google.protobuf.Empty> getPrepareMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Prepare",
      requestType = pub.dtm.client.grpc.Dtmgimp.DtmRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest,
      com.google.protobuf.Empty> getPrepareMethod() {
    io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest, com.google.protobuf.Empty> getPrepareMethod;
    if ((getPrepareMethod = DtmGrpc.getPrepareMethod) == null) {
      synchronized (DtmGrpc.class) {
        if ((getPrepareMethod = DtmGrpc.getPrepareMethod) == null) {
          DtmGrpc.getPrepareMethod = getPrepareMethod =
              io.grpc.MethodDescriptor.<pub.dtm.client.grpc.Dtmgimp.DtmRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Prepare"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DtmMethodDescriptorSupplier("Prepare"))
              .build();
        }
      }
    }
    return getPrepareMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest,
      com.google.protobuf.Empty> getAbortMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Abort",
      requestType = pub.dtm.client.grpc.Dtmgimp.DtmRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest,
      com.google.protobuf.Empty> getAbortMethod() {
    io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest, com.google.protobuf.Empty> getAbortMethod;
    if ((getAbortMethod = DtmGrpc.getAbortMethod) == null) {
      synchronized (DtmGrpc.class) {
        if ((getAbortMethod = DtmGrpc.getAbortMethod) == null) {
          DtmGrpc.getAbortMethod = getAbortMethod =
              io.grpc.MethodDescriptor.<pub.dtm.client.grpc.Dtmgimp.DtmRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Abort"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DtmMethodDescriptorSupplier("Abort"))
              .build();
        }
      }
    }
    return getAbortMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest,
      com.google.protobuf.Empty> getRegisterBranchMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "RegisterBranch",
      requestType = pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest,
      com.google.protobuf.Empty> getRegisterBranchMethod() {
    io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest, com.google.protobuf.Empty> getRegisterBranchMethod;
    if ((getRegisterBranchMethod = DtmGrpc.getRegisterBranchMethod) == null) {
      synchronized (DtmGrpc.class) {
        if ((getRegisterBranchMethod = DtmGrpc.getRegisterBranchMethod) == null) {
          DtmGrpc.getRegisterBranchMethod = getRegisterBranchMethod =
              io.grpc.MethodDescriptor.<pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "RegisterBranch"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DtmMethodDescriptorSupplier("RegisterBranch"))
              .build();
        }
      }
    }
    return getRegisterBranchMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest,
      pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply> getPrepareWorkflowMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "PrepareWorkflow",
      requestType = pub.dtm.client.grpc.Dtmgimp.DtmRequest.class,
      responseType = pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest,
      pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply> getPrepareWorkflowMethod() {
    io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmRequest, pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply> getPrepareWorkflowMethod;
    if ((getPrepareWorkflowMethod = DtmGrpc.getPrepareWorkflowMethod) == null) {
      synchronized (DtmGrpc.class) {
        if ((getPrepareWorkflowMethod = DtmGrpc.getPrepareWorkflowMethod) == null) {
          DtmGrpc.getPrepareWorkflowMethod = getPrepareWorkflowMethod =
              io.grpc.MethodDescriptor.<pub.dtm.client.grpc.Dtmgimp.DtmRequest, pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "PrepareWorkflow"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply.getDefaultInstance()))
              .setSchemaDescriptor(new DtmMethodDescriptorSupplier("PrepareWorkflow"))
              .build();
        }
      }
    }
    return getPrepareWorkflowMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest,
      com.google.protobuf.Empty> getSubscribeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Subscribe",
      requestType = pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest,
      com.google.protobuf.Empty> getSubscribeMethod() {
    io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest, com.google.protobuf.Empty> getSubscribeMethod;
    if ((getSubscribeMethod = DtmGrpc.getSubscribeMethod) == null) {
      synchronized (DtmGrpc.class) {
        if ((getSubscribeMethod = DtmGrpc.getSubscribeMethod) == null) {
          DtmGrpc.getSubscribeMethod = getSubscribeMethod =
              io.grpc.MethodDescriptor.<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Subscribe"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DtmMethodDescriptorSupplier("Subscribe"))
              .build();
        }
      }
    }
    return getSubscribeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest,
      com.google.protobuf.Empty> getUnsubscribeMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Unsubscribe",
      requestType = pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest,
      com.google.protobuf.Empty> getUnsubscribeMethod() {
    io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest, com.google.protobuf.Empty> getUnsubscribeMethod;
    if ((getUnsubscribeMethod = DtmGrpc.getUnsubscribeMethod) == null) {
      synchronized (DtmGrpc.class) {
        if ((getUnsubscribeMethod = DtmGrpc.getUnsubscribeMethod) == null) {
          DtmGrpc.getUnsubscribeMethod = getUnsubscribeMethod =
              io.grpc.MethodDescriptor.<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Unsubscribe"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DtmMethodDescriptorSupplier("Unsubscribe"))
              .build();
        }
      }
    }
    return getUnsubscribeMethod;
  }

  private static volatile io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest,
      com.google.protobuf.Empty> getDeleteTopicMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "DeleteTopic",
      requestType = pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest.class,
      responseType = com.google.protobuf.Empty.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest,
      com.google.protobuf.Empty> getDeleteTopicMethod() {
    io.grpc.MethodDescriptor<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest, com.google.protobuf.Empty> getDeleteTopicMethod;
    if ((getDeleteTopicMethod = DtmGrpc.getDeleteTopicMethod) == null) {
      synchronized (DtmGrpc.class) {
        if ((getDeleteTopicMethod = DtmGrpc.getDeleteTopicMethod) == null) {
          DtmGrpc.getDeleteTopicMethod = getDeleteTopicMethod =
              io.grpc.MethodDescriptor.<pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest, com.google.protobuf.Empty>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "DeleteTopic"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setSchemaDescriptor(new DtmMethodDescriptorSupplier("DeleteTopic"))
              .build();
        }
      }
    }
    return getDeleteTopicMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DtmStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DtmStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DtmStub>() {
        @java.lang.Override
        public DtmStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DtmStub(channel, callOptions);
        }
      };
    return DtmStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DtmBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DtmBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DtmBlockingStub>() {
        @java.lang.Override
        public DtmBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DtmBlockingStub(channel, callOptions);
        }
      };
    return DtmBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DtmFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DtmFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DtmFutureStub>() {
        @java.lang.Override
        public DtmFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DtmFutureStub(channel, callOptions);
        }
      };
    return DtmFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The dtm service definition.
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void newGid(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<pub.dtm.client.grpc.Dtmgimp.DtmGidReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNewGidMethod(), responseObserver);
    }

    /**
     */
    default void submit(pub.dtm.client.grpc.Dtmgimp.DtmRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubmitMethod(), responseObserver);
    }

    /**
     */
    default void prepare(pub.dtm.client.grpc.Dtmgimp.DtmRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPrepareMethod(), responseObserver);
    }

    /**
     */
    default void abort(pub.dtm.client.grpc.Dtmgimp.DtmRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAbortMethod(), responseObserver);
    }

    /**
     */
    default void registerBranch(pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRegisterBranchMethod(), responseObserver);
    }

    /**
     */
    default void prepareWorkflow(pub.dtm.client.grpc.Dtmgimp.DtmRequest request,
        io.grpc.stub.StreamObserver<pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getPrepareWorkflowMethod(), responseObserver);
    }

    /**
     */
    default void subscribe(pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubscribeMethod(), responseObserver);
    }

    /**
     */
    default void unsubscribe(pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUnsubscribeMethod(), responseObserver);
    }

    /**
     */
    default void deleteTopic(pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getDeleteTopicMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Dtm.
   * <pre>
   * The dtm service definition.
   * </pre>
   */
  public static abstract class DtmImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DtmGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Dtm.
   * <pre>
   * The dtm service definition.
   * </pre>
   */
  public static final class DtmStub
      extends io.grpc.stub.AbstractAsyncStub<DtmStub> {
    private DtmStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DtmStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DtmStub(channel, callOptions);
    }

    /**
     */
    public void newGid(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<pub.dtm.client.grpc.Dtmgimp.DtmGidReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNewGidMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void submit(pub.dtm.client.grpc.Dtmgimp.DtmRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubmitMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void prepare(pub.dtm.client.grpc.Dtmgimp.DtmRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPrepareMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void abort(pub.dtm.client.grpc.Dtmgimp.DtmRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAbortMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void registerBranch(pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRegisterBranchMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void prepareWorkflow(pub.dtm.client.grpc.Dtmgimp.DtmRequest request,
        io.grpc.stub.StreamObserver<pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getPrepareWorkflowMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void subscribe(pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSubscribeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unsubscribe(pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUnsubscribeMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void deleteTopic(pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request,
        io.grpc.stub.StreamObserver<com.google.protobuf.Empty> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getDeleteTopicMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Dtm.
   * <pre>
   * The dtm service definition.
   * </pre>
   */
  public static final class DtmBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DtmBlockingStub> {
    private DtmBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DtmBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DtmBlockingStub(channel, callOptions);
    }

    /**
     */
    public pub.dtm.client.grpc.Dtmgimp.DtmGidReply newGid(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNewGidMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty submit(pub.dtm.client.grpc.Dtmgimp.DtmRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubmitMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty prepare(pub.dtm.client.grpc.Dtmgimp.DtmRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPrepareMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty abort(pub.dtm.client.grpc.Dtmgimp.DtmRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAbortMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty registerBranch(pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRegisterBranchMethod(), getCallOptions(), request);
    }

    /**
     */
    public pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply prepareWorkflow(pub.dtm.client.grpc.Dtmgimp.DtmRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getPrepareWorkflowMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty subscribe(pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSubscribeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty unsubscribe(pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUnsubscribeMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.google.protobuf.Empty deleteTopic(pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getDeleteTopicMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Dtm.
   * <pre>
   * The dtm service definition.
   * </pre>
   */
  public static final class DtmFutureStub
      extends io.grpc.stub.AbstractFutureStub<DtmFutureStub> {
    private DtmFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DtmFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DtmFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pub.dtm.client.grpc.Dtmgimp.DtmGidReply> newGid(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNewGidMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> submit(
        pub.dtm.client.grpc.Dtmgimp.DtmRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubmitMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> prepare(
        pub.dtm.client.grpc.Dtmgimp.DtmRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPrepareMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> abort(
        pub.dtm.client.grpc.Dtmgimp.DtmRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAbortMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> registerBranch(
        pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRegisterBranchMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply> prepareWorkflow(
        pub.dtm.client.grpc.Dtmgimp.DtmRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getPrepareWorkflowMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> subscribe(
        pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSubscribeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> unsubscribe(
        pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUnsubscribeMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.google.protobuf.Empty> deleteTopic(
        pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getDeleteTopicMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_NEW_GID = 0;
  private static final int METHODID_SUBMIT = 1;
  private static final int METHODID_PREPARE = 2;
  private static final int METHODID_ABORT = 3;
  private static final int METHODID_REGISTER_BRANCH = 4;
  private static final int METHODID_PREPARE_WORKFLOW = 5;
  private static final int METHODID_SUBSCRIBE = 6;
  private static final int METHODID_UNSUBSCRIBE = 7;
  private static final int METHODID_DELETE_TOPIC = 8;

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
        case METHODID_NEW_GID:
          serviceImpl.newGid((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<pub.dtm.client.grpc.Dtmgimp.DtmGidReply>) responseObserver);
          break;
        case METHODID_SUBMIT:
          serviceImpl.submit((pub.dtm.client.grpc.Dtmgimp.DtmRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_PREPARE:
          serviceImpl.prepare((pub.dtm.client.grpc.Dtmgimp.DtmRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_ABORT:
          serviceImpl.abort((pub.dtm.client.grpc.Dtmgimp.DtmRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_REGISTER_BRANCH:
          serviceImpl.registerBranch((pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_PREPARE_WORKFLOW:
          serviceImpl.prepareWorkflow((pub.dtm.client.grpc.Dtmgimp.DtmRequest) request,
              (io.grpc.stub.StreamObserver<pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply>) responseObserver);
          break;
        case METHODID_SUBSCRIBE:
          serviceImpl.subscribe((pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_UNSUBSCRIBE:
          serviceImpl.unsubscribe((pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
          break;
        case METHODID_DELETE_TOPIC:
          serviceImpl.deleteTopic((pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest) request,
              (io.grpc.stub.StreamObserver<com.google.protobuf.Empty>) responseObserver);
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
          getNewGidMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              pub.dtm.client.grpc.Dtmgimp.DtmGidReply>(
                service, METHODID_NEW_GID)))
        .addMethod(
          getSubmitMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pub.dtm.client.grpc.Dtmgimp.DtmRequest,
              com.google.protobuf.Empty>(
                service, METHODID_SUBMIT)))
        .addMethod(
          getPrepareMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pub.dtm.client.grpc.Dtmgimp.DtmRequest,
              com.google.protobuf.Empty>(
                service, METHODID_PREPARE)))
        .addMethod(
          getAbortMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pub.dtm.client.grpc.Dtmgimp.DtmRequest,
              com.google.protobuf.Empty>(
                service, METHODID_ABORT)))
        .addMethod(
          getRegisterBranchMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pub.dtm.client.grpc.Dtmgimp.DtmBranchRequest,
              com.google.protobuf.Empty>(
                service, METHODID_REGISTER_BRANCH)))
        .addMethod(
          getPrepareWorkflowMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pub.dtm.client.grpc.Dtmgimp.DtmRequest,
              pub.dtm.client.grpc.Dtmgimp.DtmProgressesReply>(
                service, METHODID_PREPARE_WORKFLOW)))
        .addMethod(
          getSubscribeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest,
              com.google.protobuf.Empty>(
                service, METHODID_SUBSCRIBE)))
        .addMethod(
          getUnsubscribeMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest,
              com.google.protobuf.Empty>(
                service, METHODID_UNSUBSCRIBE)))
        .addMethod(
          getDeleteTopicMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              pub.dtm.client.grpc.Dtmgimp.DtmTopicRequest,
              com.google.protobuf.Empty>(
                service, METHODID_DELETE_TOPIC)))
        .build();
  }

  private static abstract class DtmBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DtmBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return pub.dtm.client.grpc.Dtmgimp.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Dtm");
    }
  }

  private static final class DtmFileDescriptorSupplier
      extends DtmBaseDescriptorSupplier {
    DtmFileDescriptorSupplier() {}
  }

  private static final class DtmMethodDescriptorSupplier
      extends DtmBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DtmMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (DtmGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DtmFileDescriptorSupplier())
              .addMethod(getNewGidMethod())
              .addMethod(getSubmitMethod())
              .addMethod(getPrepareMethod())
              .addMethod(getAbortMethod())
              .addMethod(getRegisterBranchMethod())
              .addMethod(getPrepareWorkflowMethod())
              .addMethod(getSubscribeMethod())
              .addMethod(getUnsubscribeMethod())
              .addMethod(getDeleteTopicMethod())
              .build();
        }
      }
    }
    return result;
  }
}
