package com.bluecc.pay;

import com.bluecc.api.ParameterUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.reflections.Reflections;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetaPackage {
    private static final Logger logger = Logger.getLogger(MetaPackage.class.getName());
    private String packageName;
    private List<MetaService> classes=new ArrayList<>();

    public MetaPackage(String packageName) {
        this.packageName = packageName;

        this.collect();
    }

    public String getPackageName() {
        return packageName;
    }

    public List<MetaService> getClasses() {
        return classes;
    }

    private void collect(){
        // "com.bluecc.pay.modules"
        Reflections reflections = new Reflections(this.packageName);
        Set<Class<? extends ExportService>> subTypes = reflections.getSubTypesOf(ExportService.class);
        subTypes.forEach(t -> processClass(t));
    }

    private void processClass(Class<? extends ExportService> clz){
        for(Method method:clz.getMethods()){
            Path path=method.getAnnotation(Path.class);
            if(path!=null) {
                logger.log(Level.INFO, "{0}: {1}", new String[]{method.getName(), path.value()});
                List<ParameterUtils.ParameterDesc> params = ParameterUtils.getParameterDesc(method);
                String retType=method.getReturnType().getTypeName();
                MetaService metaService = new MetaService(path.value(), params, retType);
                classes.add(metaService);
            }
        }
    }

    public static class MetaService {
        List<ParameterUtils.ParameterDesc> params;
        private String srvName;

        public String getReturnType() {
            return returnType;
        }

        private String returnType;

        public MetaService(String srvName, List<ParameterUtils.ParameterDesc> params, String returnType) {
            this.srvName = srvName;
            this.params = params;
            this.returnType=returnType;
        }

        public String getSrvName() {
            return srvName;
        }

        public List<ParameterUtils.ParameterDesc> getParams() {
            return params;
        }
    }

    public String getMetaJson() throws JsonProcessingException {
        String json = new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(this.getClasses());
        return json;
    }
}


