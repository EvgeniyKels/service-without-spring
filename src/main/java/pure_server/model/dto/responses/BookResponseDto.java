package pure_server.model.dto.responses;

public class BookResponseDto {
    private String textResult;
    private String payload;
    private long timeStamp;

    private BookResponseDto() {
    }

    private BookResponseDto(BookResponseBuilder bookResponseBuilder) {
        this.textResult = bookResponseBuilder.textResult;
        this.payload = bookResponseBuilder.payload;
        this.timeStamp = bookResponseBuilder.timeStamp;
    }

    public String getTextResult() {
        return textResult;
    }

    public String getPayload() {
        return payload;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public static class BookResponseBuilder {
        private String textResult;
        private String payload;
        private long timeStamp;

        public BookResponseBuilder() {
        }

        public BookResponseBuilder addTextResult(String textResult) {
            this.textResult = textResult;
            return this;
        }

        public BookResponseBuilder addPayload(String payload) {
            this.payload = payload;
            return this;
        }

        public BookResponseBuilder addTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        public BookResponseDto build() {
            return new BookResponseDto(this);
        }
    }
}
