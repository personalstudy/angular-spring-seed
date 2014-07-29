package org.personal.mason.ass.common.authority;

import org.personal.mason.ass.common.authority.model.User;

/**
 * Created by mason on 6/30/14.
 */
public interface JpaUserDetailsDefaults {

    void initialSettings(User model);

    public class NullJpaUserDetailsDefaults implements JpaUserDetailsDefaults{

        @Override
        public void initialSettings(User model) {

        }
    }
}
