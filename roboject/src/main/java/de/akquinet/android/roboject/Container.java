package de.akquinet.android.roboject;

import java.util.List;

import android.content.Context;
import de.akquinet.android.roboject.injectors.Injector;
import de.akquinet.android.roboject.injectors.Injector.InjectorState;


public class Container
{
    public enum LifeCyclePhase
    {
        CREATE, RESUME, SERVICES_CONNECTED, READY;
    }

    private LifeCyclePhase currentPhase;

    private List<Injector> injectors;
    private Object managed;

    public Container(Context context, Object managed, Class<?> clazz) throws RobojectException {
        this.managed = managed;
        this.currentPhase = LifeCyclePhase.CREATE;
        this.injectors = RobojectConfiguration.getDefaultInjectors(managed);

        for (Injector injector : this.injectors) {
            injector.configure(context, this, managed, clazz);
        }

        for (Injector injector : this.injectors) {
            if (injector.isValid()) {
                injector.start(context, this, managed);
            }
        }

        for (Injector injector : this.injectors) {
            if (InjectorState.READY != injector.getState()) {
                return;
            }
        }

        invokeReadyPhase();
    }

    public void update() {
        if (allInjectorsReady()) {
            invokeReadyPhase();
        }
    }

    protected void invokeResumePhase() {
        this.currentPhase = LifeCyclePhase.RESUME;
    }

    protected void invokeReadyPhase() {
        this.currentPhase = LifeCyclePhase.READY;
        if (managed instanceof RobojectLifecycle) {
            ((RobojectLifecycle) managed).onReady();
        }
    }

    private boolean allInjectorsReady() {
        for (Injector injector : injectors) {
            if (!InjectorState.READY.equals(injector.getState())) {
                return false;
            }
        }

        return true;
    }

    public LifeCyclePhase getCurrentLifecyclePhase() {
        return this.currentPhase;
    }
}
