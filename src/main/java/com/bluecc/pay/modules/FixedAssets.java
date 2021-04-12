package com.bluecc.pay.modules;

import com.bluecc.pay.ExportService;
import com.bluecc.pay.ServiceFail;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.sql.Timestamp;
import java.util.Map;

public class FixedAssets extends ExportService {
    public FixedAssets(LocalDispatcher dispatcher, Delegator delegator) {
        super(dispatcher, delegator);
    }

    @Path("createFixedAssetRegistrationReturn")
    public GenericValue createFixedAssetRegistration(
            @QueryParam("caller") GenericValue caller,
            @QueryParam("fixedAssetId") String fixedAssetId,
            @QueryParam("licenseNumber") String licenseNumber,
            @QueryParam("registrationNumber") String registrationNumber,
            @QueryParam("fromDate") Timestamp fromDate,
            @QueryParam("thruDate") Timestamp thruDate,
            @QueryParam("registrationDate") Timestamp registrationDate)
            throws GenericServiceException, ServiceFail, GenericEntityException {
        Map<String, Object> serviceResult = dispatcher.runSync("createFixedAssetRegistration",
                UtilMisc.toMap("fixedAssetId", fixedAssetId,
                        "licenseNumber", licenseNumber,
                        "registrationNumber", registrationNumber,
                        "fromDate", fromDate,
                        "thruDate", thruDate,
                        "registrationDate", registrationDate,
                        "userLogin", caller));

        checkResult("createFixedAssetRegistration", serviceResult);
        GenericValue fixedAssetRegistration = from("FixedAssetRegistration")
                .where("fixedAssetId", fixedAssetId, "fromDate", fromDate)
                .filterByDate().queryOne();
        return fixedAssetRegistration;
    }
}
