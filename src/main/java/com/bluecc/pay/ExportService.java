package com.bluecc.pay;

import org.apache.ofbiz.base.util.Debug;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.model.DynamicViewEntity;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExportService {
    private static final String MODULE = ExportService.class.getName();;
    protected LocalDispatcher dispatcher = null;
    protected Delegator delegator = null;

    public ExportService(LocalDispatcher dispatcher, Delegator delegator) {
        this.dispatcher = dispatcher;
        this.delegator = delegator;
    }

    public Delegator getDelegator() {
        return delegator;
    }

    public void setDelegator(Delegator delegator) {
        this.delegator = delegator;
    }

    /**
     * Gets dispatcher.
     * @return the dispatcher
     */
    public LocalDispatcher getDispatcher() {
        return dispatcher;
    }


    /**
     * Gets user login.
     * @param userLoginId the user login id
     * @return the user login
     * @throws GenericEntityException the generic entity exception
     */
    protected GenericValue getUserLogin(String userLoginId) throws GenericEntityException {
        return EntityQuery.use(getDelegator())
                .from("UserLogin")
                .where("userLoginId", userLoginId)
                .queryOne();
    }

    protected void checkResult(String service, Map<String, Object> serviceResult ) throws ServiceFail {
        if(!ServiceUtil.isSuccess(serviceResult)){
            throw new ServiceFail("Fail to execute service "+service, serviceResult);
        }
    }

    /**
     * From entity query.
     * @param entityName the entity name
     * @return the entity query
     */
    protected EntityQuery from(String entityName) {
        return EntityQuery.use(getDelegator()).from(entityName);
    }

    /**
     * From entity query.
     * @param dynamicViewEntity the dynamic view entity
     * @return the entity query
     */
    protected EntityQuery from(DynamicViewEntity dynamicViewEntity) {
        return EntityQuery.use(getDelegator()).from(dynamicViewEntity);
    }

    /**
     * Select entity query.
     * @param fields the fields
     * @return the entity query
     */
    protected EntityQuery select(String... fields) {
        return EntityQuery.use(getDelegator()).select(fields);
    }

    /**
     * Select entity query.
     * @param fields the fields
     * @return the entity query
     */
    protected EntityQuery select(Set<String> fields) {
        return EntityQuery.use(getDelegator()).select(fields);
    }

    /**
     * Log info.
     * @param msg the msg
     */
    protected void logInfo(String msg) {
        Debug.logInfo(msg, MODULE);
    }

    /**
     * Log error.
     * @param msg the msg
     */
    protected void logError(String msg) {
        Debug.logError(msg, MODULE);
    }

    /**
     * Log warning.
     * @param msg the msg
     */
    protected void logWarning(String msg) {
        Debug.logWarning(msg, MODULE);
    }

    @SuppressWarnings("unchecked")
    protected List<GenericValue> valueList(Map<String, Object> ctx, String item) throws ServiceFail {
        List<GenericValue> values= (List<GenericValue>) ctx.get(item);
        if(values==null){
            throw new ServiceFail("Cannot find value-list "+item+" in context",
                    ServiceUtil.returnError("Cannot find value-list "+item+" in context"));
        }
        return values;
    }

    @SuppressWarnings("unchecked")
    protected List<GenericValue> ensureValueList(Map<String, Object> ctx, String item) throws ServiceFail {
        List<GenericValue> values= (List<GenericValue>) ctx.get(item);
        if(values==null){
            return new ArrayList<>();
        }
        return values;
    }
}
