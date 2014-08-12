package org.personal.mason.ass.common.jpa;

import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.OrderBy;
import javax.persistence.criteria.Path;
import javax.persistence.metamodel.Attribute;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Created by mason on 8/9/14.
 */
public class SearchParameters {

    private boolean distinct;
    private int first;
    private int maxResults;
    private int pageSize;
    private String[] orders;

    public boolean getDistinct() {
        return distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getFirst() {
        return first;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String[] getOrders() {
        return orders;
    }
}
