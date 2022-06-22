package com.example.demo;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    static DefaultBeanFactory factory = new DefaultBeanFactory();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //初始化bean容器
        initContainer(); 
    }

    //初始化bean容器
    private void initContainer() {
        System.out.println("初始化容器");
        Set<Class<?>> beanClassSet = ScanClass.getBeanClassSet();
        if (beanClassSet != null && !beanClassSet.isEmpty()) {
            try {
                for (Class<?> beanClass : beanClassSet) {
                    GenericBeanDefinition gbd = new GenericBeanDefinition();
                    gbd.setBeanClass(beanClass);

                    //去掉注册，会导致循环引用失效，
                    factory.registryBeanDefinition(beanClass.getSimpleName(), gbd);
                } 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
   }

 


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String reqMethod = req.getMethod().toLowerCase();
        String reqPath = req.getRequestURI();

        String[] split = reqPath.split("/");
        if (split.length > 2) {
            reqPath = "/" + split[2];
        }
        //根据请求获取处理器（这里类似SpringMVC中的映射处理器）
        Handler handler = c.getHandler(reqMethod, reqPath);
        if (handler != null) {
            Class<?> controllerClass = handler.getControllerClass();
            try {
                Object controllerBean = factory.getBean(controllerClass.getSimpleName());
                //初始化参数
                Param param = RequestParamHandler.createParam(req);

                //调用与请求对应的方法（类似SpringMVC中的处理器适配器）
                Object result;
                Method actionMethod = handler.getControllerMethod();
                if (param == null || param.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
                } 
                handleDataResult(result, req, resp);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
 

    //返回json
    private void handleDataResult(Object data, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (data != null) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter writer = resp.getWriter();
            //Model转JSON
            ObjectMapper mapper = new ObjectMapper();
            if (data instanceof Collection) { //集合
                String valueAsString = mapper.writeValueAsString(data);
                writer.write(valueAsString);
            } else {
                writer.write(data.toString());
            }
            writer.flush();
            writer.close();
        }


    }
}