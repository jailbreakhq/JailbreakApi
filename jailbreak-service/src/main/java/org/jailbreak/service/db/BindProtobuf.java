package org.jailbreak.service.db;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor.Type;


@BindingAnnotation(BindProtobuf.ProtobufBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BindProtobuf {
	public static class ProtobufBinderFactory implements BinderFactory {
		
		private final Logger LOG = LoggerFactory.getLogger(ProtobufBinderFactory.class);
		
		public Binder build(Annotation annotation) {
			return new Binder<BindProtobuf, com.google.protobuf.GeneratedMessage>() {
				public void bind(SQLStatement q, BindProtobuf bind, com.google.protobuf.GeneratedMessage arg) {
					List<FieldDescriptor> descriptors = arg.getDescriptorForType().getFields();
					Object value;
					for(FieldDescriptor descriptor : descriptors) {
						if (descriptor.getType() == Type.ENUM) {
							value = ((EnumValueDescriptor)arg.getField(descriptor)).getIndex();
						} else {
							value = arg.getField(descriptor);
						}
						
						LOG.debug("Binding " + descriptor.getName() + " to " + value);
						
						q.bind(descriptor.getName(), value);
					}
				}
			};
		}
	}
}


