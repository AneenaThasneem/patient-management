package com.pm.billingsevice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {
    public static final Logger log= LoggerFactory.getLogger(BillingGrpcService.class);
@Override
    public void createBillingAccount(BillingRequest billingRequest, StreamObserver<BillingResponse>
                                     responseObserver){
         log.info("create BillingAccount request received {}", billingRequest.toString());
         //business logic-save to database ,do some calculations etc..
    BillingResponse response= BillingResponse.newBuilder()
            .setAccountId("1009").setStatus("ACTIVE")
            .build();
    responseObserver.onNext(response);
    responseObserver.onCompleted();
}
}
