package org.jailbreak.service.db;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

@BindingAnnotation(BindIds.IntArrayBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BindIds {
	public static class IntArrayBinderFactory implements BinderFactory {
		
		@SuppressWarnings("rawtypes")
		public Binder build(Annotation annotation) {
			return new Binder<BindIds, List<Integer>>() {
				public void bind(SQLStatement<?> q, BindIds bind, List<Integer> ids) {
					Array idsArray;
					try {
						// it is extremely important that we bind in 
						// java.sql.Array objects and nothing else
						// otherwise the postgresql server will return
						// a binding error
						// http://skife.org/jdbi/java/2011/12/21/jdbi_in_clauses.html#comment-548204288
						//
						// must use java.sql.Array: http://postgresql.nabble.com/Array-support-in-8-1-JDBC-driver-tt2170272.html#a2170273
						idsArray = q.getContext()
						         .getConnection()
						         .createArrayOf("integer", ids.toArray());
						q.bindBySqlType("idList", idsArray, Types.ARRAY);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			};
		}
	}
}


