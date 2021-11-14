package com.bluecc.pay;

import com.bluecc.generic.SrvBase;
import com.bluecc.packer.routines.ConfigItem;
import com.bluecc.packer.routines.RoutinesGrpc;
import com.bluecc.packer.routines.StringValue;
import com.bluecc.packer.routines.ValueObject;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;
import org.apache.ofbiz.base.container.ContainerException;
import org.apache.ofbiz.base.util.UtilProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class SrvRoutines extends SrvBase {
    private static final Logger logger = Logger.getLogger(SrvRoutines.class.getName());

    private Server server;

    public boolean start() throws ContainerException {
        /* The port on which the server should run */
        int port = this.port;
        try {
            server = ServerBuilder.forPort(port)
                    .addService(new GreeterImpl())
                    .addService(new RoutinesImpl())
                    .build()
                    .start();
        } catch (IOException e) {
            throw new ContainerException(e.getMessage(), e);
        }

        logger.info("Server started, listening on " + port);
        System.out.println(" [✔] CcPay started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                try {
                    SrvRoutines.this.stop();
                } catch (ContainerException e) {
                    e.printStackTrace(System.err);
                }
                System.err.println("*** server shut down");
            }
        });

        return true;
    }

    public void stop() throws ContainerException {
        if (server != null) {
            try {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new ContainerException(e.getMessage(), e);
            }
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws InterruptedException, ContainerException {
        final SrvRoutines server = new SrvRoutines();
        server.start();
        server.blockUntilShutdown();
    }

    static class GreeterImpl extends GreeterGrpc.GreeterImplBase {

        @Override
        public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
            HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }

    static class RoutinesImpl extends RoutinesGrpc.RoutinesImplBase{

        @Override
        public void getConfig(ConfigItem request, StreamObserver<ValueObject> responseObserver) {
            ValueObject.Builder vo=ValueObject.newBuilder();
            // String protocol = UtilProperties.getPropertyValue("jsse", "java.protocol.handler.pkgs", "NONE");
            vo.putData("timestamp", ValueObject.Value.newBuilder()
                    .setIntValue(System.currentTimeMillis()).build());
            String itemVal=UtilProperties.getPropertyValue(request.getItem(), request.getKey(), "NONE");
            vo.putData(request.getItem()+"|"+request.getKey(), ValueObject.Value.newBuilder()
                    .setStringValue(itemVal).build());

            responseObserver.onNext(vo.build());
            responseObserver.onCompleted();
        }
    }
}

