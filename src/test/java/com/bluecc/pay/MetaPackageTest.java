package com.bluecc.pay;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import static com.bluecc.api.JsonUtil.pretty;

public class MetaPackageTest {

    @Test
    public void getClasses() throws JsonProcessingException {
        MetaPackage pkg=new MetaPackage("com.bluecc.pay.modules");
        System.out.println(pretty(pkg.getClasses()));
    }
}