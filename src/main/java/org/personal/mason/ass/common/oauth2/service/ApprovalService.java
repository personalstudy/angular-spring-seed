package org.personal.mason.ass.common.oauth2.service;

import org.personal.mason.ass.common.oauth2.model.OauthApproval;

import java.util.List;

/**
 * Created by mason on 8/3/14.
 */
public interface ApprovalService<T extends OauthApproval> {

    T save(T entity);

    void delete(Iterable<? extends T> entities);

    T saveAndFlush(T entity);

    T newInstance();

    List<T> findByUserIdAndClientId(String user, String client);

    List<T> findByUserIdAndClientIdAndScope(String userId, String clientId, String scope);
}
