package mvc;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

//import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;


public class ModelAndView {
	private String viewName;
	private Map<String, Object> requestParams = new HashMap<>();
	private Map<String, Object> keyParams = new HashMap<>();
	
	public ModelAndView() {
		// TODO Auto-generated constructor stub
	}
	
	public ModelAndView(HttpServletRequest request) {
		Enumeration<String> paramNames = request.getParameterNames();  
        while (paramNames.hasMoreElements()) {  
            String paramName = paramNames.nextElement();  
  
            String[] paramValues = request.getParameterValues(paramName);  
            if (paramValues.length == 1) {  
                String paramValue = paramValues[0];  
                if (paramValue.length() != 0) {  
                    requestParams.put(paramName, paramValue);  
                }  
            }  
        }  
	}
	
	public ModelAndView addObject(String attributeName, Object attributeValue) {
		keyParams.put(attributeName, attributeValue);
		return this;
	}
	
	
	public Object getMap(String name) {
		return requestParams.get(name);
	}
	
	public Map<String, Object> getKeyParams() {
		return  keyParams;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
}
