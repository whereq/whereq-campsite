package com.whereq.campsite.web.rest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorHandlerController implements ErrorController {

  @RequestMapping("/error")
  @ResponseBody
  public String getErrorPath() {
    return "error";
  }
}
