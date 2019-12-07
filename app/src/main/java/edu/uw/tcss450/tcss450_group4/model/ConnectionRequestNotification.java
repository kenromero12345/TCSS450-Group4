package edu.uw.tcss450.tcss450_group4.model;

import java.io.Serializable;

/**
 * Notification object that is used to notify fragments of new connection requests received
 * @author Abraham Lee abe2016@uw.edu
 */
public class ConnectionRequestNotification implements Serializable {

    private final String mMemberId;

    /**
     * Helper class for building ConnectionRequestNotification.
     */
    public static class Builder {

        private final String memberId;

        /**
         * Constructs a new Builder.
         *
         * No validation is performed.
         *
         * @param memberId the memmberId
         */
        public Builder(String memberId) {
            this.memberId = memberId;
        }

        /**
         * Builds notification object
         * @return ConnectionRequestNotification
         */
        public ConnectionRequestNotification build() {
            return new ConnectionRequestNotification(this);
        }
    }

    /**
     * Construct a ConnectionRequestNotification internally from a builder.
     *
     * @param builder the builder used to construct this object
     */
    private ConnectionRequestNotification(final Builder builder) {
        mMemberId = builder.memberId;
    }

    /**
     * Get message
     * @return message
     */
    public String getMessage() {
        return mMemberId;
    }

}
