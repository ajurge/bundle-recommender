package com.product.bundle.recommender.controllers.redirect;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RootRedirectController {

    @RequestMapping(value = "/")
    public String index() {
        return "redirect:/docs/index.html";
    }
}
