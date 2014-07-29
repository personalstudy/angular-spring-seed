package org.personal.mason.ass.domain.model.rememberme;

import org.personal.mason.ass.common.auditing.model.Auditing;
import org.personal.mason.ass.common.authority.model.User;
import org.personal.mason.ass.common.rememberme.model.RememberMeToken;
import org.personal.mason.ass.domain.model.authentication.Account;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mason on 7/27/14.
 */
@Entity
@Table(name = "ass_persist_token")
public class APersistToken extends Auditing<Account, Long> implements RememberMeToken {

    @Column(name = "token_value")
    private String tokenValue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "token_date")
    private Date tokenDate;

    @Column(name = "ip_address", length = 39)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Override
    public String getTokenValue() {
        return this.tokenValue;
    }

    @Override
    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @Override
    public Date getTokenDate() {
        return this.tokenDate;
    }

    @Override
    public void setTokenDate(Date tokenDate) {
        this.tokenDate = tokenDate;
    }

    @Override
    public String getIpAddress() {
        return this.ipAddress;
    }

    @Override
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String getUserAgent() {
        return this.userAgent;
    }

    @Override
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public void setUser(User user) {
        if(user instanceof Account){
            this.account = (Account) user;
        }
    }

    @Override
    public User getUser() {
        return this.account;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
