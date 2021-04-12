package com.bluecc.pay.modules;

import com.bluecc.pay.ExportService;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.ofbiz.entity.condition.EntityCondition.makeCondition;
import static org.apache.ofbiz.entity.condition.EntityOperator.IN;

public class Payments extends ExportService {
    public Payments(LocalDispatcher dispatcher, Delegator delegator) {
        super(dispatcher, delegator);
    }

    @Path("depositWithdrawPaymentsReturn")
    public List<GenericValue> depositWithdrawPayments(
            @QueryParam("caller")GenericValue caller,
            @QueryParam("paymentIds") List<String> paymentIds,
            @QueryParam("finAccountId") String finAccountId) throws GenericServiceException, GenericEntityException {
        Map<String, Object> serviceResult = dispatcher.runSync("depositWithdrawPayments",
                UtilMisc.toMap("paymentIds", paymentIds,
                "finAccountId", finAccountId,
                "userLogin", caller));
        List<GenericValue> payments = from("Payment")
                .where(makeCondition("paymentId", IN, paymentIds))
                .queryList();

        List<GenericValue> finAccountTransList=new ArrayList<>();
        for (GenericValue payment : payments) {
            GenericValue finAccountTrans = from("FinAccountTrans")
                    .where("finAccountTransId", payment.get("finAccountTransId"))
                    .queryOne();
            finAccountTransList.add(finAccountTrans);
        }

        return finAccountTransList;
    }
}
