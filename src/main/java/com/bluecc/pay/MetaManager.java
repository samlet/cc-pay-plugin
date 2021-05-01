package com.bluecc.pay;

import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.ServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MetaManager {
    private static MetaManager INSTANCE;

    public static MetaManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new MetaManager();
        }
        return INSTANCE;
    }

    Map<String, MetaPackage> metaPackageMap=new HashMap<>();
    private MetaManager(){
        init("com.bluecc.pay.modules");
    }

    private void init(String pkg){
        this.metaPackageMap.put(pkg, new MetaPackage(pkg));
    }

    public List<MetaPackage.MetaService> getAllMeta(){
        return metaPackageMap.values().stream()
                .flatMap(p -> p.getClasses().stream())
                .collect(Collectors.toList());
    }

    public List<MetaPackage.MetaService> getMetaServices(String pkg){
        if(metaPackageMap.containsKey(pkg)){
            return metaPackageMap.get(pkg).getClasses();
        }
        return new ArrayList<>();
    }

    public static Map<String, Object> retrieveMeta(DispatchContext ctx, Map<String, ? extends Object> context) throws GenericServiceException {
        Map<String, Object> result = ServiceUtil.returnSuccess();
        try {
            String pkg=(String)context.get("packageName");
            result.put("metas", MetaManager.getInstance().getMetaServices(pkg));
        } catch (Exception e) {
            throw new GenericServiceException(e.getMessage());
        }
        return result;
    }
}
