package com.bluecc.pay;

import org.apache.ofbiz.ws.rs.resources.OFBizServiceResource;
import org.junit.Test;
import org.reflections.Reflections;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class AnnotationTest {
    @Test
    public void testAnnotation(){
        Class<?> clz= OFBizServiceResource.class;
        for(Method method:clz.getMethods()){
            Path path=method.getAnnotation(Path.class);
            if(path!=null) {
                System.out.format("%s: %s\n", method.getName(), path.value());
            }
            List<ParameterUtils.ParameterDesc> params= ParameterUtils.getParameterDesc(method);
            if(!params.isEmpty()) {
                System.out.println(params);
            }
        }
    }

    // https://github.com/ronmamo/reflections
    @Test
    public void testPackageClasses(){
        Reflections reflections = new Reflections("com.bluecc.pay.modules");
        Set<Class<? extends ExportService>> subTypes = reflections.getSubTypesOf(ExportService.class);
        subTypes.forEach(t -> System.out.println(t.getSimpleName()));
    }
}
