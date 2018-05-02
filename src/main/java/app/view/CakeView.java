package app.view;

import app.dto.CakeDto;

import java.util.List;

/**
 * Cake view class
 */
public class CakeView {

    private List<CakeDto> items;
    private Long total;

    private CakeView(Builder builder) {
        this.items = builder.items;
        this.total = builder.total;
    }

    public List<CakeDto> getItems() {
        return items;
    }

    public Long getTotal() {
        return total;
    }

    public CakeView(){}

    public static class Builder {

        private List<CakeDto> items;
        private Long total;

        public Builder() {
        }

        public Builder setItems(List<CakeDto> items) {
            this.items = items;
            return this;
        }

        public Builder setTotal(Long total) {
            this.total = total;
            return this;
        }

        public CakeView build(){
            return new CakeView(this);
        }
    }
}
