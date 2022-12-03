package org.polytech.covidapi.Metrics;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;

@Component
@Aspect
public class MetricsAppointmentAspects {

    private final MeterRegistry registry;

    @Autowired
    public MetricsAppointmentAspects(MeterRegistry registry) {
        this.registry = registry;
    }

    @AfterReturning("execution(public * org.polytech.covidapi.controllers.AppointmentController.registerAppointment())")
    public void succes(JoinPoint joinPoint) {
        Tag tag = Tag.of("covidapi-impl", joinPoint.getTarget().getClass().getSimpleName());
        registry.counter("appointments-registered", List.of(tag)).increment();
    }

    @Around("execution(public * org.polytech.covidapi.controllers.AppointmentController.registerAppointment())")
    public Object duration(ProceedingJoinPoint joinPoint)
            throws Throwable {
        Tag tag = Tag.of("covidapi-impl", joinPoint.getTarget().getClass().getSimpleName());
        Timer timer = registry.timer("appointment-registration-duration", List.of(tag));
        Instant startTime = Instant.now();
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } finally {
            timer.record(Duration.between(startTime, Instant.now()));
        }

    } 

}
