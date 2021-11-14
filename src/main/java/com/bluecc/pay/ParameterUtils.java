package com.bluecc.pay;

import javax.validation.constraints.NotNull;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParameterUtils {
    public static class ParameterDesc{
        public String path;
        public String query;
        public String name;
        public String type;

        @Override
        public String toString() {
            return "ParameterDesc{" +
                    "path='" + path + '\'' +
                    ", query='" + query + '\'' +
                    ", name='" + name + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }
    }

    @NotNull
    public static List<ParameterDesc> getParameterDesc(Method method) {
        Parameter[] parameters=method.getParameters();
        List<ParameterDesc> parameterDescs = new ArrayList<>();
        for(Parameter parameter:parameters){
            ParameterDesc desc=new ParameterDesc();
            PathParam path=parameter.getAnnotation(PathParam.class);
            desc.name=parameter.getName();
            desc.type=parameter.getType().getName();

            if(path!=null){
                desc.path=path.value();
                desc.name=path.value(); // overwrite
            }
            QueryParam query=parameter.getAnnotation(QueryParam.class);
            if(query!=null){
                desc.query=query.value();
                desc.name=query.value(); // overwrite
            }
            parameterDescs.add(desc);
        }
        return parameterDescs;
    }
}
