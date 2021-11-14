package com.bluecc.pay;

import com.bluecc.pay.dummy.RouteGuideUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import io.grpc.examples.routeguide.Feature;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.ServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SeedResources {
    public static List<Map<String, Object>> getResources() throws IOException {
        List<Feature> features= RouteGuideUtil.parseFeatures(RouteGuideUtil.getDefaultFeaturesFile());
        ObjectMapper objectMapper=new ObjectMapper();

        List<Map<String, Object>> result=new ArrayList<>();
        for(Feature f:features) {
            String json = JsonFormat.printer().print(f);
            Map<String, Object> mapValues = objectMapper.readValue(json,
                    new TypeReference<Map<String, Object>>() {});
            result.add(mapValues);
        }
        return result;
    }

    public static Map<String, Object> retrieveResources(DispatchContext ctx, Map<String, ? extends Object> context) throws GenericServiceException {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        try {
            result.put("resources", getResources());
        } catch (IOException e) {
            throw new GenericServiceException(e.getMessage());
        }
        return result;
    }
}

