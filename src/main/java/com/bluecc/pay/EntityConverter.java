package com.bluecc.pay;

import com.bluecc.packer.routines.ValueObject;
import com.google.common.io.ByteStreams;
import com.google.protobuf.ByteString;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Map;

public class EntityConverter {
    public ValueObject convertGenericValueMap(String entityName, Map<String, Object> fields) throws SQLException, IOException {
        ValueObject.Builder vo=ValueObject.newBuilder();
        for(Map.Entry<String,Object> entry:fields.entrySet()){
            Object val=entry.getValue();
            if(val instanceof Blob){
                ByteString arr= ByteString.copyFrom(ByteStreams.toByteArray(((Blob) val).getBinaryStream()));
                vo.putData(entry.getKey(), ValueObject.Value.newBuilder().setBlob(arr).build());
            }else if(val instanceof Integer){
                vo.putData(entry.getKey(), ValueObject.Value.newBuilder().setIntValue((Integer) val).build());
            }else if(val instanceof Long){
                vo.putData(entry.getKey(), ValueObject.Value.newBuilder().setIntValue((Long) val).build());
            }else if(val instanceof Double){
                vo.putData(entry.getKey(), ValueObject.Value.newBuilder().setRealValue((Double) val).build());
            }else if(val instanceof Float){
                vo.putData(entry.getKey(), ValueObject.Value.newBuilder().setRealValue((Float) val).build());
            }else if(val instanceof BigDecimal){
                vo.putData(entry.getKey(), ValueObject.Value.newBuilder().setRealValue(((BigDecimal) val).doubleValue()).build());
            }else if(val instanceof java.sql.Timestamp || val instanceof java.sql.Date || val instanceof java.sql.Time){
                vo.putData(entry.getKey(), ValueObject.Value.newBuilder().setStringValue(val.toString()).build());
            }else{
                vo.putData(entry.getKey(), ValueObject.Value.newBuilder().setStringValue(val.toString()).build());
            }
        }
        return vo.build();
    }
}

