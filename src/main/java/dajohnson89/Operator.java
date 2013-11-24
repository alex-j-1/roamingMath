package dajohnson89;

enum Operator {
    ADD("add") {
        public long apply(Long... args) {
            return args[0] + args[1];
        }
    },
    SUBTRACT("subtract") {
        public long apply(Long... args) {
            return args[0] - args[1];
        }
    },
    MULTIPLY("multiply") {
        public long apply(Long... args) {
            return args[0] * args[1];
        }
    },
    ABS("abs") {
        public long apply(Long... args) {
            return (Math.abs(args[0]));
        }
    };

    public abstract long apply(Long... args);

    private final String text;

    private Operator(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}