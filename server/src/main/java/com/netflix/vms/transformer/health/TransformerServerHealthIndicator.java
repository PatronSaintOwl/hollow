package com.netflix.vms.transformer.health;

import com.google.inject.Singleton;
import com.netflix.runtime.health.api.Health;
import com.netflix.runtime.health.api.HealthIndicator;
import com.netflix.runtime.health.api.HealthIndicatorCallback;
import com.netflix.util.Pair;
import com.netflix.vms.transformer.common.TransformerHealthIndicator;

/**
 * Health indicators notify the health status subsystem if there is an issue with the service.
 * The service instance will periodically update its health status with the discovery service and in
 * essense take itself out of the pool of instances that can serve requests.
 *
 * Your health indicator can grab state from anywhere in your service and determine if it's
 * {@link Health#unhealthy()} or {@link Health#healthy()}.
 *
 * You may have more than one health indicators registered. All registered indicators are composed
 * such that if at least one is "unhealthy" the instance health status will be "unhealthy".
 *
 * You can register them in {@link com.netflix.runtime.health.guice.HealthModule} via:
 * <pre>
 * <code>
 *
 * {@literal @}Override
 *  protected void configureHealth() {
 *      bindAdditionalHealthIndicator().to(CustomHealthIndicator.class);
 *      // Add extra indicator here.
 *      bindAdditionalHealthIndicator().to(AnotherHealthIndicator.class);
 *  }
 * </code>
 * </pre>
 *
 * @author This file is auto-generated by runtime@netflix.com. Feel free to modify.
 */
@Singleton
public class TransformerServerHealthIndicator implements HealthIndicator, TransformerHealthIndicator {
    public static final String TRANSFORMER_STATUS_STRING = "transformerStatus";

    public static enum Status {
        STARTING, // server is up and the first cycle is running
        STARTED_FAILING_CYCLE, // server has finished one or more cycles, none successfully
        STARTED_SUCCESSFUL // server has had a successful cycle
    }

    private Status startupStatus = Status.STARTING;
    private Health healthStatus = Health.unhealthy().withDetail(TRANSFORMER_STATUS_STRING, startupStatus.name()).build();

    @Override
    public void cycleSucessful() {
        setStartupStatus(Status.STARTED_SUCCESSFUL, null);
    }

    @Override
    public void cycleFailed(Throwable th) {
        if (startupStatus == Status.STARTING) {
            setStartupStatus(Status.STARTED_FAILING_CYCLE, th);
        }
    }

    @Override
    public boolean isHealthy() {
        return healthStatus.isHealthy();
    }

    @Override
    public void check(HealthIndicatorCallback healthCallback) {
        healthCallback.inform(healthStatus);
    }

    public synchronized Pair<Health, Status> getHealthStatus() {
        return new Pair<Health, Status>(healthStatus, startupStatus);
    }

    public synchronized void setStartupStatus(Status startupStatus, Throwable th) {
        this.startupStatus = startupStatus;
        if (startupStatus == Status.STARTED_SUCCESSFUL) {
            healthStatus = Health.healthy().withDetail(TRANSFORMER_STATUS_STRING, startupStatus.name()).build();
        } else {
            healthStatus = th == null ? Health.unhealthy().withDetail(TRANSFORMER_STATUS_STRING, startupStatus.name()).build() : Health.unhealthy(th)
                    .withDetail(TRANSFORMER_STATUS_STRING, startupStatus.name()).build();
        }
    }
}
