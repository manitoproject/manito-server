package manito.server.util;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum TimeoutType {
    MS_1000(1000),
    MS_2000(2000),
    MS_3000(3000),
    MS_5000(5000),
    MS_10000(10000);

    private int timeout;

    TimeoutType(int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    private static final Map<Integer, TimeoutType> map =
            Collections.unmodifiableMap(
                    Stream.of(TimeoutType.values()).collect(Collectors.toMap(TimeoutType::getTimeout, Function.identity())));

    public static TimeoutType find(String str) {
        return Optional.ofNullable(map.get(str)).orElseThrow(() -> new RuntimeException("invalid timeout"));
    }
}

