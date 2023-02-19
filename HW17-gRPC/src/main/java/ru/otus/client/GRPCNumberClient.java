package ru.otus.client;

import io.grpc.ManagedChannelBuilder;
import ru.otus.protobuf.generated.NumberRequest;
import ru.otus.protobuf.generated.RemoteNumberServiceGrpc;

import static ru.otus.common.GRPCAppConst.*;
import static ru.otus.common.GRPCNumberUtil.sleep;

public class GRPCNumberClient {

    private long currentValue = 0;

    public static void main(String[] args) {

        System.out.println(String.format("Creating connection to %s on port:%d", SERVER_HOST, SERVER_PORT));
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        var remoteService = RemoteNumberServiceGrpc.newStub(channel);

        System.out.println("Starting client job...");
        new GRPCNumberClient().go(remoteService);

        System.out.println("Shutdown client");
        channel.shutdown();
    }


    private void go(RemoteNumberServiceGrpc.RemoteNumberServiceStub remoteService) {
        var request = NumberRequest.newBuilder().setNumberFrom(SERVER_VALUE_FROM).setNumberTo(SERVER_VALUE_TO).build();
        var observer = new NumberStreamObserver();
        remoteService.generateNumbers(request, observer);


        for (long i = CLIENT_VALUE_FROM; i <= CLIENT_VALUE_TO; i++) {
            System.out.println("currentValue:" + countCurrentValue(observer));
            sleep(1);
        }
    }

    private long countCurrentValue(NumberStreamObserver observer) {
        currentValue = currentValue + observer.getLastAndReset() + 1;
        return currentValue;
    }
}
