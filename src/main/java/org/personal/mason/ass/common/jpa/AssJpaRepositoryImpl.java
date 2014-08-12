package org.personal.mason.ass.common.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.SingularAttribute;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by mason on 7/27/14.
 */

@NoRepositoryBean
public class AssJpaRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements AssJpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    private static final Logger logger = LoggerFactory.getLogger(AssJpaRepositoryImpl.class);
    private final JpaEntityInformation<T, ?> entityInformation;
    private final EntityManager entityManager;

    public AssJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
    }

    public AssJpaRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityInformation = JpaEntityInformationSupport.getMetadata(domainClass, em);
        this.entityManager = em;
    }

    @Override
    public T newInstance() {
        try {
            return getDomainClass().newInstance();
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        return null;
    }

    @Override
    public List<T> findByExample(T example) {
        return findByExample(example, (Sort)null);
    }

    @Override
    public List<T> findByExample(T example, Sort sort) {
        Specification<T> spec = new AssJpaSpecification<T>(example);
        return getQuery(spec, sort).getResultList();
    }

    @Override
    public Page<T> findByExample(T example, Pageable pageable) {
        Specification<T> spec = new AssJpaSpecification<T>(example);
        TypedQuery<T> query = getQuery(spec, pageable);
        return pageable == null ? new PageImpl<T>(query.getResultList()) : readPage(query, pageable, spec);
    }

    @Override
    public long countByExample(T example) {
        Specification<T> spec = new AssJpaSpecification<T>(example);
        return count(spec);
    }

    private final class AssJpaSpecification<T> implements Specification<T>{

        protected final List<Predicate> expressions = Collections.synchronizedList(new ArrayList<Predicate>());
        private T example;

        public AssJpaSpecification(T example) {
            this(example, Junction.UNION);
        }

        public AssJpaSpecification(T example, Junction junction) {
            this.example = example;
        }

        @Override
        public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
            if(example == null){
                return null;
            }

            ManagedType<T> managedType = entityManager.getMetamodel().managedType((Class<T>) example.getClass());
            ComparisonStyle style = new ComparisonStyle.Default();
            CompareByExample(cb, managedType, root, example, style);

            if (expressions.size() == 0) {
                logger.warn("query by example running with no criteria");
            }

            return cb.and(expressions.toArray(new Predicate[expressions.size()]));
        }

        private <X> void CompareByExample(CriteriaBuilder builder, ManagedType<X> type,
                         Path<X> from, X instance, ComparisonStyle style,
                         Attribute<?, ?>... excludes) {
            List<Attribute<?, ?>> excludeAttr = excludes == null
                    ? new ArrayList<Attribute<?,?>>() : Arrays.asList(excludes);

            Set<SingularAttribute<? super X, ?>> attrs = type.getSingularAttributes();
            for (SingularAttribute<? super X, ?> attr : attrs) {
                if (excludeAttr.contains(attr)
                        || (style.excludeIdentity() && attr.isId())
                        || (style.excludeVersion() && attr.isVersion())) {
                    continue;
                }

                Object value = extractValue(instance, attr);
                if ((style.excludeNull() && value == null)
                        || (style.excludeDefault() && isDefaultValue(attr.getJavaType(), value)))
                    continue;

                Predicate p = null;
                if (value == null) {
                    p = from.get(attr).isNull();
                    expressions.add(p);
                    continue;
                }
                if (attr.isAssociation()) {
                    ManagedType<X> declaringType = (ManagedType<X>) attr.getDeclaringType();
                    Path<X> path = (Path<X>)from.get(attr);
                    X v = (X)value;
                    CompareByExample(builder, declaringType, path, v, style, excludes);
                } else if (attr.getJavaType() == String.class) {
                    Expression<String> s = from.get(attr).as(String.class);
                    switch (style.getStringComparsionMode()) {
                        case EXACT : p = builder.equal(s, value);
                            break;
                        case CASE_INSENSITIVE : p = builder.equal(builder.upper(s), value.toString());
                            break;
                        case LIKE: p = builder.like(s, value.toString());
                            break;
                    }
                } else {
                    p = builder.equal(from.get(attr), value);
                }
                expressions.add(p);
            }
        }

        private void loop(From from, CriteriaBuilder cb, List<Predicate> criteria, Class<?> aClass) {
            Field[] fields = getAllFields(aClass);
            for (Field field : fields){
                if(Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())){
                    continue;
                }
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(example);
                } catch (IllegalAccessException e){
                    Object[] args = {field.getName(), example.getClass(), e};
                    logger.debug("FAILED TO ACCESS FIELD [%s] ON CLASS [%s]. Cause: %s", args);
                }

                if(value == null){
                    continue;
                }

                if(value instanceof Collection){
                    continue;
                }

                criteria.add(
                   cb.equal(from.get(field.getName()), value)
                );

                if(field.getType().isAssignableFrom(Persistable.class)){
                    Join join = from.join(field.getName());
                    loop(join, cb, criteria, field.getType());
                }



                if(field.getType().isAssignableFrom(Collection.class)){
                    CollectionJoin collectionJoin = from.joinCollection(field.getName());
//                    loop(collectionJoin, cb, criteria, field.getGenericType());
                }
            }
        }

        private String buildLikeValue(String value) {
            if(value == null || value.isEmpty()){
                return "%";
            }
            value = value.trim();
            value = value.replaceAll("%", "");
            return value.replace('~', '%');
        }

        private Field[] getAllFields(Class clazz){
            if(clazz == null){
                return null;
            }

            List<Field> list = new ArrayList<>();
            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
            while (clazz != Object.class){
                clazz = clazz.getSuperclass();
                list.addAll(Arrays.asList(clazz.getDeclaredFields()));
            }

            return list.toArray(new Field[list.size()]);
        }

        <X> Object extractValue(X instance, SingularAttribute<? super X, ?> attr) {
            Class<?> cls = instance.getClass();
            Field[] fields = getAllFields(cls);
            for (Field field : fields){
                if(!field.getName().equals(attr.getName())){
                    continue;
                }
                if(Modifier.isFinal(field.getModifiers()) || Modifier.isStatic(field.getModifiers())){
                    continue;
                }
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(example);
                    return value;
                } catch (IllegalAccessException e){
                    Object[] args = {field.getName(), example.getClass(), e};
                    logger.debug("FAILED TO ACCESS FIELD [%s] ON CLASS [%s]. Cause: %s", args);
                }
            }
            return null;
        }

        boolean isDefaultValue(Class<?> cls, Object val) {
            if (val == null) {
                return true;
            }
            if (cls == Boolean.class || cls == boolean.class) {
                return Boolean.FALSE.equals(val);
            } else if (cls == Character.class || cls == char.class) {
                return ((Character) val).charValue() == 0;
            } else if (cls == Byte.class || cls == byte.class
                    || cls == Double.class || cls == double.class
                    || cls == Float.class || cls == float.class
                    || cls == Long.class || cls == long.class
                    || cls == Integer.class || cls == int.class
                    || cls == Short.class || cls == short.class) {
                return ((Number) val).intValue() == 0;
            } else if (cls == String.class) {
                return "".equals(val);
            } else {
                return false;
            }
        }
    }
}

enum Junction{
    UNION, INTERSECTION;
}































