package org.example.util;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Supplier;

public class SafeScanner {
    private record Data<T extends Number>(
            T MINVALUE,
            T MAXVALUE,
            Supplier<Boolean> hasValue,
            Supplier<? extends Number> nextValue) {
    }

    private final Scanner scanner;
    private final Map<Class<? extends Number>, Data<?>> dataMap;

    public SafeScanner(Scanner scanner){
        this.scanner = scanner;

        dataMap = Map.of(
                Integer.class, new Data<>(Integer.MIN_VALUE, Integer.MAX_VALUE, scanner::hasNextInt, scanner::nextInt),
                Long.class, new Data<>(Long.MIN_VALUE, Long.MAX_VALUE, scanner::hasNextLong, scanner::nextLong)
        );
    }

    public <T extends Number & Comparable<T>> T nextNumber(Class<T> type){
        return nextNumber(type, type.cast(dataMap.get(type).MINVALUE()), type.cast(dataMap.get(type).MAXVALUE()));
    }
    public <T extends Number & Comparable<T>> T nextNumber(Class<T> type, T lowerBarrier, T upperBarrier){
        T value;
        while(true){
            if(dataMap.get(type).hasValue().get()){
                value = type.cast(dataMap.get(type).nextValue().get());
                scanner.nextLine();

                if(value.compareTo(lowerBarrier) >= 0 && value.compareTo(upperBarrier) <= 0){
                    return value;
                }
            }
            else {
                scanner.nextLine();
            }

            System.out.printf("Ошибка! Введите целое число в пределах (%s <= X <= %s): ", lowerBarrier.toString(), upperBarrier.toString());
        }
    }
}
