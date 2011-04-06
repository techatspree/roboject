package de.akquinet.android.roboject.injectors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import de.akquinet.android.roboject.Container;
import de.akquinet.android.roboject.RobojectActivity;
import de.akquinet.android.roboject.RobojectException;
import de.akquinet.android.roboject.annotations.Inject;
import de.akquinet.android.roboject.util.ReflectionUtil;

public class ServiceInjector implements Injector {
	private Context context;
	private Object managed;

	/**
	 * Method called by the container to initialize the container.
	 * 
	 * @param context
	 *            the android context
	 * @param container
	 *            the roboject container
	 * @param managed
	 *            the managed instance
	 * @param clazz
	 *            the managed class (the class of <tt>managed</tt>)
	 * @return <code>true</code> if the injector wants to contribute to the
	 *         management of the instance, <code>false</code> otherwise. In this
	 *         latter case, the injector will be ignored for this instance.
	 * @throws RobojectException
	 *             if the configuration failed.
	 */
	@Override
	public boolean configure(Context context, Container container,
			Object managed, Class<?> clazz) throws RobojectException {
		this.context = context;
		this.managed = managed;
		return true;
	}

	/**
	 * Method called by the container when all injectors are configured
	 * (immediately after configure). This method is called on valid injector
	 * only. In this method, the injector can injects field and call callbacks
	 * (however, callbacks may wait the validate call).
	 * 
	 * @param context
	 *            the android context
	 * @param container
	 *            the roboject container
	 * @param managed
	 *            the managed instance
	 */

	@Override
	public void start(Context context, Container container, Object managed) {
		List<Field> fields = ReflectionUtil.getFields(managed.getClass());
		for (Field field : fields) {
			Annotation[] annotations = field.getAnnotations();
			for (Annotation annotation : annotations) {
				if (annotation instanceof Inject) {
					injectService(field, (Inject) annotation);
				}
			}
		}
	}

	/**
	 * Method called by the container when the container is disposed. This
	 * method is called on valid injector only. In this method, the injector can
	 * free resources
	 * 
	 * @param context
	 *            the android context
	 * @param managed
	 *            the managed instance
	 */
	@Override
	public void stop(Context context, Object managed) {
		this.managed = null;
	}

	/**
	 * Checks whether the injector is valid or not.
	 * 
	 * @return <code>true</code> if the injector is valid (ready),
	 *         <code>false</code> otherwise.
	 */
	@Override
	public boolean isValid() {
		return this.managed != null;
	}

	/**
	 * Callback called by the container when all injectors are valid.
	 */
	@Override
	public void validate() {
	}

	/**
	 * Callback called by the container when at least one injector becomes
	 * invalid.
	 */
	@Override
	public void invalidate() {
	}

	private void injectService(final Field field, Inject annotation) {
		Class<? extends Service> androidServiceClass = annotation.value();
		Intent intent = new Intent(context, androidServiceClass);
		context.bindService(intent, new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				if (field.getType().isAssignableFrom(service.getClass())) {
					try {
						field.setAccessible(true);
						field.set(managed, service);
					} catch (Exception e) {
						// TODO: better error message
						throw new RuntimeException("Unable to inject service",
								e);
					}
				}
				if (managed instanceof RobojectActivity) {
					((RobojectActivity) managed).onServiceConnected(name,
							service);
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				try {
					field.set(managed, null);
				} catch (IllegalArgumentException e) {
				} catch (IllegalAccessException e) {
				}
				ServiceInjector.this.managed = null;
			}
		}, Service.BIND_AUTO_CREATE);
	}
}
