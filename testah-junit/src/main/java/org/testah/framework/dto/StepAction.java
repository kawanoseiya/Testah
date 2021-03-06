package org.testah.framework.dto;

import org.testah.TS;
import org.testah.client.dto.StepActionDto;
import org.testah.client.enums.TestStepActionType;

/**
 * The Class StepAction.
 *
 * @deprecated Use TS.step().action() instead
 */
@Deprecated
public class StepAction extends StepActionDto {

    /**
     * Creates the.
     *
     * @return the step action
     */
    public static StepAction create() {
        return new StepAction();
    }

    /**
     * Add step action dto.
     *
     * @param stepAction the step action
     * @return the step action dto
     */
    public static StepActionDto add(final StepActionDto stepAction) {
        return add(stepAction, true);
    }

    /**
     * Add step action dto.
     *
     * @param stepAction the step action
     * @param writeToLog the write to log
     * @return the step action dto
     */
    public static StepActionDto add(final StepActionDto stepAction, final boolean writeToLog) {
        if (null != TS.params() && TS.params().isRecordSteps()) {
            TS.step().action().add(stepAction);
            if (writeToLog) {
                stepAction.log();
            }
        }
        return stepAction;
    }

    /**
     * Adds the.
     *
     * @param stepAction the step action
     * @return the step action dto
     */
    public static StepActionDto add(final StepAction stepAction) {
        add((StepActionDto) stepAction, true);
        return stepAction;
    }

    /**
     * Adds the.
     *
     * @return the step action dto
     */
    public StepActionDto add() {
        return add(true);
    }

    /**
     * Add step action dto.
     *
     * @param writeToLog the write to log
     * @return the step action dto
     */
    public StepActionDto add(final boolean writeToLog) {
        return add(this, writeToLog);
    }

    /**
     * Creates the assert result.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @return the step action
     */
    public static StepAction createAssertResult(final String message, final Boolean status, final String assertMethod,
                                                final Object expected, final Object actual, final Throwable exception) {
        final StepAction step = new StepAction();
        step.setActionName(assertMethod);
        step.setMessage1(message);
        step.setStatus(status);
        step.setMessage2(String.valueOf(expected));
        step.setMessage3(String.valueOf(actual));
        step.setException(exception);
        step.setTestStepActionType(TestStepActionType.ASSERT);
        if (TS.isBrowser() && !status) {
            step.setSnapShotPath(TS.browser().takeScreenShot());
            step.setHtmlSnapShotPath(TS.browser().takeHtmlSnapshot());
        }
        TS.log().debug(String.format("%s [%s] - %s - %s%nexpected%n[%s]%nactual%n[%s]",
                TestStepActionType.ASSERT, assertMethod, status, message, expected, actual));
        if (null != step.getExceptionString()) {
            TS.log().trace(String.format("Exception Related to above Assert%n%s", step.getExceptionString()));
        }
        return step;
    }

    /**
     * Creates the verify result.
     *
     * @param message      the message
     * @param status       the status
     * @param assertMethod the assert method
     * @param expected     the expected
     * @param actual       the actual
     * @param exception    the exception
     * @return the step action
     */
    public static StepAction createVerifyResult(final String message, final Boolean status, final String assertMethod,
                                                final Object expected, final Object actual, final Throwable exception) {
        final StepAction step = new StepAction();
        step.setActionName(assertMethod);
        step.setMessage1(message + " - " + status);
        step.setStatus(null);
        step.setMessage2(String.valueOf(expected));
        step.setMessage3(String.valueOf(actual));
        step.setException(null);
        step.setTestStepActionType(TestStepActionType.VERIFY);

        TS.log().debug(String.format("%s [%s] - %s - %s%nexpected%n[%s]%nactual%n[%s]",
                TestStepActionType.VERIFY, assertMethod, status, message, expected, actual));
        return step;
    }

    /**
     * Creates the info.
     *
     * @param message1 the message1
     * @return the step action
     */
    public static StepAction createInfo(final String message1) {
        return createInfo(message1, "", "", true);
    }

    /**
     * Creates the info.
     *
     * @param message1 the message1
     * @param message2 the message2
     * @param message3 the message3
     * @param autoLog  the auto log
     * @return the step action
     */
    public static StepAction createInfo(final String message1, final String message2, final String message3,
                                        final boolean autoLog) {
        return createInfo(message1, message2, message3, autoLog, false);
    }

    /**
     * Create info step action.
     *
     * @param message1     the message 1
     * @param message2     the message 2
     * @param message3     the message 3
     * @param autoLog      the auto log
     * @param takeSnapShot the take snap shot
     * @return the step action
     */
    public static StepAction createInfo(final String message1, final String message2, final String message3,
                                        final boolean autoLog, final boolean takeSnapShot) {
        final StepAction step = new StepAction();
        step.setMessage1(message1);
        step.setMessage2(message2);
        step.setMessage3(message3);
        step.setTestStepActionType(TestStepActionType.INFO);
        if (autoLog) {
            TS.log().debug(TestStepActionType.INFO + " - " + step.getMessage1() + " - " + step.getMessage2());
        }
        if (takeSnapShot) {
            if (TS.isBrowser()) {
                step.setSnapShotPath(TS.browser().takeScreenShot());
                step.setHtmlSnapShotPath(TS.browser().takeHtmlSnapshot());
            }
        }
        return step;
    }

    /**
     * Creates the info.
     *
     * @param message1 the message1
     * @param message2 the message2
     * @return the step action
     */
    public static StepAction createInfo(final String message1, final String message2) {
        return createInfo(message1, message2, "", true);
    }

    /**
     * Creates the browser action.
     *
     * @param message1 the message1
     * @param by       the by
     * @return the step action
     */
    public static StepAction createBrowserAction(final String message1, final Object by) {
        return createBrowserAction(message1, by.toString());
    }

    /**
     * Creates the browser action.
     *
     * @param message1 the message1
     * @param message2 the message2
     * @return the step action
     */
    public static StepAction createBrowserAction(final String message1, final String message2) {
        final StepAction step = new StepAction();
        step.setMessage1(message1);
        step.setMessage2(message2);
        step.setTestStepActionType(TestStepActionType.BROWSER_ACTION);
        TS.log().debug(TestStepActionType.BROWSER_ACTION + "[" + step.getMessage1() + "] - " + step.getMessage2());
        return step;
    }

    /**
     * Screenshot step action.
     *
     * @return the step action
     */
    public static StepAction screenshot() {
        return screenshot("");
    }

    /**
     * Screenshot step action.
     *
     * @param message the message
     * @return the step action
     */
    public static StepAction screenshot(final String message) {
        return createInfo(message, "", "", true, true);
    }

    /**
     * Add a screen snapshot to a step action.
     *
     * @param stepAction the StepAction to which to add a screen snapshot
     * @return the updated StepAction
     */
    public StepAction addSnapshot(final StepAction stepAction) {
        stepAction.setSnapShotPath(TS.browser().takeScreenShot());
        stepAction.setHtmlSnapShotPath(TS.browser().takeHtmlSnapshot());
        return stepAction;
    }
}
