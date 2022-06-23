package by.cinderella.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestControllerAdvice
public class ExceptionResolver {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoHandlerFound(NoHandlerFoundException e) {
        HashMap<String, String> model = new HashMap<>();
        model.put("message", "Мы сожалеем, но такой страницы нет.");

        return  new ModelAndView("error", model);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleServerError(NoHandlerFoundException e) {
        HashMap<String, String> model = new HashMap<>();
        model.put("message", "Что-то пошло не так. Если снова увидите эту страницу, обратитесь к администратору.");

        return  new ModelAndView("error", model);
    }

    @ExceptionHandler(SecurityException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleForbidden(NoHandlerFoundException e) {
        HashMap<String, String> model = new HashMap<>();
        model.put("message", "У вас нет прав для посещения данной страницы.");

        return  new ModelAndView("error", model);
    }
}
