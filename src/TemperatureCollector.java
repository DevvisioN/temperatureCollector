import org.kaaproject.kaa.client.DesktopKaaPlatformContext;
import org.kaaproject.kaa.client.Kaa;
import org.kaaproject.kaa.client.KaaClient;
import org.kaaproject.kaa.client.SimpleKaaClientStateListener;
import org.kaaproject.kaa.client.configuration.base.SimpleConfigurationStorage;
import org.kaaproject.kaa.client.exceptions.KaaException;
import org.kaaproject.kaa.client.logging.strategies.RecordCountLogUploadStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class TemperatureCollector{
    private static final long DEFAULT_START_DELAY = 1000L;
    private static final Logger LOG = LoggerFactory.getLogger(TemperatureCollector.class);
    private static KaaClient kaaClient;
    private static ScheduledFuture<?> scheduledFuture;
    private static ScheduledExecutorService scheduledExecutorService;

    public static void main(String[] args){
        LOG.info(TemperatureCollector.class.getSimpleName()+" app successfully starting");
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        DesktopKaaPlatformContext desktopKaaPlatformContext = new DesktopKaaPlatformContext();
        kaaClient = Kaa.newClient(desktopKaaPlatformContext, new TemperatureCollectorStateListener(), true);
        RecordCountLogUploadStrategy strategy = new RecordCountLogUploadStrategy(1);
        strategy.setMaxParallelUploads(1);
        kaaClient.setLogUploadStrategy(strategy);
        kaaClient.setConfigurationStorage(new SimpleConfigurationStorage(desktopKaaPlatformContext,"configuration/config.cfg"));
        kaaClient.addConfigurationListener(configuration -> {
            //TODO methods to several changes in received configuration data
            if(configuration.getType()!=null){
                LOG.warn("new type: {}", configuration.getType());
            }
            if (configuration.getUpdatePeriod()!=null){
                LOG.warn("new update period:{}", configuration.getUpdatePeriod());
            }
            if(!configuration.getSwitcher()){
                LOG.warn("application is paused");
            }
        });
        kaaClient.start();
    }

    private static int getTemperature(){
        return new Random().nextInt(10)+25;
    }
    private static void onKaaStarted(){
    }
    private static void onChangedTemperatureType(){
    }
    private static void onChangedUpdatePeriod(){
    }
    private static void onChangedDataSwitcher(){
    }


    private static class TemperatureCollectorStateListener extends SimpleKaaClientStateListener{
        @Override
        public void onStarted() {
            super.onStarted();
        }

        @Override
        public void onStartFailure(KaaException exception) {
            super.onStartFailure(exception);
        }

        @Override
        public void onPaused() {
            super.onPaused();
        }

        @Override
        public void onPauseFailure(KaaException exception) {
            super.onPauseFailure(exception);
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onResumeFailure(KaaException exception) {
            super.onResumeFailure(exception);
        }

        @Override
        public void onStopped() {
            super.onStopped();
        }

        @Override
        public void onStopFailure(KaaException exception) {
            super.onStopFailure(exception);
        }
    }
}