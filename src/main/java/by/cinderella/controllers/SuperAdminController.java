package by.cinderella.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('SADMIN')")
public class SuperAdminController {

    @GetMapping("/users")
    public String getUsers(HttpServletRequest request, Model model) {

        return "sadmin/users";
    }
}
