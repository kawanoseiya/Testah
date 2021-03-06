package org.testah.client.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The Enum TestStepActionType.
 */
public enum TestStepActionType {

    /**
     * The assert.
     */
    ASSERT(),
    /**
     * The browser action.
     */
    BROWSER_ACTION(),
    /**
     * The http request.
     */
    HTTP_REQUEST(),
    /**
     * The info.
     */
    INFO(),
    /**
     * The verify.
     */
    VERIFY();

    /**
     * Instantiates a new test step action type.
     */
    TestStepActionType() {

    }

    @Override
    @JsonValue
    public String toString() {
        return this.name();
    }

}
