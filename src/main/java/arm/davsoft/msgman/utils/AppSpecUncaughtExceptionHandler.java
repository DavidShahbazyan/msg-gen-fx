package arm.davsoft.msgman.utils;

import arm.davsoft.msgman.Main;
import arm.davsoft.msgman.enums.ErrorCode;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 8/17/15 <br/>
 * <b>Time:</b> 6:41 PM <br/>
 */
public class AppSpecUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    /**
     * Method invoked when the given thread terminates due to the
     * given uncaught exception.
     * <p>Any exception thrown by this method will be ignored by the
     * Java Virtual Machine.
     *
     * @param t the thread
     * @param e the exception
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        String content = ErrorCode.UNKNOWN_EXCEPTION.getCode() + " - " + ErrorCode.UNKNOWN_EXCEPTION.getDescription();
        Dialogs.showErrorDialog(null, content);
//        Logger.getLogger(AppSpecUncaughtExceptionHandler.class).error("", e);
        Main.LOGGER.error(content, e);
    }
}
