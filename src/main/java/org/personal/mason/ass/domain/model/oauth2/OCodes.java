package org.personal.mason.ass.domain.model.oauth2;

import org.personal.mason.ass.common.oauth2.model.OauthCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by mason on 8/3/14.
 */
@Entity
@Table(name = "ass_oauth2_code")
public class OCodes extends AbstractPersistable<Long> implements OauthCode{
    private static final long serialVersionUID = 880869016537752129L;

    @Lob
    @Column(name = "authentication")
    private byte[] authentication;

    @Column(name = "code")
    private String code;

    @Override
    public byte[] getAuthentication() {
        return this.authentication;
    }

    @Override
    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
