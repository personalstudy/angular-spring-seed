package org.personal.mason.ass.web.controller;

import org.personal.mason.ass.domain.model.ARole;
import org.personal.mason.ass.domain.model.Account;
import org.personal.mason.ass.domain.repository.AccountRepository;
import org.personal.mason.ass.domain.repository.GroupRepository;
import org.personal.mason.ass.domain.repository.PersistTokenRepository;
import org.personal.mason.ass.domain.repository.RoleRepository;
import org.personal.mason.ass.utils.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * PoUser: xvitcoder
 * Date: 12/20/12
 * Time: 5:27 PM
 */
@Controller
public class IndexController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PersistTokenRepository persistTokenRepository;

    @RequestMapping({"/", "/index"})
    public String getIndexPage(Device device) {
        DataUtils dataUtils = new DataUtils();
        List<ARole> roles = dataUtils.initRoles();
        Account account = dataUtils.initUser();
        roleRepository.save(roles);
        accountRepository.save(account);

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
