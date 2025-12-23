package org.example.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.spi.ContextAwareBase;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ConsoleEncodingConfigurator extends ContextAwareBase implements Configurator {
    @Override
    public ExecutionStatus configure(LoggerContext lc) {
        // 1. Получаем кодировку консоли. Если консоли нет (запуск в IDE), берем UTF-8.
        Charset consoleCharset = (System.console() != null)
                ? System.console().charset()
                : StandardCharsets.UTF_8;

        // 2. Настраиваем Encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(lc);
        encoder.setPattern("%msg%n");
        encoder.setCharset(consoleCharset);
        encoder.start();

        // 3. Настраиваем Appender
        ConsoleAppender ca = new ConsoleAppender();
        ca.setContext(lc);
        ca.setEncoder(encoder);
        ca.start();

        lc.getLogger("ROOT").addAppender(ca);

        // Устанавливаем общий уровень ERROR для всей системы (чтобы сторонние библиотеки не спамили)
        lc.getLogger("ROOT").setLevel(ch.qos.logback.classic.Level.ERROR);
        lc.getLogger("ROOT").addAppender(ca);

        // Устанавливаем уровень INFO только для вашего пакета
        // Замените "com.your.package" на корень вашего проекта
        ch.qos.logback.classic.Logger myLogger = lc.getLogger("org.example");
        myLogger.setLevel(ch.qos.logback.classic.Level.INFO);

        // Важно: если вы хотите, чтобы логи вашего пакета шли только через этот аппендер
        // и не дублировались (в данном случае это не критично для ROOT)
        myLogger.setAdditive(true);

        return ExecutionStatus.DO_NOT_INVOKE_NEXT_IF_ANY;
    }
}
