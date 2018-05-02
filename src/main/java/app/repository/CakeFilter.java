package app.repository;

import app.entity.Status;

/**
 * Need to sorting data from repository
 */
public class CakeFilter {

    /**
     * Fields
     */
    private int page ;
    private int limit ;
    private String text ;
    private Status[] statuses;

    private  CakeFilter(Builder builder) {
        this.page = builder.page;
        this.limit = builder.limit;
        this.text = builder.text;
        this.statuses = builder.statuses;

    }

    public static class Builder{
        private int page = 0;
        private int limit = 0;
        private String text = "";
        private Status[] statuses = new Status[]{};


        public  CakeFilter build(){
            return new CakeFilter(this);
        }

        public Builder setPage(int page) {
            this.page = page;
            return this;
        }

        public Builder setLimit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setStatuses(Status[] statuses) {
            this.statuses = statuses;
            return this;
        }
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public String getText() {
        return text;
    }

    public Status[] getStatuses() {
        return statuses;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setStatuses(Status[] statuses) {
        this.statuses = statuses;
    }
}
