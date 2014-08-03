package org.personal.mason.ass.web.controller;

import org.personal.mason.ass.common.oauth2.JpaClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created with IntelliJ IDEA.
 * PoUser: xvitcoder
 * Date: 12/20/12
 * Time: 5:27 PM
 */
@Controller
public class IndexController {

    @RequestMapping({"/", "/index"})
    public String getIndexPage(Device device) {
        if (device != null && device.isMobile() || device.isTablet()) {
            return "mobile/" + "index";
        } else {
            return "normal/" + "index";
        }
    }

    @RequestMapping("/view")
    public String getRailwayStationPartialPage(Device device,
                                               @RequestParam("vn") String viewName) {
        if (device != null && device.isMobile() || device.isTablet()) {
            return "mobile/" + viewName;
        } else {
            return "normal/" + viewName;
        }
    }
}
