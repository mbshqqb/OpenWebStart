package com.openwebstart.launcher;

import net.adoptopenjdk.icedteaweb.Assert;
import net.adoptopenjdk.icedteaweb.launch.JvmLauncher;
import net.adoptopenjdk.icedteaweb.logging.Logger;
import net.adoptopenjdk.icedteaweb.logging.LoggerFactory;
import net.sourceforge.jnlp.runtime.Boot;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.stream.IntStream;

public class JnlpApplicationLauncher {

    private static final Logger LOG = LoggerFactory.getLogger(JnlpApplicationLauncher.class);

    private final ThreadGroup applicationThreadGroup;

    private final JvmLauncher jvmLauncher;

    private final Set<String> usedThreadNames;

    public JnlpApplicationLauncher(final JvmLauncher jvmLauncher) {
        this.jvmLauncher = Assert.requireNonNull(jvmLauncher, "jvmLauncher");
        this.applicationThreadGroup = new ThreadGroup(Thread.currentThread().getThreadGroup(), "JNLP Application Threads");
        usedThreadNames = new HashSet<>();
    }

    public void launch(final String appName, final String[] args) {
        final Executor applicationExecutor = createApplicationExecutor(appName);
        applicationExecutor.execute(() -> {
            try {
                LOG.info("Starting application '{}'", appName);
                LOG.debug("Application thread '{}'", Thread.currentThread().getName());
                LOG.debug("ITW Boot called with custom args {}.", Arrays.toString(args));
                Boot.main(jvmLauncher, args);
            } catch (final Exception e) {
                LOG.error("Error in execution of application '" + appName + "'", e);
            } finally {
                LOG.info("Application '{}' closed", appName);
            }
        });
    }

    private Executor createApplicationExecutor(final String appName) {
        return r -> {
            final Thread appThread = new Thread(applicationThreadGroup, r);
            appThread.setName(createUniqueThreadName(appName));
            appThread.setDaemon(false);
            appThread.start();
        };
    }

    private synchronized String createUniqueThreadName(final String appName) {
        final String prefix = "Application thread for ";
        final String bestName = prefix + "'" + appName + "'";
        if(!usedThreadNames.contains(bestName)) {
            usedThreadNames.add(bestName);
            return bestName;
        }
        final int threadIndex = IntStream.range(1, 1000)
                .filter(i -> !usedThreadNames.contains(bestName + "(" + i + ")"))
                .findFirst()
                .orElse(-1);
        if(threadIndex != -1) {
            final String threadName = bestName + "(" + threadIndex + ")";
            usedThreadNames.add(threadName);
            return threadName;
        }
        return bestName + "(" + UUID.randomUUID().toString() + ")";
    }
}
