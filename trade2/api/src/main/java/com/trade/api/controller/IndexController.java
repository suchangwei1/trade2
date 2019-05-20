package com.trade.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController extends ApiBaseController {

    @RequestMapping({"/", "/index"})
    public Object index() {
        return "";
    }

}
