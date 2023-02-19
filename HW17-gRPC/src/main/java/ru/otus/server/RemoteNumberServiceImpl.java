package ru.otus.server;

import io.grpc.stub.StreamObserver;
import ru.otus.protobuf.generated.NumberRequest;
import ru.otus.protobuf.generated.NumberResponse;
import ru.otus.protobuf.generated.RemoteNumberServiceGrpc;


import static ru.otus.common.GRPCNumberUtil.sleep;

public class RemoteNumberServiceImpl extends RemoteNumberServiceGrpc.RemoteNumberServiceImplBase {

    @Override
    public void generateNumbers(NumberRequest request, StreamObserver<NumberResponse> responseObserver) {
        System.out.println(String.format("Got request. Generate number from %d to %d", request.getNumberFrom(), request.getNumberTo()));
        for (long i = request.getNumberFrom(); i <= request.getNumberTo(); i++) {

            responseObserver.onNext(NumberResponse.newBuilder().setValue(i).build());
            sleep(2);

            if (i == request.getNumberTo()) {
                responseObserver.onCompleted();
            }
        }
        System.out.println("Generation finished");
    }
}
