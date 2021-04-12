package com.bluecc.pay;

import com.bluecc.packer.EntityConverter;
import com.bluecc.packer.routines.ValueObject;
import io.grpc.stub.StreamObserver;
import org.apache.ofbiz.entity.GenericValue;

import java.io.IOException;
import java.sql.SQLException;

public class GenericValueObserver implements StreamObserver<GenericValue> {
    StreamObserver<ValueObject> observer;
    public GenericValueObserver(StreamObserver<ValueObject> observer){
        this.observer=observer;
    }

    @Override
    public void onNext(GenericValue value) {
        EntityConverter converter=new EntityConverter();
        try {
            ValueObject vo=converter.convertGenericValueMap(value.getEntityName(), value);
            this.observer.onNext(vo);
        } catch (SQLException | IOException throwables) {
            this.onError(throwables);
        }
    }

    @Override
    public void onError(Throwable t) {
        observer.onError(t);
    }

    @Override
    public void onCompleted() {
        observer.onCompleted();
    }
}
