package com.bluecc.pay.modules;

import com.bluecc.pay.ExportService;
import com.bluecc.pay.ServiceFail;
import org.apache.commons.net.ntp.TimeStamp;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import java.util.Map;

public class FixedAssets extends ExportService {
    public FixedAssets(LocalDispatcher dispatcher, Delegator delegator) {
        super(dispatcher, delegator);
    }

    public GenericValue createFixedAssetRegistration(
            GenericValue caller,
            String fixedAssetId, String licenseNumber,
            String registrationNumber,
            TimeStamp fromDate,
            TimeStamp thruDate,
            TimeStamp registrationDate) throws GenericServiceException, ServiceFail, GenericEntityException {
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
