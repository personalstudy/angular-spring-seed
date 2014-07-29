package org.personal.mason.ass.common.rememberme.model;

import org.personal.mason.ass.common.authority.model.User;

import java.util.Date;

/**
 * Created by mason on 7/22/14.
 */
public interface RememberMeToken {

    public Long getId();

    public String getTokenValue();

    public void setTokenValue(String tokenValue);

    public Date getTokenDate();

    public void setTokenDate(Date tokenDate);

    public String getIpAddress();

    public void setIpAddress(String ipAddress);

    public String getUserAgent();

    public void setUserAgent(String userAgent);

    void setUser(User user);

    User getUser();
}
