package mvc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Handler {
	private Object handleObj;
	Method method;
	
	public Handler() {
		
	}
	
	public Handler(Object obj) {
		this.handleObj = obj;
	}

	public Object getHandleObj() {
		return handleObj;
	}

	public void setHandleObj(Object handleObj) {
		this.handleObj = handleObj;
	}
	
	public void setMethod(Method method) {
		this.method = method;
	}

	public ModelAndView	 handle(ModelAndView mav) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ModelAndView modelAndView = (ModelAndView)method.invoke(handleObj, mav);	
		return modelAndView;
	}
	
}
