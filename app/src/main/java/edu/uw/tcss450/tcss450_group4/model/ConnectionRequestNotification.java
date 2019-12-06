package edu.uw.tcss450.tcss450_group4.model;

import java.io.Serializable;

public class ConnectionRequestNotification implements Serializable {

    private final String mMemberId;


    public static class Builder {
        private final String memberId;

        public Builder(String memberId) {
            this.memberId = memberId;
        }

        public ConnectionRequestNotification build() {
            return new ConnectionRequestNotification(this);
        }
    }

    private ConnectionRequestNotification(final Builder builder) {
        mMemberId = builder.memberId;
    }

    public String getMessage() {
        return mMemberId;
    }

}
