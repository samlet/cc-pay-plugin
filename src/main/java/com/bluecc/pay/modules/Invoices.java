package com.bluecc.pay.modules;

import com.bluecc.pay.ExportService;
import com.bluecc.pay.ServiceFail;
import org.apache.ofbiz.accounting.invoice.InvoiceWorker;
import org.apache.ofbiz.base.util.UtilDateTime;
import org.apache.ofbiz.base.util.UtilMisc;
import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.GenericServiceException;
import org.apache.ofbiz.service.LocalDispatcher;

import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Invoices extends ExportService {
    public Invoices(LocalDispatcher dispatcher, Delegator delegator) {
        super(dispatcher, delegator);
    }

    public BigDecimal getInvoiceTotal(@QueryParam("invoiceId") String invoiceId) throws GenericEntityException {
        GenericValue invoice =  EntityQuery.use(delegator).from("Invoice")
                .where("invoiceId", invoiceId).queryOne();
        return InvoiceWorker.getInvoiceTotal(invoice);
    }

    public List<GenericValue> getInvoiceItems(@QueryParam("invoiceId") String invoiceId) throws GenericEntityException {
        return from("InvoiceItem")
                .where("invoiceId", invoiceId)
                .queryList();
    }

    @NotNull
    public List<GenericValue> currentCustomTimePeriods(GenericValue caller, String organizationPartyId) throws GenericServiceException, ServiceFail {
        Map<String, Object> serviceResult = dispatcher.runSync("findCustomTimePeriods",
                UtilMisc.toMap("organizationPartyId", organizationPartyId,
                "findDate", UtilDateTime.nowTimestamp(),
                "userLogin", caller));
        checkResult("findCustomTimePeriods", serviceResult);
        return ensureValueList(serviceResult, "customTimePeriodList");
    }
}

