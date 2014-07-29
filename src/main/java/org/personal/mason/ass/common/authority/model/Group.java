package org.personal.mason.ass.common.authority.model;

import java.util.Collection;

/**
 * Created by mason on 7/20/14.
 */
public interface Group {
    boolean isEnabled();

    Collection<? extends Authority> getRoles();
}
