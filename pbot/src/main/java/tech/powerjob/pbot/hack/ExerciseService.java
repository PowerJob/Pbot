package tech.powerjob.pbot.hack;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * Exercise for oracle cloud service
 *
 * @author tjq
 * @since 2023/3/13
 */
@Slf4j
@Service
public class ExerciseService {

    @Value("${powerjob.exercise.enable:false}")
    private boolean enableExercise;

    private static final ExecutorService EXERCISE_POOL = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(),
            0, TimeUnit.DAYS,
            new ArrayBlockingQueue<>(16),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Scheduled(fixedDelay = 600000)
    public void exercise() {

        if (!enableExercise) {
            return;
        }

        double percent = ThreadLocalRandom.current().nextDouble(0.2, 0.4);
        int seconds = ThreadLocalRandom.current().nextInt(30, 90);

        useCpu(percent, seconds);

        try {
            Thread.sleep(seconds + ThreadLocalRandom.current().nextInt(20000));
        } catch (Exception ignore) {
        }
    }


    @SneakyThrows
    private static void useCpu(double percent, int seconds) {

        if (percent > 0.9) {
            throw new IllegalArgumentException("percent need <= 0.9");
        }

        long startTs = System.currentTimeMillis();

        int busyTime = (int) (10 * percent);
        int idleTime = (int) (10 * (1 - percent));

        int cores = Runtime.getRuntime().availableProcessors();

        log.info("[ExerciseService] percent: {}, seconds: {}, cores: {}", percent, seconds, cores);

        for (int i = 0; i < cores; i++) {
            EXERCISE_POOL.submit(() -> {
                while(true){
                    long startTime = System.currentTimeMillis();
                    //busy loop:
                    while((System.currentTimeMillis()-startTime)<=busyTime) {

                        long offset = System.currentTimeMillis() - startTs;
                        if (offset > seconds * 1000L) {
                            return;
                        }

                    }
                    try {
                        Thread.sleep(idleTime);
                    } catch (Exception ignore) {
                    }
                }
            });
        }
    }
}
