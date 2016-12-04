package mvc;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.reflections.Reflections;

@SuppressWarnings("serial")
public class DispathcherServlet extends HttpServlet {
	//
	private Map<String, Handler> handleMap;
	private List<Object> contollers;
	
	public DispathcherServlet() {
		super();
		handleMap = new HashMap<>();
		contollers = new ArrayList<>();
		loadControllers();
		System.out.println("I'm created!");
		
	}
	
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		String uri = arg0.getRequestURI().toString();

		int begin_index = uri.lastIndexOf('/');
		if(begin_index != -1){
			String param = uri.substring(begin_index);
			Handler handler = (Handler)handleMap.get(param);
			if(handler == null){
				if(this.setHandler(param)){
					handler = handleMap.get(param);
				}
				else{
					//
					System.out.println("log not found handler for " + param);
					super.service(arg0, arg1);
				}
			}
			try {
				ModelAndView modelAndView = handler.handle(new ModelAndView(arg0));
				String jspName = modelAndView.getViewName();
				if(jspName != null){
					Map<String, Object> paramMap = modelAndView.getKeyParams();
					for(Map.Entry<String, Object> entry : paramMap.entrySet()){
						arg0.setAttribute(entry.getKey(), entry.getValue());
					}
					arg0.getRequestDispatcher(jspName + ".jsp").forward(arg0, arg1);
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
				arg1.setStatus(500);
			}		
		}else{
			super.service(arg0, arg1);
		}
	}
	
	private void addHandler(String name, Handler handler){
		handleMap.put(name, handler);
	}
	
	private boolean setHandler(String param){
		boolean found = false;
		Handler handler = new Handler();
		RequestMapping requestMapping = null;
		for (Object object : contollers) {
			Method[] methods = object.getClass().getDeclaredMethods();
			for (Method method : methods) {
				requestMapping = method.getAnnotation(RequestMapping.class);
				//if method is found with the handling uri
				if(requestMapping != null && requestMapping.value().equals(param)){
					found = true;
					handler.setHandleObj(object);
					handler.setMethod(method);
					addHandler(param, handler);
					break;
				}
			}
			if(found)	break;
		}
		return found;
	}	
	private void loadControllers(){
		Reflections reflections = new Reflections("test");		
		Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
		for (Class<?> controllerClass : controllerClasses) {
			try {
				contollers.add(controllerClass.newInstance());
				System.out.println("log load a controller class: " + controllerClass.getName());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}	
}
